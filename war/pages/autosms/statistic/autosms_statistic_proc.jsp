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
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="860px" >
		<thead>
			<tr>
				<th style="height:30px;">기준값</th>
				<th style="height:30px;width:170px">전체통수</th>
				<th style="height:30px;width:170px">결과대기통수</th>
				<th style="height:30px;width:170px">성공통수</th>
				<th style="height:30px;width:170px">실패통수</th>				
			</tr>
		</thead>
		<tbody id="<%=id%>_grid_content">
			<c:forEach items="${statisticList}" var="statistic">
				<TR class="tbl_tr" >
					<TD class="tbl_td" ><b><c:out value="${statistic.viewStandard}"/></b></TD>		
					<TD class="tbl_td" ><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','total')"><c:out value="${statistic.sendTotal}"/></a></TD>
					<TD class="tbl_td" ><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','ready')"><c:out value="${statistic.readyTotal}"/></a></TD>	
					<TD class="tbl_td" ><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','success')"><c:out value="${statistic.successTotal}"/></a></TD>		
					<TD class="tbl_td" ><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','fail')"><c:out value="${statistic.failTotal}"/></a></TD>						
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
	String autosmsID = request.getParameter("autosmsID");
	String sYear = request.getParameter("sYear");
	String sMonth = request.getParameter("sMonth");
	String sDay = request.getParameter("sDay");
	String standard = request.getParameter("standard");
	String packages = request.getParameter("packages");
%>	
	<IFRAME id="<%=id%>_ChartView" name="<%=id%>_ChartView" src="iframe/googlechart/autosms_chart_iframe.jsp?autosmsID=<%=autosmsID%>&sYear=<%=sYear%>&sMonth=<%=sMonth %>&sDay=<%=sDay %>&packages=<%=packages%>&standard=<%=standard %>" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%
}
//****************************************************************************************************/
// 대상자 미리보 기
//****************************************************************************************************/
if(method.equals("personpreview")) {

	String autosmsID = request.getParameter("autosmsID");
	String sYear = request.getParameter("sYear");
	String sMonth = request.getParameter("sMonth");
	String sDay = request.getParameter("sDay");
	String standard = request.getParameter("standard");
	String type = request.getParameter("type");
	String key= request.getParameter("key");
	int pv_width = 650;
%>
	<form id="<%=id%>_sform" name="<%=id%>_sform">
	<input type="hidden" id="autosmsID" name="autosmsID" value="<%=autosmsID %>">		
	<input type="hidden" id="sYear" name="sYear" value="<%=sYear %>">
	<input type="hidden" id="sMonth" name="sMonth" value="<%=sMonth %>">
	<input type="hidden" id="sDay" name="sDay" value="<%=sDay %>">
	<input type="hidden" id="standard" name="standard" value="<%=standard %>">
	<input type="hidden" id="type" name="type" value="<%=type %>">
	<input type="hidden" id="key" name="key" value="<%=key %>">
	
	<div class="search_wrapper" style="width:<%=pv_width %>px">
		<div class="start">	
			<ul id="sSearchType"  class="selectBox" title="검색 조건 선택" >
				<li data="receiverPhone" >전화번호</li>
				<li data="smsCode" >응답코드</li>				
			</ul>	
		</div>
		<div>
			<input type="text" id="sSearchText" name="sSearchText" size="15" title="검색할 문자의 일부를 입력하세요"/>	
		</div>
		<div class="right"><a href="javascript:$('<%=id%>').excelDown()" class="web20button blue">Excel</a></div>
		<div class="right"><a href="javascript:$('<%=id%>').allList()" class="web20button blue">전체목록</a></div>
		<div class="right"><a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a></div>
	</div>
	</form>
	
	<div style="clear:both;width:<%=pv_width %>px">
		<form name="<%=id%>_list_form" id="<%=id%>_list_form">
		<div style="margin-left:5px;float:right" id="<%=id%>_total"></div><div style="clear:both"></div>
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
		<thead>
			<tr>
				<th style="height:30px;width:100px">전화번호</th>
				<th style="height:30px;width:100px">응답코드</th>
				<th style="height:30px;">응답메시지</th>
				<th style="height:30px;width:200px">발송시간</th>
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
	//************************************************************
	//Excel 다운
	//************************************************************/
	$('<%=id%>').excelDown = function() {
		var frm = $('<%=id%>_sform');
		location.href = "pages/autosms/statistic/autosms_statistic_personpreview_excel.jsp?sStandard="+frm.standard.value+"&autosmsID="+frm.autosmsID.value+"&sYear="+frm.sYear.value+"&sMonth="+frm.sMonth.value+"&sDay="+frm.sDay.value+"&key="+frm.key.value+"&type="+frm.type.value+"&sSearchType="+frm.sSearchType.value+"&sSearchText="+frm.sSearchText.value;
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
	
			url: 'autosms/autosms.do?method=personPreview&id=<%=id%>', 
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

if(method.equals("personpreviewlist")) { %>

	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="standard" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	<c:forEach items="${personPreview}" var="personPreview">
		<TR class="tbl_tr" >
			<TD class="tbl_td"><c:out value="${personPreview.receiverPhone}"/></TD>
			<TD class="tbl_td"><c:out value="${personPreview.smsCode}"/></TD>
			<TD class="tbl_td" align="left"><c:out value="${personPreview.smsCodeMsg}"/></TD>
			<TD class="tbl_td"><c:out value="${personPreview.registDate}"/></TD>					
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