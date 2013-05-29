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

	//관리자라면 
	String isAdmin = LoginInfo.getIsAdmin(request);

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
	
	//각 설문 별 보기 총 갯수
	int exampleAllCount = service.exampleAllCount(pollID);
	
	out.clear();
	File file = (File)application.getAttribute("javax.servlet.context.tempdir");
	String filePath = file.getAbsolutePath();
	String fileName = "Individual_Report_Poll_"+nowDate+"_"+nowTime+".xls";
	
	WritableWorkbook workbook = null;
	WritableSheet sheet = null;
	Label label = null;
	jxl.write.Number number = null;
	
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
             
        
        sheet.setRowView(0, 500);
        sheet.mergeCells(0, 0, 6, 0);
 		label = new Label(0, 0, "설문통계" , titleFormat);
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
       
        sheet.setColumnView(0, 30);	//첫번째 열사이즈 30
        sheet.setColumnView(1, 12);	//두번째 열사이즈 25
        
        for(int c=0;c<exampleAllCount;c++){
        	sheet.setColumnView(c+2, 25);	//첫번째 열사이즈 15
        }
        List<PollQuestion> pollQuestionList =  service.selectQuestionByPollID(pollID);
        
        int pollQuestionSize = pollQuestionList.size();		
        int matrixSize = 0;
        
//		if(isAdmin.equals("Y")){ // 이메일과 고객명을 관리자만 보게 할 경우 활성화 , 모두 보게 할 경우 주석처리			
			sheet.mergeCells(0,6,0,7);
	        label = new Label(0,6, "이메일", pollNumberFormat);
	        sheet.addCell(label);
	        sheet.mergeCells(1,6,1,7);
	        label = new Label(1,6, "고객명", pollNumberFormat);
	        sheet.addCell(label);
