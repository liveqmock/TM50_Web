<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*"%>
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

	
	<c:forEach items="${onetooneList}" var="onetoone">
	<TR class="tbl_tr" onetoone_id="<c:out value="${onetoone.onetooneID}"/>" >				
		<TD class="tbl_td"><b><c:out value="${onetoone.onetooneID}"/></b></TD>
		<TD class="tbl_td"><b><c:out value="${onetoone.onetooneName}"/></b></TD>
		<TD class="tbl_td"><b><c:out value="${onetoone.onetooneAlias}"/></b></TD>			
	</TR>
	</c:forEach>

	
	<script type="text/javascript">

		window.addEvent('domready', function(){		
		
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
			
			});
			$('<%=id%>').grid_content.render();
			
		});

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