package web.admin.persistdomain.service;

import java.util.List;
import java.util.Map;

import web.admin.persistdomain.model.PersistDomain;

public interface PersistDomainService {
	
	public List<PersistDomain> list(int currentPage, int countPerPage,Map<String, String> searchMap);
	
	public int getCount(Map<String, String> searchMap);
	
	public PersistDomain view(int domainID);
	
	public int insert(PersistDomain persistDomain);
	
	public int update(PersistDomain persistDomain);
	
	public int delete(String[] domainIDs);

	
	
}
