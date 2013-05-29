package web.intermail.control;

import java.util.*;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.intermail.model.*;
import web.common.model.FileUpload;
import web.common.util.DateUtils;
import web.common.util.FileUploadUtil;
import web.common.util.LoginInfo;
import web.common.util.ServletUtil;
import web.common.util.Constant;
import web.intermail.service.InterMailService;


public class InterMailController extends MultiActionController{

	private InterMailService interMailService = null; 	
	private String sCurPage = null;
	private String message = "";	
	private String realUploadPath = null;
	
	public void setInterMailService(InterMailService interMailService) {
		this.interMailService = interMailService;
	}
	
	//action-servlet.xml에 저장된 업로드 경로를 읽어온다. 
	public void setRealUploadPath(String realUploadPath) {
		this.realUploadPath = realUploadPath;
	}

	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		
		String step = ServletUtil.getParamString(req, "step");
		String pageURL = "";
		if(step.equals("history")){
			pageURL = "/pages/intermail/statistic/intermail_history.jsp";
		}else{
			pageURL = "/pages/intermail/write/intermail_write.jsp";
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
		int iTotalCnt = interMailService.getTotalCountInterMailEvent(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		

		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
		return new ModelAndView("/pages/intermail/write/intermail_write_proc.jsp?method=list","intermailEventList", interMailService.listInterMailEvents(curPage, iLineCnt, searchMap));
	}
	
	
	/**
	 * <p>연동메일  수정창으로 이동한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int intermailID = ServletUtil.getParamInt(req,"eIntermailID","0");
		List<InterMailSchedule> interMailScheduleList =  interMailService.selectInterMailSchedule(intermailID);
		req.setAttribute("interMailScheduleList", interMailScheduleList);		
		return new ModelAndView("/pages/intermail/write/intermail_write_proc.jsp?method=edit&intermailID="+intermailID,"intermailEvent", interMailService.viewInterMailEvent(intermailID));	
	}
	
	
	/**
	 * <p>연동메일 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String intermailTitle =ServletUtil.getParamString(req, "eIntermailTitle");
		String userID = LoginInfo.getUserID(req);
		String description =ServletUtil.getParamString(req, "eDescription");
		String templateTitle =ServletUtil.getParamString(req, "eTemplateTitle");
		String templateContent =ServletUtil.getParamString(req, "eTemplateContent");
		String templateSenderMail =ServletUtil.getParamString(req, "eTemplateSenderMail");
		String templateSenderName =ServletUtil.getParamString(req, "eTemplateSenderName");
		String templateReceiverName =ServletUtil.getParamString(req, "eTemplateReceiverName");
		String returnMail =ServletUtil.getParamString(req, "eReturnMail");
		String fileKey =ServletUtil.getParamString(req, "eFileKey");
		String templateFileName =ServletUtil.getParamString(req, "eFileName");		
		String encodingType =ServletUtil.getParamString(req, "eEncodingType");		
		String secretYN =ServletUtil.getParamString(req, "eSecretYN");
		String[] scheduleIDs = ServletUtil.getParamStringArray(req, "eScheduleID");
		String repeatGroupType = ServletUtil.getParamString(req, "eRepeatGroupType");	
		
		InterMailEvent interMailEvent = new InterMailEvent();
		interMailEvent.setIntermailTitle(intermailTitle);
		interMailEvent.setUserID(userID);
		interMailEvent.setDescription(description);
		interMailEvent.setTemplateTitle(templateTitle);
		interMailEvent.setTemplateContent(templateContent);
		interMailEvent.setTemplateSenderMail(templateSenderMail);
		interMailEvent.setTemplateSenderName(templateSenderName);
		interMailEvent.setTemplateReceiverName(templateReceiverName);
		interMailEvent.setReturnMail(returnMail);
		interMailEvent.setFileKey(fileKey);
		interMailEvent.setRepeatGroupType(repeatGroupType);
		
		
		//템플릿파일을 올렸다면 
		if(!fileKey.equals("")){
			interMailEvent.setTemplateFileName(templateFileName);
			interMailEvent.setTemplateFileContent(getStringFileContent(fileKey));
		}
		
		interMailEvent.setEncodingType(encodingType);		
		interMailEvent.setSecretYN(secretYN);
		
		int resultVal = 0;
		int intermailID = 0;
		
		synchronized(this){
			
			resultVal = interMailService.insertInterMailEvent(interMailEvent);
			if(resultVal>0){
				intermailID = interMailService.getMaxInterMailID();
				
				//intermailID, scheduleID, sendType, state, sendScheduleDate, statisticEndDate
				for(int i=0;i<scheduleIDs.length;i++){
					
					InterMailSchedule interMailSchedule = new InterMailSchedule();
					interMailSchedule.setIntermailID(intermailID);
					interMailSchedule.setScheduleID(Integer.parseInt(scheduleIDs[i]));
					interMailSchedule.setSendType(ServletUtil.getParamString(req, "eSendType_"+scheduleIDs[i]));
					interMailSchedule.setState(Constant.INTER_STATE_READY);
					
					String sendScheduleDate = null;
					String statisticEndDate = null;			
					//수동발송이라면 
					if(interMailSchedule.getSendType().equals("1")){
						String sendScheduleDate1 =  ServletUtil.getParamString(req, "eSendSchedule_"+scheduleIDs[i]);
						String sendScheduleDate2 =  ServletUtil.getParamString(req, "eSendScheduleHH_"+scheduleIDs[i]);
						String sendScheduleDate3 =  ServletUtil.getParamString(req, "eSendScheduleMM_"+scheduleIDs[i]);
						
						if(!sendScheduleDate1.equals("") && !sendScheduleDate2.equals("") && !sendScheduleDate3.equals("")){
							sendScheduleDate = sendScheduleDate1+" "+sendScheduleDate2+":"+sendScheduleDate3+":00";						
						}
						String statisticEndDate1 =  ServletUtil.getParamString(req, "eStatisticSchedule_"+scheduleIDs[i]);
						String statisticEndDate2 =  ServletUtil.getParamString(req, "eStatisticScheduleHH_"+scheduleIDs[i]);
						String statisticEndDate3 =  ServletUtil.getParamString(req, "eStatisticScheduleMM_"+scheduleIDs[i]);
						
							
						if(!statisticEndDate1.equals("") && !statisticEndDate2.equals("") && !statisticEndDate3.equals("")){
							statisticEndDate = statisticEndDate1+" "+statisticEndDate2+":"+statisticEndDate3+":00";						
						}						
					//자동발송	
					}else{
						//자동발송이라면 현재 날짜를 넣어준다.
						sendScheduleDate = DateUtils.getDateTimeString();		
						
					}
					
					interMailSchedule.setSendScheduleDate(sendScheduleDate);
					interMailSchedule.setStatisticEndDate(statisticEndDate);
					
					
					if(interMailService.insertInterMailSchedule(interMailSchedule)<1){					
						resultVal = 0;
						break;
					}
				}				
			}// end if 
		} //end synchronized
		
		//큐테이블 생성
		if(interMailService.createSendQueueTable("tm_intermail_sendqueue_"+intermailID)<0){	
			resultVal = 0;
		}
		
		
		if(resultVal > 0){			 
			this.sCurPage = "1"; 
			return list(req,res);
		}else{
			interMailService.deleteInterMailSchedule(intermailID);
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");	
		}
		
		
		
		return null;	
	}
	
	
	/**
	 * <p>연동메일 수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int intermailID = ServletUtil.getParamInt(req, "eIntermailID", "0");
		String intermailTitle =ServletUtil.getParamString(req, "eIntermailTitle");
		String userID = LoginInfo.getUserID(req);
		String description =ServletUtil.getParamString(req, "eDescription");
		String templateTitle =ServletUtil.getParamString(req, "eTemplateTitle");
		String templateContent =ServletUtil.getParamString(req, "eTemplateContent");
		String templateSenderMail =ServletUtil.getParamString(req, "eTemplateSenderMail");
		String templateSenderName =ServletUtil.getParamString(req, "eTemplateSenderName");
		String templateReceiverName =ServletUtil.getParamString(req, "eTemplateReceiverName");
		String returnMail =ServletUtil.getParamString(req, "eReturnMail");
		String fileKey =ServletUtil.getParamString(req, "eFileKey");
		String templateFileName =ServletUtil.getParamString(req, "eFileName");		
		String encodingType =ServletUtil.getParamString(req, "eEncodingType");		
		String secretYN =ServletUtil.getParamString(req, "eSecretYN");
		String[] scheduleIDs = ServletUtil.getParamStringArray(req, "eScheduleID");
		String repeatGroupType = ServletUtil.getParamString(req, "eRepeatGroupType");	
		
		
		InterMailEvent interMailEvent = new InterMailEvent();
		interMailEvent.setIntermailID(intermailID);
		interMailEvent.setIntermailTitle(intermailTitle);
		interMailEvent.setUserID(userID);
		interMailEvent.setDescription(description);
		interMailEvent.setTemplateTitle(templateTitle);
		interMailEvent.setTemplateContent(templateContent);
		interMailEvent.setTemplateSenderMail(templateSenderMail);
		interMailEvent.setTemplateSenderName(templateSenderName);
		interMailEvent.setTemplateReceiverName(templateReceiverName);
		interMailEvent.setReturnMail(returnMail);
		interMailEvent.setFileKey(fileKey);
		interMailEvent.setRepeatGroupType(repeatGroupType);
		
		//템플릿파일을 올렸다면 
		if(!fileKey.equals("")){
			interMailEvent.setTemplateFileName(templateFileName);
			interMailEvent.setTemplateFileContent(getStringFileContent(fileKey));
		}
		
		interMailEvent.setEncodingType(encodingType);		
		interMailEvent.setSecretYN(secretYN);
		
		int resultVal = 0;		
		
		synchronized(this){
			
			resultVal = interMailService.updateInterMailEvent(interMailEvent);
			
			if(resultVal>0){
				
				//intermailID, scheduleID, sendType, state, sendScheduleDate, statisticEndDate
				for(int i=0;i<scheduleIDs.length;i++){
					
					InterMailSchedule interMailSchedule = new InterMailSchedule();
					interMailSchedule.setIntermailID(intermailID);
					interMailSchedule.setScheduleID(Integer.parseInt(scheduleIDs[i]));
					interMailSchedule.setSendType(ServletUtil.getParamString(req, "eSendType_"+scheduleIDs[i]));
					interMailSchedule.setState(Constant.INTER_STATE_READY);
					
					String sendScheduleDate = null;
					String statisticEndDate = null;			
					//수동발송이라면 
					if(interMailSchedule.getSendType().equals("1")){
						String sendScheduleDate1 =  ServletUtil.getParamString(req, "eSendSchedule_"+scheduleIDs[i]);
						String sendScheduleDate2 =  ServletUtil.getParamString(req, "eSendScheduleHH_"+scheduleIDs[i]);
						String sendScheduleDate3 =  ServletUtil.getParamString(req, "eSendScheduleMM_"+scheduleIDs[i]);
						
						if(!sendScheduleDate1.equals("") && !sendScheduleDate2.equals("") && !sendScheduleDate3.equals("")){
							sendScheduleDate = sendScheduleDate1+" "+sendScheduleDate2+":"+sendScheduleDate3+":00";						
						}
						String statisticEndDate1 =  ServletUtil.getParamString(req, "eStatisticSchedule_"+scheduleIDs[i]);
						String statisticEndDate2 =  ServletUtil.getParamString(req, "eStatisticScheduleHH_"+scheduleIDs[i]);
						String statisticEndDate3 =  ServletUtil.getParamString(req, "eStatisticScheduleMM_"+scheduleIDs[i]);
						
							
						if(!statisticEndDate1.equals("") && !statisticEndDate2.equals("") && !statisticEndDate3.equals("")){
							statisticEndDate = statisticEndDate1+" "+statisticEndDate2+":"+statisticEndDate3+":00";						
						}						
					//자동발송 	
					}else{
						//자동발송이라면 현재 날짜를 넣어준다.
						sendScheduleDate = DateUtils.getDateTimeString();						
					}
					
					interMailSchedule.setSendScheduleDate(sendScheduleDate);
					interMailSchedule.setStatisticEndDate(statisticEndDate);
					
					
					if(interMailService.updateInterMailSchedule(interMailSchedule)<1){					
						resultVal = 0;
						break;
					}
				}				
			}// end if 
		} //end synchronized
		
		if(resultVal > 0){
			this.sCurPage = "1"; 
			return list(req,res);
		}else{		
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");	
		}
		
		
		
		return null;	
	}
	
	
	/**
	 * <p>연동메일 삭제(관련 통계 및 발송데이타를 전체 삭제한다)
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{			
		
		int intermailID = ServletUtil.getParamInt(req, "eIntermailID", "0");				
		
		if(intermailID==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
			
		interMailService.deleteInterMailSchedule(intermailID);		
		return list(req,res);
		
	}
	
	
	/**
	 * <p>파일 업로드 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
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
				if(interMailService.insertFile(fileUpload) < 0) {
					ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
					return;
				}
			}
		}
	}
	
	
	
	
	
	/* 파일 정보를 보이게한다. */
	public ModelAndView getFileInfo(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String uploadKey = ServletUtil.getParamStringDefault(req, "uploadKey", "");
		String intermailID = ServletUtil.getParamString(req, "intermailID");
		
		if(!intermailID.equals("0") && uploadKey.equals("")) {
			InterMailEvent interMailEvent = interMailService.viewInterMailEvent( Integer.parseInt(intermailID) );
			if(interMailEvent != null) uploadKey = interMailEvent.getFileKey();
		}
			
		FileUpload fileUpload= interMailService.getFileInfo(uploadKey);
		if(fileUpload != null) {
			
			if(!FileUploadUtil.existFile( fileUpload.getNewFileName(), this.realUploadPath) ) {
				return null;
			}			
		}

		return new ModelAndView("/pages/intermail/write/intermail_write_proc.jsp?method=getFileInfo","fileUpload", interMailService.getFileInfo(uploadKey));
	}
	
	/**
	 * <p>파일을 다운로드 한다.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void fileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String uploadKey = ServletUtil.getParamString(req, "uploadKey");
		FileUpload fileUpload = null;
		
		
		fileUpload = interMailService.getFileInfo(uploadKey);
	
		if(fileUpload != null) {
			FileUploadUtil.downloadFile(req, res, this.realUploadPath, fileUpload.getNewFileName(), fileUpload.getRealFileName());
		}
	}
	
	
	/**
	 * <p>파일을 읽어들여 가져온다.
	 * @param fileKey
	 * @return
	 */
	private String getStringFileContent(String fileKey){
		BufferedReader br = null;
		StringBuffer sb = null;
		try{
			br = new BufferedReader(new FileReader(this.realUploadPath+fileKey));
			sb = new StringBuffer();
			char[] buffer = new char[512];
			int readcount = 0;
			while( (readcount=br.read(buffer))!=-1){
				sb.append(buffer,0,readcount);
			}			
		}catch(Exception e){
			logger.error(e);
		}finally{
			try{
				if(br!=null) br.close();				
			}catch(Exception e){}
		}
		return sb.toString();
	}
	
	
	/**
	 * <p>상태값 변경 업데이트  
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView changeSendState(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int intermailID = ServletUtil.getParamInt(req,"intermailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		String  state = ServletUtil.getParamString(req, "state");

		int resultVal = interMailService.updateSendState(intermailID, scheduleID, state);
		
		if(resultVal>0){
			this.message = "";
		}else{
			ServletUtil.messageGoURL(res,"상태값 변경에 실패 하였습니다.","");		
		}
		return null;
	}
	
	/**
	 * <p>연동메일 시간별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticHourlyList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int intermailID = ServletUtil.getParamInt(req, "intermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("intermailID", intermailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		return new ModelAndView("/pages/intermail/statistic/intermail_statistic_proc.jsp?method=statisticlist","statisticList", interMailService.statisticHourlyList(searchMap));
	}
	
	
	/**
	 * <p>자동메일 일자별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticDailyList(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int intermailID = ServletUtil.getParamInt(req, "intermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("intermailID", intermailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		return new ModelAndView("/pages/intermail/statistic/intermail_statistic_proc.jsp?method=statisticlist","statisticList", interMailService.statisticDailyList(searchMap));
	}
	
	
	/**
	 * <p>연동메일 월별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticMonthlyList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int intermailID = ServletUtil.getParamInt(req, "intermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
	
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("intermailID", intermailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		
		return new ModelAndView("/pages/intermail/statistic/intermail_statistic_proc.jsp?method=statisticlist","statisticList", interMailService.statisticMonthlyList(searchMap));
	}
	
	
	/**
	 * <p>연동메일 도메인별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticDomainList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int intermailID = ServletUtil.getParamInt(req, "intermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("intermailID", intermailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		
		return new ModelAndView("/pages/intermail/statistic/intermail_statistic_proc.jsp?method=statisticlist","statisticList", interMailService.statisticDomainList(searchMap));
	}
	
	
	/**
	 * <p>자동메일 실패원인별 통계 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView statisticFailCauseList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int intermailID = ServletUtil.getParamInt(req, "intermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String year = ServletUtil.getParamString(req, "sYear");
		String month = ServletUtil.getParamString(req, "sMonth");
		if(month.length()==1)
			month = "0"+month;
		
		String day = ServletUtil.getParamString(req, "sDay");
		if(day.length()==1)
			day = "0"+day;
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("intermailID", intermailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		return new ModelAndView("/pages/intermail/statistic/intermail_statistic_proc.jsp?method=statisticfailcauselist","statisticFailCauseList", interMailService.statisticFailCauseList(searchMap));
	}
	
	
	
	
	/**
	 * <p>발송결과 내역 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listSendHistory(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		
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
		
	
		req.setAttribute("curPage1", Integer.toString(curPage));
		req.setAttribute("iLineCnt1",Integer.toString(iLineCnt));
		req.setAttribute("message1", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		String searchType = ServletUtil.getParamString(req,"sSearchType");
		String resultYear = ServletUtil.getParamString(req,"sResultYear");
		String resultMonth = ServletUtil.getParamString(req,"sResultMonth");
		String searchText = ServletUtil.getParamString(req,"sSearchText");
		String smtpCodeType = ServletUtil.getParamString(req,"sSmtpCodeType");
		String openYN = ServletUtil.getParamString(req,"sOpenYN");
		String openFileYN = ServletUtil.getParamString(req,"sOpenFileYN");
	
		
		int intermailID = ServletUtil.getParamInt(req, "intermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
		String resultYearMonth = ServletUtil.getParamString(req, "resultYearMonth");
		
		if(!resultYear.equals("") && !resultMonth.equals("")){
			resultYearMonth = resultYear+""+resultMonth;
		}
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("currentPage", curPage);
		paramsMap.put("countPerPage", iLineCnt);
		paramsMap.put("intermailID", intermailID);
		paramsMap.put("scheduleID", scheduleID);
		paramsMap.put("resultYearMonth", resultYearMonth);
		paramsMap.put("searchType", searchType);
		paramsMap.put("searchText", searchText);
		paramsMap.put("smtpCodeType", smtpCodeType);
		paramsMap.put("openYN", openYN);
		paramsMap.put("openFileYN", openFileYN);
		
		
		
		//총카운트 		
		int iTotalCnt = interMailService.totaltInterMailHistory(paramsMap);
		req.setAttribute("iTotalCnt1", Integer.toString(iTotalCnt));
		
		
		//해당 연동 캠페인에서 호출한 것이라면
		String resultPage = "";
		if(intermailID>0 && scheduleID>0){
			resultPage = "/pages/intermail/statistic/intermail_statistic_proc.jsp?method=listSendHistory";
		}else{
			resultPage = "/pages/intermail/statistic/intermail_history_proc.jsp?method=listSendHistory";
		}
		
		return new ModelAndView(resultPage,"interMailSendResultList", interMailService.selectInterMailHistory(paramsMap));
		
	}
	
	/**
	 * <p>연동메일 대상자 미리보기 
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
		
		int intermailID = ServletUtil.getParamInt(req, "intermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
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
		searchMap.put("intermailID", intermailID);
		searchMap.put("scheduleID", scheduleID);
		searchMap.put("year", year);
		searchMap.put("month", month);
		searchMap.put("day", day);
		searchMap.put("standard", standard);
		searchMap.put("type", type);
		searchMap.put("key", key);
		
		List<MailStatistic> porsonPreview = interMailService.porsonPreview(searchMap);
		
		//총카운트 받아오기 위해 
		searchMap.put("curPage", 0);
		searchMap.put("iLineCnt", 0);
		int iTotalCnt = interMailService.totalPorsonPreview(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("standard",standard);
		req.setAttribute("message", this.message);
		
		return new ModelAndView("/pages/intermail/statistic/intermail_statistic_proc.jsp?method=porsonpreviewlist","porsonPreview", porsonPreview);
	}
	
	
	/**
	 * <p>연동메일 대상자 미리보기 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView listInterMailSend(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int intermailID = ServletUtil.getParamInt(req, "intermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");
	
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
		Map<String, Object> paramMap = new HashMap<String, Object>();		
		paramMap.put("sSearchType", sSearchType);
		paramMap.put("sSearchText", sSearchText);
		paramMap.put("currentPage", curPage);
		paramMap.put("countPerPage", iLineCnt);
		paramMap.put("intermailID", intermailID);
		paramMap.put("scheduleID", scheduleID);
		
		//총카운트 		
		int iTotalCnt = interMailService.totalInterMailSendList(paramMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		

		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
		return new ModelAndView("/pages/intermail/write/intermail_approve_proc.jsp?method=listInterMailSend","intermailSendList", interMailService.selectInterMailSendList(paramMap));
	}
	
	/**
	 * <p>연동메일  수정창으로 이동한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewInterMailSend(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int intermailID = ServletUtil.getParamInt(req,"intermailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		String sendID = ServletUtil.getParamString(req,"sendID");		
		String flag = ServletUtil.getParamString(req,"flag");
		return new ModelAndView("/pages/intermail/write/intermail_approve_proc.jsp?method="+flag,"interMailSend", interMailService.viewInterMailSend(intermailID, scheduleID, sendID)); 	
	}
	
	
	/**
	 * <p>선택된 발송대기건 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteSelectedSend(HttpServletRequest req, HttpServletResponse res) throws Exception{			
		
		int intermailID = ServletUtil.getParamInt(req, "eIntermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "eScheduleID", "0");
		String[] sendIDs = ServletUtil.getParamStringArray(req, "eSendID");		
		
		if(intermailID==0 || scheduleID==0 || sendIDs==null){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
			
		interMailService.deleteSelectedInterMailSend(intermailID, scheduleID, sendIDs);
		return listInterMailSend(req,res);
		
	}
	
	/**
	 * <p>전체  발송대기건 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteAllSend(HttpServletRequest req, HttpServletResponse res) throws Exception{			
		
		int intermailID = ServletUtil.getParamInt(req, "eIntermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "eScheduleID", "0");		
		
		if(intermailID==0 || scheduleID==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}			
		if(interMailService.deleteAllInterMailSend(intermailID, scheduleID)>0){
			//발송대기건수가 하나도 없으므로 발송대기중(00) 상태로 변경한다.
			interMailService.updateSendState(intermailID, scheduleID, Constant.INTER_STATE_READY);
		}
		return listInterMailSend(req,res);
		
	}
	
	/**
	 * <p>승인처리  
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateSendApprove(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int intermailID = ServletUtil.getParamInt(req, "eIntermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "eScheduleID", "0");	
		if(intermailID==0 || scheduleID==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}		
		interMailService.updateInterMailSendApprove(intermailID, scheduleID, Constant.INTER_STATE_PREPARED);   //발송준비완료로 업데이트 checkedYN=Y
		return listInterMailSend(req,res);
	}
	
	
	/**
	 * <p>연동메일 복사
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView copyInterMail(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int intermailID = ServletUtil.getParamInt(req,"intermailID","0");
		//List<InterMailSchedule> interMailScheduleList =  interMailService.selectInterMailSchedule(intermailID);
		//req.setAttribute("interMailScheduleList", interMailScheduleList);		
		return new ModelAndView("/pages/intermail/write/intermail_write_proc.jsp?method=edit&flag=copy","intermailEvent", interMailService.viewInterMailEvent(intermailID));	
	}
	
	
	
	/**
	 * <p>재발송 처리 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView resendRetryMail(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String retrySendFlag = ServletUtil.getParamString(req,"eRetrySendFlag");
		int intermailID = ServletUtil.getParamInt(req, "intermailID", "0");
		int scheduleID = ServletUtil.getParamInt(req, "scheduleID", "0");	
		String resultYear = ServletUtil.getParamString(req,"sResultYear");
		String resultMonth = ServletUtil.getParamString(req,"sResultMonth");
		
		String resultYearMonth = resultYear+resultMonth;
		
		boolean resultBool = true;
				
		Map<String,Object> resultMap = interMailService.checkRetryFinishYN(intermailID, scheduleID) ;
		
		String retryFinishYN = (String)resultMap.get("retryFinishYN");
		
		if(retryFinishYN==null || retryFinishYN.equals("") || retryFinishYN.equals("N")){
			
			ServletUtil.messageGoURL(res,"기본 재발송이 완료되지 않았습니다. \\r\\n기본재발송이 설정된 횟수 만큼 완료된 후에 재발송이 가능합니다.","");
			return null;
		}
		
		
		
		//체크된 실패메일 재발송일 경우 
		if(retrySendFlag.equals("checked")){		
			String[] sendIDs = ServletUtil.getParamStringArray(req, "eSendID");
			
			if(sendIDs==null || sendIDs.length==0){
				ServletUtil.messageGoURL(res,"재발송할 선택된 데이터가 없습니다.","");
				return null;
			}
			
			List<InterMailSendResult> interMailSendResultList = new ArrayList<InterMailSendResult>();
			
			for(int i=0;i<sendIDs.length;i++){
				InterMailSendResult interMailSendResult = new InterMailSendResult();
				interMailSendResult.setIntermailID(intermailID);
				interMailSendResult.setScheduleID(scheduleID);
				interMailSendResult.setSendID(sendIDs[i]);
				interMailSendResultList.add(interMailSendResult);
			}
			
			resultBool = interMailService.resendretrySendChecked(resultYearMonth, interMailSendResultList);
		}
		
		//전체 실패된 메일 재발송일 경우 
		else{
			resultBool = interMailService.resendRetrySendAll(resultYearMonth, intermailID, scheduleID);
		}
		
		if(!resultBool){
			ServletUtil.messageGoURL(res,"재발송이 실패되었습니다..","");			
		}else{
			ServletUtil.messageGoURL(res,"재발송이 처리되었습니다. 재발송은 발송엔진에서 처리하므로 다소 시간이 소요됩니다.","");
		}
		
		return listSendHistory(req,res);
	}
	
		
}
