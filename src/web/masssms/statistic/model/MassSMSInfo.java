package web.masssms.statistic.model;

import java.io.Serializable;



/**
 * <p>ez_massmail_info : 대량메일 작성중 기본정보 : 자세한 정보는 테이블내역서를 보시오.
 * @author 임영호
 * @date 2009-07-02
 */
@SuppressWarnings("serial")
public class MassSMSInfo  implements Serializable{
	
	private int masssmsID=0;
	private int scheduleID = 0;	
	private String masssmsTitle="";
	private String description="";
	private String userID;
	private String userName = ""; //ez_users에 userName
	private String groupID="";
	private String groupName="";

	private String sendType="1";  //발송타입  : 1: 즉시발송, 2:예약발송, 3:반복발송
	private String repeatSendType="";   //반복발송타입

	private String repeatSendTimeHH="";	//반복발송시간
	private String repeatSendTimeMM="";  //반복발송분 
	
	private int repeatSendDay=0;   //반복발송특정일
	private String repeatSendWeek="";		//반복발송주일
	private String statisticsOpenType="1";   //1:비공유, 2:소속공유, 3:전체공유
	private String memo="";
	private String modifyUserID="";
	private String modifyUserName="";
	private String modifyDate="";
	private String approveUserID="";			//승인아이디
	private String approveUserName="";		//승인자명
	private String approveDate="";				//승인된 날짜 
	private String registDate="";
	
	private String repeatSendStartDate = null;			//반복발송시작일
	private String repeatSendEndDate = null;			//반복발송종료일
	private String statisticsType="";
	
	private String sendScheduleDate="";		//발송일 
	private String saveState = "";				//00: 임시저장, 10: 승인대기중, 11: 발송준비대기중
	private String sendSchedule=""; 			//발송년월
	private String sendScheduleDateHH="";			//시간
	private String sendScheduleDateMM="";			//분 
	

	//tm_masssms_filterset
	
	private String sendedType="";				//발송된 타입 
	private String sendedYear="";
	private String sendedMonth="";
	private int sendedCount=0;
	private String rejectType="";
	
	
	//tm_masssms_sms
	private String senderPhone="";
	private String smsMsg="";

	private String repeatSendWeekSun = "";		//일 
	private String repeatSendWeekMon = "";		//월
	private String repeatSendWeekTue = "";		//화
	private String repeatSendWeekWed = "";		//수
	private String repeatSendWeekThu = "";		//목
	private String repeatSendWeekFri = "";		//금
	private String repeatSendWeekSat = "";		//금
	
	
	
	
	private String state = "";
	private String stateDesc = "";
	private String statisticsEndDate = "";
	private String prepareStartTime = "";
	private String prepareEndTime = "";
	private String sendStartTime = "";
	private String sendEndTime = "";
	
	private String backupYearMonth= "";
	private int sendTotal = 0;
	private int successTotal = 0;
	private int failTotal = 0;
	private int readyTotal = 0;
	
	
	public int getMasssmsID() {
		return masssmsID;
	}
	public void setMassmailID(int masssmsID) {
		this.masssmsID = masssmsID;
	}
	
