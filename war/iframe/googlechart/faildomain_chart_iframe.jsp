<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml"> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.massmail.statistic.service.MassMailStatService"%>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%>
<%@ page import="web.massmail.statistic.model.MassMailStatisticDomainFail"%>

<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<% 
	int massmailID = new Integer(request.getParameter("massmailID"));
	int scheduleID = new Integer(request.getParameter("scheduleID"));
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("massmailID", massmailID);
	searchMap.put("scheduleID", scheduleID);
	String packages = request.getParameter("packages");
	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	List<MassMailStatisticDomainFail> massMailStatisticList = service.statisticFailDomainList(searchMap);
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
        data.addColumn('number', '전체실패통수');
        data.addColumn('number', '일시적인오류통수');
        data.addColumn('number', '영구적인오류');
        data.addColumn('number', '기타오류');
        data.addColumn('number', '결과확인 불명'); 
        
        data.addRows(<%=massMailStatisticList.size()%>); 
        <%
	        int i =0;
	        for(MassMailStatisticDomainFail massMailStatistic : massMailStatisticList){%>
	        data.setValue(<%=i%>, 0, '<%=massMailStatistic.getDomainName()%>'); 
	        data.setValue(<%=i%>, 1, <%=massMailStatistic.getFailCount()%>); 
	        data.setValue(<%=i%>, 2, <%=massMailStatistic.getFailcauseType1Count()%>); 
	        data.setValue(<%=i%>, 3, <%=massMailStatistic.getFailcauseType2Count()%>); 
	        data.setValue(<%=i%>, 4, <%=massMailStatistic.getFailcauseType3Count()%>); 
	        data.setValue(<%=i%>, 5, <%=massMailStatistic.getFailcauseType4Count()%>); 
        <%	i++;
        	}
        %>
        var chart = "";
        <%if(packages.equals("columnchart")){%>
       	 	chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        <%}else{%>
       		 chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        <%}%>  
        chart.draw(data, {width: 840, height: 300, is3D: true, title: '실패도메인별 통계', legend: 'bottom', legendFontSize: 15, axisFontSize: 10}); 
      } 
    </script> 

</head>  
<body style="font-family: Arial;border: 0 none;">    
	<div id="chart_div"></div>  
</body>
</html>