package web.massmail.statistic.model;

import java.io.Serializable;

/**
 * <p>ez_massmail_report_month
 * @author 임영호
 *
 */
@SuppressWarnings("serial")
public class MassMailReportMonth implements Serializable{

	private int totalCount = 0;
	private int writeCount = 0;
	private int appReadyCount = 0;
	private int readyCount = 0;
	private int sendingCount = 0;
	private int sendFinishCount  = 0;
	private int errCount = 0;
	private int sendingStopCount  = 0;
	
	private String standard = "";
	private int sendTotal = 0;
	private int successTotal = 0;
	private int failTotal = 0;
	private int openTotal = 0;
	private int clickTotal = 0;
	private int rejectcallTotal = 0;
	
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getWriteCount() {
		return writeCount;
	}
	public void setWriteCount(int writeCount) {
		this.writeCount = writeCount;
	}
	public int getAppReadyCount() {
		return appReadyCount;
	}
	public void setAppReadyCount(int appReadyCount) {
		this.appReadyCount = appReadyCount;
	}
	public int getReadyCount() {
		return readyCount;
	}
	public void setReadyCount(int readyCount) {
		this.readyCount = readyCount;
	}
	public int getSendingCount() {
		return sendingCount;
	}
	public void setSendingCount(int sendingCount) {
		this.sendingCount = sendingCount;
	}
	public int getSendFinishCount() {
		return sendFinishCount;
	}
	public void setSendFinishCount(int sendFinishCount) {
		this.sendFinishCount = sendFinishCount;
	}
	public int getErrCount() {
		return errCount;
	}
	public void setErrCount(int errCount) {
		this.errCount = errCount;
	}
	public int getSendingStopCount() {
		return sendingStopCount;
	}
	public void setSendingStopCount(int sendingStopCount) {
		this.sendingStopCount = sendingStopCount;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
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
	
}
