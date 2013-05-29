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
		
					
		//모델을 이용하여 리스트를 가져온다.
		PollService service = PollControlHelper.getUserService(application);
		List<Map<String,Integer>> resultList = null;
		Map<String,Integer> resultMap = null;
		resultList = service.getPollQuestionNumber(pollID);
		int questionNo = 0;
		
%>					
			<select id="eNewQuestionNo" name="eNewQuestionNo">
			<option value="0">0</option>
			
<%
			if(resultList.size()==0){							
%>						
				<option value="1" selected>1</option>
<%
			}else{

				for(int i=0;i<resultList.size();i++){
					questionNo =  resultList.get(i).get("questionNo");					
%>			
					<option value="<%=questionNo %>"><%=questionNo%></option>					
<%
				}//end for 
%>
					
<%				
			}// end if 
%>
			</select>	
			
			
