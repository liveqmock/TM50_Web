package web.target.sendhistory.dao;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import web.massmail.statistic.model.MassMailInfo;
import web.massmail.write.model.TargetingGroup;
import web.target.sendhistory.model.MassMailSendResult;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.*;



public class SendHistoryDAOImpl extends DBJdbcDaoSupport implements SendHistoryDAO{ 
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	
	public int totalCountMassMailSendResult(String[] userInfo, Map<String, String> searchMap) throws DataAccessException{
	
		String sendedYear = searchMap.get("sendedYear");
		String sendedMonth = searchMap.get("sendedMonth");
		
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");

		String sendYN = searchMap.get("sendYN");
		String openYN = searchMap.get("openYN");
		String clickYN = searchMap.get("clickYN");
		String rejectcallYN = searchMap.get("rejectcallYN");
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.sendhistory.totalcount");				
		String joinsql = QueryUtil.getStringQuery("targetlist_sql","target.sendhistory.join");		
		sql+=" tm_massmail_sendresult_"+sendedYear+sendedMonth+" s " ;
		sql+=joinsql;
		
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
		if(sendYN.equals("Y")){
			sql += " AND s.smtpCodeType=0 ";
		}else if(sendYN.equals("N")){
			sql += " AND s.smtpCodeType>0 ";
		}
		if(openYN!=null && !openYN.equals("")){
			sql += " AND  s.openYN='"+openYN+"' ";
		}
		if(clickYN!=null && !clickYN.equals("")){
			sql += " AND  s.clickYN='"+clickYN+"' ";
		}
		if(rejectcallYN!=null && !rejectcallYN.equals("")){
			sql += " AND  s.rejectcallYN='"+rejectcallYN+"' ";
		}

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MassMailSendResult> listMassMailSendResult(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap)  throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		
		String sendedYear = searchMap.get("sendedYear");
		String sendedMonth = searchMap.get("sendedMonth");
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String sendYN = searchMap.get("sendYN");
		String openYN = searchMap.get("openYN");
		String clickYN = searchMap.get("clickYN");
		String rejectcallYN = searchMap.get("rejectcallYN");
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.sendhistory.select");		
		String joinsql = QueryUtil.getStringQuery("targetlist_sql","target.sendhistory.join");
		String tailsql = QueryUtil.getStringQuery("targetlist_sql","target.sendhistory.tail");
		
		sql+=" tm_massmail_sendresult_"+sendedYear+sendedMonth+" s " ;
		sql+=joinsql;
		
		
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		
		//소속관리자라면 
		if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
			sql+= " AND u.groupID = '"+userInfo[2]+"'";
		}
		//일반사용자라면 
		else if(userInfo[0].equals(Constant.USER_LEVEL_USER)){
			sql+= " AND u.userID = '"+userInfo[1]+"'"; 	
		}	
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}	
		if(sendYN.equals("Y")){
			sql += " AND s.smtpCodeType=0 ";
		}else if(sendYN.equals("N")){
			sql += " AND s.smtpCodeType>0 ";
		}
		if(openYN!=null && !openYN.equals("")){
			sql += " AND  s.openYN='"+openYN+"' ";
		}
		if(clickYN!=null && !clickYN.equals("")){
			sql += " AND  s.clickYN='"+clickYN+"' ";
		}
		if(rejectcallYN!=null && !rejectcallYN.equals("")){
			sql += " AND  s.rejectcallYN='"+rejectcallYN+"' ";
		}
		sql+= tailsql;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassMailSendResult massMailSendResult = new MassMailSendResult();
				massMailSendResult.setEmail(rs.getString("email"));
				massMailSendResult.setName(ThunderUtil.getOnetooneValue(rs.getString("onetooneInfo"), Constant.ONETOONE_NAME_TAG));
				massMailSendResult.setMassmailID(rs.getInt("massmailID"));
				massMailSendResult.setScheduleID(rs.getInt("scheduleID"));
				massMailSendResult.setMassmailTitle(rs.getString("massmailTitle"));
				massMailSendResult.setSmtpCodeType(rs.getString("smtpCodeType"));
				massMailSendResult.setFailCauseCodeName(rs.getString("failCauseCodeName"));
				massMailSendResult.setSmtpMsg(rs.getString("smtpMsg"));
				massMailSendResult.setOpenYN(rs.getString("openYN"));
				massMailSendResult.setClickYN(rs.getString("clickYN"));
				massMailSendResult.setRejectcallYN(rs.getString("rejectcallYN"));
				massMailSendResult.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				massMailSendResult.setOpenDate(DateUtils.getStringDate(rs.getString("openDate")));
				massMailSendResult.setPollID(rs.getInt("pollID"));
				return massMailSendResult;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>대량메일 기본정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo viewMassMailInfo(int massmailID, int scheduleID) throws DataAccessException{
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
			massMailInfo.setMassmailID(massmailID);
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
			//massMailInfo.setHoursBetween(0);
		}
		
		return massMailInfo;
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

}
