package web.massmail.write.model;

import java.io.Serializable;


/**
 * <p>ez_massmail_schedule : 대량메일 발송정보 
 * @author 김유근
 * @date 2009-07-02
 */
@SuppressWarnings("serial")
public class MassMailSchedule implements Serializable{
	
	//ez_massmail_schedule
	private int massmailID;
	private int scheduleID;
	private String sendScheduleDate = "";		//발송일	
	private String state="";
	private String statisticsEndDate="";
	private int targetTotalCount = 0;
	
	
	public void setScheduleID(int scheduleID){
		this.scheduleID = scheduleID;
	}
	public int getScheduleID(){
		return scheduleID;
	}
	
	public void setMassmailID(int massmailID){
		this.massmailID = massmailID;
	}
	public int getMassmailID(){
		return massmailID;
	}
	
	public void setSendScheduleDate(String sendScheduleDate){
		this.sendScheduleDate = sendScheduleDate;
	}
	public String getSendScheduleDate(){
		return sendScheduleDate;
	}
	
	
	public void setState(String state){
		this.state = state;
	}
	public String getState(){
		return state;
	}
	
	public void setStatisticsEndDate(String statisticsEndDate){
		this.statisticsEndDate = statisticsEndDate;
	}
	public String getstatisticsEndDate(){
		return statisticsEndDate;
	}
	

	public void setTargetTotalCount(int targetTotalCount){
		this.targetTotalCount = targetTotalCount;
	}
	public int getTargetTotalCount(){
		return targetTotalCount;
	}
}
