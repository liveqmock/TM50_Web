<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String automailID = request.getParameter("automailID");
	String sYear = request.getParameter("sYear");
	String sMonth = request.getParameter("sMonth");
	String sDay = request.getParameter("sDay");
	String standard = request.getParameter("standard");
	standard= "statistic"+standard.substring(0,1).toUpperCase()+standard.substring(1)+"Pie";
	
%>

	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<h2>Pie Chart</h2>
		<div class="dash_box_content">
			<div id="<%=id%>Chart" ></div>
		</div>
		        	
	</div>
    
<script type="text/javascript">


/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/
	/* 리스트 표시 */
	window.addEvent("domready",function () {
		createSwfobject(
				  'swf/open-flash-chart.swf', '<%=id%>Chart',
				  '100%', '400px','9.0.0', 'swf/expressInstall.swf',
				  {'data-file': escape('automail/automail.do?method=<%=standard%>&automailID=<%=automailID%>&sYear=<%=sYear%>&sMonth=<%=sMonth%>&sDay=<%=sDay%>')});
	});

</script>