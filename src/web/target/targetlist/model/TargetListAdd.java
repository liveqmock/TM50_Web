package web.target.targetlist.model;

public class TargetListAdd {

	private int targetID;
	private int targetAddID;
	private String addType; 
	private String addTypeInput;
	private String realFileName;		//실제 보여지는 파일명
	private String newFileName;		//디렉토리에 저장된 파일명
	private String uploadKey="";	
	private String directText;
	private String registDate;
	
	public int getTargetID() {
		return targetID;
	}
	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}
	public int getTargetAddID() {
		return targetAddID;
	}
	public void setTargetAddID(int targetAddID) {
		this.targetAddID = targetAddID;
	}
	public String getAddType() {
		return addType;
	}
	public void setAddType(String addType) {
		this.addType = addType;
	}
	public String getAddTypeInput() {
		return addTypeInput;
	}
	public void setAddTypeInput(String addTypeInput) {
		this.addTypeInput = addTypeInput;
	}
	public String getRealFileName() {
		return realFileName;
	}
	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}
	public String getNewFileName() {
		return newFileName;
	}
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	public String getUploadKey() {
		return uploadKey;
	}
	public void setUploadKey(String uploadKey) {
		this.uploadKey = uploadKey;
	}
	public String getDirectText() {
		return directText;
	}
	public void setDirectText(String directText) {
		this.directText = directText;
	}
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	
}
