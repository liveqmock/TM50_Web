package web.target.targetlist.model;

import java.io.Serializable;


/**
 * <p>원투원정보테이블 tm_onetoone
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class OneToOne implements Serializable{

	private int onetooneID;
	private String onetooneName;
	private String onetooneAlias;
	private String fieldDesc;
	private String fieldName;
	
	
	

	public void setOnetooneID(int onetooneID){
		this.onetooneID = onetooneID;
	}
	public int getOnetooneID(){
		return onetooneID;
	}
	
	public void setOnetooneName(String onetooneName){
		this.onetooneName = onetooneName;
	}
	public String getOnetooneName(){
		return onetooneName;
	}
	
	public void setOnetooneAlias(String onetooneAlias){
		this.onetooneAlias = onetooneAlias;
	}
	public String getOnetooneAlias(){
		return onetooneAlias;
	}
	public String getFieldDesc() {
		return fieldDesc;
	}
	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
