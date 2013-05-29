<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@page import="web.content.poll.model.PollCode"%>
<%@page import="web.content.poll.service.PollService"%>
<%@page import="web.content.poll.control.PollControlHelper"%>
<%@page import="web.common.util.LoginInfo"%>
  
<%
	
		String id = request.getParameter("id");
		String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();
		String codeType = request.getParameter("codeType") == null ? "" : request.getParameter("codeType").trim();
		
		String codeID = request.getParameter("codeID") == null ? "" : request.getParameter("codeID").trim();
	
			
		
		//모델을 이용하여 리스트를 가져온다.
		PollService service = PollControlHelper.getUserService(application);
		List<PollCode> pollCodeList = null;
		pollCodeList = service.selectPollCodeType(codeType);
			
%>
			<div class="left">
			
			<ul id="eCodeType" class="selectBox" onclick="javascript:$('<%=id %>_question').selectCodeID(this.value);" title="응답유형선택" <%=(mustInput.equals("Y")? "mustInput='Y' msg='응답유형선택'":"")%>>

<%
			if(pollCodeList.size()==0){							
%>						
				<li data="" select="yes">등록된 데이타가  없습니다</li>
<%
			}else{
				
%>
				<li data="CUSTOM">사용자 직접입력</li>
<%
				
				for(int i=0;i<pollCodeList.size();i++){
					PollCode pollCode =  (PollCode)pollCodeList.get(i);
%>			
					
					<li data="<%=pollCode.getCodeID() %>" <% if (pollCode.getCodeID().equals(codeID)){%>select="yes"<%} %>><%=pollCode.getCodeName() %></li>
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>		
			</div>
			
<%
	
if(!codeID.equals("")){		
		
				
			
		//모델을 이용하여 리스트를 가져온다.		
		List<PollCode> pollCodeDescList = null;
		pollCodeDescList = service.selectPollCode(codeID);
		
		
			
%>
			<div class="left">
			<table width="100%">
			<tbody>		
<%
				
			for(int i=0;i<pollCodeDescList.size();i++){
				PollCode pollCode =  (PollCode)pollCodeDescList.get(i);
%>			
								
			<tr>						
			<td><%=i+1%>.<input type="hidden" name="eCodeNo" value="<%=pollCode.getCodeDesc() %>"><%=pollCode.getCodeDesc() %></td>
			</tr>
			
<%		
			}// end for			
%>			
			</tbody>
			</table>	
			</div>
<%} %>

