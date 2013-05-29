package web.masssms.write.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.admin.usergroup.model.User;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.Constant;
import web.common.util.DateUtils;
import web.common.util.QueryUtil;
import web.common.util.ThunderUtil;
import web.masssms.write.model.TargetingGroup;
import web.masssms.write.model.*;
import web.masssms.write.dao.MassSMSDAO;


public class MassSMSDAOImpl extends DBJdbcDaoSupport implements MassSMSDAO{
	
	/**
	 * <p>Backup Date 정보를 받아온다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<BackupDate> getBackupDate() throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.getBackupDate");
				
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
	 * <p>유저별 등록된 테스트핸드폰을 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> listTesterHp(String userID,String testerHp) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listTesterHp");		
		
		
		String sqlwhere ="";
		if(testerHp!=null && !testerHp.equals("") ){
			sqlwhere = " AND testerHp='"+testerHp+"'";
		}
		
		sqlwhere +=" AND testerHp !=''";
		sql = sql+sqlwhere;
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("testerHp",rs.getString("testerHp"));
				resultMap.put("testerName",rs.getString("testerName"));				
				return resultMap;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
	}
	
	
	/**
	 *  <p>대량SMS 기본작성
	 * @param MassSMSInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSInfo(MassSMSInfo massSMSInfo)  throws DataAccessException{	
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.insertMassSMSInfo");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("masssmsTitle", massSMSInfo.getMasssmsTitle());
		param.put("description", massSMSInfo.getDescription());
		param.put("userID", massSMSInfo.getUserID());		
		param.put("sendType", massSMSInfo.getSendType());		
		param.put("statisticsOpenType", massSMSInfo.getStatisticsOpenType());;		
		param.put("repeatSendType", massSMSInfo.getRepeatSendType());
		param.put("repeatSendStartDate", massSMSInfo.getRepeatSendStartDate());
		param.put("repeatSendEndDate", massSMSInfo.getRepeatSendEndDate());
		param.put("repeatSendDay", massSMSInfo.getRepeatSendDay());
		param.put("repeatSendWeek", massSMSInfo.getRepeatSendWeek());
		param.put("approveUserID", massSMSInfo.getApproveUserID());
		param.put("memo", massSMSInfo.getMemo());
		param.put("priority", massSMSInfo.getPriority());
		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>가장 최근에 입력된 masssmsID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassSMSIDInfo() throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.write.getMassSMSIDInfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	
	/**
	 * <p>대량SMS 타게팅 그룹등록 
	 * @param masssmsID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSTargetGroup(int masssmsID, int targetID,  String exceptYN) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.insertMassSMSTargetGroup");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		param.put("targetID", new Integer(targetID));	
		param.put("exceptYN", exceptYN);
		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>대상자 예상카운트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int expectTargetTotalCount(int masssmsID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.expectTargetTotalCount");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>대량SMS 스케줄등록 
	 * @param massMailInfo 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSSchedule(MassSMSSchedule massSMSSchedule) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.insertMassSMSSchedule");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID",massSMSSchedule.getMasssmsID());
		param.put("scheduleID",massSMSSchedule.getScheduleID());
		param.put("sendScheduleDate", massSMSSchedule.getSendScheduleDate());		
		param.put("statisticsEndDate", massSMSSchedule.getstatisticsEndDate());
		param.put("targetTotalCount", massSMSSchedule.getTargetTotalCount());
		param.put("state", massSMSSchedule.getState());
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>대량SMS 필터링등록 
	 * @param massMailInfo 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSFilterSet(MassSMSInfo massSMSInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.insertMassSMSFilterSet");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID",massSMSInfo.getMasssmsID());
		param.put("sendedType", massSMSInfo.getSendedType());
		param.put("sendedYear", massSMSInfo.getSendedYear());
		param.put("sendedMonth", massSMSInfo.getSendedMonth());
		param.put("sendedCount", massSMSInfo.getSendedCount());
		param.put("rejectType", massSMSInfo.getRejectType());	
		param.put("duplicationYN", massSMSInfo.getDuplicationYN());	
	
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>대량SMS 메일내용 입력 
	 * @param massMailInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSSMS(MassSMSInfo massSMSInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.insertMassSMSSMS");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID",massSMSInfo.getMasssmsID());
		param.put("senderPhone",massSMSInfo.getSenderPhone());
		param.put("msgType",massSMSInfo.getMsgType());
		param.put("smsMsg",massSMSInfo.getSmsMsg());		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	/**
	 * <p>기본정보를 삭제한다.
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSInfo(int masssmsID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteMassSMSInfo");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>대상그룹삭제 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSTargetGroup(int masssmsID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteMassSMSTargetGroup");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>필터설정삭제
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSFilterSet(int masssmsID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteMassSMSFilterSet");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>SMS정보삭제
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSSMS(int masssmsID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteMassSMSSMS");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));		
		return getSimpleJdbcTemplate().update(sql, param);	
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
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.write.getUserInfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
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
	 * <p>시스템sms에 입력한다.
	 * @param systemNotify
	 * @return
	 * @throws DataAccessException
	 */
	public int insertSystemNotify(SystemNotify systemNotify) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.insert.insertSystemNotify");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("notifyFlag",systemNotify.getNotifyFlag());
		param.put("notifyType",systemNotify.getNotifyType());
		param.put("userID",systemNotify.getUserID());
		param.put("senderPhone",systemNotify.getSenderPhone());		
		param.put("receiverPhone",systemNotify.getReceiverPhone());
		param.put("smsContent",systemNotify.getSmsContent());
		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>테스트 전송SMS 리스트 
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
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listSystemNotify");		
		
		
		//검색조건이 있다면		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE '%" + searchText + "%' ";
		}		
			
		String sqlTail = QueryUtil.getStringQuery("masssms_sql","masssms.write.selectnotifytail");			
		sql += sqlTail;
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				SystemNotify systemNotify = new SystemNotify();
				
				systemNotify.setNotifyID(rs.getInt("notifyID"));
				systemNotify.setUserID(rs.getString("userID"));
				systemNotify.setReceiverPhone(rs.getString("receiverPhone"));				
				systemNotify.setWasSended(rs.getString("wasSended"));
				systemNotify.setSmsCode(rs.getString("smsCode"));
				systemNotify.setSmsCodeMsg(rs.getString("smsCodeMsg"));		
				systemNotify.setRegistDate(rs.getString("registDate"));
				return systemNotify;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("notifyFlag",notifyFlag);
		param.put("userID",userID);
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
	public int totalCountNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.totalCountNotify");		
		
		String searchType = searchMap.get("sSearchType");
		String searchText = searchMap.get("sSearchText");
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE '%" + searchText + "%' ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("notifyFlag",notifyFlag);
		param.put("userID",userID);
		
		return getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	/**
	 * <p>대량SMS 수정 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSInfo(MassSMSInfo massSMSInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.updateMassSMSInfo");		
		Map<String,Object> param = new HashMap<String, Object>();			
		param.put("masssmsTitle", massSMSInfo.getMasssmsTitle());
		param.put("description", massSMSInfo.getDescription());		
		param.put("modifyUserID", massSMSInfo.getModifyUserID());
		param.put("modifyDate", massSMSInfo.getModifyDate());
		param.put("sendType", massSMSInfo.getSendType());		
		param.put("repeatSendType", massSMSInfo.getRepeatSendType());
		param.put("repeatSendStartDate", massSMSInfo.getRepeatSendStartDate());
		param.put("repeatSendEndDate", massSMSInfo.getRepeatSendEndDate());
		param.put("repeatSendDay", massSMSInfo.getRepeatSendDay());
		param.put("repeatSendWeek", massSMSInfo.getRepeatSendWeek());
		param.put("approveUserID", massSMSInfo.getApproveUserID());
		param.put("memo", massSMSInfo.getMemo());
		param.put("statisticsOpenType", massSMSInfo.getStatisticsOpenType());	
		param.put("masssmsID", massSMSInfo.getMasssmsID());
		param.put("priority",massSMSInfo.getPriority());
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>스케줄일괄삭제
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSSchedule(int masssmsID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteMassSMSSchedule");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));			
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>스케줄삭제
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSSchedule(int masssmsID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteMassSMSSchedule");		
		sql+= " AND scheduleID="+scheduleID;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>대량SMS 필터링수정 
	 * @param masssmsID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSFilterSet(MassSMSInfo massSMSInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.updateMassSMSFilterSet");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID",massSMSInfo.getMasssmsID());
		param.put("sendedType", massSMSInfo.getSendedType());
		param.put("sendedYear", massSMSInfo.getSendedYear());
		param.put("sendedMonth", massSMSInfo.getSendedMonth());
		param.put("sendedCount", massSMSInfo.getSendedCount());
		param.put("rejectType", massSMSInfo.getRejectType());	
		param.put("duplicationYN", massSMSInfo.getDuplicationYN());
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>대량메일 메일내용 수정
	 * @param massMailInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSSMS(MassSMSInfo massSMSInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.updateMassSMSSMS");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID",massSMSInfo.getMasssmsID());
		param.put("senderPhone",massSMSInfo.getSenderPhone());
		param.put("msgType",massSMSInfo.getMsgType());
		param.put("smsMsg",massSMSInfo.getSmsMsg());		
		return getSimpleJdbcTemplate().update(sql, param);	
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
	public List<MassSMSList> listMassSMSList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String sendScheduleDateStart = searchMap.get("sendScheduleDateStart");
		String sendScheduleDateEnd = searchMap.get("sendScheduleDateEnd");
		String sendType = searchMap.get("sendType");
		String groupID = searchMap.get("groupID");		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listMassSMSList");		
		
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
		
		sql += " AND s.sendScheduleDate >= '"+ sendScheduleDateStart +"' AND s.sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE '%" + searchText + "%' ";
		}		
		
		if(sendType!=null && !sendType.equals("")){
			sql += " AND m.sendType='"+sendType+"' ";
		}
		if(groupID!=null && !groupID.equals("")){
			sql += " AND u.groupID='"+groupID+"' ";
		}	
			
		
		String sqlTail = QueryUtil.getStringQuery("masssms_sql","masssms.write.selecttail");			
		sql += sqlTail;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassSMSList massSMSList = new MassSMSList();
				
				massSMSList.setMasssmsID(rs.getInt("masssmsID"));				
				massSMSList.setMasssmsTitle(rs.getString("masssmsTitle"));
				massSMSList.setScheduleID(rs.getInt("scheduleID"));
				massSMSList.setSendStartTime(rs.getString("sendStartTime"));
				massSMSList.setSendScheduleDate(DateUtils.getStringDate(rs.getString("sendScheduleDate")));
				massSMSList.setSendEndTime(rs.getString("sendEndTime"));
				massSMSList.setState(rs.getString("state"));
				massSMSList.setSendType(rs.getString("sendType"));
				massSMSList.setApproveUserID(rs.getString("approveUserID"));
				massSMSList.setSendTypeDesc(ThunderUtil.descSendType(rs.getString("sendType")));
				massSMSList.setTargetCount(rs.getInt("targetTotalCount"));
				massSMSList.setSendCount(rs.getInt("sendTotalCount"));
				massSMSList.setUserID(rs.getString("userID"));
				massSMSList.setUserName(rs.getString("userName"));
				massSMSList.setGroupID(rs.getString("groupID"));
				massSMSList.setGroupName(rs.getString("groupName"));
				massSMSList.setPriority(rs.getString("priority"));				
				massSMSList.setRegistDate(rs.getString("registDate"));
				
				return massSMSList;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>대량 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountMassSMSList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.totalCountMassSMSList");		
		
		String searchType = searchMap.get("sSearchType");
		String searchText = searchMap.get("sSearchText");
		String sendScheduleDateStart = searchMap.get("sendScheduleDateStart");
		String sendScheduleDateEnd = searchMap.get("sendScheduleDateEnd");
		String sendType = searchMap.get("sendType");
		String groupID = searchMap.get("groupID");
		
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
			sql += " AND  "+searchType+" LIKE '%" + searchText + "%' ";
		}		
		
		if(sendType!=null && !sendType.equals("")){
			sql += " AND m.sendType='"+sendType+"' ";
		}
		
		if(groupID!=null && !groupID.equals("")){
			sql += " AND u.groupID='"+groupID+"' ";
		}	
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<SMSTargetingGroup> listTargetingGroup(int masssmsID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listTargetingGroup");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				SMSTargetingGroup smsTargetingGroup = new SMSTargetingGroup();
				smsTargetingGroup.setTargetGroupID(rs.getInt("targetGroupID"));
				smsTargetingGroup.setTargetID(rs.getInt("targetID"));
				smsTargetingGroup.setTargetName(rs.getString("targetName"));
				smsTargetingGroup.setTargetCount(rs.getInt("targetCount"));
				smsTargetingGroup.setTargetType(rs.getString("targetType"));
				smsTargetingGroup.setMasssmsID(rs.getInt("masssmsID"));
				smsTargetingGroup.setExceptYN(rs.getString("exceptYN"));	
				return smsTargetingGroup;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("masssmsID", new Integer(masssmsID));

		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	
	/**
	 * <p>대량SMS저장내용보기 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo viewMassSMSInfo(int masssmsID, int scheduleID) throws DataAccessException{
		MassSMSInfo massSMSInfo = new MassSMSInfo();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.write.viewMassSMSInfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
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
			massSMSInfo.setMasssmsID(Integer.parseInt(String.valueOf(resultMap.get("masssmsID"))));
			massSMSInfo.setMasssmsTitle((String)resultMap.get("masssmsTitle"));			
			massSMSInfo.setDescription((String)resultMap.get("description"));
			massSMSInfo.setSendType((String)resultMap.get("sendType"));			
			massSMSInfo.setStatisticsOpenType((String)resultMap.get("statisticsOpenType"));
			massSMSInfo.setRepeatSendType((String)resultMap.get("repeatSendType"));
			
			
			//반복발송일 경우 
			if(massSMSInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){
				massSMSInfo.setRepeatSendStartDate(String.valueOf(resultMap.get("repeatSendStartDate")));
				massSMSInfo.setRepeatSendEndDate(String.valueOf(resultMap.get("repeatSendEndDate")));			
				massSMSInfo.setRepeatSendTimeHH(String.valueOf(resultMap.get("repeatSendTimeHH")));
				massSMSInfo.setRepeatSendTimeMM(String.valueOf(resultMap.get("repeatSendTimeMM")));
				massSMSInfo.setRepeatSendWeek((String)resultMap.get("repeatSendWeek"));
				massSMSInfo.setRepeatSendDay(Integer.parseInt(String.valueOf(resultMap.get("repeatSendDay"))));
			}else{
				massSMSInfo.setRepeatSendStartDate("");
				massSMSInfo.setRepeatSendEndDate("");			
				massSMSInfo.setRepeatSendTimeHH("");
				massSMSInfo.setRepeatSendTimeMM("");
				massSMSInfo.setRepeatSendWeek("");
				massSMSInfo.setRepeatSendDay(0);			
			}

			
			
			massSMSInfo.setMemo((String)resultMap.get("memo"));
			massSMSInfo.setUserID((String)resultMap.get("userID"));
			massSMSInfo.setUserName((String)resultMap.get("userName"));
			massSMSInfo.setGroupID((String)resultMap.get("groupID"));
			massSMSInfo.setApproveUserID((String)resultMap.get("approveUserID"));
			massSMSInfo.setApproveDate(String.valueOf(resultMap.get("approveDate")));
			massSMSInfo.setPriority(String.valueOf(resultMap.get("priority")));
			massSMSInfo.setSenderPhone((String)resultMap.get("senderPhone"));			
			massSMSInfo.setMsgType((String)resultMap.get("msgType"));
			massSMSInfo.setSmsMsg((String)resultMap.get("smsMsg"));
			massSMSInfo.setRejectType((String)(resultMap.get("rejectType")));
			massSMSInfo.setDuplicationYN((String)(resultMap.get("duplicationYN")));
			massSMSInfo.setSendedCount(Integer.parseInt(String.valueOf(resultMap.get("sendedCount"))));
			massSMSInfo.setSendedMonth((String)(resultMap.get("sendedMonth")));
			massSMSInfo.setSendedType((String)(resultMap.get("sendedType")));
			massSMSInfo.setSendedYear((String)(resultMap.get("sendedYear")));
			massSMSInfo.setScheduleID(Integer.parseInt(String.valueOf(resultMap.get("scheduleID"))));
			massSMSInfo.setSendScheduleDate(String.valueOf(resultMap.get("sendScheduleDate")));
			massSMSInfo.setSendScheduleDateHH(String.valueOf(resultMap.get("sendScheduleDateHH")));
			massSMSInfo.setSendScheduleDateMM(String.valueOf(resultMap.get("sendScheduleDateMM")));
			massSMSInfo.setState((String)(resultMap.get("state")));
			
		
			//반복발송 주가 있다면 
			if(massSMSInfo.getRepeatSendWeek()!=null && !massSMSInfo.getRepeatSendWeek().equals("")){
				for(int i=1;i<=7;i++){
					if(massSMSInfo.getRepeatSendWeek().indexOf("1")!=-1){
						massSMSInfo.setRepeatSendWeekSun("1");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("2")!=-1){
						massSMSInfo.setRepeatSendWeekMon("2");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("3")!=-1){
						massSMSInfo.setRepeatSendWeekTue("3");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("4")!=-1){
						massSMSInfo.setRepeatSendWeekWed("4");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("5")!=-1){
						massSMSInfo.setRepeatSendWeekThu("5");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("6")!=-1){
						massSMSInfo.setRepeatSendWeekFri("6");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("7")!=-1){
						massSMSInfo.setRepeatSendWeekSat("7");
					}
				} // end for

			}// end if 
	
			
		}else{
			return massSMSInfo;
		}
		return massSMSInfo;
	}
	
	
	/**
	 * <p>필터링삭제
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSFiltering(int masssmsID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteMassSMSFiltering");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));		
		param.put("scheduleID", new Integer(scheduleID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>통계삭제
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSStatistic(int masssmsID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteMassSMSStatistic");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));		
		param.put("scheduleID", new Integer(scheduleID));		
		return getSimpleJdbcTemplate().update(sql, param);	
	}


	/**
	 * <p>대량SMS 삭제 (deleteYN = 'Y')
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSScheduleDeleteYN(int masssmsID, int scheduleID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.updateMassSMSScheduleDeleteYN");					
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("masssmsID", masssmsID);
		param.put("scheduleID", scheduleID);
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>발송 상태 변경
	 * @param masssmsID
	 * @param scheduleID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int masssmsID, int scheduleID, String state) throws DataAccessException{		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.updateSendState");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("state", state);
		param.put("masssmsID", new Integer(masssmsID));
		param.put("scheduleID", new Integer(scheduleID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>DB에 쿼리정보를 가져온다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getQeuryDB(int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.getQeuryDB");		
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
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listOnetooneTarget");	
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
	 * <p>시스템SMS삭제(즉, 테스트SMS)
	 * @param notifyIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSystemNotify(String[] notifyIDs) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteSystemNotify"); 
	
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
	 * <p>대량SMS 리스트 상태값 확인 
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 */
	public MassSMSList getMasssmsState(String masssmsID, String scheduleID)  throws DataAccessException{		
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.getMasssmsState");		
		Map<String, Object> resultMap = null;
		MassSMSList massSMSList = new MassSMSList();
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("masssmsID", new Integer(masssmsID));
		param.put("scheduleID", new Integer(scheduleID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			massSMSList.setState(String.valueOf(resultMap.get("state")));
			massSMSList.setSendCount(Integer.parseInt(String.valueOf(resultMap.get("sendTotalCount"))));			
		}
		return massSMSList;
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
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listTargeting");		
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
			sql += " AND "+searchType+" LIKE '%" + searchName + "%' ";
		}	
		//검색에 맞게 조회한다.		
		if(bookMark!=null && !bookMark.equals("")){
			sql += " AND t.bookMark='"+bookMark+"'";
		}else{
			sql += " AND t.bookMark NOT IN('D') ";   //D는 삭제된 것이므로 제외한다.
		}
		
		String sqlTail = QueryUtil.getStringQuery("masssms_sql","masssms.write.targettail");			
		sql += sqlTail;
		
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
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.write.getTargetingTotalCount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
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
			sql += " AND "+searchType+" LIKE '%" + searchName + "%' ";
		}
		//검색에 맞게 조회한다.		
		if(bookMark!=null && !bookMark.equals("")){
			sql += " AND t.bookMark='"+bookMark+"'";
		}else{
			sql += " AND t.bookMark NOT IN('D') ";   //D는 삭제된 것이므로 제외한다.
		}
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>반복SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */

	public int totalCountMassSMSRepeat(String[] userInfo, Map<String, String> searchMap) throws DataAccessException{
		
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");

		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.totalCountMassSMSRepeat");		
		
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
			sql += " AND  "+searchType+" LIKE '%" + searchText + "%' ";
		}		

		return  getSimpleJdbcTemplate().queryForInt(sql);
		
	}
	
	/**
	 * <p>반복SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MassSMSList> listMassSMSRepeat(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException{
		
		
		int start = (currentPage - 1) * countPerPage;
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String repeatSendType = searchMap.get("repeatSendType");
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listMassSMSRepeat");		
		
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
			sql += " AND  "+searchType+" LIKE '%" + searchText + "%' ";
		}		

		if(!repeatSendType.equals("")){
			sql += " AND repeatSendType LIKE '%" + repeatSendType + "%' ";
		}
		
		String sqlTail = QueryUtil.getStringQuery("masssms_sql","masssms.write.selectrepeatmailtail");			
		sql += sqlTail;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassSMSList massSMSList = new MassSMSList();
				
				massSMSList.setMasssmsID(rs.getInt("masssmsID"));
				massSMSList.setMasssmsTitle(rs.getString("masssmsTitle"));				
				massSMSList.setUserName(rs.getString("userName"));
				massSMSList.setRepeatSendType(rs.getString("repeatSendType"));
				massSMSList.setRepeatSendTypeDesc(ThunderUtil.descRepeatSendType(rs.getString("repeatSendType")));
				massSMSList.setRepeatSendStartDate(rs.getString("repeatSendStartDate"));
				massSMSList.setRepeatSendEndDate(rs.getString("repeatSendEndDate"));

	
						
				return massSMSList;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
	}
	
	/**
	 * <p>반복SMS 정보보기 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo viewRepeatMassSMSInfo(int masssmsID) throws DataAccessException{
		MassSMSInfo massSMSInfo = new MassSMSInfo();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("masssms_sql","masssms.write.viewRepeatMassSMSInfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massSMSInfo.setMasssmsID(Integer.parseInt(String.valueOf(resultMap.get("masssmsID"))));
			massSMSInfo.setMasssmsTitle((String)resultMap.get("masssmsTitle"));
			massSMSInfo.setUserID((String)resultMap.get("userID"));
			massSMSInfo.setRepeatSendType((String)resultMap.get("repeatSendType"));
			massSMSInfo.setRepeatSendStartDate(String.valueOf(resultMap.get("repeatSendStartDate")));
			massSMSInfo.setRepeatSendEndDate(String.valueOf(resultMap.get("repeatSendEndDate")));			
			massSMSInfo.setRepeatSendWeek((String)resultMap.get("repeatSendWeek"));
			massSMSInfo.setRepeatSendDay(Integer.parseInt(String.valueOf(resultMap.get("repeatSendDay"))));
			

		
			//반복발송 주가 있다면 
			if(massSMSInfo.getRepeatSendWeek()!=null && !massSMSInfo.getRepeatSendWeek().equals("")){
				for(int i=1;i<=7;i++){
					if(massSMSInfo.getRepeatSendWeek().indexOf("1")!=-1){
						massSMSInfo.setRepeatSendWeekSun("1");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("2")!=-1){
						massSMSInfo.setRepeatSendWeekMon("2");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("3")!=-1){
						massSMSInfo.setRepeatSendWeekTue("3");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("4")!=-1){
						massSMSInfo.setRepeatSendWeekWed("4");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("5")!=-1){
						massSMSInfo.setRepeatSendWeekThu("5");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("6")!=-1){
						massSMSInfo.setRepeatSendWeekFri("6");
					}
					if(massSMSInfo.getRepeatSendWeek().indexOf("7")!=-1){
						massSMSInfo.setRepeatSendWeekSat("7");
					}
				} // end for

			}// end if 
	
			
		}else{
			return massSMSInfo;
		}
		return massSMSInfo;
	}
	
	
	/**
	 * <p>반복SMS스케즐카운트 
	 * @param massmailID
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountRepeatSchedule(int masssmsID, Map<String, String> searchMap) throws DataAccessException{
		String sendScheduleDateStart = searchMap.get("sendScheduleDateStart");
		String sendScheduleDateEnd = searchMap.get("sendScheduleDateEnd");
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.totalCountRepeatSchedule");				
		
		//검색조건이 있다면		
		sql += " AND sendScheduleDate >= '"+ sendScheduleDateStart +"' AND sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("masssmsID", new Integer(masssmsID));
		
		return  getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	/**
	 * <p>반복SMS 스케줄리스트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public  List<MassSMSList> listRepeatSchedule(int masssmsID, int currentPage, int countPerPage, Map<String, String> searchMap)  throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String sendScheduleDateStart = searchMap.get("sendScheduleDateStart");
		String sendScheduleDateEnd = searchMap.get("sendScheduleDateEnd");
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.listRepeatSchedule");		
		
		
		//검색조건이 있다면		
		sql += " AND sendScheduleDate >= '"+ sendScheduleDateStart +"' AND sendScheduleDate <= '"+ sendScheduleDateEnd +"' ";

			
		String sqlTail = QueryUtil.getStringQuery("masssms_sql","masssms.write.listRepeatScheduleTail");			
		sql += sqlTail;		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MassSMSList massSMSList = new MassSMSList();
				massSMSList.setMasssmsID(rs.getInt("masssmsID"));
				massSMSList.setScheduleID(rs.getInt("scheduleID"));
				massSMSList.setSendStartTime(rs.getString("sendStartTime"));
				massSMSList.setSendScheduleDate(rs.getString("sendScheduleDate"));
				massSMSList.setState(rs.getString("state"));
			
				
				return massSMSList;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("masssmsID", new Integer(masssmsID));
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>체크된 반복SMS 스케즐리스트 삭제 
	 * @param masssmsID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByChecked(int masssmsID, String[] scheduleIDs) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteRepeatScheduleByChecked");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));

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
	 * <p>기간내 반복SMS 스케즐리스트 삭제 
	 * @param masssmsID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByDate(int masssmsID, String fromDate, String toDate) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.deleteRepeatScheduleByDate");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		return getSimpleJdbcTemplate().update(sql, param);
		
	}
	
	
	
	/**
	 * <p>반복SMS 스케줄리스트 삭제시 가장 마지막것으로 업데이트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRepeatSendEndDate(int masssmsID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.updateRepeatSendEndDate");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>반복SMS 삭제시 남은 스케줄이 있는지 체크하기 위해
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkMasssmsSchedule(int masssmsID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.checkMasssmsSchedule");		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("masssmsID", new Integer(masssmsID));
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TargetingGroup> listTargetingGroup(String target_ids) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("masssms_sql","masssms.write.selecttargetgroupids");
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
	
}
