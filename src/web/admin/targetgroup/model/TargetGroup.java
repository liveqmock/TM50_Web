package web.admin.targetgroup.model;

import java.io.Serializable;


/**
 * <p>대상자분류 테이블 tm_target_group
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class TargetGroup implements Serializable{

	private int targetGroupID;
	private String targetGroupName;
	private String description;
	private String useYN;
	private String registDate;
	private String isDefault = "N";
	
	
	
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the useYN
	 */
	public String getUseYN() {
		return useYN;
	}
	/**
	 * @param useYN the useYN to set
	 */
	public void setUseYN(String useYN) {
		this.useYN = useYN;
	}
	/**
	 * @return the registDate
	 */
	public String getRegistDate() {
		return registDate;
	}
	/**
	 * @param registDate the registDate to set
	 */
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	/**
	 * @return the targetGroupID
	 */
	public int getTargetGroupID() {
		return targetGroupID;
	}
	/**
	 * @param targetGroupID the targetGroupID to set
	 */
	public void setTargetGroupID(int targetGroupID) {
		this.targetGroupID = targetGroupID;
	}
	/**
	 * @return the targetGroupName
	 */
	public String getTargetGroupName() {
		return targetGroupName;
	}
	/**
	 * @param targetGroupName the targetGroupName to set
	 */
	public void setTargetGroupName(String targetGroupName) {
		this.targetGroupName = targetGroupName;
	}
}
