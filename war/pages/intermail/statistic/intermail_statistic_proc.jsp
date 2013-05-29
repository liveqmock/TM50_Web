<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");

//****************************************************************************************************/
// 통계 리스트
//****************************************************************************************************/
if(method.equals("statisticlist")) {
%>
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="960px" >
		<thead>
			<tr>
				<th style="height:30px;">기준값</th>
				<th style="height:30px;width:170px">전체통수</th>
				<th style="height:30px;width:170px">성공통수</th>
				<th style="height:30px;width:170px">실패통수</th>
				<th style="height:30px;width:170px">오픈통수</th>
			</tr>
		</thead>
		<tbody id="<%=id%>_grid_content">
			<c:forEach items="${statisticList}" var="statistic">
				<TR class="tbl_tr" >
					<TD class="tbl_td" ><b><c:out value="${statistic.viewStandard}"/></b></TD>		
					<TD class="tbl_td" ><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','total')"><c:out value="${statistic.sendTotal}"/></a></TD>	
					<TD class="tbl_td" ><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','success')"><c:out value="${statistic.successTotal}"/></a></TD>		
					<TD class="tbl_td" ><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','fail')"><c:out value="${statistic.failTotal}"/></a></TD>	
					<TD class="tbl_td" ><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','open')"><c:out value="${statistic.openTotal}"/></a></TD>	
				</TR>
			</c:forEach>
		</tbody>
	</TABLE>	
	<script type="text/javascript">
	window.addEvent('domready', function(){
		renderTableHeader( "<%=id %>List" );
		// 테이블 렌더링
		$('<%=id%>').grid_content = new renderTable({
			element: $('<%=id%>_grid_content') // 렌더링할 대상
			,cursor: 'pointer' // 커서
			,focus: true  // 마우스 이동시 포커스 여부
			,select: true // 마우스 클릭시 셀렉트 표시 여부
		});
		$('<%=id%>').grid_content.render();
	});
	</script>	
<%
}
//****************************************************************************************************/
// 실패원별 통계 리스트
//****************************************************************************************************/
if(method.equals("statisticfailcauselist")) {
%>
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->	
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="960px" align="center">
			<thead>
				<tr>
					<th style="height:30px;width:286px">실패구분 (구분코드)</th>
					<th style="height:30px;width:286px" align="left">상세 원인 (원인코드)</th>
					<th style="height:30px;width:286px">실패 통수</th>
				</tr>
			</thead>
			<tbody id="<%=id%>_grid_content">
				<c:set var="count" value="0"/>
				<c:forEach items="${statisticFailCauseList}" var="statisticFailCause">
				<c:set var="count" value="${count + 1}"/>
					<TR class="tbl_tr" >
					<c:if test="${statisticFailCause.failCauseType == '1' || statisticFailCause.failCauseType == '2'}">
						<c:if test="${count == 1 || count == 4}">
							<TD class="tbl_td" rowspan="3"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statisticFailCause.failcauseCode}"/>','fail')"><c:out value="${statisticFailCause.failCauseTypeDes}"/>(<c:out value="${statisticFailCause.failCauseType}"/>)</a></TD>
						</c:if>
						<TD class="tbl_td" align="left"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statisticFailCause.failcauseCode}"/>','fail')"><c:out value="${statisticFailCause.failCauseDes}"/>(<c:out value="${statisticFailCause.failcauseCode}"/>)</a></TD>						
						<TD class="tbl_td" ><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statisticFailCause.failcauseCode}"/>','fail')"><c:out value="${statisticFailCause.failCount}"/></a></TD>
					</c:if>
					<c:if test="${statisticFailCause.failCauseType == '3'}">
						<c:if test="${count == 7}">
							<TD class="tbl_td" rowspan="2"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statisticFailCause.failcauseCode}"/>','fail')"><c:out value="${statisticFailCause.failCauseTypeDes}"/>(<c:out value="${statisticFailCause.failCauseType}"/>)</a></TD>
						</c:if>
						<TD class="tbl_td" align="left"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statisticFailCause.failcauseCode}"/>','fail')"><c:out value="${statisticFailCause.failCauseDes}"/>(<c:out value="${statisticFailCause.failcauseCode}"/>)</a></TD>						
						<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statisticFailCause.failcauseCode}"/>','fail')"><c:out value="${statisticFailCause.failCount}"/></a></TD>
					</c:if>
					<c:if test="${statisticFailCause.failCauseType == '4'}">
						<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statisticFailCause.failcauseCode}"/>','fail')"><c:out value="${statisticFailCause.failCauseTypeDes}"/>(<c:out value="${statisticFailCause.failCauseType}"/>)</a></TD>	
						<TD class="tbl_td" align="left"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statisticFailCause.failcauseCode}"/>','fail')"><c:out value="${statisticFailCause.failCauseDes}"/>(<c:out value="${statisticFailCause.failcauseCode}"/>)</a></TD>						
						<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statisticFailCause.failcauseCode}"/>','fail')"><c:out value="${statisticFailCause.failCount}"/></a></TD>
					</c:if>
					</TR>
				</c:forEach>
			</tbody>
		</TABLE>
	<script type="text/javascript">
	window.addEvent('domready', function(){
		renderTableHeader( "<%=id %>List" );
		// 테이블 렌더링
		$('<%=id%>').grid_content = new renderTable({
			element: $('<%=id%>_grid_content') // 렌더링할 대상
			,cursor: 'pointer' // 커서
			,focus: true  // 마우스 이동시 포커스 여부
			,select: true // 마우스 클릭시 셀렉트 표시 여부
		});
		$('<%=id%>').grid_content.render();
	});
	</script>	
