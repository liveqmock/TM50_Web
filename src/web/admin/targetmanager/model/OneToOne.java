package web.admin.targetmanager.model;

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
	
}