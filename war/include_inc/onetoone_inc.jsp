<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@page import="web.admin.onetoone.model.OneToOne"%>
<%@page import="web.admin.onetoone.service.OneToOneService"%>
<%@page import="web.admin.onetoone.control.OneToOneControlHelper"%>

  
<%

	
		String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();
		//모델을 이용하여 리스트를 가져온다.
		OneToOneService service = OneToOneControlHelper.getUserService(application);
		List<OneToOne> onetooneList = service.listOneToOne();
		
		String id = request.getParameter("id") == null ? "" : request.getParameter("id").trim();
		String flag = request.getParameter("flag") == null ? "" : request.getParameter("flag").trim();

	
%>

<script type="text/javascript">
	function insOnetoone(alias){	
		alert('<%=mustInput%>');	
		alert('<%=flag%>');
		// $('<%=id%>_form').eReceiverName.value = alias;
	}
</script>
			
			<ul id="selectedGroupID"  onchange="javascript:insOnetoone(this.value)" class="selectBox" <%=(mustInput.equals("Y")? "mustInput='Y' msg='일대일치환선택'":"")%>>

<%
			if(onetooneList.size()==0){							
%>						
				<li data="" select="yes">일대일치환데이타가 없습니다.<%=mustInput %></li>
<%
			}else{
				
%>
			<li data="">--일대일치환--</li>
<%
				for(int i=0;i<onetooneList.size();i++){
					OneToOne oneToOne =  (OneToOne)onetooneList.get(i);
%>								
					<li data="<%=oneToOne.getOnetooneAlias()%>"><%=oneToOne.getOnetooneName()%></li>
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>