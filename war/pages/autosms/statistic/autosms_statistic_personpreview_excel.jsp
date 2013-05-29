<%@ page contentType="application/vnd.ms-excel;charset=euc-kr"%><%@ page import="java.io.*" %><%@ page import="web.autosms.service.AutoSMSService"%><%@ page import="web.autosms.control.AutoSMSControlHelper"%><%@ page import="web.autosms.model.*"%><%@ page import="web.common.util.*"%><%@ page import="java.util.*"%><%
	
	String standard = request.getParameter("sStandard");
	String title = "";
	List<AutoSMSPerson> personPreviewList = null;
	AutoSMSService service = AutoSMSControlHelper.getUserService(application);
	
	String autosmsID = request.getParameter("autosmsID");
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
	searchMap.put("autosmsID", new Integer(autosmsID));
	searchMap.put("year", year);
	searchMap.put("month", month);
	searchMap.put("day", day);
	searchMap.put("standard", standard);
	searchMap.put("type", type);
	searchMap.put("key", key);
	try{
		personPreviewList = service.personPreview(searchMap);
	}catch(Exception e){
	}

	response.addHeader("Content-Disposition","attachment; filename="+ standard+"_"+type+"_sms.csv");
	if(personPreviewList != null){
	for(AutoSMSPerson porsonPreview : personPreviewList){%><%=porsonPreview.getReceiverPhone()%>
<%}}%>
