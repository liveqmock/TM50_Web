<%@ page language="java" contentType="application/vnd.ms-excel; charset=EUC-KR"    pageEncoding="EUC-KR"%><%@ page import="web.massmail.statistic.service.MassMailStatService"%><%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%><%@ page import="web.massmail.statistic.model.MassMailStatisticPoll"%><%@ page import="java.util.Map"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.List"%><% 
    int massmailID = new Integer(request.getParameter("massmailID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));
	int pollID = new Integer(request.getParameter("pollID"));
	int sendID = new Integer(request.getParameter("sendID"));
	String matrixX = request.getParameter("matrixX");
	String matrixY = request.getParameter("matrixY");
	String email = request.getParameter("email");
	
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("searchType", "");
	searchMap.put("searchText", "");

	searchMap.put("massmailID", massmailID);
	searchMap.put("scheduleID", scheduleID);
	searchMap.put("pollID", pollID);
	
	
	
	//엑셀파일 저장 정보
	response.setHeader("Content-Disposition", "attachment; filename=poll.csv");
	response.setHeader("Content-Description", "JSP Generated Data");
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	List<MassMailStatisticPoll> massMailStatisticPollList = service.viewDetailAnswer(pollID, massmailID, scheduleID, sendID, matrixX, matrixY);
	
%>
[응답 이메일],<%=email%>
[설문번호],[질문],[보기]
<%	
	String questioNo = "", questionText="",answerText="";
	String tempNo = "", tempQuestionText = "";
	String saveNo = "0", saveQuestionNo = "-1";
	
	for(MassMailStatisticPoll massMailStatisticPoll:massMailStatisticPollList){
		questioNo = Integer.toString(massMailStatisticPoll.getQuestionNo());
		questionText = massMailStatisticPoll.getQuestionText();
		
		if (Integer.valueOf(questioNo).equals(Integer.valueOf(saveQuestionNo) + 1)) {
			saveNo = "0";
		}
		
		if (!tempNo.equals(questioNo) && saveNo.equals("0")) {
			tempNo = questioNo;
			tempQuestionText = questionText;
			saveQuestionNo = questioNo;
		} else {
			tempNo = "";
			tempQuestionText = "";
			saveNo = "";				
		}

		//객관식일 경우 
		if(massMailStatisticPoll.getExampleType().equals("1") || massMailStatisticPoll.getExampleType().equals("5")){
			answerText = massMailStatisticPoll.getExampleID()+"."+massMailStatisticPoll.getExampleDesc();
			if(massMailStatisticPoll.getExampleID()==massMailStatisticPoll.getResponseExampleID()){
				answerText+="◀";
			}
		//주관식일 경우 		
		}else if(massMailStatisticPoll.getExampleType().equals("2")){
			answerText = massMailStatisticPoll.getResponseText();
		}
		 
%><%=tempNo%>,<%=web.common.util.StringUtil.replace(web.common.util.StringUtil.replace(tempQuestionText,",",""),"\r\n","") %>,<%=web.common.util.StringUtil.replace(web.common.util.StringUtil.replace(answerText,",",""),"\r\n","") %>
<%}%>