
package web.admin.menu.model;

import java.io.Serializable;

/**
 * <p>tm_submenu 테이블
 * @author coolang(김유근)
 */
@SuppressWarnings("serial")
public class MenuSub  implements Serializable{
	
	private String subMenuID = "";			//서브메뉴아이디
	private String subMenuName = "";	//서브메뉴명	
	private String mainMenuID = "";		//메인메뉴아이
	private int priorNum = 1;						 //우선순위값 (디폴트=1) 
	private String path	= "";						//서브메뉴 파일 경로 
	private String useYN="";					//초기
	private int width = 0;				// 윈도우의 가로 크기
	private int height = 0;				// 윈도우의 세로 크기 (0이면 자동)
	private int x = 0;					// 윈도우의 x 좌표 (0이면 자동)
	private int y = 0;					// 윈도우의 y 좌표 (0이면 자동)
	private String divider = "N";			//메뉴에 위선을 표시 (그룹화)
	private String windowId = "";	    // 윈도우 아이디
	private String tabPath = "";
	private String padding = "";		// 윈도우 컨텐츠에 padding 적용 여부
	private String accordian = "";		// 윈도우 컨텐츠에 accordian 적용 여부
	private String hasAuth = "";		// 서브메뉴에 해당하는 권한이 있는지 여부
	private String fullSize = "" ;      // 열리는 윈도우가  전체 영역을 차지하는지 여부
	private String popupYN = "" ;       // 팝업창 처리 여부
	public void setSubMenuID(String subMenuID){
		this.subMenuID = subMenuID;
	}
	public String getSubMenuID(){
		return subMenuID;
	}
	
	public void setSubMenuName(String subMenuName){
		this.subMenuName = subMenuName;
	}
	
	public String getSubMenuName(){
		return subMenuName;
	}
	
	public void setMainMenuID(String mainMenuID){
		this.mainMenuID = mainMenuID;
	}
	public String getMainMenuID(){
		return mainMenuID;
	}
	
	public  void setPriorNum(int priorNum){
		this.priorNum = priorNum;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public String getPath(){
		return path;
	}
	
	public int getPriorNum(){
		return priorNum;
	}
	
	public void setUseYN(String useYN){
		this.useYN = useYN;
	}
	
	public String getShowYN(){
		return useYN;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getX() {
		return this.x;
	}

	public void setY(int y){
		this.y= y;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setDivider(String divider){
		this.divider = divider;
	}

	public String getDivider(){
		return this.divider;
	}

	public void setWindowId(String windowId){
		this.windowId = windowId;
	}

	public String getWindowId(){
		return this.windowId;
	}
	
	public void setTabPath(String tabPath){
		this.tabPath = tabPath;
	}

	public String getTabPath(){
		return this.tabPath;
	}
	
	public void setPadding(String padding){
		this.padding = padding;
	}

	public String getPadding(){
		return this.padding;
	}
	
	public void setAccordian(String accordian){
		this.accordian = accordian;
	}

	public String getAccordian(){
		return this.accordian;
	}
	
	public void setHasAuth(String hasAuth){
		this.hasAuth = hasAuth;
	}

	public String getHasAuth(){
		return this.hasAuth;
	}
	public void setFullSize(String fullSize){
		this.fullSize = fullSize;
	}

	public String getFullSize(){
		return this.fullSize;
	}
	public String getPopupYN() {
		return popupYN;
	}
	public void setPopupYN(String popupYN) {
		this.popupYN = popupYN;
	}
	
}
