package web.api.automail.service;

import org.apache.log4j.Logger;

import web.api.automail.dao.AutoMailAPIDAO;
import web.api.automail.model.AutoMailAPI;

public class AutoMailAPIServiceImpl implements AutoMailAPIService {
	
	
	private Logger logger = Logger.getLogger("TM");	
	private AutoMailAPIDAO autoMailAPIDAO = null;
	
	public void setAutoMailAPIDAO(AutoMailAPIDAO autoMailAPIDAO){	
		this.autoMailAPIDAO = autoMailAPIDAO;
	}
	
	public boolean insertAutoMail_queue(AutoMailAPI autoMailAPI)
	{
		int result = 0;
		
		try{
			result = autoMailAPIDAO.insertAutoMail_queue(autoMailAPI);
		}catch(Exception e){
			logger.error(e);
		}
		if(result > 0)
			return true;
		else 
			return false;
	}

}
