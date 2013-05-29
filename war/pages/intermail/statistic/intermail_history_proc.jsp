<%@page import="com.oreilly.servlet.ServletUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%@ page import="web.intermail.control.InterMailControlHelper" %>
<%@ page import="web.intermail.service.InterMailService" %>
<%@ page import="web.intermail.model.*" %>
<%@ page import="java.util.*" %>  
<%

String id = request.getParameter("id");
String method = request.getParameter("method");


//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("listSendHistory")) {
	
%>

	<jsp:useBean id="curPage1" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt1" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt1" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
			<c:forEach items="${interMailSendResultList}" var="interMailSendResult">
			<TR class="tbl_tr" tr_intermail_id="${interMailSendResult.intermailID}" tr_schedule_id="${interMailSendResult.scheduleID}" tr_send_id="${interMailSendResult.sendID}">			
				<TD class="tbl_td">${interMailSendResult.email}</TD>
				<TD class="tbl_td">${interMailSendResult.intermailTitle}</TD>
				<TD class="tbl_td">${interMailSendResult.registDate}</TD>
				<TD class="tbl_td">
				<c:if test="${interMailSendResult.smtpCodeType == '0'}">
					성공
				</c:if>
				<c:if test="${interMailSendResult.smtpCodeType != '0'}">
					실패
				</c:if>
				</TD>				
				<TD class="tbl_td">${interMailSendResult.retrySendCount}</TD>
				<TD class="tbl_td">${interMailSendResult.retryLastDate}</TD>
				<TD class="tbl_td">${interMailSendResult.openDate}</TD>
				<TD class="tbl_td">${interMailSendResult.openFileDate}</TD>		
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
				'curPage': '<%=curPage1%>', 
				'iLineCnt': '<%=iLineCnt1%>', 
				'iTotalCnt': '<%=iTotalCnt1%>', 
				'iPageCnt': '10' 
			});
			
		
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			
		});		

	</script>
<%}%>