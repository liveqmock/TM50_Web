package web.automail.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.automail.service.AutoMailService;
import web.common.util.LoginInfo;
import web.common.util.ServletUtil;
import web.automail.model.AutoMailEvent;
import web.automail.model.MailStatistic;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import web.common.util.FileUploadUtil;
import web.common.model.FileUpload;

import java.util.Iterator;


public class AutoMailController  extends MultiActionController {
	
	private AutoMailService autoMailService = null; 	
	private String sCurPage = null;
	private String message = "";	
	private String realUploadPath = null;
	
	public void setAutoMailService(AutoMailService autoMailService) {
		this.autoMailService = autoMailService;
	}
	
	//action-servlet.xml에 저장된 업로드 경로를 읽어온다. 
	public void setRealUploadPath(String realUploadPath) {
		this.realUploadPath = realUploadPath;
	}

	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		//return new ModelAndView("/pages/automail/write/automail_write.jsp");
		
		String step = ServletUtil.getParamString(req, "step");
		String pageURL = "";
		if(step.equals("history")){
			pageURL = "/pages/intermail/statistic/intermail_history.jsp";
		}else if(step.equals("reportmonth")){
			pageURL = "/pages/automail/statistic/automail_reportmonth.jsp";
		}else{
			pageURL = "/pages/automail/write/automail_write.jsp";
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
		int iTotalCnt = autoMailService.getTotalCountAutoMailEvent(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		

		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
		return new ModelAndView("/pages/automail/write/automail_write_proc.jsp?method=list","automailEventList", autoMailService.listAutoMailEvents(curPage, iLineCnt, searchMap));
	}
		
	
	/**
	 * <p>자동메일 인서트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String automailTitle =ServletUtil.getParamString(req, "eAutomailTitle");
		String userID = LoginInfo.getUserID(req);
		String description =ServletUtil.getParamString(req, "eDescription");
		String templateTitle =ServletUtil.getParamString(req, "eTemplateTitle");
		String templateContent =ServletUtil.getParamString(req, "eTemplateContent");
		String templateSenderMail =ServletUtil.getParamString(req, "eTemplateSenderMail");
		String templateSenderName =ServletUtil.getParamString(req, "eTemplateSenderName");
		String templateReceiverName =ServletUtil.getParamString(req, "eTemplateReceiverName");
		String returnMail =ServletUtil.getParamString(req, "eReturnMail");
		String replyMail =ServletUtil.getParamString(req, "eReplyMail");
		//String fileName =ServletUtil.getParamString(req, "eFileName");
		//String uploadKey =ServletUtil.getParamString(req, "eNewFile");
		String encodingType =ServletUtil.getParamString(req, "eEncodingType");
		String state =ServletUtil.getParamString(req, "eState");
		String repeatGroupType =ServletUtil.getParamString(req, "eRepeatGroupType");
		
		
		
		
		
		
		AutoMailEvent autoMailEvent = new AutoMailEvent();
		autoMailEvent.setAutomailTitle(automailTitle);
		autoMailEvent.setUserID(userID);
		autoMailEvent.setDescription(description);
		autoMailEvent.setTemplateTitle(templateTitle);
		autoMailEvent.setTemplateContent(templateContent);
		autoMailEvent.setTemplateSenderMail(templateSenderMail);
		autoMailEvent.setTemplateSenderName(templateSenderName);
		autoMailEvent.setTemplateReceiverName(templateReceiverName);
		autoMailEvent.setReplyMail(replyMail);
		autoMailEvent.setReturnMail(returnMail);
		autoMailEvent.setEncodingType(encodingType);
		autoMailEvent.setState(state); 
		autoMailEvent.setRepeatGroupType(repeatGroupType);
		//autoMailEvent.setFileName(fileName);
		//autoMailEvent.setUploadKey(uploadKey);
		
		//업로드된 파일을 base64로 인코팅해준다. 
		//String fileContent = ThunderUtil.getAttachedFileToBase64(realUploadPath, uploadKey);
		//autoMailEvent.setFileContent(fileContent);

		
		int resultVal = autoMailService.insertAutoMailEvent(autoMailEvent);
		
		if(resultVal > 0){
			this.sCurPage = "1"; 
			return list(req,res);
		}
		
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		
		return null;	
	}
	
	
	/**
	 * <p>자동메일업데이트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String automailTitle =ServletUtil.getParamString(req, "eAutomailTitle");
		String description =ServletUtil.getParamString(req, "eDescription");
		String templateTitle =ServletUtil.getParamString(req, "eTemplateTitle");
		String templateContent =ServletUtil.getParamString(req, "eTemplateContent");
		String templateSenderMail =ServletUtil.getParamString(req, "eTemplateSenderMail");
		String templateSenderName =ServletUtil.getParamString(req, "eTemplateSenderName");
		String templateReceiverName =ServletUtil.getParamString(req, "eTemplateReceiverName");
		String returnMail =ServletUtil.getParamString(req, "eReturnMail");
		String replyMail =ServletUtil.getParamString(req, "eReplyMail");
		//String fileName =ServletUtil.getParamString(req, "eFileName");
		//String uploadKey =ServletUtil.getParamString(req, "eNewFile");
		String encodingType =ServletUtil.getParamString(req, "eEncodingType");
		String state =ServletUtil.getParamString(req, "eState");
		String repeatGroupType =ServletUtil.getParamString(req, "eRepeatGroupType");
		int automailID = ServletUtil.getParamInt(req, "eAutomailID", "0");

		
		
		AutoMailEvent autoMailEvent = new AutoMailEvent();
		autoMailEvent.setAutomailID(automailID);
		autoMailEvent.setAutomailTitle(automailTitle);
		autoMailEvent.setDescription(description);
		autoMailEvent.setTemplateTitle(templateTitle);
		autoMailEvent.setTemplateContent(templateContent);
		autoMailEvent.setTemplateSenderMail(templateSenderMail);
		autoMailEvent.setTemplateSenderName(templateSenderName);
		autoMailEvent.setTemplateReceiverName(templateReceiverName);
		autoMailEvent.setReplyMail(replyMail);
		autoMailEvent.setReturnMail(returnMail);
		autoMailEvent.setEncodingType(encodingType);
		autoMailEvent.setState(state);	
		autoMailEvent.setRepeatGroupType(repeatGroupType);
		//autoMailEvent.setFileName(fileName);
		//autoMailEvent.setUploadKey(uploadKey);
		
		//업로드된 파일을 base64로 인코팅해준다. 
		//String fileContent = ThunderUtil.getAttachedFileToBase64(realUploadPath, uploadKey);
		//autoMailEvent.setFileContent(fileContent);
		
		
		int resultVal = autoMailService.updateAutoMailEvent(autoMailEvent);
		
		if(resultVal > 0){
			this.sCurPage = "1"; 
			return list(req,res);
		}
		
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		
		return null;	
	}
	

	
	/**
	 * <p>자동메일  수정창으로 이동한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int automailID = ServletUtil.getParamInt(req,"eAutomailID","0");
		return new ModelAndView("/pages/automail/write/automail_write_proc.jsp?method=edit","automailEvent", autoMailService.viewAutoMailEvent(automailID));	
	}
	
	
	@SuppressWarnings("unchecked")
	public void uploadFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
		
		String uploadKey = mreq.getParameter("uploadKey");
		if(uploadKey == null) {
			throw new Exception("추가시 uploadKey를 파라미터로 반드시 전달 해야됨");
		}

		FileUpload fileUpload = null;
		
		Iterator fileNameIterator = mreq.getFileNames();		
		while(fileNameIterator.hasNext()){
			MultipartFile multiFile = mreq.getFile((String)fileNameIterator.next());			
			if(multiFile.getSize()>0){
				fileUpload = FileUploadUtil.uploadNewFile(mreq, multiFile, this.realUploadPath, uploadKey);
				fileUpload.setUploadKey(uploadKey);			
			}
		}
	}	
	
	
	/**
	 * <p>자동메일 삭제(관련 통계 및 발송데이타를 전체 삭제한다)
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{			
		
		int automailID = ServletUtil.getParamInt(req, "eAutomailID", "0");				
		
		if(automailID==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
			
		autoMailService.deleteAutoMailEventAll(automailID);		
		return list(req,res);
		
	}
	
	/* 파일 정보를 보이게한다. */
	public ModelAndView getFileInfo(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		AutoMailEvent automailEvent = autoMailService.viewAutoMailEvent(automailID);
		FileUpload fileUpload = new FileUpload();
		fileUpload.setRealFileName(automailEvent.getFileName());
		fileUpload.setNewFileName(automailEvent.getUploadKey());
		
		
		if(fileUpload != null) {			
			if(!FileUploadUtil.existFile( fileUpload.getNewFileName(), this.realUploadPath) ) {
				return null;
			}
			
		}

		return new ModelAndView("/pages/automail/write/automail_write_proc.jsp","fileUpload",fileUpload);
	}
	
	/**
	 * <p>파일을 다운로드 한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void fileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		AutoMailEvent automailEvent = autoMailService.viewAutoMailEvent(automailID);	
	
		if(automailEvent != null) {
			FileUploadUtil.downloadFile(req, res, this.realUploadPath, automailEvent.getUploadKey(), automailEvent.getFileName());
		}
	}
	/**
	 * <p>자동메일 시간별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticHourlyList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		return new ModelAndView("/pages/automail/statistic/automail_statistic_proc.jsp?method=statisticlist","statisticList", autoMailService.statisticHourlyList(searchMap));
	}
	

	/**
	 * <p>자동메일 시간별 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticHourly(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticHourly(searchMap) );
	}
	
	/**
	 * <p>자동메일 시간별 파이 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticHourlyPie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticHourlyPie(searchMap) );
	}
	
	/**
	 * <p>자동메일 일자별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticDailyList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		return new ModelAndView("/pages/automail/statistic/automail_statistic_proc.jsp?method=statisticlist","statisticList", autoMailService.statisticDailyList(searchMap));
	}
	
	/**
	 * <p>자동메일 일자별 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticDaily(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticDaily(searchMap) );
	}
	
	/**
	 * <p>자동메일 일자별 파이 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticDailyPie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticDailyPie(searchMap) );
	}
	
	/**
	 * <p>자동메일 월별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticMonthlyList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
	
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		
		return new ModelAndView("/pages/automail/statistic/automail_statistic_proc.jsp?method=statisticlist","statisticList", autoMailService.statisticMonthlyList(searchMap));
	}
	
	/**
	 * <p>자동메일 월별 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticMonthly(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
	
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticMonthly(searchMap) );
	}
	
	/**
	 * <p>자동메일 월별 파이 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticMonthlyPie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
	
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticMonthlyPie(searchMap) );
	}
	
	/**
	 * <p>자동메일 도메인별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticDomainList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		return new ModelAndView("/pages/automail/statistic/automail_statistic_proc.jsp?method=statisticlist","statisticList", autoMailService.statisticDomainList(searchMap));
	}
	
	/**
	 * <p>자동메일 도메인별 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticDomain(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticDomain(searchMap) );
	}
	
	/**
	 * <p>자동메일 도메인별 파이 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticDomainPie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticDomainPie(searchMap) );
	}
	
	/**
	 * <p>자동메일 실패원인별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticFailCauseList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		return new ModelAndView("/pages/automail/statistic/automail_statistic_proc.jsp?method=statisticfailcauselist","statisticFailCauseList", autoMailService.statisticFailCauseList(searchMap));
	}
	
	/**
	 * <p>자동메일 실패원인별 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void statisticFailcause(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticFailCause(searchMap) );
	}
	
	/**
	 * <p>자동메일 실패원인별 파이 챠트 json 
	 * @param req
	 * @param res
	 * @return
	 */
	public void statisticFailcausePie(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		res.setContentType("text/html; charset=UTF-8");
		res.getWriter().print( autoMailService.statisticFailCausePie(searchMap) );
	}
	
	/**
	 * <p>자동메일 대상자 미리보기 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView porsonPreview(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
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
		
		int automailID = ServletUtil.getParamInt(req, "automailID", "0");
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
		searchMap.put("automailID", automailID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		searchMap.put("standard", standard);
		searchMap.put("type", type);
		searchMap.put("key", key);
		
		List<MailStatistic> porsonPreview = autoMailService.porsonPreview(searchMap);
		
		//총카운트 받아오기 위해 
		searchMap.put("curPage", 0);
		searchMap.put("iLineCnt", 0);
		int iTotalCnt = autoMailService.totalPorsonPreview(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("standard",standard);
		req.setAttribute("message", this.message);
		
		return new ModelAndView("/pages/automail/statistic/automail_statistic_proc.jsp?method=porsonpreviewlist","porsonPreview", porsonPreview);
	}
	
	/**
	 * <p>자동메일 월간보고서 리스트
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView autoMailReportMonth(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
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
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
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
		
		int iTotalCnt = autoMailService.getTotalCountAutoMailReportMonth(sendTime);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		
//		List<MailStatistic> autoMailReportMonthAll = autoMailService.autoMailReportMonthAll(sendTime);
		
//		req.setAttribute("autoMailReportMonthAll", autoMailReportMonthAll);
		
		return new ModelAndView("/pages/automail/statistic/automail_reportmonth_proc.jsp?method=list","autoMailReportMonthList", autoMailService.autoMailReportMonth(searchMap));
	}
	
	/**
	 * <p>자동메일 월단위 총합
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView autoMailReportMonthAll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String year = ServletUtil.getParamString(req, "sSendedYear");
		String month = ServletUtil.getParamString(req, "sSendedMonth");
		
		String sendTime = year + month;
		
		return new ModelAndView("/pages/automail/statistic/automail_reportmonth_proc.jsp?method=countall","mailStatistic", autoMailService.autoMailReportMonthAll(sendTime));
	}
}


