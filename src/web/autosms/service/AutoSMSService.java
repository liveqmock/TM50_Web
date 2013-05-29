package web.autosms.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.autosms.model.*;

public interface AutoSMSService {

	/**
	 * <p>이벤트SMS리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSEvent> selectAutoSMSEventList(int currentPage, int countPerPage,Map<String, String> searchMap);
	
	
	
	/**
	 * <p>자동SMS 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoSMSEvent(Map<String, String> searchMap);
	
	
	
	/**
	 * <p>자동SMS보기 
	 * @param autosmsID
	 * @throws DataAccessException
	 */
	public AutoSMSEvent viewAutoSMSEvent(int autosmsID);
	
	
	
	/**
	 * <p>이벤트SMS등록
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAutoSMSEvent(AutoSMSEvent autoSMSEvent);
	
	
	
	/**
	 * <p>이벤트SMS수정
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAutoSMSEvent(AutoSMSEvent autoSMSEvent);
	
	
	/**
	 * <p>이벤트SMS삭제
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public void deleteAutoSMSEventAll(int autosmsID);
		

	/**
	 * <p>자동SMS 시간별 통계 리스트 
	 * @return
	 */
	public List<AutoSMSStatistic> statisticHourlyList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>자동SMS 일자별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSStatistic> statisticDailyList(Map<String, Object> searchMap);
	
	
	
	/**
	 * <p>자동SMS 월별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSStatistic> statisticMonthlyList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>자동SMS 대상자 미리보기 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSPerson> personPreview(Map<String, Object> searchMap);
	
	/**
	 * <p>자동SMS 기간중 총 시도 건수
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public int getSendTotal(Map<String, Object> searchMap);
	
	/**
	 * <p>자동SMS 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap);
	
	/**
	 * <p>자동SMS 월간 통계 ID 별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSStatistic> autoSMSReportMonth(Map<String, Object> searchMap);
	
	/**
	 * <p>자동SMS 월간 통계 전체 발송 현황
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */	
	public AutoSMSStatistic autoSMSReportMonthAll(String sendTime);
	
	/**
	 * <p>자동SMS 월간통계 카운트 
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoSMSReportMonth(String sendTime);

}
