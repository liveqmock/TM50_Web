<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="web.common.util.*"%> 
<%
	String id = request.getParameter("id");


	String isAdmin = LoginInfo.getIsAdmin(request);
	String userAuth = LoginInfo.getUserAuth(request);
	String userID =LoginInfo.getUserID(request);
	String groupID = LoginInfo.getGroupID(request);
%>
<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:1180px">
	<div class="start">
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
			<li data="intermailTitle">연동메일명</li>
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
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>

</div>
</form>



<div style="clear:both;width:1180px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="1180px" >
	<thead>
		<tr>				
		<th style="height:30px;width:150px">이메일</th>
		<th style="height:30px;">연동메일명</th>
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