<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="web.common.util.LoginInfo"%>

<%
	String id = request.getParameter("id");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:670px">
	<div class="start">
		<ul id="sSearchType"  class="selectBox">
			<li data="testerName" >이름</li>
			<li data="testerEmail" >이메일</li>
			<%if(LoginInfo.getIsAdmin(request).equals("Y") ){%>
			<li data="u.userName" >작성자</li>
			<%}%>
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15"  />
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>				
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').editWindow( 0 )" class="web20button bigblue" >추가</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
	</div>
</div>
<div style="clear:both">
	<img src="images/tag_blue.png" /> 테스트 메일은 최대 30건만 입력이 가능합니다. ( 현재 <span id="<%=id%>_totalCount"></span>건 등록)
</div>
</form>

<div style="clear:both;width:670px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eTesterID,this.checked)"/></th>
		<th style="height:30px;width:100px">이름</th>
		<th style="height:30px;">이메일</th>
			
		<%if(LoginInfo.getIsAdmin(request).equals("Y") ){%>
			<th style="height:30px;width:120px">휴대폰</th>
			<th style="height:30px;width:80px">작성자</th>
		<%}else{ %>
			<th style="height:30px;width:200px">휴대폰</th>
		
		<%} %>
		
			
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
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("tester_id"));
		}
	);

	$('<%=id%>').popup.addSeparator();

	$('<%=id%>').popup.add('삭제', function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("tester_id") ); 
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

			width: 700,
			//height: $('mainColumn').style.height.toInt(),
			height: 300,
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'content/tester/tester.do?id=<%=id%>&method=edit&eTesterID='+seq
		}
	);
	
}

/***********************************************/
/* 저장버튼 클릭 -  저장
/***********************************************/
$('<%=id%>').saveData = function( seq, flag ) {
	var frm = $('<%=id%>_form');
	var goUrl = '';

	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}

	//if (frm.eTesterEmail.value && !CheckEmail(frm.eTesterEmail.value)) {
	//	frm.eTesterEmail.focus();
	//	toolTip.showTipAtControl(frm.eTesterEmail,'이메일 형식이 틀립니다');
		
	//	return;
	//}

	if(frm.eTesterHp.value && !CheckTel(frm.eTesterHp.value)) {
		frm.eTesterHp.focus();
		toolTip.showTipAtControl(frm.eTesterHp,'휴대전화 형식이 틀립니다');
		
		
		return;
	}
	var st=frm.eTesterEmail.value;
	if(st.charAt(st.length-1)==";")
		st=st.substring(0,st.length-1);		
	
	var st_data = st.split(";");
	if (st_data.length > 20 ) {
		frm.eTesterEmail.focus();
		toolTip.showTipAtControl(frm.eTesterEmail,'이메일은 최대 20개 까지 등록할 수 있습니다');
		
		return;
	}
	
	frm.eTesterEmail.value = st;
	copyForm( $('<%=id%>_rform'), frm );

	if(seq == '0') {
		goUrl = 'content/tester/tester.do?id=<%=id%>&method=insert';
	} else {
		goUrl = 'content/tester/tester.do?id=<%=id%>&method=update';
	}
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: goUrl
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			closeWindow( $('<%=id%>_modal') );
			if(flag=='Y')
				$('<%=id%>').editWindow( 0 );
				
		}
	});

	nemoRequest.post(frm);
	
	
}

/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eTesterID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	//if(frm.elements['eTesterID'].length == checked) {
	//	$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	//}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'content/tester/tester.do?method=delete&id=<%=id%>'
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

		, url: 'content/tester/tester.do?method=delete&id=<%=id%>&eTesterID=' + seq
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

		url: 'content/tester/tester.do?method=list&id=<%=id%>', 
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