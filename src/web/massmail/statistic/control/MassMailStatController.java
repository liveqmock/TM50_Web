package web.massmail.statistic.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import sun.print.resources.serviceui;

import web.common.util.*;
import web.massmail.statistic.service.MassMailStatService;
import java.util.HashMap;
import java.util.Map;

import java.util.List;
import web.massmail.statistic.model.*;
import web.massmail.write.model.OnetooneTarget;




public class MassMailStatController extends MultiActionController{
	

	private MassMailStatService massMailStatService= null;
	private String sCurPage = null;
	private String message = "";

	
	public void setMassMailStatService(MassMailStatService massMailStatService){		
		this.massMailStatService = massMailStatService;
	}


	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		String step = ServletUtil.getParamString(req, "step");
		String pageURL = "";
		if(step.equals("month")){
			pageURL = "/pages/massmail/statistic/massmail_report_month.jsp";
		}else if(step.equals("users")){
			pageURL = "/pages/massmail/statistic/massmail_statistic_users.jsp";
		}else if(step.equals("schedule")){
			pageURL = "/pages/massmail/statistic/massmail_schedule.jsp";
		}
		return new ModelAndView(pageURL);
	}
		
	
	/**
	 * <p>월간보고서 기본 통계 정보 + 발송 정보 
	 * @param req
	 * 
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massmailReportMonthBasic(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		String dateFrom = ServletUtil.getParamString(req, "sDateFrom");
		String dateTo = ServletUtil.getParamString(req, "sDateTo");
		String userID = ServletUtil.getParamString(req, "sUserID");
		String groupID = ServletUtil.getParamString(req, "sGroupID");
		
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		if(year.equals("")){
			year = DateUtils.getStrYear();
		}
		if(month.equals("")){
			month = DateUtils.getStrMonth();
		}
		if(dateFrom.length()==1)
			dateFrom = "0"+dateFrom;
		if(dateTo.length()==1)
			dateTo = "0"+dateTo;
		if(month.length()==1)
			month = "0"+month;
		try{
			if(!(DateUtils.isValid(year+"-"+month+"-"+dateTo, "yyyy-MM-dd"))){
				dateTo = String.valueOf(DateUtils.lastDay(Integer.parseInt(year),Integer.parseInt(month)));
			}
			if(!(DateUtils.isValid(year+"-"+month+"-"+dateFrom, "yyyy-MM-dd"))){
				dateFrom = "1";
			}
		}catch(Exception e){
		
		}
		
		MassMailReportMonth massMailReportMonthTotalInfo = massMailStatService.massMailReportMonthTotalInfo(year, month, dateFrom, dateTo, userID, groupID, userInfo);
		req.setAttribute("reportMonthSendInfo", massMailStatService.massMailReportMonthSendInfo(year, month, dateFrom, dateTo, userID, groupID, userInfo));
		req.setAttribute("year", year);
		req.setAttribute("month", month);
		req.setAttribute("dateFrom", dateFrom);
		req.setAttribute("dateTo", dateTo);
		return new ModelAndView("/pages/massmail/statistic/massmail_report_month_proc.jsp?method=basicinfo","reportMonthTotalInfo", massMailReportMonthTotalInfo);
	}
	
	/**
	 * <p>월간보고서 - 도메인별 Bar 차트 (사용 안함, 확인되면 삭제 할 예정)
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void massmailReportMonthDomainStatisticBar(HttpServletRequest req, HttpServletResponse res) throws Exception{

		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		String dateFrom = ServletUtil.getParamString(req, "sDateFrom");
		String dateTo = ServletUtil.getParamString(req, "sDateTo");
		String userID= ServletUtil.getParamString(req, "sUserID");
		String groupID = ServletUtil.getParamString(req, "sGroupID");
		if(year.equals("")){
			year = DateUtils.getStrYear();
		}
		if(month.equals("")){
			month = DateUtils.getStrMonth();
		}
		
		
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.massmailReportMonthDomainStatisticBar(year, month, dateFrom, dateTo, userID, groupID) );
	}
	
	/**
	 * <p>월간보고서 - 시간대별 Bar 차트 (사용 안함, 확인되면 삭제 할 예정)
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void massmailReportMonthTimeStatisticBar(HttpServletRequest req, HttpServletResponse res) throws Exception{

		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		String dateFrom = ServletUtil.getParamString(req, "sDateFrom");
		String dateTo = ServletUtil.getParamString(req, "sDateTo");
		String userID= ServletUtil.getParamString(req, "sUserID");
		String groupID = ServletUtil.getParamString(req, "sGroupID");
		if(year.equals("")){
			year = DateUtils.getStrYear();
		}
		if(month.equals("")){
			month = DateUtils.getStrMonth(); 
		}
		
		
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.massmailReportMonthTimeStatisticBar(year, month, dateFrom, dateTo, userID, groupID) );
	}
	
	/**
	 * <p>월간보고서 상태별 메일 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massmailReportMonthList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
	
		if (curPage <= 0) curPage = 1;
		int iLineCnt = 5;	// 라인 갯수
		
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		String dateFrom = ServletUtil.getParamString(req, "sDateFrom");
		String dateTo = ServletUtil.getParamString(req, "sDateTo");
		String state = ServletUtil.getParamString(req, "sState");
		String userID= ServletUtil.getParamString(req, "sUserID");
		String groupID = ServletUtil.getParamString(req, "sGroupID");
		
		if(dateFrom.length()==1)
			dateFrom = "0"+dateFrom;
		if(dateTo.length()==1)
			dateTo = "0"+dateTo;
		if(month.length()==1)
			month = "0"+month;
		try{
			if(!(DateUtils.isValid(year+"-"+month+"-"+dateTo, "yyyy-MM-dd"))){
				dateTo = String.valueOf(DateUtils.lastDay(Integer.parseInt(year),Integer.parseInt(month)));
			}
			if(!(DateUtils.isValid(year+"-"+month+"-"+dateFrom, "yyyy-MM-dd"))){
				dateFrom = "1";
			}
		}catch(Exception e){
		
		}
		
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("dateFrom", dateFrom);
		searchMap.put("dateTo", dateTo);
		searchMap.put("state", state);
		searchMap.put("userID", userID);
		searchMap.put("groupID", groupID);
		
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		//총카운트 		
		int totalCount =massMailStatService.totalCountMassMailReportMonthList(userInfo, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		if(dateFrom.length()==2&&dateFrom.startsWith("0"))
			dateFrom = dateFrom.substring(1);
		if(dateTo.length()==2&&dateTo.startsWith("0"))
			dateTo = dateTo.substring(1);
		req.setAttribute("dateFrom", dateFrom);
		req.setAttribute("dateTo", dateTo);
			
		return new ModelAndView("/pages/massmail/statistic/massmail_report_month_proc.jsp?method=list","massmailWriteList", massMailStatService.massMailReportMonthList(userInfo, curPage, iLineCnt, searchMap));

	}
	
	/**
	 * <p>대량메일 기본정보
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massMailStatisticBasicInfo(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req,"massmailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		req.setAttribute("targetGroupList", massMailStatService.listTargetingGroup(massmailID));
		req.setAttribute("massmailSendInfo",massMailStatService.massMailStatisticSendInfo(massmailID,scheduleID));
		req.setAttribute("massmailInfo",massMailStatService.massMailStatisticBasicInfo(massmailID,scheduleID));
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticbasic","massmailFilter",massMailStatService.massMailStatisticFilterInfo(massmailID, scheduleID));
	}
	

	/**
	 * <p>대량메일 링크 분석 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticLinkList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticlinklist","statisticList",  massMailStatService.statisticLinkList(searchMap));
	}
	
	/**
	 * <p>대량메일 링크 분석 Bar 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticLinkBar(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticLinkBar(searchMap) );
	}
	
	/**
	 * <p>대량메일 링크 분석 Pie 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticLinkPie(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String key =  ServletUtil.getParamString(req, "key");
		
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("key", key);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticLinkPie(searchMap) );
	}
	
	/**
	 * <p>대량메일 시간별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticHourlyList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticlist&type=none","statisticList",  massMailStatService.statisticHourlyList(searchMap));
	}
	
	/**
	 * <p>대량메일 시간별 통계 Bar 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticHourlyBar(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticHourlyBar(searchMap) );
	}
	
	/**
	 * <p>대량메일 시간별 통계 Pie 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticHourlyPie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String key =  ServletUtil.getParamString(req, "key");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("key", key);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticHourlyPie(searchMap) );
	}
	
	/**
	 * <p>대량메일 일자별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticDailyList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
	
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticlist&type=none","statisticList",  massMailStatService.statisticDailyList(searchMap));
	}
	
	/**
	 * <p>대량메일 일자별 통계 Bar 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticDailyBar(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
	
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticDailyBar(searchMap) );
	}
	
	/**
	 * <p>대량메일 일자별 통계 Pie 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticDailyPie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String key =  ServletUtil.getParamString(req, "key");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("key", key);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticDailyPie(searchMap) );
	}
	
	/**
	 * <p>대량메일 도메인별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticDomainList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticlist&type=none","statisticList",  massMailStatService.statisticDomainList(searchMap));
	}
	
	/**
	 * <p>대량메일 도메인별 통계 Bar 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticDomainBar(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");

		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticDomainBar(searchMap) );
	}
	
	/**
	 * <p>대량메일 도메인별 통계 Pie 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticDomainPie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String key =  ServletUtil.getParamString(req, "key");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("key", key);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticDomainPie(searchMap) );
	}
	
	/**
	 * <p>대량메일 실패 도메인별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticFailDomainList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticfaildomainlist","statisticList",  massMailStatService.statisticFailDomainList(searchMap));
	}
	
	/**
	 * <p>대량메일 실패 도메인별 통계 Bar 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticFaildomainBar(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");

		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticFailDomainBar(searchMap) );
	}
	
	/**
	 * <p>대량메일 실패 도메인별 통계 Pie 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticFaildomainPie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String key =  ServletUtil.getParamString(req, "key");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("key", key);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticFailDomainPie(searchMap) );
	}
	
	/**
	 * <p>대량메일 실패원인별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticFailCauseList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticfailcauselist","statisticList",  massMailStatService.massMailStatisticFailCauseList(searchMap));
	}
	
	/**
	 * <p>대량메일 실패원인별 통계 Bar 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticFailcauseBar(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");

		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticFailCauseBar(searchMap) );
	}
	
	/**
	 * <p>대량메일 실패원인별 통계 Pie 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticFailcausePie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticFailCausePie(searchMap) );
	}
		
	/**
	 * <p>대량메일 대상자 미리보기 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView personPreview(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
	
		if (curPage <= 0) curPage = 1;
		
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}
		
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String standard = ServletUtil.getParamString(req, "standard");
		String type= ServletUtil.getParamString(req, "type");
		String key= ServletUtil.getParamString(req, "key");
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		int iTotalCnt = ServletUtil.getParamInt(req, "iTotalCnt", "0");
		
		String[] targetIDs = massMailStatService.getTargetIDs(massmailID);
		List<OnetooneTarget> onetooneTargets = massMailStatService.selectOnetooneByTargetID(targetIDs,massmailID);
		String[] otos = new String[onetooneTargets.size()];
		for(int i =0; i<onetooneTargets.size(); i++ )
			otos[i] = onetooneTargets.get(i).getOnetooneAlias();
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("curPage", curPage);
		searchMap.put("iLineCnt", iLineCnt);
		searchMap.put("iTotalCnt", iTotalCnt);
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("standard", standard);
		searchMap.put("type", type);
		searchMap.put("key", key);
		
		int realTotalCnt = massMailStatService.totalMassMailPersonPreview(searchMap);
		if(realTotalCnt < iTotalCnt){
			iTotalCnt = realTotalCnt; 
		}
		
		List<MassMailPersonPreview> personPreview = massMailStatService.personPreview(searchMap);
		for(int i =0; i<personPreview.size(); i++ )
		{
			String oneToOne = personPreview.get(i).getOnetooneInfo();
			String temp = "";
			for(int t =0; t<otos.length; t++ )
				temp=temp+ThunderUtil.getOnetooneValueSpace(oneToOne,otos[t])+",";
			personPreview.get(i).setOnetooneInfo(temp);
		}
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("standard",standard);
		req.setAttribute("type",type);
		req.setAttribute("message", this.message);
		req.setAttribute("personPreviewLists",personPreview);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=personpreviewlist","personPreview", personPreview);
	}

	
	
	
	/**
	 * <p>계정별 통계리스트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massmailStatisticUsers(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String year = ServletUtil.getParamString(req, "sSendedYear");
		String month = ServletUtil.getParamString(req, "sSendedMonth");
		String groupID = ServletUtil.getParamString(req, "sGroupID");
		String userID= ServletUtil.getParamString(req, "sUserID");
		if(year.equals("")){
			year = DateUtils.getStrYear();
		}
		if(month.equals("")){
			month = DateUtils.getStrMonth();
		}
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("groupID", groupID);
		searchMap.put("userID", userID);
		
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_users_proc.jsp?method=massmailStatisticUsersList","massmailStatUsersList", massMailStatService.massmailStatisticUsersList(userInfo, searchMap));
	}
	
	
	/**
	 * <p>계정그룹 바차트
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void massmailStatisticUsersBar(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String year = ServletUtil.getParamString(req, "year");
		String month = ServletUtil.getParamString(req, "month");
		String groupID = ServletUtil.getParamString(req, "groupID");
		String userID= ServletUtil.getParamString(req, "sUserID");

		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("groupID", groupID);
		searchMap.put("userID", userID);
		
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.massmailStatisticUsersBar(userInfo, searchMap));
	}
	
	/**
	 * <p>계정그룹 파이차트
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void massmailStatisticUsersPie(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String year = ServletUtil.getParamString(req, "year");
		String month = ServletUtil.getParamString(req, "month");
		String groupID = ServletUtil.getParamString(req, "sGroupID");
		String userID= ServletUtil.getParamString(req, "sUserID");
	
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("groupID", groupID);
		searchMap.put("userID", userID);
		
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.massmailStatisticUsersPie(userInfo, searchMap));
	}
	
	
	/**
	 * <p>계정별 통계리스트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massmailStatisticUsersGroup(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String year = ServletUtil.getParamString(req, "year");
		String month = ServletUtil.getParamString(req, "month");
		
		if(year.equals("")){
			year = DateUtils.getStrYear();
		}
		if(month.equals("")){
			month = DateUtils.getStrMonth();
		}
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_users_proc.jsp?method=massmailStatisticUsersGroupList","massmailStatUserGroupList", massMailStatService.massmailStatisticUsersGroupList(year, month));
	}
	
	
	/**
	 * <p>계정그룹 바차트
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void massmailStatisticUsersGroupBar(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String year = ServletUtil.getParamString(req, "year");
		String month = ServletUtil.getParamString(req, "month");

		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("year", year);
		searchMap.put("month", month);		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.massmailStatisticUsersGroupBar(searchMap));
	}
	
	/**
	 * <p>계정그룹 파이차트
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void massmailStatisticUsersGroupPie(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String year = ServletUtil.getParamString(req, "year");
		String month = ServletUtil.getParamString(req, "month");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("year", year);
		searchMap.put("month", month);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.massmailStatisticUsersGroupPie(searchMap));
	}

	/**
	 * <p>선택한 실패원인 그룹을 재발송한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView retryMail(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] failCauseCodes =  req.getParameterValues("eFailCauseCode");
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		
		int resultVal = massMailStatService.retryMail(massmailID, scheduleID, failCauseCodes);
	
		ServletUtil.messageGoURL(res,resultVal+"통에 대해 재발송을 요청 하였습니다.","");
		return null;
	}
	
	/**
	 * <p>선택한 실패 도메인 그룹을 재발송한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView retryDomain(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] domains =  req.getParameterValues("eDomain");
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		
		int resultVal = massMailStatService.retryDomain(massmailID, scheduleID, domains);
	
		ServletUtil.messageGoURL(res,resultVal+"통에 대해 재발송을 요청 하였습니다.","");
		return null;
	}
	/**
	 * <p>설문에 미응답한 대상자를 재발송한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView retryPoll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		int resultVal = massMailStatService.retryPoll(massmailID, scheduleID);
	
		ServletUtil.messageGoURL(res,resultVal+"통에 대해 재발송을 요청 하였습니다.","");
		return null;
	}
	/**
	 * <p>설문통계 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticPoll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req,"massmailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		int pollID = ServletUtil.getParamInt(req,"pollID","0");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		String backupYearMonth = massMailStatService.getBackupYearMonth(searchMap);		
		Map<String,Object> massMailStatisticPollList = null;		
		//이미 백업됬어다면 백업테이블로 조회 
		if(backupYearMonth!=null && !backupYearMonth.equals("")){
			massMailStatisticPollList = massMailStatService.massmailPollStatisticCountFinish(backupYearMonth,massmailID, scheduleID, pollID);
		}else{
			massMailStatisticPollList = massMailStatService.massmailPollStatisticCount(massmailID, scheduleID, pollID);
		}
		
		
		//전체발송통수
		int sendTotalCount = 0;
		if(massMailStatisticPollList !=null){
			sendTotalCount = Integer.parseInt(String.valueOf(massMailStatisticPollList.get("sendTotalCount")));
		}
		//전체응답통수 
		int responseCount = 0;
		if(massMailStatisticPollList !=null){
			responseCount = Integer.parseInt(String.valueOf(massMailStatisticPollList.get("responseCount")));
		}
		
		//미응답통수 
		int notresponseCount = 0;
		if(massMailStatisticPollList !=null){
			notresponseCount = sendTotalCount - responseCount;
		}
		
		req.setAttribute("sendTotalCount", Integer.toString(sendTotalCount));
		req.setAttribute("responseCount", Integer.toString(responseCount));
		req.setAttribute("notresponseCount", Integer.toString(notresponseCount));
		
		// 해당 메일에 사용 된 설문의 종료기준, 목표응답수, 설문마감일 표시
		String pollEndDate = "";
		String pollendType = "";
		String aimAnswerCnt = ""; 
		
		Map<String, Object> selectPollInfo = massMailStatService.selectPollInfo(pollID);
		
		if(selectPollInfo != null){
			pollEndDate = String.valueOf(selectPollInfo.get("pollEndDate"));
			pollendType = String.valueOf(selectPollInfo.get("pollendType"));
			aimAnswerCnt = String.valueOf(selectPollInfo.get("aimAnswerCnt"));
		}
		
		req.setAttribute("pollEndDate", pollEndDate);
		req.setAttribute("pollendType", pollendType);
		req.setAttribute("aimAnswerCnt", aimAnswerCnt);
		
		//return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticPoll","massmailStatisticPollList",massMailStatService.selectPollStatistic(pollID, massmailID, scheduleID));
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticPoll","pollQuestionList",massMailStatService.selectQuestionByPollID(pollID));
	}
	
	
	
	/**
	 * <p>설문응답자보기 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massmailPollPreview(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
	
		if (curPage <= 0) curPage = 1;
		
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}
		
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");		
		int pollID = ServletUtil.getParamInt(req, "pollID", "0");
		int questionID = ServletUtil.getParamInt(req, "questionID", "0");
		int exampleID = ServletUtil.getParamInt(req, "exampleID", "0");
		String matrixX = ServletUtil.getParamString(req,"matrixX");
		String matrixY = ServletUtil.getParamString(req,"matrixY");
		int ranking = ServletUtil.getParamInt(req, "ranking", "0");
		

		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("pollID", pollID);
		searchMap.put("questionID", questionID);
		searchMap.put("exampleID", exampleID);
		searchMap.put("matrixX", matrixX);
		searchMap.put("matrixY", matrixY);
		searchMap.put("ranking", ranking);
				
		
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);

		
		//총카운트 		
		int totalCount =massMailStatService.massmailPollPreviewTotalCount(searchMap);		
		
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		
		return new ModelAndView("/pages/massmail/statistic/massmail_poll_preview.jsp?method=pollpreviewlist","pollPersonPreviewList", massMailStatService.massmailPollPreview(curPage, iLineCnt, searchMap));
	}
	
	
	
	
	/**
	 * <p>설문전체응답자보기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massmailPollAllResponse(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
	
		if (curPage <= 0) curPage = 1;
		
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}
		
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");		
		int pollID = ServletUtil.getParamInt(req, "pollID", "0");
		

		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("pollID", pollID);
		
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);

		
		//총카운트 		
		int totalCount =massMailStatService.massmailPollAllResponseTotalCount(searchMap);

		
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		
		return new ModelAndView("/pages/massmail/statistic/massmail_poll_allresponse.jsp?method=pollAllResponseList","pollPersonPreviewList", massMailStatService.massmailPollAllResponse(curPage, iLineCnt, searchMap));
	}
	
	/**
	 * <p>설문전체미응답자보기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massmailPollAllNotResponse(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		
		if (curPage <= 0) curPage = 1;
		
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}
		
		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");		
		int pollID = ServletUtil.getParamInt(req, "pollID", "0");
		
		Map<String, Object> searchMapBackup = new HashMap<String, Object>(); 
		searchMapBackup.put("massmailID", massmailID);
		searchMapBackup.put("scheduleID", scheduleID);
		
		String backupYearMonth = massMailStatService.getBackupYearMonth(searchMapBackup);
				
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("pollID", pollID);
		
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		
		
		//총카운트 		
		int totalCount = massMailStatService.massmailPollAllNotResponseTotalCount(searchMap,backupYearMonth);
		
		
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		
		return new ModelAndView("/pages/massmail/statistic/massmail_poll_allnotresponse.jsp?method=pollAllNotResponseList","pollPersonPreviewList", massMailStatService.massmailPollAllNotResponse(curPage, iLineCnt, searchMap, backupYearMonth));
	}
	
	
	
	/**
	 * <p>설문응답 상세보기 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massmailPollAnswerView(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String id = ServletUtil.getParamString(req,"id");
		int massmailID = ServletUtil.getParamInt(req,"massmailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		int pollID = ServletUtil.getParamInt(req,"pollID","0");
		int sendID = ServletUtil.getParamInt(req,"sendID","0");
		String matrixX = ServletUtil.getParamString(req,"matrixX");
		String matrixY = ServletUtil.getParamString(req,"matrixY");
		String email = ServletUtil.getParamString(req,"email");
		
		req.setAttribute("massmailID", Integer.toString(massmailID));
		req.setAttribute("scheduleID", Integer.toString(scheduleID));
		req.setAttribute("pollID", Integer.toString(pollID));
		req.setAttribute("sendID", Integer.toString(sendID));
		req.setAttribute("matrixX", matrixX);
		req.setAttribute("matrixY", matrixY);
		req.setAttribute("email", email);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_poll_viewanswer.jsp?id="+id+"&method=pollViewAnswer","pollViewAnswerList", massMailStatService.viewDetailAnswer(pollID, massmailID, scheduleID, sendID, matrixX, matrixY)); 
		
	}
	
	/**
	 * <p>대량메일 대상자별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticTargetList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		return new ModelAndView("/pages/massmail/statistic/massmail_statistic_proc.jsp?method=statisticlist&type=target","statisticList",  massMailStatService.statisticTargetList(searchMap));
	}
	
	/**
	 * <p>대량메일 대상자별 통계 Bar 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticTargetBar(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");

		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticTargetBar(searchMap) );
	}
	
	/**
	 * <p>대량메일 대상자별 통계 Pie 차트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticTargetPie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int massmailID = ServletUtil.getParamInt(req, "massmailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String key =  ServletUtil.getParamString(req, "key");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("massmailID", massmailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("key", key);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( massMailStatService.statisticTargetPie(searchMap) );
	}
	
	
}

