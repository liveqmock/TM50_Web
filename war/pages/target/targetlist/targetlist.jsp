<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>   
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	//관리자라면 
	String isAdmin = LoginInfo.getIsAdmin(request);
	String groupID = LoginInfo.getGroupID(request);
	String userID = LoginInfo.getUserID(request);
	String userAuth = LoginInfo.getUserAuth(request);
	String auth_send_mail = LoginInfo.getAuth_send_mail(request);
	String auth_write_mail = LoginInfo.getAuth_write_mail(request);
	
	String date = null;
	String dateBef = null;
	if(request.getParameter("eSendScheduleDateStart")== null)
	{
		dateBef=DateUtils.getNowAddShortDate(-7);
		date=DateUtils.getDateString();
	}else
	{
		dateBef=request.getParameter("eSendScheduleDateStart");
		date=request.getParameter("eSendScheduleDateEnd");
		
	}
	
	String isTargetUIYN = PropertiesUtil.getStringProperties("configure", "target_ui_yn");
	String isSMSYN = PropertiesUtil.getStringProperties("configure", "mass_sms_yn");
%>
<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:980px">
	<div class="start">
		<ul id="sBookMark"  class="selectBox" onChange="javascript:$('<%=id%>').goBookMark(this.value)">
			<li data="">전체보기</li>			
			<li data="Y">즐겨찾기(Y)</li>
			<li data="N">즐겨찾기(N)</li>
			<li data="D">삭제대상자</li>
		</ul>
	</div>
	<div>
		<ul id="sTargetType"  class="selectBox">
			<li data="">--등록구분--</li>			
			<li data="1">파일업로드</li>
			<li data="2">직접입력</li>
			<li data="3">DB추출</li>
			<li data="4">기존발송추출</li>
		</ul>
	</div>
	<div>
		<ul id="sShareType"  class="selectBox">
			<li data="">--공유여부--</li>			
			<li data="1">비공유</li>
			<li data="2">그룹공유</li>
			<li data="3">전체공유</li>	
		</ul>
	</div>
	<div>
		<c:import url="../../../include_inc/search_targetgroup_inc.jsp">
		</c:import>
	</div>
	<div>
		<ul id="sSearchType"  class="selectBox">
			<li data="t.targetName" select="yes">대상자그룹명</li>			
			<li data="u.userName">작성자</li>	
			<li data="emailType">이메일</li>	
			<li data="phoneType">휴대폰</li>	
			<li data="nameType">고객명</li>			
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="20" />
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	
	
	<div class="right">
		<div class="btn_b"><a id="testEmailMinus11" title="체크된 대상자그룹을 즐겨찾기에 추가/삭제 <br> 또는 삭제 대상자 복원" style ="cursor:pointer" href="javascript:$('<%=id%>').updateBookMark()"><span id="<%=id%>TargetBtn">즐겨찾기처리</span></a></div>
		</div>
		<%if(isAdmin.equals("Y") || auth_send_mail.equals("Y") || auth_write_mail.equals("Y")){ %>
		<div class="right">
			<div class="btn_r" id="<%=id%>_massmailWriteBtn"><a id="testEmailMinus11" title="체크된 대상자그룹으로 대량메일작성" style ="cursor:pointer" href="javascript:$('<%=id%>').massmailWriteWindow()"><span>대량메일작성</span></a></div>
		</div>	
		<%} %>
		<%if(isSMSYN.equalsIgnoreCase("Y")&&(isAdmin.equals("Y") || auth_send_mail.equals("Y") || auth_write_mail.equals("Y"))){ %>
		<div class="right">
			<div class="btn_r" id="<%=id%>_masssmsWriteBtn"><a id="testEmailMinus11" title="체크된 대상자그룹으로 대량SMS작성" style ="cursor:pointer" href="javascript:$('<%=id%>').massSMSWriteWindow()"><span>대량SMS작성</span></a></div>
		</div>	
		<%} %>
	
	<div style="clear:both;width:975px">
			<div class="explain">
				<img src="images/massmail/sending.gif" alt="등록중"/> 등록중
			</div>
			<div class="explain">
				<img src="images/x.gif" alt="등록중 에러"/> 등록중 에러
			</div>
			<div class="explain">
				<img src="images/massmail/finish.gif" alt="등록완료"/> 등록완료
			</div>
			
			<div class="right">
				<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>
			</div>
			<%if(isTargetUIYN.equalsIgnoreCase("Y")){ %>
			<div class="right">
				<a href="javascript:$('<%=id%>').openTargetUI()" class="web20button bigblue">회원검색 추가</a>
			</div>
			<%} %>
			<div class="right">
				<a href="javascript:$('<%=id%>').editWindow(0)" class="web20button bigblue">대상자 추가</a>
			</div>
			<div class="right" id="<%=id%>_deleteBtn" >
				<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink">선택삭제</a>
			</div>
			
			
	</div>
	
</div>

</form>

<div style="clear:both;width:980px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder" onclick="selectAll($('<%=id%>_list_form').eTargetID,this.checked)"/></th>
		<th style="height:30px;width:50px">그룹ID</th>
		<th style="height:30px;">대상자그룹명</th>
		<th style="height:30px;width:100px">작성자</th>
		<th style="height:30px;width:50px">등록구분</th>
		<th style="height:30px;width:100px">공유여부</th>
		<th style="height:30px;width:100px">총대상인원</th>
		<th style="height:30px;width:50px">상태</th>
		<th style="height:30px;width:150px">작성일</th>		
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
	$('<%=id%>TargetBtn').innerHTML = '즐겨찾기처리';
	<%if(isAdmin.equals("Y") || auth_send_mail.equals("Y")){ %>
		$('<%=id%>_massmailWriteBtn').setStyle('display','block');
	<%}%>
	$('<%=id%>_deleteBtn').setStyle('display','block');
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

		url: 'target/targetlist/target.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}


