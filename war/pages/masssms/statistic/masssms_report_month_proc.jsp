<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>  
<%@ page import="web.admin.usergroup.model.User"%>
<%@ page import="java.util.List"%>

<%@ page import="web.admin.usergroup.service.UserGroupService"%>
<%@ page import="web.admin.usergroup.control.UserGroupControlHelper"%>
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	if(method.equals("basicinfo")) {
		
%>
	<jsp:useBean id="year" class="java.lang.String" scope="request" />
	<jsp:useBean id="month" class="java.lang.String" scope="request" />
	<jsp:useBean id="reportMonthSendInfo" class="web.masssms.statistic.model.MassSMSReportMonth" scope="request" />
    <jsp:useBean id="reportMonthDomainSendList" class="java.lang.Object" scope="request" />
	<div style="clear:both;width:800px;">
	<form name="<%=id%>_info_form" id="<%=id%>_info_form">
	<table width="100%">
		<tr>
			<td align="center">
<%
	//앞 뒤로 달 이동을 위한 연산
	String prevYear = null;
	String prevMonth = null;
	String nextYear = null;
	String nextMonth = null;
	
	if (Integer.parseInt(month) == 1)
	{
		prevYear = String.valueOf(Integer.parseInt(year) - 1);
		prevMonth = "12";
	}
	else
	{
		String tempMonth = "0" + (Integer.parseInt(month) - 1);
		if (tempMonth.length() == 3) tempMonth = tempMonth.substring(1);
		prevYear = year;
		prevMonth = tempMonth;
	}
	
	
	if (Integer.parseInt(month) == 12)
	{
		nextYear = String.valueOf(Integer.parseInt(year) + 1);
		nextMonth = "01";
	}
	else
	{
		String tempMonth = "0" + (Integer.parseInt(month) + 1);
		if (tempMonth.length() == 3) tempMonth = tempMonth.substring(1);
		nextYear = year;
		nextMonth = tempMonth;
	}
%>
				<a href="javascript:$('<%=id%>').goPrev(<%=prevYear %>,<%=prevMonth %>)" ><img src="images/arrow-left.gif" /> 이전 </a>
				<%=year %>년  <%=month %> 월 
				<a href="javascript:$('<%=id%>').goPrev(<%=nextYear %>,<%=nextMonth %>)" > 다음  <img src="images/arrow-right.gif" /></a>
			</td>
		</tr>
	</table>
	<div id="<%=id%>_infoDiv" >
		<table class="ctbl" width="100%">
		<tbody>
			<tr><td class="head" >총괄현황</td></tr>	
			<tr>
				<td>
					<table class="ctbl" width="100%">
					<tbody>
						<tr>
							<td class="ctbl ttd1" width="100px">총 대량SMS 수 </td>
							<td class="ctbl td" align="center"><a href="javascript:$('<%=id%>').getList('총 발송','')"><fmt:formatNumber value="${reportMonthTotalInfo.totalCount}"/> 개</a></td>
							<td class="ctbl ttd1" width="100px">임시 저장</td>
							<td class="ctbl td" align="center"><a href="javascript:$('<%=id%>').getList('임시 저장','00')"><fmt:formatNumber value="${reportMonthTotalInfo.writeCount}"/> 개</a></td>
							<td class="ctbl ttd1" width="100px">승인 대기</td>
							<td class="ctbl td" align="center"><a href="javascript:$('<%=id%>').getList('승인 대기','10')"><fmt:formatNumber value="${reportMonthTotalInfo.appReadyCount}"/> 개</a></td>
							<td class="ctbl ttd1" width="100px">발송 대기 </td>
							<td class="ctbl td" align="center"><a href="javascript:$('<%=id%>').getList('발송 대기','11,12,13')"><fmt:formatNumber value="${reportMonthTotalInfo.readyCount}"/> 개</a></td>
						</tr>
						<tr><td colspan="8" class="ctbl line"></td></tr>
						<tr>
							<td class="ctbl ttd1" width="100px">발송 중</td>
							<td class="ctbl td" align="center"><a href="javascript:$('<%=id%>').getList('발송 중','14,16')"><fmt:formatNumber value="${reportMonthTotalInfo.sendingCount}" type="number"/> 개</a></td>
							<td class="ctbl ttd1" width="100px">발송 완료</td>
							<td class="ctbl td" align="center"><a href="javascript:$('<%=id%>').getList('발송 완료','15')"><fmt:formatNumber value="${reportMonthTotalInfo.sendFinishCount}" type="number"/> 개</a></td>
							<td class="ctbl ttd1" width="100px">발송 중지 </td>
							<td class="ctbl td" align="center"><a href="javascript:$('<%=id%>').getList('발송 중지','32,34,44')"><fmt:formatNumber value="${reportMonthTotalInfo.sendingStopCount}" type="number"/> 개</a></td>
							<td class="ctbl ttd1" width="100px">발송 에러</td>
							<td class="ctbl td" align="center"><a href="javascript:$('<%=id%>').getList('발송 에러','22,24,26')"><fmt:formatNumber value="${reportMonthTotalInfo.errCount}" type="number"/> 개</a></td>
						</tr>
					</tbody>
					</table>
				</td>
			</tr>
		</tbody>
		</table>
		<hr/>
		<table class="ctbl" width="100%">
		<tbody>
			<tr>
				<td class="head">SMS 발송 현황</td>
			</tr>
			<tr>
				<td>
					<table class="ctbl" width="100%">
					<tbody>
						<tr>
							<td class="ctbl ttd1" width="100px">총 발송 통수 </td>
							<td class="ctbl td" align="center"><fmt:formatNumber value="${reportMonthSendInfo.sendTotal}" type="number"/> 통</td>
							<td class="ctbl ttd1" width="100px">성공 통수</td>
							<td class="ctbl td" align="center"><fmt:formatNumber value="${reportMonthSendInfo.successTotal}" type="number"/> 통</td>
							<td class="ctbl ttd1" width="100px">실패 통수</td>
							<td class="ctbl td" align="center"><fmt:formatNumber value="${reportMonthSendInfo.failTotal}" type="number"/> 통</td>
							<td class="ctbl ttd1" width="100px">대기 통수 </td>
							<td class="ctbl td" align="center"><fmt:formatNumber value="${reportMonthSendInfo.readyTotal}" type="number"/> 통</td>							
						</tr>						
					</tbody>
					</table>
				</td>
			</tr>					
		</tbody>
		</table>
	</div>
	</form>
	</div>
	<script language="javascript">
	//window.addEvent('domready',function() {
	//	roundTables($('<%=id%>'));
	//	renderTableHeader($('<%=id%>_infoDiv'));
	//});	
	</script>
<%}else if(method.equals("list")){%>
<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
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
	<c:forEach items="${masssmsWriteList}" var="masssmsList">
	<TR class="tbl_tr" tr_masssmsID="<c:out value="${masssmsList.masssmsID}"/>" tr_scheduleID="<c:out value="${masssmsList.scheduleID}"/>" tr_state="<c:out value="${masssmsList.state}"/>" tr_sendType="<c:out value="${masssmsList.sendType}"/>">				
		<TD class="tbl_td" align="left"><b><c:out value="${masssmsList.masssmsTitle}" escapeXml="true"/></b></TD>		
		<TD class="tbl_td"><c:out value="${masssmsList.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${masssmsList.sendTypeDesc}" /></TD>	
		<TD class="tbl_td"><c:out value="${masssmsList.sendScheduleDate}"/></TD>	
		<TD class="tbl_td" align="right"><fmt:formatNumber value="${masssmsList.sendCount}" type="number"/></TD>
		<TD class="tbl_td">
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
				<img src="images/massmail/ready.gif" title="발송준비완료"/>
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
			<c:if test="${masssmsList.state == '34'}" >
				<img src="images/massmail/pause.gif" title="발송중일시정지 "/>
			</c:if>
			<c:if test="${masssmsList.state == '36'}" >
				<img src="images/massmail/pause.gif" title="재발송중일시정지 "/>
			</c:if>		
			<c:if test="${masssmsList.state == '44'}" >
				<img src="images/massmail/stop.gif" title="발송정지 "/>
			</c:if>																																	
		</TD>
		<TD class="tbl_td"><a href="javascript:$('<%=id%>').showStatistic(<c:out value="${masssmsList.masssmsID}"/>, <c:out value="${masssmsList.scheduleID}"/>)"><img src="images/chart_bar.png" title="통계보기 "></TD>
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
			});
			$('<%=id%>').grid_content.render();
		});

	</script>
