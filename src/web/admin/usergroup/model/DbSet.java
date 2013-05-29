package web.admin.usergroup.model;

import java.io.Serializable;

/**
 * <p>tm_setdb테이블정보
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class DbSet  implements Serializable{
	
	private String dbID;
	private String dbName;
	private String description;
	
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
	
	public void setDescription(String description){
		this.description = description;		
	}
	public String getDescription(){
		return description;
	}
}
