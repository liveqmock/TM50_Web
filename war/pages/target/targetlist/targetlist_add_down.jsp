<%@ page language="java" contentType="application/vnd.ms-excel; charset=EUC-KR"
    pageEncoding="EUC-KR"%><%@ page import="web.target.targetlist.control.TargetListControlHelper"%><%@ page import="web.target.targetlist.service.TargetListService"%><%@ page import="web.target.targetlist.model.*"%><%
    
    int targetAddID =  new Integer(request.getParameter("targetAddID"));
    TargetListService service = TargetListControlHelper.getUserService(application);
    
  	//엑셀파일 저장 정보
	response.setHeader("Content-Disposition", "attachment; filename=TargetAddInfo_"+targetAddID+".csv");
	response.setHeader("Content-Description", "JSP Generated Data");
	String directText = service.getDirectText(targetAddID);
    %><%=directText%>