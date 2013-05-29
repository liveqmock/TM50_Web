package web.admin.dbconnect.service;

import java.util.List;
import java.util.Map;

import web.admin.dbconnect.model.*;
import web.admin.dbconnect.dao.*;


import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;


public class DbConnectServiceImpl implements DbConnectService{

	
	private Logger logger = Logger.getLogger("TM");
	private DbConnectDAO dbConnectDAO = null;
	
	
	public void setDbConnectDAO(DbConnectDAO dbConnectDAO){
		this.dbConnectDAO = dbConnectDAO;
	}
	
	
	/**
	 * <p>연동디비리스트 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectInfo> listDBConnectInfo(Map<String, Object> searchMap){
		 List<DbConnectInfo> result = null;
		 try{
			result =  dbConnectDAO.listDBConnectInfo(searchMap);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * <p>연동디비리스트 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountDBConnectInfo(Map<String, Object> searchMap){
		int result = 0;
		try{
			result = dbConnectDAO.totalCountDBConnectInfo(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>연동디비리스트 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectInfo> listDBConnectHistoryInfo(Map<String, Object> searchMap){
		 List<DbConnectInfo> result = null;
		 try{
			result =  dbConnectDAO.listDBConnectHistoryInfo(searchMap);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * <p>연동디비리스트 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountDBConnectHistoryInfo(Map<String, Object> searchMap){
		int result = 0;
		try{
			result = dbConnectDAO.totalCountDBConnectHistoryInfo(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>고객연동디비 컬럼정보 입력 
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int insertConnectDB(DbConnectInfo dbConnectInfo, Map<String, Object>[] maps){
		//try catch를 처리하면 일괄 롤백이 안되므로 안함..
		int result1 = 0; 
		int[] result2 = null;
		int result =1;
		try{
			result1 = dbConnectDAO.insertConnectDBInfo(dbConnectInfo);
			if(result1<=0) result = -1;
			result2 = dbConnectDAO.insertConnectDBColumn(maps);
			if(result2==null) result = -2;
		}catch(Exception e){
			logger.error(e);
		}

		return result;
	}
	
	/**
	 * <p>고객연동디비 컬럼정보 수정
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int updateConnectDB(DbConnectInfo dbConnectInfo, Map<String, Object>[] maps){
		//try catch를 처리하면 일괄 롤백이 안되므로 안함..
		int result1 = 0; 
		int result2 = 0;
		int[] result3 = null;
		int result =1;
		try{
			result1 = dbConnectDAO.updateConnectDBInfo(dbConnectInfo);
			if(result1<=0) result = -1;
			result2 = dbConnectDAO.deleteConnectDBColumn(dbConnectInfo.getDbID());
			if(result2<=0) result = -2;
			result3 = dbConnectDAO.insertConnectDBColumn(maps);
			if(result3==null) result = -3;
			
			dbConnectDAO.deleteHistoryNextDBConnect(dbConnectInfo.getDbID());
			dbConnectDAO.updateHistoryNextDBConnect(dbConnectInfo.getDbID(), dbConnectInfo.getQueryText(),  dbConnectInfo.getTableName(), dbConnectInfo.getUpdateScheduleDate());
			//System.out.println("^^");
		}catch(Exception e){
			logger.error(e);
		}

		return result;
	}
	
	
	
	/**
	 * <p>고객연동디비 기본정보 보기 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public DbConnectInfo viewConnectDBInfo(String dbID){
		DbConnectInfo dbConnectInfo = null;
		try{
			dbConnectInfo = dbConnectDAO.viewConnectDBInfo(dbID);
		}catch(Exception e){
			logger.error(e);
		}
		return dbConnectInfo;
	}
	
	
	
	/**
	 * <p>고액연동디비 컬럼리스트 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectColumn> listConnectDBColumn(String dbID){
		List<DbConnectColumn> result = null;
		try{
			result = dbConnectDAO.listConnectDBColumn(dbID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID){
		Map<String, Object> result = null;
		try{
			result = dbConnectDAO.getDBInfo(dbID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>이미 등록된 dbID가 있는지 확인한다.
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkDBInfo(String dbID){
		int result = 0;
		try{
			result = dbConnectDAO.checkDBInfo(dbID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public List<DbConnectInfo> listDBConnectInfo(){
		List<DbConnectInfo> result = null;
		try{
			result = dbConnectDAO.listDBConnectInfo();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
