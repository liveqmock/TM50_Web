<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml"> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.massmail.statistic.service.MassMailStatService"%>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%>
<%@ page import="web.massmail.statistic.model.MassMailStatisticUsers"%>
<%@ page import="web.common.util.LoginInfo"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<% 

	String packages = request.getParameter("packages");
	String year = request.getParameter("sYear");
	String month = request.getParameter("sMonth");
	String userID = request.getParameter("sUserID");
	String groupID = "";
	Map<String, String> searchMap = new HashMap<String, String>(); 
	searchMap.put("year", year);
	searchMap.put("month", month);
	searchMap.put("groupID", groupID);
	searchMap.put("userID", userID);
	
	String[] userInfo = new String[3];
	userInfo[0] =  LoginInfo.getUserAuth(request); 
	userInfo[1] =  LoginInfo.getUserID(request);
	userInfo[2] =  LoginInfo.getGroupID(request);
	
	
	
	List<MassMailStatisticUsers>  massMailStatisticUsersList = null;
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	massMailStatisticUsersList = service.massmailStatisticUsersList(userInfo, searchMap);
	
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
        data.addColumn('string', 'USERID'); 
        data.addColumn('number', '총발송통수'); 
        data.addColumn('number', '성공통수');
        data.addColumn('number', '실패통수');
        data.addColumn('number', '오픈통수');
        data.addColumn('number', '클릭통수');
        data.addColumn('number', '수신거부통수'); 
        data.addRows(<%=massMailStatisticUsersList.size()%>); 
        <%
	        int i =0;
	        for(MassMailStatisticUsers massMailStatisticUsers : massMailStatisticUsersList){%>
	        data.setValue(<%=i%>, 0, '<%=massMailStatisticUsers.getUserID()%>'); 
	        data.setValue(<%=i%>, 1, <%=massMailStatisticUsers.getSendTotal()%>); 
	        data.setValue(<%=i%>, 2, <%=massMailStatisticUsers.getSuccessTotal()%>);
	        data.setValue(<%=i%>, 3, <%=massMailStatisticUsers.getFailTotal()%>); 
	        data.setValue(<%=i%>, 4, <%=massMailStatisticUsers.getOpenTotal()%>); 
	        data.setValue(<%=i%>, 5, <%=massMailStatisticUsers.getClickTotal()%>);
	        data.setValue(<%=i%>, 6, <%=massMailStatisticUsers.getRejectcallTotal()%>);
        <%	i++;
        	}
        %>
       
        var chart = "";
        <%if(packages.equals("columnchart")){%>
       		chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        <%}else{%>
        	chart = new google.visualization.PieChart(document.getElementById('chart_div'));
    	<%}%>  
        chart.draw(data, {width: 800, height: 300, is3D: true, title: '계정별발송현황', legend: 'bottom',legendFontSize: 15, axisFontSize: 10}); 
      } 
    </script> 

</head>  
<body style="font-family: Arial;border: 0 none;">    
	<div id="chart_div"></div>  
</body>
</html>