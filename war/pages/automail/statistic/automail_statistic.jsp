<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String automailID = request.getParameter("automailID");

%>
<div class="toolbarTabs">
	<ul id="<%=id%>Tabs" class="tab-menu">
		<li id="<%=id%>HourlyStatistic" class="selected"><a>시간별 반응 분석</a></li>
		<li id="<%=id%>DailyStatistic"><a>일자별 통계</a></li>
		<li id="<%=id%>MonthlyStatistic"><a>월별 통계</a></li>
		<li id="<%=id%>DomainStatistic"><a>도메인별 통계</a></li>
		<li id="<%=id%>FailCauseStatistic"><a>실패원인별 통계</a></li>
	</ul>
	<div class="clear"></div>
</div>

<form name="<%=id%>_sform" id="<%=id%>_sform" method="post">
<input type="hidden" id="id" name="id" value="<%=id%>" />
<input type="hidden" id="automailID" name="automailID" value="<%=automailID%>" />
<input type="hidden" id="standard" name="standard" value="hourly" />
<input type="hidden" id="new" name="chartload" value="N"/>
<div class="search_wrapper" style="width:860px;padding-left:8px;">
	<div class="text"> 조회 날짜</div>
	<div>
		<ul id="sYear"  class="selectBox">
			<li data="<%=DateUtils.getYear()+3 %>"><%=DateUtils.getYear()+3 %></li>			
			<li data="<%=DateUtils.getYear()+3 %>"><%=DateUtils.getYear()+2 %></li>
			<li data="<%=DateUtils.getYear()+1 %>"><%=DateUtils.getYear()+1 %></li>
			<li data="<%=DateUtils.getYear() %>" select='Y'><%=DateUtils.getYear() %></li>			
			<li data="<%=DateUtils.getYear()-1 %>"><%=DateUtils.getYear()-1 %></li>
			<li data="<%=DateUtils.getYear()-2 %>"><%=DateUtils.getYear()-2 %></li>
			<li data="<%=DateUtils.getYear()-3 %>"><%=DateUtils.getYear()-3 %></li>
		</ul> 
	</div>
	<div class="text">년</div>
	<div id="<%=id%>sMonthView">
		<ul id="sMonth"  class="selectBox">
			<% for(int sMonth=1;sMonth<=12;sMonth++){ %>
				<li data="<%=sMonth %>" <%if(DateUtils.getMonth() == sMonth){ %> select='Y' <%}%>><%=sMonth %></li>
			<%} %>
		</ul>
	</div>
	<div id="<%=id%>sMonthViewText" class="text">월 </div>
	<div id="<%=id%>sDayView">
		<ul id="sDay"  class="selectBox">
				<li data="all">전체</li>
			<% for(int sDay=1;sDay<=31;sDay++){ %>
				<li data="<%=sDay %>" <%if(DateUtils.getDay() == sDay){ %> select='Y' <%}%>><%=sDay %></li>
			<%} %>
		</ul>
	</div>
	<div id="<%=id%>sDayViewText" class="text">일 </div>
	<div>
		<a href="javascript:$('<%=id%>').getData()" class="web20button pink">검색</a>
	</div>
	<div class="btn_green"><a href="javascript:$('<%=id%>').loadContent('columnchart')" style="cursor:pointer"><span>막대 그래프 조회</span></a></div>
	<div class="btn_b" style="margin-left:5px;float:left"><a href="javascript:$('<%=id%>').loadContent('linechart')" style="cursor:pointer"><span id="<%=id%>ChartBtn"> 꺽은선 그래프 조회</span></a></div>
	<div class="btn_r" style="margin-left:5px;float:left"><a href="javascript:$('<%=id%>').chartView()" style="cursor:pointer"><span id="<%=id%>ChartViewBtn">차트 숨기기</span></a></div>
	<div class="right">
		<a href="javascript:$('<%=id%>').excelDown()" class="web20button bigblue">Excel</a>
	</div>
</div>
</form>

<div id="<%=id%>Content" style="clear:both;padding:8px;">
</div>

<form name="<%=id%>_list_form" id="<%=id%>_list_form">
<div id="<%=id%>List" style="clear:both;padding:8px;" >
</div>     	
</form>
<script type="text/javascript">

