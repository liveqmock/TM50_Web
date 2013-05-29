package web.admin.dbconnect.model;

import java.io.Serializable;


/**
 * <p>연동테이블 ez_connectdb_column
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class DbConnectColumn implements Serializable{
	
	private String dbID;
	private String columnName;
	private String columnType;
	private String columnLength;
	private String columnDesc;
	private String registDate;
	
	
	public void setDbID(String dbID){
		this.dbID = dbID;
	}
	public String getDbID(){
		return dbID;
	}
	
	public void setColumnName(String columnName){
		this.columnName = columnName;
	}
	public String getColumnName(){
		return columnName;
	}
	
	public void setColumnType(String columnType){
		this.columnType = columnType;
	}
	public String getColumnType(){
		return columnType;
	}
	
	public void setColumnLength(String columnLength){
		this.columnLength = columnLength;
	}
	public String getColumnLength(){
		return columnLength;
	}
	
	
	public void setColumnDesc(String columnDesc){
		this.columnDesc = columnDesc;
	}
	public String getColumnDesc(){
		return columnDesc;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate.substring(0,10);
	}
	
	public String getRegistDate(){
		return registDate;
	}

}
