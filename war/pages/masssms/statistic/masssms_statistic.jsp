<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String masssmsID = request.getParameter("masssmsID");
	String scheduleID = request.getParameter("scheduleID");

%>
<div class="toolbarTabs">
	<ul id="<%=id%>Tabs" class="tab-menu">
		<li id="<%=id%>BasicInfo" class="selected"><a>기본정보</a></li>
	</ul>
	<div class="clear"></div>
</div>

<form name="<%=id%>_sform" id="<%=id%>_sform" method="post">
	<input type="hidden" id="id" name="id" value="<%=id%>" />
	<input type="hidden" id="masssmsID" name="masssmsID" value="<%=masssmsID%>" />
	<input type="hidden" id="scheduleID" name="scheduleID" value="<%=scheduleID%>" />	
	<input type="hidden" id="standard" name="standard" value="basic" />
	<input type="hidden" id="personPreviewYN" name="personPreviewYN" value="Y" />	
	<input type="hidden" id="backupYearMonth" name="backupYearMonth" value="" />

</form>

<div id="<%=id%>Content" style="padding:8px;">
</div>
<form name="<%=id%>_list_form" id="<%=id%>_list_form">
<div id="<%=id%>List" style="padding:8px;">
</div>

</form>
<script type="text/javascript">

/***********************************************/
/* 컨텐츠 로딩 - 기본정보 및 차트 불러오기
/***********************************************/
$('<%=id%>').loadContent = function(method, key) {

	//기본 정보 및 차트 영역 display:block
	$('<%=id%>Content').setStyle('display','block');
	page = 'masssms/statistic/masssms.do?method=massSMSStatisticBasicInfo'

		nemoRequest.init( 
		{
			url: page, 
			update: $('<%=id%>Content'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
			
			}
		});
	nemoRequest.post($('<%=id%>_sform'));
}

/************************************************************/
/*대상자 미리 보기
/************************************************************/
$('<%=id%>').personPreview = function(key,type,iTotalCnt) {
	//발송이 완료되기전 처리
	if($('<%=id%>_sform').personPreviewYN.value == 'N'){
		alert("수집된 대상자 정보가 아직 없습니다.");
		return;
	}
	//실패원인과 기타 대상자 보기의 팝업창 크기 조절
	window_width = 430;
	if($('<%=id%>_sform').standard.value == 'failcause' || $('<%=id%>_sform').standard.value == 'faildomain'){
		window_width = 730;
	}
	if($('<%=id%>_sform').standard.value == 'link'){
		window_width = 530;
	}

	nemoWindow(
			{
				'id': '<%=id%>_preview',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: window_width,
				height: $('mainColumn').style.height.toInt(),
				title: '대상자 미리보기',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/masssms/statistic/masssms_statistic_proc.jsp?id=<%=id%>_preview&method=personpreview&masssmsID=<%=masssmsID%>&scheduleID=<%=scheduleID%>&standard='+$('<%=id%>_sform').standard.value+'&type='+type+'&key='+key+'&iTotalCnt='+iTotalCnt
			}
		);
}


/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/
$('<%=id%>').init = function() {
	MochaUI.initializeTabs('<%=id%>Tabs');
	$('<%=id%>List').setStyle('display','none');	
	$('<%=id%>').loadContent('statisticbasic');	

}

/* 이벤트 설정 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	//탭 이벤트 설정
	$('<%=id%>BasicInfo').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'basic'; // 통계 기준값 변경
		$('<%=id%>').loadContent('statisticbasic'); //기본정보 불러오기
		$('<%=id%>List').setStyle('display','none'); //리스트 숨기기
	});

	
});
</script>