/* 		}else{
			matrixSize = -2;
		}*/
        
 		//일단 전체 설문문항으로 루핑..
 		//System.out.println("#pollQuestionList.size() : "+pollQuestionList.size());
        int[] matrixCount = new int[pollQuestionList.size()+1];
        int tempMatrixCount=1;
        int[] startCellPoint = new int[pollQuestionList.size()+1];// 각 문항별 시작 셀 포인트
        int tempStartCellPoint = 0;
        for(int i=0;i<pollQuestionList.size();i++){
        	PollQuestion pollQuestion = pollQuestionList.get(i);
        	if(pollQuestion.getQuestionType().equals("1")){
        		List<MassMailStatisticPoll> massMailStatisticPollListNormal = service.selectPollStatisticByQuestionID(massmailID,scheduleID,pollID,pollQuestion.getQuestionID());
        		sheet.mergeCells(i+2+matrixSize,6,i+matrixSize+massMailStatisticPollListNormal.size()+1,6);
	        	label = new Label(i+2+matrixSize,6, Integer.toString(pollQuestion.getQuestionNo())+". "+pollQuestion.getQuestionText(), pollNumberFormat);
	        	sheet.addCell(label); 
	        	tempMatrixCount+=pollQuestionList.size();
	        	tempStartCellPoint = i+2+matrixSize; // 각 문제별 첫 번째 보기 셀 시작 위치
	        	String exampleDesc ="";
	        	for(int j=0;j<massMailStatisticPollListNormal.size();j++){
        			MassMailStatisticPoll massMailStatisticPoll = massMailStatisticPollListNormal.get(j);
        			exampleDesc = massMailStatisticPoll.getExampleDesc();
        			label = new Label(i+matrixSize+j+2,7, "("+(j+1)+")"+exampleDesc , pollNumberFormat);
        			sheet.addCell(label);
        		}
	        	matrixSize+=massMailStatisticPollListNormal.size()-1;
	        	matrixCount[pollQuestion.getQuestionID()] = tempMatrixCount;
	        	startCellPoint[pollQuestion.getQuestionID()] = tempStartCellPoint;
	        }else{
        		List<MassMailStatisticPoll> massMailStatisticPollAnswerList = service.selectPollAnswerMatrixXY(massmailID,scheduleID,pollID,pollQuestion.getQuestionID());
        		sheet.mergeCells(i+2+matrixSize,6,i+matrixSize+massMailStatisticPollAnswerList.size()+1,6);
        		label = new Label(i+2+matrixSize,6, Integer.toString(pollQuestion.getQuestionNo())+". "+pollQuestion.getQuestionText(), pollNumberFormat);
        		sheet.addCell(label);
        		tempMatrixCount +=massMailStatisticPollAnswerList.size();
        		tempStartCellPoint = i+2+matrixSize; // 각 문제별 첫 번째 보기 셀 시작 위치
        		//System.out.println("##"+pollQuestion.getQuestionID() +" : "+matrixCount[pollQuestion.getQuestionID()]);
        		for(int x=0;x<massMailStatisticPollAnswerList.size();x++){
        			MassMailStatisticPoll massMailStatisticPollX = massMailStatisticPollAnswerList.get(x);
        			label = new Label(i+matrixSize+x+2,7, "("+massMailStatisticPollX.getExampleID()+")"+massMailStatisticPollX.getExampleDesc(), pollNumberFormat);
        			sheet.addCell(label);        			
        		}
        		matrixSize+=massMailStatisticPollAnswerList.size()-1;
        		matrixCount[pollQuestion.getQuestionID()] = tempMatrixCount;
        		startCellPoint[pollQuestion.getQuestionID()] = tempStartCellPoint;
        	}
        }
        for(int i=0;i<pollQuestionList.size();i++){
        	//System.out.println("##"+i+" : "+matrixCount[i]);
        }
        List<MassMailStatisticPoll> massMailIndividualStatisticPoll = service.selectPollIndividualStatistic(massmailID, scheduleID);
        int row = 7;
        int column = 0;
        String tempEmail="";
        String tempExampleDesc="";
        int tempQuestionID=-1;
        int answerStartPoint = 0;
        
        for(int i=0;i<massMailIndividualStatisticPoll.size();i++){
        	MassMailStatisticPoll massMailStatisticPoll = massMailIndividualStatisticPoll.get(i);
        	if(massMailStatisticPoll.getQuestionType().equals("1")){
        		
        		if(i == 1){
					//System.out.println("## "+massMailStatisticPoll.getQuestionID()+" : "+matrixCount[massMailStatisticPoll.getQuestionID()]);
					//System.out.println("### "+massMailStatisticPoll.getQuestionID()+" : "+massMailStatisticPoll.getExampleType());
				}
        		if(massMailStatisticPoll.getEmail().equals(tempEmail)){
        			
        			int tempanswerStartPoint = 0;
        			
        			if(massMailStatisticPoll.getQuestionID() == tempQuestionID){
        				if(massMailStatisticPoll.getExampleType().equals("1")){
        					if(massMailStatisticPoll.getResponseText() == null){
        						massMailStatisticPoll.setResponseText("");
        					}
        					tempExampleDesc = massMailStatisticPoll.getExampleID()+massMailStatisticPoll.getResponseText();
        				}else{
        					tempExampleDesc = massMailStatisticPoll.getResponseText() +"순위";
        				}
        				
        				answerStartPoint = startCellPoint[massMailStatisticPoll.getQuestionID()]+massMailStatisticPoll.getExampleID()-1;
        				label = new Label(answerStartPoint,row, tempExampleDesc, pollExampleFormat);
	        			sheet.addCell(label);	        			
        			}else{    				
        				
        				column = matrixCount[massMailStatisticPoll.getQuestionID()];//massMailStatisticPoll.getQuestionID()+1+matrixCount[massMailStatisticPoll.getQuestionID()];
        				if(massMailStatisticPoll.getExampleType().equals("1") || massMailStatisticPoll.getExampleType().equals("5")){
        					if(massMailStatisticPoll.getResponseText() == null){
        						massMailStatisticPoll.setResponseText("");
        					}
    	        			answerStartPoint = startCellPoint[massMailStatisticPoll.getQuestionID()]+massMailStatisticPoll.getExampleID()-1;
    	        			
        		        	label = new Label(answerStartPoint,row, "O"+" "+massMailStatisticPoll.getResponseText(), pollExampleFormat);
    	        			sheet.addCell(label);
        				}else if(massMailStatisticPoll.getExampleType().equals("2") || massMailStatisticPoll.getExampleType().equals("4")){
        					answerStartPoint = startCellPoint[massMailStatisticPoll.getQuestionID()]+massMailStatisticPoll.getExampleID()-1;
    	        			
    	        			label = new Label(answerStartPoint,row, massMailStatisticPoll.getResponseText(), pollExampleFormat);
    	        			sheet.addCell(label);
    	        			tempExampleDesc="";
    	        		}else{
    	        			answerStartPoint = startCellPoint[massMailStatisticPoll.getQuestionID()]+massMailStatisticPoll.getExampleID()-1;
    	        			tempExampleDesc= massMailStatisticPoll.getResponseText() +"순위";
    	        			label = new Label(answerStartPoint+tempanswerStartPoint,row, tempExampleDesc, pollExampleFormat);
    	        			sheet.addCell(label);
    	        			tempanswerStartPoint++;
    	        		}
        			}
        			
        			
        		}else{//1번문제 출력 위치
        			tempEmail = massMailStatisticPoll.getEmail();
	        		row++;	        		
//	        		if(isAdmin.equals("Y")){ // 이메일과 고객명을 관리자만 보게 할 경우 활성화 , 모두 보게 할 경우 주석처리
		        		label = new Label(0,row, massMailStatisticPoll.getEmail(), pollExampleFormat);
		        		sheet.addCell(label);
		        		label = new Label(1,row, massMailStatisticPoll.getName(), pollExampleFormat);
		        		sheet.addCell(label);
//	        		}
	        		if(massMailStatisticPoll.getExampleType().equals("1") || massMailStatisticPoll.getExampleType().equals("5")){
	        			if(massMailStatisticPoll.getResponseText() == null){
    						massMailStatisticPoll.setResponseText("");
    					}
	        			answerStartPoint = startCellPoint[massMailStatisticPoll.getQuestionID()]+massMailStatisticPoll.getExampleID()-1;
	        			
    		        	label = new Label(answerStartPoint,row, "O"+" "+massMailStatisticPoll.getResponseText(), pollExampleFormat);
	        			sheet.addCell(label);
	        		}else if(massMailStatisticPoll.getExampleType().equals("2") || massMailStatisticPoll.getExampleType().equals("4")){
	        			answerStartPoint = startCellPoint[massMailStatisticPoll.getQuestionID()]+massMailStatisticPoll.getExampleID()-1;
	        				        			
	        			label = new Label(answerStartPoint,row, massMailStatisticPoll.getResponseText(), pollExampleFormat);
	        			sheet.addCell(label);
	        			tempExampleDesc="";
	        		}else{
	        			answerStartPoint = startCellPoint[massMailStatisticPoll.getQuestionID()]+massMailStatisticPoll.getExampleID()-1;
	        			
						for(int k=0;k<massMailIndividualStatisticPoll.size();k++){
							MassMailStatisticPoll massMailStatisticPoll2 = massMailIndividualStatisticPoll.get(k);
	        				label = new Label(answerStartPoint+k,row, massMailStatisticPoll2.getResponseText()+"순위", pollExampleFormat);
		        			sheet.addCell(label);
						}	        			
	        		}
        		}
        	}else{
        		if(massMailStatisticPoll.getEmail().equals(tempEmail)){
        			int tempanswerStartPoint = 0;
        			answerStartPoint = startCellPoint[massMailStatisticPoll.getQuestionID()]+massMailStatisticPoll.getExampleID()-1;
        			label = new Label(answerStartPoint+tempanswerStartPoint,row, massMailStatisticPoll.getExampleDesc_responseText(), pollExampleFormat);        			
        			sheet.addCell(label);
        			tempanswerStartPoint++;
        		}else{
        			tempEmail = massMailStatisticPoll.getEmail();
        			row++;	        		
	        		answerStartPoint = startCellPoint[massMailStatisticPoll.getQuestionID()]+massMailStatisticPoll.getExampleID()-1;
	        		label = new Label(0,row, massMailStatisticPoll.getEmail(), pollExampleFormat);
	        		sheet.addCell(label);
	        		label = new Label(1,row, massMailStatisticPoll.getName(), pollExampleFormat);
	        		sheet.addCell(label);
	        		label = new Label(answerStartPoint,row, massMailStatisticPoll.getExampleDesc_responseText(), pollExampleFormat);
        			sheet.addCell(label);
        		}
        	}
        	tempQuestionID= massMailStatisticPoll.getQuestionID();
        	
        }
        workbook.write();
        workbook.close();  
        
		
	}catch(Exception e){
		System.out.println(e.toString());
	}finally{
		  if(workbook != null) { try { workbook.close(); } catch(Exception e) {} }
	}
%>