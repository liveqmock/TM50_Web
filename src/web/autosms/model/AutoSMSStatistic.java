package web.autosms.model;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * <p>자동sms통계 
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class AutoSMSStatistic implements Serializable {

	private int autosmsID;
	private String autosmsTitle;
	private String standard;
	private String viewStandard;
	private String sendTimeHour = "";
	private int sendTotal = 0;
	private int readyTotal = 0;
	private int successTotal = 0;
	private int failTotal = 0;
	private BigDecimal successRatio;
		
	public String getAutosmsTitle() {
		return autosmsTitle;
	}
	public void setAutosmsTitle(String autosmsTitle) {
		this.autosmsTitle = autosmsTitle;
	}
	
	public void setAutosmsID(int autosmsID){
		this.autosmsID = autosmsID;
	}
	public int getAutosmsID(){
		return autosmsID;
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
	
	public void setSendTimeHour(String sendTimeHour){
		this.sendTimeHour = sendTimeHour;
	}
	public String getSendTimeHour(){
		return sendTimeHour;
	}	
	
	public void setSendTotal(int sendTotal){
		this.sendTotal = sendTotal;
	}
	public int getSendTotal(){
		return sendTotal;
	}
	
	public void setReadyTotal(int readyTotal){
		this.readyTotal = readyTotal;
	}
	public int getReadyTotal(){
		return readyTotal;
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
	
	public BigDecimal getSuccessRatio() {
		return successRatio;
	}
	public void setSuccessRatio(BigDecimal successRatio) {
		this.successRatio = successRatio;
	}
}
