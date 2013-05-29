<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONValue"%>
<%@ page import="web.intermail.control.*"%>
<%@ page import="web.intermail.service.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="web.common.util.StringUtil"%>



<% request.setCharacterEncoding("utf-8"); %>
<%
	InterMailService service = InterMailControlHelper.getUserService(application);

	String ids = request.getParameter("intermail_id");
	Object obj = JSONValue.parse(ids);
	JSONArray id = (JSONArray)obj;
	JSONObject t = (JSONObject)id.get(0);
	JSONArray arr_intermail_id = (JSONArray)t.get("intermail_id"); 
	JSONArray arr_schedule_id = (JSONArray)t.get("schedule_id"); 
	
	JSONObject tState = new JSONObject();		
	JSONArray arrState = new JSONArray();	
	
	
	for(int i=0;i<arr_intermail_id.size();i++)
	{
		Map<String,Object> tar = service.getInterMailState(Integer.parseInt(arr_intermail_id.get(i).toString()), Integer.parseInt(arr_schedule_id.get(i).toString()));
		arrState.add(String.valueOf(tar.get("state")));
	}
	t.put("state",arrState);	
	
%>

<%=id.toString() %>
