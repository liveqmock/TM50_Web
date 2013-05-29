package web.intermail.model;

import java.io.Serializable;



/**
 * <p>tm_intemail_sendresult_YYYYMM, tm_intermail_schedule, tm_smtp_failType 조인 정보 
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class InterMailSendResult implements Serializable {

	
	private String sendID;
	private String email;
	private int intermailID;
	private String intermailTitle;
	private int scheduleID;
	private String mailTitle;
	private String mailContent;
	private String onetooneInfo;
	private String domainName;
	private String senderName;
	private String senderMail;
	private String receiverName;
	private String fileName;
	private String fileContent;
	private String repeatGroupType;
	private String smtpCodeType;
	private String smtpCode;
	private String smtpCodeTypeDesc;
	private String smtpMsg;
	private String failCauseCode;
	private String failCauseCodeDesc;
	private int retrySendCount;
	private String retryLastDate;
	private String openYN;
	private String openDate;
	private String openFileYN;
	private String openFileDate;
	private String registDate;
	private String failCauseCodeName;
	private String state;
	private String retryFinishYN;
	private int retryCount;
	private String statisticEndDate;
	
	
	public void setSendID(String sendID){
		this.sendID = sendID;
	}
	public String getSendID(){
		return sendID;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return email;
	}
		
	public void setIntermailID(int intermailID){
		this.intermailID = intermailID;
	}
	public int getIntermailID(){
		return intermailID;
	}
	
	public void setIntermailTitle(String intermailTitle){
		this.intermailTitle = intermailTitle;
	}
	public String getIntermailTitle(){
		return intermailTitle;
	}
	
	
	public void setScheduleID(int scheduleID){
		this.scheduleID = scheduleID;
	}
	public int getScheduleID(){
		return scheduleID;
	}
	
	public void setMailTitle(String mailTitle){
		this.mailTitle = mailTitle;
	}
	public String getMailTitle(){
		return mailTitle;
	}
	
	public void setMailContent(String mailContent){
		this.mailContent = mailContent;
	}
	public String getMailContent(){
		return mailContent;
	}
	
	public void setOnetooneInfo(String onetooneInfo){
		this.onetooneInfo = onetooneInfo;
	}
	public String getOnetooneInfo(){
		return onetooneInfo;
	}
	
	public void setDomainName(String domainName){
		this.domainName = domainName;
	}
	public String getDomainName(){
		return domainName;
	}
	
	public void setSenderName(String senderName){
		this.senderName = senderName;
	}
	public String getSenderName(){
		return senderName;
	}
	
	public void setSenderMail(String senderMail){
		this.senderMail = senderMail;
	}
	public String getSenderMail(){
		return senderMail;
	}
	
	public void setReceiverName(String receiverName){
		this.receiverName = receiverName;
	}
	public String getReceiverName(){
		return receiverName;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	public String getFileName(){
		return fileName;
	}
	
	public void setFileContent(String fileContent){
		this.fileContent = fileContent;
	}
	public String getFileContent(){
		return fileContent;
	}
	
	public void setRepeatGroupType(String repeatGroupType){
		this.repeatGroupType = repeatGroupType;
	}
	public String getRepeatGroupType(){
		return repeatGroupType;
	}
	
	public void setSmtpCodeType(String smtpCodeType){
		this.smtpCodeType = smtpCodeType;
	}
	public String getSmtpCodeType(){
		return smtpCodeType;
	}
	
	public void setSmtpCodeTypeDesc(String smtpCodeTypeDesc){
		this.smtpCodeTypeDesc = smtpCodeTypeDesc;
	}
	public String getSmtpCodeTypeDesc(){
		return smtpCodeTypeDesc;
	}
	
	public void setSmtpCode(String smtpCode){
		this.smtpCode = smtpCode;
	}
	public String getSmtpCode(){
		return smtpCode;
	}
	
	public void setSmtpMsg(String smtpMsg){
		this.smtpMsg = smtpMsg;
	}
	public String getSmtpMsg(){
		return smtpMsg;
	}
	
	public void setFailCauseCode(String failCauseCode){
		this.failCauseCode = failCauseCode;
	}
	public String getFailCauseCode(){
		return failCauseCode;
	}
	
	public void setFailCauseCodeDesc(String failCauseCodeDesc){
		this.failCauseCodeDesc = failCauseCodeDesc;
	}
	public String getFailCauseCodeDesc(){
		return failCauseCodeDesc;
	}
	
	public void setRetrySendCount(int retrySendCount){
		this.retrySendCount = retrySendCount;
	}
	public int getRetrySendCount(){
		return retrySendCount;
	}
	
	public void setRetryLastDate(String retryLastDate){
		this.retryLastDate = retryLastDate;
	}
	public String getRetryLastDate(){
		return retryLastDate;
	}
	
	public void setOpenYN(String openYN){
		this.openYN = openYN;
	}
	public String getOpenYN(){
		return openYN;
	}
	
	public void setOpenDate(String openDate){
		this.openDate = openDate;
	}
	public String getOpenDate(){
		return openDate;
	}
	
	public void setOpenFileYN(String openFileYN){
		this.openFileYN = openFileYN;
	}
	public String getOpenFileYN(){
		return openFileYN;
	}
	
	public void setOpenFileDate(String openFileDate){
		this.openFileDate = openFileDate;
	}
	public String getOpenFileDate(){
		return openFileDate;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	
	public void setFailCauseCodeName(String failCauseCodeName){
		this.failCauseCodeName = failCauseCodeName;
	}
	public String getFailCauseCodeName(){
		return failCauseCodeName;
	}
	
	public void setState(String state){
		this.state = state;
	}
	public String getState(){
		return state;
	}
	
	public void setRetryFinishYN(String retryFinishYN){
		this.retryFinishYN = retryFinishYN;
	}
	public String getRetryFinishYN(){
		return retryFinishYN;
	}
	
	public void setRetryCount(int retryCount){
		this.retryCount = retryCount;
	}
	public int getRetryCount(){
		return retryCount;
	}
	
	public void setStatisticEndDate(String statisticEndDate){
		this.statisticEndDate = statisticEndDate;
	}
	public String getStatisticEndDate(){
		return statisticEndDate;
	}
}
