package web.admin.usergroup.service;


import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.dao.DataAccessException;

import web.admin.usergroup.model.*;
import web.admin.usergroup.dao.UserGroupDAO;


public class UserGroupServiceImpl implements UserGroupService {
	
	private UserGroupDAO userGroupDAO = null;
	private Logger logger = Logger.getLogger(this.getClass());

	
    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {    	
        this.userGroupDAO  = userGroupDAO;        
    }	

    
	/**
	 * <p>그룹과 사용자 트리의 json 데이타를 불러온다
	 * @return
	 */	
    @SuppressWarnings("unchecked")
	public String getJsonTreeGroupUser() {
		List<Group> groupList = null;
		List<User> userList = null;
		
		JSONArray json = new JSONArray();
		JSONObject root = new JSONObject();
		JSONObject property = new JSONObject();
		JSONObject nodeData = new JSONObject();
		
		property.put("name", "<b>그룹/계정</b>");
		root.put("property",property  );
		root.put("type", "root");
		
		JSONArray dataArr = new JSONArray();
		
		try{
			groupList =  userGroupDAO.treeGroup();
		
			for(Group group : groupList) { 
			
				JSONObject data = new JSONObject();
				property = new JSONObject();
				nodeData = new JSONObject();
				
				property.put("name", group.getGroupName());
				
				if(group.getIsAdmin().equals("Y")) // 어드민 그룹이면 체크박스 disable
					property.put("hasCheckbox", false);

				nodeData.put("id", group.getGroupID());
				nodeData.put("mode", "group");
				nodeData.put("isAdmin", group.getIsAdmin().equals("Y"));
				
				data.put("property", property);
				data.put("type", "folder");
				data.put("data", nodeData);
				
				userList =  userGroupDAO.treeUser(group.getGroupID());
				
				JSONArray userArr = new JSONArray();
				for(User user : userList) {
					JSONObject userData = new JSONObject();
	
					property = new JSONObject();
					nodeData = new JSONObject();
					
					property.put("name", user.getUserName());
					nodeData.put("id", user.getUserID());
					nodeData.put("mode", "user");
					nodeData.put("isAdmin", group.getIsAdmin().equals("Y"));
					
					userData.put("property", property);
					
					if(user.getIsAdmin().equals("Y")) { // 어드민
						property.put("hasCheckbox", false); // 어드민이 면 체크박스 disable
						userData.put("type", "super_user");
					} else if(user.getUserLevel().equals("1")) { // 시스템 관리자
						property.put("hasCheckbox", false); // 시스템 관리자이 면 체크박스 disable
						userData.put("type", "system_user");
					} else if(user.getUserLevel().equals("2")) // 그룹 관리자
						userData.put("type", "group_user");
					else
						userData.put("type", "user"); // 일반 유저
					
					userData.put("data", nodeData);
					
					userArr.add(userData);
					
				}
				
				data.put("children", userArr);
				dataArr.add(data);
			}
			
			root.put("children", dataArr);
			json.add(root);
		
		}catch(Exception e){
			logger.error(e);
		}
		
		
		return json.toJSONString();
	
	}

    
	/**
	 * <p>일부문자로 사용자를 검색한 json 데이타를 불러온다
	 * @return
	 */	
    @SuppressWarnings("unchecked")
	public String getJsonQuickSearchUser( String userName) {
		List<User> userList = null;
	
		
		JSONObject json = new JSONObject();
		JSONArray rslt = new JSONArray();
		
		json.put("results", rslt);
		
		try{
			userList =  userGroupDAO.quickSearchUser(userName);
			
		
				
			for(User user : userList) {
				JSONObject userJson = new JSONObject();
				JSONObject idJson = new JSONObject();
				
				idJson.put("groupid", user.getGroupID() );
				idJson.put("userid", user.getUserID());
				
				userJson.put("id", idJson);
				userJson.put("value", user.getUserName());
				userJson.put("info", user.getUserID());
				rslt.add(userJson);
			}
				
		}catch(Exception e){
			logger.error(e);
		}
		
		return json.toJSONString();
	
	}
    

