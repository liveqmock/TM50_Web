<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@page import="web.common.util.*"%>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String driverID = request.getParameter("driverID");

String isAdmin = LoginInfo.getIsAdmin(request);

if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력

if(method.equals("list")) {
%>
	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<!-- 메시지 가있으면 메시지를 뿌려준다 -->
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<c:forEach items="${dblist}" var="dblist">
		<TR class="tbl_tr" db_id="<c:out value="${dblist.dbID}"/>"  driver_id="<c:out value="${dblist.driverID}"/>">
			<TD class="tbl_td" align='center'><input type="checkbox" class="notBorder" id="eDbID" name="eDbID" value="<c:out value="${dblist.dbID}" />" /></TD>
 			<TD class="tbl_td"><b><c:out value="${dblist.dbID}"/></b></TD>			
			<TD class="tbl_td" align="left"><a href="javascript:$('<%=id%>').editWindow('<c:out value="${dblist.dbID}"/>','<c:out value="${dblist.driverID}"/>')"><c:out value="${dblist.dbName}"/></a></TD>	
			<TD class="tbl_td"><c:out value="${dblist.jdbcDriverName}"/></TD>
			<TD class="tbl_td"><c:out value="${dblist.useYN}"/></TD>	
			<TD class="tbl_td"><c:out value="${dblist.encodingYN}"/></TD>		
			<TD class="tbl_td"><c:out value="${dblist.defaultYN}"/></TD>	
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
<%@page import="java.util.List"%>
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<input type="hidden" id="eDbID" name="eDbID" value="<c:out value="${dbset.dbID}"/>" />			
			<input type="hidden" id="eDriverClass" name="eDriverClass" value="<c:out value="${dbset.jdbcDriverClass}"/>" >
			<c:if test="${dbset.dbID != 0 && dbset.dbAccessKey != ''}">
			<table id="<%=id %>_confirmTable" class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="90px">DB접근키</td>
				<td class="ctbl td" colspan="5">
					<input type="password" id="eDbAccessKeyConfirmer" name="eDbAccessKeyConfirmer" style="margin-top:2px" size="25" mustInput="Y" msg="DB접근키 입력"/>
					<div id="<%=id %>_confirmBtn" style="float:right;padding-right:203px;" ><a href="javascript:$('<%=id%>').dbAccessKeyConfirm()" class="web20button bigblue">확 인</a></div>
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			</tbody>
			</table>
			<table id="<%=id %>_editTable" style="Display:none" class="ctbl" width="100%">
			</c:if>
			<c:if test="${dbset.dbID == 0 || dbset.dbAccessKey == ''}">
			<table class="ctbl" width="100%">
			</c:if>
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="90px">JDBC설정</td>
				<td class="ctbl td" colspan="5">
					<jsp:include page="../../include_inc/dbjdbcset_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="Y" />
						<jsp:param name="driverID" value="<%=driverID %>" />
					</jsp:include>	
				</td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tbody>
			<c:if test="${dbset.dbID != 0}" >
			<tr>
				<td class="ctbl ttd1" width="90px">DBID </td>
				<td class="ctbl td" colspan="5"><c:out value="${dbset.dbID}"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			</c:if>		
			<tr>	
				<td class="ctbl ttd1">DB명</td>
				<td class="ctbl td" colspan="5"><input type="text" id="eDbName" name="eDbName" value="<c:out value="${dbset.dbName}"/>" size="64" mustInput="Y" msg="DB명 입력"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>	
				<td class="ctbl ttd1">설명</td>
				<td class="ctbl td" colspan="5"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${dbset.description}"/>" size="64"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>
				<td class="ctbl ttd1">DBURL</td>
				<td class="ctbl td" colspan="5"><input type="text" id="eDbURL" name="eDbURL" value="<c:out value="${dbset.dbURL}"/>" size="64" mustInput="Y" msg="DB URL 입력"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">접속계정</td>
				<td class="ctbl td" colspan="5">
					<input type="text" id="eDbUserID" name="eDbUserID" value="<c:out value="${dbset.dbUserID}"/>" size="64" mustInput="Y" msg="접속계정 입력"/><br>
					<font color="blud">* DB 접속계정은 Select 권한만 있는 계정으로 입력 해 주세요.</font>
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">접속패스워드</td>
				<td class="ctbl td" colspan="5"><input type="password" id="eDbUserPWD" name="eDbUserPWD" value="<c:out value="${dbset.dbUserPWD}"/>" size="64" mustInput="Y" msg="접속패스워드 입력"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">인코딩여부</td>
				<td class="ctbl td">
					<ul id="eEncodingYN"  class="selectBox">
						<li data="N" <c:if test="${dbset.encodingYN=='N'}">select='Y'</c:if>>N</li>
						<li data="Y" <c:if test="${dbset.encodingYN=='Y'}">select='Y'</c:if>>Y</li>
					</ul>
				</td>
				<td class="ctbl ttd1">디폴트지정여부</td>
				<td class="ctbl td">
					<ul id="eDefaultYN"  class="selectBox">
						<li data="N" <c:if test="${dbset.defaultYN=='N'}">select='Y'</c:if>>N</li>
						<li data="Y" <c:if test="${dbset.defaultYN=='Y'}">select='Y'</c:if>>Y</li>
					</ul>
				</td>
				<td class="ctbl ttd1">사용여부</td>
				<td class="ctbl td">
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${dbset.useYN=='Y'}">select='Y'</c:if>>Y</li>
						<li data="N" <c:if test="${dbset.useYN=='N'}">select='Y'</c:if>>N</li>
					</ul>
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">DB접근키</td>
				<td class="ctbl td" colspan="5">
					<div style="margin:5px 0"><strong>DB접근키</strong> <input type="password" id="eDbAccessKey" name="eDbAccessKey" size="25" value="" style="margin-left:29px;ime-mode:disabled;" <c:if test="${dbset.dbID == 0 || dbset.dbAccessKey == ''}">mustInput="Y"</c:if> msg="DB접근키 입력"/></div>
					<div style="margin:3px 0"><strong>DB접근키 확인</strong> <input type="password"  id="eDbAccessKeyConfirm" name="eDbAccessKeyConfirm" size="25" style="margin-right:10px;ime-mode:disabled;" <c:if test="${dbset.dbID == 0 || dbset.dbAccessKey == ''}">mustInput="Y"</c:if> msg="DB접근키 확인 입력"/>
						<a title="<div style='text-align:left'>												
						<img src='images/help.gif'>&nbsp;<strong>DB접근키란?</strong><br>
						* DB설정의 수정, DB데이터 접근 시 인증에 사용되는 비밀번호 입니다.<br>
						* 영문자와 숫자, 특수문자가 혼합 된 6~20자리 접근키를 입력하세요.<br>
						<div style='color:red'>* DB접근키 분실 시 담당자에게 문의하여 주시기 바랍니다.</div>
						</div>">								
						<img src='images/help.gif'>
						</a>
					</div>
				</td>
			</tr>
			</tbody>
			</table>
		</form>
	</div>
	

	<div id="<%=id %>_allBtn" <c:if test="${dbset.dbID != 0 && dbset.dbAccessKey != ''}">style="visibility:hidden"</c:if>>
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<c:if test="${dbset.dbID != 0}" >
		<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${dbset.dbID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${dbset.dbID}"/>)" class="web20button bigblue">저 장</a></div>
	</div>
	

	<script language="javascript">
		
		makeSelectBox.render($('<%=id%>_form'));

		
		function setDriver(driverID,driverClass,dburl){
			document.<%=id%>_form.eDriverID.value = driverID;
			document.<%=id%>_form.eDriverClass.value = driverClass;
			document.<%=id%>_form.eDbURL.value = dburl;
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