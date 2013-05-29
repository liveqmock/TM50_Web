<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@ page import="web.common.util.StringUtil"%>
<%@page import="web.admin.massmailgroup.model.MassMailGroup"%>
<%@page import="web.admin.massmailgroup.service.MassMailGroupService"%>
<%@page import="web.admin.massmailgroup.control.MassMailGroupControlHelper"%>
<%
		
		
		//모델을 이용하여 리스트를 가져온다.
		MassMailGroupService service = MassMailGroupControlHelper.getUserService(application);
		List<MassMailGroup> massMailgroupList = service.listMassMailGroup();
		String massmailGroupID = request.getParameter("massmailGroupID") == null ? "0" : request.getParameter("massmailGroupID").trim();
		if(massmailGroupID.equals("")) massmailGroupID="0";
	
%>
			
			<ul id="sMassmailGroupID"  class="selectBox">

<%
			if(massMailgroupList.size()==0){							
%>						
				<li data="" select="yes">대량메일그룹이  없습니다</li>
<%
			}else{
				
%>
			<li data="">--대량메일그룹--</li>
<%
				for(int i=0;i<massMailgroupList.size();i++){
					MassMailGroup massMailGroup = (MassMailGroup)massMailgroupList.get(i);
%>			
					
					<li data="<%=massMailGroup.getMassMailGroupID()%>" <% if (massMailGroup.getMassMailGroupID()== Integer.parseInt(massmailGroupID) || massMailGroup.getIsDefault().equals("Y")){%>select="yes"<%} %>><%=StringUtil.head(massMailGroup.getMassMailGroupName(),7)%></li>
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>