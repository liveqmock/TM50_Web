<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="net.fckeditor.*" %>
<%@ page import="java.util.Map"%>
<%@ page import="web.content.poll.model.PollTemplate"%>
<%@ page import="web.content.poll.service.PollService"%>
<%@ page import="web.content.poll.control.PollControlHelper"%>    
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>    
<%	
	//templateID를 통해 ez_massmail_templates에 templateContent를 가져온다. 
	String pollID = request.getParameter("pollID") == null ? "0" : request.getParameter("pollID").trim();

	Map<String,Object> resultMap = null;
	String resultPollHTML = "";
	//모델을 이용하여 리스트를 가져온다.
	if(!pollID.equals("") && !pollID.equals("0")){
		PollService service = PollControlHelper.getUserService(application);
		resultMap = service.showResultDefaultPollHTML(Integer.parseInt(pollID));
	}
	resultPollHTML = (String)resultMap.get("resultPollHTML");
	FCKeditor fckEditor = new FCKeditor(request, "ezTextarea");
	fckEditor.setBasePath("../../fckeditor/");	
	fckEditor.setHeight("600");	
	fckEditor.setToolbarSet("Tm");	
	
	if(resultPollHTML!=null && !resultPollHTML.equals("")){
		fckEditor.setValue(resultPollHTML);
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