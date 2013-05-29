<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %> 
<%@page import="web.common.util.LoginInfo"%>
<%

String id = request.getParameter("id");
String sMode = request.getParameter("mode");

String isAdmin = LoginInfo.getIsAdmin(request);


//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(sMode.equals("list")) {
	String today = DateUtils.getStrByPattern("yyyy-MM-dd");
%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	
	<c:set var="today" value="<%=today%>" /> 
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<c:set var="who_groupID" value="<%=LoginInfo.getGroupID(request)%>" /> 
	
	<c:forEach items="${boardList}" var="board" varStatus="list">
	<TR class="tbl_tr" board_id="<c:out value="${board.boardID}"/>" >
		<TD class="tbl_td" align='center'>
		<%if(LoginInfo.getUserAuth(request).equals("2") ){%>
			<c:if test="${board.userID==who}">
				<input type="checkbox" class="notBorder" id="eBoardID" name="eBoardID" value="<c:out value="${board.boardID}" />" />				
			</c:if>	
			<c:if test="${board.userID!=who}">
				<input type="checkbox" class="notBorder" id="eBoardID}_no" name="eBoardID_no" disabled value="<c:out value="${board.boardID}" />" />				
			</c:if>		
		<%}else{ %>	
			<input type="checkbox" class="notBorder" id="eBoardID" name="eBoardID" value="<c:out value="${board.boardID}" />" />
		<%}%>	
		</TD>
		<TD class="tbl_td" align="left"><b>
			<c:if test="${board.registDate==today}"><img src="images/new.png" /></c:if>		
			<c:if test="${board.priorNum < 3}"><img src="images/notice.gif" /></c:if>
		<%if(LoginInfo.getIsAdmin(request).equals("Y") ){%>			
			<a href="javascript:$('<%=id%>').editWindow(<c:out value="${board.boardID}"/>)"><c:out value="${board.title}" escapeXml="true"/></a>
		<%}else if(LoginInfo.getUserAuth(request).equals("2") ){ %>	
			<c:if test="${board.userID==who}">
			<a href="javascript:$('<%=id%>').editWindow(<c:out value="${board.boardID}"/>)"><c:out value="${board.title}" escapeXml="true"/></a>
			</c:if>	
			<c:if test="${board.userID!=who}">
			<a href="javascript:$('<%=id%>').viewWindow(<c:out value="${board.boardID}"/>)"><c:out value="${board.title}" escapeXml="true"/></a>
			</c:if>	
		<%}else{ %>				
			<a href="javascript:$('<%=id%>').viewWindow(<c:out value="${board.boardID}"/>)"><c:out value="${board.title}" escapeXml="true"/></a>
		<%} %>
		</b></TD>
		<TD class="tbl_td"><c:out value="${board.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${board.registDate}"/></TD>	
		<TD class="tbl_td"><c:out value="${board.hit}"/></TD>	
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
	
<%
}
//****************************************************************************************************/
//  편집 
//****************************************************************************************************/
if(sMode.equals("edit")) {
%>
	<jsp:useBean id="userID" class="java.lang.String" scope="request" />
	<jsp:useBean id="userName" class="java.lang.String" scope="request" />
	<jsp:useBean id="groupID" class="java.lang.String" scope="request" />
	<jsp:useBean id="groupName" class="java.lang.String" scope="request" />

	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<input type="hidden" id="eUserID" name="eUserID" value="<%=LoginInfo.getUserID(request)%>">
			<input type="hidden" id="eBoardID" name="eBoardID" value="<c:out value="${board.boardID}"/>" />
			<input type="hidden" id="eBoardUploadKey" name="eBoardUploadKey" value="<c:out value="${board.upload_key}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1 mustinput" width="80px ">제목</td>
				<td class="ctbl td"><input type="text" id="eTitle" name="eTitle" value="<c:out value="${board.title}"/>" size="95" mustInput="Y" msg="제목 입력"/></td>
				
				<td class="ctbl ttd3" width="100px">우선순위</td>
				<td class="ctbl td">
					<ul id="ePriorNum"  class="selectBox">
						<li data="1" <c:if test="${board.priorNum=='1'}">select='YES'</c:if>>1</li>
						<li data="2" <c:if test="${board.priorNum=='2'}">select='YES'</c:if>>2</li>
						<li data="3" <c:if test="${board.priorNum=='3'}">select='YES'</c:if>>3</li>
						<li data="4" <c:if test="${board.priorNum=='4'}">select='YES'</c:if>>4</li>
						<li data="5" <c:if test="${board.priorNum=='5'}">select='YES'</c:if>>5</li>
					</ul>
				</td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			
			<tr>
				<td class="ctbl ttd2 mustinput">내용</td>
				<td class="ctbl td" colspan="3">
					<div id="<%=id %>_fck" style="clear:both;width:100%">	
					<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_board_iframe.jsp?boardID=<c:out value="${board.boardID}"/>" width="100%" height="300" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
					</div>
					
					<input type="hidden" id="eContent" name="eContent" >	
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="80px ">공개설정</td>
				<td>
					<ul id="eReadAuth"  class="selectBox">
						<li data="1" <c:if test="${board.readAuth=='1'}">select='YES'</c:if>>비공개</li>
						<li data="2" <c:if test="${board.readAuth=='2'}">select='YES'</c:if>>그룹공개</li>
						<li data="3" <c:if test="${board.readAuth=='3'}">select='YES'</c:if>>전체공개</li>
						
					</ul>
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">파일첨부</td>
				<td class="ctbl td">
					<div  style="float:left" id="<%=id%>uploadWrapper" ></div>
					<div style="float:left;padding:22px">
						<a id="<%=id%>_uploadBtn" href="javascript:$('<%=id%>').uploadFile('<c:out value="${board.boardID}"/>')" class="web20button bigblue" >파일 업로드</a>
					</div>
										
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">업로드된 파일</td>
				<td class="ctbl td" style="padding:5px">
				<div id="<%=id%>uploaded" ></div>
				</td>
			</tr>		
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			</tbody>
			</table>
		</form>
	</div>
	

	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<c:if test="${board.boardID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${board.boardID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${board.boardID}"/>)" class="web20button bigblue">저 장</a></div>

	<script language="javascript">

	


	
	
	
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));

		window.addEvent('domready',function() {
			// upload 컨트롤 렌더링
			$('<%=id%>').renderUpload();

			$('<%=id%>').showFileInfo();

			
		});	
	</script>

<%
}

if(sMode.equals("getFileInfo")) {
%>

<div style="float:left">
		<a href="admin/board/board.do?method=fileDownload&uploadKey=<c:out value="${fileUpload.uploadKey}"/>" target="_blank"><img src="images/trees/file.gif" />
		<c:out value="${fileUpload.realFileName}"/></a>
	</div>
	
	
	
	
	
	
<%
}
%>
