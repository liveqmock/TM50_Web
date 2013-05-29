<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml"> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.massmail.statistic.service.MassMailStatService"%>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%>
<%@ page import="web.massmail.statistic.model.MassMailStatisticPoll"%>

<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<% 
	int massmailID = new Integer(request.getParameter("massmailID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));
	int pollID = new Integer(request.getParameter("pollID"));
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("massmailID", massmailID);
	searchMap.put("scheduleID", scheduleID);
	String packages = request.getParameter("packages");
	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	
	
	
	String backupYearMonth = service.getBackupYearMonth(searchMap);
	
	Map<String,Object> massMailStatisticPollList = null;
	
	//이미 백업됬어다면 백업테이블로 조회 
	if(backupYearMonth!=null && !backupYearMonth.equals("")){
		massMailStatisticPollList = service.massmailPollStatisticCountFinish(backupYearMonth,massmailID, scheduleID, pollID);
	}else{
		massMailStatisticPollList = service.massmailPollStatisticCount(massmailID, scheduleID, pollID);
	}
	
	
	//전체발송통수
	int sendTotalCount = Integer.parseInt(String.valueOf(massMailStatisticPollList.get("sendTotalCount")));
	
	//전체응답통수 
	int responseCount = Integer.parseInt(String.valueOf(massMailStatisticPollList.get("responseCount")));
	
	int notresponseCount = sendTotalCount - responseCount;
	
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
        data.addColumn('string', 'poll');        
        data.addColumn('number', '인원');
        
        
        data.addRows(<%=massMailStatisticPollList.size()%>); 
        data.setValue(0, 0, '응답 수(<%=responseCount%>명)'); 
        data.setValue(0, 1, <%=responseCount%>);
        data.setValue(1, 0, '미응답 수(<%=notresponseCount%>명)'); 
        data.setValue(1, 1, <%=notresponseCount%>);
       
        var chart = "";
        <%if(packages.equals("columnchart")){%>
       	 	chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        <%}else{%>
       		 chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        <%}%>  
        chart.draw(data, {width: 840, height: 300, is3D: true, title: '설문 통계(전체 발송통수 :<%=sendTotalCount%>)', legend: 'bottom', legendFontSize: 15, axisFontSize: 10}); 
      } 
    </script> 

</head>  
<body style="font-family: Arial;border: 0 none;">    
	<div id="chart_div"></div>  
</body>
</html>