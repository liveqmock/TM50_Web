<%@ page language="java" contentType="application/vnd.ms-excel; charset=EUC-KR"
    pageEncoding="EUC-KR"%><%@ page import="web.masssms.statistic.service.MassSMSStatService"%><%@ page import="web.masssms.statistic.control.MassSMSStatControlHelper"%><%@ page import="web.masssms.statistic.model.MassSMSPersonPreview"%><%@ page import="java.util.Map"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.List"%><% int masssmsID = new Integer(request.getParameter("masssmsID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));
	String standard = request.getParameter("standard");

	String type = request.getParameter("type");
	String key = request.getParameter("key");
	int iTotalCnt = new Integer(request.getParameter("iTotalCnt"));
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("searchType", "");
	searchMap.put("searchText", "");
	searchMap.put("curPage", 1);
	searchMap.put("iLineCnt", iTotalCnt);
	searchMap.put("iTotalCnt", iTotalCnt);
	searchMap.put("masssmsID", masssmsID);
	searchMap.put("scheduleID", scheduleID);
	searchMap.put("standard", standard);
	searchMap.put("type", type);
	searchMap.put("key", key);
	
	//엑셀파일 저장 정보
	response.setHeader("Content-Disposition", "attachment; filename=" + standard + ".csv");
	response.setHeader("Content-Description", "JSP Generated Data");
	
	MassSMSStatService service = MassSMSStatControlHelper.getUserService(application);
	List<MassSMSPersonPreview> personPreviewList = service.personPreview(searchMap);
	
%>[핸드폰],[SMS코드],[SMS코드메시지]
<%	
for(MassSMSPersonPreview personPreview:personPreviewList){
%><%=personPreview.getReceiverPhone()%>,<%=personPreview.getSmsCode()%>,<%=web.common.util.StringUtil.replace(personPreview.getSmsCodeMsg(),",","") %>
<%}%>