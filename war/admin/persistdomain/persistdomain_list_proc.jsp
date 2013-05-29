<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<%

String id = request.getParameter("id");
String method = request.getParameter("method");

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
	
	
	
	<c:forEach items="${persistDomainList}" var="persistDomain">
	<TR class="tbl_tr" domainID="<c:out value="${persistDomain.domainID}"/>" >
		<TD class="tbl_td" align='center'><input type="checkbox" class="notBorder" id="eDomainID" name="eDomainID" value="<c:out value="${persistDomain.domainID}" />" /></TD>		
		<TD class="tbl_td"><b><a href="javascript:$('<%=id%>').editWindow(<c:out value="${persistDomain.domainID}"/>)"><c:out value="${persistDomain.domain_name}" escapeXml="true"/></b></a></TD>	
		<TD class="tbl_td"><c:out value="${persistDomain.description}" /></TD>	
		<TD class="tbl_td"><c:out value="${persistDomain.registDate}"/></TD>	
		<TD class="tbl_td"><c:out value="${persistDomain.useYN}"/></TD>	
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
			closeWindow( $('<%=id%>_modal') );
			
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

			<input type="hidden" id="eDomainID" name="eDomainID" value="<c:out value="${persistDomain.domainID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="80px">도메인</td>
				<td class="ctbl td"><input type="text" id="eDomainName" name="eDomainName" value="<c:out value="${persistDomain.domain_name}"/>" size="50" mustInput="Y" msg="이름 입력"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>				
				<td class="ctbl ttd1" width="100px">설명</td>
				<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${persistDomain.description}"/>" size="50"/></td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			
			<tr>
				<td class="ctbl ttd1">사용여부</td>
				<td class="ctbl td" >
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${persistDomain.useYN=='Y'}">select='Y'</c:if>>사용</li>
						<li data="N" <c:if test="${persistDomain.useYN=='N'}">select='Y'</c:if>>사용안함</li>
						
						
					</ul>
				</td>
			</tr>		
			
			</tbody>
			</table>
		</form>
	</div>
	

	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<c:if test="${persistDomain.domainID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${persistDomain.domainID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${persistDomain.domainID}"/>)" class="web20button bigblue">저 장</a></div>

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