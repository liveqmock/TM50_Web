package web.masssms.write.model;

import java.io.Serializable;


/**
 * <p>시스템공지 메일(혹은 테스트메일)이나 sms 전송
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class SystemNotify implements Serializable{
	
	
	private int notifyID = 0;
	private String notifyFlag ;
	private String notifyType="2"; 	//1:이메일, 2:SMS
	private String userID;
	private String senderMail;
	private String senderName;
	private String receiverMail;
	private String receiverName;
	private String returnMail;
	private String encodingType="EUC-KR";
	private String mailType;
	private String mailTitle;
	private String mailContent;
	private String smtpCode;
	private String smtpMsg;
	private String smtpResult;
	private String senderPhone;
	private String receiverPhone;
	private String smsContent;
	private String smsCode;
	private String smsCodeMsg;
	private String wasSended;
	private String registDate;
	
	
	public void setNotifyID(int notifyID){
		this.notifyID = notifyID;
	}
	public int getNotifyID(){
		return notifyID;
	}
	
	public void setNotifyFlag(String notifyFlag){
		this.notifyFlag = notifyFlag;
	}
	public String getNotifyFlag(){
		return notifyFlag;
	}
	
	public void setNotifyType(String notifyType){
		this.notifyType = notifyType;
	}
	public String getNotifyType(){
		return notifyType;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setSenderMail(String senderMail){
		this.senderMail = senderMail;
	}
	public String getSenderMail(){
		return senderMail;
	}
	
	public void setSenderName(String senderName){
		this.senderName = senderName;
	}
	public String getSenderName(){
		return senderName;
	}


	public void setReceiverMail(String receiverMail){
		this.receiverMail = receiverMail;
	}
	public String getReceiverMail(){
		return receiverMail;
	}
	
	public void setReceiverName(String receiverName){
		this.receiverName = receiverName;
	}
	public String getReceiverName(){
		return receiverName;
	}
	
	public String getReturnMail() {
		return returnMail;
	}
	public void setReturnMail(String returnMail) {
		this.returnMail = returnMail;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setEncodingType(String encodingType){
		this.encodingType = encodingType;
	}
	public String getEncodingType(){
		return encodingType;
	}
	
	public void setMailType(String mailType){
		this.mailType = mailType;
	}
	public String getMailType(){
		return mailType;
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
	
	public void setSmtpResult(String smtpResult){
		this.smtpResult = smtpResult;
	}
	public String getSmtpResult(){
		return smtpResult;
	}
	
	public void setSenderPhone(String senderPhone){
		this.senderPhone = senderPhone;
	}
	public String getSenderSender(){
		return senderPhone;
	}
	
	public void setReceiverPhone(String receiverPhone){
		this.receiverPhone = receiverPhone;
	}
	public String getReceiverPhone(){
		return receiverPhone;
	}
	
	public void setSmsContent(String smsContent){
		this.smsContent = smsContent;
	}
	public String getSmsContent(){
		return smsContent;
	}
	
	public void setSmsCode(String smsCode){
		this.smsCode = smsCode;
	}
	public String getSmsCode(){
		return smsCode;
	}
	
	public void setSmsCodeMsg(String smsCodeMsg){
		this.smsCodeMsg = smsCodeMsg;
	}
	public String getSmsCodeMsg(){
		return smsCodeMsg;
	}
	
	public void setWasSended(String wasSended){
		this.wasSended = wasSended;
	}
	public String getWasSended(){
		return wasSended;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
}
