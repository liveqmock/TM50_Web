package web.admin.sender.model;

import java.io.Serializable;


/**
 * <p>보내는 사람 (tm_sender)
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class Sender implements Serializable {
	
	private int senderID;
	private String senderName;				//보내는 사람명 
	private String senderEmail;				//보내는 이메일
	private String senderCellPhone;			//보내는 사람 핸드폰 
	private String description;				//설명
	private String userID;					//작성자ID
	private String userName;				//작성자 이름
	private String shareType;				//1;전체사용, 2:그룹사용 , 3:개인사용
	private String shareTypeDesc;			//shareType설명
	private String useYN;
	private String defaultYN;
	private String registDate;
	
	
	public void setSenderID(int senderID){
		this.senderID = senderID;
	}
	public int getSenderID(){
		return senderID;
	}
	
	public void setSenderName(String senderName){
		this.senderName = senderName;
	}
	public String getSenderName(){
		return senderName;
	}
	
	public void setSenderEmail(String senderEmail){
		this.senderEmail = senderEmail;
	}
	public String getSenderEmail(){
		return senderEmail;
	}
	
	public void setSenderCellPhone(String senderCellPhone){
		this.senderCellPhone = senderCellPhone;
	}
	public String getSenderCellPhone(){
		return senderCellPhone;
	}
	
	
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setShareType(String shareType){
		this.shareType = shareType;
	}
	public String getShareType(){
		return shareType;
	}
	
	public void setShareTypeDesc(String shareTypeDesc){
		this.shareTypeDesc = shareTypeDesc;
	}
	public String getShareTypeDesc(){
		return shareTypeDesc;
	}
	
	public void setUseYN(String useYN){
		this.useYN = useYN;
	}	
	public String getUseYN(){
		return useYN;
	}
	
	public void setDefaultYN(String defaultYN){
		this.defaultYN = defaultYN;
	}	
	public String getDefaultYN(){
		return defaultYN;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	
}
