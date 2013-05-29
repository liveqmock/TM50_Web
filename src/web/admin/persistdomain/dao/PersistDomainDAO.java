package web.admin.persistdomain.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.admin.persistdomain.model.PersistDomain;

public interface PersistDomainDAO {
	
	
	public List<PersistDomain> list(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
		
	
	
	public int getCount(Map<String, String> searchMap) throws DataAccessException;
	
	
	
	public int insert(PersistDomain persistDomain)  throws DataAccessException;
	
	
	public int update(PersistDomain persistDomain)  throws DataAccessException;
	
	
	

	public int delete(int domainID) throws DataAccessException;
	
	
	
	
	public PersistDomain view(int persistDomain)  throws DataAccessException;
	
	public int selectDomain(PersistDomain persistDomain) throws DataAccessException;
	

}
