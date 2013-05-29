<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	String date = null;
	String dateBef = null;
	if(request.getParameter("eSendScheduleDateStart")== null)
	{
		dateBef=DateUtils.getNowAddShortDate(-7);
		date=DateUtils.getDateString();
	}else
	{
		dateBef=request.getParameter("eSendScheduleDateStart");
		date=request.getParameter("eSendScheduleDateEnd");
		
	}
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:970px">
	<div class="text">발송스케줄</div>	
	<div>						
		<input type="text" id="eSendScheduleDateStart" name="eSendScheduleDateStart" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this,$('<%=id%>_sform').eSendScheduleDateEnd,'start')" value="<%=dateBef%>"/>
		<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eSendScheduleDateStart,$('<%=id%>_sform').eSendScheduleDateEnd,'start')" align="absmiddle" />
	</div>
	<div class="text">~</div>
	<div>						
		<input type="text" id="eSendScheduleDateEnd" name="eSendScheduleDateEnd" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this,$('<%=id%>_sform').eSendScheduleDateStart,'end')" value="<%=date%>"/>
		<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eSendScheduleDateEnd,$('<%=id%>_sform').eSendScheduleDateStart,'end')" align="absmiddle" />
	</div>
	<div>		
		<ul id="eRepeatSendType"  class="selectBox" title="반복발송선택">
			<li data="">--반복발송타입--</li>			
			<li data="1">매일반복</li>
			<li data="2">매주반복</li>
			<li data="3">매월반복</li>
		</ul>
	</div>
	<div>
		<ul id="sSearchType"  class="selectBox" title="검색 조건 선택">
			<li data="i.massmailTitle" select="yes">대량메일명</li>			
			<li data="u.userName">작성자명</li>			
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15" title=" 검색할 문자의 일부를 입력하세요" />
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue" title="검색조건을 초기화합니다">전체목록</a>
	</div>
</div>
</form>

<div style="clear:both;width:970px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
		<th style="height:30px;width:50px">대량메일ID</th>
		<th style="height:30px;">대량메일명</th>
		<th style="height:30px;width:100px">작성자</th>
		<th style="height:30px;width:100px">반복타입</th>
		<th style="height:30px;width:150px">반복시작일</th>
		<th style="height:30px;width:150px">반복종료일</th>
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
/* 전체목록
/***********************************************/
$('<%=id%>').allList = function(){
	initFormValue($('<%=id%>_sform'));
	$('<%=id%>_sform').eSendScheduleDateEnd.value ='<%=date%>';
	$('<%=id%>_sform').eSendScheduleDateStart.value='<%=dateBef%>';
	$('<%=id%>').list ();
}

/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq ) {

	nemoWindow(
		{
			'id': '<%=id%>_schedule_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 600,
			height: $('mainColumn').style.height.toInt(),
			//height: 600,
			title: '반복메일 스케줄',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'massmail/write/massmail.do?id=<%=id%>_schedule_modal&preID=<%=id%>&method=editRepeat&massmailID='+seq
		}
	);
	
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

		url: 'massmail/write/massmail.do?method=listRepeat&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			//if($('<%=id%>_schedule_modal')) closeWindow( $('<%=id%>_schedule_modal') );
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}


/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').list(); 
});

</script>


