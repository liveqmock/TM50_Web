<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %> 
<%@ page import="java.util.List" %>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper" %>
<%@ page import="web.massmail.statistic.service.MassMailStatService" %>
<%@ page import="web.massmail.statistic.model.*" %>
<%@ page import="web.massmail.write.model.*" %>   
<%
	String id = request.getParameter("id");
	String massmailID = request.getParameter("massmailID");
	String scheduleID = request.getParameter("scheduleID");
	String pollID = request.getParameter("pollID");
	
	//설문권한체크 
	String isPollAuth = LoginInfo.isAccessSubMenu("40",request);
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	String[] targetIDs = service.getTargetIDs(Integer.parseInt(massmailID));
	List<OnetooneTarget> onetooneTargets = service.selectOnetooneByTargetID(targetIDs, Integer.parseInt(massmailID));
%>
<div class="toolbarTabs">
	<ul id="<%=id%>Tabs" class="tab-menu">
		<li id="<%=id%>BasicInfo" class="selected"><a>기본정보</a></li>
		<li id="<%=id%>HourlyStatistic"><a>시간별 통계</a></li>
		<li id="<%=id%>DailyStatistic"><a>일자별 통계</a></li>
		<li id="<%=id%>DomainStatistic"><a>도메인별 통계</a></li>
		<li id="<%=id%>TargetStatistic"><a>대상자별 통계</a></li>
		<li id="<%=id%>LinkStatistic"><a>링크 분석</a></li>
		<li id="<%=id%>FailDomain"><a>실패 도메인 </a></li>
		<li id="<%=id%>FailCause"><a>실패 원인</a></li>
		<%if(isPollAuth.equals("Y")){ %>	
		<li id="<%=id%>PollStatistic"><a>설문통계</a></li>
		<%} %>
	</ul>
	<div class="clear"></div>
</div>

<form name="<%=id%>_sform" id="<%=id%>_sform" method="post">
	<input type="hidden" id="oneToOneSize" name="oneToOneSize" value="<%=onetooneTargets.size()*100%>" />
	<input type="hidden" id="id" name="id" value="<%=id%>" />
	<input type="hidden" id="massmailID" name="massmailID" value="<%=massmailID%>" />
	<input type="hidden" id="scheduleID" name="scheduleID" value="<%=scheduleID%>" />
	<input type="hidden" id="pollID" name="pollID" value="<%=pollID%>" />
	<input type="hidden" id="standard" name="standard" value="basic" />
	<input type="hidden" id="personPreviewYN" name="personPreviewYN" value="Y" />
	<input type="hidden" id="retryFinishYN" name="retryFinishYN" value="N" />
	<input type="hidden" id="backupYearMonth" name="backupYearMonth" value="" />
	
	<div id="<%=id%>_button" style="padding:8px;margin-bottom:10px">
		<div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').loadContent('statisticbar','send')" style="cursor:pointer"><span>막대 그래프 조회</span></a></div>
		<div class="btn_b" style="margin-left:5px;float:left"><a href="javascript:$('<%=id%>').loadContent('statisticpie','send')" style="cursor:pointer"><span id="<%=id%>ChartBtn">원 그래프 조회</span></a></div>	
		<div class="btn_r" style="margin-left:5px;float:left"><a href="javascript:$('<%=id%>').chartView()" style="cursor:pointer"><span id="<%=id%>ChartViewBtn">그래프 숨기기</span></a></div>
	</div>
</form>

<div id="<%=id%>Content" style="padding:8px;">
</div>
<form name="<%=id%>_list_form" id="<%=id%>_list_form">
<div id="<%=id%>List" style="padding:8px;">
</div>
<div id="<%=id%>_notLink" style="padding:8px;display:none">
	<img src="images/tag_blue.png" alt="링크분석 미 사용 안내 "> 매일 본문 링크를 수집 하지 않도록 설정된 메일입니다.<br> 
</div>
</form>
<script type="text/javascript">

/***********************************************/
/* 컨텐츠 로딩 - 기본정보 및 차트 불러오기
/***********************************************/
$('<%=id%>').loadContent = function(method, key) {

	//기본 정보 및 차트 영역 display:block
	$('<%=id%>Content').setStyle('display','block');
	//차트 보이기 / 숨기기 버튼 처리
	$('<%=id%>ChartViewBtn').innerHTML = '그래프 숨기기';
	//기본정보 인지 차트인지에 따라 page 변경
	if(method == 'statisticbasic'){
		page = 'massmail/statistic/massmail.do?method=massMailStatisticBasicInfo'
	}else{
		page = 'pages/massmail/statistic/massmail_statistic_proc.jsp?method='+method+'&key='+key;
	}
	nemoRequest.init( 
		{
			url: page, 
			update: $('<%=id%>Content'), // 완료후 content가 랜더링될 element
			update: $('<%=id%>Content'),
			onSuccess: function(html,els,resHTML,scripts) {
			
			}
		});
	nemoRequest.post($('<%=id%>_sform'));
}
/***********************************************/
/* 선택 재발송 (실패원인)
/***********************************************/
$('<%=id%>').retryMail = function() {
	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eFailCauseCode']  );
	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'재발송 진행 할  실패원인 그룹을 먼저 선택하세요');
		return;
	}
	if(!confirm("재발송을 요청 하시겠습니까?")) return;

	copyForm( $('<%=id%>_sform'), frm );
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>'  // busy 를 표시할 window
		, url: 'massmail/statistic/massmail.do?method=retryMail'
		, onSuccess: function(html,els,resHTML) {	
						
		}
	});
	nemoRequest.post(frm);
	
	
}