<%}else if(method.equals("searchuser")){
	String userID =  LoginInfo.getUserID(request);
	UserGroupService service = UserGroupControlHelper.getUserService(application);
	User login_user = service.viewUser(userID);
	List<User> userList = service.listSearchUser(login_user.getUserLevel(), login_user.getGroupID());
	String selectUsers = request.getParameter("selectusers")+",";
	selectUsers = ","+selectUsers.replace(" ","");
%>
<form name="<%=id%>_user_list_form" id="<%=id%>_user_list_form">
<div class="search_wrapper" style="width:97%">
	<div></div>
	<div class="right">	
		<a href="javascript:closeWindow($('<%=id%>_search_user'))"  class="web20button bigpink">닫기</a>
	</div>	
	<div class="right">	
		<a href="javascript:$('<%=id%>').inputUserinfo()"  class="web20button bigblue">선택추가</a>
	</div>
</div>
<div style="clear:both;width:97%">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" align="center">	
				<thead>
					<tr>
						<th id="<%=id%>_th1" style="height:30px;width:30px;"><input id="sCheckAll" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_user_list_form').eUserInfo,this.checked)"/></th>
						<th id="<%=id%>_th1" style="height:30px;">소속그룹</th>
						<th id="<%=id%>_th2" style="height:30px;width:100px;">사용자ID</th>
						<th id="<%=id%>_th3" style="height:30px;width:150px;">사용자명</th>
					</tr>
				</thead>
			<tbody id="<%=id%>_search_user_grid_content">
			
			<%for(User user : userList){%>
				<TR class="tbl_tr" >
					<TD class="tbl_td" align="center"><input type="checkbox" id="eUserInfo" name="eUserInfo" value="<%=user.getUserID() %>" <%if(selectUsers.indexOf(","+user.getUserID()+",") > -1) {%>checked<%} %>/></TD>
					<TD class="tbl_td" align="center"><%=user.getGroupName()%></TD>		
					<TD class="tbl_td" align="center"><%=user.getUserID() %></TD>	
					<TD class="tbl_td" align="center"><%=user.getUserName() %>
					<input type="hidden" id="sUserID" name="sUserID" value="<%=user.getUserID() %>">
					<input type="hidden" id="sUserNm" name="sUserNM" value="<%=user.getUserName() %>">
					</TD>		
				</TR>
			<%} %>
			</tbody>
	</TABLE>
</div>
</form>

	<script type="text/javascript">	

		$('<%=id%>').inputUserinfo = function (){
			var frm = $('<%=id%>_user_list_form');
			var checked = isChecked( frm.elements['eUserInfo']  );

			if( checked == 0 ) {
				toolTip.showTipAtControl(frm.sCheckAll,'해당 자료를 먼저 선택하세요');
				return;
			}
			var userIds = "";
			var userNms = "";
			var j = 0;
			for(var i=0;i <frm.elements['eUserInfo'].length;i++){
				if( frm.elements['eUserInfo'][i].checked == true){
					if(j == 0){
						userIds = frm.sUserID[i].value;
						userNms = frm.sUserNM[i].value;
					}else{
						userIds += ","+frm.sUserID[i].value;
						userNms += ","+frm.sUserNM[i].value;
					}
					j++;
				}
			}
			$('<%=id%>_sform').sUserID.value = userIds;
			$('<%=id%>userNamesWrapper').setStyle('display','block');
			$('<%=id%>userNames').innerHTML = userNms;
			closeWindow($('<%=id%>_search_user'));
		}
		/***********************************************/
		/* 검색조건 설정
		/***********************************************/
		window.addEvent('domready', function(){
		
			// 테이블 렌더링
			$('<%=id%>_search_user').grid_content = new renderTable({
				element: $('<%=id%>_search_user_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
			});
			$('<%=id%>_search_user').grid_content.render();
		});
	</script>	
<%}%>