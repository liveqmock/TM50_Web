<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String massmailID = request.getParameter("massmailID");
String scheduleID = request.getParameter("scheduleID");
String pollID = request.getParameter("pollID");

//****************************************************************************************************/
//설문미응답자보기 
//****************************************************************************************************/
if(method.equals("pollAllNotResponse")) {	

%>

	<form id="<%=id%>_sform" name="<%=id%>_sform">
	<input type="hidden" id="massmailID" name="massmailID" value="<%=massmailID %>">		
	<input type="hidden" id="scheduleID" name="scheduleID" value="<%=scheduleID %>">	
	<input type="hidden" id="pollID" name="pollID" value="<%=pollID %>">
	
	

	
	<div class="search_wrapper" style="width:550px">
		<div>
			<ul id="sSearchType"  class="selectBox" >
				<li data="email" >E-Mail</li>			
			</ul>
		</div>
		<div>
			<input type="text" id="sSearchText" name="sSearchText" size="15"/>
		</div>
		<div>
			<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
		</div>
		<div class="right">
			<a href="javascript:$('<%=id%>').allList()" class="web20button blue">전체목록</a>
		</div>
		<div class="right">
			<a href="javascript:$('<%=id%>').personPreviewDown()" class="web20button blue">Excel 다운</a>
		</div>
		
		
	</div>
	<div class="search_wrapper" style="clear:both;width:550px">
		<div class="right">
			<div id="<%=id%>_total"></div>
		</div>
	</div>
	</form>
	
	<div style="clear:both;width:400px">
		<form name="<%=id%>_list_form" id="<%=id%>_list_form">
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="550px" >
		<thead>
			<tr>
				<th style="height:30px;width:280px">E-Mail</th>				
				<th style="height:30px;width:270px">발송시간</th>
			</tr>
		</thead>
	
		<tbody id="<%=id%>_grid_content">
		
		</tbody>
		</table>
		</form>
	</div>
	<div id="<%=id%>_Down" style="padding:8px;">
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
	
			url: 'massmail/statistic/massmail.do?method=massmailPollAllNotResponse&id=<%=id%>', 
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




<%
}
//설문리스트 
if(method.equals("pollAllNotResponseList")) {
	

%>

	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />	
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	
	<c:forEach items="${pollPersonPreviewList}" var="personPreview">
		<TR class="tbl_tr" >
			<TD class="tbl_td"><c:out value="${personPreview.email}"/></TD>			
			<TD class="tbl_td"><c:out value="${personPreview.registDate}"/></TD>					
		</TR>
	</c:forEach>

	<script type="text/javascript">


		//************************************************************
		//대상자 다운
		//************************************************************/
		$('<%=id%>').personPreviewDown = function() {			
			location.href = "pages/massmail/statistic/massmail_poll_allnotresponse_excel.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&pollID=<%=pollID%>&iTotalCnt=<%=iTotalCnt%>";
			
		}


		window.addEvent('domready', function(){

			$('<%=id%>_total').innerHTML = '[ 대상인원 : <%=iTotalCnt%> 통 ]';

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
				'iPageCnt': '5' 
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

<%
}
%>