package web.admin.persistfail.model;

import java.io.Serializable;


/**
 * <p>ez_massmail_persistfail
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class PersistFail implements Serializable{
	
	private String persistfailID = "";
	private String email = "";
	private int massmailID = 0;
	private int targetID = 0;
	private int massmailGroupID = 0;
	private String smtpCode = "";
	private String smtpMsg = "";
	private String customerID = "";
	private String registDate = "";
	private String massmailTitle = "";
	private String targetName = "";
	private String massmailGroupName = "";
	
	
	
	
	public String getMassmailGroupName() {
		return massmailGroupName;
	}
	public void setMassmailGroupName(String massmailGroupName) {
		this.massmailGroupName = massmailGroupName;
	}
	public String getMassmailTitle() {
		return massmailTitle;
	}
	public void setMassmailTitle(String massmailTitle) {
		this.massmailTitle = massmailTitle;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public void setPersistfailID(String persistfailID){
		this.persistfailID = persistfailID;
	}
	public String getPersistfailID(){
		return persistfailID;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return email;
	}
	
	public void setMassmailID(int massmailID){
		this.massmailID = massmailID;
	}
	public int getMassmailID(){
		return massmailID;
	}
	
	public void setTargetID(int targetID){
		this.targetID = targetID;
	}
	public int getTargetID(){
		return targetID;
	}
	
	public void setMassmailGroupID(int massmailGroupID){
		this.massmailGroupID = massmailGroupID;
	}
	public int getMassmailGroupID(){
		return massmailGroupID;
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
	
	public void setCustomerID(String customerID){
		this.customerID = customerID;
	}
	public String getCustomerID(){
		return customerID;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}

}
