<%@ page contentType="application/vnd.ms-excel;charset=euc-kr"%><%@ page import="java.io.*" %><%@ page import="web.automail.service.AutoMailService"%><%@ page import="web.automail.control.AutoMailControlHelper"%><%@ page import="web.automail.model.*"%><%@ page import="web.common.util.*"%><%@ page import="java.util.*"%><%
	
	String standard = request.getParameter("sStandard");
	String title = "";
	List<MailStatistic> porsonPreviewList = null;
	AutoMailService service = AutoMailControlHelper.getUserService(application);
	
	String automailID = request.getParameter("automailID");
	String year = request.getParameter("sYear");
	String month = request.getParameter("sMonth");
	if(month.length()==1)
		month = "0"+month;
	String day = request.getParameter("sDay");
	if(day.length()==1)
		day = "0"+day;
	String key = request.getParameter("key");
	String type = request.getParameter("type");
	String sSearchType = request.getParameter("sSearchType");
	String sSearchText = request.getParameter("sSearchText");
	int curPage = 0;
	int iLineCnt = 0;
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("searchType", sSearchType);
	searchMap.put("searchText", sSearchText);
	searchMap.put("curPage", curPage);
	searchMap.put("iLineCnt", iLineCnt);
	searchMap.put("automailID", new Integer(automailID));
	searchMap.put("year", year);
	searchMap.put("month", month);
	searchMap.put("day", day);
	searchMap.put("standard", standard);
	searchMap.put("type", type);
	searchMap.put("key", key);
	try{
		porsonPreviewList = service.porsonPreview(searchMap);
	}catch(Exception e){
	}

	response.addHeader("Content-Disposition","attachment; filename="+ standard+"_"+type+"_email.csv");
	if(porsonPreviewList != null){
	for(MailStatistic porsonPreview : porsonPreviewList){%><%=porsonPreview.getEmail()%>
<%}}%>
