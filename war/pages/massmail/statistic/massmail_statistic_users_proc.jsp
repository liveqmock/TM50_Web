<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String year = request.getParameter("sSendedYear");
String month = request.getParameter("sSendedMonth");



//계정별 통계 
if(method.equals("massmailStatisticUsersList")){
%>
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<c:set var="sendTotalHap" value="0"/>
	<c:set var="successTotalHap" value="0"/>
	<c:set var="failTotalHap" value="0"/>
	<c:set var="openTotalHap" value="0"/>
	<c:set var="clickTotalHap" value="0"/>
	<c:set var="rejectcallTotalHap" value="0"/>
	
	<c:forEach items="${massmailStatUsersList}" var="massMailStatisticUsers">
		<TR class="tbl_tr" >
			<TD class="tbl_td" ><b><c:out value="${massMailStatisticUsers.userName}"/></b></TD>		
			<TD class="tbl_td"><fmt:formatNumber value="${massMailStatisticUsers.sendTotal}" type="number"/></TD>	
			<TD class="tbl_td"><fmt:formatNumber value="${massMailStatisticUsers.successTotal}" type="number"/></TD>		
			<TD class="tbl_td"><fmt:formatNumber value="${massMailStatisticUsers.failTotal}" type="number"/></TD>	
			<TD class="tbl_td"><fmt:formatNumber value="${massMailStatisticUsers.openTotal}" type="number"/></TD>	
			<TD class="tbl_td"><fmt:formatNumber value="${massMailStatisticUsers.clickTotal}" type="number"/></TD>
			<TD class="tbl_td"><fmt:formatNumber value="${massMailStatisticUsers.rejectcallTotal}" type="number"/></TD>
		</TR>
		<c:set var="sendTotalHap" value="${sendTotalHap + massMailStatisticUsers.sendTotal}"/>
		<c:set var="successTotalHap" value="${successTotalHap + massMailStatisticUsers.successTotal}"/>
		<c:set var="failTotalHap" value="${failTotalHap + massMailStatisticUsers.failTotal}"/>
		<c:set var="openTotalHap" value="${openTotalHap + massMailStatisticUsers.openTotal}"/>
		<c:set var="clickTotalHap" value="${clickTotalHap + massMailStatisticUsers.clickTotal}"/>
		<c:set var="rejectcallTotalHap" value="${rejectcallTotalHap + massMailStatisticUsers.rejectcallTotal}"/>
		
	</c:forEach>
	<c:if test="${!empty massmailStatUsersList}">
		<TR >
			<TD class="tbl_td" >합 계</TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${sendTotalHap}" type="number"/></TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${successTotalHap}" type="number"/></TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${failTotalHap}" type="number"/></TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${openTotalHap}" type="number"/></TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${clickTotalHap}" type="number"/></TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${rejectcallTotalHap}" type="number"/></TD>
		</TR>
	</c:if>
	<script type="text/javascript">	
		window.addEvent('domready', function(){
		
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
			});
			$('<%=id%>').grid_content.render();
		});
	</script>	
<%
}
//****************************************************************************************************/
// Bar 차트 조회
//****************************************************************************************************/
if(method.equals("massmailStatisticUsersBar")) {
	String sUserID = request.getParameter("sUserID");
%>
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<h2>Bar Chart</h2>
		  	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/user_chart_iframe.jsp?sYear=<%=year%>&sMonth=<%=month%>&sUserID=<%=sUserID%>&packages=columnchart" width="800px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
	</div>
    
	<script type="text/javascript">
	
	/***********************************************/
	/* 검색 조건 컨트롤 초기화
	/***********************************************/
		//<div class="dash_box_content">
		//	<div id="<%=id%><%=method %>Chart" ></div>
		//</div>
		/* 리스트 표시 */
		//window.addEvent("domready",function () {
		//	createSwfobject(
		//			  'swf/open-flash-chart.swf', '<%=id%><%=method %>Chart',
		//			  '100%', '400px','9.0.0', 'swf/expressInstall.swf',
		//			  {'data-file': escape('massmail/statistic/massmail.do?method=massmailStatisticUsersBar&year=<%=year%>&month=<%=month%>&sUserID=<%=sUserID%>')});
		//});
	
	</script>
<%
}

//****************************************************************************************************/
// Pie 차트 조회
//****************************************************************************************************/
if(method.equals("massmailStatisticUsersPie")) {
	String sUserID = request.getParameter("sUserID");
%>
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
			<h2>Pie Chart </h2>
		    <IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/user_chart_iframe.jsp?sYear=<%=year%>&sMonth=<%=month%>&sUserID=<%=sUserID%>&packages=piechart" width="800px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>      	
	</div>
	<script type="text/javascript">
	//<div class="dash_box_content">
	//<div id="<%=id%><%=method %>Chart"></div>
	//</div>	
		/* 리스트 표시 */
		//window.addEvent("domready",function () {	
		//	createSwfobject(
		//			  'swf/open-flash-chart.swf', '<%=id%><%=method %>Chart',
		//			  '100%', '400px','9.0.0', 'swf/expressInstall.swf',
		//			  {'data-file': escape('massmail/statistic/massmail.do?method=massmailStatisticUsersPie&year=<%=year%>&month=<%=month%>&sUserID=<%=sUserID%>')});
	
		//});

	</script>
<%
}
%>


