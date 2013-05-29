package web.target.targetlist.control;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import web.common.util.Constant;
import web.common.util.PropertiesUtil;
import web.target.targetlist.model.*;
import web.target.targetlist.service.*;


/**
 * <p>대상자등록시 CSV파일일 경우에 수행되는 스레드. CSV파일을 파싱해서 onetoone_target에 테이블에 넣고 
 * <p>csvimport에 넣는다. 그리고 나서 targeting에서 state=1 (등록) 으로 처리한다. 
 * @author 김유근 
 *
 */ 

public class TargetListFileAddThread extends Thread{
	
	private Logger logger = Logger.getLogger("TM");	
	private TargetListService targetListService = null;
	private int targetID = 0;
	private String filePath = "";
	private String selectSQL = "";
	private List<OnetooneTarget> onetooneTargetList = null;
	private String fileType="";
	private String addType="";
	private String directText="";
	private String uploadKey="";
	private String targetTable = "";
	private final int LOOPCOUNT = 500;
	private String colName = "";
	private int csvColumnPos = 0;
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	
	/*
	 * 원투원의 기본적인 세팅값이 아래의 원투원값은 고정값이며 수정해서는 안된다. (1~4)
	 * 단, 고객사의 요청으로 변경되면 아래의 값도 고정하고 tm_onetoone에 값도 역시 변경되어야한다. 항상 두개다 일치해야 한다. 
	 */
	

	//생성자 
	public TargetListFileAddThread(TargetListService targetListService, String targetTable,  String uploadKey, int targetID, String filePath, List<OnetooneTarget> onetooneTargetList, String fileType, String directText, String addType ){
		this.targetListService = targetListService;
		this.targetID = targetID;
		this.filePath = filePath;
		this.onetooneTargetList = onetooneTargetList;
		this.fileType = fileType;
		this.directText = directText;
		this.uploadKey = uploadKey;
		this.targetTable = targetTable;
		this.addType = addType;
	}
	
	public void run(){	
		
		if(targetTable==null || targetTable.equals("")){
			logger.error("파일 임포트 테이블을 찾을 수 없습니다.");
			return;
		}


		//1.시작 상태 업데이트 
		if(targetListService.updateTargetingAddStart(Constant.TARGET_STATE_READY,targetID) <= 0){
			logger.error("state 값 변경에 실패 하였습니다. targetID="+targetID);	
			return;
		}
		//2. 대상자파일에서 수집처리 시작
		int targetCount = 0;
		String state = "";
		
		//추가일경우
		if(addType.equals("1")){
			//엑셀파일이라면 
			if(fileType.equals("excel")){
				targetCount = insertParsingExcelImport(targetTable);
			//엑셀외에 파일이라면(csv, txt)	
			}else if(fileType.equals("csvtxt")){
				targetCount = insertParsingFileImport(targetTable);
			//직접입력이라면	
			}else if(fileType.equals("direct")){
				targetCount = insertParsingDirectImport(targetTable);
			}
		//삭제일경우
		}else{
			//이메일 기준으로 삭제 해야 하므로, 기존 대상자의 이메일 컬럼 정보를 가져온다 
			colName = targetListService.getEmailFieldName(targetID);
			
			System.out.println("email field name(colName) : " + colName);
			if(colName!=null && !(colName.equals("")))
			{
				String[] temp = colName.split(",");
				colName = temp[0];
				csvColumnPos = Integer.parseInt(temp[1]);
				//엑셀파일이라면 
				if(fileType.equals("excel")){
					targetCount = deleteParsingExcelImport(targetTable);
				//엑셀외에 파일이라면(csv, txt)	
				}else if(fileType.equals("csvtxt")){
					targetCount = deleteParsingFileImport(targetTable);
					//직접입력이라면	
				}else if(fileType.equals("direct")){
					targetCount = deleteParsingDirectImport(targetTable);
				}
				
				
			}
			else
			{				
				logger.error("email field name(colName) is empty");
				state = Constant.TARGET_STATE_ERROR;
			}
			
			
			
		}
		//3.결과업데이트 
		if(targetCount>0){	//성공이라면 		
			state = Constant.TARGET_STATE_FINISH;
		}else{
			state = Constant.TARGET_STATE_ERROR;
		}
		
		
		
		
		targetListService.updateTargetingAddEnd(targetTable, state, targetID);

	}// end run
	