/***********************************************/
/* 선택 재발송 (실패도메인)
/***********************************************/
$('<%=id%>').retryDomain = function() {
	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eDomain']  );
	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'재발송 진행 할  도메인을 먼저 선택하세요');
		return;
	}
	if(!confirm("재발송을 요청 하시겠습니까?")) return;

	copyForm( $('<%=id%>_sform'), frm );
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>'  // busy 를 표시할 window
		, url: 'massmail/statistic/massmail.do?method=retryDomain'
		, onSuccess: function(html,els,resHTML) {	
						
		}
	});
	nemoRequest.post(frm);
}

/***********************************************/
/* 선택 재발송 (설문 미응답)
/***********************************************/
$('<%=id%>').retryPoll = function() {
	var frm = $('<%=id%>_list_form');
	
	if(!confirm("미응답자에 대해 재발송을 요청 하시겠습니까?")) return;

	copyForm( $('<%=id%>_sform'), frm );
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>'  // busy 를 표시할 window
		, url: 'massmail/statistic/massmail.do?method=retryPoll'
		, onSuccess: function(html,els,resHTML) {	
						
		}
	});
	nemoRequest.post(frm);
	
	
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
	window_width = 630;
	if($('<%=id%>_sform').standard.value == 'failcause' || $('<%=id%>_sform').standard.value == 'faildomain'){
		window_width = 730;
	}
	if($('<%=id%>_sform').standard.value == 'link'){
		window_width = 730;
	}
	
	var temp = 0;
	if($('<%=id%>_sform').oneToOneSize.value > 500)
		temp = 300;
	//window_width = Number(window_width) + Number($('<%=id%>_sform').oneToOneSize.value);
	window_width = Number(window_width) + Number(temp);
	
	nemoWindow(
			{
				'id': '<%=id%>_preview',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: window_width.toString(),
				height: $('mainColumn').style.height.toInt(),
				title: '대상자 미리보기',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/statistic/massmail_statistic_proc.jsp?id=<%=id%>_preview&method=personpreview&massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&standard='+$('<%=id%>_sform').standard.value+'&type='+type+'&key='+key+'&iTotalCnt='+iTotalCnt
			}
		);
}
//************************************************************
//링크분석 이미지 미리보기
//************************************************************/
$('<%=id%>').viewImage = function(webURL) {
	nemoWindow(
			{
				'id': '<%=id%>_url_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 500,
				height: $('mainColumn').style.height.toInt(),
				title: '이미지 미리보기 ',
				type: 'modal',
				loadMethod: 'iframe',
				contentURL: webURL
			}
		);
	
}

//************************************************************
//통계 데이터(리스트) 불러오기
//************************************************************/
$('<%=id%>').getData = function() {
	
	//시간별 통계 처리
	if($('<%=id%>_sform').standard.value == 'hourly')
	{
		page = 'massmail/statistic/massmail.do?method=statisticHourlyList';
	}
	//일자별 통계 처리
	if($('<%=id%>_sform').standard.value == 'daily')
	{
		page = 'massmail/statistic/massmail.do?method=statisticDailyList';
	}
	//도메인별 통계 처리
	if($('<%=id%>_sform').standard.value == 'domain')
	{
		page = 'massmail/statistic/massmail.do?method=statisticDomainList';
	}
	//대상자별 통계 처리
	if($('<%=id%>_sform').standard.value == 'target')
	{
		page = 'massmail/statistic/massmail.do?method=statisticTargetList';
	}
	//링크분석 처리
	if($('<%=id%>_sform').standard.value == 'link')
	{
		page = 'massmail/statistic/massmail.do?method=statisticLinkList';
	}
	//실패도메인 통계 처리
	if($('<%=id%>_sform').standard.value == 'faildomain')
	{
		page = 'massmail/statistic/massmail.do?method=statisticFailDomainList';
	}
	//실패원인별 통계
	if($('<%=id%>_sform').standard.value == 'failcause'){
		page = 'massmail/statistic/massmail.do?method=statisticFailCauseList';
	}
	//설문 통계
	if($('<%=id%>_sform').standard.value == 'poll'){
		page = 'massmail/statistic/massmail.do?method=statisticPoll';
	}
	nemoRequest.init({
		url: page	, 
		update: $('<%=id%>List'), // 완료후 content가 랜더링될 element
		updateEl: $('<%=id%>List'),
		onSuccess: function(html,els,resHTML,scripts) {
		}
	});
	
	nemoRequest.post($('<%=id%>_sform'));
}

