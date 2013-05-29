package web.intermail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.intermail.model.*;
import web.common.dao.DBJdbcDaoSupport;
import web.common.model.FileUpload;
import web.common.util.*;




public class InterMailDAOImpl extends DBJdbcDaoSupport implements InterMailDAO{
	
	
	/**
	 * <p>연동메일 갯수 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountInterMailEvent(Map<String, String> searchMap) throws DataAccessException{
		String searchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.getTotalCountInterMailEvent"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " WHERE "+searchType+" LIKE :searchText ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("searchText", "%"+sSearchText+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>연동메일리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<InterMailEvent> listInterMailEvents(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage ;
		
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.write.listInterMailEvents");			
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " WHERE "+sSearchType+" LIKE :seach_text ";
		}		
			
		String sqlTail = QueryUtil.getStringQuery("intermail_sql","intermail.write.tail");			
		sql += sqlTail;
		
		//e.intermailID, s.scheduleID, e.intermailTitle, e.userID, s.state, e.registDate
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				InterMailEvent  interMailEvent = new InterMailEvent();
				interMailEvent.setIntermailID(rs.getInt("intermailID"));
				interMailEvent.setScheduleID(rs.getInt("scheduleID"));
				interMailEvent.setIntermailTitle(rs.getString("intermailTitle"));
				interMailEvent.setSendType(rs.getString("sendType"));
				interMailEvent.setState(rs.getString("state"));
				interMailEvent.setUserID(rs.getString("userID"));
				interMailEvent.setUserName(rs.getString("userName"));				
				interMailEvent.setTemplateFileName(rs.getString("templateFileName"));
				interMailEvent.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));			
				interMailEvent.setRepeatGroupType(rs.getString("repeatGroupType"));
				interMailEvent.setResultYearMonth(rs.getString("resultYearMonth"));
				return interMailEvent;
			}		
		};
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("seach_text", "%"+sSearchText+"%");
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
		
	}
	
	
	/**
	 * <p>연동메일이벤트보기 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailEvent viewInterMailEvent(int intermailID) throws DataAccessException{
		InterMailEvent interMailEvent = new InterMailEvent();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.viewInterMailEvent"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
			//넘겨받은 파라미터를 세팅한다. 
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("intermailID", new Integer(intermailID));
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				interMailEvent.setIntermailID(intermailID);
				interMailEvent.setIntermailTitle((String)resultMap.get("intermailTitle"));
				interMailEvent.setUserID((String)resultMap.get("userID"));
				interMailEvent.setDescription((String)resultMap.get("description"));
				interMailEvent.setTemplateTitle((String)resultMap.get("templateTitle"));
				interMailEvent.setTemplateContent((String)resultMap.get("templateContent"));
				interMailEvent.setTemplateSenderMail((String)resultMap.get("templateSenderMail"));
				interMailEvent.setTemplateSenderName((String)resultMap.get("templateSenderName"));
				interMailEvent.setTemplateReceiverName((String)resultMap.get("templateReceiverName"));
				interMailEvent.setReturnMail((String)resultMap.get("returnMail"));
				interMailEvent.setEncodingType((String)resultMap.get("encodingType"));
				interMailEvent.setFileKey((String)resultMap.get("fileKey"));				
				interMailEvent.setTemplateFileName((String)resultMap.get("templateFileName"));
				interMailEvent.setSecretYN((String)resultMap.get("secretYN"));
				interMailEvent.setRepeatGroupType((String)resultMap.get("repeatGroupType"));
				interMailEvent.setRegistDate(String.valueOf(resultMap.get("registDate")));			
				
			}else{
				return interMailEvent;
			}
		return interMailEvent;
	}
	
	
	
	/**
	 * <p>연동발송설정보기
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<InterMailSchedule> selectInterMailSchedule(int intermailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.write.selectInterMailSchedule");			
		//e.intermailID, s.scheduleID, e.intermailTitle, e.userID, s.state, e.registDate
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				InterMailSchedule  interMailSchedule = new InterMailSchedule();
				interMailSchedule.setScheduleID(rs.getInt("scheduleID"));
				interMailSchedule.setSendType(rs.getString("sendType"));
				interMailSchedule.setState(rs.getString("state"));				
				interMailSchedule.setSendScheduleDate(DateUtils.getStringDate(rs.getString("sendScheduleDate")));
				interMailSchedule.setSendScheduleDateHH(rs.getString("sendScheduleDateHH"));
				interMailSchedule.setSendScheduleDateMM(rs.getString("sendScheduleDateMM"));
				interMailSchedule.setSendStartTime(rs.getString("sendStartTime"));
				interMailSchedule.setSendEndTime(rs.getString("sendEndTime"));
				interMailSchedule.setRetryStartTime(rs.getString("retryStartTime"));
				interMailSchedule.setRetryEndTime(rs.getString("retryEndTime"));
				interMailSchedule.setRetryCount(rs.getInt("retryCount"));
				interMailSchedule.setRetryFinishYN(rs.getString("retryFinishYN"));
				interMailSchedule.setStatisticEndDate(DateUtils.getStringDate(rs.getString("statisticEndDate")));
				interMailSchedule.setStatisticEndDateHH(rs.getString("statisticEndDateHH"));
				interMailSchedule.setStatisticEndDateMM(rs.getString("statisticEndDateMM"));
				interMailSchedule.setRegistDate(rs.getString("registDate"));

				return interMailSchedule;
			}		
		};
		Map<String,Object> param = new HashMap<String, Object>();			
		param.put("intermailID", new Integer(intermailID));
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>업로드키로 파일정보 불러오기 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public FileUpload getFileInfo(String uploadKey) throws DataAccessException{
		FileUpload fileUpload = new FileUpload();
		Map<String, Object> resultMap = null;
		
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.fileupload.info");			
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("fileKey", uploadKey);		
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			fileUpload.setUploadKey(uploadKey);
			fileUpload.setNewFileName(String.valueOf(resultMap.get("fileKey")));
			fileUpload.setRealFileName(String.valueOf(resultMap.get("fileName")));			
		}
		
		return fileUpload;
	}
	
	/**
	 * <p>연동메일 정보입력
	 * @param intermailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertInterMailEvent(InterMailEvent interMailEvent) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.insertInterMailEvent"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("intermailTitle", interMailEvent.getIntermailTitle());
		param.put("userID", interMailEvent.getUserID());
		param.put("description", interMailEvent.getDescription());
		param.put("templateTitle", interMailEvent.getTemplateTitle());
		param.put("templateContent", interMailEvent.getTemplateContent());
		param.put("templateSenderMail", interMailEvent.getTemplateSenderMail());
		param.put("templateSenderName", interMailEvent.getTemplateSenderName());
		param.put("templateReceiverName", interMailEvent.getTemplateReceiverName());		
		param.put("returnMail", interMailEvent.getReturnMail());
		param.put("encodingType", interMailEvent.getEncodingType());		
		param.put("secretYN", interMailEvent.getSecretYN());		
		param.put("fileKey", interMailEvent.getFileKey());
		param.put("templateFileName", interMailEvent.getTemplateFileName());
		param.put("templateFileContent", interMailEvent.getTemplateFileContent());
		param.put("repeatGroupType", interMailEvent.getRepeatGroupType());
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>스케줄입력 
	 * @param interMailSchedule
	 * @return
	 * @throws DataAccessException
	 */	
	public int insertInterMailSchedule(InterMailSchedule interMailSchedule) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.insertInterMailSchedule"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("intermailID", interMailSchedule.getIntermailID());
		param.put("scheduleID", interMailSchedule.getScheduleID());
		param.put("sendType", interMailSchedule.getSendType());
		param.put("state", interMailSchedule.getState());	
		param.put("sendScheduleDate", interMailSchedule.getSendScheduleDate());
		param.put("statisticEndDate", interMailSchedule.getStatisticEndDate());
		
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>가장 최근에 입력된 intermailID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxInterMailID() throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.getMaxInterMailID"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.	
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	
	/**
	 * <p>tm_intermail_schedule에 intermailID에 해당되는 deleteYN=Y로 변경 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteInterMailSchedule(int intermailID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.deleteInterMailSchedule"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("intermailID", new Integer(intermailID));
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	
	/**
	 * <p>파일 인서트 
	 * @param fileKey
	 * @param fileName
	 * @return
	 * @throws DataAccessException
	 */
	public int insertFile(FileUpload fileUpload) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.insertFile"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("fileKey", fileUpload.getUploadKey());
		param.put("fileName", fileUpload.getRealFileName());		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>연동메일 정보입력
	 * @param intermailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailEvent(InterMailEvent interMailEvent) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.updateInterMailEvent"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("intermailTitle", interMailEvent.getIntermailTitle());
		param.put("userID", interMailEvent.getUserID());
		param.put("description", interMailEvent.getDescription());
		param.put("templateTitle", interMailEvent.getTemplateTitle());
		param.put("templateContent", interMailEvent.getTemplateContent());
		param.put("templateSenderMail", interMailEvent.getTemplateSenderMail());
		param.put("templateSenderName", interMailEvent.getTemplateSenderName());
		param.put("templateReceiverName", interMailEvent.getTemplateReceiverName());		
		param.put("returnMail", interMailEvent.getReturnMail());
		param.put("encodingType", interMailEvent.getEncodingType());		
		param.put("secretYN", interMailEvent.getSecretYN());		
		param.put("fileKey", interMailEvent.getFileKey());
		param.put("templateFileName", interMailEvent.getTemplateFileName());
		param.put("templateFileContent", interMailEvent.getTemplateFileContent());
		param.put("repeatGroupType", interMailEvent.getRepeatGroupType());
		param.put("intermailID", interMailEvent.getIntermailID());
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	
	/**
	 * <p>연동메일 스케줄 업데이트 
	 * @param interMailSchedule
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailSchedule(InterMailSchedule interMailSchedule) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.updateInterMailSchedule"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("intermailID", interMailSchedule.getIntermailID());
		param.put("scheduleID", interMailSchedule.getScheduleID());
		param.put("sendType", interMailSchedule.getSendType());
		param.put("state", interMailSchedule.getState());	
		param.put("sendScheduleDate", interMailSchedule.getSendScheduleDate());
		param.put("statisticEndDate", interMailSchedule.getStatisticEndDate());		
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
		
	
	/**
	 * <p>발송 상태 변경
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int intermailID, int scheduleID, String state) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.write.updateSendState");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("state", state);
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	/**
	 * <p>연동상태값을 가져온다.
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> getInterMailState(int intermailID, int scheduleID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.getInterMailState");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));		
		return getSimpleJdbcTemplate().queryForMap(sql, param);
	}
	
	
	/**
	 * <p>tm_intermail_send_ 테이블 생성 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public int createSendQueueTable(String tableName) throws DataAccessException{
		String sql = "CREATE TABLE "+tableName;
		String sqlSub =  QueryUtil.getStringQuery("intermail_sql","intermail.write.createSendQueueTable"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		sql = sql+" "+sqlSub;
		Map<String,Object> param = new HashMap<String, Object>();
		return getSimpleJdbcTemplate().update(sql, param);
	}	
	
	
	/**
	 * <p>연동메일 - 시간별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> statisticHourly(Map<String, Object> searchMap) throws DataAccessException{
		
		int intermailID = (Integer)searchMap.get("intermailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String checkDate = year+month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.hourly"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
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
	 * <p>연동메일 기간중 일자별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> statisticDaily(Map<String, Object> searchMap) throws DataAccessException{
		
		int intermailID = (Integer)searchMap.get("intermailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String checkDate = year+month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.daily"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
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
	 * <p>연동메일 기간중 월별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> statisticMonthly(Map<String, Object> searchMap) throws DataAccessException{
		
		int intermailID = (Integer)searchMap.get("intermailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		String year = (String)searchMap.get("year");
		//String month = (String)searchMap.get("month");
		String checkDate = year;

		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.monthly"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
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
	 * <p>연동메일 기간중 도메인별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> statisticDomain(Map<String, Object> searchMap) throws DataAccessException{
		
		int intermailID = (Integer)searchMap.get("intermailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		
		String checkDate = year+month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.domain"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
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
	 * <p>연동메일 기간중 실패원인별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<FailCauseStatistic> statisticFailCauseList(Map<String, Object> searchMap) throws DataAccessException{
		
		int intermailID = (Integer)searchMap.get("intermailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		
		String checkDate = year+month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.failcause"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
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
	 * <p>연동메일 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailStatistic> porsonPreview(Map<String, Object> searchMap) throws DataAccessException{
		int intermailID = (Integer)searchMap.get("intermailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
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
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewselect");

		

		if(standard.equals("monthly")){
			sql+= " tm_intermail_sendresult_"+key+" ";
		}else{
			sql+= " tm_intermail_sendresult_"+year+month+" ";	
		}

		if(standard.equals("hourly")){
			if(type.equals("total")){
				if(key.length() == 1){key="0"+key;}
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewhourlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewhourlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewhourlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewhourlyopen");
			}
		}else if(standard.equals("daily")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdailytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdailysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdailyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdailyopen");
			}
		}else if(standard.equals("monthly")){
			startDate = year+"-01-01";
			endDate = year+"-12-31 23:59:59";
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewmonthlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewmonthlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewmonthlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewmonthlyopen");
			}
		}else if(standard.equals("domain")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdomaintotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdomainsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdomainfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdomainopen");
			}
		}else if(standard.equals("failcause")){
			sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewfailcause");
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
		}
		if(currentPage > 0 && countPerPage > 0){
			String sqlTail = QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreview.tail");
			sql += sqlTail;
		}
	
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("key", key);
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		
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
	 * <p>연동메일 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap) throws DataAccessException{
		int intermailID = (Integer)searchMap.get("intermailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
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
		
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewtotal");
		if(standard.equals("monthly")){
			sql+= " tm_intermail_sendresult_"+key+" ";
		}else{
			sql+= " tm_intermail_sendresult_"+year+month+" ";	
		}
		
		if(standard.equals("hourly")){
			if(key.length() == 1){key="0"+key;}
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewhourlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewhourlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewhourlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewhourlyopen");
			}
		}else if(standard.equals("daily")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdailytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdailysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdailyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdailyopen");
			}
		}else if(standard.equals("monthly")){
			startDate = year+"-01-01";
			endDate = year+"-12-31 23:59:59";
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewmonthlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewmonthlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewmonthlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewmonthlyopen");
			}
		}else if(standard.equals("domain")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdomaintotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdomainsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdomainfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewdomainopen");
			}
		}else if(standard.equals("failcause")){
			sql += QueryUtil.getStringQuery("intermail_sql","intermail.statistic.porsonpreviewfailcause");
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
		}
		
		//System.out.println("sql : "+sql);
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("key", key);
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	
	/**
	 * <p>발송대기자 전체 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalInterMailSendList(Map<String, Object> paramMap) throws DataAccessException{
		String searchType = (String)paramMap.get("sSearchType");
		String sSearchText = (String)paramMap.get("sSearchText");
		int intermailID = Integer.parseInt(String.valueOf(paramMap.get("intermailID")));
		int scheduleID = Integer.parseInt(String.valueOf(paramMap.get("scheduleID")));
		
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.write.totalInterMailSendList"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		sql = sql+" tm_intermail_send_"+intermailID+" WHERE intermailID=:intermailID AND scheduleID=:scheduleID ";
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("searchText", "%"+sSearchText+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	
	/**
	 * <p>발송대기자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<InterMailSend> selectInterMailSendList(Map<String, Object> paramMap)  throws DataAccessException{
		
		
		int currentPage = Integer.parseInt(String.valueOf(paramMap.get("currentPage")));	
		int countPerPage = Integer.parseInt(String.valueOf(paramMap.get("countPerPage")));	
		int intermailID = Integer.parseInt(String.valueOf(paramMap.get("intermailID")));	
		int scheduleID = Integer.parseInt(String.valueOf(paramMap.get("scheduleID")));
	
		
		int start = (currentPage - 1) * countPerPage ;
				
		String sSearchType = (String)paramMap.get("sSearchType");
		String sSearchText = (String)paramMap.get("sSearchText");
		
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.write.selectInterMailSendList1");
		sql = sql+" tm_intermail_send_"+intermailID+" WHERE intermailID=:intermailID AND scheduleID=:scheduleID ";
		//검색조건이 있다면		
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " AND "+sSearchType+" LIKE :seach_text ";
		}		
			
		String sqlTail = QueryUtil.getStringQuery("intermail_sql","intermail.write.selectInterMailSendList2");			
		sql += sqlTail;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				InterMailSend  interMailSend = new InterMailSend();
				interMailSend.setSendID(rs.getString("sendID"));
				interMailSend.setScheduleID(rs.getInt("scheduleID"));
				interMailSend.setIntermailID(rs.getInt("intermailID"));
				interMailSend.setDomainName(rs.getString("domainName"));
				interMailSend.setEmail(rs.getString("email"));
				interMailSend.setMailTitle(rs.getString("mailTitle"));
				interMailSend.setFileName(rs.getString("fileName"));
				interMailSend.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				return interMailSend;

			}		
		};
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("seach_text", "%"+sSearchText+"%");
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>발송대기자 상세보기 
	 * @param intermailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailSend viewInterMailSend(int intermailID, int scheduleID, String sendID) throws DataAccessException{
		
		InterMailSend interMailSend = new InterMailSend();
		Map<String, Object> resultMap = null;
		
		String sql1 =  QueryUtil.getStringQuery("intermail_sql","intermail.write.viewInterMailSend1"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sql2 =  QueryUtil.getStringQuery("intermail_sql","intermail.write.viewInterMailSend2"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sql = sql1+" tm_intermail_send_"+intermailID+" "+sql2;
			//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("sendID", sendID);
			
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
			
		if(resultMap!=null){
			interMailSend.setIntermailID(intermailID);
			interMailSend.setScheduleID(scheduleID);
			interMailSend.setSendID((String)resultMap.get("sendID"));
			interMailSend.setEmail((String)resultMap.get("email"));
			interMailSend.setDomainName((String)resultMap.get("domainName"));
			interMailSend.setReceiverName(ThunderUtil.replaceOnetoone((String)resultMap.get("onetooneInfo"),(String)resultMap.get("receiverName")));
			interMailSend.setSenderName((String)resultMap.get("senderName"));
			interMailSend.setSenderMail((String)resultMap.get("senderMail"));
			interMailSend.setOnetooneInfo((String)resultMap.get("onetooneInfo"));
			interMailSend.setRepeatGroupType((String)resultMap.get("repeatGroupType"));
			
			interMailSend.setMailTitle(ThunderUtil.replaceOnetoone((String)resultMap.get("onetooneInfo"),(String)resultMap.get("mailTitle")));			
			interMailSend.setFileName((String)resultMap.get("fileName"));				
			
			//메일본문이나 전체에 반복그룹 사용이라면 
			if(interMailSend.getRepeatGroupType().equals(Constant.INTERMAIL_REPEAT_TYPE_CONTENT) || interMailSend.getRepeatGroupType().equals(Constant.INTERMAIL_REPEAT_TYPE_ALL)){
				interMailSend.setMailContent(ThunderUtil.replaceOnetooneRepeat((String)resultMap.get("onetooneInfo"),(String)resultMap.get("mailContent")));
			}else{
				interMailSend.setMailContent(ThunderUtil.replaceOnetoone((String)resultMap.get("onetooneInfo"),(String)resultMap.get("mailContent")));
			}
			//첨부파일이나 전체에 반복그룹 사용이라면 
			if(interMailSend.getRepeatGroupType().equals(Constant.INTERMAIL_REPEAT_TYPE_FILE) || interMailSend.getRepeatGroupType().equals(Constant.INTERMAIL_REPEAT_TYPE_ALL)){
				interMailSend.setFileContent(ThunderUtil.replaceOnetooneRepeat((String)resultMap.get("onetooneInfo"),(String)resultMap.get("fileContent")));
			}else{
				interMailSend.setFileContent(ThunderUtil.replaceOnetoone((String)resultMap.get("onetooneInfo"),(String)resultMap.get("fileContent")));
			}			
			
		}else{
			return interMailSend;
		}
		return interMailSend;
	}
	
	/**
	 * <p>체크된 발송대기자 삭제
	 * @param intermailID
	 * @param scheduleID
	 * @param sendIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSelectedInterMailSend(int intermailID, int scheduleID, String[] sendIDs)  throws DataAccessException{
		String sql1 = QueryUtil.getStringQuery("intermail_sql","intermail.write.deleteSelectedInterMailSend1");
		String sql2 = QueryUtil.getStringQuery("intermail_sql","intermail.write.deleteSelectedInterMailSend2");
		String sql = sql1+" tm_intermail_send_"+intermailID+sql2;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));

		String sqlcols = "";
		for(int i=0;i<sendIDs.length;i++){
			if(i==sendIDs.length-1){
				sqlcols +=sendIDs[i]+")";
			}else{
				sqlcols+=sendIDs[i]+",";
			}
		}
		
		sql += sqlcols;				
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	
	/**
	 * <p>모든  발송대기자 삭제
	 * @param intermailID
	 * @param scheduleID
	 * @param sendIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAllInterMailSend(int intermailID, int scheduleID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.write.deleteAllInterMailSend");
		sql += " tm_intermail_send_"+intermailID+" WHERE scheduleID="+scheduleID;
		return getSimpleJdbcTemplate().update(sql);		
	}
	
	/**
	 * <p>발송승인 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailSendApprove(int intermailID, int scheduleID, String state) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.write.updateInterMailSendApprove");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("state", state);		
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	
	/**
	 * <p>발송결과 히스토리 내역 
	 * @param paramsMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<InterMailSendResult> selectInterMailHistory(Map<String,Object> paramsMap)  throws DataAccessException{
	
		int currentPage = Integer.parseInt(String.valueOf(paramsMap.get("currentPage")));
		int countPerPage = Integer.parseInt(String.valueOf(paramsMap.get("countPerPage")));
		
		int start = (currentPage - 1) * countPerPage ;
		
		String searchType = (String)paramsMap.get("searchType");
		String searchText = (String)paramsMap.get("searchText");
		String resultYearMonth = (String)paramsMap.get("resultYearMonth");
		String smtpCodeType = (String)paramsMap.get("smtpCodeType");
		String openYN = (String)paramsMap.get("openYN");
		String openFileYN = (String)paramsMap.get("openFileYN");
		int intermailID = Integer.parseInt(String.valueOf(paramsMap.get("intermailID")));
		int scheduleID = Integer.parseInt(String.valueOf(paramsMap.get("scheduleID")));
			
		String sql = "";
		String sql1 = QueryUtil.getStringQuery("intermail_sql","intermail.statsitic.selectInterMailHistory1");
		String sql2 = QueryUtil.getStringQuery("intermail_sql","intermail.statsitic.selectInterMailHistory2");
		String sqlTail = QueryUtil.getStringQuery("intermail_sql","intermail.statsitic.selectInterMailHistoryTail");
		
		sql = sql1 +" tm_intermail_sendresult_"+resultYearMonth+" r " + sql2;
		//검색조건이 있다면
		
		if(intermailID!=0){
			sql += " AND r.intermailID = "+intermailID;
		}
		
		if(scheduleID!=0){
			sql += " AND r.scheduleID = "+scheduleID;
		}		
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :seach_text ";
		}		
		
		if(smtpCodeType!=null && !smtpCodeType.equals("")){
			if(smtpCodeType.equals("0")){
				sql += " AND r.smtpCodeType='0' ";   //메일성공
			}else{
				sql += " AND r.smtpCodeType<>'0' ";	//실패메일 
			}
		}
		
		if(openYN!=null && !openYN.equals("")){
			sql += " AND r.openYN = '"+openYN+"'";
		}
		
		if(openFileYN!=null && !openFileYN.equals("")){
			sql += " AND r.openFileYN = '"+openFileYN+"'";
		}		

		
		sql+=sqlTail;		
		
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				
				InterMailSendResult  interMailSendResult = new InterMailSendResult();
				interMailSendResult.setSendID(rs.getString("sendID"));
				interMailSendResult.setEmail(rs.getString("email"));
				interMailSendResult.setIntermailID(rs.getInt("intermailID"));
				interMailSendResult.setIntermailTitle(rs.getString("intermailTitle"));
				interMailSendResult.setScheduleID(rs.getInt("scheduleID"));
				interMailSendResult.setMailTitle(rs.getString("mailTitle"));
				interMailSendResult.setFileName(rs.getString("fileName"));
				interMailSendResult.setSmtpCodeType(rs.getString("smtpCodeType"));
				interMailSendResult.setSmtpCodeTypeDesc(ThunderUtil.descSmtpCodeType(rs.getString("smtpCodeType")));
				interMailSendResult.setSmtpCode(rs.getString("smtpCode"));
				interMailSendResult.setSmtpMsg(rs.getString("smtpMsg"));
				interMailSendResult.setFailCauseCode(rs.getString("failCauseCode"));
				interMailSendResult.setFailCauseCodeDesc(ThunderUtil.descFailCauseCodeType(rs.getString("failCauseCode")));
				interMailSendResult.setRetrySendCount(rs.getInt("retrySendCount"));
				interMailSendResult.setRetryLastDate(DateUtils.getStringDate(rs.getString("retryLastDate")));
				interMailSendResult.setOpenYN(rs.getString("openYN"));
				interMailSendResult.setOpenDate(DateUtils.getStringDate(rs.getString("openDate")));
				interMailSendResult.setOpenFileYN(rs.getString("openFileYN"));
				interMailSendResult.setOpenFileDate(DateUtils.getStringDate(rs.getString("openFileDate")));
				interMailSendResult.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				interMailSendResult.setFailCauseCodeName(rs.getString("failCauseCodeName"));
				interMailSendResult.setState(rs.getString("state"));
				interMailSendResult.setRetryFinishYN(rs.getString("retryFinishYN"));
				interMailSendResult.setRetryCount(rs.getInt("retryCount"));
				interMailSendResult.setStatisticEndDate(rs.getString("statisticEndDate"));
				
				return interMailSendResult;			

			}		
		};
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("seach_text", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	
	
	/**
	 * <p>발송결과 히스토리 총 카운트  
	 * @param paramsMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totaltInterMailHistory(Map<String,Object> paramsMap)  throws DataAccessException{
		String searchType = (String)paramsMap.get("searchType");
		String searchText = (String)paramsMap.get("searchText");
		String resultYearMonth = (String)paramsMap.get("resultYearMonth");
		String smtpCodeType = (String)paramsMap.get("smtpCodeType");
		String openYN = (String)paramsMap.get("openYN");
		String openFileYN = (String)paramsMap.get("openFileYN");
		int intermailID = Integer.parseInt(String.valueOf(paramsMap.get("intermailID")));
		int scheduleID = Integer.parseInt(String.valueOf(paramsMap.get("scheduleID")));
			
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.totaltInterMailHistory"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		sql = sql+" tm_intermail_sendresult_"+resultYearMonth+" WHERE 1=1 ";
		

		if(intermailID!=0){
			sql += " AND intermailID = "+intermailID;
		}
		
		if(scheduleID!=0){
			sql += " AND scheduleID = "+scheduleID;
		}		
		

		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
		}	

		
		if(smtpCodeType!=null && !smtpCodeType.equals("")){
			if(smtpCodeType.equals("0")){
				sql += " AND smtpCodeType='0' ";   //메일성공
			}else{
				sql += " AND smtpCodeType<>'0' ";	//실패메일 
			}
		}
		
		
		if(openYN!=null && !openYN.equals("")){
			sql += " AND openYN = '"+openYN+"'";
		}
		
		if(openFileYN!=null && !openFileYN.equals("")){
			sql += " AND openFileYN = '"+openFileYN+"'";
		}		

		Map<String,Object> param = new HashMap<String, Object>();
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql, param);
		
		
	}
	
	
	/**
	 * <p>발송결과자 상세보기 
	 * @param intermailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailSendResult viewInterMailSendResult(String yearMonth, int intermailID, int scheduleID, String sendID) throws DataAccessException{
		
		InterMailSendResult interMailSendResult = new InterMailSendResult();
		Map<String, Object> resultMap = null;
		
		String sql1 =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.viewInterMailSendResult1"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sql2 =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.viewInterMailSendResult2"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sql = sql1+" tm_intermail_sendresult_"+yearMonth+" "+sql2;
			//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("sendID", sendID);
			
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
			
		if(resultMap!=null){
			interMailSendResult.setIntermailID(intermailID);
			interMailSendResult.setScheduleID(scheduleID);
			interMailSendResult.setSendID((String)resultMap.get("sendID"));
			interMailSendResult.setEmail((String)resultMap.get("email"));
			interMailSendResult.setDomainName((String)resultMap.get("domainName"));
			interMailSendResult.setReceiverName(ThunderUtil.replaceOnetoone((String)resultMap.get("onetooneInfo"),(String)resultMap.get("receiverName")));
			interMailSendResult.setSenderName((String)resultMap.get("senderName"));
			interMailSendResult.setSenderMail((String)resultMap.get("senderMail"));
			interMailSendResult.setOnetooneInfo((String)resultMap.get("onetooneInfo"));
			interMailSendResult.setRepeatGroupType((String)resultMap.get("repeatGroupType"));
			
			interMailSendResult.setMailTitle(ThunderUtil.replaceOnetoone((String)resultMap.get("onetooneInfo"),(String)resultMap.get("mailTitle")));			
			interMailSendResult.setFileName((String)resultMap.get("fileName"));				
			
			//메일본문이나 전체에 반복그룹 사용이라면 
			if(interMailSendResult.getRepeatGroupType().equals(Constant.INTERMAIL_REPEAT_TYPE_CONTENT) || interMailSendResult.getRepeatGroupType().equals(Constant.INTERMAIL_REPEAT_TYPE_ALL)){
				interMailSendResult.setMailContent(ThunderUtil.replaceOnetooneRepeat((String)resultMap.get("onetooneInfo"),(String)resultMap.get("mailContent")));
			}else{
				interMailSendResult.setMailContent(ThunderUtil.replaceOnetoone((String)resultMap.get("onetooneInfo"),(String)resultMap.get("mailContent")));
			}
			//첨부파일이나 전체에 반복그룹 사용이라면 
			if(interMailSendResult.getRepeatGroupType().equals(Constant.INTERMAIL_REPEAT_TYPE_FILE) || interMailSendResult.getRepeatGroupType().equals(Constant.INTERMAIL_REPEAT_TYPE_ALL)){
				interMailSendResult.setFileContent(ThunderUtil.replaceOnetooneRepeat((String)resultMap.get("onetooneInfo"),(String)resultMap.get("fileContent")));
			}else{
				interMailSendResult.setFileContent(ThunderUtil.replaceOnetoone((String)resultMap.get("onetooneInfo"),(String)resultMap.get("fileContent")));
			}			
			
		}else{
			return interMailSendResult;
		}
		return interMailSendResult;
	}
	
	
	/**
	 * <p>오류자 재발송 상태값 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailScheduleRetryState(int intermailID, int scheduleID, String retryFinishYN)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.statistic.updateInterMailScheduleRetryState");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("retryFinishYN", retryFinishYN);		
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	/**
	 * <p>발송결과 오류자 상태값 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailResultRetryState(String sendID, int intermailID, int scheduleID, String wasRetrySended)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.statistic.updateInterMailResultRetryState");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("wasRetrySended", wasRetrySended);		
		return getSimpleJdbcTemplate().update(sql,param);	
	}
	
	/**
	 * <p>재발송을 위한 재발송완료 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRetryFinishSet(String retryFinishYN, int intermailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("intermail_sql","intermail.statistic.updateRetryFinishSet");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("retryFinishYN", retryFinishYN);
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));				
		return getSimpleJdbcTemplate().update(sql,param);	
	}
	
	/**
	 * <p>재발송을 위해 전체 실패자 업데이트 (영구적인 오류자는 제외)
	 * @param wasRetrySended
	 * @param intermailID
	 * @param scheduleID
	 * @param resultYearMonth
	 * @return
	 * @throws DataAccessException
	 */
	public int updateWasRetrySendedAllFailed(String wasRetrySended, int intermailID, int scheduleID, String resultYearMonth)  throws DataAccessException{
		
		
		String sql2 = QueryUtil.getStringQuery("intermail_sql","intermail.statistic.updateWasRetrySendedAllFailed");
		String sql = "UPDATE tm_intermail_sendresult_"+resultYearMonth+" "+sql2;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("wasRetrySended", wasRetrySended);
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));				
		return getSimpleJdbcTemplate().update(sql,param);	
	}
	
	
	
	/**
	 * <p>재발송을 위해 체크된  실패자 업데이트 (배치)
	 * @param wasRetrySended
	 * @param intermailID
	 * @param scheduleID
	 * @param resultYearMonth
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updateWasRetrySendedCheckedFailedBatch(String resultYearMonth, List<InterMailSendResult> interMailSendResultList)  throws DataAccessException{
	
		String sql = "UPDATE tm_intermail_sendresult_"+resultYearMonth;		
		String sqlSub =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.updateWasRetrySendedCheckedFailedBatch"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(interMailSendResultList.toArray());
		sql=sql+" "+sqlSub;
		
		return getSimpleJdbcTemplate().batchUpdate(sql,params);		
	}
	
	/**
	 * <p>재발송이 완료되었는 지 확인 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> checkRetryFinishYN(int intermailID, int scheduleID) throws DataAccessException{
		Map<String, Object> resultMap = null;	
		
		String sql =  QueryUtil.getStringQuery("intermail_sql","intermail.statistic.checkRetryFinishYN"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("intermailID", new Integer(intermailID));
		param.put("scheduleID", new Integer(scheduleID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
	
		return resultMap;
	}
	
}
	
	