<% 
}

if(method.equals("chartview")) { 
	String intermailID = request.getParameter("intermailID");
	String scheduleID = request.getParameter("scheduleID");
	String sYear = request.getParameter("sYear");
	String sMonth = request.getParameter("sMonth");
	String sDay = request.getParameter("sDay");
	String standard = request.getParameter("standard");
	String packages = request.getParameter("packages");
	if(standard.equals("failcause")){%>
		<IFRAME id="<%=id%>_ChartView" name="<%=id%>_ChartView" src="iframe/googlechart/inter_failcause_chart_iframe.jsp?intermailID=<%=intermailID%>&scheduleID=<%=scheduleID%>&sYear=<%=sYear%>&sMonth=<%=sMonth %>&sDay=<%=sDay %>&packages=<%=packages%>&standard=<%=standard %>" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%  }else{ %>
		<IFRAME id="<%=id%>_ChartView" name="<%=id%>_ChartView" src="iframe/googlechart/inter_chart_iframe.jsp?intermailID=<%=intermailID%>&scheduleID=<%=scheduleID%>&sYear=<%=sYear%>&sMonth=<%=sMonth %>&sDay=<%=sDay %>&packages=<%=packages%>&standard=<%=standard %>" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%  }
}
//****************************************************************************************************/
// 대상자 미리보 기
//****************************************************************************************************/
if(method.equals("porsonpreview")) {

	String intermailID = request.getParameter("intermailID");
	String scheduleID = request.getParameter("scheduleID");
	String sYear = request.getParameter("sYear");
	String sMonth = request.getParameter("sMonth");
	String sDay = request.getParameter("sDay");
	String standard = request.getParameter("standard");
	String type = request.getParameter("type");
	String key= request.getParameter("key");
	int pv_width = 400;
	if(standard.equals("failcause")){
		pv_width = 600;
	}
%>
	<form id="<%=id%>_sform" name="<%=id%>_sform">
	<input type="hidden" id="intermailID" name="intermailID" value="<%=intermailID %>">
	<input type="hidden" id="scheduleID" name="scheduleID" value="<%=scheduleID %>">
	<input type="hidden" id="sYear" name="sYear" value="<%=sYear %>">
	<input type="hidden" id="sMonth" name="sMonth" value="<%=sMonth %>">
	<input type="hidden" id="sDay" name="sDay" value="<%=sDay %>">
	<input type="hidden" id="standard" name="standard" value="<%=standard %>">
	<input type="hidden" id="type" name="type" value="<%=type %>">
	<input type="hidden" id="key" name="key" value="<%=key %>">
	
	<div class="search_wrapper" style="width:<%=pv_width %>px">
		<div class="start">	
			<ul id="sSearchType"  class="selectBox" title="검색 조건 선택" >
				<li data="email" >E-Mail</li>
				<% 
					if(standard.equals("failcause")){
				%>
				<li data="smtpCode" >응답코드</li>
				<li data="smtpCode" >로그</li>
				<%} %>
			</ul>	
		</div>
		<div><input type="text" id="sSearchText" name="sSearchText" size="15" title="검색할 문자의 일부를 입력하세요"/></div>
		<div class="right"><a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a></div>
		<div class="right"><a href="javascript:$('<%=id%>').allList()" class="web20button blue">전체목록</a></div>
		<div class="right"><a href="javascript:$('<%=id%>').excelDown()" class="web20button blue">Excel</a></div>
	</div>
	</form>
	
	<div style="clear:both;width:<%=pv_width %>px">
		<form name="<%=id%>_list_form" id="<%=id%>_list_form">
		<div style="margin-left:5px;float:right" id="<%=id%>_total"></div><div style="clear:both"></div>
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="<%=pv_width %>px" >
	<% 
		if(standard.equals("failcause")){
	%>
		<thead>
			<tr>
				<th style="height:30px;width:200px">E-Mail</th>
				<th style="height:30px;width:80px">SMTP 코드</th>
				<th style="height:30px">SMPT 로그</th>
			</tr>
		</thead>
	<%
		}else{
	%>
		<thead>
			<tr>
				<th style="height:30px;width:200px">E-Mail</th>
				<th style="height:30px;width:200px">발송시간</th>
			</tr>
		</thead>
	<%} %>
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
	//************************************************************
	//Excel 다운
	//************************************************************/
	$('<%=id%>').excelDown = function() {
		var frm = $('<%=id%>_sform');
		location.href = "pages/intermail/statistic/intermail_statistic_porsonpreview_excel.jsp?sStandard="+frm.standard.value+"&intermailID="+frm.intermailID.value+"&scheduleID="+frm.scheduleID.value+"&sYear="+frm.sYear.value+"&sMonth="+frm.sMonth.value+"&sDay="+frm.sDay.value+"&key="+frm.key.value+"&type="+frm.type.value+"&sSearchType="+frm.sSearchType.value+"&sSearchText="+frm.sSearchText.value;
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
	
			url: 'intermail/intermail.do?method=porsonPreview&id=<%=id%>', 
			update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
				
				//MochaUI.arrangeCascade();
			}
		});
		nemoRequest.post($('<%=id%>_rform'));
	}
	/***********************************************/
	/* 전체목록
	/***********************************************/
	$('<%=id%>').allList = function(){
		initFormValue($('<%=id%>_sform'));
		$('<%=id%>').list ();
	}
	
	/* 리스트 표시 */
	window.addEvent("domready",function () {
		$('<%=id%>').init();
		$('<%=id%>').list(); 
	});
	
	</script>

