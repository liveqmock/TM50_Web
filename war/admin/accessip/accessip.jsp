<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%>  
<%
	String id = request.getParameter("id");
	SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(application);
	String accessIPUseYN = systemSetservice.getSystemSetInfo("1","accessIPUseYN");
	
	String isAdmin = LoginInfo.getIsAdmin(request);

	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div id="<%=id%>_accessIPUseN" style="width:700px;display:none">
	<table cellpadding=0 cellspacing=0>
		<tbody>
		<tr>
			<td><img src="images/tag_red.png" alt="Tips "></td>
			<td colspan="2">접근 IP 제한을 사용하지 않도록 설정 되어있습니다. </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>사용하도록 설정을 변경 하시려면 아래의 [사용하기] 버튼을 클릭하세요.</td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td><div class="btn_b"><a href="javascript:$('<%=id%>').changeUseYN('Y')" style ="cursor:pointer"><span>사용하기</span></a></div></td>
		</tr>
		</tbody>
	</table>
</div>
<div id="<%=id%>_accessIPUseY" style="width:700px;display:none">
	<table cellpadding=0 cellspacing=0>
		<tbody>
		<tr>
			<td><img src="images/tag_blue.png" alt="Tips "></td>
			<td colspan="2">접근 IP 제한을 사용하고 있습니다. </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>사용하지 않도록 설정을 변경 하시려면 아래의 [제한해제] 버튼을 클릭하세요.</td>
			<td>&nbsp;&nbsp;&nbsp;</td>
			<td><div class="btn_r"><a href="javascript:$('<%=id%>').changeUseYN('N')" style ="cursor:pointer"><span>제한해제</span></a></div></td>
		</tr>
		</tbody>
	</table>
</div>

<div class="search_wrapper" style="width:700px">
	<div>
		<ul id="sSearchType"  class="selectBox" title="검색 조건 선택">	
			<li data="userName">등록자명</li>
			<li data="a.description">설명</li>
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15" title=" 검색할 문자의 일부를 입력하세요" />
	</div>
	<div>
		<ul id="sUseYN"  class="selectBox" title="사용여부 선택" >
			<li data="">--사용여부--</li>
			<li data="Y">사용</li>
			<li data="N">미사용</li>
		</ul>
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink" title="삭제는 예약발송이면서  발송 준비 대기중, 임시 저장, 발송 완료, 발송 완전 중지, 발송준비중 정지, 발송 대기 중 중지, 발송 준비중 오류, 발송중 오류인 경우에만 가능합니다">선택삭제</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').editWindow(0)" class="web20button bigblue" >추가</a>
	</div>	
</div>
</form>

<div style="clear:both;width:700px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="700px" >
	<thead>
		<tr>		
		<th style="height:30px;width:30px"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder" onclick="selectAll($('<%=id%>_list_form').eAccessipID,this.checked)"/></th>
		<th style="height:30px;width:130px">IP대역</th>
		<th style="height:30px">설명</th>
		<th style="height:30px;width:100px">등록자</th>
		<th style="height:30px;width:130px">등록일</th>
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
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("accessip_id"));
		}
	);
	$('<%=id%>').popup.addSeparator();
	$('<%=id%>').popup.add('삭제', function(target,e) { 
						$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("accessip_id") ); 
					}
	);

	$('<%=id%>').popup.setSize(150, 0);
}

/***********************************************/
/* 전체목록
/***********************************************/
$('<%=id%>').allList = function(){
	
	initFormValue($('<%=id%>_sform'));
	$('<%=id%>').list ();
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

		url: 'admin/accessip/accessip.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq ) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 550,
			//height: $('mainColumn').style.height.toInt(),
			height: 320,
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/accessip/accessip.do?id=<%=id%>&method=edit&accessipID='+seq
		}
	);
	
}

/***********************************************/
/* 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function( seq ) {
	var frm = $('<%=id%>_form');
	var goUrl = '';
	
	if(frm.eOctetA.value == ''){
		alert("첫번째 Octet은 필수 입력값입니다.");
		frm.eOctetA.focus();
		return;
	}
	
	if(frm.eOctetB.value == ''){
		alert("두번째 Octet은 필수 입력값입니다.");
		frm.eOctetB.focus();
		return;
	}
	
	copyForm( $('<%=id%>_rform'), frm );

	if(seq == '0') {
		goUrl = 'admin/accessip/accessip.do?id=<%=id%>&method=insert';
	} else {
		goUrl = 'admin/accessip/accessip.do?id=<%=id%>&method=update&accessipID='+seq;
	}

	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				, url: goUrl
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
					closeWindow( $('<%=id%>_modal') );
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

		, url: 'admin/accessip/accessip.do?method=delete&id=<%=id%>&eAccessipID=' + seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			// 수정창을 닫는다
			if($('<%=id%>_modal')) closeWindow( $('<%=id%>_modal') );
		}
	});
	nemoRequest.post($('<%=id%>_rform'));

}

/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eAccessipID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eAccessipID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'admin/accessip/accessip.do?method=delete&id=<%=id%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			$('<%=id%>_list_form').sCheckAll.checked = false;
		}
	});
	nemoRequest.post(frm);

}
/***********************************************/
/* 사용여부변경
/***********************************************/
$('<%=id%>').changeUseYN = function(useYN) {
	var frm = $('<%=id%>_list_form');
	if(useYN=='Y'){
		$('<%=id%>_accessIPUseY').setStyle('display','block');
		$('<%=id%>_accessIPUseN').setStyle('display','none');
	}else{
		$('<%=id%>_accessIPUseN').setStyle('display','block');
		$('<%=id%>_accessIPUseY').setStyle('display','none');
	}
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		, url: 'admin/systemset/systemset.do?method=updateConfigValue&id=<%=id%>&configFlag=1&configName=accessIPUseYN&configValue='+useYN
		, onSuccess: function(html,els,resHTML) {
		
		}
	});
	nemoRequest.post(frm);
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 	
	<%if(accessIPUseYN.equals("Y")){ %>
		$('<%=id%>_accessIPUseY').setStyle('display','block');
	<%}else{%>
		$('<%=id%>_accessIPUseN').setStyle('display','block');
	<%}%>
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