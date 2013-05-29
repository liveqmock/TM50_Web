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
	<jsp:useBean id="reportMonthSendInfo" class="web.massmail.statistic.model.MassMailReportMonth" scope="request" />
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
							<td class="ctbl ttd1" width="100px">총 대량메일 수 </td>
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
				<td class="head">E-Mail 발송 현황</td>
			</tr>
			<tr>
				<td>
					<table class="ctbl" width="100%">
					<tbody>
						<tr>
							<td class="ctbl ttd1" width="120px">① 총 발송 통수 </td>
							<td class="ctbl td" align="center"><fmt:formatNumber value="${reportMonthSendInfo.sendTotal}" type="number"/> 통</td>
							<td class="ctbl ttd1" width="120px">② 성공 통수(②/①)</td>
							<td class="ctbl td" align="center">
								<c:set var="successratio" value="0"/>
								<c:if test="${reportMonthSendInfo.successTotal > 0}">
									<c:set var="successratio" value="${reportMonthSendInfo.successTotal / reportMonthSendInfo.sendTotal * 100}" />	
								</c:if>
								<fmt:formatNumber value="${reportMonthSendInfo.successTotal}" type="number"/> 통 (<fmt:formatNumber value="${successratio}" pattern="0.00"/> %)
							</td>
							<td class="ctbl ttd1" width="130px">③ 실패 통수(③/①)</td>
							<td class="ctbl td" align="center">
								<c:set var="failratio" value="0"/>
								<c:if test="${reportMonthSendInfo.failTotal > 0}">
									<c:set var="failratio" value="${reportMonthSendInfo.failTotal / reportMonthSendInfo.sendTotal * 100}"/>	
								</c:if>
								<fmt:formatNumber value="${reportMonthSendInfo.failTotal}" type="number"/> 통 (<fmt:formatNumber value="${failratio}" pattern="0.00"/> %)
							</td>
						</tr>
						<tr><td colspan="8" class="ctbl line"></td></tr>
						<tr>
							<td class="ctbl ttd1" width="120px">④오픈 통수(④/②)</td>
							<td class="ctbl td" align="center">
								<c:set var="openratio" value="0"/>
								<c:if test="${reportMonthSendInfo.openTotal > 0}">
									<c:set var="openratio" value="${reportMonthSendInfo.openTotal / reportMonthSendInfo.successTotal * 100}"/>	
								</c:if>
								<fmt:formatNumber value="${reportMonthSendInfo.openTotal}" type="number"/> (<fmt:formatNumber value="${openratio}" pattern="0.00"/> %)통
							</td>
							<td class="ctbl ttd1" width="120px">⑤클릭 통수(⑤/④)</td>
							<td class="ctbl td" align="center">
								<c:set var="clickratio" value="0"/>
									<c:if test="${reportMonthSendInfo.clickTotal > 0}">
									<c:set var="clickratio" value="${reportMonthSendInfo.clickTotal / reportMonthSendInfo.openTotal * 100}"/>	
								</c:if>
								<fmt:formatNumber value="${reportMonthSendInfo.clickTotal}" type="number"/> 통 (<fmt:formatNumber value="${clickratio}" pattern="0.00"/> %)
							</td>
							<td class="ctbl ttd1" width="130px">⑥수신 거부 통수(⑥/①)</td>
							<td class="ctbl td" align="center">
								<c:set var="rejectcallratio" value="0"/>
								<c:if test="${reportMonthSendInfo.rejectcallTotal > 0}">
									<c:set var="rejectcallratio" value="${reportMonthSendInfo.rejectcallTotal/ reportMonthSendInfo.sendTotal * 100}"/>	
								</c:if>
								<fmt:formatNumber value="${reportMonthSendInfo.rejectcallTotal}" type="number"/> 통 (<fmt:formatNumber value="${rejectcallratio}" pattern="0.00"/> %)
							</td>
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
	<jsp:useBean id="dateTo" class="java.lang.String" scope="request" />
	<jsp:useBean id="dateFrom" class="java.lang.String" scope="request" />
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<c:forEach items="${massmailWriteList}" var="massmailList">
	<TR class="tbl_tr" tr_massmailID="<c:out value="${massmailList.massmailID}"/>" tr_scheduleID="<c:out value="${massmailList.scheduleID}"/>" tr_state="<c:out value="${massmailList.state}"/>" tr_sendType="<c:out value="${massmailList.sendType}"/>">				
		<TD class="tbl_td" align="left"><b><c:out value="${massmailList.massmailTitle}" escapeXml="true"/></b></TD>		
		<TD class="tbl_td"><c:out value="${massmailList.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${massmailList.sendTypeDesc}" /></TD>	
		<TD class="tbl_td"><c:out value="${massmailList.sendScheduleDate}"/></TD>	
		<TD class="tbl_td" align="right"><fmt:formatNumber value="${massmailList.sendCount}" type="number"/></TD>
		<TD class="tbl_td">
			<c:if test="${massmailList.state == '00'}" >
				<img src="images/massmail/save.gif" title="임시저장중"/>
			</c:if>	
			<c:if test="${massmailList.state == '10'}" >
				<img src="images/massmail/approve.gif" title="승인대기중"/>
			</c:if>		
			<c:if test="${massmailList.state == '11'}" >
				<img src="images/massmail/ready.gif" title="발송준비대기중"/>
			</c:if>				
			<c:if test="${massmailList.state == '12'}" >
				<img src="images/massmail/prepareing.gif" title="발송준비중"/>
			</c:if>	
			<c:if test="${massmailList.state == '13'}" >
				<img src="images/massmail/ready.gif" title="발송준비완료"/>
			</c:if>	
			<c:if test="${massmailList.state == '14'}" >
				<img src="images/massmail/sending.gif" title="발송중"/>
			</c:if>		
			<c:if test="${massmailList.state == '15'}" >
				<img src="images/massmail/finish.gif" title="발송완료"/>
			</c:if>		
			<c:if test="${massmailList.state == '16'}" >
				<img src="images/massmail/sending.gif" title="오류자재발송중"/>
			</c:if>					
			<c:if test="${massmailList.state == '22'}" >
				<img src="images/massmail/error.gif" title="발송준비중에러"/>
			</c:if>		
			<c:if test="${massmailList.state == '24'}" >
				<img src="images/massmail/error.gif" title="발송중에러"/>
			</c:if>	
			<c:if test="${massmailList.state == '26'}" >
				<img src="images/massmail/error.gif" title="오류자재발송중에러"/>
			</c:if>	
			<c:if test="${massmailList.state == '32'}" >
				<img src="images/massmail/stop.gif" title="발송준비중정지"/>
			</c:if>		
			<c:if test="${massmailList.state == '34'}" >
				<img src="images/massmail/pause.gif" title="발송중일시정지 "/>
			</c:if>
			<c:if test="${massmailList.state == '36'}" >
				<img src="images/massmail/pause.gif" title="재발송중일시정지 "/>
			</c:if>		
			<c:if test="${massmailList.state == '44'}" >
				<img src="images/massmail/stop.gif" title="발송정지 "/>
			</c:if>																																	
		</TD>
		<TD class="tbl_td"><a href="javascript:$('<%=id%>').showStatistic(<c:out value="${massmailList.massmailID}"/>, <c:out value="${massmailList.scheduleID}"/>, <c:out value="${massmailList.pollID}"/>)"><img src="images/chart_bar.png" title="통계보기 "></TD>
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

		<% for(int i=1;i<=31;i++){ %>
			document.getElementById("sDateTo<%=i %>").setAttribute("select","N");
			document.getElementById("sDateFrom<%=i %>").setAttribute("select","N");
		<%}%>
		var st = document.getElementById("sDateTo<%=dateTo %>");
		var sf = document.getElementById("sDateFrom<%=dateFrom %>");
		makeSelectBox.select(st)
		makeSelectBox.select(sf)

	</script>
