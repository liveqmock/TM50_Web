<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@page import="web.content.poll.model.PollCode"%>
<%@page import="web.content.poll.service.PollService"%>
<%@page import="web.content.poll.control.PollControlHelper"%>
<%@page import="web.common.util.LoginInfo"%>
  
<%
	
		String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();
		String codeID = request.getParameter("codeID") == null ? "" : request.getParameter("codeID").trim();
		String codeNos = request.getParameter("codeNo") == null ? "0" : request.getParameter("codeNo").trim();	
		int codeNo = Integer.parseInt(codeNos);	
		//모델을 이용하여 리스트를 가져온다.
		PollService service = PollControlHelper.getUserService(application);
		List<PollCode> pollCodeList = null;
		pollCodeList = service.selectPollCode(codeID);
			
%>
			<input type="hidden" id="eCodeID" name="eCodeID" value="<%=codeID %>">
			<ul id="eCodeNo"  class="selectBox" title="설문유형선택" <%=(mustInput.equals("Y")? "mustInput='Y' msg='설문유형선택'":"")%>>

<%
			if(pollCodeList.size()==0){							
%>						
				<li data="" select="yes">등록된 데이타가  없습니다</li>
<%
			}else{
				
%>
				<li data="" select="yes">--설문유형--</li>
<%
				
				for(int i=0;i<pollCodeList.size();i++){
					PollCode pollCode =  (PollCode)pollCodeList.get(i);
%>			
					
					<li data="<%=pollCode.getCodeNo() %>" <% if (pollCode.getCodeNo()==codeNo){%>select="yes"<%} %>><%=pollCode.getCodeDesc() %></li>
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>			
