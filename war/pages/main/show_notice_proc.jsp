<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="web.common.util.*" %> 
<%@page import="web.common.util.LoginInfo"%>
<%

String id = request.getParameter("id");
String sMode = request.getParameter("mode");

//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(sMode.equals("list")) {
	String today = DateUtils.getStrByPattern("yyyy-MM-dd");
%>
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<form name="<%=id%>_list_sform" id="<%=id%>_list_sform">
	<div class="show_wrapper">
		<div class="right">
			<a href="javascript:$('<%=id%>').viewList()">더보기<img src='images/arrow-right.gif'></a>
		</div>
		<div class="line"></div>
	<c:set var="today" value="<%=today%>" /> 
	<c:forEach items="${boardList}" var="board" varStatus="list" end="4">
	<input type="hidden" id="eBoardID" name="eBoardID" value="<c:out value="${board.boardID}" />" />
	<input type="hidden" id="eUserName" name="eUserName" value="<c:out value="${board.userName}" />" />
	<input type="hidden" id="eRegistDate" name="eRegistDate" value="<c:out value="${board.registDate}" />" />
	<input type="hidden" id="eHit" name="eHit" value="<c:out value="${board.hit}" />" />
		<div>	
			<c:if test="${board.priorNum < 3}"><img src="images/medal_gold_1.png" title="우선순위 상"/></c:if>
			<c:if test="${board.priorNum == 3}"><img src="images/medal_silver_3.png" title="우선순위 중"/></c:if>
			<c:if test="${board.priorNum > 3}"><img src="images/medal_bronze_2.png" title="우선순위 하"/></c:if>
			<c:if test="${board.registDate==today}"><img src="images/new.png" /></c:if>
			<a href="javascript:$('<%=id%>').editWindow(<c:out value="${board.boardID}"/>)"  title="<div style='text-align:left'><strong>작성자 : </strong> <c:out value="${board.userName}"/><br> <strong>작성일 : </strong> <c:out value="${board.registDate}"/></div>">
				<c:out value="${fn:substring(board.title,0,16)}" escapeXml="true"/> 
				<c:if test="${fn:length(board.title) > 16}" >
					..
   				</c:if>
			</a>
		</div>
	</c:forEach>
	</div>
	</form>


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
//보기
//****************************************************************************************************/
if(sMode.equals("view")) {
%>

	<div style="margin-bottom:10px;width:97%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">

			<input type="hidden" id="eBoardID" name="eBoardID" value="<c:out value="${board.boardID}"/>" />
			<input type="hidden" id="eBoardUploadKey" name="eBoardUploadKey" value="<c:out value="${board.upload_key}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="80px " align="center">제목</td>
				<td class="ctbl td" colspan="3"><c:out value="${board.title}"/> </td>	
			<tr>
				<td colspan="4" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="80px " align="center">작성자</td>
				<td class="ctbl td"><c:out value="${board.userName}"/> </td>
				<td class="ctbl ttd1" width="80px " align="center">등록일</td>
				<td class="ctbl td"><c:out value="${board.registDate}"/> </td>			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>		
			<tr>
				<td class="ctbl ttd2" width="80px "  align="center">내용</td>	
				<td class="ctbl td" colspan="3">
				<table>
					<tr><td>
					<c:out value="${board.content}" escapeXml="false"/>
					</td></tr>				
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="80px "  align="center">파일</td>
				<td class="ctbl td" style="padding:5px" colspan="3">
				<div id="<%=id%>uploaded" ></div>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="ctbl line"></td>
			</tr>		
			</tbody>
			</table>
		<div style="float:right;padding-top:10px"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
		</form>
	</div>
	

	
	
	
	
	<script language="javascript">

		window.addEvent('domready',function() {
			

			$('<%=id%>').showFileInfo();

			
		});	
	</script>
	
<%

}

%>
