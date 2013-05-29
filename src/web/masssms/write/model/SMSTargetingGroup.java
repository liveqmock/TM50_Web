package web.masssms.write.model;

import java.io.Serializable;


/**
 * <p>tm_massmail_targetgroup의 정보 
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class SMSTargetingGroup implements Serializable{

	private int targetGroupID;				//대상자그룹아이디
	private int masssmsID;					//대량SMS아이디
	private int targetID;					
	private String targetName;				//대상자그룹명 
	private String targetType;				//대상자등록타입 : 1: CSV, 2:QUERY
	private String dbID;							//쿼리에 해당하는 DBID
	private String queryText;
	private int targetCount=0;
	private int priorNum = 1;					//우선순위 
	private String exceptYN="N";			//제외여부 
	private String registDate="";
	
	public void setTargetGroupID(int targetGroupID){
		this.targetGroupID = targetGroupID;
	}
	public int getTargetGroupID(){
		return targetGroupID;
	}
	
	public void setMasssmsID(int masssmsID){
		this.masssmsID = masssmsID;
	}
	public int getMasssmsID(){
		return masssmsID;
	}
	
	public void setTargetID(int targetID){
		this.targetID = targetID;
	}
	public int getTargetID(){
		return targetID;
	}
	
	public void setTargetName(String targetName){
		this.targetName = targetName;
	}
	public String getTargetName(){
		return targetName;
	}
	
	public void setTargetType(String targetType){
		this.targetType = targetType;
	}
	public String getTargetType(){
		return targetType;
	}
	
	public void setDbID(String dbID){
		this.dbID = dbID;
	}
	public String getDbID(){
		return dbID;
	}
	
	public void setQueryText(String queryText){
		this.queryText = queryText;
	}
	public String getQueryText(){
		return queryText;
	}
	
	public void setTargetCount(int targetCount){
		this.targetCount = targetCount;
	}
	public int getTargetCount(){
		return targetCount;
	}
	
	public void setPriorNum(int priorNum){
		this.priorNum = priorNum;
	}
	public int getPriorNum(){
		return priorNum;
	}
	
	public void setExceptYN(String exceptYN){
		this.exceptYN = exceptYN;
	}
	public String getExceptYN(){
		return exceptYN;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
}
