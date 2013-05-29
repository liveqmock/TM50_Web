package web.admin.systemset.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.admin.systemset.model.SystemSet;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;
import org.springframework.jdbc.core.namedparam.*;



public class SystemSetDAOImpl extends DBJdbcDaoSupport  implements SystemSetDAO {
	

	
	/**
	 * <p> 환경설정 리스트 
	 */
	@SuppressWarnings("unchecked")
	public List<SystemSet> listSystemSet(String configFlag) throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.systemset.select");	
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				SystemSet systemSet = new SystemSet(); 
				systemSet.setConfigID(rs.getInt("configID"));
				systemSet.setConfigFlag(rs.getString("configFlag"));
				systemSet.setName(rs.getString("name")); 
				systemSet.setConfigName(rs.getString("configName")); 
				systemSet.setConfigValue(rs.getString("configValue")); 
				systemSet.setConfigDesc(rs.getString("configDesc"));
				return systemSet; 
			}			
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("configFlag", configFlag);

		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}


	/**
	 * <p>환경설정 업데이트 
	 * @param arrSystemSet
	 * @return
	 * @throws Exception
	 */
	public int[] updateSystemSet(List<SystemSet> arrSystemSet) throws Exception {		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.systemset.update");
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(arrSystemSet.toArray());
		
		return getSimpleJdbcTemplate().batchUpdate(sql,params);
	}
	
	/**
	 * <p>환경 설정에서 특정 configName의 value 값을 받아온다
	 * @param configFlag,configName
	 * @return configValue
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public String getSystemSetInfo(String configFlag, String configName) throws DataAccessException{
		String configValue ="configValue";
		Map resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.systemset.valuse"); 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("configFlag",configFlag);
		param.put("configName",configName);
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			configValue = (String)resultMap.get("configValue");
		}else{
			return configValue;
		}
		
		
		return configValue;
	}
	
	
	/**
	 * <p>접근 IP 관리 (접근 가능 IP인지 확인)
	 * @param userID
	 * @return
	 */
	public int checkAccessIP(String remoteIP) throws DataAccessException{	
		String[] splitIP =  web.common.util.StringUtil.stringToStringArray(remoteIP,".");
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.AccessIP.checkAccessIP");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("remoteIP", remoteIP);
		param.put("octetA", splitIP[0]);
		param.put("octetB", splitIP[1]);
		param.put("octetC", splitIP[2]);
				
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>환경설정 업데이트 - configFlag, configName 사용
	 * @param configFlag
	 * * @param configName
	 * @return
	 * @throws Exception
	 */
	public int updateConfigValue(String configFlag, String configName, String configValue) throws DataAccessException {		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.systemset.updateConfigValue");
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("configValue", configValue);
		param.put("configFlag", configFlag);
		param.put("configName", configName);
		return getSimpleJdbcTemplate().update(sql,param);
	}
}
