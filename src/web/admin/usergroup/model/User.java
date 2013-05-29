
package web.admin.usergroup.model;

import java.io.Serializable;


/**
 * <p>tm_users 테이블
 * @author coolang (김유근)
 */
@SuppressWarnings("serial")
public class User  implements Serializable {


	private String userID = "";
	private String userPWD = "";
	private String userName = "";
	private String groupID =""; 				//사용자 그룹아이디 
	private String groupName = "";
	private String userLevel ="";
	private String email = "";
	private String cellPhone = "";
	private String description = "";
	private String useYN = "";
	private String registDate="";
	private String isAdmin = "";
	private String authCSV = "";
	private String authQuery = "";
	private String authWriteMail = "";
	private String authSendMail = "";
	private String isHelper = "N";
	private String senderName = "";
	private String senderEmail = "";
	private String senderCellPhone = "";
	private String authDirect = "";
	private String authRelated = "";
	private String authWriteSMS = "";
	private String authSendSMS = "";
	private int failCount;
	
	
	
	
	
	/* 유저의 권한을 초기화 한다 */
	public void initUserAuth() {
		this.setAuthCSV("N");
		this.setAuthQuery("N");
		this.setAuthDirect("N");
		this.setAuthRelated("N");
		this.setAuthSendMail("N");
		this.setAuthWriteMail("N");
		this.setIsHelper("N");
		this.setAuthSendSMS("N");
		this.setAuthWriteSMS("N");
	}
	
	
	
	public String getSenderName() {
		return senderName;
	}



	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}



	public String getSenderEmail() {
		return senderEmail;
	}



	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}



	public String getSenderCellPhone() {
		return senderCellPhone;
	}



	public void setSenderCellPhone(String senderCellPhone) {
		this.senderCellPhone = senderCellPhone;
	}



	public String getIsHelper() {
		return isHelper;
	}



	public void setIsHelper(String ishelper) {
		this.isHelper = ishelper;
	}



	public void setUserID(String userID){
		this.userID = userID;
	}
	
	public String getUserID(){
		return userID;
	}
	
	public void setUserPWD(String userPWD){
		this.userPWD = userPWD;
	}
	
	public String getUserPWD(){
		return userPWD;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getGroupName(){
		return groupName;
	}	
	
	public void setGroupID(String groupID){
		this.groupID = groupID;
	}		
	
	public String  getGroupID(){
		return groupID;
	}
			
	public void setUserLevel(String userLevel){
		this.userLevel = userLevel;
	}		
	
	public String  getUserLevel(){
		return userLevel;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setCellPhone(String cellPhone){
		this.cellPhone = cellPhone;
	}
	
	public String getCellPhone(){
		return cellPhone;
	}
	
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setUseYN(String useYN){
		this.useYN = useYN;
	}
	
	public String getUseYN(){
		return useYN;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}	
	public String  getRegistDate(){
		return registDate;
	}
	public void setIsAdmin(String isAdmin){
		this.isAdmin = isAdmin;
	}
	
	public String getIsAdmin(){
		return isAdmin;
	}
	
	public String getLevelName(){
		String retv = "";
		
		if(this.isAdmin.equals("Y")) {
			retv = "수퍼관리자";
		} else {
			switch (Integer.parseInt(this.userLevel))
			{
				case 1 :
					retv = "시스템관리자";
					break;
				case 2 :
					retv = "그룹관리자";
					break;
				case 3 :
					retv = "일반사용자";
					break;
			}
		}
		return retv;		
	}

	public void setAuthCSV(String authCSV){
		this.authCSV = authCSV;
	}
	
	public String getAuthCSV(){
		return this.authCSV;
	}
	
	public void setAuthQuery(String authQuery){
		this.authQuery = authQuery;
	}
	
	public String getAuthQuery(){
		return this.authQuery;
	}

	public void setAuthWriteMail(String authWriteMail){
		this.authWriteMail = authWriteMail;
	}
	
	public String getAuthWriteMail(){
		return this.authWriteMail;
	}
	public void setAuthSendMail(String authSendMail){
		this.authSendMail = authSendMail;
	}
	
	public String getAuthSendMail(){
		return this.authSendMail;
	}



	public String getAuthDirect() {
		return authDirect;
	}



	public void setAuthDirect(String authDirect) {
		this.authDirect = authDirect;
	}



	public String getAuthRelated() {
		return authRelated;
	}



	public void setAuthRelated(String authRelated) {
		this.authRelated = authRelated;
	}

	
	public void setAuthWriteSMS(String authWriteSMS){
		this.authWriteSMS = authWriteSMS;
	}
	
	public String getAuthWriteSMS(){
		return this.authWriteSMS;
	}
	public void setAuthSendSMS(String authSendSMS){
		this.authSendSMS = authSendSMS;
	}
	
	public String getAuthSendSMS(){
		return this.authSendSMS;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
}
