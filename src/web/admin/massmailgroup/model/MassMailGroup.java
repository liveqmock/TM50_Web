package web.admin.massmailgroup.model;

import java.io.Serializable;


/**
 * <p>대량메일그룹 테이블 tm_massmail_group
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class MassMailGroup implements Serializable{

	private int massMailGroupID;
	private String massMailGroupName;
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
	 * @return the massMailGroupID
	 */
	public int getMassMailGroupID() {
		return massMailGroupID;
	}
	/**
	 * @param massMailGroupID the massMailGroupID to set
	 */
	public void setMassMailGroupID(int massMailGroupID) {
		this.massMailGroupID = massMailGroupID;
	}
	/**
	 * @return the massMailGroupName
	 */
	public String getMassMailGroupName() {
		return massMailGroupName;
	}
	/**
	 * @param massMailGroupName the massMailGroupName to set
	 */
	public void setMassMailGroupName(String massMailGroupName) {
		this.massMailGroupName = massMailGroupName;
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
}
