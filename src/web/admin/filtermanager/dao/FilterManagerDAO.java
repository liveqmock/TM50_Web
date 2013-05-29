package web.admin.filtermanager.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.filtermanager.model.FilterManager;

public interface FilterManagerDAO {
	
	public int getListTotalCount(Map<String, Object> searchMap) throws DataAccessException;
	
	public List<FilterManager> listFilterManager(Map<String, Object> searchMap) throws DataAccessException;
	
	public FilterManager viewFilterManager(String filterID)  throws DataAccessException;
	
	public int insert(FilterManager  filterManager) throws DataAccessException;
	
	public int update(FilterManager  filterManager) throws DataAccessException;
	
	public int delete(String filterID) throws DataAccessException;
	

}
