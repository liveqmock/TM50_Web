package web.admin.systemset.model;

import java.io.Serializable;

/**
 * <p>ez_config 테이블 
 * @author 임영호 
 *
 */
@SuppressWarnings("serial")
public class SystemSet implements Serializable{
	
	private int configID = 0;
	private String configFlag = "";
	private String name = ""; 
	private String configName =""; 
	private String configValue ="";
	private String configDesc = ""; 
	
	public void setConfigID(int configID){ 
		this.configID = configID;
	}
	
	public int getConfigID(){ 
		return configID; 
	}
	
	public void setConfigFlag(String configFlag){ 
		this.configFlag = configFlag;
	}
	
	public String getConfigFlag(){ 
		return configFlag; 
	}
	
	public String getName(){ 
		return name; 
	}
	public void setName(String name){ 
		this.name = name; 
	}
	
	public String getConfigName(){ 
		return configName; 
	}
	public void setConfigName(String configName){ 
		this.configName = configName; 
	}
	
	
	public String getConfigValue(){ 
		return configValue;
	}
	public void setConfigValue(String configValue){ 
		this.configValue = configValue;
	}
	
	public String getConfigDesc(){ 
		return configDesc; 
	}
	
	public void setConfigDesc(String configDesc){ 
		this.configDesc = configDesc; 
	}
	
}
