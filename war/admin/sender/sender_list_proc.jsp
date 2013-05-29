<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@page import="web.common.util.LoginInfo"%>
<%@ page import="web.common.util.*" %> 
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
	
	
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<c:forEach items="${senderList}" var="sender">
	<TR class="tbl_tr" sender_id="<c:out value="${sender.senderID}"/>" >
		<TD class="tbl_td" align='center'>
		<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
			<input type="checkbox" class="notBorder" id="eSenderID" name="eSenderID" value="<c:out value="${sender.senderID}" />" />
		<%}else { %>
			<c:if test="${sender.userID==who}">
			<input type="checkbox" class="notBorder" id="eSenderID" name="eSenderID" value="<c:out value="${sender.senderID}" />" />
			</c:if>	
			<c:if test="${sender.userID!=who}">
			<input type="checkbox" class="notBorder" id="eSenderID_no" name="eSenderID_no" disabled value="<c:out value="${sender.senderID}" />" />
			</c:if>	
		<%} %>
		</TD>		
		<TD class="tbl_td"><b><a href="javascript:$('<%=id%>').editWindow(<c:out value="${sender.senderID}"/>)"><c:out value="${sender.senderName}" escapeXml="true"/></b></a></TD>	
		<TD class="tbl_td"><c:out value="${sender.senderEmail}" /></TD>	
		<TD class="tbl_td"><c:out value="${sender.senderCellPhone}"/></TD>	
		<TD class="tbl_td"><c:out value="${sender.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${sender.shareTypeDesc}"/></TD>
		
		<TD class="tbl_td"><c:out value="${sender.useYN}"/></TD>	
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
if(method.equals("edit")) {
%>
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">

			<input type="hidden" id="eSenderID" name="eSenderID" value="<c:out value="${sender.senderID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="80px">이름</td>
				<td class="ctbl td">
				<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
				<input type="text" id="eSenderName" name="eSenderName" value="<c:out value="${sender.senderName}"/>" size="50" mustInput="Y" msg="이름 입력"/>
				<%}else { %>
				<c:if test="${sender.userID==who || sender.userID==null}">
				<input type="text" id="eSenderName" name="eSenderName" value="<c:out value="${sender.senderName}"/>" size="50" mustInput="Y" msg="이름 입력"/>
				</c:if>	
				<c:if test="${sender.userID!=who}">
				<c:out value="${sender.senderName}"/>
				</c:if>	
				<%} %>
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>				
				<td class="ctbl ttd1" width="100px">이메일</td>
				<td class="ctbl td">
				<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
				<input type="text" id="eSenderEmail" name="eSenderEmail" value="<c:out value="${sender.senderEmail}"/>" size="50" mustInput="Y" msg="이메일 입력"/>
				<%}else { %>
				<c:if test="${sender.userID==who || sender.userID==null}">
				<input type="text" id="eSenderEmail" name="eSenderEmail" value="<c:out value="${sender.senderEmail}"/>" size="50" mustInput="Y" msg="이메일 입력"/>
				</c:if>	
				<c:if test="${sender.userID!=who}">
				<c:out value="${sender.senderEmail}"/>
				</c:if>	
				<%} %>
			
				</td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">핸드폰</td>
				<td class="ctbl td" >
				<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
				<input type="text" id="eSenderCellPhone" name="eSenderCellPhone" 
							<c:if test="${sender.senderCellPhone==null}">value="핸드폰번호는 '-'를 포함합니다" onclick="javascript:$('<%=id%>').deleteValue()"</c:if>
							<c:if test="${sender.senderCellPhone!=null}">value="<c:out value="${sender.senderCellPhone}"/>"</c:if> 
							size="50" mustInput="N" />

				<%}else { %>
				<c:if test="${sender.userID==who || sender.userID==null}">
				<input type="text" id="eSenderCellPhone" name="eSenderCellPhone" 
							<c:if test="${sender.senderCellPhone==null}">value="핸드폰번호는 '-'를 포함합니다" onclick="javascript:$('<%=id%>').deleteValue()"</c:if>
							<c:if test="${sender.senderCellPhone!=null}">value="<c:out value="${sender.senderCellPhone}"/>"</c:if> 
							size="50" mustInput="N" />
				</c:if>	
				<c:if test="${sender.userID!=who}">
				<c:out value="${sender.senderCellPhone}"/>
				</c:if>	
				<%} %>
				</td>
   			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">공유타입</td>
				<td class="ctbl td" >
				<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
				<ul id="eShareType"  class="selectBox">
						<li data="3" <c:if test="${sender.shareType=='3'}">select='YES'</c:if>>전체공유</li>
						<li data="2" <c:if test="${sender.shareType=='2'}">select='YES'</c:if>>그룹공유</li>
						<li data="1" <c:if test="${sender.shareType=='1'}">select='YES'</c:if>>비공유</li>
						
					</ul>
	
				<%}else { %>
				<c:if test="${sender.userID==who || sender.userID==null}">
				<ul id="eShareType"  class="selectBox">
						<li data="3" <c:if test="${sender.shareType=='3'}">select='YES'</c:if>>전체공유</li>
						<li data="2" <c:if test="${sender.shareType=='2'}">select='YES'</c:if>>그룹공유</li>
						<li data="1" <c:if test="${sender.shareType=='1'}">select='YES'</c:if>>비공유</li>
						
					</ul>
				</c:if>	
				<c:if test="${sender.userID!=who}">
					<c:if test="${sender.shareType=='3'}">전체공유</c:if>
					<c:if test="${sender.shareType=='2'}">그룹공유</c:if>
					<c:if test="${sender.shareType=='1'}">비공유</c:if>
				</c:if>	
				<%} %>

					
				</td>
			</tr>		
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<!-- 
			<tr>
				<td class="ctbl ttd1">디폴트여부</td>
				<td class="ctbl td" >
					<ul id="eDefaultYN"  class="selectBox">
						<li data="Y" <c:if test="${sender.defaultYN=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${sender.defaultYN=='N'}">select='YES'</c:if>>N</li>
						
					</ul>
				</td>
			</tr>
			 -->						
			<tr>
				<td class="ctbl ttd1">사용여부</td>
				<td class="ctbl td" >
				<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
				<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${sender.useYN=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${sender.useYN=='N'}">select='YES'</c:if>>N</li>
						
					</ul>

				<%}else { %>
				<c:if test="${sender.userID==who || sender.userID==null}">
				<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${sender.useYN=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${sender.useYN=='N'}">select='YES'</c:if>>N</li>
						
					</ul>

				</c:if>	
				<c:if test="${sender.userID!=who}">
					<c:if test="${sender.useYN=='Y'}">사용</c:if>
					<c:if test="${sender.useYN=='N'}">사용안함</c:if>
				</c:if>	
				<%} %>
					
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" >설명</td>
				<td class="ctbl td" colspan="3">
				<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
				<input type="text" id="eDescription" name="eDescription" value="<c:out value="${sender.description}"/>" size="50" />
				<%}else { %>
				<c:if test="${sender.userID==who || sender.userID==null}">
				<input type="text" id="eDescription" name="eDescription" value="<c:out value="${sender.description}"/>" size="50" />
				</c:if>	
				<c:if test="${sender.userID!=who}">
				<c:out value="${sender.description}"/>
				</c:if>	
				<%} %>
				</td>
				
			</tr>
			</tbody>
			</table>
		</form>
	</div>
	

	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
	<c:if test="${sender.senderID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${sender.senderID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${sender.senderID}"/>)" class="web20button bigblue">저 장</a></div>
	<%}else { %>
	<c:if test="${sender.userID==who || sender.userID==null}">
	<c:if test="${sender.senderID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${sender.senderID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${sender.senderID}"/>)" class="web20button bigblue">저 장</a></div>
	</c:if>	
	<%} %>
	

	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
	</script>

<%
}
%>
		