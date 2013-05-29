package web.masssms.statistic.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;

import web.masssms.statistic.model.*;
import web.masssms.write.model.MassSMSList;
import web.masssms.write.model.SMSTargetingGroup;



public interface  MassSMSStatDAO {
	
	/**
	 * <p>대상자그룹리스트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<SMSTargetingGroup> listTargetingGroup(int masssmsID) throws DataAccessException;
	
	
	/**
	 * <p>대량sms 통계 발송정보
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo massSMSStatisticSendInfo(int masssmsID, int scheduleID) throws DataAccessException;
	
	
	
	/**
	 * <p>대량SMS 통계 기본정보
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo massSMSStatisticBasicInfo(int masssmsID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 통계 필터정보
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSFilter massSMSStatisticFilterInfo(int masssmsID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 대상자 미리보기 총카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalMassSMSPersonPreview(Map<String, Object> searchMap, String backupYearMonth) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSPersonPreview> massSMSPersonPreview(Map<String, Object> searchMap, String backupYearMonth) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 결과 백업 테이블 정보 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String getBackupYearMonth(Map<String, Object> searchMap)  throws DataAccessException;
	
	/**
	 * <p>대량SMS 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountMassSMSReportMonthList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSList> massSMSReportMonthList(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 월간 보고서 - 총괄현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSReportMonth massSMSReportMonthTotalInfo(String year, String month, String userID,String groupID, String[] userInfo) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 월간 보고서 - SMS 발송 현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSReportMonth massSMSReportMonthSendInfo(String year, String month , String userID, String groupID, String[] userInfo) throws DataAccessException;
	
	
	/**
	 * <p>계정별 통계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSStatisticUsers> masssmsStatisticUsersList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException;
}



	

