package web.masssms.statistic.dao;


import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.common.dao.DBJdbcDaoSupport;
import web.masssms.statistic.model.*;
import web.masssms.write.model.MassSMSList;
import web.masssms.write.model.SMSTargetingGroup;
import web.common.util.*;

import org.springframework.dao.EmptyResultDataAccessException;


public class MassSMSStatDAOImpl extends DBJdbcDaoSupport implements MassSMSStatDAO{
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<SMSTargetingGroup> listTargetingGroup(int masssmsID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listTargetingGroup");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				SMSTargetingGroup targetingGroup = new SMSTargetingGroup();
				targetingGroup.setTargetGroupID(rs.getInt("targetGroupID"));
				targetingGroup.setTargetID(rs.getInt("targetID"));
				targetingGroup.setTargetName(rs.getString("targetName"));
				targetingGroup.setTargetCount(rs.getInt("targetCount"));
				targetingGroup.setTargetType(rs.getString("targetType"));
				targetingGroup.setMasssmsID(rs.getInt("masssmsID"));
				targetingGroup.setExceptYN(rs.getString("exceptYN"));
	
				return targetingGroup;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("masssmsID", new Integer(masssmsID));

		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>대량sms 통계 발송정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo massSMSStatisticSendInfo(int masssmsID, int scheduleID) throws DataAccessException{
		MassSMSInfo massSMSInfo = new MassSMSInfo();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.statistic.massSMSStatisticSendInfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		param.put("scheduleID", new Integer(scheduleID));
	
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massSMSInfo.setSendTotal(Integer.parseInt(String.valueOf(resultMap.get("sendTotal"))));
			massSMSInfo.setSuccessTotal(Integer.parseInt(String.valueOf(resultMap.get("successTotal"))));
			massSMSInfo.setFailTotal(Integer.parseInt(String.valueOf(resultMap.get("failTotal"))));
			massSMSInfo.setReadyTotal(Integer.parseInt(String.valueOf(resultMap.get("ReadyTotal"))));
		}
		
		return massSMSInfo;
	}
	
	
	