	/**
	 * <p>사용자그룹(tm_usergroup) 모두를 출력한다.
	 * @return
	 */    
	public List<Group> listGroup(){
		List<Group>  resultList = null;
		try{
			resultList =  userGroupDAO.treeGroup();
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
    
	/**
	 * <p>사용자그룹(tm_usergroup)를 출력한다.
	 * @return
	 */    
	public List<Group> listGroup(int currentPage, int countPerPage, Map<String, String> searchMap){
		List<Group>  resultList = null;
		try{
			resultList =  userGroupDAO.listGroup(currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
    
	/**
	 * <p>사용자(tm_users)를 출력한다.
	 * @return
	 */    
	public List<User> listUser(int currentPage, int countPerPage, Map<String, String> searchMap, String groupID, String userLevel, String useYN){
		List<User> resultList = null;
		try{
			resultList =  userGroupDAO.listUser(currentPage, countPerPage, searchMap, groupID, userLevel, useYN);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
    
	/**
	 * <p>사용자(tm_users)를 출력한다.
	 * @return
	 */    
	public List<User> listSearchUser(String userLevel, String groupID){
		List<User> resultList = null;
		try{
			resultList =  userGroupDAO.listSearchUser(userLevel, groupID);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	/**
	 * 사용자그룹(tm_usergroup)에 추가한다.
	 * @param group
	 * @return
	 */
	public int insertGroup(Group group){
		int result = 0;
		try{
			result = userGroupDAO.insertGroup(group);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * 사용자를 (tm_users)에 추가한다.
	 * @param group
	 * @return
	 */
	public int insertUser(User user, UserAuth[] userAuth){
		int result = 0;
		try{
			if(user.getUserLevel().equals("1"))
			{
				result = userGroupDAO.insertgroupadmin(user);
				if(result > 0)
				{
					userGroupDAO.insertMenuGroupAdminAuth(user.getUserID());						
					userGroupDAO.insertDataGroupAdminAuth(user.getUserID());
				}
			}
			else
			{
				result = userGroupDAO.insertUser(user);
				if(result > 0)
				{
					userGroupDAO.insertUserMenuDefaultAuth(user.getUserID());				
					userGroupDAO.insertUserDataDefaultAuth(user.getUserID());
				}
			}
			
			
		}catch(Exception e){
			result = 0;
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * 사용자그룹(tm_usergroup)에 삭제한다.
	 * @param groupID
	 * @return
	 */	
	public boolean deleteGroup(JSONArray group_ids){
		boolean rslt = true;
		try{
			for(int i=0;i<group_ids.size();i++){	
				rslt = rslt && (userGroupDAO.deleteGroup((String)group_ids.get(i)) > 0);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return rslt;
	}
	
	
	/**
	 * 사용자그룹(tm_usergroup)에 삭제한다.
	 * @param groupID
	 * @return
	 */	
	public boolean deleteUser(JSONArray userIDS){
		boolean rslt = true;
		try{
			for(int i=0;i<userIDS.size();i++){	
				rslt = rslt && (userGroupDAO.deleteUser((String)userIDS.get(i)) > 0);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return rslt;
	}
	
	/**
	 * 사용자그룹(tm_usergroup)에서 수정한다.
	 * @param group
	 * @return
	 */
	public int updateGroup(Group group){
		
		int result = 0;
		try{
			result = userGroupDAO.updateGroup(group);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * 사용자그룹(tm_usergroup)에서 보여준다.
	 * @param groupID
	 * @return
	 */
	public Group viewGroup(String groupID){
		Group group = null;
		try{
			group = userGroupDAO.viewGroup(groupID);
		}catch(Exception e){
			logger.error(e);
		}
		return group;
	}
	
	
	/**
	 * 사용자를 (tm_users)에서 보여준다.
	 * @param groupID
	 * @return
	 */
	public User viewUser(String userID){
		User user = null;
		try{
			user = userGroupDAO.viewUser(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return user;
	}
	
	public User viewHelper(){
		User user = null;
		try{
			user = userGroupDAO.viewHelper();
		}catch(Exception e){
			logger.error(e);
		}
		return user;
	}
	
	/**
	 * <p>그룹의 총카운트를 구해온다.
	 * @return
	 */
	public int getGroupTotalCount(Map<String, String> searchMap){
		int result = 0;
		try{
			result = userGroupDAO.getGroupTotalCount(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>계정의 총카운트를 구해온다.
	 * @return
	 */	
	public int getUserTotalCount(Map<String, String> searchMap,String groupID, String userLevel, String useYN){
		int result = 0;
		try{
			result = userGroupDAO.getUserTotalCount(searchMap, groupID, userLevel, useYN);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 *<p>tm_usergroup의 groupID의 max값을 가져온다.
	 */
	public int getMaxdbID(){
		int result = 0;
		try{
			result = userGroupDAO.getMaxdbID();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>그룹코드/그룹명을 보여준다.
	 * @return
	 */	
	public List<Group> listUserGroupInfo(){
		List<Group> resultList = null;
		try{
			resultList =  userGroupDAO.listUserGroupInfo();
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}

	
	/**
	 * <p>사용자ID의 메뉴별권한 
	 * @param userID
	 * @param mainMenuID
	 * @param subMenuID
	 * @return	 
	 */
	public String[] getUserAuth(String userID, String mainMenuID, String subMenuID){
		Map<String,Object> resultMap = null;
		String result[]= new String[2];
		try{
			resultMap = userGroupDAO.getUserAuth(userID, mainMenuID, subMenuID);
			
			if(!resultMap.get("subMenuAuth").equals("")){
				result[0]=(String)resultMap.get("authType");
				result[1]=(String)resultMap.get("subMenuAuth");
			}
			
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>db 정보 출력 
	 * @return
	 */	
	public List<DbSet>  selectSetDBList(){
		List<DbSet>  result = null;
		try{
			result = userGroupDAO.selectSetDBList();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>권한정보출력 
	 * @param userLevel
	 * @return
	 */
	public List<UserAuth> selectUserLevelAuth(String userLevel){
		
		List<UserAuth> result = null;
		try{
			result = userGroupDAO.selectUserLevelAuth(userLevel);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>해당사용자 권한 정보 json 출력 
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public String getJsonMenuAuth( String userID ) {
		
		JSONObject json = new JSONObject();

		// 메뉴 권한
		JSONArray arr = new JSONArray();
		List<UserAuth> result = null;
		try{
			result = userGroupDAO.viewUserMenuAuth(userID);
			
			for(UserAuth auth: result) {
				arr.add( auth.getSubMenuID());
			}
			
			json.put("menu", arr);
			
		}catch(Exception e){
			logger.error(e);
		}
		
		// 대상자 등록 권한
		arr = new JSONArray();
		try{
			User user = null;
			
			// 그룹의 계정이면
			if(userID.length() > 7 && userID.substring(0,7).equals("_group_"))
				user = userGroupDAO.viewGroupAuth(userID.substring(7));
			else
			//사용자 계정이면
				user = userGroupDAO.viewUser(userID);
			
			if(user.getAuthCSV().equals("Y")) // csv 파일 업로드 권한 
				arr.add( "auth_csv" );
			
			if(user.getAuthDirect().equals("Y")) // 직접입력권한 
				
				arr.add( "auth_direct" );
			/*
			if(user.getAuthRelated().equals("Y")) // 기존연관 권한 
				arr.add( "auth_related" );
			*/
		    // 쿼리 권한은 하부 데이타 권한에서 판단 된다
			/*
			if(user.getAuthQuery().equals("Y")) // 쿼리 권한 
				arr.add( "auth_query" );
			*/
			
			if(user.getAuthSendMail().equals("Y")) // 메일 전송 권한 
				arr.add( "auth_send_mail" );

			if(user.getAuthWriteMail().equals("Y")) // 메일 전송 권한 
				arr.add( "auth_write_mail" );			
			
			if(user.getAuthSendSMS().equals("Y")) // SMS 전송 권한 
				arr.add( "auth_send_sms" );

			if(user.getAuthWriteSMS().equals("Y")) // SMS 전송 권한 
				arr.add( "auth_write_sms" );

			
			json.put("auth", arr);
			
		}catch(Exception e){
			logger.error(e);
		}
		
		// 데이타 권한
		arr = new JSONArray();
		try{
			List<DbSet> dbSetList = userGroupDAO.viewUserDbSet(userID);
			
			for(DbSet dbSet: dbSetList) {
				arr.add(dbSet.getDbID());
			}
			
			json.put("dbset", arr);
			
		}catch(Exception e){
			logger.error(e);
		}
		
		
		
		return json.toJSONString();
		
	}
    
	
	/**
	 * <p>해당사용자 권한정보출력 
	 * @param userLevel
	 * @return
	 */
	public List<UserAuth> viewUserIDLevelAuth(String selUserID){
		
		List<UserAuth> result = null;
		try{
			result = userGroupDAO.viewUserIDLevelAuth(selUserID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>사용자 정보수정(권한)
	 * @param user
	 * @param userAuth
	 * @return
	 */
	
	public int updateUsersAuth(User user, UserAuth[] userAuth){
		
		int result = 0;
		try{
			if(user.getIsHelper().equals("Y")){
				userGroupDAO.setIsHelperN();
			}
			result =userGroupDAO.updateUser(user);
			/*
			result = userGroupDAO.deleteAuth(user.getUserID());   //기존권한은 삭제하고 
			
			for(int i=0;i<userAuth.length;i++){									//새로 넣어준다. 
				if(userAuth[i]!=null)
					result = userGroupDAO.insertUserAuth(userAuth[i]);
				
			}
			*/
			
			
			
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}

	/**
	 * <p>사용자 의 그룹 변경 
	 * @param user
	 * @param userAuth
	 * @return
	 */
	public int updatechangeUserGroupID(String userID, String groupID) {
		int result = 0;
		try{
			result = userGroupDAO.updatechangeUserGroupID(userID, groupID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	
	}
	
	
	/**
	 * <p>그룹의 권한 저장
	 * @param JSONStr
	 * @return
	 */
	
	public boolean updateGroupAuth( String GroupID, String JSONStr ) {
		
		boolean retv = true;
		int i = 0;
		String auth = "";
		
		Object obj = JSONValue.parse(JSONStr);

		JSONArray array = (JSONArray)obj;
		
		JSONObject json = (JSONObject)array.get(0);
		
		
		 

		try{
			// 기존 메뉴 권한을 전부 삭제
			userGroupDAO.deleteGroupAllMenuAuth(GroupID);

			// 메뉴 권한을 추가
			JSONArray subMenuArr = (JSONArray)json.get("submenu");
			for(i = 0; i < subMenuArr.size(); i++ ) {
				retv  = retv && (userGroupDAO.insertGroupMenuAuth(GroupID, (String)subMenuArr.get(i)) > 0);
			}
			
			// 사용권한 설정
			User user = new User();
			user.initUserAuth();
			
			JSONArray authArr = (JSONArray)json.get("auth");
			for(i = 0; i < authArr.size(); i++ ) {
				auth = (String)authArr.get(i);
				if(auth.equals("auth_csv")) 
					user.setAuthCSV("Y");
				if(auth.equals("auth_query")) 
					user.setAuthQuery("Y");
				if(auth.equals("auth_direct")) 
					user.setAuthDirect("Y");
				if(auth.equals("auth_related")) 
					user.setAuthRelated("Y");
				if(auth.equals("auth_send_mail")) 
					user.setAuthSendMail("Y");
				if(auth.equals("auth_write_mail")) 
					user.setAuthWriteMail("Y");
				if(auth.equals("auth_send_sms")) 
					user.setAuthSendSMS("Y");
				if(auth.equals("auth_write_sms")) 
					user.setAuthWriteSMS("Y");				
			}
			retv  = retv && (userGroupDAO.updateGroupAuth(GroupID, user ) > 0);
			

			
			// 기존 데이터 권한을 전부 삭제
			userGroupDAO.deleteGroupAllDataAuth(GroupID);

			// 메뉴 권한을 추가
			JSONArray dbSetArr = (JSONArray)json.get("dbset");
			for(i = 0; i < dbSetArr.size(); i++ ) {
				retv  = retv && (userGroupDAO.insertGroupDataAuth(GroupID, (String)dbSetArr.get(i)) > 0);
			}
			
			
		}catch(Exception e){
			logger.error(e);
		}
		
		
		return retv;
		
	}
	
	/**
	 * <p>사용자의 권한 저장
	 * @param JSONStr
	 * @return
	 */
	
	public boolean updateUserAuth( String userID, String JSONStr ) {
		
		boolean retv = true;
		int i = 0;
		String auth = "";
		
		Object obj = JSONValue.parse(JSONStr);

		JSONArray array = (JSONArray)obj;
		
		JSONObject json = (JSONObject)array.get(0);
		

		try{
			// 기존 메뉴 권한을 전부 삭제
			userGroupDAO.deleteAllUserMenuAuth(userID);

			// 메뉴 권한을 추가
			JSONArray subMenuArr = (JSONArray)json.get("submenu");
			for(i = 0; i < subMenuArr.size(); i++ ) {
				retv  = retv && (userGroupDAO.insertUserMenuAuth(userID, (String)subMenuArr.get(i)) > 0);
			}
			
			// 사용권한 설정
			User user = new User();
			user.initUserAuth();
			
			JSONArray authArr = (JSONArray)json.get("auth");
			for(i = 0; i < authArr.size(); i++ ) {
				auth = (String)authArr.get(i);
				if(auth.equals("auth_csv")) 
					user.setAuthCSV("Y");
				if(auth.equals("auth_query")) 
					user.setAuthQuery("Y");
				if(auth.equals("auth_direct")) 
					user.setAuthDirect("Y");
				if(auth.equals("auth_related")) 
					user.setAuthRelated("Y");
				if(auth.equals("auth_send_mail")) 
					user.setAuthSendMail("Y");
				if(auth.equals("auth_write_mail")) 
					user.setAuthWriteMail("Y");
				if(auth.equals("auth_send_sms")) 
					user.setAuthSendSMS("Y");
				if(auth.equals("auth_write_sms")) 
					user.setAuthWriteSMS("Y");				
			}
			retv  = retv && (userGroupDAO.updateUserAuth(userID, user ) > 0);
			

			
			// 기존 데이터 권한을 전부 삭제
			userGroupDAO.deleteAllUserDataAuth(userID);

			// 메뉴 권한을 추가
			JSONArray dbSetArr = (JSONArray)json.get("dbset");
			for(i = 0; i < dbSetArr.size(); i++ ) {
				retv  = retv && (userGroupDAO.insertUserDataAuth(userID, (String)dbSetArr.get(i)) > 0);
			}
			
			
		}catch(Exception e){
			logger.error(e);
		}
		
		
		return retv;
		
	}

	/**
	 * <p>선택된사용자의 권한 저장
	 * @return
	 */
	
	public boolean updateSelectedUserAuth( String userIDJson, String authJson ) {
		boolean retv = true;
		int i = 0;
		
		JSONArray userArray = (JSONArray)JSONValue.parse(userIDJson);

		String auth = "";
		
		Object obj = JSONValue.parse(authJson);
		JSONArray array = (JSONArray)obj;
		JSONObject json = (JSONObject)array.get(0);

		User user = new User();
		
		try{
			
			for(int j = 0; j < userArray.size(); j ++ ) {

				// 메뉴 권한을 삭제
				JSONArray deleteSubMenuArr = (JSONArray)json.get("delete_submenu");
				for(i = 0; i < deleteSubMenuArr.size(); i++ ) {
					userGroupDAO.deleteUserMenuAuth((String)userArray.get(j), (String)deleteSubMenuArr.get(i) );
				}
	
				// 메뉴 권한을 추가
				JSONArray insertSubMenuArr = (JSONArray)json.get("insert_submenu");
				for(i = 0; i < insertSubMenuArr.size(); i++ ) {
					userGroupDAO.insertUserMenuAuth((String)userArray.get(j), (String)insertSubMenuArr.get(i));
				}
				
				// 사용권한 설정
				user.initUserAuth();
				JSONArray deleteAuthArr = (JSONArray)json.get("delete_auth");
				for(i = 0; i < deleteAuthArr.size(); i++ ) {
					auth = (String)deleteAuthArr.get(i);
					if(auth.equals("auth_csv")) 
						user.setAuthCSV("N");
					if(auth.equals("auth_query")) 
						user.setAuthQuery("N");
					if(auth.equals("auth_direct")) 
						user.setAuthDirect("N");
					if(auth.equals("auth_send_mail")) 
						user.setAuthSendMail("N");
					if(auth.equals("auth_write_mail")) 
						user.setAuthWriteMail("N");
					if(auth.equals("auth_send_sms")) 
						user.setAuthSendSMS("N");
					if(auth.equals("auth_write_sms")) 
						user.setAuthWriteSMS("N");					
				}
				JSONArray insertAuthArr = (JSONArray)json.get("insert_auth");
				for(i = 0; i < insertAuthArr.size(); i++ ) {
					auth = (String)insertAuthArr.get(i);
					if(auth.equals("auth_csv")) 
						user.setAuthCSV("Y");
					if(auth.equals("auth_query")) 
						user.setAuthQuery("Y");
					if(auth.equals("auth_direct")) 
						user.setAuthDirect("Y");
					if(auth.equals("auth_related")) 
						user.setAuthRelated("Y");
					if(auth.equals("auth_send_mail")) 
						user.setAuthSendMail("Y");
					if(auth.equals("auth_write_mail")) 
						user.setAuthWriteMail("Y");
					if(auth.equals("auth_send_sms")) 
						user.setAuthSendSMS("Y");
					if(auth.equals("auth_write_sms")) 
						user.setAuthWriteSMS("Y");		
				}
				userGroupDAO.updateUserAuth((String)userArray.get(j), user );
				
	
				JSONArray deleteDbSetArr = (JSONArray)json.get("delete_dbset");
				// db 권한을 삭제
				for(i = 0; i < deleteDbSetArr.size(); i++ ) {
					userGroupDAO.deleteUserDataAuth((String)userArray.get(j), (String)deleteDbSetArr.get(i));
				}
				// db 권한을 추가
				JSONArray insertDbSetArr = (JSONArray)json.get("insert_dbset");
				for(i = 0; i < insertDbSetArr.size(); i++ ) {
					userGroupDAO.insertUserDataAuth((String)userArray.get(j), (String)insertDbSetArr.get(i));
				}
			}
			
			
			
			
		}catch(Exception e){
			retv = false;
		    e.printStackTrace( System.out ); 
			logger.error(e);
		}
		
		
		return retv;
				
		
	}
	
	/**
	 * <p>그릅관리자 및 전체관리자 
	 * @param groupID
	 * @return
	 */
	public List<User> selectUserMaster(String groupID){
		List<User> result = null;
		try{
			result = userGroupDAO.selectUserMaster(groupID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	/**
	 * <p>사용자를 (ez_users)에서 보여준다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public User getHelperUser(){
		User result = null;
		try{
			result = userGroupDAO.getHelperUser();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	public List<Group> listGroup_inc_admin(String userID){
		List<Group>  resultList = null;
		try{
			resultList =  userGroupDAO.listGroup_inc_admin(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	public List<Group> listGroup_inc(String userID){
		List<Group>  resultList = null;
		try{
			resultList =  userGroupDAO.listGroup_inc(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
		
	/**
	 * <p> 사용자 ID 사용 제한 해제
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDerestrict(String userID){
		int result = 0;
		try{
			result = userGroupDAO.updateDerestrict(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
