<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String webURL = request.getParameter("webURL");
	String encodingType = request.getParameter("encodingType");
	if(encodingType == null){
		encodingType="UTF-8";
	}
	ExtractorHTML ehtml = new ExtractorHTML(webURL);
	String content = ehtml.getHTML(encodingType);
%>
<%=content%>