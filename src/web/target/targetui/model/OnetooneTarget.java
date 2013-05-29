package web.target.targetui.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OnetooneTarget  implements Serializable{
	
	private int targetID;
	private String fieldName;
	private int onetooneID;
	private String fieldDesc;					//필드설명(한글)
	private int csvColumnPos;				//csv컬럼명의 위치 table에는 없는 필드

	
	public void setTargetID(int targetID){
		this.targetID = targetID;
	}
	public int getTargetID(){
		return targetID;
	}	
	
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	}
	public String getFieldName(){
		return fieldName;
	}	
	
	public void setOnetooneID(int onetooneID){
		this.onetooneID = onetooneID;
	}
	public int getOnetooneID(){
		return onetooneID;
	}
	
	public void setFieldDesc(String fieldDesc){
		this.fieldDesc = fieldDesc;
	}
	public String getFieldDesc(){
		return fieldDesc;
	}
		
	
	public void setCsvColumnPos(int csvColumnPos){
		this.csvColumnPos = csvColumnPos;
	}
	public int getCsvColumnPos(){
		return csvColumnPos;
	}
	


}
