<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml"> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.massmail.statistic.service.MassMailStatService"%>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%>
<%@ page import="web.massmail.statistic.model.MassMailStatisticFailCause"%>

<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<% 

	int massmailID = new Integer(request.getParameter("massmailID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));
	String packages = request.getParameter("packages");
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("massmailID", massmailID);
	searchMap.put("scheduleID", scheduleID);

	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	List<MassMailStatisticFailCause> massMailStatisticList = service.massMailStatisticFailCauseList(searchMap);
	
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
	        for(MassMailStatisticFailCause massMailStatistic : massMailStatisticList){%>
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