<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml"> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.intermail.service.InterMailService"%>
<%@ page import="web.intermail.control.InterMailControlHelper"%>
<%@ page import="web.intermail.model.FailCauseStatistic"%>

<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<% 

	String standard = request.getParameter("standard");
	String packages = request.getParameter("packages");
	int intermailID = new Integer(request.getParameter("intermailID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));	
	String year = request.getParameter("sYear");
	String month = request.getParameter("sMonth");
	if(month.length()==1)
		month = "0"+month;
	String day = request.getParameter("sDay");
	if(day.length()==1)
		day = "0"+day;
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("intermailID", intermailID);
	searchMap.put("scheduleID", scheduleID);
	searchMap.put("year", year);
	searchMap.put("month", month);
	searchMap.put("day", day);
	
	InterMailService service = InterMailControlHelper.getUserService(application);
	List<FailCauseStatistic> massMailStatisticList = service.statisticFailCauseList(searchMap);
	
%>
<head>   
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>    
<title>Google Visualization API Sample</title>   
<script type="text/javascript" src="http://www.google.com/jsapi"></script>    
<script type="text/javascript"> 
	  google.load("visualization", "1", {packages:["<%=packages%>"]}); 
      google.setOnLoadCallback(drawChart); 
      function drawChart() { 
        var data = new google.visualization.DataTable(); 
        data.addColumn('string', 'standard'); 
        data.addColumn('number', '실패통수');
        
        data.addRows(<%=massMailStatisticList.size()%>); 
        <%
	        int i =0;
	        for(FailCauseStatistic massMailStatistic : massMailStatisticList){%>
	        data.setValue(<%=i%>, 0, '<%=massMailStatistic.getFailCauseDes()%>'); 
	        data.setValue(<%=i%>, 1, <%=massMailStatistic.getFailCount()%>); 
        <%	i++;
        	}
        %>
        var chart = "";
        <%if(packages.equals("barchart")){%>
        	chart =new google.visualization.BarChart(document.getElementById('chart_div'));
        <%}else{%>
        	chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        <%}%> 
        chart.draw(data, {width: 840, height: 300, is3D: true, title: '실패원인별 통계', legend: 'bottom', legendFontSize: 13, axisFontSize: 10}); 
      } 
    </script> 

</head>  
<body style="font-family: Arial;border: 0 none;">    
	<div id="chart_div"></div>  
</body>
</html>