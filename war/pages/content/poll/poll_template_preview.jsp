<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.*"%>
<%@page import="web.content.poll.model.PollTemplate"%>
<%@page import="web.content.poll.service.PollService"%>
<%@page import="web.content.poll.control.PollControlHelper"%>
<%@page import="web.common.util.LoginInfo"%>
<%
			
		
		String id = request.getParameter("id");
	
		String pollTemplateIDs = request.getParameter("pollTemplateID") == null ? "0" : request.getParameter("pollTemplateID").trim();	
		int pollTemplateID = Integer.parseInt(pollTemplateIDs);	
		//모델을 이용하여 리스트를 가져온다.
		PollService service = PollControlHelper.getUserService(application);		
		PollTemplate pollTemplate = service.viewPollTemplate(pollTemplateID);
			
%>
<%=pollTemplate.getPollTemplateHTML()%>