package web.admin.rejectmail.service;

import java.util.List;
import java.util.Map;
import web.admin.rejectmail.model.RejectMail;

public interface RejectMailService {

	/**
	 * <p>수신거부자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<RejectMail> listRejectMail(int currentPage, int countPerPage ,Map<String, String> searchMap);
	
	/**
	 * <p>전체카운트 
	 * @param searchMap
	 * @return
	 */
	public int totalCountRejectMail(Map<String, String> searchMap);
	
	/**
	 * <p>수신거부자 삭제 
	 * @param maps
	 * @return
	 */
	public int[] deleteRejectMail(Map<String, Object>[] maps);
	
	
	/**
	 * <p>수신거부자 직접입력
	 * @param rejectMail
	 * @return
    */
	public int insertRejectMail(RejectMail rejectMail);
	
	
	/**
	 * <p>ez_massmail_reject에 인서트 한다. 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int[] insertCSVImport(String sql, List paramList);
}
