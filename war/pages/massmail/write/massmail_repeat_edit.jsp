<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%@page import="web.common.util.LoginInfo"%>

<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	String massmailID = request.getParameter("massmailID");
	String isAdmin = LoginInfo.getIsAdmin(request);
	String userID =LoginInfo.getUserID(request);


//****************************************************************************************************/
//반복메일 스케줄 
//****************************************************************************************************/
if(method.equals("editRepeat")) {
	String preID = request.getParameter("preID");
%>

<jsp:useBean id="massmailInfo" class="web.massmail.write.model.MassMailInfo" scope="request" />
<div style="float:left" >
		<form id="<%=id%>_form" name="<%=id%>_form">
		
			<table class="ctbl" width="560px">
			<tbody>
				<tr>
					<td class="ctbl ttd1" width="100px" align="left">대량메일명</td>
					<td class="ctbl td"><c:out value="${massmailInfo.massmailTitle}"/></td>
				</tr>
				<tr>
					<td colspan="10" class="ctbl line"></td>
				</tr>
				<tr>
					<td class="ctbl ttd1" width="100px" align="left">반복발송타입</td>
					<td class="ctbl td"><%=ThunderUtil.descRepeatSendType(massmailInfo.getRepeatSendType()) %></td>
				</tr>
				<c:if test="${massmailInfo.repeatSendType=='2'}">
				<tr>
					<td colspan="10" class="ctbl line"></td>
				</tr>	
				<tr>
					<td class="ctbl ttd1" width="100px">매주반복</td>
					<td class="ctbl td">
					<input type="checkbox" class="notBorder" disabled <c:if test="${massmailInfo.repeatSendWeekSun=='1'}">checked</c:if> value="1" />일&nbsp;&nbsp;
					<input type="checkbox" class="notBorder" disabled <c:if test="${massmailInfo.repeatSendWeekMon=='2'}">checked</c:if> value="2" />월&nbsp;&nbsp;
					<input type="checkbox" class="notBorder" disabled <c:if test="${massmailInfo.repeatSendWeekTue=='3'}">checked</c:if> value="3" />화&nbsp;&nbsp;
					<input type="checkbox" class="notBorder" disabled <c:if test="${massmailInfo.repeatSendWeekWed=='4'}">checked</c:if> value="4" />수&nbsp;&nbsp;
					<input type="checkbox" class="notBorder" disabled <c:if test="${massmailInfo.repeatSendWeekThu=='5'}">checked</c:if> value="5" />목&nbsp;&nbsp;
					<input type="checkbox" class="notBorder" disabled <c:if test="${massmailInfo.repeatSendWeekFri=='6'}">checked</c:if> value="6" />금&nbsp;&nbsp;
					<input type="checkbox" class="notBorder" disabled <c:if test="${massmailInfo.repeatSendWeekSat=='7'}">checked</c:if> value="7" />토&nbsp;&nbsp;
					</td>
				</tr>			
				</c:if>		
				<c:if test="${massmailInfo.repeatSendType=='3'}">
				<tr>
					<td colspan="10" class="ctbl line"></td>
				</tr>	
				<tr>
					<td class="ctbl ttd1" width="100px">매월반복</td>
					<td class="ctbl td">
					<input type="text" readonly  size="3" value="<c:out value="${massmailInfo.repeatSendDay}"/>" />일&nbsp;&nbsp;
					</td>
				</tr>			
				</c:if>							
				<tr>
					<td colspan="10" class="ctbl line"></td>
				</tr>		
				<tr>
					<td class="ctbl ttd1" width="100px" align="left">반복발송기간</td>
					<td class="ctbl td"><c:out value="${massmailInfo.repeatSendStartDate}"/> ~ <c:out value="${massmailInfo.repeatSendEndDate}"/></td>					
				</tr>
							
			</tbody>
			</table>
		</form>
</div>



<div style="float:left">
<form id="<%=id%>_sform" name="<%=id%>_sform">
<input type="hidden" name="eMassmailID" value="<c:out value="${massmailInfo.massmailID}"/>" />	
	<table border="0" cellpadding="3" width="560px">
		<tbody>
		<tr>			
			<td>	
			<div style="float:left;margin-top:2px;margin-right:5px">발송기간</div>		
			<div style="float:left;margin-top:2px">						
				<input type="text" id="eSendScheduleDateStart" name="eSendScheduleDateStart" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getYearMonthDayStr(massmailInfo.getRepeatSendStartDate()) %>"/>
				<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eSendScheduleDateStart,$('<%=id%>_sform').eSendScheduleDateEnd,'start')" align="absmiddle" />
			</div>
			<div style="float:left;margin-left:5px;margin-top:3px">~&nbsp;&nbsp;&nbsp;</div>	
			<div style="float:left;margin-top:2px">						
				<input type="text" id="eSendScheduleDateEnd" name="eSendScheduleDateEnd" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getYearMonthDayStr(massmailInfo.getRepeatSendEndDate()) %>"/>
				<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eSendScheduleDateEnd,$('<%=id%>_sform').eSendScheduleDateStart,'end')" align="absmiddle" />
			</div>						
			<div style="float:left;margin-left:10px">	
				<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
			</div>
			<%if(isAdmin.equals("Y") || massmailInfo.getUserID().equals(userID)){ %>
				<div style="float:right;margin-left:10px">	
				<a href="javascript:$('<%=id%>').deleteScheduleCheck()" class="web20button pink" title="체크된 데이타만 삭제합니다.(단, 발송준비대기중인  스케줄만 삭제 가능)">체크삭제</a>
				</div>			
				<div style="float:right;margin-left:10px">	
				<a href="javascript:$('<%=id%>').deleteScheduleDate()" class="web20button blue" title="발송기간내에 검색된 모든 데이타를 삭제합니다.(단, 발송준비대기중인  스케줄만 삭제 가능)">기간삭제</a>
				</div>
			<%} %>
			</td>
		</tr>
		</tbody>
	</table>
