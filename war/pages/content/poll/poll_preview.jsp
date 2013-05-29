<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.*"%>
<%@page import="web.content.poll.model.PollInfo"%>
<%@page import="web.content.poll.service.PollService"%>
<%@page import="web.content.poll.control.PollControlHelper"%>
<%@page import="web.common.util.LoginInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>설문미리보기</title>
<link href="/fckeditor/editor/css/fck_editorarea.css" type="text/css" rel="stylesheet" />
</head>
<body>

<%
	
		String id = request.getParameter("id");				
		String pollIDs = request.getParameter("pollID") == null ? "0" : request.getParameter("pollID").trim();
		
%>
<%

if(!pollIDs.equals("") && !pollIDs.equals("0")){
		
				
		int pollID = Integer.parseInt(pollIDs);	
		//모델을 이용하여 리스트를 가져온다.
		PollService service = PollControlHelper.getUserService(application);		
		PollInfo pollInfo = service.viewPollInfo(pollID);
			
%>	
<%=pollInfo.getResultPollHTML()%>
<%} %>
</body>
</html>