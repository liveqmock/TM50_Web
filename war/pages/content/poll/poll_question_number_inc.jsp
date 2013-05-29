<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.*"%>
<%@page import="web.content.poll.model.PollTemplate"%>
<%@page import="web.content.poll.service.PollService"%>
<%@page import="web.content.poll.control.PollControlHelper"%>
<%@page import="web.common.util.*"%>

  
<%
			
		String id = request.getParameter("id");		
		String pollIDs = request.getParameter("pollID") == null ? "0" : request.getParameter("pollID").trim();	
		int pollID = Integer.parseInt(pollIDs);	
		
		
		//기 선택된 설문ID
		String selQuestionNos = request.getParameter("selQuestionNos") == null ? "0" : request.getParameter("selQuestionNos").trim();	
		if(selQuestionNos.equals("")) selQuestionNos = "0";
		int selQuestionNo = Integer.parseInt(selQuestionNos);	
		
				
		//모델을 이용하여 리스트를 가져온다.
		PollService service = PollControlHelper.getUserService(application);
		List<Map<String,Integer>> resultList = null;
		Map<String,Integer> resultMap = null;
		resultList = service.getPollQuestionNumber(pollID);
		int questionNo = 0;
		
%>					
			<select id="eQuestionNo" name="eQuestionNo" onchange="javascript:$('<%=id%>_question').selectQuestion(this.value)">
			
<%
			if(resultList.size()==0){							
%>						
				<option value="1" selected>1</option>
<%
			}else{

				for(int i=0;i<resultList.size();i++){
					questionNo =  resultList.get(i).get("questionNo");					
%>			
					<option value="<%=questionNo %>" <% if (questionNo==selQuestionNo){%>selected<%} %>><%=questionNo%></option>					
<%
				}//end for 
%>
					<option value="<%=questionNo+1%>" <% if(selQuestionNo==0 || (selQuestionNo>resultList.size()) ){ %>selected <%} %>><%=(questionNo+1)%></option>
<%				
			}// end if 
%>
			</select>	
			
			
