
package web.common.model;

import java.io.Serializable;

/**
 * <p>파일 업로드 클래스 
 * @author coolang(김유근)
 */

@SuppressWarnings("serial")
public class FileUpload implements Serializable{
	
	private String uploadKey;				
	private String realFileName;			
	private String newFileName;			
	private String fileContent;


	
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
	
	public void setFileContent(String fileContent){
		this.fileContent = fileContent;
	}
	public String getFileContent(){
		return fileContent;
	}
}
