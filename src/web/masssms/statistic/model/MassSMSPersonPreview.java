package web.masssms.statistic.model;

import java.io.Serializable;


/**
 * <p>ez_massmail_person_perview
 * @author 임영호
 *
 */
@SuppressWarnings("serial")
public class MassSMSPersonPreview implements Serializable{

	private int sendID;
	private String receiverPhone;
	private String registDate;
	private String smsCode;
	private String smsCodeMsg;
	
	public int getSendID(){
		return sendID;
	}
	public void setSendID(int sendID){
		this.sendID = sendID;
	}	
	
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	public String getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	public String getSmsCodeMsg() {
		return smsCodeMsg;
	}
	public void setSmsCodeMsg(String smsCodeMsg) {
		this.smsCodeMsg = smsCodeMsg;
	}

}
