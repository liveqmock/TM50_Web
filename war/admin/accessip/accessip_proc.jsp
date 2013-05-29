<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>  
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.admin.accessip.model.*"%>
<%@ page import="java.util.*"%>  

<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String dbID = request.getParameter("dbID");

String isAdmin = LoginInfo.getIsAdmin(request);

if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력

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
	
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	
	 
	<c:forEach items="${accessipList}" var="accessip">
	<TR class="tbl_tr" accessip_id="<c:out value="${accessip.accessipID}"/>" >
		<TD class="tbl_td"><input type="checkbox" class="notBorder" id="eAccessipID" name="eAccessipID" value="<c:out value="${accessip.accessipID}" />" /></TD>
		<TD class="tbl_td" align="left">
			<a href="javascript:$('<%=id%>').editWindow(<c:out value="${accessip.accessipID}"/>)">
				<c:out value="${accessip.octetA}"/>.<c:out value="${accessip.octetB}"/>.
				<c:out value="${accessip.octetC}"/><c:if test="${accessip.octetC == ''}"> *</c:if>.
				<c:out value="${accessip.octetD}"/><c:if test="${accessip.octetD == ''}"> *</c:if>
			</a>
		</TD>
		<TD class="tbl_td"><c:out value="${accessip.description}"/></TD>
		<TD class="tbl_td"><c:out value="${accessip.userName}"/></TD>
		<TD class="tbl_td"><c:out value="${accessip.registDate}"/></TD>
		<TD class="tbl_td"><c:out value="${accessip.useYN}"/></TD>
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
//  편집 
//****************************************************************************************************/
if(method.equals("edit")) {
%>
	<div style="margin-bottom:10px;width:100%">
		<div>
			<img src="images/tag_blue.png" alt="Tips "> 첫번째, 두번째 Octet은 필수 입력 값입니다.<br/>
			<img src="images/tag_blue.png" alt="Tips "> Octet에 입력 값이 없을 경우 0~255까지 모두 접근이 가능합니다.(세번째, 네번째만 설정 가능)<br/>
			 &nbsp; &nbsp; &nbsp;&nbsp; 예) 211.155.4.공백 : IP 211.155.4.0~255 접근가능  <br/>
			<img src="images/tag_blue.png" alt="Tips "> Octet은 32비트 IP 주소의 네 부분을 이루는 8비트의 숫자입니다.<br/>
			<img src="images/octet.jpg" width="350px">
		
		</div>
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<input type="hidden" id="eAccessipID" name="eAccessipID" value="<c:out value="${accessIP.accessipID}"/>" />
			<input type="hidden" id="eUserID" name="eUserID" value="<%=LoginInfo.getUserID(request)%>">
			<table class="ctbl" width="100%">
			<tbody>
			<tr>	
				<td class="ctbl ttd1" width="80px">IP</td>
				<td class="ctbl td">
				<input type="text" id="eOctetA" name="eOctetA" size="10" value="<c:out value="${accessIP.octetA}"/>"/>.
				<input type="text" id="eOctetB" name="eOctetB" size="10" value="<c:out value="${accessIP.octetB}"/>"/>.
				<input type="text" id="eOctetC" name="eOctetC" size="10" value="<c:out value="${accessIP.octetC}"/>"/>.
				<input type="text" id="eOctetD" name="eOctetD" size="10" value="<c:out value="${accessIP.octetD}"/>"/></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="80px">설명 </td>
				<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${accessIP.description}"/>" size="50" mustInput="Y" msg="그룹명 입력"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1" width="80px">사용여부</td>
				<td class="ctbl td">
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${accessIP.useYN=='Y'}">select='Y'</c:if>>Y</li>
						<li data="N" <c:if test="${accessIP.useYN=='N'}">select='Y'</c:if>>N</li>
					</ul>
				</td>
			</tr>
			</tbody>
			</table>
		</form>
	</div>
	
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	<c:if test="${accessIP.accessipID > 1  }" >
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${accessIP.accessipID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${accessIP.accessipID}"/>)" class="web20button bigblue">저 장</a></div>

	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
	</script>
<%
}
%>

<%}else{%>
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td align="center" valign="middle">
			<center><img src="../../images/error.jpg" /></center>
		</td>
	</tr>
</table>
<%}%>