/***********************************************/
/* onetooen 이메일을 선택한 셀렉트 박스가 있는지 체크
/***********************************************/
$('<%=id%>').isSelectedOneToOne = function( oneToOneID ) {

	var frm = $('<%=id%>_form');
	var selectObj = $('<%=id%>_form').elements[oneToOneID];
	if(selectObj.length == 21){
		var valArr = selectObj.value.split('≠');
		if($('<%=id%>').ontooneEmailID == valArr[1]) { // 이메일을 선택한 셀렉트 박스가 있는지
			return true;
		}else if($('<%=id%>').ontooneHpID == valArr[1]){ //휴대폰을 선택한 셀렉트 박스가 있는지
			return true;
		}
	}else{
		for(var i=0; i < selectObj.length; i++ ) {
			var valArr = selectObj[i].value.split('≠');
			if($('<%=id%>').ontooneEmailID == valArr[1]) { // 이메일을 선택한 셀렉트 박스가 있는지
				return true;
			}else if($('<%=id%>').ontooneHpID == valArr[1]){ //휴대폰을 선택한 셀렉트 박스가 있는지
				return true;
			}
			
		}
	}

	return false;

}


/***********************************************/
/* query onetooen 이메일을 선택한 셀렉트 박스가 있는지 체크
/***********************************************/
$('<%=id%>').isSelectedQueryOneToOne = function( oneToOneID ) {
	var frm = $('<%=id%>_form');
	var selectObj = $('<%=id%>_form').elements[oneToOneID];
	if(selectObj.length == 21){
		if($('<%=id%>').ontooneEmailID == selectObj.value) { // 이메일을 선택한 셀렉트 박스가 있는지
			return true;
		}else if($('<%=id%>').ontooneHpID == selectObj.value){  //휴대폰을 선택한 셀렉트 박스가 있는지 
			return true;
		}
		
	}else{
		for(var i=0; i < selectObj.length; i++ ) {
			//alert(selectObj[i].value);
			if($('<%=id%>').ontooneEmailID == selectObj[i].value) { // 이메일을 선택한 셀렉트 박스가 있는지
				return true;
			}else if($('<%=id%>').ontooneHpID == selectObj.value){  //휴대폰을 선택한 셀렉트 박스가 있는지 
				return true;
			}
		}
	}

	return false;

}

/***********************************************/
/* query onetooen 모든 컬럼에 맞게 일대일치환을 했는지 체크 
/***********************************************/
$('<%=id%>').isSelectedQueryOneToOneAll = function( oneToOneID ) {

	var frm = $('<%=id%>_form');
	var selectObj = $('<%=id%>_form').elements[oneToOneID];
	if(selectObj.length == 21){
		if(selectObj.value==''){
			return false;
		}
	}else{
		for(var i=0; i < selectObj.length; i++ ) {
			if(selectObj[i].value==''){
				return false;
			}
		}
	}

	return true;

}

/***********************************************/
/* onetooen 중복선택 방지
/***********************************************/
$('<%=id%>').chkDuplicateSelectFile = function( selObj, selectObjsID, rownum ) {
	
	var frm = $('<%=id%>_form');
	var selectObjs = $('<%=id%>_form').elements[selectObjsID];				
	
	for(var i=0; i < selectObjs.length; i++ ) {
		
		if(selObj.selectedIndex == selectObjs[i].selectedIndex && selObj != selectObjs[i] && selObj.selectedIndex!=0) {
			alert(selObj.options[selObj.selectedIndex].text+'는(은) 이미 선택 하셨습니다' );
			selObj.selectedIndex = 0;
			return; 
		} 
		
	}
	

	return false;

}



/***********************************************/
/* onetooen 중복선택 방지(쿼리)
/***********************************************/
$('<%=id%>').chkDuplicateSelectQuery = function( selObj, selectObjsID, rownum ) {
	
	var frm = $('<%=id%>_form');
	var selectObjs = $('<%=id%>_form').elements[selectObjsID];
	
	if(selObj.selectedIndex == 0) {
		if(selObj.length==selectObjs.length)
		{
				$('<%=id%>_form').eDescript.value = '';
				return;
			
		}	
		else
		{
			$('<%=id%>_form').eDescript[rownum].value = '';
			return; 			
		}
	}
	if(selObj.length==selectObjs.length)
	{
		$('<%=id%>_form').eDescript.value = selObj.options[selObj.selectedIndex].text;
		return;
	}	
			
	
	for(var i=0; i < selectObjs.length; i++ ) {
		
		if(selObj.selectedIndex == selectObjs[i].selectedIndex && selObj != selectObjs[i]) {
			toolTip.showTipAtControlOffset(selectObjs[i],'"'+selObj.options[selObj.selectedIndex].text+'" 는(은) 이미 선택 하셨습니다',{x:-10,y:+90});
			$('<%=id%>_form').eDescript[rownum].value ='';
			selObj.selectedIndex = 0;
			return; 
		} 
		else
			
			$('<%=id%>_form').eDescript[rownum].value = selObj.options[selObj.selectedIndex].text;		
	}
	

	return false;

}
/***********************************************/
/* onetooen 중복선택 방지(기존발송추출)
/***********************************************/
$('<%=id%>').chkDuplicateSelectSended = function( selObj, selectObjsID, rownum ) {
	
	var frm = $('<%=id%>_form');
	var selectObjs = $('<%=id%>_form').elements[selectObjsID];
	
	if(selObj.selectedIndex == 0) {
		if(selObj.length==selectObjs.length)
		{
				$('<%=id%>_form').eSendedDescript.value = '';
				return;
			
		}	
		else
		{
			$('<%=id%>_form').eSendedDescript[rownum].value = '';
			return; 			
		}
	}
	if(selObj.length==selectObjs.length)
	{
		$('<%=id%>_form').eSendedDescript.value = selObj.options[selObj.selectedIndex].text;
		return;
	}	
			
	
	for(var i=0; i < selectObjs.length; i++ ) {
		
		if(selObj.selectedIndex == selectObjs[i].selectedIndex && selObj != selectObjs[i]) {
			toolTip.showTipAtControlOffset(selectObjs[i],'"'+selObj.options[selObj.selectedIndex].text+'" 는(은) 이미 선택 하셨습니다',{x:-10,y:+90});
			$('<%=id%>_form').eSendedDescript[rownum].value ='';
			selObj.selectedIndex = 0;
			return; 
		} 
		else
			
			$('<%=id%>_form').eSendedDescript[rownum].value = selObj.options[selObj.selectedIndex].text;		
	}
	

	return false;

}
/***********************************************/
/* 입력창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq ) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 810,
			//height: $('mainColumn').style.height.toInt(),
			height: $('mainColumn').style.height.toInt(),
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'target/targetlist/target.do?id=<%=id%>&method=edit&targetID='+seq
		}
	);
	
}
/***********************************************/
/* 회원검색UI 수정창 열기
/***********************************************/
$('<%=id%>').editWindowTargetUI = function( seq ) {

	nemoWindow(
			{
				'id': 'targetui',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: 810,
				//height: $('mainColumn').style.height.toInt(),
				height: $('mainColumn').style.height.toInt()-100,
				title: '회원검색UI',
				type: 'modal',
				loadMethod: 'xhr',
				noOtherClose: true,
				contentURL: 'target/targetui/targetui.do?id=targetui&targetID='+seq
			}
		);
	
}

