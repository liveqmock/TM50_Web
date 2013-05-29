package web.admin.persistfail.service;

import java.util.List;
import java.util.Map;
import web.admin.persistfail.model.*;


public interface PersistFailService {

	/**
	 * <p>영구적인 메일리스트 
	 * @param searchMap
	 * @return
	 */
	public List<PersistFail> listPersistFailMail(int currentPage, int countPerPage,Map<String, Object> searchMap);
	
	
	/**
	 * <p>전체카운트 
	 * @param searchMap
	 * @return
	 */
	public int totalCountPersistFailMail(Map<String, Object> searchMap);
		
	
	/**
	 * <p>선택한 리스트 삭제 
	 * @param persistFailMails
	 * @return
	 */
	public int[] deletePersistFailMail(Map<String, Object>[] maps);
}
