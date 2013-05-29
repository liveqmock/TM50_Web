<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>

<%
	String id = request.getParameter("id");
	String isAdmin = LoginInfo.getIsAdmin(request);
	
	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>
<div style="clear:both;width:550px">
	<form name="<%=id%>_form" id="<%=id%>_form">
	<table border="0" cellspacing="0" cellpadding="0" width="600px" >
		<tbody id="<%=id%>_grid_content">
		</tbody>
	</table>
	</form>
</div>
        
<script type="text/javascript">
var <%=id%>ServerTimer;
$('<%=id%>').onload = function ( type ) {

	var frm = $('<%=id%>_sform');

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode='+type, 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/* 화면 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').onload('engine'); 
});
//MochaUI.Windows.instances.get('<%=id%>').addEvent('onClose',function() { $clear(<%=id%>ServerTimer) } );
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