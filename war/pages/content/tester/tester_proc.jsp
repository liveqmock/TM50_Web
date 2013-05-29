<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@page import="web.common.util.LoginInfo"%>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");


//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
%>

	
<%@page import="web.common.util.ImageExtractor"%>
	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
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
	
	
	
	<c:forEach items="${testerList}" var="tester">
	<TR class="tbl_tr" tester_id="<c:out value="${tester.testerID}"/>"  sharegroup_id="<c:out value="${tester.testerID}"/>">
		<TD class="tbl_td" align='center'><input type="checkbox" class="notBorder" id="eTesterID" name="eTesterID" value="<c:out value="${tester.testerID}" />" /></TD>		
		<TD class="tbl_td"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${tester.testerID}"/>')"><c:out value="${tester.testerName}" escapeXml="true"/></b></a></TD>		
		<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${tester.testerID}"/>')"><c:out value="${fn:substring(tester.testerEmail,0,40)}" escapeXml="true"/>
		<c:if test="${fn:length(tester.testerEmail) > 40}" >
					..
   				</c:if>
		</b></a></TD>	
		
		
		
		<%if(LoginInfo.getIsAdmin(request).equals("Y") ){%>
			<TD class="tbl_td" align="left"><c:out value="${tester.testerHp}"/></TD>	
			<TD class="tbl_td" ><c:out value="${tester.userName}"/></TD>	
			
		<%}else{ %>
			<TD class="tbl_td" align="left"><c:out value="${tester.testerHp}"/></TD>	
		
		<%} %>
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
		$('<%=id%>_totalCount').innerHTML = '<%=iTotalCnt%>';
	});
	


	</script>
<%
}
//****************************************************************************************************/
//  편집 
//****************************************************************************************************/
if(method.equals("edit")) {
%>	
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
		<input type="hidden" id="eTesterID" name="eTesterID" value="<c:out value="${tester.testerID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" colspan=2>
					<div>
						<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><strong>테스트 리스트를 입력합니다(최대 20명, 이메일 구분 ';' 예:test@test.com;test2@test.com )</strong></div>
					</div>
				</td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">이름</td>
				<td class="ctbl td"><input type="text" id="eTesterName" name="eTesterName" value="<c:out value="${tester.testerName}"/>" size="40" mustInput="Y" msg="그룹명"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>				
				<td class="ctbl ttd1" width="100px">이메일</td>
				<td class="ctbl td">
				<textarea id="eTesterEmail" name="eTesterEmail" style="width:500px;height:100px"  mustInput="Y" msg="이메일"><c:out value="${tester.testerEmail}"/></textarea>
				
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>				
				<td class="ctbl ttd1" width="100px">휴대폰</td>
				<td class="ctbl td"><input type="text" id="eTesterHp" name="eTesterHp" value="<c:out value="${tester.testerHp}"/>" size="40" /></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			</tbody>
			<tbody id="<%=id%>_check_content" >
			</tbody>
			</table>
		</form>
	</div>
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<c:if test="${tester.testerID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${tester.testerID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData(<c:out value="${tester.testerID}"/>,'Y')" class="web20button bigblue">저 장 후 추가</a></div>
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData(<c:out value="${tester.testerID}"/>,'N')" class="web20button bigblue">저 장 후 닫기</a></div>
	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
	</script>

<%} %>

		