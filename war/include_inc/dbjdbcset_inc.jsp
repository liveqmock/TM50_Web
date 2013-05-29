<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>
<%@page import="web.admin.dbjdbcset.model.DbJdbcSet"%>
<%@page import="web.admin.dbjdbcset.service.DbJdbcSetService"%>
<%@page import="web.admin.dbjdbcset.control.DbJdbcSetControlHelper"%>

<%
	String id = request.getParameter("id");
	String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();
	String driverID = request.getParameter("driverID") == null ? "" : request.getParameter("driverID").trim();
	//모델을 이용하여 리스트를 가져온다.
	DbJdbcSetService service = DbJdbcSetControlHelper.getUserService(application);
	List<DbJdbcSet> jdbcList = service.listJdbcSet();

%>

<script type="text/javascript">

$('<%=id%>_form').settingParent = function (){
	
	var index = $('<%=id%>_form').selectedDriverID.selectedIndex;
	
	if(index<0){
		$('<%=id%>_form').eDbURL.value="";
		$('<%=id%>_form').eDriverClass.value="";
		return;
	}
	

	$('<%=id%>_form').eDbURL.value = $('<%=id%>_form').url[index].value;
	$('<%=id%>_form').eDriverClass.value = $('<%=id%>_form').classes[index].value;
	
}

</script>
<input type="hidden" id="url" name="url" value="">
<input type="hidden" id="classes" name="classes" value="">
			<ul id="selectedDriverID"  onChange="javascript:$('<%=id%>_form').settingParent()" class="selectBox"  <%=(mustInput.equals("Y")? "mustInput='Y' msg='JDBC 드라이버 선택'":"")%>>
			
<%
			if(jdbcList.size()==0){							
%>						
				<li data="" select="yes">등록된  JDBC 드라이버가 없습니다.</li>
<%
			}else{
				
%>
			<li data="">JDBC 드라이버를  선택하세요</li>
<%
				for(int i=0;i<jdbcList.size();i++){
					DbJdbcSet dbJdbcSet =  (DbJdbcSet)jdbcList.get(i);
%>			
					
					<li data="<%=dbJdbcSet.getJdbcDriverID()%>" <% if (dbJdbcSet.getJdbcDriverID().equals(driverID)){%>select="yes"<%} %>><%=dbJdbcSet.getJdbcDriverName() %></li>
			
					<input type="hidden" id="url" name="url" value="<%=dbJdbcSet.getJdbcSampleURL() %>">
					<input type="hidden" id="classes" name="classes" value="<%=dbJdbcSet.getJdbcDriverClass() %>">
					
					
<%
				}//end for 
				
			}// end if 
%>
			</ul>