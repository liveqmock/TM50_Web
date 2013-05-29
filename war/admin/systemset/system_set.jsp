<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>

<%
	String id = request.getParameter("id");
	String configFlag = request.getParameter("configFlag");
	
	String isAdmin = LoginInfo.getIsAdmin(request);

	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:750px">
	<div class="text">
		<img src="images/tag_blue.png"> 메일 시스템 성능 관련 설정을 포함한 기타 환경을 설정합니다. 
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').saveData()" class="web20button bigblue" >저 장</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').list()" class="web20button bigpink" title="입력 값을 수정전 값으로 되돌립니다.">다시입력</a>
	</div>
</div>

</form>

<div style="clear:both;width:750px">
	<form name="<%=id%>_form" id="<%=id%>_form">
	<table class="ctbl" border="0" cellspacing="0" cellpadding="0" width="750px" >
		<tbody id="<%=id%>_grid_content">
	</tbody>
	</table>
	</form>
</div>
    
<div id="<%=id%>_paging" class="page_wrapper"></div>
    
<script type="text/javascript">

/***********************************************/
/* 저장버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function() {

	var frm = $('<%=id%>_form');
	
	var goUrl = 'admin/systemset/systemset.do?id=<%=id%>&configFlag=<%=configFlag%>&method=updateSystemSet&itemCount='+frm.itemCount.value;

	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}
	copyForm( $('<%=id%>_rform'), frm );
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: goUrl
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			closeWindow( $('<%=id%>_modal') );
		}
	});
	nemoRequest.post(frm);
	
	
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

		url: 'admin/systemset/systemset.do?method=list&id=<%=id%>&configFlag=<%=configFlag%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').list(); 
});

</script>


<%}else{%>
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td align="center" valign="middle">
			<center><img src="../../images/error.jpg" /></center>
		</td>
	</tr>
</table>
<%}%>