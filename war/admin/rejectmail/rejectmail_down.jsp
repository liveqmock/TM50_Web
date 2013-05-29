<%@ page language="java" contentType="application/vnd.ms-excel; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="java.io.*" %><%@ page import="jxl.write.*" %><%@ page import="jxl.*" %><%@ page import="web.admin.rejectmail.service.RejectMailService"%><%@ page import="web.admin.rejectmail.control.RejectMailControlHelper"%><%@ page import="web.admin.rejectmail.model.RejectMail"%><%@ page import="java.util.Map"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.List"%><%@ page import="web.common.util.*"%><%
 	String loginAuth = LoginInfo.getUserAuth(request);
	String groupID = LoginInfo.getGroupID(request);
	
	Map<String, String> searchMap = new HashMap<String, String>(); 
	searchMap.put("searchType", request.getParameter("searchType"));
	searchMap.put("searchText", request.getParameter("searchText"));
	searchMap.put("loginAuth", loginAuth);
	searchMap.put("groupID", groupID);
	
	
	//엑셀파일 저장 정보
	response.setHeader("Content-Disposition", "attachment; filename=rejectmail_list.csv");
	response.setHeader("Content-Description", "JSP Generated Data");
	
	RejectMailService service = RejectMailControlHelper.getUserService(application);
	List<RejectMail> rejectMailList = service.listRejectMail(0,0,searchMap);

	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	
	try{
		workbook = Workbook.createWorkbook(response.getOutputStream());
		sheet = workbook.createSheet("수신거부리스트", 0);
		sheet.setRowView(0, 500);
		sheet.setColumnView(0, 7);
		sheet.addCell(new Label(0, 0, "이메일"));
		//sheet.addCell(new Label(1, 0, "대량메일명"));
		//sheet.addCell(new Label(2, 0, "대량메일그룹명"));
		//sheet.addCell(new Label(3, 0, "작성자"));
		//sheet.addCell(new Label(4, 0, "등록구분"));
		//sheet.addCell(new Label(5, 0, "등록일"));
		
		for(int i=1; i<=rejectMailList.size();i++){
			RejectMail rejectMail = rejectMailList.get(i-1);
			//for(int t=0;t<=5;t++){
				//sheet.addCell(new Label(t, i, rejectMail.getEmail()));
				sheet.addCell(new Label(0, i, rejectMail.getEmail()));
			//}
			
		}
		
		workbook.write();
		
	} catch(Exception e) {
		System.out.println(e.toString());
	} finally {
	
	    if(workbook != null) { try { workbook.close(); } catch(Exception e) {} }
	
	}
	
%>