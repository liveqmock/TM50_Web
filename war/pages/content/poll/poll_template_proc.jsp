<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %> 
<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String shareGroupID = request.getParameter("shareGroupID");

//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("listPollTemplate")) {
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
	<c:forEach items="${pollTemplateList}" var="pollTemplate">
	<TR class="tbl_tr" polltemplate_id="<c:out value="${pollTemplate.pollTemplateID}"/>"  sharegroup_id="<c:out value="${pollTemplate.shareGroupID}"/>">
		<TD class="tbl_td" align='center'>
		<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
			<input type="checkbox" class="notBorder" id="ePollTemplateID" name="ePollTemplateID" value="<c:out value="${pollTemplate.pollTemplateID}" />" />
		<%}else { %>
			<c:if test="${pollTemplate.userID==who}">
				<input type="checkbox" class="notBorder" id="ePollTemplateID" name="ePollTemplateID" value="<c:out value="${pollTemplate.pollTemplateID}" />" />			
			</c:if>	
			<c:if test="${pollTemplate.userID!=who}">
				<input type="checkbox" class="notBorder" id="ePollTemplateID_no" name="ePollTemplateID_no" disabled value="<c:out value="${pollTemplate.pollTemplateID}" />" />		
			</c:if>		
					
		<%} %>
		</TD>		
				
		<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${pollTemplate.pollTemplateID}"/>','<c:out value="${pollTemplate.shareGroupID}"/>')"><c:out value="${pollTemplate.pollTemplateTitle}" escapeXml="true"/></b></a></TD>		
		<TD class="tbl_td"><c:out value="${pollTemplate.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${pollTemplate.groupName}" /></TD>	
		<TD class="tbl_td"><c:out value="${pollTemplate.useYN}"/></TD>	
		<TD class="tbl_td"><c:out value="${pollTemplate.registDate}"/></TD>
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
if(method.equals("editPollTemplate")) {
	if(shareGroupID.equals("undefined")){
		shareGroupID = "NOT";
	}	
%>	
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
		<input type="hidden" id="ePollTemplateHTML" name="ePollTemplateHTML">
		<input type="hidden" id="eShareGroupID" name="eShareGroupID">
			<input type="hidden" id="ePollTemplateID" name="ePollTemplateID" value="<c:out value="${pollTemplate.pollTemplateID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">템플릿명</td>
				<td class="ctbl td">
				<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
					<input type="text" id="ePollTemplateTitle" name="ePollTemplateTitle" value="<c:out value="${pollTemplate.pollTemplateTitle}"/>" size="50" mustInput="Y" msg="템플릿명을  입력"/></td>
				<%}else { %>
				<c:if test="${pollTemplate.userID==who || pollTemplate.userID==null }">
					<input type="text" id="ePollTemplateTitle" name="ePollTemplateTitle" value="<c:out value="${pollTemplate.pollTemplateTitle}"/>" size="50" mustInput="Y" msg="템플릿명을  입력"/></td>
				</c:if>	
				<c:if test="${pollTemplate.userID!=who}">
					<c:out value="${pollTemplate.pollTemplateTitle}"/>
				</c:if>	
				
				<%} %>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1" width="100px">치환변수삽입</td>
				<td class="ctbl td">
						<div style="float:left">
						<ul id="eValues" class="selectBox" onclick="javascript:$('<%=id%>').selectOnetoone(this.value)">
						<li data="">--치환변수--</li>		
						<li data="<%=Constant.POLL_TEMPLATE_TITLE%>">설문제목(필수)<%=Constant.POLL_TEMPLATE_TITLE%></li>				
						<li data="<%=Constant.POLL_TEMPLATE_HEAD%>">시작문구(선택)<%=Constant.POLL_TEMPLATE_HEAD%></li>
						<li data="<%=Constant.POLL_TEMPLATE_QUESTIONS%>">문항들(필수) <%=Constant.POLL_TEMPLATE_QUESTIONS%></li>
						<li data="<%=Constant.POLL_TEMPLATE_TAIL%>">종료문구(선택)<%=Constant.POLL_TEMPLATE_TAIL%></li>						
						</ul>
						</div>
						<div style="float:left;margin-left:20px;margin-top:5px">
							<a title="<div style='text-align:left'>												
								<img src='images/tag_red.png'><strong>설명</strong><br>
								&nbsp; HTML안에 치환변수를 적절한 위치에 삽입함으로써 사용자가가 정해진 양식으로 설문을 작성토록 합니다.<br>										
								&nbsp; 1.설문제목을 넣을 위치에 [$TM_POLL_TITLE]를 삽입합니다.(필수)<br>
								&nbsp; 2.시작문구를 넣을 위치에 [$TM_POLL_HEAD]를 삽입합니다.(선택)<br>
								&nbsp; 3.문항들을 넣을 위치에 [$TM_POLL_QUESTIONS]를 삽입합니다.(필수)<br>
								&nbsp; 4.종료문구를 넣을 위치에 [$TM_POLL_TAIL]를 삽입합니다.(선택)</div>">
								<img src='images/tag_blue.png'> <strong>치환변수란?</strong>
							</a> 
						</div>									
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>				
				<td class="ctbl ttd1" width="100px">HTML</td>
				<td class="ctbl td">
				<%if(LoginInfo.getIsAdmin(request).equals("Y")|| LoginInfo.getUserAuth(request).equals("2") ){%>
					<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_polltemplate_iframe.jsp?pollTemplateID=<c:out value="${pollTemplate.pollTemplateID}"/>" height=400  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
				<%}else { %>
				<c:if test="${pollTemplate.userID==who || pollTemplate.userID==null}">
					<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_polltemplate_iframe.jsp?pollTemplateID=<c:out value="${pollTemplate.pollTemplateID}"/>" height=400  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
				</c:if>	
				<c:if test="${pollTemplate.userID!=who}">
				<table>
					<tr><td>
					<c:out value="${pollTemplate.pollTemplateHTML}" escapeXml="false"/>
					</td></tr>				
				</table>
				</c:if>
				<%} %>
				</td>				
			</tr>				
			
			<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">공유그룹</td>
				<td class="ctbl td">
					<jsp:include page="../../../include_inc/selectgroup_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="Y" />
						<jsp:param name="groupID" value="<%=shareGroupID %>" />
					</jsp:include>
				</td>
			</tr>	
			<%}else { %>
			<c:if test="${pollTemplate.userID==who || pollTemplate.userID==null}">
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">공유그룹</td>
				<td class="ctbl td">
					<jsp:include page="../../../include_inc/selectgroup_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="Y" />
						<jsp:param name="groupID" value="<%=shareGroupID %>" />
					</jsp:include>					
					
				</td>
			</tr>
			</c:if>										
			<%} %>
				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1">사용여부</td>
				<td class="ctbl td" >
					<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${pollTemplate.useYN=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${pollTemplate.useYN=='N'}">select='YES'</c:if>>N</li>
						
					</ul>
					<%}else { %>
					<c:if test="${pollTemplate.userID==who || pollTemplate.userID==null}">
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${pollTemplate.useYN=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${pollTemplate.useYN=='N'}">select='YES'</c:if>>N</li>
						
					</ul>			
					</c:if>
					<c:if test="${pollTemplate.userID!=who}">
					<c:out value="${pollTemplate.useYN}"/>
					</c:if>
					
					<%} %>
					
				</td>
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
	<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
	
	<c:if test="${pollTemplate.pollTemplateID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${pollTemplate.pollTemplateID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	<!-- 
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').checkData(<c:out value="${pollTemplate.pollTemplateID}"/>)" class="web20button bigblue">저 장</a></div>
	 -->
	 <div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData(<c:out value="${pollTemplate.pollTemplateID}"/>)" class="web20button bigblue">저 장</a></div>
	
	
	<%}else { %>
	<c:if test="${pollTemplate.userID==who || pollTemplate.userID==null}">
	<c:if test="${pollTemplate.pollTemplateID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${pollTemplate.pollTemplateID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData(<c:out value="${pollTemplate.pollTemplateID}"/>)" class="web20button bigblue">저 장</a></div>
	
	
	</c:if>	
	
	
	<%} %>
	
	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));


		/***********************************************/
		/* 치환처리
		/***********************************************/
		$('<%=id%>').selectOnetoone = function(val){
			$('<%=id%>_ifrmFckEditor').contentWindow.insertFCKHtml(val);
		}


		
	</script>

<%}%>


		