/***********************************************/
/* 회원검색UI 추가창 열기
/***********************************************/
$('<%=id%>').openTargetUI = function(  ) {

	nemoWindow(
			{
				'id': 'targetui',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: 810,
				//height: $('mainColumn').style.height.toInt(),
				height: $('mainColumn').style.height.toInt()-100,
				title: '회원검색UI',
				type: 'modal',
				loadMethod: 'xhr',
				noOtherClose: true,
				contentURL: 'target/targetui/targetui.do?id=targetui'
			}
		);	
}
/***********************************************/
/* 대량메일 작성창 열기
/***********************************************/
$('<%=id%>').massmailWriteWindow = function( seq ) {
	var frm = $('<%=id%>_list_form');
	var sfrm = $('<%=id%>_sform');
	var checked = isChecked( frm.elements['eTargetID']  );
	if(sfrm.sBookMark.value == 'D'){
		toolTip.showTipAtControl(frm.sCheckAll,'삭제 대상자는 사용하실 수 없습니다.');
		return;
	}
	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'해당 자료를 먼저 선택하세요');
		return;
	}
	var target_ids = "";
	var except_ids = "";
	var j = 0;
	for(var i=0;i <frm.elements['eTargetID'].length;i++){
		if( frm.elements['eTargetID'][i].checked == true){
			if(frm.elements['<%=id%>writeYN'][i].value == 'Y'){;
				if(j == 0){
					target_ids = frm.elements['eTargetID'][i].value;
				}else{
					target_ids = target_ids+","+frm.elements['eTargetID'][i].value;
				}
				j++;
			}else{
				except_ids = except_ids + frm.elements['eTargetID'][i].value+" ";
			}
			
		}
	}
	if(target_ids == ''){
		if(frm.elements['<%=id%>writeYN'].value == 'Y'){
			target_ids = frm.elements['eTargetID'].value;
		}else{
			alert("사용 할 수 없는 대상자 그룹입니다.");
			return;
		}
	}
	if(except_ids != ''){
		alert("사용 할 수 없는 대상자 그룹이 포함 되어 있습니다. \r\n 제외 그룹 ID : "+except_ids);
	}
	nemoWindow(
		{
		    'id': 'massmailWrite',
			busyEl: 'massmailWrite', // 창을 열기까지 busy 가 표시될 element
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			title: '대량메일작성',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'massmail/write/massmail.do?method=editAddTarget&id=massmailWrite&target_ids='+target_ids
		}
	);
	
}


/***********************************************/
/* 대량SMS 작성창 열기
/***********************************************/
$('<%=id%>').massSMSWriteWindow = function( seq ) {
	var frm = $('<%=id%>_list_form');
	var sfrm = $('<%=id%>_sform');
	var checked = isChecked( frm.elements['eTargetID']  );
	if(sfrm.sBookMark.value == 'D'){
		toolTip.showTipAtControl(frm.sCheckAll,'삭제 대상자는 사용하실 수 없습니다.');
		return;
	}
	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'해당 자료를 먼저 선택하세요');
		return;
	}
	var target_ids = "";
	var except_ids = "";
	var j = 0;
	for(var i=0;i <frm.elements['eTargetID'].length;i++){
		if( frm.elements['eTargetID'][i].checked == true){
			if(frm.elements['<%=id%>writeYN'][i].value == 'Y'){;
				if(j == 0){
					target_ids = frm.elements['eTargetID'][i].value;
				}else{
					target_ids = target_ids+","+frm.elements['eTargetID'][i].value;
				}
				j++;
			}else{
				except_ids = except_ids + frm.elements['eTargetID'][i].value+" ";
			}
			
		}
	}
	if(target_ids == ''){
		if(frm.elements['<%=id%>writeYN'].value == 'Y'){
			target_ids = frm.elements['eTargetID'].value;
		}else{
			alert("사용 할 수 없는 대상자 그룹입니다.");
			return;
		}
	}
	if(except_ids != ''){
		alert("사용 할 수 없는 대상자 그룹이 포함 되어 있습니다. \r\n 제외 그룹 ID : "+except_ids);
	}
	nemoWindow(
		{
		    'id': 'masssmsWrite',
			busyEl: 'masssmsWrite', // 창을 열기까지 busy 가 표시될 element
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			title: '대량SMS작성',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'masssms/write/masssms.do?method=editAddTarget&id=masssmsWrite&target_ids='+target_ids
		}
	);
	
}

/***********************************************/
/* 파일 업로드 랜더링
/***********************************************/
$('<%=id%>').renderUpload = function() {
	
	$('<%=id%>').uploader = new SwFileUpload('<%=id%>UploadFlash', {
		width: 450,
		container: '<%=id%>uploadWrapper',
		fileTypeName: '파일업로드(csv,txt,xls,xlsx)',
		allowFileType: '*.csv;*.txt;*.xls;*.xlsx',
		uploadPage: 'target/targetlist/target.do?method=fileUpload',
		onComplete: function( isError, fileNameArray ) { // 업로드 완료

			$('<%=id%>_uploadBtn').setStyle('display','block');
			$('<%=id%>_saveBtn').setStyle('display','block');
			
			$('<%=id%>').showFileInfo( $('<%=id%>').showFileOneToOne );
				
		},
		onNotExistsUploadFile: function() { // 업로드 할 파일이 없을때
			//$('<%=id%>_uploadBtn').setStyle('display','block');
			//$('<%=id%>_saveBtn').setStyle('display','block');
			alert('업로드할 파일을 선택해 주시기 바랍니다');
			//toolTip.showTipAtControlOffset( $('<%=id%>_uploadBtn'),'업로드할 파일을 선택해 주시기 바랍니다',{x:100,y:0});
		}
		
	}).render();

}