	/**
	 * <p>파일을 파싱해서 tm_fileimport에 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int  insertParsingFileImport(String tableName){
		
		
		int insertCount = 0;		
		
		BufferedReader br = null;
		String[] strLineArray = null;
		int count = 0;
		
		// 한 인서트 구문에서 동시에 인서트 시킬 레코드의 갯수
		//String[] sqlArray = null;
		
		String insertSQL = "";
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = "INSERT INTO "+tableName+" (fileImportID, targetID,"; 	//sql문 생성
		else if(db_type.equals(DB_TYPE_MYSQL))
			insertSQL = "INSERT INTO "+tableName+" (addYN,targetID,"; //sql문 생성
		this.selectSQL = "SELECT ";
		String columnSQL = "";
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			columnSQL += onetooneTargetList.get(i).getFieldName()+",";
		}
		columnSQL = columnSQL.substring(0,columnSQL.length()-1);
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = insertSQL + columnSQL + ") VALUES("+tableName+"_SEQ.nextval, "+targetID+",";
		else if(db_type.equals(DB_TYPE_MYSQL))
			insertSQL = insertSQL + columnSQL + ") VALUES(\"Y\","+targetID+",";
		this.selectSQL = this.selectSQL + columnSQL +" FROM "+tableName+" WHERE targetID="+targetID;
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			insertSQL += "?,";
		}
		insertSQL = insertSQL.substring(0,insertSQL.length()-1)+")";
		
		
		int line = 1;		//줄라인 	
		List<Object> paramList = new ArrayList();
		List<String> params = null;
		try{			
			br = new BufferedReader(new FileReader(filePath));
			String strLine = "";
			
			//int csvlineCount = getCountCSVFile(filePath);  //파일의 총라인수를 구한다. 
		
			while((strLine=br.readLine())!=null){
				if(line==1){	
					//첫번째 줄은 필드명이므로 무시한다. 
				}else{				
					strLineArray = strLine.split(",");
					params = new ArrayList();
					
					try {
						for(int i=0; i < onetooneTargetList.size(); i ++) {
							if(strLineArray.length>onetooneTargetList.get(i).getCsvColumnPos()-1)
								params.add(strLineArray[onetooneTargetList.get(i).getCsvColumnPos()-1]);
							else
								params.add("");
						}
						paramList.add(params);
						insertCount ++;
					} catch (Exception e) {
						insertCount = 0;
						logger.error(e);
						e.printStackTrace( System.out);
					}

					count ++;
				
					if( count % LOOPCOUNT== 0){		//LOOPCOUNT씩 만큼 끊어서 넣어라 
						targetListService.insertFileImport(insertSQL,paramList);	//tm_fileimport에 인서트한다. 
						//paramList = new ArrayList();
						paramList.clear();
						count = 0;				
					}
					
				}//end else 
				line++;
				
			}//end while
			
			//마지막 남은 것을 인서트 
			targetListService.insertFileImport(insertSQL,paramList);	//tm_fileimport에 인서트한다. 
			
			
		}catch(Exception e){		
			insertCount = 0;
			logger.error(e);
			e.printStackTrace( System.out);
		}finally{
			paramList.clear();
			paramList = null;
			if(br!=null) try{br.close();}catch(IOException e1){}
		}		
		
	
		return insertCount;
	}
	
	/**
	 * <p>파일을 파싱해서 tm_fileimport에 DELETE 한다. 
	 * @param onetooneTarget
	 * @return
	 */

