package web.masssms.write.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OnetooneTarget  implements Serializable{
	private String onetooneName = "";
	private String onetooneAlias = "";
	private String fieldName = "";
	private String fieldDesc = "";
	
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
	
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	}	
	public String getFieldName(){
		return fieldName;
	}
	
	public void setFieldDesc(String fieldDesc){
		this.fieldDesc = fieldDesc;
	}	
	public String getFieldDesc(){
		return fieldDesc;
	}
		
}
