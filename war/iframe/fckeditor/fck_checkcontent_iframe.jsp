<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="web.common.util.StringUtil"%>
<% 
	String id = request.getParameter("id");
	String seq = request.getParameter("seq");
	String eTemplateContent = request.getParameter("eTemplateContent");
	web.common.util.ImageExtractor ie = new web.common.util.ImageExtractor(eTemplateContent);
	java.util.ArrayList linkinfoList = (java.util.ArrayList) ie.extractAllImages();
	String[] temp_array = null;
	
	if(linkinfoList.size()==0){
%>
	<html>
		<head>
		<script language="javascript">
				// 로컬 이미지가 없을 경우 바로 저장
				parent.saveData();
			</script>
		</head>
	</html>
<%	}else{ 
	String baseURL = "http://localhost:8080/ezmail/upload/"+web.common.util.DateUtils.getDateString()+"/";
	eTemplateContent = ie.changeImageURL(linkinfoList, baseURL);
%>
	<html>
		<body onload="upload()">
			<!-- 엑티브엑스 컨트롤 -->
			<OBJECT id="EzMailActiveX" name="AndwiseActiveX"
				  classid="clsid:D417B099-B318-47CE-AC50-FB530D339F32"
				  codebase="JiniWorksFileUploadProj.cab#version=1,0,27,0"
				  width=600
				  height=250
				  align=center
				  hspace=0
				  vspace=0
			>
			
			<!-- 업로드 url -->
			<param name='upload_url' value="http://localhost:8080/ezmail/activex/upload_file1.jsp"/>
			<!-- 서버 페이지에서 성공적으로 완료되면 페이지에 표시할 문자 -->
			<param name='compare_success_text' value="SUCCESS"/>
			<!-- 파일 컨트롤 아이디 -->
			<param name='file_control_id' value="upload_file"/>
			
			<param name='control_enabled' value="false"/>
			
			</OBJECT>
			
			<p><input type="button" onclick="upload()" value="업로드"></p>
			
			<form id="check_form" method="post">
			<table style="display:none"><tr><td id="content">
				<textarea name="inner_text"><%= StringUtil.replace(StringUtil.replace(StringUtil.replace(StringUtil.replace(eTemplateContent,"&","&amp;"),"<","&lt;"),">","&gt;"),"§","&sect") %></textarea>
			</td></tr></table>
			
			<script type="text/javascript">
				var obj = document.getElementById("EzMailActiveX");
				<%for(int i=0;i < linkinfoList.size();i++){
				    temp_array = (String[]) linkinfoList.get(i);%>      
					obj.add_file("<%=temp_array[0]%>");
				<%}%>
				function upload() {
					var obj = document.getElementById("EzMailActiveX");
					obj.upload();
					if(obj.result_code == '0')
						parent.changeData();
					else
						alert('이미지 업로드 중 오류가 발생했습니다. \r관리자에게 문의 하세요');
								
				}
			</script>
		</body>
	</html>
		
<%} %>

