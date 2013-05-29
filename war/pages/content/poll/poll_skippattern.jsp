<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String id = request.getParameter("id");
	String preID = request.getParameter("preID");
	String pollID = request.getParameter("ePollID");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<input type="hidden" id="ePollID" name="ePollID" value="<%=pollID%>" />	
<div class="search_wrapper" style="width:780px">
	<div class="start">
		<img src="images/tag_blue.png"> 객관식 문항 중 단일 응답에 대해서만 스킵패턴 설정이 가능합니다. <br/>
		<img src="images/tag_blue.png"> 스킵패턴 설정이 가능한 질문은 <img src="/images/book_next.png" />로 표시됩니다.
	</div>
</div>

</form>

<div style="clear:both;width:780px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="780px" >
	<thead>
		<tr>
			<th style="height:30px;width:50px">번호</th>
			<th style="height:30px;">질문</th>
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
	</form>
</div>
<div id="<%=id%>_poll_view" style="padding-top:8px;width:780px"></div>

    
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

		url: 'content/poll/poll.do?method=selSingularQuestion&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}
/***********************************************/
/* 질문 정보 보기
/***********************************************/
$('<%=id%>').questionView = function ( questionID ) {	

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'content/poll/poll.do?method=viewSingularQuestion&id=<%=id%>&ePollID=<%=pollID%>&eQuestionID='+questionID, 
		update: $('<%=id%>_poll_view'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/***********************************************/
/* 저장 클릭  
/***********************************************/
$('<%=id%>').saveSkipPattern = function( questionID ) {

	var frm = $('<%=id%>_question_form');
	//필수입력 조건 체크
	if(!checkFormValue($('<%=id%>_question_form'))) {
		return;
	}			
	
	goUrl = 'content/poll/poll.do?id=<%=id%>&method=updateQuestionSkipPatten';
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>'  // busy 를 표시할 window
		, updateWindowId: '<%=id%>' // 완료후 버튼,힌트 가 랜더링될 windowid
		, url: goUrl
		, onSuccess: function(html,els,resHTML) {
			$('<%=preID%>_poll_frame').src = "pages/content/poll/poll_preview.jsp?id=<%=id %>&pollID=<%=pollID %>";	
		}
	});

	nemoRequest.post($('<%=id%>_question_form'));			
}


/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').list(); 
});
</script>