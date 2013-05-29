package web.response.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;






public interface ResponseService {
	
	/**
	 * <p>메일을 최초 오픈하였을 때 tm_massmail_openresult 테이블에 openYN='Y' 로 set
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateOpenMassMailSendResult(int sendID,int massmailID, int scheduleID);
	
	
	/**
	 * <p>메일본문에 링크를 하나라도 최초 클릭하였을 때 tm_massmail_openresult 테이블에 clickYN='Y' 로 set 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateClickMassMailSendResult(int sendID,int massmailID, int scheduleID);
	
	
	/**
	 * <p>메일본문에 최초 수신거부를 클릭하였을 때 tm_massmail_openresult 테이블에 rejectcall='Y' 로 set 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateRejectMassMailSendResult(int sendID,int massmailID, int scheduleID);
	
	
	/**
	 * <p>메일본문에 링크한 정보 인서트 
	 * @param massmailID
	 * @param linkID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int insertLinkClick(int massmailID, int scheduleID, int linkID, int sendID, String email, int targetID);
	
	
	/**
	 * <p>수신거부에 등록되어 있는지 확인 
	 * @param email
	 * @param massmailID
	 * @return
	 */
	public int checkReject(String email, int massmailID);
	
	
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
	public int insertRejectClick(int massmailID, String email, int targetID, int massmailGroupID, String userID, String groupID, String customerID);
	
	
	/**
	 * <p>자동 메일을 오픈하였을 때 ez_automail_sendresult에 openYN='Y' 로 최초한번 업데이트 
	 * @param automailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenAutoMailSendResult(String sendID,int automailID, String yearMonth);
	
	
	/**
	 * <p>통계수집을 요청하기 위한 업데이트 tm_massmail_schedule.updateStatisticYN='N' 으로 해야 통계가 다시 수집된다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public int updateScheduleStatistic(int massmailID, int scheduleID);
	
	
	/**
	 * <p>sendID에 해당하는 userID, gorupID, customerID를 리턴한다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public Map<String, Object> getSendIDInfo(int massmailID, int scheduleID, int sendID);
	
	
	/**
	 * <p>테이블명이 존재하는지 체크한다. 
	 * @param tableName
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Map<String, Object>> isExistTable(String tableName);
	
	
	
	/**
	 * <p>메일을 오픈하였을 때 tm_intermail_sendresult_YYYYMM에 openYN='Y' 로 최초한번 업데이트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenInterMailSendResult(String sendID,int intermailID, int scheduleID, String yearMonth);
	
	
	
	/**
	 * <p>첨부파일을 오픈하였을 때 tm_intermail_sendresult_YYYYMM에 openFileYN='Y', openFileDate 로 최초한번 업데이트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenFileInterMailSendResult(String sendID,int intermailID, int scheduleID, String yearMonth);

}
