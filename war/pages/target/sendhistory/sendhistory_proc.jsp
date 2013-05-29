<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %> 
<%@ page import="web.massmail.statistic.model.*" %>  
<%
String id = request.getParameter("id");
String method = request.getParameter("method");

//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
%>

	
<%@page import="web.common.util.ImageExtractor"%><jsp:useBean id="curPage" class="java.lang.String" scope="request" />
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
	
	
	<c:set var="remoteid" value="0"/>
	<c:forEach items="${sendHistoryList}" var="sendHistory">
	
	<TR class="tbl_tr"> 
		<TD class="tbl_td"><c:out value="${sendHistory.email}" /></TD>	
		<TD class="tbl_td"><c:out value="${sendHistory.name}" /></TD>		
		<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').viewMassmailInfo('<c:out value="${sendHistory.massmailID}"/>', '<c:out value="${sendHistory.scheduleID}"/>')"><c:out value="${sendHistory.massmailTitle}" escapeXml="true"/></b></a></TD>		
		<TD class="tbl_td">
			<c:if test="${sendHistory.smtpCodeType == 'Y'}" >
				<c:out value="${sendHistory.smtpCodeType}"/>
			</c:if>
			<c:if test="${sendHistory.smtpCodeType == 'N'}" >
				<a href="javascript:$('<%=id%>').viewLog('<c:out value="${remoteid}"/>')"><c:out value="${sendHistory.smtpCodeType}"/></a>
				<div class="remoteControl_wrapper2">
					<div id="<%=id %>remoteControl<c:out value="${remoteid}"/>" name="<%=id %>remoteControl<c:out value="${remoteid}"/>" class="remoteControl2" style="z-index:150;display:none">
						<h6>실패원인 <div class="btnClose"><a href="javascript:$('<%=id%>').viewLogClose('<c:out value="${remoteid}"/>')"><img src="images/remote_btn_close.gif" title="실패원인 닫기" width="15" height="14"></a></div></h6>
						<div class="remoteBox2">
							<div class="remoteBtn2">
								<c:out value="${sendHistory.failCauseCodeName}"/>
							</div>
							<div class="remoteBtn2">
								<c:out value="${sendHistory.smtpMsg}"/>
							</div>
						</div>
					</div>
				</div>
				<c:set var="remoteid" value="${remoteid + 1}"/>
			</c:if>			
		</TD>	
		<TD class="tbl_td">
			<c:if test="${sendHistory.openYN == 'Y'}" >
				<span title="수신 확인 시간 : <c:out value="${sendHistory.openDate}"/>">
			</c:if>
			<c:out value="${sendHistory.openYN}" />
			<c:if test="${sendHistory.openYN == 'Y'}" >
				</span>
			</c:if>
		</TD>	
		<TD class="tbl_td"><c:out value="${sendHistory.clickYN}"/></TD>	
		<TD class="tbl_td"><c:out value="${sendHistory.rejectcallYN}"/></TD>	
		<TD class="tbl_td"><c:out value="${sendHistory.registDate}"/></TD>
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
<% }
if(method.equals("massmailinfo")){	
	String today = DateUtils.getStrByPattern("yyyy-MM-dd HH:mm:ss");
%>
<div style="margin-bottom:10px;width:100%">
	<jsp:useBean id="massmailInfo" class="web.massmail.statistic.model.MassMailInfo" scope="request" />
	<jsp:useBean id="targetGroupList" class="java.lang.Object" scope="request" />
	<table border="0" cellpadding="3" class="ctbl" width="100%">
		<tbody>
			<tr>
				<td class="ctbl ttd1" width="120px">대량메일 명 </td>
				<td class="ctbl td" width="130px"><c:out value="${massmailInfo.massmailTitle}"/></td>
				<td class="ctbl ttd1" width="120px">작성자</td>
				<td class="ctbl td"><c:out value="${massmailInfo.userName}"/></td>
				<td class="ctbl ttd1" width="120px">발송타입 </td>
				<td class="ctbl td" width="130px"><c:out value="${massmailInfo.sendType}"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1">발송 대상자 그룹</td>
				<td class="ctbl td" colspan="3">
					<c:forEach items="${targetGroupList}" var="targetgroup">
						<c:out value="${targetgroup.targetName}"/> [<c:out value="${targetgroup.targetCount}"/>통] &nbsp;&nbsp;&nbsp;
					
					</c:forEach>
				</td>
				<td class="ctbl ttd1">발송 인원</td>
				<td class="ctbl td"><fmt:formatNumber value="${massmailInfo.sendedCount}" type="number"/> 통</td>
			</tr>		
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1">작성 일시</td>
				<td class="ctbl td"><c:out value="${massmailInfo.registDate}"/></td>
				<td class="ctbl ttd1">발송시작시간</td>
				<td class="ctbl td">
						<c:out value="${massmailInfo.sendStartTime}"/>
				</td>
				<td class="ctbl ttd1">발송종료시간</td>
				<td class="ctbl td"><c:out value="${massmailInfo.sendEndTime}"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>	
				<td class="ctbl ttd1">보내는사람이름</td>
				<td class="ctbl td"><c:out value="${massmailInfo.senderName}"/></td>
				<td class="ctbl ttd1">보내는사람이메일</td>
				<td class="ctbl td"><c:out value="${massmailInfo.senderMail}"/></td>
				<td class="ctbl ttd1">메일본문링크</td>
				<td class="ctbl td">수집
					<c:if test="${massmailInfo.mailLinkYN == 'N'}">안함</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">메일제목</td>
				<td class="ctbl td" colspan="5"><c:out value="${massmailInfo.mailTitle}"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>
				<td colspan="6" class="ctbl td">
					<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_massmailwrite_iframe.jsp?massmailID=<c:out value="${massmailInfo.massmailID}"/>" width="100%" height="500" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div style="float:right"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>
<% }
if(method.equals("addTarget")){	
	
%>
<div style="margin-bottom:10px;width:100%">
<form id="<%=id%>_form" name="<%=id%>_form" method="post">
	<table border="0" cellpadding="3" class="ctbl" width="100%">
		<tbody>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px" align="left">대상자그룹명</td>
				<td class="ctbl td"><input type="text" id="eTargetName" name="eTargetName" value="" size="50" mustInput="Y" msg="대량메일명을 입력하세요"/></td>
			</tr>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1" width="100px">설명</td>
				<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="" size="50"/></td>
			</tr>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>				
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">공유여부</td>
				<td class="ctbl td">
					<ul id="eShareType"  class="selectBox">
						<li data="1">비공유</li>
						<li data="2">그룹공유</li>
						<li data="3">전체공유</li>												
					</ul>
				</td>
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">고객명 사용 여부</td>
				<td class="ctbl td">
					<div style="float:left;margin-top:3px">
					<ul id="eNameUseYN"  class="selectBox" onChange="javascript:$('<%=id%>').setInsteadView(this.value)">
						<li data="N">미사용</li>
						<li data="Y">사용</li>									
					</ul>
					</div>
					<div style="float:right;margin-top:8px"><img src="images/tag_blue.png" alt="Tip" /> 일대일 치환 항목에서의  사용 여부를 의미합니다.</div>
				</td>
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr id="<%=id%>_insteadTR" style="display:none">
				<td class="ctbl ttd1 mustinput" width="100px">고객명 대처 문자</td>
				<td class="ctbl td">
					<div style="margin-top:10px">
						<input type="text" id="eInstead" name="eInstead" value="" size="50"/><br/>
					</div>
					<div style="margin-top:10px">
						<img src="images/tag_blue.png" alt="Tip" /> 고객명이 없는 대상자는 대처 문자의 값으로 발송됩니다.<br/>
						<img src="images/tag_blue.png" alt="Tip" /> 공백일 경우 이메일 값이 고객명으로 사용됩니다.
					</div>
				</td>
			</tr>		
		</tbody>
	</table>
</form>
</div>
<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
<div style="float:right;padding-right:10px"><a href="javascript:$('<%=id%>').addTarget()" class="web20button bigblue">저 장</a></div>

<script language="javascript">
	
	window.addEvent("domready",function() {
		makeSelectBox.render($('<%=id%>_form'));
	});
	
</script>
<%} %>	
		