package web.autosms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import web.autosms.dao.AutoSMSDAO;
import web.autosms.model.AutoSMSEvent;
import web.autosms.model.AutoSMSPerson;
import web.autosms.model.AutoSMSStatistic;
import web.common.util.DateUtils;
import web.common.util.StringUtil;

public class AutoSMSServiceImpl implements AutoSMSService{
	
	private Logger logger = Logger.getLogger("TM");	
	private AutoSMSDAO autoSMSDAO = null;
	
	public void setAutoSMSDAO(AutoSMSDAO autoSMSDAO){	
		this.autoSMSDAO = autoSMSDAO;
	}
	

	/**
	 * <p>이벤트SMS리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSEvent> selectAutoSMSEventList(int currentPage, int countPerPage,Map<String, String> searchMap){
		List<AutoSMSEvent> result = null;
		try{
			result = autoSMSDAO.selectAutoSMSEventList(currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>자동SMS 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoSMSEvent(Map<String, String> searchMap){
		int result = 0;
		try{
			result = autoSMSDAO.getTotalCountAutoSMSEvent(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>자동SMS보기 
	 * @param autosmsID
	 * @throws DataAccessException
	 */
	public AutoSMSEvent viewAutoSMSEvent(int autosmsID){
		AutoSMSEvent autoSMSEvent = null;
		try{
			autoSMSEvent = autoSMSDAO.viewAutoSMSEvent(autosmsID);
		}catch(Exception e){
			logger.error(e);
		}
		return autoSMSEvent;
	}
	
	
	
