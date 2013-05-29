<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import ="jxl.format.Colour"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.ScriptStyle"%>
<%@ page import="web.massmail.statistic.service.MassMailStatService"%>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%>
<%@ page import="web.massmail.statistic.model.*"%>
<%@ page import="web.common.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jxl.write.Number"%>
<%
	
	String year = request.getParameter("sYear");
	String month = request.getParameter("sMonth");
	String userID =  request.getParameter("sUserID");
	String groupID =  request.getParameter("sGroupID");
	String[] userInfo = new String[3];
	userInfo[0] =  LoginInfo.getUserAuth(request); 
	userInfo[1] =  LoginInfo.getUserID(request);
	userInfo[2] =   LoginInfo.getGroupID(request);
	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
    
    Map<String, String> searchMap = new HashMap<String, String>(); 
	searchMap.put("year", year);
	searchMap.put("month", month);
	searchMap.put("userID", userID);
	searchMap.put("groupID", groupID);
	
	List<MassMailStatisticUsers> massMailStatisticUsersList = service.massmailStatisticUsersList(userInfo, searchMap);
    
	out.clear();
	File file = (File)application.getAttribute("javax.servlet.context.tempdir");
	String filePath = file.getAbsolutePath();
	String fileName = "Report_Users.xls";
	
	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	Number number = null;
	
	    
    try {
    	 response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    	 workbook = Workbook.createWorkbook(response.getOutputStream());
    	 sheet = workbook.createSheet("계정별 발송 현황", 0);
    	 
    	//Font 설정
    	WritableFont titleFont = new WritableFont(WritableFont.createFont("굴림체"), 16, WritableFont.BOLD, false);
    	WritableFont gulimFont = new WritableFont(WritableFont.createFont("굴림체"), 12, WritableFont.NO_BOLD, false);
    	WritableFont gulimBOLDFont = new WritableFont(WritableFont.createFont("굴림체"), 10, WritableFont.BOLD, false);
 		
 		//Format 설정
 		
 		WritableCellFormat titleFormat = new WritableCellFormat (titleFont);
 		titleFormat.setAlignment(Alignment.CENTRE);
 		       
 		WritableCellFormat defaultFormat = new WritableCellFormat (gulimBOLDFont);
        defaultFormat.setBackground(Colour.GRAY_25);
        defaultFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        defaultFormat.setAlignment(Alignment.CENTRE);
        defaultFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

        WritableCellFormat dataFormat = new WritableCellFormat (gulimFont);
        dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        dataFormat.setAlignment(Alignment.CENTRE);
        dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        sheet.setColumnView(0, 13);
        sheet.setColumnView(1, 12);
        sheet.setColumnView(2, 12);
        sheet.setColumnView(3, 12);
        sheet.setColumnView(4, 12);
        sheet.setColumnView(5, 12);
        sheet.setColumnView(6, 14);
     
        sheet.setRowView(0, 500);
        sheet.mergeCells(0, 0, 6, 0);
 		label = new Label(0, 0, "계정별 발송 현황 ("+year+"년 "+month+"월)", titleFormat);
 		sheet.addCell(label);
 		
 		label = new Label(0,2,"계정", defaultFormat);
        sheet.addCell(label);
        label = new Label(1,2,"전체통수", defaultFormat);
        sheet.addCell(label);
        label = new Label(2,2,"성공통수", defaultFormat);
        sheet.addCell(label);
        label = new Label(3,2,"실패통수", defaultFormat);
        sheet.addCell(label);
        label = new Label(4,2,"오픈통수", defaultFormat);
        sheet.addCell(label);
        label = new Label(5,2,"클릭통수", defaultFormat);
        sheet.addCell(label);
        label = new Label(6,2,"수신거부통수", defaultFormat);
        sheet.addCell(label);     
        
        int i=0;
        int sendTotal=0;
        int successTotal=0;
        int failTotal=0;
        int openTotal=0;
        int clickTotal=0;
        int rejectcallTotal=0;
        
        for(MassMailStatisticUsers massMailStatisticUsers : massMailStatisticUsersList){
        	
        	label = new Label(0,3+i,massMailStatisticUsers.getUserName(), dataFormat);
            sheet.addCell(label);
            number = new Number(1,3+i,massMailStatisticUsers.getSendTotal(), dataFormat);
            sheet.addCell(number);
            number = new Number(2,3+i,massMailStatisticUsers.getSuccessTotal(), dataFormat);
            sheet.addCell(number);
            number = new Number(3,3+i,massMailStatisticUsers.getFailTotal(), dataFormat);
            sheet.addCell(number);
            number = new Number(4,3+i,massMailStatisticUsers.getOpenTotal(), dataFormat);
            sheet.addCell(number);
            number = new Number(5,3+i,massMailStatisticUsers.getClickTotal(), dataFormat);
            sheet.addCell(number);
            number = new Number(6,3+i,massMailStatisticUsers.getRejectcallTotal(), dataFormat);
            sheet.addCell(number); 
            i++;
            sendTotal+=massMailStatisticUsers.getSendTotal();
            successTotal+=massMailStatisticUsers.getSuccessTotal();
            failTotal+=massMailStatisticUsers.getFailTotal();
            openTotal+=massMailStatisticUsers.getOpenTotal();
            clickTotal+=massMailStatisticUsers.getClickTotal();
            rejectcallTotal+=massMailStatisticUsers.getRejectcallTotal();
        }
        label = new Label(0,3+i,"합계", defaultFormat);
        sheet.addCell(label);
        number = new Number(1,3+i,sendTotal, dataFormat);
        sheet.addCell(number);
        number = new Number(2,3+i,successTotal, dataFormat);
        sheet.addCell(number);
        number = new Number(3,3+i,failTotal, dataFormat);
        sheet.addCell(number);
        number = new Number(4,3+i,openTotal, dataFormat);
        sheet.addCell(number);
        number = new Number(5,3+i,clickTotal, dataFormat);
        sheet.addCell(number);
        number = new Number(6,3+i,rejectcallTotal, dataFormat);
        sheet.addCell(number); 
        workbook.write();
        workbook.close();
        
	} catch(Exception e) {
		System.out.println(e.toString());
	} finally {
	
	    if(workbook != null) { try { workbook.close(); } catch(Exception e) {} }
	
	}
	
%>