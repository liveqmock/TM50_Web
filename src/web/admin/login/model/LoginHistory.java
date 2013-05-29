package web.admin.login.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LoginHistory implements Serializable{
	
	private int loginHistoryID;
	private String userID;
	private String registDate;
	private String loginYN;
	private String description;
	private String accessIP;
	
	
	
	public LoginHistory(String userID, String loginYN, String description, String accessIP) {
		
		this.userID = userID;
		this.loginYN = loginYN;
		this.description = description;
		this.accessIP = accessIP;
	}
	public LoginHistory() {
		super();
	}
	
	/**
	 * @return the loginHistoryID
	 */
	public int getLoginHistoryID() {
		return loginHistoryID;
	}
	/**
	 * @param loginHistoryID the loginHistoryID to set
	 */
	public void setLoginHistoryID(int loginHistoryID) {
		this.loginHistoryID = loginHistoryID;
	}
	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	/**
	 * @return the registDate
	 */
	public String getRegistDate() {
		return registDate;
	}
	/**
	 * @param registDate the registDate to set
	 */
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	/**
	 * @return the loginYN
	 */
	public String getLoginYN() {
		return loginYN;
	}
	/**
	 * @param loginYN the loginYN to set
	 */
	public void setLoginYN(String loginYN) {
		this.loginYN = loginYN;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the accessIP
	 */
	public String getAccessIP() {
		return accessIP;
	}
	/**
	 * @param accessIP the accessIP to set
	 */
	public void setAccessIP(String accessIP) {
		this.accessIP = accessIP;
	}
	
	

}
