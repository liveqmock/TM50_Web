package web.admin.dbjdbcset.model;

import java.io.Serializable;

/**
 * <p>tm_dbset, tm_jdbcset  테이블 
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class DbJdbcSet implements Serializable{

	private String dbID = "0";
	private String dbName = "";					//db명
	private String dbURL = "";						//db접속 url
	private String dbUserID = "";					//접속계정
	private String dbUserPWD = "";				//접속패스워드
	private String dbAccessKey = "";			//DB접근키
	private String driverID = "";					//tm_setjdbc의 driverID
	private String encodingYN = "";				//인코딩여부
	private String description = "";	
	private String useYN = "";						
	private String defaultYN = "";					//디폴트여부	
	private JdbSet jdbcSet = null;
	
	public DbJdbcSet(){
		jdbcSet = new JdbSet();
	}
		
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
	
	public void setDbURL(String dbURL){
		this.dbURL = dbURL;
	}
	
	public String getDbURL(){
		return dbURL;
	}
	
	public void setDbUserID(String dbUserID){
		this.dbUserID = dbUserID;
	}
	
	public String getDbUserID(){
		return dbUserID;
	}
	
	public void setDbUserPWD(String dbUserPWD){
		this.dbUserPWD = dbUserPWD;
	}
	
	public String getDbUserPWD(){
		return dbUserPWD;
	}
	
	public String getDbAccessKey() {
		return dbAccessKey;
	}

	public void setDbAccessKey(String dbAccessKey) {
		this.dbAccessKey = dbAccessKey;
	}
	
	public void setDriverID(String driverID){
		this.driverID = driverID;
	}
	
	public String getDriverID(){
		return driverID;
	}
	
	public void setEncodingYN(String encodingYN){
		this.encodingYN = encodingYN;
	}
	
	public String getEncodingYN(){
		return encodingYN;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	
	
	public void setDefaultYN(String defaultYN){
		this.defaultYN = defaultYN;
	}
	
	public String getDefaultYN(){
		return defaultYN;
	}
	
	public void setUseYN(String useYN){
		this.useYN = useYN;
	}
	
	public String getUseYN(){
		return useYN;
	}
	
	
	
	public void setJdbcDriverID(String driverID){
		jdbcSet.setDriverID(driverID);
	}	
	
	public String getJdbcDriverID(){
		return jdbcSet.getDriverID();
	}
	
	
	public void setJdbcDriverName(String driverName){
		jdbcSet.setDriverName(driverName);
	}
	
	public String getJdbcDriverName(){
		return jdbcSet.getDriverName();
	}
	
	
	public void setJdbcDriverClass(String driverClass){
		jdbcSet.setDriverClass(driverClass);
	}	
	
	public String getJdbcDriverClass(){
		return jdbcSet.getDriverClass();
	}
	
	public void setJdbcSampleURL(String sampleURL){
		jdbcSet.setSampleURL(sampleURL);
	}
	
	public String getJdbcSampleURL(){
		return jdbcSet.getSampleURL();
	}
}
	

/*
 * tm_jdbcset 테이블 
 */
class JdbSet{
	
	private String driverID	= "";					//드라이버ID
	private String driverName = "";			//드라이버명
	private String driverClass = "";			//드라이버클래스
	private String sampleURL = "";			//샘플 URL 
	
	public void setDriverID(String driverID){
		this.driverID = driverID;
	}
	
	public String getDriverID(){
		return driverID;
	}
	
	public void setDriverName(String driverName){
		this.driverName = driverName;
	}
	
	public String getDriverName(){
		return driverName;
	}
	
	
	public void setDriverClass(String driverClass){
		this.driverClass = driverClass;
	}
	
	public String getDriverClass(){
		return driverClass;
	}
	
	public void setSampleURL(String sampleURL){
		this.sampleURL = sampleURL;
	}
	
	public String getSampleURL(){
		return sampleURL;
	}
}