<%}

if(method.equals("porsonpreviewlist")) { %>

	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="standard" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	<c:forEach items="${porsonPreview}" var="porsonPreview">
		<TR class="tbl_tr" >
			<TD class="tbl_td"><c:out value="${porsonPreview.email}"/></TD>
			<%if(standard.equals("failcause")){ %>
				<TD class="tbl_td"><c:out value="${porsonPreview.smtpCode}"/></TD>
				<TD class="tbl_td" align="left"><c:out value="${porsonPreview.smtpMsg}"/></TD>
			<%}else{ %>					
				<TD class="tbl_td"><c:out value="${porsonPreview.registDate}"/></TD>
			<%} %>		
		</TR>
	</c:forEach>
	<script type="text/javascript">

		window.addEvent('domready', function(){

			$('<%=id%>_total').innerHTML = '[ 대상인원 : <%=iTotalCnt%> 명 ]';

			// 페이징 표시
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				,url: 'pages/common/pagingStr.jsp'
				,update: $('<%=id%>_paging') // 완료후 content가 랜더링될 element
			});
			nemoRequest.get({
				'id':'<%=id%>', 
				'curPage': '<%=curPage%>', 
				'iLineCnt': '<%=iLineCnt%>', 
				'iTotalCnt': '<%=iTotalCnt%>', 
				'iPageCnt': '10' 
			});
			
		
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				//,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			
		});

	</script>
<%}%>


<%

