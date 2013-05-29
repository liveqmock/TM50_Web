package web.masssms.statistic.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.common.util.*;
import web.masssms.statistic.service.MassSMSStatService;
import java.util.HashMap;
import java.util.Map;

import java.util.List;
import web.masssms.statistic.model.*;




public class MassSMSStatController extends MultiActionController{
	

	private MassSMSStatService massSMSStatService= null;
	private String sCurPage = null;
	private String message = "";

	
	public void setMassSMSStatService(MassSMSStatService massSMSStatService){		
		this.massSMSStatService = massSMSStatService;
	}


	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		String step = ServletUtil.getParamString(req, "step");
		String pageURL = "";
		if(step.equals("month")){
			pageURL = "/pages/masssms/statistic/masssms_report_month.jsp";
		}else if(step.equals("users")){
			pageURL = "/pages/masssms/statistic/masssms_statistic_users.jsp";
		}else if(step.equals("schedule")){
			pageURL = "/pages/masssms/statistic/masssms_schedule.jsp";
		}
		return new ModelAndView(pageURL);
	}
	
	
	/**
	 * <p>대량메일 기본정보
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView massSMSStatisticBasicInfo(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int masssmsID = ServletUtil.getParamInt(req,"masssmsID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		req.setAttribute("targetGroupList", massSMSStatService.listTargetingGroup(masssmsID));
		req.setAttribute("masssmsSendInfo",massSMSStatService.massSMSStatisticSendInfo(masssmsID,scheduleID));
		req.setAttribute("masssmsInfo",massSMSStatService.massSMSStatisticBasicInfo(masssmsID,scheduleID));
		return new ModelAndView("/pages/masssms/statistic/masssms_statistic_proc.jsp?method=statisticbasic","masssmsFilter",massSMSStatService.massSMSStatisticFilterInfo(masssmsID, scheduleID));
	}
	
	
	/**
	 * <p>대량SMS 대상자 미리보기 
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
		
		int masssmsID = ServletUtil.getParamInt(req, "masssmsID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String standard = ServletUtil.getParamString(req, "standard");
		String type= ServletUtil.getParamString(req, "type");		
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		int iTotalCnt = ServletUtil.getParamInt(req, "iTotalCnt", "0");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("curPage", curPage);
		searchMap.put("iLineCnt", iLineCnt);
		searchMap.put("iTotalCnt", iTotalCnt);
		searchMap.put("masssmsID", masssmsID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("standard", standard);
		searchMap.put("type", type);
		
		
		int realTotalCnt = massSMSStatService.totalMassSMSPersonPreview(searchMap);
		if(realTotalCnt < iTotalCnt){
			iTotalCnt = realTotalCnt; 
		}
		
		List<MassSMSPersonPreview> personPreview = massSMSStatService.personPreview(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("standard",standard);
		req.setAttribute("message", this.message);
		
		return new ModelAndView("/pages/masssms/statistic/masssms_statistic_proc.jsp?method=personpreviewlist","personPreview", personPreview);
	}

	/**
	 * <p>월간보고서 상태별 메일 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView masssmsReportMonthList(HttpServletRequest req, HttpServletResponse res) throws Exception{
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
		String state = ServletUtil.getParamString(req, "sState");
		String userID= ServletUtil.getParamString(req, "sUserID");
		String groupID = ServletUtil.getParamString(req, "sGroupID");
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("state", state);
		searchMap.put("userID", userID);
		searchMap.put("groupID", groupID);
		
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		//총카운트 		
		int totalCount =massSMSStatService.totalCountMassSMSReportMonthList(userInfo, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
			
		return new ModelAndView("/pages/masssms/statistic/masssms_report_month_proc.jsp?method=list","masssmsWriteList", massSMSStatService.massSMSReportMonthList(userInfo, curPage, iLineCnt, searchMap));

	}
		
	
	/**
	 * <p>월간보고서 기본 통계 정보 + 발송 정보 
	 * @param req
	 * 
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView masssmsReportMonthBasic(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
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
		MassSMSReportMonth massSMSReportMonthTotalInfo = massSMSStatService.massSMSReportMonthTotalInfo(year, month, userID, groupID, userInfo);
		req.setAttribute("reportMonthSendInfo", massSMSStatService.massSMSReportMonthSendInfo(year, month, userID, groupID, userInfo));
		req.setAttribute("year", year);
		req.setAttribute("month", month);
		return new ModelAndView("/pages/masssms/statistic/masssms_report_month_proc.jsp?method=basicinfo","reportMonthTotalInfo", massSMSReportMonthTotalInfo);
	}
	
	/**
	 * <p>계정별 통계리스트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView masssmsStatisticUsers(HttpServletRequest req, HttpServletResponse res) throws Exception{
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
		
		return new ModelAndView("/pages/masssms/statistic/masssms_statistic_users_proc.jsp?method=masssmsStatisticUsersList","masssmsStatUsersList", massSMSStatService.masssmsStatisticUsersList(userInfo, searchMap));
	}
	
}

