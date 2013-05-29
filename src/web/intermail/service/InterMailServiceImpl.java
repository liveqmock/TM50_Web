package web.intermail.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import web.common.model.FileUpload;
import web.common.util.DateUtils;
import web.common.util.StringUtil;
import web.intermail.dao.InterMailDAO;
import web.intermail.model.*;


public class InterMailServiceImpl implements InterMailService{
	
	
	private Logger logger = Logger.getLogger("TM");	
	private InterMailDAO interMailDAO = null;
	
	public void setInterMailDAO(InterMailDAO interMailDAO){	
		this.interMailDAO = interMailDAO;
	}
	
	/**
	 * <p>연동메일 갯수 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountInterMailEvent(Map<String, String> searchMap){
		int result = 0;
		try{
			result = interMailDAO.getTotalCountInterMailEvent(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>자동메일리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<InterMailEvent> listInterMailEvents(int currentPage, int countPerPage,Map<String, String> searchMap){
		List<InterMailEvent> result = null;
		try{
			result = interMailDAO.listInterMailEvents(currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>자동메일이벤트보기 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailEvent viewInterMailEvent(int intermailID){
		InterMailEvent result = null;
		try{
			result = interMailDAO.viewInterMailEvent(intermailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>연동발송설정보기
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<InterMailSchedule> selectInterMailSchedule(int intermailID){
		List<InterMailSchedule> result = null;
		try{
			result = interMailDAO.selectInterMailSchedule(intermailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>업로드키로 파일정보 불러오기 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public FileUpload getFileInfo(String uploadKey){
		FileUpload result = null;
		try{
			result = interMailDAO.getFileInfo(uploadKey);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>연동메일 정보입력
	 * @param intermailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertInterMailEvent(InterMailEvent interMailEvent){
		int result = 0;
		try{
			result = interMailDAO.insertInterMailEvent(interMailEvent);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>스케줄입력 
	 * @param interMailSchedule
	 * @return
	 * @throws DataAccessException
	 */
	public int insertInterMailSchedule(InterMailSchedule interMailSchedule){
		int result = 0;
		try{
			result = interMailDAO.insertInterMailSchedule(interMailSchedule);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>가장 최근에 입력된 intermailID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxInterMailID(){
		int result = 0;
		try{
			result = interMailDAO.getMaxInterMailID();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	/**
	 * <p>파일 인서트 
	 * @param fileKey
	 * @param fileName
	 * @return
	 * @throws DataAccessException
	 */
	public int insertFile(FileUpload fileUpload){
		int result = 0;
		try{
			result = interMailDAO.insertFile(fileUpload);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>연동메일 정보입력
	 * @param intermailEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailEvent(InterMailEvent interMailEvent){
		int result = 0;
		try{
			result = interMailDAO.updateInterMailEvent(interMailEvent);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>연동메일 스케줄 업데이트 
	 * @param interMailSchedule
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailSchedule(InterMailSchedule interMailSchedule){
		int result = 0;
		try{
			result = interMailDAO.updateInterMailSchedule(interMailSchedule);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>연동메일 레코드 삭제(deleteYN=Y 엔진에서 삭제한다)
	 * @param automailID
	 * @return
	 */
	public int deleteInterMailSchedule(int intermailID){
		int result = 0;
		try{
			result = interMailDAO.deleteInterMailSchedule(intermailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>발송 상태 변경
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int intermailID, int scheduleID, String state){
		int result = 0;
		try{
			result = interMailDAO.updateSendState(intermailID,scheduleID,state);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>연동상태값을 가져온다.
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> getInterMailState(int intermailID, int scheduleID){
		Map<String,Object> result = null;
		try{
			result = interMailDAO.getInterMailState(intermailID,scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>tm_intermail_send_ 테이블 생성 
	 * @param intermailID
	 * @return
	 * @throws DataAccessException
	 */
	public int createSendQueueTable(String tableName){
		int result = 0;
		try{
			result = interMailDAO.createSendQueueTable(tableName);
		}catch(Exception e){
			result = -1;
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>연동메일 시간별 통계 리스트
	 * @pram  searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticHourlyList(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			List<MailStatistic> statisticHourlyList = interMailDAO.statisticHourly(searchMap);
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>(); 
			for(MailStatistic mailStatistic : statisticHourlyList  ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
			statisticHourlyList.clear();
			for(int i = 0; i < 24; i ++) {
				MailStatistic tempMailStatistic = new MailStatistic();
				tempMailStatistic.setStandard(i+"");
				tempMailStatistic. setViewStandard(StringUtil.toFormatStr("00",(double)i)+"시 ~ " +StringUtil.toFormatStr("00",(double)i+1)+"시");
				if(map.containsKey(i)) {	
					tempMailStatistic.setSendTotal(map.get(i).getSendTotal());
					tempMailStatistic.setSuccessTotal(map.get(i).getSuccessTotal());
					tempMailStatistic.setFailTotal(map.get(i).getFailTotal());
					tempMailStatistic.setOpenTotal(map.get(i).getOpenTotal());
				}else{
					tempMailStatistic.setSendTotal(0);
					tempMailStatistic.setSuccessTotal(0);
					tempMailStatistic.setFailTotal(0);
					tempMailStatistic.setOpenTotal(0);
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
	 * <p>연동메일 일자별 통계 리스트 
	 * @param searchMap	 
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDailyList(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			List<MailStatistic> statisticDailyList = interMailDAO.statisticDaily(searchMap);
			
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>(); 
			for(MailStatistic mailStatistic : statisticDailyList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
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
				
				MailStatistic tempMailStatistic = new MailStatistic();
				tempMailStatistic.setStandard(key_value+"");
				tempMailStatistic. setViewStandard(DateUtils.reFormat(key_value+"","yyyymmdd","yyyy-mm-dd"));
				if(map.containsKey(key_value)) {
					tempMailStatistic.setStandard(key_value+"");
					tempMailStatistic.setSendTotal(map.get(key_value).getSendTotal());
					tempMailStatistic.setSuccessTotal(map.get(key_value).getSuccessTotal());
					tempMailStatistic.setFailTotal(map.get(key_value).getFailTotal());
					tempMailStatistic.setOpenTotal(map.get(key_value).getOpenTotal());
				}else{
					tempMailStatistic.setSendTotal(0);
					tempMailStatistic.setSuccessTotal(0);
					tempMailStatistic.setFailTotal(0);
					tempMailStatistic.setOpenTotal(0);
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
	 * <p>연동메일 월별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticMonthlyList(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			List<MailStatistic> statisticMonthlyList = interMailDAO.statisticMonthly(searchMap);
			// 맵에 담는다.
			Map<Integer, MailStatistic> map = new HashMap<Integer, MailStatistic>(); 
			for(MailStatistic mailStatistic : statisticMonthlyList ){
				map.put(new Integer(mailStatistic.getStandard()),mailStatistic);
			}
			statisticMonthlyList.clear();
			String year = (String)searchMap.get("year");
			
			String startDate = year+"0101";
			String endDate = year+"1231";
			
			int count = DateUtils.monthBetween(startDate, endDate);
			
			for(int i = 0; i <= count; i ++) {
				int key_value = new Integer(DateUtils.reFormat(DateUtils.addMonths(startDate, i),"yyyymmdd","yyyymm"));	
				MailStatistic tempMailStatistic = new MailStatistic();
				tempMailStatistic.setStandard(key_value+"");
				tempMailStatistic.setViewStandard(DateUtils.reFormat(key_value+"","yyyymm","yyyy-mm"));
				if(map.containsKey(key_value)) {
					tempMailStatistic.setSendTotal(map.get(key_value).getSendTotal());
					tempMailStatistic.setSuccessTotal(map.get(key_value).getSuccessTotal());
					tempMailStatistic.setFailTotal(map.get(key_value).getFailTotal());
					tempMailStatistic.setOpenTotal(map.get(key_value).getOpenTotal());
				}else{
					tempMailStatistic.setSendTotal(0);
					tempMailStatistic.setSuccessTotal(0);
					tempMailStatistic.setFailTotal(0);
					tempMailStatistic.setOpenTotal(0);
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
	 * <p>연동메일 도메인별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailStatistic> statisticDomainList(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			result = interMailDAO.statisticDomain(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>연동메일 기간중 실패원인별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<FailCauseStatistic> statisticFailCauseList(Map<String, Object> searchMap){
		List<FailCauseStatistic> result = null;
		try{
			result = interMailDAO.statisticFailCauseList(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>연동메일 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MailStatistic> porsonPreview(Map<String, Object> searchMap){
		List<MailStatistic> result = null;
		try{
			result = interMailDAO.porsonPreview(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>연동메일 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap){
		int result = 0;
		try{
			result = interMailDAO.totalPorsonPreview(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>발송대기자 전체 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalInterMailSendList(Map<String, Object> paramMap){
		int result = 0;
		try{
			result = interMailDAO.totalInterMailSendList(paramMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>발송대기자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<InterMailSend> selectInterMailSendList(Map<String, Object> paramMap){
		List<InterMailSend>  result = null;
		try{
			result = interMailDAO.selectInterMailSendList(paramMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>발송대기자 상세보기 
	 * @param intermailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailSend viewInterMailSend(int intermailID, int scheduleID, String sendID){
		InterMailSend  interMailSend = null;
		try{
			interMailSend = interMailDAO.viewInterMailSend(intermailID, scheduleID, sendID);
		}catch(Exception e){
			logger.error(e);
		}
		return interMailSend;
	}
	
	/**
	 * <p>체크된 발송대기자 삭제
	 * @param intermailID
	 * @param scheduleID
	 * @param sendIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSelectedInterMailSend(int intermailID, int scheduleID, String[] sendIDs){
		int  result = 0;
		try{
			result = interMailDAO.deleteSelectedInterMailSend(intermailID, scheduleID, sendIDs);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>모든  발송대기자 삭제
	 * @param intermailID
	 * @param scheduleID
	 * @param sendIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAllInterMailSend(int intermailID, int scheduleID){
		int  result = 0;
		try{
			result = interMailDAO.deleteAllInterMailSend(intermailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>발송승인 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailSendApprove(int intermailID, int scheduleID, String state){
		int  result = 0;
		try{
			result = interMailDAO.updateInterMailSendApprove(intermailID, scheduleID, state);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>발송결과 히스토리 내역 
	 * @param paramsMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<InterMailSendResult> selectInterMailHistory(Map<String,Object> paramsMap){
		List<InterMailSendResult> result = null;
		try{
			result = interMailDAO.selectInterMailHistory(paramsMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>발송결과 히스토리 총 카운트  
	 * @param paramsMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totaltInterMailHistory(Map<String,Object> paramsMap){
		int  result = 0;
		try{
			result = interMailDAO.totaltInterMailHistory(paramsMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>발송결과자 상세보기 
	 * @param intermailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */
	public InterMailSendResult viewInterMailSendResult(String yearMonth, int intermailID, int scheduleID, String sendID){
		InterMailSendResult result = null;
		try{
			result = interMailDAO.viewInterMailSendResult(yearMonth, intermailID, scheduleID, sendID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	/**
	 * <p>오류자 재발송 상태값 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailScheduleRetryState(int intermailID, int scheduleID, String retryFinishYN){
		int  result = 0;
		try{
			result = interMailDAO.updateInterMailScheduleRetryState(intermailID, scheduleID, retryFinishYN);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>발송결과 오류자 상태값 변경 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateInterMailResultRetryState(String sendID, int intermailID, int scheduleID, String wasRetrySended){
		int  result = 0;
		try{
			result = interMailDAO.updateInterMailResultRetryState(sendID,intermailID, scheduleID, wasRetrySended);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>실패메일(영구적인 오류자제외) 전체 재발송 
	 * @param resultYearMonth
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 */
	public boolean resendRetrySendAll( String resultYearMonth, int intermailID, int scheduleID){
		//트렌젝션을 위해 try~catch를 하지 않는다.
		boolean result = true;
		int result1 = interMailDAO.updateRetryFinishSet("N", intermailID, scheduleID);
		int result2 = interMailDAO.updateWasRetrySendedAllFailed("X", intermailID, scheduleID, resultYearMonth);
		
		if(result1<0 || result2<0){
			result = false;
		}
		return result;
		
	}
	
	/**
	 * <p>체크된 메일  재발송
	 * @param resultYearMonth
	 * @param interMailSendResultLIst
	 * @return
	 */
	public boolean resendretrySendChecked(String resultYearMonth,List<InterMailSendResult> interMailSendResultList){
		boolean result = true;
		int result1 = interMailDAO.updateRetryFinishSet("N", interMailSendResultList.get(0).getIntermailID(), interMailSendResultList.get(0).getScheduleID());
		int[] result2 = interMailDAO.updateWasRetrySendedCheckedFailedBatch(resultYearMonth, interMailSendResultList) ;
		
		if(result1<0 || result2==null){
			result = false;
		}
		return result;
	}
	
	/**
	 * <p>재발송이 완료되었는 지 확인 
	 * @param intermailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> checkRetryFinishYN(int intermailID, int scheduleID){
		Map<String,Object>  result = null;
		try{
			result = interMailDAO.checkRetryFinishYN(intermailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
}
