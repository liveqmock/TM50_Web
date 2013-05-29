package web.api.automail.model;

public class AutoMailAPI {
	
	private String automailID;
	private String mailTitle;
	private String mailContent;
	private String senderMail;
	private String senderName;
	private String receiverName;
	private String email;
	private String onetooneInfo;
	private String customerID;
	private String reserveDate;
	
	
	
	
	public AutoMailAPI(String automailID, String mailTitle, String mailContent,
			String senderMail, String senderName, String receiverName,
			String email, String onetooneInfo, String reserveDate) {
		super();
		this.automailID = automailID;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
		this.senderMail = senderMail;
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.email = email;
		this.onetooneInfo = onetooneInfo;
		this.reserveDate = reserveDate;
	}
	
	
	
	public AutoMailAPI() {
		
	}



	/**
	 * @return the automailID
	 */
	public String getAutomailID() {
		return automailID;
	}
	/**
	 * @param automailID the automailID to set
	 */
	public void setAutomailID(String automailID) {
		this.automailID = automailID;
	}
	/**
	 * @return the mailTitle
	 */
	public String getMailTitle() {
		return mailTitle;
	}
	/**
	 * @param mailTitle the mailTitle to set
	 */
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	/**
	 * @return the mailContent
	 */
	public String getMailContent() {
		return mailContent;
	}
	/**
	 * @param mailContent the mailContent to set
	 */
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	/**
	 * @return the senderMail
	 */
	public String getSenderMail() {
		return senderMail;
	}
	/**
	 * @param senderMail the senderMail to set
	 */
	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}
	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}
	/**
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the onetooneInfo
	 */
	public String getOnetooneInfo() {
		return onetooneInfo;
	}
	/**
	 * @param onetooneInfo the onetooneInfo to set
	 */
	public void setOnetooneInfo(String onetooneInfo) {
		this.onetooneInfo = onetooneInfo;
	}
	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}



	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	/**
	 * @return the reserveDate
	 */
	public String getReserveDate() {
		return reserveDate;
	}
	/**
	 * @param reserveDate the reserveDate to set
	 */
	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}
	
	
	
	

}
