package web.massmail.statistic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import web.massmail.statistic.dao.MassMailStatDAO;
import web.massmail.statistic.model.*;
import web.massmail.write.dao.MassMailDAO;
import web.massmail.write.model.MassMailList;
import web.massmail.write.model.OnetooneTarget;
import web.massmail.write.model.TargetingGroup;
import web.common.util.*;
import web.content.poll.model.PollQuestion;




public class MassMailStatServiceImpl implements MassMailStatService{

	private Logger logger = Logger.getLogger("TM");
	private MassMailStatDAO massMailStatDAO = null;
	
	public void setMassMailStatDAO(MassMailStatDAO massMailStatDAO){
		this.massMailStatDAO = massMailStatDAO;
	}
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(int massmailID){
		List<TargetingGroup> result = null;
		try{
			result = massMailStatDAO.listTargetingGroup(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량메일 통계 - 기본정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo massMailStatisticBasicInfo(int massmailID, int scheduleID){
		MassMailInfo massMailInfo = null;
		try{
			massMailInfo = massMailStatDAO.massMailStatisticBasicInfo(massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return massMailInfo;
	}
	
	/**
	 * <p>대량메일 통계 발송정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo massMailStatisticSendInfo(int massmailID, int scheduleID){
		MassMailInfo massMailInfo = null;
		try{
			massMailInfo = massMailStatDAO.massMailStatisticSendInfo(massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return massMailInfo;
	}
	/**
	 * <p>대량메일 통계 필터정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailFilter massMailStatisticFilterInfo(int massmailID, int scheduleID){
		 MassMailFilter result = null;
		try{
			result = massMailStatDAO.massMailStatisticFilterInfo(massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>대량메일 링크별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailLink> statisticLinkList(Map<String, Object> searchMap){
			List<MassMailLink> result = null;
			try{
				result = massMailStatDAO.massMailStatisticLink(searchMap);
			}catch(Exception e){
				logger.error(e);
			}
			return result;
	}
	
	/**
	 * <p>대량메일 링크별 통계 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticLinkBar(Map<String, Object> searchMap){		
		
		String result = "'";
		
		try{
			List<MassMailLink> massMailLinkList = massMailStatDAO.massMailStatisticLink(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailLink massMailLink  : massMailLinkList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("linkID", massMailLink.getLinkID());
				rsMap.put("linkCount", massMailLink.getLinkCount());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartLinkBar(statisticArrayList);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;

	}
	
	/**
	 * <p>대량메일 링크별 통계 Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticLinkPie(Map<String, Object> searchMap){
		
		
		String result = "'";
		
		try{
			List<MassMailLink> massMailLinkList = massMailStatDAO.massMailStatisticLink(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailLink massMailLink  : massMailLinkList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("linkID", massMailLink.getLinkID());
				rsMap.put("linkCount", massMailLink.getLinkCount());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartLinkPie(statisticArrayList);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;

	}
	
	/**
	 * <p>대량메일 시간별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatistic> statisticHourlyList(Map<String, Object> searchMap){
			List<MassMailStatistic> result = null;
			try{
				List<MassMailStatistic> massMailStatisticList = massMailStatDAO.massMailStatisticHourly(searchMap);
				// 맵에 담는다.
				Map<Integer, MassMailStatistic> map = new HashMap<Integer, MassMailStatistic>(); 
				for(MassMailStatistic massmailStatistic : massMailStatisticList ){
					map.put(new Integer(massmailStatistic.getStandard()),massmailStatistic);
				}
				massMailStatisticList.clear();
				for(int i = 0; i < 24; i ++) {
					MassMailStatistic tempMassmailStatistic = new MassMailStatistic();
					tempMassmailStatistic.setStandard(i+"");
					tempMassmailStatistic.setViewStandard(StringUtil.toFormatStr("00",(double)i)+"시 ~ " +StringUtil.toFormatStr("00",(double)i+1)+"시");
					if(map.containsKey(i)) {	
						tempMassmailStatistic.setSendTotal(map.get(i).getSendTotal());
						tempMassmailStatistic.setSuccessTotal(map.get(i).getSuccessTotal());
						tempMassmailStatistic.setFailTotal(map.get(i).getFailTotal());
						tempMassmailStatistic.setOpenTotal(map.get(i).getOpenTotal());
						tempMassmailStatistic.setClickTotal(map.get(i).getClickTotal());
						tempMassmailStatistic.setRejectcallTotal(map.get(i).getRejectcallTotal());
					}else{
						tempMassmailStatistic.setSendTotal(0);
						tempMassmailStatistic.setSuccessTotal(0);
						tempMassmailStatistic.setFailTotal(0);
						tempMassmailStatistic.setOpenTotal(0);
						tempMassmailStatistic.setClickTotal(0);
						tempMassmailStatistic.setRejectcallTotal(0);
					}
					massMailStatisticList.add(i, tempMassmailStatistic);
				}
				
				result = massMailStatisticList;
			}catch(Exception e){
				logger.error(e);
			}
			return result;
	}
	
	/**
	 * <p>대량메일 시간별 통계 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticHourlyBar(Map<String, Object> searchMap){
		String result = "'";
		
		try{
			List<MassMailStatistic> massMailStatisticList = massMailStatDAO.massMailStatisticHourly(searchMap);
			
			// 맵에 담는다.
			Map<Integer, MassMailStatistic> map = new HashMap<Integer, MassMailStatistic>(); 
			for(MassMailStatistic massmailStatistic : massMailStatisticList ){
				map.put(new Integer(massmailStatistic.getStandard()),massmailStatistic);
			}
			//차트를 만든다. 
			result = ChartUtil.createMailChartTimeBar(map);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>대량메일 시간별 통계 Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticHourlyPie(Map<String, Object> searchMap){
		
		String result = "";
		String key = (String)searchMap.get("key");
		
		try{
			List<MassMailStatistic> massMailStatisticList = massMailStatDAO.massMailStatisticDaily(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatistic massMailStatistic  : massMailStatisticList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getStandard());
				rsMap.put("sendTotal", massMailStatistic.getSendTotal());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllPie(statisticArrayList, key);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;	
		
	
	}
	/**
	 * <p>대량메일 일자별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatistic> statisticDailyList(Map<String, Object> searchMap){
			List<MassMailStatistic> result = null;
			try{
				result = massMailStatDAO.massMailStatisticDaily(searchMap);
			}catch(Exception e){
				logger.error(e);
			}
			return result;
	}
	
	/**
	 * <p>대량메일 일자별 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticDailyBar(Map<String, Object> searchMap){
		
		String result = "";
		
		try{
			List<MassMailStatistic> massMailStatisticList = massMailStatDAO.massMailStatisticDaily(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatistic massMailStatistic  : massMailStatisticList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getStandard());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllBar(statisticArrayList);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;
		

	}
	
	/**
	 * <p>대량메일 일자별 Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticDailyPie(Map<String, Object> searchMap){
		
		String result = "";
		String key = (String)searchMap.get("key");
		
		try{
			List<MassMailStatistic> massMailStatisticList = massMailStatDAO.massMailStatisticDaily(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatistic massMailStatistic  : massMailStatisticList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getStandard());
				rsMap.put("sendTotal", massMailStatistic.getSendTotal());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllPie(statisticArrayList, key);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;			

	}
	
	/**
	 * <p>대량메일 도메인별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatistic> statisticDomainList(Map<String, Object> searchMap){
			List<MassMailStatistic> result = null;
			try{
				result = massMailStatDAO.massMailStatisticDomain(searchMap);
			}catch(Exception e){
				logger.error(e);
			}
			return result;
	}
	
	/**
	 * <p>대량메일 도메인별 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */

	public String statisticDomainBar(Map<String, Object> searchMap){
		
		String result = "";
		
		try{
			List<MassMailStatistic> massMailStatisticList = massMailStatDAO.massMailStatisticDomain(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatistic massMailStatistic  : massMailStatisticList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getStandard());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllBar(statisticArrayList);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;
		
	
		
	}
	
	/**
	 * <p>대량메일 도메인별 Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticDomainPie(Map<String, Object> searchMap){		
		
		String result = "";
		String key = (String)searchMap.get("key");
		
		try{
			List<MassMailStatistic> massMailStatisticList = massMailStatDAO.massMailStatisticDomain(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatistic massMailStatistic  : massMailStatisticList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getStandard());
				rsMap.put("sendTotal", massMailStatistic.getSendTotal());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllPie(statisticArrayList, key);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;	

	}
	
	/**
	 * <p>대량메일 실패 도메인 통계 리스트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticDomainFail> statisticFailDomainList(Map<String, Object> searchMap){
		List<MassMailStatisticDomainFail> result = null;
		try{
			result = massMailStatDAO.massMailStatisticFailDomain(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
		
	}
	
	/**
	 * <p>대량메일 실패 도메인별 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticFailDomainBar(Map<String, Object> searchMap){		
		
		String result = "";
		
		try{
			List<MassMailStatisticDomainFail> massMailStatisticList  = massMailStatDAO.massMailStatisticFailDomain(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatisticDomainFail massMailStatistic  : massMailStatisticList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getDomainName());
				rsMap.put("temparyErrorCount", massMailStatistic.getFailcauseType1Count());			//1: 일시적인 오류 카운트 
				rsMap.put("persistantErrorCount", massMailStatistic.getFailcauseType2Count());			//2: 영구적인 오류 카운트
				rsMap.put("etcSMTPErrorCount", massMailStatistic.getFailcauseType3Count());			//3: 기타 SMTP오류 카운트
				rsMap.put("systemErrorCount", massMailStatistic.getFailcauseType4Count());				//4: 시스템내부 오류 카운트
				rsMap.put("undefindErrorCount", massMailStatistic.getFailcauseType5Count());			//5: 불확실한 오류 카운트
				rsMap.put("failCount", massMailStatistic.getFailCount());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailFailDomainBar(statisticArrayList);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;
		
	
	}
	
	/**
	 * <p>대량메일 실패도메인별  Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticFailDomainPie(Map<String, Object> searchMap){
		
		
		String result = "";
		String key = (String)searchMap.get("key");
		try{
			List<MassMailStatisticDomainFail> massMailStatisticList  = massMailStatDAO.massMailStatisticFailDomain(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatisticDomainFail massMailStatistic  : massMailStatisticList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getDomainName());
				rsMap.put("temparyErrorCount", massMailStatistic.getFailcauseType1Count());			//1: 일시적인 오류 카운트 
				rsMap.put("persistantErrorCount", massMailStatistic.getFailcauseType2Count());			//2: 영구적인 오류 카운트
				rsMap.put("etcSMTPErrorCount", massMailStatistic.getFailcauseType3Count());			//3: 기타 SMTP오류 카운트
				rsMap.put("systemErrorCount", massMailStatistic.getFailcauseType4Count());				//4: 시스템내부 오류 카운트
				rsMap.put("undefindErrorCount", massMailStatistic.getFailcauseType5Count());			//5: 불확실한 오류 카운트
				rsMap.put("failCount", massMailStatistic.getFailCount());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailFailDomainPie(statisticArrayList,key);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;

	}
	/**
	 * <p>대량메일 실패원인별 통계 리스트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticFailCause> massMailStatisticFailCauseList(Map<String, Object> searchMap){
		List<MassMailStatisticFailCause> result = null;
		try{
			result = massMailStatDAO.massMailStatisticFailCause(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
	}
	
	/**
	 * <p>대량메일 실패원인별 통계 Bar 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticFailCauseBar(Map<String, Object> searchMap){
		
		String result = "";
		
		try{
			List<MassMailStatisticFailCause> statisticFailCauseList = massMailStatDAO.massMailStatisticFailCause(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatisticFailCause massMailStatistic  : statisticFailCauseList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getFailCauseType());
				rsMap.put("failCount", massMailStatistic.getFailCount());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailFailCauseBar(statisticArrayList);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;

	}
	/**
	 * <p>대량메일 실패원인별 통계 Pie 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticFailCausePie(Map<String, Object> searchMap){
		
		String result = "";
		
		try{
			List<MassMailStatisticFailCause> statisticFailCauseList = massMailStatDAO.massMailStatisticFailCause(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatisticFailCause massMailStatistic  : statisticFailCauseList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getFailCauseDes());
				rsMap.put("failCount", massMailStatistic.getFailCount());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailFailCausePie(statisticArrayList);
			
		}catch(Exception e){
			result="";
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
	public int totalMassMailPersonPreview(Map<String, Object> searchMap){
		int result = 0;
		try{
			String backupYearMonth = massMailStatDAO.getBackupYearMonth(searchMap); 
			result = massMailStatDAO.totalMassMailPersonPreview(searchMap, backupYearMonth);
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
	public List<MassMailPersonPreview> personPreview(Map<String, Object> searchMap){
		List<MassMailPersonPreview> result = null;
		try{
			String backupYearMonth = massMailStatDAO.getBackupYearMonth(searchMap); 
			result = massMailStatDAO.massMailPersonPreview(searchMap, backupYearMonth);
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
	}
	
	/**
	 * <p>대량메일 월간 보고서 총괄현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailReportMonth massMailReportMonthTotalInfo(String year, String month, String dateFrom, String dateTo, String userID, String groupID, String[] userInfo){
		MassMailReportMonth massMailReportMonthTotalInfo = null;
		try{
			massMailReportMonthTotalInfo = massMailStatDAO.massMailReportMonthTotalInfo(year, month, dateFrom, dateTo, userID, groupID, userInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return massMailReportMonthTotalInfo;
	}
	/**
	 * <p>대량메일 월간 보고서 총괄현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailReportMonth massMailReportMonthSendInfo(String year, String month, String dateFrom, String dateTo, String userID, String groupID, String[] userInfo){
		MassMailReportMonth massMailReportMonthTotalInfo = null;
		try{
			massMailReportMonthTotalInfo = massMailStatDAO.massMailReportMonthSendInfo(year, month, dateFrom, dateTo, userID, groupID, userInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return massMailReportMonthTotalInfo;
	}
	/**
	 * <p>대량메일 월간 보고서 - Domain별 통계 리스트
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailReportMonth> massmailReportMonthDomainStatisticList(String year, String month, String dateFrom, String dateTo, String userID, String groupID ){
		List<MassMailReportMonth> result = null;
		try{
			result = massMailStatDAO.massMailReportMonthDomainSendList(year, month, dateFrom, dateTo, userID, groupID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 월간 보고서 - Domain별 차트 (사용 안함, 확인되면 삭제 할 예정)
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailReportMonthDomainStatisticBar(String year, String month, String dateFrom, String dateTo, String userID, String groupID ){
		//List<MassMailReportMonth> result = null;
		String result="";
		try{
			List<MassMailReportMonth>massMailReportMonthList = massMailStatDAO.massMailReportMonthDomainSendList(year, month, dateFrom, dateTo, userID, groupID);
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailReportMonth massMailReportMonth : massMailReportMonthList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailReportMonth.getStandard());
				rsMap.put("successTotal",massMailReportMonth.getSuccessTotal());
				rsMap.put("failTotal", massMailReportMonth.getFailTotal());
				rsMap.put("openTotal", massMailReportMonth.getOpenTotal());
				rsMap.put("clickTotal", massMailReportMonth.getClickTotal());
				rsMap.put("rejectTotal", massMailReportMonth.getRejectcallTotal());
				float successRatio = (float) massMailReportMonth.getSuccessTotal() / massMailReportMonth.getSendTotal() * 100;
				rsMap.put("successRatio", successRatio);
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllBar(statisticArrayList);
			
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 월간 보고서 - 시간대별 Email 발송 통계 리스트
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailReportMonth> massmailReportMonthTimeStatisticList(String year, String month, String dateFrom, String dateTo, String userID, String groupID){
		List<MassMailReportMonth> result = null;
		try{
			List<MassMailReportMonth>  massMailReportMonthList= massMailStatDAO.massMailReportMonthTimeSendList(year, month, dateFrom, dateTo, userID, groupID);
			// 맵에 담는다.
			Map<Integer, MassMailReportMonth> map = new HashMap<Integer,  MassMailReportMonth>(); 
			for(MassMailReportMonth massMailReportMonth : massMailReportMonthList ){
				map.put(new Integer(massMailReportMonth.getStandard()),massMailReportMonth);
			}
			massMailReportMonthList.clear();
			for(int i = 0; i < 24; i ++) {
				MassMailReportMonth tempMassMailReportMonth = new MassMailReportMonth();
				tempMassMailReportMonth.setStandard(i+"");
				
				if(map.containsKey(i)) {	
					tempMassMailReportMonth.setSendTotal(map.get(i).getSendTotal());
					tempMassMailReportMonth.setSuccessTotal(map.get(i).getSuccessTotal());
					tempMassMailReportMonth.setFailTotal(map.get(i).getFailTotal());
					tempMassMailReportMonth.setOpenTotal(map.get(i).getOpenTotal());
					tempMassMailReportMonth.setClickTotal(map.get(i).getClickTotal());
					tempMassMailReportMonth.setRejectcallTotal(map.get(i).getRejectcallTotal());
				}else{
					tempMassMailReportMonth.setSendTotal(0);
					tempMassMailReportMonth.setSuccessTotal(0);
					tempMassMailReportMonth.setFailTotal(0);
					tempMassMailReportMonth.setOpenTotal(0);
					tempMassMailReportMonth.setClickTotal(0);
					tempMassMailReportMonth.setRejectcallTotal(0);
				}
				massMailReportMonthList.add(i, tempMassMailReportMonth);
			}
			result = massMailReportMonthList;
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 월간 보고서 - 시간대별 Email 발송 현황 (사용 안함, 확인되면 삭제 할 예정)
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailReportMonthTimeStatisticBar(String year, String month, String dateFrom, String dateTo, String userID, String groupID){
		//List<MassMailReportMonth> result = null;
		String result="";
		try{
			List<MassMailReportMonth> massMailReportMonthList = massMailStatDAO.massMailReportMonthTimeSendList(year, month, dateFrom, dateTo, userID, groupID);

			// 맵에 담는다.
			Map<Integer, MassMailReportMonth> map = new HashMap<Integer, MassMailReportMonth>(); 
			for(MassMailReportMonth massmailStatistic : massMailReportMonthList ){
				map.put(new Integer(massmailStatistic.getStandard()),massmailStatistic);
			}
			
			//차트를 만든다. 
			//result = ChartUtil.createMailChartAllBar(statisticArrayList);
			result = ChartUtil.createMailMonthChartTimeBar(map);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MassMailList> massMailReportMonthList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap){
		 List<MassMailList> result = null;
		 try{
			 result = massMailStatDAO.massMailReportMonthList(userInfo, currentPage, countPerPage, searchMap);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 */
	public int totalCountMassMailReportMonthList(String[] userInfo, Map<String, String> searchMap){
		int result = 0;
		try{
			result = massMailStatDAO.totalCountMassMailReportMonthList(userInfo, searchMap);
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
	 */
	public List<MassMailStatisticUsers> massmailStatisticUsersList(String[] userInfo, Map<String, String> searchMap){
		List<MassMailStatisticUsers> result = null;
		try{
			result = massMailStatDAO.massmailStatisticUsersList(userInfo, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>계정별 통계합계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 */
	public List<MassMailStatisticUsers> massmailStatisticUsersHap(String year, String month){
		List<MassMailStatisticUsers> result = null;
		try{
			result = massMailStatDAO.massmailStatisticUsersHap(year, month);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 계정별 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailStatisticUsersBar(String[] userInfo, Map<String, String> searchMap){
	
		String result = "";		
		try{
			List<MassMailStatisticUsers> massMailStatisticUsersList = massMailStatDAO.massmailStatisticUsersList(userInfo, searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatisticUsers massMailStatistic  : massMailStatisticUsersList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getUserID());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllBar(statisticArrayList);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 계정별 Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailStatisticUsersPie(String[] userInfo, Map<String, String> searchMap){
		String result = "";		
		String key = "all";
		try{
			List<MassMailStatisticUsers> massMailStatisticUsersList = massMailStatDAO.massmailStatisticUsersList(userInfo, searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatisticUsers massMailStatistic  : massMailStatisticUsersList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getUserID());
				rsMap.put("sendTotal", massMailStatistic.getSendTotal());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
								
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllPie(statisticArrayList, key);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>그룹별 통계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 */
	public List<MassMailStatisticUsers> massmailStatisticUsersGroupList(String year, String month){
		List<MassMailStatisticUsers> result = null;
		try{
			result = massMailStatDAO.massmailStatisticUsersGroupList(year, month);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>그룹별 통계합계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 */
	public List<MassMailStatisticUsers> massmailStatisticUsersGroupHap(String year, String month){
		List<MassMailStatisticUsers> result = null;
		try{
			result = massMailStatDAO.massmailStatisticUsersGroupHap(year, month);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p> 초기화면 - 관심 도메인 정보
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticDomain> massmailStatisticConcernedDomain(){
		List<MassMailStatisticDomain> result = null;
		try{
			result = massMailStatDAO.massmailStatisticConcernedDomain();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	/**
	 * <p>대량메일 그룹별 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailStatisticUsersGroupBar(Map<String, Object> searchMap){

		String result = "";		
		try{
			List<MassMailStatisticUsers> massMailStatisticUsersList = massMailStatDAO.massmailStatisticUsersGroupList((String)searchMap.get("year"), (String)searchMap.get("month"));
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatisticUsers massMailStatistic  : massMailStatisticUsersList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getUserID());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllBar(statisticArrayList);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>대량메일 그룹별 Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailStatisticUsersGroupPie(Map<String, Object> searchMap){
		String result = "";		
		String key = (String)searchMap.get("key");
		try{
			List<MassMailStatisticUsers> massMailStatisticUsersList = massMailStatDAO.massmailStatisticUsersGroupList((String)searchMap.get("year"), (String)searchMap.get("month"));
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatisticUsers massMailStatistic  : massMailStatisticUsersList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getUserID());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllPie(statisticArrayList, key);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>재발송을 요청한다. 
	 * @param massmailID, scheduleID, failCauseCode
	 * @return
	 * @throws DataAccessException
	 */
	public int retryMail(int massmailID, int scheduleID, String[] failCauseCodes){
		int result1 = 0, result2 = 0;
		try{
			for(int i=0;i<failCauseCodes.length;i++){
				result1 += massMailStatDAO.updateRetrySended(massmailID, scheduleID, failCauseCodes[i]);
			}
			result2 = massMailStatDAO.updateRetryFinishYN(massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result1*result2;
	}
	/**
	 * <p>선택한 실페 도메인 그룹의 재발송을 요청한다. 
	 * @param massmailID, scheduleID, failCauseCode
	 * @return
	 * @throws DataAccessException
	 */
	public int retryDomain(int massmailID, int scheduleID, String[] domains){
		int result1 = 0, result2 = 0;
		try{
			for(int i=0;i<domains.length;i++){
				result1 += massMailStatDAO.updateRetrySendedDomain(massmailID, scheduleID, domains[i]);
			}
			result2 = massMailStatDAO.updateRetryFinishYN(massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result1*result2;
	}
	/**
	 * <p><p>설문에 미응답한 대상자를 재발송한다.
	 * @param massmailID, scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int retryPoll(int massmailID, int scheduleID){
		int result1 = 0, result2 = 0;
		try{
			result1 = massMailStatDAO.updateRetrySendedPoil(massmailID, scheduleID);
			result2 = massMailStatDAO.updateRetryFinishYN(massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result1*result2;
	}
	/**
	 * <p>설문통계 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollStatistic(int pollID, int massmailID, int scheduleID){
		List<MassMailStatisticPoll> result = null;
		try{
			result = massMailStatDAO.selectPollStatistic(pollID, massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일에 해당하는 설문 아이디 가져오기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int getPollIDByMassMail(int massmailID){
		int result = 0;
		try{
			result = massMailStatDAO.getPollIDByMassMail(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
	
	/**
	 * <p>설문응답자 보기 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param questionID
	 * @param exampleID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailPersonPreview> massmailPollPreview(int currentPage, int countPerPage, Map<String, Object> searchMap){
		List<MassMailPersonPreview> result = null;
		try{
			result = massMailStatDAO.massmailPollPreview(currentPage, countPerPage, searchMap);
			
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
	
	/**
	 * <p>설문응답자 수 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int massmailPollPreviewTotalCount(Map<String, Object> searchMap){
		int result = 0;
		try{
			result = massMailStatDAO.massmailPollPreviewTotalCount(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
	
	/**
	 * <p>설문통계
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> massmailPollStatisticCount(int massmailID, int scheduleID, int pollID){
		Map<String,Object>  result = null;
		try{
			result = massMailStatDAO.massmailPollStatisticCount(massmailID, scheduleID, pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문통계(통계수집완료시)
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> massmailPollStatisticCountFinish(String yearMonth, int massmailID, int scheduleID, int pollID){
		Map<String,Object>  result = null;
		try{
			result = massMailStatDAO.massmailPollStatisticCountFinish(yearMonth, massmailID, scheduleID, pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량메일 결과 백업 테이블 정보 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String getBackupYearMonth(Map<String, Object> searchMap){
		String  result = "";
		try{
			result = massMailStatDAO.getBackupYearMonth(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문모든 응답자들 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailPersonPreview> massmailPollAllResponse(int currentPage, int countPerPage, Map<String, Object> searchMap){
		List<MassMailPersonPreview> result = null;
		try{
			result = massMailStatDAO.massmailPollAllResponse(currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>설문모든 미응답자들 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailPersonPreview> massmailPollAllNotResponse(int currentPage, int countPerPage, Map<String, Object> searchMap, String backupYearMonth){
		List<MassMailPersonPreview> result = null;
		try{
			result = massMailStatDAO.massmailPollAllNotResponse(currentPage, countPerPage, searchMap, backupYearMonth);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>설문전체응답자 수 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int massmailPollAllResponseTotalCount(Map<String, Object> searchMap) throws DataAccessException{
		int result = 0;
		try{
			result = massMailStatDAO.massmailPollAllResponseTotalCount(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문응답상세보기 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassMailStatisticPoll> viewDetailAnswer(int pollID, int massmailID, int scheduleID, int sendID, String matrixX, String matrixY){
		 List<MassMailStatisticPoll> result = null;
		try{
			result = massMailStatDAO.viewDetailAnswer(pollID, massmailID, scheduleID, sendID, matrixX, matrixY);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문에 해당하는 설문 문항들을 가져온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollQuestion> selectQuestionByPollID(int pollID){
		 List<PollQuestion> result = null;
			try{
				result = massMailStatDAO.selectQuestionByPollID(pollID);
			}catch(Exception e){
				logger.error(e);
			}
			return result;
	}
	
	/**
	 * <p>해당 설문ID에 해당하는 보기와 응답수를 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollStatisticByQuestionID(int massmailID, int scheduleID, int pollID, int questionID){
		 List<MassMailStatisticPoll> result = null;
			try{
				result = massMailStatDAO.selectPollStatisticByQuestionID(massmailID, scheduleID, pollID, questionID);
			}catch(Exception e){
				logger.error(e);
			}
			return result;
	}
	
	/**
	 * <p>순위선택 응답수를 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollStatisticByExampleType3(int massmailID, int scheduleID, int pollID, int questionID, int selectCount){
		 List<MassMailStatisticPoll> result = null;
		 try{
			 result = massMailStatDAO.selectPollStatisticByExampleType3(massmailID, scheduleID, pollID, questionID, selectCount);
			
		 }catch(Exception e){
				logger.error(e);
		 }
		 return result;
	 }
	/**
	 * <p>세로(Y축) 보기를 가져온다.
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollExampleMatrixY(int pollID, int questionID){
		List<MassMailStatisticPoll> result = null;
		try{
			result = massMailStatDAO.selectPollExampleMatrixY(pollID, questionID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>매트릭스 응답통계를 가져온다.
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollAnswerMatrixXY(int massmailID, int scheduleID, int pollID, int questionID){
		List<MassMailStatisticPoll> result = null;
		try{
			result = massMailStatDAO.selectPollAnswerMatrixXY(massmailID, scheduleID, pollID, questionID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문에 해당하는 문항정보보기 
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailStatisticPoll viewQuestion(int pollID, int questionID){
		MassMailStatisticPoll result = null;
		try{
			result = massMailStatDAO.viewQuestion(pollID, questionID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문 개인별 응답 현황 
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollIndividualStatistic(int massmailID, int scheduleID){
		List<MassMailStatisticPoll> result = null;
		try{
			Map<String, Object> searchMap = new HashMap<String, Object>(); 
			searchMap.put("massmailID", massmailID);
			searchMap.put("scheduleID", scheduleID);
			
			String backupYearMonth = massMailStatDAO.getBackupYearMonth(searchMap); 
			result = massMailStatDAO.selectPollIndividualStatistic( massmailID, scheduleID, backupYearMonth);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 대상자별 통계 리스트 
	 * @param searchMap
	 * @return
	 */
	public List<MassMailStatistic> statisticTargetList(Map<String, Object> searchMap){
			List<MassMailStatistic> result = null;
			try{
				result = massMailStatDAO.massMailStatisticTarget(searchMap);
			}catch(Exception e){
				logger.error(e);
			}
			return result;
	}
	
	/**
	 * <p>대량메일 대상자별 Bar 차트
	 * @param searchMap
	 * @return
	 */

	public String statisticTargetBar(Map<String, Object> searchMap){
		
		String result = "";
		
		try{
			List<MassMailStatistic> massMailStatisticList = massMailStatDAO.massMailStatisticTarget(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatistic massMailStatistic  : massMailStatisticList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getStandard());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllBar(statisticArrayList);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;
		
	
		
	}
	
	/**
	 * <p>대량메일 대상자별 Pie 차트
	 * @param searchMap
	 * @return
	 */
	public String statisticTargetPie(Map<String, Object> searchMap){		
		
		String result = "";
		String key = (String)searchMap.get("key");
		
		try{
			List<MassMailStatistic> massMailStatisticList = massMailStatDAO.massMailStatisticTarget(searchMap);
			
			//차트에 넘기기 위한 배열
			ArrayList<Map<String,Object>> statisticArrayList = new ArrayList<Map<String,Object>>();
			
			for(MassMailStatistic massMailStatistic  : massMailStatisticList){
				Map<String,Object> rsMap = new HashMap<String, Object>();
				rsMap.put("standard", massMailStatistic.getStandard());
				rsMap.put("sendTotal", massMailStatistic.getSendTotal());
				rsMap.put("successTotal", massMailStatistic.getSuccessTotal());
				rsMap.put("failTotal", massMailStatistic.getFailTotal());
				rsMap.put("openTotal", massMailStatistic.getOpenTotal());
				rsMap.put("clickTotal", massMailStatistic.getClickTotal());
				rsMap.put("rejectTotal", massMailStatistic.getRejectcallTotal());
				rsMap.put("successRatio", massMailStatistic.getSuccessRatio());
				
				statisticArrayList.add(rsMap);
			}
			
			//차트를 만든다. 
			result = ChartUtil.createMailChartAllPie(statisticArrayList, key);
			
		}catch(Exception e){
			result="";
			logger.error(e);
		}
		return result;	

	}
	
	/**
	 * <p>타겟ID에 해당되는 원투원 정보 
	 * @param targetIDs
	 * @return
	 */
	public List<OnetooneTarget> selectOnetooneByTargetID(String[] targetIDs, int massmailID){
		List<OnetooneTarget> result = null;
		try{
			result =  massMailStatDAO.selectOnetooneByTargetID(targetIDs,massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 대량메일에ID에 해당되는 타겟ID 
	 * @param massmailID
	 * @return
	 */
	public String[] getTargetIDs(int massmailID){
		String[] result = null;
		try{
			List<String> resultLists =  massMailStatDAO.getTargetIDs(massmailID);
			result = new String[resultLists.size()];
			for(int i=0;i<resultLists.size();i++)
			{
				result[i] = resultLists.get(i);
			}
			
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>설문통계( 해당 메일에 사용 된 설문의 종료기준, 목표응답수, 설문마감일 표시) 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> selectPollInfo(int pollID){
		Map<String, Object> result=null;
		try{
			result = massMailStatDAO.selectPollInfo(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>해당 설문조사 타이틀
	 * @param searchMap
	 * @return
	 */
	public String selectPollTitle(int pollID){
		String  result = "";
		try{
			result = massMailStatDAO.selectPollTitle(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>각 문항당 보기의 갯 수 
	 * @param pollID, questionID
	 * @return 
	 */
	public int exampleCount(int pollID, int questionID){
		int result = 0;
		try{
			result = massMailStatDAO.exampleCount(pollID, questionID);
		}catch(Exception e){
			logger.error(e);
		}		
		return result;
	}
	
	/**
	 * <p>문항 별 보기 내용
	 * @param pollID
	 * @param questionID
	 * @param exampleID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String selectExampleDesc(int pollID,int questionID,int exampleID){
		String  result = "";
		try{
			result = massMailStatDAO.selectExampleDesc(pollID, questionID, exampleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>주관식 답안 목록 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> responseTextAll(int pollID, int massmailID, int scheduleID, int questionID){
		List<MassMailStatisticPoll> result = null;
		try{
			result = massMailStatDAO.responseTextAll(pollID, massmailID, scheduleID, questionID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>객관식 문항  주관식 답안 갯수(문제마다 포함된 갯수) 
	 * @param pollID, questionID
	 * @return 
	 */
	public int exampleExYNcount(int pollID, int questionID){
		int result = 0;
		try{
			result = massMailStatDAO.exampleExYNcount(pollID, questionID);
		}catch(Exception e){
			logger.error(e);
		}		
		return result;
	}
	
	
	/**
	 * <p>객관식 문항  주관식 답안 갯수(설문내에 포함된 갯수) 
	 * @param pollID
	 * @return 
	 * @throws DataAccessException
	 */
	public int pollExampleExYNcount (int pollID){
		int result = 0;
		try{
			result = massMailStatDAO.pollExampleExYNcount(pollID);
		}catch(Exception e){
			logger.error(e);
		}		
		return result;
	}	
	
	/**
	 * <p>설문 내 주관식 문항 갯 수
	 * @param pollID
	 * @return 
	 * @throws DataAccessException
	 */
	public int exampleTypeCount (int pollID){
		int result = 0;
		try{
			result = massMailStatDAO.exampleTypeCount(pollID);
		}catch(Exception e){
			logger.error(e);
		}		
		return result;
	}
	
	/**
	 * <p>설문 내 보기 총 갯 수(셀 총 갯 수 - 여백설정을 위한 카운트)
	 * @param pollID
	 * @return 
	 * @throws DataAccessException
	 */
	public int exampleAllCount (int pollID){
		int result = 0;
		try{
			result = massMailStatDAO.exampleAllCount(pollID);
		}catch(Exception e){
			logger.error(e);
		}		
		return result;
	}
	
	
	/**
	 * <p>설문전체 미응답자 수 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int massmailPollAllNotResponseTotalCount(Map<String, Object> searchMap, String backupYearMonth){
		int result = 0;
		try{
			result = massMailStatDAO.massmailPollAllNotResponseTotalCount(searchMap, backupYearMonth);
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
}