//************************************************************
//통계 리스트
//************************************************************/
$('<%=id%>').getData = function() {
    //시간별 통계 처리
	if($('<%=id%>_sform').standard.value == 'hourly')
	{
		page = 'automail/automail.do?method=statisticHourlyList';
	}
    //일별 통계 처리
	if($('<%=id%>_sform').standard.value == 'daily')
	{
		page = 'automail/automail.do?method=statisticDailyList';
	}
    //월별 통계 처리
	if($('<%=id%>_sform').standard.value == 'monthly')
	{
		page = 'automail/automail.do?method=statisticMonthlyList';
		$('<%=id%>sMonthView').setStyle('display','none');
		$('<%=id%>sMonthViewText').setStyle('display','none');
		$('<%=id%>sDayView').setStyle('display','none');
		$('<%=id%>sDayViewText').setStyle('display','none');
	}else{
		$('<%=id%>sMonthView').setStyle('display','block');
		$('<%=id%>sMonthViewText').setStyle('display','block');
		$('<%=id%>sDayView').setStyle('display','block');
		$('<%=id%>sDayViewText').setStyle('display','block');
	}
    //도메인별 통계 처리
	if($('<%=id%>_sform').standard.value == 'domain')
	{
		page = 'automail/automail.do?method=statisticDomainList';
	}
	//실패 원인별 통계
	if($('<%=id%>_sform').standard.value == 'failcause')
	{
		page = 'automail/automail.do?method=statisticFailCauseList';
		$('<%=id%>ChartBtn').innerHTML = '원 그래프 조회';
	}else{
		$('<%=id%>ChartBtn').innerHTML = '꺽은선 그래프 조회';
	}
	nemoRequest.init({
		url: page	, 
		update: $('<%=id%>List'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			$('<%=id%>_sform').chartload.value = 'N';
			$('<%=id%>Content').setStyle('display','none');
		}
	});
	
	nemoRequest.post($('<%=id%>_sform'));
}
//************************************************************
//대상자 미리 보기
//************************************************************/
$('<%=id%>').personPreview = function(key,type) {
	pv_width = 430;
	if($('<%=id%>_sform').standard.value == 'failcause'){
		pv_width = 630;
	}
	nemoWindow(
			{
				'id': '<%=id%>_preview',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: pv_width,
				height: $('mainColumn').style.height.toInt(),
				title: '대상자 미리보기',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/automail/statistic/automail_statistic_proc.jsp?id=<%=id%>_preview&method=porsonpreview&method=porsonPreview&automailID=<%=automailID%>&sYear='+$('<%=id%>_sform').sYear.value+'&sDay='+$('<%=id%>_sform').sDay.value+'&sMonth='+$('<%=id%>_sform').sMonth.value+'&standard='+$('<%=id%>_sform').standard.value+'&type='+type+'&key='+key
			}
		);
}
/***********************************************/
/* 컨텐츠 로딩
/***********************************************/
$('<%=id%>').loadContent = function(packages) {
	$('<%=id%>Content').setStyle('display','block');
	//page = 'pages/automail/statistic/'+kind+'_statistic.jsp';
	page = 'pages/automail/statistic/automail_statistic_proc.jsp?method=chartview&packages='+packages;
	nemoRequest.init( 
		{
			url: page, 
			update: $('<%=id%>Content'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
				$('<%=id%>_sform').chartload.value = 'Y';
			}
		});
	nemoRequest.post($('<%=id%>_sform'));
}

/***********************************************/
/* 차트 보이기 버튼 컨트롤
/***********************************************/
$('<%=id%>').chartView = function() {
	if($('<%=id%>_sform').chartload.value == 'N'){
		alert('조회된 차트가 없습니다.');
	}else{
		if($('<%=id%>Content').getStyle('display') == 'none'){
			$('<%=id%>Content').setStyle('display','block');
			$('<%=id%>ChartViewBtn').innerHTML = '차트 숨기기';
		}else{
			$('<%=id%>Content').setStyle('display','none');
			$('<%=id%>ChartViewBtn').innerHTML = '차트 보이기';
		}
	}
}
/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/
$('<%=id%>').init = function() {

	MochaUI.initializeTabs('<%=id%>Tabs');
	var frm = $('<%=id%>_sform');
	makeSelectBox.render( frm );
	$('<%=id%>').getData();
}

//************************************************************
//Excel 다운
//************************************************************/
$('<%=id%>').excelDown = function() {
	var frm = $('<%=id%>_sform');
	location.href = "pages/automail/statistic/automail_statistic_excel.jsp?sStandard="+frm.standard.value+"&automailID="+frm.automailID.value+"&sYear="+frm.sYear.value+"&sMonth="+frm.sMonth.value+"&sDay="+frm.sDay.value;
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	
	$('<%=id%>HourlyStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'hourly';
		$('<%=id%>').getData();
	});
	$('<%=id%>DailyStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'daily';
		$('<%=id%>').getData();
	});
	$('<%=id%>MonthlyStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'monthly';
		$('<%=id%>').getData();
	});
	$('<%=id%>DomainStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'domain';
		$('<%=id%>').getData();
	});
	$('<%=id%>FailCauseStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'failcause';
		$('<%=id%>').getData();
	});
});
</script>