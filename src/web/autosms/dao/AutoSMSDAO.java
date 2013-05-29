package web.autosms.dao;


import org.springframework.dao.DataAccessException;
import java.util.List;
import java.util.Map;
import web.autosms.model.*;




public interface AutoSMSDAO {	
	
	/**
	 * <p>이벤트SMS리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSEvent> selectAutoSMSEventList(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>자동SMS 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoSMSEvent(Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>자동SMS보기 
	 * @param autosmsID
	 * @throws DataAccessException
	 */
	public AutoSMSEvent viewAutoSMSEvent(int autosmsID) throws DataAccessException;
	
	
	
	/**
	 * <p>이벤트SMS등록
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAutoSMSEvent(AutoSMSEvent autoSMSEvent) throws DataAccessException;
	
	
	
	/**
	 * <p>이벤트SMS수정
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAutoSMSEvent(AutoSMSEvent autoSMSEvent) throws DataAccessException;
	
	
	/**
	 * <p>이벤트SMS삭제
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoSMSEvent(int autosmsID) throws DataAccessException;
	
	
	
	/**
	 * <p>자동SMS sendqueue 삭제 
	 * @param autosmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoSMSSendQueue(int autosmsID) throws DataAccessException;
	
	
	
	/**
	 * <p>자동SMS statistic 삭제 
	 * @param autosmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoSMSStatistic(int autosmsID) throws DataAccessException;
	
	
	
	/**
	 * <p>자동SMS 기간중 시간별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSStatistic> statisticHourly(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동SMS 기간중 일자별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSStatistic> statisticDaily(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동SMS 기간중 월별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSStatistic> statisticMonthly(Map<String, Object> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>자동SMS 대상자 미리보기 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSPerson> personPreview(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동SMS 기간중 총 시도 건수
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public int getSendTotal(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동SMS 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>자동SMS 월간 통계 ID 별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<AutoSMSStatistic> autoSMSReportMonth(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>자동SMS 월간 통계 전체 발송 현황
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */	
	public AutoSMSStatistic autoSMSReportMonthAll(String sendTime) throws DataAccessException;
	
	/**
	 * <p>자동SMS 월간통계 카운트 
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoSMSReportMonth(String sendTime) throws DataAccessException;
}
