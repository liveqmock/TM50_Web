package web.admin.targetmanager.service;


import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import web.admin.dbjdbcset.model.DbJdbcSet;
import web.admin.targetmanager.dao.TargetManagerDAO;
import web.admin.targetmanager.model.OneToOne;
import web.admin.targetmanager.model.TargetUIManager;
import web.admin.targetmanager.model.TargetUIManagerSelect;
import web.admin.targetmanager.model.TargetUIManagerWhere;


public class TargetManagerServiceImpl implements TargetManagerService {
	private TargetManagerDAO targetManagerDAO = null;
	private Logger logger = Logger.getLogger("TM");
	
	public void setTargetManagerDAO(TargetManagerDAO targetManagerDAO)
	{
		this.targetManagerDAO = targetManagerDAO;
	}
	
	@SuppressWarnings("unchecked")
	public List list(Map<String, Object> searchMap){
		List resultList = null;
		
		try{
			resultList =  targetManagerDAO.list(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	public int getMaxID()
	{
		int result = -1; 
		try{
			result = targetManagerDAO.getMaxID();
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
		
	}
	
	public int getCount(Map<String, Object> searchMap)
	{
		int result = -1; 
		try{
			result = targetManagerDAO.getCount(searchMap);
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
		
	}
	
	public int insertTargetManagerInfo(TargetUIManager targetManger)
	{
		int result = 0;
		try{
			result = targetManagerDAO.insertTargetManagerInfo(targetManger);
			if(targetManger.getDefaultYN().equals("Y"))
				targetManagerDAO.updateTargetManagerDefault(targetManger.getTargetUIManagerID());
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public int updateTargetManagerInfo(TargetUIManager targetManger)
	{
		int result = 0;
		try{
			result = targetManagerDAO.updateTargetManagerInfo(targetManger);
			if(targetManger.getDefaultYN().equals("Y"))
				targetManagerDAO.updateTargetManagerDefault(targetManger.getTargetUIManagerID());
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	public TargetUIManager view(int targetUIManagerID){
		TargetUIManager targetManager = null;
		try{
			targetManager = targetManagerDAO.view(targetUIManagerID);
		}catch(Exception e){
			logger.error(e);
		}
		
		return targetManager;
	}
	
	
	
	public Map<String, Object> getDBInfo(String dbID) {
		 Map<String, Object> result = null;
		
		 try{
			 result = targetManagerDAO.getDBInfo(dbID);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	
	/**
	 * <p>원투원정보를 불러온다.
	 * @return
	 */
	public List<OneToOne> listOneToOne(){
		List<OneToOne> result = null;
		try{
			result = targetManagerDAO.listOneToOne();  
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public List<TargetUIManagerSelect> getTargetUIManagerSelect(int targetUIManagerID) 
	{
		List<TargetUIManagerSelect> result = null;
		try{
			result = targetManagerDAO.getTargetUIManagerSelect(targetUIManagerID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public List<TargetUIManagerWhere> getTargetUIManagerWhere(int targetUIManagerID)
	{
		List<TargetUIManagerWhere> result = null;
		try{
			result = targetManagerDAO.getTargetUIManagerWhere(targetUIManagerID); 
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
		
	}
	
	/**
	 * <p>db 리스트를 불러온다.
	 * @return
	 */
	public List<DbJdbcSet> getDBList(){
		 List<DbJdbcSet> result = null;
		 try{
			 result = targetManagerDAO.getDBList();
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	public void insertTargetManagerSelect(List<TargetUIManagerSelect> targetUIManagerSelectList)
	{
		try{
			for(int i=0;i<targetUIManagerSelectList.size();i++){
				targetManagerDAO.insertTargetManagerSelect(targetUIManagerSelectList.get(i));
			}
		}catch(Exception e){
			logger.error(e);
		}
		
	}
	
	public void insertTargetManagerWhere(List<TargetUIManagerWhere> targetUIManagerWhereList)
	{
		try{
			for(int i=0;i<targetUIManagerWhereList.size();i++){
				targetManagerDAO.insertTargetManagerWhere(targetUIManagerWhereList.get(i));
			}
		}catch(Exception e){
			logger.error(e);
		}
		
	}
	
	public int deleteTargetManagerInfo(int targetUIManagerID) 
	{
		int result = 0; 		
		try
		{			
			result = targetManagerDAO.deleteTargetManagerInfo(targetUIManagerID);
			
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
	
	public int deleteTargetManagerSelect(int targetUIManagerID) 
	{
		int result = 0; 		
		try
		{			
			result = targetManagerDAO.deleteTargetManagerSelect(targetUIManagerID);
			
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
	
	public int deleteTargetManagerWhere(int targetUIManagerID) 
	{
		int result = 0; 		
		try
		{			
			result = targetManagerDAO.deleteTargetManagerWhere(targetUIManagerID);
			
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
	
	public int createGeneralTable(int targetUIManagerID){
		int result = 0;	
		try{
			result =  targetManagerDAO.createGeneralTable(targetUIManagerID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	public int updateTargetUseNo(int targetUIManagerID) {
		int result = 0; 		
		try
		{			
			result = targetManagerDAO.updateTargetUseNo(targetUIManagerID);
			
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
	
	
	
	
	
	

}
