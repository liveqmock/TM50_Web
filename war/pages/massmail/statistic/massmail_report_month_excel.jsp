<%@ page contentType="application/vnd.ms-excel;charset=euc-kr"%>
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
<%@ page import="web.massmail.write.model.*"%>
<%@ page import="web.common.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jxl.write.Number"%>
<%
	
	String year = request.getParameter("sYear");
	String month = request.getParameter("sMonth");
	String dateFrom = request.getParameter("sDateFrom");
	String dateTo = request.getParameter("sDateTo");
	String userID =  request.getParameter("sUserID");
	String groupID =  request.getParameter("sGroupID");
	String listTitle = "�� �߼� ���� ����Ʈ";
	String state = request.getParameter("sState");
	String[] userInfo = new String[3];
	userInfo[0] =  LoginInfo.getUserAuth(request); 
	userInfo[1] =  LoginInfo.getUserID(request);
	userInfo[2] =   LoginInfo.getGroupID(request);
	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
    MassMailReportMonth massMailReportMonthTotalInfo = service.massMailReportMonthTotalInfo(year, month, dateFrom, dateTo, userID, groupID, userInfo);
    MassMailReportMonth massMailReportMonthSendInfo = service.massMailReportMonthSendInfo(year, month, dateFrom, dateTo, userID, groupID, userInfo);
    
    Map<String, String> searchMap = new HashMap<String, String>(); 
	searchMap.put("year", year);
	searchMap.put("month", month);
	searchMap.put("dateFrom", dateFrom);
	searchMap.put("dateTo", dateTo);
	searchMap.put("state", state);
	searchMap.put("userID", userID);
	searchMap.put("groupID", groupID);
	
	List<MassMailList> massmaillist = service.massMailReportMonthList(userInfo, 1, 500, searchMap);
    
	out.clear();
	File file = (File)application.getAttribute("javax.servlet.context.tempdir");
	String filePath = file.getAbsolutePath();
	String fileName = "Report_Month.xls";
	
	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	Number number = null;
	
	    
    try {
    	 response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    	 workbook = Workbook.createWorkbook(response.getOutputStream());
    	 sheet = workbook.createSheet("�������м�", 0);
    	 
    	//Font ����
    	WritableFont titleFont = new WritableFont(WritableFont.createFont("����ü"), 16, WritableFont.BOLD, false);
    	WritableFont gulimFont = new WritableFont(WritableFont.createFont("����ü"), 12, WritableFont.NO_BOLD, false);
    	WritableFont gulimBOLDFont = new WritableFont(WritableFont.createFont("����ü"), 10, WritableFont.BOLD, false);
 		WritableFont gulimBLUEFont = new WritableFont(WritableFont.createFont("����ü"), 10, WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.DARK_BLUE,ScriptStyle.NORMAL_SCRIPT);
 		
 		//Format ����
 		
 		WritableCellFormat titleFormat = new WritableCellFormat (titleFont);
 		titleFormat.setAlignment(Alignment.CENTRE);
 		
 		WritableCellFormat subTitleFormat = new WritableCellFormat (gulimBLUEFont);
 		subTitleFormat.setAlignment(Alignment.LEFT);
 		subTitleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        
 		WritableCellFormat defaultFormat = new WritableCellFormat (gulimBOLDFont);
        defaultFormat.setBackground(Colour.GRAY_25);
        defaultFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        defaultFormat.setAlignment(Alignment.CENTRE);
        defaultFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

        WritableCellFormat dataFormat = new WritableCellFormat (gulimFont);
        dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        dataFormat.setAlignment(Alignment.CENTRE);
        dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        sheet.setColumnView(0, 7);
        sheet.setColumnView(1, 7);
        sheet.setColumnView(2, 7);
        sheet.setColumnView(3, 7);
        sheet.setColumnView(4, 7);
        sheet.setColumnView(5, 7);
        sheet.setColumnView(6, 7);
        sheet.setColumnView(7, 7);
        sheet.setColumnView(8, 7);
        sheet.setColumnView(9, 7);
        sheet.setColumnView(10, 7);
        sheet.setColumnView(11, 7);

     
        sheet.setRowView(0, 500);
        sheet.mergeCells(0, 0, 11, 0);
 		label = new Label(0, 0, "�������м� ("+year+"�� "+month+"��)", titleFormat);
 		sheet.addCell(label);
 		
 		 
        sheet.setRowView(2, 300);
        sheet.mergeCells(0, 2, 11, 2);
 		label = new Label(0, 2, "[�Ѱ���Ȳ]", subTitleFormat);
 		sheet.addCell(label);
 		
 		sheet.mergeCells(0, 3, 2, 3);
 		sheet.mergeCells(3, 3, 5, 3);
 		sheet.mergeCells(6, 3, 8, 3);
 		sheet.mergeCells(9, 3, 11, 3);

        label = new Label(0,3,"�� �뷮���� ��", defaultFormat);
        sheet.addCell(label);
        number = new Number(3,3,massMailReportMonthTotalInfo.getTotalCount(), dataFormat);
        sheet.addCell(number);
        label = new Label(6,3,"�ӽ� ����", defaultFormat);
        sheet.addCell(label);
        number = new Number(9,3,massMailReportMonthTotalInfo.getWriteCount(), dataFormat);
        sheet.addCell(number);
        
        sheet.mergeCells(0, 4, 2, 4);
 		sheet.mergeCells(3, 4, 5, 4);
 		sheet.mergeCells(6, 4, 8, 4);
 		sheet.mergeCells(9, 4, 11, 4);
 		
        
        label = new Label(0,4,"���� ���", defaultFormat);
        sheet.addCell(label);
        number = new Number(3,4,massMailReportMonthTotalInfo.getAppReadyCount(), dataFormat);
        sheet.addCell(number);
        label = new Label(6,4,"�߼� ���", defaultFormat);
        sheet.addCell(label);
        number = new Number(9,4,massMailReportMonthTotalInfo.getReadyCount(), dataFormat);
        sheet.addCell(number);
        
        sheet.mergeCells(0, 5, 2, 5);
 		sheet.mergeCells(3, 5, 5, 5);
 		sheet.mergeCells(6, 5, 8, 5);
 		sheet.mergeCells(9, 5, 11, 5);
 		
        label = new Label(0,5,"�߼� ��", defaultFormat);
        sheet.addCell(label);
        number = new Number(3,5,massMailReportMonthTotalInfo.getSendingCount(), dataFormat);
        sheet.addCell(number);
        label = new Label(6,5,"�߼� �Ϸ�", defaultFormat);
        sheet.addCell(label);
        number = new Number(9,5,massMailReportMonthTotalInfo.getSendFinishCount(), dataFormat);
        sheet.addCell(number);
        
        sheet.mergeCells(0, 6, 2, 6);
 		sheet.mergeCells(3, 6, 5, 6);
 		sheet.mergeCells(6, 6, 8, 6);
 		sheet.mergeCells(9, 6, 11, 6);
 		
        label = new Label(0,6,"�߼� ����", defaultFormat);
        sheet.addCell(label);
        number = new Number(3,6,massMailReportMonthTotalInfo.getErrCount(), dataFormat);
        sheet.addCell(number);
        label = new Label(6,6,"�߼� ����", defaultFormat);
        sheet.addCell(label);
        number = new Number(9,6,massMailReportMonthTotalInfo.getSendingStopCount(), dataFormat);
        sheet.addCell(number);
        
        sheet.setRowView(8, 300);
        sheet.mergeCells(0, 8, 11, 8);
 		label = new Label(0, 8, "[E-Mail �߼� ��Ȳ]", subTitleFormat);
 		sheet.addCell(label);
        
 		sheet.mergeCells(0, 9, 2, 9);
 		sheet.mergeCells(3, 9, 5, 9);
 		sheet.mergeCells(6, 9, 8, 9);
 		sheet.mergeCells(9, 9, 11, 9);
 		
 		label = new Label(0,9,"�� �߼� ���", defaultFormat);
        sheet.addCell(label);
        number = new Number(3,9,massMailReportMonthSendInfo.getSendTotal(), dataFormat);
        sheet.addCell(number);
        label = new Label(6,9,"���� ���", defaultFormat);
        sheet.addCell(label);
        number = new Number(9,9,massMailReportMonthSendInfo.getSuccessTotal(), dataFormat);
        sheet.addCell(number);
        
        sheet.mergeCells(0, 10, 2, 10);
 		sheet.mergeCells(3, 10, 5, 10);
 		sheet.mergeCells(6, 10, 8, 10);
 		sheet.mergeCells(9, 10, 11, 10);
 		
 		label = new Label(0,10,"���� ���", defaultFormat);
        sheet.addCell(label);
        number = new Number(3,10,massMailReportMonthSendInfo.getFailTotal(), dataFormat);
        sheet.addCell(number);
        label = new Label(6,10,"���� ���", defaultFormat);
        sheet.addCell(label);
        number = new Number(9,10,massMailReportMonthSendInfo.getOpenTotal(), dataFormat);
        sheet.addCell(number);
        
        sheet.mergeCells(0, 11, 2, 11);
 		sheet.mergeCells(3, 11, 5, 11);
 		sheet.mergeCells(6, 11, 8, 11);
 		sheet.mergeCells(9, 11, 11, 11);
        
 		label = new Label(0,11,"Ŭ�� ���", defaultFormat);
        sheet.addCell(label);
        number = new Number(3,11,massMailReportMonthSendInfo.getClickTotal(), dataFormat);
        sheet.addCell(number);
        label = new Label(6,11,"���� �ź� ���", defaultFormat);
        sheet.addCell(label);
        number = new Number(9,11,massMailReportMonthSendInfo.getRejectcallTotal(), dataFormat);
        sheet.addCell(number);
        
        sheet.setRowView(13, 300);
        sheet.mergeCells(0, 13, 11, 13);
 		label = new Label(0, 13, "["+listTitle+"]", subTitleFormat);
 		sheet.addCell(label);
        
 		sheet.mergeCells(1, 14, 3, 14);
 		sheet.mergeCells(4, 14, 5, 14);
 		sheet.mergeCells(6, 14, 7, 14);
 		sheet.mergeCells(8, 14, 9, 14);
 		sheet.mergeCells(10, 14, 11, 14);
 		
 		label = new Label(0,14,"ID", defaultFormat);
        sheet.addCell(label);
        label = new Label(1,14,"�뷮���ϸ�", defaultFormat);
        sheet.addCell(label);
        label = new Label(4,14,"�ۼ���", defaultFormat);
        sheet.addCell(label);
        label = new Label(6,14,"�߼۽�����", defaultFormat);
        sheet.addCell(label);
        label = new Label(8,14,"�ѹ߼۰Ǽ�", defaultFormat);
        sheet.addCell(label);
        label = new Label(10,14,"����", defaultFormat);
        sheet.addCell(label);
        int i =0;
        String StateType = "";
        for(MassMailList massmail : massmaillist){
        	i++;
        	sheet.mergeCells(1, 14+i, 3, 14+i);
     		sheet.mergeCells(4, 14+i, 5, 14+i);
     		sheet.mergeCells(6, 14+i, 7, 14+i);
     		sheet.mergeCells(8, 14+i, 9, 14+i);
     		sheet.mergeCells(10, 14+i, 11, 14+i);
     		 
     	    label = new Label(0,14+i,massmail.getMassmailID()+"" , dataFormat);
            sheet.addCell(label);
            label = new Label(1,14+i,massmail.getMassmailTitle(), dataFormat);
            sheet.addCell(label);
            label = new Label(4,14+i,massmail.getUserName(), dataFormat);
            sheet.addCell(label);
            label = new Label(6,14+i,massmail.getSendScheduleDate(), dataFormat);
            sheet.addCell(label);
            number = new Number(8,14+i,massmail.getSendCount(), dataFormat);
            sheet.addCell(number);
             
             if(massmail.getState().equals("00")){
             	StateType = "�ӽ�������";
             }
             if(massmail.getState().equals("10")){
             	StateType = "���δ����";
             }
             if(massmail.getState().equals("11")){
             	StateType = "�߼��غ�����";
             }
             if(massmail.getState().equals("12")){
             	StateType = "�߼��غ���";
             }
             if(massmail.getState().equals("13")){
             	StateType = "�߼��غ�Ϸ�";
             }
             if(massmail.getState().equals("14")){
             	StateType = "�߼���";
             }
             if(massmail.getState().equals("15")){
             	StateType = "�߼ۿϷ�";
             }
             if(massmail.getState().equals("16")){
             	StateType = "��������߼���";
             }
             if(massmail.getState().equals("22")){
             	StateType = "�߼��غ��߿���";
             }
             if(massmail.getState().equals("24")){
             	StateType = "�߼��߿���";
             }
             if(massmail.getState().equals("26")){
             	StateType = "��������߼��߿���";
             }
             if(massmail.getState().equals("32")){
             	StateType = "�߼��غ�������";
             }
             if(massmail.getState().equals("34")){
             	StateType = "�߼��Ͻ�����";
             }
             if(massmail.getState().equals("44")){
             	StateType = "�߼�����";
             }
             
            label = new Label(10,14+i,StateType, dataFormat);
            sheet.addCell(label);
     		
        }
        workbook.write();
        workbook.close();
        
	} catch(Exception e) {
		System.out.println(e.toString());
	} finally {
	
	    if(workbook != null) { try { workbook.close(); } catch(Exception e) {} }
	
	}
	
%>