<%}else if(method.equals("domainstatisticbar")){
	String year = request.getParameter("sYear");
	String month = request.getParameter("sMonth");
	String dateFrom = request.getParameter("sDateFrom");
	String dateTo = request.getParameter("sDateTo");
	String type = request.getParameter("type");
	String sUserID = request.getParameter("sUserID");
	String methodvalue = "";
	//if(type.equals("domain")){
	//	methodvalue = "massmailReportMonthDomainStatisticBar";
	//}else{
	//	methodvalue = "massmailReportMonthTimeStatisticBar";
	//}
%>
	<div style="clear:both;width:800px">
	<form name="<%=id%>_info_form" id="<%=id%>_info_form">
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<%if(type.equals("domain")){ %>
			<h2>도메인 별 E-Mail 발송 현황 </h2>
		<%}else{ %>
			<h2>시간대 별 E-Mail 발송 현황</h2>
		<%} %>
		<%if(type.equals("domain")){ %>
			* 발송 통수 기준으로 상위 10개도메인의 현황을 보여줍니다.
		<%} %>
		<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/month_chart_iframe.jsp?type=<%=type%>&sYear=<%=year%>&sMonth=<%=month%>&sDateFrom=<%=dateFrom%>&sDateTo=<%=dateTo%>&sUserID=<%=sUserID%>" width="800px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
	</div>
    </form>
    </div>
	<script type="text/javascript">
	
	/***********************************************/
	/* 검색 조건 컨트롤 초기화
	/***********************************************/
	//<div class="dash_box_chart_content">
		//	<div id="<%//=id%>Chart" ></div>
		//</div>  
		/* 리스트 표시 */
		//window.addEvent("domready",function () {
		//	createSwfobject(
		//			  'swf/open-flash-chart.swf', '<%=id%>Chart',
		//			  '100%', '400px','9.0.0', 'swf/expressInstall.swf',
		//			  {'data-file': escape('massmail/statistic/massmail.do?method=<%=methodvalue%>&sYear=<%=year%>&sMonth=<%=month%>&sUserID=<%=sUserID%>')});
		//});
	
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