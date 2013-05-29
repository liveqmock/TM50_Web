<%@ page language="java" contentType="application/vnd.ms-excel; charset=EUC-KR"
    pageEncoding="EUC-KR"%><%@ page import="web.target.targetlist.service.TargetListService"%><%@ page import="web.target.targetlist.control.*"%><%@ page import="web.target.targetlist.model.*"%><%@ page import="java.util.Map"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.List"%><%@ page import="web.common.util.*"%><% int targetID = new Integer(request.getParameter("targetID"));
	
	String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	TargetListService service = TargetListControlHelper.getUserService(application);
    TargetList temp_target=service.viewTargetList(targetID);
	String query = temp_target.getQueryText();  
	String dbId = temp_target.getDbID();
	int iTotalCnt = 0;
	TargetingPreviewController control = null; 
	
	if(dbId.equals("10"))
	{
		String sql = query.substring(query.toUpperCase().indexOf(" FROM "));				
		sql = "select count(*) " + sql;						 
		iTotalCnt = service.getTargetPreviewListTotalCount(sql);  
	}
	else
	{ 
		control = TargetingPreviewController.getInstance(service);
		iTotalCnt =control.getTargetPreviewListTotalCount(query, dbId, "", "");
	}
	
	List<OnetooneTarget> onetoone = service.viewOnetooneTarget(targetID);
	String[] onetoFieldName = new String[onetoone.size()];
	String[] onetoFieldDesc = new String[onetoone.size()];
	
	for(int i = 0; i < onetoone.size(); i++){
		
		OnetooneTarget ot = onetoone.get(i);
		onetoFieldName[i] = ot.getFieldName();	
		onetoFieldDesc[i] = ot.getFieldDesc();	
		
	}
		
	
	//엑셀파일 저장 정보
	response.setHeader("Content-Disposition", "attachment; filename=" + "target_list" + targetID + ".csv");
	response.setHeader("Content-Description", "JSP Generated Data");
	List<String[]> targetPreviewList = null;
	if(dbId.equals("10"))
	{
		
		String sql = QueryUtil.getPagingQueryByDB(db_type, query, onetoFieldName[0], "0",  String.valueOf(iTotalCnt));
		sql = query.replace("select ", "select fileImportID, ");
		sql = query.replace("SELECT ", "SELECT fileImportID, ");
		targetPreviewList = service.getTargetPreviewList(sql, onetoFieldName); 
	}
	else
	{
		targetPreviewList = control.getTargetPreviewList(targetID, query, dbId, "", "", 1, iTotalCnt, iTotalCnt  );
	}	
	
	
	for(int i=0; i < onetoFieldDesc.length; i++)
	{
		out.print(onetoFieldDesc[i]);
		if(i!=onetoFieldDesc.length-1)
			out.print(",");
		
	}
	out.println();
	// fileImportID 처리 때문에 파일업로드, 직접입력일 경우에는 첫번째 값(fileImportID)을 제외하고 출력한다.
	int start =0;
	if(dbId.equals("10")){start=1;}
	for(String[] targetPreview:targetPreviewList)
	{
		for(int i=start ;i < targetPreview.length; i++)
		{
			out.print(targetPreview[i]);
			if(i!=targetPreview.length-1)
				out.print(",");		
		}
		out.println();
			
	}
%>
