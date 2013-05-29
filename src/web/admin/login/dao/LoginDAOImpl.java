package web.admin.login.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.admin.login.model.LoginHistory;
import web.admin.login.model.Users;
import web.admin.menu.model.MenuSub;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.DateUtils;
import web.common.util.QueryUtil;

public class LoginDAOImpl extends DBJdbcDaoSupport  implements LoginDAO{
	
	/**
	 * <p>아이디와 패스워드에 해당하는 유저를 가져온다. 
	 * @param userID
	 * @param userPWD
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public Users getUsersInfo(String userID, String userPWD) throws DataAccessException{
		
		Users users = new Users();
		Map resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.login.finduser"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		param.put("userPWD", userPWD);
		
		resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);		
		
		if(resultMap!=null){
			users.setUserID((String)resultMap.get("userID"));
			users.setUserName((String)resultMap.get("userName"));		
			users.setGroupID((String)resultMap.get("groupID"));
			users.setGroupName((String)resultMap.get("groupName"));
			users.setUserLevel((String)resultMap.get("userLevel"));
			users.setIsAdmin((String)resultMap.get("isAdmin"));
			users.setAuth_csv((String)resultMap.get("auth_csv"));
			users.setAuth_direct((String)resultMap.get("auth_direct"));
			users.setAuth_related((String)resultMap.get("auth_related"));
			users.setAuth_query((String)resultMap.get("auth_query"));
			users.setAuth_write_mail((String)resultMap.get("auth_write_mail"));
			users.setAuth_send_mail((String)resultMap.get("auth_send_mail"));
			users.setAuth_write_sms((String)resultMap.get("auth_write_sms"));
			users.setAuth_send_sms((String)resultMap.get("auth_send_sms"));			
			users.setSenderName((String)resultMap.get("senderName"));
			users.setSenderEmail((String)resultMap.get("senderEmail"));
			users.setSenderCellPhone((String)resultMap.get("senderCellPhone"));
			users.setFailCount(Integer.parseInt(String.valueOf(resultMap.get("failCount"))));
			users.setModifyDate(DateUtils.getStringShortDate(String.valueOf(resultMap.get("modifyDate"))));
		}else{
			return null;
		}
		
		return users;
		
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
	 * <p>로그인 실패시 tm_users 테이블의 updateLoginFailCount를 1증가 시킨다.
	 * @param userID
	 * @return
	 */
	public int updateLoginFailCount(String userID) throws DataAccessException{		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.login.updateLoginFailCount");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
				
		return getSimpleJdbcTemplate().update(sql,param);
	}

	/**
	 * <p>로그인 실패 횟수 확인
	 * @param userID
	 * @return
	 */
	public int checkLoginFailCount(String userID) throws DataAccessException{		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.login.checkLoginFailCount");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
				
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>로그인 설공시 tm_users 테이블의 updateLoginFailCount를 0으로 변경한다.
	 * @param userID
	 * @return
	 */
	public int updateLoginFailCountZero(String userID) throws DataAccessException{		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.login.updateLoginFailCountZero");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
				
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	/**
	 * <p>로그인 시도 기록 저장
	 * @param loginHistory
	 * @return
	 */
	public int insertLoginHistory(LoginHistory loginHistory) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.loginhistory.insert");		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", loginHistory.getUserID());
		param.put("loginYN", loginHistory.getLoginYN());
		param.put("description", loginHistory.getDescription());
		param.put("accessIP", loginHistory.getAccessIP());	
	
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	

}
