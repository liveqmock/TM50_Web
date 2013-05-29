package web.massmail.statistic.model;

import java.io.Serializable;


/**
 * <p>ez_massmail_sendtimestatistic
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class MassMailStatisticTime implements Serializable{
	
	private int massmailID = 0;
	private String yearDate = "";
	private String monthDate = "";
	private String dayDate = "";
	private String hourDate = "";
	private int sendCount = 0;
	private int successCount = 0;
	private int failCount = 0;
	
	public void setMassmailID(int massmailID){
		this.massmailID = massmailID;
	}
	public int getMassmailID(){
		return massmailID;
	}
	
	public void setYearDate(String yearDate){
		this.yearDate = yearDate;
	}
	public String getYearDate(){
		return yearDate;
	}
	
	public void setMonthDate(String monthDate){
		this.monthDate = monthDate;
	}
	public String getMonthDate(){
		return monthDate;
	}
	
	public void setDayDate(String dayDate){
		this.dayDate = dayDate;
	}
	public String getDayDate(){
		return dayDate;
	}
	
	public void setHourDate(String hourDate){
		this.hourDate = hourDate;
	}
	public String getHourDate(){
		return hourDate;
	}
	
	public void setSendCount(int sendCount){
		this.sendCount = sendCount;
	}
	public int getSendCount(){
		return sendCount;
	}
	
	public void setSuccessCount(int successCount){
		this.successCount = successCount;
	}
	public int getSuccessCount(){
		return successCount;
	}
		
	public void setFailCount(int failCount){
		this.failCount = failCount;
	}
	public int getFailCount(){
		return failCount;
	}
}
