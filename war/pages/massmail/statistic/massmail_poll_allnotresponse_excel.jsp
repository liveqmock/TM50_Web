<%@ page language="java" contentType="application/vnd.ms-excel; charset=EUC-KR"    pageEncoding="EUC-KR"%><%@ page import="web.massmail.statistic.service.MassMailStatService"%><%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%><%@ page import="web.massmail.statistic.model.MassMailPersonPreview"%><%@ page import="java.util.Map"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.List"%><% 
    int massmailID = new Integer(request.getParameter("massmailID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));
	int pollID = new Integer(request.getParameter("pollID"));	
	int iTotalCnt = new Integer(request.getParameter("iTotalCnt"));
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("searchType", "");
	searchMap.put("searchText", "");

	searchMap.put("massmailID", massmailID);
	searchMap.put("scheduleID", scheduleID);
	searchMap.put("pollID", pollID);
	
	Map<String, Object> searchMapBackup = new HashMap<String, Object>(); 
	searchMapBackup.put("massmailID", massmailID);
	searchMapBackup.put("scheduleID", scheduleID);
	
	
	//엑셀파일 저장 정보
	response.setHeader("Content-Disposition", "attachment; filename=notResponsePoll.csv");
	response.setHeader("Content-Description", "JSP Generated Data");	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);	
	String backupYearMonth = service.getBackupYearMonth(searchMapBackup);
	List<MassMailPersonPreview> personNotResponseList = service.massmailPollAllNotResponse(1,iTotalCnt,searchMap,backupYearMonth);
%>[이메일],[발송시간]
<%	
	for(MassMailPersonPreview personNotResponse:personNotResponseList){
%><%=personNotResponse.getEmail()%>,<%=personNotResponse.getRegistDate()%>
<%}%>