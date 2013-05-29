<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%> 
<%@ page import="web.intermail.control.*"%>
<%@ page import="web.intermail.service.*"%>
<%@ page import="java.util.Map"%>

<%
	String id = request.getParameter("id");
	String preID = request.getParameter("preID");
	String intermailID = request.getParameter("intermailID");
	String scheduleID = request.getParameter("scheduleID");


	String isAdmin = LoginInfo.getIsAdmin(request);
	String userAuth = LoginInfo.getUserAuth(request);
	String userID =LoginInfo.getUserID(request);
	String groupID = LoginInfo.getGroupID(request);
	
	InterMailService service = InterMailControlHelper.getUserService(application);
	
	Map<String,Object>resultMap = service.getInterMailState(Integer.parseInt(intermailID),Integer.parseInt(scheduleID));
	
	String state = (String)resultMap.get("state");

%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:800px">
	<div class="start">
		<ul id="sSearchType"  class="selectBox">
			<li data="email" select="yes">이메일</li>			
			<li data="domainName">도메인명</li>			
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15" />
	</div>	
			
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<% if(state.equals(Constant.INTER_STATE_APPROVE)) {%>
	<div class="right">
		<a href="javascript:$('<%=id%>').updateSendApprove()" class="web20button bigpink" title="발송승인하면 취소가 불가능합니다.">발송승인</a>
	</div>	
	<div class="right">
		<a href="javascript:$('<%=id%>').deleteAllSend()" class="web20button bigblue" >전체삭제</a>
	</div>	
	<div class="right">
		<a href="javascript:$('<%=id%>').deleteSelectedSend()" class="web20button bigblue">선택삭제</a>
	</div>
	<%} %>
</div>

</form>

<div style="clear:both;width:800px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">	
	<input type="hidden" id="eIntermailID" name="eIntermailID" value="<%=intermailID %>">
	<input type="hidden" id="eScheduleID" name="eScheduleID" value="<%=scheduleID %>">
	
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="800px" >
	<thead>
		<tr>
		<th style="height:30px;width:30px"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder" onclick="selectAll($('<%=id%>_list_form').eSendID,this.checked)"/></th>		
		<th style="height:30px;width:200px" >이메일명</th>
		<th style="height:30px;">메일제목</th>
		<th style="height:30px;width:200px">첨부파일명</th>
		<th style="height:30px;width:150px">등록일</th>		
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
	$('<%=id%>').list ();
}

/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq ) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 900,
			height: $('mainColumn').style.height.toInt(),
			//height: 600,
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'intermail/intermail.do?id=<%=id%>&method=edit&eIntermailID='+seq
		}
	);
	
}

/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eIntermailID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eTemplateID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'intermail/intermail.do?method=delete&id=<%=id%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

			$('<%=id%>_list_form').sCheckAll.checked = false;

		}
	});
	nemoRequest.post(frm);

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

		url: 'intermail/intermail.do?method=listInterMailSend&id=<%=id%>&intermailID=<%=intermailID%>&scheduleID=<%=scheduleID%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}


/***********************************************/
/* 선택 삭제
/***********************************************/
$('<%=id%>').deleteSelectedSend = function() {
	
	
	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eSendID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}
	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?\r\n삭제한 데이터는 발송대상에서 제외됩니다.")) return;

	copyForm( $('<%=id%>_rform'), frm );
	
	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
				, url: 'intermail/intermail.do?method=deleteSelectedSend&id=<%=id%>'
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {	
					$('<%=id%>_list_form').sCheckAll.checked = false;		
					$('<%=id%>').list();
				}
			});
	nemoRequest.post(frm);
}

/***********************************************/
/* 전체 삭제
/***********************************************/
$('<%=id%>').deleteAllSend = function() {
	var frm = $('<%=id%>_list_form');
	
	
	if(!confirm("정말 전체 데이터를 삭제하시겠습니까?\r\n삭제하면 발송대상자에서 제외되고 발송상태는 대기중상태(초기값)로 변경됩니다.")) return;

	copyForm( $('<%=id%>_rform'), frm );
	
	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
				, url: 'intermail/intermail.do?method=deleteAllSend&id=<%=id%>'
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {					
					$('<%=id%>').list();
					$('<%=preID%>').list();
					closeWindow( $('<%=id%>') );
				}
			});
	nemoRequest.post(frm);

}

/***********************************************/
/* 발송승인
/***********************************************/
$('<%=id%>').updateSendApprove = function(){
	var frm = $('<%=id%>_list_form');
	
	if(frm.eTotalCnt.value==0){
		alert("발송대기건수가 없습니다. 승인할 수 없습니다.");
		closeWindow( $('<%=id%>') );		
		return;
	}
	
	if(!confirm("정말 발송 승인하시겠습니까?\r\n발송승인하면 취소가 불가능하며 발송이 이루어집니다.")) return;

	copyForm( $('<%=id%>_rform'), frm );
	
	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
				, url: 'intermail/intermail.do?method=updateSendApprove&id=<%=id%>'
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
					$('<%=preID%>').list();
					closeWindow( $('<%=id%>') );					
				}
			});
	nemoRequest.post(frm);
}

/***********************************************/
/* 메일 본문 및 파일 컨텐츠 보기 
/***********************************************/
$('<%=id%>').showContent = function(flag,intermailID,scheduleID,sendID){
		
	nemoWindow(
			{
				'id': '<%=id%>_approve_modal',	
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element	
				width: 900,				
				height: 750,
				title: '내용보기',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'intermail/intermail.do?id=<%=id%>_approve_modal&method=viewInterMailSend&flag='+flag+'&intermailID='+intermailID+'&scheduleID='+scheduleID+'&sendID='+sendID
			}
		);
};


/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();	
	$('<%=id%>').list(); 
});

</script>