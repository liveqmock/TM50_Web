package web.admin.menu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import web.admin.menu.model.*;
import web.admin.usergroup.model.AuthMaster;
import web.admin.usergroup.model.DbSet;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class MenuDAOImpl extends DBJdbcDaoSupport   implements MenuDAO{
	
	/**
	 * <p>메인메뉴리스트를 가져온다.
	 */
	@SuppressWarnings("unchecked")
	public List<MenuMain> listMenuMain() throws DataAccessException{
	
		String sql = QueryUtil.getStringQuery("admin_sql","admin.menu.selectmain");					
			
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MenuMain  menuMain = new MenuMain();				
				menuMain.setMainMenuID(rs.getString("mainMenuID"));
				menuMain.setMainMenuName(rs.getString("mainMenuName"));
				menuMain.setPriorNum(rs.getInt("priorNum"));
				menuMain.setUseYN(rs.getString("useYN"));
				menuMain.setIsAdmin(rs.getString("isAdmin"));
				return menuMain;
			}			
		};
			
		
		return  getJdbcTemplate().query(sql, rowMapper);
		
	}
	
	/**
	 * <p>권한에 해당하는 서브메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 */	
	@SuppressWarnings("unchecked")
	public List<MenuSub> listMenuSubAuth(String userID) throws DataAccessException {
		
		String sql = "";
		
		sql = QueryUtil.getStringQuery("admin_sql","admin.menu.selectSubAuth");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				MenuSub  menuSub = new MenuSub();		
				
				menuSub.setWindowId(rs.getString("windowId"));
				menuSub.setSubMenuID(rs.getString("subMenuID"));
				menuSub.setMainMenuID(rs.getString("mainMenuID"));
				return menuSub;
			}			
		};
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper,param);
	}

	
	/**
	 * <p>submenu에서 주어진menuMainID에 해당되는 리스트를 가져온다. 
	 * <p>menuMainID가 "" 면 전체 리스트를 가져온다. 
	 */
	@SuppressWarnings("unchecked")
	public List<MenuSub> listMenuSub(String meunMainID) throws DataAccessException{

		String sql = "";
		Vector v = new Vector();
		
		// 전체 검색
		if(meunMainID.equals("")) {
			sql = QueryUtil.getStringQuery("admin_sql","admin.menu.selectsub");
		} else {
		// 메인메뉴별 검색
			sql = QueryUtil.getStringQuery("admin_sql","admin.menu.selectsub");
			sql += " AND mainMenuId = ? ";
			sql += QueryUtil.getStringQuery("admin_sql","admin.menu.selectsubtail");
			v.add(meunMainID);
		}
			
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				MenuSub  menuSub = new MenuSub();		
				
				menuSub.setSubMenuID(rs.getString("subMenuID"));
				menuSub.setSubMenuName(rs.getString("subMenuName"));
				menuSub.setMainMenuID(rs.getString("mainMenuID"));
				menuSub.setPath(rs.getString("path"));
				menuSub.setPriorNum(rs.getInt("priorNum"));
				menuSub.setUseYN(rs.getString("useYN"));	
				menuSub.setWidth(rs.getInt("width"));
				menuSub.setHeight(rs.getInt("height"));
				menuSub.setX(rs.getInt("x"));
				menuSub.setY(rs.getInt("y"));
				menuSub.setWindowId(rs.getString("windowId"));
				menuSub.setTabPath(rs.getString("tabPath").trim());
				menuSub.setDivider(rs.getString("divider"));
				menuSub.setPadding(rs.getString("padding"));
				menuSub.setAccordian(rs.getString("accordian"));
				menuSub.setHasAuth(rs.getString("hasAuth"));
				menuSub.setFullSize(rs.getString("fullSize"));
				menuSub.setPopupYN(rs.getString("popupYN"));
				
				return menuSub;
			}			
		};
	
		return  getSimpleJdbcTemplate().query(sql, rowMapper,v.toArray());

	}
	
	/**
	 * <p>권한 마스터 불러오기
	 */
	@SuppressWarnings("unchecked")
	public List<AuthMaster> listAuthMaster(String menuSubID) throws DataAccessException{
	
		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.authMaster");					
			
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				AuthMaster  authMaster = new AuthMaster();
				authMaster.setFieldName(rs.getString("fieldName"));
				authMaster.setSubMenuID(rs.getString("submenuid"));
				authMaster.setDescript(rs.getString("descript"));
				return authMaster;
			}			
		};

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("submenuID", menuSubID);
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
	}
	
	/**
	 * <p>db 정보 출력 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbSet> selectSetDBList() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectdb");	
		//System.out.println("query:"+sql);
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbSet dbSet = new DbSet();
				dbSet.setDbID(rs.getString("dbID"));
				dbSet.setDbName(rs.getString("dbName"));
				dbSet.setDescription(rs.getString("description"));
				
				return dbSet;
			}			
		};
			
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	

}
