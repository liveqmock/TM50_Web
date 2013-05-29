package web.admin.rejectsms.service;

import java.util.List;
import java.util.Map;
import web.admin.rejectsms.model.RejectSMS;
import web.admin.rejectsms.dao.*;
import org.apache.log4j.Logger;


public class RejectSMSServiceImpl implements RejectSMSService{

	private Logger logger = Logger.getLogger("TM");
	private RejectSMSDAO rejectSMSDAO = null;
	
	public void setRejectSMSDAO(RejectSMSDAO rejectSMSDAO){
		this.rejectSMSDAO = rejectSMSDAO;
	}
	
	
	/**
	 * <p>수신거부자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<RejectSMS> listRejectSMS(int currentPage, int countPerPage,Map<String, String> searchMap){
		
		List<RejectSMS> result = null;
		try{
			result = rejectSMSDAO.listRejectSMS(currentPage, countPerPage, searchMap);
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
	public int totalCountRejectSMS(Map<String, String> searchMap){
		int result = 0;
		try{
			result = rejectSMSDAO.totalCountRejectSMS(searchMap);
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
	public int[] deleteRejectSMS(Map<String, Object>[] maps){
		int[] result = null;
		try{
			result = rejectSMSDAO.deleteRejectSMS(maps);
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
	public int insertRejectSMS(RejectSMS rejectSMS){
		int result = 0;
		try{
			if(rejectSMSDAO.getRejectSMS(rejectSMS) == 0)
				result = rejectSMSDAO.insertRejectSMS(rejectSMS);
							
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
			result = rejectSMSDAO.insertCSVImport(sql, paramList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
