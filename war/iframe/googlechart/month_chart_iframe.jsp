<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml"> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.massmail.statistic.service.MassMailStatService"%>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%>
<%@ page import="web.massmail.statistic.model.MassMailReportMonth"%>

<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<% 

	String type = request.getParameter("type");
	String year = request.getParameter("sYear");
	String month = request.getParameter("sMonth");
	String dateFrom = request.getParameter("sDateFrom");
	String dateTo = request.getParameter("sDateTo");
	String userID = request.getParameter("sUserID");
	String groupID = "";
	String title="";
	List<MassMailReportMonth> massMailReportMonthList = null;
	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	if(type.equals("domain")){
		massMailReportMonthList = service.massmailReportMonthDomainStatisticList(year, month, dateFrom, dateTo, userID, groupID);
	}else{
		massMailReportMonthList = service.massmailReportMonthTimeStatisticList(year, month, dateFrom, dateTo, userID, groupID);
	}
	
%>
<head>   
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>    
<title>Google Visualization API Sample</title>   
<script type="text/javascript" src="http://www.google.com/jsapi"></script>    
<script type="text/javascript"> 
      google.load("visualization", "1", {packages:["columnchart"]}); 
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
        data.addRows(<%=massMailReportMonthList.size()%>); 
        <%
	        int i =0;
	        for(MassMailReportMonth massMailReportMonth : massMailReportMonthList){%>
	        data.setValue(<%=i%>, 0, '<%=massMailReportMonth.getStandard()%>'); 
	        data.setValue(<%=i%>, 1, <%=massMailReportMonth.getSendTotal()%>); 
	        data.setValue(<%=i%>, 2, <%=massMailReportMonth.getSuccessTotal()%>);
	        data.setValue(<%=i%>, 3, <%=massMailReportMonth.getFailTotal()%>); 
	        data.setValue(<%=i%>, 4, <%=massMailReportMonth.getOpenTotal()%>); 
	        data.setValue(<%=i%>, 5, <%=massMailReportMonth.getClickTotal()%>);
	        data.setValue(<%=i%>, 6, <%=massMailReportMonth.getRejectcallTotal()%>);
        <%	i++;
        	}
        %>
       
       	chart = new google.visualization.ColumnChart(document.getElementById('chart_div')); 
        chart.draw(data, {width: 800, height: 300, is3D: true, title: '<%=title%>', legend: 'bottom',legendFontSize: 15, axisFontSize: 10}); 
      } 
    </script> 

</head>  
<body style="font-family: Arial;border: 0 none;">    
	<div id="chart_div"></div>  
</body>
</html>