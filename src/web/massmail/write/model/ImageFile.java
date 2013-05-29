package web.massmail.write.model;

import java.io.Serializable;


/**
 * <p>ez_massmail_file
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class ImageFile implements Serializable {

	private String fileKey;
	private String userID;
	private String fileName;	
	private String filePath;
	private String fileSize;
	private String registDate;
	
	
	public void setFileKey(String fileKey){
		this.fileKey = fileKey;
	}
	public String getFileKey(){
		return fileKey;
	}	
	
	public void setUserID(String userID){
		this.userID = userID;
	}
	public String getUserID(){
		return userID;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	public String getFileName(){
		return fileName;
	}
	
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	public String getFilePath(){
		return filePath;
	}
	
	public void setFileSize(String fileSize){
		this.fileSize = fileSize;
	}
	public String getFileSize(){
		return fileSize;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
}
