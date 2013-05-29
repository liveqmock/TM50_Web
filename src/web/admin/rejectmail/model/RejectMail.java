package web.admin.rejectmail.model;

import java.io.Serializable;


/**
 * <p>ez_massmail_reject
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class RejectMail  implements Serializable{

	private int rejectID;
	private String email = "";
	private int massmailID = 0;
	private int massmailGroupID = 0;
	private int targetID = 0;
	private String registDate = "";
	private String customerID = "";
	private String userID = "";
	private String groupID = "";
	private String massmailGroupName = "";
	private String massmailTitle = "";
	private String userName = "";

	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMassmailTitle() {
		return massmailTitle;
	}
	public void setMassmailTitle(String massmailTitle) {
		this.massmailTitle = massmailTitle;
	}
	public void setRejectID(int rejectID){
		this.rejectID = rejectID;
	}
	public int getRejectID(){
		return rejectID;
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
	
	
	public void setMassmailGroupID(int massmailGroupID){
		this.massmailGroupID = massmailGroupID;
	}
	public int getMassmailGroupID(){
		return massmailGroupID;
	}
	
	public void setTargetID(int targetID){
		this.targetID = targetID;
	}
	public int getTargetID(){
		return targetID;
	}
	
	public void setMassmailGroupName(String massmailGroupName){
		this.massmailGroupName = massmailGroupName;
	}
	public String getMassmailGroupName(){
		return massmailGroupName;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	
	public void setCustomerID(String customerID){
		this.customerID = customerID;
	}
	public String getCustomerID(){
		return customerID;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setGroupID(String groupID){
		this.groupID = groupID;
	}
	public String getGroupID(){
		return groupID;
	}


}