/*
* 발송결과 히스토리 
*/
if(method.equals("sendhistory")) { %>


	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="980px" >
	<thead>
		<tr>		
		<th style="height:30px;width:30px"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder" onclick="selectAll($('<%=id%>_list_form').eSendID,this.checked)"/></th>		
		<th style="height:30px;width:150px">이메일</th>
		<th style="height:30px;width:150px">발송일</th>
		<th style="height:30px;width:50px">성공유무</th>		
		<th style="height:30px;width:50px">재발송횟수</th>
		<th style="height:30px;width:150px">최종재발송일</th>
		<th style="height:30px;width:150px">메일본문오픈일</th>
		<th style="height:30px;width:150px">첨부파일오픈일</th>		
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
    
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

		url: 'intermail/intermail.do?method=listSendHistory&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
	
	
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();	
	$('<%=id%>').list(); 
});

</script>

<%}%>

<%if(method.equals("listSendHistory")) { %>

			<jsp:useBean id="curPage1" class="java.lang.String" scope="request" />
			<jsp:useBean id="iLineCnt1" class="java.lang.String" scope="request" />			
			<jsp:useBean id="iTotalCnt1" class="java.lang.String" scope="request" />
			<jsp:useBean id="message1" class="java.lang.String" scope="request" />
			
			<%if(!message1.equals("")) { %>
				<script type="text/javascript">
				alert("<%=message1%>");
				</script>
			<%}%>

			
			<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
				 주석이 없으면 업데이트 되지 않으므로 주의
				 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
			-->			

			<c:forEach items="${interMailSendResultList}" var="interMailSendResult">
			<TR class="tbl_tr" tr_intermail_id="${interMailSendResult.intermailID}" tr_schedule_id="${interMailSendResult.scheduleID}" tr_send_id="${interMailSendResult.sendID}">
				<TD class="tbl_td">
				<c:if test="${interMailSendResult.smtpCodeType != '0' && interMailSendResult.smtpCodeType != '2' && interMailSendResult.retrySendCount != '0'}">
				<input type="checkbox" class="notBorder" id="eSendID" name="eSendID" value="${interMailSendResult.sendID}" />
				</c:if>&nbsp;
				</TD>		
				<TD class="tbl_td">${interMailSendResult.email}</TD>
				<TD class="tbl_td">${interMailSendResult.registDate}</TD>
				
				<c:if test="${interMailSendResult.smtpCodeType == '0'}">
				<TD class="tbl_td" align="center">
					성공
				</TD>	
				</c:if>				
				<c:if test="${interMailSendResult.smtpCodeType != '0'}">
				<TD class="tbl_td" align="center" title="[${interMailSendResult.smtpCodeTypeDesc}:${interMailSendResult.smtpCode}]<br>${interMailSendResult.smtpMsg}">
					실패
				</TD>					
				</c:if>				
				<TD class="tbl_td" align="center">${interMailSendResult.retrySendCount}</TD>
				<TD class="tbl_td">${interMailSendResult.retryLastDate}</TD>
				<TD class="tbl_td">${interMailSendResult.openDate}</TD>
				<TD class="tbl_td">${interMailSendResult.openFileDate}</TD>		
			</TR>
			</c:forEach>
			
			<script type="text/javascript">

				window.addEvent('domready', function(){
					
					
					// 페이징 표시
					nemoRequest.init( 
					{
						busyWindowId: '<%=id%>'  // busy 를 표시할 window
						,url: 'pages/common/pagingStr.jsp'
						,update: $('<%=id%>_paging') // 완료후 content가 랜더링될 element
					});
					nemoRequest.get({
						'id':'<%=id%>', 
						'curPage': '<%=curPage1%>', 
						'iLineCnt': '<%=iLineCnt1%>', 
						'iTotalCnt': '<%=iTotalCnt1%>', 
						'iPageCnt': '10' 
					});
					
				
					// 테이블 렌더링
					$('<%=id%>').grid_content = new renderTable({
						element: $('<%=id%>_grid_content') // 렌더링할 대상
						,cursor: 'pointer' // 커서
						,focus: true  // 마우스 이동시 포커스 여부
						,select: true // 마우스 클릭시 셀렉트 표시 여부
						,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
					});
					$('<%=id%>').grid_content.render();
					
				});


				
			</script>

<%} %>




