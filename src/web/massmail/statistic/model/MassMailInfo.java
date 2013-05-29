package web.massmail.statistic.model;

import java.io.Serializable;



/**
 * <p>ez_massmail_info : 대량메일 작성중 기본정보 : 자세한 정보는 테이블내역서를 보시오.
 * @author 임영호
 * @date 2009-07-02
 */
@SuppressWarnings("serial")
public class MassMailInfo  implements Serializable{
	
	private int massmailID=0;
	private int scheduleID = 0;
	private int massmailGroupID=0;
	private String massmailTitle="";
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
	

	//ez_massmail_filterset
	private String duplicationYN = "N";			//중복발송여부 기본 N
	private String sendedType="";				//발송된 타입 
	private String sendedYear="";
	private String sendedMonth="";
	private int sendedCount=0;
	private String rejectType="";
	private int persistErrorCount=0;
	
	//ez_massmail_mail
	private String senderName="";
	private String senderMail="";
	private String receiverName="";
	private String returnMail="";
	private String replyMail="";
	private String encodingType="EUC-KR"; //디폴트 
	private String mailType="text/html"; //디폴트 
	private String mailTitle="";
	private String mailLinkYN = "N";		//메일링크수집여부 
	private String mailContent="";
	private float mailContentSize;    //메일 템플릿 크기
	private String fileName="";
	private String webURL="";
	private String webURLType="";
	private int pollID=0;
	private int avgSendCount=0; //평균발송통수
	private int targetTotalCount = 0; //예상발송통수

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
	private int retryCount = 0;
	private String retryStartTime = "";
	private String retryEndTime = "";
	private String retryFinishYN = "";
	private String backupYearMonth= "";
	private int sendTotal = 0;
	private int successTotal = 0;
	private int failTotal = 0;
	private int openTotal = 0;
	private int clickTotal = 0;
	private int rejectcallTotal = 0;
	public int getMassmailID() {
		return massmailID;
	}
	public void setMassmailID(int massmailID) {
		this.massmailID = massmailID;
	}
	public int getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}
	public int getMassmailGroupID() {
		return massmailGroupID;
	}
	public void setMassmailGroupID(int massmailGroupID) {
		this.massmailGroupID = massmailGroupID;
	}
	public String getMassmailTitle() {
		return massmailTitle;
	}
	public void setMassmailTitle(String massmailTitle) {
		this.massmailTitle = massmailTitle;
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
	public String getDuplicationYN() {
		return duplicationYN;
	}
	public void setDuplicationYN(String duplicationYN) {
		this.duplicationYN = duplicationYN;
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
	public int getPersistErrorCount() {
		return persistErrorCount;
	}
	public void setPersistErrorCount(int persistErrorCount) {
		this.persistErrorCount = persistErrorCount;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderMail() {
		return senderMail;
	}
	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReturnMail() {
		return returnMail;
	}
	public void setReturnMail(String returnMail) {
		this.returnMail = returnMail;
	}
	public String getReplyMail() {
		return replyMail;
	}
	public void setReplyMail(String replyMail) {
		this.replyMail = replyMail;
	}
	public String getEncodingType() {
		return encodingType;
	}
	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}
	public String getMailType() {
		return mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getMailLinkYN() {
		return mailLinkYN;
	}
	public void setMailLinkYN(String mailLinkYN) {
		this.mailLinkYN = mailLinkYN;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getWebURL() {
		return webURL;
	}
	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}
	public String getWebURLType() {
		return webURLType;
	}
	public void setWebURLType(String webURLType) {
		this.webURLType = webURLType;
	}
	public int getPollID() {
		return pollID;
	}
	public void setPollID(int pollID) {
		this.pollID = pollID;
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
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public String getRetryStartTime() {
		return retryStartTime;
	}
	public void setRetryStartTime(String retryStartTime) {
		this.retryStartTime = retryStartTime;
	}
	public String getRetryEndTime() {
		return retryEndTime;
	}
	public void setRetryEndTime(String retryEndTime) {
		this.retryEndTime = retryEndTime;
	}
	public String getRetryFinishYN() {
		return retryFinishYN;
	}
	public void setRetryFinishYN(String retryFinishYN) {
		this.retryFinishYN = retryFinishYN;
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
	public int getOpenTotal() {
		return openTotal;
	}
	public void setOpenTotal(int openTotal) {
		this.openTotal = openTotal;
	}
	public int getClickTotal() {
		return clickTotal;
	}
	public void setClickTotal(int clickTotal) {
		this.clickTotal = clickTotal;
	}
	public int getRejectcallTotal() {
		return rejectcallTotal;
	}
	public void setRejectcallTotal(int rejectcallTotal) {
		this.rejectcallTotal = rejectcallTotal;
	}
	public float getMailContentSize() {
		return mailContentSize;
	}
	public void setMailContentSize(float mailContentSize) {
		this.mailContentSize = mailContentSize;
	}
	public int getAvgSendCount() {
		return avgSendCount;
	}
	public void setAvgSendCount(int avgSendCount) {
		this.avgSendCount = avgSendCount;
	}
	/**
	 * @return the targetTotalCount
	 */
	public int getTargetTotalCount() {
		return targetTotalCount;
	}
	/**
	 * @param targetTotalCount the targetTotalCount to set
	 */
	public void setTargetTotalCount(int targetTotalCount) {
		this.targetTotalCount = targetTotalCount;
	}
	
	
	
}









