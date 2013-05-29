
package web.admin.board.model;

import java.io.Serializable;

/**
 * tm_board 테이블 
 * @author coolang(김유근)
 */

@SuppressWarnings("serial")
public class Board implements Serializable{
	
	private int boardID;				//board 아이디(유일키):자동증가
	private String userID;			//사용자 userid
	private String title;					//board 제목
	private String content;			//board 내용
	private String fileName;			//board 업로드 파일명
	private String registDate;		//board 등록일
	private int hit;							//board 읽은 카운트
	private int priorNum = 3;				//board 우선순위 낮은 순으로 정렬.
	private String userName;			// tm_user테이블의 userName 필드
	private String upload_key;			// 첨부파일 키
	private String readAuth = "3";
	

	
	public void setBoardID(int boardID){
		this.boardID = boardID;
	}
	
	public int getBoardID(){
		return boardID;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	
	public String getUserID(){
		return userID;
	}
	
	
	public void setRegistDate(String registDate){
		this.registDate = registDate.substring(0,10);
	}
	
	public String getRegistDate(){
		return registDate;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setHit(int hit){
		this.hit = hit;
	}
	
	public int getHit(){
		return hit;
	}
	
	public void setPriorNum(int priorNum){
		this.priorNum = priorNum;
	}
	
	public int getPriorNum(){
		return priorNum;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public String getFileName(){		
		return fileName;
	}
	
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public String getUserName(){		
		return userName;
	}

	public String getUpload_key() {
		return upload_key;
	}

	public void setUpload_key(String upload_key) {
		this.upload_key = upload_key;
	}

	public String getReadAuth() {
		return readAuth;
	}

	public void setReadAuth(String readAuth) {
		this.readAuth = readAuth;
	}
	
	

	
	
	
	

	
	
	
	
	
}
