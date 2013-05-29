package web.massmail.statistic.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * <p>설문결과 
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class MassMailStatisticPoll implements Serializable{
	
	private int pollID;
	private int questionID;
	private int questioNo;
	private int exampleID;						//보기아이디
	private int exampleMultiCount;			//중복개수
	private String questionText;				//질문내용
	private String questionType;				//1.단일질문 2.일반매트릭스
	private String requiredYN;					//필수여부
	private String exampleType;				//응답타입 : 1:객관식, 2:주관식	
	private String exampleGubun;			// //1:단일응답, 2:복수응답
	private String exampleDesc;				//보기 지문
	private int responseCount=0;				//응답건수 
	private String[] responseCountArry;				//응답건수(순위선택)
	private int questionCount;				//질문응답건수 
	private String responseRatio;				//응답율 
	private int sendTotalCount=0;		//전체메이발송건수
	private int responseExampleID;		//선택한 설문응답아이디
	private String responseText;				//설문내용 
	private String exampleExYN;				//객관식 보기중에 주관식인 경우 
	private String email;				//이메일 : 설문 개인별 응답 현황 용 
	private String name;				//고객명 : 설문 개인별 응답 현황 용
	private String exampleDesc_responseText; //설문 개인별 응답 현황 용
	public ArrayList<Integer> arrayListValue  = new ArrayList<Integer>(); 	//매트릭스 Y값의 컬럼을 담기 위해 
	public String pollEndDate;			//설문통계 - 설문 마감일
	public String pollendType;			//설문통계 - 설문 종료 조건
	public int aimAnswerCnt;			//설문통계 - 목표 응답수
	
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
	
	public void setQuestionNo(int questioNo){
		this.questioNo = questioNo;
	}
	public int getQuestionNo(){
		return questioNo;
	}
	
	public void setExampleID(int exampleID){
		this.exampleID = exampleID;
	}
	public int getExampleID(){
		return exampleID;
	}
	
	public void setExampleMultiCount(int exampleMultiCount){
		this.exampleMultiCount = exampleMultiCount;
	}
	public int getExampleMultiCount(){
		return exampleMultiCount;
	}
	
	public void setQuestionText(String questionText){
		this.questionText = questionText;
	}
	public String getQuestionText(){
		return questionText;
	}
	
	public void setQuestionType(String questionType){
		this.questionType = questionType;
	}
	public String getQuestionType(){
		return questionType;
	}
	
	public void setRequiredYN(String requiredYN){
		this.requiredYN = requiredYN;
	}
	public String getRequiredYN(){
		return requiredYN;
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
	
	public void setExampleDesc(String exampleDesc){
		this.exampleDesc = exampleDesc;
	}
	public String getExampleDesc(){
		return exampleDesc;
	}
	
	public void setResponseCount(int responseCount){
		this.responseCount = responseCount;
	}
	public int getResponseCount(){
		return responseCount;
	}
	
	public int getQuestioNo() {
		return questioNo;
	}
	public void setQuestioNo(int questioNo) {
		this.questioNo = questioNo;
	}
	public String[] getResponseCountArry() {
		return responseCountArry;
	}
	public void setResponseCountArry(String[] responseCountArry) {
		this.responseCountArry = responseCountArry;
	}
	public ArrayList<Integer> getArrayListValue() {
		return arrayListValue;
	}
	public void setArrayListValue(ArrayList<Integer> arrayListValue) {
		this.arrayListValue = arrayListValue;
	}
	public void setQuestionCount(int questionCount){
		this.questionCount = questionCount;
	}
	public int getQuestionCount(){
		return questionCount;
	}
	
	public void setResponseRatio(String responseRatio){
		this.responseRatio = responseRatio;
	}
	public String getResponseRatio(){
		return responseRatio;
	}
	
	public void setSendTotalCount(int sendTotalCount){
		this.sendTotalCount = sendTotalCount;
	}
	public int getSendTotalCount(){
		return sendTotalCount;
	}
	
	public void setResponseExampleID(int responseExampleID){
		this.responseExampleID = responseExampleID;
	}
	public int getResponseExampleID(){
		return responseExampleID;
	}
	
	public void setResponseText(String responseText){
		this.responseText = responseText;
	}
	public String getResponseText(){
		return responseText;
	}
	
	public void setExampleExYN(String exampleExYN){
		this.exampleExYN = exampleExYN;
	}
	public String getExampleExYN(){
		return exampleExYN;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getExampleDesc_responseText() {
		return exampleDesc_responseText;
	}
	public void setExampleDesc_responseText(String exampleDesc_responseText) {
		this.exampleDesc_responseText = exampleDesc_responseText;
	}
	
	public String getPollEndDate() {
		return pollEndDate;
	}
	public void setPollEndDate(String pollEndDate) {
		this.pollEndDate = pollEndDate;
	}
	public String getPollendType() {
		return pollendType;
	}
	
	public void setPollendType(String pollendType) {
		this.pollendType = pollendType;
	}
	public int getAimAnswerCnt() {
		return aimAnswerCnt;
	}
	public void setAimAnswerCnt(int aimAnswerCnt) {
		this.aimAnswerCnt = aimAnswerCnt;
	}	

}
