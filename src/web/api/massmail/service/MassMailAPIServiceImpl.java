package web.api.massmail.service;

import java.util.Map;

import org.apache.log4j.Logger;

import web.api.massmail.dao.MassMailAPIDAO;



public class MassMailAPIServiceImpl implements MassMailAPIService {
	
	private MassMailAPIDAO massMailAPIDAO = null;
	private Logger logger = Logger.getLogger(this.getClass());

	
	
    public void setMassMailAPIDAO(MassMailAPIDAO massMailAPIDAO) {
    	
        this.massMailAPIDAO = massMailAPIDAO;         
    }
    
    public int getOnetooneID(String onetooneAlias){
		int result = 0;
		try{
			result = massMailAPIDAO.getOnetooneID(onetooneAlias);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
    
    public int getTargetState(int targetID){
		int result = 0;
		try{
			result = massMailAPIDAO.getTargetState(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
    
    public String getTemplateContent(int template_id)
    {
    	String result = null;
    	Map<String,Object> resultMap = null;
		try{
			resultMap = massMailAPIDAO.getTemplateContent(template_id);
			if(!resultMap.get("templateContent").equals("")){
				result =(String)resultMap.get("templateContent");
				
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
    	
    }
    

}
