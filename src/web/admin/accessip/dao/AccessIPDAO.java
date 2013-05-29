package web.admin.accessip.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.admin.accessip.model.*;

public interface AccessIPDAO {
	
	/**
	 * <p>접근IP관리 리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 * @throws DataAccessException
	 */
	public List<AccessIP> listAccessIP(int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException;
		

	/**
	 * <p>접근IP관리 리스트의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */
	public int getAccessIPTotalCount(Map<String, String> searchMap)  throws DataAccessException;
	

	/**
	 * <p>접근IP관리 설정 정보
	 * @param accessipID
	 * @return
	 * @throws DataAccessException
	 */
	public AccessIP viewAccessIP(int accessipID)throws DataAccessException;
	
	/**
	 * <p>접근IP관리 설정 추가한다.
	 * @param accessIP
	 * @return
	 */
	public int insertAccessIP(AccessIP accessIP)throws DataAccessException;
	
	/**
	 * <p접근IP관리 설정 수정한다.
	 * @param accessIP
	 * @return
	 */
	public int updateAccessIP(AccessIP accessIP)throws DataAccessException;
	

	/**
	 * <p> 접근IP관리 설정을 삭제한다. 
	 * @param accessipID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAccessIP(int accessipID) throws DataAccessException;
}
