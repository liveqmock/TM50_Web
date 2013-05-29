package web.api.autosms.model;

public class AutosmsAPI {
	
	private int sendID;       
	private int autosmsID;    
	private String senderPhone;  
	private String receiverPhone;
	private String smsMsg;       
	private String msgType;      
	private String onetooneInfo; 
	private String wasSended;    
	private String customerID;   
	private String registDate;
	
	
	public AutosmsAPI(int autosmsID, String senderPhone,
			String receiverPhone, String smsMsg, String msgType,
			String onetooneInfo) 
	{
	
		this.autosmsID = autosmsID;
		this.senderPhone = senderPhone;
		this.receiverPhone = receiverPhone;
		this.smsMsg = smsMsg;
		this.msgType = msgType;
		this.onetooneInfo = onetooneInfo;
		
	
	}


	public AutosmsAPI() {
		
	}


	/**
	 * @return the sendID
	 */
	public int getSendID() {
		return sendID;
	}


	/**
	 * @param sendID the sendID to set
	 */
	public void setSendID(int sendID) {
		this.sendID = sendID;
	}


	/**
	 * @return the autosmsID
	 */
	public int getAutosmsID() {
		return autosmsID;
	}


	/**
	 * @param autosmsID the autosmsID to set
	 */
	public void setAutosmsID(int autosmsID) {
		this.autosmsID = autosmsID;
	}


	/**
	 * @return the senderPhone
	 */
	public String getSenderPhone() {
		return senderPhone;
	}


	/**
	 * @param senderPhone the senderPhone to set
	 */
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}


	/**
	 * @return the receiverPhone
	 */
	public String getReceiverPhone() {
		return receiverPhone;
	}


	/**
	 * @param receiverPhone the receiverPhone to set
	 */
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}


	/**
	 * @return the smsMsg
	 */
	public String getSmsMsg() {
		return smsMsg;
	}


	/**
	 * @param smsMsg the smsMsg to set
	 */
	public void setSmsMsg(String smsMsg) {
		this.smsMsg = smsMsg;
	}


	/**
	 * @return the msgType
	 */
	public String getMsgType() {
		return msgType;
	}


	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
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
	 * @return the wasSended
	 */
	public String getWasSended() {
		return wasSended;
	}


	/**
	 * @param wasSended the wasSended to set
	 */
	public void setWasSended(String wasSended) {
		this.wasSended = wasSended;
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
	
	
	
	
	
	
	


}
