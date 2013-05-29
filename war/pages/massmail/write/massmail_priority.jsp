<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");


//****************************************************************************************************/
//대상차 팝업  
//****************************************************************************************************/
if(method.equals("priority")) {
%>


<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper">
	<div style="float:right">
	<table border="0" cellpadding="3">
		<tbody>
		<tr>
			<td>
				<a href="javascript:$('<%=id%>').saveData()" class="web20button bigblue">발송순서 변경</a>
			</td>
			<td>
				<a href="javascript:closeWindow($('<%=id%>'))" class="web20button bigpink">닫기</a>
			</td>	
			<td>
				<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">다시입력</a>
			</td>		
		</tr>
		</tbody>
	</table>
	</div>
	<div style="clear:both">
	<img src="images/tag_blue.png" /> 발송할 대량메일의 발송순서를 확인하고, 발송 순서를 변경할 수 있습니다. 
	</div>
	<div style="clear:both">
	<img src="images/tag_blue.png" /> 현재 발송중인 메일보다 <b>발송순서를 빠르게 변경</b>하면, 발송중이던 메일은 <b>'발송중대기'</b>상태로 변경되고 새 메일이 발송됩니다. 
	</div>
	<div style="clear:both">
	<img src="images/tag_blue.png" /> 발송중대기 : 대량메일 발송 중에 다른 대량메일이 우선 발송되어 대기중인 상태( <b>발송순서가 되면 자동으로 발송됨</b>) 
	</div>
	
</div>

</form>

<div style="clear:both;width:770px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="770px" >
	<thead>
		<tr>
		<th style="height:30px;width:60px">현재순서</th>
		<th style="height:30px;width:60px">순서변경</th>
		<th style="height:30px">대량메일명</th>
		<th style="height:30px;width:80px">예상발송인원</th>
		<th style="height:30px;width:90px">상태</th>
		<th style="height:30px;width:110px">발송시간</th>
		
		
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
/* 전체목록
/***********************************************/
$('<%=id%>').allList = function(){
	
	$('<%=id%>').list ();
}

/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_sform');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );

	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );


	$('<%=id%>').list();

}

/***********************************************/
/* 저장버튼 클릭 -  저장
/***********************************************/
$('<%=id%>').saveData = function() {

	var frm = $('<%=id%>_list_form');
	var ids = $('<%=id%>_list_form').elements['eReadyMassmailID'];

	if(typeof ids == 'undefined' ){
		alert('변경할 메일이 없습니다.');
		return;
	}

	if(typeof ids.length == 'undefined' ){
		alert('메일이 1개이므로 발송순서를 변경할 수 없습니다.');
		return;
	}
	if(confirm('발송순서가 변경됩니다. 변경하시겠습니까?'))
	{		
		
		copyForm( $('<%=id%>_rform'), frm );
	
		nemoRequest.init( 
		{
			busyWindowId: '<%=id%>'  // busy 를 표시할 window
			, url: 'massmail/write/massmail.do?method=savePriority&id=<%=id%>'
			, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
			, onSuccess: function(html,els,resHTML) {
				$('<%=id%>').list();
				//closeWindow( $('<%=id%>') );
				//MochaUI.massmailListWindow();
			}
		});

		nemoRequest.post(frm);
	}
	
	
}



/***********************************************/
/* 대상자검색리스트 
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
		busyWindowId: '<%=id%>_modal',  // busy 를 표시할 window
		updateWindowId: '<%=id%>_modal',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'massmail/write/massmail.do?method=getMassMailList&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}



/***********************************************/
/* 우선순위값 선택 변경
/***********************************************/
$('<%=id%>').chkDuplicateSelect = function( selObj, selectObjsID) {
	
	var selectObjs = $('<%=id%>_list_form').elements[selectObjsID];	
	var selectOldValues = $('<%=id%>_list_form').elements['selectOldValue'];	
	var selObjOldValue = 0;

	for(var i=0; i < selectObjs.length; i++ ) {
		if( selObj == selectObjs[i])
			selObjOldValue = selectOldValues[i].value;
	}		
	
	if(selObjOldValue > selObj.selectedIndex)
	{
		for(var i=0; i < selectObjs.length; i++ ) {
			if(selObj.selectedIndex <= selectObjs[i].selectedIndex && selObjOldValue > selectObjs[i].selectedIndex && selObj != selectObjs[i]) {
				selectObjs[i].selectedIndex = selectObjs[i].selectedIndex+1;
				$('<%=id%>_list_form').elements['selectOldValue'][i].value = selectObjs[i].selectedIndex ; 
			} 
			if(selObj == selectObjs[i])
				$('<%=id%>_list_form').elements['selectOldValue'][i].value = selObj.selectedIndex ;
		}
	}
	else
	{
		for(var i=0; i < selectObjs.length; i++ ) {
			if(selObj.selectedIndex >= selectObjs[i].selectedIndex && selObjOldValue < selectObjs[i].selectedIndex && selObj != selectObjs[i]) {
				selectObjs[i].selectedIndex = selectObjs[i].selectedIndex-1;
				$('<%=id%>_list_form').elements['selectOldValue'][i].value = selectObjs[i].selectedIndex ; 
			} 
			if(selObj == selectObjs[i])
				$('<%=id%>_list_form').elements['selectOldValue'][i].value = selObj.selectedIndex ;
		}
	}	
}


/* 리스트 표시 */
window.addEvent("domready",function () {	
	$('<%=id%>').init();
	$('<%=id%>').list(); 
});

</script>


<%
}
//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	
	<c:forEach items="${readyMassmailList}" var="readyMassmail" varStatus="list">
	<TR class="tbl_tr">
		<input type="hidden" id="eReadyMassmailID" name="eReadyMassmailID" value="<c:out value="${readyMassmail.massmailID}"/>" />	
		<input type="hidden" id="eReadyScheduleID" name="eReadyScheduleID" value="<c:out value="${readyMassmail.scheduleID}"/>" />	
		<TD class="tbl_td" align="center"><c:out value="${readyMassmail.rownum}"/></TD>		
		<TD class="tbl_td" align="center">
			<input id="selectOldValue" name="selectOldValue" type="hidden" value="<c:out value="${readyMassmail.rownum-1}"/>">
			<select id="ePriority" name="ePriority" onChange="$('<%=id%>').chkDuplicateSelect(this,'ePriority')">
				<c:forEach items="${readyMassmailList}" var="selectReady">
					<option value="<c:out value="${selectReady.rownum}"/>" <c:if test="${selectReady.rownum==readyMassmail.rownum}">selected</c:if>><c:out value="${selectReady.rownum}"/></option>
				</c:forEach>
			</select>
		</TD>
		<TD class="tbl_td" align="left"><b><c:out value="${readyMassmail.massmailTitle}"  escapeXml="true"/></b></TD>		
		<TD class="tbl_td" align="center"><fmt:formatNumber value="${readyMassmail.targetTotalCount}" type="number"/></TD>	
		<TD class="tbl_td" align="center">
			<c:if test="${readyMassmail.state == '13'}" >
					발송대기중
			</c:if>	
			<c:if test="${readyMassmail.state == '14'}" >
					발송중
			</c:if>
			<c:if test="${readyMassmail.state == '45'}" >
					발송중대기
			</c:if>		
		</TD>
		<TD class="tbl_td" align="center"><c:out value="${readyMassmail.sendScheduleDate}" /></TD>	
		
	</TR>
	</c:forEach>
	
	<script type="text/javascript">

		window.addEvent('domready', function(){

			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'curser' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
			});
			$('<%=id%>').grid_content.render();
			
		});

	</script>
<%
}
%>