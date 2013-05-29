<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<%
		String id = request.getParameter("id");
		String massmailID = request.getParameter("massmailID");
		String scheduleID = request.getParameter("scheduleID");
		String pollID = request.getParameter("pollID");
		String questionID = request.getParameter("questionID");
		String questionType = request.getParameter("questionType");

%> 
<div id="<%=id%>_button" style="padding:8px;margin-bottom:10px">
	<div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').showChart('columnchart')" style="cursor:pointer"><span>막대 그래프 조회</span></a></div>
	<% if(questionType.equals("2")){ %>
	<div class="btn_b" style="margin-left:5px;float:left"><a href="javascript:$('<%=id%>').showChart('columnchart_stacked')" style="cursor:pointer"><span>막대 그래프 조회(누적)</span></a></div>
	<%} %>
	<div class="btn_r" style="margin-left:5px;float:left"><a href="javascript:$('<%=id%>').showChart('linechart')" style="cursor:pointer"><span id="<%=id%>ChartBtn">선 그래프 조회</span></a></div>
	<% if(questionType.equals("1")){ %>
	<div class="btn_b" style="margin-left:5px;float:left"><a href="javascript:$('<%=id%>').showChart('piechart')" style="cursor:pointer"><span id="<%=id%>ChartBtn">원 그래프 조회</span></a></div>
	<%} %>
			
</div>
<div>
<IFRAME id="<%=id%>_PollChart" name="<%=id%>_PollChart" src="iframe/googlechart/poll_detail_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&pollID=<%=pollID%>&questionID=<%=questionID%>&questionType=<%=questionType%>&packages=columnchart" width="600px" height="400px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>    
</div>

<script type="text/javascript"> 

	$('<%=id%>').showChart = function(packages){		
		$('<%=id%>_PollChart').src="iframe/googlechart/poll_detail_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&pollID=<%=pollID%>&questionID=<%=questionID%>&questionType=<%=questionType%>&packages="+packages;	
	
	}
	
</script>