	public int getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}
	
	public String getMasssmsTitle() {
		return masssmsTitle;
	}
	public void setMasssmsTitle(String masssmsTitle) {
		this.masssmsTitle = masssmsTitle;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
	public String getRepeatSendType() {
		return repeatSendType;
	}
	public void setRepeatSendType(String repeatSendType) {
		this.repeatSendType = repeatSendType;
	}
	
	public String getRepeatSendTimeHH() {
		return repeatSendTimeHH;
	}
	public void setRepeatSendTimeHH(String repeatSendTimeHH) {
		this.repeatSendTimeHH = repeatSendTimeHH;
	}
	
	public String getRepeatSendTimeMM() {
		return repeatSendTimeMM;
	}
	public void setRepeatSendTimeMM(String repeatSendTimeMM) {
		this.repeatSendTimeMM = repeatSendTimeMM;
	}
	
	public int getRepeatSendDay() {
		return repeatSendDay;
	}
	public void setRepeatSendDay(int repeatSendDay) {
		this.repeatSendDay = repeatSendDay;
	}
	
	public String getRepeatSendWeek() {
		return repeatSendWeek;
	}
	public void setRepeatSendWeek(String repeatSendWeek) {
		this.repeatSendWeek = repeatSendWeek;
	}
	
	public String getStatisticsOpenType() {
		return statisticsOpenType;
	}
	public void setStatisticsOpenType(String statisticsOpenType) {
		this.statisticsOpenType = statisticsOpenType;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getModifyUserID() {
		return modifyUserID;
	}
	
	public void setModifyUserID(String modifyUserID) {
		this.modifyUserID = modifyUserID;
	}
	public String getModifyUserName() {
		return modifyUserName;
	}
	
	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getApproveUserID() {
		return approveUserID;
	}
	
	public void setApproveUserID(String approveUserID) {
		this.approveUserID = approveUserID;
	}
	public String getApproveUserName() {
		return approveUserName;
	}
	public void setApproveUserName(String approveUserName) {
		this.approveUserName = approveUserName;
	}
	
	public String getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}
	
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	
	public String getRepeatSendStartDate() {
		return repeatSendStartDate;
	}
	public void setRepeatSendStartDate(String repeatSendStartDate) {
		this.repeatSendStartDate = repeatSendStartDate;
	}
	public String getRepeatSendEndDate() {
		return repeatSendEndDate;
	}
	
	public void setRepeatSendEndDate(String repeatSendEndDate) {
		this.repeatSendEndDate = repeatSendEndDate;
	}
	public String getStatisticsType() {
		return statisticsType;
	}
	
	public void setStatisticsType(String statisticsType) {
		this.statisticsType = statisticsType;
	}
	public String getSendScheduleDate() {
		return sendScheduleDate;
	}
	public void setSendScheduleDate(String sendScheduleDate) {
		this.sendScheduleDate = sendScheduleDate;
	}
	
	public String getSaveState() {
		return saveState;
	}
	public void setSaveState(String saveState) {
		this.saveState = saveState;
	}
	
	public String getSendSchedule() {
		return sendSchedule;
	}
	public void setSendSchedule(String sendSchedule) {
		this.sendSchedule = sendSchedule;
	}
	
	public String getSendScheduleDateHH() {
		return sendScheduleDateHH;
	}	
	public void setSendScheduleDateHH(String sendScheduleDateHH) {
		this.sendScheduleDateHH = sendScheduleDateHH;
	}
	
	public String getSendScheduleDateMM() {
		return sendScheduleDateMM;
	}
	public void setSendScheduleDateMM(String sendScheduleDateMM) {
		this.sendScheduleDateMM = sendScheduleDateMM;
	}
		
	public String getSendedType() {
		return sendedType;
	}
	public void setSendedType(String sendedType) {
		this.sendedType = sendedType;
	}
	public String getSendedYear() {
		return sendedYear;
	}
	public void setSendedYear(String sendedYear) {
		this.sendedYear = sendedYear;
	}
	public String getSendedMonth() {
		return sendedMonth;
	}
	public void setSendedMonth(String sendedMonth) {
		this.sendedMonth = sendedMonth;
	}
	public int getSendedCount() {
		return sendedCount;
	}
	public void setSendedCount(int sendedCount) {
		this.sendedCount = sendedCount;
	}
	public String getRejectType() {
		return rejectType;
	}
	public void setRejectType(String rejectType) {
		this.rejectType = rejectType;
	}

	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
		
	public String getSmsMsg() {
		return smsMsg;
	}
	public void setSmsMsg(String smsMsg) {
		this.smsMsg = smsMsg;
	}
	
	public String getRepeatSendWeekSun() {
		return repeatSendWeekSun;
	}
	public void setRepeatSendWeekSun(String repeatSendWeekSun) {
		this.repeatSendWeekSun = repeatSendWeekSun;
	}
	
	public String getRepeatSendWeekMon() {
		return repeatSendWeekMon;
	}
	public void setRepeatSendWeekMon(String repeatSendWeekMon) {
		this.repeatSendWeekMon = repeatSendWeekMon;
	}
	
	public String getRepeatSendWeekTue() {
		return repeatSendWeekTue;
	}
	public void setRepeatSendWeekTue(String repeatSendWeekTue) {
		this.repeatSendWeekTue = repeatSendWeekTue;
	}
	
	public String getRepeatSendWeekWed() {
		return repeatSendWeekWed;
	}
	public void setRepeatSendWeekWed(String repeatSendWeekWed) {
		this.repeatSendWeekWed = repeatSendWeekWed;
	}
	
	public String getRepeatSendWeekThu() {
		return repeatSendWeekThu;
	}
	public void setRepeatSendWeekThu(String repeatSendWeekThu) {
		this.repeatSendWeekThu = repeatSendWeekThu;
	}
	
	public String getRepeatSendWeekFri() {
		return repeatSendWeekFri;
	}
	public void setRepeatSendWeekFri(String repeatSendWeekFri) {
		this.repeatSendWeekFri = repeatSendWeekFri;
	}
	
	public String getRepeatSendWeekSat() {
		return repeatSendWeekSat;
	}
	public void setRepeatSendWeekSat(String repeatSendWeekSat) {
		this.repeatSendWeekSat = repeatSendWeekSat;
	}
	
	public String getState() {
		return state;
	}	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getStateDesc() {
		return stateDesc;
	}	
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}
	
	public String getStatisticsEndDate() {
		return statisticsEndDate;
	}	
	public void setStatisticsEndDate(String statisticsEndDate) {
		this.statisticsEndDate = statisticsEndDate;
	}
	
	public String getPrepareStartTime() {
		return prepareStartTime;
	}	
	public void setPrepareStartTime(String prepareStartTime) {
		this.prepareStartTime = prepareStartTime;
	}
	
	public String getPrepareEndTime() {
		return prepareEndTime;
	}	
	public void setPrepareEndTime(String prepareEndTime) {
		this.prepareEndTime = prepareEndTime;
	}
	
	public String getSendStartTime() {
		return sendStartTime;
	}	
	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}
	
	public String getSendEndTime() {
		return sendEndTime;
	}	
	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}
	
	public String getBackupYearMonth() {
		return backupYearMonth;
	}
	public void setBackupYearMonth(String backupYearMonth) {
		this.backupYearMonth = backupYearMonth;
	}
	
	public int getSendTotal() {
		return sendTotal;
	}
	public void setSendTotal(int sendTotal) {
		this.sendTotal = sendTotal;
	}
	
	public int getSuccessTotal() {
		return successTotal;
	}
	public void setSuccessTotal(int successTotal) {
		this.successTotal = successTotal;
	}
	
	public int getFailTotal() {
		return failTotal;
	}
	public void setFailTotal(int failTotal) {
		this.failTotal = failTotal;
	}
	
	public int getReadyTotal() {
		return readyTotal;
	}
	public void setReadyTotal(int readyTotal) {
		this.readyTotal = readyTotal;
	}
	

	
}