/***********************************************/
/* 파일 업로드 저장 버튼 클릭
/***********************************************/

$('<%=id%>').uploadFile = function( eTargetID ) {
	
	// 버튼을 숨긴다.
	//$('<%=id%>_uploadBtn').setStyle('display','none');
	//$('<%=id%>_saveBtn').setStyle('display','none');
	
	// 추가 일때 필요한 key
	$('<%=id%>').uploader.uploadKey = '<%=request.getSession().getId()%>'+(new Date().getTime());
	
	// 파라미터 셋팅
	$('<%=id%>').uploader.setParameter({ 'id': '<%=id%>', 'targetID' : eTargetID });
	// 업로드
	$('<%=id%>').uploader.upload();
	
	
	
}	

/***********************************************/
/*파일 다운로드
/***********************************************/
$('<%=id%>').downloadFile = function( uploadKey ) {


		
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window

		url: 'target/targetlist/target.do?method=fileDownload',
		 
		onSuccess: function(html,els,resHTML,scripts) {
			document.location.href = 'target/targetlist/target.do?method=fileDownload&uploadKey='+uploadKey+'&reDirectDownload=Y';
		}
	});
	nemoRequest.get({'id': '<%=id%>', 'uploadKey': uploadKey});
	

}

/***********************************************/
/* 파일 정보 보이기
/***********************************************/
$('<%=id%>').showFileInfo = function( nextFunc ) {

	// 업로드가 완료되면 처리될 action
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		updateWindowId: $('<%=id%>uploaded'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'target/targetlist/target.do?method=getFileInfo',
		update: $('<%=id%>uploaded'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {

			if(nextFunc) nextFunc();
						
		}
	});
	nemoRequest.get({
		'id': '<%=id%>', 
		'targetID': $('<%=id%>_form').eTargetID.value,
		'uploadKey': $('<%=id%>').uploader.uploadKey
	});
	
}

