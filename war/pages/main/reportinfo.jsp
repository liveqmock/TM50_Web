<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>  
<%
	String id = request.getParameter("id");
%>
	<div class="show_wrapper" style="width:90%">
		<div> 
			<img src="images/icon_thunder.gif"> <b>스펨등록여부</b> <a href="javascript:$('<%=id%>').helpWindow()" title="도움말 보기" style="cursor:help"><img src="images/help.gif" width="17" /></a>
		</div>
		<div id="<%=id%>CBLRBLContent">
		</div> 
		<div>
			<img src="images/icon_thunder.gif"> <b>관심 도메인 성공율</b>
		</div>
		<div id="<%=id%>DomainContent">
		</div>
	</div>

<script type="text/javascript">
	$('<%=id%>').loadContent = function() {
		nemoRequest.init( 
			{
				url: 'pages/main/reportinfo_proc.jsp?id=<%=id%>&method=cblrbl', 
				update: $('<%=id%>CBLRBLContent'),
				updateEl: $('<%=id%>CBLRBLContent'),
				onSuccess: function(html,els,resHTML,scripts) {
				
				}
			});
		nemoRequest.post();
		nemoRequest.init( 
			{
				url: 'pages/main/reportinfo_proc.jsp?id=<%=id%>&method=domain', 
				update: $('<%=id%>DomainContent'),
				updateEl: $('<%=id%>DomainContent'),
				onSuccess: function(html,els,resHTML,scripts) {
				}
			});
		nemoRequest.post();
	}
	$('<%=id%>').helpWindow = function() {
		nemoWindow(
				{
					'id': '<%=id%>_help',
		
					busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
					width: 580,
					height: $('mainColumn').style.height.toInt(),
					title: 'CBL / KISA RBL 이란?',
					container: 'desktop',
					noOtherClose: true,
					loadMethod: 'xhr',
					contentURL: 'pages/helpdesk/helpdesk_proc.jsp?id=<%=id%>&method=spam'
				}
			);
	}
	/* 리스트 표시 */
	window.addEvent("domready",function () {
		$('<%=id%>').loadContent();
	});
</script>