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
	int questionID = new Integer(request.getParameter("questionID"));
	String questionType = request.getParameter("questionType");
	String packages = request.getParameter("packages");		//차트타입 
	
	boolean isStacked = false;
	if(questionType.equals("2")){		//매트릭스라면 막대그래프면 누적데이터로 표시 
		isStacked = false;
	}
	
	if(packages==null || packages.equals("")){
		packages="columnchart";	
	}
	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);	
	
	MassMailStatisticPoll massMailStatisticPoll = service.viewQuestion(pollID, questionID);
	
	
	
	int questionNo = massMailStatisticPoll.getQuestionNo();
	String questionText = massMailStatisticPoll.getQuestionText();
	
%>


	
<%	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------
	//일반 설문일 경우 
	if(questionType.equals("1")){
	
	List<MassMailStatisticPoll> massMailStatisticPollList = service.selectPollStatisticByQuestionID(massmailID,scheduleID,pollID,questionID);
	
%>
<html>
<head>   
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>    
<title>Google Visualization API Sample</title>   
<script type="text/javascript" src="https://www.google.com/jsapi"></script>   
<script type="text/javascript"> 
      google.load("visualization", "1", {packages:["<%=packages%>"]}); 
      google.setOnLoadCallback(drawChart); 
      
      function drawChart() {    	  
    	var data = new google.visualization.DataTable(); 
    	data.addColumn('string', 'standard');
    	data.addColumn('number','응답수');
        data.addRows(<%=massMailStatisticPollList.size()%>);
        <%
			for(int i=0;i<massMailStatisticPollList.size();i++){
				MassMailStatisticPoll massMailStatisticPoll2 = massMailStatisticPollList.get(i);				
		%>
            data.setValue(<%=i%>, 0, '<%=massMailStatisticPoll2.getExampleDesc()%>'); 
	        data.setValue(<%=i%>, 1, <%=massMailStatisticPoll2.getResponseCount()%>); 
	   <%}%>
	   
        var chart = "";
        <%if(packages.equals("columnchart")){%>
       	 	chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
       	<%}else if(packages.equals("linechart")){%>
         	chart = new google.visualization.LineChart(document.getElementById('chart_div'));
        <%}else{%>
       		chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        <%}%>  
        //chart.draw(data, {width: 550, height: 400, is3D: true, title: '[<%=questionNo%>]번 설문', legend: 'bottom', legendFontSize: 15, axisFontSize: 10});
        chart.draw(data, {width: 550, height: 400, is3D: true, title: '<%=questionNo%>.번 설문', hAxis: {title: '', titleTextStyle: {color: 'red'}}});
      } 
    </script> 

</head>  
<body style="font-family: Arial;border: 0 none;">
	<div id="chart_div"></div>  
</body>
</html>
<%

	//-----------------------------------------------------------------------------------------------------------------------------------------------------
	//매트릭스 설문일 경우 
	}else if(questionType.equals("2")){
		
		
		//세로(Y)축의 데이터를 가져온다. 			
		List<MassMailStatisticPoll> massMailStatisticPollListMatrixY = service.selectPollExampleMatrixY(pollID, questionID);
		
		//응답한 매트릭스 
		List<MassMailStatisticPoll> massMailStatisticPollAnswerList = service.selectPollAnswerMatrixXY(massmailID,scheduleID,pollID,questionID);



%>
<html>
<head>   
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>    
<title>Google Visualization API Sample</title>   
<script type="text/javascript" src="https://www.google.com/jsapi"></script>   
<script type="text/javascript"> 
      google.load("visualization", "1", {packages:["corechart"]}); 
      google.setOnLoadCallback(drawChart); 
      
      function drawChart() {    	  
    	var data = new google.visualization.DataTable(); 
    	data.addColumn('string', 'standard');
    	<%
    		//System.out.println("data.addColumn('string', 'standard')");
    	
    		for(int i=0;i<massMailStatisticPollAnswerList.size();i++){
    			MassMailStatisticPoll MassMailStatisticPollX1 = massMailStatisticPollAnswerList.get(i);
    	%>
    		data.addColumn('number','<%=MassMailStatisticPollX1.getExampleDesc()%>');
    		
    		
    	<%
    		
    		//System.out.println("data.addColumn('number','"+(MassMailStatisticPollX1.getExampleDesc())+"')");
    		}
    	%>
    	
    	
        data.addRows(<%=(massMailStatisticPollAnswerList.size()+1)%>);        
        
        <%        	
        	//System.out.println("data.addRows("+(massMailStatisticPollAnswerList.size()+1)+")");
        
        	for(int y=0;y<massMailStatisticPollListMatrixY.size();y++){
        		MassMailStatisticPoll MassMailStatisticPollY = massMailStatisticPollListMatrixY.get(y);        		
        %>
        	data.setValue(<%=y%>, 0, '<%=MassMailStatisticPollY.getExampleDesc()%>'); 
        	
        
        <%      
 		   	//System.out.println("data.setValue("+y+",0,'"+MassMailStatisticPollY.getExampleDesc()+"')"); 
        	}     
        %>
        
        <%
        for(int y=0;y<massMailStatisticPollListMatrixY.size();y++){
    		for(int x=0;x<massMailStatisticPollAnswerList.size();x++){    		
				MassMailStatisticPoll MassMailStatisticPollX2 = (MassMailStatisticPoll)massMailStatisticPollAnswerList.get(x);
    	%>
    		
    		data.setValue(<%=y%>, <%=(x+1)%>, <%=MassMailStatisticPollX2.arrayListValue.get(y) %>); 
    	<%
    		//System.out.println("data.setValue("+y+","+(x+1)+","+MassMailStatisticPollX2.arrayListValue.get(y)+")");    	
    		}
    	}
    	%>
      
    	
	   
        var chart = "";
        <%if(packages.equals("columnchart")){%>
       	 	chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));       	 	 
       		chart.draw(data, {width: 550, height: 400, is3D: true, title: '<%=questionNo%>.번 설문', hAxis: {title: '', titleTextStyle: {color: 'red'}}});
       <%}else if(packages.equals("columnchart_stacked")){%>
       	 	chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
       		chart.draw(data, {width: 550, height: 400, is3D: true, title: '<%=questionNo%>.번 설문', isStacked:true, hAxis: {title: '', titleTextStyle: {color: 'red'}}});       		  
       	<%}else if(packages.equals("linechart")){%>
         	chart = new google.visualization.LineChart(document.getElementById('chart_div'));
         	chart.draw(data, {width: 550, height: 400, title: '<%=questionNo%>.번 설문'});
        <%}else{%>
       		chart = new google.visualization.PieChart(document.getElementById('chart_div'));
       		chart.draw(data, {width: 550, height: 400, title: '<%=questionNo%>.번 설문'});
        <%}%>  
         
      } 
    </script> 

</head>  
<body style="font-family: Arial;border: 0 none;">
	<div id="chart_div"></div>  
</body>
</html>

<%} %>
