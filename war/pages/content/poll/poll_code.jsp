<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String id = request.getParameter("id");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:700px">
	<div class="text" style="margin:7px 0;">
		<img src="images/tag_blue.png" alt="설문유형안내 ">설문유형 : 하나의 코드타입에서 관리하며 코드입력만 추가/수정/삭제하실 수 있습니다.<br>
		<img src="images/tag_blue.png" alt="보기유형안내 ">보기유형 : 여러개로 코드타입을 추가하여 사용하실 수 있습니다.
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').editWindow('','','','')" class="web20button bigblue">보기유형 추가</a>
	</div>
</div>

</form>

<div style="clear:both;width:700px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="700px" >
	<thead>
		<tr>
		<th style="height:30px;width:100px"">코드타입</th>		
		<th style="height:30px;width:50px">코드ID</th>
		<th style="height:30px;width:100px">코드명</th>
		<th style="height:30px;width:50px">코드번호</th>		
		<th style="height:30px">코드설명</th>		
		<th style="height:30px;width:80px">사용여부</th>
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
	</form>
</div>
    

    
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
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function(codeID,codeType,useYN) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
			width: 600,
			height: 400,
			title: '설문등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'content/poll/poll.do?id=<%=id%>&method=editPollCode&codeID='+codeID+'&codeType='+codeType+'&useYN='+useYN
		}
	);
	
}



/***********************************************/
/* 저장버튼 클릭 -  저장
/***********************************************/
$('<%=id%>').saveData = function( codeID ) {

	var frm = $('<%=id%>_form');
	//필수입력 조건 체크
	if(!checkFormValue($('<%=id%>_form'))) {
		return;
	}			

	// 코드명의 < , script , /> 등 스크립트 사용 의심 문자 필터	
	if ( frm.eCodeName.value.indexOf('<') >= 0 || frm.eCodeName.value.indexOf('/>') >= 0 || frm.eCodeName.value.indexOf('script') >= 0 ){
		alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
		return;
	}
				
	if(frm.eCodeDesc.length==null){
		alert('보기는 2개이상 등록되어야 합니다.');
		return;
	}else{
		for(i=0;i<frm.eCodeDesc.length;i++){
			if(frm.eCodeDesc[i].value==''){
				alert('보기 지문을 입력하세요');
				return;
			}
		}
		for(i=0;i<frm.eCodeDesc.length;i++){
			// 보기지문의 < , script , /> 등 스크립트 사용 의심 문자 필터	
			if ( frm.eCodeDesc[i].value.indexOf('<') >= 0 || frm.eCodeDesc[i].value.indexOf('/>') >= 0 || frm.eCodeDesc[i].value.indexOf('script') >= 0 ){
				alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
				return;
			}
		}
	}

	if(codeID == '') {
		goUrl = 'content/poll/poll.do?id=<%=id%>&method=insertPollCode';
	} else {
		goUrl = 'content/poll/poll.do?id=<%=id%>&method=updatePollCode&codeID='+codeID;
	}

	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//, updateWindowId: '<%=id%>' // 완료후 버튼,힌트 가 랜더링될 windowid
		, url: goUrl
		//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element		
		, onSuccess: function(html,els,resHTML) {		
			$('<%=id%>').list();					
			closeWindow( $('<%=id%>_modal') );			
		}
	});

	nemoRequest.post($('<%=id%>_form'));		
	
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

		, url: 'content/poll/poll.do?method=deletePollCode&id=<%=id%>&codeID='+seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			// 수정창을 닫는다
			$('<%=id%>').list();
			closeWindow( $('<%=id%>_modal') );

		}
	});
	nemoRequest.post($('<%=id%>_form'));

}


/***********************************************/
/* 리스트 
/***********************************************/

$('<%=id%>').list = function ( ) {	

	var frm = $('<%=id%>_sform');

	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'content/poll/poll.do?method=listPollCode&id=<%=id%>', 
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
	$('<%=id%>').list(); 
});
</script>