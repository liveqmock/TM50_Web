<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String preID = request.getParameter("preID");
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	String templateType = request.getParameter("templateType");

//****************************************************************************************************/
//대상차 팝업  
//****************************************************************************************************/
if(method.equals("mailTemplate")) {
%>

<div style="clear:both;width:380px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="480px" >
	<thead>
		<tr>		
		<th style="height:30px">템플릿명</th>
		<th style="height:30px;width:100px">등록자</th>
		<th style="height:30px;width:100px">사용구분</th>
		<th style="height:30px;width:50px">적용</th>
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
/* 메일템플릿 사용카운트 증가 
/***********************************************/
$('<%=id%>').updateUsedCount = function (seq) {	

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'massmail/write/massmail.do?id=<%=id%>&method=updateUsedCount&eTemplateID='+seq, 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
			$('<%=id%>').insertTemplateContent(seq);
		}
	});
	nemoRequest.post();
}



/***********************************************/
/* 테스트메일 텍스트창에 입력 
/***********************************************/
$('<%=id%>').insertTemplateContent = function(seq){
	//alert(eval("$('<%=id%>_list_form').templateContent"+seq+".value"));
	var html = eval("$('<%=id%>_list_form').templateContent"+seq+".value");
	$('<%=preID%>_ifrmFckEditor').contentWindow.setFCKHtml(html);	
	closeWindow($('<%=id%>'));
}

/***********************************************/
/* 템플릿보기
/***********************************************/
$('<%=id%>').viewTemplateWindow = function(seq) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',
	
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
	
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			//height: 500,
			title: '메일템플릿보기',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'massmail/write/massmail.do?id=<%=id%>&method=viewMailTemplate&eTemplateID='+seq
		}
	);
}


/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_list_form');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );

	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );


	$('<%=id%>').list();

}


/***********************************************/
/* 탬플릿 리스트 
/***********************************************/

$('<%=id%>').list = function ( ) {	

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'massmail/write/massmail.do?method=listMailTemplate&id=<%=id%>&templateType=<%=templateType%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post();
}

/* 리스트 표시 */
window.addEvent("domready",function () {	
	$('<%=id%>').init();
	//$('<%=id%>').createPopup();

});

</script>


<%
}
//****************************************************************************************************/
// 탬플릿리스트
//****************************************************************************************************/
if(method.equals("listMailTemplate")) {
%>

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
	
	
	<c:forEach items="${mailTemplateList}" var="mailTemplate">
	
	<TR class="tbl_tr" targetList_id="<c:out value="${mailTemplate.templateID}"/>">		
		<TD class="tbl_td" align="left"><a href="javascript:$('<%=id%>').viewTemplateWindow('<c:out value="${mailTemplate.templateID}"/>');"><c:out value="${mailTemplate.templateName}" escapeXml="true"/></a>
				<input type="hidden" name="templateContent<c:out value="${mailTemplate.templateID}"/>" value="<c:out value="${mailTemplate.templateContent}"/>"/>
		</TD>		
		<TD class="tbl_td"><c:out value="${mailTemplate.userName}"/></TD>	
		<TD class="tbl_td">
			<c:if test="${mailTemplate.templateType == '1'}">대량메일</c:if>
			<c:if test="${mailTemplate.templateType == '2'}">자동메일</c:if>
			<c:if test="${mailTemplate.templateType == '3'}">연동메일</c:if>
			<c:if test="${mailTemplate.templateType == '4'}">공통</c:if>
		</TD>
		<TD class="tbl_td"><a href="javascript:$('<%=id%>').updateUsedCount('<c:out value="${mailTemplate.templateID}"/>')">선택</a></TD>	
	</TR>
	</c:forEach>
	
	<script type="text/javascript">


		window.addEvent('domready', function(){
				
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
<%
}
//****************************************************************************************************/
//  편집 
//****************************************************************************************************/
if(method.equals("viewMailTemplate")) {
%>	
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">

			<input type="hidden" id="eTemplateID" name="eTemplateID" value="<c:out value="${mailTemplate.templateID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">템플릿명</td>
				<td class="ctbl td"><input type="text" id="eTemplateName" name="eTemplateName" value="<c:out value="${mailTemplate.templateName}"/>" size="50" mustInput="Y" msg="템플릿명을  입력"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>				
				<td class="ctbl ttd1" width="100px">HTML</td>
				<td class="ctbl td">
				<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_mailtemplate_iframe.jsp?templateID=<c:out value="${mailTemplate.templateID}"/>" height=400  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
				</td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			</tbody>
			<tbody id="<%=id%>_check_content" >
			</tbody>
			</table>
		</form>
	</div>
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;margin-right:5px;"><a href="javascript:$('<%=id%>').updateUsedCount('<c:out value="${mailTemplate.templateID}"/>')"  class="web20button bigblue">적 용</a></div>
	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
	</script>

<%
}
%>