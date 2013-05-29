<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="net.fckeditor.*" %>
<%@ page import="web.intermail.model.InterMailEvent"%>
<%@ page import="web.intermail.service.InterMailService"%>
<%@ page import="web.intermail.control.InterMailControlHelper"%>    
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>    
<%	
	//templateID를 통해 ez_massmail_templates에 templateContent를 가져온다. 
	String intermailID = request.getParameter("intermailID") == null ? "0" : request.getParameter("intermailID").trim();

	InterMailEvent autoMailEvent = null;
	//모델을 이용하여 리스트를 가져온다.
	if(!intermailID.equals("") && !intermailID.equals("0")){
		InterMailService service = InterMailControlHelper.getUserService(application);
		autoMailEvent = service.viewInterMailEvent(Integer.parseInt(intermailID));
	}
	FCKeditor fckEditor = new FCKeditor(request, "ezTextarea");
	fckEditor.setBasePath("../../fckeditor/");	
	fckEditor.setHeight("400");	
	fckEditor.setToolbarSet("Tm");	
	
	if(autoMailEvent!=null){
		fckEditor.setValue(autoMailEvent.getTemplateContent());
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
</script>
</head>

<body>
<%
	//fckEditor.setValue("This is some <strong>sample text</strong>. You are using <a href=\"http://www.fckeditor.net\">FCKeditor</a>.");	
	out.println(fckEditor);	
%>
</body>
</html>