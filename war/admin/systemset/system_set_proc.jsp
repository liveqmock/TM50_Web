<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%
	String id = request.getParameter("id");

	String isAdmin = LoginInfo.getIsAdmin(request);
	
	if(isAdmin.equals("Y")){ // 관리자 계정이 아닐 경우 URL 접근 시 접근불가 페이지 출력
%>

<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<!-- 메시지 가있으면 메시지를 뿌려준다 -->
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	<!-- ################# List 시작 ####################### -->
	<%	
		int cnt = 0; //전체 변수의 단위 지정  
	%>

	<c:forEach items="${systemSetList}" var="systemSet">
		<tr>
			<td class="ctbl ttd1" width="150px">
				<c:out value="${systemSet.name}"/>				
			</td>
			<td class="ctbl td">
				<input type="hidden" id="configID<%=cnt%>" name="configID<%=cnt%>" value="<c:out value="${systemSet.configID}"/>" />
				<input type="text" id="configValue<%=cnt%>" name="configValue<%=cnt%>" value="<c:out value="${systemSet.configValue}"/>" size="55" mustInput="Y"  msg="<c:out value="${systemSet.name}"/> 입력" />
			</td>
			<td class="ctbl td">				
				<c:out value="${systemSet.configDesc}"/>
			</td>			
		</tr>
		<tr>
			<td colspan="10" class="ctbl line"></td>
		</tr>
		
		<% cnt++;  %>	
	</c:forEach>
	<tr>
			<td class="ctbl td"><input type="hidden" id="itemCount" name="itemCount" value="<%=cnt%>" /></td>
		</tr>
	<!--#################################################### -->
    

                     
                     
                     
<%}else{%>
<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td align="center" valign="middle">
			<center><img src="../../images/error.jpg" /></center>
		</td>
	</tr>
</table>
<%}%>