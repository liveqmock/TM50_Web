package web.automail.dao;

import java.util.*;

import web.automail.model.*;

import org.springframework.dao.DataAccessException;

public interface AutoMailDAO {
	
	

	/**
	 * <p>자동메일리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoMailEvent> listAutoMailEvents(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
		
	
	
	/**
	 * <p>자동메일카운트 증가 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoMailEvent(Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>자동메일이벤트등록 
	 * @param autoMailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAutoMailEvent(AutoMailEvent autoMailEvent) throws DataAccessException;
	
	
	/**
	 * <p>자동메일이벤트수정
	 * @param autoMailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAutoMailEvent(AutoMailEvent autoMailEvent) throws DataAccessException;
	
	
	
	/**
	 * <p>자동메일이벤트보기 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public AutoMailEvent viewAutoMailEvent(int automailID) throws DataAccessException;
	
	
	
	
	/**
	 * <p>자동메일 event 삭제 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoMailEvent(int automailID) throws DataAccessException;
	
	
	/**
	 * <p>자동메일 sendqueue 삭제 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoMailSendQueue(int automailID) throws DataAccessException;

	
	
	/**
	 * <p>자동메일 domainstatistic 삭제 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoMailDomainStatistic(int automailID) throws DataAccessException;
	
	
	/**
	 * <p>자동메일 failstatistic 삭제 
	 * @param automailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoMailFailStatistic(int automailID) throws DataAccessException;
	
	/**
	 * <p>자동메일 기간중 시간별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticHourly(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동메일 기간중 일자별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDaily(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동메일 기간중 월별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticMonthly(Map<String, Object> searchMap) throws DataAccessException;

	/**
	 * <p>자동메일 기간중 도메인별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDomain(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동메일 기간중 실패원인별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<FailCauseStatistic> statisticFailCause(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동메일 대상자 미리보기 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> porsonPreview(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동메일 기간중 총 시도 건수
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public int getSendTotal(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동메일 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동메일 월간 통계 ID 별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MailStatistic> autoMailReportMonth(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동메일 월간 통계 전체 발송 현황
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */	
	public MailStatistic autoMailReportMonthAll(String sendTime) throws DataAccessException;

	/**
	 * <p>자동메일 월간통계 카운트 
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */	
	public int getTotalCountAutoMailReportMonth(String sendTime) throws DataAccessException;
}
