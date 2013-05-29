package web.admin.rejectmail.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.admin.rejectmail.model.*;

public interface RejectMailDAO {

	
	/**
	 * <p>수신거부자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<RejectMail> listRejectMail(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>전체카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountRejectMail(Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>수신거부자 삭제 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] deleteRejectMail(Map<String, Object>[] maps) throws DataAccessException;
	
	
	/**
	 * <p>수신거부자 직접입력
	 * @param rejectMail
	 * @return
	 * @throws DataAccessException
	 */
	public int insertRejectMail(RejectMail rejectMail) throws DataAccessException;
	
	
	/**
	 * <p>ez_massmail_reject에 인서트 한다. 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")	
	public int[] insertCSVImport(String sql, List paramList) throws DataAccessException;
	
	public int getRejectMail(RejectMail rejectMail) throws DataAccessException;
	
}
