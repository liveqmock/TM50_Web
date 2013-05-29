package web.massmail.write.model;

import java.io.Serializable;

public class MassMailPriority implements Serializable{
	
	//@rank:=@rank+1 rn, s.massmailID, s.scheduleID, s.priority, m.massmailTitle, m.sendType, s.sendScheduleDate, s.targetTotalCount, s.sendTotalCount, s.state

	private int rownum;
	private String massmailTitle="";
	private int massmailID;
	private int scheduleID;
	private int priority;
	private String sendType;
	private String state;
	private String sendScheduleDate;
	private int targetTotalCount;
	private int sendTotalCount;
	
	/**
	 * @return the rownum
	 */
	public int getRownum() {
		return rownum;
	}
	/**
	 * @param rownum the rownum to set
	 */
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	/**
	 * @return the massmailTitle
	 */
	public String getMassmailTitle() {
		return massmailTitle;
	}
	/**
	 * @param massmailTitle the massmailTitle to set
	 */
	public void setMassmailTitle(String massmailTitle) {
		this.massmailTitle = massmailTitle;
	}
	/**
	 * @return the massmailID
	 */
	public int getMassmailID() {
		return massmailID;
	}
	/**
	 * @param massmailID the massmailID to set
	 */
	public void setMassmailID(int massmailID) {
		this.massmailID = massmailID;
	}
	/**
	 * @return the scheduleID
	 */
	public int getScheduleID() {
		return scheduleID;
	}
	/**
	 * @param scheduleID the scheduleID to set
	 */
	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the sendType
	 */
	public String getSendType() {
		return sendType;
	}
	/**
	 * @param sendType the sendType to set
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the sendScheduleDate
	 */
	public String getSendScheduleDate() {
		return sendScheduleDate;
	}
	/**
	 * @param sendScheduleDate the sendScheduleDate to set
	 */
	public void setSendScheduleDate(String sendScheduleDate) {
		this.sendScheduleDate = sendScheduleDate;
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
	/**
	 * @return the sendTotalCount
	 */
	public int getSendTotalCount() {
		return sendTotalCount;
	}
	/**
	 * @param sendTotalCount the sendTotalCount to set
	 */
	public void setSendTotalCount(int sendTotalCount) {
		this.sendTotalCount = sendTotalCount;
	}
	
	
	
}
