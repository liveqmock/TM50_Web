package web.admin.loginhistory.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import web.admin.loginhistory.model.LoginHistory;
import web.admin.loginhistory.dao.LoginHistoryDAO;

public class LoginHistoryServiceImpl implements LoginHistoryService {
	
	private LoginHistoryDAO loginHistoryDAO = null;
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	public void setLoginHistoryDAO(LoginHistoryDAO loginHistoryDAO){
		this.loginHistoryDAO = loginHistoryDAO;
	}
	
	/**
	 * <p>로그인 시도 기록 조회
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public  List<LoginHistory> listLoginHistory(int currentPage, int countPerPage, Map<String, String> searchMap){
		List<web.admin.loginhistory.model.LoginHistory>  result = null;
		
		try{
			result = loginHistoryDAO.listLoginHistory(currentPage, countPerPage, searchMap); 
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>로그인 시도 기록 조회 카운트
	 * @param searchMap
	 * @return
	 */
	public int getCountLoginHistory(Map<String, String> searchMap) {
		int result = 0;
		try{
			result = loginHistoryDAO.getCountLoginHistory(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
		
	}

}
