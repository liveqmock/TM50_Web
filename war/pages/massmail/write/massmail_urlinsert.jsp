<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String preID = request.getParameter("preID");
	String id = request.getParameter("id");
	String eWebURLInsert = request.getParameter("eWebURLInsert");
	String encodingType = request.getParameter("encodingType");
	if(encodingType == null){
		encodingType="UTF-8";
	}
	ExtractorHTML ehtml = new ExtractorHTML(eWebURLInsert);
	String content = ehtml.getHTML(encodingType);
	
%>
<form id="<%=id%>_contentform" name="<%=id%>_contentform">
<textarea id="urlInsertContent" name="urlInsertContent" ><%= StringUtil.replace(StringUtil.replace(StringUtil.replace(StringUtil.replace(content,"&","&amp;"),"<","&lt;"),">","&gt;"),"§","&sect")%></textarea>
</form>
<script type="text/javascript">

var frm = $('<%=id%>_contentform');
//var html = frm.urlInsertContent.value;
$('<%=preID%>_ifrmFckEditor').contentWindow.setFCKHtml(frm.urlInsertContent.value);
closeWindow($('<%=id%>'));
</script>