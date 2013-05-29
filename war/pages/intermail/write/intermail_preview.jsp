<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.*"%>
<%@page import="web.intermail.model.InterMailSend"%>
<%@page import="web.intermail.service.InterMailService"%>
<%@page import="web.intermail.control.InterMailControlHelper"%>
<%@page import="web.common.util.ServletUtil"%>

<%
	
		
		int intermailID =  ServletUtil.getParamInt(request,"intermailID","0");
		int scheduleID =  ServletUtil.getParamInt(request,"scheduleID","0");
		String sendID =  ServletUtil.getParamString(request,"sendID");
		String flag = ServletUtil.getParamString(request,"flag");

%>
<%

if(intermailID!=0 && scheduleID!=0 && !sendID.equals("")){
	
	String content = "";
	
	//모델을 이용하여 리스트를 가져온다.
	InterMailService service = InterMailControlHelper.getUserService(application);		
	InterMailSend interMailSend = service.viewInterMailSend(intermailID, scheduleID, sendID);
	
	if(flag.equals("mailContent")){
		content = interMailSend.getMailContent();
	}
	else if(flag.equals("fileContent")){
		content = interMailSend.getFileContent();		
	}
			
%>
<%=content%>
<%} %>
