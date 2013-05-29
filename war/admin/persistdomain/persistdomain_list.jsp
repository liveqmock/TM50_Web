<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>

<%
	String id = request.getParameter("id");

	String isAdmin = LoginInfo.getIsAdmin(request);
	
	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:650px">
	<div style="float:left">
	<table border="0" cellpadding="3">
		<tbody>
		<tr>
			<td>
				<ul id="sSearchType"  class="selectBox" >
					<li data="domain_name" >도메인</li>
					<li data="description">설명</li>
				</ul>
			</td>
			<td>
				<input type="text" id="sSearchText" name="sSearchText" size="15"  />
			</td>
			<td>
				<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
			</td>
			
		</tr>
		</tbody>
	</table>
	</div>
	<div style="float:right">
	<table border="0" cellpadding="3">
		<tbody>
		<tr>
			<td aling="right">
				<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
			</td>
			<td aling="right">
				<a href="javascript:$('<%=id%>').editWindow( 0 )" class="web20button bigblue">추가</a>
			</td>
			<td>				
				<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>				
			</td>
		</tr>
		</tbody>
	</table>
	</div>
</div>

</form>

<div style="clear:both;width:650px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="650px" >
	<thead>
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eDomainID,this.checked)"/></th>
		<th style="height:30px;width:150px">도메인</th>
		<th style="height:30px;width:">설명</th>
		<th style="height:30px;width:140px">등록일</th>
		<th style="height:30px;width:50px">사용</th>
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

	$('<%=id%>').popup.add('수정', 
		function(target,e) {
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("domainID") );
		}
	);

	$('<%=id%>').popup.addSeparator();

	$('<%=id%>').popup.add('삭제', function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("domainID") ); 
		}
	);

	$('<%=id%>').popup.setSize(150, 0);

}


/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq ) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 400,
			//height: $('mainColumn').style.height.toInt(),
			height: 170,
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/persistdomain/persistdomain.do?id=<%=id%>&method=edit&domainID='+seq
		}
	);
	
}
/***********************************************/
/* 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function( seq ) {

	var frm = $('<%=id%>_form');
	var goUrl = '';

	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}
	copyForm( $('<%=id%>_rform'), frm );
	
	if(seq == '0') {
		goUrl = 'admin/persistdomain/persistdomain.do?id=<%=id%>&method=insert';
	} else {
		goUrl = 'admin/persistdomain/persistdomain.do?id=<%=id%>&method=update';
	}
	if(frm.eDomainName.value.toLowerCase().indexOf('.') == -1) {
		toolTip.showTipAtControl(frm.eDomainName,'도메인 형식에 맞지 않습니다.');
		return;
	}		
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: goUrl
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			//closeWindow( $('<%=id%>_modal') );
		}
	});
	nemoRequest.post(frm);
	
	
}

/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eDomainID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eDomainID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'admin/persistdomain/persistdomain.do?method=delete&id=<%=id%>'
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

		, url: 'admin/persistdomain/persistdomain.do?method=delete&id=<%=id%>&eDomainID=' + seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			// 수정창을 닫는다
			if($('<%=id%>_modal')) closeWindow( $('<%=id%>_modal') );

		}
	});
	nemoRequest.post($('<%=id%>_rform'));

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

		url: 'admin/persistdomain/persistdomain.do?method=list&id=<%=id%>', 
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


<%}else{%>
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td align="center" valign="middle">
			<center><img src="../../images/error.jpg" /></center>
		</td>
	</tr>
</table>
<%}%>