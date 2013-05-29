<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="web.common.util.*"%> 
<%
	String id = request.getParameter("id");


	String isAdmin = LoginInfo.getIsAdmin(request);
	String userAuth = LoginInfo.getUserAuth(request);
	String userID =LoginInfo.getUserID(request);
	String groupID = LoginInfo.getGroupID(request);
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:950px">
	<div class="start">
		<ul id="sSearchType"  class="selectBox">
			<li data="intermailTitle" select="yes">연동메일명</li>			
			<li data="userName">작성자명</li>			
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="15" />
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	
	<div style="clear:both;width:950px">
			<div class="explain">
				<img src="images/massmail/approve.gif" alt="승인이미지"/> 승인대기중
			</div>
			<div class="explain">
				<img src="images/massmail/prepareing.gif" alt="발송준비이미지"/> 발송준비중
			</div>
			<div class="explain">
				<img src="images/massmail/ready.gif" alt="발송대기이미지"/> 발송대기중
			</div>
			<div class="explain">
				<img src="images/massmail/sending.gif" alt="발송중이미지"/> 발송 중
			</div>
			<div class="explain">
				<img src="images/massmail/pause.gif" alt="일시정지이미지"/> 일시 정지
			</div>
			<div class="explain">
				<img src="images/massmail/stop.gif" alt="발송정지이미지"/> 발송 정지
			</div>
			<div class="explain">
				<img src="images/massmail/finish.gif" alt="발송완료이미지"/> 발송 완료
			</div>
			<div class="explain">
				<img src="images/massmail/error.gif" alt="에러이미지"/> 에러
			</div>
			<div class="right">
				<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>	
			</div>
			<div class="right">
				<a href="javascript:$('<%=id%>').editWindow( 0 )" class="web20button bigblue">추가</a>
			</div>
		</div>
</div> 	


</form>

