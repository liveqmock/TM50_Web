package web.admin.domainset.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.admin.domainset.model.DomainSet;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;


public class DomainSetDAOImpl extends DBJdbcDaoSupport  implements DomainSetDAO {
	
	

	/**
	 * <p>도메인설정표시 
	 * @param domainFlag
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DomainSet> listDomainSet(String domainFlag)	throws DataAccessException {
	
		String sql = QueryUtil.getStringQuery("admin_sql","admin.domainset.select");		
			
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DomainSet domainSet = new DomainSet(); 				
				domainSet.setDomainID(rs.getInt("domainID")); 
				domainSet.setDomainType(rs.getString("domainType")); 
				domainSet.setDomainName(rs.getString("domainName")); 
				domainSet.setSocketPerSendCount(rs.getInt("socketPerSendCount")); 
				domainSet.setThreadCount(rs.getInt("threadCount")); 
				domainSet.setSocketTimeOut(rs.getInt("socketTimeOut")); 
				if(rs.getString("domainType").equals("1")){ //기타도메인이라면 
					domainSet.setDomainTypeDesc("기타도메인");
				}else if(rs.getString("domainType").equals("3")){
					domainSet.setDomainTypeDesc("재발송스레드");
				}else{
					domainSet.setDomainTypeDesc("포털도메인");
				}
				return domainSet; 
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("domainFlag", domainFlag);
			
		return  getSimpleJdbcTemplate().query(sql,rowMapper,param);
	}

	/**
	 * <p>도메인설정 삭제 
	 */
	public int deleteDomainSet(int domainID) throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.domainset.delete");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("domainID", domainID);
		return getSimpleJdbcTemplate().update(sql,param);
	}
	

	/**
	 * <p>도메인입력 
	 */
	public int insertDomainSet(DomainSet domainSet) throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.domainset.insert");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("domainFlag", domainSet.getDomainFlag());
		param.put("domainName", domainSet.getDomainName());	
		param.put("threadCount", domainSet.getThreadCount());
		param.put("socketTimeOut", domainSet.getSocketTimeOut());
		param.put("socketPerSendCount", domainSet.getSocketPerSendCount());
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	

	/**
	 * <p>도메인설정 변경 
	 */
	public int updateDomainSet(DomainSet domainSet) throws DataAccessException {
		
		String sql  = QueryUtil.getStringQuery("admin_sql","admin.domainset.update");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("domainID", domainSet.getDomainID());
		param.put("domainName", domainSet.getDomainName());	
		param.put("threadCount", domainSet.getThreadCount());
		param.put("socketTimeOut", domainSet.getSocketTimeOut());
		param.put("socketPerSendCount", domainSet.getSocketPerSendCount());
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	/**
	 * <p>도메인설정 보기
	 */
	@SuppressWarnings("unchecked")
	public DomainSet viewDomainSet(int domainID)  throws DataAccessException{
		DomainSet domain = new DomainSet();
		Map resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.domainset.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
			//넘겨받은 파라미터를 세팅한다. 
		
			Map<String,Object> param = new HashMap<String, Object>();		
			param.put("domainID", new Integer(domainID));
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){				
				domain.setDomainID(Integer.parseInt(String.valueOf(resultMap.get("domainID"))));
				domain.setDomainType((String)resultMap.get("domainType"));
				domain.setDomainName((String)resultMap.get("domainName"));
				domain.setThreadCount(Integer.parseInt(String.valueOf(resultMap.get("threadCount"))));
				domain.setSocketTimeOut(Integer.parseInt(String.valueOf(resultMap.get("socketTimeOut"))));
				domain.setSocketPerSendCount(Integer.parseInt(String.valueOf(resultMap.get("socketPerSendCount"))));
			}else{
				return domain;
			}
			
			return domain;
		
	}
	
	
	
	public int selectDomain(DomainSet domainSet) throws DataAccessException{

		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.domainset.count"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		
		sql += " WHERE domainName = '" + domainSet.getDomainName() + "' and domainID != '" + domainSet.getDomainID() + "' and domainFlag = '" + domainSet.getDomainFlag() + "'";
		
		return  getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	
	public DomainSet viewDomainNameSet(String domainName)  throws DataAccessException{
		DomainSet domain = new DomainSet();
		Map resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.domainset.viewname"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
			//넘겨받은 파라미터를 세팅한다. 
		
			Map<String,Object> param = new HashMap<String, Object>();		
			param.put("domainName", new String(domainName));
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){				
				domain.setDomainID(Integer.parseInt(String.valueOf(resultMap.get("domainID"))));
				domain.setDomainType((String)resultMap.get("domainType"));
				domain.setDomainName((String)resultMap.get("domainName"));
				domain.setThreadCount(Integer.parseInt(String.valueOf(resultMap.get("threadCount"))));
				domain.setSocketTimeOut(Integer.parseInt(String.valueOf(resultMap.get("socketTimeOut"))));
				domain.setSocketPerSendCount(Integer.parseInt(String.valueOf(resultMap.get("socketPerSendCount"))));
			}else{
				return domain;
			}
			
			return domain;
		
	}


}
