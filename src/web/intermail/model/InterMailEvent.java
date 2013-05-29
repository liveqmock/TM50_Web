package web.intermail.model;

import java.io.Serializable;


/**
 * <p>tm_intermail_event, tm_intermail_schedule
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class InterMailEvent  implements Serializable {
	
	private int intermailID;
	private int scheduleID;
	private String intermailTitle ;
	private String userID;
	private String userName;
	private String description;
	private String templateTitle;
	private String templateContent;
	private String templateSenderMail;
	private String templateSenderName;
	private String templateReceiverName;	
	private String returnMail;
	private String encodingType;	
	private String secretYN;				//보안메일사용여부
	private String registDate;
	private String state;
	private String fileKey;
	private String templateFileName;
	private String templateFileContent;
	private String sendType;			//1:수동발송, 2:자동발송
	private String repeatGroupType;	//반복치환 사용선택 1:모두사용안함, 2:첨부파일에만 사용, 3:메일 본문에만 사용, 4:모두사용 
	private String resultYearMonth;
	
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
	
	public void setIntermailTitle(String intermailTitle){
		this.intermailTitle = intermailTitle;
	}
	public String getIntermailTitle(){
		return intermailTitle;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserName(){
		return userName;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	
	public void setTemplateTitle(String templateTitle){
		this.templateTitle = templateTitle;
	}
	public String getTemplateTitle(){
		return templateTitle;
	}
	
	public void setTemplateContent(String templateContent){
		this.templateContent = templateContent;
	}
	public String getTemplateContent(){
		return templateContent;
	}
	
	public void setTemplateSenderMail(String templateSenderMail){
		this.templateSenderMail = templateSenderMail;
	}
	public String getTemplateSenderMail(){
		return templateSenderMail;
	}
	
	public void setTemplateSenderName(String templateSenderName){
		this.templateSenderName = templateSenderName;
	}
	public String getTemplateSenderName(){
		return templateSenderName;
	}

	public void setTemplateReceiverName(String templateReceiverName){
		this.templateReceiverName = templateReceiverName;
	}
	public String getTemplateReceiverName(){
		return templateReceiverName;
	}	

	
	public void setReturnMail(String returnMail){
		this.returnMail = returnMail;
	}
	public String getReturnMail(){
		return returnMail;
	}

	public void setEncodingType(String encodingType){
		this.encodingType = encodingType;
	}
	public String getEncodingType(){
		return encodingType;
	}
	
	public void setSecretYN(String secretYN){
		this.secretYN = secretYN;
	}
	public String getSecretYN(){
		return secretYN;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	
	public void setState(String state){
		this.state = state;
	}
	public String getState(){
		return state;
	}
	
	public void setFileKey(String fileKey){
		this.fileKey = fileKey;
	}
	public String getFileKey(){
		return fileKey;
	}
	
	public void setTemplateFileName(String templateFileName){
		this.templateFileName = templateFileName;
	}
	public String getTemplateFileName(){
		return templateFileName;
	}
	
	
	public void setTemplateFileContent(String templateFileContent){
		this.templateFileContent = templateFileContent;
	}
	public String getTemplateFileContent(){
		return templateFileContent;
	}
	
	public void setSendType(String sendType){
		this.sendType = sendType;
	}
	public String getSendType(){
		return sendType;
	}
	
	public void setRepeatGroupType(String repeatGroupType){
		this.repeatGroupType = repeatGroupType;
	}
	public String getRepeatGroupType(){
		return repeatGroupType;
	}
	
	public void setResultYearMonth(String resultYearMonth){
		this.resultYearMonth = resultYearMonth;
	}
	public String getResultYearMonth(){
		return resultYearMonth;
	}
	
}
