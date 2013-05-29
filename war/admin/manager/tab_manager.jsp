<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>
<%
	String id = request.getParameter("id");

	String isAdmin = LoginInfo.getIsAdmin(request);
	
	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>
<div class="toolbarTabs">
	<ul id="managerTabs" class="tab-menu">
		<li id="managerKisaLink" ><a>Kisa</a></li>
		<li id="managerCBLLink"><a>SPAMHAUS</a></li>
		<li id="managerEngineCheck" class="selected"><a>Engine</a></li>
		<li id="managerServerCheck"><a>서버 성능</a></li> 
		<!--  <li id="managerTailLink"><a>WebTail</a></li>-->
	</ul>
	<div class="clear"></div>
</div>

<script type="text/javascript">

	MochaUI.initializeTabs('managerTabs');

	$('managerKisaLink').addEvent('click', function(e){
		$clear(<%=id%>ServerTimer);
		nemoRequest.init( 
				{
					busyWindowId: '<%=id%>',  
					updateWindowId: '<%=id%>',  

					url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=kisa', 
					update: $('<%=id%>_grid_content'), 
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
				nemoRequest.post($('<%=id%>_rform'));
	});

	$('managerCBLLink').addEvent('click', function(e){
		$clear(<%=id%>ServerTimer);
		nemoRequest.init( 
				{
					busyWindowId: '<%=id%>',  
					updateWindowId: '<%=id%>',  

					url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=cblrbl', 
					update: $('<%=id%>_grid_content'), 
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
				nemoRequest.post($('<%=id%>_rform'));
	});

	$('managerEngineCheck').addEvent('click', function(e){
		$clear(<%=id%>ServerTimer);
		nemoRequest.init( 
				{
					busyWindowId: '<%=id%>',  
					updateWindowId: '<%=id%>',  

					url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=engine', 
					update: $('<%=id%>_grid_content'), 
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
				nemoRequest.post($('<%=id%>_rform'));
	});

	$('managerServerCheck').addEvent('click', function(e){
		$clear(<%=id%>ServerTimer);
		nemoRequest.init( 
				{
					busyWindowId: '<%=id%>',  
					updateWindowId: '<%=id%>',  

					url: 'admin/manager/manager_proc.jsp?id=<%=id%>&mode=server', 
					update: $('<%=id%>_grid_content'), 
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
				nemoRequest.post($('<%=id%>_rform'));
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