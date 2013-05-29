package web.content.poll.control;


import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.common.model.FileUpload;
import web.common.util.*;
import web.content.poll.model.*;
import web.content.poll.service.*;


public class PollController extends MultiActionController{

	private PollService pollService = null;
	private String sCurPage = null;
	private String message = "";
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";
	private String realUploadPath = null;
	
	public void setPollService(PollService pollService){
		this.pollService = pollService;
	}
	
	//action-servlet.xml에 저장된 업로드 경로를 읽어온다. 
	public void setRealUploadPath(String realUploadPath) {
		this.realUploadPath = realUploadPath;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		String step = ServletUtil.getParamString(req, "step");
		String pageURL = "";
		if(step.equals("pollInfo")){
			pageURL = "/pages/content/poll/poll.jsp";
		}else if(step.equals("pollTemplate")){
			pageURL =  "/pages/content/poll/poll_template.jsp";
		}else if(step.equals("pollCode")){
			pageURL =  "/pages/content/poll/poll_code.jsp";
		}
		return new ModelAndView(pageURL);
	
	}
	
	
	/**
	 * <p>리스트 출력 
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
		String sUseYN = ServletUtil.getParamString(req,"sUseYN");
		String sSelectedGroupID = ServletUtil.getParamString(req,"sSelectedGroupID");
		String sResultFinishYN = ServletUtil.getParamString(req,"sResultFinishYN");
			

		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("useYN", sUseYN);	
		searchMap.put("resultFinishYN", sResultFinishYN);
		searchMap.put("selectedGroupID", sSelectedGroupID);	
		String userAuth = LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		
		String userID = LoginInfo.getUserID(req);
		String groupID =  LoginInfo.getGroupID(req);
		req.setAttribute("groupID", groupID);
		
		searchMap.put("sUseYN", sUseYN);	
		
		//userID, groupID, userAuth, 
		searchMap.put("userID", userID);
		searchMap.put("groupID", groupID);
		searchMap.put("userAuth", userAuth);
		
		//총카운트 		
		int totalCount =pollService.selectPollCount(searchMap);		
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		return new ModelAndView("/pages/content/poll/poll_proc.jsp?method=list","pollInfoList", pollService.selectPollInfoList(curPage, iLineCnt, searchMap));
		
	}
	
	/**
	 * <p>설문기본정보추가
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView insertPollInfo(HttpServletRequest req, HttpServletResponse res) throws Exception {		
	
		String pollTitle= ServletUtil.getParamString(req,"ePollTitle");	
		String description = ServletUtil.getParamString(req,"eDescription");
		String userID = LoginInfo.getUserID(req);
		String shareGroupID =  ServletUtil.getParamString(req,"selectedGroupID");
		String startTitle= ServletUtil.getParamString(req,"eStartTitle");
		String endTitle= ServletUtil.getParamString(req,"eEndTitle");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		String codeID = ServletUtil.getParamString(req,"eCodeID");
		String codeNo = ServletUtil.getParamString(req,"eCodeNo");
		
		String type = "insert"; // 설문 초기 생성의 경우 insert , 복사의 경우  작성자 변경을 위한 userID 값
		
		//설문 종료 조건
		String[] pollEndTypes =  ServletUtil.getParamStringArray(req, "ePollEndType"); 
		String pollEndType = "";
		if(pollEndTypes.length ==2){
			pollEndType="3";
		}else{
			pollEndType = pollEndTypes[0];
		}
		logger.info("pollEndType : "+pollEndType);
		
		
		//목표 응답수
		String aimAnswerCnt = ServletUtil.getParamString(req,"eAimAnswerCnt");
		if(aimAnswerCnt == null || aimAnswerCnt.equals(""))
		{
			aimAnswerCnt = "0";
		}
		logger.info("aimAnswerCnt : "+aimAnswerCnt);
		
		//설문마감일 설정 
		String pollEndDate = ServletUtil.getParamString(req,"ePollEndDate");
		String pollEndDateHH = ServletUtil.getParamString(req,"ePollEndDateHH");
		String pollEndDateMM = ServletUtil.getParamString(req,"ePollEndDateMM");
		
		if(!pollEndDate.equals("") && !pollEndDateHH.equals("") && !pollEndDateMM.equals("")){
			pollEndDate = pollEndDate+" "+pollEndDateHH+":"+pollEndDateMM+":00";
		}
		
		
		int pollTemplateID = ServletUtil.getParamInt(req,"ePollTemplateID","0");
		
		String pollTemplateHTML = pollService.showTemplateHTML(pollTemplateID);
		
		PollInfo pollInfo = new PollInfo();
		pollInfo.setPollTitle(pollTitle);
		pollInfo.setDescription(description);
		pollInfo.setShareGroupID(shareGroupID);
		pollInfo.setUserID(userID);
		pollInfo.setStartTitle(startTitle);
		pollInfo.setEndTitle(endTitle);
		pollInfo.setUseYN(useYN);
		pollInfo.setCodeID(codeID);
		pollInfo.setCodeNo(Integer.parseInt(codeNo));	
		pollInfo.setPollEndDate(pollEndDate); 
		pollInfo.setPollTemplateID(pollTemplateID);		
		pollInfo.setAimAnswerCnt(aimAnswerCnt);
		pollInfo.setPollEndType(pollEndType);
		
		int pollID = 0;
		String resultPollHTML = "";
		synchronized(this){
			if(pollService.insertPollInfo(pollInfo,type)>0){
				pollID = pollService.getMaxPollID();		
				resultPollHTML = makeHTMLByInfo(pollTemplateHTML,pollID);
				resultPollHTML = resultPollHTML.replace(Constant.POLL_TEMPLATE_TITLE, Constant.POLL_SUBJECT_START+pollTitle+Constant.POLL_SUBJECT_END);
				pollService.updateDefaultResultPollHTML(pollID, resultPollHTML, resultPollHTML);			
			}
		}		
		if(pollID>0){					
			 res.getWriter().print(pollID);
			 return null;		
		}else{		
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
	}

	
	/**
	 * <p>설문기본정보수정
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView updatePollInfo(HttpServletRequest req, HttpServletResponse res) throws Exception {		
	
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");		
		String pollTitle= ServletUtil.getParamString(req,"ePollTitle");		
		String description = ServletUtil.getParamString(req,"eDescription");
		String userID = LoginInfo.getUserID(req);
		String shareGroupID =  ServletUtil.getParamString(req,"selectedGroupID");
		String useYN = ServletUtil.getParamString(req,"eUseYN");		
		String resultFinishYN = ServletUtil.getParamString(req,"eResultFinishYN");
		String codeID = ServletUtil.getParamString(req,"eCodeID");
		String codeNo = ServletUtil.getParamString(req,"eCodeNo");	
		

		//설문 종료 조건
		String[] pollEndTypes =  ServletUtil.getParamStringArray(req, "ePollEndType"); 
		String pollEndType = "";
		if(pollEndTypes.length ==2){
			pollEndType="3";
		}else{
			pollEndType = pollEndTypes[0];
		}
		
		//목표 응답수
		String aimAnswerCnt = ServletUtil.getParamString(req,"eAimAnswerCnt");
		if(aimAnswerCnt == null || aimAnswerCnt.equals(""))
		{
			aimAnswerCnt = "0";
		}
		
		//설문마감일 설정 
		String pollEndDate = ServletUtil.getParamString(req,"ePollEndDate");
		String pollEndDateHH = ServletUtil.getParamString(req,"ePollEndDateHH");
		String pollEndDateMM = ServletUtil.getParamString(req,"ePollEndDateMM");
		
		if(!pollEndDate.equals("") && !pollEndDateHH.equals("") && !pollEndDateMM.equals("")){
			pollEndDate = pollEndDate+" "+pollEndDateHH+":"+pollEndDateMM+":00";
		}
				
		PollInfo pollInfo = new PollInfo();
		pollInfo.setPollID(pollID);
		pollInfo.setPollTitle(pollTitle);
		pollInfo.setDescription(description);
		pollInfo.setShareGroupID(shareGroupID);
		pollInfo.setUserID(userID);
		pollInfo.setUseYN(useYN);
		pollInfo.setResultFinishYN(resultFinishYN);
		pollInfo.setCodeID(codeID);
		pollInfo.setCodeNo(Integer.parseInt(codeNo));
		pollInfo.setPollEndDate(pollEndDate); 
		pollInfo.setAimAnswerCnt(aimAnswerCnt);
		pollInfo.setPollEndType(pollEndType);
	
		Map<String,Object> resultMap = pollService.showResultDefaultPollHTML(pollID);
	
	
		String defaultPollHTML = (String)resultMap.get("defaultPollHTML");
		String resultPollHTML = (String)resultMap.get("resultPollHTML");
		
		PollInfo pollInfo2 = pollService.viewPollInfo(pollID);
		
		
		defaultPollHTML = defaultPollHTML.replace(Constant.POLL_SUBJECT_START+pollInfo2.getPollTitle()+Constant.POLL_SUBJECT_END, Constant.POLL_SUBJECT_START+pollTitle+Constant.POLL_SUBJECT_END);		
		resultPollHTML = resultPollHTML.replace(Constant.POLL_SUBJECT_START+pollInfo2.getPollTitle()+Constant.POLL_SUBJECT_END, Constant.POLL_SUBJECT_START+pollTitle+Constant.POLL_SUBJECT_END);
		
		int resultVal1=0,resultVal2=0;
		
		resultVal1 = pollService.updatePollInfo(pollInfo);		
		resultVal2 = pollService.updateDefaultResultPollHTML(pollID, defaultPollHTML, resultPollHTML);
		
		
		
		
		if(resultVal1>0 && resultVal2>0){			
			
			this.sCurPage = "1"; 
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
	}
	
	
	
	/**
	 * <p>설문마감설정일 변경 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updatePollEndType(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");	
		//설문마감일 설정 
		String pollEndDate = ServletUtil.getParamString(req,"ePollEndDate");
		String pollEndDateHH = ServletUtil.getParamString(req,"ePollEndDateHH");
		String pollEndDateMM = ServletUtil.getParamString(req,"ePollEndDateMM");
		
		if(!pollEndDate.equals("") && !pollEndDateHH.equals("") && !pollEndDateMM.equals("")){
			pollEndDate = pollEndDate+" "+pollEndDateHH+":"+pollEndDateMM+":00";
		}
		
		//설문 종료 조건
		String[] pollEndTypes =  ServletUtil.getParamStringArray(req, "ePollEndType"); 
		String pollEndType = "";
		if(pollEndTypes.length ==2){
			pollEndType="3";
		}else{
			pollEndType = pollEndTypes[0];
		}
		
		//목표 응답수
		String aimAnswerCnt = ServletUtil.getParamString(req,"eAimAnswerCnt");
		if(aimAnswerCnt == null || aimAnswerCnt.equals(""))
		{
			aimAnswerCnt = "0";
		}
		int resultVal = pollService.updatePollEndType(pollID, pollEndType, pollEndDate, aimAnswerCnt);
		
		if(resultVal>0){		
			ServletUtil.messageGoURL(res,"설문 종료 조건이 변경되었습니다.","");
			return null;
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}	
	}
	
	
	/**
	 * <p>템플릿에 을 파싱하여 설문 초기화면 작성 
	 * @param pollTemplateHTML
	 * @param pollID
	 * @return
	 */
	private String makeHTMLByInfo(String pollTemplateHTML, int pollID){		
		
		String resultPollHTML = "";
		
		//시작문구처리 
		String startHTML = "\r\n<div>"
								+"\r\n"+Constant.POLL_STARTTITLE_START			
								+"\r\n&nbsp;"
								+"\r\n"+Constant.POLL_STARTTITLE_END		
								+"\r\n</div>";
		
		//마지막문구 처리 
		String endHTML = "\r\n<div>"
								+"\r\n"+Constant.POLL_ENDTITLE_START
								+"\r\n&nbsp;"
								+"\r\n"+Constant.POLL_ENDTITLE_END
								+"\r\n</div>";
		
		//시작문구 
		resultPollHTML = pollTemplateHTML.replace(Constant.POLL_TEMPLATE_HEAD, startHTML);
		
		
		//문항시작문구 처리 
		String questionHTML="\r\n<!-- #TM_QUESTION_START -->"									
									+"\r\n<!-- #TM_QUESTION_END -->";
									
		
		resultPollHTML = resultPollHTML.replace(Constant.POLL_TEMPLATE_QUESTIONS, questionHTML);
		
		//마지막문구 
		resultPollHTML = resultPollHTML.replace(Constant.POLL_TEMPLATE_TAIL, endHTML);
		
		return resultPollHTML;
	}
	
	/**
	 * <p>시작문구 수정 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateStartTitle(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");		
		String startTitle= ServletUtil.getParamString(req,"eStartTitle");
		startTitle = startTitle.replace("\r\n", "<br>");
				
		String resultPollHTML = "";
		PollInfo pollInfo = pollService.viewPollInfo(pollID);
		resultPollHTML = pollInfo.getResultPollHTML();
		
		resultPollHTML = replaceTitleHTML(resultPollHTML, Constant.POLL_STARTTITLE_START, Constant.POLL_STARTTITLE_END, startTitle);

		
		int resultVal = pollService.updateStartEndTitle("startTitle", pollID, startTitle);
	
		
		if(resultVal>0){	 
			if(pollService.updateDefaultResultPollHTML(pollID, resultPollHTML, resultPollHTML)<0){
				ServletUtil.messageGoURL(res,"저장에 실패했습니다","");				
			}
			return null;
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}	
	}
	
	
	
	/**
	 * <p>종료문구 수정 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateEndTitle(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");		
		String endTitle= ServletUtil.getParamString(req,"eEndTitle");
		endTitle = endTitle.replace("\r\n", "<br>");
		
		String resultPollHTML = "";
		PollInfo pollInfo = pollService.viewPollInfo(pollID);
		resultPollHTML = pollInfo.getResultPollHTML();

		resultPollHTML = replaceTitleHTML(resultPollHTML, Constant.POLL_ENDTITLE_START, Constant.POLL_ENDTITLE_END, endTitle);
		
		int resultVal = pollService.updateStartEndTitle("endTitle", pollID, endTitle);
		if(resultVal>0){	 
			if(pollService.updateDefaultResultPollHTML(pollID, resultPollHTML,  resultPollHTML)<0){
				ServletUtil.messageGoURL(res,"저장에 실패했습니다","");				
			}
			return null;
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}	
	}
	
	
	/**
	 * <p>시작/ 종료문구 변경 
	 * @param resultPollHTML
	 * @param titleStart
	 * @param titleEnd
	 * @param title
	 * @return
	 */
	private String replaceTitleHTML(String resultPollHTML, String titleStart, String titleEnd, String title){
		
		int start = resultPollHTML.indexOf(titleStart) +titleStart.length();
		int end = resultPollHTML.indexOf(titleEnd)+titleEnd.length();
		String replaceHTML = resultPollHTML.substring(start, end);
		
		
		resultPollHTML = resultPollHTML.replace(replaceHTML, "\r\n"+title+"\r\n"+titleEnd+"\r\n");
		
		return resultPollHTML;

	}
	
