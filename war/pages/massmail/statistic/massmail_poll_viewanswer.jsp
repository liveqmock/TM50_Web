<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %> 
<%

	String id = request.getParameter("id");
	String method = request.getParameter("method");

%>
<%
	if(method.equals("pollViewAnswer")){
%>

	<jsp:useBean id="massmailID" class="java.lang.String" scope="request" />
	<jsp:useBean id="scheduleID" class="java.lang.String" scope="request" />
	<jsp:useBean id="pollID" class="java.lang.String" scope="request" />
	<jsp:useBean id="sendID" class="java.lang.String" scope="request" />
	<jsp:useBean id="matrixX" class="java.lang.String" scope="request" />
	<jsp:useBean id="matrixY" class="java.lang.String" scope="request" />
	<jsp:useBean id="email" class="java.lang.String" scope="request" />
	
	
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<div><h2>설문상세보기</h2></div>
		<div>
		<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="800px" align="center">
		<TR style="height:30px;" >					
		<TD class="ctbl ttd1" width="200px">이메일</TD>		
		<TD class="ctbl td" align="center"><%=email%></TD>		
		<TD class="ctbl td" align="center"><a href="javascript:$('<%=id%>').personPreviewDown()" class="web20button blue">Excel 다운</a></TD>								
		</TR>
		</TABLE>	
		</div>
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="800px" align="center">
			<thead>
				<tr>
					<th style="height:30px;width:50px;">번호</th>
					<th style="height:30px;" align="left">질문</th>
					<th style="height:30px;width:200px;">구분</th>
					<th style="height:30px;width:300px;" align="left">보기</th>
				</tr>
			</thead>		
			
			<c:set var="temp" value=""/>	
			<c:set var="questionDescription1" value=""/>
			<c:set var="questionDescription2" value=""/>
			<c:set var="questionDescription3" value=""/>
			<c:set var="questionDescription4" value=""/>
			<c:set var="questionDescription" value=""/>
			
			<c:forEach items="${pollViewAnswerList}" var="MassMailStatisticPoll">			
			<c:if test="${MassMailStatisticPoll.questionType==1}">
				<c:set var="questionDescription1" value="일반질문"/>
			</c:if>
			<c:if test="${MassMailStatisticPoll.questionType==2}">
				<c:set var="questionDescription1" value="단일매트리릭스"/>
			</c:if>			
			<c:if test="${MassMailStatisticPoll.questionType==3}">
				<c:set var="questionDescription1" value="복합매트리릭스"/>
			</c:if>					
			<c:if test="${MassMailStatisticPoll.exampleType==1}">
				<c:set var="questionDescription2" value="객관식"/>
			</c:if>
			<c:if test="${MassMailStatisticPoll.exampleType==2}">
				<c:set var="questionDescription2" value="주관식"/>
			</c:if>			
			<c:if test="${MassMailStatisticPoll.exampleType==5}">
				<c:set var="questionDescription2" value="척도형"/>
			</c:if>
			<c:if test="${MassMailStatisticPoll.exampleGubun==1}">
				<c:set var="questionDescription3" value="단일응답"/>
			</c:if>				
			<c:if test="${MassMailStatisticPoll.exampleGubun==2}">
				<c:set var="questionDescription3" value="복수응답"/>
			</c:if>				
						
			<c:set var="questionDescription" value="${questionDescription1}/${questionDescription2}/${questionDescription3}"/>

					
					
				<c:if test="${temp == MassMailStatisticPoll.questionNo}">
					<TR style="height:30px;" >					
					<TD class="tbl_td"></TD>		
					<TD class="tbl_td" align="left"></TD>										
					<TD class="tbl_td"></TD>
					<c:if test="${MassMailStatisticPoll.exampleType == '1'}">
					<TD class="tbl_td" align="left"><c:out value="${MassMailStatisticPoll.exampleID}"/>.&nbsp;<c:out value="${MassMailStatisticPoll.exampleDesc}"/>
					<c:if test="${MassMailStatisticPoll.exampleID == MassMailStatisticPoll.responseExampleID}">◀</c:if>
					</TD>
					</c:if>
					<c:if test="${MassMailStatisticPoll.exampleType == '5'}">
					<TD class="tbl_td" align="left"><c:out value="${MassMailStatisticPoll.exampleID}"/>.&nbsp;<c:out value="${MassMailStatisticPoll.exampleDesc}"/>
					<c:if test="${MassMailStatisticPoll.exampleID == MassMailStatisticPoll.responseExampleID}">◀</c:if>
					</TD>
					</c:if>
					<c:if test="${MassMailStatisticPoll.exampleType == '2'}">
					<TD class="tbl_td" align="left">&nbsp;<c:out value="${MassMailStatisticPoll.responseText}"/></TD>
					</c:if>
							
					</TR>	
				</c:if>											
				<c:if test="${temp != MassMailStatisticPoll.questionNo}">
				<c:set var="temp" value="${MassMailStatisticPoll.questionNo}"/>
					<TR>
						<TD colspan="10" class="ctbl line"></TD>
					</TR>				
					<TR style="height:30px;">					
					<TD class="tbl_td"><c:out value="${MassMailStatisticPoll.questionNo}"/></TD>		
					<TD class="tbl_td" align="left"><c:out value="${MassMailStatisticPoll.questionText}"/></TD>										
					<TD class="tbl_td"><c:out value="${questionDescription}"/></TD>
					<c:if test="${MassMailStatisticPoll.exampleType == '1'}">
					<TD class="tbl_td" align="left"><c:out value="${MassMailStatisticPoll.exampleID}"/>.&nbsp;<c:out value="${MassMailStatisticPoll.exampleDesc}"/>
					<c:if test="${MassMailStatisticPoll.exampleID == MassMailStatisticPoll.responseExampleID}">◀</c:if>
					</TD>
					</c:if>
					<c:if test="${MassMailStatisticPoll.exampleType == '5'}">
					<TD class="tbl_td" align="left"><c:out value="${MassMailStatisticPoll.exampleID}"/>.&nbsp;<c:out value="${MassMailStatisticPoll.exampleDesc}"/>
					<c:if test="${MassMailStatisticPoll.exampleID == MassMailStatisticPoll.responseExampleID}">◀</c:if>
					</TD>
					</c:if>
					<c:if test="${MassMailStatisticPoll.exampleType == '2'}">
					<TD class="tbl_td" align="left">&nbsp;<c:out value="${MassMailStatisticPoll.responseText}"/></TD>
					</c:if>
	

					</TR>	
				</c:if>			
			</c:forEach>	
			
		</TABLE>
	</div>
	<script type="text/javascript">

		//************************************************************
		//대상자 다운
		//************************************************************/
		$('<%=id%>').personPreviewDown = function() {
			location.href = "pages/massmail/statistic/massmail_poll_viewanswer_excel.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&pollID=<%=pollID%>&sendID=<%=sendID%>&matrixX=<%=matrixX%>&matrixY=<%=matrixY%>&email=<%=email%>";
			
		}

	
		window.addEvent('domready', function(){			
			renderTableHeader( "<%=id %>List" );
			// 테이블 렌더링
		
		});
		
				
	</script>	


<%}%>