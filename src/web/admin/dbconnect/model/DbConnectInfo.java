package web.admin.dbconnect.model;

import java.io.Serializable;
import web.common.util.DateUtils;


/**
 * <p>연동테이블 ez_connectdb_info 
 * @author 김유근
 *
 */
/**
 * @author limyh
 *
 */
@SuppressWarnings("serial")
public class DbConnectInfo implements Serializable{

	private String dbID;
	private String dbName;
	private String connectDBName;
	private String tableName;
	private String queryText;
	private String userID;
	private String useYN;
	private String updateType; 					//1: 1회 수집, 2: 매주, 3:매월 
	private int updateValue=0;					//수집값 (요일 및 날짜)
	private String updateScheduleDate;		//수집예정일
	private String updateScheduleHour;		//수집예정시간
	private String updateScheduleMinute;		//수집예정시간
	private String updateStartDate;				//수집시작일
	private String updateEndDate;				//수집종료일
	private int totalCount=0;							//수집된 레코드카운트 
	private String registDate;
	private String state;
	private String defaultYN="N";
	
	
	public void setDbID(String dbID){
		this.dbID = dbID;
	}
	public String getDbID(){
		return dbID;
	}
	
	public void setDbName(String dbName){
		this.dbName = dbName;
	}
	public String getDbName(){
		return dbName;
	}
	
	
	public String getConnectDBName() {
		return connectDBName;
	}
	public void setConnectDBName(String connectDBName) {
		this.connectDBName = connectDBName;
	}
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	public String getTableName(){
		return tableName;
	}
	
	public void setQueryText(String queryText){
		this.queryText = queryText;
	}
	public String getQueryText(){
		return queryText;
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
	
	public void setUpdateType(String updateType){
		this.updateType = updateType;
	}	
	public String getUpdateType(){
		return updateType;
	}
	
	public void setUpdateValue(int updateValue){
		this.updateValue = updateValue;
	}	
	public int getUpdateValue(){
		return updateValue;
	}
	
	public void setUpdateScheduleDate(String updateScheduleDate){		
		this.updateScheduleDate = DateUtils.getDateSubstring(updateScheduleDate);
	}	
	public String getUpdateScheduleDate(){
		return updateScheduleDate;
	}
	
	
	public void setUpdateScheduleHour(String updateScheduleHour){		
		this.updateScheduleHour = DateUtils.getDateSubstring(updateScheduleHour);
	}	
	public String getUpdateScheduleHour(){
		return updateScheduleHour;
	}
	
	public void setUpdateScheduleMinute(String updateScheduleMinute){		
		this.updateScheduleMinute = DateUtils.getDateSubstring(updateScheduleMinute);
	}	
	public String getUpdateScheduleMinute(){
		return updateScheduleMinute;
	}
	
	public void setUpdateStartDate(String updateStartDate){		
		this.updateStartDate = DateUtils.getDateSubstring(updateStartDate);
	}	
	public String getUpdateStartDate(){
		return updateStartDate;
	}
	
	public void setUpdateEndDate(String updateEndDate){
		this.updateEndDate = DateUtils.getDateSubstring(updateEndDate);
	}	
	public String getUpdateEndDate(){
		return updateEndDate;
	}
	
	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}
	public int getTotalCount(){
		return totalCount;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	
	public String getRegistDate(){
		return registDate;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public void setDefaultYN(String defaultYN){
		this.defaultYN = defaultYN;
	}
	public String getDefaultYN(){
		return defaultYN;
	}
	
}
