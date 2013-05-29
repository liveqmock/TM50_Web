<%@ page contentType="application/vnd.ms-excel;charset=euc-kr"%>
<%@ page import="java.io.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import ="jxl.format.Colour"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.ScriptStyle"%>
<%@ page import="web.autosms.service.*"%>
<%@ page import="web.autosms.control.AutoSMSControlHelper"%>
<%@ page import="web.autosms.model.*"%>
<%@ page import="web.common.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="jxl.write.Number"%>
<%
	int iLineCnt = 0;
	try{
		iLineCnt = Integer.parseInt(request.getParameter("iTotalCnt"));	
	}catch(Exception e){
		iLineCnt = 0;
	}
	
	System.out.println(iLineCnt);
	
	String year = request.getParameter("sSendedYear");
	String month = request.getParameter("sSendedMonth");
	
	//String totalCount = request.getParameter("totalCount");	
	String sendTime = year + month;
	
	
	AutoSMSService service = AutoSMSControlHelper.getUserService(application);   
	
    Map<String, Object> searchMap = new HashMap<String, Object>(); 
    searchMap.put("sendTime", sendTime);
	searchMap.put("iLineCnt", iLineCnt);
	searchMap.put("curPage", 1);
	
	List<AutoSMSStatistic> AutoSMSStatisticList = service.autoSMSReportMonth(searchMap);
	AutoSMSStatistic AutoSMSStatistic = service.autoSMSReportMonthAll(sendTime);

	out.clear();
	File file = (File)application.getAttribute("javax.servlet.context.tempdir");
	String filePath = file.getAbsolutePath();
	String fileName = "AutoSMSReportMonth.xls";
	
	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	Number number = null;
	
	    
    try {
    	 response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    	 workbook = Workbook.createWorkbook(response.getOutputStream());
    	 sheet = workbook.createSheet("월간통계분석", 0);
    	 
    	//Font 설정
    	WritableFont titleFont = new WritableFont(WritableFont.createFont("굴림체"), 16, WritableFont.BOLD, false);
    	WritableFont gulimFont = new WritableFont(WritableFont.createFont("굴림체"), 12, WritableFont.NO_BOLD, false);
    	WritableFont gulimBOLDFont = new WritableFont(WritableFont.createFont("굴림체"), 10, WritableFont.BOLD, false);
 		WritableFont gulimBLUEFont = new WritableFont(WritableFont.createFont("굴림체"), 10, WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.DARK_BLUE,ScriptStyle.NORMAL_SCRIPT);
 		
 		//Format 설정
 		
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
        
        sheet.setColumnView(0, 10);
        sheet.setColumnView(1, 10);
        sheet.setColumnView(2, 10);
        sheet.setColumnView(3, 10);
        sheet.setColumnView(4, 10);
        sheet.setColumnView(5, 10);
        sheet.setColumnView(6, 10);
        sheet.setColumnView(7, 10);
        sheet.setColumnView(8, 10);
        sheet.setColumnView(9, 10);
        sheet.setColumnView(10,10);
        sheet.setColumnView(11,10);  

     
        sheet.setRowView(0, 500);
        sheet.mergeCells(0, 0, 13, 0);
 		if(month.equals("13")){
	        label = new Label(0, 0, "연간통계분석 ("+year+"년)", titleFormat);
	 		sheet.addCell(label);
 		}else{
 			label = new Label(0, 0, "월간통계분석 ("+year+"년 "+month+"월)", titleFormat);
 	 		sheet.addCell(label);
 		}
 		
 		sheet.setRowView(2, 300);
 	    sheet.mergeCells(0, 2, 13, 2);
 	 	label = new Label(0, 2, "[총 발송 현황]", subTitleFormat);
 	 	sheet.addCell(label);
 	 	
 	 	sheet.mergeCells(0, 3, 1, 3);
 		sheet.mergeCells(2, 3, 3, 3);
 		sheet.mergeCells(4, 3, 5, 3);
 		sheet.mergeCells(6, 3, 7, 3);
 		sheet.mergeCells(8, 3, 9, 3);
 		sheet.mergeCells(10, 3, 11, 3);
 		sheet.mergeCells(12, 3, 13, 3);
 	 	
 		label = new Label(0,3,"총  SMS발송량", defaultFormat);
        sheet.addCell(label);
        number = new Number(2,3,AutoSMSStatistic.getSendTotal(), dataFormat);
        sheet.addCell(number);
        

 		label = new Label(4,3,"성공통수", defaultFormat);
        sheet.addCell(label);
        number = new Number(6,3,AutoSMSStatistic.getSuccessTotal(), dataFormat);
        sheet.addCell(number);
        

 		label = new Label(8,3,"실패통수", defaultFormat);
        sheet.addCell(label);
        number = new Number(10,3,AutoSMSStatistic.getFailTotal(), dataFormat);
        sheet.addCell(number);
        
 	 	sheet.mergeCells(0, 6, 0, 6);
 		sheet.mergeCells(1, 6, 5, 6);
 		sheet.mergeCells(6, 6, 7, 6);
 		sheet.mergeCells(8, 6, 9, 6);
 		sheet.mergeCells(10, 6, 11, 6);
 		sheet.mergeCells(12, 6, 13, 6);
 		
 		label = new Label(0,6,"ID", defaultFormat);
        sheet.addCell(label);
        
 		label = new Label(1,6,"이벤트 제목", defaultFormat);
        sheet.addCell(label);
        
 		label = new Label(6,6,"집계기간", defaultFormat);
        sheet.addCell(label);
        
 		label = new Label(8,6,"총발송량", defaultFormat);
        sheet.addCell(label);
        
 		label = new Label(10,6,"성공통수", defaultFormat);
        sheet.addCell(label);
        
 		label = new Label(12,6,"실패통수", defaultFormat);
        sheet.addCell(label);
        
        int i = 0;
        
        for ( AutoSMSStatistic autosms:AutoSMSStatisticList){
        	i++;
        	
        	sheet.mergeCells(0, 6+i, 0, 6+i);
     		sheet.mergeCells(1, 6+i, 5, 6+i);
     		sheet.mergeCells(6, 6+i, 7, 6+i);
     		sheet.mergeCells(8, 6+i, 9, 6+i);
     		sheet.mergeCells(10, 6+i, 11, 6+i);
     		sheet.mergeCells(12, 6+i, 13, 6+i);
     		
     		label = new Label(0, 6+i, autosms.getAutosmsID()+"", dataFormat);
            sheet.addCell(label);
            label = new Label(1, 6+i, autosms.getAutosmsTitle(), dataFormat);
            sheet.addCell(label);
            
            label = new Label(6, 6+i, autosms.getSendTimeHour(), dataFormat);
            sheet.addCell(label);
            
            label = new Label(8, 6+i, autosms.getSendTotal()+"", dataFormat);
            sheet.addCell(label);
            label = new Label(10, 6+i, autosms.getSuccessTotal()+"", dataFormat);
            sheet.addCell(label);
            label = new Label(12, 6+i, autosms.getFailTotal()+"", dataFormat);
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