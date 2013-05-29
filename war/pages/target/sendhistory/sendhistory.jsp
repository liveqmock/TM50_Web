<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:970px">
	<div>
		<ul id="sSendedYear"  class="selectBox">
			<li data="<%=DateUtils.getYear() %>"><%=DateUtils.getYear() %></li>			
			<li data="<%=DateUtils.getYear()-1 %>"><%=DateUtils.getYear()-1 %></li>
			<li data="<%=DateUtils.getYear()-2 %>"><%=DateUtils.getYear()-2 %></li>
		</ul>
	</div>	
	<div class="text">년</div>	
	<div>	
		<ul id="sSendedMonth"  class="selectBox">
			<% for(int month=1;month<=12;month++){ %>
				<li id="sMonth<%=month %>" data="<%=month %>" <%if(month==DateUtils.getMonth()){ %> select='Y' <%}%>><%=month %></li>
			<%} %>
		</ul>
	</div>
	<div class="text">월</div>	
	<div>		
		<ul id="sSendYN"  class="selectBox">
			<li data="">--성공유무--</li>			
			<li data="Y">발송성공</li>
			<li data="N">발송실패</li>
		</ul>
	</div>
	<div>		
		<ul id="sOpenYN"  class="selectBox">
			<li data="">--오픈여부--</li>			
			<li data="Y">오픈</li>
			<li data="N">미오픈</li>
		</ul>
	</div>
	<div>		
		<ul id="sClickYN"  class="selectBox">
			<li data="">--클릭여부--</li>			
			<li data="Y">클릭</li>
			<li data="N">미클릭</li>
		</ul>
	</div>
	<div>		
		<ul id="sRejectcallYN"  class="selectBox">
			<li data="">--수신거부--</li>			
			<li data="Y">거부</li>
			<li data="N">허용</li>
		</ul>
	</div>
	<div>
		<ul id="sSearchType"  class="selectBox">
			<li data="s.email" >이메일</li>		
			<li data="s.onetooneInfo" >이름</li>			
		</ul>
	</div>
	<div>	
		<input type="text" id="sSearchText" name="sSearchText" size="20"/>
	</div>
	<div >	
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>				
	</div>
	<div class="right">
			<a href="javascript:$('<%=id%>').addTargetWindow()" class="web20button bigblue" title="검색조건에 해당하는 대상자를 대상자그룹으로 등록한다.">대상자그룹추가</a>				
		</div>	
	<div style="clear:both;width:965px">
		<div class="text"><img src="images/tag_blue.png" alt="Tip" />통계수집이 완료되고 백업이  완료된 데이터만  검색됩니다.</div>
		<div class="right">
			<a href="javascript:$('<%=id%>').excelDown()" class="web20button bigblue">&nbsp;&nbsp;&nbsp;Excel&nbsp;&nbsp;&nbsp;</a>
		</div>			
	</div>
</div>

</form>

<div style="clear:both;width:970px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="970px" >
	<thead>
		<tr>
		<th style="height:30px;width:200px">이메일</th>
		<th style="height:30px;width:100px">고객명</th>
		<th style="height:30px;">대량메일명</th>
		<th style="height:30px;width:50px">성공유무</th>
		<th style="height:30px;width:60px">오픈여부</th>
		<th style="height:30px;width:60px">클릭여부</th>
		<th style="height:30px;width:60px">수신거부</th>
		<th style="height:30px;width:110px">발송일</th>		
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
	var frm = $('<%=id%>_sform');
	frm.sSearchText.value="";
	initFormValue($('<%=id%>_sform'));
	frm.sSendedMonth.value="<%=DateUtils.getMonth()%>";
	var sm = document.getElementById("sMonth<%=DateUtils.getMonth()%>");
	makeSelectBox.select(sm);
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

		url: 'target/sendhistory/sendhistory.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/***********************************************/
