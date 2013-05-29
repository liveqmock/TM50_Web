package tm.massmail.sendapi;

public class ThunderMassMail {
	
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
	
	private String thunderMailURL;
	private String fileOneToOne;
	
	private String massMailGroupID;
	
	private String contentType;
	private String template_id;
	
	private String sendType = "1";
	private String sendSchedule;
	
	private String attachPath;
	private String attachFileNames;
	private String attachFileRealNames;
	
	

	public ThunderMassMail() {
		
	}

	/**
	 * 메일 제목, 필수
	 * @return the mailTitle
	 */
	public String getMailTitle() {
		return mailTitle;
	}

	/**
	 * 메일 제목, 필수
	 * @param mailTitle the mailTitle to set
	 */
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	/**
	 * 보내는 사람 이메일(회신 메일 계정), 필수
	 * @return the senderEmail
	 */
	public String getSenderEmail() {
		return senderEmail;
	}

	/**
	 * 보내는 사람 이메일(회신 메일 계정), 필수
	 * @param senderEmail the senderEmail to set
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	/**
	 * 보내는 사람 이름, 필수
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * 보내는 사람 이름, 필수
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * 받는 사람 이름, 필수
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}

	/**
	 * 받는 사람 이름, 필수
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * 메일 내용, 필수
	 * @return the mailContent
	 */
	public String getMailContent() {
		return mailContent;
	}

	/**
	 * 메일 내용, 필수
	 * @param mailContent the mailContent to set
	 */
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	/**
	 * 대상자 등록 유형, 필수
	 * string : 리스트 직접 입력
	 * query : 쿼리문 입력
	 * @return the targetType
	 */
	public String getTargetType() {
		return targetType;
	}

	/**
	 * 대상자 등록 유형, 필수
	 * string : 리스트 직접 입력
	 * query : 쿼리문 입력
	 * @param targetType the targetType to set
	 */
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	/**
	 * 대상자 추출 쿼리문, 대상자 등록 유형이 query 일 때 필수
	 * @return the targetQuery
	 */
	public String getTargetQuery() {
		return targetQuery;
	}

	/**
	 * 대상자 추출 쿼리문, 대상자 등록 유형이 query 일 때 필수
	 * @param targetQuery the targetQuery to set
	 */
	public void setTargetQuery(String targetQuery) {
		this.targetQuery = targetQuery;
	}

	/**
	 * 대상자 추출 DB 아이디,대상자 등록 유형이 query 일 때 필수
	 * 쿼리문이 수행될 DB
	 * @return the targetDBId
	 */
	public String getTargetDBId() {
		return targetDBId;
	}

	/**
	 * 대상자 추출 DB 아이디,대상자 등록 유형이 query 일 때 필수
	 * 쿼리문이 수행될 DB
	 * @param targetDBId the targetDBId to set
	 */
	public void setTargetDBId(String targetDBId) {
		this.targetDBId = targetDBId;
	}

	/**
	 * 대상자 정보, 대상자 등록 유형이 string 일 때 필수
	 * @return the targetString
	 */
	public String getTargetString() {
		return targetString;
	}

	/**
	 * 대상자 정보, 대상자 등록 유형이 string 일 때 필수
	 * @param targetString the targetString to set
	 */
	public void setTargetString(String targetString) {
		this.targetString = targetString;
	}

	/**
	 * 메일 작성자 아이디, 필수
	 * @return the writer
	 */
	public String getWriter() {
		return writer;
	}

	/**
	 * 메일 작성자 아이디, 필수
	 * @param writer the writer to set
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}

	/**
	 * 썬더메일 웹 UI url, 필수
	 * ex) ems.test.com:80
	 * @return the thunderURL
	 */
	public String getThunderMailURL() {
		return thunderMailURL;
	}

	/**
	 * 썬더메일 웹 UI url, 필수
	 * ex) ems.test.com:80
	 * @param thunderURL the thunderURL to set
	 */
	public void setThunderMailURL(String thunderMailURL) {
		this.thunderMailURL = "http://"+thunderMailURL+"/api/massmail";
	}

	/**
	 * 일대일 치환 정보, 필수
	 * ex)[$email]≠[이메일]ø[$name]≠[고객명]ø[$cellPhone]≠[휴대폰]
	 * @return the fileOneToOne
	 */
	public String getFileOneToOne() {
		return fileOneToOne;
	}

	/**
	 * 일대일 치환 정보, 필수
	 * ex)[$email]≠[이메일]ø[$name]≠[고객명]ø[$cellPhone]≠[휴대폰]
	 * @param fileOneToOne the fileOneToOne to set
	 */
	public void setFileOneToOne(String fileOneToOne) {
		this.fileOneToOne = fileOneToOne;
	}

	/**
	 * 대량메일 그룹 아이디
	 * @return the massMailGroupID
	 */
	public String getMassMailGroupID() {
		return massMailGroupID;
	}

	/**
	 * 대량메일 그룹 아이디
	 * @param massMailGroupID the massMailGroupID to set
	 */
	public void setMassMailGroupID(String massMailGroupID) {
		this.massMailGroupID = massMailGroupID;
	}



	/**
	 * 메일 내용 입력 타입, 필수
	 * content: 메일 내용 직접 전송, template: 메일 템플릿 사용
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}



	/**
	 * 메일 내용 입력 타입, 필수
	 * content: 메일 내용 직접 전송, template: 메일 템플릿 사용
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}



	/**
	 * 메일 내용 템플릿 아이디
	 * ContentType이 template 일때 필수 
	 * @return the template_id
	 */
	public String getTemplate_id() {
		return template_id;
	}



	/**
	 * 메일 내용 템플릿 아이디
	 * ContentType이 template 일때 필수
	 * @param template_id the template_id to set
	 */
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	/**
	 * @return the sendType
	 */
	public String getSendType() {
		return sendType;
	}

	/**
	 * @param sendType the sendType to set
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	/**
	 * @return the sendSchedule
	 */
	public String getSendSchedule() {
		return sendSchedule;
	}

	/**
	 * @param sendSchedule the sendSchedule to set
	 */
	public void setSendSchedule(String sendSchedule) {
		this.sendSchedule = sendSchedule;
	}
	
	/**
	 * @return the attachPath
	 */
	public String getAttachPath() {
		return attachPath;
	}

	/**
	 * @param attachPath the attachPath to set
	 */
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

	/**
	 * @return the attachFileNames
	 */
	public String getAttachFileNames() {
		return attachFileNames;
	}

	/**
	 * @param attachFileNames the attachFileNames to set
	 */
	public void setAttachFileNames(String attachFileNames) {
		this.attachFileNames = attachFileNames;
	}

	/**
	 * @return the attachFileRealNames
	 */
	public String getAttachFileRealNames() {
		return attachFileRealNames;
	}

	/**
	 * @param attachFileRealNames the attachFileRealNames to set
	 */
	public void setAttachFileRealNames(String attachFileRealNames) {
		this.attachFileRealNames = attachFileRealNames;
	}
	
	
}
