<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="web.common.util.*"%>
<%
	String id = request.getParameter("id");

	String isAdmin = LoginInfo.getIsAdmin(request);
	
	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:100%">
	<div class="start">
		<ul id="sSearchType"  class="selectBox" >
			<li data="dbName" select="yes">DB명</li>
			<li data="driverClass">드라이버명</li>
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15" />
	</div>
	<div>
		<ul id="sSearchSelectType"  class="selectBox" >
			<li data="" select="yes">-검색조건선택-</li>
			<li data="useYN">사용여부</li>
			<li data="encodingYN">인코딩여부</li>
			<li data="defaultYN">디폴트여부</li>
					
		</ul>
	</div>
	<div>
		<ul id="sSearchSelect"  class="selectBox" >
			<li data="" >전체</li>
			<li data="Y" >Y</li>
			<li data="N">N</li>
		</ul>
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="right">
			<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>	
	</div>
	<div class="right">
				<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
	</div>
	<div class="right">
			<a href="javascript:$('<%=id%>').editWindow( 0 )" class="web20button bigblue">추가</a>
	</div>
</div>

</form>

<div style="clear:both;width:100%">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eDbID,this.checked)"/></th>
		<th style="height:30px;width:50px">DBID</th>
		<th style="height:30px;">DB명</th>
		<th style="height:30px;width:100px">드라이버명</th>
		<th style="height:30px;width:100px">사용여부</th>
		<th style="height:30px;width:50px">인코딩여부</th>
		<th style="height:30px;width:50px">디폴트여부</th>
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
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("db_id"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("driver_id") );
		}
	);

	$('<%=id%>').popup.addSeparator();

	$('<%=id%>').popup.add('삭제', function(target,e) { 
						$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("db_id") ); 
					}
	);

	$('<%=id%>').popup.setSize(150, 0);

}


/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq, driverID ) {
	
	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 550,
			//height: $('mainColumn').style.height.toInt(),
			height: 350,
			title: 'DB 등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/dbjdbcset/dbjdbcset.do?id=<%=id%>&method=edit&dbID='+seq+'&driverID='+driverID
		}
	);
	
}
/***********************************************/
/* DB접근키 확인버튼 클릭
/***********************************************/
$('<%=id%>').dbAccessKeyConfirm = function() {
	
	var frm = $('<%=id%>_form');
	
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		url: 'admin/dbjdbcset/dbjdbcset.do?id=<%=id%>&method=checkConfirm',
		onSuccess: function(html,els,resHTML,scripts) {
			if(resHTML == 'success') { // 입력한 값이 일치하면
				$('<%=id%>_confirmTable').setStyle('display','none');
				$('<%=id%>_confirmBtn').setStyle('display','none');
				$('<%=id%>_editTable').setStyle('display','block');
				$('<%=id%>_allBtn').setStyle('visibility','');
			} else {
				alert('DB접근키가 일치하지 않습니다.');
				frm.eDbAccessKeyConfirmer.value = '';
			}
		}
	});
	
	nemoRequest.post(frm);
	
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
		goUrl = 'admin/dbjdbcset/dbjdbcset.do?id=<%=id%>&method=insert';
	} else {
		goUrl = 'admin/dbjdbcset/dbjdbcset.do?id=<%=id%>&method=update';
	}
	
	//DB인증비밀번호 와 확인번호 일치여부 체크
	var key = frm.eDbAccessKey.value;
	var keyConfirm = frm.eDbAccessKeyConfirm.value;
	
	if(key != keyConfirm) {
		alert('[DB접근키 오류] 입력된 DB접근키 값이 서로 일치하지 않습니다.');
		frm.eDbAccessKey.value = "";
		frm.eDbAccessKeyConfirm.value = "";
		frm.eDbAccessKey.focus();
		return;
	}
	
	//DB인증비밀번호 가능여부 체크
	if(key != '') {
		if(!/^[a-zA-Z0-9`~!@#$%^&*()|\\\'\";:\/?<>,._+]{6,20}$/.test(key)) {
			alert('DB접근키는 영문자와 숫자, 특수문자 조합으로 6~20자리를 사용해야 합니다.');
			frm.eDbAccessKey.value = "";
			frm.eDbAccessKeyConfirm.value = "";
			frm.eDbAccessKey.focus();
			return;
		}
		var chk_num = key.search(/[0-9]/g);
		var chk_eng = key.search(/[a-z]/ig);
		var chk_special = key.search(/[`~!@#$%^&*()|\\\'\";:\/?<>,._+]/gi);
		 
		if(chk_num < 0 || chk_eng < 0 || chk_special < 0) {
			alert('DB접근키는 숫자와 영문자, 특수문자를 혼용하여야 합니다.');
			frm.eDbAccessKey.value = "";
			frm.eDbAccessKeyConfirm.value = "";
			frm.eDbAccessKey.focus();
			return;
		}
		 
		if(/(\w)\1\1/.test(key)) {
			alert('DB접근키에 같은 문자를 3번 이상 연속으로 사용하실 수 없습니다.');
			frm.eDbAccessKey.value = "";
			frm.eDbAccessKeyConfirm.value = "";
			frm.eDbAccessKey.focus();
			return;
		}
	}
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: goUrl
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			//closeWindow( $('<%=id%>_modal') );
			
			if(resHTML.indexOf("false") == -1) {
				if(confirm("사용자에게 추가한 DB를 사용할 수 있는 권한을 부여하시겠습니까?")) {
					nemoWindow(
							{
							    'id': 'userGroup',
								busyEl: 'userGroup', // 창을 열기까지 busy 가 표시될 element
								width: 900,
								height: $('mainColumn').style.height.toInt(),
								title: '그룹/계정관리',
								//type: 'modal',
								container: 'desktop',
								noOtherClose: true,
								loadMethod: 'xhr',
								contentURL: 'admin/usergroup/usergroup.do?id=userGroup'
							}
					);
				}
			}
			$('<%=id%>').allList()
		}
	});
	nemoRequest.post(frm);
	
	
}

/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eDbID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eDbID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'admin/dbjdbcset/dbjdbcset.do?method=delete&id=<%=id%>'
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

		, url: 'admin/dbjdbcset/dbjdbcset.do?method=delete&id=<%=id%>&eDbID=' + seq
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

		url: 'admin/dbjdbcset/dbjdbcset.do?method=list&id=<%=id%>', 
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