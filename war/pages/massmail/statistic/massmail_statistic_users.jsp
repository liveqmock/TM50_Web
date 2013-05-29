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
<div class="search_wrapper" style="width:870px">
	<div class="start">
		<ul id="sSendedYear"  class="selectBox">
			<li data="<%=DateUtils.getYear() %>"><%=DateUtils.getYear() %></li>			
			<li data="<%=DateUtils.getYear()-1 %>"><%=DateUtils.getYear()-1 %></li>
			<li data="<%=DateUtils.getYear()-2 %>"><%=DateUtils.getYear()-2 %></li>
		</ul>
	</div>
	<div class="text">년</div>
	<div>
		<ul id="sSendedMonth"  class="selectBox">
			<% for(int month=1;month<=12;month++){ %>
				<li data="<%=month %>" <%if(month==DateUtils.getMonth()){ %> select='Y' <%}%>><%=month %></li>
			<%} %>
		</ul>
	</div>
	<div class="text">월</div>
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
			<% for(Group group: groupList) { %>
				<li data="<%=group.getGroupID()%>"><%=group.getGroupName()%></li>
			<%}%>
		</ul>
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="btn_green"><a href="javascript:$('<%=id%>').loadContent('massmailStatisticUsersBar','send')" style="cursor:pointer"><span>막대 그래프 조회</span></a></div>	
	<div class="btn_b"><a href="javascript:$('<%=id%>').loadContent('massmailStatisticUsersPie','send')" style="cursor:pointer"><span>원 그래프 조회</span></a></div>
	<div class="btn_r"><a href="javascript:$('<%=id%>').chartView()" style="cursor:pointer"><span id="<%=id%>ChartViewBtn">그래프 숨기기</span></a></div>
	<div class="right">
		<a href="javascript:$('<%=id%>').excelDown()" class="web20button bigblue">Excel</a>
	</div>	
	<div id="<%=id%>userNamesWrapper" style="clear:both;width:800px;display:none;color:green">
		통계 적용 대상자 : <span id="<%=id%>userNames" style="color:#548CBA;font-weight:bold"></span>
	</div>
</div>	
</form>

<div id="<%=id%>Content" style="clear:both;padding:8px;">
</div>
<div style="clear:both;width:870px">
<form name="<%=id%>_list_form" id="<%=id%>_list_form">
<div id="<%=id%>List" >
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="870px" >	
		<thead>
		<tr>
			<th style="height:30px;">계정</th>
			<th style="height:30px;width:100px">전체 통수 </th>
			<th style="height:30px;width:100px">성공통수 </th>
			<th style="height:30px;width:100px">실패통수 </th>
			<th style="height:30px;width:100px">오픈통수</th>
			<th style="height:30px;width:100px">클릭통수</th>
			<th style="height:30px;width:100px">수신거부통수</th>
		</tr>
		</thead>
		<tbody id="<%=id%>_grid_content">
		</tbody>
		</TABLE>
</div>
</form>
</div>
<script type="text/javascript">
/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_sform');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );

	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );

}

/***********************************************/
/* 리스트 
/***********************************************/
$('<%=id%>').list = function () {	
	var frm = $('<%=id%>_sform');

	//검색 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}
	// 검색 값을 새로운 폼값(검색후 픽스될) 에 담는다.
	cloneForm($('<%=id%>_sform'), '<%=id%>_rform', '<%=id%>',   $('<%=id%>_grid_content'));	

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'massmail/statistic/massmail.do?method=massmailStatisticUsers&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			$('<%=id%>Content').setStyle('display','none');
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/***********************************************/
/* 컨텐츠 로딩
/***********************************************/
$('<%=id%>').loadContent = function(method, key) {
	$('<%=id%>Content').setStyle('display','block');
	$('<%=id%>ChartViewBtn').innerHTML = '그래프 숨기기';	
	var frm = $('<%=id%>_sform');
	nemoRequest.init( 
		{
			url: 'pages/massmail/statistic/massmail_statistic_users_proc.jsp?method='+method+'&sUserID='+frm.sUserID.value,
			update: $('<%=id%>Content'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
			
			}
		});
	nemoRequest.post($('<%=id%>_sform'));
}


/***********************************************/
/* 차트 보이기/숨기기
/***********************************************/
$('<%=id%>').chartView = function() {

	if($('<%=id%>Content').getStyle('display') == 'none'){
		$('<%=id%>Content').setStyle('display','block');
		$('<%=id%>ChartViewBtn').innerHTML = '그래프 숨기기';
	}else{
		$('<%=id%>Content').setStyle('display','none');
		$('<%=id%>ChartViewBtn').innerHTML = '그래프 보이기';
	}
}
/***********************************************/
/* 사용자 검색 버튼 클릭
/***********************************************/
$('<%=id%>').searchUser = function() {
	window_width = 430;
	
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
//************************************************************
//Excel 다운
//************************************************************/
$('<%=id%>').excelDown = function() {
	var frm = $('<%=id%>_sform');
	location.href = "pages/massmail/statistic/massmail_statistic_users_excel.jsp?sYear="+frm.sSendedYear.value+"&sMonth="+frm.sSendedMonth.value+"&sUserID="+frm.sUserID.value+"&sGroupID="+frm.sGroupID.value;
}

/***********************************************/
/* 사용자 검색 초기화
/***********************************************/
$('<%=id%>').searchClear = function() {
	$('<%=id%>_sform').sUserID.value = '';
	$('<%=id%>userNames').innerHTML = '';
	$('<%=id%>userNamesWrapper').setStyle('display','none');
}
/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').list();
});
</script>
