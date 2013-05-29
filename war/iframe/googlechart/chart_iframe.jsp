<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml"> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.massmail.statistic.service.MassMailStatService"%>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%>
<%@ page import="web.massmail.statistic.model.MassMailStatistic"%>

<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<% 

	String standard = request.getParameter("standard");
	int massmailID = new Integer(request.getParameter("massmailID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));
	String packages = request.getParameter("packages");
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("massmailID", massmailID);
	searchMap.put("scheduleID", scheduleID);
	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	List<MassMailStatistic> massMailStatisticList = null;
	String title ="시간별 통계";
	if(standard.equals("hourly")){	
		massMailStatisticList = service.statisticHourlyList(searchMap);
	}
	if(standard.equals("daily")){
		massMailStatisticList = service.statisticDailyList(searchMap);
		title ="일자별 통계";
	}
	if(standard.equals("domain")){
		massMailStatisticList = service.statisticDomainList(searchMap);
		title ="도메인별 통계";
	}
	if(standard.equals("target")){
		massMailStatisticList = service.statisticTargetList(searchMap);
		title ="대상자별 통계";
	}
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
        data.addColumn('number', '총발송통수'); 
        data.addColumn('number', '성공통수');
        data.addColumn('number', '실패통수');
        data.addColumn('number', '오픈통수');
        data.addColumn('number', '클릭통수');
        data.addColumn('number', '수신거부통수'); 
        data.addRows(<%=massMailStatisticList.size()%>); 
        <%
	        int i =0;
	        for(MassMailStatistic massMailStatistic : massMailStatisticList){%>
	        data.setValue(<%=i%>, 0, '<%=massMailStatistic.getViewStandard()%>'); 
	        data.setValue(<%=i%>, 1, <%=massMailStatistic.getSendTotal()%>); 
	        data.setValue(<%=i%>, 2, <%=massMailStatistic.getSuccessTotal()%>);
	        data.setValue(<%=i%>, 3, <%=massMailStatistic.getFailTotal()%>); 
	        data.setValue(<%=i%>, 4, <%=massMailStatistic.getOpenTotal()%>); 
	        data.setValue(<%=i%>, 5, <%=massMailStatistic.getClickTotal()%>);
	        data.setValue(<%=i%>, 6, <%=massMailStatistic.getRejectcallTotal()%>);
        <%	i++;
        	}
        %>
        var chart = "";
        <%if(packages.equals("columnchart")){%>
       		chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        <%}else{%>
    		chart = new google.visualization.LineChart(document.getElementById('chart_div'));
    	<%}%>  
        chart.draw(data, {width: 840, height: 300, is3D: true, title: '<%=title%>', legend: 'bottom',legendFontSize: 15, axisFontSize: 10}); 
      } 
    </script> 

</head>  
<body style="font-family: Arial;border: 0 none;">    
	<div id="chart_div"></div>  
</body>
</html>