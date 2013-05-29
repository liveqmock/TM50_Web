<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%@ page import="web.admin.usergroup.model.User"%>
<%@ page import="web.admin.usergroup.service.UserGroupService"%>
<%@ page import="web.admin.usergroup.control.UserGroupControlHelper"%>
<% 
	UserGroupService service = UserGroupControlHelper.getUserService(application);
	User user = service.getHelperUser();
%>
<table width="100%">
	<tr>
		<td> 
			<img src="images/user.png"> <%=user.getUserName() %>
		</td>
	</tr>
	<tr>
		<td> 
			<img src="images/telephone_go.png"> <%=user.getCellPhone() %>
		</td>
	</tr>
	<tr>
		<td> 
			<img src="images/email_go.png"> <%=user.getEmail() %>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td> 
			  <div class="btn_green" style="float:right;"><a href="#"><span>도움말 다운로드</span></a></div><div class="btn_green" style="float:right;"><a href="#"><span>도움말</span></a></div>
		</td>	
		
	</tr>
</table>

