package web.admin.manager.model;

import java.io.Serializable;

/**
 * <p>ez_config 테이블 
 * @author 임영호 
 *
 */
@SuppressWarnings("serial")
public class Manager implements Serializable{
	private String engineID = "";
	private String engineName = "";
	private String engineDesc = ""; 
	private String engineStatus =""; 
	private int errorCount = 0;
	private String serverIP = "";
	private String updateDate = "";
	private String logPath = "";
	private String logName = "";
	
	public String getEngineID() {
		return engineID;
	}
	public void setEngineID(String engineID) {
		this.engineID = engineID;
	}
	public String getEngineName() {
		return engineName;
	}
	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}
	public String getEngineDesc() {
		return engineDesc;
	}
	public void setEngineDesc(String engineDesc) {
		this.engineDesc = engineDesc;
	}
	public String getEngineStatus() {
		return engineStatus;
	}
	public void setEngineStatus(String engineStatus) {
		this.engineStatus = engineStatus;
	}
	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getLogPath() {
		return logPath;
	}
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	public String getLogName() {
		return logName;
	}
	public void setLogName(String logName) {
		this.logName = logName;
	}
	
}
