<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@page import="web.admin.massmailgroup.model.MassMailGroup"%>
<%@page import="web.admin.massmailgroup.service.MassMailGroupService"%>
<%@page import="web.admin.massmailgroup.control.MassMailGroupControlHelper"%>
<%
		String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();
		String massmailGroupID = request.getParameter("massmailGroupID") == null ? "0" : request.getParameter("massmailGroupID").trim();
		if(massmailGroupID.equals("")) massmailGroupID="0";
		
		
		//모델을 이용하여 리스트를 가져온다.
		MassMailGroupService service = MassMailGroupControlHelper.getUserService(application);
		List<MassMailGroup> massMailgroupList = service.listMassMailGroup();
	
%>
			
			<ul id="eMassmailGroupID"  class="selectBox" <%=(mustInput.equals("Y")? "mustInput='Y' msg='그룹선택'":"")%>>

<%
			if(massMailgroupList.size()==0){							
%>						
				<li data="" select="yes">대량메일그룹이  없습니다<%=mustInput %></li>
<%
			}else{
				
%>
			<li data="">대량메일그룹선택</li>
<%
				for(int i=0;i<massMailgroupList.size();i++){
					MassMailGroup massMailGroup = (MassMailGroup)massMailgroupList.get(i);
%>			
					
					<li data="<%=massMailGroup.getMassMailGroupID()%>" <% if (massMailGroup.getMassMailGroupID()== Integer.parseInt(massmailGroupID) || massMailGroup.getIsDefault().equals("Y")){%>select="yes"<%} %>><%=massMailGroup.getMassMailGroupName()%></li>
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>