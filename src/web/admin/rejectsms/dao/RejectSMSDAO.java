package web.admin.rejectsms.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.admin.rejectsms.model.*;

public interface RejectSMSDAO {

	
	/**
	 * <p>수신거부자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<RejectSMS> listRejectSMS(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>전체카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountRejectSMS(Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>수신거부자 삭제 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] deleteRejectSMS(Map<String, Object>[] maps) throws DataAccessException;
	
	
	/**
	 * <p>수신거부자 직접입력
	 * @param rejectMail
	 * @return
	 * @throws DataAccessException
	 */
	public int insertRejectSMS(RejectSMS rejectSMS) throws DataAccessException;
	
	
	/**
	 * <p>ez_massmail_reject에 인서트 한다. 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */		
	public int[] insertCSVImport(String sql, List paramList) throws DataAccessException;
	
	
	
	/**
	 * 수신거부 sms를 가져온다.
	 * @param rejectSMS
	 * @return
	 * @throws DataAccessException
	 */
	public int getRejectSMS(RejectSMS rejectSMS) throws DataAccessException;
	
}
