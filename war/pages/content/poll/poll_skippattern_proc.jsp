<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.LoginInfo"%>
<%@ page import="web.common.util.*" %> 
<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String pollID = request.getParameter("ePollID");
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
		<TD class="tbl_td" align='center'><c:out value="${questionList.questionNo}"/></TD>
		<TD class="tbl_td"  align='left'>
			<c:if test="${questionList.questionType == '1' && questionList.exampleType == '1' && questionList.exampleGubun == '1'}">
				<img src="/images/book_next.png" /><a href="javascript:$('<%=id%>').questionView(${questionList.questionID})">
			</c:if>
			<c:out value="${questionList.questionText}"/>
			<c:if test="${questionList.questionType == '1' && questionList.exampleType == '1' && questionList.exampleGubun == '1'}">
				</a>
			</c:if>
			
		</TD>					
	</TR>
	</c:forEach>
	<script type="text/javascript">

		window.addEvent('domready', function(){
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
<%}else if(method.equals("view")) { %>
	<jsp:useBean id="pollExampleList" class="java.util.ArrayList" scope="request" />
	<form id="<%=id%>_question_form" name="<%=id%>_question_form" method="post">	
		<input type="hidden" id="ePollID" name="ePollID" value="<%=pollID%>" />	
		<input type="hidden" id="eQuestionID" name="eQuestionID" value="${pollQuestion.questionID}" />	
		<table class="ctbl" width="100%">
			<tbody>
			<tr>				
				<td class="ctbl ttd1" width="100px">질문</td>
				<td class="ctbl td">			
					<textarea id="eQuestionText" name="eQuestionText" style="width:660px;height:50px" disabled>${pollQuestion.questionNo}. ${pollQuestion.questionText}</textarea>	
				</td>				
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>				
				<td class="ctbl ttd1" width="100px">보기 입력</td>
				<td class="ctbl td">	
					<table id="<%=id%>_pollExample" width="100%">	
						<c:forEach items="${pollExampleList}" var="pollExample" varStatus="cnt">					
							<tr>
								<td width="300px">
									<c:out value="${cnt.count}"/>.<input type="text" id="eExampleDesc" name="eExampleDesc" size="45" value="${pollExample.exampleDesc}" disabled/>
								</td>
								<td>|</td>
								<td>
									<input type="text" id="eGoToQuestionNo" name="eGoToQuestionNo" value="<c:out value="${pollExample.goToQuestionNo}"/>" size="1">번으로 이동
								</td>
								<td>|</td>
								<td>
									<input type="text" id="eResponseStart" name="eResponseStart" value="<c:out value="${pollExample.noResponseStart}"/>" size="1">번 부터 <input type="text" id="eResponseEnd" name="eResponseEnd" value="<c:out value="${pollExample.noResponseEnd}"/>" size="1">번 응답불가
								</td>
							</tr>
						</c:forEach>
					</table>	
				</td>				
			</tr>
		</table>
		<div>
	<div style="float:right;padding-top:10px"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;padding-right:10px;padding-top:10px" ><a id="<%=id%>_saveBtn2" href="javascript:$('<%=id%>').saveSkipPattern('${pollQuestion.questionID}')" class="web20button bigblue">저장</a></div>
	</div>
	</form>
<%}%>		