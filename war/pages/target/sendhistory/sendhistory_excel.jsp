<%@ page contentType="application/vnd.ms-excel;charset=euc-kr"%>
<%@ page import="java.io.*" %>
<%@ page import="jxl.*" %>
<%@ page import="jxl.write.*" %>
<%@ page import ="jxl.format.Colour"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.UnderlineStyle"%>
<%@ page import=" jxl.format.ScriptStyle"%>
<%@ page import="jxl.write.Number"%>
<%@ page import="web.common.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="web.target.sendhistory.model.*"%>
<%@ page import="web.target.sendhistory.service.SendHistoryService"%>
<%@ page import="web.target.sendhistory.control.SendHistoryControllerHelper"%>
<%

	String sendedYear = request.getParameter("sSendedYear");
	String sendedMonth = request.getParameter("sSendedMonth");
	String sendYN = request.getParameter("sSendYN");
	String openYN = request.getParameter("sOpenYN");
	String clickYN = request.getParameter("sClickYN");
	String rejectcallYN = request.getParameter("sRejectcallYN");
	String searchType = request.getParameter("sSearchType");
	String searchText = request.getParameter("sSearchText");
	
	
	//페이지조건과 검색조건을 모두 map에 담아 사용하자
	Map<String, String> searchMap = new HashMap<String, String>(); 
	if(Integer.parseInt(sendedMonth)<10){
		sendedMonth="0"+sendedMonth;
	}

	searchMap.put("sendedYear",sendedYear);
	searchMap.put("sendedMonth",sendedMonth);
	searchMap.put("searchType", searchType);
	searchMap.put("searchText", searchText);	
	searchMap.put("sendYN", sendYN);
	searchMap.put("openYN", openYN);
	searchMap.put("clickYN", clickYN);
	searchMap.put("rejectcallYN", rejectcallYN);
	String[] userInfo = new String[3];
	userInfo[0] =  LoginInfo.getUserAuth(request); 
	userInfo[1] =  LoginInfo.getUserID(request);
	userInfo[2] =   LoginInfo.getGroupID(request);
	
	
	SendHistoryService sendHistoryService = SendHistoryControllerHelper.getUserService(application); 		
	int totalCount = 60000;//sendHistoryService.totalCountMassMailSendResult(userInfo, searchMap);
	//System.out.println("totalCount : "+totalCount);
	List<MassMailSendResult> massMailSendResultList = sendHistoryService.listMassMailSendResult(userInfo, 1, totalCount, searchMap);
	
	out.clear();
	File file = (File)application.getAttribute("javax.servlet.context.tempdir");
	String filePath = file.getAbsolutePath();
	String fileName = "EmailSendHistory.xls";
	
	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	Number number = null;
	
	try {
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
   	    workbook = Workbook.createWorkbook(response.getOutputStream());
   	    sheet = workbook.createSheet("이메일 별 발송 히스토리", 0);
   	    
   		//Font 설정
    	WritableFont titleFont = new WritableFont(WritableFont.createFont("굴림체"), 16, WritableFont.BOLD, false);
    	WritableFont gulimFont = new WritableFont(WritableFont.createFont("굴림체"), 12, WritableFont.NO_BOLD, false);
    	WritableFont gulimBOLDFont = new WritableFont(WritableFont.createFont("굴림체"), 10, WritableFont.BOLD, false);
 		WritableFont gulimBLUEFont = new WritableFont(WritableFont.createFont("굴림체"), 10, WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.DARK_BLUE,ScriptStyle.NORMAL_SCRIPT);
 		
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
        
        WritableCellFormat dataFormat1 = new WritableCellFormat (gulimFont);
        dataFormat1.setBorder(Border.ALL, BorderLineStyle.THIN);
        dataFormat1.setAlignment(Alignment.LEFT);
        dataFormat1.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        sheet.setRowView(0, 500);
        sheet.mergeCells(0, 0, 15, 0);
 		label = new Label(0, 0, "이메일별 발송 히스토리", titleFormat);
 		sheet.addCell(label);
 		
 		sheet.mergeCells(0, 2, 2, 2);
 		sheet.mergeCells(3, 2, 4, 2);
 		sheet.mergeCells(5, 2, 8, 2);
 		sheet.mergeCells(13, 2, 15, 2);
 		
 		label = new Label(0,2,"이메일", defaultFormat);
        sheet.addCell(label);
        label = new Label(3,2,"고객명", defaultFormat);
        sheet.addCell(label);
        label = new Label(5,2,"대량메일명", defaultFormat);
        sheet.addCell(label);
        label = new Label(9,2,"성공유무", defaultFormat);
        sheet.addCell(label);
        label = new Label(10,2,"오픈여부", defaultFormat);
        sheet.addCell(label);
        label = new Label(11,2,"클릭여부", defaultFormat);
        sheet.addCell(label);
        label = new Label(12,2,"수신거부", defaultFormat);
        sheet.addCell(label);
        label = new Label(13,2,"발송일", defaultFormat);
        sheet.addCell(label);
        int i =0;
        String StateType = "";
        
        for(MassMailSendResult massMailSendResult : massMailSendResultList){
        	i++;
        	sheet.mergeCells(0, 2+i, 2, 2+i);
     		sheet.mergeCells(3, 2+i, 4, 2+i);
     		sheet.mergeCells(5, 2+i, 8, 2+i);
     		sheet.mergeCells(13, 2+i, 15, 2+i);
     		
     		label = new Label(0,2+i,massMailSendResult.getEmail()+"" , dataFormat1);
            sheet.addCell(label);
            label = new Label(3,2+i,massMailSendResult.getName()+"" , dataFormat1);
            sheet.addCell(label);
            label = new Label(5,2+i,massMailSendResult.getMassmailTitle()+"" , dataFormat1);
            sheet.addCell(label);
            label = new Label(9,2+i,massMailSendResult.getSmtpCodeType()+"" , dataFormat);
            sheet.addCell(label);
            label = new Label(10,2+i,massMailSendResult.getOpenYN()+"" , dataFormat);
            sheet.addCell(label);
            label = new Label(11,2+i,massMailSendResult.getClickYN()+"" , dataFormat);
            sheet.addCell(label);
            label = new Label(12,2+i,massMailSendResult.getRejectcallYN()+"" , dataFormat);
            sheet.addCell(label);
            label = new Label(13,2+i,massMailSendResult.getRegistDate()+"" , dataFormat);
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
