package web.api.massmail.model;

public class MassMailAPI {
	
	private String mailTitle;
	private String senderEmail;
	private String senderName;
	private String receiverName;
	private String mailContent;
	
	private String targetType;
	private String targetQuery;
	private String targetDBId;
	private String targetString;
	
	private String writer;
	
	
	

	public MassMailAPI(String mailTitle, String senderEmail, String senderName,
			String receiverName, String mailContent, String targetType,
			String targetQuery, String targetDBId, String targetString,
			String writer) {
		super();
		this.mailTitle = mailTitle;
		this.senderEmail = senderEmail;
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.mailContent = mailContent;
		this.targetType = targetType;
		this.targetQuery = targetQuery;
		this.targetDBId = targetDBId;
		this.targetString = targetString;
		this.writer = writer;
	}

	public MassMailAPI() {
		
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
	 * @return the senderEmail
	 */
	public String getSenderEmail() {
		return senderEmail;
	}

	/**
	 * @param senderEmail the senderEmail to set
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
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
	 * @return the targetType
	 */
	public String getTargetType() {
		return targetType;
	}

	/**
	 * @param targetType the targetType to set
	 */
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	/**
	 * @return the targetQuery
	 */
	public String getTargetQuery() {
		return targetQuery;
	}

	/**
	 * @param targetQuery the targetQuery to set
	 */
	public void setTargetQuery(String targetQuery) {
		this.targetQuery = targetQuery;
	}

	/**
	 * @return the targetDBId
	 */
	public String getTargetDBId() {
		return targetDBId;
	}

	/**
	 * @param targetDBId the targetDBId to set
	 */
	public void setTargetDBId(String targetDBId) {
		this.targetDBId = targetDBId;
	}

	/**
	 * @return the targetString
	 */
	public String getTargetString() {
		return targetString;
	}

	/**
	 * @param targetString the targetString to set
	 */
	public void setTargetString(String targetString) {
		this.targetString = targetString;
	}

	/**
	 * @return the writer
	 */
	public String getWriter() {
		return writer;
	}

	/**
	 * @param writer the writer to set
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}
	
	
	

}
