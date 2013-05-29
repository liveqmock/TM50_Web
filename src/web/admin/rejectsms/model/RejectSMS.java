package web.admin.rejectsms.model;

import java.io.Serializable;


/**
 * <p>tm_masssms_reject
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class RejectSMS  implements Serializable{

	private int rejectID;
	private String receiverPhone = "";	
	private String registDate = "";
	private String userID = "";
	private String groupID = "";	
	
	private String userName = "";

	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setRejectID(int rejectID){
		this.rejectID = rejectID;
	}
	public int getRejectID(){
		return rejectID;
	}
	
	
	public void setReceiverPhone(String receiverPhone){
		this.receiverPhone = receiverPhone;
	}
	public String getReceiverPhone(){
		return receiverPhone;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
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
