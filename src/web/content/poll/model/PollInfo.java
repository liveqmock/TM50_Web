package web.content.poll.model;

import java.io.Serializable;



/**
 * <p>설문정보
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class PollInfo implements Serializable{
	private int pollID;
	private String pollTitle;
	private String description;
	private String shareGroupID;
	private String groupName;
	private String userName;
	private String userID;	
	private String startTitle;
	private String endTitle;	
	private String useYN;
	private String codeID;
	private int codeNo;
	private int pollTemplateID;
	private String resultPollHTML;
	private String resultFinishYN;
	private String defaultPollHTML;
	private String pollEndDate;		//설문마감일
	private String pollEndDateHH;
	private String pollEndDateMM;
	private String registDate;
	private String aimAnswerCnt;
	private String pollEndType;
	public void setPollID(int pollID){
		this.pollID = pollID;
	}
	public int getPollID(){
		return pollID;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setPollTitle(String pollTitle){
		this.pollTitle = pollTitle;
	}
	public String getPollTitle(){
		return pollTitle;
	}
	
	public void setShareGroupID(String shareGroupID){
		this.shareGroupID = shareGroupID;
	}
	public String getShareGroupID(){
		return shareGroupID;
	}
	
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getGroupName(){
		return groupName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserName(){
		return userName;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setUseYN(String useYN){
		this.useYN = useYN;
	}
	public String getUseYN(){
		return useYN;
	}
	
	public void setStartTitle(String startTitle){
		this.startTitle = startTitle;
	}
	public String getStartTitle(){
		return startTitle;
	}
	
	public void setEndTitle(String endTitle){
		this.endTitle = endTitle;
	}
	public String getEndTitle(){
		return endTitle;
	}	
	
	public void setCodeID(String codeID){
		this.codeID = codeID;
	}
	public String getCodeID(){
		return codeID;
	}
	
	public void setCodeNo(int codeNo){
		this.codeNo = codeNo;
	}
	public int getCodeNo(){
		return codeNo;
	}
	
	public void setPollTemplateID(int pollTemplateID){
		this.pollTemplateID = pollTemplateID;
	}
	public int getPollTemplateID(){
		return pollTemplateID;
	}
	
	public void setResultPollHTML(String resultPollHTML){
		this.resultPollHTML = resultPollHTML;
	}
	public String getResultPollHTML(){
		return resultPollHTML;
	}
	
	public void setResultFinishYN(String resultFinishYN){
		this.resultFinishYN = resultFinishYN;
	}
	public String getResultFinishYN(){
		return resultFinishYN;
	}
	
	public void setDefaultPollHTML(String defaultPollHTML){
		this.defaultPollHTML = defaultPollHTML;
	}
	public String getDefaultPollHTML(){
		return defaultPollHTML;
	}
	
	public void setPollEndDate(String pollEndDate){
		this.pollEndDate = pollEndDate;
	}
	public String getPollEndDate(){
		return pollEndDate;
	}
	
	public void setPollEndDateHH(String pollEndDateHH){
		this.pollEndDateHH = pollEndDateHH;
	}
	public String getPollEndDateHH(){
		return pollEndDateHH;
	}
	
	public void setPollEndDateMM(String pollEndDateMM){
		this.pollEndDateMM = pollEndDateMM;
	}
	public String getPollEndDateMM(){
		return pollEndDateMM;
	}
	
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	public String getAimAnswerCnt() {
		return aimAnswerCnt;
	}
	public void setAimAnswerCnt(String aimAnswerCnt) {
		this.aimAnswerCnt = aimAnswerCnt;
	}
	public String getPollEndType() {
		return pollEndType;
	}
	public void setPollEndType(String pollEndType) {
		this.pollEndType = pollEndType;
	}
	
}
