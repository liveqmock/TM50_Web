package web.content.poll.model;

import java.io.Serializable;



/**
 * <p>설문템플릿
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class PollTemplate implements Serializable{

	private int pollTemplateID;
	private String pollTemplateTitle;
	private String pollTemplateHTML;
	private String userID;
	private String userName;
	private String shareGroupID;
	private String groupName;
	private String useYN;
	private String registDate;
	
	
	public void setPollTemplateID(int pollTemplateID){
		this.pollTemplateID = pollTemplateID;
	}
	public int getPollTemplateID(){
		return pollTemplateID;
	}
	
	public void setPollTemplateTitle(String pollTemplateTitle){
		this.pollTemplateTitle = pollTemplateTitle;
	}
	public String getPollTemplateTitle(){
		return pollTemplateTitle;
	}
	
	public void setPollTemplateHTML(String pollTemplateHTML){
		this.pollTemplateHTML = pollTemplateHTML;
	}
	public String getPollTemplateHTML(){
		return pollTemplateHTML;
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
	
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getGroupName(){
		return groupName;
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
}