	/**
	 * <p>이벤트SMS등록
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAutoSMSEvent(AutoSMSEvent autoSMSEvent){
		int result = 0;
		try{
			result = autoSMSDAO.insertAutoSMSEvent(autoSMSEvent);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>이벤트SMS수정
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAutoSMSEvent(AutoSMSEvent autoSMSEvent){
		int result = 0;
		try{
			result = autoSMSDAO.updateAutoSMSEvent(autoSMSEvent);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>이벤트SMS삭제
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public void deleteAutoSMSEventAll(int autosmsID){	
		autoSMSDAO.deleteAutoSMSEvent(autosmsID);
		autoSMSDAO.deleteAutoSMSSendQueue(autosmsID);
		autoSMSDAO.deleteAutoSMSStatistic(autosmsID);	
	}
	
	
	/**
	 * <p>자동SMS 시간별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSStatistic> statisticHourlyList(Map<String, Object> searchMap){
		List<AutoSMSStatistic> result = null;
		try{
			List<AutoSMSStatistic> statisticHourlyList = autoSMSDAO.statisticHourly(searchMap);
			// 맵에 담는다.
			Map<Integer, AutoSMSStatistic> map = new HashMap<Integer, AutoSMSStatistic>(); 
			for(AutoSMSStatistic autoSMSStatistic : statisticHourlyList  ){
				map.put(new Integer(autoSMSStatistic.getStandard()),autoSMSStatistic);
			}
			statisticHourlyList.clear();
			for(int i = 0; i < 24; i ++) {
				AutoSMSStatistic tempMailStatistic = new AutoSMSStatistic();
				tempMailStatistic.setStandard(i+"");
				tempMailStatistic. setViewStandard(StringUtil.toFormatStr("00",(double)i)+"시 ~ " +StringUtil.toFormatStr("00",(double)i+1)+"시");
				if(map.containsKey(i)) {	
					tempMailStatistic.setSendTotal(map.get(i).getSendTotal());
					tempMailStatistic.setSuccessTotal(map.get(i).getSuccessTotal());
					tempMailStatistic.setFailTotal(map.get(i).getFailTotal());
					tempMailStatistic.setReadyTotal(map.get(i).getReadyTotal());
				}else{
					tempMailStatistic.setSendTotal(0);
					tempMailStatistic.setSuccessTotal(0);
					tempMailStatistic.setFailTotal(0);
					tempMailStatistic.setReadyTotal(0);
				}
				statisticHourlyList.add(tempMailStatistic);
			}
			result = statisticHourlyList;
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
		
	}
	
	
	/**
	 * <p>자동메일 일자별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSStatistic> statisticDailyList(Map<String, Object> searchMap){
		List<AutoSMSStatistic> result = null;
		try{
			List<AutoSMSStatistic> statisticDailyList = autoSMSDAO.statisticDaily(searchMap);
			
			// 맵에 담는다.
			Map<Integer, AutoSMSStatistic> map = new HashMap<Integer, AutoSMSStatistic>(); 
			for(AutoSMSStatistic autoSMSStatistic : statisticDailyList ){
				map.put(new Integer(autoSMSStatistic.getStandard()),autoSMSStatistic);
			}
			statisticDailyList.clear();
			String year = (String)searchMap.get("year");
			String month = (String)searchMap.get("month");
			String day = (String)searchMap.get("day");
			
			String startDate = year+month;
			String endDate = year+month;
			
			if(day.equals("all")){
				startDate+="01";
				endDate=DateUtils.lastDayOfMonth(startDate);
				//System.out.println(endDate);
			}else{
				startDate += day;
				endDate += day;
			}
			
			int count = DateUtils.daysBetween1(startDate, endDate);
			
			for(int i = 0; i <= count; i ++) {
				int key_value = new Integer(DateUtils.addDays(startDate, i));
				
				AutoSMSStatistic tempMailStatistic = new AutoSMSStatistic();
				tempMailStatistic.setStandard(key_value+"");
				tempMailStatistic. setViewStandard(DateUtils.reFormat(key_value+"","yyyymmdd","yyyy-mm-dd"));
				if(map.containsKey(key_value)) {
					tempMailStatistic.setStandard(key_value+"");
					tempMailStatistic.setSendTotal(map.get(key_value).getSendTotal());
					tempMailStatistic.setSuccessTotal(map.get(key_value).getSuccessTotal());
					tempMailStatistic.setFailTotal(map.get(key_value).getFailTotal());
					tempMailStatistic.setReadyTotal(map.get(key_value).getReadyTotal());
				}else{
					tempMailStatistic.setSendTotal(0);
					tempMailStatistic.setSuccessTotal(0);
					tempMailStatistic.setFailTotal(0);
					tempMailStatistic.setReadyTotal(0);
				}
				statisticDailyList.add(tempMailStatistic);
			}
			
			result = statisticDailyList;
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
		
	}
	
	
	/**
	 * <p>자동메일 월별 통계 리스트 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSStatistic> statisticMonthlyList(Map<String, Object> searchMap){
		List<AutoSMSStatistic> result = null;
		try{
			List<AutoSMSStatistic> statisticMonthlyList = autoSMSDAO.statisticMonthly(searchMap);
			// 맵에 담는다.
			Map<Integer, AutoSMSStatistic> map = new HashMap<Integer, AutoSMSStatistic>(); 
			for(AutoSMSStatistic mailStatistic : statisticMonthlyList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
			statisticMonthlyList.clear();
			String year = (String)searchMap.get("year");
			
			String startDate = year+"0101";
			String endDate = year+"1231";
			
			int count = DateUtils.monthBetween(startDate, endDate);
			
			for(int i = 0; i <= count; i ++) {
				int key_value = new Integer(DateUtils.reFormat(DateUtils.addMonths(startDate, i),"yyyymmdd","yyyymm"));	
				AutoSMSStatistic tempMailStatistic = new AutoSMSStatistic();
				tempMailStatistic.setStandard(key_value+"");
				tempMailStatistic.setViewStandard(DateUtils.reFormat(key_value+"","yyyymm","yyyy-mm"));
				if(map.containsKey(key_value)) {
					tempMailStatistic.setSendTotal(map.get(key_value).getSendTotal());
					tempMailStatistic.setSuccessTotal(map.get(key_value).getSuccessTotal());
					tempMailStatistic.setFailTotal(map.get(key_value).getFailTotal());
					tempMailStatistic.setReadyTotal(map.get(key_value).getReadyTotal());
				}else{
					tempMailStatistic.setSendTotal(0);
					tempMailStatistic.setSuccessTotal(0);
					tempMailStatistic.setFailTotal(0);
					tempMailStatistic.setReadyTotal(0);
				}
				statisticMonthlyList.add(tempMailStatistic);
			}
			result = statisticMonthlyList;
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	
	
	/**
	 * <p>자동SMS 대상자 미리보기 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<AutoSMSPerson> personPreview(Map<String, Object> searchMap){
		List<AutoSMSPerson> result = null;
		try{
			result = autoSMSDAO.personPreview(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>자동SMS 기간중 총 시도 건수
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public int getSendTotal(Map<String, Object> searchMap){
		int result = 0;
		try{
			result = autoSMSDAO.getSendTotal(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>자동SMS 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap){
		int result = 0;
		try{
			result = autoSMSDAO.totalPorsonPreview(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>자동SMS 월간 통계 ID 별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	
	public List<AutoSMSStatistic> autoSMSReportMonth(Map<String, Object> searchMap){
		List<AutoSMSStatistic> result = null;
		
		try {
			result = autoSMSDAO.autoSMSReportMonth(searchMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		
		return result;
	}
	
	/**
	 * <p>자동SMS 월간 통계 전체 발송 현황
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */	
	
	public AutoSMSStatistic autoSMSReportMonthAll(String sendTime){
		AutoSMSStatistic result = null;
		
		try {
			result = autoSMSDAO.autoSMSReportMonthAll(sendTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		
		return result;
	}
	
	/**
	 * <p>자동SMS 월간통계 카운트 
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoSMSReportMonth(String sendTime){
		int result = 0;
		try{
			result = autoSMSDAO.getTotalCountAutoSMSReportMonth(sendTime);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
}
