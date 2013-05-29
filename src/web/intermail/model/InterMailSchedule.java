package web.intermail.model;

import java.io.Serializable;


/**
 * <p>tm_intermail_schedule 
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class InterMailSchedule implements Serializable {
	
	private int intermailID;
	private int scheduleID;
	private String sendType;		//1: 수동발송, 2;자동발송
	private String state;  //10: 발송대기중	11: 발송준비완료	12: 발송중	13: 발송완료	 21: 발송일시중지	22: 발송완전중지	40: 발송준비중에러 	41: 발송중에러	42: 오류자재발송중에러	
	private String sendScheduleDate;
	private String sendScheduleDateHH;
	private String sendScheduleDateMM;
	private String sendStartTime;
	private String sendEndTime;
	private String retryStartTime;
	private String retryEndTime;
	private int retryCount;				//재발송된 카운트 
	private String retryFinishYN;		//재발송 완료여부 
	private String statisticEndDate;		//통계마감일 
	private String statisticEndDateHH;
	private String statisticEndDateMM;	
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
	
	public void setSendType(String sendType){
		this.sendType = sendType;
	}
	public String getSendType(){
		return sendType;
	}
	
	public void setState(String state){
		this.state = state;
	}
	public String getState(){
		return state;
	}
	
	public void setSendScheduleDate(String sendScheduleDate){
		this.sendScheduleDate = sendScheduleDate;
	}
	public String getSendScheduleDate(){
		return sendScheduleDate;
	}
	
	public void setSendScheduleDateHH(String sendScheduleDateHH){
		this.sendScheduleDateHH = sendScheduleDateHH;
	}
	public String getSendScheduleDateHH(){
		return sendScheduleDateHH;
	}
	
	public void setSendScheduleDateMM(String sendScheduleDateMM){
		this.sendScheduleDateMM = sendScheduleDateMM;
	}
	public String getSendScheduleDateMM(){
		return sendScheduleDateMM;
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
	
	public void setRetryStartTime(String retryStartTime){
		this.retryStartTime = retryStartTime;
	}
	public String getRetryStartTime(){
		return retryStartTime;
	}
	
	public void setRetryEndTime(String retryEndTime){
		this.retryEndTime = retryEndTime;
	}
	public String getRetryEndTime(){
		return retryEndTime;
	}
	
	public void setRetryCount(int retryCount){
		this.retryCount = retryCount;
	}
	public int getRetryCount(){
		return retryCount;
	}
	
	public void setRetryFinishYN(String retryFinishYN){
		this.retryFinishYN = retryFinishYN;
	}
	public String getRetryFinishYN(){
		return retryFinishYN;
	}	
	
	public void setStatisticEndDate(String statisticEndDate){
		this.statisticEndDate = statisticEndDate;
	}
	public String getStatisticEndDate(){
		return statisticEndDate;
	}	
	
	public void setStatisticEndDateHH(String statisticEndDateHH){
		this.statisticEndDateHH = statisticEndDateHH;
	}
	public String getStatisticEndDateHH(){
		return statisticEndDateHH;
	}	
	
	public void setStatisticEndDateMM(String statisticEndDateMM){
		this.statisticEndDateMM = statisticEndDateMM;
	}
	public String getStatisticEndDateMM(){
		return statisticEndDateMM;
	}	

	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}


}