/***********************************************/
/* 파일 원투원 정보 불러오기
/***********************************************/
$('<%=id%>').showFileOneToOne = function( ) {

	$('<%=id%>_fileOneToOne').empty();
	$('<%=id%>_directOneToOne').empty();
	$('<%=id%>_queryOneToOne').empty();
	$('<%=id%>_sendedOneToOne').empty();
	// 파일정보 표시 이 후 컬럼 정보(원투원)를 불러온다.
	nemoRequest.init({
		updateWindowId: $('<%=id%>_fileOneToOne'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'target/targetlist/target.do?method=getFileHeader',
		update: $('<%=id%>_fileOneToOne'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
		
		}
	});
	nemoRequest.get({
		'id': '<%=id%>', 
		'targetID': $('<%=id%>_form').eTargetID.value,
		'uploadKey': $('<%=id%>').uploader.uploadKey
	});		
}




/***********************************************/
/* 직접입력 원투원 정보 불러오기
/***********************************************/
$('<%=id%>').showDirectOneToOne = function(targetID) {

	$('<%=id%>_fileOneToOne').empty();
	$('<%=id%>_directOneToOne').empty();
	$('<%=id%>_queryOneToOne').empty();
	$('<%=id%>_sendedOneToOne').empty();
	// 파일정보 표시 이 후 컬럼 정보(원투원)를 불러온다.
	
	var frm = $('<%=id%>_form');
	if(frm.eDirectText.value==''){
		toolTip.showTipAtControl(frm.eDirectText,'먼저 직접입력을 하시기 바랍니다.');
		return;
	}
	
	nemoRequest.init({
		updateWindowId: $('<%=id%>_directOneToOne'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'target/targetlist/target.do?method=getDirectHeader&id=<%=id%>&targetID='+targetID,
		update: $('<%=id%>_directOneToOne'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			if(resHTML.trim() == '') { //  오류가 있으면
				$('<%=id%>').isValidateDirect = false;
				new Element('img',{ 'src' : 'images/x.gif' } ).inject( $('<%=id%>_validateDirectImg').empty());
			}
			else
			{
				$('<%=id%>').isValidateDirect = true;
				new Element('img',{ 'src' : 'images/icon-check.gif' } ).inject( $('<%=id%>_validateDirectImg').empty());
			}
		}
	});
	nemoRequest.post(frm);

}


/***********************************************/
/* 직접입력  원투원 정보 불러오기(수정)
/***********************************************/
$('<%=id%>').showDirectOneToOnEdit = function(targetID) {

		
	var frm = $('<%=id%>_form');
	
	
	nemoRequest.init({
		updateWindowId: $('<%=id%>_directOneToOne'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'target/targetlist/target.do?method=getDirectHeader&id=<%=id%>&targetID='+targetID,
		update: $('<%=id%>_directOneToOne'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			$('<%=id%>').isValidateDirect = false;
		}
	});
	nemoRequest.post(frm);

}




/***********************************************/
/* 기존발송 원투원 정보 불러오기
/***********************************************/
$('<%=id%>').showSendedOneToOne = function() {

	$('<%=id%>_fileOneToOne').empty();
	$('<%=id%>_directOneToOne').empty();
	$('<%=id%>_queryOneToOne').empty();
	$('<%=id%>_sendedOneToOne').empty();
	
	// 파일정보 표시 이 후 컬럼 정보(원투원)를 불러온다.
	
	var frm = $('<%=id%>_form');
	if(!frm.eConnectedDB.value){
		toolTip.showTipAtControl(frm.eConnectedDB.listBox.displayControl,'먼저 일대일치환연동을 선택하세요 ');
		return;
	}
	
	nemoRequest.init({
		updateWindowId: $('<%=id%>_sendedOneToOne'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'target/targetlist/target.do?method=makeOneToOneForSended&id=<%=id%>',
		update: $('<%=id%>_sendedOneToOne'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
		
		}
	});
	nemoRequest.post(frm);		
}
/***********************************************/
/* query 원투원 정보 불러오기 (수정일 경우)
/***********************************************/
$('<%=id%>').showSendedOneToOneEdit = function() {

	var frm = $('<%=id%>_form');

	// 파일정보 표시 이 후 컬럼 정보(원투원)를 불러온다.
	nemoRequest.init({
		updateWindowId: $('<%=id%>_sendedOneToOne'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'pages/target/targetlist/targetlist_proc.jsp?method=makeOneToOneForSended',
		update: $('<%=id%>_sendedOneToOne'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
		}
	});
	nemoRequest.get({
		'id': '<%=id%>', 
		'targetID': $('<%=id%>_form').eTargetID.value
	});
	
}

$('<%=id%>').getConnectedTable = function(seq){
	
	var frm = $('<%=id%>_form');
	if(seq != ''){
		frm.eConnectedTable.value = eval("frm.eConnectedTable_"+seq+".value");
	}
}
/***********************************************/
/* DB접근키 확인버튼 클릭
/***********************************************/
$('<%=id%>').dbAccessKeyConfirm = function() {
	
	var frm = $('<%=id%>_form');
	frm.eDbID.value = frm.eSelectDbID.value;
	
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		url: 'target/targetlist/target.do?id=<%=id%>&method=checkConfirm',
		onSuccess: function(html,els,resHTML,scripts) {
			
			if(resHTML == 'insertSuccess') { // 입력한 값이 일치하면
				$('<%=id %>_confirmTable').setStyle('display', 'none');
				frm.eSelectDbID.disabled = true;
				frm.eQueryText.disabled = false;
				frm.eCountQueryText.disabled = false;
			} else if(resHTML == 'updateSuccess') {
				frm.eQueryUpdateYN.disabled = false;
				$('<%=id %>_confirmTable').setStyle('display', 'none');
				$('<%=id %>_queryTextTable').setStyle('display', 'block');
				$('<%=id%>_onetooneInfo').setStyle('display', 'block');
				$('<%=id%>_allBtn').setStyle('visibility', '');
			}
			
			if(resHTML == 'dbfalse') {
				alert('DB를 선택해 주세요.');
			} else if(resHTML == 'false') {
				alert('DB접근키가 일치하지 않습니다.');
				frm.eDbAccessKeyConfirmer.value = '';
			}
		}
	});
	
	nemoRequest.post(frm);
	
}
/***********************************************/
/* 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function( targetID ) {
	var frm = $('<%=id%>_form');
	var goUrl = '';
	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}

	copyForm( $('<%=id%>_rform'), frm );

	// 파일업로드 -------------------------------------------------
	if(frm.eTargetType[0].checked==true){
		// 파일업로드가 있는지 여부
		
		if($('<%=id%>uploaded').getChildren().length == 0) {
			toolTip.showTipAtControl( $('<%=id%>_uploadBtn'),'대상자 파일을 업로드 하세요');
			return;
		}
		// onetoone 이메일을 선택한 셀렉트 박스가 있는지 체크
		if(!$('<%=id%>').isSelectedOneToOne( 'eFileOneToOne' )) {
			//toolTip.showTipAtControl($('<%=id%>_fileOneToOne'),'일대일치환에서 이메일은 필수 입니다');
			alert('일대일치환에서 \'이메일\'(OR 휴대폰)은 필수 입니다. 일대일치환 설정을 확인하세요 ');
			return;
		}
		var method = "insertFile";
		if(targetID>0){
			method = "updateFile";
		}
		goUrl = 'target/targetlist/target.do?id=<%=id%>&method='+method+'&targetID='+targetID+'&uploadKey='+$('<%=id%>').uploader.uploadKey;
	}

	//직접입력 -------------------------------------------------------
	else if(frm.eTargetType[1].checked==true){
		if(frm.eDirectText.value==''){
			toolTip.showTipAtControl( frm.eDirectText,'직접입력창에 작성하세요');
			return;
		}
		if(!$('<%=id%>').isValidateDirect) {
			toolTip.showTipAtControl($('<%=id%>_validateDirectBtn'),'입력확인 버튼을 눌러 주시기 바랍니다');
			return;
		}
		// onetoone 이메일을 선택한 셀렉트 박스가 있는지 체크
		if(!$('<%=id%>').isSelectedOneToOne( 'eDirectOneToOne' )) {
			//toolTip.showTipAtControl($('<%=id%>_directOneToOne'),'일대일치환에서 이메일은 필수 입니다');
			alert('일대일치환에서 \'이메일\'(OR 휴대폰)은 필수 입니다. 일대일치환 설정을 확인하세요 ');
			return;
		}
		var method = "insertDirect";
		if(targetID>0){
			method = "updateDirect";
		}
		goUrl = 'target/targetlist/target.do?id=<%=id%>&method='+method+'&targetID='+targetID;	
	}
		
	//DB추출 -------------------------------------------------------
	else if(frm.eTargetType[2].checked==true){

		//db 종류를 선택 했는지..
		if(!frm.eSelectDbID.value) {
			toolTip.showTipAtControl(frm.eSelectDbID.listBox.displayControl,'DB를 선택 하세요');
			return;
		}

		if(targetID>0){
			if(!frm.eQueryUpdateYN.checked==true){
				if(!frm.eQueryText.value) {
					toolTip.showTipAtControl(frm.eQueryText,'쿼리를 입력 하세요');
					return;
				}
				if(!frm.eCountQueryText.value) {
					toolTip.showTipAtControl(frm.eCountQueryText,'인원 수 계산 쿼리를 입력 하세요');
					return;
				}
				if(!$('<%=id%>').isValidateQuery) {
					toolTip.showTipAtControl($('<%=id%>_validateQueryBtn'),'쿼리 검증 버튼을 눌러 주시기 바랍니다');
					return;
				}
			
				if(!$('<%=id%>').isValidateCountQuery) {
					toolTip.showTipAtControl($('<%=id%>_validateCountQueryBtn'),'인원수 계산 버튼을 눌러 주시기 바랍니다');
					return;
				}
			}
			
		}else{
			if(!frm.eQueryText.value) {
				toolTip.showTipAtControl(frm.eQueryText,'쿼리를 입력 하세요');
				return;
			}

			if(!frm.eCountQueryText.value) {
				toolTip.showTipAtControl(frm.eCountQueryText,'인원 수 계산 쿼리를 입력 하세요');
				return;
			}				
		}

		if(!$('<%=id%>').isValidateQuery) {
			toolTip.showTipAtControl($('<%=id%>_validateQueryBtn'),'쿼리 검증 버튼을 눌러 주시기 바랍니다');
			return;
		}
	
		if(!$('<%=id%>').isValidateCountQuery) {
			toolTip.showTipAtControl($('<%=id%>_validateCountQueryBtn'),'인원수 계산 버튼을 눌러 주시기 바랍니다');
			return;
		}

		// onetoone 이메일을 선택한 셀렉트 박스가 있는지 체크
		if(!$('<%=id%>').isSelectedQueryOneToOne( 'eQueryOneToOne' )) {
			//toolTip.showTipAtControl($('<%=id%>_queryOneToOne'),'일대일치환에서 이메일은 필수 입니다');
			alert('일대일치환에서 \'이메일\'(OR 휴대폰)은 필수 입니다. 일대일치환 설정을 확인하세요 ');
			return;
		}

		//모든 필드에 일대일 치환을 선택했는지 확인
		if(!$('<%=id%>').isSelectedQueryOneToOneAll( 'eQueryOneToOne' )) {
			//toolTip.showTipAtControl($('<%=id%>_queryOneToOne'),'일대일치환을 선택하지 않은 컬럼이 있습니다.');
			alert('일대일치환을 선택하지 않은 컬럼이 있습니다.');
			return;
		}	

		var method = "insertQuery";
		if(targetID>0){
			method = "updateQuery";
		}
		
		goUrl = 'target/targetlist/target.do?id=<%=id%>&method='+method+'&targetID='+targetID;	
	}
	//기존발송추출 -------------------------------------------------------
	else if(frm.eTargetType[3].checked==true){
		//대상 기간 선택 여부를 확인 한다.
		if(!frm.eSendedDate.value) {
			toolTip.showTipAtControl(frm.eSendedDate,'대상기간을 선택하세요');
			return;
		}
		//연동 DB 선택 여부를 확인 한다.
		if(!frm.eConnectedDB.value){
			toolTip.showTipAtControl(frm.eConnectedDB.listBox.displayControl,'먼저 일대일치환연동을 선택하세요 ');
			return;
		}
		// onetoone 이메일을 선택한 셀렉트 박스가 있는지 체크
		if(!$('<%=id%>').isSelectedQueryOneToOne( 'eQueryOneToOne' )) {
			//toolTip.showTipAtControl($('<%=id%>_sendedOneToOne'),'일대일치환에서 이메일은 필수 입니다');
			alert('일대일치환에서 \'이메일\'(OR 휴대폰)은 필수 입니다. 일대일치환 설정을 확인하세요 ');
			return;
		}

		//모든 필드에 일대일 치환을 선택했는지 확인
		if(!$('<%=id%>').isSelectedQueryOneToOneAll( 'eQueryOneToOne' )) {
			//toolTip.showTipAtControl($('<%=id%>_sendedOneToOne'),'일대일치환을 선택안한 컬럼이 있습니다.');
			alert('일대일치환을 선택하지 않은 컬럼이 있습니다.');
			return;
		}	
	
		var method = "insertSended"; 
		if(targetID>0){
			method = "updateSended";
		}

		goUrl = 'target/targetlist/target.do?id=<%=id%>&method='+method+'&targetID='+targetID;
	}
	//-----------------------------------------------------------

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: $('<%=id%>') // 완료후 버튼,힌트 가 랜더링될 window
		, url: goUrl
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			closeWindow( $('<%=id%>_modal') );
			$('<%=id%>').list();
		}
	});
	nemoRequest.post(frm);
}


/***********************************************/
/*쿼리 검증을 누르면 원투원을 가져온다
/***********************************************/
$('<%=id%>').testQuery = function() {
	$('<%=id%>').getOnoToOneForQuery( $('<%=id%>').getCountQuery );
}


/***********************************************/
/*원투원을 가져온다
/***********************************************/
$('<%=id%>').getOnoToOneForQuery = function( nextFunc ) {
	var frm = $('<%=id%>_form');
	if(!frm.eSelectDbID.value) {
		toolTip.showTipAtControl(frm.eQueryText,'DB를 선택 하세요');
		return;
	}
	if(!frm.eQueryText.value) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리를 입력 하세요');
		return;
	}
	//if(frm.eQueryText.value.indexOf('*') != -1) {
	//	toolTip.showTipAtControl(frm.eQueryText,'쿼리에 *는 포함할 수 없습니다');
	//	return;
	//}
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
	if(frm.eQueryText.value.toLowerCase().indexOf('order by ') != -1) {
		toolTip.showTipAtControl(frm.eQueryText,'쿼리에 order by 는 포함할 수 없습니다');
		return;
	}		


	
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		url: 'target/targetlist/target.do?method=makeOneToOneForQuery&id=<%=id%>',
		update: $('<%=id%>_queryOneToOne'), 
		onSuccess: function(html,els,resHTML,scripts) {

		if(resHTML.trim() == '') { // 쿼리에 오류가 있으면
			$('<%=id%>').isValidateQuery = false;

			new Element('img',{ 'src' : 'images/x.gif' } ).inject( $('<%=id%>_validateQueryImg').empty());
			frm.eCountQueryText.value = '';
			
			
		} else {
			$('<%=id%>').isValidateQuery = true;
			
			new Element('img',{ 'src' : 'images/icon-check.gif' } ).inject( $('<%=id%>_validateQueryImg').empty());

			if(nextFunc) nextFunc(); //카운트 쿼리 가져오기 실행
		}

		$('<%=id%>').isValidateCountQuery = false;
		frm.eTargetCount_edit.value = '';
		$('<%=id%>_validateCountQueryImg').empty();
		

		}
	});


	nemoRequest.post(frm);

}

