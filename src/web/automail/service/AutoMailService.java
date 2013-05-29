package web.automail.service;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.automail.model.AutoMailEvent;
import web.automail.model.FailCauseStatistic;
import web.automail.model.MailStatistic;
public interface AutoMailService {

	/**
	 * <p>자동메일리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<AutoMailEvent> listAutoMailEvents(int currentPage, int countPerPage,Map<String, String> searchMap);
		
	
	/**
	 * <p>자동메일카운트 증가 
	 * @param searchMap
	 * @return
	 */
	public int getTotalCountAutoMailEvent(Map<String, String> searchMap);
	
	
	/**
	 * <p>자동메일이벤트등록 
	 * @param autoMailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAutoMailEvent(AutoMailEvent autoMailEvent);
	
	
	/**
	 * <p>자동메일이벤트수정
	 * @param autoMailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAutoMailEvent(AutoMailEvent autoMailEvent);
	
	
	/**
	 * <p>자동메일이벤트보기 
	 * @param automailID
	 * @return
	 */
	public AutoMailEvent viewAutoMailEvent(int automailID);
	
	
	/**
	 * <p>자동메일 모든  레코드 삭제 
	 * @param automailID
	 * @return
	 */
	public void deleteAutoMailEventAll(int automailID);
	
	/**
	 * <p>자동메일 시간별 통계 리스트 
	 * @return
	 */
	public List<MailStatistic> statisticHourlyList(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 시간별 챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticHourly(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 시간별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticHourlyPie(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 일자별 통계 리스트 
	 ** @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDailyList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>자동메일 일자별 챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticDaily(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 일자별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	
	public String statisticDailyPie(Map<String, Object> searchMape);
	
	/**
	 * <p>자동메일 월별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticMonthlyList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>자동메일 월별 챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticMonthly(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 월별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	
	public String statisticMonthlyPie(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 도메인별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDomainList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>자동메일 도메인별 챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticDomain(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 도메인별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	
	public String statisticDomainPie(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 실패원인별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<FailCauseStatistic> statisticFailCauseList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>자동메일 실패원인별 챠트 json 
	 * @return
	 */
	public String statisticFailCause(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 실패원인별 파이챠트 json 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	
	public String statisticFailCausePie(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 대상자 미리보기 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> porsonPreview(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 월간 통계 ID 별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> autoMailReportMonth(Map<String, Object> searchMap);
	
	/**
	 * <p>자동메일 월간 통계 전체 발송 현황
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */	
	public MailStatistic autoMailReportMonthAll(String sendTime);
	
	/**
	 * <p>자동메일 월간통계 카운트 
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoMailReportMonth(String sendTime);
}