	/**
	 * <p>설문기본정보보기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editTitle(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		String columnName = ServletUtil.getParamString(req,"eColumnName");
		int pollTemplateID = ServletUtil.getParamInt(req,"ePollTemplateID","0");
		
		PollTemplate pollTemplage = pollService.viewPollTemplate(pollTemplateID);
		String templateHTML = pollTemplage.getPollTemplateHTML();
		String isExistTitle = "YES"; //시작문구 존재 유무 
		String isExistEnd = "YES";
		if(templateHTML.indexOf(Constant.POLL_TEMPLATE_HEAD)==-1){
			isExistTitle = "NO";
		}
		if(templateHTML.indexOf(Constant.POLL_TEMPLATE_TAIL)==-1){
			isExistEnd = "NO";
		}
		
		return new ModelAndView("/pages/content/poll/poll_title.jsp?method=edit&pollID="+pollID+"&columnName="+columnName+"&isExistTitle="+isExistTitle+"&isExistEnd="+isExistEnd,"pollInfo", pollService.viewStartEndTitle(pollID));		
	}
	
	
	/**
	 * <p>설문문항 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void insertQuestion(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		String questionType = ServletUtil.getParamString(req,"eQuestionType"); //질문유형 /1.단일질문 2.매트릭스		
		
		//일반 질문 
		if(questionType.equals("1")){
			insertQuestionNormal(req,res);
		}else{	//매트릭스 질문 
			insertQuestionMatrix(req,res);
		}
		
	}

	
	
	/**
	 * <p>설문문항 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertQuestionNormal(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");		
		String questionType = ServletUtil.getParamString(req,"eQuestionType"); //질문유형 /1.단일질문 2.매트릭스  
		String questionHead = ServletUtil.getParamString(req,"eQuestionHead"); //머릿말
		int questionNo = ServletUtil.getParamInt(req,"eQuestionNo","1"); //질문번호
		String questionText = ServletUtil.getParamString(req,"eQuestionText"); //질문내용
		String exampleType = ServletUtil.getParamString(req,"eExampleType"); //응답타입 //1: 객관식, 2:주관식, 3:순위선택
		String requiredYN = ServletUtil.getParamString(req,"eRequiredYN"); //필수응답여부 
		int examplePosition = ServletUtil.getParamInt(req,"eExamplePosition","1"); //보기정렬방식 
		
		if(!requiredYN.equals("Y")) requiredYN="N";
				
		String exampleGubun = ServletUtil.getParamString(req,"eExampleGubun");	 //1:단일응답, 2:복수응답
		int exampleMultiCount = ServletUtil.getParamInt(req,"eExampleMultiCount","0");  //복수응답시 최대허용개수 
		int exampleMultiMinimumCount = ServletUtil.getParamInt(req,"eExampleMultiMinimumCount","0");  //최소복수응답시 허용개수
		String eCodeType = ServletUtil.getParamString(req,"eCodeType");	//응답보기타입
		
		String[] exampleExs = ServletUtil.getParamStringArray(req,"eExampleEx");		//응답보기 주관식 여부가 체크된 번호 
		
		String questionFileURL = ServletUtil.getParamString(req,"eQuestionFileURL"); // 질문 멀티미디어 URL
		String questionLayoutType = ServletUtil.getParamString(req,"eQuestionLayoutType"); 	  // 질문 멀티미디어 레이아웃 TYPE
		
		if(pollID==0){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}
		
		
		String[] exampleDesc = null;
		String[] fileURLs = null;
		String[] layoutTypes = null;
		
		if(eCodeType.equals("CUSTOM")){
			if(exampleType.equals("3") || exampleType.equals("5")){
				exampleDesc = ServletUtil.getParamStringArray(req,"eRankingExampleDesc");	//응답내용(보기내용)
			}else{
				exampleDesc = ServletUtil.getParamStringArray(req,"eExampleDesc");	//응답내용(보기내용)
				fileURLs = ServletUtil.getParamStringArray(req,"eFileURL");
				layoutTypes = ServletUtil.getParamStringArray(req,"eLayoutType");
			}
		}else{
			exampleDesc = ServletUtil.getParamStringArray(req,"eCodeNo");	//응답내용(보기내용)			
			exampleGubun = "1"; //단일응답		
		}
		
		if(exampleType.equals("4")){
			examplePosition = ServletUtil.getParamInt(req,"eLimitCount","1"); //숫자입력에서 글자수 제한 값으로 사용
		}
		
		PollQuestion pollQuestion = new PollQuestion();
		pollQuestion.setPollID(pollID);							 
		pollQuestion.setQuestionType(questionType);
		pollQuestion.setQuestionHead(questionHead);
		pollQuestion.setQuestionNo(questionNo);
		pollQuestion.setQuestionText(questionText);
		pollQuestion.setExampleType(exampleType);
		pollQuestion.setExampleGubun(exampleGubun);
		pollQuestion.setExampleMultiCount(exampleMultiCount);
		pollQuestion.setExampleMultiMinimumCount(exampleMultiMinimumCount);
		pollQuestion.setRequiredYN(requiredYN);
		pollQuestion.setExamplePosition(examplePosition);
		if(questionFileURL !=null){
			pollQuestion.setFileURL(questionFileURL);
			pollQuestion.setLayoutType(questionLayoutType);
		}
		int resultVal = 0;
		int questionID = 0;
		
		synchronized(this){
			
			questionID = pollService.getMaxQuestionID(pollID);			
			pollQuestion.setQuestionID(questionID);		
			//문항입력 
			resultVal = pollService.insertPollQuestion(pollQuestion);
			if(resultVal<=0){
				ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
				return null;
			}
		
			
			PollExample[] pollExamples = null;
			
			//객관식일 경우 
			if(exampleType.equals("1")){
				pollExamples = new PollExample[exampleDesc.length];				
				
				for(int i=0;i<exampleDesc.length;i++){
					pollExamples[i] = new PollExample();
					pollExamples[i].setQuestionID(questionID);
					pollExamples[i].setPollID(pollID);
					pollExamples[i].setExampleID((i+1));
					pollExamples[i].setExampleDesc(exampleDesc[i]);				
					pollExamples[i].setExampleExYN("N");  //일단기본은 N
					if(fileURLs !=null){
						pollExamples[i].setFileURL(fileURLs[i]);
						pollExamples[i].setLayoutType(layoutTypes[i]);
					}
					pollExamples[i].setGoToQuestionNo(0);
					
					//보기 주관식이 있을 경우
					if(exampleExs!=null){
						for(int j=0;j<exampleExs.length;j++){
							if(pollExamples[i].getExampleID()==Integer.parseInt(exampleExs[j])){
								pollExamples[i].setExampleExYN("Y");	
								break;
							}
						}
					}					
					
					resultVal = pollService.insertPollExample(pollExamples[i]);			
				}
			}
			//순위선택일 경우 
			else if(exampleType.equals("3") || exampleType.equals("5")){
				pollExamples = new PollExample[exampleDesc.length];				
				
				for(int i=0;i<exampleDesc.length;i++){
					pollExamples[i] = new PollExample();
					pollExamples[i].setQuestionID(questionID);
					pollExamples[i].setPollID(pollID);
					pollExamples[i].setExampleID((i+1));
					pollExamples[i].setExampleDesc(exampleDesc[i]);				
					pollExamples[i].setExampleExYN("N");  //일단기본은 N
					pollExamples[i].setGoToQuestionNo(0);
										
					resultVal = pollService.insertPollExample(pollExamples[i]);			
				}
			}
			//숫자입력일 경우 
			else if(exampleType.equals("4")){
				
				pollExamples = new PollExample[1];
				pollExamples[0] = new PollExample();
				pollExamples[0].setQuestionID(questionID);
				pollExamples[0].setPollID(pollID);
				pollExamples[0].setExampleID(1);
				pollExamples[0].setExampleDesc(ServletUtil.getParamString(req,"eBeforeText")+"≠"+ServletUtil.getParamString(req,"eAfterText"));
				pollExamples[0].setExampleExYN("N");
				
				resultVal = pollService.insertPollExample(pollExamples[0]);	
			}
			//주관식일 경우 
			else{
				pollExamples = new PollExample[1];
				pollExamples[0] = new PollExample();
				pollExamples[0].setQuestionID(questionID);
				pollExamples[0].setPollID(pollID);
				pollExamples[0].setExampleID(1);
				pollExamples[0].setExampleDesc("주관식");	
				pollExamples[0].setExampleExYN("N");
				
				resultVal = pollService.insertPollExample(pollExamples[0]);		
			}
			
			pollQuestion.setQuestionID(questionID);
			
			String eAnswerTextType1 = ServletUtil.getParamString(req, "eAnswerTextType1"); // 주관식 문항 형식 구분
			String eAnswerTextType2 = ServletUtil.getParamString(req, "eAnswerTextType2"); // 숫자/문자 문항 형식 구분
			
			String answerTextType = "";
			
			if(eAnswerTextType1.equals("") || eAnswerTextType1 == null){
				answerTextType = eAnswerTextType2;				
			}else{
				answerTextType = eAnswerTextType1;
			}
			
			String questionHTML = makeHTMLByQuestionNormal(pollQuestion,pollExamples,"insert",answerTextType);
			PollInfo pollInfo = pollService.viewPollInfo(pollID) ;
			String resultPollHTML = pollInfo.getResultPollHTML();
			
		
			resultPollHTML = resultPollHTML.replace("<!-- #TM_QUESTION_END -->", questionHTML);
						
			resultVal = pollService.updateDefaultResultPollHTML(pollID, resultPollHTML, resultPollHTML); //실제 HTML업데이트 
		}
		
		
		if(resultVal>0){	 
			return null;
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
		
	}
	
	
	/**
	 * <p>설문문항 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertQuestionMatrix(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");		
		String questionType = ServletUtil.getParamString(req,"eQuestionType"); //질문유형 /1.단일질문 2.매트릭스
		String questionHead = ServletUtil.getParamString(req,"eQuestionHead"); //머릿말
		int questionNo = ServletUtil.getParamInt(req,"eQuestionNo","1"); //질문번호
		String questionText = ServletUtil.getParamString(req,"eQuestionText"); //질문내용
		String exampleType = ServletUtil.getParamString(req,"eExampleTypeMatrix"); //응답타입 //1: 주관식, 2;객관식
		String requiredYN = ServletUtil.getParamString(req,"eRequiredYNMatrix"); //필수응답여부 
		int matrixTextSize = ServletUtil.getParamInt(req,"eMatrixTextSize","10"); //질문번호
		
		if(!requiredYN.equals("Y")) requiredYN="Y";	//매트릭스는 무조건 필수응답임 

		if(pollID==0){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}
		
		String[] exampleDescMatrixX = ServletUtil.getParamStringArray(req,"eExampleDescMatrixX");  //가로 
		String[] exampleDescMatrixY = ServletUtil.getParamStringArray(req,"eExampleDescMatrixY");  //세로		
		
			
		PollQuestion pollQuestion = new PollQuestion();
		pollQuestion.setPollID(pollID);			 
		pollQuestion.setQuestionType(questionType);
		pollQuestion.setQuestionHead(questionHead);
		pollQuestion.setQuestionNo(questionNo);
		pollQuestion.setQuestionText(questionText);
		pollQuestion.setExampleType(exampleType);
		pollQuestion.setExampleGubun("0");	 //매트릭스에서는 불필요함으로 그냥 0으로 세팅 
		pollQuestion.setExampleMultiCount(0);		//매트릭스에서는 불필요함으로 그냥 0으로 세팅 		
		pollQuestion.setRequiredYN(requiredYN);
		pollQuestion.setExamplePosition(0);  //매트릭스에서는 불필요함으로 그냥 0으로 세팅 
		pollQuestion.setMatrixTextSize(matrixTextSize);		//매트릭스 주관식일 경우 0 아닌 텍스트박스 길이값
			
		int resultVal = 0;
		int questionID = 0;
		
	
		synchronized(this){
			
			questionID = pollService.getMaxQuestionID(pollID);		
			pollQuestion.setQuestionID(questionID);					
			
			//문항입력 
			resultVal = pollService.insertPollQuestion(pollQuestion);
			
			if(resultVal<=0){
				ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
				return null;
			}
			
		
						
			
			PollExample[] pollExamplesMatrixX = null;
			PollExample[] pollExamplesMatrixY = null;
			
			pollExamplesMatrixX = new PollExample[exampleDescMatrixX.length];
			pollExamplesMatrixY = new PollExample[exampleDescMatrixY.length];
				
			for(int i=0;i<exampleDescMatrixX.length;i++){
				pollExamplesMatrixX[i] = new PollExample();
				pollExamplesMatrixX[i].setQuestionID(questionID);
				pollExamplesMatrixX[i].setPollID(pollID);
				pollExamplesMatrixX[i].setExampleID((i+1));
				pollExamplesMatrixX[i].setMatrixXY("X");
				pollExamplesMatrixX[i].setExampleDesc(exampleDescMatrixX[i]);				
				pollExamplesMatrixX[i].setExampleExYN("N");  //일단기본은 N					
				pollExamplesMatrixX[i].setGoToQuestionNo(0);					
				resultVal = pollService.insertPollExample(pollExamplesMatrixX[i]);					
			}// end for (matrix X)
				
			for(int i=0;i<exampleDescMatrixY.length;i++){
				pollExamplesMatrixY[i] = new PollExample();
				pollExamplesMatrixY[i].setQuestionID(questionID);
				pollExamplesMatrixY[i].setPollID(pollID);
				pollExamplesMatrixY[i].setExampleID((i+1));
				pollExamplesMatrixY[i].setMatrixXY("Y");
				pollExamplesMatrixY[i].setExampleDesc(exampleDescMatrixY[i]);				
				pollExamplesMatrixY[i].setExampleExYN("N");  //일단기본은 N					
				pollExamplesMatrixY[i].setGoToQuestionNo(0);					
				resultVal = pollService.insertPollExample(pollExamplesMatrixY[i]);					
			}// end for (matrix Y)
					
		
			
			pollQuestion.setQuestionID(questionID);
			String questionHTML = makeHTMLByQuestionMatrix(pollQuestion,pollExamplesMatrixX, pollExamplesMatrixY,"insert");
			
			
			PollInfo pollInfo = pollService.viewPollInfo(pollID) ;
			String resultPollHTML = pollInfo.getResultPollHTML();
			
		
			resultPollHTML = resultPollHTML.replace("<!-- #TM_QUESTION_END -->", questionHTML);
						
			resultVal = pollService.updateDefaultResultPollHTML(pollID, resultPollHTML, resultPollHTML); //실제 HTML업데이트 
			
		}
		
		
		if(resultVal>0){	 
			return null;
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
		
	}
	
	

	/**
	 * <p>일반질문에 따른 HTML 만들기 
	 * @param pollQuestion
	 * @param exampleDesc
	 * @return
	 */
	private String makeHTMLByQuestionNormal(PollQuestion pollQuestion , PollExample[] pollExamples, String flag, String answerTextType){	
		
		String optionHTML = "";		
		String endHTML = "";
		//삽입이라면 
		if(flag.equals("insert")){
			endHTML = "\r\n<!-- #TM_QUESTION_END -->";
		}else{
			endHTML = "";
		}
		
		//복수응답이 가능할 경우 
		if(pollQuestion.getExampleMultiMinimumCount()>0){
			optionHTML = " (복수응답 : "+pollQuestion.getExampleMultiMinimumCount()+" 개 이상, "+pollQuestion.getExampleMultiCount()+" 개 이하)";
		}else if(pollQuestion.getExampleGubun().equals("2")){
			optionHTML = " (복수응답 : "+pollQuestion.getExampleMultiCount()+" 개 이하)";
		}
		
		int examplePosition = pollQuestion.getExamplePosition();
		//순위선택일 경우
		if(pollQuestion.getExampleType().equals("3")){
			examplePosition = 2;
		}
		//숫자 입력
		if(pollQuestion.getExampleType().equals("4")){
			examplePosition = 1;
		}
		
		String questionHTML = 	 "\r\n<!-- #TM_QUESTION_START_"+pollQuestion.getQuestionID()+" -->"
										+"\r\n<a name=\""+pollQuestion.getQuestionNo()+"\"></a>"
										+ "\r\n<br><div>" 
										+ "\r\n<table>"
										+ "\r\n<tr><td colspan=\""+examplePosition+"\"><font size=\"4\"><b>"+pollQuestion.getQuestionHead().replace("\n", "<br>")+"</b></font></td></tr>";
				if(pollQuestion.getFileURL() != null && !pollQuestion.getFileURL().equals("") && pollQuestion.getLayoutType().equals("2")){
					String tagName="img ";
					if(pollQuestion.getFileURL() .indexOf(".swf") > -1 || pollQuestion.getFileURL() .indexOf(".SWF") > -1){
						tagName = "embed pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' ";
					}
					questionHTML += "\r\n<tr><td colspan=\""+examplePosition+"\"><"+tagName+" src='"+pollQuestion.getFileURL()+"'></td></tr>" ;
				}
			   //questionHTML +=  	"\r\n<tr><td colspan=\""+examplePosition+"\">"
				//			+      "<table><tr><td valign=\"top\"><b>["+Constant.POLL_QUESTION_DES+pollQuestion.getQuestionNo()+"]</b></td><td align=\"left\">";
		//필수 항목일 경우
		if(pollQuestion.getRequiredYN().equals("Y")){
			questionHTML += "\r\n<tr><td colspan=\""+examplePosition+"\">"
					+		"<table><tr><td valign=\"top\" width=\"85\"><b>["+Constant.POLL_QUESTION_DES+pollQuestion.getQuestionNo()+"]</b>&nbsp&nbsp" 
					+		"<img src=\"/images/quest_notnull.gif\" align=\"top\"></td>";
			questionHTML +=	"<td>&nbsp"+pollQuestion.getQuestionText().replace("\n", "<br>")+optionHTML+"</td></tr></table></td></tr>";
		}else if(pollQuestion.getExampleType().equals("2")){
			questionHTML += "\r\n<tr><td colspan=\""+examplePosition+"\">"
					+		"<table><tr><td valign=\"top\" width=\"55\"><b>["+Constant.POLL_QUESTION_DES+pollQuestion.getQuestionNo()+"]</b></td>";
			questionHTML +=	"<td>&nbsp"+pollQuestion.getQuestionText().replace("\n", "<br>")+optionHTML+"</td></tr></table></td></tr>";				
		}else{			
			questionHTML += "\r\n<tr><td colspan=\""+examplePosition+"\">"
					+		"<table><tr><td valign=\"top\" width=\"55\"><b>["+Constant.POLL_QUESTION_DES+pollQuestion.getQuestionNo()+"]</b></td>";
			questionHTML +=	"<td>&nbsp"+pollQuestion.getQuestionText().replace("\n", "<br>")+optionHTML+"</td><td><a href=\"javascript:checkreset("+pollQuestion.getQuestionID()+")\" style =\"cursor:pointer\"><img src=\"/images/quest_checkreset.gif\" border=\'0\' align=\"bottom\"></a></td></tr></table></td></tr>";
		}
		
		if(pollQuestion.getFileURL() != null && !pollQuestion.getFileURL().equals("") && pollQuestion.getLayoutType().equals("1")){
			String tagName="img ";
			if(pollQuestion.getFileURL() .indexOf(".swf") > -1 || pollQuestion.getFileURL() .indexOf(".SWF") > -1){
				tagName = "embed pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' ";
			}
			questionHTML += "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\"><"+tagName+" src='"+pollQuestion.getFileURL()+"'></td></tr>" ;
		}
		//히든값을 넣자.
		String questionInfoHTML = "\r\n<input type=\"hidden\" name=\"questionType_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getQuestionType()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"requiredYN_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getRequiredYN()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"tempRequiredYN_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getRequiredYN()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"exampleType_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getExampleType()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"exampleGubun_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getExampleGubun()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"exampleCount_"+pollQuestion.getQuestionID()+"\" value=\""+pollExamples.length+"\">"
									+ "\r\n<input type=\"hidden\" name=\"exampleMultiCount_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getExampleMultiCount()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"exampleMultiMinimumCount_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getExampleMultiMinimumCount()+"\">";
								
			
		//답변이  주관식일 경우 
		if(pollQuestion.getExampleType().equals("2")){
			if(answerTextType.equals("1")){
				questionHTML+="\r\n<tr><td><input type=\"text\" name=\"exampleType_text_"+pollQuestion.getQuestionID()+"\" size=\"40\" maxlength=\"20\"></td></tr>"
					+"\r\n</table>"
					+"\r\n"+questionInfoHTML
					+"\r\n</div>"
				+"\r\n<!-- #TM_QUESTION_END_"+pollQuestion.getQuestionID()+" -->"+endHTML;
			}else{
				questionHTML+="\r\n<tr><td><textarea name=\"exampleType_text_"+pollQuestion.getQuestionID()+"\" rows=\"5\", cols=\"80\"></textarea></td></tr>"
					 					+"\r\n</table>"
					 					+"\r\n"+questionInfoHTML
					 					+"\r\n</div>"
										+"\r\n<!-- #TM_QUESTION_END_"+pollQuestion.getQuestionID()+" -->"+endHTML;
			}
		}
		//객관식일 경우 
		else if(pollQuestion.getExampleType().equals("1")){
			
			for(int i=0;i<pollExamples.length;i++){	
				String inputTypeHTML = "";	
				String divStartHTML = "";	
				String divEndHTML = "";	
				//단일응답
				if(pollQuestion.getExampleGubun().equals("1")){
					inputTypeHTML = "<input type=\"radio\" name=\"exampleGubun_single_"+pollQuestion.getQuestionID()+"\" value=\""+ pollExamples[i].getExampleID()+"\" onclick=\"javascript:goToQuestionNum("+pollQuestion.getQuestionID()+","+pollExamples[i].getExampleID()+")\">";
				//복수응답	
				}else{
					inputTypeHTML = "<input type=\"checkbox\" name=\"exampleGubun_multi_"+pollQuestion.getQuestionID()+"_"+pollExamples[i].getExampleID()+"\" value=\""+ pollExamples[i].getExampleID()+"\">";
				}
				String tagName="img ";
				if(pollExamples[i].getFileURL() != null && !pollExamples[i].getFileURL().equals("")){
					if(pollExamples[i].getFileURL().indexOf(".swf") > -1 || pollExamples[i].getFileURL().indexOf(".SWF") > -1){
						tagName = "embed pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' ";
					}
				}
				//멀티미디어 처리
				if(pollExamples[i].getFileURL() != null && !pollExamples[i].getFileURL().equals("") && pollExamples[i].getLayoutType() != null && pollExamples[i].getLayoutType().equals("2")){
					inputTypeHTML = "\r\n<div style='clear:both;text-align:center'><"+tagName+" src='"+pollExamples[i].getFileURL()+"'>\r\n</div><div>"+inputTypeHTML;
					divStartHTML="\r\n<div style='clear:both;float:left;'>";
					divEndHTML="\r\n</div></div>";
				}
				if(pollExamples[i].getFileURL() != null && !pollExamples[i].getFileURL().equals("") && pollExamples[i].getLayoutType() != null && pollExamples[i].getLayoutType().equals("3")){
					inputTypeHTML = "\r\n<div style='float:left;'><"+tagName+" src='"+pollExamples[i].getFileURL()+"'></div>\r\n<div>"+inputTypeHTML;
					divStartHTML="\r\n<div style='clear:both;float:left;text-align:center;'>";
					divEndHTML="\r\n</div></div>";
				}
				
				if(pollExamples[i].getFileURL() != null && !pollExamples[i].getFileURL().equals("") && pollExamples[i].getLayoutType() != null && pollExamples[i].getLayoutType().equals("1")){
					divStartHTML="<div style='clear:both;float:left;'><div>";
					divEndHTML= "</div><div style='clear:both;text-align:center'><"+tagName+" src='"+pollExamples[i].getFileURL()+"'></div></div>";
				}
				
				if(pollExamples[i].getFileURL() != null && !pollExamples[i].getFileURL().equals("") && pollExamples[i].getLayoutType() != null && pollExamples[i].getLayoutType().equals("4")){
					divStartHTML="<div style='clear:both;float:left;text-align:center;'><div style='float:left;'>";
					divEndHTML= "</div><div><"+tagName+" src='"+pollExamples[i].getFileURL()+"'></div></div>";
				}
				
				if(i==0){
					questionHTML+="\r\n<tr>";
				}
				
				
				
				questionHTML+="\r\n<td>"+divStartHTML+inputTypeHTML+(i+1)+".&nbsp&nbsp"+pollExamples[i].getExampleDesc()+"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;"+divEndHTML;
				//주관식이 포함된 객관식이라면 
				if(pollExamples[i].getExampleExYN().equals("Y")){
					questionHTML+="<input type=\"text\"  name=\"exampleExYN_"+pollQuestion.getQuestionID()+"_"+pollExamples[i].getExampleID()+"\""+" size=\"50\">";
				}
				
				
				questionHTML+="\r\n</td>";
				
				if( (i+1) % pollQuestion.getExamplePosition()==0){
					questionHTML+="\r\n</tr>\r\n<tr>";	
				}
					
				//마지막처리 
				if( (i+1)==pollExamples.length){
					if( ((i+1) % pollQuestion.getExamplePosition())!=0){ //나누어 떨어져 지지않는 마지막 처리 
						questionHTML+="\r\n</tr>";
					}
				}	
					
			}// end for							
				questionHTML+="\r\n</table>\r\n" +questionInfoHTML+"\r\n</div>"									
									+"\r\n<!-- #TM_QUESTION_END_"+pollQuestion.getQuestionID()+" -->"+endHTML;									
		}
		//순위선택일 경우 
		else if(pollQuestion.getExampleType().equals("3")){
			String inputTypeHTML = "";	
			for(int i=0;i<pollExamples.length;i++){
				inputTypeHTML = "<select name=\"exampleGubun_select_"+pollQuestion.getQuestionID()+"_"+pollExamples[i].getExampleID()+"\"  onChange=\"javascript:checkRankingSelection(this,'exampleGubun_select_"+pollQuestion.getQuestionID()+"_', "+pollExamples[i].getExampleID()+", "+pollExamples.length+")\">"
							  + "<option value=\"\">-- 순위선택 --</option>";
				for(int j=1;j<=pollExamples.length;j++){
					inputTypeHTML +="\r\n <option value=\""+j+"\">"+j+" 순위</option>";
				}
				inputTypeHTML +="\r\n </select>";
				if(i==0){
					questionHTML+="\r\n<tr>";
				}
				questionHTML+="\r\n<td>"+(i+1)+"."+pollExamples[i].getExampleDesc()+"</td><td>"+inputTypeHTML+"</td>";
				
				if( (i+1) % pollQuestion.getExamplePosition()==0){
					questionHTML+="\r\n</tr>\r\n<tr>";	
				}
					
				//마지막처리 
				if( (i+1)==pollExamples.length){
					if( ((i+1) % pollQuestion.getExamplePosition())!=0){ //나누어 떨어져 지지않는 마지막 처리 
						questionHTML+="\r\n</tr>";
					}
				}	
			}// end for	
			questionHTML+="\r\n</table>\r\n" +questionInfoHTML+"\r\n</div>"									
			+"\r\n<!-- #TM_QUESTION_END_"+pollQuestion.getQuestionID()+" -->"+endHTML;	
		}
		//숫자입력일 경우 
		else if(pollQuestion.getExampleType().equals("4")){
			String beforeText =pollExamples[0].getExampleDesc().substring(0, pollExamples[0].getExampleDesc().indexOf("≠"));
			String afterText =pollExamples[0].getExampleDesc().substring(pollExamples[0].getExampleDesc().indexOf("≠")+1);
			
			questionHTML+="\r\n<tr><td>"+beforeText+" <input type=\"\" name=\"exampleType_text_"+pollQuestion.getQuestionID()+"\" size=\"30\" ";
			
			if(answerTextType.equals("1")){
				questionHTML+= " onKeyUp=\"javascript:onlyNumber(this,"+pollQuestion.getExamplePosition()+")\" />"+afterText;
			}else if(answerTextType.equals("2")){
				questionHTML+= " maxlength="+pollQuestion.getExamplePosition()+" />"+afterText;
			}
			
			if(pollQuestion.getExamplePosition() > 0){
				if(answerTextType.equals("1")){
					questionHTML+=" <br/> * 최대 입력 가능한 값은 "+pollQuestion.getExamplePosition()+afterText+"입니다.";
				}else if(answerTextType.equals("2")){
					questionHTML+=" <br/> * 최대 입력 가능한 문자 수는 "+pollQuestion.getExamplePosition()+"자 입니다.";
				}
			}
			
			questionHTML+="</td></tr>"
				+"\r\n</table>"
				+"\r\n"+questionInfoHTML
				+"\r\n</div>"
			+"\r\n<!-- #TM_QUESTION_END_"+pollQuestion.getQuestionID()+" -->"+endHTML;
		}
		//척도형일 경우
		else if(pollQuestion.getExampleType().equals("5")){
			String barAddHTML = "\r\n<tr height=\"10\">\r\n<td width=\"50\"></td>"; 
			String radioAddHTML = "\r\n<tr>";
			for(int i=0;i<pollExamples.length;i++){
				if(i==0){
					questionHTML+="\r\n<tr><td><table border=\"0\" cellSpacing=\"0\" cellPadding=\"0\"><tr>";
				}else{
					barAddHTML +="\r\n<td width=\"4\" background=\"/images/scale_bar.gif\" style=\"background-repeat: repeat-x;\"/></td>"
				        +"\r\n<td width=\"50\" background=\"/images/scale_bg.gif\" style=\"background-repeat: repeat-x;\"></td>"
				        +"\r\n<td width=\"50\" background=\"/images/scale_bg.gif\" style=\"background-repeat: repeat-x;\"></td>";
				}
				
				questionHTML +="\r\n<td style=\"text-align: center;\" colSpan=\"3\">"+pollExamples[i].getExampleDesc()+"</td>";
				
				radioAddHTML +="\r\n<td style=\"text-align: center;\" colSpan=\"3\"><input type=\"radio\" name=\"exampleGubun_single_"+pollQuestion.getQuestionID()+"\" value=\""+ pollExamples[i].getExampleID()+"\" /></td>";
				
			}
			barAddHTML += "\r\n<td width=\"4\" background=\"/images/scale_bar.gif\" style=\"background-repeat: repeat-x;\"/></td>"
			            + "\r\n<td width=\"50\"></td>"
			            + "</tr>";
			
			questionHTML+="\r\n</tr>"+barAddHTML+radioAddHTML+"\r\n</tr></table></td></tr>"
						+"\r\n</table>\r\n" +questionInfoHTML+"\r\n</div>"									
						+"\r\n<!-- #TM_QUESTION_END_"+pollQuestion.getQuestionID()+" -->"+endHTML;	
			
		}
		
		return questionHTML;
		
	}
	
	
	/**
	 * <p>매트릭스질문에 따른 HTML 만들기 
	 * @param pollQuestion
	 * @param exampleDesc
	 * @return
	 */
	private String makeHTMLByQuestionMatrix(PollQuestion pollQuestion , PollExample[] pollExamplesMatrixX, PollExample[] pollExamplesMatrixY, String flag){	
		String optionHTML = "";		
		String endHTML = "";
		//삽입이라면 
		if(flag.equals("insert")){
			endHTML = "\r\n<!-- #TM_QUESTION_END -->";
		}else{
			endHTML = "";
		}
		String questionHTML = 	 "\r\n<!-- #TM_QUESTION_START_"+pollQuestion.getQuestionID()+" -->"
									+"\r\n<a name=\""+pollQuestion.getQuestionNo()+"\"></a>"
									+ "\r\n<br><div>" 
									+ "\r\n<table>" 
									+ "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\"><font size=\"4\"><b>"+pollQuestion.getQuestionHead().replace("\n", "<br>")+"</b></font></td></tr>" 
									+ "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\">"
									+"<table><tr><td valign=\"top\" width=\"85\"><b>["+Constant.POLL_QUESTION_DES+pollQuestion.getQuestionNo()+"]</b>&nbsp&nbsp<img src=\"/images/quest_notnull.gif\" align=\"top\"></td><td>&nbsp"+ pollQuestion.getQuestionText().replace("\n", "<br>")+optionHTML+"</td><tr></table></tr>";

		//히든값을 넣자.
		String questionInfoHTML = "\r\n<input type=\"hidden\" name=\"questionType_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getQuestionType()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"requiredYN_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getRequiredYN()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"exampleType_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getExampleType()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"exampleGubun_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getExampleGubun()+"\">"
									+ "\r\n<input type=\"hidden\" name=\"exampleMultiCount_"+pollQuestion.getQuestionID()+"\" value=\""+pollQuestion.getExampleMultiCount()+"\">";
		
		
		String inputTypeHTML = "";
		//객관식일 경우 			
	
		
		questionHTML += "\r\n<tr><td>"
							+"\r\n<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\">";
		
		
		//첫번째 row 
		questionHTML += "\r\n<tr><td style=\"background-color:#BED6ED;\">&nbsp;</td>";
		for(int j=0;j<pollExamplesMatrixY.length;j++){
			questionHTML += "<td align=\"center\" style=\"background-color:#BED6ED;\">&nbsp;"+pollExamplesMatrixY[j].getExampleDesc()+"&nbsp;</td>"	;
		}
		questionHTML += "</tr>";
		
		//두번째 row 부터 ~~
		for(int i=0;i<pollExamplesMatrixX.length;i++){
			questionHTML += "\r\n<tr><td style=\"background-color:#BED6ED;\">&nbsp;"+ pollExamplesMatrixX[i].getExampleDesc() +"&nbsp;</td>";
			for(int j=0;j<pollExamplesMatrixY.length;j++){				
				//객관일 경우 radio
				if(pollQuestion.getExampleType().equals("1")){
					inputTypeHTML = "<input type=\"radio\" name=\"exampleGubun_matrixRadio_"+pollQuestion.getQuestionID()+"_"+pollExamplesMatrixX[i].getExampleID()+"\" value=\""+ pollExamplesMatrixX[i].getExampleID()+":"+pollExamplesMatrixY[j].getExampleID()+"\">";
				}
				//주관식일 경우 text 
				else{
					inputTypeHTML = "<input type=\"text\" name=\"exampleGubun_matrixText_"+pollQuestion.getQuestionID()+"_"+pollExamplesMatrixX[i].getExampleID()+"_"+pollExamplesMatrixY[j].getExampleID()+"\"  size=\""+ pollQuestion.getMatrixTextSize()+"\">";
				}				
				questionHTML += "<td align=\"center\">"+inputTypeHTML+"</td>"	;	
			}
			questionHTML += "</tr>";
			
		}//end for 
		questionHTML+="\r\n</table>\r\n"
							+"\r\n</td></tr></tbody></table>"
							+questionInfoHTML+"\r\n</div>"									
							+"\r\n<!-- #TM_QUESTION_END_"+pollQuestion.getQuestionID()+" -->"+endHTML;			
		
		return questionHTML;
		
	}
	
