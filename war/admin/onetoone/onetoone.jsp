<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>

<%
	String id = request.getParameter("id");

	String isAdmin = LoginInfo.getIsAdmin(request);
	
	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>


<div style="clear:both;width:450px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="450px" >
	<thead>
		<tr>		
		<th style="height:30px;width:100px">원투원ID</th>
		<th style="height:30px">원투원명</th>
		<th style="height:30px;width:200px">원투원변수</th>
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
/* 리스트 
/***********************************************/

$('<%=id%>').list = function () {
	

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'admin/onetoone/onetoone.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post();
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