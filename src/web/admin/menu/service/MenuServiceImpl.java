package web.admin.menu.service;

import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.dao.DataAccessException;
import web.admin.menu.dao.MenuDAO;
import web.admin.menu.model.*;
import web.admin.usergroup.model.AuthMaster;
import web.admin.usergroup.model.DbSet;


public class MenuServiceImpl implements MenuService{
	

	private MenuDAO menuDAO = null;
	private Logger logger = Logger.getLogger(this.getClass());

	
    public void setMenuDAO(MenuDAO menuDAO) {    	
        this.menuDAO = menuDAO;        
    }	
    
   
	/**
	 * <p>메인메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 */
    
	public List<MenuMain>  listMenuMain(){
		List<MenuMain> resultList = null;
		try{
			resultList =  menuDAO.listMenuMain();
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
    	
    }
	
	/**
	 * <p>서브메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<MenuSub> listMenuSub(String mainMenuID){
		List<MenuSub> resultList = null;
		try{
			resultList =  menuDAO.listMenuSub(mainMenuID);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
		
	}
	
	/**
	 * <p>서브메뉴리스트 전체를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 *
	 */	
	public List<MenuSub>  listMenuSub() {
		return listMenuSub("");
	}
	
	
	/**
	 * <p>사용자 권한에 해당하는 서브메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 *
	 */	
	public List<MenuSub>  listMenuSubAuth( String userID ){
		
		List<MenuSub> resultList = null;
		try{
			resultList =  menuDAO.listMenuSubAuth(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
		
	}
	
	/**
	 * <p>메뉴 트리의 json 데이타를 불러온다
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public String getJsonTreeMenu() {
		
		List<MenuMain> menuMainList = null;
		List<MenuSub> menuSubList = null;
		List<AuthMaster> authMasterList = null;
		List<DbSet> setDBList = null;
		
		JSONArray json = new JSONArray();
		JSONObject root = new JSONObject();
		JSONObject property = new JSONObject();
		JSONObject nodeData = new JSONObject();
		
		property.put("name", "<b>메뉴권한</b>");
		root.put("property",property  );
		root.put("type", "root");
		
		JSONArray dataArr = new JSONArray();
		
		try{
			menuMainList =  menuDAO.listMenuMain();
		}catch(Exception e){
			logger.error(e);
		}
		
		for(MenuMain menuMain : menuMainList) { 
			
			if(menuMain.getIsAdmin().equals("Y")) continue;
		
			JSONObject data = new JSONObject();
			property = new JSONObject();
			nodeData = new JSONObject();
			
			property.put("name", menuMain.getMainMenuName());
			nodeData.put("id", menuMain.getMainMenuID());
			nodeData.put("mode", "main_menu");
			
			data.put("property", property);
			data.put("type", "folder");
			data.put("data", nodeData);
			
			try{
				menuSubList =  menuDAO.listMenuSub(menuMain.getMainMenuID());
			}catch(Exception e){
				logger.error(e);
			}
			
			JSONArray userArr = new JSONArray();
			for(MenuSub menuSub : menuSubList) {
				JSONObject userData = new JSONObject();

				property = new JSONObject();
				nodeData = new JSONObject();
				
				property.put("name", menuSub.getSubMenuName());
				nodeData.put("id", menuSub.getSubMenuID());
				nodeData.put("mode", "sub_menu");
				nodeData.put("hasAuth", menuSub.getHasAuth().equals("Y"));
				
				userData.put("property", property);
				userData.put("type", "menu");
				userData.put("data", nodeData);
				
				// 메뉴별 세부 옵션권한 체크
				if(menuSub.getHasAuth().equals("Y")) {
					
					JSONArray authArr = new JSONArray();
					JSONObject authData = null;
					
					try{
						authMasterList =  menuDAO.listAuthMaster(menuSub.getSubMenuID());
					}catch(Exception e){
						logger.error(e);
					}
					
					for(AuthMaster authMaster : authMasterList) {
						if(!(authMaster.getFieldName().equals("auth_related")))
						{
							authData = new JSONObject();
							property = new JSONObject();
							nodeData = new JSONObject();
						
							property.put("name", authMaster.getDescript());
							nodeData.put("id", authMaster.getFieldName());
							nodeData.put("mode", "auth");
							nodeData.put("hasAuth", authMaster.getFieldName().equals("auth_query"));

							authData.put("property", property);
							authData.put("type", authMaster.getFieldName());
							authData.put("data", nodeData);
						
					
						
						if(authMaster.getFieldName().equals("auth_query")) {
							JSONArray authDBArr = new JSONArray();
							JSONObject authDBData = null;
							
							try{
								setDBList = menuDAO.selectSetDBList();
							}catch(Exception e){
								logger.error(e);
							}
							
							for(DbSet dbSet : setDBList) {
								authDBData = new JSONObject();
								property = new JSONObject();
								nodeData = new JSONObject();
								
								property.put("name", dbSet.getDbName());
								nodeData.put("id", dbSet.getDbID());
								nodeData.put("mode", "dbset");

								authDBData.put("property", property);
								authDBData.put("type", "dbset");
								authDBData.put("data", nodeData);
								
								authDBArr.add(authDBData);
							}
							
							if(authDBArr.size() > 0) {
								authData.put("children", authDBArr );
							}
							
						}
						
						//*********** 데이타 억세스 권한 끝 ******************//
						authArr.add(authData);
						
						}	
					}
					
					if(authArr.size() > 0) {
						userData.put("children", authArr);
					}
				}
				// 메뉴별 세부 옵션권한 체크 끝
				
				userArr.add(userData);
				
			}
			
			data.put("children", userArr);
			dataArr.add(data);
		}
		
		root.put("children", dataArr);
		json.add(root);
		
		return json.toJSONString();		
	}

	
}
