<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String id = request.getParameter("id");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:850px">
	<div class="start">
		<ul id="sSearchType"  class="selectBox">
			<li data="automailTitle" select="yes">자동메일명</li>			
			<li data="userName">작성자명</li>			
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15" />
	</div>
	
			
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	
	<div class="explain">
		&nbsp;&nbsp; 발송 상태 : &nbsp;&nbsp;
		<img src="images/spinner.gif" alt="가동중"/> 가동중
	</div>
	<div class="explain">
		<img src="images/spinner-placeholder.gif" alt="일시정지"/> 일시정지
	</div>
	<div class="explain">
		<img src="images/spinner-placeholder-red.gif" alt="정지"/> 정지
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>	
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').editWindow( 0 )" class="web20button bigblue">추가</a>
	</div>
</div>

</form>

<div style="clear:both;width:850px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="850px" >
	<thead>
		<tr>		
		<th style="height:30px;width:40px">ID</th>
		<th style="height:30px;" >자동메일명</th>
		<th style="height:30px;width:80px">작성자</th>
		<th style="height:30px;width:180px">반송메일</th>
		<th style="height:30px;width:180px">회신메일</th>
		<th style="height:30px;width:40px">상태</th>
		<th style="height:30px;width:40px">통계</th>
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
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("automailevent_id") );
		}
	);

	$('<%=id%>').popup.addSeparator();

	$('<%=id%>').popup.add('삭제', function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("automailevent_id") ); 
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
			contentURL: 'automail/automail.do?id=<%=id%>&method=edit&eAutomailID='+seq
		}
	);
	
}
/***********************************************/
/* 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function() {

	
	var frm = $('<%=id%>_form');
	var goUrl = '';	
	frm.eTemplateContent.value = $("<%=id%>_ifrmFckEditor").contentWindow.getFCKHtml();

	
	//if(!frm.eTemplateContent.value) {
	//	toolTip.showTipAtControl($('<%=id%>_saveBtn'),'본문을 입력하세요');
	//	return;
	//}

	
	//필수입력 조건 체크
	//if(!checkFormValue(frm)) {
	//	return;
	//}
	if(frm.eAutomailTitle.value== '') {
		alert('자동메일명은 필수항목입니다.');
		frm.eAutomailTitle.focus();
		return;
	}
	if(frm.eReturnMail.value== '') {
		alert('반송 메일은 필수항목입니다.');
		frm.eReturnMail.focus();
		return;
	}
	if ((frm.eReturnMail.value.indexOf("@") == -1) || (frm.eReturnMail.value.indexOf(".") == -1) || (frm.eReturnMail.value.length <= 5)) {
        	alert("반송 메일이 E-Mail 형식에 맞지 않습니다.");
        	frm.eReturnMail.focus();
     		return;
	}

	if (frm.eTemplateSenderMail.value.length > 0 && ((frm.eTemplateSenderMail.value.indexOf("@") == -1) || (frm.eTemplateSenderMail.value.indexOf(".") == -1) || (frm.eTemplateSenderMail.value.length <= 5 ))) {
    	alert("보내는 메일이 E-Mail 형식에 맞지 않습니다.");
    	frm.eTemplateSenderMail.focus();
 		return;
	}
	
	copyForm( $('<%=id%>_rform'), frm );

	var seq =  frm.eAutomailID.value;

	if(seq == '0') {
		goUrl = 'automail/automail.do?id=<%=id%>&method=insert';
	} else {
		goUrl = 'automail/automail.do?id=<%=id%>&method=update';
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
	var checked = isChecked( frm.elements['eAutomailID']  );

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

		, url: 'automail/automail.do?method=delete&id=<%=id%>'
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

	if(!confirm("정말로 삭제 하시겠습니까?  \r\n삭제하시면 관련된 모든 데이타는 영구삭제가 됩니다.")) return;

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'automail/automail.do?method=delete&id=<%=id%>&eAutomailID=' + seq
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

		url: 'automail/automail.do?method=list&id=<%=id%>', 
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

  var frm = $('<%=id%>_form');
 $('<%=id%>').uploader = new SwFileUpload('<%=id%>Upload', {
  width: 450,
  container: '<%=id%>uploadWrapper',
  fileTypeName: '모든 파일',
  allowFileType: '*.*',
  limitSize:10,
  uploadPage: 'automail/automail.do?method=uploadFile',
  
  onComplete: function( isError, fileNameArray ) { // 업로드 완료

   if(!isError) {
	   frm.eFileName.value = fileNameArray[0]; // 파일명
	   frm.eNewFile.value =$('<%=id%>').uploader.uploadKey;
	   //alert(frm.eFileName.value);
	   //alert(frm.eNewFile.value);
	 
	   //$('<%=id%>').saveData();
	   
   }    
  },
  
  onNotExistsUploadFile: function() { // 업로드 할 파일이 없을때
  //$('<%=id%>').saveData();
   //alert('업로드할 CSV파일을 선택해 주시기 바랍니다');
  }
  
 }).render();
}

/***********************************************/
/* 파일 업로드 저장 버튼 클릭
/***********************************************/

$('<%=id%>').uploadFile = function() {

	var frm = $('<%=id%>_form');

	//검색 조건 체크
	//if(!checkFormValue(frm)) {
	//	return;
	//}

	
	 // 추가 일때 필요한 key
 	$('<%=id%>').uploader.uploadKey = '<%=request.getSession().getId()%>'+(new Date().getTime());
 
 	// 업로드
 	$('<%=id%>').uploader.upload();
 
 
}

/***********************************************/
/* 메일템플릿열기
/***********************************************/
$('<%=id%>').templateWindow = function(seq) {

	var frm = $('<%=id%>_form');
	
	nemoWindow(
		{
			'id': '<%=id%>_template_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 410,
			//height: $('mainColumn').style.height.toInt(),
			height: 300,
			title: '메일템플릿 ',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'pages/massmail/write/massmail_template.jsp?id=<%=id%>_template_modal&method=mailTemplate&preID=<%=id%>&templateType=' + seq
		}
	);
}

/***********************************************/
/* 통계 버튼 클릭
/***********************************************/

$('<%=id%>').showStatistic = function( automailID ) {

	nemoWindow(
			{
				'id': '<%=id%>Statistic',
				busyEl: '<%=id%>Statistic', // 창을 열기까지 busy 가 표시될 element
				width: 900,
				height: $('mainColumn').style.height.toInt(),
				title: '자동메일 통계 조회',
				type: 'modal',
				loadMethod: 'xhr',
				padding: { top: 0, right: 0, bottom: 0, left: 0 },
				contentURL: 'pages/automail/statistic/automail_statistic.jsp?id=<%=id%>Statistic&automailID='+automailID
			}
	);
	
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
});

</script>