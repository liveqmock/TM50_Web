<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.LoginInfo"%>
<%@ page import="web.common.util.*" %> 
<%

String id = request.getParameter("id");
String method = request.getParameter("method");

//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
%>

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
	<c:forEach items="${pollQuestionList}" var="questionList">
	<TR class="tbl_tr">
		<TD class="ctbl ttd1" align='center'><input type="radio" class="notBorder" id="ePollInfo" name="ePollInfo" value="<c:out value="${questionList.pollID}"/>-<c:out value="${questionList.questionID}"/>-<c:out value="${questionList.questionType}"/>" /></TD>
		<TD class="ctbl ttd1" align='left'><c:out value="${questionList.questionHTML}" escapeXml="false"/></TD>					
	</TR>
	</c:forEach>
	<script type="text/javascript">

		window.addEvent('domready', function(){

			
			
		});

	</script>
<%}%>		