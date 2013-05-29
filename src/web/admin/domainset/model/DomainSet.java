package web.admin.domainset.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DomainSet  implements Serializable {
	
	private String domainFlag="";
	private int domainID; 
	private String domainName=""; 
	private int threadCount; 
	private String domainType=""; 
	private int socketTimeOut; 
	private int socketPerSendCount; 
	private String domainTypeDesc="'";  //도메인타입에 대한 설명 
	
	
	public void setDomainFlag(String domainFlag){
		this.domainFlag = domainFlag;
	}
	public String getDomainFlag(){
		return domainFlag;
	}
	
	public void setDomainID(int domainID){
		this.domainID = domainID; 
	}
	public int getDomainID(){ 
		return domainID; 
	}
	
	public void setDomainName(String domainName){ 
		this.domainName = domainName; 
	}
	public String getDomainName(){ 
		return domainName; 
	}
	
	public void setThreadCount(int threadCount){ 
		this.threadCount = threadCount; 
	}
	public int getThreadCount(){ 
		return threadCount; 
	}
	
	public void setDomainType(String domainType){ 
		this.domainType = domainType; 
	}
	public String getDomainType(){ 
		return domainType; 
	}
	
	public void setSocketTimeOut(int socketTimeOut){ 
		this.socketTimeOut = socketTimeOut; 
	}
	public int getSocketTimeOut(){ 
		return socketTimeOut; 
	}
	
	public void setSocketPerSendCount(int socketPerSendCount){ 
		this.socketPerSendCount = socketPerSendCount;
	}
	public int getSocketPerSendCount(){
		return socketPerSendCount; 
	}
	
	public void setDomainTypeDesc(String domainTypeDesc){
		this.domainTypeDesc = domainTypeDesc;
	}
	
	public String getDomainTypeDesc(){
		return domainTypeDesc;
	}
}
