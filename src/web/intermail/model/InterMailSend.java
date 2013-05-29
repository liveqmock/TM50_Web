package web.intermail.model;

import java.io.Serializable;



/**
 * <p>tm_intemail_send_ 테이블정보
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class InterMailSend implements Serializable{

	private int intermailID;
	private int scheduleID;
	private String sendID;
	private String email;
	private String domainName;
	private String senderName;
	private String senderMail;
	private String receiverName;
	private String mailTitle;
	private String mailContent;
	private String onetooneInfo;
	private String fileName;
	private String fileContent;
	private String repeatGroupType;
	private String registDate;
	
	
	public void setIntermailID(int intermailID){
		this.intermailID = intermailID;		
	}
	public int getIntermailID(){
		return intermailID;
	}
	
	public void setScheduleID(int scheduleID){
		this.scheduleID = scheduleID;
	}
	public int getScheduleID(){
		return scheduleID;
	}
	
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
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	
}
