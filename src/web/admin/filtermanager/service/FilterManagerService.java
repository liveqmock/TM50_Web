package web.admin.filtermanager.service;

import java.util.List;
import java.util.Map;

import web.admin.filtermanager.model.FilterManager;

public interface FilterManagerService {
	
	public int getListTotalCount(Map<String, Object> searchMap);
	
	public List listFilterManager(Map<String, Object> searchMap);
	
	public FilterManager viewFilterManager(String filterID);
	
	public int insert(FilterManager filterManager);
	
	public int update(FilterManager filterManager);
	
	public int delete(String filterID);
	

}
