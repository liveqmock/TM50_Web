package web.content.poll.model;

import java.io.Serializable;


/**
 * <p>설문문항
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class PollQuestion implements Serializable{
	
	private int pollID;
	private int questionID;
	private int questionNo;				//질문번호
	private String questionType;			//1.단일질문 2.일반매트릭스,3.복합매트릭스
	private String questionHead;			//질문 머릿글	
	private String questionText;			//질문내용
	private String exampleType;			//응답타입 : 1:객관식, 2:주관식
	private String exampleGubun;			 //1:단일응답, 2:복수응답
	private int exampleMultiCount;
	private int exampleMultiMinimumCount;
	private String requiredYN="Y";				//필수여부 	 
	private int examplePosition;
	private int matrixTextSize;
	private String registDate;
	private String questionHTML;
	private String fileURL;
	private String layoutType;
	
	public void setPollID(int pollID){
		this.pollID = pollID;
	}
	public int getPollID(){
		return pollID;
	}
	
	public void setQuestionID(int questionID){
		this.questionID = questionID;
	}
	public int getQuestionID(){
		return questionID;
	}
	
	public void setQuestionType(String questionType){
		this.questionType = questionType;
	}
	public String getQuestionType(){
		return questionType;
	}
	
	public void setQuestionHead(String questionHead){
		this.questionHead = questionHead;
	}
	public String getQuestionHead(){
		return questionHead;
	}
	
	public void setQuestionNo(int questionNo){
		this.questionNo = questionNo;
	}
	public int getQuestionNo(){
		return questionNo;
	}
	
	public void setQuestionText(String questionText){
		this.questionText = questionText;
	}
	public String getQuestionText(){
		return questionText;
	}
	
	public void setExampleType(String exampleType){
		this.exampleType = exampleType;
	}
	public String getExampleType(){
		return exampleType;
	}
	
	public void setExampleGubun(String exampleGubun){
		this.exampleGubun = exampleGubun;
	}
	public String getExampleGubun(){
		return exampleGubun;
	}	
	
	
	public void setExampleMultiCount(int exampleMultiCount){
		this.exampleMultiCount = exampleMultiCount;
	}
	public int getExampleMultiCount(){
		return exampleMultiCount;
	}
	
	
	public int getExampleMultiMinimumCount() {
		return exampleMultiMinimumCount;
	}
	public void setExampleMultiMinimumCount(int exampleMultiMinimumCount) {
		this.exampleMultiMinimumCount = exampleMultiMinimumCount;
	}
	public void setRequiredYN(String requiredYN){
		this.requiredYN = requiredYN;
	}
	public String getRequiredYN(){
		return requiredYN;
	}
	
	public void setExamplePosition(int examplePosition){
		this.examplePosition = examplePosition;
	}
	public int getExamplePosition(){
		return examplePosition;
	}
	
	public void setMatrixTextSize(int matrixTextSize){
		this.matrixTextSize = matrixTextSize;
	}
	public int getMatrixTextSize(){
		return matrixTextSize;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	public String getQuestionHTML() {
		return questionHTML;
	}
	public void setQuestionHTML(String questionHTML) {
		this.questionHTML = questionHTML;
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
	
}
