package web.admin.dbconnect.dao;


import org.springframework.dao.DataAccessException;
import web.admin.dbconnect.model.*;

import java.util.*;

public interface DbConnectDAO {
	
	
	/**
	 * <p>연동디비리스트 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectInfo> listDBConnectInfo(Map<String, Object> searchMap) throws DataAccessException;	
	
	
	/**
	 * <p>연동디비리스트 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountDBConnectInfo(Map<String, Object> searchMap) throws DataAccessException;	

	
	/**
	 * <p>연동디비리스트 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectInfo> listDBConnectHistoryInfo(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>연동디비리스트 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountDBConnectHistoryInfo(Map<String, Object> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>고객연동디비 기본정보 입력 
	 * @param dbConnectInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertConnectDBInfo(DbConnectInfo dbConnectInfo) throws DataAccessException;	
	
	
	/**
	 * <p>고객연동디비 컬럼정보 입력 
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertConnectDBColumn(Map<String, Object>[] maps) throws DataAccessException;	
	
	
	
	/**
	 * <p>고객연동디비 기본정보 보기 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public DbConnectInfo viewConnectDBInfo(String dbID) throws DataAccessException;	
	
	
	
	/**
	 * <p>고액연동디비 컬럼리스트 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectColumn> listConnectDBColumn(String dbID) throws DataAccessException;	
	
	
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID)  throws DataAccessException;
	
	
	
	/**
	 * <p>이미 등록된 dbID가 있는지 확인한다.
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkDBInfo(String dbID) throws DataAccessException;
	
	/**
	 * <p>고객연동디비 기본정보 수정 
	 * @param dbConnectInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updateConnectDBInfo(DbConnectInfo dbConnectInfo) throws DataAccessException;
	

	/**
	 * <p>고객연동디비 컬럼정보 수정
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updateConnectDBColumn(Map<String, Object>[] maps) throws DataAccessException;
	
	/**
	 * <p>고객연동디비 컬럼정보 삭제
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteConnectDBColumn(String dbID) throws DataAccessException;
	
	
	public List<DbConnectInfo> listDBConnectInfo() throws DataAccessException;
	
	/**
	 * <p> ez_connectdb_history의 기존 다음 수집 정보 Delete
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteHistoryNextDBConnect(String dbID) throws DataAccessException;
	

	/**
	 * <p> ez_connectdb_history의 다음 수집 정보 Insert
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateHistoryNextDBConnect(String dbID, String queryText, String tableName, String registDate) throws DataAccessException;
	
	
}


