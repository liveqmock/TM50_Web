package web.content.poll.model;

import java.io.Serializable;



/**
 * <p>설문응답
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class PollAnswer implements Serializable{
	
	private int massmailID;
	private int questionID;
	private int scheduleID;
	private int pollID;
	private int sendID;
	private int exampleID;
	private int matrixX;
	private int matrixY;
	private String email;
	private String responseText;
	private String registDate;
	private int ranking;
	
	
	
	public void setMassmailID(int massmailID){
		this.massmailID = massmailID;
	}
	public int getMassmailID(){
		return massmailID;
	}
	
	public void setQuestionID(int questionID){
		this.questionID = questionID;
	}
	public int getQuestionID(){
		return questionID;
	}
	
	
	public void setScheduleID(int scheduleID){
		this.scheduleID = scheduleID;
	}
	public int getScheduleID(){
		return scheduleID;
	}	
	
	public void setPollID(int pollID){
		this.pollID = pollID;
	}
	public int getPollID(){
		return pollID;
	}
	
	public void setSendID(int sendID){
		this.sendID = sendID;
	}
	public int getSendID(){
		return sendID;
	}
	
	public void setExampleID(int exampleID){
		this.exampleID = exampleID;
	}
	public int getExampleID(){
		return exampleID;
	}
	
	public void setMatrixX(int matrixX){
		this.matrixX = matrixX;
	}
	public int getMatrixX(){
		return matrixX;
	}
	
	public void setMatrixY(int matrixY){
		this.matrixY = matrixY;
	}
	public int getMatrixY(){
		return matrixY;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return email;
	}
	
	
	public void setResponseText(String responseText){
		this.responseText = responseText;
	}
	public String getResponseText(){
		return responseText;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	
}
