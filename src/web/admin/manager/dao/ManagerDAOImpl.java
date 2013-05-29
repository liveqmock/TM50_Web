package web.admin.manager.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import web.admin.manager.model.*;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;



public class ManagerDAOImpl extends DBJdbcDaoSupport   implements ManagerDAO{

	/**
	 * <p>메인메뉴리스트를 가져온다.
	 */
	@SuppressWarnings("unchecked")
	public List<Manager> listEngine() throws DataAccessException{
	
		String sql = QueryUtil.getStringQuery("admin_sql","admin.manager.select");					
			
		RowMapper rowMapper = new RowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Manager  manager = new Manager();				
					manager.setEngineID(rs.getString("engineID"));
					manager.setEngineName(rs.getString("engineName"));
					manager.setEngineDesc(rs.getString("engineDesc"));
					manager.setEngineStatus(rs.getString("engineStatus"));
					manager.setErrorCount(rs.getInt("errorCount"));
					manager.setServerIP(rs.getString("serverIP"));
					manager.setUpdateDate(rs.getString("updateDate"));
					manager.setLogPath(rs.getString("logPath"));
					manager.setLogName(rs.getString("logName"));
				return manager;
			}			
		};
			
		
		return  getJdbcTemplate().query(sql, rowMapper);
		
	}
	
	/**
	 * <p>EZMAIL 발송엔진상태 업데이트
	 * @param engineID, engineStatus
	 * @throws DataAccessException
	 */
	public int enginStatusUpdate(String engineID, String engineStatus) throws DataAccessException{
		/*String sql = QueryUtil.getStringQuery("admin_sql","admin.manager.update");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("engineStatus", engineStatus);
		param.put("engineID", engineID);
		
		return getSimpleJdbcTemplate().update(sql,param);*/
		String sql  = QueryUtil.getStringQuery("admin_sql","admin.manager.update");
		
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("engineStatus", engineStatus);
		param.put("engineID", engineID);
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	public Manager getEngineState(String engine_id, String server_ip)  throws DataAccessException{
		
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.manager.selectenginestate");		
		Map<String, Object> resultMap = null;
		Manager manager = new Manager();
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("serverIP", server_ip);
		param.put("engineID", new Integer(engine_id));
		
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			manager.setEngineStatus(String.valueOf(resultMap.get("engineStatus")));
			manager.setErrorCount(Integer.parseInt(String.valueOf(resultMap.get("errorCount"))));
			manager.setUpdateDate(String.valueOf(resultMap.get("updateDate")));
		}
		return manager;
	}
	
	/**
	 * <p>대량메일 발송 확인
	 * @throws DataAccessException
	 */
	public int isSendMassMail() throws DataAccessException
	{
		String query = QueryUtil.getStringQuery("common_sql","common.util.selectsendmailstate");		
	
		Map<String,Object> param = new HashMap<String, Object>();		
	
		
		return  getSimpleJdbcTemplate().queryForInt(query, param);
	}
}
