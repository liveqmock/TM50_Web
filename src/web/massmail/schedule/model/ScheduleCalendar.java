package web.massmail.schedule.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ScheduleCalendar implements Serializable {
	
	private int MassMailID; 
	private int ScheduleID; 
	private String MassMailTitle; 
	private String UserID; 
	private String SendScheduleDate; 
	private int GroupID; 
	private String UserName; 
	private String UserLevel;
	private String description; 
	private int sendType;
	private String state;
	private String targetTotalCount;
	private int statisticsOpenType;
	
	
	public ScheduleCalendar() {
		
	}
	
	
	
	public int getMassMailID() {
		return MassMailID;
	}
	public void setMassMailID(int massMailID) {
		MassMailID = massMailID;
	}
	public int getScheduleID() {
		return ScheduleID;
	}
	public void setScheduleID(int scheduleID) {
		ScheduleID = scheduleID;
	}
	public String getMassMailTitle() {
		return MassMailTitle;
	}
	public void setMassMailTitle(String massMailTitle) {
		MassMailTitle = massMailTitle;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getSendScheduleDate() {
		return SendScheduleDate;
	}
	public void setSendScheduleDate(String sendScheduleDate) {
		SendScheduleDate = sendScheduleDate;
	}
	public int getGroupID() {
		return GroupID;
	}
	public void setGroupID(int groupID) {
		GroupID = groupID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserLevel() {
		return UserLevel;
	}
	public void setUserLevel(String userLevel) {
		UserLevel = userLevel;
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



	/**
	 * @return the statisticsOpenType
	 */
	public int getStatisticsOpenType() {
		return statisticsOpenType;
	}



	/**
	 * @param statisticsOpenType the statisticsOpenType to set
	 */
	public void setStatisticsOpenType(int statisticsOpenType) {
		this.statisticsOpenType = statisticsOpenType;
	}
	
	
	
	
	
	
	
	
	
	
	
}
