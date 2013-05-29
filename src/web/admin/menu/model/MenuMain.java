
package web.admin.menu.model;

import java.io.Serializable;

/**
 * <p>tm_mainmenu 테이블정보를 담는다.
 * @author coolang (김유근)
 * 
 */
@SuppressWarnings("serial")
public class MenuMain implements Serializable{
	
	private String mainMenuID = "";			//메인메뉴아이디
	private String mainMenuName = "";	//메인메뉴명	
	private int priorNum = 1;						 //우선순위값 (디폴트=1) 
	private String useYN = "Y";					//초기
	private String isAdmin = "N";

	
	public void setMainMenuID(String mainMenuID){
		this.mainMenuID = mainMenuID;
	}
	public String getMainMenuID(){
		return mainMenuID;
	}
	
	public void setMainMenuName(String mainMenuName){
		this.mainMenuName = mainMenuName;
	}
	
	public String getMainMenuName(){
		return mainMenuName;
	}
	
	public  void setPriorNum(int priorNum){
		this.priorNum = priorNum;
	}
	
	public int getPriorNum(){
		return priorNum;
	}
	
	public void setUseYN(String useYN){
		this.useYN = useYN;
	}
	
	public String getUseYN(){
		return useYN;
	}

	public void setIsAdmin(String isAdmin){
		this.isAdmin = isAdmin;
	}
	
	public String getIsAdmin(){
		return isAdmin;
	}
	

}
