<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="web.common.util.*" %> 
<%@page import="web.common.util.LoginInfo"%>
<%

String id = request.getParameter("id");
String sMethod = request.getParameter("method");

//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(sMethod.equals("list")) {
	
%>

	
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<c:forEach items="${massmailWriteList}" var="massmailList">
	<TR class="tbl_tr" tr_massmailID="<c:out value="${massmailList.massmailID}"/>" tr_scheduleID="<c:out value="${massmailList.scheduleID}"/>" tr_state="<c:out value="${massmailList.state}"/>" tr_sendType="<c:out value="${massmailList.sendType}"/>">
		<TD class="tbl_td" align="left">
   			<c:if test="${massmailList.sendType == '1'}" >
   				<img src="images/s_mail.gif" title="즉시발송"/>
   			</c:if>
   			<c:if test="${massmailList.sendType == '2'}" >
   				<img src="images/s_mail_reserve.gif" title="예약발송"/>
   			</c:if>
   			<c:if test="${massmailList.sendType == '3'}" >
   				<img src="images/s_reload_2.gif" title="반복발송"/>
   			</c:if> &nbsp;
   			<a href="javascript:$('<%=id%>').viewWindow('<c:out value="${massmailList.massmailID}"/>', '<c:out value="${massmailList.scheduleID}"/>')"
   			title="<div style='text-align:left'><strong>작성자 : </strong> <c:out value="${massmailList.userName}"/><br><strong> 발송스케줄  : </strong> <c:out value="${massmailList.sendScheduleDate}"/> </div>">
   			<c:out value="${fn:substring(massmailList.massmailTitle,0,14)}" escapeXml="true"/></a>
			<c:if test="${fn:length(massmailList.massmailTitle) > 14}" >
				..
   			</c:if>
			<c:if test="${massmailList.state == '00'}" >
				<img src="images/massmail/save.gif" title="임시저장중"/>
			</c:if>	
			<c:if test="${massmailList.state == '10'}" >
				<img src="images/massmail/approve.gif" title="승인대기중"/>
			</c:if>		
			<c:if test="${massmailList.state == '11'}" >
				<img src="images/massmail/ready.gif" title="발송준비대기중"/>
			</c:if>				
			<c:if test="${massmailList.state == '12'}" >
				<img src="images/massmail/ready.gif" title="발송준비중"/>
			</c:if>	
			<c:if test="${massmailList.state == '13'}" >
				<img src="images/massmail/ready.gif" title="발송준비완료"/>
			</c:if>	
			<c:if test="${massmailList.state == '14'}" >
				<img src="images/massmail/sending.gif" title="발송중"/>
			</c:if>		
			<c:if test="${massmailList.state == '15'}" >
				<img src="images/massmail/finish.gif" title="발송완료"/>
			</c:if>		
			<c:if test="${massmailList.state == '16'}" >
				<img src="images/massmail/sending.gif" title="오류자재발송중"/>
			</c:if>					
			<c:if test="${massmailList.state == '22'}" >
				<img src="images/massmail/error.gif" title="발송준비중에러"/>
			</c:if>		
			<c:if test="${massmailList.state == '24'}" >
				<img src="images/massmail/error.gif" title="발송중에러"/>
			</c:if>	
			<c:if test="${massmailList.state == '26'}" >
				<img src="images/massmail/error.gif" title="오류자재발송중에러"/>
			</c:if>	
			<c:if test="${massmailList.state == '32'}" >
				<img src="images/massmail/stop.gif" title="발송준비중정지"/>
			</c:if>		
			<c:if test="${massmailList.state == '34'}" >
				<img src="images/massmail/pause.gif" title="발송일시정지 "/>
			</c:if>	
			<c:if test="${massmailList.state == '44'}" >
				<img src="images/massmail/stop.gif" title="발송정지 "/>
			</c:if>							
		</TD>	
	</TR>
	</c:forEach>


	<!-- 메시지 가있으면 메시지를 뿌려준다 -->
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
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
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
<%
}
%>
