<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>  
<%
	String id = request.getParameter("id");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:770px">
	<div style="float:left">
	<table border="0" cellpadding="3">
		<tbody>
		<tr>
			<td>
				<ul id="sSearchType"  class="selectBox" title="검색 조건 선택">	
					<li data="connectDBName">고객정보수집명</li>
				</ul>
			</td>
			<td>
				<input type="text" id="sSearchText" name="sSearchText" size="15" title=" 검색할 문자의 일부를 입력하세요" />
			</td>
			 <td>
				<ul id="sUseYN"  class="selectBox" title="검색 조건 선택" >
					<li data="">전체</li>
					<li data="Y">사용</li>
					<li data="N">미사용</li>
				</ul>
			</td>			
			<td>
				<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
			</td>
		</tr>
		</tbody>
	</table>
	</div>
	<div style="float:right">
	<table border="0" cellpadding="3">
		<tbody>
		<tr>
			<td aling="right">
				<a href="javascript:$('<%=id%>').editWindow('')" class="web20button bigblue">추가</a>
			</td>
		</tr>
		</tbody>
	</table>
	</div>	
</div>
</form>

<div style="clear:both;width:770px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="770px" >
	<thead>
		<tr>		
		<th style="height:30px;width:40px">DBID</th>
		<th style="height:30px;">고객 정보 수집명</th>
		<th style="height:30px;width:70px">등록자ID</th>
		<th style="height:30px;width:60px">사용여부</th>
		<th style="height:30px;width:160px">수집 테이블명</th>
		<th style="height:30px;width:90px">수집 대상자수</th>
		<th style="height:30px;width:50px">상태</th>
		<th style="height:30px;width:140px">다음 수집일</th>		
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
/* 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function(seq) {
	var frm = $('<%=id%>_form');

	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}
	var rHH = frm.eUpdateScheduleHour.value;
	if(rHH<10) rHH = "0"+rHH;
	var rMM = frm.eUpdateScheduleMinute.value;
	if(rMM<10) rMM = "0"+rMM;

	var scheduleStartDate = frm.eUpdateSchedule.value+" "+rHH+":"+rMM+":00";
	if(frm.eUpdateType.value=='1'){
		if(scheduleStartDate<'<%=DateUtils.getDateTimeString()%>'){
			alert('1회수집일이 현재시각보다 이전입니다.');		
			frm.eUpdateSchedule.focus();
			return;
		}
	}
	if(!$('<%=id%>').isValidateQuery) {
		alert('쿼리실행을 먼저 실행하세요.');		
		return;
	}

	if(seq==0) {
		goUrl = 'admin/dbconnect/dbconnect.do?id=<%=id%>&method=insert&';
	}else{
		goUrl = 'admin/dbconnect/dbconnect.do?id=<%=id%>&method=update';
	}
	copyForm( $('<%=id%>_rform'), frm );
	
	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

				, url: goUrl
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
					closeWindow( $('<%=id%>_modal') );
					if($('<%=id%>_info')){
						$('<%=id%>_info').loadContent();
						$('<%=id%>_info').list();
					}
				}
			});

	nemoRequest.post($('<%=id%>_form'));
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
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("db_id") );
		}
	);

	$('<%=id%>').popup.setSize(150, 0);

}

/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function(seq) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 800,
			height: $('mainColumn').style.height.toInt(),
			//height: 600,
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/dbconnect/dbconnect.do?id=<%=id%>&method=edit&dbID='+seq
		}
	);
	
}

/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').dbInfoViewWindow = function(seq) {

	nemoWindow(
		{
			'id': '<%=id%>_info',
			
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 650,
			height: $('mainColumn').style.height.toInt(),
			//height: 600,
			title: '확인/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'admin/dbconnect/dbconnecthistory.jsp?id=<%=id%>_info&preID=<%=id%>&dbID='+seq
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

		url: 'admin/dbconnect/dbconnect.do?method=listdbInfo&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}


/***********************************************/
/* 수집시기 선택 
/***********************************************/
$('<%=id%>').chgConnectDate = function(seq){

	//1회 수집
	if(seq=='1'){
		$('<%=id%>_one').setStyle('display','block');
		$('<%=id%>_week').setStyle('display','none');
		$('<%=id%>_month').setStyle('display','none');
	//매주 수집 	
	}else if(seq=='2'){
		$('<%=id%>_week').setStyle('display','block');
		$('<%=id%>_one').setStyle('display','none');
		$('<%=id%>_month').setStyle('display','none');
	//매월 수집	
	}else if(seq=='3'){
		$('<%=id%>_month').setStyle('display','block');
		$('<%=id%>_week').setStyle('display','none');
		$('<%=id%>_one').setStyle('display','none');
	}
	
	
}



