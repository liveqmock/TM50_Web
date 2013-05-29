<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@page import="web.common.util.LoginInfo"%>
<%@ page import="web.common.util.*" %> 
<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String shareGroupID = request.getParameter("shareGroupID");

//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
%>

	
<%@page import="web.common.util.ImageExtractor"%><jsp:useBean id="curPage" class="java.lang.String" scope="request" />
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
	
	
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<c:forEach items="${mailTemplateList}" var="mailTemplate">
	<TR class="tbl_tr" mailtemplate_id="<c:out value="${mailTemplate.templateID}"/>"  sharegroup_id="<c:out value="${mailTemplate.shareGroupID}"/>">
		<TD class="tbl_td" align='center'>
		<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
			<input type="checkbox" class="notBorder" id="eTemplateID" name="eTemplateID" value="<c:out value="${mailTemplate.templateID}" />" />
		<%}else { %>
			<c:if test="${mailTemplate.userID==who}">
				<input type="checkbox" class="notBorder" id="eTemplateID" name="eTemplateID" value="<c:out value="${mailTemplate.templateID}" />" />			
			</c:if>	
			<c:if test="${mailTemplate.userID!=who}">
				<input type="checkbox" class="notBorder" id="eTemplateID_no" name="eTemplateID_no" disabled value="<c:out value="${mailTemplate.templateID}" />" />		
			</c:if>		
					
		<%} %>
		</TD>		
		<TD class="tbl_td"><c:out value="${mailTemplate.templateID}"/></TD>		
		<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${mailTemplate.templateID}"/>','<c:out value="${mailTemplate.shareGroupID}"/>')"><c:out value="${mailTemplate.templateName}" escapeXml="true"/></b></a></TD>
		<TD class="tbl_td"><c:out value="${mailTemplate.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${mailTemplate.groupName}" /></TD>	
		<TD class="tbl_td"><c:out value="${mailTemplate.useYN}"/></TD>	
		<TD class="tbl_td">
			<c:if test="${mailTemplate.templateType == '1'}">대량메일</c:if>
			<c:if test="${mailTemplate.templateType == '2'}">자동메일</c:if>
			<c:if test="${mailTemplate.templateType == '3'}">연동메일</c:if>
			<c:if test="${mailTemplate.templateType == '4'}">공통</c:if>
		</TD>	
		<TD class="tbl_td"><c:out value="${mailTemplate.registDate}"/></TD>
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
				//,cursor: 'pointer' // 커서
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
	if(shareGroupID.equals("undefined")){
		shareGroupID = "NOT";
	}
