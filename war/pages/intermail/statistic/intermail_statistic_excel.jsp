<%@ page contentType="application/vnd.ms-excel;charset=euc-kr"%>
<%@ page import="java.io.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import ="jxl.format.Colour"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.ScriptStyle"%>
<%@ page import="web.intermail.service.InterMailService"%>
<%@ page import="web.intermail.control.InterMailControlHelper"%>
<%@ page import="web.intermail.model.*"%>
<%@ page import="web.common.util.*"%>
<%@ page import="java.util.*"%>
<%
	String standard = request.getParameter("sStandard");
	String title = "";
	List<MailStatistic> mailStatisticList = null;
	List<FailCauseStatistic> failCauselStatisticList = null;
	InterMailService service = InterMailControlHelper.getUserService(application);
	
	String intermailID = request.getParameter("intermailID");
	String scheduleID = request.getParameter("scheduleID");
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
	searchMap.put("intermailID", new Integer(intermailID));
	searchMap.put("scheduleID", new Integer(scheduleID));
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
	}else if (standard.equals("domain")){
		title = "도메인";
		mailStatisticList = service.statisticDomainList(searchMap);
	}else if (standard.equals("failcause")){
		title = "실패원인";
		failCauselStatisticList = service.statisticFailCauseList(searchMap);
	}
	
	out.clear();
	File file = (File)application.getAttribute("javax.servlet.context.tempdir");
	String filePath = file.getAbsolutePath();
	String fileName = "InterMail_Report.xls";
	
	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	   
    try {
    	 response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    	 workbook = Workbook.createWorkbook(response.getOutputStream());
    	 sheet = workbook.createSheet("연동메일 - "+title+"별 통계 현황" , 0);
    	 
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
 		label = new Label(0, 0, "연동메일 - "+title+"별 통계 현황" , titleFormat);
 		sheet.addCell(label);
 
 		
        if(standard.equals("failcause")){
        	sheet.mergeCells(3, 2, 4, 2);
        	label = new Label(0,2,"번호", defaultFormat);
            sheet.addCell(label);
        	label = new Label(1,2,"실패구분", defaultFormat);
	        sheet.addCell(label);
	        label = new Label(2,2,"실패통수", defaultFormat);
	        sheet.addCell(label);
	        label = new Label(3,2,"설명", defaultFormat);
	        sheet.addCell(label);
    	}else{
    		label = new Label(0,2,title, defaultFormat);
            sheet.addCell(label);
    		label = new Label(1,2,"전체통수", defaultFormat);
    	    sheet.addCell(label);
    	    label = new Label(2,2,"성공통수", defaultFormat);
    	    sheet.addCell(label);
    	    label = new Label(3,2,"실패통수", defaultFormat);
    	    sheet.addCell(label);
    	    label = new Label(4,2,"오픈통수", defaultFormat);
    	    sheet.addCell(label); 
    	}
        
        int i =0;
        
        if(standard.equals("failcause")){
        	 for(FailCauseStatistic failCauselStatistic : failCauselStatisticList){
        		i++;
        		sheet.mergeCells(3, 2+i, 4, 2+i);
             	label = new Label(0,2+i,failCauselStatistic.getFailCauseType(), dataFormat);
                 sheet.addCell(label);
             	label = new Label(1,2+i,failCauselStatistic.getFailCauseTypeDes(), dataFormat);
     	        sheet.addCell(label);
     	        label = new Label(2,2+i,failCauselStatistic.getFailCount()+"", dataFormat);
     	        sheet.addCell(label);
     	        label = new Label(3,2+i,failCauselStatistic.getFailCauseDes(), dataFormat);
     	        sheet.addCell(label);
        	 }
        }else{
        	
        	 for(MailStatistic mailStatistic : mailStatisticList){
        		i++;
        		label = new Label(0,2+i,mailStatistic.getStandard(), dataFormat);
                sheet.addCell(label);
         		label = new Label(1,2+i,mailStatistic.getSendTotal()+"", dataFormat);
         	    sheet.addCell(label);
         	    label = new Label(2,2+i,mailStatistic.getSuccessTotal()+"", dataFormat);
         	    sheet.addCell(label);
         	    label = new Label(3,2+i,mailStatistic.getFailTotal()+"", dataFormat);
         	    sheet.addCell(label);
         	    label = new Label(4,2+i,mailStatistic.getOpenTotal()+"", dataFormat);
         	    sheet.addCell(label); 
        	 }
        }
     	
        
       
        workbook.write();
        workbook.close();
        
	} catch(Exception e) {
		System.out.println(e.toString());
	} finally {
	
	    if(workbook != null) { try { workbook.close(); } catch(Exception e) {} }
	
	}
	
%>