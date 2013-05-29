package web.response.dao;

import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface ResponseDAO {

	
	/**
	 * <p>메일을 최초 오픈하였을 때 tm_massmail_openresult 테이블에 openYN='Y' 로 인서트 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int insertOpenMassMailSendResult(int sendID,int massmailID, int scheduleID) throws DataAccessException;
	
	/**
	 * <p>메일을 최초 오픈하였을 때 tm_massmail_openresult 테이블에 openYN='Y' 로 업데이트(이미 같은 sendID가 있을 때) 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateOpenMassMailSendResult(int sendID, int massmailID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>메일본문에 최초 링크를 하나라도 클릭하였을 때 tm_massmail_openresult 테이블에 clickYN='Y' 로 인서트 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int insertClickMassMailSendResult(int sendID, int massmailID,int scheduleID) throws DataAccessException;
	
	/**
	 * <p>메일본문에 최초 링크를 하나라도 클릭하였을 때 tm_massmail_openresult 테이블에 clickYN='Y' 로 업데이트(이미 같은 sendID가 있을 때)
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int updateClickMassMailSendResult(int sendID, int massmailID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>메일본문에 최초 수신거부를 클릭하였을 때 tm_massmail_openresult 테이블에 rejectcall='Y' 로 인서트 
	 * @param massmailID
	 * @param sendID
	 * @param scheduleID
	 * @return
	 */
	public int insertRejectMassMailSendResult(int sendID, int massmailID,int scheduleID) throws DataAccessException;
	
	/**
	 * <p>메일본문에 최초 수신거부를 클릭하였을 때 tm_massmail_openresult 테이블에 rejectcall='Y'로 업데이트(이미 같은 sendID가 있을 때)
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateRejectMassMailSendResult(int sendID,int massmailID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>메일본문에 링크한 정보 인서트 
	 * @param massmailID
	 * @param linkID
	 * @param sendID
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public int insertLinkClick(int massmailID, int scheduleID, int linkID, int sendID, String email, int targetID) throws DataAccessException;
	
	
	/**
	 * <p>수신거부에 등록되어 있는지 확인 
	 * @param email
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkReject(String email, int massmailID)  throws DataAccessException;
	
	
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
	public int insertRejectClick(int massmailID, String email, int targetID, int massmailGroupID, String userID, String groupID, String customerID) throws DataAccessException;
	
	
	/**
	 * <p>자동 메일을 오픈하였을 때 ez_automail_sendresult에 openYN='Y' 로 최초한번 업데이트 
	 * @param automailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenAutoMailSendResult(String sendID,int automailID, String yearMonth) throws DataAccessException;
	
	
	
	
	/**
	 * <p>통계수집을 요청하기 위한 업데이트 tm_massmail_schedule.updateStatisticYN='N' 으로 해야 통계가 다시 수집된다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public int updateScheduleStatistic(int massmailID, int scheduleID) throws DataAccessException;
	
	
	
	
	/**
	 * <p>sendID에 해당하는 userID, gorupID, customerID를 리턴한다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getSendIDInfo(int massmailID, int scheduleID, int sendID)  throws DataAccessException;
	
	
	/**
	 * <p>테이블명이 존재하는지 체크한다. 
	 * @param tableName
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Map<String, Object>> isExistTable(String tableName) throws DataAccessException;
	
	
	
	/**
	 * <p>메일을 오픈하였을 때 tm_intermail_sendresult_YYYYMM에 openYN='Y' 로 최초한번 업데이트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenInterMailSendResult(String sendID,int intermailID, int scheduleID, String yearMonth) throws DataAccessException;
	
	
	
	/**
	 * <p>첨부파일을 오픈하였을 때 tm_intermail_sendresult_YYYYMM에 openFileYN='Y', openFileDate 로 최초한번 업데이트 
	 * @param massmailID
	 * @param sendID
	 * @param email
	 * @return
	 */
	public int updateOpenFileInterMailSendResult(String sendID,int intermailID, int scheduleID, String yearMonth) throws DataAccessException;
	
	
	/**
	 * tm_massmail_openresult 테이블에 데이터가 있는지 확인한다.
	 * 
	 */	
	public int isExistData(int sendID, int massmailID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * tm_massmail_reject 테이블에 데이터가 있는지 확인한다.
	 * 
	 */
	public int isExistDataReject(String email, int massmailGroupID) throws DataAccessException;
	
	
	

}
