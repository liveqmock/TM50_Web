package web.admin.domainset.service;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import web.admin.domainset.dao.DomainSetDAO;
import web.admin.domainset.model.DomainSet;

public class DomainSetServiceImpl implements DomainSetService {

	private DomainSetDAO domainSetDAO = null;
	private Logger logger = Logger.getLogger("TM");

    public void setDomainSetDAO(DomainSetDAO domainSetDAO) {    	
        this.domainSetDAO = domainSetDAO;        
    }
    
    
	public int deleteDomainSet(String[] domainSetIDs) throws DataAccessException {
		
		int result = 0; 		
		try{
			for(int i=0;i<domainSetIDs.length;i++){
				result = domainSetDAO.deleteDomainSet(Integer.parseInt(domainSetIDs[i]));
			}
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
	


	public int insertDomainSet(DomainSet domainSet) throws DataAccessException {
		
		int result = -1; 
		
		try{ 
			if(selectDomain(domainSet)==0)
				result = domainSetDAO.insertDomainSet(domainSet);  
			
		}catch(Exception e){ 
			logger.error(e); 
		}
		
		return result;
	}

		
	public List<DomainSet> listDomainSet(String domainFlag) throws DataAccessException {
		
		List<DomainSet> list = null;
		try{ 			
			list = domainSetDAO.listDomainSet(domainFlag); 			
		}catch(Exception e){ 
			logger.error(e); 
		}
		return list;
	}

	public int updateDomainSet(DomainSet domainSet) throws DataAccessException {
		
		int result = 0; 
		
		try{ 
			if(selectDomain(domainSet)==0)
				result = domainSetDAO.updateDomainSet(domainSet);  
		}catch(Exception e){ 
			logger.error(e); 
		}
		
		return result;
	}
	
	
	public DomainSet viewDomain(int domainID){
		DomainSet domain = null;
		try{			
			domain = domainSetDAO.viewDomainSet(domainID);			
		}catch(Exception e){
			logger.error(e);
		}
		return domain;
	}
	
	private int selectDomain(DomainSet domainSet){
		int domain = 0;
		try{			
			domain = domainSetDAO.selectDomain(domainSet);			
		}catch(Exception e){
			logger.error(e);
		}
		return domain;
	}
	
	public DomainSet viewDomainName(String domainName){
		DomainSet domain = null;
		try{
			domain = domainSetDAO.viewDomainNameSet(domainName);
		}catch(Exception e){
			logger.error(e);
		}
		return domain;
	}

}
