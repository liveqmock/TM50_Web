package web.content.mailtemplate.model;

import java.io.Serializable;

/**
 * tm_mailtemplate 테이블
 * @author 김유근 
 */
@SuppressWarnings("serial")
public class MailTemplate implements Serializable{
	private int templateID=0;
	private String templateName;
	private String templateContent;
	private String userID;
	private String userName;
	private String shareGroupID;
	private String useYN;
	private String groupName;
	private String registDate;
	private String templateType;
	

	public void setTemplateID(int templateID){
		this.templateID = templateID;
	}
	public int getTemplateID(){
		return templateID;
	}
	
	public void setTemplateName(String templateName){
		this.templateName = templateName;
	}
	public String getTemplateName(){
		return templateName;
	}

	public void setTemplateContent(String templateContent){
		this.templateContent = templateContent;
	}
	public String getTemplateContent(){
		return templateContent;
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

	
	public void setShareGroupID(String shareGroupID){
		this.shareGroupID = shareGroupID;
	}
	public String getShareGroupID(){
		return shareGroupID;
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
	public String getRegistDate(){
		return registDate;
	}
	
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getGroupName(){
		return groupName;
	}
	
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}	
	
}