/***********************************************/
/* query 원투원 정보 불러오기 (수정일 경우)
/***********************************************/
$('<%=id%>').showQueryOneToOneEdit = function() {

	//$('<%=id%>_fileOneToOne').empty();
	//$('<%=id%>_queryOneToOne').empty();
	var frm = $('<%=id%>_form');

	// 파일정보 표시 이 후 컬럼 정보(원투원)를 불러온다.
	nemoRequest.init({
		updateWindowId: $('<%=id%>_queryOneToOne'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'pages/target/targetlist/targetlist_proc.jsp?method=makeOneToOneForQuery',
		update: $('<%=id%>_queryOneToOne'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			frm.eSelectDbID.disabled=true;
			frm.eQueryText.disabled=true;
			frm.eCountQueryText.disabled=true;
			$('<%=id%>').isValidateQuery = true;
			$('<%=id%>').isValidateCountQuery = true;
			$('<%=id%>_validateQueryBtn').setStyle('display','none');
			$('<%=id%>_validateCountQueryBtn').setStyle('display','none');
		}
	});
	nemoRequest.get({
		'id': '<%=id%>', 
		'targetID': $('<%=id%>_form').eTargetID.value
	});
	
}

/***********************************************/
/* 수정시 쿼리 수정을 체크했을 경우 
/***********************************************/
$('<%=id%>').chkQueryUpdate = function() {
	var frm = $('<%=id%>_form');
	if(frm.eQueryUpdateYN.checked==true){
		frm.eQueryText.disabled=false;
		frm.eCountQueryText.disabled=false;
		$('<%=id%>').isValidateQuery = false;
		$('<%=id%>').isValidateCountQuery = false;
		$('<%=id%>_validateQueryBtn').setStyle('display','block');
		$('<%=id%>_validateCountQueryBtn').setStyle('display','block');
		
	}else{
		frm.eSelectDbID.disabled=true;
		frm.eQueryText.disabled=true;
		frm.eCountQueryText.disabled=true;
		$('<%=id%>').isValidateQuery = true;
		$('<%=id%>').isValidateCountQuery = true;
		$('<%=id%>_validateQueryBtn').setStyle('display','none');
		$('<%=id%>_validateCountQueryBtn').setStyle('display','none');
		
	}
}

/***********************************************/
/*카운트 쿼리를  가져온다
/***********************************************/
$('<%=id%>').getCountQuery = function( ) {

		var frm = $('<%=id%>_form');
	
	
		
	
		if(!frm.eSelectDbID.value) {
			toolTip.showTipAtControl(frm.eSelectDbID.listBox.displayControl,'DB를 선택 하세요');
			return;
		}
		
		if(!frm.eQueryText.value) {
			toolTip.showTipAtControl(frm.eQueryText,'쿼리를 입력 하세요');
			return;
		}
	
		nemoRequest.init({
			busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
			url: 'target/targetlist/target.do?method=getCountQuery&id=<%=id%>',
			onSuccess: function(html,els,resHTML,scripts) {

			if(resHTML.trim() == '') { // 쿼리에 오류가 있으면
				//$('<%=id%>').isValidateCountQuery = false;
				//new Element('img',{ 'src' : 'images/x.gif' } ).inject( $('<%=id%>_validateQueryImg').empty());
				
			} else {
				//$('<%=id%>').isValidateCountQuery = true;
				//new Element('img',{ 'src' : 'images/icon-check.gif' } ).inject( $('<%=id%>_validateQueryImg').empty());
			}

			// 카운트 쿼리에 담는다.
			frm.eCountQueryText.value = resHTML;
			}
		});
	
	nemoRequest.post(frm);

}


/***********************************************/
/* 카운트 쿼리 실행
/***********************************************/
$('<%=id%>').runCountQuery = function() {
	var frm = $('<%=id%>_form');
	if(!frm.eSelectDbID.value) {
		toolTip.showTipAtControl(frm.eSelectDbID.listBox.displayControl,'DB를 선택 하세요');	
		return;
	}	

	
	if(!frm.eCountQueryText.value) {
		toolTip.showTipAtControl(frm.eCountQueryText,'인원수 계산 쿼리를 입력 하세요');
		return;
		
	} else {
		if(!$('<%=id%>').isValidateQuery || !frm.eCountQueryText.value ) {
			toolTip.showTipAtControl($('<%=id%>_validateQueryBtn'),'인원계산 쿼리를 자동 생상하기 위해 쿼리 검증 버튼을 눌러 주시기 바랍니다');
			return;
		}	
	}
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		url: 'target/targetlist/target.do?method=runCountQuery&id=<%=id%>',
		onSuccess: function(html,els,resHTML,scripts) {

		if(resHTML.trim() == '') { // 쿼리에 오류가 있으면
			$('<%=id%>').isValidateCountQuery = false;

			frm.eTargetCount.value = '';

			new Element('img',{ 'src' : 'images/x.gif' } ).inject( $('<%=id%>_validateCountQueryImg').empty());
			
		} else {
			$('<%=id%>').isValidateCountQuery = true;

			new Element('img',{ 'src' : 'images/icon-check.gif' } ).inject( $('<%=id%>_validateCountQueryImg').empty());
			
			frm.eTargetCount_edit.value = resHTML;
			

		}
	}
	});
	nemoRequest.post(frm);
}


