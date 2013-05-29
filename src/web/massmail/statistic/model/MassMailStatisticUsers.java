package web.massmail.statistic.model;

import java.io.Serializable;



/**
 * <p>사용자별 통계
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class MassMailStatisticUsers implements Serializable {

	private String userID;
	private String userName;
	private String groupID;
	private String groupName;
	private int sendTotal = 0;
	private int successTotal = 0;
	private int failTotal = 0;
	private int openTotal = 0;
	private int clickTotal = 0;
	private int rejectcallTotal = 0;
	private String successRatio="0";
	
	
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
	
	public void setGroupID(String groupID){
		this.groupID = groupID;
	}
	public String getGroupID(){
		return groupID;
	}
	
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getGroupName(){
		return groupName;
	}
	
	public void setSendTotal(int sendTotal){
		this.sendTotal = sendTotal;
	}
	public int getSendTotal(){
		return sendTotal;
	}
	
	
	public void setSuccessTotal(int successTotal){
		this.successTotal = successTotal;
	}
	public int getSuccessTotal(){
		return successTotal;
	}
	
	public void setFailTotal(int failTotal){
		this.failTotal = failTotal;
	}
	public int getFailTotal(){
		return failTotal;
	}
		
	public void setOpenTotal(int openTotal){
		this.openTotal = openTotal;
	}
	public int getOpenTotal(){
		return openTotal;
	}
	
	public void setClickTotal(int clickTotal){
		this.clickTotal = clickTotal;
	}
	public int getClickTotal(){
		return clickTotal;
	}
	
	public void setRejectcallTotal (int rejectcallTotal){
		this.rejectcallTotal = rejectcallTotal;
	}
	public int getRejectcallTotal(){
		return rejectcallTotal;
	}
	
	public void setSuccessRatio(String successRatio){
		this.successRatio = successRatio;
	}
	public String getSuccessRatio(){
		return successRatio;
	}
}
