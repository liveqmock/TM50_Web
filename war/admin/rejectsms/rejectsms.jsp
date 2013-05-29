<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    

<%
	String id = request.getParameter("id");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:850px">
	<div class="start">
				<ul id="sSearchType"  class="selectBox" >
					<li data="r.receiverPhone">폰번호</li>		
					<li data="u.userName">작성자</li>
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
	<div class="right">
		<a href="javascript:$('<%=id%>').personPreviewDown()" class="web20button bigblue" >Excel</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').editWindow()" class="web20button bigblue">추가</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택 삭제</a>
	</div>
	
</div>

</form>

<div style="clear:both;width:850px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="850px" >
	<thead>
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eRejectID,this.checked)"/></th>
		<th style="height:30px;">폰번호</th>
		<th style="height:30px;width:100px">작성자</th>
		<th style="height:30px;width:120px">등록일</th>
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
/* 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function() {
	var frm = $('<%=id%>_form');

	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}

	if(frm.eInputType[0].checked == true) {
		goUrl = 'admin/rejectsms/rejectsms.do?id=<%=id%>&method=insertTypeDirect';
	} else if(frm.eInputType[1].checked == true) {
		goUrl = 'admin/rejectsms/rejectsms.do?id=<%=id%>&method=insertTypeFile';
	}
	
	copyForm( $('<%=id%>_rform'), frm );
	
	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

				, url: goUrl
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {

					//파일업로드라면 
					if(frm.eInputType[1].checked == true) {					
						$('<%=id%>').list(); 
					}
					closeWindow( $('<%=id%>_modal') );
				}
			});

	nemoRequest.post(frm);
}


/***********************************************/
/* 입력방법수정
/***********************************************/
$('<%=id%>').chgInputType = function(seq){

	//직접입력이라면 
	if(seq=='1'){
		$('<%=id%>_file').setStyle('display','none');
		$('<%=id%>_sms').setStyle('display','block');
	}
	//파일업로드이라면 
	else if(seq=='2'){
		$('<%=id%>_file').setStyle('display','block');
		$('<%=id%>_sms').setStyle('display','none');
	}
};


/***********************************************/
/* 파일 업로드 랜더링
/***********************************************/
$('<%=id%>').renderUpload = function() {

  var frm = $('<%=id%>_form');
 $('<%=id%>').uploader = new SwFileUpload('<%=id%>Upload', {
  width: 450,
  container: '<%=id%>uploadWrapper',
  fileTypeName: '지원파일(csv,txt,xls)',
  allowFileType: '*.csv;*.txt;*.xls',
  limitSize:50,
  uploadPage: 'admin/rejectsms/rejectsms.do?method=uploadFile',
  
  onComplete: function( isError, fileNameArray ) { // 업로드 완료

   if(!isError) {
	   frm.eFileName.value = fileNameArray[0]; // 파일명
	   frm.eNewFile.value =$('<%=id%>').uploader.uploadKey;
	   //alert(frm.eFileName.value);
	   //alert(frm.eNewFile.value);
	 
	   $('<%=id%>').saveData();
	   
   }    
  },
  
  onNotExistsUploadFile: function() { // 업로드 할 파일이 없을때
   //$('<%=id%>').saveData();
   alert('업로드할 파일을 선택해 주시기 바랍니다');
  }
  
 }).render();
}

/***********************************************/
/* 파일 업로드 저장 버튼 클릭
/***********************************************/

$('<%=id%>').uploadFile = function() {

	var frm = $('<%=id%>_form');
	

	//검색 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}

	if(frm.eInputType[1].checked==true){
		 // 추가 일때 필요한 key
	 	$('<%=id%>').uploader.uploadKey = '<%=request.getSession().getId()%>'+(new Date().getTime());
		 
	 	// 업로드
	 	$('<%=id%>').uploader.upload();
	}else{
		$('<%=id%>').saveData();
	}
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


	$('<%=id%>').popup.add('삭제', function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("rejectsms_id") ); 
		}
	);

	$('<%=id%>').popup.setSize(150, 0);

}

/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function(  ) {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eRejectID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eRejectID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'admin/rejectsms/rejectsms.do?method=delete&id=<%=id%>'
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

		, url: 'admin/rejectsms/rejectsms.do?method=delete&id=<%=id%>&eRejectID=' + seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			// 수정창을 닫는다
			//if($('<%=id%>_modal')) closeWindow( $('<%=id%>_modal') );

		}
	});
	nemoRequest.post($('<%=id%>_rform'));

}


/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function() {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 600,
			//height: $('mainColumn').style.height.toInt(),
			height: 300,
			title: '등록',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/rejectsms/rejectsms.do?id=<%=id%>&method=write'
		}
	);
	
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

		url: 'admin/rejectsms/rejectsms.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

//************************************************************
//대상자 다운
//************************************************************/
$('<%=id%>').personPreviewDown = function() {
	
	var searchType=$('<%=id%>_sform').sSearchType.value;
	var searchText=$('<%=id%>_sform').sSearchText.value;
	
	
	
	location.href = "admin/rejectsms/rejectsms_down.jsp?searchType="+searchType+"&searchText="+searchText;
}




/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
});

</script>