	/**
	 * <p>설문 문항 수정 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void updateQuestion(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String questionType = ServletUtil.getParamString(req,"eQuestionType"); //질문유형 /1.단일질문 2.매트릭스		
		
		//일반 질문 
		if(questionType.equals("1")){
			updateQuestionNormal(req,res);
		}else{	//매트릭스 질문 
			updateQuestionMatrix(req,res);
		}
		
	}
	
	
	/**
	 * <p>설문 문항 수정 -일반 질문
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateQuestionNormal(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		int questionID = ServletUtil.getParamInt(req,"eQuestionID","0");	
		String questionType = ServletUtil.getParamString(req,"eQuestionType"); //질문유형 /1.단일질문 2.일반매트릭스,3.복합매트릭스
		String questionHead = ServletUtil.getParamString(req,"eQuestionHead"); //머릿말
		int questionNo = ServletUtil.getParamInt(req,"eQuestionNo","1"); //질문번호
		String questionText = ServletUtil.getParamString(req,"eQuestionText"); //질문내용
		String exampleType = ServletUtil.getParamString(req,"eExampleType"); //응답타입 //1:객관식, 2:주관식, 3:순위선택
		String requiredYN = ServletUtil.getParamString(req,"eRequiredYN"); //필수응답여부 
		if(!requiredYN.equals("Y")) requiredYN="N";
		int examplePosition = ServletUtil.getParamInt(req,"eExamplePosition","1"); //보기정렬방식
		String eCodeType = ServletUtil.getParamString(req,"eCodeType");	//응답보기타입		
		String exampleGubun = ServletUtil.getParamString(req,"eExampleGubun");	 //1:단일응답, 2:복수응답
		int exampleMultiCount = ServletUtil.getParamInt(req,"eExampleMultiCount","0");  //복수응답시 허용개수 
		int exampleMultiMinimumCount = ServletUtil.getParamInt(req,"eExampleMultiMinimumCount","0");  //최소복수응답시 허용개수
		String[] exampleExs = ServletUtil.getParamStringArray(req,"eExampleEx");		//응답보기 주관식 여부가 체크된 번호 
		
		String questionFileURL = ServletUtil.getParamString(req,"eQuestionFileURL"); // 질문 멀티미디어 URL
		String questionLayoutType = ServletUtil.getParamString(req,"eQuestionLayoutType"); 	  // 질문 멀티미디어 레이아웃 TYPE
		
		if(pollID==0 || questionID==0){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}
			 

		String[] exampleDesc = null;
		String[] fileURLs = null;
		String[] layoutTypes = null;
		if(eCodeType.equals("CUSTOM")){
			if(exampleType.equals("3") || exampleType.equals("5")){
				exampleDesc = ServletUtil.getParamStringArray(req,"eRankingExampleDesc");	//응답내용(보기내용)
			}else{
				exampleDesc = ServletUtil.getParamStringArray(req,"eExampleDesc");	//응답내용(보기내용)
				fileURLs = ServletUtil.getParamStringArray(req,"eFileURL");
				layoutTypes = ServletUtil.getParamStringArray(req,"eLayoutType");
			}
		}else{
			exampleDesc = ServletUtil.getParamStringArray(req,"eCodeNo");	//응답내용(보기내용)			
			exampleGubun = "1"; //단일응답		
		}
		

		if(exampleType.equals("4")){
			examplePosition = ServletUtil.getParamInt(req,"eLimitCount","1"); //숫자입력에서 글자수 제한 값으로 사용
		}
		
		PollQuestion pollQuestion = new PollQuestion();
		pollQuestion.setPollID(pollID);
		pollQuestion.setQuestionID(questionID);
		pollQuestion.setQuestionNo(questionNo);
		pollQuestion.setQuestionType(questionType);
		pollQuestion.setQuestionHead(questionHead);		
		pollQuestion.setQuestionText(questionText);
		pollQuestion.setExampleType(exampleType);
		pollQuestion.setExampleGubun(exampleGubun);
		pollQuestion.setExampleMultiCount(exampleMultiCount);	
		pollQuestion.setExampleMultiMinimumCount(exampleMultiMinimumCount);
		pollQuestion.setRequiredYN(requiredYN);			
		pollQuestion.setExamplePosition(examplePosition);
		if(questionFileURL !=null){
				pollQuestion.setFileURL(questionFileURL);
				pollQuestion.setLayoutType(questionLayoutType);
		}
		PollExample[] pollExamples = null;
		
		//객관식인 경우 
		if(exampleType.equals("1")){
			pollExamples = new PollExample[exampleDesc.length];	
			for(int i=0;i<exampleDesc.length;i++){
				pollExamples[i] = new PollExample();
				pollExamples[i].setQuestionID(questionID);
				pollExamples[i].setPollID(pollID);
				pollExamples[i].setExampleID(i+1);
				pollExamples[i].setExampleDesc(exampleDesc[i]);
				pollExamples[i].setExampleExYN("N");  //일단기본은 N
				if (fileURLs == null && layoutTypes == null) {
					pollExamples[i].setFileURL("");
					pollExamples[i].setLayoutType("");
				} else {
					pollExamples[i].setFileURL(fileURLs[i]);
					pollExamples[i].setLayoutType(layoutTypes[i]);
				}
				//보기 주관식이 있을 경우 
				if(exampleExs!=null){
					for(int j=0;j<exampleExs.length;j++){
						if(pollExamples[i].getExampleID()==Integer.parseInt(exampleExs[j])){
							pollExamples[i].setExampleExYN("Y");	
							break;
						}
					}
				}	
				pollExamples[i].setGoToQuestionNo(0);
			}
		}
		//순위선택일 경우 
		else if(exampleType.equals("3") || exampleType.equals("5")){
			pollExamples = new PollExample[exampleDesc.length];	
			for(int i=0;i<exampleDesc.length;i++){
				pollExamples[i] = new PollExample();
				pollExamples[i].setQuestionID(questionID);
				pollExamples[i].setPollID(pollID);
				pollExamples[i].setExampleID(i+1);
				pollExamples[i].setExampleDesc(exampleDesc[i]);
				pollExamples[i].setExampleExYN("N"); 
				pollExamples[i].setGoToQuestionNo(0);
			}
		}
		//숫자입력일 경우 
		else if(exampleType.equals("4")){
			
			pollExamples = new PollExample[1];
			pollExamples[0] = new PollExample();
			pollExamples[0].setQuestionID(questionID);
			pollExamples[0].setPollID(pollID);
			pollExamples[0].setExampleID(1);
			pollExamples[0].setExampleDesc(ServletUtil.getParamString(req,"eBeforeText")+"≠"+ServletUtil.getParamString(req,"eAfterText"));
			pollExamples[0].setExampleExYN("N");
		}
		//주관식인 경우 
		else{
			pollExamples = new PollExample[1];	
			pollExamples[0] = new PollExample();
			pollExamples[0].setQuestionID(questionID);
			pollExamples[0].setPollID(pollID);
			pollExamples[0].setExampleID(1);
			pollExamples[0].setExampleDesc("주관식");
			pollExamples[0].setExampleExYN("N");
		}
			
	
		Map<String,Object> resultMap = pollService.showResultDefaultPollHTML(pollID);
		String resultPollHTML = (String)resultMap.get("resultPollHTML");

			
		//기존 질문문항 html 부분을 찾는다. 
		String startHtml = "<!-- #TM_QUESTION_START_"+pollQuestion.getQuestionID()+" -->";
		String endHtml  = "<!-- #TM_QUESTION_END_"+pollQuestion.getQuestionID()+" -->";
		
		int startIdx = resultPollHTML.indexOf(startHtml);
		int endIdx = resultPollHTML.indexOf(endHtml)+ endHtml.length();
		
		String oldQuestionHTML = resultPollHTML.substring(startIdx, endIdx);
			
		if(oldQuestionHTML==null || oldQuestionHTML.equals("")){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다 \r\n기존에 저장된 질문을 읽어 올수 없습니다.","");
			return null;	
		}
		

		String eAnswerTextType1 = ServletUtil.getParamString(req, "eAnswerTextType1"); // 주관식 문항 형식 구분
		String eAnswerTextType2 = ServletUtil.getParamString(req, "eAnswerTextType2"); // 숫자/문자 문항 형식 구분

		String answerTextType = "";
		
		if(eAnswerTextType1.equals("") || eAnswerTextType1 == null){
			answerTextType = eAnswerTextType2;
		}else{
			answerTextType = eAnswerTextType1;
		}
		
		//새로 만듬 
		String newQuestionHTML = makeHTMLByQuestionNormal(pollQuestion,pollExamples,"update",answerTextType);	
		
		//새것으로 교체 
		resultPollHTML = resultPollHTML.replace(oldQuestionHTML, newQuestionHTML);
				
		int resultVal = pollService.updatePollQuestionExample(pollQuestion, pollExamples, resultPollHTML);
		
		if(resultVal>0){			
			return null;
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
	}
	
	
	/**
	 * <p>설문 문항 수정 - 매트릭스 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateQuestionMatrix(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		int questionID = ServletUtil.getParamInt(req,"eQuestionID","0");	
		String questionType = ServletUtil.getParamString(req,"eQuestionType"); //질문유형 /1.단일질문 2.일반매트릭스,3.복합매트릭스
		String questionHead = ServletUtil.getParamString(req,"eQuestionHead"); //머릿말
		int questionNo = ServletUtil.getParamInt(req,"eQuestionNo","1"); //질문번호
		String questionText = ServletUtil.getParamString(req,"eQuestionText"); //질문내용
		String exampleType = ServletUtil.getParamString(req,"eExampleTypeMatrix"); //응답타입 //1: 주관식, 2;객관식
		String requiredYN = ServletUtil.getParamString(req,"eRequiredYNMatrix"); //필수응답여부 
		int matrixTextSize = ServletUtil.getParamInt(req,"eMatrixTextSize","10"); //질문번호
		if(!requiredYN.equals("Y")) requiredYN="N";
		
		if(pollID==0 || questionID==0){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}
		
		String[] exampleDescMatrixX = ServletUtil.getParamStringArray(req,"eExampleDescMatrixX");  //가로 
		String[] exampleDescMatrixY = ServletUtil.getParamStringArray(req,"eExampleDescMatrixY");  //세로	
		
				
		PollQuestion pollQuestion = new PollQuestion();
		pollQuestion.setPollID(pollID);
		pollQuestion.setQuestionID(questionID);
		pollQuestion.setQuestionNo(questionNo);
		pollQuestion.setQuestionType(questionType);
		pollQuestion.setQuestionHead(questionHead);		
		pollQuestion.setQuestionText(questionText);
		pollQuestion.setExampleType(exampleType);
		pollQuestion.setExampleGubun("0");    //매트릭스에서는 불필요함으로 그냥 0으로 세팅 
		pollQuestion.setExampleMultiCount(0);		  //매트릭스에서는 불필요함으로 그냥 0으로 세팅 
		pollQuestion.setRequiredYN(requiredYN);			
		pollQuestion.setExamplePosition(0);   //매트릭스에서는 불필요함으로 그냥 0으로 세팅 
		pollQuestion.setMatrixTextSize(matrixTextSize);		
		PollExample[] pollExamplesMatrixX = null;
		PollExample[] pollExamplesMatrixY = null;
		
		pollExamplesMatrixX = new PollExample[exampleDescMatrixX.length];
		pollExamplesMatrixY = new PollExample[exampleDescMatrixY.length];
		
		
		for(int i=0;i<exampleDescMatrixX.length;i++){
			pollExamplesMatrixX[i] = new PollExample();
			pollExamplesMatrixX[i].setQuestionID(questionID);
			pollExamplesMatrixX[i].setPollID(pollID);
			pollExamplesMatrixX[i].setExampleID((i+1));
			pollExamplesMatrixX[i].setMatrixXY("X");
			pollExamplesMatrixX[i].setExampleDesc(exampleDescMatrixX[i]);				
			pollExamplesMatrixX[i].setExampleExYN("N");  //일단기본은 N					
			pollExamplesMatrixX[i].setGoToQuestionNo(0);
		}// end for (matrix X)
			
		for(int i=0;i<exampleDescMatrixY.length;i++){
			pollExamplesMatrixY[i] = new PollExample();
			pollExamplesMatrixY[i].setQuestionID(questionID);
			pollExamplesMatrixY[i].setPollID(pollID);
			pollExamplesMatrixY[i].setExampleID((i+1));
			pollExamplesMatrixY[i].setMatrixXY("Y");
			pollExamplesMatrixY[i].setExampleDesc(exampleDescMatrixY[i]);				
			pollExamplesMatrixY[i].setExampleExYN("N");  //일단기본은 N					
			pollExamplesMatrixY[i].setGoToQuestionNo(0);								
		}// end for (matrix Y)
					
		
		Map<String,Object> resultMap = pollService.showResultDefaultPollHTML(pollID);
		String resultPollHTML = (String)resultMap.get("resultPollHTML");

			
		//기존 질문문항 html 부분을 찾는다. 
		String startHtml = "<!-- #TM_QUESTION_START_"+pollQuestion.getQuestionID()+" -->";
		String endHtml  = "<!-- #TM_QUESTION_END_"+pollQuestion.getQuestionID()+" -->";
		
		int startIdx = resultPollHTML.indexOf(startHtml);
		int endIdx = resultPollHTML.indexOf(endHtml)+ endHtml.length();
		
		String oldQuestionHTML = resultPollHTML.substring(startIdx, endIdx);
			
		if(oldQuestionHTML==null || oldQuestionHTML.equals("")){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다 \r\n기존에 저장된 질문을 읽어 올수 없습니다.","");
			return null;	
		}
		
		//새로 만듬 
		String newQuestionHTML = makeHTMLByQuestionMatrix(pollQuestion,pollExamplesMatrixX, pollExamplesMatrixY,"update");	
		
		//새것으로 교체 
		resultPollHTML = resultPollHTML.replace(oldQuestionHTML, newQuestionHTML);
				
		int resultVal = pollService.updatePollQuestionExampleMatrix(pollQuestion, pollExamplesMatrixX, pollExamplesMatrixY, resultPollHTML);
		
		if(resultVal>0){	 
			return null;
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
	}
	
	
	
	/**
	 * <p>설문문항 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteQuestion(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		int questionID = ServletUtil.getParamInt(req,"eQuestionID","0");	
		int questionNo = ServletUtil.getParamInt(req,"eQuestionNo","0");
		
		PollQuestion pollQuestion = new PollQuestion();
		pollQuestion.setPollID(pollID);
		pollQuestion.setQuestionID(questionID);
		pollQuestion.setQuestionNo(questionNo);
		
		
		Map<String,Object> resultMap = pollService.showResultDefaultPollHTML(pollID);
		String resultPollHTML = (String)resultMap.get("resultPollHTML");
		
		String oldQuestionHTML = splitQuestionHTML(questionID,resultPollHTML);
		
		
		//해당문항 제거 
		resultPollHTML = resultPollHTML.replace(oldQuestionHTML, "");
		
		int questionCount = pollService.getQuestionCount(pollID);
		
		
		for(int i=questionNo;i<questionCount;i++){
			String findNo = "["+Constant.POLL_QUESTION_DES+(i+1)+"]";
			resultPollHTML = resultPollHTML.replace(findNo, "["+Constant.POLL_QUESTION_DES+(i)+"]");
		}
		
		int resultVal = pollService.deletePollQuestionExample(pollQuestion, resultPollHTML);
		
		if(resultVal>0){	 
			return null;
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}	
	}
	
	
	/**
	 * <p>설문문항에 HTML
	 * @param questionID
	 * @param resultHTML
	 * @return
	 */
	private String splitQuestionHTML(int questionID, String resultHTML){
		
		//기존 질문문항 html 부분을 찾는다. 
		String startHtml = "<!-- #TM_QUESTION_START_"+questionID+" -->";
		String endHtml  = "<!-- #TM_QUESTION_END_"+questionID+" -->";
		
		int startIdx = resultHTML.indexOf(startHtml);
		int endIdx = resultHTML.indexOf(endHtml)+ endHtml.length();
		
		return resultHTML.substring(startIdx, endIdx);
	}

	
	/**
	 * <p>설문기본정보보기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{				
		int pollID = ServletUtil.getParamInt(req,"pollID","0");
		String shareGroupID =  ServletUtil.getParamString(req,"shareGroupID");			
		String codeID =  ServletUtil.getParamString(req,"codeID");				
		String usingYN = "N";
		
		
		//현재 사용중인 설문이 있다면 
		if(pollID>0 && pollService.checkUsingPoll(pollID)>0){
			usingYN = "Y";
		}
			
		return new ModelAndView("/pages/content/poll/poll_proc.jsp?method=edit&shareGroupID="+shareGroupID+"&pollID="+pollID+"&codeID="+codeID+"&usingYN="+usingYN,"pollInfo", pollService.viewPollInfo(pollID));		
	}
	
	
	/**
	 * <p>코드 출력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selectCode(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String id=  ServletUtil.getParamString(req,"id");
		String codeType=  ServletUtil.getParamString(req,"codeType");
		String codeID=  ServletUtil.getParamString(req,"codeID");
		return new ModelAndView("/pages/content/poll/poll_code_type_inc.jsp?id="+id+"&codeType="+codeType+"&codeID="+codeID);	
	}
	
	
	
	/**
	 * <p>설문문항 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editPollQuestion(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		int pollTemplateID = ServletUtil.getParamInt(req,"ePollTemplateID","0");				
		int questionNo = ServletUtil.getParamInt(req,"eQuestionNo","0");
		
		PollTemplate pollTemplate = pollService.viewPollTemplate(pollTemplateID);
		String templateHTML = pollTemplate.getPollTemplateHTML();
		String isExistQuestion = "YES";
	
		
		if(templateHTML.indexOf(Constant.POLL_TEMPLATE_QUESTIONS)==-1){
			isExistQuestion = "NO";  
		}
	
		if(questionNo>0){
			req.setAttribute("selQuestionNos", Integer.toString(questionNo));	
		}
		Map<String,Object> resultMap = pollService.getQuestionIDByNo(pollID, questionNo);
		
		int questionID = 0;
		String questionType = "";
		if(resultMap!=null){
			questionID = Integer.parseInt(resultMap.get("questionID").toString());
			questionType = (String)resultMap.get("questionType");
		}
		List<PollExample> pollExampleList = null; //일반질문용 보기
		List<PollExample> pollExampleListMatrixX = null; //매트릭스 가로보기 
		List<PollExample> pollExampleListMatrixY= null; //매트릭스 세로보기
		
		
		//일반 질문일 경우 
		if(questionType.equals("1")){
			if(db_type.equals(DB_TYPE_ORACLE))
				pollExampleList = pollService.selectPollExample(pollID, questionID, questionType, " ");	
			else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
				pollExampleList = pollService.selectPollExample(pollID, questionID, questionType, "");
			req.setAttribute("pollExampleList", pollExampleList);
		}
		//매트릭스일 경우 
		else if(questionType.equals("2")){
			pollExampleListMatrixX = pollService.selectPollExample(pollID, questionID, questionType, "X");
			pollExampleListMatrixY = pollService.selectPollExample(pollID, questionID, questionType, "Y");
			req.setAttribute("pollExampleListMatrixX", pollExampleListMatrixX);
			req.setAttribute("pollExampleListMatrixY", pollExampleListMatrixY);
		}
		
	
		return new ModelAndView("/pages/content/poll/poll_question_make.jsp?pollID="+pollID+"&pollTemplateID="+pollTemplateID+"&isExistQuestion="+isExistQuestion,"pollQuestion", pollService.viewPollQuestion(pollID, questionID));	
	}
	
	
	/**
	 * <p>설문템플릿
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView listPollTemplate(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		
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
		String sUseYN = ServletUtil.getParamString(req,"sUseYN");
		String sSelectedGroupID = ServletUtil.getParamString(req,"sSelectedGroupID");

		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("useYN", sUseYN);	
		searchMap.put("selectedGroupID", sSelectedGroupID);	
		String userAuth = LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		
		String userID = LoginInfo.getUserID(req);
		String groupID =  LoginInfo.getGroupID(req);
		searchMap.put("sUseYN", sUseYN);	
		
		//userID, groupID, userAuth, 
		searchMap.put("userID", userID);
		searchMap.put("groupID", groupID);
		searchMap.put("userAuth", userAuth);
		
		//총카운트 		
		int totalCount =pollService.selectPollTemplateCount(searchMap);	
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		
		return new ModelAndView("/pages/content/poll/poll_template_proc.jsp?method=listPollTemplate","pollTemplateList", pollService.selectPollTemplate(curPage, iLineCnt, searchMap));
	}
	
	/**
	 * <p>설문템플릿 수정 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editPollTemplate(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		
		int pollTemplateID = ServletUtil.getParamInt(req,"ePollTemplateID","0");		
		String shareGroupID =  ServletUtil.getParamString(req,"shareGroupID");								
		return new ModelAndView("/pages/content/poll/poll_template_proc.jsp?method=editPollTemplate&shareGroupID="+shareGroupID+"&pollTemplateID="+pollTemplateID,"pollTemplate", pollService.viewPollTemplate(pollTemplateID)); 		

	}
	
	
	/**
	 * <p>설문템플릿 등록 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertPollTemplate(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		int pollTemplateID = ServletUtil.getParamInt(req,"ePollTemplateID","0");		
		String pollTemplateTitle = ServletUtil.getParamString(req,"ePollTemplateTitle");
		String pollTemplateHTML = ServletUtil.getParamString(req,"ePollTemplateHTML");
		String userID = LoginInfo.getUserID(req);
		String shareGroupID =  ServletUtil.getParamString(req,"selectedGroupID");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		
		PollTemplate pollTemplate = new PollTemplate();
		pollTemplate.setPollTemplateID(pollTemplateID);
		pollTemplate.setPollTemplateTitle(pollTemplateTitle);
		pollTemplate.setPollTemplateHTML(pollTemplateHTML);
		pollTemplate.setUserID(userID);
		pollTemplate.setShareGroupID(shareGroupID);
		pollTemplate.setUseYN(useYN);
		
		int resultVal = pollService.insertPollTemplate(pollTemplate);
		if(resultVal>0){
			this.sCurPage = "1"; 
			return listPollTemplate(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
		
	}
	
	
	/**
	 * <p>설문템플릿 수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updatePollTemplate(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		int pollTemplateID = ServletUtil.getParamInt(req,"ePollTemplateID","0");		
		String pollTemplateTitle = ServletUtil.getParamString(req,"ePollTemplateTitle");
		String pollTemplateHTML = ServletUtil.getParamString(req,"ePollTemplateHTML");
		String userID = LoginInfo.getUserID(req);
		String shareGroupID =  ServletUtil.getParamString(req,"selectedGroupID");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		
		PollTemplate pollTemplate = new PollTemplate();
		pollTemplate.setPollTemplateID(pollTemplateID);
		pollTemplate.setPollTemplateTitle(pollTemplateTitle);
		pollTemplate.setPollTemplateHTML(pollTemplateHTML);
		pollTemplate.setUserID(userID);
		pollTemplate.setShareGroupID(shareGroupID);
		pollTemplate.setUseYN(useYN);
		
		int resultVal = pollService.updatePollTemplate(pollTemplate);
		if(resultVal>0){
			this.sCurPage = "1"; 
			return listPollTemplate(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
	}
	
	
	/**
	 * <p>설문템플릿 수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deletePollTemplate(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] pollTemplateIDs =  req.getParameterValues("ePollTemplateID");	
		int resultVal = pollService.deletePollTemplate(pollTemplateIDs);
		if(resultVal>0){
			this.sCurPage = "1"; 
			return listPollTemplate(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}		
	}
	
	
	
	/**
	 * <p>설문  HTML 수정 (에디터에서 수정했을 경우)
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateResultPollHTML(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		String resultPollHTML =  ServletUtil.getParamString(req,"eResultPollHTML");
		String resultFinishYN =  ServletUtil.getParamString(req,"resultFinishYN");
		
		int resultVal = 0;
		//초기화로 한다. 
		if(resultFinishYN.equals("N")){
			resultVal = pollService.updateResultPollHTMLToDefault(pollID);
		}else if(resultFinishYN.equals("Y")){			
			resultVal = pollService.updateResultPollHTMLToFinish(pollID, resultPollHTML);
		}
		
		if(resultVal>0){			 
			return  null; 
		}else{
			ServletUtil.messageGoURL(res,"수정에 실패했습니다","");
			return null;	
		}	
	}
	
	
	/**
	 * <p>설문 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deletePoll(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		
		String[] pollIDs =  req.getParameterValues("ePollID");	
		if(pollIDs==null || pollIDs.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		for(int i=0;i<pollIDs.length;i++){
			if(pollService.checkUsingPoll(Integer.parseInt(pollIDs[i]))>0){
				ServletUtil.messageGoURL(res,"설문아이디 [ "+pollIDs[i]+" ]는 현재 대량메일에서 사용중입니다.","");
				return null;
			}
		}
		
		int resultVal = pollService.deletePoll(pollIDs);
		
		if(resultVal<0){
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");
			return null;
		}else{
			return list(req,res);
		}
		
	}
	
	
	/**
	 * <p>설문 번호 변경 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateQuestionNoHTML(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		int oldQuestionNo = ServletUtil.getParamInt(req,"eOldQuestionNo","0");	
		int newQuestionNo = ServletUtil.getParamInt(req,"eNewQuestionNo","0");
		int oldQuestionID =  ServletUtil.getParamInt(req,"eOldQuestionID","0");
			
		int newQuestionID = 0;
		Map<String,Object> resultMap = pollService.getQuestionIDByNo(pollID, newQuestionNo);
		
		if(resultMap==null || resultMap.equals("")){
			newQuestionID = 0;
		}else{
			newQuestionID = Integer.parseInt(resultMap.get("questionID").toString());
		}
		
		int resultVal = 0;
			
		//변경 방식 
		String chgNumType = ServletUtil.getParamString(req,"eChgNumType");

		
		//번호 교체라면 
		if(chgNumType.equals("replace")){			
			resultVal = updateChangeQuestionNo(pollID, oldQuestionID, oldQuestionNo, newQuestionID, newQuestionNo);
		}

		//뒤로  이동 
		else if(chgNumType.equals("movenext")){
			resultVal = updateMoveNextQuestionNo(pollID, oldQuestionID, oldQuestionNo, newQuestionID, newQuestionNo);
		}
	
		if(resultVal<0){
			ServletUtil.messageGoURL(res,"수정에 실패 하였습니다.","");
			return null;
		}else{
			return null;
		}
		
	}
	
	
	
	/**
	 * <p>설문번호 맞교환 
	 * @param pollID
	 * @param oldQuestionID
	 * @param oldQuestionNo
	 * @param newQuestionID
	 * @param newQuestionNo
	 * @return
	 */
	public int updateChangeQuestionNo(int pollID, int oldQuestionID, int oldQuestionNo, int newQuestionID, int newQuestionNo){		
		
		Map<String,Object> resultMap = pollService.showResultDefaultPollHTML(pollID);
		String resultPollHTML = (String)resultMap.get("resultPollHTML");
		
		//1. 변경전의 기존의  문항html을 가져온다.
		String oldQuestionHTML = splitQuestionHTML(oldQuestionID,resultPollHTML);
		String newQuestionHTML = splitQuestionHTML(newQuestionID,resultPollHTML);		
		
		//2.  문항 번호를 바꾸어준다. 
		String oldfindNo="";
		String oldQuestionHTML2 = "";
		String newfindNo="";
		String newQuestionHTML2="";
		
		oldfindNo = "<a name=\""+oldQuestionNo+"\">";
		oldQuestionHTML2 = oldQuestionHTML.replace(oldfindNo, "<a name=\""+newQuestionNo+"\">");
		
		newfindNo = "<a name=\""+newQuestionNo+"\">";
		newQuestionHTML2 = newQuestionHTML.replace(newfindNo, "<a name=\""+oldQuestionNo+"\">");
		
		
		oldfindNo = "["+Constant.POLL_QUESTION_DES+oldQuestionNo+"]";
		oldQuestionHTML2 = oldQuestionHTML2.replace(oldfindNo, "["+Constant.POLL_QUESTION_DES+newQuestionNo+"]");
		
		newfindNo = "["+Constant.POLL_QUESTION_DES+newQuestionNo+"]";
		newQuestionHTML2 = newQuestionHTML2.replace(newfindNo, "["+Constant.POLL_QUESTION_DES+oldQuestionNo+"]");
		
		
		//3. html을 변경한다.
		resultPollHTML = resultPollHTML.replace(oldQuestionHTML, newQuestionHTML2);
		resultPollHTML = resultPollHTML.replace(newQuestionHTML, oldQuestionHTML2);		
		
		return pollService.updateChangeQuestionNoHTML(pollID, oldQuestionID, oldQuestionNo, newQuestionID, newQuestionNo, resultPollHTML);		
		
	}
	
	

	
	
