package web.massmail.statistic.model;

import java.io.Serializable;


/**
 * <p>대량메일 대상자리스트 
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class MassMailStatisticMember implements Serializable{

	private int sendID = 0;
	private int massmailID = 0;
	private String email = "";
	private String smtpCodeType = "";
	private String smtpCode = "";
	private String smtpMsg = "";
	private String openYN = "";
	private String clickYN = "";
	private String openDate = "";	
	private String rejectcallYN = "";
	private String customerID = "";
	
	public void setSendID(int sendID){
		this.sendID = sendID;
	}
	public int getSendID(){
		return sendID;
	}
	
	public void setMassmailID(int massmailID){
		this.massmailID = massmailID;
	}
	public int getMassmailID(){
		return massmailID;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return email;
	}
	
	public void setSmtpCodeType(String smtpCodeType){
		this.smtpCodeType = smtpCodeType;
	}	
	public String getSmtpCodeType(){
		return smtpCodeType;
	}
	
	public void setSmtpCode(String smtpCode){
		this.smtpCode = smtpCode;
	}	
	public String getSmtpCode(){
		return smtpCode;
	}

	public void setSmtpMsg(String smtpMsg){
		this.smtpMsg = smtpMsg;
	}	
	public String getSmtpMsg(){
		return smtpMsg;
	}
	
	public void setOpenYN(String openYN){
		this.openYN = openYN;
	}	
	public String getOpenYN(){
		return openYN;
	}
	
	public void setClickYN(String clickYN){
		this.clickYN = clickYN;
	}	
	public String getClickYN(){
		return clickYN;
	}
	
	public void setOpenDate(String openDate){
		this.openDate = openDate;
	}	
	public String getOpenDate(){
		return openDate;
	}
	
	public void setRejectcallYN(String rejectcallYN){
		this.rejectcallYN = rejectcallYN;
	}	
	public String getRejectcallYN(){
		return rejectcallYN;
	}
	
	public void setCustomerID(String customerID){
		this.customerID = customerID;
	}	
	public String getCustomerID(){
		return customerID;
	}
}
