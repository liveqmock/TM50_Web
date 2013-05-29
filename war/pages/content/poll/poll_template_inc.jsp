<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.*"%>
<%@page import="web.content.poll.model.PollTemplate"%>
<%@page import="web.content.poll.service.PollService"%>
<%@page import="web.content.poll.control.PollControlHelper"%>
<%@page import="web.common.util.*"%>
  
<%
			
		String id = request.getParameter("id");
		String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();		
		String pollTemplateIDs = request.getParameter("pollTemplateID") == null ? "0" : request.getParameter("pollTemplateID").trim();	
		int pollTemplateID = Integer.parseInt(pollTemplateIDs);	
		//모델을 이용하여 리스트를 가져온다.
		PollService service = PollControlHelper.getUserService(application);
		List<PollTemplate> pollTemplateList = null;
		Map<String, String> searchMap =  new HashMap<String, String>();
		searchMap.put("userAuth", LoginInfo.getUserAuth(request));
		searchMap.put("userID", LoginInfo.getUserID(request));
		searchMap.put("groupID", LoginInfo.getGroupID(request));
		
		
		pollTemplateList = service.selectPollTemplateByUsers(searchMap);
		
		
		
			
%>					

			<select name="ePollTemplateID" id="ePollTemplateID" <%if(pollTemplateID!=0){ %> disabled <%} %>  mustInput="Y" onchange="javascript:selectPollTemplate(this.value);">
<%
			if(pollTemplateList.size()==0){							
%>						
				<option value="" selected>등록된 데이타가  없습니다</option>
<%
			}else{
				
%>
				<option value="" selected>--설문템플릿--</option>
<%
				
				for(int i=0;i<pollTemplateList.size();i++){
					PollTemplate pollTemplate =  (PollTemplate)pollTemplateList.get(i);
%>			
					
					<option value="<%=pollTemplate.getPollTemplateID() %>" <% if (pollTemplate.getPollTemplateID()==pollTemplateID){%> selected <%} %>><%= pollTemplate.getPollTemplateTitle() %></option>
					
<%
				}//end for 
				
			}// end if 
%>
			</select>			
		

			
			
<script type="text/javascript">
/***********************************************/
/* 설문템플릿 선택 
/***********************************************/
function selectPollTemplate(seq){
		
	
	

	
}
</script>