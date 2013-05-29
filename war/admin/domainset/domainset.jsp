<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="web.common.util.*"%>

<%
	
	String id = request.getParameter("id");
	String domainFlag = request.getParameter("domainFlag");
	
	String isAdmin = LoginInfo.getIsAdmin(request);

	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:650px">
	<div class="text">
		<img src="images/tag_blue.png"> 메일 발송 스레드에 대한 설정을 합니다.
	</div> 
	<% if(!domainFlag.equals("2")){ %>
	<div class="right">
		<a href="javascript:$('<%=id%>').editWindow( 0 )" class="web20button bigblue">추가 </a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
	</div>
	<%} %>
</div>

</form>

<div style="clear:both;width:650px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="650px" >
	<thead>
		<tr>
		<th style="height:40px;width:50px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eDomainID,this.checked)"/></th>		
		<th style="height:40px;">도메인</th>
		<th style="height:40px;width:100px">타입</th>
		<th style="height:40px;width:100px">스레드<br>개수</th>
		<th style="height:40px;width:100px">소켓응답<br>대기시간(초)</th>
		<th style="height:40px;width:100px">소켓 당<br>발송건수</th>
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

	$('<%=id%>').popup = new PopupMenu();

	$('<%=id%>').popup.add('수정', 
		function(target,e) {
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("domainset_id") );
		}
	);

	<% if(!domainFlag.equals("2")){ %>
	$('<%=id%>').popup.addSeparator();

	$('<%=id%>').popup.add('삭제', function(target,e) { 
						$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("domainset_id") ); 
					}
	);
	<%}%>

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

			width: 350,
			//height: $('mainColumn').style.height.toInt(),
			height: 200,
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/domainset/domainset.do?id=<%=id%>&domainFlag=<%=domainFlag%>&method=edit&domainID='+seq
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
	if(frm.eThreadCount.value<='0'){
		alert('스레드개수는 0이상을 입력해야 합니다');
		frm.eThreadCount.focus();
		return;
	}
	if(frm.eSocketTimeOut.value<='0'){
		alert('소켓응답대기시간은  0이상을 입력해야 합니다');
		frm.eSocketTimeOut.focus();
		return;
	}
	if(frm.eSocketPerSendCount.value<='0'){
		alert('소켓당발송건수는  0이상을 입력해야 합니다');
		frm.eSocketPerSendCount.focus();
		return;
	}

	
	copyForm( $('<%=id%>_rform'), frm );

	if(seq == '0') {
		goUrl = 'admin/domainset/domainset.do?id=<%=id%>&domainFlag=<%=domainFlag%>&method=insert';
	} else {
		goUrl = 'admin/domainset/domainset.do?id=<%=id%>&domainFlag=<%=domainFlag%>&method=update';
	}
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: goUrl
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			if(resHTML.indexOf("O")>0){
				$('<%=id%>').getSendState();
			}
			$('<%=id%>').list();
			
		}
	});
	nemoRequest.post(frm);
	
	
}

var httpRequest = null;
$('<%=id%>').getXMLHttpRequest= function(){
	if (window.ActiveXObject) {
		try {
			return new ActiveXObject("Msxml2.XMLHTTP");
		} catch(e) {
			try {
				return new ActiveXObject("Microsoft.XMLHTTP");
			} catch(e1) { return null; }
		}
	} else if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else {
		return null;
	}
}



$('<%=id%>').sendRequest = function(url, params, callback, method) {
	
	httpRequest = $('<%=id%>').getXMLHttpRequest();
	
	var httpMethod = method ? method : 'GET';
	if (httpMethod != 'GET' && httpMethod != 'POST') {
		httpMethod = 'GET';
	}
	var httpParams = (params == null || params == '') ? null : params;
	var httpUrl = url;
	if (httpMethod == 'GET' && httpParams != null) {
		httpUrl = httpUrl + "?" + httpParams;
	}
	
	httpRequest.open(httpMethod, httpUrl, true);
	httpRequest.setRequestHeader(
		'Content-Type', 'application/x-www-form-urlencoded');
	httpRequest.onreadystatechange = callback;

	httpRequest.send(httpMethod == 'POST' ? httpParams : null);

}

$('<%=id%>').getSendState = function() {

	if(confirm("도메인 설정을 변경하면 발송엔진을 재시작해야 합니다. 지금 재시작 하시겠습니까?"))
	{

		$('<%=id%>').sendRequest("returnSendState.jsp", "type=check", $('<%=id%>').callbackFunction, "GET");
	}


}

$('<%=id%>').reset = function() {


	nemoWindow(
			{
			    'id': 'manager',
				busyEl: 'manager', // 창을 열기까지 busy 가 표시될 element
				width: 650,
				height: $('mainColumn').style.height.toInt(),
				title: '시스템관리',
				//type: 'modal',
				container: 'desktop',
				noOtherClose: true,
				loadMethod: 'xhr',
				toolbar : true,
				toolbarURL : 'admin/manager/tab_manager.jsp?id=manager',
				contentURL: 'admin/manager/manager.do?id=manager'
			}
		);
}

$('<%=id%>').callbackFunction = function() {
		if(httpRequest.readyState==4)
		{
			if(httpRequest.status==200)
			{			
				var send_state = httpRequest.responseText;
				if(send_state.indexOf("false")>0)
				{
					$('<%=id%>').sendRequest("returnSendState.jsp", "type=restart&domainFlag="+<%=domainFlag%>, $('<%=id%>').reset, "GET");
					

				}
				else
				{
					alert("현재 메일이 발송 중입니다. 발송 완료 후 재시작하시기 바랍니다.");
					nemoWindow(
						{
							'id': 'massmailList',
							busyEl: 'massmailList', // 창을 열기까지 busy 가 표시될 element
							width: 1000,
							height: $('mainColumn').style.height.toInt(),
							//height: 270,
							title: '발송결과리스트',
							//type: 'modal',
							container: 'desktop',
							noOtherClose: true,
							loadMethod: 'xhr',
							contentURL: 'massmail/write/massmail.do?step=list&id=massmailList'
										
						}
					);
				}

			}

		}
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

		, url: 'admin/domainset/domainset.do?method=delete&id=<%=id%>&domainFlag=<%=domainFlag%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

			$('<%=id%>_list_form').sCheckAll.checked = false;
			$('<%=id%>').getSendState();

			

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

		, url: 'admin/domainset/domainset.do?method=delete&id=<%=id%>&domainFlag=<%=domainFlag%>&eDomainID=' + seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			// 수정창을 닫는다
			if($('<%=id%>_modal')) closeWindow( $('<%=id%>_modal') );
			$('<%=id%>').getSendState();

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

		url: 'admin/domainset/domainset.do?method=list&id=<%=id%>&domainFlag=<%=domainFlag%>', 
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
	//$('<%=id%>').createPopup();
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