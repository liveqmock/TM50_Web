<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String htmlMsg = "";
	if(request.getParameter("message").equals("rejectSuccess")){
		htmlMsg = "해당 이메일["+request.getParameter("email")+"]을 수신거부처리 하였습니다.";
	}else if(request.getParameter("message").equals("rejectFail")){
		htmlMsg = "해당 이메일["+request.getParameter("email")+"]을 수신거부처리 실패하였습니다.";
	}else if(request.getParameter("message").equals("rejectAlready")){
		htmlMsg = "해당 이메일["+request.getParameter("email")+"]은 이미 수신거부처리 되었습니다.";
	}
%>

   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>수신거부처리</title>
</head>
<body>	


	<center><%=htmlMsg%></center>
</body>
</html>