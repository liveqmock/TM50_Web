package web.masssms.schedule.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ScheduleCalendar implements Serializable {
	
	private int masssmsID; 
	private int scheduleID; 
	private String masssmsTitle; 
	private String userID; 
	private String sendScheduleDate; 
	private int groupID; 
	private String userName; 
	private String userLevel;
	private String description; 
	private int sendType;
	private String state;
	private String targetTotalCount;
	
	
	public int getMassMailID() {
		return masssmsID;
	}
	public void setMasssmsID(int masssmsID) {
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
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getSendScheduleDate() {
		return sendScheduleDate;
	}
	public void setSendScheduleDate(String sendScheduleDate) {
		this.sendScheduleDate = sendScheduleDate;
	}
	
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getTargetTotalCount() {
		return targetTotalCount;
	}
	public void setTargetTotalCount(String targetTotalCount) {
		this.targetTotalCount = targetTotalCount;
	}
	
	
	
	
	
	
	
	
	
	
	
}
