<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="web.common.util.*"%>
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
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	
	
	<c:forEach items="${rejectsmsList}" var="rejectsms">
	<TR class="tbl_tr" rejectsms_id="<c:out value="${rejectsms.rejectID}"/>" >
		<TD class="tbl_td" align='center'><input type="checkbox" class="notBorder"  id="eRejectID" name="eRejectID" value="<c:out value="${rejectsms.rejectID}" />" /></TD>		
		<TD class="tbl_td" align="left"><b><c:out value="${rejectsms.receiverPhone}" escapeXml="true"/></b></TD>
		<TD class="tbl_td" align='center'><c:out value="${rejectsms.userName}"/></TD>				
		<TD class="tbl_td" align="left"><c:out value="${rejectsms.registDate}"/></TD>
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

	
	
	<%if(!message.equals("")) { %>
	
		alert("<%=message%>");
	
	<%}%>


	</script>
	
<%
}
//****************************************************************************************************/
//편집
//****************************************************************************************************/
if(method.equals("write")) {
%>
	<jsp:useBean id="userID" class="java.lang.String" scope="request" />
	<jsp:useBean id="userName" class="java.lang.String" scope="request" />
	<jsp:useBean id="groupID" class="java.lang.String" scope="request" />
	<jsp:useBean id="groupName" class="java.lang.String" scope="request" />
	
	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<input type="hidden" id="eFileName" name="eFileName">
			<input type="hidden" id="eNewFile" name="eNewFile">		
			<input type="hidden" id="eUserID" name="eUserID" value="<%=userID %>">
			<input type="hidden" id="eGroupID" name="eGroupID" value="<%=groupID %>">
	
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">입력방법</td>
				<td class="ctbl td">
				<input type="radio" id="eInputType" name="eInputType" checked value="1" onclick="javascript:$('<%=id%>').chgInputType('1')">직접입력&nbsp;&nbsp;
				<input type="radio" id="eInputType" name="eInputType" value="2" onclick="javascript:$('<%=id%>').chgInputType('2')">파일업로드(CSV, TXT, xls)
				</td>
			</tr>
			</tbody>
			</table>
			<div id="<%=id%>_sms">
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">설명 </td>
				<td class="ctbl td" colspan="3">
					1.폰번호를 각각 한줄씩 입력 합니다. (최대 1000라인)<br>
					2.폰번호만 추가할 수 있습니다. 
					
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>				
				<td class="ctbl ttd1">폰번호</td>
				<td class="ctbl td"><textarea id="eReceiverPhone" name="eReceiverPhone" style="height:100px;width:100%" ></textarea></td>
			</tr>	
			</tbody>
			</table>						
			</div>
			<div id="<%=id%>_file" style="display:none">
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">설명 </td>
				<td class="ctbl td" colspan="3">첨부파일은 csv,txt,xls 파일만 가능하고  파일안에는   폰번호만 각각 한줄씩 입력되어 있어야 합니다.</td>
			</tr>			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">첨부파일 </td>
				<td class="ctbl td" colspan="3">
				<div  id="<%=id%>uploadWrapper" ></div>			
				</td>
			</tr>	
			</tbody>
			</table>
			</div>		
		</form>
	</div>
	

	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').uploadFile()" class="web20button bigblue">저 장</a></div>

	<script language="javascript">
		$('<%=id%>_file').setStyle('display','none');
		makeSelectBox.render($('<%=id%>_form'));
		$('<%=id%>').renderUpload();

		

			
	</script>

<%} %>


		