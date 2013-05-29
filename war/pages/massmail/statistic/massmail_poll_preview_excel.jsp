<%@ page language="java" contentType="application/vnd.ms-excel; charset=EUC-KR"
    pageEncoding="EUC-KR"%><%@ page import="web.massmail.statistic.service.MassMailStatService"%><%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%><%@ page import="web.massmail.statistic.model.MassMailPersonPreview"%><%@ page import="java.util.Map"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.List"%><% 
    int massmailID = new Integer(request.getParameter("massmailID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));
	int pollID = new Integer(request.getParameter("pollID"));
	int questionID = new Integer(request.getParameter("questionID"));
	int exampleID = new Integer(request.getParameter("exampleID"));
	int iTotalCnt = new Integer(request.getParameter("iTotalCnt"));
	String matrixX = request.getParameter("matrixX");
	String matrixY = request.getParameter("matrixY");
	int ranking = new Integer(request.getParameter("ranking"));
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("searchType", "");
	searchMap.put("searchText", "");

	searchMap.put("massmailID", massmailID);
	searchMap.put("scheduleID", scheduleID);
	searchMap.put("pollID", pollID);
	searchMap.put("questionID", questionID);
	searchMap.put("exampleID", exampleID);
	searchMap.put("matrixX", matrixX);
	searchMap.put("matrixY", matrixY);
	searchMap.put("ranking", ranking);
	
	//엑셀파일 저장 정보
	response.setHeader("Content-Disposition", "attachment; filename=poll.csv");
	response.setHeader("Content-Description", "JSP Generated Data");	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	List<MassMailPersonPreview> personPreviewList = service.massmailPollPreview(1,iTotalCnt,searchMap);	
%>[이메일],[응답],[응답시간]
<%	
	for(MassMailPersonPreview personPreview:personPreviewList){
%><%=personPreview.getEmail()%>,<%=web.common.util.StringUtil.replace(web.common.util.StringUtil.replace(personPreview.getResponseText(),",",""),"\r\n","") %>,<%=personPreview.getRegistDate()%>
<%}%>