	/**
	 * 번호 뒤로 이동 
	 * @param pollID
	 * @param oldQuestionID
	 * @param oldQuestionNo
	 * @param newQuestionID
	 * @param newQuestionNo
	 * @return
	 */
	public int updateMoveNextQuestionNo(int pollID, int oldQuestionID, int oldQuestionNo, int newQuestionID, int newQuestionNo){
		
		int addminusNum = 0;			//번호를 가감할 변수
		int allowQuestionNo = 0;
		int questionNoFrom = 0;
		int questionNoTo = 0;
		
		if(oldQuestionNo>newQuestionNo){
			allowQuestionNo = newQuestionNo+1;
			questionNoFrom = newQuestionNo;
			questionNoTo = oldQuestionNo;
			addminusNum = 1;
		}else{
			allowQuestionNo = newQuestionNo;
			questionNoFrom = oldQuestionNo;
			questionNoTo = newQuestionNo+1;
			addminusNum = -1;
		}
		
		Map<String,Object> resultMap = pollService.showResultDefaultPollHTML(pollID);
		String resultPollHTML = (String)resultMap.get("resultPollHTML");
		String targetHTML = "";
		
		
		if(newQuestionNo!=0){
			targetHTML = "<!-- #TM_QUESTION_END_"+pollService.getQuestionIDByNo(pollID, newQuestionNo).get("questionID")+" -->"; //주어진 번호 앞에 넣어줄 타켓 html
		}else{
			targetHTML = "<!-- #TM_QUESTION_START -->"; //0번 뒤는 맨앞을 의미한다.
		}
		
		
		//옮긴 현재  문항html을 가져온다.
		String oldQuestionHTML = splitQuestionHTML(oldQuestionID,resultPollHTML);		
		
	
		//이전 문항을 제거 
		resultPollHTML = resultPollHTML.replace(oldQuestionHTML, "");
		
		
		//문항을 번호로 수정한다.	
		String oldfindNo="";
		String oldQuestionHTML2="";
		
		oldfindNo = "<a name=\""+oldQuestionNo+"\">";
		oldQuestionHTML2 = oldQuestionHTML.replace(oldfindNo, "<a name=\""+allowQuestionNo+"\">");		
		
		oldfindNo = "["+Constant.POLL_QUESTION_DES+oldQuestionNo+"]";
		oldQuestionHTML2 = oldQuestionHTML2.replace(oldfindNo, "["+Constant.POLL_QUESTION_DES+allowQuestionNo+"]");
		
		oldQuestionHTML2=targetHTML+"\r\n\r\n"+oldQuestionHTML2;
		
		
		//수정된 문항의 위치를 교체한다.
		resultPollHTML = resultPollHTML.replace(targetHTML, oldQuestionHTML2);
		
		
		
		//옮긴 설문 문항이고 그 앞문항에 사이에 설문문항 리스트 
		List<Map<String,Integer>> questionIDList = pollService.selectQuestionNo(pollID, questionNoFrom,  questionNoTo);
		String questionIDHtml1="";
		String questionIDHtml2="";
		String findNo="";
		for(int i=0;i<questionIDList.size();i++){
			questionIDHtml1 = splitQuestionHTML(questionIDList.get(i).get("questionID"),resultPollHTML);			
			
			findNo = "<a name=\""+questionIDList.get(i).get("questionNo")+"\">";
			questionIDHtml2 = questionIDHtml1.replace(findNo, "<a name=\""+(questionIDList.get(i).get("questionNo")+addminusNum)+"\">");	
			
			findNo = "["+Constant.POLL_QUESTION_DES+questionIDList.get(i).get("questionNo")+"]";
			questionIDHtml2 = questionIDHtml2.replace(findNo, "["+Constant.POLL_QUESTION_DES+(questionIDList.get(i).get("questionNo")+addminusNum)+"]");			
			
			
			resultPollHTML = resultPollHTML.replace(questionIDHtml1, questionIDHtml2);	
		}
		return pollService.updateMoveNextQuestionNoHTML(addminusNum, allowQuestionNo, questionIDList, pollID, oldQuestionID, oldQuestionNo, newQuestionID, newQuestionNo, resultPollHTML);
		

	}
	
	
	
	
	/**
	 * <p>대량메일 작서에서 설문리스트를 불러올때 사용 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listPoll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화		
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		String userAuth = LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		
		String userID = LoginInfo.getUserID(req);
		String groupID =  LoginInfo.getGroupID(req);			
		
		//userID, groupID, userAuth, 
		searchMap.put("userID", userID);
		searchMap.put("groupID", groupID);
		searchMap.put("userAuth", userAuth);
		searchMap.put("nowDate", DateUtils.getDateTimeString());
		
		return new ModelAndView("/pages/massmail/write/massmail_poll.jsp?method=listPoll","pollInfoList", pollService.selectPollList(searchMap));
	}
		
	
	
	/**
	 * <p>설문을 복사한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView copyPoll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		
		String type = LoginInfo.getUserID(req); // 설문 초기 생성의 경우 insert , 복사의 경우  작성자 변경을 위한 userID 값
		
		PollInfo pollInfo = pollService.viewPollInfo(pollID);
		
		int resultVal = 0;
		
		synchronized(this) {
			//기본정보 입력 
			resultVal = pollService.insertPollInfo(pollInfo , type);
		
			int newPollID = pollService.getMaxPollID();		
			
			if(resultVal>0){
				resultVal = pollService.insertCopyPoll(pollID, newPollID, type);
			}else{				
				resultVal = -1;
			}			
		}	

		if(resultVal>0){			
			this.sCurPage = "1"; 
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
		
	}

	
	
	/**
	 * <p>설문코드리스트  
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listPollCode(HttpServletRequest req, HttpServletResponse res) throws Exception{
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화		
		
		String codeID = ServletUtil.getParamString(req,"sCodeID");
		String codeType = ServletUtil.getParamString(req,"sCodeType");
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
				
		searchMap.put("groupID", codeID);
		searchMap.put("codeType", codeType);
		
		return new ModelAndView("/pages/content/poll/poll_code_proc.jsp?method=listPollCode","pollCodeList", pollService.selectPollCodeList(searchMap)); 
	}
	
	
	/**
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editPollCode(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String codeID = ServletUtil.getParamString(req,"codeID");
		String codeType = ServletUtil.getParamString(req,"codeType");	
		String useYN = ServletUtil.getParamString(req,"useYN");
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("codeID", codeID);
		
		req.setCharacterEncoding("UTF-8");
		req.setAttribute("codeID",codeID);
		req.setAttribute("codeType",codeType);
	
		req.setAttribute("useYN",useYN);
		
		List<PollCode> pollCodeList = pollService.selectPollCodeList(searchMap);
		
		if(pollCodeList.size()>0){
			req.setAttribute("codeName",pollCodeList.get(0).getCodeName());
		}
		
		return new ModelAndView("/pages/content/poll/poll_code_proc.jsp?method=editPollCode","pollCodeList", pollCodeList); 		

	}
	
	/**
	 * <p>설문코드 삽입 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertPollCode(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String codeType = ServletUtil.getParamString(req,"eCodeType");
		String codeName = ServletUtil.getParamString(req,"eCodeName");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		String[] codeDescs = ServletUtil.getParamStringArray(req, "eCodeDesc");
		int maxCodeID = pollService.getMaxPollCodeID();
		String codeID = "";
		if(maxCodeID>0){
			codeID = Integer.toString(maxCodeID);
		}
		List<PollCode> pollCodes = new ArrayList<PollCode>();
		for(int i=0;i<codeDescs.length;i++){
			PollCode pollCode = new PollCode();
			pollCode.setCodeID(codeID);
			pollCode.setCodeName(codeName);
			pollCode.setCodeNo(i+1);
			pollCode.setCodeDesc(codeDescs[i]);
			pollCode.setCodeType(codeType);
			pollCode.setUseYN(useYN);
			pollCodes.add(pollCode);
		}
		
		int[] resultVal = pollService.insertPollCode(pollCodes);
		if(resultVal!=null){
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
	}
	
	
	/**
	 * <p>설문코드수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updatePollCode(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String codeID = ServletUtil.getParamString(req,"eCodeID");
		String codeType = ServletUtil.getParamString(req,"eCodeType");
		String codeName = ServletUtil.getParamString(req,"eCodeName");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		String[] codeDescs = ServletUtil.getParamStringArray(req, "eCodeDesc");
		
		List<PollCode> pollCodes = new ArrayList<PollCode>();
		for(int i=0;i<codeDescs.length;i++){
			PollCode pollCode = new PollCode();
			pollCode.setCodeID(codeID);
			pollCode.setCodeName(codeName);
			pollCode.setCodeNo(i+1);
			pollCode.setCodeDesc(codeDescs[i]);
			pollCode.setCodeType(codeType);
			pollCode.setUseYN(useYN);
			pollCodes.add(pollCode);
		}
		
		int[] resultVal = pollService.updatePollCode(codeID, pollCodes);
		if(resultVal!=null){
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
	}
	
	
	
	/**
	 * <p>삭제처리 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deletePollCode(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String codeID = ServletUtil.getParamString(req,"codeID");
		int resultVal = pollService.deletePollCode(codeID);
		if(resultVal>0){
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}	
	}
	
	
	/**
	 * <p> 사용자들이 설문응답 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView responseAnswer(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int pollID = ServletUtil.getParamInt(req,"pollID","0");
		int massmailID = ServletUtil.getParamInt(req,"massmailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		int sendID = ServletUtil.getParamInt(req,"sendID","0");		
		String email =  ServletUtil.getParamString(req,"email");
		
		
		//이미 설문에 응한 사용자인지 체크한다.
		if(pollService.checkPollAnswer(pollID, massmailID, scheduleID, sendID, email)>0){
			ServletUtil.messageClose(res, "이미 설문에 참여하셨습니다.");	
			return null;
		}
		
		if(pollService.checkExistMember(massmailID, scheduleID, sendID, email)==0){
			ServletUtil.messageClose(res, "설문대상자가 아닙니다.");	
			return null;			
		}		
	
		if(pollService.checkPollEndDate(pollID, DateUtils.getDateTimeString())>0){
			ServletUtil.messageClose(res, "해당 설문은 이미 종료가 된 설문입니다.");	
			return null;		
		}	
					
		List<Map<String,Integer>> questionList = pollService.selQuestion(pollID);
		
		//일반설문응답
		List<PollAnswer> pollAnswerList = new ArrayList<PollAnswer>();

		for(int i=0;i<questionList.size();i++){
			
		
			int questionID = questionList.get(i).get("questionID");
			int questionNo = questionList.get(i).get("questionNo");
			//System.out.println("############################### questionNo : "+questionNo);
			
			//1.단일질문 2.매트릭스 
			String questionType =  ServletUtil.getParamString(req, "questionType_"+questionID);  
			
			//응답타입 : 1:객관식, 2:주관식
			String exampleType =  ServletUtil.getParamString(req, "exampleType_"+questionID);
			
			//1:단일응답, 2:복수응답
			String exampleGubun =  ServletUtil.getParamString(req, "exampleGubun_"+questionID);
			
			
			//필수응답여부 
			String requiredYN = ServletUtil.getParamString(req, "requiredYN_"+questionID);
			//System.out.println("exampleType : "+exampleType);
		
	
			
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("exampleType", exampleType);
			paramMap.put("exampleGubun", exampleGubun);
			paramMap.put("requiredYN", requiredYN);
			paramMap.put("email", email);			
			paramMap.put("questionID", new Integer(questionID));
			paramMap.put("questionNo", new Integer(questionNo));
			paramMap.put("massmailID", new Integer(massmailID));
			paramMap.put("scheduleID", new Integer(scheduleID));
			paramMap.put("pollID", new Integer(pollID));
			paramMap.put("sendID", new Integer(sendID));
			

			
			//--------------------------------------------------------------------------------------------------------------------------------------------
			//일반설문타입이라면
			//--------------------------------------------------------------------------------------------------------------------------------------------
			if(questionType.equals("1")){				
				
				PollAnswer pollAnswer = new PollAnswer();			
				pollAnswer.setMassmailID(massmailID);
				pollAnswer.setPollID(pollID);
				pollAnswer.setScheduleID(scheduleID);
				pollAnswer.setSendID(sendID);
				pollAnswer.setEmail(email);	
				pollAnswer.setQuestionID(questionID);
				pollAnswer.setMatrixX(0);
				pollAnswer.setMatrixY(0);
				
				//객관식이라면 
				if(exampleType.equals("1")){
					
					//단일응답이라면
					if(exampleGubun.equals("1")){
					
						String exampleGubun_single=  ServletUtil.getParamString(req, "exampleGubun_single_"+questionID);
						
						//필수응답문항인데 없으면 팅기자.
						if(requiredYN.equals("Y") && exampleGubun_single.equals("")){
							ServletUtil.messageGoBack(res,questionNo+"번 은 필수 응답입니다.");
							return null;
						}
					
						if(!exampleGubun_single.equals("")){
							String responseText =  req.getParameter("exampleExYN_"+questionID+"_"+exampleGubun_single);
							if(responseText!=null && responseText.equals("")){
								ServletUtil.messageGoBack(res,questionNo+"번의 선택하신 보기의 주관식 답변을 입력해주세요.");
								return null;
							}else if(responseText!=null && !responseText.equals("")){						
								pollAnswer.setResponseText(responseText);
							}
							pollAnswer.setExampleID(Integer.parseInt(exampleGubun_single));
							pollAnswerList.add(pollAnswer);
						}
					}
					//복수응답이라면 
					else{
						
						List<Map<String,Object>> exampleList =null;
						if(db_type.equals(DB_TYPE_ORACLE))
							exampleList = pollService.selExample(pollID, questionID, " ");
						else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
							exampleList = pollService.selExample(pollID, questionID, "");
						int exampleMultiCount = ServletUtil.getParamInt(req,"exampleMultiCount_"+questionID,"0");
						int exampleMultiMinimumCount = ServletUtil.getParamInt(req,"exampleMultiMinimumCount_"+questionID,"0");
						int exampleCount = 0;
						int checkRequired = 0;
						
						for(int j=0;j<exampleList.size();j++){
							pollAnswer = new PollAnswer();
							
							String  exampleGubun_multi =  ServletUtil.getParamString(req, "exampleGubun_multi_"+questionID+"_"+exampleList.get(j).get("exampleID"));
							

							if(!exampleGubun_multi.equals("")){
								exampleCount++;
								if(exampleCount>exampleMultiCount){
									ServletUtil.messageGoBack(res,questionNo+"번의 복수응답은 "+exampleMultiCount+" 개 이하로 선택하세요.");
									return null;
								}
								
								//필수응답문항인데 없다면 
								if(checkRequired==0 && requiredYN.equals("Y") && exampleGubun_multi.equals("")){
									checkRequired = 0;							
								}else{
									checkRequired = 1;
								}
								
								String responseText =  req.getParameter("exampleExYN_"+questionID+"_"+exampleGubun_multi);
								if(responseText!=null && responseText.equals("")){
									ServletUtil.messageGoBack(res,questionNo+"번의 선택하신 보기의 주관식 답변을 입력해주세요.");
									return null;
								}else if(responseText!=null && !responseText.equals("")){
							
									pollAnswer.setResponseText(responseText);
								}												
								
								pollAnswer.setMassmailID(massmailID);
								pollAnswer.setPollID(pollID);
								pollAnswer.setScheduleID(scheduleID);
								pollAnswer.setSendID(sendID);
								pollAnswer.setEmail(email);		
								pollAnswer.setQuestionID(questionID);
								pollAnswer.setExampleID(Integer.parseInt(exampleGubun_multi));
								pollAnswerList.add(pollAnswer);
							}
						}
						
						if(requiredYN.equals("Y") && exampleMultiMinimumCount > 0 && exampleMultiMinimumCount > exampleCount){
							ServletUtil.messageGoBack(res,questionNo+"번의 복수응답은 "+exampleMultiMinimumCount+" 개 이상 선택하세요.");
							return null;
						}
						
						if(requiredYN.equals("N") && checkRequired > 0 &&exampleMultiMinimumCount > 0 && exampleMultiMinimumCount > exampleCount){
							ServletUtil.messageGoBack(res,questionNo+"번의 복수응답은 "+exampleMultiMinimumCount+" 개 이상 선택하세요.");
							return null;
						}
						//선택되어진 것이 하나도 없다면 
						if(checkRequired==0 && requiredYN.equals("Y")){
							ServletUtil.messageGoBack(res,questionNo+"번 은 필수 응답입니다.");
							return null;
						}					
					}
				}
				//주관식이라면 
				else if(exampleType.equals("2")){
					String exampleType_2 = ServletUtil.getParamString(req, "exampleType_text_"+questionID);
					
					//필수응답문항인데 없으면 팅기자.
					if(requiredYN.equals("Y") && exampleType_2.equals("")){
						ServletUtil.messageGoBack(res,questionNo+"번 은 필수 응답입니다.");
						return null;
					}
					
					if(!exampleType_2.equals("")){
						pollAnswer.setExampleID(1);
						pollAnswer.setResponseText(exampleType_2);
						pollAnswerList.add(pollAnswer);
					}
					
				}
				//순위선택
				else if(exampleType.equals("3")){
					List<Map<String,Object>> exampleList =null;
					if(db_type.equals(DB_TYPE_ORACLE))
						exampleList = pollService.selExample(pollID, questionID, " ");
					else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
						exampleList = pollService.selExample(pollID, questionID, "");
					int exampleMultiCount = ServletUtil.getParamInt(req,"exampleMultiCount_"+questionID,"0");
					int exampleCount = 0;
					int checkRequired = 0;
					for(int j=0;j<exampleList.size();j++){
						pollAnswer = new PollAnswer();
						
						String  exampleGubun_select =  ServletUtil.getParamString(req, "exampleGubun_select_"+questionID+"_"+exampleList.get(j).get("exampleID"));
						
						if(!exampleGubun_select.equals("")){
							checkRequired++;
							pollAnswer.setMassmailID(massmailID);
							pollAnswer.setPollID(pollID);
							pollAnswer.setScheduleID(scheduleID);
							pollAnswer.setSendID(sendID);
							pollAnswer.setEmail(email);		
							pollAnswer.setQuestionID(questionID);
							pollAnswer.setExampleID(Integer.parseInt(exampleList.get(j).get("exampleID").toString()));
							pollAnswer.setResponseText(exampleGubun_select);
							pollAnswer.setRanking(Integer.parseInt(exampleGubun_select));
							pollAnswerList.add(pollAnswer);
							
						}else if(requiredYN.equals("Y")){
							ServletUtil.messageGoBack(res,questionNo+"번은 필수 응답입니다.");
							return null;
						}
						
					}
					if(checkRequired > 0 && checkRequired != exampleList.size()){
						ServletUtil.messageGoBack(res,questionNo+"번 문항 중 순위 선택을 하지 않은 문항이 있습니다.");
						return null;
					}
				}
				//숫자입력
				else if(exampleType.equals("4")){
					String exampleType_4 = ServletUtil.getParamString(req, "exampleType_text_"+questionID);
					System.out.println("answer : "+exampleType_4);
					//필수응답문항인데 없으면 팅기자.
					if(requiredYN.equals("Y") && exampleType_4.equals("")){
						ServletUtil.messageGoBack(res,questionNo+"번 은 필수 응답입니다.");
						return null;
					}
					
					pollAnswer.setExampleID(1);
					pollAnswer.setResponseText(exampleType_4);
					pollAnswerList.add(pollAnswer);
				}
				//척도형
				else if(exampleType.equals("5")){
					String exampleGubun_single=  ServletUtil.getParamString(req, "exampleGubun_single_"+questionID);
					System.out.println("answer : "+exampleGubun_single);
					//필수응답문항인데 없으면 팅기자.
					if(requiredYN.equals("Y") && exampleGubun_single.equals("")){
						ServletUtil.messageGoBack(res,questionNo+"번 은 필수 응답입니다!!");
						return null;
					}
				
					if(!exampleGubun_single.equals("")){
						String responseText =  req.getParameter("exampleExYN_"+questionID+"_"+exampleGubun_single);
						if(responseText!=null && responseText.equals("")){
							ServletUtil.messageGoBack(res,questionNo+"번의 선택하신 보기의 주관식 답변을 입력해주세요.");
							return null;
						}else if(responseText!=null && !responseText.equals("")){						
							pollAnswer.setResponseText(responseText);
						}
						pollAnswer.setExampleID(Integer.parseInt(exampleGubun_single));
						pollAnswerList.add(pollAnswer);
					}
				}
					
			}
			
			//--------------------------------------------------------------------------------------------------------------------------------------------
			//매트릭스 설문이라면 
			//--------------------------------------------------------------------------------------------------------------------------------------------
			else if(questionType.equals("2")){							

				
				List<Map<String,Object>> exampleListX= pollService.selExample(pollID, questionID, "X");	//X의 갯수만큼 돌면 된다. 
				
				//객관식이라면 
				if(exampleType.equals("1")){			
					
					for(int x=0;x<exampleListX.size();x++){
						String  matrixValue =  ServletUtil.getParamString(req, "exampleGubun_matrixRadio_"+questionID+"_"+exampleListX.get(x).get("exampleID"));
						
						//필수응답문항인데 없으면 팅기자.
						if(requiredYN.equals("Y") && (matrixValue==null || matrixValue.equals(""))){					
							ServletUtil.messageGoBack(res,questionNo+"번 은 필수 응답입니다.");
							return null;
						}				
						
						if(matrixValue!=null && !matrixValue.equals("")){
							String[] matriSplit = matrixValue.split(":");							
							PollAnswer pollAnswer = new PollAnswer();
							pollAnswer.setMassmailID(massmailID);
							pollAnswer.setPollID(pollID);
							pollAnswer.setScheduleID(scheduleID);
							pollAnswer.setSendID(sendID);
							pollAnswer.setEmail(email);	
							pollAnswer.setQuestionID(questionID);							
							pollAnswer.setExampleID((Integer)exampleListX.get(x).get("exampleID"));
							pollAnswer.setMatrixX(Integer.parseInt(matriSplit[0]));   //가로 값
							pollAnswer.setMatrixY(Integer.parseInt(matriSplit[1]));   //세로 값
							
							pollAnswerList.add(pollAnswer);
							
						}				
					}// end for 			
				}
				
				//주관식이라면 
				else if(exampleType.equals("2")){
					
					List<Map<String,Object>> exampleListY = pollService.selExample(pollID, questionID, "Y");	//X의 갯수만큼 돌면 된다. 
					
					for(int x=0;x<exampleListX.size();x++){  //X만큼 돌고 				
						for(int y=0;y<exampleListY.size();y++){ //그리고 Y만큼 돌자
							String  matrixValue =  ServletUtil.getParamString(req, "exampleGubun_matrixText_"+questionID+"_"+exampleListX.get(x).get("exampleID")+"_"+exampleListY.get(y).get("exampleID"));
							
							//필수응답문항인데 없으면 팅기자.
							if(requiredYN.equals("Y") && (matrixValue==null || matrixValue.equals(""))){					
								ServletUtil.messageGoBack(res,questionNo+"번 은 필수 응답이므로 모든 항목에 작성해주세요");
								return null;
							}		
							
							if(matrixValue!=null && !matrixValue.equals("")){
								
								PollAnswer pollAnswer = new PollAnswer();
								pollAnswer.setMassmailID(massmailID);
								pollAnswer.setPollID(pollID);
								pollAnswer.setScheduleID(scheduleID);
								pollAnswer.setSendID(sendID);
								pollAnswer.setEmail(email);	
								pollAnswer.setQuestionID(questionID);								
								pollAnswer.setExampleID((Integer)exampleListX.get(x).get("exampleID"));
								pollAnswer.setMatrixX((Integer)(exampleListX.get(x).get("exampleID")));   //가로 값(x)
								pollAnswer.setMatrixY((Integer)(exampleListX.get(y).get("exampleID")));   //세로 값(y)
								pollAnswer.setResponseText(matrixValue);

								pollAnswerList.add(pollAnswer);
							}
						}// end for y 				
					}//end for x 			
				}
						
			}
			//--------------------------------------------------------------------------------------------------------------------------------------------

			
		}//end for
		
		
		if(pollService.insertPollAnswer(pollAnswerList)!=null){
			ServletUtil.messageClose(res, "설문에 응해주셔서 감사합니다.");	
		}else{
			ServletUtil.messageClose(res, "설문등록에 실패했습니다.");
		}	
			
		return null;		
				
	}	
	
	/**
	 * <p> 문항 불러오기 - 문항 정보를 받아온다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView copyQuestionList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String questionText =  ServletUtil.getParamString(req,"sQuestionText");
		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("questionText", questionText);
		
		List<PollQuestion> pollQuestionList = pollService.copyQuestionList(searchMap);
		String questionHTML ="";
		
		for(int i =0;i < pollQuestionList.size();i++){
			if(pollQuestionList.get(i).getQuestionType().equals("1")){
				List<PollExample> pollExampleList = pollService.selectPollExample(pollQuestionList.get(i).getPollID(), pollQuestionList.get(i).getQuestionID(), pollQuestionList.get(i).getQuestionType()," ");
				questionHTML = makeHTMLByQuestionNormal(pollQuestionList.get(i), pollExampleList);
			}else{
				 List<PollExample> pollExamplesMatrixX = pollService.selectPollExample(pollQuestionList.get(i).getPollID(), pollQuestionList.get(i).getQuestionID(), pollQuestionList.get(i).getQuestionType(),"X");
				 List<PollExample> pollExamplesMatrixY = pollService.selectPollExample(pollQuestionList.get(i).getPollID(), pollQuestionList.get(i).getQuestionID(), pollQuestionList.get(i).getQuestionType(),"Y");
				 questionHTML=makeHTMLByQuestionMatrix(pollQuestionList.get(i), pollExamplesMatrixX, pollExamplesMatrixY);
			}
			pollQuestionList.get(i).setQuestionHTML(questionHTML);
		}
	
		return new ModelAndView("/pages/content/poll/poll_question_copy_proc.jsp?method=list","pollQuestionList",pollQuestionList);
	}
	
	/**
	 * <p>일반질문에 따른 HTML 만들기 
	 * @param pollQuestion
	 * @param exampleDesc
	 * @return
	 */
	private String makeHTMLByQuestionNormal(PollQuestion pollQuestion , List<PollExample> pollExamples){	
		
		String optionHTML = "";		
		//삽입이라면 
		
		//복수응답이 가능할 경우 
		if(pollQuestion.getExampleGubun().equals("2")){
			if(pollQuestion.getExampleMultiMinimumCount()>0){
				optionHTML = " (복수응답 : "+pollQuestion.getExampleMultiMinimumCount()+"이상, "+pollQuestion.getExampleMultiCount()+" 개 이하)";
			}else if(pollQuestion.getExampleGubun().equals("2")){
				optionHTML = " (복수응답 : "+pollQuestion.getExampleMultiCount()+" 개 이하)";
			}
		}
		
		String questionHTML = "\r\n<table>" ;
		if(pollQuestion.getQuestionHead() == null){
			questionHTML += "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\">&nbsp;</td></tr>";
		}else{
			questionHTML += "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\">"+pollQuestion.getQuestionHead().replace("\n", "<br>")+"</td></tr>";
		}
		if(pollQuestion.getFileURL() != null && !pollQuestion.getFileURL().equals("") && pollQuestion.getLayoutType().equals("2")){
			String tagName="img ";
			if(pollQuestion.getFileURL() .indexOf(".swf") > -1 || pollQuestion.getFileURL() .indexOf(".SWF") > -1){
				tagName = "embed pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' ";
			}
			questionHTML += "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\"><"+tagName+" src='"+pollQuestion.getFileURL()+"'></td></tr>" ;
		}
		questionHTML +=			"\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\">"
								+"<table><tr><td valign=\"top\"><b>["+Constant.POLL_QUESTION_DES+pollQuestion.getQuestionNo()+"]</b></td><td align=\"left\">";
		//필수 항목일 경우
		if(pollQuestion.getRequiredYN().equals("Y")){
			questionHTML += "<img src=\"/images/quest_notnull.gif\" align=\"bottom\">"; 	
		}
										
		questionHTML +=	pollQuestion.getQuestionText().replace("\n", "<br>")+optionHTML+"</td></tr></table></td></tr>";
		
		if(pollQuestion.getFileURL() != null && !pollQuestion.getFileURL().equals("") && pollQuestion.getLayoutType().equals("1")){
			String tagName="img ";
			if(pollQuestion.getFileURL() .indexOf(".swf") > -1 || pollQuestion.getFileURL() .indexOf(".SWF") > -1){
				tagName = "embed pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' ";
			}
			questionHTML += "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\"><"+tagName+" src='"+pollQuestion.getFileURL()+"'></td></tr>" ;
		}					
			
		//답변이  주관식일 경우 
		if(pollQuestion.getExampleType().equals("2")){
			questionHTML+="\r\n<tr><td><textarea name=\"exampleType_text_"+pollQuestion.getQuestionID()+"\" rows=\"5\", cols=\"80\"></textarea></td></tr>"
			 					+"\r\n</table>"
			 					+"\r\n</div>";
									
		}
		//객관식일 경우 
		else if(pollQuestion.getExampleType().equals("1")){
			
			for(int i=0;i<pollExamples.size();i++){		
				String inputTypeHTML = "";	
				String divStartHTML = "";	
				String divEndHTML = "";	
				//단일응답
				if(pollQuestion.getExampleGubun().equals("1")){
					inputTypeHTML = "<input type=\"radio\" class=\"notBorder\" name=\"exampleGubun_single_"+pollQuestion.getQuestionID()+"\" value=\""+ pollExamples.get(i).getExampleID()+"\" onclick=\"javascript:goToQuestionNum('"+ pollExamples.get(i).getGoToQuestionNo()+"')\">";
				//복수응답	
				}else{
					inputTypeHTML = "<input type=\"checkbox\" class=\"notBorder\" name=\"exampleGubun_multi_"+pollQuestion.getQuestionID()+"_"+pollExamples.get(i).getExampleID()+"\" value=\""+ pollExamples.get(i).getExampleID()+"\">";
				}
				
				//멀티미디어 처리
				if(pollExamples.get(i).getFileURL() != null && !pollExamples.get(i).getFileURL().equals("") && pollExamples.get(i).getLayoutType() != null && pollExamples.get(i).getLayoutType().equals("2")){
					inputTypeHTML = "\r\n<div style='clear:both;text-align:center'><img src='"+pollExamples.get(i).getFileURL()+"'>\r\n</div><div>"+inputTypeHTML;
					divStartHTML="<div style='clear:both;float:left;'>";
					divEndHTML="\r\n</div></div>";
				}
				if(pollExamples.get(i).getFileURL() != null && !pollExamples.get(i).getFileURL().equals("") && pollExamples.get(i).getLayoutType() != null && pollExamples.get(i).getLayoutType().equals("3")){
					inputTypeHTML = "\r\n<div style='float:left;'><img src='"+pollExamples.get(i).getFileURL()+"'></div>\r\n<div>"+inputTypeHTML;
					divStartHTML="<div style='clear:both;float:left;text-align:center;'>";
					divEndHTML="\r\n</div></div>";
				}
				
				if(pollExamples.get(i).getFileURL() != null && !pollExamples.get(i).getFileURL().equals("") && pollExamples.get(i).getLayoutType() != null && pollExamples.get(i).getLayoutType().equals("1")){
					divStartHTML="<div style='clear:both;float:left;'><div>";
					divEndHTML= "</div><div style='clear:both;text-align:center'><img src='"+pollExamples.get(i).getFileURL()+"'></div></div>";
				}
				
				if(pollExamples.get(i).getFileURL() != null && !pollExamples.get(i).getFileURL().equals("") && pollExamples.get(i).getLayoutType() != null && pollExamples.get(i).getLayoutType().equals("4")){
					divStartHTML="<div style='clear:both;float:left;text-align:center;'><div style='float:left;'>";
					divEndHTML= "</div><div><img src='"+pollExamples.get(i).getFileURL()+"'></div></div>";
				}
				
				if(i==0){
					questionHTML+="\r\n<tr>";
				}
				
				
				
				questionHTML+="\r\n<td>"+divStartHTML+inputTypeHTML+(i+1)+"."+pollExamples.get(i).getExampleDesc()+"&nbsp;"+divEndHTML;
				//주관식이 포함된 객관식이라면 
				if(pollExamples.get(i).getExampleExYN().equals("Y")){
					questionHTML+="<input type=\"text\"  name=\"exampleExYN_"+pollQuestion.getQuestionID()+"_"+pollExamples.get(i).getExampleID()+"\""+" size=\"50\">";
				}
				
				
				questionHTML+="\r\n</td>";
				
				if( (i+1) % pollQuestion.getExamplePosition()==0){
					questionHTML+="\r\n</tr>\r\n<tr>";	
				}
					
				//마지막처리 
				if( (i+1)==pollExamples.size()){
					if( ((i+1) % pollQuestion.getExamplePosition())!=0){ //나누어 떨어져 지지않는 마지막 처리 
						questionHTML+="\r\n</tr>";
					}
				}	
					
			}// end for							
			questionHTML+="\r\n</table>";							
		}
		//순위선택일 경우 
		else if(pollQuestion.getExampleType().equals("3")){
			String inputTypeHTML = "";	
			for(int i=0;i<pollExamples.size();i++){
				inputTypeHTML = "<select name=\"exampleGubun_select_"+pollQuestion.getQuestionID()+"_"+pollExamples.get(i).getExampleID()+"\">"
							  + "<option value=\"\">-- 순위선택 --</option>";
				for(int j=1;j<=pollExamples.size();j++){
					inputTypeHTML +="\r\n <option value=\""+j+"\">"+j+" 순위</option>";
				}
				inputTypeHTML +="\r\n </select>";
				if(i==0){
					questionHTML+="\r\n<tr>";
				}
				questionHTML+="\r\n<td>"+(i+1)+"."+pollExamples.get(i).getExampleDesc()+"</td><td>"+inputTypeHTML+"</td>";
				
				if( (i+1) % pollQuestion.getExamplePosition()==0){
					questionHTML+="\r\n</tr>\r\n<tr>";	
				}
					
				//마지막처리 
				if( (i+1)==pollExamples.size()){
					if( ((i+1) % pollQuestion.getExamplePosition())!=0){ //나누어 떨어져 지지않는 마지막 처리 
						questionHTML+="\r\n</tr>";
					}
				}	
			}// end for	
			questionHTML+="\r\n</table>\r\n</div>";	
		}
		//숫자입력일 경우 
		else if(pollQuestion.getExampleType().equals("4")){
			String beforeText =pollExamples.get(0).getExampleDesc().substring(0, pollExamples.get(0).getExampleDesc().indexOf("≠"));
			String afterText =pollExamples.get(0).getExampleDesc().substring(pollExamples.get(0).getExampleDesc().indexOf("≠")+1);
			questionHTML+="\r\n<tr><td>"+beforeText+" <input type=\"\" name=\"exampleType_text_"+pollQuestion.getQuestionID()+"\" size=\""+(pollQuestion.getExamplePosition()+3)+"\" /> "+afterText+"</td></tr>"
				+"\r\n</table>"
				+"\r\n</div>";
		}
		//척도형일 경우
		else if(pollQuestion.getExampleType().equals("5")){
			String barAddHTML = "\r\n<tr height=\"10\">\r\n<td width=\"50\"></td>"; 
			String radioAddHTML = "\r\n<tr>";
			for(int i=0;i<pollExamples.size();i++){
				if(i==0){
					questionHTML+="\r\n<tr><td><table border=\"0\" cellSpacing=\"0\" cellPadding=\"0\"><tr>";
				}else{
					barAddHTML +="\r\n<td width=\"4\" background=\"/images/scale_bar.gif\" style=\"background-repeat: repeat-x;\"/></td>"
				        +"\r\n<td width=\"50\" background=\"/images/scale_bg.gif\" style=\"background-repeat: repeat-x;\"></td>"
				        +"\r\n<td width=\"50\" background=\"/images/scale_bg.gif\" style=\"background-repeat: repeat-x;\"></td>";
				}
				
				questionHTML +="\r\n<td style=\"text-align: center;\" colSpan=\"3\">"+pollExamples.get(i).getExampleDesc()+"</td>";
				
				radioAddHTML +="\r\n<td style=\"text-align: center;\" colSpan=\"3\"><input type=\"radio\" name=\"exampleGubun_single_"+pollQuestion.getQuestionID()+"\" value=\""+ pollExamples.get(i).getExampleID()+"\" /></td>";
				
			}
			barAddHTML += "\r\n<td width=\"4\" background=\"/images/scale_bar.gif\" style=\"background-repeat: repeat-x;\"/></td>"
			            + "\r\n<td width=\"50\"></td>"
			            + "</tr>";
			
			questionHTML+="\r\n</tr>"+barAddHTML+radioAddHTML+"\r\n</tr></table></td></tr>"
						+"\r\n</table>\r\n</div>";	
			
		}
		
		return questionHTML;
		
	}
	
