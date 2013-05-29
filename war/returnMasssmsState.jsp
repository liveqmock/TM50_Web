<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.JSONValue"%>
<%@ page import="web.masssms.write.control.*"%>
<%@ page import="web.masssms.write.service.*"%>
<%@ page import="web.masssms.write.model.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="web.common.util.StringUtil"%>



<% request.setCharacterEncoding("utf-8"); %>
<%
	MassSMSService service = MassSMSControlHelper.getUserService(application);

	String ids = request.getParameter("masssms_id");
	Object obj = JSONValue.parse(ids);
	JSONArray id = (JSONArray)obj;
	JSONObject t = (JSONObject)id.get(0);
	JSONArray arr_masssms_id = (JSONArray)t.get("masssms_id"); 
	JSONArray arr_schedule_id = (JSONArray)t.get("schedule_id"); 
	
	JSONObject tState = new JSONObject();		
	JSONArray arrState = new JSONArray();	
	JSONArray arrCount = new JSONArray();
	
	for(int i=0;i<arr_masssms_id.size();i++)
	{
		MassSMSList tar = service.getMasssmsState(arr_masssms_id.get(i).toString(), arr_schedule_id.get(i).toString());
		arrState.add(String.valueOf(tar.getState()));		
		arrCount.add(StringUtil.formatPrice(tar.getSendCount())); 
	}
	t.put("state",arrState);
	t.put("count",arrCount);
	
%>

<%=id.toString() %>