</form>	
</div>

<div style="float:left">
		<form name="<%=id%>_list_form" id="<%=id%>_list_form">
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="560px" >
		<thead>
			<tr>
			<th style="height:30px;width:40px"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder" onclick="selectAll($('<%=id%>_list_form').eScheduleID,this.checked)"/></th>		
			<th style="height:30px;width:200px">반복발송일</th>
			<th style="height:30px;width:200px">실제발송일</th>
			<th style="height:30px;width:60px">상태</th>
			<th style="height:30px;width:60px">통계</th>		
			</tr>
		</thead>
		<tbody id="<%=id%>_grid_content">
		
		</tbody>
		</table>
		</form>
		<br>
</div>

<div id="<%=id%>_paging" class="page_wrapper"></div>
<script type="text/javascript">
/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_list_form');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );

	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );

	$('<%=id%>').list();

}

/***********************************************/
/* 체크된 내역삭제 
/***********************************************/
$('<%=id%>').deleteScheduleCheck = function(){
	var frm = $('<%=id%>_list_form');

	var checked = isChecked( frm.elements['eScheduleID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'massmail/write/massmail.do?method=deleteRepeatScheduleByChecked&id=<%=id%>'
		//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			
			refreshWindow('<%=id%>');
			$('<%=preID%>').list(); 
			
		}
	});
	nemoRequest.post(frm);
}


/***********************************************/
/* 검색기간 내역삭제 
/***********************************************/
$('<%=id%>').deleteScheduleDate = function(){
	var frm = $('<%=id%>_sform');

	var fromDate = frm.eSendScheduleDateStart.value;
	var toDate = frm.eSendScheduleDateEnd.value;

	if(!confirm("[ "+ fromDate + " ~ " + toDate + " ] 사이에 발송데이타를 모두 완전 삭제하시겠습니까?")){
		return;
	}

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'massmail/write/massmail.do?method=deleteRepeatScheduleByDate&id=<%=id%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			
		
			
			refreshWindow('<%=id%>');
			$('<%=preID%>').list(); 
			
		}
	});
	nemoRequest.post(frm);
}

