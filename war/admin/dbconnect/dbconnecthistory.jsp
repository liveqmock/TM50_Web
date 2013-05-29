<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>  
<%
	String id = request.getParameter("id");
	String preID = request.getParameter("preID");
	
	String dbID = request.getParameter("dbID");
	String date = null;
	String dateBef = null;
	if(request.getParameter("eSearchDateStart")== null)
	{
		dateBef=DateUtils.getNowAddShortDate(-7);
		date=DateUtils.getDateString();
	}else
	{
		dateBef=request.getParameter("eSearchDateStart");
		date=request.getParameter("eSearchDateEnd");
		
	}
%>
<div id="<%=id%>_info_content" ></div>
<div class="search_wrapper" style="width:610px">
<form id="<%=id%>_sform" name="<%=id%>_sform">
	<input type="hidden" id="dbID" name="dbID" value="<%=dbID%>"/>
	<input type="hidden" id="eHistoryYN" name="eHistoryYN" value="Y"/>
	<div class="text">실수집일</div>		
	<div>						
		<input type="text" id="eSearchDateStart" name="eSearchDateStart" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=dateBef%>"/>
	</div>
	<div>
		<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eSearchDateStart)" align="absmiddle" />
	</div>
	<div>						
		<input type="text" id="eSearchDateEnd" name="eSearchDateEnd" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=date%>"/>
	</div>
	<div>	
		<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eSearchDateEnd)" align="absmiddle" />
	</div>	
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="right">	
		<a href="javascript:$('dbConnect').editWindow(<%=dbID%>)" class="web20button blue">수정</a>
	</div>
</form>
</div>

<div style="clear:both;width:610px;">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form"><hr/>
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>		
			<th style="height:30px;">수집 테이블명</th>
			<th style="height:30px;width:140px">수집 시작 시간</th>
			<th style="height:30px;width:140px">수집 완료 시간</th>
			<th style="height:30px;width:70px">수집 대상자수</th>
			<th style="height:30px;width:70px">상태</th>
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

		url: 'admin/dbconnect/dbconnect.do?method=listdbHistoryInfo&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
	
}

$('<%=id%>').loadContent = function () {	

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'admin/dbconnect/dbconnect.do?method=dbInfo&id=<%=id%>&dbID=<%=dbID%>', 
		update: $('<%=id%>_info_content'), // 완료후 content가 랜더링될 element
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
	$('<%=id%>').loadContent();
});


</script>