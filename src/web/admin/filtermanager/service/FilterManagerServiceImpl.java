package web.admin.filtermanager.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import web.admin.dbjdbcset.model.DbJdbcSet;
import web.admin.filtermanager.dao.FilterManagerDAO;
import web.admin.filtermanager.model.FilterManager;



public class FilterManagerServiceImpl implements FilterManagerService {
	
	private Logger logger = Logger.getLogger("TM");
	private FilterManagerDAO filterManagerDAO = null;
		
	public void setFilterManagerDAO(FilterManagerDAO filterManagerDAO){
		this.filterManagerDAO = filterManagerDAO;
	}
	
	public int getListTotalCount(Map<String, Object> searchMap){
		int result = -1; 
		try{
			result = filterManagerDAO.getListTotalCount(searchMap);
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List listFilterManager(Map<String, Object> searchMap){
		List resultList = null;
		try{
			resultList =  filterManagerDAO.listFilterManager(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	public FilterManager viewFilterManager(String filterID){
		FilterManager filterManager = null;
		try{
			filterManager = filterManagerDAO.viewFilterManager(filterID);
		}catch(Exception e){
			logger.error(e);
		}
		
		return filterManager;
	}
	
	public int insert(FilterManager filterManager){
		int result = 0;
		try{
			result = filterManagerDAO.insert(filterManager);
						
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public int update(FilterManager filterManager){
		int result = 0;
		try{
			result = filterManagerDAO.update(filterManager);
						
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public int delete(String filterID){
		int result = 0;
		try{			
			result = filterManagerDAO.delete(filterID);	
			
		}catch(Exception e){
			logger.error(e);
		}
		return result;		
	}

}
