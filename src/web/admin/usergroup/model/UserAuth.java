package web.admin.usergroup.model;

import java.io.Serializable;

/**
 * <p>tm_users_auth 테이블정보 (사용자권한)
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class UserAuth  implements Serializable{
	
	private String userID;
	private String mainMenuID;
	private String subMenuID;
	private String authType;
	private String subMenuAuth;
	private String registDate;
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setMainMenuID(String mainMenuID){
		this.mainMenuID = mainMenuID;
	}
	public String getMainMenuID(){
		return mainMenuID;
	}
	
	public void setSubMenuID(String subMenuID){
		this.subMenuID = subMenuID;
	}
	public String getSubMenuID(){
		return subMenuID;
	}
	
	public void setAuthType(String authType){
		this.authType = authType;
	}
	public String getAuthType(){
		return authType;
	}
	
	public void setSubMenuAuth(String subMenuAuth){
		this.subMenuAuth = subMenuAuth;
	}
	public String getSubMenuAuth(){
		return subMenuAuth;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getResitDate(){
		return registDate;
	}
	
	
	
	
}
