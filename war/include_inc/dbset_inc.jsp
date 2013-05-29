<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.*"%>
<%@page import="web.admin.dbjdbcset.model.DbJdbcSet"%>
<%@page import="web.admin.dbjdbcset.service.DbJdbcSetService"%>
<%@page import="web.admin.dbjdbcset.control.DbJdbcSetControlHelper"%>

<%
	String id = request.getParameter("id");
	String mustInput = request.getParameter("mustInput") == null ? "" : request.getParameter("mustInput").trim();
	String dbID = request.getParameter("dbID") == null ? "" : request.getParameter("dbID").trim();
	//모델을 이용하여 리스트를 가져온다.
	DbJdbcSetService service = DbJdbcSetControlHelper.getUserService(application);
	List<DbJdbcSet> dbList = service.listDBList(null);

%>

			<ul id="edbID" class="selectBox" title="DB선택" <%=(mustInput.equals("Y")? "mustInput='Y' msg='DB선택'":"")%>>
			
<%
			if(dbList.size()==0){							
%>						
				<li data="" select="yes">등록된  DB가 없습니다.</li>
<%
			}else{
				if(dbID.equals("")){
%>
					<li data="">--DB선택--</li>
<%
				}
				for(int i=0;i<dbList.size();i++){
						DbJdbcSet dbJdbcSet =  (DbJdbcSet)dbList.get(i);
%>
						<li data="<%=dbJdbcSet.getDbID()%>" <% if (dbJdbcSet.getDbID().equals(dbID) ){%>select="yes"<%} %>><%=dbJdbcSet.getDbName()%></li>

<%						
				}//end for
			}// end if 
%>
			</ul>