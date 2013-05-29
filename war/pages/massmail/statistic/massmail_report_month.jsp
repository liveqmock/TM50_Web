<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>   
<%@ page import="web.admin.usergroup.model.Group"%>
<%@ page import="web.admin.usergroup.service.UserGroupService"%>
<%@ page import="web.admin.usergroup.control.UserGroupControlHelper"%>
<%@ page import="java.util.List"%> 
<%@page import="web.common.util.LoginInfo"%>
<%
	String id = request.getParameter("id");
	UserGroupService service = UserGroupControlHelper.getUserService(application);
	List<Group> groupList = service.listGroup();
	String isAdmin = LoginInfo.getIsAdmin(request);
%>
<form id="<%=id%>_sform" name="<%=id%>_sform">
<input type="hidden" id="id" name="id" value="<%=id%>" />
<input type="hidden" id="sState" name="sState"/>
<input type="hidden" id="new" name="chartload" value="N"/>
<div class="search_wrapper" style="width:800px;">
	<div>
		<ul id="sYear"  class="selectBox">
			<li id="sYear<%=DateUtils.getYear()+3 %>" data="<%=DateUtils.getYear()+3 %>"><%=DateUtils.getYear()+3 %></li>			
			<li id="sYear<%=DateUtils.getYear()+2 %>" data="<%=DateUtils.getYear()+3 %>"><%=DateUtils.getYear()+2 %></li>
			<li id="sYear<%=DateUtils.getYear()+1 %>" data="<%=DateUtils.getYear()+1 %>"><%=DateUtils.getYear()+1 %></li>
			<li id="sYear<%=DateUtils.getYear()%>" data="<%=DateUtils.getYear() %>" select='Y'><%=DateUtils.getYear() %></li>			
			<li id="sYear<%=DateUtils.getYear()-1 %>" data="<%=DateUtils.getYear()-1 %>"><%=DateUtils.getYear()-1 %></li>
			<li id="sYear<%=DateUtils.getYear()-2 %>" data="<%=DateUtils.getYear()-2 %>"><%=DateUtils.getYear()-2 %></li>
			<li id="sYear<%=DateUtils.getYear()-3 %>" data="<%=DateUtils.getYear()-3 %>"><%=DateUtils.getYear()-3 %></li>
		</ul> 
	</div>
	<div class="text">년</div>
	<div>
		<ul id="sMonth"  class="selectBox">
			<% for(int sMonth=1;sMonth<=12;sMonth++){ %>
				<li id="sMonth<%=sMonth %>" data="<%=sMonth %>" <%if(DateUtils.getMonth() == sMonth){ %> select='Y' <%}%>><%=sMonth %></li>
			<%} %>
		</ul>
	</div>
	<div class="text">월 </div>
	<div>
		<ul id="sDateFrom"  class="selectBox">
			<% for(int sDateFrom=1;sDateFrom<=31;sDateFrom++){ %>
				<li id="sDateFrom<%=sDateFrom %>" data="<%=sDateFrom %>" <%if(1 == sDateFrom){ %> select='Y' <%}%>><%=sDateFrom %></li>
			<%} %>
		</ul>
	</div>
	<div class="text">일 ~ </div>
	<div>
		<ul id="sDateTo"  class="selectBox">
			<% for(int sDateTo=1;sDateTo<=31;sDateTo++){ %>
				<li id="sDateTo<%=sDateTo %>" data="<%=sDateTo %>" <%if(31 == sDateTo){ %> select='Y' <%}%>><%=sDateTo %></li>
			<%} %>
		</ul>
	</div>
	<div class="text">일</div>
	<%if(isAdmin.equals("Y")){ %>
	<div class="text">사용자 검색</div> 
	<div> 
		<a href="javascript:$('<%=id%>').searchUser()"><img style="margin-right:5px" src="images/search_person.gif" title="사용자 검색"/></a><a href="javascript:$('<%=id%>').searchClear()"><img style="margin-right:5px" src="images/search_clear.gif" title="사용자 검색 조건 초기화"/></a>
	</div>
	<%} %>
	<input type="hidden" id="sUserID" name="sUserID" />
	<div <%if(isAdmin.equals("N")){ %> style="display:none"<% }%>>
		<ul id="sGroupID" class="selectBox">
			<li data="">--소속그룹--</li>
			<% for(Group group: groupList) {%>
				<li data="<%=group.getGroupID()%>"><%=group.getGroupName()%></li>
			<%}%>
		</ul>
	</div>
	
	<div>
		<a href="javascript:$('<%=id%>').loadContent()" class="web20button pink">검색</a>	
	</div>
	<br>
	<br>
	<div class="btn_green"><a href="javascript:$('<%=id%>').loadChart('time')" style="cursor:pointer"><span>시간대별 그래프 조회</span></a></div>	
	<div class="btn_b"><a href="javascript:$('<%=id%>').loadChart('domain')" style="cursor:pointer"><span>도메인별 그래프 조회</span></a></div>
	<div class="btn_r"><a href="javascript:$('<%=id%>').chartView()" style="cursor:pointer"><span id="<%=id%>ChartViewBtn">그래프 숨기기</span></a></div>	
	<div class="right">
		<a href="javascript:$('<%=id%>').excelDown()" class="web20button bigblue">Excel</a>
	</div>
	<div id="<%=id%>userNamesWrapper" style="clear:both;width:800px;display:none;color:green">
		통계 적용 대상자 : <span id="<%=id%>userNames" style="color:#548CBA;font-weight:bold">aaa</span>
	</div>
