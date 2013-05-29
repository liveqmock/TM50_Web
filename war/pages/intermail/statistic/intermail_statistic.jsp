<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String intermailID = request.getParameter("intermailID");
	String scheduleID = request.getParameter("scheduleID");
	String resultYearMonth = request.getParameter("resultYearMonth");

%>
<div class="toolbarTabs">
	<ul id="<%=id%>Tabs" class="tab-menu">
		<li id="<%=id%>HourlyStatistic" class="selected"><a>시간별 반응 분석</a></li>
		<li id="<%=id%>DailyStatistic"><a>일자별 통계</a></li>
		<li id="<%=id%>MonthlyStatistic"><a>월별 통계</a></li>
		<li id="<%=id%>DomainStatistic"><a>도메인별 통계</a></li>
		<li id="<%=id%>FailCauseStatistic"><a>실패원인별 통계</a></li>
		<li id="<%=id%>SendHistoryStatistic"><a>발송결과내역</a></li>
	</ul>
	<div class="clear"></div>
</div>

<form name="<%=id%>_sform" id="<%=id%>_sform" method="post">
<input type="hidden" id="id" name="id" value="<%=id%>" />
<input type="hidden" id="intermailID" name="intermailID" value="<%=intermailID%>" />
<input type="hidden" id="scheduleID" name="scheduleID" value="<%=scheduleID%>" />
<input type="hidden" id="resultYearMonth" name="resultYearMonth" value="<%=resultYearMonth%>" />
<input type="hidden" id="standard" name="standard" value="hourly" />
<input type="hidden" id="new" name="chartload" value="N"/>
<div class="search_wrapper" style="width:960px;padding-left:8px;">
	<div id="<%=id %>_searchAndChart1">
		<div class="text"> 조회 날짜</div>
		<div>
			<ul id="sYear"  class="selectBox">
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
	</div>
	<div>
		<a href="javascript:$('<%=id%>').getData()" class="web20button pink" id="<%=id %>_searchAndChartBtn1">검색</a>
	</div>
	<div id="<%=id %>_searchAndChart2">
		<div class="btn_green"><a href="javascript:$('<%=id%>').loadContent('columnchart')" style="cursor:pointer"><span>막대 그래프 조회</span></a></div>
		<div class="btn_b" style="margin-left:5px;float:left"><a href="javascript:$('<%=id%>').loadContent('linechart')" style="cursor:pointer"><span id="<%=id%>ChartBtn"> 꺽은선 그래프 조회</span></a></div>
		<div class="btn_r" style="margin-left:5px;float:left"><a href="javascript:$('<%=id%>').chartView()" style="cursor:pointer"><span id="<%=id%>ChartViewBtn">차트 숨기기</span></a></div>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').excelDown()" class="web20button bigblue" id="<%=id %>_searchAndChartBtn2">Excel</a>
	</div>
	
	<div id="<%=id %>_searchAndHistory1" style="margin-left:-5px"> 
		<div>
			<ul id="sResultYear"  class="selectBox">
				<li data="<%=DateUtils.getYear() %>" select="yes"><%=DateUtils.getYear() %></li>			
				<li data="<%=DateUtils.getYear()-1 %>"><%=DateUtils.getYear()-1 %></li>
				<li data="<%=DateUtils.getYear()-2 %>"><%=DateUtils.getYear()-2 %></li>			
			</ul>
		</div>
		<div>
			<ul id="sResultMonth"  class="selectBox">
				<% for(int sMonth=1;sMonth<=12;sMonth++){ %>
					<li data="<%=sMonth %>" <%if(DateUtils.getMonth() == sMonth){ %> select="yes" <%}%>><%=sMonth %></li>
				<%} %>		
			</ul>
		</div>		
		<div>
			<ul id="sSearchType"  class="selectBox">
				<li data="email" select="yes">이메일</li>			
			</ul>
		</div>	
		<div>
			<input type="text" id="sSearchText" name="sSearchText" size="15" />
		</div>		
		<div>
			<ul id="sSmtpCodeType"  class="selectBox">
				<li data="" select="yes">--성공유무--</li>			
				<li data="0">발송성공</li>
				<li data="1">발송실패</li>
			</ul>
		</div>		
		<div>
			<ul id="sOpenYN"  class="selectBox">
				<li data="" select="yes">--메일본문오픈여부--</li>			
				<li data="Y">Y</li>
				<li data="N">N</li>
			</ul>
		</div>		
		<div>
			<ul id="sOpenFileYN"  class="selectBox">
				<li data="" select="yes">--첨부파일오픈여부--</li>			
				<li data="Y">Y</li>
				<li data="N">N</li>
			</ul>
		</div>	
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink" id="<%=id %>_searchAndHistoryBtn1">검색</a>
	</div>
	<div class="right">
		<div id="<%=id %>_searchAndHistory2">
		<ul id="eRetrySendFlag" class="selectBox">						
			<li data="checked">체크된 실패메일</li>
			<li data="all">전체 실패메일</li>
		</ul>
		</div>
		<div>
		<a href="javascript:$('<%=id%>').retrySend()" class="web20button blue" title="재발송은  오류자재발송이 모두 완료되었고,<br>영구적인 실패가 아닌 일반 실패메일만 가능합니다." id="<%=id %>_searchAndHistoryBtn2">재발송</a>
		</div>
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
		page = 'intermail/intermail.do?method=statisticHourlyList';
	}
    //일별 통계 처리
	if($('<%=id%>_sform').standard.value == 'daily')
	{
		page = 'intermail/intermail.do?method=statisticDailyList';
	}
    //월별 통계 처리
	if($('<%=id%>_sform').standard.value == 'monthly')
	{
		page = 'intermail/intermail.do?method=statisticMonthlyList';
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
		page = 'intermail/intermail.do?method=statisticDomainList';
	}
    
	//실패 원인별 통계
	if($('<%=id%>_sform').standard.value == 'failcause')
	{
		page = 'intermail/intermail.do?method=statisticFailCauseList';
		$('<%=id%>ChartBtn').innerHTML = '원 그래프 조회';
	}else{
		$('<%=id%>ChartBtn').innerHTML = '꺽은선 그래프 조회';
	}
	
	//발송결과내역
	if($('<%=id%>_sform').standard.value == 'sendhistory')
	{		
		$('<%=id %>_searchAndChartBtn1').setStyle('visibility','hidden');
		$('<%=id %>_searchAndChartBtn2').setStyle('visibility','hidden');
		$('<%=id%>_searchAndChartBtn1').setStyle('width','0');
		$('<%=id%>_searchAndChartBtn1').setStyle('width','0');
		$('<%=id %>_searchAndChart1').setStyle('display','none');
		$('<%=id %>_searchAndChart2').setStyle('display','none');
		$('<%=id %>_searchAndHistoryBtn1').setStyle('visibility','');
		$('<%=id %>_searchAndHistoryBtn2').setStyle('visibility','');
		$('<%=id %>_searchAndHistory1').setStyle('display','block');
		$('<%=id %>_searchAndHistory2').setStyle('display','block');
		//page = 'intermail/intermail.do?method=sendHistoryList';
		page = 'pages/intermail/statistic/intermail_statistic_proc.jsp?method=sendhistory';
	}else{
		$('<%=id %>_searchAndChartBtn1').setStyle('visibility','');
		$('<%=id %>_searchAndChartBtn2').setStyle('visibility','');
		$('<%=id%>_searchAndChartBtn1').setStyle('width','85%');
		$('<%=id %>_searchAndChart1').setStyle('display','block');
		$('<%=id %>_searchAndChart2').setStyle('display','block');
		
		$('<%=id %>_searchAndHistoryBtn1').setStyle('visibility','hidden');
		$('<%=id %>_searchAndHistoryBtn2').setStyle('visibility','hidden');
		$('<%=id %>_searchAndHistory1').setStyle('display','none');
		$('<%=id %>_searchAndHistory2').setStyle('display','none');
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
				contentURL: 'pages/intermail/statistic/intermail_statistic_proc.jsp?id=<%=id%>_preview&method=porsonpreview&method=porsonPreview&intermailID=<%=intermailID%>&scheduleID=<%=scheduleID%>&sYear='+$('<%=id%>_sform').sYear.value+'&sDay='+$('<%=id%>_sform').sDay.value+'&sMonth='+$('<%=id%>_sform').sMonth.value+'&standard='+$('<%=id%>_sform').standard.value+'&type='+type+'&key='+key
			}
		);
}
/***********************************************/
/* 컨텐츠 로딩
/***********************************************/
$('<%=id%>').loadContent = function(packages) {
	$('<%=id%>Content').setStyle('display','block');	
	page = 'pages/intermail/statistic/intermail_statistic_proc.jsp?method=chartview&packages='+packages;
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
	location.href = "pages/intermail/statistic/intermail_statistic_excel.jsp?sStandard="+frm.standard.value+"&intermailID="+frm.intermailID.value+"&scheduleID="+frm.scheduleID.value+"&sYear="+frm.sYear.value+"&sMonth="+frm.sMonth.value+"&sDay="+frm.sDay.value;
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
	$('<%=id%>SendHistoryStatistic').addEvent('click', function(){
		$('<%=id%>_sform').standard.value = 'sendhistory';
		$('<%=id%>').getData();
	});
	
});

/*****************************************************
 * 실패 메일 재발송 
 *****************************************************/
$('<%=id%>').retrySend = function(){
		
	var frm = $('<%=id%>_list_form');
	
	var retrySendFlag = $('<%=id%>_sform').eRetrySendFlag.value;
	var msg = "";
	if(retrySendFlag=="checked"){
		msg = "체크된 데이터를 재발송 하시겠습니까?"
	}else{
		msg = "전체 실패된  데이터(영구적인 오류자는 제외)를 재발송 하시겠습니까?"
	}
	
	if(!confirm(msg)) return;
	
	copyForm( $('<%=id%>_sform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>'  // busy 를 표시할 window		
		,url: 'intermail/intermail.do?method=resendRetryMail'		
		,onSuccess: function(html,els,resHTML) {			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post(frm);
}


</script>