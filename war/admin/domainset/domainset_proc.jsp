<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.CommonAccessContolHelper"%> 
<%@ page import="web.common.service.CommonAccessService"%> 
<%@page import="web.common.util.*"%>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String domainFlag =  request.getParameter("domainFlag");

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
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<c:forEach items="${domainList}" var="domain">
	<TR class="tbl_tr" domainset_id="<c:out value="${domain.domainID}"/>" >	
		<c:if test="${domain.domainType == 1 || domain.domainType == 3}"> 
		<TD class="tbl_td" align='center'><input type="checkbox" class="notBorder" id="eDomainID_no" name="eDomainID_no" disabled value="<c:out value="${domain.domainID}"/>" ></TD>			
		</c:if>	
		<c:if test="${domain.domainType != 1 && domain.domainType != 3}"> 
		<TD class="tbl_td" align='center'><input type="checkbox" class="notBorder" id="eDomainID" name="eDomainID" value="<c:out value="${domain.domainID}"/>" ></TD>			
		</c:if>	
		<TD class="tbl_td"><b><a href="javascript:$('<%=id%>').editWindow(<c:out value="${domain.domainID}"/>)"><c:out value="${domain.domainName}" escapeXml="true"/></a></b></TD>	
		<TD class="tbl_td"><c:out value="${domain.domainTypeDesc}" /></TD>	
		<TD class="tbl_td"><c:out value="${domain.threadCount}"/>(개)</TD>	
		<TD class="tbl_td"><c:out value="${domain.socketTimeOut}"/>(초)</TD>	
		<TD class="tbl_td"><c:out value="${domain.socketPerSendCount}"/>(건)</TD>	
	</TR>
	</c:forEach>
	
		<script type="text/javascript">

		window.addEvent('domready', function(){
		
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				//,cursor: 'pointer' // 커서
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

			<input type="hidden" id="eDomainID" name="eDomainID" value="<c:out value="${domain.domainID}"/>" />
			<input type="hidden" id="eDomainFlag" name="eDomainFlag" value="<%=domainFlag %>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="150px">도메인</td>
				<td class="ctbl td"><input type="text" id="eDomainName" name="eDomainName" value="<c:out value="${domain.domainName}"/>" <c:if test="${domain.domainType == 1 || domain.domainType == 3}" > readOnly </c:if> size="20" mustInput="Y" msg="이름 입력"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>
				<td class="ctbl ttd1" width="150px">스레드개수</td>
				<td class="ctbl td" ><input type="text" id="eThreadCount" name="eThreadCount" style="ime-mode:disabled;" onkeypress="javascript:$('<%=id%>').fn_checkNumber();" value="<c:out value="${domain.threadCount}"/>" size="20" maxlength="3" mustInput="Y" /></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1" width="150px">소켓응답대기시간(초)</td>
				<td class="ctbl td" ><input type="text" id="eSocketTimeOut" name="eSocketTimeOut" style="ime-mode:disabled;"  onkeypress="javascript:$('<%=id%>').fn_checkNumber();" value="<c:out value="${domain.socketTimeOut}"/>" size="20" maxlength="3" mustInput="Y"  /></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>				
				<td class="ctbl ttd1" width="150px" >소켓당발송건수</td>
				<td class="ctbl td" ><input type="text" id="eSocketPerSendCount" name="eSocketPerSendCount" style="ime-mode:disabled;" onkeypress="javascript:$('<%=id%>').fn_checkNumber();" value="<c:out value="${domain.socketPerSendCount}"/>" <c:if test="${domain.domainType == 1 || domain.domainType == 3}" > readOnly </c:if> size="20" maxlength="3" mustInput="Y" /></td>
			</tr>

			
			</tbody>
			</table>
		</form>
	</div>
	

	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	<%if(!domainFlag.equals("2")){ %>
	<c:if test="${domain.domainID != 0 }" >
		<c:if test="${domain.domainType != 1 && domain.domainType != 3}" >
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${domain.domainID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	</c:if>
	
	
	
	<%} %>	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${domain.domainID}"/>)" class="web20button bigblue">저 장</a></div>
	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));

		$('<%=id%>').fn_checkNumber = function() {

			if( (event.keyCode < 48 ) || (event.keyCode > 57) ) {
			alert('숫자를 입력하세요')
			event.returnValue = false;

			}

		}
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