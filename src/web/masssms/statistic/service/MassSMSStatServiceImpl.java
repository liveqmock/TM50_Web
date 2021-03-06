package web.masssms.statistic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;


import web.masssms.statistic.model.*;
import web.masssms.statistic.dao.MassSMSStatDAO;
import web.masssms.write.model.MassSMSList;
import web.masssms.write.model.SMSTargetingGroup;
import web.common.util.*;




public class MassSMSStatServiceImpl implements MassSMSStatService{

	private Logger logger = Logger.getLogger("TM");
	private MassSMSStatDAO massSMSStatDAO = null;
	
	public void setMassSMSStatDAO(MassSMSStatDAO massSMSStatDAO){
		this.massSMSStatDAO = massSMSStatDAO;
	}
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<SMSTargetingGroup> listTargetingGroup(int masssmsID){
		List<SMSTargetingGroup> result = null;
		try{
			result = massSMSStatDAO.listTargetingGroup(masssmsID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량sms 통계 발송정보
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo massSMSStatisticSendInfo(int masssmsID, int scheduleID){
		MassSMSInfo result = null;
		try{
			result = massSMSStatDAO.massSMSStatisticSendInfo(masssmsID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량SMS 통계 기본정보
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo massSMSStatisticBasicInfo(int masssmsID, int scheduleID){
		MassSMSInfo result = null;
		try{
			result = massSMSStatDAO.massSMSStatisticBasicInfo(masssmsID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량SMS 통계 필터정보
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSFilter massSMSStatisticFilterInfo(int masssmsID, int scheduleID){
		MassSMSFilter result = null;
		try{
			result = massSMSStatDAO.massSMSStatisticFilterInfo(masssmsID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 대상자 미리보기 총카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalMassSMSPersonPreview(Map<String, Object> searchMap){
		int result = 0;
		try{
			String backupYearMonth = massSMSStatDAO.getBackupYearMonth(searchMap); 
			result = massSMSStatDAO.totalMassSMSPersonPreview(searchMap, backupYearMonth);
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
	}
	
	/**
	 * <p>대량메일 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassSMSPersonPreview> personPreview(Map<String, Object> searchMap){
		List<MassSMSPersonPreview> result = null;
		try{
			String backupYearMonth = massSMSStatDAO.getBackupYearMonth(searchMap); 
			result = massSMSStatDAO.massSMSPersonPreview(searchMap, backupYearMonth);
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
	}
	
	/**
	 * <p>대량SMS 결과 백업 테이블 정보 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String getBackupYearMonth(Map<String, Object> searchMap){
		String  result = "";
		try{
			result = massSMSStatDAO.getBackupYearMonth(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량SMS 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountMassSMSReportMonthList(String[] userInfo, Map<String, String> searchMap){
		int result = 0;
		try{			
			result = massSMSStatDAO.totalCountMassSMSReportMonthList(userInfo, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
	}
	
	/**
	 * <p>대량SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSList> massSMSReportMonthList(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap){
		List<MassSMSList> result = null;
		try{
			result = massSMSStatDAO.massSMSReportMonthList(userInfo, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량SMS 월간 보고서 - 총괄현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSReportMonth massSMSReportMonthTotalInfo(String year, String month, String userID,String groupID, String[] userInfo){
		MassSMSReportMonth result = null;
		try{
			result = massSMSStatDAO.massSMSReportMonthTotalInfo(year, month, userID, groupID, userInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량SMS 월간 보고서 - SMS 발송 현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSReportMonth massSMSReportMonthSendInfo(String year, String month , String userID, String groupID, String[] userInfo){
		MassSMSReportMonth result = null;
		try{
			result = massSMSStatDAO.massSMSReportMonthSendInfo(year, month, userID, groupID, userInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>계정별 통계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSStatisticUsers> masssmsStatisticUsersList(String[] userInfo, Map<String, String> searchMap){
		 List<MassSMSStatisticUsers> result = null;
		try{
			result = massSMSStatDAO.masssmsStatisticUsersList(userInfo, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
