<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %> 
<%

String id = request.getParameter("id");
String method = request.getParameter("method");

String isAdmin = LoginInfo.getIsAdmin(request);
String userAuth = LoginInfo.getUserAuth(request);
String userID = LoginInfo.getUserID(request);
String groupID = LoginInfo.getGroupID(request);

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
	
	
	<c:set var="userID" value="<%=userID %>"/>
	<c:set var="groupID" value="<%=groupID %>"/>
	<c:set var="isAdmin" value="<%=isAdmin %>"/>
	<c:set var="userAuth" value="<%=userAuth %>"/>
	
	<c:forEach items="${masssmsWriteList}" var="masssmsList">
	<c:set var="useCheck" value="N"/>
	<TR id="eMasssms<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" class="tbl_tr" tr_masssmsID="<c:out value="${masssmsList.masssmsID}"/>" tr_scheduleID="<c:out value="${masssmsList.scheduleID}"/>" tr_state="<c:out value="${masssmsList.state}"/>" tr_sendType="<c:out value="${masssmsList.sendType}"/>" tr_userID="<c:out value="${massmailList.userID}"/>" tr_groupID="<c:out value="${masssmsList.groupID}"/>">				
		<TD class="tbl_td" align="center">
		<div id="input<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" input_sendtype="<c:out value="${masssmsList.sendType}"/>" input_userID="<c:out value="${masssmsList.userID}"/>" input_groupID="<c:out value="${masssmsList.groupID}"/>">
			<c:if test="${((masssmsList.sendType =='2' && masssmsList.state == '11') || masssmsList.state == '00' || masssmsList.state == '15' || masssmsList.state == '44' || masssmsList.state == '22' || masssmsList.state == '24' || masssmsList.state == '32' || masssmsList.state == '33' || (masssmsList.sendType !='3' && masssmsList.state == '10' && masssmsList.approveUserID == userID)) && (isAdmin == 'Y' || masssmsList.userID == userID || (userAuth == '2' && masssmsList.groupID == groupID))}" >
				<input type="checkbox" class="notBorder" id="eMassSMSID" name="eMassSMSID" value="<c:out value="${masssmsList.masssmsID}"/>-<c:out value="${masssmsList.scheduleID}"/>-<c:out value="${masssmsList.sendType}"/>" /><c:set var="useCheck" value="Y"/>	
			</c:if>
			<c:if test="${useCheck == 'N'}" >
				<input type="checkbox"  class="notBorder" value=""  disabled/>
			</c:if>
		</div>
		</TD>	
		<TD class="tbl_td"><c:out value="${masssmsList.masssmsID}"/></TD>	
		<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>','<c:out value="${masssmsList.state}"/>')"><c:out value="${masssmsList.masssmsTitle}" escapeXml="true"/></a></b></TD>		
		<TD class="tbl_td"><c:out value="${masssmsList.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${masssmsList.sendTypeDesc}" /></TD>	
		<TD class="tbl_td"><c:out value="${masssmsList.sendScheduleDate}"/></TD>	
		<TD class="tbl_td" align="right">
		<div id="count<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
			<fmt:formatNumber value="${masssmsList.sendCount}" type="number"/>
		</div>
		</TD>
		<TD class="tbl_td">
		
			<a href="javascript:$('<%=id%>').remoteControl('<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>')">
			<div id="masssmsState<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
				<c:if test="${masssmsList.state == '00'}" >
					<img src="images/massmail/save.gif" title="임시저장중"/>
				</c:if>	
				<c:if test="${masssmsList.state == '10'}" >
					<img src="images/massmail/approve.gif" title="승인대기중"/>
				</c:if>		
				<c:if test="${masssmsList.state == '11'}" >
					<img src="images/massmail/ready.gif" title="발송준비대기중"/>
				</c:if>				
				<c:if test="${masssmsList.state == '12'}" >
					<img src="images/massmail/prepareing.gif" title="발송준비중"/>
				</c:if>	
				<c:if test="${masssmsList.state == '13'}" >
					<img src="images/massmail/ready.gif" title="발송준비완료 <br>(발송대기)"/>
				</c:if>	
				<c:if test="${masssmsList.state == '14'}" >
					<img src="images/massmail/sending.gif" title="발송중"/>
				</c:if>		
				<c:if test="${masssmsList.state == '15'}" >
					<img src="images/massmail/finish.gif" title="발송완료"/>
				</c:if>		
				<c:if test="${masssmsList.state == '16'}" >
					<img src="images/massmail/sending.gif" title="오류자재발송중"/>
				</c:if>					
				<c:if test="${masssmsList.state == '22'}" >
					<img src="images/massmail/error.gif" title="발송준비중에러"/>
				</c:if>		
				<c:if test="${masssmsList.state == '24'}" >
					<img src="images/massmail/error.gif" title="발송중에러"/>
				</c:if>	
				<c:if test="${masssmsList.state == '26'}" >
					<img src="images/massmail/error.gif" title="오류자재발송중에러"/>
				</c:if>	
				<c:if test="${masssmsList.state == '32'}" >
					<img src="images/massmail/stop.gif" title="발송준비중정지"/>
				</c:if>		
				<c:if test="${masssmsList.state == '33'}" >
					<img src="images/massmail/stop.gif" title="발송대기중정지"/>
				</c:if>	
				<c:if test="${masssmsList.state == '34'}" >
					<img src="images/massmail/pause.gif" title="발송중일시정지 "/>
				</c:if>	
				<c:if test="${masssmsList.state == '36'}" >
					<img src="images/massmail/pause.gif" title="재발송중일시정지 "/>
				</c:if>	
				<c:if test="${masssmsList.state == '44'}" >
					<img src="images/massmail/stop.gif" title="발송정지 "/>
				</c:if>	
			</div>	
			</a>
			<input type="hidden" id="<%=id %>state<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>state<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" value="<c:out value="${masssmsList.state}"/>" />
			<input type="hidden" id="<%=id %>approveUserID<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>approveUserID<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" value="<c:out value="${masssmsList.approveUserID}"/>" />
			<div class="remoteControl_wrapper">
				<div id="<%=id %>remoteControl<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="remoteControl<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>"class="remoteControl" style="z-index:150;display:none">
					<h6>상태변경 <div class="btnClose"><a href="javascript:$('<%=id%>').remoteControlClose('<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>')"><img src="images/remote_btn_close.gif" title="리모컨 닫기" width="15" height="14"></a></div></h6>
					<div class="remoteBox">
						<!-- 일시정지(상태값 : 34) -->
						<div class="remoteBtn" id="<%=id %>34<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>34<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('34','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">일시정지</a>
						</div>
						<!-- 재발송 일시정지(상태값 : 36) -->
						<div class="remoteBtn" id="<%=id %>36<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>36<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('36','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">일시정지</a>
						</div>
						<!-- 정지 (상태값 : 44)-->
						<div class="remoteBtn" id="<%=id %>44<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>44<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('44','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">발송정지</a>
						</div>
						<!-- 발송 준비중 정지 (상태값 : 32)-->
						<div class="remoteBtn" id="<%=id %>32<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>32<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('32','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">발송정지</a>
						</div>
						<!-- 발송 준비완료  정지 (상태값 : 33)-->
						<div class="remoteBtn" id="<%=id %>33<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>33<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('33','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">발송보류</a>
						</div>
						<!-- 재발송 (상태값 : 14 : 발송중으로) - 일시정지 / 발송중오류시 -->
						<div class="remoteBtn" id="<%=id %>14<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>14<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('14','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">재시작</a>
						</div>
						<!-- 재발송 (상태값 : 16 : 오류자 재 발송중으로) - 일시정지 / 발송중오류시 -->
						<div class="remoteBtn" id="<%=id %>16<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>16<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('16','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">재시작</a>
						</div>
						<!-- 재발송 (상태값 : 11 : 발송준비대기중으로)- 발송준비중 오류 -->
						<div class="remoteBtn" id="<%=id %>11<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>11<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('11','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">재시작</a>
						</div>
						<!-- 재발송 (상태값 : 13 : 발송대기중(발송준비완료)으로)- 발송준비완료 중 정지 -->
						<div class="remoteBtn" id="<%=id %>13<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>13<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('13','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">재시작</a>
						</div>
						<!-- 승인 (상태값 : 11 : 발송준비대기중으로) -->
						<div class="remoteBtn" id="<%=id %>appY<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>appY<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').approveYN('Y','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">승 인</a>
						</div>
						<!-- 반려 (상태값 : 13 : 발송대기중(발송준비완료)으로)- 발송준비완료 중 정지 -->
						<div class="remoteBtn" id="<%=id %>appN<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>appN<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').approveYN('N','<c:out value="${masssmsList.masssmsID}"/>','<c:out value="${masssmsList.scheduleID}"/>')">반 려</a>
						</div>
						<div id="<%=id %>not<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>" name="<%=id %>not<c:out value="${masssmsList.masssmsID}"/>_<c:out value="${masssmsList.scheduleID}"/>">
							변경 할 수  없는<br />
							상태 입니다.
						</div>
						
					</div>
				</div>
			</div>
																												
		</TD>
		<TD class="tbl_td"><a href="javascript:$('<%=id%>').showStatistic('<c:out value="${masssmsList.masssmsID}"/>', '<c:out value="${masssmsList.scheduleID}"/>')"><img src="images/chart_bar.png" title="통계보기 "></TD>
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