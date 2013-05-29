package web.massmail.statistic.model;

import java.io.Serializable;

/**
 * <p>tm_massmail_link 
 * @author 김유근
 *
 */
@SuppressWarnings("serial")
public class MassMailLink  implements Serializable{
	
	private int linkID = 0;
	private int massmailID = 0;
	private int scheduleID = 0;
	private String linkName = "";	
	private String linkURL = "";
	private String linkDesc = "";			//링크설명 
	private String linkType = "1";			//1:일반링크, 2:수신거부 
	private String useYN = "Y";				//사용여부 
	private int linkCount = 0;				//링크카운트 
	private String registDate="";
	
	private String imgLinkURL = "Y";
	
	public void setLinkID(int linkID){
		this.linkID = linkID;
	}
	public int getLinkID(){
		return linkID;
	}
	
	public void setMassmailID(int massmailID){
		this.massmailID = massmailID;
	}
	public int getMassmailID(){
		return massmailID;
	}
	
	public void setLinkName(String linkName){
		this.linkName = linkName;
	}
	public String getLinkName(){
		return linkName;
	}
	
	public void setLinkURL(String linkURL){
		this.linkURL = linkURL;
	}
	public String getLinkURL(){
		return linkURL;
	}
	
	public void setLinkDesc(String linkDesc){
		this.linkDesc = linkDesc;
	}
	public String getLinkDesc(){
		return linkDesc;
	}
	
	public void setLinkType(String linkType){
		this.linkType = linkType;
	}
	public String getLinkType(){
		return linkType;
	}
	
	public void setLinkCount(int linkCount){
		this.linkCount = linkCount;
	}
	public int getLinkCount(){
		return linkCount;
	}
	
	public void setUseYN(String useYN){
		this.useYN = useYN;
	}
	public String getUseYN(){
		return useYN;
	}
	
	public void setRegistDate(String registDate){
		this.registDate = registDate;
	}
	public String getRegistDate(){
		return registDate;
	}
	public int getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}
	public String getImgLinkURL() {
		return imgLinkURL;
	}
	public void setImgLinkURL(String imgLinkURL) {
		this.imgLinkURL = imgLinkURL;
	}
	
}
