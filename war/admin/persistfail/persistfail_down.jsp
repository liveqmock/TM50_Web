<%@ page language="java" contentType="application/vnd.ms-excel; charset=EUC-KR"
    pageEncoding="EUC-KR"%><%@ page import="web.admin.persistfail.service.*"%><%@ page import="web.admin.persistfail.control.*"%><%@ page import="web.admin.persistfail.model.*"%><%@ page import="java.util.Map"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.List"%><%@ page import="web.common.util.*"%><% 
	
    PersistFailService service = PersistFailControlHelper.getUserService(application);
    //PersistFail temp_target=service.listPersistFailMail().viewTargetList(targetID);
	
    String sSearchType = ServletUtil.getParamString(request,"sSearchType");
	String sSearchText = ServletUtil.getParamString(request,"sSearchText");
		
	String dateStart = ServletUtil.getParamString(request,"eDateStart");
	String dateEnd = ServletUtil.getParamString(request,"eDateEnd");
	
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("searchType", sSearchType);
	searchMap.put("searchText", sSearchText);
	
	searchMap.put("dateStart", dateStart+" 00:00:00");
	searchMap.put("dateEnd", dateEnd+" 23:59:59");		
		
	int iTotalCnt = service.totalCountPersistFailMail(searchMap);
	PersistFailController control = null; 
	
	
	List<PersistFail> persistFailList = service.listPersistFailMail(1, iTotalCnt, searchMap);
		
	
	//엑셀파일 저장 정보
	response.setHeader("Content-Disposition", "attachment; filename=" + "persist_fail_list.csv");
	response.setHeader("Content-Description", "JSP Generated Data");
		
	out.print("[email]");
	out.print(",");
	out.print("[massmail_name]");
	out.print(",");
	out.print("[target_name]");
	out.print(",");
	out.print("[mail_group_name]");
	out.print(",");
	out.print("[smtp_code]");
	out.print(",");
	out.print("[smtp_msg]");
	out.print(",");
	out.print("[regist_date]");
	
	
	out.println();
	
	for(PersistFail persistFail:persistFailList)
	{
		out.print(persistFail.getEmail());
		out.print(",");
		if(persistFail.getMassmailTitle()!=null && !(persistFail.getMassmailTitle().equals("")))
			out.print(persistFail.getMassmailTitle());
		else
			out.print("");
		out.print(",");
		out.print(persistFail.getTargetName());
		out.print(",");
		out.print(persistFail.getMassmailGroupName());
		out.print(",");
		out.print(persistFail.getSmtpCode());
		out.print(",");
		out.print(persistFail.getSmtpMsg().replace(","," "));
		out.print(",");
		out.print(persistFail.getRegistDate());
		
		
		
		out.println();
		
	}
%>