	@SuppressWarnings({ "unchecked" })
	private int  deleteParsingFileImport(String tableName){
		int deleteCount = 0;	
		
		BufferedReader br = null;
		String[] strLineArray = null;
		int count = 0;
	
		//sql문 생성
		String deleteSQL = "DELETE FROM "+tableName +" WHERE targetID="+targetID+" AND "+colName+"=?";
		
		int line = 1;		//줄라인 	
		List<Object> paramList = new ArrayList();
		List<String> params = null;
		try{			
			br = new BufferedReader(new FileReader(filePath));
			String strLine = "";
		
			while((strLine=br.readLine())!=null){
				if(line==1){	
					//첫번째 줄은 필드명이므로 무시한다. 
				}else{			
					try {
						strLineArray = strLine.split(",");
						params = new ArrayList(); 
						params.add(strLineArray[csvColumnPos-1]);
						paramList.add(params);
						deleteCount ++;
					} catch (Exception e) {
					}
					count ++;
				
					if( count % LOOPCOUNT== 0){		//LOOPCOUNT씩 만큼 끊어서 넣어라 
						targetListService.insertFileImport(deleteSQL,paramList);	//tm_fileimport에 인서트한다. 
						//paramList = new ArrayList();
						paramList.clear();
						count = 0;				
					}
					
				}//end else 
				line++;
				
			}//end while
			
			//마지막 남은 것을 인서트 
			targetListService.insertFileImport(deleteSQL,paramList);	//tm_fileimport에 인서트한다. 
			
			
		}catch(Exception e){		
			deleteCount = 0;
			logger.error(e);
			e.printStackTrace( System.out);
		}finally{
			paramList.clear();
			paramList = null;
			if(br!=null) try{br.close();}catch(IOException e1){}
		}		
		
		return deleteCount;
	}
		
