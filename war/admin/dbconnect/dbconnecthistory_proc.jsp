<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="web.common.util.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String dbID = request.getParameter("dbID");


if(method.equals("dbInfo")){
%>
	
		<form id="<%=id%>_infoform" name="<%=id%>_form" method="post">
		<div style="margin-bottom:10px;width:610px">
		<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">고객정보수집명</td>
				<td class="ctbl td" width="150px"><c:out value="${dbconnectInfo.connectDBName}"/> </td>
				<td class="ctbl ttd1" width="100px">DB 명(DB ID)</td>
				<td class="ctbl td"><c:out value="${dbconnectInfo.dbName}"/> (<c:out value="${dbconnectInfo.dbID}"/>)</td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">수집주기</td>
				<td class="ctbl td">
					<c:if test="${dbconnectInfo.updateType == '1'}">
						1회수집
					</c:if>
					<c:if test="${dbconnectInfo.updateType == '2'}">
						매주 수집
					</c:if>
					<c:if test="${dbconnectInfo.updateType == '3'}">
						매월 수집
					</c:if>
				</td>
				<td class="ctbl ttd1" width="100px">
					<c:if test="${dbconnectInfo.updateType == '1'}">
						1회수집
					</c:if>
					<c:if test="${dbconnectInfo.updateType == '2'}">
						매주 
					</c:if>
					<c:if test="${dbconnectInfo.updateType == '3'}">
						매월 
					</c:if>
				</td>
				<td class="ctbl td">
					<c:if test="${dbconnectInfo.updateType == '1'}">
						<c:out value="${dbconnectInfo.updateScheduleDate}"/>
					</c:if>
					<c:if test="${dbconnectInfo.updateType == '2'}">
						<c:if test="${dbconnectInfo.updateValue == '1'}">
							일요일
						</c:if>
						<c:if test="${dbconnectInfo.updateValue == '2'}">
							월요일
						</c:if>
						<c:if test="${dbconnectInfo.updateValue == '3'}">
							화요일
						</c:if>
						<c:if test="${dbconnectInfo.updateValue == '4'}">
							수요일
						</c:if>
						<c:if test="${dbconnectInfo.updateValue == '5'}">
							목요일
						</c:if>
						<c:if test="${dbconnectInfo.updateValue == '6'}">
							금요일
						</c:if>
						<c:if test="${dbconnectInfo.updateValue == '7'}">
							토요일
						</c:if>
						(<c:out value="${dbconnectInfo.updateScheduleHour}"/>시  <c:out value="${dbconnectInfo.updateScheduleMinute}"/>분) 
					</c:if>
					<c:if test="${dbconnectInfo.updateType == '3'}">
						<c:out value="${dbconnectInfo.updateValue}"/> 일
					</c:if>
				</td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">사용 테이블명</td>
				<td class="ctbl td" ><c:out value="${dbconnectInfo.tableName}"/> </td>
				<td class="ctbl ttd1" width="100px">수집 인원</td>
				<td class="ctbl td" colspan="3"><c:out value="${dbconnectInfo.totalCount}"/> 명</td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">수집 쿼리</td>
				<td class="ctbl td" colspan="3"><c:out value="${dbconnectInfo.queryText}"/> </td>
			</tr>
		</tbody>
		</table>
		</div>
		</form>
<%
}
//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("historylist")) {
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
	
	
	
	<c:forEach items="${dbconnectInfoList}" var="dbconnectInfo">
	<TR class="tbl_tr">
		<TD class="tbl_td"><c:out value="${dbconnectInfo.tableName}"/></TD>
		<TD class="tbl_td"><c:out value="${dbconnectInfo.updateStartDate}"/></TD>
		<TD class="tbl_td">
			<c:if test="${dbconnectInfo.state == '2'}">
				<c:out value="${dbconnectInfo.updateEndDate}"/>
			</c:if>
		</TD>
		<TD class="tbl_td"><c:out value="${dbconnectInfo.totalCount}"/> 명</TD>
		<TD class="tbl_td">
			<c:if test="${dbconnectInfo.state == '0'}">
				<img src="images/massmail/ready.gif" title="수집대기중"/>
			</c:if>
			<c:if test="${dbconnectInfo.state == '1'}">
				<img src="images/massmail/sending.gif" title="수집중"/>
			</c:if>
			<c:if test="${dbconnectInfo.state == '2'}">
				<img src="images/massmail/finish.gif" title="수집완료"/>
			</c:if>
			<c:if test="${dbconnectInfo.state == '3'}">
				<img src="images/massmail/error.gif" title="수집중에러"/>
			</c:if>
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
			
		});

	</script>
<%
}
%>		