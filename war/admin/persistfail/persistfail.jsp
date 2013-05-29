<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");

String date = null;
String dateBef = null;
if(request.getParameter("eDateStart")== null)
{
	dateBef=DateUtils.getNowAddShortDate(-365);
	date=DateUtils.getDateString();
}else
{
	dateBef=request.getParameter("eDateStart");
	date=request.getParameter("eDateEnd");
	
}
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:100%">
<div class="text">
	등록일
</div>
<div >
	<input type="text" id="eDateStart" name="eDateStart" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this,$('<%=id%>_sform').eDateEnd,'start')" value="<%=dateBef%>"/>
	<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eDateStart,$('<%=id%>_sform').eDateEnd,'start')" align="absmiddle" />
</div>
<div class="text">
	~
</div>

<div >
	<input type="text" id="eDateEnd" name="eDateEnd" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this,$('<%=id%>_sform').eDateStart,'end')" value="<%=date%>"/>
		<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eDateEnd,$('<%=id%>_sform').eDateStart,'end')" align="absmiddle" />
	
</div>
<div >
	<ul id="sSearchType"  class="selectBox" >	
		<li data="email">이메일</li>	
		<li data="massmailTitle">대량메일명</li>
		<li data="targetName">대상자그룹명</li>	
		<li data="smtpCode">실패코드</li>
		<li data="massmailGroupName">대량메일그룹</li>		  						
	</ul>
</div>

<div >
	<input type="text" id="sSearchText" name="sSearchText" size="15"  />
</div>
<div >
	<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
</div>
<div class="right">
	<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>				
</div>
<div class="right">
	<a href="javascript:$('<%=id%>').downList()" class="web20button bigblue">다운로드</a>				
</div>
<div class="right">
	<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
</div>

	
</div>

</form>

<div style="clear:both;width:100%">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
		<th style="height:30px;width:40px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').ePersistfailID,this.checked)"/></th>
		<th style="height:30px;width:150px">이메일</th>
		<th style="height:30px;width:130px">대량메일명</th>
		<th style="height:30px;width:130px">대상자그룹명</th>
		<th style="height:30px;width:130px">대량메일그룹</th>
		<th style="height:30px;width:50px">실패코드</th>
		<th style="height:30px;">실패메시지</th>
		<th style="height:30px;width:110px">등록일</th>
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
	$('<%=id%>').list();
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
/* 팝업메뉴 create
/***********************************************/
$('<%=id%>').createPopup = function() {

	$('<%=id%>').popup = new PopupMenu('<%=id%>');


	$('<%=id%>').popup.add('삭제', function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("persistfail_id") ); 
		}
	);

	$('<%=id%>').popup.setSize(150, 0);

}


/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['ePersistfailID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['ePersistfailID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'admin/persistfail/persistfail.do?method=delete&id=<%=id%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

			$('<%=id%>_list_form').sCheckAll.checked = false;

		}
	});
	nemoRequest.post(frm);

}

/***********************************************/
/* 삭제
/***********************************************/
$('<%=id%>').deleteData = function( seq ) {

	if(!confirm("정말로 삭제 하시겠습니까?")) return;

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'admin/persistfail/persistfail.do?method=delete&id=<%=id%>&ePersistfailID=' + seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			// 수정창을 닫는다
			//if($('<%=id%>_modal')) closeWindow( $('<%=id%>_modal') );

		}
	});
	nemoRequest.post($('<%=id%>_rform'));

}
//************************************************************
//대상자 다운
//************************************************************/
$('<%=id%>').downList = function() {
	var frm = $('<%=id%>_sform');
	
		// 검색 값을 새로운 폼값(검색후 픽스될) 에 담는다.
		cloneForm($('<%=id%>_sform'), '<%=id%>_rform', '<%=id%>',   $('<%=id%>_grid_content'));

	
	location.href = "admin/persistfail/persistfail_down.jsp?sSearchType="+$('<%=id%>_rform').sSearchType.value+"&sSearchText="+$('<%=id%>_rform').sSearchText.value+"&eDateStart="+$('<%=id%>_rform').eDateStart.value+"&eDateEnd="+$('<%=id%>_rform').eDateEnd.value;
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

		url: 'admin/persistfail/persistfail.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
});

</script>