<div style="clear:both;width:950px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="950px" >
	<thead>
		<tr>		
		<th style="height:30px;width:40px">ID</th>
		<th style="height:30px;" >연동메일명</th>		
		<th style="height:30px;width:80px">작성자</th>
		<th style="height:30px;width:180px">첨부파일명</th>
		<th style="height:30px;width:180px">등록일</th>
		<th style="height:30px;width:100px">반복치환부분</th>
		<th style="height:30px;width:100px">발송타입</th>
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
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_intermailID") );
		}
	);

	$('<%=id%>').popup.addSeparator();
	$('<%=id%>').popup.add('삭제', function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_intermailID") ); 
		}
	);
	
	$('<%=id%>').popup.addSeparator();
	$('<%=id%>').popup.add('복사', 
		function(target,e) { 
			$('<%=id%>').copyInterMail($('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_intermailID") ); 
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
			contentURL: 'intermail/intermail.do?id=<%=id%>&method=edit&eIntermailID='+seq
		}
	);
	
}


/***********************************************/
/* 연동메일 복사 하기
/***********************************************/
$('<%=id%>').copyInterMail = function(intermailID) {
	nemoWindow(
		{
			'id': '<%=id%>_modal',
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			title: '등록/수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'intermail/intermail.do?id=<%=id%>&method=copyInterMail&intermailID='+intermailID
		}
	);
	
}

/***********************************************/
/* 발송타입 변경
/***********************************************/
$('<%=id%>').changeSendType = function(scheduleID,seq){
	var frm = $('<%=id%>_form');
	
	//자동발송이라면 
	if(seq=='2'){
		eval("frm.eSendSchedule_"+scheduleID).disabled = true;		
		eval("frm.eSendScheduleHH_"+scheduleID).disabled = true;
		eval("frm.eSendScheduleMM_"+scheduleID).disabled = true;
		eval("frm.eStatisticCHK_"+scheduleID).checked = false;
		eval("frm.eStatisticCHK_"+scheduleID).disabled = true;
		eval("frm.eStatisticSchedule_"+scheduleID).disabled = true;
		eval("frm.eStatisticScheduleHH_"+scheduleID).disabled = true;
		eval("frm.eStatisticScheduleMM_"+scheduleID).disabled = true;

	
	//수동발송이라면 
	}else{
		eval("frm.eSendSchedule_"+scheduleID).disabled = false;
		eval("frm.eSendScheduleHH_"+scheduleID).disabled = false;
		eval("frm.eSendScheduleMM_"+scheduleID).disabled = false;
		eval("frm.eStatisticCHK_"+scheduleID).checked = true;
		eval("frm.eStatisticCHK_"+scheduleID).disabled = false;
		eval("frm.eStatisticSchedule_"+scheduleID).disabled = false;
		eval("frm.eStatisticScheduleHH_"+scheduleID).disabled = false;
		eval("frm.eStatisticScheduleMM_"+scheduleID).disabled = false;
		
		if(eval("frm.eStatisticSchedule_"+scheduleID).value==""){			
			eval("frm.eStatisticSchedule_"+scheduleID).value = "<%=DateUtils.getDateString() %>";
		}
		
	}
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
	if(frm.eIntermailTitle.value== '') {
		alert('연동메일명은 필수항목입니다.');
		frm.eIntermailTitle.focus();
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

	var seq =  frm.eIntermailID.value;
	var flag = frm.eFlag.value;

	if(seq == '0' || flag == 'copy') {
		goUrl = 'intermail/intermail.do?id=<%=id%>&method=insert';
	} else {
		goUrl = 'intermail/intermail.do?id=<%=id%>&method=update';
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
/* 삭제
/***********************************************/
$('<%=id%>').deleteData = function( seq ) {

	if(!confirm("정말로 삭제 하시겠습니까?  \r\n삭제하시면 관련된 모든 데이타는 영구삭제가 됩니다.")) return;

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'intermail/intermail.do?method=delete&id=<%=id%>&eIntermailID=' + seq
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

		url: 'intermail/intermail.do?method=list&id=<%=id%>', 
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
  allowFileType: '*.html;*.htm',
  limitSize:10,
  uploadPage: 'intermail/intermail.do?method=uploadFile',
  
  onComplete: function( isError, fileNameArray ) { // 업로드 완료

   if(!isError) {
	   frm.eFileName.value = fileNameArray[0]; // 파일명
	   frm.eFileKey.value =$('<%=id%>').uploader.uploadKey;
	   
		$('<%=id%>_uploadBtn').setStyle('display','block');
		$('<%=id%>_saveBtn').setStyle('display','block');
		
		$('<%=id%>').showFileInfo();
	   
	   //alert(frm.eFileName.value);
	   //alert(frm.eNewFile.value);
	 
	   //$('<%=id%>').saveData();
	   
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
	//if(!checkFormValue(frm)) {
	//	return;
	//}

	
	 // 추가 일때 필요한 key
 	$('<%=id%>').uploader.uploadKey = '<%=request.getSession().getId()%>'+(new Date().getTime());
 
 	// 업로드
 	$('<%=id%>').uploader.upload();
 
 
}


/***********************************************/
/*파일 다운로드
/***********************************************/
$('<%=id%>').downloadFile = function( uploadKey ) {


		
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window

		url: 'intermail/intermail.do?method=fileDownload',
		 
		onSuccess: function(html,els,resHTML,scripts) {
			document.location.href = 'intermail/intermail.do?method=fileDownload&uploadKey='+uploadKey;
		}
	});
	nemoRequest.get({'id': '<%=id%>', 'uploadKey': uploadKey});
	

}

/***********************************************/
/* 파일 정보 보이기
/***********************************************/
$('<%=id%>').showFileInfo = function(  ) {

	// 업로드가 완료되면 처리될 action
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		updateWindowId: $('<%=id%>uploaded'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'intermail/intermail.do?method=getFileInfo',
		update: $('<%=id%>uploaded'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {

		}
	});
	nemoRequest.get({
		'id': '<%=id%>', 
		//'targetID': $('<%=id%>_form').eTargetID.value,
		'uploadKey': $('<%=id%>').uploader.uploadKey
	});
	
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

$('<%=id%>').showStatistic = function(intermailID,scheduleID, resultYearMonth) {

	nemoWindow(
			{
				'id': '<%=id%>Statistic',
				busyEl: '<%=id%>Statistic', // 창을 열기까지 busy 가 표시될 element
				width: 1000,
				height: $('mainColumn').style.height.toInt(),
				title: '연동메일 통계 조회',
				type: 'modal',
				loadMethod: 'xhr',
				padding: { top: 0, right: 0, bottom: 0, left: 0 },
				contentURL: 'pages/intermail/statistic/intermail_statistic.jsp?id=<%=id%>Statistic&intermailID='+intermailID+'&scheduleID='+scheduleID+'&resultYearMonth='+resultYearMonth
			}
	);
	
}




$('<%=id%>').chkState = function()
{
	setInterval("$('<%=id%>').showState()", 5000);
}
var els = null;
var httpRequest = null;
$('<%=id%>').showState = function() {
	
	var temp = document.getElementById("<%=id%>_grid_content");
	var list = temp.childNodes;
	var json = [{intermail_id:[], schedule_id:[]}];
	for(var i=0;i<list.length;i++)
	{
		els = list.item(i);
		if(els.nodeName=='TR')
		{
			if( els.getAttribute("tr_state")=='00' || els.getAttribute("tr_state")=='10' || els.getAttribute("tr_state")=='11' || els.getAttribute("tr_state")=='12' || els.getAttribute("tr_state")=='13' || els.getAttribute("tr_state")=='15')
			{
				json[0].intermail_id.push(els.getAttribute("tr_intermailID"));
				json[0].schedule_id.push(els.getAttribute("tr_scheduleID"));
			}
		}	
	}
	$('<%=id%>').sendRequest("returnIntermailState.jsp", "intermail_id="+JSON.encode( json ), $('<%=id%>').callbackFunction, "GET");
	
}

$('<%=id%>').callbackFunction = function() {

	var isAdmin = '<%=isAdmin%>';
	var userAuth = '<%=userAuth%>';
	var userID = '<%=userID%>';
	var groupID = '<%=groupID%>';
	
	if(httpRequest.readyState==4)
	{
		if(httpRequest.status==200)
		{			
			var st = eval("("+httpRequest.responseText+")");
			if(st[0].intermail_id!=null)
			{
				for(var i=0;i<st[0].intermail_id.length;i++)
				{
					var tagDiv = document.getElementById("intermailState"+st[0].intermail_id[i]+"_"+st[0].schedule_id[i]);
					var tagTr = document.getElementById("eIntermail"+st[0].intermail_id[i]+"_"+st[0].schedule_id[i]);
	
					if(st[0].state[i] != tagTr.getAttribute("tr_state"))
					{
						var t = tagDiv.childNodes;
						for(var y =0; y < t.length ;y++)
						{	var stemp = t.item(y);
							if(stemp.nodeName=='IMG')
							{
								var tagImg = stemp;		
								var tagInp = document.getElementById("<%=id %>state"+st[0].intermail_id[i]+"_"+st[0].schedule_id[i]);
								if(st[0].state[i]=='00')
								{								
									tagImg.setAttribute("src","images/massmail/ready.gif");					
									tagImg.setAttribute("title","발송대기중");	
								
								}									
								else if(st[0].state[i]=='10')
								{								
									tagImg.setAttribute("src","images/massmail/ready.gif");					
									tagImg.setAttribute("title","발송준비중");	
								
								}							
								else if(st[0].state[i]=='11')
								{
								
									tagImg.setAttribute("src","images/massmail/ready.gif");					
									tagImg.setAttribute("title","발송준비완료");
	
								}
								else if(st[0].state[i]=='12')
								{
								
									tagImg.setAttribute("src","images/massmail/sending.gif");					
									tagImg.setAttribute("title","발송중");	
	
								
								
								}
								else if(st[0].state[i]=='13')
								{
								
									tagImg.setAttribute("src","images/massmail/finish.gif");					
									tagImg.setAttribute("title","발송완료");	
	
								
								}
								else if(st[0].state[i]=='14')
								{
								
									tagImg.setAttribute("src","images/massmail/approve.gif");					
									tagImg.setAttribute("title","발송승인대기중");	
	
								
								}		
								else if(st[0].state[i]=='15')
								{
								
									tagImg.setAttribute("src","images/massmail/sending.gif");					
									tagImg.setAttribute("title","오류자재발송중");	
	
								
								}									
								else if(st[0].state[i]=='20')
								{
								
									tagImg.setAttribute("src","images/massmail/pause.gif");					
									tagImg.setAttribute("title","대기중일시중지");
	
								}									
								else if(st[0].state[i]=='21')
								{
								
									tagImg.setAttribute("src","images/massmail/pause.gif");					
									tagImg.setAttribute("title","발송중일시중지");
	
								}							
								
								
								else if(st[0].state[i]=='22')
								{
								
									tagImg.setAttribute("src","images/massmail/stop.gif");					
									tagImg.setAttribute("title","발송완전중지");
	
										
								
								}
								else if(st[0].state[i]=='40')
								{
								
									tagImg.setAttribute("src","images/massmail/error.gif");					
									tagImg.setAttribute("title","발송준비중에러");	
	
								}
								else if(st[0].state[i]=='41')
								{
								
									tagImg.setAttribute("src","images/massmail/error.gif");					
									tagImg.setAttribute("title","발송중에러");
	
								
								}
								
								
								else if(st[0].state[i]=='42')
								{
									
									tagImg.setAttribute("src","images/massmail/error.gif");					
									tagImg.setAttribute("title","오류자재발송중에러");	
								
								}
								
								toolTip.init(tagDiv);
								tagTr.setAttribute("tr_state",st[0].state[i]); 
								tagInp.setAttribute("value",st[0].state[i]); 
								 						
							}
						}
					}
				}
			}
		}		
		
	}	
}

/***********************************************/
/* 
/***********************************************/
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
var oldRemoteControlID ="";
/***********************************************/
/*상태 변경 리모콘 호출 
/***********************************************/
$('<%=id%>').remoteControl = function(id) {
	if(oldRemoteControlID != ''){
		$('<%=id %>remoteControl'+oldRemoteControlID).setStyle('display','none');
	}
	oldRemoteControlID = id;
	$('<%=id %>remoteControl'+id).setStyle('display','block');
	
	//발송대기중 OR 발송완료
	if($('<%=id %>state'+id).value == '10' || $('<%=id %>state'+id).value == '13'){
		$('<%=id %>20'+id).setStyle('display','block');	//발송대기중 일시중지		
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}	
	
	//발송중 
	if($('<%=id %>state'+id).value == '12'){
		$('<%=id %>21'+id).setStyle('display','block');	//발송일시중지
		$('<%=id %>22'+id).setStyle('display','block');	//발송완전중지
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}
	//발송준비중에러 
	if($('<%=id %>state'+id).value == '40'){		
		$('<%=id %>10'+id).setStyle('display','block');	//발송대기중
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}
	
	//발송대기중일시중지
	if($('<%=id %>state'+id).value == '20'){
		$('<%=id %>10'+id).setStyle('display','block');	//발송대기중
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}

	
	//발송중에러 , 오류자재발송중에러 , 발송일시중지 
	if($('<%=id %>state'+id).value == '41' || $('<%=id %>state'+id).value == '42' || $('<%=id %>state'+id).value == '21'){
		$('<%=id %>12'+id).setStyle('display','block');	//발송중
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}
}

/***********************************************/
/*상태 변경 리모콘 닫기 
/***********************************************/
$('<%=id%>').remoteControlClose = function(id) {
	$('<%=id %>remoteControl'+id).setStyle('display','none');
}

/***********************************************/
/* 발송 상태 변경
/***********************************************/

$('<%=id%>').changeSendState = function(state,intermailID,scheduleID){
	var message ="";
	
	if(state == '20'){
		message = "일시정지 하시겠습니까? \r\n발송대기중이거나 발송완료상태에서 일시정지한 메일은 재시작시 발송대기중으로 변경됩니다.";
	}
	if(state == '21'){
		message = "일시정지 하시겠습니까? \r\n일시정지한 메일은 재시작 하실 수 있습니다.";
	}
	if(state == '22'){
		message = "발송정지 하시겠습니까? \r\n발송 정지한 메일은 재시작 하실 수 없습니다.";
	}
	if(state == '10' || state == '12'){
		message = "재시작 하시겠습니까? ";
	}

	if(!confirm(message)) return;
	
	nemoRequest.init( 
	{
				
		updateWindowId: '<%=id%>',  
		url: 'intermail/intermail.do?method=changeSendState&state='+state+'&intermailID='+intermailID+'&scheduleID='+scheduleID, 
		update: $('<%=id%>Content'), 
		onSuccess: function(html,els,resHTML,scripts) {
			$('<%=id%>').list();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
	
	
}	

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
	<%=id%>ServerTimer = $('<%=id%>').showState.periodical(60000);
	MochaUI.Windows.instances.get('<%=id%>').addEvent('onClose',function() { $clear(<%=id%>ServerTimer) } );
});

</script>