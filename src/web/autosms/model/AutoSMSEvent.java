package web.autosms.model;

import java.io.Serializable;



/**
 * <p>자동SMS 이벤트
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class AutoSMSEvent implements Serializable{

	private int autosmsID;
	private String autosmsTitle;
	private String userID;
	private String userName;
	private String description;
	private String templateSenderPhone;
	private String templateMsg;
	private String state;
	private String registDate;
	
	public void setAutosmsID(int autosmsID){
		this.autosmsID = autosmsID;
	}
	public int getAutosmsID(){
		return autosmsID;
	}
	
	public void setAutosmsTitle(String autosmsTitle){
		this.autosmsTitle = autosmsTitle;
	}
	public String getAutosmsTitle(){
		return autosmsTitle;
	}
	
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
	
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	
	public void setTemplateSenderPhone(String templateSenderPhone){
		this.templateSenderPhone = templateSenderPhone;
	}
	public String getTemplateSenderPhone(){
		return templateSenderPhone;
	}
	
	public void setTemplateMsg(String templateMsg){
		this.templateMsg = templateMsg;
	}
	public String getTemplateMsg(){
		return templateMsg;
	}
	
	public void setState(String state){
		this.state = state;
	}
	public String getState(){
		return state;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
}
