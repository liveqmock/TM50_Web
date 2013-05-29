<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%>
<%@ page import="web.admin.login.control.LoginControlHelper"%>
<%@ page import="web.admin.login.service.LoginService"%>
<%@ page import="web.admin.login.model.LoginHistory"%>
<%
	SystemSetService sysService = SystemSetControllerHelper.getUserService(application);
	String remoteIP = request.getRemoteAddr();
	String accessIPUseYN = sysService.getSystemSetInfo("1","accessIPUseYN");
	boolean accessIP = true;
	if(accessIPUseYN.equals("Y")){
		if(remoteIP.equals("0:0:0:0:0:0:0:1")) remoteIP ="127.0.0.1";
		if(sysService.checkAccessIP(remoteIP) == 0){
			accessIP=false;
			LoginService loginService = LoginControlHelper.getUserService(application);
			LoginHistory loginHistory = new LoginHistory("- 접속 차단 - ", "N", "[접속 차단] 미승인 IP", remoteIP);
			loginService.insertLoginHistory(loginHistory);
		}
	}
	       
%>