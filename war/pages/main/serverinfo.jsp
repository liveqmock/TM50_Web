<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>  
<%
	String id = request.getParameter("id");
%>
	
<div id="<%=id%>Content" style="padding:8px;">
</div> 

<script type="text/javascript">
	$('<%=id%>').loadContent = function() {
		nemoRequest.init( 
			{
				url: 'pages/main/serverinfo_proc.jsp?id=<%=id%>', 
				update: $('<%=id%>Content'),
				updateEl: $('<%=id%>Content'),
				onSuccess: function(html,els,resHTML,scripts) {
				
				}
			});
		nemoRequest.post();
	}
	
	/* 내용 표시 */
	window.addEvent("domready",function () {
		$('<%=id%>').loadContent();
	});
</script>