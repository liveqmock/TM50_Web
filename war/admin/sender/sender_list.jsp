<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String id = request.getParameter("id");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:670px">
	<div class="start">
		<ul id="sSearchType"  class="selectBox" >					
			<li data="S.senderName" >이름</li>
			<li data="S.senderEmail">이메일</li>
			<li data="U.userName">작성자</li>
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15"  />
	</div>
	<div>
				<ul id="sSearchType_gubun"  class="selectBox" >
					<li data="">-공유타입선택-</li>	
					<li data="all">전체공유</li>		
					<li data="group">그룹공유</li>
					<li data="use_not">비공유</li>
				</ul>
	</div>
	<div>
				<ul id="sSearchType_use"  class="selectBox" >
					<li data="">-사용여부선택-</li>	
					<li data="Y">사용</li>		
					<li data="N">사용안함</li>
				</ul>
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>	
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').editWindow( 0 )" class="web20button bigblue">추가</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
	</div>
</div>
</form>

<div style="clear:both;width:670px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" name="sCheckAll" class="notBorder" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eSenderID,this.checked)"/></th>
		<th style="height:30px;width:80px">이름</th>
		<th style="height:30px;width:">이메일</th>
		<th style="height:30px;width:100px">핸드폰</th>
		<th style="height:30px;width:80px">작성자</th>
		<th style="height:30px;width:80px">공유타입</th>		
		<th style="height:30px;width:80px">사용여부</th>
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
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("sender_id") );
		}
	);

	$('<%=id%>').popup.addSeparator();

	$('<%=id%>').popup.add('삭제', function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("sender_id") ); 
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
			height: 270,
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/sender/sender.do?id=<%=id%>&method=edit&senderID='+seq
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
	
	if(frm.eSenderCellPhone.value=="핸드폰번호는 '-'를 포함합니다")
	{	
		frm.eSenderCellPhone.value="";
	}
	
	if (frm.eSenderEmail.value && !CheckEmail(frm.eSenderEmail.value)) {
		toolTip.showTipAtControl(frm.eSenderEmail,'이메일 형식이 틀립니다');
		return;
	}

	if(frm.eSenderCellPhone.value && !CheckTel(frm.eSenderCellPhone.value)) {
		toolTip.showTipAtControl(frm.eSenderCellPhone,'휴대전화 형식이 틀립니다');
		return;
	}

	
		
	copyForm( $('<%=id%>_rform'), frm );

	if(seq == '0') {
		goUrl = 'admin/sender/sender.do?id=<%=id%>&method=insert';
	} else {
		goUrl = 'admin/sender/sender.do?id=<%=id%>&method=update';
	}
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: goUrl
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			closeWindow( $('<%=id%>_modal') );
		}
	});
	nemoRequest.post(frm);
	
	
}

/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eSenderID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eSenderID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'admin/sender/sender.do?method=delete&id=<%=id%>'
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

		, url: 'admin/sender/sender.do?method=delete&id=<%=id%>&eSenderID=' + seq
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

		url: 'admin/sender/sender.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/***********************************************/
/* 핸드폰 번호 
/***********************************************/
$('<%=id%>').deleteValue = function (  ) {	
	
	var frm = $('<%=id%>_form');

	frm.eSenderCellPhone.value = '';


	
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	//$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
});

</script>