<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml"> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.intermail.service.InterMailService"%>
<%@ page import="web.intermail.control.InterMailControlHelper"%>
<%@ page import="web.intermail.model.MailStatistic"%>

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
	
	String title ="시간별 통계";
	
	InterMailService service = InterMailControlHelper.getUserService(application);
	List<MailStatistic> mailStatisticList = null;
	
	if(standard.equals("hourly")){	
		mailStatisticList = service.statisticHourlyList(searchMap);
	}
	if(standard.equals("daily")){
		mailStatisticList = service.statisticDailyList(searchMap);
		title="일자별 통계";
	}
	if(standard.equals("monthly")){
		mailStatisticList = service.statisticMonthlyList(searchMap);
		title="월별 통계";
	}
	if(standard.equals("domain")){
		mailStatisticList = service.statisticDomainList(searchMap);
		title="도메인별 통계";
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
        data.addRows(<%=mailStatisticList.size()%>); 
        <%
        	int i =0;
        	for(MailStatistic mailStatistic : mailStatisticList){%>
        		data.setValue(<%=i%>, 0, '<%=mailStatistic.getStandard()%>'); 
        		data.setValue(<%=i%>, 1, <%=mailStatistic.getSendTotal()%>); 
    	        data.setValue(<%=i%>, 2, <%=mailStatistic.getSuccessTotal()%>);
    	        data.setValue(<%=i%>, 3, <%=mailStatistic.getFailTotal()%>); 
    	        data.setValue(<%=i%>, 4, <%=mailStatistic.getOpenTotal()%>); 
        <%
        	i++;
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