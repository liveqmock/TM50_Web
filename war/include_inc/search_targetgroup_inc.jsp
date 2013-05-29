<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@ page import="web.common.util.StringUtil"%>
<%@page import="web.admin.targetgroup.model.TargetGroup"%>
<%@page import="web.admin.targetgroup.service.TargetGroupService"%>
<%@page import="web.admin.targetgroup.control.TargetGroupControlHelper"%>
<%
		
		
		//모델을 이용하여 리스트를 가져온다.
		TargetGroupService service = TargetGroupControlHelper.getUserService(application);
		List<TargetGroup> massMailgroupList = service.listTargetGroup();
		String targetGroupID = request.getParameter("targetGroupID") == null ? "0" : request.getParameter("targetGroupID").trim();
		if(targetGroupID.equals("")) targetGroupID="0";
	
%>
			
			<ul id="sTargetGroupID"  class="selectBox">

<%
			if(massMailgroupList.size()==0){							
%>						
				<li data="" select="yes">대상자분류가  없습니다</li>
<%
			}else{
				
%>
			<li data="">--대상자분류--</li>
<%
				for(int i=0;i<massMailgroupList.size();i++){
					TargetGroup targetGroup = (TargetGroup)massMailgroupList.get(i);
%>			
					
					<li data="<%=targetGroup.getTargetGroupID()%>" <% if (targetGroup.getTargetGroupID()== Integer.parseInt(targetGroupID) || targetGroup.getIsDefault().equals("Y")){%>select="yes"<%} %>><%=StringUtil.head(targetGroup.getTargetGroupName(),7)%></li>
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>