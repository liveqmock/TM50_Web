package web.content.poll.model;

import java.io.Serializable;


/**
 * <p>설문코드 
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class PollCode implements Serializable{

	private String codeID ;
	private int codeNo;
	private String codeName;
	private String codeDesc;
	private String useYN;
	private String codeType;
	private String codeTypeDesc;
	
	public void setCodeID(String codeID){
		this.codeID = codeID;
	}
	public String getCodeID(){
		return codeID;
	}
	
	public void setCodeNo(int codeNo){
		this.codeNo = codeNo;
	}
	public int getCodeNo(){
		return codeNo;
	}
	
	public void setCodeName(String codeName){
		this.codeName = codeName;		
	}
	public String getCodeName(){
		return codeName;
	}
	
	public void setCodeDesc(String codeDesc){
		this.codeDesc = codeDesc;
	}
	public String getCodeDesc(){
		return codeDesc;
	}
	
	public void setUseYN(String useYN){
		this.useYN = useYN;
	}
	public String getUseYN(){
		return useYN;
	}
	
	public void setCodeType(String codeType){
		this.codeType = codeType;
	}
	public String getCodeType(){
		return codeType;
	}
	
	public void setCodeTypeDesc(String codeTypeDesc){
		this.codeTypeDesc = codeTypeDesc;
	}
	public String getCodeTypeDesc(){
		return codeTypeDesc;
	}
	
}
