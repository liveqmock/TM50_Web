package web.admin.dbjdbcset.dao;


import java.util.List;
import java.util.Map;

import web.admin.dbjdbcset.model.*;

import org.springframework.dao.DataAccessException;

public interface DbJdbcSetDAO {
	
	/**
	 * <p>tm_jdbcset리스트를 출력한다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbJdbcSet> listJdbcSet() throws DataAccessException;	
	
	/**
	 * <p>tm_jdbcset 출력 
	 * @param driverID
	 * @return
	 * @throws DataAccessException
	 */
	public DbJdbcSet viewJdbcSet(String driverID) throws DataAccessException;
	

	/**
	 * <p>tm_dbset에 인서트한다. 
	 * @param dbJdbcSet
	 * @return
	 * @throws DataAccessException
	 */
	public int insertDbSet(DbJdbcSet dbJdbcSet) throws DataAccessException;

	
	/**
	 * <p>ez_dbset를 삭제한다. 
	 * @param dbID
	 * @return
	 */
	public int deleteDbSet(String dbID) throws DataAccessException;
	
	
	/**
	 * <p>tm_dbset에 업데이트 
	 * @param dbJdbcSet
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDbSet(DbJdbcSet dbJdbcSet) throws  DataAccessException;
	
	
	/**
	 * <p>tm_dbset, tm_jdbcset의 조인리스트를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbJdbcSet> listDbJdbcSet(Map<String, Object> searchMap) throws DataAccessException;	
	
	
	/**
	 * <p>tm_dbset, tm_jdbcset의 조인리스트의 총 Count를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getListDbJdbcSetTotalCount(Map<String, Object> searchMap) throws DataAccessException;	
	
	
	/**
	 * <p>tm_dbset에서 db_id의 max값을 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxdbID() throws DataAccessException;	
	
	
	/**
	 * <p>tm_dbset 에서 dbAccessKey를 불러온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public String getDbAccessKey(String dbID)  throws DataAccessException;
	
	
	/**
	 * <p>tm_dbset을 불러온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public DbJdbcSet viewDbSet(String dbID)  throws DataAccessException;	
	
	
	/**
	 * <p>사용자별 DB리스트 출력 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbJdbcSet>  listUserDbList(String[] dbID)  throws DataAccessException;	
	
	
	/**
	 * <p>사용중인 타게팅인 dbID가 있는지 확인 
	 * @param dbID
	 * @return
	 */
	public int checkUseDBbydbID(String dbID)  throws DataAccessException;
	
	
	
	/**
	 * <p>어드민일 경우 등록된 모든 db리스트가 나온다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbJdbcSet> listDBList(String dbID) throws DataAccessException;
	
	public int updateDefault(DbJdbcSet dbJdbcSet) throws  DataAccessException;
	
	public int insertDbSetAdmin(DbJdbcSet dbJdbcSet) throws DataAccessException;
	
	public int insertDbSetAdminGroup(DbJdbcSet dbJdbcSet) throws DataAccessException;
	
	public int deleteDbSetUserAuth(String dbID) throws DataAccessException;

	
}