	/**
	 * <p>파일을 파싱해서 tm_fileimport에 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int  insertParsingExcelImport(String tableName){
		
		
		int insertCount = 0;		
		int count = 0;
		BufferedReader br = null;
		
		String insertSQL = "";
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = "INSERT INTO "+tableName+" (fileImportID, targetID,"; 	//sql문 생성
		else if(db_type.equals(DB_TYPE_MYSQL))
			insertSQL = "INSERT INTO "+tableName+" (addYN, targetID,"; //sql문 생성
		this.selectSQL = "SELECT ";
		String columnSQL = "";
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			columnSQL += onetooneTargetList.get(i).getFieldName()+",";
		}
		columnSQL = columnSQL.substring(0,columnSQL.length()-1);
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = insertSQL + columnSQL + ") VALUES("+tableName+"_SEQ.nextval, "+targetID+",";
		else if(db_type.equals(DB_TYPE_MYSQL))
			insertSQL = insertSQL + columnSQL + ") VALUES(\"Y\","+targetID+",";
		this.selectSQL = this.selectSQL + columnSQL +" FROM "+tableName+" WHERE targetID="+targetID;
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			insertSQL += "?,";
		}
		insertSQL = insertSQL.substring(0,insertSQL.length()-1)+")";
		
		
		int line = 0;		//줄라인 	
		List<Object> paramList = new ArrayList();
		List<String> params = null;
		int lineCount = 0;	//총라인수 
		try{			
			Workbook workbook = Workbook.getWorkbook(new File(filePath));
			Sheet sheet = workbook.getSheet(0);	
			lineCount = sheet.getRows();
		
			while(line<lineCount){
				if(line==0){	
					//첫번째 줄은 필드명이므로 무시한다.
				}else{
					params = new ArrayList();
					
					try {						
						Cell[] cell = sheet.getRow(line);
						for(int i=0; i < sheet.getColumns(); i ++) {							
						//	System.out.println("line#"+line+"#====="+cell[i].getContents());
							params.add(cell[i].getContents());
						}
						paramList.add(params);
						insertCount ++;
					} catch (Exception e) {
					}
					
					count ++;
					
					if( count % LOOPCOUNT== 0){		//LOOPCOUNT씩 만큼 끊어서 넣자 
						targetListService.insertFileImport(insertSQL,paramList);	//tm_fileimport에 인서트한다. 
						paramList.clear();
						count = 0;				
					}
				}//end else
				
				line++;

			}// end while
			
			//마지막 남은 것을 인서트 하자. 
			targetListService.insertFileImport(insertSQL,paramList);	//tm_fileimport에 인서트한다. 
			
			
	
		}catch(Exception e){		
			insertCount = 0;
			logger.error(e);
			//e.printStackTrace( System.out);
		}finally{
			paramList.clear();
			paramList = null;
			if(br!=null) try{br.close();}catch(IOException e1){}
		}		
		
		
		return insertCount;
	}
	
	/**
	 * <p>파일을 파싱해서 tm_fileimport에 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	private int  deleteParsingExcelImport(String tableName){
		
		
		int deleteCount = 0;		
		int count = 0;
		BufferedReader br = null;
		
	
		//sql문 생성
		String deleteSQL = "DELETE FROM "+tableName +" WHERE targetID="+targetID+" AND "+colName+"=?";
		
		int line = 0;		//줄라인 	
		List<Object> paramList = new ArrayList();
		List<String> params = null;
		int lineCount = 0;	//총라인수 
		try{			
			Workbook workbook = Workbook.getWorkbook(new File(filePath));
			Sheet sheet = workbook.getSheet(0);	
			lineCount = sheet.getRows();
		
			while(line<lineCount){
				if(line==0){	
					//첫번째 줄은 필드명이므로 무시한다.
				}else{
					params = new ArrayList();
					
					try {						
						Cell[] cell = sheet.getRow(line);
						//for(int i=0; i < sheet.getColumns(); i ++) {							
						//	System.out.println("line#"+line+"#====="+cell[i].getContents());
						params.add(cell[csvColumnPos-1].getContents());
						//}
						paramList.add(params);
						deleteCount ++;
					} catch (Exception e) {
					}
					
					count ++;
					
					if( count % LOOPCOUNT== 0){		//LOOPCOUNT씩 만큼 끊어서 넣자 
						targetListService.insertFileImport(deleteSQL,paramList);	//tm_fileimport에 인서트한다. 
						paramList.clear();
						count = 0;				
					}
				}//end else
				
				line++;

			}// end while
			
			//마지막 남은 것을 인서트 하자. 
			targetListService.insertFileImport(deleteSQL,paramList);	//tm_fileimport에 인서트한다. 
			
			
	
		}catch(Exception e){		
			deleteCount = 0;
			logger.error(e);
			//e.printStackTrace( System.out);
		}finally{
			paramList.clear();
			paramList = null;
			if(br!=null) try{br.close();}catch(IOException e1){}
		}		
		
		
		return deleteCount;
	}
	
	
	/**
	 * <p>파일을 파싱해서 tm_fileimport에 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int  insertParsingDirectImport(String tableName){
		
		int insertCount = 0;		
		BufferedReader br = null;
		String[] strLineArray = null;
		int count = 0;
		
	
		String insertSQL = "";
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = "INSERT INTO "+tableName+" (fileImportID, targetID,"; 	//sql문 생성
		else if(db_type.equals(DB_TYPE_MYSQL))
			insertSQL = "INSERT INTO "+tableName+" (addYN, targetID,"; //sql문 생성
		this.selectSQL = "SELECT ";
		String columnSQL = "";
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			columnSQL += onetooneTargetList.get(i).getFieldName()+",";
		}
		columnSQL = columnSQL.substring(0,columnSQL.length()-1);
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = insertSQL + columnSQL + ") VALUES("+tableName+"_SEQ.nextval, "+targetID+",";
		else if(db_type.equals(DB_TYPE_MYSQL))
			insertSQL = insertSQL + columnSQL + ") VALUES(\"Y\","+targetID+",";
		this.selectSQL = this.selectSQL + columnSQL +" FROM "+tableName+" WHERE targetID="+targetID;
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			insertSQL += "?,";
		}
		insertSQL = insertSQL.substring(0,insertSQL.length()-1)+")";
		
		
		int line = 1;		//줄라인 	
		List<Object> paramList = new ArrayList();
		List<String> params = null;
		try{			
			br = new BufferedReader(new StringReader(directText));
			String strLine = "";		
			
		
			while((strLine=br.readLine())!=null){
				if(line==1){	
					//첫번째 줄은 필드명이므로 무시한다. 
				}else{		
					while(strLine.indexOf(",,")!=-1)
					{
						strLine = strLine.replaceAll(",,", ", ,"); 
					}
					
					if(strLine.endsWith(","))
					{
						strLine = strLine + " ";
					}
					if(strLine.startsWith(","))
					{
						strLine = " " + strLine;
					}
					strLineArray = strLine.split(",");
					params = new ArrayList();
					
					try {
						for(int i=0; i < onetooneTargetList.size(); i ++) {
							params.add(strLineArray[onetooneTargetList.get(i).getCsvColumnPos()-1]);
						}
						paramList.add(params);
						insertCount ++;
					} catch (Exception e) {
						insertCount = 0;
						logger.error(e);
						e.printStackTrace( System.out);
					}

					count ++;
					
					if( count % LOOPCOUNT== 0){		//LOOPCOUNT씩 만큼 끊어라 넣아라.
						targetListService.insertFileImport(insertSQL,paramList);	//tm_fileimport에 인서트한다. 
						paramList.clear();
						count = 0;				
					}
					
				}//end else 
				line++;
				
			}//end while
			
			targetListService.insertFileImport(insertSQL,paramList);	//tm_fileimport에 인서트한다. 
			
		}catch(Exception e){		
			insertCount = 0;
			logger.error(e);
			e.printStackTrace( System.out);
		}finally{
			paramList.clear();
			paramList = null;
			if(br!=null) try{br.close();}catch(IOException e1){}
		}		
		
		
		
		return insertCount;
	}
	
	/**
	 * <p>파일을 파싱해서 tm_fileimport에 DELETE한다. 
	 * @param onetooneTarget
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int  deleteParsingDirectImport(String tableName){
		int deleteCount = 0;
		int count = 0;
		BufferedReader br = null;
		String[] strLineArray = null;
		String deleteSQL = "DELETE FROM "+tableName +" WHERE targetID="+targetID+" AND "+colName+"=?";
		
		int line = 1;		//줄라인 	
		List<Object> paramList = new ArrayList();
		List<String> params = null;
		
		try{	
			br = new BufferedReader(new StringReader(directText));
			String strLine = "";
			
			while((strLine=br.readLine())!=null){
				if(line==1){	
					//첫번째 줄은 필드명이므로 무시한다. 
				}else{				
					strLineArray = strLine.split(",");
					params = new ArrayList();
					
					try {
						params.add(strLineArray[csvColumnPos-1]);
						//for(int i=0; i < onetooneTargetList.size(); i ++) {
						//	System.out.println(onetooneTargetList.get(i).getCsvColumnPos()-1+" strLineArray[onetooneTargetList.get(i).getCsvColumnPos()-1 : "+strLineArray[onetooneTargetList.get(i).getCsvColumnPos()-1]);
						//}
						paramList.add(params);
						deleteCount ++;
					} catch (Exception e) {
					}

					count ++;
					
					if( count % LOOPCOUNT== 0){		//LOOPCOUNT씩 만큼 끊어라 넣아라.
						targetListService.insertFileImport(deleteSQL,paramList);	//tm_fileimport에 인서트한다. 
						paramList.clear();
						count = 0;				
					}
					
				}//end else 
				line++;
				
			}//end while
			
			targetListService.insertFileImport(deleteSQL,paramList);	//tm_fileimport에 인서트한다.
		}catch(Exception e){		
			deleteCount = 0;
			logger.error(e);
			e.printStackTrace( System.out);
		}finally{
			paramList.clear();
			paramList = null;
			if(br!=null) try{br.close();}catch(IOException e1){}
		}		
		return deleteCount;
	}
}
