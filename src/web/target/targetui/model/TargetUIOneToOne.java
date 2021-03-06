package web.target.targetui.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TargetUIOneToOne implements Serializable{
	private int targetUIManagerID;
	private int selectID;
	private String selectFieldName;
	private int onetooneID;
	private String selectDescription;
	private int csvColumnPos;
	
	
	
	
	/**
	 * @return the targetUIManagerID
	 */
	public int getTargetUIManagerID() {
		return targetUIManagerID;
	}
	/**
	 * @param targetUIManagerID the targetUIManagerID to set
	 */
	public void setTargetUIManagerID(int targetUIManagerID) {
		this.targetUIManagerID = targetUIManagerID;
	}
	/**
	 * @return the selectID
	 */
	public int getSelectID() {
		return selectID;
	}
	/**
	 * @param selectID the selectID to set
	 */
	public void setSelectID(int selectID) {
		this.selectID = selectID;
	}
	/**
	 * @return the selectFieldName
	 */
	public String getSelectFieldName() {
		return selectFieldName;
	}
	/**
	 * @param selectFieldName the selectFieldName to set
	 */
	public void setSelectFieldName(String selectFieldName) {
		this.selectFieldName = selectFieldName;
	}
	/**
	 * @return the onetooneID
	 */
	public int getOnetooneID() {
		return onetooneID;
	}
	/**
	 * @param onetooneID the onetooneID to set
	 */
	public void setOnetooneID(int onetooneID) {
		this.onetooneID = onetooneID;
	}
	/**
	 * @return the selectDescription
	 */
	public String getSelectDescription() {
		return selectDescription;
	}
	/**
	 * @param selectDescription the selectDescription to set
	 */
	public void setSelectDescription(String selectDescription) {
		this.selectDescription = selectDescription;
	}
	/**
	 * @return the csvColumnPos
	 */
	public int getCsvColumnPos() {
		return csvColumnPos;
	}
	/**
	 * @param csvColumnPos the csvColumnPos to set
	 */
	public void setCsvColumnPos(int csvColumnPos) {
		this.csvColumnPos = csvColumnPos;
	}
	
	
	
	
	
	

}
