<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONValue"%>
<%@ page import="web.massmail.write.control.*"%>
<%@ page import="web.massmail.write.service.*"%>
<%@ page import="web.massmail.write.model.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="web.common.util.StringUtil"%>



<% request.setCharacterEncoding("utf-8"); %>
<%
	MassMailService service = MassMailControlHelper.getUserService(application);

	String ids = request.getParameter("massmail_id");
	Object obj = JSONValue.parse(ids);
	JSONArray id = (JSONArray)obj;
	JSONObject t = (JSONObject)id.get(0);
	JSONArray arr_massmail_id = (JSONArray)t.get("massmail_id"); 
	JSONArray arr_schedule_id = (JSONArray)t.get("schedule_id"); 
	
	JSONObject tState = new JSONObject();		
	JSONArray arrState = new JSONArray();	
	JSONArray arrCount = new JSONArray();
	JSONArray arrSuccessCount = new JSONArray();
	
	for(int i=0;i<arr_massmail_id.size();i++)
	{
		MassMailList tar = service.getMassmailState(arr_massmail_id.get(i).toString(), arr_schedule_id.get(i).toString());
		arrState.add(String.valueOf(tar.getState()));		
		arrCount.add(StringUtil.formatPrice(tar.getSendCount())); 
		arrSuccessCount.add(StringUtil.formatPrice(String.valueOf(tar.getSuccessCount()))); 
	}
	t.put("state",arrState);
	t.put("count",arrCount);
	t.put("successCount",arrSuccessCount);
	
%>

<%=id.toString() %>