</div>
</form>
<div id="<%=id%>DomainChart" style="padding:8px;display:none;">
</div>
<div id="<%=id%>Content" style="padding:8px;">
</div> 
<form name="<%=id%>_list_form" id="<%=id%>_list_form">
<div id="<%=id%>List" style="padding:8px;" >
	<table class="ctbl" style="width:800px;">
	<tbody>
		<tr>
			<td class="head"><span id="<%=id%>_dash_box_title"></td>
		</tr>
		<tr><td class="ctbl line"></td></tr>
		<tr>
			<td>
				<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="800px">	
				<thead>
					<tr>
						<th style="height:30px;">대량메일명</th>
						<th style="height:30px;width:100px">작성자</th>
						<th style="height:30px;width:100px">발송타입</th>
						<th style="height:30px;width:150px">발송스케줄</th>
						<th style="height:30px;width:100px">총발송건수</th>
						<th style="height:30px;width:50px">상태</th>
						<th style="height:30px;width:50px">통계</th>		
					</tr>
				</thead>
				<tbody id="<%=id%>_grid_content">
				</tbody>
				</TABLE>
				<div id="<%=id%>_paging" class="page_wrapper"></div>	
			</td>
		</tr>
	</tbody>
	</table>
</div>		
</form>


<script type="text/javascript">

//************************************************************
//통계 데이터(리스트) 불러오기
//************************************************************/
$('<%=id%>').list = function(forPage) {
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
	
	page = 'massmail/statistic/massmail.do?method=massmailReportMonthList&curPage='+$('<%=id%>').searchForm.curPage.value;
	//page = 'pages/massmail/statistic/massmail_report_month_proc.jsp?method=list';
	nemoRequest.init({
		url: page	, 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
		}
	});
	
	nemoRequest.post($('<%=id%>_sform'));
}

$('<%=id%>').goPrev = function(y, m) {
	var frm = $('<%=id%>_sform');
	frm.sYear.value = y;
	frm.sMonth.value = m; 
	var sy = document.getElementById("sYear"+y);
	var sm = document.getElementById("sMonth"+m);
	makeSelectBox.select(sy)
	makeSelectBox.select(sm)
	
	$('<%=id%>').loadContent(); 	
}

$('<%=id%>').searchClear = function() {
	$('<%=id%>_sform').sUserID.value = '';
	$('<%=id%>userNames').innerHTML = '';
	$('<%=id%>userNamesWrapper').setStyle('display','none');
	
}

$('<%=id%>').getList = function(title, state) {
	$('<%=id%>_sform').sState.value = state;
	$('<%=id%>_dash_box_title').innerHTML = '<h2>'+title+' 메일 리스트</h2>';
	$('<%=id%>').list();
}