/***********************************************/
/* 북마크(즐겨찾기로 이동)
/***********************************************/
$('<%=id%>').goBookMark = function(seq){
	if(seq == 'D'){
		$('<%=id%>TargetBtn').innerHTML = '선택 그룹복원';
		$('<%=id%>_massmailWriteBtn').setStyle('display','none');
		$('<%=id%>_deleteBtn').setStyle('display','none');
	}else{
		$('<%=id%>TargetBtn').innerHTML = '즐겨찾기처리';
		$('<%=id%>_massmailWriteBtn').setStyle('display','block');
		$('<%=id%>_deleteBtn').setStyle('display','block');
	}
	$('<%=id%>').list();
}


/***********************************************/
/*대상자  미리보기  창 열기
/***********************************************/
$('<%=id%>').previewWindow = function( seq, ing ) {

	

	var frm = $('<%=id%>_list_form');
	var tagTr = document.getElementById("eTarget"+seq);
	var state = tagTr.getAttribute("state");

	if(state == "2") {
		alert('대상자 설정에 오류가 있습니다');
		return;
	}
	if(state == "1") {
		alert('처리중인 자료는 확인할 수 없습니다');
		return;
	}
	nemoWindow(
			{
				'id': '<%=id%>_preview_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 700,
				height: $('mainColumn').style.height.toInt(),
				title: '대상자 미리보기',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/target/targetlist/target_preview_list.jsp?method=preview&id=<%=id%>_preview_modal&targetID='+seq
				
			}
		);
	
}

