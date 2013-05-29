package web.response.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import web.response.dao.*;



public class ResponseServiceImpl implements ResponseService{
	
	private ResponseDAO responseDAO = null; 
	private Logger logger = Logger.getLogger("TM");
	
	public void setResponseDAO(ResponseDAO responseDAO){
		this.responseDAO = responseDAO;
	}
	
	/**
	 * <p>메일을 최초 오픈하였을 때 tm_massmail_openresult 테이블에 openYN='Y' 로 set
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateOpenMassMailSendResult(int sendID,int massmailID, int scheduleID){
		int result = 0;
		try{
			
			if(responseDAO.isExistData(sendID, massmailID, scheduleID)==0)
				result = responseDAO.insertOpenMassMailSendResult(sendID, massmailID, scheduleID);
			else
				result = responseDAO.updateOpenMassMailSendResult(sendID, massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>메일본문에 링크를 하나라도 최초 클릭하였을 때 tm_massmail_openresult 테이블에 clickYN='Y' 로 set 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateClickMassMailSendResult(int sendID,int massmailID, int scheduleID){
		int result = 0;
		try{
			if(responseDAO.isExistData(sendID, massmailID, scheduleID)==0)
				result = responseDAO.insertClickMassMailSendResult(sendID, massmailID, scheduleID);
			else 
				result = responseDAO.updateClickMassMailSendResult(sendID, massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>메일본문에 최초 수신거부를 클릭하였을 때 tm_massmail_openresult 테이블에 rejectcall='Y' 로 set 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateRejectMassMailSendResult(int sendID,int massmailID, int scheduleID){
		int result = 0;
		try{
			if(responseDAO.isExistData(sendID, massmailID, scheduleID)==0)
				result = responseDAO.insertRejectMassMailSendResult(sendID, massmailID, scheduleID);
				
			else
				result = responseDAO.updateRejectMassMailSendResult(sendID, massmailID, scheduleID);
			
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>메일본문에 링크한 정보 인서트 
	 * @param massmailID
	 * @param linkID
	 * @param sendID
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public int insertLinkClick(int massmailID, int scheduleID, int linkID, int sendID, String email, int targetID){
		int result = 0;
		try{
			result = responseDAO.insertLinkClick(massmailID, scheduleID, linkID, sendID, email, targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>수신거부에 등록되어 있는지 확인 
	 * @param email
	 * @param massmailID
	 * @return
	 */
	public int checkReject(String email, int massmailID){
		int result = 0;
		try{
			result = responseDAO.checkReject(email, massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>메일본문에 수신거부클릭 인서트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @param targetID
	 * @param massmailGroupID
	 * @param customerID
	 * @return
	 */
	public int insertRejectClick(int massmailID, String email, int targetID, int massmailGroupID, String userID, String groupID, String customerID){
		int result = 0;
		try{
			if(responseDAO.isExistDataReject(email, massmailGroupID)==0)
				result = responseDAO.insertRejectClick(massmailID, email, targetID, massmailGroupID, userID, groupID, customerID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>자동 메일을 오픈하였을 때 ez_automail_sendresult에 openYN='Y' 로 최초한번 업데이트 
	 * @param automailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenAutoMailSendResult(String sendID,int automailID, String yearMonth){
		int result = 0;
		try{
			result = responseDAO.updateOpenAutoMailSendResult(sendID, automailID, yearMonth);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>통계수집을 요청하기 위한 업데이트 tm_massmail_schedule.updateStatisticYN='N' 으로 해야 통계가 다시 수집된다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public int updateScheduleStatistic(int massmailID, int scheduleID){
		int result = 0;
		try{
			result = responseDAO.updateScheduleStatistic(massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>sendID에 해당하는 userID, gorupID, customerID를 리턴한다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public Map<String, Object> getSendIDInfo(int massmailID, int scheduleID, int sendID){
		Map<String,Object> result = null;
		try{
			result = responseDAO.getSendIDInfo(massmailID, scheduleID, sendID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>테이블명이 존재하는지 체크한다. 
	 * @param tableName
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Map<String, Object>> isExistTable(String tableName){
		List<Map<String,Object>> result = null;
		try{
			result = responseDAO.isExistTable(tableName);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>메일을 오픈하였을 때 tm_intermail_sendresult_YYYYMM에 openYN='Y' 로 최초한번 업데이트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenInterMailSendResult(String sendID,int intermailID, int scheduleID, String yearMonth){
		int result = 0;
		try{
			result = responseDAO.updateOpenInterMailSendResult(sendID, intermailID, scheduleID, yearMonth);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>첨부파일을 오픈하였을 때 tm_intermail_sendresult_YYYYMM에 openFileYN='Y', openFileDate 로 최초한번 업데이트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenFileInterMailSendResult(String sendID,int intermailID, int scheduleID, String yearMonth){
		int result = 0;
		try{
			result = responseDAO.updateOpenFileInterMailSendResult(sendID, intermailID, scheduleID, yearMonth);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

}
