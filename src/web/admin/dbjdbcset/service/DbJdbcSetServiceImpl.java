package web.admin.dbjdbcset.service;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import web.admin.dbjdbcset.dao.DbJdbcSetDAO;
import web.admin.dbjdbcset.model.*;


public class DbJdbcSetServiceImpl  implements DbJdbcSetService{

	private DbJdbcSetDAO dbJdbcSetDAO = null;
	private Logger logger = Logger.getLogger("TM");

	
    public void setDbJdbcSetDAO(DbJdbcSetDAO dbJdbcSetDAO) {    	
        this.dbJdbcSetDAO = dbJdbcSetDAO;        
    }	
    
    
    @SuppressWarnings("unchecked")
	public List listJdbcSet(){
		List resultList = null;
		try{
			resultList =  dbJdbcSetDAO.listJdbcSet();
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
    }
    
	public DbJdbcSet viewJdbcSet(String driverID){
    	DbJdbcSet dbJdbcSet = null;
		try{
			dbJdbcSet = dbJdbcSetDAO.viewJdbcSet(driverID);
		}catch(Exception e){
			logger.error(e);
		}
		return dbJdbcSet;
    }
	
	public int insertDbSet(DbJdbcSet dbJdbcSet){
		int result = 0;
		try{
			result = dbJdbcSetDAO.insertDbSet(dbJdbcSet);
			if(dbJdbcSet.getDefaultYN().equals("Y"))
			{
				dbJdbcSetDAO.updateDefault(dbJdbcSet);
			}
			if(result!=0)
			{
				dbJdbcSetDAO.insertDbSetAdmin(dbJdbcSet);
				dbJdbcSetDAO.insertDbSetAdminGroup(dbJdbcSet);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	public int deleteDbSet(String dbID){
		int result = 0;
		try{			
			result = dbJdbcSetDAO.deleteDbSet(dbID);	
			if(result!=0)
			{
				dbJdbcSetDAO.deleteDbSetUserAuth(dbID);				
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;		
	}
	
	
	public int updateDbSet(DbJdbcSet dbJdbcSet){
		int result = 0;
		try{
			result = dbJdbcSetDAO.updateDbSet(dbJdbcSet);
			if(dbJdbcSet.getDefaultYN().equals("Y"))
			{
				dbJdbcSetDAO.updateDefault(dbJdbcSet);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;		
	}
	
	
	public String getDbAccessKey(String dbID) {
		String result = "";
		try {
			result = dbJdbcSetDAO.getDbAccessKey(dbID);
		} catch(Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public List listDbJdbcSet(Map<String, Object> searchMap){
		List resultList = null;
		try{
			resultList =  dbJdbcSetDAO.listDbJdbcSet(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	public int getListDbJdbcSetTotalCount(Map<String, Object> searchMap){
		int result = -1; 
		try{
			result = dbJdbcSetDAO.getListDbJdbcSetTotalCount(searchMap);
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	/**
	 * <p>tm_dbset의 dbID의 max값을 가져온다. 
	 */
	public int getMaxdbID(){
		int maxdbID = 0;  
		try{
			maxdbID = dbJdbcSetDAO.getMaxdbID();		
		}catch(Exception e){
			logger.error(e);
		}	
		return maxdbID;
	}
	
	/*
	 * <p>tm_dbset를 보여준다. 
	 * (non-Javadoc)
	 * @see bixon.web.admin.dbjdbcset.service.DbJdbcSetService#viewDbSet(java.lang.String)
	 */
	public DbJdbcSet viewDbSet(String dbID){
		DbJdbcSet dbJdbcSet = null;
		try{
			dbJdbcSet = dbJdbcSetDAO.viewDbSet(dbID);
		}catch(Exception e){
			logger.error(e);
		}
		
		return dbJdbcSet;
	}
	
	
	/**
	 * <p>다수의 dbID별 DB리스트 출력 
	 * @param dbID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List listUserDbList(String[] dbID){
		List resultList = null;
		try{
			resultList =  dbJdbcSetDAO.listUserDbList(dbID);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}

	
	/**
	 * <p>사용중인 타게팅인 dbID가 있는지 확인 
	 * @param dbID
	 * @return
	 */
	public int checkUseDBbydbID(String dbID){
		int result = 0;  
		try{
			result = dbJdbcSetDAO.checkUseDBbydbID(dbID);
		}catch(Exception e){
			logger.error(e);
		}	
		return result;
	}
	
	
	/**
	 * <p>어드민일 경우 등록된 모든 db리스트가 나온다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbJdbcSet> listDBList(String dbID){
		List<DbJdbcSet> result = null;
		try{
			result = dbJdbcSetDAO.listDBList(dbID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
