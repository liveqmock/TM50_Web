<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String id = request.getParameter("id");
	String preID = request.getParameter("preID");
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div>
	<img src='images/tag_blue.png'> 기존문항 불러오기는 기존에 작성하였던 문항을 <strong>복사</strong>하는 기능입니다. <br/>
	<img src='images/tag_blue.png'> 검색 <strong>조건을 입력</strong>하시고 <strong>엔터</strong> 또는 <strong>[검색]</strong> 버튼을 클릭 하시면 조건에 해당하는 문항이 검색됩니다.<br/>
	<img src='images/tag_blue.png'> 복사 할 문항을 선택하신 후 <strong>[선택복사]</strong>를 클릭하시면 해당 문항의 정보가 설문문항 등록 화면에 <strong>자동으로 입력</strong>됩니다.<br/><br/>
</div>
<div class="search_wrapper" style="width:770px">
	<div class="start">
		<ul id="sSearchType"  class="selectBox" >
			<li data="questionText" select="yes">질문내용</li>		
		</ul>
	</div>
	<div>
		<input type="text" id="sQuestionText" name="sQuestionText" size="15"  />
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>
	<div class="right">
		<a href="javascript:$('<%=preID%>').copyQuestion()" class="web20button bigblue">선택복사</a>	
	</div>
</div>

</form>

<div style="clear:both;width:770px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="770px" >
	<thead>
		<tr>
		<th style="height:30px;width:40px">선택</th>
		<th style="height:30px;width:730px">설문ID</th>
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
/* 리스트 
/***********************************************/

$('<%=id%>').list = function ( forPage ) {	

	var frm = $('<%=id%>_sform');

	if(frm.sQuestionText.value ==''){
		alert("검색조건을 입력하세요!!");
		return;
	}
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

		url: 'content/poll/poll.do?method=copyQuestionList&id=<%=id%>', 
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
	//$('<%=id%>').createPopup();
	//$('<%=id%>').list(); 
});
</script>