%>	
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
		<input type="hidden" id="eTemplateContent" name="eTemplateContent">
		<input type="hidden" id="eShareGroupID" name="eShareGroupID">
			<input type="hidden" id="eTemplateID" name="eTemplateID" value="<c:out value="${mailTemplate.templateID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">템플릿명</td>
				<td class="ctbl td">
				<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
					<input type="text" id="eTemplateName" name="eTemplateName" value="<c:out value="${mailTemplate.templateName}"/>" size="50" mustInput="Y" msg="템플릿명을  입력"/>
				<%}else { %>
				<c:if test="${mailTemplate.userID==who || mailTemplate.userID==null }">
					<input type="text" id="eTemplateName" name="eTemplateName" value="<c:out value="${mailTemplate.templateName}"/>" size="50" mustInput="Y" msg="템플릿명을  입력"/>
				</c:if>	
				<c:if test="${mailTemplate.userID!=who}">
					<c:out value="${mailTemplate.templateName}"/>
				</c:if>	
				
				<%} %>
				</td>
				<td class="ctbl ttd1" width="100px">템플릿 구분</td>
				<td class="ctbl td">
					<ul id="eTemplateType"  class="selectBox" mustInput="Y" msg="템플릿 구분 선택">
						<li data="">--사용구분--</li>
						<li data="1" <c:if test="${mailTemplate.templateType == '1'}">select="yes"</c:if>>대량메일</li>
						<li data="2" <c:if test="${mailTemplate.templateType == '2'}">select="yes"</c:if>>자동메일</li>
						<li data="3" <c:if test="${mailTemplate.templateType == '3'}">select="yes"</c:if>>연동메일</li>
						<li data="4" <c:if test="${mailTemplate.templateType == '4'}">select="yes"</c:if>>공통</li>
					</ul>
				</td>
			</tr>
			<tr>
				<td colspan="12" class="ctbl line"></td>
			</tr>			
			<tr>				
				<td class="ctbl ttd1" width="100px">HTML</td>
				<td class="ctbl td" colspan="3"><div class="left"><div class="btn_r"><a id="imageUp" style ="cursor:pointer" href="javascript:$('<%=id%>').imageWindow()"><span>이미지삽입</span></a></div></div>
				<%if(LoginInfo.getIsAdmin(request).equals("Y")|| LoginInfo.getUserAuth(request).equals("2") ){%>
					<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_mailtemplate_iframe.jsp?templateID=<c:out value="${mailTemplate.templateID}"/>" height=400  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
				<%}else { %>
				<c:if test="${mailTemplate.userID==who || mailTemplate.userID==null}">
					<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_mailtemplate_iframe.jsp?templateID=<c:out value="${mailTemplate.templateID}"/>" height=400  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
				</c:if>	
				<c:if test="${mailTemplate.userID!=who}">
				<table>
					<tr><td>
					<c:out value="${mailTemplate.templateContent}" escapeXml="false"/>
					</td></tr>				
				</table>
				</c:if>
				<%} %>
				</td>
				
			</tr>	
			
			
			<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
			<tr>
				<td colspan="12" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">공유그룹</td>
				<td class="ctbl td" colspan="3">
					<jsp:include page="../../../include_inc/selectgroup_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="Y" />
						<jsp:param name="groupID" value="<%=shareGroupID %>" />
					</jsp:include>
				</td>
			</tr>	
					<%}else { %>
			<c:if test="${mailTemplate.userID==who || mailTemplate.userID==null}">
			<tr>
				<td colspan="12" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">공유그룹</td>
				<td class="ctbl td" colspan="3">
					<jsp:include page="../../../include_inc/selectgroup_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="Y" />
						<jsp:param name="groupID" value="<%=shareGroupID %>" />
					</jsp:include>					
					
				</td>
			</tr>
			</c:if>	
										
					<%} %>
				
			<tr>
				<td colspan="12" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1">사용여부</td>
				<td class="ctbl td" colspan="3">
					<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${mailTemplate.useYN=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${mailTemplate.useYN=='N'}">select='YES'</c:if>>N</li>
						
					</ul>
					<%}else { %>
					<c:if test="${mailTemplate.userID==who || mailTemplate.userID==null}">
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${mailTemplate.useYN=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${mailTemplate.useYN=='N'}">select='YES'</c:if>>N</li>
						
					</ul>			
					</c:if>
					<c:if test="${mailTemplate.userID!=who}">
					<c:out value="${mailTemplate.useYN}"/>
					</c:if>
					
					<%} %>
					
				</td>
			</tr>
			<tr>
				<td colspan="12" class="ctbl line"></td>
			</tr>
			</tbody>
			<tbody id="<%=id%>_check_content" >
			</tbody>
			</table>
		</form>
	</div>
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
	
	<c:if test="${mailTemplate.templateID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${mailTemplate.templateID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	<!-- 
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').checkData(<c:out value="${mailTemplate.templateID}"/>)" class="web20button bigblue">저 장</a></div>
	 -->
	 <div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData(<c:out value="${mailTemplate.templateID}"/>)" class="web20button bigblue">저 장</a></div>
	
	
	<%}else { %>
	<c:if test="${mailTemplate.userID==who || mailTemplate.userID==null}">
	<c:if test="${mailTemplate.templateID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${mailTemplate.templateID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	<!-- 
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').checkData(<c:out value="${mailTemplate.templateID}"/>)" class="web20button bigblue">저 장</a></div>
	 -->
	 <div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData(<c:out value="${mailTemplate.templateID}"/>)" class="web20button bigblue">저 장</a></div>
	
	
	</c:if>	
	
	
	<%} %>
	
	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
	</script>

<%}
if(method.equals("check")) {
	String seq = request.getParameter("seq");
%>
	<tr>
		<td class="ctbl td" colspan="2">
			<IFRAME id="<%=id%>_ifrmContentCheck" name="<%=id%>_ifrmContentCheck" src="" height=400  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0 style="display:none"></IFRAME>
		</td>
	</tr>
	<script language="javascript">
		var frm = $('<%=id%>_form');
		frm.action = "iframe/fckeditor/fck_checkcontent_iframe.jsp?id=<%=id%>&method=check&seq=<%=seq %>";
		frm.target = "<%=id%>_ifrmContentCheck";
		frm.submit();
		
		function saveData() {
			$('<%=id%>').saveData('<%=seq%>');
		}
		
		function changeData() {
			frm.eTemplateContent.value = <%=id%>_ifrmContentCheck.check_form.inner_text.value;
			$('<%=id%>').saveData('<%=seq%>');
		}
	</script>
<%} %>

		