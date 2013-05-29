package web.admin.dbconnect.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.dbconnect.model.*;

public interface DbConnectService {
	
	
	/**
	 * <p>연동디비리스트 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectInfo> listDBConnectInfo(Map<String, Object> searchMap);

	
	/**
	 * <p>연동디비리스트 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountDBConnectInfo(Map<String, Object> searchMap);
	
	/**
	 * <p>연동디비리스트 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectInfo> listDBConnectHistoryInfo(Map<String, Object> searchMap);
	
	
	/**
	 * <p>연동디비리스트 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountDBConnectHistoryInfo(Map<String, Object> searchMap);
	
	
	/**
	 * <p>고객연동디비 컬럼정보 입력 
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int insertConnectDB(DbConnectInfo dbConnectInfo,Map<String, Object>[] maps);
	
	
	
	/**
	 * <p>고객연동디비 기본정보 보기 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public DbConnectInfo viewConnectDBInfo(String dbID);
	
	
	
	/**
	 * <p>고액연동디비 컬럼리스트 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectColumn> listConnectDBColumn(String dbID);
	
	
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID);
	
	
	/**
	 * <p>이미 등록된 dbID가 있는지 확인한다.
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkDBInfo(String dbID);
	
	/**
	 * <p>고객연동디비 컬럼정보 수정
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int updateConnectDB(DbConnectInfo dbConnectInfo, Map<String, Object>[] maps);
	
	
	public List<DbConnectInfo> listDBConnectInfo();

}
