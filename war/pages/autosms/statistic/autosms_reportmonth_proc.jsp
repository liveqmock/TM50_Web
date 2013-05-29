<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");


//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	<jsp:useBean id="dateList" class="java.lang.String" scope="request" />

	<c:forEach items="${autoSMSReportMonthList}" var="autoSMSReportMonth">
	<TR class="tbl_tr" automailevent_id="<c:out value="${autoSMSReportMonth.autosmsID}"/>">
		<TD class="tbl_td"><c:out value="${autoSMSReportMonth.autosmsID}"/></TD>					
		<TD class="tbl_td" align="left"><c:out value="${autoSMSReportMonth.autosmsTitle}"/></TD>		
		<TD class="tbl_td"><c:out value="${dateList}"/></TD>
		<TD class="tbl_td"><c:out value="${autoSMSReportMonth.sendTotal}" /></TD>	
		<TD class="tbl_td"><c:out value="${autoSMSReportMonth.successTotal}"/></TD>		
		<TD class="tbl_td"><c:out value="${autoSMSReportMonth.failTotal}"/></TD>
	</TR>
	</c:forEach>
	
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->	
	
	<script type="text/javascript">

		window.addEvent('domready', function(){


			// 페이징 표시
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				,url: 'pages/common/pagingStr.jsp'
				,update: $('<%=id%>_paging') // 완료후 content가 랜더링될 element
			});
			nemoRequest.get({
				'id':'<%=id%>', 
				'curPage': '<%=curPage%>', 
				'iLineCnt': '<%=iLineCnt%>', 
				'iTotalCnt': '<%=iTotalCnt%>', 
				'iPageCnt': '10' 
			});
			
		
			// 테이블 렌더링	
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				//,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			var frmList = $('<%=id%>_list_form');
			frmList.iTotalCnt.value = '<%=iTotalCnt%>';
		});

	</script>
<%
}

//****************************************************************************************************/
//총 발송 현황
//****************************************************************************************************/

if(method.equals("countall")){
%>
	<tr>
		<td class="ctbl ttd1" width="100px">총 SMS발송량</td>
		<td class="ctbl td" align="center"><c:out value="${smsStatistic.sendTotal}"/>개</td>
		<td class="ctbl ttd1" width="100px">성공통수</td>
		<td class="ctbl td" align="center"> <c:out value="${smsStatistic.successTotal}"/>개</td>
		<td class="ctbl ttd1" width="100px">실패통수</td>
		<td class="ctbl td" align="center"> <c:out value="${smsStatistic.failTotal}"/>개</td>		
	</tr>
	<tr><td colspan="8" class="ctbl line"></td></tr>
<%
}
%>