/***********************************************/
/* 차트 보이기 /숨기기
/***********************************************/
$('<%=id%>').chartView = function() {
	if($('<%=id%>Content').getStyle('display') == 'none'){
		$('<%=id%>Content').setStyle('display','block');
		$('<%=id%>ChartViewBtn').innerHTML = '그래프 숨기기';
	}else{
		$('<%=id%>Content').setStyle('display','none');
		$('<%=id%>ChartViewBtn').innerHTML = '그래프 보이기';
	}
}

/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/
$('<%=id%>').init = function() {
	MochaUI.initializeTabs('<%=id%>Tabs');
	$('<%=id%>List').setStyle('display','none');
	$('<%=id%>_button').setStyle('display','none');
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
		$('<%=id%>_button').setStyle('display','none'); //동작버튼 숨기기
		$('<%=id%>_notLink').setStyle('display','none'); //링크안내 숨기기
	});
	$('<%=id%>HourlyStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'hourly'; // 통계 기준값 변경
		$('<%=id%>').loadContent('statisticbar'); //차트 불러오기
		$('<%=id%>List').setStyle('display','block'); //리스트 보이기
		$('<%=id%>_button').setStyle('display','block'); //동작버튼 보이기
		$('<%=id%>ChartBtn').innerHTML = '꺽은선 그래프 조회';
		$('<%=id%>_notLink').setStyle('display','none'); //링크안내 숨기기
		$('<%=id%>').getData(); //리스트 불러오기
	});
	$('<%=id%>DailyStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'daily'; // 통계 기준값 변경
		$('<%=id%>').loadContent('statisticbar'); //차트 불러오기
		$('<%=id%>List').setStyle('display','block'); //리스트 보이기
		$('<%=id%>_button').setStyle('display','block'); //동작버튼 보이기
		$('<%=id%>ChartBtn').innerHTML = '꺽은선 그래프 조회';
		$('<%=id%>_notLink').setStyle('display','none'); //링크안내 숨기기
		$('<%=id%>').getData(); //리스트 불러오기
	});
	$('<%=id%>DomainStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'domain'; // 통계 기준값 변경
		$('<%=id%>').loadContent('statisticbar'); //차트 불러오기
		$('<%=id%>List').setStyle('display','block'); //리스트 보이기
		$('<%=id%>_button').setStyle('display','block'); //동작버튼 보이기
		$('<%=id%>ChartBtn').innerHTML = '꺽은선 그래프 조회';
		$('<%=id%>_notLink').setStyle('display','none'); //링크안내 숨기기
		$('<%=id%>').getData(); //리스트 불러오기
	});
	$('<%=id%>TargetStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'target'; // 통계 기준값 변경
		$('<%=id%>').loadContent('statisticbar'); //차트 불러오기
		$('<%=id%>List').setStyle('display','block'); //리스트 보이기
		$('<%=id%>_button').setStyle('display','block'); //동작버튼 보이기
		$('<%=id%>ChartBtn').innerHTML = '꺽은선 그래프 조회';
		$('<%=id%>_notLink').setStyle('display','none'); //링크안내 숨기기
		$('<%=id%>').getData(); //리스트 불러오기
	});
	$('<%=id%>FailDomain').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'faildomain'; // 통계 기준값 변경
		$('<%=id%>').loadContent('statisticbar'); //차트 불러오기
		$('<%=id%>List').setStyle('display','block'); //리스트 보이기
		$('<%=id%>_button').setStyle('display','block'); //동작버튼 보이기
		$('<%=id%>ChartBtn').innerHTML = '원 그래프 조회';
		$('<%=id%>_notLink').setStyle('display','none'); //링크안내 숨기기
		$('<%=id%>').getData(); //리스트 불러오기
	});
	$('<%=id%>FailCause').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'failcause'; // 통계 기준값 변경
		$('<%=id%>').loadContent('statisticbar'); //차트 불러오기
		$('<%=id%>List').setStyle('display','block'); //리스트 보이기
		$('<%=id%>_button').setStyle('display','block'); //동작버튼 보이기
		$('<%=id%>ChartBtn').innerHTML = '원 그래프 조회';
		$('<%=id%>_notLink').setStyle('display','none'); //링크안내 숨기기
		$('<%=id%>').getData(); //리스트 불러오기
	});
	<%if(isPollAuth.equals("Y")){ %>	
		$('<%=id%>PollStatistic').addEvent('click', function(){
			$('<%=id%>_sform').standard.value = 'poll'; // 통계 기준값 변경
			$('<%=id%>').loadContent('statisticpie'); //기본정보 불러오기
			//$('<%=id%>Content').setStyle('display','none');
			$('<%=id%>List').setStyle('display','block'); //리스트 숨기기
			$('<%=id%>_button').setStyle('display','block'); //동작버튼 숨기기
			$('<%=id%>_notLink').setStyle('display','none'); //링크안내 숨기기
			$('<%=id%>').getData(); //리스트 불러오기
			
		});
	<%}%>
	
});
</script>