	/**
	 * <p>대량SMS 통계 기본정보
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo massSMSStatisticBasicInfo(int masssmsID, int scheduleID) throws DataAccessException{
		MassSMSInfo massSMSInfo = new MassSMSInfo();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.statistic.massSMSStatisticBasicInfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		param.put("scheduleID", new Integer(scheduleID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			massSMSInfo.setMasssmsTitle((String)resultMap.get("masssmsTitle"));
			massSMSInfo.setSendType(ThunderUtil.descSendType((String)resultMap.get("sendType")));
			massSMSInfo.setStatisticsOpenType((String)resultMap.get("statisticsOpenType"));
			massSMSInfo.setRegistDate((String)resultMap.get("registDate"));
			massSMSInfo.setUserName((String)resultMap.get("userName"));			
			massSMSInfo.setSenderPhone((String)resultMap.get("senderPhone"));
			massSMSInfo.setSendScheduleDate(String.valueOf(resultMap.get("sendScheduleDate")));
			massSMSInfo.setStatisticsEndDate(String.valueOf(resultMap.get("statisticsEndDate")));
			massSMSInfo.setPrepareStartTime(String.valueOf(resultMap.get("prepareStartTime")));
			massSMSInfo.setPrepareEndTime(String.valueOf((String)resultMap.get("prepareEndTime")));
			massSMSInfo.setSendStartTime(String.valueOf(resultMap.get("sendStartTime")));
			massSMSInfo.setSendEndTime(String.valueOf(resultMap.get("sendEndTime")));
			massSMSInfo.setState((String)resultMap.get("state"));
			massSMSInfo.setStateDesc(ThunderUtil.descState((String)resultMap.get("state")));
			massSMSInfo.setBackupYearMonth((String)resultMap.get("backupYearMonth"));
			massSMSInfo.setSendedType((String)resultMap.get("sendedType"));
			massSMSInfo.setSendedYear((String)resultMap.get("sendedYear"));
			massSMSInfo.setSendedMonth((String)resultMap.get("sendedMonth"));
			massSMSInfo.setSendedCount(Integer.parseInt(String.valueOf(resultMap.get("sendedCount"))));
			massSMSInfo.setRejectType((String)resultMap.get("rejectType"));
		}
		
		return massSMSInfo;
	}
	
	
	/**
	 * <p>대량SMS 통계 필터정보
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSFilter massSMSStatisticFilterInfo(int masssmsID, int scheduleID) throws DataAccessException{
		MassSMSFilter massSMSFilter = new MassSMSFilter();
		Map<String, Object> resultMap = null;
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.statistic.massSMSStatisticFilterInfo");
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("masssmsID", new Integer(masssmsID));
		param.put("scheduleID", new Integer(scheduleID));
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massSMSFilter.setFilterType0(Integer.parseInt(String.valueOf(resultMap.get("filterType0"))));
			massSMSFilter.setFilterType1(Integer.parseInt(String.valueOf(resultMap.get("filterType1"))));
			massSMSFilter.setFilterType2(Integer.parseInt(String.valueOf(resultMap.get("filterType2"))));
			massSMSFilter.setFilterType3(Integer.parseInt(String.valueOf(resultMap.get("filterType3"))));
			massSMSFilter.setFilterType4(Integer.parseInt(String.valueOf(resultMap.get("filterType4"))));
			massSMSFilter.setFilterType5(Integer.parseInt(String.valueOf(resultMap.get("filterType5"))));
			massSMSFilter.setFilterTotal(Integer.parseInt(String.valueOf(resultMap.get("filterTotal"))));
		}
		return  massSMSFilter;
	}
	
	
	/**
	 * <p>대량SMS 대상자 미리보기 총카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalMassSMSPersonPreview(Map<String, Object> searchMap, String backupYearMonth) throws DataAccessException{
		int masssmsID = (Integer)searchMap.get("masssmsID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		String standard = (String)searchMap.get("standard");
		String type= (String)searchMap.get("type");
		
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
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewcount");
		String tableName = " tm_masssms_sendresult_"+masssmsID+"_"+scheduleID+" ";
		if(backupYearMonth !=null && !backupYearMonth.equals("")){
			tableName = " tm_masssms_sendresult_"+backupYearMonth+" ";
		}
		
		sql += tableName;
		
		if(standard.equals("basic")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewtotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewfail");
			}else if(type.equals("ready")){
				sql += QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewready");
			}else{
				if(backupYearMonth !=null && !backupYearMonth.equals("")){
					tableName = " tm_masssms_filter_"+backupYearMonth+" ";
				}else{
					tableName = " tm_masssms_filter_"+masssmsID+"_"+scheduleID;
				}
				sql = QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewcount")+tableName+" "+QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewtotal");
				if(!type.equals("filterall")){
					sql += " AND filterType='"+type+"'";
				}
			}
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";
		}
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		param.put("scheduleID", new Integer(scheduleID));
			

		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	
	/**
	 * <p>대량SMS 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassSMSPersonPreview> massSMSPersonPreview(Map<String, Object> searchMap, String backupYearMonth) throws DataAccessException{
		
		int masssmsID = (Integer)searchMap.get("masssmsID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		String standard = (String)searchMap.get("standard");
		String type= (String)searchMap.get("type");
		
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
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewselect");
		String tableName = " tm_masssms_sendresult_"+masssmsID+"_"+scheduleID+" ";
		if(backupYearMonth !=null && !backupYearMonth.equals("")){
			tableName = " tm_masssms_sendresult_"+backupYearMonth+" ";
		}
		
		sql += tableName;
		
		if(standard.equals("basic")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewtotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewsuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewfail");
			}else if(type.equals("ready")){
				sql += QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewready");			
			}else{
				if(backupYearMonth !=null && !backupYearMonth.equals("")){
					tableName = " tm_masssms_filter_"+backupYearMonth+" ";
				}else{
					tableName = " tm_masssms_filter_"+masssmsID+"_"+scheduleID;
				}
				sql = QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewfilter")
				    +tableName+" "+QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreviewtotal");
				if(!type.equals("filterall")){
					sql += " AND filterType='"+type+"'";
				}
			}
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";
		}
		
		String sqlTail = QueryUtil.getStringQuery("masssms_sql","masssms.statistic.personpreview.tail");
		sql += sqlTail;
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		param.put("scheduleID", new Integer(scheduleID));
		
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassSMSPersonPreview massSMSPersonPreview = new MassSMSPersonPreview();
				massSMSPersonPreview.setReceiverPhone(rs.getString("receiverPhone"));
				massSMSPersonPreview.setRegistDate(rs.getString("registDate"));
				massSMSPersonPreview.setSmsCode(rs.getString("smsCode"));
				massSMSPersonPreview.setSmsCodeMsg(rs.getString("smsCodeMsg"));
				return massSMSPersonPreview;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	

	/**
	 * <p>대량SMS 결과 백업 테이블 정보 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String getBackupYearMonth(Map<String, Object> searchMap)  throws DataAccessException{
		
		int masssmsID = (Integer)searchMap.get("masssmsID");
		int scheduleID = (Integer)searchMap.get("scheduleID");
		
		Map<String, Object> resultMap = null;
		String result = "";
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.statistic.backupyearmonth");
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
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
	 * <p>대량SMS 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountMassSMSReportMonthList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.statistic.totalCountMassSMSReportMonthList");		
		
		String year = searchMap.get("year");
		String month = searchMap.get("month");
		String state = searchMap.get("state");
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");
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
		
		String sql_where = QueryUtil.getStringQuery("masssms_sql","masssms.reportmonth.where");	
		
		sql+=" AND "+sql_where;
		
		if(!state.equals("")){
			sql+= " AND state IN ("+state+")";
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("year", new Integer(year));
		param.put("month", new Integer(month));
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>대량SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassSMSList> massSMSReportMonthList(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String year = searchMap.get("year");
		String month = searchMap.get("month");
		String state = searchMap.get("state");
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listMassSMSList");		
		
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
		String sql_where = QueryUtil.getStringQuery("masssms_sql","masssms.reportmonth.where");	
		
		sql+=" AND "+sql_where+" ";
		
		if(!state.equals("")){
			sql+= " AND state IN ("+state+")";
		}
			
		String sqlTail = QueryUtil.getStringQuery("masssms_sql","masssms.write.selecttail");			
		sql += sqlTail;
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassSMSList massSMSlList = new MassSMSList();
				
				massSMSlList.setMasssmsID(rs.getInt("masssmsID"));				
				massSMSlList.setMasssmsTitle(rs.getString("masssmsTitle"));
				massSMSlList.setScheduleID(rs.getInt("scheduleID"));
				massSMSlList.setSendStartTime(rs.getString("sendStartTime"));
				massSMSlList.setSendScheduleDate(DateUtils.getStringDate(rs.getString("sendScheduleDate")));
				massSMSlList.setSendEndTime(rs.getString("sendEndTime"));
				massSMSlList.setState(rs.getString("state"));
				massSMSlList.setSendType(rs.getString("sendType"));
				massSMSlList.setSendTypeDesc(ThunderUtil.descSendType(rs.getString("sendType")));
				massSMSlList.setTargetCount(rs.getInt("targetTotalCount"));
				massSMSlList.setSendCount(rs.getInt("sendTotalCount"));
				massSMSlList.setUserName(rs.getString("userName"));
				massSMSlList.setGroupName(rs.getString("groupName"));
				massSMSlList.setRegistDate(rs.getString("registDate"));
				
				return massSMSlList;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>(); 
		param.put("year", new Integer(year));
		param.put("month", new Integer(month));
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>대량SMS 월간 보고서 - 총괄현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSReportMonth massSMSReportMonthTotalInfo(String year, String month, String userID,String groupID, String[] userInfo) throws DataAccessException{
		MassSMSReportMonth massSMSReportMonth = new MassSMSReportMonth();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.reportmonth.totalinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
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
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("year", new Integer(year));
		param.put("month", new Integer(month));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massSMSReportMonth.setTotalCount(Integer.parseInt(String.valueOf(resultMap.get("totalCount"))));
			massSMSReportMonth.setWriteCount(Integer.parseInt(String.valueOf(resultMap.get("writeCount"))));
			massSMSReportMonth.setAppReadyCount(Integer.parseInt(String.valueOf(resultMap.get("appReadyCount"))));
			massSMSReportMonth.setReadyCount(Integer.parseInt(String.valueOf(resultMap.get("readyCount"))));
			massSMSReportMonth.setSendingCount(Integer.parseInt(String.valueOf(resultMap.get("sendingCount"))));
			massSMSReportMonth.setSendFinishCount(Integer.parseInt(String.valueOf(resultMap.get("sendFinishCount"))));
			massSMSReportMonth.setSendingStopCount(Integer.parseInt(String.valueOf(resultMap.get("sendingStopCount"))));

		}
		
		return massSMSReportMonth;
	}
	
	
	/**
	 * <p>대량SMS 월간 보고서 - SMS 발송 현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSReportMonth massSMSReportMonthSendInfo(String year, String month , String userID, String groupID, String[] userInfo) throws DataAccessException{
		MassSMSReportMonth massSMSReportMonth = new MassSMSReportMonth();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.reportmonth.sendinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
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
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("year", new Integer(year));
		param.put("month", new Integer(month));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massSMSReportMonth.setSendTotal(Integer.parseInt(String.valueOf(resultMap.get("sendTotal"))));
			massSMSReportMonth.setSuccessTotal(Integer.parseInt(String.valueOf(resultMap.get("successTotal"))));
			massSMSReportMonth.setFailTotal(Integer.parseInt(String.valueOf(resultMap.get("failTotal"))));
			massSMSReportMonth.setReadyTotal(Integer.parseInt(String.valueOf(resultMap.get("readyTotal"))));

		}
		
		return massSMSReportMonth;
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
	public List<MassSMSStatisticUsers> masssmsStatisticUsersList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException{
		String year = searchMap.get("year");
		String month = searchMap.get("month");
		String groupID = searchMap.get("groupID");
		String userID = searchMap.get("userID");
		
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.statistic.usersselect"); 
		String sqlGroup =  QueryUtil.getStringQuery("masssms_sql","masssms.statistic.usersgroupby");
		
		
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
				MassSMSStatisticUsers massSMSStatisticUsers = new MassSMSStatisticUsers();
				massSMSStatisticUsers.setUserID(rs.getString("userID"));
				massSMSStatisticUsers.setUserName(rs.getString("userName"));
				massSMSStatisticUsers.setGroupName(rs.getString("groupName"));
				massSMSStatisticUsers.setSendTotal(rs.getInt("sendTotal"));
				massSMSStatisticUsers.setSuccessTotal(rs.getInt("successTotal"));
				massSMSStatisticUsers.setFailTotal(rs.getInt("failTotal"));
				massSMSStatisticUsers.setReadyTotal(rs.getInt("readyTotal"));				
				massSMSStatisticUsers.setSuccessRatio(StringUtil.getRatioToString(rs.getInt("successTotal"), rs.getInt("sendTotal")));
				
				
				return massSMSStatisticUsers;
			}		
		};
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("year", new Integer(year));
		param.put("month", new Integer(month));
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
}
