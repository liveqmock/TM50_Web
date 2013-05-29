<%@ page contentType="application/vnd.ms-excel;charset=euc-kr"%>
<%@ page import="java.io.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import ="jxl.format.Colour"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.ScriptStyle"%>
<%@ page import="web.autosms.service.AutoSMSService"%>
<%@ page import="web.autosms.control.AutoSMSControlHelper"%>
<%@ page import="web.autosms.model.*"%>
<%@ page import="web.common.util.*"%>
<%@ page import="java.util.*"%>
<%
	String standard = request.getParameter("sStandard");
	String title = "";
	List<AutoSMSStatistic> mailStatisticList = null;	
	AutoSMSService service = AutoSMSControlHelper.getUserService(application);
	
	String autosmsID = request.getParameter("autosmsID");
	//String startDate = request.getParameter("startDate").replaceAll("-", "");
	//String endDate = request.getParameter("endDate").replaceAll("-", "");
	String year = request.getParameter("sYear");
	String month = request.getParameter("sMonth");
	if(month.length()==1)
		month = "0"+month;
	String day = request.getParameter("sDay");
	if(day.length()==1)
		day = "0"+day;
	

	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("autosmsID", new Integer(autosmsID));
	//searchMap.put("startDate", startDate);
	//searchMap.put("endDate", endDate);
	searchMap.put("year", year);
	searchMap.put("month", month);
	searchMap.put("day", day);
	
	if(standard.equals("hourly")){
		title = "시간";
		mailStatisticList = service.statisticHourlyList(searchMap);
	}else if (standard.equals("daily")){
		title = "일자";
		mailStatisticList = service.statisticDailyList(searchMap);
	}else if (standard.equals("monthly")){
		title = "월";
		mailStatisticList = service.statisticMonthlyList(searchMap);
	}
	
	out.clear();
	File file = (File)application.getAttribute("javax.servlet.context.tempdir");
	String filePath = file.getAbsolutePath();
	String fileName = "AutoSMS_Report.xls";
	
	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	   
    try {
    	 response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    	 workbook = Workbook.createWorkbook(response.getOutputStream());
    	 sheet = workbook.createSheet("자동SMS - "+title+"별 통계 현황" , 0);
    	 
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
        
        sheet.setColumnView(0, 17);
        sheet.setColumnView(1, 17);
        sheet.setColumnView(2, 17);
        sheet.setColumnView(3, 17);
        sheet.setColumnView(4, 17);
        sheet.setColumnView(5, 17);
        sheet.setColumnView(6, 17);
     
        sheet.setRowView(0, 500);
    	sheet.mergeCells(0, 0, 4, 0);
 		label = new Label(0, 0, "자동SMS - "+title+"별 통계 현황" , titleFormat);
 		sheet.addCell(label);
 
 		
     
    	label = new Label(0,2,title, defaultFormat);
        sheet.addCell(label);
    	label = new Label(1,2,"전체통수", defaultFormat);
    	sheet.addCell(label);
    	label = new Label(2,2,"결과대기통수", defaultFormat);
    	sheet.addCell(label);
    	label = new Label(3,2,"성공통수", defaultFormat);
    	sheet.addCell(label);
    	label = new Label(4,2,"실패통수", defaultFormat);
    	sheet.addCell(label); 
    
        
        int i =0;
        for(AutoSMSStatistic mailStatistic : mailStatisticList){
        	i++;
        	label = new Label(0,2+i,mailStatistic.getStandard(), dataFormat);
            sheet.addCell(label);
         	label = new Label(1,2+i,mailStatistic.getSendTotal()+"", dataFormat);
         	sheet.addCell(label);
         	label = new Label(2,2+i,mailStatistic.getReadyTotal()+"", dataFormat);
         	sheet.addCell(label);
         	label = new Label(3,2+i,mailStatistic.getSuccessTotal()+"", dataFormat);
         	sheet.addCell(label);
         	label = new Label(4,2+i,mailStatistic.getFailTotal()+"", dataFormat);
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