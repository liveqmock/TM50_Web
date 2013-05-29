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
<%@ page import="web.content.poll.model.*" %>
<%
	
	int massmailID = Integer.parseInt(request.getParameter("massmailID"));
	int scheduleID = Integer.parseInt(request.getParameter("scheduleID"));
	int pollID = Integer.parseInt(request.getParameter("pollID"));
	
	String nowTime = request.getParameter("nowTime");
	String nowDate = request.getParameter("nowDate");
	
	Map<String, Object> searchMap = new HashMap<String, Object>(); 
	searchMap.put("massmailID", massmailID);
	searchMap.put("scheduleID", scheduleID);
	
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	String backupYearMonth = service.getBackupYearMonth(searchMap);		
	Map<String,Object> massMailStatisticPollList = null;		
	//이미 백업됬어다면 백업테이블로 조회 
	if(backupYearMonth!=null && !backupYearMonth.equals("")){
		massMailStatisticPollList = service.massmailPollStatisticCountFinish(backupYearMonth,massmailID, scheduleID, pollID);
	}else{
		massMailStatisticPollList = service.massmailPollStatisticCount(massmailID, scheduleID, pollID);
	}
	
	
	//전체발송통수
	int sendTotalCount = Integer.parseInt(String.valueOf(massMailStatisticPollList.get("sendTotalCount")));
	
	//전체응답통수 
	int responseCount = Integer.parseInt(String.valueOf(massMailStatisticPollList.get("responseCount")));	
	
	//미응답통수 
	int notresponseCount = sendTotalCount - responseCount;
	
	out.clear();
	File file = (File)application.getAttribute("javax.servlet.context.tempdir");
	String filePath = file.getAbsolutePath();
	String fileName = "Report_Poll_"+nowDate+"_"+nowTime+".xls";
	
	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	jxl.write.Number number = null;
		
	WritableSheet sheet2 = null;
		
	try{
		
	   	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		workbook = Workbook.createWorkbook(response.getOutputStream());
		sheet = workbook.createSheet("설문통계", 0);
			
   	 
    	//Font 설정
    	WritableFont titleFont = new WritableFont(WritableFont.createFont("굴림체"), 16, WritableFont.BOLD, false);
    	WritableFont gulimFont = new WritableFont(WritableFont.createFont("굴림체"), 12, WritableFont.NO_BOLD, false);
    	WritableFont gulimBOLDFont = new WritableFont(WritableFont.createFont("굴림체"), 10, WritableFont.BOLD, false);
    	
 		//Format 설정
 		
 		//제목설정
 		WritableCellFormat titleFormat = new WritableCellFormat (titleFont);
 		titleFormat.setAlignment(Alignment.CENTRE);
 		
 		
 		//일반보기
 		WritableCellFormat pollExampleFormat = new WritableCellFormat (gulimFont);
 		pollExampleFormat.setBorder(Border.ALL, BorderLineStyle.NONE);
 		pollExampleFormat.setAlignment(Alignment.LEFT);
 		pollExampleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
 		       
 		//설문번호	
 		WritableCellFormat pollNumberFormat = new WritableCellFormat (gulimBOLDFont);
 		pollNumberFormat.setBackground(Colour.GRAY_25);
 		pollNumberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
 		pollNumberFormat.setAlignment(Alignment.CENTRE);
 		pollNumberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); 		
        
        
 		//설문문항	
 		WritableCellFormat pollQuestionFormat = new WritableCellFormat (gulimBOLDFont);
 		pollQuestionFormat.setBackground(Colour.GRAY_25);
 		pollQuestionFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
 		pollQuestionFormat.setAlignment(Alignment.LEFT);
 		pollQuestionFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
 		
 	
 		

        //데이터설정
        //WritableCellFormat dataFormat = new WritableCellFormat (gulimFont);
        //dataFormat.setBorder(Border.ALL, BorderLineStyle.NONE);
        //dataFormat.setAlignment(Alignment.CENTRE);
        //dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        
    // sheet2 기본 출력 및 설정
        sheet.setRowView(0, 500);
        sheet.mergeCells(0, 0, 6, 0);
 		label = new Label(0, 0, service.selectPollTitle(pollID) , titleFormat);
 		sheet.addCell(label);
        
        
 		label = new Label(0,2,"전체통계", pollQuestionFormat);
        sheet.addCell(label);        
        number = new jxl.write.Number(1,2,sendTotalCount);
        sheet.addCell(number);
        label = new Label(2,2,"( "+StringUtil.getRatioToString(sendTotalCount,sendTotalCount)+"% )");
        sheet.addCell(label);
        
        label = new Label(0,3,"응답수", pollQuestionFormat);
        sheet.addCell(label);        
        number = new jxl.write.Number(1,3,responseCount);
        sheet.addCell(number);
        label = new Label(2,3,"( "+StringUtil.getRatioToString(responseCount,sendTotalCount)+"% )");
        sheet.addCell(label);
        
        
        
        label = new Label(0,4,"미응답수", pollQuestionFormat);
        sheet.addCell(label);        
        number = new jxl.write.Number(1,4,notresponseCount);        
        sheet.addCell(number);       
        label = new Label(2,4,"( "+StringUtil.getRatioToString(notresponseCount,sendTotalCount)+"% )");        
        sheet.addCell(label);
        
		String statisticYear = nowDate.substring(0,4)+"년 ";
		String statisticMonth = nowDate.substring(4,6)+"월 ";
		String statisticDate = nowDate.substring(6)+"일";
        
        label = new Label(8,2,"수집날짜", pollQuestionFormat);
        sheet.addCell(label);        
        label = new Label(9,2,statisticYear+statisticMonth+statisticDate);
        sheet.addCell(label);
       	
       
        sheet.setColumnView(0, 20);	//첫번째 열사이즈 15 
       
        
        int pollExampleExYNcount = service.pollExampleExYNcount(pollID); // 객관식 문항  주관식 답안 갯수(설문내에 포함된 갯수)
        int exampleTypeCount = service.exampleTypeCount(pollID); // 설문 내 주관식 문항 갯 수
        
