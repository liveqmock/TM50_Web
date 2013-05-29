package web.admin.persistdomain.model;

import java.io.Serializable;


/**
 * <p>보내는 사람 (tm_sender)
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class PersistDomain implements Serializable {
	
	private int domainID;
	private String domain_name;				//보내는 사람명 
	private String description;					//보내는 이메일
	private String registDate;			//보내는 사람 핸드폰 
	private String userID;					//설명
	private String useYN;
	
	public int getDomainID() {
		return domainID;
	}
	public void setDomainID(int domainID) {
		this.domainID = domainID;
	}
	
	public String getDomain_name() {
		return domain_name;
	}
	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getUseYN() {
		return useYN;
	}
	public void setUseYN(String useYN) {
		this.useYN = useYN;
	}
	
	
	
	
}
