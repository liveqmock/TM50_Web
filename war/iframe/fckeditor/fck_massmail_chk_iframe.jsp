<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="net.fckeditor.*" %>
    <%String parent_ID = request.getParameter("parent_id"); %>
 
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../../fckeditor/fckeditor.js">

</script>

<script type="text/javascript">
function insertFCKHtml(html){
var oEditor = FCKeditorAPI.GetInstance('tmTextarea')			
oEditor.InsertHtml(html);
}

function getFCKHtml(){	
	var oEditor = FCKeditorAPI.GetInstance('tmTextarea')			
	return oEditor.GetXHTML();
}

function setFCKHtml(html){
	var oEditor = FCKeditorAPI.GetInstance('tmTextarea')			
	oEditor.SetHTML(html);
}

</script>
</head>
<body>
<script>

	var oFCKeditor = new FCKeditor('tmTextarea','100%','500','Tm',parent.$('<%=parent_ID%>').getElementById("eContent").value);  

	oFCKeditor.BasePath="../../fckeditor/";	
	
	oFCKeditor.Create();	

</script>

</body>
</html>