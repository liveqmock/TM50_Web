package web.automail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.automail.model.AutoMailEvent;
import web.automail.model.MailStatistic;
import web.automail.model.FailCauseStatistic;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;


	
public class AutoMailDAOImpl extends DBJdbcDaoSupport implements AutoMailDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";

	/**
	 * <p>자동메일리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AutoMailEvent> listAutoMailEvents(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage ;
		
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sql = QueryUtil.getStringQuery("automail_sql","automail.event.select");			
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " WHERE "+sSearchType+" LIKE :seach_text ";
		}		
			
		String sqlTail = QueryUtil.getStringQuery("automail_sql","automail.event.tail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				AutoMailEvent  autoMailEvent = new AutoMailEvent();				
				autoMailEvent.setAutomailID(rs.getInt("automailID"));
				autoMailEvent.setAutomailTitle(rs.getString("automailTitle"));
				autoMailEvent.setState(rs.getString("state"));
				autoMailEvent.setReturnMail(rs.getString("returnMail"));
				autoMailEvent.setTemplateSenderMail(rs.getString("templateSenderMail"));
				autoMailEvent.setRegistDate(rs.getString("registDate"));
				autoMailEvent.setUserName(rs.getString("userName"));
				//autoMailEvent.setLastSendDate(DateUtils.getStringDate(rs.getString("lastSendDate")));
				return autoMailEvent;
			}		
		};
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("seach_text", "%"+sSearchText+"%");
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
		
	}
	
	/**
	 * <p>자동메일카운트 증가 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoMailEvent(Map<String, String> searchMap) throws DataAccessException{
		String searchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.event.totalcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " WHERE "+searchType+" LIKE :seach_text ";
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("seach_text", "%"+sSearchText+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>자동메일이벤트등록 
	 * @param autoMailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAutoMailEvent(AutoMailEvent autoMailEvent) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.event.insert"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("automailTitle", autoMailEvent.getAutomailTitle());
		param.put("userID", autoMailEvent.getUserID());
		param.put("description", autoMailEvent.getDescription());
		param.put("templateTitle", autoMailEvent.getTemplateTitle());
		param.put("templateContent", autoMailEvent.getTemplateContent());
		param.put("templateSenderMail", autoMailEvent.getTemplateSenderMail());
		param.put("templateSenderName", autoMailEvent.getTemplateSenderName());
		param.put("templateReceiverName", autoMailEvent.getTemplateReceiverName());
		param.put("returnMail", autoMailEvent.getReturnMail());
		//param.put("fileName", autoMailEvent.getFileName());
		//param.put("fileContent", autoMailEvent.getFileContent());
		param.put("encodingType", autoMailEvent.getEncodingType());
		param.put("state", autoMailEvent.getState());
		param.put("repeatGroupType", autoMailEvent.getRepeatGroupType());
		//param.put("uploadKey", autoMailEvent.getUploadKey());
	
		
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>자동메일이벤트수정
	 * @param autoMailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAutoMailEvent(AutoMailEvent autoMailEvent) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.event.update"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("automailID", autoMailEvent.getAutomailID());
		param.put("automailTitle", autoMailEvent.getAutomailTitle());
		param.put("userID", autoMailEvent.getUserID());
		param.put("description", autoMailEvent.getDescription());
		param.put("templateTitle", autoMailEvent.getTemplateTitle());
		param.put("templateContent", autoMailEvent.getTemplateContent());
		param.put("templateSenderMail", autoMailEvent.getTemplateSenderMail());
		param.put("templateSenderName", autoMailEvent.getTemplateSenderName());
		param.put("templateReceiverName", autoMailEvent.getTemplateReceiverName());
		param.put("returnMail", autoMailEvent.getReturnMail());
		//param.put("fileName", autoMailEvent.getFileName());
		//param.put("fileContent", autoMailEvent.getFileContent());
		param.put("encodingType", autoMailEvent.getEncodingType());
		param.put("state", autoMailEvent.getState());
		param.put("repeatGroupType", autoMailEvent.getRepeatGroupType());
		//param.put("uploadKey", autoMailEvent.getUploadKey());
	
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>자동메일이벤트보기 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public AutoMailEvent viewAutoMailEvent(int automailID) throws DataAccessException{
		AutoMailEvent autoMailEvent = new AutoMailEvent();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.event.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
			//넘겨받은 파라미터를 세팅한다. 
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("automailID", new Integer(automailID));
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				autoMailEvent.setAutomailID(automailID);
				autoMailEvent.setAutomailTitle((String)resultMap.get("automailTitle"));
				autoMailEvent.setUserID((String)resultMap.get("userID"));
				autoMailEvent.setDescription((String)resultMap.get("description"));
				autoMailEvent.setTemplateTitle((String)resultMap.get("templateTitle"));
				autoMailEvent.setTemplateContent((String)(resultMap.get("templateContent")==null?"":resultMap.get("templateContent")));
				autoMailEvent.setTemplateSenderMail((String)resultMap.get("templateSenderMail"));
				autoMailEvent.setTemplateSenderName((String)resultMap.get("templateSenderName"));
				autoMailEvent.setTemplateReceiverName((String)resultMap.get("templateReceiverName"));
				autoMailEvent.setReturnMail((String)resultMap.get("returnMail"));
				//autoMailEvent.setFileName((String)resultMap.get("fileName"));
				autoMailEvent.setEncodingType((String)resultMap.get("encodingType"));
				autoMailEvent.setState((String)resultMap.get("state"));
				autoMailEvent.setRepeatGroupType((String)resultMap.get("repeatGroupType"));
				//autoMailEvent.setUploadKey((String)resultMap.get("uploadKey"));
				autoMailEvent.setRegistDate(String.valueOf(resultMap.get("registDate")));
				
			}else{
				return autoMailEvent;
			}
		return autoMailEvent;
	}
	
	/**
	 * <p>자동메일 event 삭제 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoMailEvent(int automailID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.event.delete.event"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("automailID", automailID);
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>자동메일 sendqueue 삭제 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoMailSendQueue(int automailID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.event.delete.sendqueue"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("automailID", automailID);
		return getSimpleJdbcTemplate().update(sql, param);
	}

	
	/**
	 * <p>자동메일 domainstatistic 삭제 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoMailDomainStatistic(int automailID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.event.delete.domainstatistic"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("automailID", automailID);
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>자동메일 failstatistic 삭제 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoMailFailStatistic(int automailID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.event.delete.failstatistic"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("automailID", automailID);
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>대량메일 - 시간별 통계 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> statisticHourly(Map<String, Object> searchMap) throws DataAccessException{
		
		int automailID = (Integer)searchMap.get("automailID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String checkDate = year + month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.statistic.hourly"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("automailID", new Integer(automailID));
		param.put("checkDate", checkDate);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MailStatistic mailStatistic = new MailStatistic();
				
				mailStatistic.setStandard(rs.getString("standard"));
				mailStatistic.setSendTotal(rs.getInt("sendTotal"));
				mailStatistic.setSuccessTotal(rs.getInt("successTotal"));
				mailStatistic.setFailTotal(rs.getInt("failTotal"));
				mailStatistic.setOpenTotal(rs.getInt("openTotal"));
				mailStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return mailStatistic;
			}		
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>자동메일 기간중 일자별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> statisticDaily(Map<String, Object> searchMap) throws DataAccessException{
		
		int automailID = (Integer)searchMap.get("automailID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String checkDate = year+month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.statistic.daily"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("automailID", new Integer(automailID));
		param.put("checkDate", checkDate);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MailStatistic mailStatistic = new MailStatistic();
				
				mailStatistic.setStandard(rs.getString("standard"));
				mailStatistic.setSendTotal(rs.getInt("sendTotal"));
				mailStatistic.setSuccessTotal(rs.getInt("successTotal"));
				mailStatistic.setFailTotal(rs.getInt("failTotal"));
				mailStatistic.setOpenTotal(rs.getInt("openTotal"));
				mailStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return mailStatistic;
			}		
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	

	/**
	 * <p>자동메일 기간중 월별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> statisticMonthly(Map<String, Object> searchMap) throws DataAccessException{
		
		int automailID = (Integer)searchMap.get("automailID");
		String year = (String)searchMap.get("year");
		//String month = (String)searchMap.get("month");
		String checkDate = year;

		String sql =  QueryUtil.getStringQuery("automail_sql","automail.statistic.monthly"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("automailID", new Integer(automailID));
		param.put("checkDate", checkDate);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MailStatistic mailStatistic = new MailStatistic();
				
				mailStatistic.setStandard(rs.getString("standard"));
				mailStatistic.setSendTotal(rs.getInt("sendTotal"));
				mailStatistic.setSuccessTotal(rs.getInt("successTotal"));
				mailStatistic.setFailTotal(rs.getInt("failTotal"));
				mailStatistic.setOpenTotal(rs.getInt("openTotal"));
				mailStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return mailStatistic;
			}		
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>자동메일 기간중 도메인별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> statisticDomain(Map<String, Object> searchMap) throws DataAccessException{
		
		int automailID = (Integer)searchMap.get("automailID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		
		String checkDate = year+month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.statistic.domain"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("automailID", new Integer(automailID));
		param.put("checkDate", checkDate);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MailStatistic mailStatistic = new MailStatistic();
				
				mailStatistic.setStandard(rs.getString("domainname"));
				mailStatistic.setViewStandard(rs.getString("domainname"));
				mailStatistic.setSendTotal(rs.getInt("sendTotal"));
				mailStatistic.setSuccessTotal(rs.getInt("successTotal"));
				mailStatistic.setFailTotal(rs.getInt("failTotal"));
				mailStatistic.setOpenTotal(rs.getInt("openTotal"));
				mailStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				return mailStatistic;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>자동메일 기간중 실패원인별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<FailCauseStatistic> statisticFailCause(Map<String, Object> searchMap) throws DataAccessException{
		
		int automailID = (Integer)searchMap.get("automailID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		
		String checkDate = year+month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.statistic.failcause"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("automailID", new Integer(automailID));
		param.put("checkDate", checkDate);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				FailCauseStatistic failCauseStatistic = new FailCauseStatistic();
					failCauseStatistic.setFailCauseTypeDes(rs.getString("failcauseTypeName"));
					failCauseStatistic.setFailCauseType(rs.getString("failCauseType"));
					failCauseStatistic.setFailCount(rs.getBigDecimal("failCount"));
					failCauseStatistic.setFailCauseDes(rs.getString("failcauseCodeName"));
					failCauseStatistic.setFailcauseCode(rs.getString("failcauseCode"));
				return failCauseStatistic;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>자동메일 기간중 총 시도 건수
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public int getSendTotal(Map<String, Object> searchMap) throws DataAccessException{
		
		int automailID = (Integer)searchMap.get("automailID");
		String startDate = (String)searchMap.get("startDate");
		String endDate = (String)searchMap.get("endDate");
		
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.statistic.sendTotal"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		System.out.println("startDate : " + startDate);
		System.out.println("endDate : " + endDate);
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("automailID", new Integer(automailID));
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
	
	/**
	 * <p>자동메일 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> porsonPreview(Map<String, Object> searchMap) throws DataAccessException{
		int automailID = (Integer)searchMap.get("automailID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String standard = (String)searchMap.get("standard");
		String type= (String)searchMap.get("type");
		String key= (String)searchMap.get("key");
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		
		int start = (currentPage - 1) * countPerPage;
		//countPerPage = countPerPage * currentPage;
		
		String startDate =year+"-"+month+"-"+day;
		String endDate =startDate+" 23:59:59";
		if(day.equals("all")){
			startDate =  year+"-"+month+"-01";
			int lastDay = 0;
			try{
				lastDay = DateUtils.lastDay(Integer.parseInt(year), Integer.parseInt(month));
			}catch(Exception e){}
			endDate = year+"-"+month+"-"+lastDay+" 23:59:59";
		}
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewselect");

		

		if(standard.equals("monthly")){
			sql+= " tm_automail_sendresult_"+key+" ";
		}else{
			sql+= " tm_automail_sendresult_"+year+month+" ";	
		}

		if(standard.equals("hourly")){
			if(key.length() == 1){key="0"+key;}
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewhourlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewhourlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewhourlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewhourlyopen");
			}
		}else if(standard.equals("daily")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdailytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdailysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdailyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdailyopen");
			}
		}else if(standard.equals("monthly")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewmonthlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewmonthlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewmonthlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewmonthlyopen");
			}
		}else if(standard.equals("domain")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdomaintotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdomainsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdomainfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdomainopen");
			}
		}else if(standard.equals("failcause")){
			sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewfailcause");
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";
		}
		
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		else{
			String sqlTail = "";
			if(currentPage > 0 && countPerPage > 0){
				sqlTail = QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreview.tail");
			}
			else{
				if(db_type.equals(DB_TYPE_MSSQL))
					sqlTail = " ) AS A";
			}
			sql += sqlTail;
		}
	
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("automailID", new Integer(automailID));
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("key", key);
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MailStatistic mailStatistic = new MailStatistic();
				mailStatistic.setEmail(rs.getString("email"));
				mailStatistic.setRegistDate(rs.getString("registDate"));
				mailStatistic.setSmtpCode(rs.getString("smtpCode"));
				mailStatistic.setSmtpMsg(rs.getString("smtpMsg"));
				return mailStatistic;
			}		
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>자동메일 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap) throws DataAccessException{
		int automailID = (Integer)searchMap.get("automailID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String standard = (String)searchMap.get("standard");
		String type= (String)searchMap.get("type");
		String key= (String)searchMap.get("key");
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		
		String startDate =year+"-"+month+"-"+day;
		String endDate =startDate+" 23:59:59";
		if(day.equals("all")){
			startDate =  year+"-"+month+"-01";
			int lastDay = 0;
			try{
				lastDay = DateUtils.lastDay(Integer.parseInt(year), Integer.parseInt(month));
			}catch(Exception e){}
			endDate = year+"-"+month+"-"+lastDay+" 23:59:59";
		}
		
		String sql =  QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewtotal");
		if(standard.equals("monthly")){
			sql+= " tm_automail_sendresult_"+key+" ";
		}else{
			sql+= " tm_automail_sendresult_"+year+month+" ";	
		}
		
		if(standard.equals("hourly")){
			if(key.length() == 1){key="0"+key;}
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewhourlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewhourlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewhourlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewhourlyopen");
			}
		}else if(standard.equals("daily")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdailytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdailysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdailyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdailyopen");
			}
		}else if(standard.equals("monthly")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewmonthlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewmonthlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewmonthlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewmonthlyopen");
			}
		}else if(standard.equals("domain")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdomaintotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdomainsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdomainfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewdomainopen");
			}
		}else if(standard.equals("failcause")){
			sql += QueryUtil.getStringQuery("automail_sql","automail.statistic.porsonpreviewfailcause");
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";
		}
		
		//System.out.println("sql : "+sql);
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("automailID", new Integer(automailID));
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("key", key);
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>자동메일 월간 통계 ID 별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public List<MailStatistic> autoMailReportMonth(Map<String, Object> searchMap) throws DataAccessException{
		
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		String sql = "";
		String sendTime = (String)searchMap.get("sendTime");
		int start = (currentPage - 1) * countPerPage ;
		
		if(Integer.parseInt(sendTime.substring(4))==13){
			sql = QueryUtil.getStringQuery("automail_sql", "automail.write.reportyear");
			sendTime = sendTime.substring(0,4);
		}else{
			sql = QueryUtil.getStringQuery("automail_sql", "automail.write.reportmonth");
		}
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
				
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper(){
			public Object mapRow(ResultSet rs , int rownum) throws SQLException{
				
				MailStatistic mailstatistic = new MailStatistic();
				
				mailstatistic.setAutomailID(rs.getInt("automailID"));
				mailstatistic.setAutomailTitle(rs.getString("automailTitle"));
				mailstatistic.setSendTimeHour(rs.getString("sendTimeHour"));
				mailstatistic.setSendTotal(rs.getInt("sendTotal"));
				mailstatistic.setSuccessTotal(rs.getInt("successTotal"));
				mailstatistic.setFailTotal(rs.getInt("failTotal"));
				mailstatistic.setOpenTotal(rs.getInt("openTotal"));
				
				return mailstatistic;
			}
		};
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sendtime", sendTime);
		param.put("start", start);
		param.put("count",countPerPage);
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>자동메일 월간 통계 전체 발송 현황
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */	
	public MailStatistic autoMailReportMonthAll(String sendTime) throws DataAccessException{
		Map<String, Object> resultMap = null;
		MailStatistic mailStatistic = new MailStatistic();
		String sql = "";
		
		if(Integer.parseInt(sendTime.substring(4))==13){
			sql = QueryUtil.getStringQuery("automail_sql", "automail.write.reportyearall");
			sendTime = sendTime.substring(0,4);
		}else{
			sql = QueryUtil.getStringQuery("automail_sql", "automail.write.reportmonthall");
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sendtime", sendTime);
		try{
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(Exception e){}
		
		if(resultMap!=null){
			mailStatistic.setSendTotal(Integer.parseInt(String.valueOf(resultMap.get("sendTotal"))));
			mailStatistic.setSuccessTotal(Integer.parseInt(String.valueOf(resultMap.get("successTotal"))));
			mailStatistic.setFailTotal(Integer.parseInt(String.valueOf(resultMap.get("failTotal"))));
			mailStatistic.setOpenTotal(Integer.parseInt(String.valueOf(resultMap.get("openTotal"))));
			
		}else{
			return mailStatistic;
		}
		
		return mailStatistic;
	}
	
	/**
	 * <p>자동메일 월간통계 카운트 
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoMailReportMonth(String sendTime) throws DataAccessException{
		
		String sql = "";
		
		if(Integer.parseInt(sendTime.substring(4))==13){
			sql = QueryUtil.getStringQuery("automail_sql","automail.write.reportyearcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
			sendTime = sendTime.substring(0,4);
		}else{
			sql = QueryUtil.getStringQuery("automail_sql","automail.write.reportmonthcount");
		}		 

		Map<String,Object> param = new HashMap<String, Object>();
		param.put("sendtime", sendTime);
		
		return  getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
}
