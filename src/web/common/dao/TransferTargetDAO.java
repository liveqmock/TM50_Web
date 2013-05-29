package web.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.common.model.OnetooneTarget;
import web.common.model.TargetList;

public interface TransferTargetDAO {
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID)  throws DataAccessException;
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetList(TargetList targetList) throws DataAccessException;
	
	/**
	 * <p>target_list에서 max의 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID() throws DataAccessException;
	
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(OnetooneTarget onetooneTarget) throws DataAccessException;
	
	/**
	 * <p>해당월에 대상자파일 테이블이 있는지 확인 
	 * @param yyyymm
	 * @return
	 */
	public List<Map<String, Object>> getFileImportTableIsExist(String tableName);
	
	/**
	 * <p>파일임포트 테이블 생성 
	 * @param tableName
	 * @return
	 * @throws DataAccessException
	 */
	public int createFileImportTable(String tableName) throws DataAccessException;
	
	/**
	 * <p>ez_fileimport 인서트 한다. 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")	
	public int[] insertFileImport(String sql, final List paramList) throws DataAccessException;
	
	/**
	 * <p>대상자완료 상태 변경 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingEnd(String state, int targetCount, String query, String tableName, int targetID, String count_query) throws DataAccessException;
}
