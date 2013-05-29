<%@ page contentType="application/vnd.ms-excel;charset=euc-kr"%>
<%@ page import="java.io.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import ="jxl.format.Colour"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.ScriptStyle"%>
<%@ page import="web.masssms.statistic.service.MassSMSStatService"%>
<%@ page import="web.masssms.statistic.control.MassSMSStatControlHelper"%>
<%@ page import="web.masssms.statistic.model.*"%>
<%@ page import="web.common.util.*"%>
<%@ page import="java.util.*"%>
<%
	
	String year = request.getParameter("sYear");
	String month = request.getParameter("sMonth");
	String userID =  request.getParameter("sUserID");
	String groupID =  request.getParameter("sGroupID");
	String[] userInfo = new String[3];
	userInfo[0] =  LoginInfo.getUserAuth(request); 
	userInfo[1] =  LoginInfo.getUserID(request);
	userInfo[2] =   LoginInfo.getGroupID(request);
	
	MassSMSStatService service = MassSMSStatControlHelper.getUserService(application);
    
    Map<String, String> searchMap = new HashMap<String, String>(); 
	searchMap.put("year", year);
	searchMap.put("month", month);
	searchMap.put("userID", userID);
	searchMap.put("groupID", groupID);
	
	List<MassSMSStatisticUsers> massSMSStatisticUsersList = service.masssmsStatisticUsersList(userInfo, searchMap);
    
	out.clear();
	File file = (File)application.getAttribute("javax.servlet.context.tempdir");
	String filePath = file.getAbsolutePath();
	String fileName = "Report_Users.xls";
	
	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	
	
	    
    try {
    	 response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    	 workbook = Workbook.createWorkbook(response.getOutputStream());
    	 sheet = workbook.createSheet("���庰 �߼� ��Ȳ", 0);
    	 
    	//Font ����
    	WritableFont titleFont = new WritableFont(WritableFont.createFont("����ü"), 16, WritableFont.BOLD, false);
    	WritableFont gulimFont = new WritableFont(WritableFont.createFont("����ü"), 12, WritableFont.NO_BOLD, false);
    	WritableFont gulimBOLDFont = new WritableFont(WritableFont.createFont("����ü"), 10, WritableFont.BOLD, false);
 		
 		//Format ����
 		
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
     
        sheet.setRowView(0, 500);
        sheet.mergeCells(0, 0, 6, 0);
 		label = new Label(0, 0, "���庰 �߼� ��Ȳ ("+year+"�� "+month+"��)", titleFormat);
 		sheet.addCell(label);
 		
 		label = new Label(0,2,"����", defaultFormat);
        sheet.addCell(label);
        label = new Label(1,2,"��ü���", defaultFormat);
        sheet.addCell(label);
        label = new Label(2,2,"�������", defaultFormat);
        sheet.addCell(label);
        label = new Label(3,2,"�������", defaultFormat);
        sheet.addCell(label);
        label = new Label(4,2,"������", defaultFormat);
        sheet.addCell(label);
        
        int i=0;
        int sendTotal=0;
        int successTotal=0;
        int failTotal=0;
        int readyTotal=0;
        
        
        for(MassSMSStatisticUsers massSMSStatisticUsers : massSMSStatisticUsersList){
        	
        	label = new Label(0,3+i,massSMSStatisticUsers.getUserName(), dataFormat);
            sheet.addCell(label);
            label = new Label(1,3+i,Integer.toString(massSMSStatisticUsers.getSendTotal()), dataFormat);
            sheet.addCell(label);
            label = new Label(2,3+i,Integer.toString(massSMSStatisticUsers.getSuccessTotal()), dataFormat);
            sheet.addCell(label);
            label = new Label(3,3+i,Integer.toString(massSMSStatisticUsers.getFailTotal()), dataFormat);
            sheet.addCell(label);
            label = new Label(4,3+i,Integer.toString(massSMSStatisticUsers.getReadyTotal()), dataFormat);
            sheet.addCell(label);
            i++;
            sendTotal+=massSMSStatisticUsers.getSendTotal();
            successTotal+=massSMSStatisticUsers.getSuccessTotal();
            failTotal+=massSMSStatisticUsers.getFailTotal();
            readyTotal+=massSMSStatisticUsers.getReadyTotal();
        }
        label = new Label(0,3+i,"�հ�", defaultFormat);
        sheet.addCell(label);
        label = new Label(1,3+i,sendTotal+"", dataFormat);
        sheet.addCell(label);
        label = new Label(2,3+i,successTotal+"", dataFormat);
        sheet.addCell(label);
        label = new Label(3,3+i,failTotal+"", dataFormat);
        sheet.addCell(label);
        label = new Label(4,3+i,readyTotal+"", dataFormat);
        sheet.addCell(label);
        workbook.write();
        workbook.close();
        
	} catch(Exception e) {
		System.out.println(e.toString());
	} finally {
	
	    if(workbook != null) { try { workbook.close(); } catch(Exception e) {} }
	
	}
	
%>