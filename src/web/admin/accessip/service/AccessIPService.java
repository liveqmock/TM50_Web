package web.admin.accessip.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.accessip.model.AccessIP;

public interface AccessIPService {

	/**
	 * <p>접근IP관리 리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 * @throws DataAccessException
	 */
	public List<AccessIP> listAccessIP(int currentPage, int countPerPage, Map<String, String> searchMap);
	
	/**
	 * <p>접근IP관리 리스트의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */
	public int getAccessIPTotalCount(Map<String, String> searchMap);
	
	/**
	 * <p>접근IP관리 설정 정보
	 * @param accessipID
	 * @return
	 * @throws DataAccessException
	 */
	public AccessIP viewAccessIP(int accessipID);
	
	/**
	 * <p>접근IP관리 설정 추가한다.
	 * @param accessIP
	 * @return
	 */
	public int insertAccessIP(AccessIP accessIP);
	
	/**
	 * <p>접근IP관리 설정 수정한다.
	 * @param accessIP
	 * @return
	 */
	public int updateAccessIP(AccessIP accessIP);
	
	/**
	 * <p>접근IP관리 설정을 삭제한다.
	 * @param accessipIDs
	 * @return
	 */
	public int deleteAccessIP(String[] accessipIDs);
	
}
