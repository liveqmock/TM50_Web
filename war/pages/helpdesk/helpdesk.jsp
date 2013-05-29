<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = request.getParameter("id");
%>
	<div class="helpdesk_wrapper">
		<div class="menu"><a href="#" onClick="window.open('../../helper_docs/1.html','new','width=950,height=700','scrollbars=yes');" >도움말 보기</a></div>
		<div class="menu"><a href="">도움말 다운로드</a></div>
		<div class="menu"><a href="javascript:aboutWindow()">ThunderMail 정보</a></div>
	</div>
	<script type="text/javascript">
		function aboutWindow() {
			new MochaUI.Window({
				id: 'about',
				title: 'MochaUI',
				loadMethod: 'xhr',
				contentURL: 'pages/main/about.html',
				type: 'modal2',
				width: 350,
				height: 175,
				contentBgColor: '#a7dbff url(images/thunsermail_logo.gif) left 3px no-repeat',
				padding: { top: 43, right: 12, bottom: 10, left: 5 },
				scrollbars:  false
			});
		}
	</script>