package web.content.tester.model;


/**
 * <p>테스터 관리(ez_massmail_tester)
 * @author 김유근
 *
 */
public class Tester {

	private int testerID;
	private String testerName;
	private String testerEmail;
	private String testerHp;
	private String userID;
	private String registDate;
	private String userName;
	
	
	public void setTesterID(int testerID){
		this.testerID = testerID;
	}
	public int getTesterID(){
		return testerID;
	}
	
	public void setTesterName(String testerName){
		this.testerName = testerName;
	}
	public String getTesterName(){
		return testerName;
	}
	
	public void setTesterEmail(String testerEmail){
		this.testerEmail = testerEmail;
	}
	public String getTesterEmail(){
		return testerEmail;
	}
	
	public void setTesterHp(String testerHp){
		this.testerHp = testerHp;
	}
	public String getTesterHp(){
		return testerHp;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
}
