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
if(method.equals("listInterMailSend")) {
%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
		
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	
	<input type="hidden" id="eTotalCnt" name="eTotalCnt" value="<%=iTotalCnt %>">
	<c:forEach items="${intermailSendList}" var="interMailSend">	
	<TR id="eIntermail${interMailSend.intermailID}_${interMailSend.scheduleID}" class="tbl_tr" tr_intermailID="${interMailSend.intermailID}" tr_scheduleID="${interMailSend.scheduleID}">
		<TD class="tbl_td"><input type="checkbox" class="notBorder" id="eSendID" name="eSendID" value="${interMailSend.sendID}"></TD>
		<TD class="tbl_td">${interMailSend.email}</TD>	
		<TD class="tbl_td"><a href="javascript:$('<%=id%>').showContent('mailContent','${interMailSend.intermailID}','${interMailSend.scheduleID}','${interMailSend.sendID}')">${interMailSend.mailTitle}</a></TD>	
		<TD class="tbl_td">
		<c:if test="${interMailSend.fileName!=''}" >
		<a href="javascript:$('<%=id%>').showContent('fileContent','${interMailSend.intermailID}','${interMailSend.scheduleID}','${interMailSend.sendID}')">${interMailSend.fileName}</a>
		</c:if>		
		</TD>		
		<TD class="tbl_td" align="center">${interMailSend.registDate}</TD>
	</TR>
	</c:forEach>

	
	
	
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
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			
		});

	</script>
<%
}


//****************************************************************************************************/
//메일 본문 상세보기 
//****************************************************************************************************/
if(method.equals("mailContent")) {	
%>

	<div style="margin-bottom:10px;width:98%">
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">받는 이메일</td>
				<td class="ctbl td">${interMailSend.email}</td>
				<td class="ctbl ttd1" width="100px">받는 사람명</td>
				<td class="ctbl td">${interMailSend.receiverName}</td>
				
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">보내는 메일</td>
				<td class="ctbl td">${interMailSend.senderMail}</td>
				<td class="ctbl ttd1" width="100px">보내는 사람명 </td>
				<td class="ctbl td">${interMailSend.senderName}</td>
			</tr>					
			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">메일제목</td>
				<td class="ctbl td" colspan="3">${interMailSend.mailTitle}</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>								
			<tr>				
				<td class="ctbl ttd1" width="100px">HTML</td>
				<td class="ctbl td" colspan="3">
				<iframe id="<%=id %>_apprve_frame" src="/pages/intermail/write/intermail_preview.jsp?id=<%=id %>&intermailID=${interMailSend.intermailID}&scheduleID=${interMailSend.scheduleID}&sendID=${interMailSend.sendID}&flag=mailContent" frameborder="1" width="100%" height="600" marginwidth="0" marginheight="0" scrolling="auto" style="border:1 solid navy"></iframe>
				</td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			</tbody>
			</table>		
	</div>

<%
}

//****************************************************************************************************/
//메일 본문 상세보기 
//****************************************************************************************************/
if(method.equals("fileContent")) {
%>
	<div style="margin-bottom:10px;width:98%">
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">받는 이메일</td>
				<td class="ctbl td">${interMailSend.email}</td>
				<td class="ctbl ttd1" width="100px">받는 사람명</td>
				<td class="ctbl td">${interMailSend.receiverName}</td>
				
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">보내는 메일</td>
				<td class="ctbl td">${interMailSend.senderMail}</td>
				<td class="ctbl ttd1" width="100px">보내는 사람명 </td>
				<td class="ctbl td">${interMailSend.senderName}</td>
			</tr>					
			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">첨부파일명</td>
				<td class="ctbl td" colspan="3">${interMailSend.fileName}</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>								
			<tr>				
				<td class="ctbl ttd1" width="100px">첨부파일내용</td>
				<td class="ctbl td" colspan="3">
				<iframe id="<%=id %>_apprve_frame" src="/pages/intermail/write/intermail_preview.jsp?id=<%=id %>&intermailID=${interMailSend.intermailID}&scheduleID=${interMailSend.scheduleID}&sendID=${interMailSend.sendID}&flag=fileContent" frameborder="1" width="100%" height="600" marginwidth="0" marginheight="0" scrolling="auto" style="border:1 solid navy"></iframe>
				</td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			</tbody>
			</table>		
	</div>

<%}%>