/* 대량메일 수정창 열기
/***********************************************/
$('<%=id%>').viewMassmailInfo = function( seq, seq2) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			//height: 600,
			title: '대량메일발송정보',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'target/sendhistory/sendhistory.do?id=<%=id%>_modal&method=view&massmailID='+seq+'&scheduleID='+seq2
		}
	);
	
}

var oldRemoteControlID ="";
/***********************************************/
/*상태 변경 리모콘 호출 
/***********************************************/
$('<%=id%>').viewLog = function(id) {
	
	if(oldRemoteControlID != ''){
		$('<%=id %>remoteControl'+oldRemoteControlID).setStyle('display','none');
	}
	oldRemoteControlID = id;
	$('<%=id %>remoteControl'+id).setStyle('display','block');
}

/***********************************************/
/* 실패원인 창 닫기 
/***********************************************/
$('<%=id%>').viewLogClose = function(id) {
	$('<%=id %>remoteControl'+id).setStyle('display','none');
}

/***********************************************/
/* 대상자 그룹 등록 창 열기
/***********************************************/
$('<%=id%>').addTargetWindow = function() {
	nemoWindow(
			{
				'id': '<%=id%>_modal',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: 520,
				//height: $('mainColumn').style.height.toInt(),
				height: 250,
				title: '대상자그룹등록',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'target/sendhistory/sendhistory.do?id=<%=id%>&method=addTargetWindow'
			}
		);
}

/***********************************************/
/* 대상자 그룹 등록 
/***********************************************/
$('<%=id%>').addTarget = function() {
	var goUrl = 'target/targetlist/target.do?id=<%=id%>&method=sendHistoryAddTarget';
	var frm = $('<%=id%>_form');
	copyForm(frm, $('<%=id%>_rform'));
	
	if(frm.eTargetName.value==''){
		toolTip.showTipAtControl( frm.eTargetName,'대상자그룹명을 입력하세요.');
		return;
	}
	if(!confirm("검색하신 대상자를 대상자 그룹으로 추가 하시겠습니까?")) return;
	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: $('<%=id%>') // 완료후 버튼,힌트 가 랜더링될 window
				, url: goUrl
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
					closeWindow( $('<%=id%>_modal') );
					$('<%=id%>').viewTegetList();
				}
			});
	nemoRequest.post($('<%=id%>_rform'));
}

/***********************************************/
/* 대상자 관리 리스트창 열기
/***********************************************/
$('<%=id%>').viewTegetList = function() {
	
	closeWindow($('<%=id%>_modal'));
	
	nemoWindow(
		{
			'id': 'target',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 1000,
			height: $('mainColumn').style.height.toInt(),
			//height: 270,
			title: '대상자관리',
			//type: 'modal',
			container: 'desktop',
			noOtherClose: true,
			loadMethod: 'xhr',
			contentURL: 'target/targetlist/target.do?id=target'
		}
	);
	
}

/***********************************************/
/*고객명 사용여부선택에 따른 대처 필드 처리 
/***********************************************/
$('<%=id%>').setInsteadView = function(seq) {
	if(seq=='Y'){
		$('<%=id%>_insteadTR').setStyle('display','block');
	}else{
		$('<%=id%>_insteadTR').setStyle('display','none');
	}
}

//************************************************************
//Excel 다운
//************************************************************/
$('<%=id%>').excelDown = function() {
	var frm = $('<%=id%>_sform');
	location.href = "pages/target/sendhistory/sendhistory_excel.jsp?sSendedYear="+frm.sSendedYear.value+"&sSendedMonth="+frm.sSendedMonth.value+"&sSendYN="+frm.sSendYN.value+"&sOpenYN="+frm.sOpenYN.value+"&sClickYN="+frm.sClickYN.value+"&sRejectcallYN="+frm.sRejectcallYN.value+"&sSearchType="+frm.sSearchType.value+"&sSearchText="+frm.sSearchText.value;
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').list ();
});

</script>


