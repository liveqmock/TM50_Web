<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
	<c:forEach items="${filterManagerlist}" var="filterManager">
		<TR class="tbl_tr" filter_id="<c:out value="${filterManager.filterID}"/>" >
			<TD class="tbl_td" align='center'><input type="checkbox" class="notBorder" id="eFilterID" name="eFilterID" value="<c:out value="${filterManager.filterID}" />" /></TD>
 			<TD class="tbl_td">
 				<c:if test="${filterManager.filterType=='1'}">제목</c:if>
				<c:if test="${filterManager.filterType=='2'}">본문</c:if>
				<c:if test="${filterManager.filterType=='3'}">제목/본문</c:if>		
 			</TD>			
			<TD class="tbl_td" align="left"><a href="javascript:$('<%=id%>').editWindow('<c:out value="${filterManager.filterID}"/>')">
				<c:out value="${fn:substring(filterManager.content,0,20)}" escapeXml="true"/>
				<c:if test="${fn:length(filterManager.content) > 20}" >
					..
   				</c:if>
				</a>
			</TD>
			<TD class="tbl_td" align="left">
				<c:out value="${fn:substring(filterManager.description,0,20)}" escapeXml="true"/>
				<c:if test="${fn:length(filterManager.description) > 20}" >
					..
   				</c:if>
				
			</TD>		
			
			
			<TD class="tbl_td">
					<c:if test="${filterManager.contentType=='1'}">스팸성 문구</c:if>
					<c:if test="${filterManager.contentType=='2'}">Script</c:if>
					<c:if test="${filterManager.contentType=='3'}">Tag</c:if>			
				
			</TD>
			<TD class="tbl_td"><c:out value="${filterManager.userName}"/></TD>	
			<TD class="tbl_td" style="width: 153px;"><c:out value="${filterManager.registDate}"/></TD>		
			<TD class="tbl_td">
				<c:if test="${filterManager.filterLevel=='1'}">발송 불가</c:if>
				<c:if test="${filterManager.filterLevel=='2'}">제외 권장</c:if>
				<c:if test="${filterManager.filterLevel=='3'}">사용안함</c:if>	
					
			</TD>	
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
			<input type="hidden" id="eFilterID" name="eFilterID" value="<c:out value="${filterManager.filterID}"/>" />			
			<table class="ctbl" width="100%">
						
			<tbody>
			<tr>	
				<td class="ctbl ttd1">내용</td>
				<td class="ctbl td" colspan="5"><input type="text" id="eContent" name="eContent" value="<c:out value="${filterManager.content}"/>" size="64" mustInput="Y" msg="내용 입력"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>	
				<td class="ctbl ttd1">설명</td>
				<td class="ctbl td" colspan="5"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${filterManager.description}"/>" size="64"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>
				<td class="ctbl ttd1">구분</td>
				<td class="ctbl td">
					<ul id="eFilterType"  class="selectBox">
						<li data="1" <c:if test="${filterManager.filterType=='1'}">select='Y'</c:if>>제목</li>
						<li data="2" <c:if test="${filterManager.filterType=='2'}">select='Y'</c:if>>본문</li>
						<li data="3" <c:if test="${filterManager.filterType=='3'}">select='Y'</c:if>>제목/본문</li>
					</ul>
				</td>
				<td class="ctbl ttd1">필터타입</td>
				<td class="ctbl td">
					<ul id="eContentType"  class="selectBox">
						<li data="1" <c:if test="${filterManager.contentType =='1'}">select='Y'</c:if>>스팸성 문구</li>
						<li data="2" <c:if test="${filterManager.contentType =='2'}">select='Y'</c:if>>Script</li>
						<li data="3" <c:if test="${filterManager.contentType =='3'}">select='Y'</c:if>>Tag</li>
					</ul>
				</td>
				<td class="ctbl ttd1">단계</td>
				<td class="ctbl td">
					<ul id="eFilterLevel"  class="selectBox">
						<li data="1" <c:if test="${filterManager.filterLevel=='1'}">select='Y'</c:if>>발송 불가</li>
						<li data="2" <c:if test="${filterManager.filterLevel=='2'}">select='Y'</c:if>>제외 권장</li>
						<li data="3" <c:if test="${filterManager.filterLevel=='3'}">select='Y'</c:if>>사용안함</li>
					</ul>
				</td>
			</tr>	
			</tbody>
			</table>
		</form>
	</div>
	

	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<c:if test="${dbset.dbID != 0}" >
		<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${filterManager.filterID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${filterManager.filterID}"/>)" class="web20button bigblue">저 장</a></div>

	<script language="javascript">
		
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