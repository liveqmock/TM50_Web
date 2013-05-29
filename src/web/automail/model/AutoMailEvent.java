package web.automail.model;


/**
 * <p>ez_automail_event
 * @author 김유근
 *
 */
public class AutoMailEvent {
	
	private int automailID;
	private String automailTitle;
	private String userID;
	private String userName;
	private String description;
	private String templateTitle;
	private String templateContent;
	private String templateSenderMail;
	private String templateSenderName;
	private String templateReceiverName;
	private String returnMail;
	private String replyMail;
	private String fileName;
	private String fileContent;
	private String encodingType;
	private String state;
	private String uploadKey;
	private String registDate;
	private String lastSendDate;
	private String repeatGroupType;
	
	public void setAutomailID(int automailID){
		this.automailID = automailID;
	}
	public int getAutomailID(){
		return automailID;
	}
	
	public void setAutomailTitle(String automailTitle){
		this.automailTitle = automailTitle;
	}
	public String getAutomailTitle(){
		return automailTitle;
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
	
	public void setReplyMail(String replyMail){
		this.replyMail = replyMail;
	}
	public String getReplyMail(){
		return replyMail;
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
	
	public void setEncodingType(String encodingType){
		this.encodingType = encodingType;
	}
	public String getEncodingType(){
		return encodingType;
	}
	
	public void setState(String state){
		this.state = state;
	}
	public String getState(){
		return state;
	}
	
	public void setUploadKey(String uploadKey){
		this.uploadKey = uploadKey;
	}
	public String getUploadKey(){
		return uploadKey;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	public String getLastSendDate() {
		return lastSendDate;
	}
	public void setLastSendDate(String lastSendDate) {
		this.lastSendDate = lastSendDate;
	}
	/**
	 * @return the repeatGroupType
	 */
	public String getRepeatGroupType() {
		return repeatGroupType;
	}
	/**
	 * @param repeatGroupType the repeatGroupType to set
	 */
	public void setRepeatGroupType(String repeatGroupType) {
		this.repeatGroupType = repeatGroupType;
	}
	
	
	
}
