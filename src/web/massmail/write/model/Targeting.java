package web.massmail.write.model;

import java.io.Serializable;


/**
 * <p>tm_targeting와 tm_users조인정보 
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class Targeting  implements Serializable{

	private int targetID;							//대상자ID
	private String userID;	
	private String userName;	
	private String groupID;
	private String groupName;
	private String targetName;					//대상자명
	private String targetType;					//대상자타입(1: 파일업로드, 2:쿼리)	
	private String dbID;								//DBID
	private String mode;							//전체공유 : 'all_share',그룹공유 : 'group_'+그룹아이디,개인: 'user_'+유저아이디
	private String queryText="";					//쿼리
	private int targetCount=0;						//대상자카운트 
	private String state;							//1: 등록, 2:처리중, 3:처리완료, 4:처리중에러

	
	
	public  void setTargetID(int targetID){
		this.targetID = targetID;		
	}
	public int getTargetID(){
		return targetID;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setTargetName(String targetName){
		this.targetName = targetName;
	}
	public String getTargetName(){
		return targetName;
	}
	
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getGroupName(){
		return groupName;
	}
	
	public void setTargetType(String targetType){
		this.targetType = targetType;
	}
	public String getTargetType(){
		return targetType;
	}
	
	public void setDbID(String dbID){
		this.dbID = dbID;
	}
	public String getDbID(){
		return dbID;
	}	
	
	
	public void setMode(String mode){
		this.mode = mode;
	}
	public String getMode(){
		return mode;
	}
	
	public void setQueryText(String queryText){
		this.queryText = queryText;
	}
	public String getQueryText(){
		return queryText;
	}	
	
	public void setTargetCount(int targetCount){
		this.targetCount = targetCount;		
	}
	public int getTargetCount(){
		return targetCount;
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
	
	
	public void setState(String state){
		this.state = state;
	}
	public String getState(){
		return state;
	}
	
	
}