/***********************************************/
/* 통계 버튼 클릭
/***********************************************/

$('<%=id%>').showStatistic = function( massmailID, scheduleID ) {

	nemoWindow(
			{
				'id': '<%=id%>Statistic',
				busyEl: '<%=id%>Statistic', // 창을 열기까지 busy 가 표시될 element
				width: 900,
				height: $('mainColumn').style.height.toInt(),
				title: '대량메일 통계 조회',
				type: 'modal',
				loadMethod: 'xhr',
				padding: { top: 0, right: 0, bottom: 0, left: 0 },
				contentURL: 'pages/massmail/statistic/massmail_statistic.jsp?id=<%=id%>Statistic&massmailID='+massmailID+'&scheduleID='+scheduleID
			}
	);
	
}

/***********************************************/
/* 반복메일 리스트 
/***********************************************/

$('<%=id%>').list = function (forPage) {

	var frm = $('<%=id%>_sform');

	//페이징 클릭에서 리스트 표시는 기존 검색을 따른다
	if(!forPage) {
		//검색 조건 체크
		if(!checkFormValue(frm)) {
			return;
		}
		// 검색 값을 새로운 폼값(검색후 픽스될) 에 담는다.
		cloneForm($('<%=id%>_sform'), '<%=id%>_rform', '<%=id%>',   $('<%=id%>_grid_content'));
	}


	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'massmail/write/massmail.do?method=listRepeatSchedule&id=<%=id%>&massmailID=<%=massmailID%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});

	nemoRequest.post($('<%=id%>_rform'));
}




/* 리스트 표시 */
window.addEvent("domready",function (){
	
	$('<%=id%>').init(); 
	var frm = $('<%=id%>_sform');
	if(frm.eMassmailID.value == '0'){
		//closeWindow( $('<%=id%>') );
		$('<%=preID%>').list(); 
	}
});


</script>


<%
}
//****************************************************************************************************/
// 탬플릿리스트
//****************************************************************************************************/
if(method.equals("listRepeatSchedule")) {
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
	
	
	<c:forEach items="${massmailRepeatSchedule}" var="massmailList">	
	<TR class="tbl_tr" attachedList_id="<c:out value="${massmailList.scheduleID}"/>">	
		
		<TD class="tbl_td" align="center">
			<input type="hidden" name="eMassmailID" value="<c:out value="${massmailList.massmailID}"/>" />			
			<input type="checkbox" id="eScheduleID" name="eScheduleID" value="<c:out value="${massmailList.scheduleID}" />" />
		</TD>	
		<TD class="tbl_td"><c:out value="${massmailList.sendScheduleDate}"/></TD>
		<TD class="tbl_td"><c:out value="${massmailList.sendStartTime}"/></TD>		
		<TD class="tbl_td" align="center">
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
				<img src="images/massmail/ready.gif" title="발송준비중"/>
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
				<img src="images/massmail/pause.gif" title="발송일시정지 "/>
			</c:if>	
			<c:if test="${massmailList.state == '44'}" >
				<img src="images/massmail/stop.gif" title="발송정지 "/>
			</c:if>
		</TD>
		<TD class="tbl_td" align="center">
			<a href="javascript:$('<%=id%>').showStatistic(<c:out value="${massmailList.massmailID}"/>, <c:out value="${massmailList.scheduleID}"/>)"><img src="images/chart_bar.png" title="통계보기 ">
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
			<%if(iTotalCnt.equals("0")){%>
				closeWindow( $('<%=id%>') );	
			<%}%>
		});

	</script>
<%
}
%>