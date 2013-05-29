package tm.autosms.sendapi;

public class ThunderAutoSMS {
	
	     
	private String autosmsID;     
	private String senderPhone;   
	private String receiverPhone; 
	private String smsMsg;        
	private String msgType;       
	private String onetooneInfo;  
	private String ThunderMailURL;
	
	private String customerID;

	/**
	 * @return the autosmsID
	 */
	public String getAutosmsID() {
		return autosmsID;
	}

	/**
	 * @param autosmsID the autosmsID to set
	 */
	public void setAutosmsID(String autosmsID) {
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
	 * @return the thunderMailURL
	 */
	public String getThunderMailURL() {
		return ThunderMailURL;
	}

	/**
	 * @param thunderMailURL the thunderMailURL to set
	 */
	public void setThunderMailURL(String thunderMailURL) {
		ThunderMailURL = "http://"+thunderMailURL+"/api/autosms";
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
	    
	
	


}
