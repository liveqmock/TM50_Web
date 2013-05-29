package web.admin.accessip.model;

import java.io.Serializable;


/**
 * <p>보내는 사람 (tm_sender)
 * @author coolang
 *
 */
@SuppressWarnings("serial")
public class AccessIP implements Serializable {
	
	private int accessipID;		//접근IP ID
	private String userID;		//등록자 User ID	
	private String userName;	//등록자 명	
	private String description; //설명
	private String octetA;		//첫번째 옥택		
	private String octetB;		//두번째 옥택 
	private String octetC;		//세번째 옥택
	private String octetD;		//네번째 옥택
	private String octetType;	//octet 제한 구분 
	private String useYN;		//사용 여부
	private String registDate;	//등록일
	
	public int getAccessipID() {
		return accessipID;
	}
	public void setAccessipID(int accessipID) {
		this.accessipID = accessipID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOctetA() {
		return octetA;
	}
	public void setOctetA(String octetA) {
		this.octetA = octetA;
	}
	public String getOctetB() {
		return octetB;
	}
	public void setOctetB(String octetB) {
		this.octetB = octetB;
	}
	public String getOctetC() {
		return octetC;
	}
	public void setOctetC(String octetC) {
		this.octetC = octetC;
	}
	public String getOctetD() {
		return octetD;
	}
	public void setOctetD(String octetD) {
		this.octetD = octetD;
	}
	public String getOctetType() {
		return octetType;
	}
	public void setOctetType(String octetType) {
		this.octetType = octetType;
	}
	public String getUseYN() {
		return useYN;
	}
	public void setUseYN(String useYN) {
		this.useYN = useYN;
	}
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
}
