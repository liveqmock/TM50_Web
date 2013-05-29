package web.admin.login.model;

import java.io.Serializable;


/**
 * <p>tm_users, tm_usergroup 조인정보 
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class Users  implements Serializable{

	private String userID;
	private String userName;
	private String groupID;
	private String groupName;
	private String userLevel;
	private String isAdmin;
	private String auth_csv;
	private String auth_direct;
	private String auth_related;
	private String auth_query;
	private String auth_write_mail;
	private String auth_send_mail;
	private String auth_write_sms;
	private String auth_send_sms;
	private String senderName;
	private String senderEmail;
	private String senderCellPhone;
	private int failCount;
	private String modifyDate;

	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserName(){
		return userName;
	}
	
	public void setGroupID(String groupID){
		this.groupID = groupID;
	}
	public String getGroupID(){
		return groupID;
	}
	
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getGroupName(){
		return groupName;
	}
	
	public void setUserLevel(String userLevel){
		this.userLevel = userLevel;
	}
	public String getUserLevel(){
		return userLevel;
	}
	
	
	public void setIsAdmin(String isAdmin){
		this.isAdmin = isAdmin;
	}
	public String getIsAdmin(){
		return isAdmin;
	}
	
	public void setAuth_csv(String auth_csv){
		this.auth_csv = auth_csv;
	}
	public String getAuth_csv(){
		return auth_csv;
	}
	
	public void setAuth_query(String auth_query){
		this.auth_query = auth_query;
	}
	public String getAuth_query(){
		return auth_query;
	}
	
	public void setAuth_write_mail(String auth_write_mail){
		this.auth_write_mail = auth_write_mail;
	}
	public String getAuth_write_mail(){
		return auth_write_mail;
	}
	
	public void setAuth_send_mail(String auth_send_mail){
		this.auth_send_mail = auth_send_mail;
	}
	public String getAuth_send_mail(){
		return auth_send_mail;
	}
	
	public void setAuth_write_sms(String auth_write_sms){
		this.auth_write_sms = auth_write_sms;
	}
	public String getAuth_write_sms(){
		return auth_write_sms;
	}
	
	public void setAuth_send_sms(String auth_send_sms){
		this.auth_send_sms = auth_send_sms;
	}
	public String getAuth_send_sms(){
		return auth_send_sms;
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
	public String getAuth_direct() {
		return auth_direct;
	}
	public void setAuth_direct(String auth_direct) {
		this.auth_direct = auth_direct;
	}
	public String getAuth_related() {
		return auth_related;
	}
	public void setAuth_related(String auth_related) {
		this.auth_related = auth_related;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
	
}
