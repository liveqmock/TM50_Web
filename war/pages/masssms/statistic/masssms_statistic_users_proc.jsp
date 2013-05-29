<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String year = request.getParameter("sSendedYear");
String month = request.getParameter("sSendedMonth");



//계정별 통계 
if(method.equals("masssmsStatisticUsersList")){
%>
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<c:set var="sendTotalHap" value="0"/>
	<c:set var="successTotalHap" value="0"/>
	<c:set var="failTotalHap" value="0"/>
	<c:set var="readyTotalHap" value="0"/>
	
	<c:forEach items="${masssmsStatUsersList}" var="massSMSStatisticUsers">
		<TR class="tbl_tr" >
			<TD class="tbl_td" ><b><c:out value="${massSMSStatisticUsers.userName}"/></b></TD>		
			<TD class="tbl_td"><fmt:formatNumber value="${massSMSStatisticUsers.sendTotal}" type="number"/></TD>	
			<TD class="tbl_td"><fmt:formatNumber value="${massSMSStatisticUsers.successTotal}" type="number"/></TD>		
			<TD class="tbl_td"><fmt:formatNumber value="${massSMSStatisticUsers.failTotal}" type="number"/></TD>	
			<TD class="tbl_td"><fmt:formatNumber value="${massSMSStatisticUsers.readyTotal}" type="number"/></TD>
		</TR>
		<c:set var="sendTotalHap" value="${sendTotalHap + massSMSStatisticUsers.sendTotal}"/>
		<c:set var="successTotalHap" value="${successTotalHap + massSMSStatisticUsers.successTotal}"/>
		<c:set var="failTotalHap" value="${failTotalHap + massSMSStatisticUsers.failTotal}"/>
		<c:set var="readyTotalHap" value="${readyTotalHap + massSMSStatisticUsers.readyTotal}"/>
	</c:forEach>
	<c:if test="${!empty masssmsStatUsersList}">
		<TR >
			<TD class="tbl_td" >합 계</TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${sendTotalHap}" type="number"/></TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${successTotalHap}" type="number"/></TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${failTotalHap}" type="number"/></TD>
			<TD class="tbl_td" ><fmt:formatNumber value="${readyTotalHap}" type="number"/></TD>
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
if(method.equals("masssmsStatisticUsersBar")) {
	String sUserID = request.getParameter("sUserID");
%>
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<h2>Bar Chart</h2>
		  	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/masssms_user_chart_iframe.jsp?sYear=<%=year%>&sMonth=<%=month%>&sUserID=<%=sUserID%>&packages=columnchart" width="800px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
	</div>

<%
}

//****************************************************************************************************/
// Pie 차트 조회
//****************************************************************************************************/
if(method.equals("masssmsStatisticUsersPie")) {
	String sUserID = request.getParameter("sUserID");
%>
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
			<h2>Pie Chart </h2>
		    <IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/masssms_user_chart_iframe.jsp?sYear=<%=year%>&sMonth=<%=month%>&sUserID=<%=sUserID%>&packages=piechart" width="800px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>      	
	</div>

<%
}
%>