/***********************************************/
/* 선택 - 북마크(즐겨찾기)
/***********************************************/
$('<%=id%>').updateBookMark = function( ) {

	var frm = $('<%=id%>_list_form');
	var sfrm = $('<%=id%>_sform');
	var checked = isChecked( frm.elements['eTargetID']  );



	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'해당 자료를 먼저 선택하세요');
		return;
	}

	var seq = "N";
	if(sfrm.sBookMark.value=='' || sfrm.sBookMark.value=='N'){
		if(!confirm("선택하신 "+checked+"개의 자료를  즐겨찾기에 추가하사겠습니까?")) return;
		seq = "Y";
	}else if(sfrm.sBookMark.value=='Y'){
		if(!confirm("선택하신 "+checked+"개의 자료를  즐겨찾기에서 삭제하사겠습니까?")) return;
	}else{
		if(!confirm("선택하신 "+checked+"개의 자료를  복원 하시겠습니까?")) return;
	}
	

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eTargetID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'target/targetlist/target.do?method=updateBookMark&id=<%=id%>&bookMarkYN='+seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

			$('<%=id%>_list_form').sCheckAll.checked = false;

		}
	});
	nemoRequest.post(frm);

}

/***********************************************/
/* 선택 - 삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function( ) {

	var frm = $('<%=id%>_list_form');
	var sfrm = $('<%=id%>_sform');
	var checked = isChecked( frm.elements['eTargetID']  );



	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'해당 자료를 먼저 선택하세요');
		return;
	}
	var target_ids = "";
	var except_ids = "";
	for(var i=0;i <frm.elements['eTargetID'].length;i++){
		if( frm.elements['eTargetID'][i].checked == true){
			target_ids = "check";
			if(frm.elements['<%=id%>useYN'][i].value == 'N'){;
				frm.elements['eTargetID'][i].checked = false;
				checked = checked -1;
				except_ids = except_ids + frm.elements['eTargetID'][i].value+" ";
			}

		}
	}
	if(target_ids == ''){
		if(frm.elements['<%=id%>useYN'].value == 'N'){;
			alert("처리 권한이 없습니다.");
			return;
		}
	}
	if(except_ids !=''){
		alert("권한이 없어 제외된 대상자 그룹이 있습니다. \r그룹 ID : "+except_ids);
	}
	if(checked == 0){
		return;
	}
	var seq = "D";
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;
	
	

	// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
	if(frm.elements['eTargetID'].length == checked) {
		$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
	}

	copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'target/targetlist/target.do?method=updateBookMark&id=<%=id%>&bookMarkYN='+seq
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

			$('<%=id%>_list_form').sCheckAll.checked = false;

		}
	});
	nemoRequest.post(frm);

}

/***********************************************/
/* 팝업메뉴 create
/***********************************************/
$('<%=id%>').createPopup = function() {

	$('<%=id%>').popup = new PopupMenu('<%=id%>');

	$('<%=id%>').popup.add('수정', 
		function(target,e) {
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("target_id") );
		}
	);

	$('<%=id%>').popup.addSeparator();

	$('<%=id%>').popup.add('삭제', function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("target_id"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("user_id"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("group_id") ); 
		}
	);

	$('<%=id%>').popup.setSize(150, 0);

}
/***********************************************/
/* 삭제 처리 
/***********************************************/
$('<%=id%>').deleteData = function ( seq, seq2, seq3 ){
	if(!('<%=isAdmin%>' == 'Y' || seq2 == '<%=userID%>' || ('<%=userAuth%>' == '2' && seq3 == '<%=groupID%>'))){
		alert("삭제 권한이 없습니다."); 
		return;
	}
	if(!confirm("정말로 삭제 하시겠습니까?")) return;
	nemoRequest.init( 
	{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

				, url: 'target/targetlist/target.do?method=delete&id=<%=id%>&targetID='+ seq
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
					// 수정창을 닫는다
					if($('<%=id%>_modal')) closeWindow( $('<%=id%>_modal') );

				}
	});
	nemoRequest.post($('<%=id%>_rform'));
}



var els = null;
var httpRequest = null;
$('<%=id%>').showState = function() {
	
	var temp = document.getElementById("<%=id%>_grid_content");
	var list = temp.childNodes;
	var json = [{target_id:[]}];
	for(var i=0;i<list.length;i++)
	{
		els = list.item(i);
		if(els.nodeName=='TR')
		{
			if(els.getAttribute("state")=='1')
			{
				json[0].target_id.push(els.getAttribute("target_id"));
			}
		}	
	}
	$('<%=id%>').sendRequest("returnTargetState.jsp", "target_id="+JSON.encode( json ), $('<%=id%>').callbackFunction, "GET");
	
}

$('<%=id%>').callbackFunction = function() {
	
	if(httpRequest.readyState==4)
	{
		if(httpRequest.status==200)
		{			
			var st = eval("("+httpRequest.responseText+")");
			for(var i=0;i<st[0].target_id.length;i++)
			{
				var tagDiv = document.getElementById("targetState"+st[0].target_id[i]);
				var tagTr = document.getElementById("eTarget"+st[0].target_id[i]);
				var t = tagDiv.childNodes;
				for(var y =0; y < t.length ;y++)
				{	var stemp = t.item(y);
					if(stemp.nodeName=='IMG')
					{
						var tagImg = stemp;
						if(st[0].state[i]=='2')
						{
							
							tagImg.setAttribute("src","images/x.gif");					
							tagImg.setAttribute("title","등록중 에러");				
							toolTip.init(tagDiv);
							tagTr.setAttribute("state",st[0].state[i]); 
							var tagDiv_count = document.getElementById("count"+st[0].target_id[i]);							
							tagDiv_count.innerHTML=st[0].count[i]; 
							
						}
						else if(st[0].state[i]=='3')
						{
							tagImg.setAttribute("src","images/massmail/finish.gif");					
							tagImg.setAttribute("title","등록완료");
							toolTip.init(tagDiv);
							tagTr.setAttribute("state",st[0].state[i]); 
							var tagDiv_count = document.getElementById("count"+st[0].target_id[i]);	
							tagDiv_count.innerHTML=st[0].count[i]; 
						}
					}
					if(stemp.nodeName=='INPUT')
					{
						var tagInp = stemp;
						if(st[0].state[i]=='2')
						{	
							tagInp.setAttribute("value","N");
						}
						else if(st[0].state[i]=='3')
						{
							tagInp.setAttribute("value","Y");
						}
					}
				}
			}
		}		
		
	}	
}

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


/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();	
	initFormValue($('<%=id%>_sform'));
	$('<%=id%>').list(); 
	$('<%=id%>').createPopup();
	<%=id%>ServerTimer = $('<%=id%>').showState.periodical(60000);
	MochaUI.Windows.instances.get('<%=id%>').addEvent('onClose',function() { $clear(<%=id%>ServerTimer) } );
});

</script>


