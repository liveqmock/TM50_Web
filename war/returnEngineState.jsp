<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONValue"%>
<%@ page import="web.admin.manager.control.*"%>
<%@ page import="web.admin.manager.service.*"%>
<%@ page import="web.admin.manager.model.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="web.common.util.StringUtil"%>



<% request.setCharacterEncoding("utf-8"); %>
<%
	ManagerService service = ManagerControlHelper.getUserService(application);

	String ids = request.getParameter("engine_id");
	Object obj = JSONValue.parse(ids);
	JSONArray id = (JSONArray)obj;
	JSONObject t = (JSONObject)id.get(0);
	JSONArray arr = (JSONArray)t.get("engine_id"); 
	JSONArray arr_ip = (JSONArray)t.get("server_ip"); 
	
	JSONObject tState = new JSONObject();		
	JSONArray arrState = new JSONArray();	
	JSONArray arrErrorCount = new JSONArray();
	JSONArray arrUpdateDate = new JSONArray();
	
	
	for(int i=0;i<arr.size();i++)
	{
		Manager tar = service.getEngineState(arr.get(i).toString(), arr_ip.get(i).toString()); 
		arrState.add(String.valueOf(tar.getEngineStatus()));	
		arrErrorCount.add(String.valueOf(tar.getErrorCount()));
		arrUpdateDate.add(String.valueOf(tar.getUpdateDate()));
		
	}
	t.put("state",arrState);
	t.put("errorCount",arrErrorCount);
	t.put("updateDate",arrUpdateDate);
	
	
%>

<%=id.toString() %>
