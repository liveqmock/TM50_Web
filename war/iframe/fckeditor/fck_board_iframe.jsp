<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="net.fckeditor.*" %>
<%@ page import="java.util.Map"%>   
<%@ page import="web.admin.board.model.*"%>
<%@ page import="web.admin.board.service.BoardService"%>
<%@ page import="web.admin.board.control.BoardControlHelper"%>    
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>    
<%	
	
	String boardID = request.getParameter("boardID") == null ? "0" : request.getParameter("boardID").trim();
	
	String content = "";
	//모델을 이용하여 리스트를 가져온다.
	if(!boardID.equals("") && !boardID.equals("0")){
		BoardService service = BoardControlHelper.getUserService(application);
		Board board = service.viewBoard(Integer.parseInt(boardID));
		content = board.getContent();
	}
	FCKeditor fckEditor = new FCKeditor(request, "ezTextarea");
	fckEditor.setBasePath("../../fckeditor/");	
	fckEditor.setHeight("300");		
	
	fckEditor.setToolbarSet("Tm");	
	fckEditor.setValue(content);

	
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