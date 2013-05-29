package web.target.targetui.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TargetUIGeneralInfo implements Serializable{
	
	private int targetID;
	private int whereID;
	private String checkedItems;
	private String periodStartValue;
	private String periodEndValue;
	private String inputValue;
	private String whereType;
	
	
	/**
	 * @return the targetID
	 */
	public int getTargetID() {
		return targetID;
	}
	/**
	 * @param targetID the targetID to set
	 */
	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}
	/**
	 * @return the whereID
	 */
	public int getWhereID() {
		return whereID;
	}
	/**
	 * @param whereID the whereID to set
	 */
	public void setWhereID(int whereID) {
		this.whereID = whereID;
	}
	/**
	 * @return the checkedItems
	 */
	public String getCheckedItems() {
		return checkedItems;
	}
	/**
	 * @param checkedItems the checkedItems to set
	 */
	public void setCheckedItems(String checkedItems) {
		this.checkedItems = checkedItems;
	}
	/**
	 * @return the periodStartValue
	 */
	public String getPeriodStartValue() {
		return periodStartValue;
	}
	/**
	 * @param periodStartValue the periodStartValue to set
	 */
	public void setPeriodStartValue(String periodStartValue) {
		this.periodStartValue = periodStartValue;
	}
	/**
	 * @return the periodEndValue
	 */
	public String getPeriodEndValue() {
		return periodEndValue;
	}
	/**
	 * @param periodEndValue the periodEndValue to set
	 */
	public void setPeriodEndValue(String periodEndValue) {
		this.periodEndValue = periodEndValue;
	}
	/**
	 * @return the inputValue
	 */
	public String getInputValue() {
		return inputValue;
	}
	/**
	 * @param inputValue the inputValue to set
	 */
	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}
	/**
	 * @return the whereType
	 */
	public String getWhereType() {
		return whereType;
	}
	/**
	 * @param whereType the whereType to set
	 */
	public void setWhereType(String whereType) {
		this.whereType = whereType;
	}
	
	

}
