package web.masssms.write.model;

import java.io.Serializable;


@SuppressWarnings("serial")
public class MassSMSList  implements Serializable{

	private int masssmsID;	
	private String masssmsTitle="";
	private String sendType="";
	private String sendTypeDesc="";
	private int scheduleID;
	private int targetCount=0;
	private int sendCount=0;
	private String sendScheduleDate="";
	private String sendStartTime="";
	private String sendEndTime="";
	private String state="";
	private String userID="";
	private String userName="";
	private String groupID="";
	private String groupName="";
	private String priority="";
	private String registDate="";	
	private String repeatSendType="";
	private String repeatSendTypeDesc="";
	private String repeatSendStartDate="";
	private String repeatSendEndDate="";
	
	private String approveUserID ="";
	
	
	
	public void setMasssmsID(int masssmsID){
		this.masssmsID = masssmsID;
	}
	public int getMasssmsID(){
		return masssmsID;
	}

	
	public void setMasssmsTitle(String masssmsTitle){
		this.masssmsTitle = masssmsTitle;
	}
	public String getMasssmsTitle(){
		return masssmsTitle;
	}
	
	public void setState(String state){
		this.state = state;
	}
	public String getState(){
		return state;
	}
		
	public void setSendType(String sendType){
		this.sendType = sendType;
	}
	public String getSendType(){
		return sendType;
	}
	
	public void setSendTypeDesc(String sendTypeDesc){
		this.sendTypeDesc = sendTypeDesc;
	}
	public String getSendTypeDesc(){
		return sendTypeDesc;
	}
	
	public void setScheduleID(int scheduleID){
		this.scheduleID = scheduleID;
	}
	public int getScheduleID(){
		return scheduleID;
	}

	
	public void setSendScheduleDate(String sendScheduleDate){
		this.sendScheduleDate = sendScheduleDate;
	}
	public String getSendScheduleDate(){
		return sendScheduleDate;
	}
	
	public void setSendStartTime(String sendStartTime){
		this.sendStartTime = sendStartTime;
	}
	public String getSendStartTime(){
		return sendStartTime;
	}
	
	public void setSendEndTime(String sendEndTime){
		this.sendEndTime = sendEndTime;
	}
	public String getSendEndTime(){
		return sendEndTime;
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserName(){
		return userName;
	}
	
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getGroupName(){
		return groupName;
	}
	
	public void setTargetCount(int targetCount){
		this.targetCount = targetCount;
	}
	public int getTargetCount(){
		return targetCount;
	}
	
	public void setSendCount(int sendCount){
		this.sendCount = sendCount;
	}
	public int getSendCount(){
		return sendCount;
	}
	
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}	
	public String getRegistDate(){
		return registDate;
	}
	
	public void setRepeatSendType(String repeatSendType){
		this.repeatSendType = repeatSendType;
	}
	
	public String getRepeatSendType(){
		return repeatSendType;
	}
	
	public void setRepeatSendTypeDesc(String repeatSendTypeDesc){
		this.repeatSendTypeDesc = repeatSendTypeDesc;
	}
	
	public String getRepeatSendTypeDesc(){
		return repeatSendTypeDesc;
	}
	
	public void setRepeatSendStartDate(String repeatSendStartDate){
		this.repeatSendStartDate = repeatSendStartDate;
	}
	public String getRepeatSendStartDate(){
		return repeatSendStartDate;
	}
	
	public void setRepeatSendEndDate(String repeatSendEndDate){
		this.repeatSendEndDate = repeatSendEndDate;
	}
	public String getRepeatSendEndDate(){
		return repeatSendEndDate;
	}
	public String getApproveUserID() {
		return approveUserID;
	}
	public void setApproveUserID(String approveUserID) {
		this.approveUserID = approveUserID;
	}
	
}