// sheet2 기본 출력 및 설정
        if(pollExampleExYNcount > 0 || exampleTypeCount > 0){
        	// 문제내에 주관식이 포함된 객관식문항이나 주관식 문항이 있을 경우 2번 시트(주관식답안 시트) 생성
        	sheet2 = workbook.createSheet("주관식 답안 목록", 1);
        	
	        sheet2.setRowView(0, 500);
	        sheet2.mergeCells(0, 0, 6, 0);
	 		label = new Label(0, 0, service.selectPollTitle(pollID) , titleFormat);
	 		sheet2.addCell(label);	        
	        
	        sheet2.setColumnView(0, 20);	//첫번째 열사이즈
	        
	        label = new Label(8,2,"수집날짜", pollQuestionFormat);
	        sheet2.addCell(label);        
	        label = new Label(9,2,statisticYear+statisticMonth+statisticDate);
	        sheet2.addCell(label);
	        
        }

// sheet1, 2 기본 출력 및 설정 끝


// 전체 문항 내용 출력
        
        List<PollQuestion> pollQuestionList =  service.selectQuestionByPollID(pollID);
        
        int ac = 6;  //sheet1 설문 시작 행
        int ac2 = 4; // sheet2 설문 시작 행 초기화
        
        //일단 전체 설문문항으로 루핑..
        for(int i=0;i<pollQuestionList.size();i++){
        	
        	PollQuestion pollQuestion = pollQuestionList.get(i);
        	
        	
        	//일반 설문일 경우 
        	if(pollQuestion.getQuestionType().equals("1") && !pollQuestion.getExampleType().equals("3")){
        		List<MassMailStatisticPoll> massMailStatisticPollListNormal = service.selectPollStatisticByQuestionID(massmailID,scheduleID,pollID,pollQuestion.getQuestionID());
        		
        		//System.out.println(pollQuestion.getQuestionNo()+"=="+ac);
        		
        		label = new Label(0,ac, Integer.toString(pollQuestion.getQuestionNo()), pollNumberFormat);
        		sheet.addCell(label);
        		sheet.mergeCells(1,ac,19,ac);
        		label = new Label(1,ac, pollQuestion.getQuestionText(), pollQuestionFormat);
        		sheet.addCell(label);
        		String exampleDesc ="";
        		for(int j=0;j<massMailStatisticPollListNormal.size();j++){
        			MassMailStatisticPoll massMailStatisticPoll = massMailStatisticPollListNormal.get(j);
        			exampleDesc = massMailStatisticPoll.getExampleDesc();
    				exampleDesc = exampleDesc.replace("≠"," [  ] ");
        			label = new Label(0,ac+(j+1),"("+(massMailStatisticPoll.getExampleID()+")."+exampleDesc), pollExampleFormat);
        			sheet.addCell(label);        			
        			number = new jxl.write.Number(1,ac+(j+1),massMailStatisticPoll.getResponseCount());
        			sheet.addCell(number);
        			label = new Label(2,ac+(j+1),"( "+StringUtil.getRatioToString(massMailStatisticPoll.getResponseCount(),responseCount)+"% )");
        			sheet.addCell(label);
        		}
        		ac = ac + massMailStatisticPollListNormal.size()+2;
        	}else if(pollQuestion.getExampleType().equals("3")){	
        		List<MassMailStatisticPoll> massMailStatisticPollListNormal = service.selectPollStatisticByQuestionID(massmailID,scheduleID,pollID,pollQuestion.getQuestionID());
				List<MassMailStatisticPoll> massMailStatisticPollExampleType3List = service.selectPollStatisticByExampleType3(massmailID,scheduleID,pollID,pollQuestion.getQuestionID(), massMailStatisticPollListNormal.size());
				label = new Label(0,ac, Integer.toString(pollQuestion.getQuestionNo()), pollNumberFormat);
        		sheet.addCell(label);
        		sheet.mergeCells(1,ac,19,ac);
        		label = new Label(1,ac, pollQuestion.getQuestionText(), pollQuestionFormat);
        		sheet.addCell(label);
        		label = new Label(0,ac+1,"", pollQuestionFormat);
    			sheet.addCell(label);
        		for(int j=0;j<massMailStatisticPollListNormal.size();j++){
        			label = new Label((j+1),ac+1,(j+1)+"순위", pollQuestionFormat);
        			sheet.addCell(label);
        		}
        		ac++;
        		for(int j=0;j<massMailStatisticPollListNormal.size();j++){
        			MassMailStatisticPoll massMailStatisticPoll = massMailStatisticPollListNormal.get(j);
        			MassMailStatisticPoll massMailStatisticPollExampleType3= (MassMailStatisticPoll)massMailStatisticPollExampleType3List.get(j);
					String[] responseCountArry = massMailStatisticPollExampleType3.getResponseCountArry();
					
        			label = new Label(0,ac+(j+1),"("+(massMailStatisticPoll.getExampleID()+")."+massMailStatisticPoll.getExampleDesc()), pollExampleFormat);
        			sheet.addCell(label); 
        			
        			number = new jxl.write.Number(1,ac+(j+1),Integer.parseInt(responseCountArry[0]));
        			sheet.addCell(number);
        			
        			number = new jxl.write.Number(2,ac+(j+1),Integer.parseInt(responseCountArry[1]));
        			sheet.addCell(number);
        			for(int c=2;c<responseCountArry.length;c++){ 
        				number = new jxl.write.Number((c+1),ac+(j+1),Integer.parseInt(responseCountArry[c]));
            			sheet.addCell(number);
        			}
        		}
        		ac = ac + massMailStatisticPollListNormal.size()+2;
        	//매트릭스일 경우 		
        	}else if(pollQuestion.getQuestionType().equals("2")){
        		List<MassMailStatisticPoll> massMailStatisticPollAnswerList = service.selectPollAnswerMatrixXY(massmailID,scheduleID,pollID,pollQuestion.getQuestionID());
        		List<MassMailStatisticPoll> massMailStatisticPollListMatrixY = service.selectPollExampleMatrixY(pollQuestion.getPollID(), pollQuestion.getQuestionID());
        		//System.out.println(pollQuestion.getQuestionNo()+"=="+ac);
        		
        		label = new Label(0,ac, Integer.toString(pollQuestion.getQuestionNo()), pollNumberFormat);
        		sheet.addCell(label);
        		sheet.mergeCells(1,ac,19,ac);
        		label = new Label(1,ac, pollQuestion.getQuestionText(), pollQuestionFormat);
        		sheet.addCell(label);
        		
        		int an = 0; //행보기열 
        		
    			for(int y=0;y<massMailStatisticPollListMatrixY.size();y++){
    				MassMailStatisticPoll massMailStatisticPollY = massMailStatisticPollListMatrixY.get(y);       
    				label = new Label(y+1,ac+1, massMailStatisticPollY.getExampleDesc(), pollExampleFormat);
    				sheet.addCell(label);    			
    				an = ac+1;
    			}
    			
    			label = new Label(0,an+1, "", pollExampleFormat);
    			sheet.addCell(label);
        		
        		for(int x=0;x<massMailStatisticPollAnswerList.size();x++){
        			MassMailStatisticPoll massMailStatisticPollX = massMailStatisticPollAnswerList.get(x);
        			label = new Label(0,an+(x+1), massMailStatisticPollX.getExampleDesc(), pollExampleFormat);
        			sheet.addCell(label);         	
        			
        			//매트릭스 응답한 숫자 
        			int yCount = massMailStatisticPollX.arrayListValue.size();
        			for(int y=0;y<yCount;y++){
            			number = new jxl.write.Number(y+1,an+(x+1),massMailStatisticPollX.arrayListValue.get(y));
            			sheet.addCell(number);            			
        			}

        		}
        		
        		
        		ac = ac + massMailStatisticPollAnswerList.size()+3;
        		
        	}
        	
        	//sheet2 주관식 답내역 출력        	
        	
        	if(pollQuestion.getQuestionType().equals("1") && !pollQuestion.getExampleType().equals("3")){
        		List<MassMailStatisticPoll> massMailStatisticPollListNormal = service.selectPollStatisticByQuestionID(massmailID,scheduleID,pollID,pollQuestion.getQuestionID());
        		List<MassMailStatisticPoll> responseTextAllList = service.responseTextAll(pollID,massmailID,scheduleID,pollQuestion.getQuestionID());
        		int exampleExYNcount = service.exampleExYNcount(pollID,pollQuestion.getQuestionID());
        		
        		if(pollQuestion.getExampleType().equals("2") || exampleExYNcount > 0){
			        label = new Label(0,ac2, Integer.toString(pollQuestion.getQuestionNo()), pollNumberFormat);
			        sheet2.addCell(label);
			        sheet2.mergeCells(1,ac2,19,ac2);
			        label = new Label(1,ac2, pollQuestion.getQuestionText(), pollQuestionFormat);
			        sheet2.addCell(label);
	    			label = new Label(0,ac2+1,"주관식 답안 내역 : ",pollExampleFormat);
		        	sheet2.addCell(label);
			        	
		        	if(pollQuestion.getExampleType().equals("2")){
		        		for(int j=0;j<massMailStatisticPollListNormal.size();j++){
		        			MassMailStatisticPoll massMailStatisticPoll = massMailStatisticPollListNormal.get(j);
		        			
			        		for(int k=0;k<responseTextAllList.size();k++){
			        			MassMailStatisticPoll responseTextAll = responseTextAllList.get(k);
			        			number = new jxl.write.Number(1,ac2+(j+1)+k,k+1);
			        			sheet2.addCell(number);
			        			label = new Label(2,ac2+(j+1)+k,responseTextAll.getResponseText(),pollExampleFormat);
			        			sheet2.addCell(label);
			        		}
		        		}
		        	}else{
		        		
		        		for(int k=0;k<responseTextAllList.size();k++){
		        			MassMailStatisticPoll responseTextAll = responseTextAllList.get(k);
		        			number = new jxl.write.Number(1,ac2+1+k,k+1);
		        			sheet2.addCell(number);
		        			label = new Label(2,ac2+1+k,responseTextAll.getResponseText(),pollExampleFormat);
		        			sheet2.addCell(label);
		        		}
		        		
		        	}
	        		
	        		ac2 = ac2 + responseTextAllList.size() + 2;
        		}
        	}        
      
        }// end for 
                
        if(pollExampleExYNcount > 0 || exampleTypeCount > 0){// 주관식 답안 시트 생성 필요시 안내 문구 출력
	        sheet.mergeCells(0,ac+1,19,ac+1);
			label = new Label(0,ac+1, "※ 주관식 답안의 상세 내역은 다음 시트에서 확인 하실 수 있습니다.");
			sheet.addCell(label);
			
	        sheet2.mergeCells(0,ac2+1,19,ac2+1);
			label = new Label(0,ac2+1, "※ 답안을 입력하지 않았을 경우 출력 되지 않습니다.");
			sheet2.addCell(label);
        }
                
        
        workbook.write();
        workbook.close();
		
	}catch(Exception e){
		System.out.println(e.toString());
	}finally{
		  if(workbook != null) { try { workbook.close(); } catch(Exception e) {} }
	}
%>