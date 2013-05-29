package web.massmail.write.dao;


import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.admin.usergroup.model.User;
import web.common.dao.DBJdbcDaoSupport;
import web.massmail.write.model.*;

import web.common.util.Constant;
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import web.common.util.ThunderUtil;

import org.springframework.dao.EmptyResultDataAccessException;


public class MassMailDAOImpl extends DBJdbcDaoSupport implements MassMailDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";
	
	
	
	/**
	 * <p>메일본문 읽어오기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> getMailContent(int massmailID) throws DataAccessException{
		
		Map<String, Object> resultMap = null;		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.viewmailcontent"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
	
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
	
		return resultMap;
	}
	
	
	/**
	 * <p>가장 최근에 입력된 massmailID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassMailIDInfo() throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.selectmassmailid"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
		
	/**
	 * <p>메일저장내용보기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo viewMassMailInfo(int massmailID, int scheduleID) throws DataAccessException{
		MassMailInfo massMailInfo = new MassMailInfo();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.viewmassmailinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
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
			massMailInfo.setMassmailID(Integer.parseInt(String.valueOf(resultMap.get("massmailID"))));
			massMailInfo.setMassmailTitle((String)resultMap.get("massmailTitle"));
			massMailInfo.setMassmailGroupID(Integer.parseInt(String.valueOf(resultMap.get("massmailGroupID"))));
			massMailInfo.setDescription((String)resultMap.get("description"));
			massMailInfo.setSendType((String)resultMap.get("sendType"));
			massMailInfo.setStatisticsType((String)resultMap.get("statisticsType"));
			massMailInfo.setStatisticsOpenType((String)resultMap.get("statisticsOpenType"));
			massMailInfo.setRepeatSendType((String)resultMap.get("repeatSendType"));
			
			
			//반복발송일 경우 
			if(massMailInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){
				massMailInfo.setRepeatSendStartDate(String.valueOf(resultMap.get("repeatSendStartDate")));
				massMailInfo.setRepeatSendEndDate(String.valueOf(resultMap.get("repeatSendEndDate")));			
				massMailInfo.setRepeatSendTimeHH(String.valueOf(resultMap.get("repeatSendTimeHH")));
				massMailInfo.setRepeatSendTimeMM(String.valueOf(resultMap.get("repeatSendTimeMM")));
				massMailInfo.setRepeatSendWeek((String)resultMap.get("repeatSendWeek"));
				massMailInfo.setRepeatSendDay(Integer.parseInt(String.valueOf(resultMap.get("repeatSendDay"))));
			}else{
				massMailInfo.setRepeatSendStartDate("");
				massMailInfo.setRepeatSendEndDate("");			
				massMailInfo.setRepeatSendTimeHH("");
				massMailInfo.setRepeatSendTimeMM("");
				massMailInfo.setRepeatSendWeek("");
				massMailInfo.setRepeatSendDay(0);			
			}

			
			
			massMailInfo.setMemo((String)resultMap.get("memo"));
			massMailInfo.setUserID((String)resultMap.get("userID"));
			massMailInfo.setUserName((String)resultMap.get("userName"));
			massMailInfo.setGroupID((String)resultMap.get("groupID"));
			massMailInfo.setApproveUserID((String)resultMap.get("approveUserID"));
			massMailInfo.setApproveDate(String.valueOf(resultMap.get("approveDate")));
			massMailInfo.setPriority(String.valueOf(resultMap.get("priority")));
			massMailInfo.setSenderMail((String)resultMap.get("senderMail"));
			massMailInfo.setSenderName((String)resultMap.get("senderName"));
			massMailInfo.setReturnMail((String)resultMap.get("returnMail"));
			massMailInfo.setEncodingType((String)resultMap.get("encodingType"));
			massMailInfo.setMailType((String)resultMap.get("mailType"));
			massMailInfo.setMailContent((String)(resultMap.get("mailContent")==null?"":resultMap.get("mailContent")));
			massMailInfo.setWebURL((String)resultMap.get("webURL"));
			massMailInfo.setWebURLType((String)resultMap.get("webURLType"));
			massMailInfo.setPollID(Integer.parseInt(String.valueOf(resultMap.get("pollID"))));		
			massMailInfo.setReceiverName((String)resultMap.get("receiverName"));
			massMailInfo.setMailTitle((String)resultMap.get("mailTitle"));
			massMailInfo.setMailLinkYN((String)resultMap.get("mailLinkYN"));
			massMailInfo.setPersistErrorCount(Integer.parseInt(String.valueOf(resultMap.get("persistErrorCount"))));
			massMailInfo.setRejectType((String)(resultMap.get("rejectType")));
			massMailInfo.setDuplicationYN((String)(resultMap.get("duplicationYN")));
			massMailInfo.setSendedCount(Integer.parseInt(String.valueOf(resultMap.get("sendedCount"))));
			massMailInfo.setSendedMonth((String)(resultMap.get("sendedMonth")));
			massMailInfo.setSendedType((String)(resultMap.get("sendedType")));
			massMailInfo.setSendedYear((String)(resultMap.get("sendedYear")));
			massMailInfo.setScheduleID(Integer.parseInt(String.valueOf(resultMap.get("scheduleID"))));
			massMailInfo.setSendScheduleDate(String.valueOf(resultMap.get("sendScheduleDate")));
			massMailInfo.setSendScheduleDateHH(String.valueOf(resultMap.get("sendScheduleDateHH")));
			massMailInfo.setSendScheduleDateMM(String.valueOf(resultMap.get("sendScheduleDateMM")));
			massMailInfo.setState((String)(resultMap.get("state")));
			massMailInfo.setAttachedFileNames((String)(resultMap.get("fileNames")));
			massMailInfo.setAttachedFilePath((String)(resultMap.get("filePath")));
			
		
			//반복발송 주가 있다면 
			if(massMailInfo.getRepeatSendWeek()!=null && !massMailInfo.getRepeatSendWeek().equals("")){
				for(int i=1;i<=7;i++){
					if(massMailInfo.getRepeatSendWeek().indexOf("1")!=-1){
						massMailInfo.setRepeatSendWeekSun("1");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("2")!=-1){
						massMailInfo.setRepeatSendWeekMon("2");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("3")!=-1){
						massMailInfo.setRepeatSendWeekTue("3");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("4")!=-1){
						massMailInfo.setRepeatSendWeekWed("4");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("5")!=-1){
						massMailInfo.setRepeatSendWeekThu("5");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("6")!=-1){
						massMailInfo.setRepeatSendWeekFri("6");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("7")!=-1){
						massMailInfo.setRepeatSendWeekSat("7");
					}
				} // end for

			}// end if 
	
			
		}else{
			return massMailInfo;
		}
		return massMailInfo;
	}
	
	
	/**
	 *  <p>대량메일 기본작성
	 * @param MassMailInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailInfo(MassMailInfo massMailInfo)  throws DataAccessException{	
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.insertmassmailinfo");			
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailGroupID", massMailInfo.getMassmailGroupID());
		param.put("massmailTitle", massMailInfo.getMassmailTitle());
		param.put("description", massMailInfo.getDescription());
		param.put("userID", massMailInfo.getUserID());		
		param.put("sendType", massMailInfo.getSendType());
		param.put("statisticsType", massMailInfo.getStatisticsType());
		param.put("statisticsOpenType", massMailInfo.getStatisticsOpenType());;		
		param.put("repeatSendType", massMailInfo.getRepeatSendType());
		param.put("repeatSendStartDate", massMailInfo.getRepeatSendStartDate());
		param.put("repeatSendEndDate", massMailInfo.getRepeatSendEndDate());
		param.put("repeatSendDay", massMailInfo.getRepeatSendDay());
		param.put("repeatSendWeek", massMailInfo.getRepeatSendWeek());
		param.put("approveUserID", massMailInfo.getApproveUserID());
		param.put("memo", massMailInfo.getMemo());
		param.put("priority", massMailInfo.getPriority());
		
		return getSimpleJdbcTemplate().update(sql, param);		
	}

	
	/**
	 * <p>대량메일 타게팅 그룹등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailTargetGroup(int massmailID, int targetID,  String exceptYN) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.inserttargetgroup");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("targetID", new Integer(targetID));	
		param.put("exceptYN", exceptYN);
		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>대량메일 스케줄등록 
	 * @param massMailInfo 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailSchedule(MassMailSchedule massMailSchedule) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.insertschedule");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID",massMailSchedule.getMassmailID());
		param.put("scheduleID",massMailSchedule.getScheduleID());
		param.put("sendScheduleDate", massMailSchedule.getSendScheduleDate());		
		param.put("statisticsEndDate", massMailSchedule.getstatisticsEndDate());
		param.put("targetTotalCount", massMailSchedule.getTargetTotalCount());
		param.put("state", massMailSchedule.getState());
		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>대량메일 필터링등록 
	 * @param massMailInfo 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailFilterSet(MassMailInfo massMailInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.insertfilterset");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID",massMailInfo.getMassmailID());
		param.put("sendedType", massMailInfo.getSendedType());
		param.put("sendedYear", massMailInfo.getSendedYear());
		param.put("sendedMonth", massMailInfo.getSendedMonth());
		param.put("sendedCount", massMailInfo.getSendedCount());
		param.put("rejectType", massMailInfo.getRejectType());
		param.put("duplicationYN", massMailInfo.getDuplicationYN());
		param.put("persistErrorCount", massMailInfo.getPersistErrorCount());
	
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>대량메일 메일내용 입력 
	 * @param massMailInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailMail(MassMailInfo massMailInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.insertmail");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID",massMailInfo.getMassmailID());
		param.put("senderName",massMailInfo.getSenderName());
		param.put("senderMail",massMailInfo.getSenderMail());
		param.put("receiverName",massMailInfo.getReceiverName());
		param.put("returnMail",massMailInfo.getReturnMail());
		param.put("encodingType",massMailInfo.getEncodingType());
		param.put("mailType",massMailInfo.getMailType());
		param.put("mailTitle",massMailInfo.getMailTitle());		
		param.put("mailContent",massMailInfo.getMailContent());
		param.put("mailLinkYN",massMailInfo.getMailLinkYN());
		param.put("webURL",massMailInfo.getWebURL());
		param.put("webURLType",massMailInfo.getWebURLType());
		param.put("pollID",massMailInfo.getPollID());		
		param.put("fileNames",massMailInfo.getAttachedFileNames());	
		param.put("filePath",massMailInfo.getAttachedFilePath());
		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>메일링크삽입 
	 * @param massMailLink
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailLink(MassMailLink massMailLink) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.insertlink");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID",massMailLink.getMassmailID());
		param.put("linkName",massMailLink.getLinkName());
		param.put("linkURL",massMailLink.getLinkURL());		
		param.put("linkDesc",massMailLink.getLinkDesc());
		param.put("linkCount",massMailLink.getLinkCount());
		param.put("linkType",massMailLink.getLinkType());
		param.put("useYN",massMailLink.getUseYN());
		
		return getSimpleJdbcTemplate().update(sql, param);	
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
	public List<MassMailList> listMassMailList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String sendScheduleDateStart = searchMap.get("sendScheduleDateStart");
		String sendScheduleDateEnd = searchMap.get("sendScheduleDateEnd");
		String sendType = searchMap.get("sendType");
		String groupID = searchMap.get("groupID");
		String massmailGroupID = searchMap.get("massmailGroupID");
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.select");		
		
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		//소속관리자라면 
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND (u.groupID = '"+userInfo[2]+"' OR m.statisticsOpenType = '3')";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND (u.userID = '"+userInfo[1]+"' OR m.statisticsOpenType = '3' OR (m.statisticsOpenType = '2' AND u.groupID = '"+userInfo[2]+"' ))"; 	
		}			
		
		
		//검색조건이 있다면
		if(db_type.equals(DB_TYPE_ORACLE))
			sql += " AND s.sendScheduleDate >= TO_DATE('"+ sendScheduleDateStart +"','YYYY-MM-DD HH24:MI:SS')  AND s.sendScheduleDate <= TO_DATE('"+ sendScheduleDateEnd +"','YYYY-MM-DD HH24:MI:SS')  ";
		else if(db_type.equals(DB_TYPE_MYSQL))
			sql += " AND s.sendScheduleDate >= '"+ sendScheduleDateStart +"' AND s.sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";
		else if(db_type.equals(DB_TYPE_MSSQL))
			sql += " AND s.sendScheduleDate >= '"+ sendScheduleDateStart +"' AND s.sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}		
		
		if(sendType!=null && !sendType.equals("")){
			sql += " AND m.sendType='"+sendType+"' ";
		}
		if(groupID!=null && !groupID.equals("")){
			sql += " AND u.groupID='"+groupID+"' ";
		}	
		
		if(massmailGroupID!=null && !massmailGroupID.equals("")){
			sql += " AND m.massmailGroupID='"+massmailGroupID+"' ";
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
				massMailList.setApproveUserID(rs.getString("approveUserID"));
				massMailList.setSendTypeDesc(ThunderUtil.descSendType(rs.getString("sendType")));
				massMailList.setTargetCount(rs.getInt("targetTotalCount"));
				massMailList.setSendCount(rs.getInt("sendTotalCount"));
				massMailList.setSuccessCount(rs.getInt("successTotal")); 
				massMailList.setUserID(rs.getString("userID"));
				massMailList.setUserName(rs.getString("userName"));
				massMailList.setGroupID(rs.getString("groupID"));
				massMailList.setGroupName(rs.getString("groupName"));
				massMailList.setPriority(rs.getString("priority"));
				massMailList.setPollID(rs.getInt("pollID"));
				massMailList.setRegistDate(rs.getString("registDate"));
				
				return massMailList;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>대량메일 Self 리스트 
	 * @param userAuth
	 * @param countPerPage
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailList> listMassMailSelfList(String[] userInfo, int countPerPage) throws DataAccessException{
				
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.select");		
		
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		
		//소속관리자라면 
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND (u.groupID = '"+userInfo[2]+"' OR m.statisticsOpenType = '3')";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND (u.userID = '"+userInfo[1]+"' OR m.statisticsOpenType = '3' OR (m.statisticsOpenType = '2' AND u.groupID = '"+userInfo[2]+"' ))"; 	
		}		
		
		sql += " AND s.sendScheduleDate <= '"+ web.common.util.DateUtils.getDateString()+" 23:59:59'";
		String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.write.selecttail");			
		sql += sqlTail;
		
		//System.out.println(sql);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassMailList massMailList = new MassMailList();
				
				massMailList.setMassmailID(rs.getInt("massmailID"));
				massMailList.setMassmailGroupID(rs.getInt("massmailGroupID"));
				massMailList.setMassmailTitle(rs.getString("massmailTitle"));
				massMailList.setScheduleID(rs.getInt("scheduleID"));
				massMailList.setSendStartTime(rs.getString("sendStartTime"));
				massMailList.setSendScheduleDate(rs.getString("sendScheduleDate"));
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
		param.put("start", new Integer(1));
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
	public int totalCountMassMailList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.totalcountmassmaillist");		
		
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String sendScheduleDateStart = searchMap.get("sendScheduleDateStart");
		String sendScheduleDateEnd = searchMap.get("sendScheduleDateEnd");
		String sendType = searchMap.get("sendType");
		String groupID = searchMap.get("groupID");
		String massmailGroupID = searchMap.get("massmailGroupID");
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql += " AND s.sendScheduleDate >= TO_DATE('"+ sendScheduleDateStart +"','YYYY-MM-DD HH24:MI:SS')  AND s.sendScheduleDate <= TO_DATE('"+ sendScheduleDateEnd +"','YYYY-MM-DD HH24:MI:SS')  ";
		else if(db_type.equals(DB_TYPE_MYSQL))
			sql += " AND s.sendScheduleDate >= '"+ sendScheduleDateStart +"' AND s.sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";
		else if(db_type.equals(DB_TYPE_MSSQL))
			sql += " AND s.sendScheduleDate >= '"+ sendScheduleDateStart +"' AND s.sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";
		
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		
		//소속관리자라면 
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND (u.groupID = '"+userInfo[2]+"' OR m.statisticsOpenType = '3')";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND (u.userID = '"+userInfo[1]+"' OR m.statisticsOpenType = '3' OR (m.statisticsOpenType = '2' AND u.groupID = '"+userInfo[2]+"' ))"; 	
		}		
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}		
		
		if(sendType!=null && !sendType.equals("")){
			sql += " AND m.sendType='"+sendType+"' ";
		}
		
		if(groupID!=null && !groupID.equals("")){
			sql += " AND u.groupID='"+groupID+"' ";
		}	
		
		if(massmailGroupID!=null && !massmailGroupID.equals("")){
			sql += " AND m.massmailGroupID='"+massmailGroupID+"' ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}

	
	
	
	/**
	 * <p>유저별 등록된 테스트메일을 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> listTesterEmail(String userID,String testerEmail) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selecttester");		
		
		
		String sqlwhere ="";
		if(testerEmail!=null && !testerEmail.equals("") ){
			sqlwhere = " AND testerEmail='"+testerEmail+"'";
		}
		sql = sql+sqlwhere;
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("testerEmail",rs.getString("testerEmail"));
				resultMap.put("testerName",rs.getString("testerName"));				
				return resultMap;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
	}
	
	
	/**
	 * <p>대상자리스트를 출력한다. 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Targeting> listTargeting(int currentPage, int countPerPage,Map<String, String> searchMap, String[] userInfo) throws DataAccessException{
		
		int start = (currentPage - 1) * countPerPage;
		String searchType = searchMap.get("sSearchType");
		String searchName = searchMap.get("sSearchText");
		String bookMark = searchMap.get("sBookMark");
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.target");		
		//수퍼관리자(시스템관리자)가 아니라면 
		if(!userInfo[0].equals(Constant.USER_LEVEL_ADMIN)){
			//전체공유와 그룹공유는  출력, 그리고 지정공유라면 본인의 그룹아이디나 사용자아이디가 있다면 출력 
			sql+=" AND ((t.shareType='"+Constant.SHARE_TYPE_ALL+"')"
				+ " OR (t.shareType='"+Constant.SHARE_TYPE_GROUP+"' AND u.groupID='"+userInfo[2]+"')" 
				+ " OR (t.shareType='"+Constant.SHARE_TYPE_D+"' AND t.shareID IN ('"+userInfo[1]+"','"+userInfo[2]+"'))";
			
			//일반사용자라면 (비공유는 본인꺼만 확인 가능)
			 if(userInfo[0].equals(Constant.USER_LEVEL_USER)){		
				 sql+=" OR (t.shareType='"+Constant.SHARE_TYPE_NOT+"' AND t.userID IN ('"+userInfo[1]+"'))";
			 }		
			
			//그룹관리자라면 (그룹구성원에 대한 것은 비공유라도 볼 수 있다.)
			if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
				 sql+=" OR (t.shareType='"+Constant.SHARE_TYPE_NOT+"' AND u.groupID IN ('"+userInfo[2]+"'))";
			}
			sql+=") ";
		}else{
			//수퍼관리자는 전체를 볼수 있기 때문에 별다른 처리를 하지 않는다.
		}
		
		//검색조건이 있다면
		if(searchName!=null && !searchName.equals("")){
			sql += " AND "+searchType+" LIKE :searchName ";
		}	
		//검색에 맞게 조회한다.		
		if(bookMark!=null && !bookMark.equals("")){
			sql += " AND t.bookMark='"+bookMark+"'";
		}else{
			sql += " AND t.bookMark NOT IN('D') ";   //D는 삭제된 것이므로 제외한다.
		}
		
		String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.write.targettail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {		
				Targeting targeting = new Targeting();
				targeting.setTargetID(rs.getInt("targetID"));
				targeting.setUserID(rs.getString("userID"));
				targeting.setUserName(rs.getString("userName"));
				targeting.setTargetName(rs.getString("targetName"));
				targeting.setTargetType(rs.getString("targetType"));
				targeting.setDbID(rs.getString("dbID"));
				targeting.setQueryText(rs.getString("queryText"));			
				targeting.setTargetCount(rs.getInt("targetCount"));
				targeting.setUserName(rs.getString("userName"));
				targeting.setGroupID(rs.getString("groupID"));
				targeting.setGroupName(rs.getString("groupName"));
				
				return targeting;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchName", "%"+searchName+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
	}
	
	
	/**
	 * <p>대상자리스트의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetingTotalCount(Map<String, String> searchMap, String[] userInfo)  throws DataAccessException{
		String searchType = searchMap.get("sSearchType");
		String searchName = searchMap.get("sSearchText");
		String bookMark = searchMap.get("sBookMark");
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.totalcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		//수퍼관리자(시스템관리자)가 아니라면 
		if(!userInfo[0].equals(Constant.USER_LEVEL_ADMIN)){
			//전체공유와 그룹공유는  출력, 그리고 지정공유라면 본인의 그룹아이디나 사용자아이디가 있다면 출력 
			sql+=" AND ((t.shareType='"+Constant.SHARE_TYPE_ALL+"')"
				+ " OR (t.shareType='"+Constant.SHARE_TYPE_GROUP+"' AND u.groupID='"+userInfo[2]+"')" 
				+ " OR (t.shareType='"+Constant.SHARE_TYPE_D+"' AND t.shareID IN ('"+userInfo[1]+"','"+userInfo[2]+"'))";
			
			//일반사용자라면 (비공유는 본인꺼만 확인 가능)
			 if(userInfo[0].equals(Constant.USER_LEVEL_USER)){		
				 sql+=" OR (t.shareType='"+Constant.SHARE_TYPE_NOT+"' AND t.userID IN ('"+userInfo[1]+"'))";
			 }		
			
			//그룹관리자라면 (그룹구성원에 대한 것은 비공유라도 볼 수 있다.)
			if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
				 sql+=" OR (t.shareType='"+Constant.SHARE_TYPE_NOT+"' AND u.groupID IN ('"+userInfo[2]+"'))";
			}
			sql+=") ";
		}else{
			//수퍼관리자는 전체를 볼수 있기 때문에 별다른 처리를 하지 않는다.
		}
		if(searchName!=null && !searchName.equals("")){
			sql += " AND "+searchType+" LIKE :searchName ";
		}
		//검색에 맞게 조회한다.		
		if(bookMark!=null && !bookMark.equals("")){
			sql += " AND t.bookMark='"+bookMark+"'";
		}else{
			sql += " AND t.bookMark NOT IN('D') ";   //D는 삭제된 것이므로 제외한다.
		}
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchName", "%"+searchName+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	/**
	 * <p>대량메일 수정 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailInfo(MassMailInfo massMailInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updatemassmailinfo");			
		
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("massmailGroupID", massMailInfo.getMassmailGroupID());	
		param.put("massmailTitle", massMailInfo.getMassmailTitle());
		param.put("description", massMailInfo.getDescription());		
		param.put("modifyUserID", massMailInfo.getModifyUserID());
		param.put("modifyDate", massMailInfo.getModifyDate());
		param.put("sendType", massMailInfo.getSendType());	
		param.put("statisticsType", massMailInfo.getStatisticsType());
		param.put("repeatSendType", massMailInfo.getRepeatSendType());
		param.put("repeatSendStartDate", massMailInfo.getRepeatSendStartDate());
		param.put("repeatSendEndDate", massMailInfo.getRepeatSendEndDate());
		param.put("repeatSendDay", massMailInfo.getRepeatSendDay());
		param.put("repeatSendWeek", massMailInfo.getRepeatSendWeek());
		param.put("approveUserID", massMailInfo.getApproveUserID());
		param.put("memo", massMailInfo.getMemo());
		param.put("statisticsOpenType", massMailInfo.getStatisticsOpenType());	
		param.put("massmailID", massMailInfo.getMassmailID());
		param.put("priority",massMailInfo.getPriority());
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	

	/**
	 * <p>대량메일 승인 날짜 업데이트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateApproveDate(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updateapprovedate");
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("massmailID", massmailID);
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	/**
	 * <p>대량메일 필터링수정 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailFilterSet(MassMailInfo massMailInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updatefilterset");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID",massMailInfo.getMassmailID());
		param.put("sendedType", massMailInfo.getSendedType());
		param.put("sendedYear", massMailInfo.getSendedYear());
		param.put("sendedMonth", massMailInfo.getSendedMonth());
		param.put("sendedCount", massMailInfo.getSendedCount());
		param.put("rejectType", massMailInfo.getRejectType());
		param.put("duplicationYN", massMailInfo.getDuplicationYN());
		param.put("persistErrorCount", massMailInfo.getPersistErrorCount());
	
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>대량메일 메일내용 수정
	 * @param massMailInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailMail(MassMailInfo massMailInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updatemail");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID",massMailInfo.getMassmailID());
		param.put("senderName",massMailInfo.getSenderName());
		param.put("senderMail",massMailInfo.getSenderMail());
		param.put("receiverName",massMailInfo.getReceiverName());
		param.put("returnMail",massMailInfo.getReturnMail());
		param.put("encodingType",massMailInfo.getEncodingType());
		param.put("mailType",massMailInfo.getMailType());
		param.put("mailTitle",massMailInfo.getMailTitle());
		param.put("mailContent",massMailInfo.getMailContent());
		param.put("webURL",massMailInfo.getWebURL());
		param.put("webURLType",massMailInfo.getWebURLType());
		param.put("pollID",massMailInfo.getPollID());				
		param.put("mailLinkYN", massMailInfo.getMailLinkYN());
		param.put("fileNames",massMailInfo.getAttachedFileNames());	
		param.put("filePath",massMailInfo.getAttachedFilePath());
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	
	/**
	 * <p>기본정보를 삭제한다.
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailInfo(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletemassmailinfo");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>대상그룹삭제 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailTargetGroup(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletetargetgroup");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	
	/**
	 * <p>필터설정삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailFilterSet(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletefilterset");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>필터링삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailFiltering(int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletefiltering");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		param.put("scheduleID", new Integer(scheduleID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	
	/**
	 * <p>스케줄일괄삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailSchedule(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deleteschedule");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));			
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	/**
	 * <p>스케줄삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailSchedule(int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deleteschedule");		
		sql+= " AND scheduleID="+scheduleID;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>메일정보삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailMail(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletemail");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	
	/**
	 * <p>메일링크정보삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailLink(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletemaillink");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>메일링크클릭삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailLinkClick(int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletelinkclick");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		param.put("scheduleID", new Integer(scheduleID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	

	
	
	/**
	 * <p>도메인통계삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailDomainStatistic(int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletedoaminstatistic");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		param.put("scheduleID", new Integer(scheduleID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	
	/**
	 * <p>실패통계삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailFailStatistic(int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletefailstatistic");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));		
		param.put("scheduleID", new Integer(scheduleID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	/**
	 * <p>시간통계삭제 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailTimeStatistic(int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletetimestatistic");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));	
		param.put("scheduleID", new Integer(scheduleID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
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
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TargetingGroup> listTargetingGroup(String target_ids) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selecttargetgroupids");
		sql +=" WHERE targetID IN ("+target_ids+")";
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				TargetingGroup targetingGroup = new TargetingGroup();
				targetingGroup.setTargetID(rs.getInt("targetID"));
				targetingGroup.setTargetName(rs.getString("targetName"));
				targetingGroup.setTargetCount(rs.getInt("targetCount"));
				targetingGroup.setTargetType(rs.getString("targetType"));
				return targetingGroup;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("target_ids", target_ids);

		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>메일링크리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailLink> listMassMailLink(int massmailID) throws DataAccessException{		

		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectmaillink");		

		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {		
				MassMailLink massMailLink = new MassMailLink();
				massMailLink.setLinkID(rs.getInt("linkID"));
				massMailLink.setMassmailID(rs.getInt("massmailID"));
				massMailLink.setLinkName(rs.getString("linkName"));
				massMailLink.setLinkURL(rs.getString("linkURL"));				
				massMailLink.setLinkCount(rs.getInt("linkCount"));
				massMailLink.setLinkDesc(rs.getString("linkDesc"));
				massMailLink.setLinkType(rs.getString("linkType"));
				return massMailLink;
			}
		};		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", new Integer(massmailID));		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}	
	
	
	/**
	 * <p>대상자그룹조회 
	 * @param massmailID
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public int getCountByTargetID(int massmailID, int targetID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.getcounttargetID");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("targetID", new Integer(targetID));
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>ez_config에서 해당되는 configValue값을 구해온다.
	 * @param configFlag
	 * @param configName
	 * @return
	 * @throws DataAccessException
	 */
	public String getConfigValue(String configFlag, String configName)  throws DataAccessException{
		Map<String, Object> resultMap = null;
		String result = "";
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.systemset.selectconfigvalue");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("configFlag", configFlag);
		param.put("configName", configName);
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			result = (String)resultMap.get("configValue");
		}
		
		return result;
		
		
	}
	
	/**
	 * <p>메일컨텐츠 업데이트(링크분석후)
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMailContent(int massmailID, String mailContent) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updatemailcontent");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("mailContent", mailContent);
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>메일링크에서 선택된 메일링크 삭제 
	 * @param massmailID
	 * @param linkID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMailLinkByLinkID(int massmailID, String[] linkIDs) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deletelinkbyid");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
	
		String sqlwhere = " linkID IN(";
		String sqlcols = "";
		for(int i=0;i<linkIDs.length;i++){
			if(i==linkIDs.length-1){
				sqlcols +=linkIDs[i]+")";
			}else{
				sqlcols+=linkIDs[i]+",";
			}
		}
		
		sql = sql + sqlwhere + sqlcols;
				
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>메일링크 처리후 최종 상태값변경 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAllScheduleState(int massmailID, String state) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updateallschedulestate");		

		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("state", state);
		return getSimpleJdbcTemplate().update(sql, param);
		
	}
	
	
	/**
	 * <p>메일링크 타입을 수신거부로 변경되면 일단 기존꺼를 모두 일반으로 변경한다.
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMailLinkTypeAll(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updatemaillinkall");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>메일타입을 변경한다.
	 * @param linkID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMailLinkType(int linkID, String linkType) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updatemaillinktype");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("linkType", linkType);
		param.put("linkID", new Integer(linkID));	
		return getSimpleJdbcTemplate().update(sql, param);
	}
	

	/**
	 * <p>시스템메일에 입력한다.
	 * @param systemNotify
	 * @return
	 * @throws DataAccessException
	 */
	public int insertSystemNotify(SystemNotify systemNotify) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.insertnotifyemail");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("notifyFlag",systemNotify.getNotifyFlag());
		param.put("notifyType",systemNotify.getNotifyType());
		param.put("userID",systemNotify.getUserID());
		param.put("senderMail",systemNotify.getSenderMail());
		param.put("senderName",systemNotify.getSenderName());
		param.put("receiverMail",systemNotify.getReceiverMail());
		param.put("receiverName",systemNotify.getReceiverName());
		param.put("returnMail",systemNotify.getReturnMail());
		param.put("mailTitle",systemNotify.getMailTitle());
		param.put("mailContent",systemNotify.getMailContent());
		param.put("fileNames",systemNotify.getFileNames());
		param.put("filePath",systemNotify.getFilePath());
		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	/**
	 * <p>DB에 쿼리정보를 가져온다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getQeuryDB(int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selecttargetquery");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("targetID", new Integer(targetID));
		return getSimpleJdbcTemplate().queryForMap(sql, param);
	}
	
	/**
	 * <p>타겟ID에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<OnetooneTarget> listOnetooneTarget(int targetID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.viewonetoonetarget");	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				OnetooneTarget onetooneTarget = new OnetooneTarget();
				onetooneTarget.setOnetooneAlias(rs.getString("onetooneAlias"));
				onetooneTarget.setFieldName(rs.getString("fieldName"));
				return onetooneTarget;
			}			
		};		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("targetID", new Integer(targetID));			
			
		return  getSimpleJdbcTemplate().query(sql, rowMapper,param);
	}

	
	
	
	/**
	 * <p>테스트 전송메일 리스트 
	 * @param notifyFlag
	 * @param userID
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SystemNotify> listSystemNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
	
		int start = (currentPage - 1) * countPerPage;
		String searchType = searchMap.get("sSearchType");
		String searchText = searchMap.get("sSearchText");
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectnotify");		
		
		
		//검색조건이 있다면		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}		
			
		String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectnotifytail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				SystemNotify systemNotify = new SystemNotify();
				
				systemNotify.setNotifyID(rs.getInt("notifyID"));
				systemNotify.setUserID(rs.getString("userID"));
				systemNotify.setReceiverMail(rs.getString("receiverMail"));
				systemNotify.setMailTitle(rs.getString("mailTitle"));				
				systemNotify.setSmtpCode(rs.getString("smtpCode"));
				systemNotify.setSmtpResult(rs.getString("smtpResult"));
				systemNotify.setSmtpMsg(rs.getString("smtpMsg"));
				systemNotify.setWasSended(rs.getString("wasSended"));
				systemNotify.setRegistDate(rs.getString("registDate"));
				return systemNotify;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("notifyFlag",notifyFlag);
		param.put("userID",userID);
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.totalcountnotify");		
		
		String searchType = searchMap.get("sSearchType");
		String searchText = searchMap.get("sSearchText");
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("notifyFlag",notifyFlag);
		param.put("userID",userID);
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	/**
	 * <p>타겟ID에 해당되는 원투원 정보 
	 * @param targetIDs
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<OnetooneTarget> selectOnetooneByTargetID(String[] targetIDs) throws DataAccessException{
	
		String sql = "";		
		String sqlfrom = "";
		String sqlwhere = "";
		
		for(int i=0;i<targetIDs.length;i++){
			if(db_type.equals(DB_TYPE_ORACLE))
				sqlfrom += "("+QueryUtil.getStringQuery("massmail_sql","massmail.write.selectonetoone")+" AND t.targetID ="+targetIDs[i]+") id"+targetIDs[i];
			else if(db_type.equals(DB_TYPE_MYSQL))
				sqlfrom += "("+QueryUtil.getStringQuery("massmail_sql","massmail.write.selectonetoone")+" AND t.targetID ="+targetIDs[i]+") AS id"+targetIDs[i];
			else if(db_type.equals(DB_TYPE_MSSQL))
				sqlfrom += "("+QueryUtil.getStringQuery("massmail_sql","massmail.write.selectonetoone")+" AND t.targetID ="+targetIDs[i]+") AS id"+targetIDs[i];
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
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				OnetooneTarget onetooneTarget = new OnetooneTarget();
				onetooneTarget.setOnetooneName(rs.getString("onetooneName"));
				onetooneTarget.setOnetooneAlias(rs.getString("onetooneAlias"));
				return onetooneTarget;
			}			
		};		
	
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	
	/**
	 * <p>메일템플릿 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailTemplate> listMailTemplate(String userID, String groupID, String userAuth, String templateType) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selecttemplate");		
		
		//소속관리자라면 
		if(userAuth.equals(Constant.USER_LEVEL_MASTER)){
			sql+=QueryUtil.getStringQuery("massmail_sql","massmail.write.selecttemplatebygroup");		
		}
		//일반사용자라면 
		else if(userAuth.equals(Constant.USER_LEVEL_USER)){
			sql+=QueryUtil.getStringQuery("massmail_sql","massmail.write.selecttemplatebyuser");	
		}			
		
		String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.write.templatetail");			
		sql += sqlTail;
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MailTemplate mailTemplate = new MailTemplate();
				mailTemplate.setTemplateID(rs.getInt("templateID"));
				mailTemplate.setTemplateName(rs.getString("templateName"));			
				mailTemplate.setTemplateContent(rs.getString("templateContent"));
				mailTemplate.setUserID(rs.getString("userID"));
				mailTemplate.setUserName(rs.getString("userName"));
				mailTemplate.setTemplateType(rs.getString("templateType")); 
				
				return mailTemplate;
			}			
		};		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID",userID);
		param.put("groupID",groupID);
		param.put("templateType",templateType);
		return  getSimpleJdbcTemplate().query(sql, rowMapper,param);
	}
	
	/**
	 * <p>메일 템플릿 보기 
	 * @param templateID
	 * @return
	 * @throws DataAccessException
	 */
	public MailTemplate viewMailTemplate(int templateID)  throws DataAccessException{
		MailTemplate mailTemplate = new MailTemplate();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.viewtemplate"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("templateID", new Integer(templateID));

		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			mailTemplate.setTemplateID(Integer.parseInt(String.valueOf(resultMap.get("templateID"))));
			mailTemplate.setTemplateName((String)resultMap.get("templateName"));
			mailTemplate.setTemplateContent((String)resultMap.get("templateContent"));
		}
		return mailTemplate;
	}
	
	/**
	 * <p>템플릿 적용시 사용카운트 증가 
	 * @param templateID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateUsedCountTemplate(int templateID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.updatetemplatecount"); 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("templateID", new Integer(templateID));
		return getSimpleJdbcTemplate().update(sql, param);

	}
	
	
	/**
	 * <p>시스템메일삭제(즉, 테스트메일)
	 * @param notifyIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSystemNotify(String[] notifyIDs) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.deletenotifyemail"); 
	
		String sqlcols = "";
		for(int i=0;i<notifyIDs.length;i++){
			if(i==notifyIDs.length-1){
				sqlcols +=notifyIDs[i]+")";
			}else{
				sqlcols+=notifyIDs[i]+",";
			}
		}
		
		sql=sql+sqlcols;
		return getSimpleJdbcTemplate().update(sql);
	}
	
	
	/**
	 * <p>테스트메일 삭제 
	 * @param userID
	 * @param testerEmails
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteTesterEmail(String userID, String[] testerEmails) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.deletetester"); 
		
		String sqlcols = "";
		for(int i=0;i<testerEmails.length;i++){
			if(i==testerEmails.length-1){
				sqlcols +="'"+testerEmails[i]+"')";
			}else{
				sqlcols+="'"+testerEmails[i]+"',";
			}
		}		
		sql=sql+sqlcols;
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID",userID);
		
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>첨부파일 리스트 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AttachedFile> listAttachedFile(String userID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectattachedfile");		
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				AttachedFile attachedFile = new AttachedFile();
				attachedFile.setFileKey(rs.getString("fileKey"));
				attachedFile.setFileSize(rs.getString("fileSize"));
				attachedFile.setFileName(rs.getString("fileName"));
				attachedFile.setFilePath(rs.getString("filePath"));
				attachedFile.setRegistDate(rs.getString("registDate"));
				attachedFile.setSendedDate(rs.getString("sendedDate"));
				attachedFile.setFileSizeByte(rs.getString("fileSizeByte"));

				return attachedFile;
			}			
		};		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID",userID);
		return  getSimpleJdbcTemplate().query(sql, rowMapper,param);
	}
	
	/**
	 * <p>첨부파일 삽입 
	 * @param attachedFile
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAttachedFile(AttachedFile attachedFile) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.insertattachedfile"); 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("userID", attachedFile.getUserID());
		param.put("fileName", attachedFile.getFileName());
		param.put("fileKey", attachedFile.getFileKey());
		param.put("filePath", attachedFile.getFilePath());
		param.put("fileSize", attachedFile.getFileSize());
		param.put("fileSizeByte", attachedFile.getFileSizeByte());
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>해당되는 파일 정보 가져오기 
	 * @param fileID
	 * @param fileKey
	 * @return
	 * @throws DataAccessException
	 */
	public AttachedFile getAttachedFile(String fileKey) throws DataAccessException{
		AttachedFile attachedFile = new AttachedFile();
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.getattachedfile"); 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("fileKey", fileKey);
		Map<String, Object> resultMap = null;
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			attachedFile.setFileName((String)(resultMap.get("fileName")));
			attachedFile.setFileKey((String)(resultMap.get("fileKey")));
			attachedFile.setFileSize((String)(resultMap.get("fileSize")));
		}
		return attachedFile;
	}
	
	
	/**
	 * <p>첨부파일 디비에서 삭제 
	 * @param fileKeys
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAttachedFile(String[] fileKeys) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.deleteattachedfile"); 
		String sqlcols = "";
		for(int i=0;i<fileKeys.length;i++){
		
			
			if(i==fileKeys.length-1){
				sqlcols +="'"+fileKeys[i]+"')";
			}else{
				sqlcols+="'"+fileKeys[i]+"',";
			}
		}		
		sql=sql+sqlcols;

		return getSimpleJdbcTemplate().update(sql);
	}
	
	
	
	/**
	 * <p>반복메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailList> listMassMailRepeat(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException{
		
		
		int start = (currentPage - 1) * countPerPage;
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String repeatSendType = searchMap.get("repeatSendType");
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectrepeatmail");		
		
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		
		//소속관리자라면 
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND u.groupID = '"+userInfo[2]+"'";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND u.userID = '"+userInfo[1]+"'"; 	
		}		
		
		//검색조건이 있다면
			
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}		

		if(!repeatSendType.equals("")){
			sql += " AND repeatSendType LIKE '%" + repeatSendType + "%' ";
		}
		String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectrepeatmailtail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
				
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassMailList massMailList = new MassMailList();
				
				massMailList.setMassmailID(rs.getInt("massmailID"));
				massMailList.setMassmailTitle(rs.getString("massmailTitle"));				
				massMailList.setUserName(rs.getString("userName"));
				massMailList.setRepeatSendType(rs.getString("repeatSendType"));
				massMailList.setRepeatSendTypeDesc(ThunderUtil.descRepeatSendType(rs.getString("repeatSendType")));
				massMailList.setRepeatSendStartDate(DateUtils.getStringDate(rs.getString("repeatSendStartDate").toString()));
				massMailList.setRepeatSendEndDate(DateUtils.getStringDate(rs.getString("repeatSendEndDate").toString()));

	
						
				return massMailList;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
	}
	
	
	/**
	 * <p>반복메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */

	public int totalCountMassMailRepeat(String[] userInfo, Map<String, String> searchMap) throws DataAccessException{
		
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String repeatSendType = searchMap.get("repeatSendType");
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.totalrepeatmail");		
		
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		
		//소속관리자라면 
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND u.groupID = '"+userInfo[2]+"'";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND u.userID = '"+userInfo[1]+"'"; 	
		}		
		
		//검색조건이 있다면
			
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}	
		
		if(!repeatSendType.equals("")){
			sql += " AND repeatSendType LIKE '%" + repeatSendType + "%' ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql,param);
		
	}
	
	/**
	 * <p>반복메일 스케줄리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public  List<MassMailList> listRepeatSchedule(int massmailID, int currentPage, int countPerPage, Map<String, String> searchMap)  throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String sendScheduleDateStart = searchMap.get("sendScheduleDateStart");
		String sendScheduleDateEnd = searchMap.get("sendScheduleDateEnd");
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectrepeatschedule");		
		
		
		//검색조건이 있다면	
		if(db_type.equals(DB_TYPE_ORACLE))
			sql += " AND sendScheduleDate >= to_date('"+ sendScheduleDateStart +"','YYYY-mm-dd hh24:mi:ss') AND sendScheduleDate <= to_date('"+ sendScheduleDateEnd +"','YYYY-mm-dd hh24:mi:ss') ";
		else if(db_type.equals(DB_TYPE_MYSQL))
			sql += " AND sendScheduleDate >= '"+ sendScheduleDateStart +"' AND sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";
		else if(db_type.equals(DB_TYPE_MSSQL))
			sql += " AND sendScheduleDate >= '"+ sendScheduleDateStart +"' AND sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";

			
		String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectrepeatscheduletail");			
		sql += sqlTail;
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassMailList massMailList = new MassMailList();
				massMailList.setMassmailID(rs.getInt("massmailID"));
				massMailList.setScheduleID(rs.getInt("scheduleID"));
				massMailList.setSendStartTime(DateUtils.getStringDate(String.valueOf(rs.getString("sendStartTime"))));
				massMailList.setSendScheduleDate(DateUtils.getStringDate(String.valueOf(rs.getString("sendScheduleDate"))));
				massMailList.setState(rs.getString("state"));
			
				
				return massMailList;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", new Integer(massmailID));
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>반복메일스케즐카운트 
	 * @param massmailID
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountRepeatSchedule(int massmailID, Map<String, String> searchMap) throws DataAccessException{
		String sendScheduleDateStart = searchMap.get("sendScheduleDateStart");
		String sendScheduleDateEnd = searchMap.get("sendScheduleDateEnd");
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.totalrepeatschedule");				
		
		//검색조건이 있다면	
		if(db_type.equals(DB_TYPE_ORACLE))
			sql += " AND sendScheduleDate >= to_date('"+ sendScheduleDateStart +"','YYYY-MM-DD hh24:mi:ss') AND sendScheduleDate <= to_date('"+ sendScheduleDateEnd +"','YYYY-MM-DD hh24:mi:ss') ";
		else if(db_type.equals(DB_TYPE_MYSQL))
			sql += " AND sendScheduleDate >= '"+ sendScheduleDateStart +"' AND sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";
		else if(db_type.equals(DB_TYPE_MSSQL))
			sql += " AND sendScheduleDate >= '"+ sendScheduleDateStart +"' AND sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", new Integer(massmailID));
		
		return  getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	
	/**
	 * <p>반복메일 정보보기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo viewRepeatMassmailInfo(int massmailID) throws DataAccessException{
		MassMailInfo massMailInfo = new MassMailInfo();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.viewrepeatmassmail"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massMailInfo.setMassmailID(Integer.parseInt(String.valueOf(resultMap.get("massmailID"))));
			massMailInfo.setMassmailTitle((String)resultMap.get("massmailTitle"));
			massMailInfo.setUserID((String)resultMap.get("userID"));
			massMailInfo.setRepeatSendType((String)resultMap.get("repeatSendType"));
			massMailInfo.setRepeatSendStartDate(DateUtils.getStringDate(String.valueOf(resultMap.get("repeatSendStartDate"))));
			massMailInfo.setRepeatSendEndDate(DateUtils.getStringDate(String.valueOf(resultMap.get("repeatSendEndDate"))));			
			massMailInfo.setRepeatSendWeek((String)resultMap.get("repeatSendWeek"));
			massMailInfo.setRepeatSendDay(Integer.parseInt(String.valueOf(resultMap.get("repeatSendDay"))));
			

		
			//반복발송 주가 있다면 
			if(massMailInfo.getRepeatSendWeek()!=null && !massMailInfo.getRepeatSendWeek().equals("")){
				for(int i=1;i<=7;i++){
					if(massMailInfo.getRepeatSendWeek().indexOf("1")!=-1){
						massMailInfo.setRepeatSendWeekSun("1");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("2")!=-1){
						massMailInfo.setRepeatSendWeekMon("2");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("3")!=-1){
						massMailInfo.setRepeatSendWeekTue("3");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("4")!=-1){
						massMailInfo.setRepeatSendWeekWed("4");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("5")!=-1){
						massMailInfo.setRepeatSendWeekThu("5");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("6")!=-1){
						massMailInfo.setRepeatSendWeekFri("6");
					}
					if(massMailInfo.getRepeatSendWeek().indexOf("7")!=-1){
						massMailInfo.setRepeatSendWeekSat("7");
					}
				} // end for

			}// end if 
	
			
		}else{
			return massMailInfo;
		}
		return massMailInfo;
	}
	
	
	/**
	 * <p>체크된 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByChecked(int massmailID, String[] scheduleIDs) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deleteschedulebychecked");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));

		String sqlcols = "";
		for(int i=0;i<scheduleIDs.length;i++){
			if(i==scheduleIDs.length-1){
				sqlcols +=scheduleIDs[i]+")";
			}else{
				sqlcols+=scheduleIDs[i]+",";
			}
		}
		
		sql += sqlcols;	
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>기간내 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByDate(int massmailID, String fromDate, String toDate) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.deleteschedulebydate");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		return getSimpleJdbcTemplate().update(sql, param);
		
	}
	
	
	
	/**
	 * <p>반복메일 스케줄리스트 삭제시 가장 마지막것으로 업데이트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRepeatSendEndDate(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updaterepeatenddate");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		return getSimpleJdbcTemplate().update(sql, param);
	}

	/**
	 * <p>사용자아이디에 해당되는 보내는 사람리스트 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Sender> selectSenderByUserID(String groupID, String userID, String defaultYN) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectsender");		
		
		if(defaultYN!=null && defaultYN.equals("Y")){
			sql+=" AND s.defaultYN='Y' ";
		}

		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {		
				Sender sender = new Sender();
				sender.setSenderName(rs.getString("senderName"));
				sender.setSenderEmail(rs.getString("senderEmail"));
				
				return sender;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("groupID", groupID);	
		param.put("userID", userID);		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>반복메일 삭제시 남은 스케줄이 있는지 체크하기 위해
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkMassmailSchedule(int massmailID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.checkmasmailschedule");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>대상자 예상카운트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int expectTargetTotalCount(int massmailID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.expecttargetcount");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>대량메일 삭제 (deleteYN = 'Y')
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailScheduleDeleteYN(int massmailID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updatedeleteyn");			
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", massmailID);
		param.put("scheduleID", scheduleID);
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	/**
	 * <p>대량메일 리스트 상태값 확인 
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public MassMailList getMassmailState(String massmail_id, String schedule_id)  throws DataAccessException{
		
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectmassmailmtate");		
		Map<String, Object> resultMap = null;
		MassMailList massMailList = new MassMailList();
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", new Integer(massmail_id));
		param.put("scheduleID", new Integer(schedule_id));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			massMailList.setState(String.valueOf(resultMap.get("state"))); 
			massMailList.setSendCount(Integer.parseInt(String.valueOf(resultMap.get("sendTotalCount"))));
			massMailList.setSuccessCount(Integer.parseInt(String.valueOf(resultMap.get("successTotal"))));
			

			
		}
		return massMailList;
	}
	/**
	 * <p>사용자를 정보를 받아온다 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public User getUserInfo(String userID) throws DataAccessException{
		
		User user = new User();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.getuserinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);	
		
			
			//SQL문이 실행된다. 
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);		
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				user.setUserName((String)(resultMap.get("userName")));
				user.setEmail((String)(resultMap.get("email")));
			}else{
				user.setUserID("");
			}
		return user;		
	}
	
	/**
	 * <p>Backup Date 정보를 받아온다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<BackupDate> getBackupDate() throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.backupdate");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {		
				BackupDate backupDate = new BackupDate();
				backupDate.setBackupYearMonth(rs.getString("backupYearMonth"));
				return backupDate;
			}
		};
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	
	
	/**
	 * <p>발송 상태 변경
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int massmailID, int scheduleID, String state) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.updateState");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("state", state);
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	@SuppressWarnings("unchecked")
	public List<FilterManager> getFilter() throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectmassmailfilter");		

		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {		
				FilterManager filterManager = new FilterManager();
				filterManager.setFilterType(rs.getInt("filterType"));
				filterManager.setContent(rs.getString("content"));
				filterManager.setContentType(rs.getInt("contentType"));
				filterManager.setFilterLevel(rs.getInt("filterLevel"));
				
				
				return filterManager;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
			
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>이미지 파일 리스트 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ImageFile> listImageFile(String userID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectimagefile");		
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				ImageFile imageFile = new ImageFile();
				imageFile.setFileKey(rs.getString("fileKey"));
				imageFile.setFileSize(rs.getString("fileSize"));
				imageFile.setFileName(rs.getString("fileName"));
				imageFile.setFilePath(rs.getString("filePath"));
				imageFile.setRegistDate(rs.getString("registDate"));

				return imageFile;
			}			
		};		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID",userID);
		return  getSimpleJdbcTemplate().query(sql, rowMapper,param);
	}
	
	/**
	 * <p>이미지 파일 업로드 
	 * @param imageFile
	 * @return
	 * @throws DataAccessException
	 */
	public int insertImageFile(ImageFile imageFile) throws DataAccessException{
		String sql =""; 
		sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.insertimagefile");
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("userID", imageFile.getUserID());
		param.put("fileName", imageFile.getFileName());
		param.put("fileKey", imageFile.getFileKey());
		param.put("filePath", imageFile.getFilePath());
		param.put("fileSize", imageFile.getFileSize());
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>이미지파일 디비에서 삭제 
	 * @param fileKeys
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteImageFile(String[] fileKeys) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.deleteimagefile"); 
		String sqlcols = "";
		for(int i=0;i<fileKeys.length;i++){
		
			
			if(i==fileKeys.length-1){
				sqlcols +="'"+fileKeys[i]+"')";
			}else{
				sqlcols+="'"+fileKeys[i]+"',";
			}
		}		
		sql=sql+sqlcols;

		return getSimpleJdbcTemplate().update(sql);
	}
	
	/**
	 * 동일한 이름의 이미지파일이 있는지 확인	
	 * @param fileName
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ImageFile> isExistImageFile(String fileName, String userID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.getimagefile");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				ImageFile imageFile = new ImageFile();
				imageFile.setFileKey(rs.getString("fileKey"));
				imageFile.setFileSize(rs.getString("fileSize"));
				imageFile.setFileName(rs.getString("fileName"));
				imageFile.setFilePath(rs.getString("filePath"));
				imageFile.setRegistDate(rs.getString("registDate"));

				return imageFile;
			}			
		};		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("fileName", fileName);
		param.put("userID", userID);
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper,param);
	}
	
	/**
	 * <p>발송할 대량메일 리스트 - 우선순위 관리창에서 사용 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailPriority> getMassMailList() throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.getMassMailList");			
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassMailPriority massMailPriority = new MassMailPriority();
				//@rank:=@rank+1 rn, s.massmailID, s.scheduleID, s.priority, m.massmailTitle, m.sendType, s.sendScheduleDate, s.targetTotalCount, s.sendTotalCount, s.state \
				massMailPriority.setRownum(rs.getInt("rn"));
				massMailPriority.setMassmailID(rs.getInt("massmailID"));
				massMailPriority.setScheduleID(rs.getInt("scheduleID"));
				massMailPriority.setPriority(rs.getInt("priority"));
				massMailPriority.setMassmailTitle(rs.getString("massmailTitle"));
				massMailPriority.setSendType(rs.getString("sendType"));
				massMailPriority.setSendScheduleDate(DateUtils.getStringDate(rs.getString("sendScheduleDate")));
				massMailPriority.setTargetTotalCount(rs.getInt("targetTotalCount"));
				massMailPriority.setSendTotalCount(rs.getInt("sendTotalCount"));
				massMailPriority.setState(rs.getString("state"));
				
				return massMailPriority;
			}			
		};		
	
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	/**
	 * <p>우선순위 저장 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updatePriority(Map<String, Object>[] maps) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.write.updatepriority"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}
	

	/**
	 * <p>발송결과리스트 - 상태 값 변경시 비교할 현재 DB 상태 값 추출
	 * @param massmailID
	 * @param schdeduleID
	 * @return
	 * @throws DataAccessException
	 */
	
	public String getMassMailState(int massmailID, int scheduleID) throws DataAccessException{
		Map<String,Object> resultMap = null;
		
		String result = "";
		String sql = QueryUtil.getStringQuery("massmail_sql", "massmail.write.getMassMailState");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", massmailID);
		param.put("scheduleID", scheduleID);
		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		} catch (EmptyResultDataAccessException e) {
		}
		
		if(resultMap != null){
			result = (String)resultMap.get("state");
		}
		
		return result;
	}
	
	
	
	
	/**
	 * <p>수신거부 링크 리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailLink> listMassMailRejectLink(int massmailID) throws DataAccessException{		

		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.write.selectMailRejcetLink");		

		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {		
				MassMailLink massMailLink = new MassMailLink();
				massMailLink.setLinkID(rs.getInt("linkID"));
				massMailLink.setMassmailID(rs.getInt("massmailID"));
				massMailLink.setLinkName(rs.getString("linkName"));
				massMailLink.setLinkURL(rs.getString("linkURL"));				
				massMailLink.setLinkCount(rs.getInt("linkCount"));
				massMailLink.setLinkDesc(rs.getString("linkDesc"));
				massMailLink.setLinkType(rs.getString("linkType"));
				return massMailLink;
			}
		};		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massmailID", new Integer(massmailID));		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
}
