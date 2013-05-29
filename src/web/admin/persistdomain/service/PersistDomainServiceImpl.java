package web.admin.persistdomain.service;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import web.admin.persistdomain.dao.PersistDomainDAO;
import web.admin.persistdomain.model.PersistDomain;



public class PersistDomainServiceImpl implements PersistDomainService{

	private Logger logger = Logger.getLogger("TM");
	private PersistDomainDAO persistDomainDAO = null;
	
	public void setPersistDomainDAO(PersistDomainDAO persistDomainDAO){
		this.persistDomainDAO = persistDomainDAO;
	}
	
	
	public List<PersistDomain> list(int currentPage, int countPerPage,Map<String, String> searchMap){
		List<PersistDomain> result = null;
		try{
			result = persistDomainDAO.list(currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public int getCount(Map<String, String> searchMap){
		int result = 0;
		try{
			result = persistDomainDAO.getCount(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	public PersistDomain view(int domainID){
		PersistDomain persistDomain = null;
		try{			
			persistDomain = persistDomainDAO.view(domainID);			
		}catch(Exception e){
			logger.error(e);
		}
		return persistDomain;
	}
	
	public int insert(PersistDomain persistDomain){
		int result = 0;
		try{
			if(selectDomain(persistDomain)==0)
				result = persistDomainDAO.insert(persistDomain);
			else
				result = -1;
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public int update(PersistDomain persistDomain){
		int result = 0;
		try{
			if(selectDomain(persistDomain)==0)
				result = persistDomainDAO.update(persistDomain);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	public int delete(String[] domainIDs){
		int result = 0;
		try{
			for(int i=0;i<domainIDs.length;i++){
				result = persistDomainDAO.delete(Integer.parseInt(domainIDs[i]));
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	private int selectDomain(PersistDomain persistDomain){
		int domain = 0;
		try{			
			domain = persistDomainDAO.selectDomain(persistDomain);			
		}catch(Exception e){
			logger.error(e);
		}
		return domain;
	}
	
}
