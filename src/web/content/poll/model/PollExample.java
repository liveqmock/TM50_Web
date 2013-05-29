package web.content.poll.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PollExample  implements Serializable{

	private int questionID;
	private int exampleID;
	private int pollID;
	private String exampleType;		//응답형식 : 1: 라디오버튼	2: 체크박스	3: 주관식입력	
	private String exampleDesc;			//보기내용 
	private String exampleExYN;		//주관식여부 Y,N
	private int goToQuestionNo=0;			//응답시 해당 questionID으로 이동
	private String matrixXY="";				//매트릭스 X, Y 축 구분	
	private String registDate;	
	private String fileURL;	
	private String layoutType;
	private int noResponseStart=0;
	private int noResponseEnd=0;
	
	
	public void setQuestionID(int questionID){
		this.questionID = questionID;
	}
	public int getQuestionID(){
		return questionID;
	}
		
	public void setExampleID(int exampleID){
		this.exampleID = exampleID;
	}
	public int getExampleID(){
		return exampleID;
	}
		
	public void setPollID(int pollID){
		this.pollID = pollID;
	}
	public int getPollID(){
		return pollID;
	}
	
	public void setExampleType(String exampleType){
		this.exampleType = exampleType;
	}
	public String getExampleType(){
		return exampleType;
	}	

	
	public void setExampleDesc(String exampleDesc){
		this.exampleDesc = exampleDesc;
	}
	public String getExampleDesc(){
		return exampleDesc;
	}	
	
	public void setExampleExYN(String exampleExYN){
		this.exampleExYN = exampleExYN;
	}
	public String getExampleExYN(){
		return exampleExYN;
	}	

	public void setGoToQuestionNo(int goToQuestionNo){
		this.goToQuestionNo = goToQuestionNo;
	}
	public int getGoToQuestionNo(){
		return goToQuestionNo;
	}

	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	
	public void setMatrixXY(String matrixXY){
		this.matrixXY = matrixXY;
	}
	public String getMatrixXY(){
		return matrixXY;
	}
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public String getLayoutType() {
		return layoutType;
	}
	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}
	public int getNoResponseStart() {
		return noResponseStart;
	}
	public void setNoResponseStart(int noResponseStart) {
		this.noResponseStart = noResponseStart;
	}
	public int getNoResponseEnd() {
		return noResponseEnd;
	}
	public void setNoResponseEnd(int noResponseEnd) {
		this.noResponseEnd = noResponseEnd;
	}
	
}
