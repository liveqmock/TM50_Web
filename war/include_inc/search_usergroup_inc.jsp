<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@ page import="web.common.util.StringUtil"%>
<%@page import="web.admin.usergroup.model.Group"%>
<%@page import="web.admin.usergroup.service.UserGroupService"%>
<%@page import="web.admin.usergroup.control.UserGroupControlHelper"%>
  
<%
		//모델을 이용하여 리스트를 가져온다.
		UserGroupService service = UserGroupControlHelper.getUserService(application);
		List<Group> groupList = service.listGroup();	
%>
			
			<ul id="sGroupID"  class="selectBox">

<%
			if(groupList.size()==0){							
%>						
				<li data="" select="yes">등록된 그룹이  없습니다</li>
<%
			}else{
				
%>
			<li data="">--사용자그룹--</li>
			
<%
				for(int i=0;i<groupList.size();i++){
					Group group =  (Group)groupList.get(i);
%>			
					
					<li data="<%=group.getGroupID() %>"><%=StringUtil.head(group.getGroupName(),7) %></li>
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>