/***********************************************/
/* 사용자 검색 버튼 클릭
/***********************************************/
$('<%=id%>').searchUser = function() {
	window_width = 480;
	
	nemoWindow(
			{
				'id': '<%=id%>_search_user',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: window_width,
				height: $('mainColumn').style.height.toInt(),
				title: '사용자 검색',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/statistic/massmail_report_month_proc.jsp?id=<%=id%>&method=searchuser&selectusers='+$('<%=id%>_sform').sUserID.value
			}
		);
}
/***********************************************/
/* 컨텐츠 로딩
/***********************************************/
$('<%=id%>').loadContent = function() {
	page = 'massmail/statistic/massmail.do?method=massmailReportMonthBasic'
	nemoRequest.init( 
		{
			url: page, 
			update: $('<%=id%>Content'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
				$('<%=id%>_sform').chartload.value = 'N';
				$('<%=id%>_sform').sState.value = '';
				$('<%=id%>_dash_box_title').innerHTML = '<h2>총 발송 메일 리스트</h2>';
				$('<%=id%>DomainChart').setStyle('display','none');
				$('<%=id%>').list();
				
			}
		});
	nemoRequest.post($('<%=id%>_sform'));
}

/***********************************************/
/* 차트 로딩
/***********************************************/
$('<%=id%>').loadChart = function(type) {
	$('<%=id%>DomainChart').setStyle('display','block');
	$('<%=id%>ChartViewBtn').innerHTML = '그래프 숨기기';
	$('<%=id%>_sform').chartload.value = 'Y';
	var frm = $('<%=id%>_sform');
	page = 'pages/massmail/statistic/massmail_report_month_proc.jsp?method=domainstatisticbar&type='+type+'&sDateFrom='+frm.sDateFrom.value+'&sDateTo='+frm.sDateTo.value+'&sUserID='+frm.sUserID.value;
	
	nemoRequest.init( 
		{
			url: page, 
			update: $('<%=id%>DomainChart'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
			
			}
		});
	nemoRequest.post($('<%=id%>_sform'));
}

/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq, seq2, seq3 ) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 900,
			height: $('mainColumn').style.height.toInt(),
			//height: 600,
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'massmail/write/massmail.do?id=<%=id%>_modal&method=edit&massmailID='+seq+'&scheduleID='+seq2+'&state='+seq3
		}
	);
	
}

/***********************************************/
/* 삭제
/***********************************************/
$('<%=id%>').deleteData = function( seq1 ,seq2, seq3 ) {

	if(!confirm("정말로 삭제 하시겠습니까?  \r\n삭제하시면 관련된 모든 데이타는 영구삭제가 됩니다.")) return;

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'massmail/write/massmail.do?method=deleteMassMailAll&id=<%=id%>&eMassmailID='+seq1+'&eScheduleID='+seq2+'&eSendType='+seq3
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {			
			$('<%=id%>').list();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));

}

/***********************************************/
/* 통계 버튼 클릭
/***********************************************/

$('<%=id%>').showStatistic = function( massmailID, scheduleID, pollID ) {

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
				contentURL: 'pages/massmail/statistic/massmail_statistic.jsp?id=<%=id%>Statistic&massmailID='+massmailID+'&scheduleID='+scheduleID+'&pollID='+pollID
			}
	);
	
}

/***********************************************/
/* 차트 보이기 버튼 컨트롤
/***********************************************/
$('<%=id%>').chartView = function() {
	if($('<%=id%>_sform').chartload.value == 'N'){
		alert('조회된 그래프가 없습니다.');
	}else{
		if($('<%=id%>DomainChart').getStyle('display') == 'none'){
			$('<%=id%>DomainChart').setStyle('display','block');
			$('<%=id%>ChartViewBtn').innerHTML = '그래프 숨기기';
		}else{
			$('<%=id%>DomainChart').setStyle('display','none');
			$('<%=id%>ChartViewBtn').innerHTML = '그래프 보이기';
		}
	}
}
//************************************************************
//Excel 다운
//************************************************************/
$('<%=id%>').excelDown = function() {
	var frm = $('<%=id%>_sform');
	location.href = "pages/massmail/statistic/massmail_report_month_excel.jsp?sYear="+frm.sYear.value+"&sMonth="+frm.sMonth.value+"&sDateFrom="+frm.sDateFrom.value+"&sDateTo="+frm.sDateTo.value+"&sUserID="+frm.sUserID.value+"&sState="+frm.sState.value+"&sGroupID="+frm.sGroupID.value;
}

$('<%=id%>').init = function() {
	var frm = $('<%=id%>_sform');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );
	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );

	$('<%=id%>').loadContent();
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
});
</script>