	/**
	 * <p>매트릭스질문에 따른 HTML 만들기 
	 * @param pollQuestion
	 * @param exampleDesc
	 * @return
	 */
	private String makeHTMLByQuestionMatrix(PollQuestion pollQuestion , List<PollExample> pollExamplesMatrixX, List<PollExample> pollExamplesMatrixY){	
		String questionHTML = 	 "<table>" ;
		if(pollQuestion.getQuestionHead() == null){
			questionHTML += "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\">&nbsp;</td></tr>";
		}else{
			questionHTML += "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\">"+pollQuestion.getQuestionHead().replace("\n", "<br>")+"</td></tr>";
		}
		questionHTML += "\r\n<tr><td colspan=\""+pollQuestion.getExamplePosition()+"\">"
						+"<table><tr><td valign=\"top\"><b>["+Constant.POLL_QUESTION_DES+pollQuestion.getQuestionNo()+"]</b></td><td align=\"left\">"+ pollQuestion.getQuestionText().replace("\n", "<br>")+"</td></td><tr></table></tr>";
		
		String inputTypeHTML = "";
		//객관식일 경우 			
	
		
		questionHTML += "\r\n<tr><td>"
							+"\r\n<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\">";
		
		
		//첫번째 row 
		questionHTML += "\r\n<tr><td style=\"background-color:#BED6ED;\">&nbsp;</td>";
		for(int j=0;j<pollExamplesMatrixY.size();j++){
			questionHTML += "<td align=\"center\" style=\"background-color:#BED6ED;\">&nbsp;"+pollExamplesMatrixY.get(j).getExampleDesc()+"&nbsp;</td>"	;
		}
		questionHTML += "</tr>";
		
		//두번째 row 부터 ~~
		for(int i=0;i<pollExamplesMatrixX.size();i++){
			questionHTML += "\r\n<tr><td style=\"background-color:#BED6ED;\">&nbsp;"+ pollExamplesMatrixX.get(i).getExampleDesc() +"&nbsp;</td>";
			for(int j=0;j<pollExamplesMatrixY.size();j++){				
				//객관일 경우 radio
				if(pollQuestion.getExampleType().equals("1")){
					inputTypeHTML = "<input type=\"radio\" class=\"notBorder\" name=\"exampleGubun_matrixRadio_"+pollQuestion.getQuestionID()+"_"+pollExamplesMatrixX.get(i).getExampleID()+"\" value=\""+ pollExamplesMatrixX.get(i).getExampleID()+":"+pollExamplesMatrixY.get(i).getExampleID()+"\">";
				}
				//주관식일 경우 text 
				else{
					inputTypeHTML = "<input type=\"text\" name=\"exampleGubun_matrixText_"+pollQuestion.getQuestionID()+"_"+pollExamplesMatrixX.get(i).getExampleID()+"_"+pollExamplesMatrixY.get(i).getExampleID()+"\"  size=\""+ pollQuestion.getMatrixTextSize()+"\">";
				}				
				questionHTML += "<td align=\"center\">"+inputTypeHTML+"</td>"	;	
			}
			questionHTML += "</tr>";
			
		}//end for 
		questionHTML+="\r\n</table>\r\n"
					+"\r\n</td></tr></tbody></table>";			
		
		return questionHTML;
		
	}
	
