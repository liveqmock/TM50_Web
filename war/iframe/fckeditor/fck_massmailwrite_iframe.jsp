<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="net.fckeditor.*" %>
<%@ page import="java.util.Map"%>   
<%@ page import="web.massmail.write.model.MassMailInfo"%>
<%@ page import="web.massmail.write.service.MassMailService"%>
<%@ page import="web.massmail.write.control.MassMailControlHelper"%>    
 
<%	
	
	String massmailID = request.getParameter("massmailID") == null ? "0" : request.getParameter("massmailID").trim();
	
	String mailContent = "";
	//모델을 이용하여 리스트를 가져온다.
	if(!massmailID.equals("") && !massmailID.equals("0")){
		MassMailService service = MassMailControlHelper.getUserService(application);
		Map<String,Object>rsMap = service.getMailContent(Integer.parseInt(massmailID));
		mailContent = String.valueOf(rsMap.get("mailContent")==null?"":rsMap.get("mailContent"));
		
	}
	
	
%>
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
<div style="display:none">
	<textarea id="content_hidden" name="content_hidden">
	<%=mailContent%>
	</textarea>
</div>


<script>

	var oFCKeditor = new FCKeditor('tmTextarea','100%','500','Tm',document.getElementById("content_hidden").value);  

	oFCKeditor.BasePath="../../fckeditor/";	
	
	oFCKeditor.Create();	

</script>

</body>
</html>