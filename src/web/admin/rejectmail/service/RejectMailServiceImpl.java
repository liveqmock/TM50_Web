package web.admin.rejectmail.service;

import java.util.List;
import java.util.Map;
import web.admin.rejectmail.model.RejectMail;
import web.admin.rejectmail.dao.*;
import org.apache.log4j.Logger;


public class RejectMailServiceImpl implements RejectMailService{

	private Logger logger = Logger.getLogger("TM");
	private RejectMailDAO rejectMailDAO = null;
	
	public void setRejectMailDAO(RejectMailDAO rejectMailDAO){
		this.rejectMailDAO = rejectMailDAO;
	}
	
	
	/**
	 * <p>수신거부자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<RejectMail> listRejectMail(int currentPage, int countPerPage,Map<String, String> searchMap){
		
		List<RejectMail> result = null;
		try{
			result = rejectMailDAO.listRejectMail(currentPage, countPerPage, searchMap);
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
	public int totalCountRejectMail(Map<String, String> searchMap){
		int result = 0;
		try{
			result = rejectMailDAO.totalCountRejectMail(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>수신거부자 삭제 
	 * @param maps
	 * @return
	 */
	public int[] deleteRejectMail(Map<String, Object>[] maps){
		int[] result = null;
		try{
			result = rejectMailDAO.deleteRejectMail(maps);
		}catch(Exception e){
			result = null;
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>수신거부자 직접입력
	 * @param rejectMail
	 * @return
    */
	public int insertRejectMail(RejectMail rejectMail){
		int result = 0;
		try{
			if(rejectMailDAO.getRejectMail(rejectMail) == 0)
				result = rejectMailDAO.insertRejectMail(rejectMail);
							
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>ez_massmail_reject에 인서트 한다. 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int[] insertCSVImport(String sql, List paramList){
		int[] result = null;
		try{
			result = rejectMailDAO.insertCSVImport(sql, paramList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
