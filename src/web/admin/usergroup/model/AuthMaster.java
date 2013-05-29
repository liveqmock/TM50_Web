package web.admin.usergroup.model;

import java.io.Serializable;


/**
 * <p>tm_users 테이블
 * @author coolang (김유근)
 */
@SuppressWarnings("serial")
public class AuthMaster  implements Serializable {


	private String subMenuID = "";
	private String fieldName = "";
	private String descript = "";
	
	public void setSubMenuID(String subMenuID){
		this.subMenuID = subMenuID;
	}
	
	public String getSubMenuID(){
		return subMenuID;
	}
	
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	}
	
	public String getFieldName(){
		return fieldName;
	}
	
	public void setDescript(String descript){
		this.descript = descript;
	}
	
	public String getDescript(){
		return descript;
	}
}
