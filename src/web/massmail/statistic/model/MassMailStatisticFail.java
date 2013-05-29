package web.massmail.statistic.model;

import java.io.Serializable;


/**
 * <p>ez_massmail_failstatistic
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class MassMailStatisticFail  implements Serializable{
	
	private int massmailID = 0;
	private String domainName = "";
	private String failcauseType = "";
	private int failCount = 0;

	public void setMassmailID(int massmailID){
		this.massmailID = massmailID;
	}
	public int getMassmailID(){
		return massmailID;
	}
	
	public void setDomainName(String domainName){
		this.domainName = domainName;
	}
	public String getDomainName(){
		return domainName;
	}
	
	public void setFailcauseType(String failcauseType){
		this.failcauseType = failcauseType;
	}
	public String getFailcauseType(){
		return failcauseType;
	}
	
	public void setFailCount(int failCount){
		this.failCount = failCount;
	}
	public int getFailCount(){
		return failCount;
	}
	
	
}
