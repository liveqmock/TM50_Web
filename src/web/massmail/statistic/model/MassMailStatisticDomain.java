package web.massmail.statistic.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>ez_massmail_domainstatistic
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class MassMailStatisticDomain implements Serializable{
	
	private int massmailID = 0;
	private String domainName = "";
	private int sendTotal = 0;
	private int filterTotal = 0;
	private int successTotal = 0;
	private int failTotal = 0;
	private int openTotal = 0;
	private int clickTotal = 0;
	private int rejectcallTotal = 0;
	private BigDecimal successRatio;
	
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
	
	public void setSendTotal(int sendTotal){
		this.sendTotal = sendTotal;
	}
	public int getSendTotal(){
		return sendTotal;
	}
	
	public void setFilterTotal(int filterTotal){
		this.filterTotal  = filterTotal;
	}
	public int getFilterTotal(){
		return filterTotal;
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
	public BigDecimal getSuccessRatio() {
		return successRatio;
	}
	public void setSuccessRatio(BigDecimal  successRatio) {
		this. successRatio =  successRatio;
	}
	
}
