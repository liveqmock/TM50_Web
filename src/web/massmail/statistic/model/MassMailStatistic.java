package web.massmail.statistic.model;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>ez_massmail_statistic
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class MassMailStatistic implements Serializable{

	private int massmailID = 0;
	private int sendTotal = 0;
	private int filterTotal = 0;
	private int successTotal = 0;
	private int failTotal = 0;
	private int openTotal = 0;
	private int clickTotal = 0;
	private int rejectcallTotal = 0;
	String standard ;
	String viewStandard ;
	private BigDecimal successRatio;
	public int getMassmailID() {
		return massmailID;
	}
	public void setMassmailID(int massmailID) {
		this.massmailID = massmailID;
	}
	public int getSendTotal() {
		return sendTotal;
	}
	public void setSendTotal(int sendTotal) {
		this.sendTotal = sendTotal;
	}
	public int getFilterTotal() {
		return filterTotal;
	}
	public void setFilterTotal(int filterTotal) {
		this.filterTotal = filterTotal;
	}
	public int getSuccessTotal() {
		return successTotal;
	}
	public void setSuccessTotal(int successTotal) {
		this.successTotal = successTotal;
	}
	public int getFailTotal() {
		return failTotal;
	}
	public void setFailTotal(int failTotal) {
		this.failTotal = failTotal;
	}
	public int getOpenTotal() {
		return openTotal;
	}
	public void setOpenTotal(int openTotal) {
		this.openTotal = openTotal;
	}
	public int getClickTotal() {
		return clickTotal;
	}
	public void setClickTotal(int clickTotal) {
		this.clickTotal = clickTotal;
	}
	public int getRejectcallTotal() {
		return rejectcallTotal;
	}
	public void setRejectcallTotal(int rejectcallTotal) {
		this.rejectcallTotal = rejectcallTotal;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getViewStandard() {
		return viewStandard;
	}
	public void setViewStandard(String viewStandard) {
		this.viewStandard = viewStandard;
	}
	public BigDecimal getSuccessRatio() {
		return successRatio;
	}
	public void setSuccessRatio(BigDecimal successRatio) {
		this.successRatio = successRatio;
	}
	
}
