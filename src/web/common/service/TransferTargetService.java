package web.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.common.model.OnetooneTarget;
import web.common.model.TargetList;

public interface TransferTargetService {
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID) ;
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 */
	public int insertTargetList(TargetList targetList) throws DataAccessException;

	/**
	 * <p>target_list에서 최근 targetID를 가져온다. 
	 * @return
	 */
	public int getMaxTargetID();
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(List<OnetooneTarget> onetooneTargetList);
	
	/**
	 * <p>파일임포트 테이블이 있다면 테이블명 리턴 
	 * @param tableName
	 * @return
	 */
	public List<Map<String, Object>> getFileImportTableIsExist(String tableName);
	
	/**
	 * <p>파일임포트 테이블 생성 
	 * @param tableName
	 * @return
	 */
	public int createFileImportTable(String tableName);
	
	/**
	 * <p>ez_fileimport에 인서트 한다. 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")	
	public int[] insertFileImport(String sql, List paramList);
	
	/**
	 * <p>대상자 등록시 종료 처리 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param state
	 */
	public int updateTargetingEnd(String state, int targetCount, String query, String tableName, int targetID, String count_query);
}
