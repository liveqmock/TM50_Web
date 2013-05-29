package web.admin.rejectsms.service;

import java.util.List;
import java.util.Map;
import web.admin.rejectsms.model.RejectSMS;

public interface RejectSMSService {

	/**
	 * <p>수신거부자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<RejectSMS> listRejectSMS(int currentPage, int countPerPage ,Map<String, String> searchMap);
	
	/**
	 * <p>전체카운트 
	 * @param searchMap
	 * @return
	 */
	public int totalCountRejectSMS(Map<String, String> searchMap);
	
	/**
	 * <p>수신거부자 삭제 
	 * @param maps
	 * @return
	 */
	public int[] deleteRejectSMS(Map<String, Object>[] maps);
	
	
	/**
	 * <p>수신거부자 직접입력
	 * @param rejectMail
	 * @return
    */
	public int insertRejectSMS(RejectSMS rejectSMS);
	
	
	/**
	 * <p>ez_massmail_reject에 인서트 한다. 
	 * @param sql
	 * @return
	 */	
	public int[] insertCSVImport(String sql, List paramList);
}
