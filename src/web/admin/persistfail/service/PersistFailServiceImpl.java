package web.admin.persistfail.service;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import web.admin.persistfail.model.PersistFail;
import web.admin.persistfail.dao.*;

public class PersistFailServiceImpl implements PersistFailService{
	
	private Logger logger = Logger.getLogger("TM");
	private PersistFailDAO persistFailDAO = null;
	
	public void setPersistFailDAO(PersistFailDAO persistFailDAO){
		this.persistFailDAO = persistFailDAO;
	}
	
	/**
	 * <p>영구적인 메일리스트 
	 * @param searchMap
	 * @return
	 */
	public List<PersistFail> listPersistFailMail(int currentPage, int countPerPage,Map<String, Object> searchMap){
		List<PersistFail> result = null;
		try{
			result = persistFailDAO.listPersistFailMail(currentPage,countPerPage,searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>전체카운트 
	 * @param searchMap
	 * @return
	 */
	public int totalCountPersistFailMail(Map<String, Object> searchMap){
		int result = 0;
		try{
			result = persistFailDAO.totalCountPersistFailMail(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
		

	
	/**
	 * <p>선택한 리스트 삭제 
	 * @param persistFailMails
	 * @return
	 */
	public int[] deletePersistFailMail(Map<String, Object>[] maps){
		int[] result = null;
		try{
			result = persistFailDAO.deletePersistFailMail(maps);
		}catch(Exception e){
			result = null;
			logger.error(e);
		}
		return result;
	}

}
