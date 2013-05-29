<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String id = request.getParameter("id");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:980px">
	<div class="start">
		<ul id="sSearchType"  class="selectBox" >
			<li data="p.pollTitle" select="yes">설문명</li>
			<li data="u.userName">작성자</li>					
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15"  />
	</div>
	<div>
		<jsp:include page="../../../include_inc/search_selectgroup_inc.jsp" flush="true"></jsp:include>
	</div>
	<div>
		<ul id="sUseYN"  class="selectBox" >
			<li data="">--사용여부--</li>
			<li data="Y">사용</li>
			<li data="N">미사용</li>					
		</ul>
	</div>
	<div>
		<ul id="sResultFinishYN"  class="selectBox" >
			<li data="">--작성완료여부--</li>
			<li data="N">임시저장</li>
			<li data="Y">작성완료</li>					
		</ul>
	</div>	
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>	
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').editWindow(0,0,0,0)" class="web20button bigblue">추가</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
	</div>
</div>

</form>

<div style="clear:both;width:980px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="980px" >
	<thead>
		<tr>
		<th style="height:30px;width:30px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').ePollID,this.checked)"/></th>
		<th style="height:30px;width:50px">설문ID</th>
		<th style="height:30px;">설문명</th>
		<th style="height:30px;width:80px">작성자</th>
		<th style="height:30px;width:80px">공유그룹</th>
		<th style="height:30px;width:60px">사용여부</th>
		<th style="height:30px;width:80px">작성완료여부</th>
		<th style="height:30px;width:110px">등록일</th>
		<th style="height:30px;width:80px">종료기준</th>
		<th style="height:30px;width:110px">종료조건</th>
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
			$('<%=id%>').editWindow(
					$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_pollID"),
					$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_shareGroupID"),
					$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_codeNO"),
					$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_pollTemplateID")
			);
		}
	);	
	$('<%=id%>').popup.addSeparator();
	$('<%=id%>').popup.add('삭제', 
			function(target,e) { 
				$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_pollID")); 
			}
	);
	$('<%=id%>').popup.addSeparator();
	$('<%=id%>').popup.add('복사', 
		function(target,e) { 
			$('<%=id%>').copyPoll($('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_pollID")); 
		}
	);	
	
	$('<%=id%>').popup.setSize(150, 0);

}


/***********************************************/
/* 설문복사
/***********************************************/
$('<%=id%>').copyPoll = function(pollID){
	var frm = $('<%=id%>_list_form');		
	
	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'content/poll/poll.do?method=copyPoll&id=<%=id%>&ePollID='+pollID
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			
		}
	});
	nemoRequest.post(frm);
}


/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq, shareGroupID, codeNo, pollTemplateID ) {

	nemoWindow(
		{
			'id': '<%=id%>_info',
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			title: '설문등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'content/poll/poll.do?id=<%=id%>&method=edit&pollID='+seq+'&shareGroupID='+shareGroupID+'&codeNo='+codeNo+'&pollTemplateID='+pollTemplateID
		}
	);
	
}


/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['ePollID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['ePollID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'content/poll/poll.do?method=deletePoll&id=<%=id%>'
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

	if(!confirm("정말로 삭제 하시겠습니까?\r\n모든 데이터가 삭제됩니다.")) return;

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'content/poll/poll.do?method=deletePoll&id=<%=id%>&ePollID='+seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			// 수정창을 닫는다
			closeWindow( $('<%=id%>_info') );

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

		url: 'content/poll/poll.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/***********************************************/
/* 설문 종료 조건 변경
/***********************************************/
$('<%=id%>').changePollEndType = function() {
	var frm = $('<%=id%>_info_form');
	if(frm.ePollEndType[0].checked==true){
		$('pollEndDateTR').setStyle('display','');
		$('pollEndDateLine').setStyle('display','');
	}else{
		$('pollEndDateTR').setStyle('display','none');
		$('pollEndDateLine').setStyle('display','none');
	}
	if(frm.ePollEndType[1].checked==true){
		$('aimAnswerCntTR').setStyle('display','');
		$('aimAnswerCntLine').setStyle('display','');
	}else{
		$('aimAnswerCntTR').setStyle('display','none');
		$('aimAnswerCntLine').setStyle('display','none');
	}
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
});
</script>