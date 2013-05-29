<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@ page import="web.common.util.*" %>  
<%@ page import="web.massmail.write.service.MassMailService" %>    
<%@ page import="web.massmail.write.model.*" %>    
<%@ page import="web.massmail.write.control.MassMailControlHelper" %> 
<%
		String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();
		String sendedDate = request.getParameter("sendedDate") == null ? "" : request.getParameter("sendedDate").trim();
		
		//모델을 이용하여 리스트를 가져온다.
		MassMailService service = MassMailControlHelper.getUserService(application);
		List<BackupDate> backupDateList = service.getBackupDate();
	
%>
			
			<ul id="eSendedDate"  class="selectBox" <%=(mustInput.equals("Y")? "mustInput='Y' msg='사용기간'":"")%>>

<%
			if(backupDateList.size()==0){							
%>						
				<li data="" select="yes">사용가능한 대상 기간이  없습니다<%=mustInput %></li>
<%
			}else{
				
%>
<%
				for(int i=0;i<backupDateList.size();i++){
					BackupDate backupDate = (BackupDate)backupDateList.get(i);
%>			
					<li data="<%=backupDate.getBackupYearMonth() %>" <% if(backupDate.getBackupYearMonth().equals(sendedDate)){%>select="yes"<%} %>><%=DateUtils.reFormat(backupDate.getBackupYearMonth(),"yyyyMM","yyyy년MM월") %></li>					
<%
				}//end for 
				
			}// end if 
%>
			</ul>