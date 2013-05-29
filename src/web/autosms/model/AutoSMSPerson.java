package web.autosms.model;

import java.io.Serializable;


/**
 * <p>자동SMS 전송자들 
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class AutoSMSPerson implements Serializable {

	private int autosmsID;
	private String receiverPhone;
	private String smsCode;
	private String smsCodeMsg;
	private String registDate;
	
	
	public void setAutosmsID(int autosmsID){
		this.autosmsID = autosmsID;
	}
	public int getAutosmsID(){
		return autosmsID;
	}
	
	public void setReceiverPhone(String receiverPhone){
		this.receiverPhone = receiverPhone;
	}
	public String getReceiverPhone(){
		return receiverPhone;
	}	
	
	public void setSmsCode(String smsCode){
		this.smsCode = smsCode;
	}
	public String getSmsCode(){
		return smsCode;
	}
	
	public void setSmsCodeMsg(String smsCodeMsg){
		this.smsCodeMsg = smsCodeMsg;
	}
	public String getSmsCodeMsg(){
		return smsCodeMsg;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	
}
