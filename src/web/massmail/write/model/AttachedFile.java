package web.massmail.write.model;

import java.io.Serializable;


/**
 * <p>ez_massmail_file
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class AttachedFile implements Serializable {

	private String fileKey;
	private String userID;
	private String fileName;	
	private String filePath;
	private String fileSize;
	private String fileSizeByte;
	private String registDate;
	private String sendedDate;
	
	
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
	/**
	 * @return the sendedDate
	 */
	public String getSendedDate() {
		return sendedDate;
	}
	/**
	 * @param sendedDate the sendedDate to set
	 */
	public void setSendedDate(String sendedDate) {
		this.sendedDate = sendedDate;
	}
	
	/**
	 * @return the fileSizeByte
	 */
	public String getFileSizeByte() {
		return fileSizeByte;
	}
	/**
	 * @param fileSizeByte the fileSizeByte to set
	 */
	public void setFileSizeByte(String fileSizeByte) {
		this.fileSizeByte = fileSizeByte;
	}
}
