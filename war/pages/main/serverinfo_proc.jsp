<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.CheckServer" %>  
<%@ page import="web.common.util.PropertiesUtil" %>    
<%
	String id = request.getParameter("id");

	String install_path = PropertiesUtil.getStringProperties("configure", "install_path");
	long hardTotalSpace = CheckServer.getHardTotalSpace(install_path);
	long hardUsableSpace = CheckServer.getHardUsableSpace(install_path);
	long hardUseSpace = hardTotalSpace - hardUsableSpace;
	
%>
	<table>
	 	<%if(CheckServer.floatToGB(hardUsableSpace) < 1){ %>
	    <tr>
			<td colspan="3"> 
				<img src="images/exclamation.png">
				<font color="red">
				디스크 용량이 부족합니다.</font>
			</td>
		</tr>
	    <%}else if(CheckServer.floatToGB(hardUsableSpace) < 5){ %>
	    <tr>
			<td colspan="3"> 
				<img src="images/error.png">
				<font color="orange">
				디스크 용량 관리가 필요합니다.</font>
			</td>
		</tr>
		
	    <%}else{ %>
	    <tr>
			<td colspan="3"> 
				<img src="images/accept.png">
				<font color="green">
				디스크 용량이 충분합니다..</font>
			</td>
		</tr>
	    <%} %>
	    <tr>
			<td colspan="3"></td>
		</tr>
		<tr>
			<td width="10px"><img src="images/drive_delete.png"></td>
			<td>사용중인 공간 :</td>
			<td align="right"><%=String.format("%.2f",CheckServer.floatToGB(hardUseSpace)) %>GB</td>
		</tr>
		<tr>
			<td><img src="images/drive_add.png"></td>
		<td>사용 가능한 공간 :</td>
			<td align="right"><%=String.format("%.2f",CheckServer.floatToGB(hardUsableSpace)) %>GB</td>
		</tr>
		<tr bgcolor="F7F7F7">
			<td colspan="3" height="1px" bgcolor="#888888"></td>
		</tr>
		<tr>
			<td><img src="images/drive.png"></td>
			<td>총 할당 공간 :</td>
			<td align="right" ><%=String.format("%.2f",CheckServer.floatToGB(hardTotalSpace)) %>GB</td>
		</tr>
	</table><hr />
	<table>
		<tr>
			<td>
				<img src="images/tag_blue.png" alt="Tips "> <strong>Tips</strong>
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;<img src="images/arrow-right2.gif"> <strong>Ctrl-Alt-Q :</strong> 선택 창 숨기기/보이기.
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;<img src="images/arrow-right2.gif"> <strong>F11 :</strong> 브라우저 전체 화면 보기.
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;<img src="images/arrow-right2.gif"> <strong>Internet Explorer 7, 8 사용시 :</strong><br /> 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;도구>>인터넷옵션>>검색기록[설정] <br />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;>> 저장된 페이지의 새 버전 확인<br />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;설정이 "페이지를 열 때마다" 로 <br /> 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;설정 되어있어야합니다.<br />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;설정되어 있지 않을 경우 Logout과 <br/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;권한설정에서 문제가 발생 <br/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;할 수 있습니다.
			</td>
		</tr>
	</table>

