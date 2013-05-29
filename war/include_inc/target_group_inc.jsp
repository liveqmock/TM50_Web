<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@page import="web.admin.targetgroup.model.TargetGroup"%>
<%@page import="web.admin.targetgroup.service.TargetGroupService"%>
<%@page import="web.admin.targetgroup.control.TargetGroupControlHelper"%>
<%
		String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();
		String targetGroupID = request.getParameter("targetGroupID") == null ? "0" : request.getParameter("targetGroupID").trim();
		if(targetGroupID.equals("")) targetGroupID="0";
		
		//모델을 이용하여 리스트를 가져온다.
		TargetGroupService service = TargetGroupControlHelper.getUserService(application);
		List<TargetGroup> targetgroupList = service.listTargetGroup();
		List<TargetGroup> targetgroupDefaultY = service.getTargetGroupDefaultY(); // 대상자 그룹 분류 디폴트 Y 검색
		
	
%>
			
			<ul id="eTargetGroupID"  class="selectBox" <%=(mustInput.equals("Y")? "mustInput='Y' msg='분류선택'":"")%>>

<%
			if(targetgroupList.size()==0){							
%>						
				<li data="" select="yes">대상자분류가  없습니다<%=mustInput %></li>
<%
			}else{
				if(targetgroupDefaultY.size() == 0 && targetGroupID.equals("0")){			
%>				
					<li data="" select="yes">대상자그룹분류선택</li>		
<%
				}
					
				if(targetgroupDefaultY.size() > 0 && targetGroupID.equals("0")){
					for(int i=0;i<targetgroupList.size();i++){
						TargetGroup targetgroup = (TargetGroup)targetgroupList.get(i);					
						TargetGroup targetDefaultGroup = (TargetGroup)targetgroupDefaultY.get(0);// 디폴트로 검색된 대상자그룹 분류는 없거나 1개 이기 때문에 .get(0)으로(첫번째 인덱스) 설정
%>								
						<li data="<%=targetgroup.getTargetGroupID()%>" <% if (targetgroup.getTargetGroupID()== targetDefaultGroup.getTargetGroupID()){%>select="yes"<%} %>><%=targetgroup.getTargetGroupName()%></li>					
<%
					}//end for 
				}else{
					for(int i=0;i<targetgroupList.size();i++){
						TargetGroup targetgroup = (TargetGroup)targetgroupList.get(i);
%>									
						<li data="<%=targetgroup.getTargetGroupID()%>" <% if (targetgroup.getTargetGroupID()== Integer.parseInt(targetGroupID)){%>select="yes"<%} %>><%=targetgroup.getTargetGroupName()%></li>
<%
					}//end for 
				}
			}// end if 
%>
			</ul>