/***********************************************/
/* 컬럼을 가져온다.
/***********************************************/
$('<%=id%>').getQueryColumn = function( ) {

	var frm = $('<%=id%>_form');

	if(!frm.edbID.value) {
		toolTip.showTipAtControl(frm.edbID,'DB를 선택 하세요');
		return;
	}
	
	if(!frm.eQueryText.value) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리를 입력 하세요');
		return;
	}

	if(frm.eQueryText.value.indexOf('*') != -1) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리에 *는 포함할 수 없습니다');
		return;
	}
	if(frm.eQueryText.value.toLowerCase().indexOf('delete ') != -1) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리에 delete 는 포함할 수 없습니다');
		return;
	}
	if(frm.eQueryText.value.toLowerCase().indexOf('update ') != -1) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리에 update 는 포함할 수 없습니다');
		return;
	}
	if(frm.eQueryText.value.toLowerCase().indexOf('truncate ') != -1) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리에 truncate 는 포함할 수 없습니다');
		return;
	}
	if(frm.eQueryText.value.toLowerCase().indexOf('insert ') != -1) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리에 insert 는 포함할 수 없습니다');
		return;
	}	
	if(frm.eQueryText.value.toLowerCase().indexOf('drop ') != -1) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리에 drop 는 포함할 수 없습니다');
		return;
	}		
	if(frm.eQueryText.value.toLowerCase().indexOf('create ') != -1) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리에 create 는 포함할 수 없습니다');
		return;
	}		
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		url: 'admin/dbconnect/dbconnect.do?method=listQueryColumn&id=<%=id%>',
		update: $('<%=id%>_listQueryColumn'), 
		onSuccess: function(html,els,resHTML,scripts) {
			if(resHTML.trim() == '') { // 쿼리에 오류가 있으면
				$('<%=id%>').isValidateQuery = false;
				new Element('img',{ 'src' : 'images/x.gif' } ).inject( $('<%=id%>_validateQueryImg').empty());
			} else {
				$('<%=id%>').isValidateQuery = true;
				new Element('img',{ 'src' : 'images/icon-check.gif' } ).inject( $('<%=id%>_validateQueryImg').empty());
			}
		}
	});
	nemoRequest.post(frm);

}

/***********************************************/
/* query 원투원 정보 불러오기 (수정일 경우)
/***********************************************/
$('<%=id%>').showQueryOneToOneEdit = function() {

	var frm = $('<%=id%>_form');

	// 파일정보 표시 이 후 컬럼 정보(원투원)를 불러온다.
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		url: 'admin/dbconnect/dbconnect.do?method=showQueryColumn&id=<%=id%>',
		update: $('<%=id%>_listQueryColumn'), 
		onSuccess: function(html,els,resHTML,scripts) {
			frm.edbID.disabled=true;
			frm.eQueryText.disabled=true;
			$('<%=id%>').isValidateQuery = true;
			$('<%=id%>_validateQueryBtn').setStyle('display','none');
		}
	});
	nemoRequest.get({
		'id': '<%=id%>', 
		'edbID' : $('<%=id%>_form').edbID.value
	});
	
}

/***********************************************/
/* 수정시 쿼리 수정을 체크했을 경우 
/***********************************************/
$('<%=id%>').chkQueryUpdate = function() {
	var frm = $('<%=id%>_form');
	if(frm.eQueryUpdateYN.checked==true){
		frm.edbID.disabled=false;
		frm.eQueryText.disabled=false;
		$('<%=id%>').isValidateQuery = false;
		$('<%=id%>_validateQueryBtn').setStyle('display','block');
	}else{
		frm.edbID.disabled=true;
		frm.eQueryText.disabled=true;
		$('<%=id%>').isValidateQuery = true;
		$('<%=id%>_validateQueryBtn').setStyle('display','none');
	}
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 	
});

</script>