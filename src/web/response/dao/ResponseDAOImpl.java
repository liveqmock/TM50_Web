package web.response.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;
import web.common.util.PropertiesUtil;

public class ResponseDAOImpl extends DBJdbcDaoSupport implements ResponseDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";

	/**
	 * <p>메일을 최초 오픈하였을 때 tm_massmail_openresult 테이블에 openYN='Y' 로 인서트 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int insertOpenMassMailSendResult(int sendID, int massmailID, int scheduleID) throws DataAccessException{
		String sql1 = "insert into tm_massmail_openresult_"+massmailID+"_"+scheduleID ;
		String sqlSub = QueryUtil.getStringQuery("response_sql","massmail.response.open.insert");			
		String sql = sql1+sqlSub;
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", new Integer(sendID));
				
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	/**
	 * <p>메일을 최초 오픈하였을 때 tm_massmail_openresult 테이블에 openYN='Y' 로 업데이트(이미 같은 sendID가 있을 때) 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateOpenMassMailSendResult(int sendID, int massmailID, int scheduleID) throws DataAccessException{
		String sql1 = "update tm_massmail_openresult_"+massmailID+"_"+scheduleID ;
		String sqlSub = QueryUtil.getStringQuery("response_sql","massmail.response.open.update");			
		String sql = sql1 + " " + sqlSub;
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", new Integer(sendID));
				
		return getSimpleJdbcTemplate().update(sql,param);
	}
	

	
	
	/**
	 * <p>메일본문에 최초 링크를 하나라도 클릭하였을 때 tm_massmail_openresult 테이블에 clickYN='Y' 로 인서트 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int insertClickMassMailSendResult(int sendID, int massmailID,int scheduleID) throws DataAccessException{
		String sql1 = "insert into tm_massmail_openresult_"+massmailID+"_"+scheduleID;
		String sqlSub = QueryUtil.getStringQuery("response_sql","massmail.response.click.insert");
		String sql = sql1+sqlSub;
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", new Integer(sendID));
				
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>메일본문에 최초 링크를 하나라도 클릭하였을 때 tm_massmail_openresult 테이블에 clickYN='Y' 로 업데이트(이미 같은 sendID가 있을 때)
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateClickMassMailSendResult(int sendID, int massmailID, int scheduleID) throws DataAccessException{
		String sql1 = "update tm_massmail_openresult_"+massmailID+"_"+scheduleID ;
		String sqlSub = QueryUtil.getStringQuery("response_sql","massmail.response.click.update");			
		String sql = sql1 + " " + sqlSub;
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", new Integer(sendID));
				
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>메일본문에 최초 수신거부를 클릭하였을 때 tm_massmail_openresult 테이블에 rejectcall='Y' 로 인서트 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int insertRejectMassMailSendResult(int sendID, int massmailID,int scheduleID) throws DataAccessException{
		String sql1 = "insert into tm_massmail_openresult_"+massmailID+"_"+scheduleID;
		String sqlSub = QueryUtil.getStringQuery("response_sql","massmail.response.rejectresult.insert");
		String sql = sql1+sqlSub;
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", new Integer(sendID));
				
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>메일본문에 최초 수신거부를 클릭하였을 때 tm_massmail_openresult 테이블에 rejectcall='Y'로 업데이트(이미 같은 sendID가 있을 때)
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateRejectMassMailSendResult(int sendID,int massmailID, int scheduleID) throws DataAccessException{
		String sql1 = "UPDATE tm_massmail_openresult_"+massmailID+"_"+scheduleID;
		String sqlSub = QueryUtil.getStringQuery("response_sql","massmail.response.rejectresult.update");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", new Integer(sendID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		String sql = sql1+" "+sqlSub;
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	/**
	 * <p>메일본문에 링크한 정보 인서트 
	 * @param massmailID
	 * @param linkID
	 * @param sendID
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public int insertLinkClick(int massmailID, int scheduleID, int linkID, int sendID, String email, int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("response_sql","massmail.response.linkclick.insert");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", new Integer(sendID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("linkID", new Integer(linkID));
		param.put("targetID", new Integer(targetID));
		param.put("email", email);
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	/**
	 * <p>수신거부에 등록되어 있는지 확인 
	 * @param email
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkReject(String email, int massmailID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("response_sql","massmail.response.reject.selectcount");			
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("email", email);
		param.put("massmailID", massmailID);
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>메일본문에 수신거부클릭 인서트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @param targetID
	 * @param massmailGroupID
	 * @param customerID
	 * @return
	 */
	public int insertRejectClick(int massmailID, String email, int targetID, int massmailGroupID, String userID, String groupID, String customerID){
		String sql = QueryUtil.getStringQuery("response_sql","massmail.response.reject.insert");			
		Map<String,Object> param = new HashMap<String, Object>();		
		
		param.put("massmailID", new Integer(massmailID));
		param.put("massmailGroupID", new Integer(massmailGroupID));
		param.put("targetID", new Integer(targetID));
		param.put("userID", userID);
		param.put("groupID", groupID);
		param.put("email", email);
		param.put("customerID", customerID);
		return getSimpleJdbcTemplate().update(sql,param);
	}

	/**
	 * <p>자동 메일을 오픈하였을 때 ez_automail_sendresult에 openYN='Y' 로 최초한번 업데이트 
	 * @param automailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenAutoMailSendResult(String sendID,int automailID, String yearMonth) throws DataAccessException{
		String sql1 = "UPDATE tm_automail_sendresult_"+yearMonth;
		String sql2 = QueryUtil.getStringQuery("response_sql","automail.response.openclick.update");
		String sql = sql1 +" "+sql2;
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", sendID);
		param.put("automailID", new Integer(automailID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	
	/**
	 * <p>통계수집을 요청하기 위한 업데이트 tm_massmail_schedule.updateStatisticYN='N' 으로 해야 통계가 다시 수집된다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public int updateScheduleStatistic(int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("response_sql","massmail.response.schedule.update");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>sendID에 해당하는 userID, gorupID, customerID를 리턴한다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getSendIDInfo(int massmailID, int scheduleID, int sendID)  throws DataAccessException{
		
		Map<String, Object> resultMap = null;
		String tableName = "";	
		if(db_type.equals(DB_TYPE_ORACLE))	
			tableName = " tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" r ";
		else if(db_type.equals(DB_TYPE_MYSQL))
			tableName = " tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" as r ";
		else if(db_type.equals(DB_TYPE_MSSQL))
			tableName = " tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" as r ";
		String sql1 =  QueryUtil.getStringQuery("response_sql","massmail.response.reject.selectinfo1"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sql2 =  QueryUtil.getStringQuery("response_sql","massmail.response.reject.selectinfo2"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sql = sql1+tableName+sql2;
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("sendID", new Integer(sendID));
	
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
	
		return resultMap;
	}
	
	/**
	 * <p>테이블명이 존재하는지 체크한다. 
	 * @param tableName
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Map<String, Object>> isExistTable(String tableName) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("response_sql","massmail.response.isExistTable");	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("tableName", tableName);
		return getSimpleJdbcTemplate().queryForList(sql, param);
	}
	
	
	/**
	 * <p>메일을 오픈하였을 때 tm_intermail_sendresult_YYYYMM에 openYN='Y, openDate=NOW()' 로 최초한번 업데이트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenInterMailSendResult(String sendID,int intermailID, int scheduleID, String yearMonth) throws DataAccessException{
		String sql1 = "UPDATE tm_intermail_sendresult_"+yearMonth;
		String sqlSub = QueryUtil.getStringQuery("response_sql","intermail.response.openclick.update");			
		String sql = sql1+" "+sqlSub;
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", new Integer(sendID));
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>메일을 오픈하였을 때 tm_intermail_sendresult_YYYYMM에 openYN='Y, openDate=NOW()' 로 최초한번 업데이트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenFileInterMailSendResult(String sendID,int intermailID, int scheduleID, String yearMonth) throws DataAccessException{
		String sql1 = "UPDATE tm_intermail_sendresult_"+yearMonth;
		String sqlSub = QueryUtil.getStringQuery("response_sql","intermail.response.openFileclick.update");			
		String sql = sql1+" "+sqlSub;
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", new Integer(sendID));
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * tm_massmail_openresult 테이블에 데이터가 있는지 확인한다.
	 * 
	 */
	public int isExistData(int sendID, int massmailID, int scheduleID) throws DataAccessException{
		String sql_where = QueryUtil.getStringQuery("response_sql","massmail.response.isExistData");	
		String sql_select = "select count(*) from tm_massmail_openresult_" + massmailID + "_" + scheduleID;
		String sql = sql_select + " " +  sql_where;
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("sendID", sendID);
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * tm_massmail_reject 테이블에 데이터가 있는지 확인한다.
	 * 
	 */
	public int isExistDataReject(String email, int massmailGroupID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("response_sql","massmail.response.isExistDataReject");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("email", email);
		param.put("massmailGroupID", new Integer(massmailGroupID));
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
}
