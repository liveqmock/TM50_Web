package web.target.targetlist.model;

public class TargetList {

	private int targetID;
	private String targetName;
	private String description;
	private String userID;
	private String userName;
	private String groupID;
	private String bookMark;
	private String shareType;
	private String shareID;
	private String targetType;
	private String realFileName;		//실제 보여지는 파일명
	private String newFileName;		//디렉토리에 저장된 파일명
	private String uploadKey;
	private String dbID;
	private String connectedDbID;
	private String queryText;	
	private String countQuery;
	private int targetCount;
	private String directText;
	private String state; 
	private String startDate;
	private String endDate;
	private String sendedDate;
	private int massmailGroupID=0;
	private String successYN;
	private String openYN;
	private String clickYN;
	private String rejectcallYN;
	private String targetTable;
	private String registDate;
	private int targetGroupID=0;
	
	
	public void setTargetID(int targetID){
		this.targetID = targetID;
	}
	public int getTargetID(){
		return targetID;		
	}
	
	public void setTargetName(String targetName){
		this.targetName = targetName;
	}
	public String getTargetName(){
		return targetName;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserName(){
		return userName;
	}
	
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	
	public void setBookMark(String bookMark){
		this.bookMark = bookMark;
	}	
	public String getBookMark(){
		return bookMark;
	}
	
	public void setShareType(String shareType){
		this.shareType = shareType;
	}	
	public String getShareType(){
		return shareType;
	}
	
	public void setShareID(String shareID){
		this.shareID = shareID;
	}	
	public String getShareID(){
		return shareID;
	}
	
	public void setTargetType(String targetType){
		this.targetType = targetType;
	}	
	public String getTargetType(){
		return targetType;
	}

	
	public void setRealFileName(String realFileName){
		this.realFileName = realFileName;
	}	
	public String getRealFileName(){
		return realFileName;
	}
	
	public void setNewFileName(String newFileName){
		this.newFileName = newFileName;
	}	
	public String getNewFileName(){
		return newFileName;
	}
	
	public void setUploadKey(String uploadKey){
		this.uploadKey = uploadKey;
	}	
	public String getUploadKey(){
		return uploadKey;
	}
	
	public void setDbID(String dbID){
		this.dbID = dbID;
	}	
	public String getDbID(){
		return dbID;
	}
	
	public String getConnectedDbID() {
		return connectedDbID;
	}
	public void setConnectedDbID(String connectedDbID) {
		this.connectedDbID = connectedDbID;
	}
	
	public void setQueryText(String queryText){
		this.queryText = queryText;
	}	
	public String getQueryText(){
		return queryText;
	}
	

	public void setCountQuery(String countQuery){
		this.countQuery = countQuery;
	}	
	public String getCountQuery(){
		return countQuery;
	}
	
	public void setTargetCount(int targetCount){
		this.targetCount = targetCount;
	}	
	public int getTargetCount(){
		return targetCount;
	}
	
	public void setDirectText(String directText){
		this.directText = directText;
	}	
	public String getDirectText(){
		return directText;
	}
	
	public void setState(String state){
		this.state = state;
	}	
	public String getState(){
		return state;
	}
	
	
	public void setStartDate(String startDate){
		this.startDate = startDate;
	}	
	public String getStartDate(){
		return startDate;
	}
	
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}	
	public String getEndDate(){
		return endDate;
	}
	public String getSendedDate() {
		return sendedDate;
	}
	public void setSendedDate(String sendedDate) {
		this.sendedDate = sendedDate;
	}
	
	public int getMassmailGroupID() {
		return massmailGroupID;
	}
	public void setMassmailGroupID(int massmailGroupID) {
		this.massmailGroupID = massmailGroupID;
	}
	public void setSuccessYN(String successYN){
		this.successYN = successYN;
	}	
	public String getSuccessYN(){
		return successYN;
	}
	
	public void setOpenYN(String openYN){
		this.openYN = openYN;
	}	
	public String getOpenYN(){
		return openYN;
	}
	
	public void setClickYN(String clickYN){
		this.clickYN = clickYN;
	}	
	public String getClickYN(){
		return clickYN;
	}
	
	public String getRejectcallYN() {
		return rejectcallYN;
	}
	
	public void setRejectcallYN(String rejectcallYN) {
		this.rejectcallYN = rejectcallYN;
	}
	
	public void setTargetTable(String targetTable){
		this.targetTable = targetTable;
	}
	public String getTargetTable(){
		return targetTable;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}	
	public String getRegistDate(){
		return registDate;
	}
	/**
	 * @return the targetGroupID
	 */
	public int getTargetGroupID() {
		return targetGroupID;
	}
	/**
	 * @param targetGroupID the targetGroupID to set
	 */
	public void setTargetGroupID(int targetGroupID) {
		this.targetGroupID = targetGroupID;
	}
	
}
