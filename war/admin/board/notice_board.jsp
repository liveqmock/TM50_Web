<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="web.common.util.LoginInfo"%>

<%
	String id = request.getParameter("id");

	String isAdmin = LoginInfo.getIsAdmin(request);

%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:100%">
	<div class="start">
		<ul id="sSearchType"  class="selectBox" >
			<li data="title" select="yes">제목</li>
			<li data="content">내용</li>
			<li data="userName">작성자</li>
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15" />
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="right">			
		<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>				
	</div>
	<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
	
	<div class="right">
		<a href="javascript:$('<%=id%>').editWindow( 0 )" class="web20button bigblue">추가</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
	</div>
	<%} %>
</div>

</form>

<div style="clear:both">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
		<th style="height:30px;width:40px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eBoardID,this.checked)"/></th>
		
		<th style="height:30px">제목</th>
		<th style="height:30px;width:80px">작성자</th>
		<th style="height:30px;width:90px">등록일</th>
		<th style="height:30px;width:50px">조회수</th>
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
	<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
	
	$('<%=id%>').popup = new PopupMenu('<%=id%>');

	$('<%=id%>').popup.add('수정', 
		function(target,e) {
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("board_id") );
		}
	);

	$('<%=id%>').popup.addSeparator();

	$('<%=id%>').popup.add('삭제', function(target,e) { 
						$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("board_id") ); 
					}
	);

	$('<%=id%>').popup.setSize(150, 0);
	<%}%>

}


/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq ) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 950,
			//height: $('mainColumn').style.height.toInt(),
			height: $('mainColumn').style.height.toInt(),
			title: '공지사항 등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/board/board.do?id=<%=id%>&method=edit&boardID='+seq
		}
	);
	
}

/***********************************************/
/* 보기창 열기
/***********************************************/
$('<%=id%>').viewWindow = function( seq ) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 700,
			height: $('mainColumn').style.height.toInt()-200,
			//height: 500,
			title: '공지사항 보기',
			type: 'modal',
			//noOtherClose: true,
			loadMethod: 'xhr',
			contentURL: 'admin/board/board.do?id=<%=id%>&method=view&boardID='+seq
		}
	);
	
}

/***********************************************/
/* 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function( seq ) {

	var frm = $('<%=id%>_form');
	var goUrl = '';
	
	frm.eContent.value = $('<%=id%>_ifrmFckEditor').contentWindow.getFCKHtml();
	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}
	
	copyForm( $('<%=id%>_rform'), frm );

	////////////////////////
	
		//$('<%=id%>').uploadFile('<%=id%>');
		frm.elements['eBoardUploadKey'].value = $('<%=id%>').uploader.uploadKey;
		
		 
	////////////////////////
	
	
	
	if(seq == '0') {
		goUrl = 'admin/board/board.do?id=<%=id%>&method=insert';
	} else {
		
		goUrl = 'admin/board/board.do?id=<%=id%>&method=update';
	}
	
	nemoRequest.init( 
	{
		//busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		 url: goUrl
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			closeWindow( $('<%=id%>_modal') );
			$('board-panel').list();
		}
	});
	nemoRequest.post(frm);
	
	
}

/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eBoardID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eBoardID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'admin/board/board.do?method=delete&id=<%=id%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

			$('<%=id%>_list_form').sCheckAll.checked = false;
			$('board-panel').list();

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

		, url: 'admin/board/board.do?method=delete&id=<%=id%>&eBoardID=' + seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			// 수정창을 닫는다
			if($('<%=id%>_modal')) closeWindow( $('<%=id%>_modal') );
			$('board-panel').list();

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
		<%if(!(LoginInfo.getUserAuth(request).equals("3"))){%>
			url: 'admin/board/board.do?method=list&id=<%=id%>',
		<%}else{%>
			url: 'admin/board/board.do?method=list&id=<%=id%>&groupID=<%=LoginInfo.getGroupID(request)%>',
		<%}%> 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}
/***********************************************/
/* 파일 업로드 랜더링
/***********************************************/
$('<%=id%>').renderUpload = function() {
	
	$('<%=id%>').uploader = new SwFileUpload('<%=id%>UploadFlash', {
		width: 420,
		container: '<%=id%>uploadWrapper',
		fileTypeName: '모든 파일',
		allowFileType: '*.*',
		uploadPage: 'admin/board/board.do?method=fileUpload',
		onComplete: function( isError, fileNameArray ) { // 업로드 완료
        
			$('<%=id%>_uploadBtn').setStyle('display','block');
		
				
		},
		onNotExistsUploadFile: function() { // 업로드 할 파일이 없을때
			$('<%=id%>_uploadBtn').setStyle('display','block');
			$('<%=id%>_saveBtn').setStyle('display','block');
			
			toolTip.showTipAtControlOffset( $('<%=id%>_uploadBtn'),'업로드할 파일을 선택해 주시기 바랍니다',{x:100,y:0});
		}
		
	}).render();
}

/***********************************************/
/* 파일 업로드 
/***********************************************/

$('<%=id%>').uploadFile = function( eBoardID ) {
	// 버튼을 숨긴다.
	//$('<%=id%>_uploadBtn').setStyle('display','none');
	//$('<%=id%>_saveBtn').setStyle('display','none');
	
	// 추가 일때 필요한 key
	$('<%=id%>').uploader.uploadKey = '<%=request.getSession().getId()%>'+(new Date().getTime());
	
	// 파라미터 셋팅
	$('<%=id%>').uploader.setParameter({ 'id': '<%=id%>', 'boardID' : eBoardID });
	// 업로드
	$('<%=id%>').uploader.upload();
	
	
}	

/***********************************************/
/* 파일 정보 보이기
/***********************************************/
$('<%=id%>').showFileInfo = function() {
	
	// 업로드가 완료되면 처리될 action
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		updateWindowId: $('<%=id%>uploaded'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'admin/board/board.do?method=getFileInfo',
		update: $('<%=id%>uploaded'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {

			//if(nextFunc) nextFunc();
						
		}
	});
	nemoRequest.get({
		'id': '<%=id%>', 
		'boardID': $('<%=id%>_form').eBoardID.value,
		'uploadKey': $('<%=id%>_form').eBoardUploadKey.value
	});
	
	
}


/***********************************************/
/*파일 다운로드
/***********************************************/
$('<%=id%>').downloadFile = function( uploadKey ) {


		
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window

		url: 'admin/board/board.do?method=fileDownload',
		 
		onSuccess: function(html,els,resHTML,scripts) {
			document.location.href = 'admin/board/board.do?method=fileDownload&uploadKey='+uploadKey+'&reDirectDownload=Y';
		}
	});
	nemoRequest.get({'id': '<%=id%>', 'uploadKey': uploadKey});
	

}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
	

});

</script>
