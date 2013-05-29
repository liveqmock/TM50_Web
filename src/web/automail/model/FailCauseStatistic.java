package web.automail.model;

import java.math.BigDecimal;


/**
 * 
 * @author 윤훈범
 * @create date 2009-07-29	
 */
 
public class FailCauseStatistic {
	private String failCauseTypeDes;
	private String failCauseType;
	private String failCauseDes;
	private BigDecimal failCount;
	private String failcauseCode;
	
	public String getFailCauseTypeDes() {
		return failCauseTypeDes;
	}
	public void setFailCauseTypeDes(String failCauseTypeDes) {
		this.failCauseTypeDes = failCauseTypeDes;
	}
	public String getFailCauseType() {
		return failCauseType;
	}
	public void setFailCauseType(String failCauseType) {
		this.failCauseType = failCauseType;
	}
	public String getFailCauseDes() {
		return failCauseDes;
	}
	public void setFailCauseDes(String failCauseDes) {
		this.failCauseDes = failCauseDes;
	}
	public BigDecimal getFailCount() {
		return failCount;
	}
	public void setFailCount(BigDecimal failCount) {
		this.failCount = failCount;
	}
	public String getFailcauseCode() {
		return failcauseCode;
	}
	public void setFailcauseCode(String failcauseCode) {
		this.failcauseCode = failcauseCode;
	}
	
	
}
	