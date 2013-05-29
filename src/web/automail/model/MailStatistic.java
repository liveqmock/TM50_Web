package web.automail.model;

import java.math.BigDecimal;

 
public class MailStatistic {
	
	private int automailID;
	private String automailTitle;
	private String sendTimeHour;
	private String standard;
	private String viewStandard;
	private int sendTotal;
	private int successTotal;
	private int failTotal;
	private int openTotal;
	private BigDecimal successRatio;
	private String email;
	private String registDate;
	private String smtpCode;
	private String smtpMsg;
	
	public String getAutomailTitle() {
		return automailTitle;
	}
	public void setAutomailTitle(String automailTitle) {
		this.automailTitle = automailTitle;
	}
	
	public int getAutomailID() {
		return automailID;
	}
	public void setAutomailID(int automailID) {
		this.automailID = automailID;
	}
	
	public String getSendTimeHour() {
		return sendTimeHour;
	}
	public void setSendTimeHour(String sendTimeHour) {
		this.sendTimeHour = sendTimeHour;
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
	
	public int getSendTotal() {
		return sendTotal;
	}
	public void setSendTotal(int sendTotal) {
		this.sendTotal = sendTotal;
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
	
	public BigDecimal getSuccessRatio() {
		return successRatio;
	}
	public void setSuccessRatio(BigDecimal successRatio) {
		this.successRatio = successRatio;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	
	public String getSmtpCode() {
		return smtpCode;
	}
	public void setSmtpCode(String smtpCode) {
		this.smtpCode = smtpCode;
	}
	
	public String getSmtpMsg() {
		return smtpMsg;
	}
	public void setSmtpMsg(String smtpMsg) {
		this.smtpMsg = smtpMsg;
	}
	
	
}
	