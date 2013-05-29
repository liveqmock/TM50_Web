package web.autosms.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.autosms.model.*;
import web.autosms.service.AutoSMSService;
import web.common.util.LoginInfo;
import web.common.util.ServletUtil;

public class AutoSMSController extends MultiActionController{
	private AutoSMSService autoSMSService = null; 	
	private String sCurPage = null;
	private String message = "";	
	
	
	public void setAutoSMSService(AutoSMSService autoSMSService) {
		this.autoSMSService = autoSMSService;
	}
	
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		
		String step = ServletUtil.getParamString(req, "step");
		String pageURL = "";
		
		if(step.equals("reportmonth")){
			pageURL = "/pages/autosms/statistic/autosms_reportmonth.jsp";
		}else{
			pageURL = "/pages/autosms/write/autosms_write.jsp";
		}		
		
		return new ModelAndView(pageURL);
	}
	
	
	/**
	 * <p>자동메일 목록 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		if (curPage <= 0) curPage = 1;
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수

		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}
	
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sSearchType", sSearchType);
		searchMap.put("sSearchText", sSearchText);		
		
		//총카운트 		
		int iTotalCnt = autoSMSService.getTotalCountAutoSMSEvent(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
		return new ModelAndView("/pages/autosms/write/autosms_write_proc.jsp?method=list","autosmsEventList", autoSMSService.selectAutoSMSEventList(curPage, iLineCnt, searchMap));
		
	}
	
	/**
	 * <p>자동메일  수정창으로 이동한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int autosmsID = ServletUtil.getParamInt(req,"eAutosmsID","0");
		return new ModelAndView("/pages/autosms/write/autosms_write_proc.jsp?method=edit","autosmsEvent", autoSMSService.viewAutoSMSEvent(autosmsID));	
	}
	
	
	
	/**
	 * <p>이벤트sms 등록
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String autosmsTitle =ServletUtil.getParamString(req, "eAutosmsTitle");
		String	 userID  = LoginInfo.getUserID(req);
		String description =ServletUtil.getParamString(req, "eDescription");
		String templateSenderPhone =ServletUtil.getParamString(req, "eTemplateSenderPhone");
		String templateMsg =ServletUtil.getParamString(req, "smsMsg");
		String state =ServletUtil.getParamString(req, "eState");
				
		AutoSMSEvent autoSMSEvent = new AutoSMSEvent();
		autoSMSEvent.setAutosmsTitle(autosmsTitle);
		autoSMSEvent.setDescription(description);
		autoSMSEvent.setUserID(userID);
		autoSMSEvent.setTemplateSenderPhone(templateSenderPhone);
		autoSMSEvent.setTemplateMsg(templateMsg);
		autoSMSEvent.setState(state);
		
		int resultVal = autoSMSService.insertAutoSMSEvent(autoSMSEvent);
		
		if(resultVal > 0){
			this.sCurPage = "1"; 
			return list(req,res);
		}
		
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		
		return null;			
	}
	
	
	/**
	 * <p>이벤트sms 등록
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int autosmsID = ServletUtil.getParamInt(req, "eAutosmsID", "0");
		String autosmsTitle =ServletUtil.getParamString(req, "eAutosmsTitle");
		String	 userID  = LoginInfo.getUserID(req);
		String description =ServletUtil.getParamString(req, "eDescription");
		String templateSenderPhone =ServletUtil.getParamString(req, "eTemplateSenderPhone");
		String templateMsg =ServletUtil.getParamString(req, "smsMsg");
		String state =ServletUtil.getParamString(req, "eState");
				
		AutoSMSEvent autoSMSEvent = new AutoSMSEvent();
		autoSMSEvent.setAutosmsID(autosmsID);
		autoSMSEvent.setAutosmsTitle(autosmsTitle);
		autoSMSEvent.setDescription(description);
		autoSMSEvent.setUserID(userID);
		autoSMSEvent.setTemplateSenderPhone(templateSenderPhone);
		autoSMSEvent.setTemplateMsg(templateMsg);
		autoSMSEvent.setState(state);
		
		int resultVal = autoSMSService.updateAutoSMSEvent(autoSMSEvent);
		
		if(resultVal > 0){
			this.sCurPage = "1"; 
			return list(req,res);
		}
		
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		
		return null;			
	}
	
	
	/**
	 * <p>자동메일 삭제(관련 통계 및 발송데이타를 전체 삭제한다)
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{			
		
		int autosmsID = ServletUtil.getParamInt(req, "eAutosmsID", "0");				
		
		if(autosmsID==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		autoSMSService.deleteAutoSMSEventAll(autosmsID);	
		
		return list(req,res);
		
	}
	
	/**
	 * <p>자동SMS 시간별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticHourlyList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int autosmsID = ServletUtil.getParamInt(req, "autosmsID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("autosmsID", autosmsID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		return new ModelAndView("/pages/autosms/statistic/autosms_statistic_proc.jsp?method=statisticlist","statisticList", autoSMSService.statisticHourlyList(searchMap));
	}
	
	/**
	 * <p>자동SMS 일자별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticDailyList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int autosmsID = ServletUtil.getParamInt(req, "autosmsID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("autosmsID", autosmsID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		return new ModelAndView("/pages/autosms/statistic/autosms_statistic_proc.jsp?method=statisticlist","statisticList", autoSMSService.statisticDailyList(searchMap));
	}
	
	
	/**
	 * <p>자동SMS 월별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticMonthlyList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int autosmsID = ServletUtil.getParamInt(req, "autosmsID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
	
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("autosmsID", autosmsID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		
		return new ModelAndView("/pages/autosms/statistic/autosms_statistic_proc.jsp?method=statisticlist","statisticList", autoSMSService.statisticMonthlyList(searchMap));
	}
	
	
	/**
	 * <p>자동SMS 대상자 미리보기 
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
		
		int autosmsID = ServletUtil.getParamInt(req, "autosmsID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
			
		if(month.length()==1)
			month = "0"+month;		
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		String standard = ServletUtil.getParamString(req, "standard");
		String type= ServletUtil.getParamString(req, "type");
		String key= ServletUtil.getParamString(req, "key");
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("curPage", curPage);
		searchMap.put("iLineCnt", iLineCnt);
		searchMap.put("autosmsID", autosmsID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		searchMap.put("standard", standard);
		searchMap.put("type", type);
		searchMap.put("key", key);
		
		List<AutoSMSPerson> autoSMSPersonList = autoSMSService.personPreview(searchMap);
		
		//총카운트 받아오기 위해 
		searchMap.put("curPage", 0);
		searchMap.put("iLineCnt", 0);
		int iTotalCnt = autoSMSService.totalPorsonPreview(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("standard",standard);
		req.setAttribute("message", this.message);
		
		return new ModelAndView("/pages/autosms/statistic/autosms_statistic_proc.jsp?method=personpreviewlist","personPreview", autoSMSPersonList);
	}
	
	/**
	 * <p>자동SMS 월간 보고서 리스트
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView autoSMSReportMonth(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String year = ServletUtil.getParamString(req, "sSendedYear");
		String month = ServletUtil.getParamString(req, "sSendedMonth");
		String dateList = "";
		
		String sendTime = year + month;
		
		if(Integer.parseInt(month) < 13){
			dateList = year + "년 " + month + "월";
		}else{
			dateList = year + "년 ";
		}
		
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
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("curPage", curPage);
		searchMap.put("iLineCnt", iLineCnt);
		searchMap.put("sendTime", sendTime);
		
		req.setAttribute("dateList", dateList);
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		int iTotalCnt = autoSMSService.getTotalCountAutoSMSReportMonth(sendTime);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
				
		return new ModelAndView("/pages/autosms/statistic/autosms_reportmonth_proc.jsp?method=list","autoSMSReportMonthList", autoSMSService.autoSMSReportMonth(searchMap));
	}
	
	/**
	 * <p>자동SMS 월단위 총합
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView autoSMSReportMonthAll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String year = ServletUtil.getParamString(req, "sSendedYear");
		String month = ServletUtil.getParamString(req, "sSendedMonth");
		
		String sendTime = year + month;
		
		return new ModelAndView("/pages/autosms/statistic/autosms_reportmonth_proc.jsp?method=countall","smsStatistic", autoSMSService.autoSMSReportMonthAll(sendTime));
	}
	
}
