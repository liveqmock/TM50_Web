<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id"); 
String method = request.getParameter("method"); 

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
	
	
	
	<c:forEach items="${persistfailList}" var="persistfail">
	<TR class="tbl_tr" persistfail_id="<c:out value="${persistfail.persistfailID}"/>" >
		<TD class="tbl_td" align='center'><input type="checkbox" class="notBorder" id="ePersistfailID" name="ePersistfailID" value="<c:out value="${persistfail.persistfailID}" />" /></TD>		
		<TD class="tbl_td" align="left"><b><c:out value="${persistfail.email}" escapeXml="true"/></b></TD>	
		<TD class="tbl_td"><c:if test="${persistfail.massmailTitle==null}">-</c:if>
			<c:if test="${persistfail.massmailTitle!=null}"><c:out value="${persistfail.massmailTitle}" /></c:if>
		</TD>	
		<TD class="tbl_td"><c:out value="${persistfail.targetName}"/></TD>	
		<TD class="tbl_td"><c:out value="${persistfail.massmailGroupName}"/></TD>
		<TD class="tbl_td"><c:out value="${persistfail.smtpCode}"/></TD>
		<TD class="tbl_td" align="left"><c:out value="${persistfail.smtpMsg}"/></TD>		
		<TD class="tbl_td"><c:out value="${persistfail.registDate}"/></TD>
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
<%
}
%>


		