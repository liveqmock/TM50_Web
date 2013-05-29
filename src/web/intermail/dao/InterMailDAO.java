package web.intermail.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import web.common.model.FileUpload;
import web.intermail.model.*;


public interface InterMailDAO {

	/**
	 * <p>연동메일 갯수 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountInterMailEvent(Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>연동메일리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<InterMailEvent> listInterMailEvents(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>연동메일이벤트보기 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailEvent viewInterMailEvent(int intermailID) throws DataAccessException;
	
	
	
	/**
	 * <p>연동발송설정보기
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<InterMailSchedule> selectInterMailSchedule(int intermailID) throws DataAccessException;
	
	
	/**
	 * <p>업로드키로 파일정보 불러오기 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public FileUpload getFileInfo(String uploadKey) throws DataAccessException;
	
	
	
	/**
	 * <p>연동메일 정보입력
	 * @param intermailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertInterMailEvent(InterMailEvent interMailEvent) throws DataAccessException;
	
	
	
	/**
	 * <p>스케줄입력 
	 * @param interMailSchedule
	 * @return
	 * @throws DataAccessException
	 */
	public int insertInterMailSchedule(InterMailSchedule interMailSchedule) throws DataAccessException;
	
	
	
	/**
	 * <p>가장 최근에 입력된 intermailID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxInterMailID() throws DataAccessException;	
	
	
	/**
	 * <p>tm_intermail_schedule에 intermailID에 해당되는 레코드 삭제 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteInterMailSchedule(int intermailID) throws DataAccessException;	
	
	
	/**
	 * <p>파일 인서트 
	 * @param fileKey
	 * @param fileName
	 * @return
	 * @throws DataAccessException
	 */
	public int insertFile(FileUpload fileUpload) throws DataAccessException;
	
	
	/**
	 * <p>연동메일 정보입력
	 * @param intermailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailEvent(InterMailEvent interMailEvent) throws DataAccessException;
	
	
	
	/**
	 * <p>연동메일 스케줄 업데이트 
	 * @param interMailSchedule
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailSchedule(InterMailSchedule interMailSchedule) throws DataAccessException;


	/**
	 * <p>발송 상태 변경
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int intermailID, int scheduleID, String state) throws DataAccessException;
	
	
	
	/**
	 * <p>연동상태값을 가져온다.
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> getInterMailState(int intermailID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>tm_intermail_send_ 테이블 생성 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public int createSendQueueTable(String tableName) throws DataAccessException;
	
	
	/**
	 * <p>연동메일 - 시간별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticHourly(Map<String, Object> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>연동메일 기간중 일자별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MailStatistic> statisticDaily(Map<String, Object> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>연동메일 기간중 월별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MailStatistic> statisticMonthly(Map<String, Object> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>연동메일 기간중 도메인별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MailStatistic> statisticDomain(Map<String, Object> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>연동메일 기간중 실패원인별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<FailCauseStatistic> statisticFailCauseList(Map<String, Object> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>연동메일 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MailStatistic> porsonPreview(Map<String, Object> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>연동메일 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>발송대기자 전체 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalInterMailSendList(Map<String, Object> paramMap) throws DataAccessException;
	
	
	/**
	 * <p>발송대기자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<InterMailSend> selectInterMailSendList(Map<String, Object> paramMap)  throws DataAccessException;
	
	
	
	/**
	 * <p>발송대기자 상세보기 
	 * @param intermailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailSend viewInterMailSend(int intermailID, int scheduleID, String sendID) throws DataAccessException;
	
	
	
	
	/**
	 * <p>체크된 발송대기자 삭제
	 * @param intermailID
	 * @param scheduleID
	 * @param sendIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSelectedInterMailSend(int intermailID, int scheduleID, String[] sendIDs)  throws DataAccessException;
	
	
	
	/**
	 * <p>모든  발송대기자 삭제
	 * @param intermailID
	 * @param scheduleID
	 * @param sendIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAllInterMailSend(int intermailID, int scheduleID)  throws DataAccessException;


	
	/**
	 * <p>발송승인 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailSendApprove(int intermailID, int scheduleID, String state) throws DataAccessException;
	
	
	
	
	/**
	 * <p>발송결과 히스토리 내역 
	 * @param paramsMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<InterMailSendResult> selectInterMailHistory(Map<String,Object> paramsMap)  throws DataAccessException;
	
	
	
	/**
	 * <p>발송결과 히스토리 총 카운트  
	 * @param paramsMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totaltInterMailHistory(Map<String,Object> paramsMap)  throws DataAccessException;
	
	
	
	/**
	 * <p>발송결과자 상세보기 
	 * @param intermailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailSendResult viewInterMailSendResult(String yearMonth, int intermailID, int scheduleID, String sendID) throws DataAccessException;
	
	
	
	
	/**
	 * <p>오류자 재발송 상태값 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailScheduleRetryState(int intermailID, int scheduleID, String retryFinishYN)  throws DataAccessException;
	
	
	/**
	 * <p>발송결과 오류자 상태값 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailResultRetryState(String sendID, int intermailID, int scheduleID, String wasRetrySended)  throws DataAccessException;
	
	
	
	/**
	 * <p>재발송을 위한 재발송완료 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRetryFinishSet(String retryFinishYN, int intermailID, int scheduleID) throws DataAccessException;
		
	
	/**
	 * <p>재발송을 위해 전체 실패자 업데이트 
	 * @param wasRetrySended
	 * @param intermailID
	 * @param scheduleID
	 * @param resultYearMonth
	 * @return
	 * @throws DataAccessException
	 */
	public int updateWasRetrySendedAllFailed(String wasRetrySended, int intermailID, int scheduleID, String resultYearMonth)  throws DataAccessException;
	
	
	
	/**
	 * <p>재발송을 위해 체크된  실패자 업데이트 (배치)
	 * @param wasRetrySended
	 * @param intermailID
	 * @param scheduleID
	 * @param resultYearMonth
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updateWasRetrySendedCheckedFailedBatch(String resultYearMonth, List<InterMailSendResult> interMailSendResultList)  throws DataAccessException;

	
	
	/**
	 * <p>재발송이 완료되었는 지 확인 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> checkRetryFinishYN(int intermailID, int scheduleID) throws DataAccessException;
}
