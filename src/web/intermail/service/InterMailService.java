package web.intermail.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import web.common.model.FileUpload;
import web.intermail.model.*;


public interface InterMailService {
	
	/**
	 * <p>연동메일 갯수 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountInterMailEvent(Map<String, String> searchMap);
	
	
	/**
	 * <p>자동메일리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<InterMailEvent> listInterMailEvents(int currentPage, int countPerPage,Map<String, String> searchMap);
	
	
	
	/**
	 * <p>자동메일이벤트보기 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailEvent viewInterMailEvent(int intermailID);
	
	
	/**
	 * <p>연동발송설정보기
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<InterMailSchedule> selectInterMailSchedule(int intermailID);
	
	
	/**
	 * <p>업로드키로 파일정보 불러오기 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public FileUpload getFileInfo(String uploadKey);
	
	
	/**
	 * <p>연동메일 정보입력
	 * @param intermailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertInterMailEvent(InterMailEvent interMailEvent);
	
	
	
	/**
	 * <p>스케줄입력 
	 * @param interMailSchedule
	 * @return
	 * @throws DataAccessException
	 */
	public int insertInterMailSchedule(InterMailSchedule interMailSchedule);

	/**
	 * <p>가장 최근에 입력된 intermailID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxInterMailID();
	
	
	
	
	/**
	 * <p>파일 인서트 
	 * @param fileKey
	 * @param fileName
	 * @return
	 * @throws DataAccessException
	 */
	public int insertFile(FileUpload fileUpload);
	
	
	/**
	 * <p>연동메일 정보입력
	 * @param intermailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailEvent(InterMailEvent interMailEvent);
	
	
	
	/**
	 * <p>연동메일 스케줄 업데이트 
	 * @param interMailSchedule
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailSchedule(InterMailSchedule interMailSchedule);
	
	
	
	/**
	 * <p>연동메일 레코드 삭제
	 * @param automailID
	 * @return
	 */
	public int deleteInterMailSchedule(int intermailID);
	
	
	/**
	 * <p>발송 상태 변경
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int intermailID, int scheduleID, String state);
	
	
	/**
	 * <p>연동상태값을 가져온다.
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> getInterMailState(int intermailID, int scheduleID);
	
	
	/**
	 * <p>tm_intermail_send_ 테이블 생성 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public int createSendQueueTable(String tableName);
	
	
	/**
	 * <p>연동메일 시간별 통계 리스트
	 * @pram  searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticHourlyList(Map<String, Object> searchMap);
	
	
	
	/**
	 * <p>연동메일 일자별 통계 리스트 
	 * @param searchMap	 
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDailyList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>연동메일 월별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticMonthlyList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>연동메일 도메인별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDomainList(Map<String, Object> searchMap);
	
	
	
	/**
	 * <p>연동메일 기간중 실패원인별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<FailCauseStatistic> statisticFailCauseList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>연동메일 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MailStatistic> porsonPreview(Map<String, Object> searchMap);
	
	
	
	/**
	 * <p>연동메일 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap);
	
	
	/**
	 * <p>발송대기자 전체 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalInterMailSendList(Map<String, Object> paramMap);
	
	
	/**
	 * <p>발송대기자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<InterMailSend> selectInterMailSendList(Map<String, Object> paramMap);
	
	
	
	/**
	 * <p>발송대기자 상세보기 
	 * @param intermailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailSend viewInterMailSend(int intermailID, int scheduleID, String sendID);
	
	
	
	/**
	 * <p>체크된 발송대기자 삭제
	 * @param intermailID
	 * @param scheduleID
	 * @param sendIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSelectedInterMailSend(int intermailID, int scheduleID, String[] sendIDs);
	
	
	
	/**
	 * <p>모든  발송대기자 삭제
	 * @param intermailID
	 * @param scheduleID
	 * @param sendIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAllInterMailSend(int intermailID, int scheduleID);
	
	
	/**
	 * <p>발송승인 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailSendApprove(int intermailID, int scheduleID, String state);
	
	
	/**
	 * <p>발송결과 히스토리 내역 
	 * @param paramsMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<InterMailSendResult> selectInterMailHistory(Map<String,Object> paramsMap);
	
	
	
	/**
	 * <p>발송결과 히스토리 총 카운트  
	 * @param paramsMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totaltInterMailHistory(Map<String,Object> paramsMap);
	

	
	
	/**
	 * <p>발송결과자 상세보기 
	 * @param intermailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailSendResult viewInterMailSendResult(String yearMonth, int intermailID, int scheduleID, String sendID);
	
	
	/**
	 * <p>오류자 재발송 상태값 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailScheduleRetryState(int intermailID, int scheduleID, String retryFinishYN);
	
	
	/**
	 * <p>발송결과 오류자 상태값 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailResultRetryState(String sendID, int intermailID, int scheduleID, String wasRetrySended);
	
	
	
	/**
	 * <p>실패메일(영구적인 오류자제외) 전체 재발송 
	 * @param resultYearMonth
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 */
	public boolean resendRetrySendAll( String resultYearMonth, int intermailID, int scheduleID);
	
	
	
	/**
	 * <p>체크된 메일  재발송
	 * @param resultYearMonth
	 * @param interMailSendResultLIst
	 * @return
	 */
	public boolean resendretrySendChecked(String resultYearMonth,List<InterMailSendResult> interMailSendResultList);
	
	
	/**
	 * <p>재발송이 완료되었는 지 확인 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> checkRetryFinishYN(int intermailID, int scheduleID);

}
