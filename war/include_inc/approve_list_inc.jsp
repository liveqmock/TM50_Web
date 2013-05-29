<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@page import="web.admin.usergroup.model.User"%>
<%@page import="web.admin.usergroup.service.UserGroupService"%>
<%@page import="web.admin.usergroup.control.UserGroupControlHelper"%>

<%
		String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();
		String groupID = request.getParameter("groupID") == null ? "" : request.getParameter("groupID").trim();
		String approveUserID = request.getParameter("approveUserID") == null ? "" : request.getParameter("approveUserID").trim();
		//모델을 이용하여 리스트를 가져온다.
		UserGroupService service = UserGroupControlHelper.getUserService(application);
		List<User> userList = service.selectUserMaster(groupID);
%>
			
			<ul id="eApproveUserID"  class="selectBox" title="승인담당자선택" <%=(mustInput.equals("Y")? "mustInput='Y' msg='승인담당자선택'":"")%>>

<%
			if(userList.size()==0){							
%>						
				<li data="" select="yes">등록된 데이타가  없습니다</li>
<%
			}else{
				
%>

<%
				for(int i=0;i<userList.size();i++){
					User user =  (User)userList.get(i);
%>			
					
					<li data="<%=user.getUserID() %>" <% if (user.getUserID().equals(approveUserID)){%>select="yes"<%} %>><%=user.getUserName() %></li>
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>