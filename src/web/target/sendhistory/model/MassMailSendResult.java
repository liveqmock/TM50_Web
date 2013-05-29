package web.target.sendhistory.model;


import java.io.Serializable;


/**
 * <p>tm_massmail_send 정보 
 */
@SuppressWarnings("serial")
public class MassMailSendResult  implements Serializable{
	
	private String sendID ="";
	private String email = "";
	private String name = "";
	private String domainName = "";
	private int massmailID =0;
	private String massmailTitle="";
	private int scheduleID =0;
	private int massmailGroupID =0;
	private int pollID = 0;
	private int targetID = 0;
	private String serverID = "";
	private String userID = "";
	private String onetooneInfo = "";
	private String portalYN = "";
	private String wasSended = "";
	private String smtpCodeType = "";
	private String smtpCode = "";
	private String smtpMsg = "";
	private String customerID = "";
	private String openYN="";
	private String clickYN="";
	private String rejectcallYN="";	
	private String registDate="";

	private String failCauseCodeName="";
	private String openDate="";

	
	
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
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDomainName(String domainName){
		this.domainName = domainName;
	}
	public String getDomainName(){
		return domainName;
	}
	
	public void setMassmailID(int massmailID){
		this.massmailID = massmailID;
	}
	
	public int getMassmailID(){
		return massmailID;
	}
	
	
	public void setMassmailTitle(String massmailTitle){
		this.massmailTitle = massmailTitle;
	}
	
	public String getMassmailTitle(){
		return massmailTitle;
	}
	
	
	public void setScheduleID(int scheduleID){
		this.scheduleID = scheduleID;
	}
	
	public int getScheduleID(){
		return scheduleID;
	}
	
	public void setMassmailGroupID(int massmailGroupID){
		this.massmailGroupID = massmailGroupID;
	}
	
	public int getMassmailGroupID(){
		return massmailGroupID;
	}
	
	public void setTargetID(int targetID){
		this.targetID = targetID;
	}
	
	public int getTargetID(){
		return targetID;
	}
	
	public void setPollID(int pollID){
		this.pollID = pollID;
	}
	public int getPollID(){
		return pollID;
	}
	
	public void setPortalYN(String portalYN){
		this.portalYN = portalYN;
	}
	public String getPortalYN(){
		return portalYN;
	}
	
	public void setServerID(String serverID){
		this.serverID = serverID;
	}
	public String getServerID(){
		return serverID;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setOnetooneInfo(String onetooneInfo){
		this.onetooneInfo = onetooneInfo;
	}
	
	public String getOnetooneInfo(){
		return onetooneInfo;
	}
	
	public void setWasSended(String wasSended){
		this.wasSended = wasSended;
	}	
	public String getWasSended(){
		return wasSended;
	}
	
	public void setSmtpCodeType(String smtpCodeType){
		this.smtpCodeType = smtpCodeType;
	}	
	public String getSmtpCodeType(){
		return smtpCodeType;
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

	
	public void setCustomerID(String customerID){
		this.customerID = customerID;
	}	
	public String getCustomerID(){
		return customerID;
	}
	
	public void setOpenYN(String openYN){
		this.openYN = openYN;
	}
	public String getOpenYN(){
		return openYN;
	}
	
	public void setClickYN(String clickYN){
		this.clickYN = clickYN;
	}
	public String getClickYN(){
		return clickYN;
	}
	
	public void setRejectcallYN(String rejectcallYN){
		this.rejectcallYN = rejectcallYN;
	}
	public String getRejectcallYN(){
		return rejectcallYN;
	}

	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	public String getFailCauseCodeName() {
		return failCauseCodeName;
	}
	public void setFailCauseCodeName(String failCauseCodeName) {
		this.failCauseCodeName = failCauseCodeName;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	
	
}
