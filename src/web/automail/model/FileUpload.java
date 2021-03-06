package web.automail.model;
import java.io.Serializable;


@SuppressWarnings("serial")
public class FileUpload implements Serializable{
	
	private String uploadKey;				
	private String realFileName;			
	private String newFileName;			

	
	public void setUploadKey(String uploadKey){
		this.uploadKey = uploadKey;
	}
	
	public String getUploadKey(){
		return uploadKey;
	}
	
	
	public void setRealFileName(String realFileName){
		this.realFileName = realFileName;
	}
	
	public String getRealFileName(){		
		return realFileName;
	}
	
	public void setNewFileName(String newFileName){
		this.newFileName = newFileName;
	}
	
	public String getNewFileName(){		
		return newFileName;
	}
}
