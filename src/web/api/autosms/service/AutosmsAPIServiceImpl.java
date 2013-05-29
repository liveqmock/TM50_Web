package web.api.autosms.service;

import org.apache.log4j.Logger;

import web.api.autosms.dao.AutosmsAPIDAO;
import web.api.autosms.model.AutosmsAPI;

public class AutosmsAPIServiceImpl implements AutosmsAPIService {
	
	private Logger logger = Logger.getLogger("TM");	
	private AutosmsAPIDAO autosmsAPIDAO = null;
	
	public void setAutosmsAPIDAO(AutosmsAPIDAO autosmsAPIDAO){	
		this.autosmsAPIDAO = autosmsAPIDAO;
	}
	
	public boolean insertAutoSms_queue(AutosmsAPI autoSmsAPI)
	{ 
		int result = 0;
		
		try{
			result = autosmsAPIDAO.insertAutoSms_queue(autoSmsAPI);
		}catch(Exception e){
			logger.error(e);
		}
		if(result > 0)
			return true;
		else 
			return false;
	}

}
