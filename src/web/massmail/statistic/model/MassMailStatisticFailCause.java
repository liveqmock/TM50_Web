package web.massmail.statistic.model;

import java.math.BigDecimal;


/**
 * 
 * @author 윤훈범
 * @create date 2009-07-29	
 */
 
public class MassMailStatisticFailCause {
	private String failCauseTypeDes;
	private String failCauseType;
	private String failCauseDes;
	private BigDecimal failCount;
	private String failCauseCode;
	
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
	public String getFailCauseCode() {
		return failCauseCode;
	}
	public void setFailCauseCode(String failCauseCode) {
		this.failCauseCode = failCauseCode;
	}

	
}
	