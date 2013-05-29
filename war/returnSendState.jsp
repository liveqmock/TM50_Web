<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.common.util.CommonAccessContolHelper"%> 
<%@ page import="web.common.service.CommonAccessService"%> 



<% request.setCharacterEncoding("utf-8"); %>
<%

	String type = request.getParameter("type");
if(type.equals("check"))
{
	CommonAccessService service = CommonAccessContolHelper.getUserService(application);
	boolean result = service.isSendMassMail();
	
	%><%=result %><%
}
else
{
	String domainFlag = request.getParameter("domainFlag");
	
	if(domainFlag.equals("1"))
	{			
		web.common.util.TmInstaller.stopService("TM_MassMailPrepareMain");
		web.common.util.TmInstaller.stopService("TM_MassMailSendMain");
		web.common.util.TmInstaller.stopService("TM_MassMailStatisticMain");
		web.common.util.TmInstaller.stopService("TM_MassMailBackupMain");
		web.common.util.TmInstaller.stopService("TM_ObserverMain");				
		
		web.common.util.TmInstaller.startService("TM_MassMailPrepareMain");
		web.common.util.TmInstaller.startService("TM_MassMailSendMain");
		web.common.util.TmInstaller.startService("TM_MassMailStatisticMain");
		web.common.util.TmInstaller.startService("TM_MassMailBackupMain");
		web.common.util.TmInstaller.startService("TM_ObserverMain");
	}
	else if(domainFlag.equals("2"))
	{
		web.common.util.TmInstaller.stopService("TM_AutoMailSendMain");
		web.common.util.TmInstaller.stopService("TM_AutoMailStatisticMain");
		
		web.common.util.TmInstaller.startService("TM_AutoMailSendMain");
		web.common.util.TmInstaller.startService("TM_AutoMailStatisticMain");
		
	}	
	
	
}
%>


