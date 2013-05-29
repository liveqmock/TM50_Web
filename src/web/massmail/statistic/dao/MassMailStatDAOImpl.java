package web.massmail.statistic.dao;


import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.common.dao.DBJdbcDaoSupport;
import web.massmail.statistic.model.*;
import web.massmail.write.model.MassMailList;
import web.massmail.write.model.OnetooneTarget;
import web.massmail.write.model.TargetingGroup;
import web.common.util.*;
import web.content.poll.model.PollQuestion;

import org.springframework.dao.EmptyResultDataAccessException;


public class MassMailStatDAOImpl extends DBJdbcDaoSupport implements MassMailStatDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TargetingGroup> listTargetingGroup(int massmailID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selecttargetgroup");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				TargetingGroup targetingGroup = new TargetingGroup();
				targetingGroup.setTargetGroupID(rs.getInt("targetGroupID"));
				targetingGroup.setTargetID(rs.getInt("targetID"));
				targetingGroup.setTargetName(rs.getString("targetName"));
				targetingGroup.setTargetCount(rs.getInt("targetCount"));
				targetingGroup.setTargetType(rs.getString("targetType"));
				targetingGroup.setMassmailID(rs.getInt("massmailID"));
				targetingGroup.setExceptYN(rs.getString("exceptYN"));
	
				return targetingGroup;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", new Integer(massmailID));

		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>대량메일 통계 기본정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo massMailStatisticBasicInfo(int massmailID, int scheduleID) throws DataAccessException{
		MassMailInfo massMailInfo = new MassMailInfo();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.basicinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			massMailInfo.setMassmailTitle((String)resultMap.get("massmailTitle"));
			massMailInfo.setSendType(ThunderUtil.descSendType((String)resultMap.get("sendType")));
			massMailInfo.setStatisticsOpenType((String)resultMap.get("statisticsOpenType"));
			massMailInfo.setRegistDate((String)resultMap.get("registDate"));
			massMailInfo.setMailLinkYN((String)resultMap.get("mailLinkYN"));
			massMailInfo.setUserName((String)resultMap.get("userName"));
			massMailInfo.setMailTitle((String)resultMap.get("mailTitle"));
			massMailInfo.setSenderMail((String)resultMap.get("senderMail"));
			massMailInfo.setSenderName((String)resultMap.get("senderName"));
			massMailInfo.setReturnMail((String)resultMap.get("returnMail"));
			massMailInfo.setSendScheduleDate(String.valueOf(resultMap.get("sendScheduleDate")));
			massMailInfo.setStatisticsEndDate(String.valueOf(resultMap.get("statisticsEndDate")));
			massMailInfo.setPrepareStartTime(String.valueOf(resultMap.get("prepareStartTime")));
			massMailInfo.setPrepareEndTime(String.valueOf((String)resultMap.get("prepareEndTime")));
			massMailInfo.setSendStartTime(String.valueOf(resultMap.get("sendStartTime")));
			massMailInfo.setSendEndTime(String.valueOf(resultMap.get("sendEndTime")));
			massMailInfo.setRetryCount(Integer.parseInt(String.valueOf(resultMap.get("retryCount"))));
			massMailInfo.setRetryStartTime(String.valueOf(resultMap.get("retryStartTime")));
			massMailInfo.setRetryEndTime(String.valueOf(resultMap.get("retryEndTime")));
			massMailInfo.setState((String)resultMap.get("state"));
			massMailInfo.setStateDesc(ThunderUtil.descState((String)resultMap.get("state")));
			massMailInfo.setRetryFinishYN((String)resultMap.get("retryFinishYN"));
			massMailInfo.setBackupYearMonth((String)resultMap.get("backupYearMonth"));
			massMailInfo.setSendedType((String)resultMap.get("sendedType"));
			massMailInfo.setSendedYear((String)resultMap.get("sendedYear"));
			massMailInfo.setSendedMonth((String)resultMap.get("sendedMonth"));
			massMailInfo.setSendedCount(Integer.parseInt(String.valueOf(resultMap.get("sendedCount"))));
			massMailInfo.setRejectType((String)resultMap.get("rejectType"));
			massMailInfo.setPersistErrorCount(Integer.parseInt(String.valueOf(resultMap.get("persistErrorCount"))));
			byte[] temp = String.valueOf(resultMap.get("mailContent")).getBytes();
			massMailInfo.setMailContentSize((float)(temp.length+800)/1024); //메일 헤더에 해당하는 값 800 더한다.
			if(massMailInfo.getState().equals("15")){
				try{
					int secondsBetween = DateUtils.secondsBetween(massMailInfo.getSendStartTime(), massMailInfo.getSendEndTime(), "yyyy-MM-dd HH:mm:ss");
					if(secondsBetween >= 3600){
						int sendTotalCount = Integer.parseInt(String.valueOf(resultMap.get("sendTotalCount")));
						massMailInfo.setAvgSendCount((sendTotalCount/secondsBetween)*3600);
					}else{
						massMailInfo.setAvgSendCount(0);
					}
				}catch(Exception e)
				{
					
				}
			}
			massMailInfo.setTargetTotalCount(Integer.parseInt(String.valueOf(resultMap.get("targetTotalCount"))));
			//massMailInfo.setHoursBetween(0);
		}
		
		return massMailInfo;
	}
	
	/**
	 * <p>대량메일 통계 발송정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo massMailStatisticSendInfo(int massmailID, int scheduleID) throws DataAccessException{
		MassMailInfo massMailInfo = new MassMailInfo();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.sendinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
	
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massMailInfo.setSendTotal(Integer.parseInt(String.valueOf(resultMap.get("sendTotal"))));
			massMailInfo.setSuccessTotal(Integer.parseInt(String.valueOf(resultMap.get("successTotal"))));
			massMailInfo.setFailTotal(Integer.parseInt(String.valueOf(resultMap.get("failTotal"))));
			massMailInfo.setOpenTotal(Integer.parseInt(String.valueOf(resultMap.get("openTotal"))));
			massMailInfo.setClickTotal(Integer.parseInt(String.valueOf(resultMap.get("clickTotal"))));
			massMailInfo.setRejectcallTotal(Integer.parseInt(String.valueOf(resultMap.get("rejectcallTotal"))));
		}
		
		return massMailInfo;
	}
	

	/**
	 * <p>대량메일 통계 필터정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailFilter massMailStatisticFilterInfo(int massmailID, int scheduleID) throws DataAccessException{
		MassMailFilter massMailFilter = new MassMailFilter();
		Map<String, Object> resultMap = null;
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.filterinfo");
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massMailFilter.setFilterType0(Integer.parseInt(String.valueOf(resultMap.get("filterType0"))));
			massMailFilter.setFilterType1(Integer.parseInt(String.valueOf(resultMap.get("filterType1"))));
			massMailFilter.setFilterType2(Integer.parseInt(String.valueOf(resultMap.get("filterType2"))));
			massMailFilter.setFilterType3(Integer.parseInt(String.valueOf(resultMap.get("filterType3"))));
			massMailFilter.setFilterType4(Integer.parseInt(String.valueOf(resultMap.get("filterType4"))));
			massMailFilter.setFilterType5(Integer.parseInt(String.valueOf(resultMap.get("filterType5"))));
			massMailFilter.setFilterType6(Integer.parseInt(String.valueOf(resultMap.get("filterType6"))));
			massMailFilter.setFilterType7(Integer.parseInt(String.valueOf(resultMap.get("filterType7"))));
			massMailFilter.setFilterTotal(Integer.parseInt(String.valueOf(resultMap.get("filterTotal"))));
		}
		return  massMailFilter;
	}

	/**
	 * <p>대량 메일 링크분석 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailLink> massMailStatisticLink(Map<String, Object> searchMap) throws DataAccessException{
		
		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.link"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailLink massMailLink = new MassMailLink();
				massMailLink.setLinkID(rs.getInt("linkID"));
				massMailLink.setScheduleID(rs.getInt("scheduleID"));
				massMailLink.setLinkName(rs.getString("linkName"));
				massMailLink.setLinkURL(rs.getString("linkURL"));
				massMailLink.setLinkCount(rs.getInt("linkCount"));
				massMailLink.setLinkDesc(rs.getString("linkDesc"));
				massMailLink.setLinkType(rs.getString("linkType"));
				return massMailLink;
			}
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);	
	}

	/**
	 * <p>대량 메일 시간별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatistic> massMailStatisticHourly(Map<String, Object> searchMap) throws DataAccessException{
		
		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.hourly"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatistic massMailStatistic = new MassMailStatistic();
				massMailStatistic.setStandard(rs.getString("hourDate").trim());
				massMailStatistic.setSendTotal(rs.getInt("sendCount"));
				massMailStatistic.setSuccessTotal(rs.getInt("successCount"));
				massMailStatistic.setFailTotal(rs.getInt("failCount"));
				massMailStatistic.setOpenTotal(rs.getInt("openCount"));
				massMailStatistic.setClickTotal(rs.getInt("clickCount"));
				massMailStatistic.setRejectcallTotal(rs.getInt("rejectcallCount"));
				massMailStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return massMailStatistic;
			}
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);	
	}
	
	/**
	 * <p>대량 메일 일자별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatistic> massMailStatisticDaily(Map<String, Object> searchMap) throws DataAccessException{
		
		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.daily"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));

		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatistic massMailStatistic = new MassMailStatistic();
				massMailStatistic.setStandard(rs.getString("dayDate"));
				massMailStatistic.setViewStandard(rs.getString("dayDate")+"일");
				massMailStatistic.setSendTotal(rs.getInt("sendCount"));
				massMailStatistic.setSuccessTotal(rs.getInt("successCount"));
				massMailStatistic.setFailTotal(rs.getInt("failCount"));
				massMailStatistic.setOpenTotal(rs.getInt("openCount"));
				massMailStatistic.setClickTotal(rs.getInt("clickCount"));
				massMailStatistic.setRejectcallTotal(rs.getInt("rejectcallCount"));
				massMailStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return massMailStatistic;
			}
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);	
	}
	
	/**
	 * <p>대량 메일 도메인별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatistic> massMailStatisticDomain(Map<String, Object> searchMap) throws DataAccessException{
		
		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.domain"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));

		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatistic massMailStatistic = new MassMailStatistic();
				massMailStatistic.setStandard(rs.getString("domainName"));
				massMailStatistic.setViewStandard(rs.getString("domainName"));
				massMailStatistic.setSendTotal(rs.getInt("sendTotal"));
				massMailStatistic.setSuccessTotal(rs.getInt("successTotal"));
				massMailStatistic.setFailTotal(rs.getInt("failTotal"));
				massMailStatistic.setOpenTotal(rs.getInt("openTotal"));
				massMailStatistic.setClickTotal(rs.getInt("clickTotal"));
				massMailStatistic.setRejectcallTotal(rs.getInt("rejectcallTotal"));
				massMailStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return massMailStatistic;
			}
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);	
	}
	
	/**
	 * <p>대량 메일 실패 도메인별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticDomainFail> massMailStatisticFailDomain(Map<String, Object> searchMap) throws DataAccessException{
		
		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.faildomain"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));

		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticDomainFail massMailStatisticDomainFail = new MassMailStatisticDomainFail();
				massMailStatisticDomainFail.setDomainName(rs.getString("domainName"));
				massMailStatisticDomainFail.setSendCount(rs.getInt("sendCount"));
				massMailStatisticDomainFail.setFailCount(rs.getInt("failCount"));
				massMailStatisticDomainFail.setFailcauseType1Count(rs.getInt("failcauseType1Count"));
				massMailStatisticDomainFail.setFailcauseType2Count(rs.getInt("failcauseType2Count"));
				massMailStatisticDomainFail.setFailcauseType3Count(rs.getInt("failcauseType3Count"));
				massMailStatisticDomainFail.setFailcauseType4Count(rs.getInt("failcauseType4Count"));
				return massMailStatisticDomainFail;
			}
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);	
	}
	
	/**
	 * <p>대량메일 실패원인별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticFailCause> massMailStatisticFailCause(Map<String, Object> searchMap) throws DataAccessException{

		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");

		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.failcause"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticFailCause failCauseStatistic = new MassMailStatisticFailCause();
					failCauseStatistic.setFailCauseTypeDes(rs.getString("failcauseTypeName"));
					failCauseStatistic.setFailCauseType(rs.getString("failCauseType"));
					failCauseStatistic.setFailCount(rs.getBigDecimal("failCount"));
					failCauseStatistic.setFailCauseDes(rs.getString("failcauseCodeName"));
					failCauseStatistic.setFailCauseCode(rs.getString("failcauseCode"));
				return failCauseStatistic;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>대량메일 결과 백업 테이블 정보 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String getBackupYearMonth(Map<String, Object> searchMap)  throws DataAccessException{
		
		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		
		Map<String, Object> resultMap = null;
		String result = "";
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.backupyearmonth");
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			result = (String)resultMap.get("backupYearMonth");
		}
		
		return result;
		
	}
	/**
	 * <p>대량메일 대상자 미리보기 총카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalMassMailPersonPreview(Map<String, Object> searchMap, String backupYearMonth) throws DataAccessException{
		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		String standard = (String)searchMap.get("standard");
		String type= (String)searchMap.get("type");
		String key= (String)searchMap.get("key");
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		int iTotalCnt = (Integer)searchMap.get("iTotalCnt");
		int start = (currentPage - 1) * countPerPage;
		if(iTotalCnt < countPerPage){
			countPerPage = iTotalCnt ;
		}
		if(iTotalCnt < (start + countPerPage)){
			countPerPage = iTotalCnt - start;
		}
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewcount");
		String tableName = " tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" ";
		if(backupYearMonth !=null && !backupYearMonth.equals("")){
			tableName = " tm_massmail_sendresult_"+backupYearMonth+" ";
		}
		
		sql += tableName;
		
		if(standard.equals("link")){
			 sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewlinktotal");	
		}else if(standard.equals("hourly")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlyopen");
			}else if(type.equals("click")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlyclick");
			}else if(type.equals("rejectcall")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlyrejectcall");
			}
		}else if(standard.equals("daily")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailyopen");
			}else if(type.equals("click")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailyclick");
			}else if(type.equals("rejectcall")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailyrejectcall");
			}
		}else if(standard.equals("domain")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomaintotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainopen");
			}else if(type.equals("click")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainclick");
			}else if(type.equals("rejectcall")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainrejectcall");
			}
		}else if(standard.equals("faildomain")){
			if(type.equals("total")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainfail");
			}else if(type.equals("1")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype1");
			}else if(type.equals("2")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype2");
			}else if(type.equals("3")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype3");
			}else if(type.equals("4")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype4");
			}else if(type.equals("5")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype5");
			}
		}else if(standard.equals("failcause")){
			sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfailcause");
		}else if(standard.equals("basic")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewtotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewopen");
			}else if(type.equals("click")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewclick");
			}else if(type.equals("rejectcall")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewrejectcall");
			}else{
				if(backupYearMonth !=null && !backupYearMonth.equals("")){
					tableName = " tm_massmail_filter_"+backupYearMonth+" ";
				}else{
					tableName = " tm_massmail_filter_"+massmailID+"_"+scheduleID;
				}
				sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewcount")+tableName+" "+QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewtotal");
				if(!type.equals("filterall")){
					sql += " AND filterType='"+type+"'";
				}
			}
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
		}
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		if(!standard.equals("basic")){
			if(standard.equals("hourly")||standard.equals("daily")){
				param.put("key",  new Integer(key));
			}else{
				param.put("key", key);
			}
		}
		param.put("searchText", "%"+searchText+"%");

		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	/**
	 * <p>대량메일 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailPersonPreview> massMailPersonPreview(Map<String, Object> searchMap, String backupYearMonth) throws DataAccessException{
		
		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		String standard = (String)searchMap.get("standard");
		final String type= (String)searchMap.get("type");
		final String key= (String)searchMap.get("key");
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		int iTotalCnt = (Integer)searchMap.get("iTotalCnt");
		int start = (currentPage - 1) * countPerPage;
		if(iTotalCnt < countPerPage){
			countPerPage = iTotalCnt ;
		}
		if(iTotalCnt < (start + countPerPage)){
			countPerPage = iTotalCnt - start;
		}
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewselect");
		String tableName = " tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" ";
		if(backupYearMonth !=null && !backupYearMonth.equals("")){
			tableName = " tm_massmail_sendresult_"+backupYearMonth+" ";
		}
		
		sql += tableName;
		
		if(standard.equals("link")){
			 sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewlink");	
		}else if(standard.equals("hourly")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlyopen");
			}else if(type.equals("click")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlyclick");
			}else if(type.equals("rejectcall")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewhourlyrejectcall");
			}
		}else if(standard.equals("daily")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailyfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailyopen");
			}else if(type.equals("click")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailyclick");
			}else if(type.equals("rejectcall")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdailyrejectcall");
			}
		}else if(standard.equals("domain")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomaintotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainopen");
			}else if(type.equals("click")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainclick");
			}else if(type.equals("rejectcall")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainrejectcall");
			}
		}else if(standard.equals("faildomain")){
			if(type.equals("total")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewdomainfail");
			}else if(type.equals("1")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype1");
			}else if(type.equals("2")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype2");
			}else if(type.equals("3")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype3");
			}else if(type.equals("4")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype4");
			}else if(type.equals("5")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfaildomaintype5");
			}
		}else if(standard.equals("failcause")){
			sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfailcause");
		}else if(standard.equals("basic")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewtotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfail");
			}else if(type.equals("open")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewopen");
			}else if(type.equals("click")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewclick");
			}else if(type.equals("rejectcall")){
				sql += QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewrejectcall");
			}else{
				if(backupYearMonth !=null && !backupYearMonth.equals("")){
					tableName = " tm_massmail_filter_"+backupYearMonth+" ";
				}else{
					tableName = " tm_massmail_filter_"+massmailID+"_"+scheduleID;
				}
				sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewfilter")
				    +tableName+" "+QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreviewtotal");
				if(!type.equals("filterall")){
					sql += " AND filterType='"+type+"'";
				}
			}
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
		}
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
		{
			String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.personpreview.tail");
			sql += sqlTail;
		}
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		if(!standard.equals("basic")){
			if(standard.equals("hourly")||standard.equals("daily")){
				param.put("key",  new Integer(key));
			}else{
				param.put("key", key);
			}
		}
		if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
		{
			param.put("start", new Integer(start));
			param.put("count", new Integer(countPerPage));
		}
		param.put("searchText", "%"+searchText+"%");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailPersonPreview massMailMailPersonPreview = new MassMailPersonPreview();
				massMailMailPersonPreview.setEmail(rs.getString("email"));
				massMailMailPersonPreview.setRegistDate(rs.getString("registDate"));
				massMailMailPersonPreview.setSmtpCode(rs.getString("smtpCode"));
				massMailMailPersonPreview.setSmtpMsg(rs.getString("smtpMsg"));
				if (type.equals("0")||type.equals("1")||type.equals("2")||type.equals("3")||type.equals("4")||type.equals("5")||type.equals("6")||type.equals("7")||type.equals("filterall")||type.equals("total")) {
					massMailMailPersonPreview.setOnetooneInfo("");
				} else {
					massMailMailPersonPreview.setOnetooneInfo(rs.getString("onetooneInfo"));
					massMailMailPersonPreview.setOpenDate(rs.getString("openDate"));
				}
				if(type.equals("total") && key.equals("basic"))
					massMailMailPersonPreview.setOnetooneInfo(rs.getString("onetooneInfo"));
				return massMailMailPersonPreview;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>대량메일 월간 보고서 - 총괄현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailReportMonth massMailReportMonthTotalInfo(String year, String month, String dateFrom, String dateTo, String userID,String groupID, String[] userInfo) throws DataAccessException{
		MassMailReportMonth massMailReportMonthTotalInfo = new MassMailReportMonth();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.reportmonth.totalinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//소속관리자라면  
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND U.groupID = '"+userInfo[2]+"'";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND U.userID = '"+userInfo[1]+"'"; 	
		}		
		if(!userID.trim().equals("")){
			sql += " AND U.userID IN ('"+StringUtil.replace(userID,",","','")+"') ";
		}
		if(!groupID.trim().equals("")){
			sql += " AND U.groupID = '"+groupID+"' ";
		}
		
		if(dateFrom.length()==1)
			dateFrom = "0"+dateFrom;
		if(dateTo.length()==1)
			dateTo = "0"+dateTo;
		if(month.length()==1)
			month = "0"+month;
		
		String fromDate = year+"-"+month+"-"+dateFrom + " 00:00:00";
		String toDate = year+"-"+month+"-"+dateTo + " 23:59:59";
		
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massMailReportMonthTotalInfo.setTotalCount(Integer.parseInt(String.valueOf(resultMap.get("totalCount"))));
			massMailReportMonthTotalInfo.setWriteCount(Integer.parseInt(String.valueOf(resultMap.get("writeCount"))));
			massMailReportMonthTotalInfo.setAppReadyCount(Integer.parseInt(String.valueOf(resultMap.get("appReadyCount"))));
			massMailReportMonthTotalInfo.setReadyCount(Integer.parseInt(String.valueOf(resultMap.get("readyCount"))));
			massMailReportMonthTotalInfo.setSendingCount(Integer.parseInt(String.valueOf(resultMap.get("sendingCount"))));
			massMailReportMonthTotalInfo.setSendFinishCount(Integer.parseInt(String.valueOf(resultMap.get("sendFinishCount"))));
			massMailReportMonthTotalInfo.setSendingStopCount(Integer.parseInt(String.valueOf(resultMap.get("sendingStopCount"))));

		}
		
		return massMailReportMonthTotalInfo;
	}
	
	
	/**
	 * <p>대량메일 월간 보고서 - Email 발송 현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailReportMonth massMailReportMonthSendInfo(String year, String month, String dateFrom, String dateTo, String userID, String groupID, String[] userInfo) throws DataAccessException{
		MassMailReportMonth massMailReportMonthTotalInfo = new MassMailReportMonth();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.reportmonth.sendinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//소속관리자라면  
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND U.groupID = '"+userInfo[2]+"'";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND U.userID = '"+userInfo[1]+"'"; 	
		}		
		
		if(!userID.trim().equals("")){
			sql += " AND U.userID IN ('"+StringUtil.replace(userID,",","','")+"') ";
		}
		if(!groupID.trim().equals("")){
			sql += " AND U.groupID = '"+groupID+"' ";
		}
		if(dateFrom.length()==1)
			dateFrom = "0"+dateFrom;
		if(dateTo.length()==1)
			dateTo = "0"+dateTo;
		if(month.length()==1)
			month = "0"+month;
		
		String fromDate = year+"-"+month+"-"+dateFrom + " 00:00:00";
		String toDate = year+"-"+month+"-"+dateTo + " 23:59:59";
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massMailReportMonthTotalInfo.setSendTotal(Integer.parseInt(String.valueOf(resultMap.get("sendTotal"))));
			massMailReportMonthTotalInfo.setSuccessTotal(Integer.parseInt(String.valueOf(resultMap.get("successTotal"))));
			massMailReportMonthTotalInfo.setFailTotal(Integer.parseInt(String.valueOf(resultMap.get("failTotal"))));
			massMailReportMonthTotalInfo.setOpenTotal(Integer.parseInt(String.valueOf(resultMap.get("openTotal"))));
			massMailReportMonthTotalInfo.setClickTotal(Integer.parseInt(String.valueOf(resultMap.get("clickTotal"))));
			massMailReportMonthTotalInfo.setRejectcallTotal(Integer.parseInt(String.valueOf(resultMap.get("rejectcallTotal"))));

		}
		
		return massMailReportMonthTotalInfo;
	}
	
	/**
	 * <p>대량메일 월간 보고서 - 도메인별 Email 발송 현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailReportMonth> massMailReportMonthDomainSendList(String year, String month, String dateFrom, String dateTo, String userID, String groupID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.reportmonth.domainsendinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		if(!userID.trim().equals("")){
			sql += " AND userID IN ('"+StringUtil.replace(userID,",","','")+"') ";
		}
		if(!groupID.trim().equals("")){
			sql += " AND groupID = '"+groupID+"' ";
		}
		
		if(dateFrom.length()==1)
			dateFrom = "0"+dateFrom;
		if(dateTo.length()==1)
			dateTo = "0"+dateTo;
		if(month.length()==1)
			month = "0"+month;
		
		String fromDate = year+"-"+month+"-"+dateFrom + " 00:00:00";
		String toDate = year+"-"+month+"-"+dateTo + " 23:59:59";
		
		sql +=  QueryUtil.getStringQuery("massmail_sql","massmail.reportmonth.domainsendinfogroupby");
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailReportMonth massMailReportMonthTotalInfo = new MassMailReportMonth();
				massMailReportMonthTotalInfo.setStandard(rs.getString("domainName"));
				massMailReportMonthTotalInfo.setSendTotal(rs.getInt("sendTotal"));
				massMailReportMonthTotalInfo.setSuccessTotal(rs.getInt("successTotal"));
				massMailReportMonthTotalInfo.setFailTotal(rs.getInt("failTotal"));
				massMailReportMonthTotalInfo.setOpenTotal(rs.getInt("openTotal"));
				massMailReportMonthTotalInfo.setClickTotal(rs.getInt("clickTotal"));
				massMailReportMonthTotalInfo.setRejectcallTotal(rs.getInt("rejectcallTotal"));
				
				
				return massMailReportMonthTotalInfo;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
			
	}
	
	/**
	 * <p>대량메일 월간 보고서 - 시간별 Email 발송 현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailReportMonth> massMailReportMonthTimeSendList(String year, String month, String dateFrom, String dateTo, String userID, String groupID) throws DataAccessException{
		
		if(dateFrom.length()==1)
			dateFrom = "0"+dateFrom;
		if(dateTo.length()==1)
			dateTo = "0"+dateTo;
		if(month.length()==1)
			month = "0"+month;
		
		String fromDate = year+"-"+month+"-"+dateFrom + " 00:00:00";
		String toDate = year+"-"+month+"-"+dateTo + " 23:59:59";
		
		//String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.reportmonth.timesendinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.reportmonth.timesendinfo"); 
		if(!userID.trim().equals("")){
			sql += " AND userID IN ('"+StringUtil.replace(userID,",","','")+"') ";
		}
		if(!groupID.trim().equals("")){
			sql += " AND groupID = '"+groupID+"' ";
		}
		sql += QueryUtil.getStringQuery("massmail_sql","massmail.reportmonth.timesendinfogroupby");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailReportMonth massMailReportMonthTotalInfo = new MassMailReportMonth();
				massMailReportMonthTotalInfo.setStandard(rs.getString("hourDate"));
				massMailReportMonthTotalInfo.setSendTotal(rs.getInt("sendCount"));
				massMailReportMonthTotalInfo.setSuccessTotal(rs.getInt("successCount"));
				massMailReportMonthTotalInfo.setFailTotal(rs.getInt("failCount"));
				massMailReportMonthTotalInfo.setOpenTotal(rs.getInt("openCount"));
				massMailReportMonthTotalInfo.setClickTotal(rs.getInt("clickCount"));
				massMailReportMonthTotalInfo.setRejectcallTotal(rs.getInt("rejectcallCount"));
				
				
				return massMailReportMonthTotalInfo;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
			
	}
	
	/**
	 * <p>대량메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailList> massMailReportMonthList(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String year = searchMap.get("year");
		String month = searchMap.get("month");
		String dateFrom = searchMap.get("dateFrom");
		String dateTo = searchMap.get("dateTo");
		String state = searchMap.get("state");
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.select");	
		
		if(dateFrom.length()==1)
			dateFrom = "0"+dateFrom;
		if(dateTo.length()==1)
			dateTo = "0"+dateTo;
		if(month.length()==1)
			month = "0"+month;
		
		String fromDate = year+"-"+month+"-"+dateFrom + " 00:00:00";
		String toDate = year+"-"+month+"-"+dateTo + " 23:59:59";
		
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		
		//소속관리자라면  
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND u.groupID = '"+userInfo[2]+"'";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND u.userID = '"+userInfo[1]+"'"; 	
		}		
		
		if(!userID.trim().equals("")){
			sql+= " AND u.userID IN ('"+StringUtil.replace(userID,",","','")+"')"; 	
		}
		if(!groupID.trim().equals("")){
			sql += " AND u.groupID = '"+groupID+"' ";
		}
		String sql_where = QueryUtil.getStringQuery("massmail_sql","massmail.reportmonth.where");	
		
		sql+=" AND "+sql_where+" ";
		
		if(!state.equals("")){
			sql+= " AND state IN ("+state+")";
		}
			
		String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.write.selecttail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassMailList massMailList = new MassMailList();
				
				massMailList.setMassmailID(rs.getInt("massmailID"));
				massMailList.setMassmailGroupID(rs.getInt("massmailGroupID"));
				massMailList.setMassmailTitle(rs.getString("massmailTitle"));
				massMailList.setScheduleID(rs.getInt("scheduleID"));
				massMailList.setSendStartTime(rs.getString("sendStartTime"));
				massMailList.setSendScheduleDate(DateUtils.getStringDate(rs.getString("sendScheduleDate")));
				massMailList.setSendEndTime(rs.getString("sendEndTime"));
				massMailList.setState(rs.getString("state"));
				massMailList.setSendType(rs.getString("sendType"));
				massMailList.setSendTypeDesc(ThunderUtil.descSendType(rs.getString("sendType")));
				massMailList.setTargetCount(rs.getInt("targetTotalCount"));
				massMailList.setSendCount(rs.getInt("sendTotalCount"));
				massMailList.setUserName(rs.getString("userName"));
				massMailList.setGroupName(rs.getString("groupName"));
				massMailList.setRegistDate(rs.getString("registDate"));
				
				return massMailList;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>(); 
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountMassMailReportMonthList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.totalcountmassmaillist");		
		
		String year = searchMap.get("year");
		String month = searchMap.get("month");
		String dateFrom = searchMap.get("dateFrom");
		String dateTo = searchMap.get("dateTo");
		String state = searchMap.get("state");
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");
		if(dateFrom.length()==1)
			dateFrom = "0"+dateFrom;
		if(dateTo.length()==1)
			dateTo = "0"+dateTo;
		if(month.length()==1)
			month = "0"+month;
		String fromDate = year+"-"+month+"-"+dateFrom + " 00:00:00";
		String toDate = year+"-"+month+"-"+dateTo + " 23:59:59";
		
		//sql += " AND s.sendScheduleDate >= '"+ sendScheduleDateStart +"' AND s.sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";

		
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		
		//소속관리자라면 
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND u.groupID = '"+userInfo[2]+"'";
		}
		
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND u.userID = '"+userInfo[1]+"'"; 	
		}
		if(!userID.trim().equals("")){
			sql+= " AND u.userID IN ('"+StringUtil.replace(userID,",","','")+"')"; 	
		}
		if(!groupID.trim().equals("")){
			sql += " AND u.groupID = '"+groupID+"' ";
		}
		
		String sql_where = QueryUtil.getStringQuery("massmail_sql","massmail.reportmonth.where");	
		
		sql+=" AND "+sql_where;
		
		if(!state.equals("")){
			sql+= " AND state IN ("+state+")";
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>계정별 통계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticUsers> massmailStatisticUsersList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException{
		String year = searchMap.get("year");
		String month = searchMap.get("month");
		String groupID = searchMap.get("groupID");
		String userID = searchMap.get("userID");
		
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.usersselect"); 
		String sqlGroup =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.usersgroupby");
		
		
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND E.groupID = '"+userInfo[2]+"'";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND C.userID = '"+userInfo[1]+"'"; 	
		}
		if(!userID.trim().equals("")){
			sql+= " AND C.userID IN ('"+StringUtil.replace(userID,",","','")+"') "; 	
		}
		if(!groupID.trim().equals("")){
			sql+= " AND E.groupID = '"+groupID+"' "; 	
		}
		sql = sql + sqlGroup;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticUsers massMailStatisticUsers = new MassMailStatisticUsers();
				massMailStatisticUsers.setUserID(rs.getString("userID"));
				massMailStatisticUsers.setUserName(rs.getString("userName"));
				massMailStatisticUsers.setGroupName(rs.getString("groupName"));
				massMailStatisticUsers.setSendTotal(rs.getInt("sendTotal"));
				massMailStatisticUsers.setSuccessTotal(rs.getInt("successTotal"));
				massMailStatisticUsers.setFailTotal(rs.getInt("failTotal"));
				massMailStatisticUsers.setOpenTotal(rs.getInt("openTotal"));
				massMailStatisticUsers.setClickTotal(rs.getInt("clickTotal"));
				massMailStatisticUsers.setSuccessRatio(StringUtil.getRatioToString(rs.getInt("successTotal"), rs.getInt("sendTotal")));
				
				
				return massMailStatisticUsers;
			}		
		};
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("year", new Integer(year));
		param.put("month", new Integer(month));
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>계정별 통계합계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticUsers> massmailStatisticUsersHap(String year, String month) throws DataAccessException{
		String sqlField = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.usersselectfield");
		String sqlTable = " FROM tm_massmail_sendresult_"+year+month +" r INNER JOIN tm_users u ON r.userID=u.userID INNER JOIN tm_usergroup g ON u.groupID=g.groupID ";	
	
		
		String sql = "SELECT " + sqlField + sqlTable;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticUsers massMailStatisticUsers = new MassMailStatisticUsers();
				massMailStatisticUsers.setUserID(rs.getString("userID"));
				massMailStatisticUsers.setUserName(rs.getString("userName"));
				massMailStatisticUsers.setGroupName(rs.getString("groupName"));
				massMailStatisticUsers.setSendTotal(rs.getInt("sendTotal"));
				massMailStatisticUsers.setSuccessTotal(rs.getInt("successTotal"));
				massMailStatisticUsers.setFailTotal(rs.getInt("failTotal"));
				massMailStatisticUsers.setOpenTotal(rs.getInt("openTotal"));
				massMailStatisticUsers.setClickTotal(rs.getInt("clickTotal"));
				massMailStatisticUsers.setSuccessRatio(StringUtil.getRatioToString(rs.getInt("successTotal"), rs.getInt("sendTotal")));
				
				
				return massMailStatisticUsers;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper);		
	}
	
	
	/**
	 * <p>그룹별 통계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticUsers> massmailStatisticUsersGroupList(String year, String month) throws DataAccessException{

		String sqlSel =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.groupselect"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sqlField = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.usersselectfield");
		String sqlTable = " FROM tm_massmail_sendresult_"+year+month +" r INNER JOIN tm_users u ON r.userID=u.userID INNER JOIN tm_usergroup g ON u.groupID=g.groupID ";	
		String sqlGroup =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.groupgroupby"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		
		String sql = sqlSel + sqlField + sqlTable + sqlGroup;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticUsers massMailStatisticUsers = new MassMailStatisticUsers();
				massMailStatisticUsers.setUserID(rs.getString("userID"));
				massMailStatisticUsers.setUserName(rs.getString("userName"));
				massMailStatisticUsers.setGroupName(rs.getString("groupName"));
				massMailStatisticUsers.setSendTotal(rs.getInt("sendTotal"));
				massMailStatisticUsers.setSuccessTotal(rs.getInt("successTotal"));
				massMailStatisticUsers.setFailTotal(rs.getInt("failTotal"));
				massMailStatisticUsers.setOpenTotal(rs.getInt("openTotal"));
				massMailStatisticUsers.setClickTotal(rs.getInt("clickTotal"));
				massMailStatisticUsers.setSuccessRatio(StringUtil.getRatioToString(rs.getInt("successTotal"), rs.getInt("sendTotal")));
				
				
				return massMailStatisticUsers;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper);		
	}
	
	/**
	 * <p>그룹별 통계합계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticUsers> massmailStatisticUsersGroupHap(String year, String month) throws DataAccessException{
		String sqlField = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.usersselectfield");
		String sqlTable = " FROM tm_massmail_sendresult_"+year+month +" r INNER JOIN tm_users u ON r.userID=u.userID INNER JOIN tm_usergroup g ON u.groupID=g.groupID ";	
		
		String sql = "SELECT " + sqlField + sqlTable;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticUsers massMailStatisticUsers = new MassMailStatisticUsers();
				massMailStatisticUsers.setUserID(rs.getString("userID"));
				massMailStatisticUsers.setUserName(rs.getString("userName"));
				massMailStatisticUsers.setGroupName(rs.getString("groupName"));
				massMailStatisticUsers.setSendTotal(rs.getInt("sendTotal"));
				massMailStatisticUsers.setSuccessTotal(rs.getInt("successTotal"));
				massMailStatisticUsers.setFailTotal(rs.getInt("failTotal"));
				massMailStatisticUsers.setOpenTotal(rs.getInt("openTotal"));
				massMailStatisticUsers.setClickTotal(rs.getInt("clickTotal"));
				massMailStatisticUsers.setSuccessRatio(StringUtil.getRatioToString(rs.getInt("successTotal"), rs.getInt("sendTotal")));
				
				
				return massMailStatisticUsers;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper);		
	}
	
	/**
	 * <p> 초기화면 - 관심 도메인 정보
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticDomain> massmailStatisticConcernedDomain () throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.concerneddomain"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticDomain massMailStatisticDomain = new MassMailStatisticDomain();
				massMailStatisticDomain.setDomainName(rs.getString("domainName"));
				massMailStatisticDomain.setSendTotal(rs.getInt("sendTotal"));
				massMailStatisticDomain.setSuccessTotal(rs.getInt("successTotal"));
				massMailStatisticDomain.setFailTotal(rs.getInt("failTotal"));
				massMailStatisticDomain.setSuccessRatio(rs.getBigDecimal("successRatio"));
					
				return massMailStatisticDomain;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper);		
	}

	/**
	 * <p>재발송을 요청한다. => tm_massmail_schedule에서 retryFinishYN='N' 로 업데이트 
	 * @param massmailID, scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRetryFinishYN (int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.updateretryfinishYN");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));	
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>실패원인 분석 - 재발송을 요청한다. => tm_massmail_sendresult에서 wasRetrySended = 'X' 로 업데이트 
	 * @param massmailID, scheduleID, failCauseCode
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRetrySended (int massmailID, int scheduleID, String failCauseCode) throws DataAccessException{
		String updateTable = "UPDATE tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" ";
		String sql = updateTable+QueryUtil.getStringQuery("massmail_sql","massmail.statistic.updateretrysended");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("failCauseCode", failCauseCode);	
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>실패도메인 분석 - 재발송을 요청한다. => tm_massmail_sendresult에서 wasRetrySended = 'X' 로 업데이트 
	 * @param massmailID, scheduleID, failCauseCode
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRetrySendedDomain (int massmailID, int scheduleID, String domainName) throws DataAccessException{
		String updateTable = "UPDATE tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" ";
		String sql = updateTable+QueryUtil.getStringQuery("massmail_sql","massmail.statistic.updateretrysendeddomain");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("domainName", domainName);	
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p><p>설문에 미응답한 대상자를 재발송한다.
	 * @param massmailID, scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRetrySendedPoil (int massmailID, int scheduleID) throws DataAccessException{
		String updateTable = "UPDATE tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" ";
		String sql = updateTable+QueryUtil.getStringQuery("massmail_sql","massmail.statistic.updateretrysendedpoll");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", massmailID);	
		param.put("scheduleID", scheduleID);
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>설문통계 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> selectPollStatistic(int pollID, int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.selectPollStatistic"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));			
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticPoll massMailStatisticPoll = new MassMailStatisticPoll();
				massMailStatisticPoll.setPollID(rs.getInt("pollID"));
				massMailStatisticPoll.setQuestionID(rs.getInt("questionID"));
				massMailStatisticPoll.setQuestionNo(rs.getInt("questionNo"));
				massMailStatisticPoll.setQuestionText(rs.getString("questionText"));
				massMailStatisticPoll.setQuestionType(rs.getString("questionType"));
				massMailStatisticPoll.setRequiredYN(rs.getString("requiredYN"));
				massMailStatisticPoll.setExampleType(rs.getString("exampleType"));
				massMailStatisticPoll.setExampleGubun(rs.getString("exampleGubun"));
				massMailStatisticPoll.setExampleMultiCount(rs.getInt("exampleMultiCount"));
				massMailStatisticPoll.setExampleID(rs.getInt("exampleID"));
				massMailStatisticPoll.setExampleDesc(rs.getString("exampleDesc"));
				massMailStatisticPoll.setResponseCount(rs.getInt("responseCount"));
				massMailStatisticPoll.setQuestionCount(rs.getInt("questionCount"));				
				massMailStatisticPoll.setResponseRatio(StringUtil.getRatioToString(rs.getInt("responseCount"), rs.getInt("questionCount")));
					
				return massMailStatisticPoll;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>대량메일에 해당하는 설문 아이디 가져오기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int getPollIDByMassMail(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.getPollIDByMassMail");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", new Integer(massmailID));		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>설문응답자 보기 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param questionID
	 * @param exampleID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailPersonPreview> massmailPollPreview(int currentPage, int countPerPage, Map<String, Object> searchMap) throws DataAccessException{
		
		
		int start = (currentPage - 1) * countPerPage ;
		countPerPage = countPerPage * currentPage;
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int massmailID = Integer.parseInt(String.valueOf(searchMap.get("massmailID")));
		int pollID = Integer.parseInt(String.valueOf(searchMap.get("pollID")));
		int scheduleID = Integer.parseInt(String.valueOf(searchMap.get("scheduleID")));
		int questionID = Integer.parseInt(String.valueOf(searchMap.get("questionID")));
		int exampleID = Integer.parseInt(String.valueOf(searchMap.get("exampleID")));
		String matrixX = (String)searchMap.get("matrixX");
		String matrixY = (String)searchMap.get("matrixY");
		int ranking = Integer.parseInt(String.valueOf(searchMap.get("ranking")));
	
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.massmailPollPreview"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
//		검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
		{
			String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.massmailPollPreviewTail");
			sql = sql +" "+sqlTail;
		}
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailPersonPreview massMailPersonPreview = new MassMailPersonPreview();
				massMailPersonPreview.setEmail(rs.getString("email"));		
				massMailPersonPreview.setResponseText(rs.getString("responseText"));
				massMailPersonPreview.setRegistDate(DateUtils.getDateSubstring(rs.getString("registDate")));					
				return massMailPersonPreview;
			}		
		};
		
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));			
		param.put("questionID", new Integer(questionID));
		param.put("exampleID", new Integer(exampleID));
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));	
		param.put("matrixX",matrixX);
		param.put("matrixY",matrixY);
		param.put("matrixY",matrixY);
		param.put("ranking", new Integer(ranking));
		param.put("searchText", "%"+searchText+"%");
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>설문응답자 수 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int massmailPollPreviewTotalCount(Map<String, Object> searchMap) throws DataAccessException{
		
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int massmailID = Integer.parseInt(String.valueOf(searchMap.get("massmailID")));
		int pollID = Integer.parseInt(String.valueOf(searchMap.get("pollID")));
		int scheduleID = Integer.parseInt(String.valueOf(searchMap.get("scheduleID")));
		int questionID = Integer.parseInt(String.valueOf(searchMap.get("questionID")));
		int exampleID = Integer.parseInt(String.valueOf(searchMap.get("exampleID")));
		String matrixX = (String)searchMap.get("matrixX");
		String matrixY = (String)searchMap.get("matrixY");
		int ranking = Integer.parseInt(String.valueOf(searchMap.get("ranking")));
	
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.massmailPollPreviewTotalCount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.	
		
				
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));			
		param.put("questionID", new Integer(questionID));
		param.put("exampleID", new Integer(exampleID));
		param.put("matrixX",matrixX);
		param.put("matrixY",matrixY);
		param.put("ranking", new Integer(ranking));
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
	
	
	/**
	 * <p>설문통계
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */	
public Map<String,Object> massmailPollStatisticCount(int massmailID, int scheduleID, int pollID) throws DataAccessException{
		
		Map<String, Object> resultMap = null;	
		String sql1 = "SELECT (SELECT COUNT(*) FROM tm_massmail_sendresult_"+massmailID+"_"+scheduleID+") sendTotalCount, ";
		String sql2= QueryUtil.getStringQuery("massmail_sql","massmail.statistic.massmailPollStatisticCount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sql = sql1+" "+sql2;
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("pollID", new Integer(pollID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
	
		return resultMap;
		
	}
	
	
	/**
	 * <p>설문통계(통계수집완료시)
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> massmailPollStatisticCountFinish(String yearMonth, int massmailID, int scheduleID, int pollID) throws DataAccessException{
		Map<String, Object> resultMap = null;	
		String sql1 = "SELECT (SELECT COUNT(*) FROM tm_massmail_sendresult_"+yearMonth+" WHERE massmailID="+massmailID+" AND scheduleID="+scheduleID+") sendTotalCount, ";
		String sql2= QueryUtil.getStringQuery("massmail_sql","massmail.statistic.massmailPollStatisticCount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sql = sql1+" "+sql2;
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("pollID", new Integer(pollID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
	
		return resultMap;
	}
	
	
	/**
	 * <p>설문모든 응답자들 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailPersonPreview> massmailPollAllResponse(int currentPage, int countPerPage, Map<String, Object> searchMap) throws DataAccessException{
		
		
		int start = (currentPage - 1) * countPerPage ;
		//countPerPage = countPerPage * currentPage;
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int massmailID = Integer.parseInt(String.valueOf(searchMap.get("massmailID")));
		int pollID = Integer.parseInt(String.valueOf(searchMap.get("pollID")));
		int scheduleID = Integer.parseInt(String.valueOf(searchMap.get("scheduleID")));
		
	
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.massmailPollAllResponse"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.	
		
				
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
			
		
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
		{
			String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.massmailPollAllResponseTail");
			sql = sql +" "+sqlTail;			
		}

			
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailPersonPreview massMailPersonPreview = new MassMailPersonPreview();
				if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
					massMailPersonPreview.setSendID(rs.getInt("sendID"));
				massMailPersonPreview.setEmail(rs.getString("email"));				
				massMailPersonPreview.setRegistDate(DateUtils.getDateSubstring(rs.getString("registDate")));					
				return massMailPersonPreview;
			}		
		};
		
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("searchText", "%"+searchText+"%");
		if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
		{
			param.put("start", new Integer(start));
			param.put("count", new Integer(countPerPage));
		}
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>설문모든 미응답자들 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailPersonPreview> massmailPollAllNotResponse(int currentPage, int countPerPage, Map<String, Object> searchMap, String backupYearMonth) throws DataAccessException{
		
		
		int start = (currentPage - 1) * countPerPage ;
		//countPerPage = countPerPage * currentPage;
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int massmailID = Integer.parseInt(String.valueOf(searchMap.get("massmailID")));
		int pollID = Integer.parseInt(String.valueOf(searchMap.get("pollID")));
		int scheduleID = Integer.parseInt(String.valueOf(searchMap.get("scheduleID")));
		
		
		String sql = "SELECT email , SUBSTRING(registDate,1,10) registDate FROM";
		if (db_type.equals(DB_TYPE_MSSQL)) {
			 sql = "SELECT * FROM ( SELECT ROW_NUMBER() OVER(ORDER BY registDate ) AS RN, email , SUBSTRING(convert(varchar,registDate,23),1,10) registDate FROM";
		} else if (db_type.equals(DB_TYPE_ORACLE)) {
			sql = "SELECT email , SUBSTR(registDate,1,10) registDate FROM";
		}
		String tableName = " tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" ";
		if(backupYearMonth !=null && !backupYearMonth.equals("")){
			tableName = " tm_massmail_sendresult_"+backupYearMonth+" ";
		}
		
		sql += tableName;
		
		String sql2 = QueryUtil.getStringQuery("massmail_sql", "massmail.statistic.massmailPollAllNotResponse");
		
		sql += sql2;
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}		
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
		{
			String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.massmailPollAllNotResponseTail");
			sql = sql +" "+sqlTail;			
		}		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailPersonPreview massMailPersonPreview = new MassMailPersonPreview();				
				massMailPersonPreview.setEmail(rs.getString("email"));				
				massMailPersonPreview.setRegistDate(DateUtils.getDateSubstring(rs.getString("registDate")));					
				return massMailPersonPreview;
			}		
		};
		
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("searchText", "%"+searchText+"%");
		if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
		{
			param.put("start", new Integer(start));
			param.put("count", new Integer(countPerPage));
		}
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>설문전체응답자 수 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int massmailPollAllResponseTotalCount(Map<String, Object> searchMap) throws DataAccessException{
		
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int massmailID = Integer.parseInt(String.valueOf(searchMap.get("massmailID")));
		int scheduleID = Integer.parseInt(String.valueOf(searchMap.get("scheduleID")));
		int pollID = Integer.parseInt(String.valueOf(searchMap.get("pollID")));
	
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.massmailPollAllResponseTotalCount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.	
		
				
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));		
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
	
	
	/**
	 * <p>설문응답상세보기 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> viewDetailAnswer(int pollID, int massmailID, int scheduleID, int sendID, String matrixX, String matrixY) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.viewDetailAnswer"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("sendID", new Integer(sendID));
		param.put("matrixX", matrixX);
		param.put("matrixY", matrixY);
		
		//q.pollID, q.questionID, q.questionNo, q.questionText, q.questionType, q.requiredYN, q.exampleType, q.exampleGubun, 
		//q.exampleMultiCount ,e.exampleID , e.exampleDesc, a.exampleID responseExampleID, a.responseText 
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticPoll massMailStatisticPoll = new MassMailStatisticPoll();
				massMailStatisticPoll.setPollID(rs.getInt("pollID"));
				massMailStatisticPoll.setQuestionID(rs.getInt("questionID"));
				massMailStatisticPoll.setQuestionNo(rs.getInt("questionNo"));
				massMailStatisticPoll.setQuestionText(rs.getString("questionText"));
				massMailStatisticPoll.setQuestionType(rs.getString("questionType"));
				massMailStatisticPoll.setRequiredYN(rs.getString("requiredYN"));
				massMailStatisticPoll.setExampleType(rs.getString("exampleType"));
				massMailStatisticPoll.setExampleGubun(rs.getString("exampleGubun"));
				massMailStatisticPoll.setExampleMultiCount(rs.getInt("exampleMultiCount"));
				massMailStatisticPoll.setExampleID(rs.getInt("exampleID"));
				massMailStatisticPoll.setExampleDesc(rs.getString("exampleDesc"));
				massMailStatisticPoll.setResponseExampleID(rs.getInt("responseExampleID"));
				massMailStatisticPoll.setResponseText(rs.getString("responseText"));
				
					
				return massMailStatisticPoll;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>설문에 해당하는 설문 문항들을 가져온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollQuestion> selectQuestionByPollID(int pollID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.selectQuestionByPollID"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		//questionID, questionNo, questionType, questionText, exampleType, exampleGubub, exampleMultiCount, requiredYN
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				PollQuestion pollQuestion = new PollQuestion();
				pollQuestion.setPollID(rs.getInt("pollID"));
				pollQuestion.setQuestionID(rs.getInt("questionID"));
				pollQuestion.setQuestionNo(rs.getInt("questionNo"));
				pollQuestion.setQuestionType(rs.getString("questionType"));	
				pollQuestion.setQuestionText(rs.getString("questionText"));
				pollQuestion.setExampleType(rs.getString("exampleType"));
				pollQuestion.setExampleGubun(rs.getString("exampleGubun"));
				pollQuestion.setExampleMultiCount(rs.getInt("exampleMultiCount"));
				pollQuestion.setRequiredYN(rs.getString("requiredYN"));
		
				return pollQuestion;
			}		
		};
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>해당 설문ID에 해당하는 보기와 응답수를 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> selectPollStatisticByQuestionID(int massmailID, int scheduleID, int pollID, int questionID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.selectPollStatisticByQuestionID"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		// e.exampleID, e.exampleDesc, e.exampleExYN , responseCount
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticPoll massMailStatisticPoll = new MassMailStatisticPoll();
				massMailStatisticPoll.setExampleID(rs.getInt("exampleID"));
				massMailStatisticPoll.setExampleDesc(rs.getString("exampleDesc"));
				massMailStatisticPoll.setExampleExYN(rs.getString("exampleExYN"));
				massMailStatisticPoll.setResponseCount(rs.getInt("responseCount"));			
				return massMailStatisticPoll;
			}		
		};
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("pollID", new Integer(pollID));
		param.put("questionID", new Integer(questionID));
		
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	/**
	 * <p>순위선택 응답수를 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> selectPollStatisticByExampleType3(int massmailID, int scheduleID, int pollID, int questionID, int selectCount) throws DataAccessException{
		String sql = "SELECT e.exampleID, e.exampleDesc, e.exampleExYN ";
		for(int i=1;i <= selectCount;i++){
			sql+= " , SUM(CASE WHEN a.exampleID=e.exampleID and ranking="+i+" THEN 1 ELSE 0 END) responseCount"+i;
		}
		sql += " FROM tm_poll_example e LEFT OUTER JOIN tm_poll_answer a "
			+  " ON e.pollID=a.pollID AND e.questionID=a.questionID "  
            +  " WHERE a.massmailID="+massmailID+" AND a.scheduleID="+scheduleID+" AND a.pollID="+pollID+" AND a.questionID="+questionID
            +  " GROUP BY e.questionID, e.exampleID, e.exampleDesc, e.exampleExYN ORDER BY e.exampleID ";	
	    final int arryCount = selectCount;

		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticPoll massMailStatisticPoll = new MassMailStatisticPoll();
				massMailStatisticPoll.setExampleID(rs.getInt("exampleID"));
				massMailStatisticPoll.setExampleDesc(rs.getString("exampleDesc"));
				massMailStatisticPoll.setExampleExYN(rs.getString("exampleExYN"));
				//massMailStatisticPoll.setResponseCount(rs.getInt("responseCount"));
				String[] result = new String[arryCount];
				for(int i=0;i < arryCount;i++){
					result[i] = rs.getString("responseCount"+(i+1));
				}
				massMailStatisticPoll.setResponseCountArry(result);
				return massMailStatisticPoll;
			}		
		};
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("pollID", new Integer(pollID));
		param.put("questionID", new Integer(questionID));
		
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	/**
	 * <p>세로(Y축) 보기를 가져온다.
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> selectPollExampleMatrixY(int pollID, int questionID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.selectPollExampleMatrixY"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		// exampleID, exampleDesc	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticPoll massMailStatisticPoll = new MassMailStatisticPoll();
				massMailStatisticPoll.setExampleID(rs.getInt("exampleID"));
				massMailStatisticPoll.setExampleDesc(rs.getString("exampleDesc"));
				return massMailStatisticPoll;
			}		
		};
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("questionID", new Integer(questionID));
		
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>매트릭스 응답통계를 가져온다.
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> selectPollAnswerMatrixXY(int massmailID, int scheduleID, int pollID, int questionID) throws DataAccessException{
		final List<MassMailStatisticPoll> massMailStatisticPollList = selectPollExampleMatrixY(pollID, questionID);
		String sql1 = "SELECT  e.exampleID, e.exampleDesc ";
		String sql2 = "";
		
		for(int i=0;i<massMailStatisticPollList.size();i++){
			sql2+=", SUM(CASE WHEN a.matrixY="+massMailStatisticPollList.get(i).getExampleID()+" THEN 1 ELSE 0 END) Y"+i;
		}
		String sql3 = " FROM tm_poll_example e INNER JOIN tm_poll_answer a "
						+"	ON e.pollID=a.pollID AND e.questionID=a.questionID AND e.exampleID=a.exampleID WHERE a.massmailID="+massmailID+"" 
						+" AND a.scheduleID="+scheduleID+" AND a.pollID="+pollID+" AND a.questionID="+questionID+" AND e.matrixXY='X' "
						+" GROUP BY e.exampleID, e.exampleDesc, e.matrixXY ORDER BY e.exampleID";
		
		String sql = sql1+sql2+sql3;
		
		//System.out.println(sql);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticPoll massMailStatisticPoll = new MassMailStatisticPoll();
				massMailStatisticPoll.setExampleID(rs.getInt("exampleID"));
				massMailStatisticPoll.setExampleDesc(rs.getString("exampleDesc"));
				for(int i=0;i<massMailStatisticPollList.size();i++){
					massMailStatisticPoll.arrayListValue.add(rs.getInt("Y"+i));
				}
				return massMailStatisticPoll;
			}		
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	/**
	 * <p>설문에 해당하는 문항정보보기 
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailStatisticPoll viewQuestion(int pollID, int questionID) throws DataAccessException{
		
		//questionNo, questionID, questionType, questionText
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.viewQuestion"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("pollID", new Integer(pollID));	
		param.put("questionID", new Integer(questionID));
		
		MassMailStatisticPoll  massMailStatisticPoll = new MassMailStatisticPoll();
		
		Map<String, Object> resultMap = null;
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){			
			massMailStatisticPoll.setQuestionNo(Integer.parseInt(String.valueOf(resultMap.get("questionNo"))));
			massMailStatisticPoll.setQuestionID(Integer.parseInt(String.valueOf(resultMap.get("questionID"))));
			massMailStatisticPoll.setQuestionType((String)resultMap.get("questionType"));
			massMailStatisticPoll.setQuestionText((String)resultMap.get("questionText"));			
		}
		
		return massMailStatisticPoll;
	}
	
	/**
	 * <p>설문 개인별 응답 현황 
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> selectPollIndividualStatistic(int massmailID, int scheduleID, String backupYearMonth) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.selectPollIndividualStatisticSelect"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String sqlWhere = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.selectPollIndividualStatisticWhere"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		String tableName = ", tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" D ";
		if(backupYearMonth !=null && !backupYearMonth.equals("")){
			tableName = ", tm_massmail_sendresult_"+backupYearMonth+" D ";
		}
		
		sql = sql+tableName+sqlWhere;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));			
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticPoll massMailStatisticPoll = new MassMailStatisticPoll();
				massMailStatisticPoll.setEmail(rs.getString("email"));
				massMailStatisticPoll.setName(ThunderUtil.getOnetooneValue(rs.getString("onetooneInfo"), Constant.ONETOONE_NAME_TAG));
				massMailStatisticPoll.setQuestionID(rs.getInt("questionID"));
				massMailStatisticPoll.setQuestionText(rs.getString("questionText"));
				massMailStatisticPoll.setExampleID(rs.getInt("exampleID"));
				massMailStatisticPoll.setExampleDesc(rs.getString("exampleDesc"));
				massMailStatisticPoll.setQuestionType(rs.getString("questionType"));
				massMailStatisticPoll.setExampleType(rs.getString("exampleType"));
				massMailStatisticPoll.setResponseText(rs.getString("responseText"));
				massMailStatisticPoll.setExampleDesc_responseText(rs.getString("exampleDesc_responseText"));
				
				return massMailStatisticPoll;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>대량 메일 대상자별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatistic> massMailStatisticTarget(Map<String, Object> searchMap) throws DataAccessException{
		
		int massmailID = (Integer)searchMap.get("massmailID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.target"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));

		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatistic massMailStatistic = new MassMailStatistic();
				massMailStatistic.setStandard(rs.getString("targetName"));
				massMailStatistic.setViewStandard(rs.getString("targetName"));
				massMailStatistic.setSendTotal(rs.getInt("sendTotal"));
				massMailStatistic.setSuccessTotal(rs.getInt("successTotal"));
				massMailStatistic.setFailTotal(rs.getInt("failTotal"));
				massMailStatistic.setOpenTotal(rs.getInt("openTotal"));
				massMailStatistic.setClickTotal(rs.getInt("clickTotal"));
				massMailStatistic.setRejectcallTotal(rs.getInt("rejectcallTotal"));
				massMailStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return massMailStatistic;
			}
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);	
	}
	
	/**
	 * <p>타겟ID에 해당되는 원투원 정보 
	 * @param targetIDs
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<OnetooneTarget> selectOnetooneByTargetID(String[] targetIDs, int massmailID) throws DataAccessException{
	
		String sql = "";		
		String sqlfrom = "";
		String sqlwhere = "";
		
		for(int i=0;i<targetIDs.length;i++){
			if(db_type.equals(DB_TYPE_ORACLE))
				sqlfrom += "("+QueryUtil.getStringQuery("massmail_sql","massmail.statistic.selectonetoone")+" AND t.targetID ="+targetIDs[i]+") id"+targetIDs[i];
			else if(db_type.equals(DB_TYPE_MYSQL) || db_type.equals(DB_TYPE_MSSQL))
				sqlfrom += "("+QueryUtil.getStringQuery("massmail_sql","massmail.statistic.selectonetoone")+" AND t.targetID ="+targetIDs[i]+") AS id"+targetIDs[i];
			if(targetIDs.length > 1){
				if(i==0 ){
					sql = "SELECT id"+targetIDs[i]+".* FROM ";
					sqlwhere = " WHERE "+"id"+targetIDs[i]+".onetooneAlias = ";
					sqlfrom += ", ";
				}else if(i == targetIDs.length-1 && targetIDs.length > 0){
					sqlwhere +="id"+targetIDs[i]+".onetooneAlias ";
				}else{
					sqlwhere +="id"+targetIDs[i]+".onetooneAlias AND id"+targetIDs[i]+".onetooneAlias = "; 
					sqlfrom += ", ";
				}
			}else{
				sql = "SELECT id"+targetIDs[i]+".* FROM ";			
			}
		}
		
		sql = sql + sqlfrom + sqlwhere;
		
		String[] temp = sql.split(" INNER JOIN ");
		if(temp.length > 30 )
			sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.selectonetoone")+ " group by o.onetooneAlias " ; 
			
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				OnetooneTarget onetooneTarget = new OnetooneTarget();
				onetooneTarget.setOnetooneName(rs.getString("onetooneName"));
				onetooneTarget.setOnetooneAlias(rs.getString("onetooneAlias"));
				return onetooneTarget;
			}			
		};		
	
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * 대량메일에ID에 해당되는 타겟ID 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<String> getTargetIDs(int massmailID) throws DataAccessException{
				
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.statistic.targetids"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				String result = rs.getString("targetID");
				return result;
			}
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);	
	}
	
	/**
	 * <p>설문통계( 해당 메일에 사용 된 설문의 종료기준, 목표응답수, 설문마감일 표시) 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectPollInfo(int pollID) throws DataAccessException{	
		
		Map<String, Object> resultMap = null;
		String sql = QueryUtil.getStringQuery("massmail_sql", "massmail.statistic.selectPollInfo");
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID",pollID);
		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		return resultMap;
		
	}
	
	/**
	 * <p>해당 설문조사 타이틀 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public String selectPollTitle(int pollID){
				
		Map<String, Object> resultMap = null;
		String result = "";
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.pollTitle");
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", pollID);
		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			result = (String)resultMap.get("pollTitle");
		}
		
		return result; 
	}
	
	
	/**
	 * <p>각 문항당 보기의 갯 수 
	 * @param pollID, questionID
	 * @return 
	 * @throws DataAccessException
	 */
	public int exampleCount (int pollID, int questionID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.exampleCount");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", pollID);
		param.put("questionID", questionID);	
		return getSimpleJdbcTemplate().queryForInt(sql, param);		
	}
	
	
	/**
	 * <p>문항 별 보기 내용
	 * @param pollID
	 * @param questionID
	 * @param exampleID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public String selectExampleDesc(int pollID,int questionID,int exampleID){
				
		Map<String, Object> resultMap = null;
		String result = "";
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.exampleDesc");
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", pollID);
		param.put("questionID", questionID);
		param.put("exampleID", exampleID);
		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			result = (String)resultMap.get("exampleDesc");
		}
		
		return result; 
	}
	
	
	/**
	 * <p>주관식 답안 목록 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> responseTextAll(int pollID, int massmailID, int scheduleID, int questionID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.responseTextAll"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));			
		param.put("questionID", new Integer(questionID));			
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailStatisticPoll massMailStatisticPoll = new MassMailStatisticPoll();
				massMailStatisticPoll.setResponseText(rs.getString("responseText"));				
					
				return massMailStatisticPoll;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	
	/**
	 * <p>객관식 문항  주관식 답안 갯수(문제마다 포함된 갯수) 
	 * @param pollID, questionID
	 * @return 
	 * @throws DataAccessException
	 */
	public int exampleExYNcount (int pollID, int questionID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.exampleExYNcount");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", pollID);
		param.put("questionID", questionID);	
		return getSimpleJdbcTemplate().queryForInt(sql, param);		
	}
	
	
	/**
	 * <p>객관식 문항  주관식 답안 갯수(설문내에 포함된 갯수) 
	 * @param pollID
	 * @return 
	 * @throws DataAccessException
	 */
	public int pollExampleExYNcount (int pollID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.pollExampleExYNcount");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", pollID);	
		return getSimpleJdbcTemplate().queryForInt(sql, param);		
	}
	
	/**
	 * <p>설문 내 주관식 문항 갯 수
	 * @param pollID
	 * @return 
	 * @throws DataAccessException
	 */
	public int exampleTypeCount (int pollID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.exampleTypeCount");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", pollID);	
		return getSimpleJdbcTemplate().queryForInt(sql, param);		
	}
	
	
	/**
	 * <p>설문 내 보기 총 갯 수(셀 총 갯 수 - 여백설정을 위한 카운트)
	 * @param pollID
	 * @return 
	 * @throws DataAccessException
	 */
	public int exampleAllCount (int pollID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.statistic.exampleAllCount");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", pollID);	
		return getSimpleJdbcTemplate().queryForInt(sql, param);		
	}
	
	/**
	 * <p>설문전체 미응답자 수 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int massmailPollAllNotResponseTotalCount(Map<String, Object> searchMap, String backupYearMonth) throws DataAccessException{
		
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int massmailID = Integer.parseInt(String.valueOf(searchMap.get("massmailID")));
		int scheduleID = Integer.parseInt(String.valueOf(searchMap.get("scheduleID")));
		int pollID = Integer.parseInt(String.valueOf(searchMap.get("pollID")));
		
		String sql = "SELECT COUNT(DISTINCT(email)) FROM";
		
		String tableName = " tm_massmail_sendresult_"+massmailID+"_"+scheduleID+" ";
		if(backupYearMonth !=null && !backupYearMonth.equals("")){
			tableName = " tm_massmail_sendresult_"+backupYearMonth+" ";
		}
		
		sql += tableName;
		
		String sql2 = QueryUtil.getStringQuery("massmail_sql", "massmail.statistic.massmailPollAllNotResponseTotalCount");
		
		sql += sql2;		
				
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));		
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
}
