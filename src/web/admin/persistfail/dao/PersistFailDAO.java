package web.admin.persistfail.dao;


import java.util.*;

import org.springframework.dao.DataAccessException;
import web.admin.persistfail.model.*; 

public interface PersistFailDAO {
	
	
	/**
	 * <p>영구적인 메일리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PersistFail> listPersistFailMail(int currentPage, int countPerPage,Map<String, Object> searchMap)  throws DataAccessException;
		
	
	/**
	 * <p>전체카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountPersistFailMail(Map<String, Object> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>선택한 리스트 삭제 
	 * @param persistFailMails
	 * @return
	 * @throws DataAccessException
	 */
	public int[] deletePersistFailMail(Map<String, Object>[] maps) throws DataAccessException;

}
