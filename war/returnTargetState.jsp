<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONValue"%>
<%@ page import="web.target.targetlist.control.*"%>
<%@ page import="web.target.targetlist.service.*"%>
<%@ page import="web.target.targetlist.model.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="web.common.util.StringUtil"%>



<% request.setCharacterEncoding("utf-8"); %>
<%
	TargetListService service = TargetListControlHelper.getUserService(application);

	String ids = request.getParameter("target_id");
	Object obj = JSONValue.parse(ids);
	JSONArray id = (JSONArray)obj;
	JSONObject t = (JSONObject)id.get(0);
	JSONArray arr = (JSONArray)t.get("target_id"); 
	
	JSONObject tState = new JSONObject();		
	JSONArray arrState = new JSONArray();	
	JSONArray arrCount = new JSONArray();
	
	for(int i=0;i<arr.size();i++)
	{
		TargetList tar = service.getTargetState(arr.get(i).toString());
		arrState.add(String.valueOf(tar.getState()));		
		arrCount.add(StringUtil.formatPrice(tar.getTargetCount())); 
	}
	t.put("state",arrState);
	t.put("count",arrCount);
	
%>

<%=id.toString() %>
