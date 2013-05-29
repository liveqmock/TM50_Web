package tm.automail.sendapi;

public class ThunderAutoMail {
	
	private String automailID;
	private String mailTitle;
	private String mailContent;
	private String senderMail;
	private String senderName;
	private String receiverName;
	private String email;
	private String onetooneInfo;
	private String ThunderMailURL;
	private String customerID;
	private String reserveDate;
	
	
		
	public ThunderAutoMail(String automailID, String mailTitle,
			String mailContent, String senderMail, String senderName,
			String receiverName, String email, String onetooneInfo, String ThunderMailURL, String reserveDate, String sendType) {
		super();
		this.automailID = automailID;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
		this.senderMail = senderMail;
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.email = email;
		this.onetooneInfo = onetooneInfo;
		this.ThunderMailURL = ThunderMailURL;
		this.reserveDate = reserveDate;
	}
	
	
	
	public ThunderAutoMail() {
		
	}

	

	/**
	 * 자동메일 아이디 
	 * @return the automailID
	 */
	public String getAutomailID() {
		return automailID;
	}
	/**
	 * 자동메일 아이디
	 * @param automailID the automailID to set
	 */
	public void setAutomailID(String automailID) {
		this.automailID = automailID;
	}
	/**
	 * 메일 제목
	 * @return the mailTitle
	 */
	public String getMailTitle() {
		return mailTitle;
	}
	/**
	 * 메일 제목
	 * @param mailTitle the mailTitle to set
	 */
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	/**
	 * 메일 내용
	 * @return the mailContent
	 */
	public String getMailContent() {
		return mailContent;
	}
	/**
	 * 메일 내용
	 * @param mailContent the mailContent to set
	 */
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	/**
	 * 보내는 사람 이메일
	 * @return the senderMail
	 */
	public String getSenderMail() {
		return senderMail;
	}
	/**
	 * 보내는 사람 이메일
	 * @param senderMail the senderMail to set
	 */
	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}
	/**
	 * 보내는 사람 이름
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * 보내는 사람 이름
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * 받는 사람 이름
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}
	/**
	 * 받는 사람 이름
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	/**
	 * 받는 사람 이메일
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 받는 사람 이메일
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 일대일치환 정보
	 * @return the onetooneInfo
	 */
	public String getOnetooneInfo() {
		return onetooneInfo;
	}
	/**
	 * 일대일치환 정보
	 * @param onetooneInfo the onetooneInfo to set
	 */
	public void setOnetooneInfo(String onetooneInfo) {
		this.onetooneInfo = onetooneInfo;
	}



	/**
	 * @return the thunderMailURL
	 */
	public String getThunderMailURL() {
		return ThunderMailURL;
	}



	/**
	 * @param thunderMailURL the thunderMailURL to set
	 */
	public void setThunderMailURL(String thunderMailURL) {
		ThunderMailURL = "http://"+thunderMailURL+"/api/automail";
	}
	
	/**
	 * 사용자 입력값
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * 사용자 입력값
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	/**
	 * 예약발송시간
	 * @return the reserveDate
	 */
	public String getReserveDate() {
		return reserveDate;
	}
	/**
	 * 예약발송시간
	 * @param reserveDate the reserveDate to set
	 */
	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}
	
	

}
