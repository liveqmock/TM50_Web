<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="net.fckeditor.*" %>
<%@ page import="web.content.mailtemplate.model.MailTemplate"%>
<%@ page import="web.content.mailtemplate.service.MailTemplateService"%>
<%@ page import="web.content.mailtemplate.control.MailTemplateControlHelper"%>    
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>    
<jsp:useBean id="templateID" class="java.lang.String" scope="request" />
<%	
	//templateID를 통해 ez_massmail_templates에 templateContent를 가져온다. 
	templateID = request.getParameter("templateID") == null ? "0" : request.getParameter("templateID").trim();

	MailTemplate mailTemplate = null;
	//모델을 이용하여 리스트를 가져온다.
	if(!templateID.equals("") && !templateID.equals("0")){
		MailTemplateService service = MailTemplateControlHelper.getUserService(application);
		mailTemplate = service.viewMailTemplate(Integer.parseInt(templateID));
	}
	FCKeditor fckEditor = new FCKeditor(request, "ezTextarea");
	fckEditor.setBasePath("../../fckeditor/");	
	fckEditor.setHeight("400");	
	fckEditor.setToolbarSet("Tm");	
	
	if(mailTemplate!=null){
		fckEditor.setValue(mailTemplate.getTemplateContent());
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
		
	function FCKeditor_OnComplete(editorInstance) {
		window.status = editorInstance.Description;
	}
	function getFCKHtml(){	
		var oEditor = FCKeditorAPI.GetInstance('ezTextarea')	
		return oEditor.GetXHTML();
	}

	//메일 본문 전체 교체 
	function setFCKHtml(html){
		var oEditor = FCKeditorAPI.GetInstance('ezTextarea')			
		oEditor.SetHTML(html);
	}

	//본문 커서위에 해당 문자열 삽입
	function insertFCKHtml(html){
		var oEditor = FCKeditorAPI.GetInstance('ezTextarea')			
		oEditor.InsertHtml(html);
	}
</script>
</head>

<body>
<%
	//fckEditor.setValue("This is some <strong>sample text</strong>. You are using <a href=\"http://www.fckeditor.net\">FCKeditor</a>.");	
	out.println(fckEditor);	
%>
</body>
</html>