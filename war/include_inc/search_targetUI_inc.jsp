<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@ page import="web.common.util.StringUtil"%>
<%@ page import="web.target.targetui.control.TargetUIController"%>
<%@ page import="web.target.targetui.control.TargetUIControlHelper"%>
<%@ page import="web.target.targetui.service.TargetUIService"%>
<%@ page import="web.target.targetui.model.TargetUIList"%>

<%
		
		
		//모델을 이용하여 리스트를 가져온다.
		TargetUIService service = TargetUIControlHelper.getUserService(application);
		List<TargetUIList> targetLists = service.listTargetUI();
		String targetUIManagerIDs = request.getParameter("targetUIManagerID") == null ? "0" : request.getParameter("targetUIManagerID").trim();
		if(targetUIManagerIDs.equals("")) targetUIManagerIDs="0";
%>
			
			<ul id="sTargetGroupID"  class="selectBox" onchange="javascript:$('targetui').chgView(this.value)">

<%
			if(targetLists.size()==0){							
%>						
				<li data="" select="yes">사용 가능한 페이지가 없습니다</li>
<%
			}else{%>
			<li data="">--회원검색UI--</li>
			<%
				for(int i=0;i<targetLists.size();i++){
					TargetUIList targetList = (TargetUIList)targetLists.get(i);
%>			
					
					<li data="<%=targetList.getTargetUIManagerID()%>" <% if (targetList.getTargetUIManagerID()== Integer.parseInt(targetUIManagerIDs) || targetList.getDefaultYN().equals("Y")){%>select="yes"<%} %>><%=StringUtil.head(targetList.getTargetUIManagerName(),7)%></li>
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>