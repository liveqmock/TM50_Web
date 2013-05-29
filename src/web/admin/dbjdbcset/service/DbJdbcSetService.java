package web.admin.dbjdbcset.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.dbjdbcset.model.*;

public interface DbJdbcSetService {
	
	/**
	 * <p>ez_jdbcset리스트를 출력한다.
	 * @return
	 * @throws DataAccessException
	 */	
	public List<DbJdbcSet> listJdbcSet();
	
	
	/**
	 * <p>tm_jdbcset를 보여준다. 
	 * @return
	 * @throws DataAccessException
	 */
	public DbJdbcSet viewJdbcSet(String driverID);
		 
	
	/**
	 * <p>tm_dbset에 인서트한다.
	 * @param dbJdbcSet
	 * @return
	 */
	public int insertDbSet(DbJdbcSet dbJdbcSet);
	
	
	/**
	 * <p>tm_dbset에서 삭제처리 
	 * @param dbID
	 * @return
	 */
	public int deleteDbSet(String dbID);
	
	
	/**
	 * <p>tm_dbset에 업데이트 한다. 
	 * @param dbJdbcSet
	 * @return
	 */
	public int updateDbSet(DbJdbcSet dbJdbcSet);
	
	
	/**
	 * <p>tm_dbset에서 dbAccessKey 값을 가져온다. 
	 * @param dbJdbcSet
	 * @return
	 */
	public String getDbAccessKey(String dbID);
	
	
	/**
	 * <p>tm_dbset, tm_jdbcset 조인 출력 
	 * @return
	 */
	public List<DbJdbcSet> listDbJdbcSet(Map<String, Object> searchMap);
	
	/**
	 * <p>다수의 dbID별 DB리스트의 총카운트를 구해온다.
	 * @param searchMap
	 * @return 
	 * @throws DataAccessException
	 */
	public int getListDbJdbcSetTotalCount(Map<String, Object> searchMap);
	
	/**
	 * <p>tm_dbset의 dbID의 max값을 가져온다. 
	 * @return
	 */
	public int getMaxdbID();

	/**
	 * <p>tm_dbset을 보여준다. 
	 * @param dbID
	 * @return
	 */
	public DbJdbcSet viewDbSet(String dbID);  
	
	
	/**
	 * <p>다수의 dbID별 DB리스트 출력 
	 * @param dbID
	 * @return
	 */
	public List<DbJdbcSet> listUserDbList(String[] dbID);
	
	
	
	/**
	 * <p>사용중인 타게팅인 dbID가 있는지 확인 
	 * @param dbID
	 * @return
	 */
	public int checkUseDBbydbID(String dbID);
	
	
	
	/**
	 * <p>어드민일 경우 등록된 모든 db리스트가 나온다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbJdbcSet> listDBList(String dbID);
	
	
}
