package web.massmail.statistic.model;

import java.io.Serializable;


/**
 * <p>ez_massmail_failstatistic
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class MassMailStatisticDomainFail  implements Serializable{
	
	
	private String domainName = "";
	private int failCount = 0;
	private int sendCount = 0;
	private int failcauseType1Count = 0;			//일시적인 오류 카운트
	private int failcauseType2Count = 0;			//영구적인 오류 카운트
	private int failcauseType3Count = 0;			//기타 SMTP 오류 카운트
	private int failcauseType4Count = 0;			//시스템내부 오류 카운트
	private int failcauseType5Count = 0;			//불확실한 오류 카운트
	
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public int getFailcauseType1Count() {
		return failcauseType1Count;
	}
	public void setFailcauseType1Count(int failcauseType1Count) {
		this.failcauseType1Count = failcauseType1Count;
	}
	public int getFailcauseType2Count() {
		return failcauseType2Count;
	}
	public void setFailcauseType2Count(int failcauseType2Count) {
		this.failcauseType2Count = failcauseType2Count;
	}
	public int getFailcauseType3Count() {
		return failcauseType3Count;
	}
	public void setFailcauseType3Count(int failcauseType3Count) {
		this.failcauseType3Count = failcauseType3Count;
	}
	public int getFailcauseType4Count() {
		return failcauseType4Count;
	}
	public void setFailcauseType4Count(int failcauseType4Count) {
		this.failcauseType4Count = failcauseType4Count;
	}
	public int getFailcauseType5Count() {
		return failcauseType5Count;
	}
	public void setFailcauseType5Count(int failcauseType5Count) {
		this.failcauseType5Count = failcauseType5Count;
	}

	
}
