
package web.admin.usergroup.model;

import java.io.Serializable;

/**
 * <p>tm_usersgroup 테이블
 * @author coolang
 */
@SuppressWarnings("serial")
public class Group implements Serializable {
	
	private String groupID = "";
	private String groupName = "";
	private String description = "";
	private int  userCount=0;			//소속구성원수 
	private String registDate="";
	private String isAdmin = "";
	
	
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
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	

	public void setUserCount(int userCount){
		this.userCount = userCount;
	}	
	public int getUserCount(){
		return userCount;
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
	
	

}
