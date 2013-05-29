<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="web.common.util.*" %> 

<%
	String id = request.getParameter("id");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:850px">
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
				<% if(month < 10){ %>
					<li data="0<%=month %>" <%if(month==DateUtils.getMonth()){ %> select='Y' <%}%>><%=month %></li>
				<%}else{%>
					<li data="<%=month %>" <%if(month==DateUtils.getMonth()){ %> select='Y' <%}%>><%=month %></li>
				<%} %>
			<%} %>
			<li data="13" >----</li>
		</ul>
	</div>
	<div class="text">월</div>
			
	<div>
		<a href="javascript:$('<%=id%>').search()" class="web20button pink">검색</a>
	</div>

	<div class="right">
		<a href="javascript:$('<%=id%>').excelDown()" class="web20button bigblue">Excel</a>
	</div>	
</div>
<br>
<br>
</form>

<div style="clear:both;width:850px">
<form name="<%=id%>_list_form" id="<%=id%>_list_form">

	<input type="hidden" name="iTotalCnt" id="iTotalCnt" value="">
	
	<table class="ctbl" width="100%">
		<tbody>
			<tr><td class="head" >총 발송 현황</td></tr>	
			<tr>
				<td>
					<table class="ctbl" width="100%">
					<tbody id="<%=id%>_total_content">
					
					</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>

<br>

	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="850px" >
	<thead>
		<tr>		
		<th style="height:30px;width:40px">ID</th>
		<th style="height:30px;" >자동SMS명</th>
		<th style="height:30px;width:120px">집계기간</th>
		<th style="height:30px;width:90px">총발송량</th>
		<th style="height:30px;width:90px">성공통수</th>
		<th style="height:30px;width:90px">실패통수</th>
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
</form>
</div>
    
<div id="<%=id%>_paging" class="page_wrapper"></div>
    
<script type="text/javascript">

/***********************************************/
/* 전체목록
/***********************************************/
$('<%=id%>').allList = function(){
	initFormValue($('<%=id%>_sform'));
	$('<%=id%>').list ();
}

/***********************************************/
/* 검색 버튼
/***********************************************/
$('<%=id%>').search = function(){
	$('<%=id%>').list ();
	$('<%=id%>').totalinfo ();
}

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

$('<%=id%>').list = function ( forPage ) {	

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

		url: 'autosms/autosms.do?method=autoSMSReportMonth&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/***********************************************/
/* 리스트 
/***********************************************/

$('<%=id%>').totalinfo = function () {	

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'autosms/autosms.do?method=autoSMSReportMonthAll&id=<%=id%>', 
		update: $('<%=id%>_total_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}
$('<%=id%>').excelDown = function () {	
	var frm = $('<%=id%>_sform');
	var frmList = $('<%=id%>_list_form');
	location.href = "pages/autosms/statistic/autosms_reportmonth_excel.jsp?sSendedYear="+frm.sSendedYear.value+"&sSendedMonth="+frm.sSendedMonth.value+"&iTotalCnt="+frmList.iTotalCnt.value;
}
/* 리스트 표시 */
window.addEvent("domready",function () {
	
	$('<%=id%>').init();
	$('<%=id%>').list(); 
	$('<%=id%>').totalinfo();
});

</script>