	/**
	 * <p>설문문항복사 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView copyPollQuestion(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		int pollTemplateID = ServletUtil.getParamInt(req,"ePollTemplateID","0");				
		int questionNo = ServletUtil.getParamInt(req,"eQuestionNo","0");
		String pollInfo = ServletUtil.getParamString(req,"ePollInfo");
		int questionID = ServletUtil.getParamInt(req,"eQuestionID","0");
		
		String isExistQuestion = "YES";

		if(pollInfo == null || pollInfo.equals("")){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 복사 할 파라미터정보가 없습니다.","");
			return null;
		}
		if(questionNo>0){
			req.setAttribute("selQuestionNos", Integer.toString(questionNo));	
		}
		
		int orgPollID = Integer.parseInt(StringUtil.getNthString(pollInfo, "-", 1));
		int orgQuestionID =  Integer.parseInt(StringUtil.getNthString(pollInfo, "-", 2));
		String questionType = StringUtil.getNthString(pollInfo, "-", 3);
		
		
		List<PollExample> pollExampleList = null; //일반질문용 보기
		List<PollExample> pollExampleListMatrixX = null; //매트릭스 가로보기 
		List<PollExample> pollExampleListMatrixY= null; //매트릭스 세로보기
		
		
		//일반 질문일 경우 
		if(questionType.equals("1")){
			if(db_type.equals(DB_TYPE_ORACLE))
				pollExampleList = pollService.selectPollExample(orgPollID, orgQuestionID, questionType, " ");	
			else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
				pollExampleList = pollService.selectPollExample(orgPollID, orgQuestionID, questionType, "");
			req.setAttribute("pollExampleList", pollExampleList);
		}
		//매트릭스일 경우 
		else if(questionType.equals("2")){
			pollExampleListMatrixX = pollService.selectPollExample(orgPollID, orgQuestionID, questionType, "X");
			pollExampleListMatrixY = pollService.selectPollExample(orgPollID, orgQuestionID, questionType, "Y");
			req.setAttribute("pollExampleListMatrixX", pollExampleListMatrixX);
			req.setAttribute("pollExampleListMatrixY", pollExampleListMatrixY);
		}
		if(questionID == 0){
			questionID = -1;
		}
		PollQuestion pollQuestion = pollService.viewPollQuestion(orgPollID, orgQuestionID);
		pollQuestion.setQuestionID(questionID);
		pollQuestion.setPollID(pollID);
		
		return new ModelAndView("/pages/content/poll/poll_question_make.jsp?pollID="+pollID+"&pollTemplateID="+pollTemplateID+"&isExistQuestion="+isExistQuestion,"pollQuestion", pollQuestion);	
	}
	
	/**
	 * <p>문항 멀티미디어 설정 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editMultimedia(HttpServletRequest req, HttpServletResponse res) throws Exception{
		return new ModelAndView("/pages/content/poll/poll_multimedia.jsp");
	 }
	
	/**
	 * <p>멀티미디어 파일을 저장한다.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void fileUpload(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
		
		int pollID = ServletUtil.getParamInt(req,"pollID","0");
		int questionID =ServletUtil.getParamInt(req,"questionID","0");
		int exampleNo = ServletUtil.getParamInt(req,"exampleNo","0");
		
		String newFileName = "";
		if(pollID == 0) {
			throw new Exception("추가시 pollID 파라미터로 반드시 전달 해야됨");
		}

		Iterator fileNameIterator = mreq.getFileNames();		
		while(fileNameIterator.hasNext()){
			MultipartFile multiFile = mreq.getFile((String)fileNameIterator.next());			
			if(multiFile.getSize()>0){
				newFileName = pollID+"_"+questionID+"_"+exampleNo+multiFile.getOriginalFilename().substring(multiFile.getOriginalFilename().indexOf(".")) ;
				FileUploadUtil.uploadNewFile(mreq, multiFile, this.realUploadPath, newFileName);
			}
		}
		
	}
	
	/**
	 * <p>스킵패턴
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editSkipPattern(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		int pollTemplateID = ServletUtil.getParamInt(req,"ePollTemplateID","0");				
		
		PollTemplate pollTemplate = pollService.viewPollTemplate(pollTemplateID);
		String templateHTML = pollTemplate.getPollTemplateHTML();
		String isExistQuestion = "YES";
	
		
		if(templateHTML.indexOf(Constant.POLL_TEMPLATE_QUESTIONS)==-1){
			isExistQuestion = "NO";  
		}
		return new ModelAndView("/pages/content/poll/poll_skippattern.jsp?pollID="+pollID+"&pollTemplateID="+pollTemplateID+"&isExistQuestion="+isExistQuestion);	
	}
	
	/**
	 * <p>객관식 (단일선택) 문항 리스트
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selSingularQuestion(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		return new ModelAndView("/pages/content/poll/poll_skippattern_proc.jsp?method=list","pollQuestionList",pollService.selSingularQuestion(pollID));	
	}
	
	/**
	 * <p>설문문항 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewSingularQuestion(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		int questionID = ServletUtil.getParamInt(req,"eQuestionID","0");
		List<PollExample> pollExampleList = pollService.selectPollExample(pollID, questionID, "1", " ");
		req.setAttribute("pollExampleList", pollExampleList);	
		
		
		return new ModelAndView("/pages/content/poll/poll_skippattern_proc.jsp?method=view","pollQuestion", pollService.viewPollQuestion(pollID, questionID));	
	}
	

	/**
	 * <p>설문 문항 수정 -스킵패턴
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateQuestionSkipPatten(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		int questionID = ServletUtil.getParamInt(req,"eQuestionID","0");	
		System.out.println("pollID : "+pollID);
		System.out.println("questionID : "+questionID);
		
		String[] goToQuestionNos = ServletUtil.getParamStringArray(req,"eGoToQuestionNo");
		String[] noResponseStarts = ServletUtil.getParamStringArray(req,"eResponseStart");
		String[] noResponseEnds = ServletUtil.getParamStringArray(req,"eResponseEnd");
		
		
		PollExample[] pollExamples = new PollExample[goToQuestionNos.length];	
		for(int i=0;i<goToQuestionNos.length;i++){
			pollExamples[i] = new PollExample();
			pollExamples[i].setQuestionID(questionID);
			pollExamples[i].setPollID(pollID);
			pollExamples[i].setExampleID(i+1);
			if(goToQuestionNos[i]==null || goToQuestionNos[i].equals("")){
				pollExamples[i].setGoToQuestionNo(0);
			}else{						
				pollExamples[i].setGoToQuestionNo(Integer.parseInt(goToQuestionNos[i]));
			}
			if(noResponseStarts[i]==null || noResponseStarts[i].equals("")){
				pollExamples[i].setNoResponseStart(0);
			}else{						
				pollExamples[i].setNoResponseStart(Integer.parseInt(noResponseStarts[i]));
			}
			
			if(noResponseEnds[i]==null || noResponseEnds[i].equals("")){
				pollExamples[i].setNoResponseEnd(0);
			}else{						
				pollExamples[i].setNoResponseEnd(Integer.parseInt(noResponseEnds[i]));
			}
		}
		int resultVal = pollService.updatePollSkipPattern(pollExamples);
		
		//새로만듬
		String newQuestionHTML = "<!-- #TM_SKIP_PATTERN_START_"+questionID+" -->";

		for(int i=0;i<pollExamples.length;i++){
			System.out.println("ResponseStart : "+pollExamples[i].getNoResponseStart());
			System.out.println("ResponseEnd : "+pollExamples[i].getNoResponseEnd());
			

			
			if(pollExamples[i].getNoResponseStart() > 0 && pollExamples[i].getNoResponseStart() == 0){
				newQuestionHTML+="\r\n<input type=\"hidden\" name=\"skipAnswer"+questionID+"_"+pollExamples[i].getExampleID()+"\" value=\""+pollExamples[i].getNoResponseStart()+"\"/>";
			}
			
			if(pollExamples[i].getNoResponseStart() == 0 && pollExamples[i].getNoResponseStart() > 0){
				newQuestionHTML+="\r\n<input type=\"hidden\" name=\"skipAnswer"+questionID+"_"+pollExamples[i].getExampleID()+"\" value=\""+pollExamples[i].getNoResponseEnd()+"\"/>";
			}
			
			if(pollExamples[i].getNoResponseStart() > 0 && pollExamples[i].getNoResponseStart() > 0){
				for(int j = pollExamples[i].getNoResponseStart();j <= pollExamples[i].getNoResponseEnd();j++){
					newQuestionHTML+="\r\n<input type=\"hidden\" name=\"skipAnswer"+questionID+"_"+pollExamples[i].getExampleID()+"\" value=\""+j+"\"/>";
				}
			}
			newQuestionHTML+= "\r\n<input type=\"hidden\" name=\"goToQuestionNum_"+questionID+"_"+pollExamples[i].getExampleID()+"\" value=\""+pollExamples[i].getGoToQuestionNo()+"\"/>";
		}
		
		newQuestionHTML+= "\r\n<!-- #TM_SKIP_PATTERN_END_"+questionID+" -->";
			
		Map<String,Object> resultMap = pollService.showResultDefaultPollHTML(pollID);
		String resultPollHTML = (String)resultMap.get("resultPollHTML");
		//기존 질문문항 html 부분을 찾는다. 
		String startHtml = "<!-- #TM_SKIP_PATTERN_START_"+questionID+" -->";
		String endHtml  = "<!-- #TM_SKIP_PATTERN_END_"+questionID+" -->";
			
		//기존에 설정 부분이 있다면
		if(resultPollHTML.indexOf(startHtml) > -1){
			int startIdx = resultPollHTML.indexOf(startHtml);
			int endIdx = resultPollHTML.indexOf(endHtml)+ endHtml.length();
			String oldQuestionHTML = resultPollHTML.substring(startIdx, endIdx);
				
			if(oldQuestionHTML==null || oldQuestionHTML.equals("")){
				ServletUtil.messageGoURL(res,"저장에 실패했습니다 \r\n기존에 저장된 질문을 읽어 올수 없습니다.","");
				return null;	
			}
			resultPollHTML = resultPollHTML.replace(oldQuestionHTML, newQuestionHTML);
		}
		//처음 설정이면
		else{
			newQuestionHTML += "<!-- #TM_QUESTION_END_"+questionID+" -->";
			String oldQuestionHTML = "<!-- #TM_QUESTION_END_"+questionID+" -->";
			resultPollHTML = resultPollHTML.replace(oldQuestionHTML, newQuestionHTML);
		}
			
		resultVal = resultVal * (pollService.updateDefaultResultPollHTML(pollID, resultPollHTML, resultPollHTML));
		if(resultVal>0){		
			ServletUtil.messageGoURL(res,"저장 되었습니다.","");
			return null;
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}	
	}
}
