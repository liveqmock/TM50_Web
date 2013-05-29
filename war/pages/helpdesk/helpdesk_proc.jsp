<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%@page import="web.common.util.LoginInfo"%>
<%@ page import="web.admin.usergroup.model.User"%>
<%@ page import="web.admin.usergroup.service.UserGroupService"%>
<%@ page import="web.admin.usergroup.control.UserGroupControlHelper"%>
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	
	if(method.equals("spam")) {
%>
	<table>
		<tr> 
			<td>
				<img src="images/cbl-logo.gif" width="170" height="64">
			    <br><br>
			</td>
		</tr>
		<tr>
			<td>
				<b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a></b>은 
				스팸 지수가 높은 IP를 수집하는 싸이트 입니다.<br>
				<b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a></b>에 
				IP가 등록되는 것 자체만으로 차단 되는 것은 아니지만, 각 메일 수신서버의  스팸차단 솔루션이<br>
				<b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a></b>을 
				참고하여 해당 IP의 메일 수신 허용/차단을 관리하므로 
				<b><a href ="http://cbl.abuseat.org/lookup.cgi" target='_blank' title="CBL 바로가기">CBL</a></b>에 IP가 등록되면 대다수 사이트의<br> 
				 대량메일 발송이 완전 차단되게 됩니다.<br><br>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td>
				<a href="https://www.kisarbl.or.kr/" target="_blank">
				<img src="images/kisa-logo.jpg" width="178" height="55">
				</a>
			</td>
			
		</tr>
		<tr>
			<td>
				<a href="https://www.kisarbl.or.kr/" target="_blank" title="한국정보보호진흥원 홈페이지 바로가기"><b>KISA-RBL</b></a>은 메일서버를 운영하는 누구나 손쉽게 효과적으로 스팸을 차단하는데 이용할 수 있도록<br>
				<b>한국정보보호진흥원(KISA)</b>에서 무료로 관리ㆍ운영하여 제공하는 실시간 스팸 차단 리스트입니다.<br>
				국내외로부터 스팸정보를 실시간으로 취합하고 이를 다양한 기준에 따라 분석한 결과,스팸전송에<br>
				 관련된 것으로 확인된 IP들을 리스트로 생성하여 1시간 단위로 제공합니다.<br>
				 대부분의 국내 대형 포털에서는  KISA RBL을 참고하여 해당 IP의 메일 수신 허용/차단을 관리하므로<br>
				 LIST에 등록 되지 않도록 화이트 도메인 등록을 꼭 해주셔야 합니다.<br><br>
			</td>
		</tr>
	</table>

<%
	}
%>