package web.target.targetlist.control;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import web.common.util.Constant;
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.target.targetlist.model.*;
import web.target.targetlist.service.*;


/**
 * <p>대상자등록시 CSV파일일 경우에 수행되는 스레드. CSV파일을 파싱해서 onetoone_target에 테이블에 넣고 
 * <p>csvimport에 넣는다. 그리고 나서 targeting에서 state=1 (등록) 으로 처리한다. 
 * @author 김유근 
 * @date 2008-03-20
 *
 */ 

public class TargetListFileThread extends Thread{
	
	private Logger logger = Logger.getLogger("TM");	
	private TargetListService targetListService = null;
	private int targetID = 0;
	private String filePath = "";
	private String selectSQL = "";
	private List<OnetooneTarget> onetooneTargetList = null;
	private String fileType="";
	private String directText="";
	private String uploadKey="";
	private String targetTable = "";
	private final int LOOPCOUNT = 500;
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";
	
	/*
	 * 원투원의 기본적인 세팅값이 아래의 원투원값은 고정값이며 수정해서는 안된다. (1~4)
	 * 단, 고객사의 요청으로 변경되면 아래의 값도 고정하고 tm_onetoone에 값도 역시 변경되어야한다. 항상 두개다 일치해야 한다. 
	 */
	

	//생성자 
	public TargetListFileThread(TargetListService targetListService, String targetTable,  String uploadKey, int targetID, String filePath, List<OnetooneTarget> onetooneTargetList, String fileType, String directText ){
		this.targetListService = targetListService;
		this.targetID = targetID;
		this.filePath = filePath;
		this.onetooneTargetList = onetooneTargetList;
		this.fileType = fileType;
		this.directText = directText;
		this.uploadKey = uploadKey;
		this.targetTable = targetTable;
	}
	
	public void run(){	
		
		if(targetTable==null || targetTable.equals("")){
			targetTable = getFileTableName();
		}


		
		if(targetTable.equals("")){
			logger.error("파일 임포트 테이블을 생성할 수 없습니다.");
			
			return;
		}
		
		//1.시작시간과 테이블명 업데이트 
		if(targetListService.updateTargetingStart(Constant.TARGET_STATE_READY,targetTable, uploadKey, targetID)>0){
			if(targetListService.deleteFileImport(targetID, targetTable)<0){
				targetListService.updateTargetingEnd(Constant.TARGET_STATE_ERROR, 0, this.selectSQL, targetID);
				logger.error("기존에 등록된 파일임포트 데이터를 삭제하지 못했습니다. tableName="+targetTable+", targetID="+targetID);				
				return;
			}
		}
		
		//2. 대상자파일에서 수집처리 시작
		int targetCount = 0;
		//엑셀파일이라면 
		if(fileType.equals("excel")){
			targetCount = insertParsingExcelImport(targetTable);
		//엑셀외에 파일이라면(csv, txt)	
		}else if(fileType.equals("csvtxt")){
			targetCount = insertParsingFileImport(targetTable);
		//직접입력이라면	
		}else if(fileType.equals("direct")){
			targetCount = insertParsingDirectImport(targetTable);
		}else if(fileType.equals("excel2007")){
			targetCount = insertParsingExcel2007Import(targetTable);
		}
		
		String state = "";
		
		int totalCount = 0; //총 대상자 카운트
		//3.결과업데이트 
		if(targetCount>0){	//성공이라면 		
			state = Constant.TARGET_STATE_FINISH;
			totalCount = targetListService.getTargetPreviewListTotalCount("select count(*) from ("+this.selectSQL+") a ");
		}else{
			state = Constant.TARGET_STATE_ERROR;
		}
		
		
		targetListService.updateTargetingEnd(state, totalCount, this.selectSQL, targetID);

	}// end run
	
	

	/**
	 * <p>테이블을 얻어오거나 생성한다.
	 * @return
	 */
	private String getFileTableName(){
		String tableName = "";
		
		 tableName = Constant.FILE_TABLE +"_"+ DateUtils.getYearMonth();			 
		 List<Map<String, Object>> tableNames = targetListService.getFileImportTableIsExist(tableName);
		 //없다면 생성해준다.
		 if(tableNames==null || tableNames.size()==0){
			
			 if(targetListService.createFileImportTable(tableName)<0){
				 tableName = ""; //실패라면 빈테이블 
			 }
		 }
		return tableName;
	}
	

		
	
	
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
		
		String insertSQL = "";
		
		// 한 인서트 구문에서 동시에 인서트 시킬 레코드의 갯수
		//String[] sqlArray = null;
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = "INSERT INTO "+tableName+" (fileImportID, targetID,"; 	//sql문 생성
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
			insertSQL = "INSERT INTO "+tableName+" (targetID,"; //sql문 생성
					
		this.selectSQL = "SELECT ";
		String columnSQL = "";
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			columnSQL += onetooneTargetList.get(i).getFieldName()+",";
		}
		columnSQL = columnSQL.substring(0,columnSQL.length()-1);
		
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = insertSQL + columnSQL + ") VALUES("+tableName+"_SEQ.nextval, "+targetID+",";
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
			insertSQL = insertSQL + columnSQL + ") VALUES("+targetID+",";
		
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
					if(!strLine.equals(""))
					{
						strLineArray = strLine.split(",");
						params = new ArrayList();
					
						try {
							for(int i=0; i < onetooneTargetList.size(); i ++) 
							{
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
	 * <p>파일을 파싱해서 tm_fileimport에 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int  insertParsingExcelImport(String tableName){
		
		
		int insertCount = 0;		
		
		BufferedReader br = null;
		int count = 0;
		
		String insertSQL = "";
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = "INSERT INTO "+tableName+" (fileImportID, targetID,"; 	//sql문 생성
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
			insertSQL = "INSERT INTO "+tableName+" (targetID,"; //sql문 생성
		
	
		this.selectSQL = "SELECT ";
		String columnSQL = "";
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			columnSQL += onetooneTargetList.get(i).getFieldName()+",";
		}
		columnSQL = columnSQL.substring(0,columnSQL.length()-1);
		
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = insertSQL + columnSQL + ") VALUES("+tableName+"_SEQ.nextval, "+targetID+",";
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
			insertSQL = insertSQL + columnSQL + ") VALUES("+targetID+",";
		
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
			int loop_count = 0;
			int null_count =0;
			int head_count =0;
			while(line<lineCount){
				if(line==0){	
					head_count = sheet.getRow(line).length;
				}else{
					params = new ArrayList();
					
					try {						
						Cell[] cell = sheet.getRow(line);
						
						for(int i=0,l=0; i < head_count; i++) {		
							 
							if(l<onetooneTargetList.size() && i==onetooneTargetList.get(l).getCsvColumnPos()-1)
							{	if(i<cell.length)
								{
									if(cell[i].getContents().equals(""))
										null_count++;
									params.add(cell[i].getContents());
									l++;
								}
								else
								{
									null_count++;
									params.add("");
									l++;
								}
								
							}
							loop_count++;							
						}
						
						if(loop_count != 0  && loop_count>null_count)
						{
							paramList.add(params);
							insertCount++;
						}
						loop_count = 0;
						null_count = 0;
						
					} catch (Exception e) {
						insertCount = 0;
						logger.error(e);
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
	@SuppressWarnings("unchecked")
	private int  insertParsingExcel2007Import(String tableName){
		
		int insertCount = 0;		
		
		BufferedReader br = null;
		int count = 0;
		
		String insertSQL = "";
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = "INSERT INTO "+tableName+" (fileImportID, targetID,"; 	//sql문 생성
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
			insertSQL = "INSERT INTO "+tableName+" (targetID,"; //sql문 생성
		
		this.selectSQL = "SELECT ";
		String columnSQL = "";
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			columnSQL += onetooneTargetList.get(i).getFieldName()+",";
		}
		columnSQL = columnSQL.substring(0,columnSQL.length()-1);
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = insertSQL + columnSQL + ") VALUES("+tableName+"_SEQ.nextval, "+targetID+",";
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
			insertSQL = insertSQL + columnSQL + ") VALUES("+targetID+",";
			
		this.selectSQL = this.selectSQL + columnSQL +" FROM "+tableName+" WHERE targetID="+targetID;
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			insertSQL += "?,";
		}
		insertSQL = insertSQL.substring(0,insertSQL.length()-1)+")";
		
		int cell_count = 0;
		//int line = 0;		//줄라인 	
		List<Object> paramList = new ArrayList();
		List<String> params = null;
		//int lineCount = 0;	//총라인수 
		try{	
			//File file = new File(filePath);
			//FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook xb= new XSSFWorkbook(filePath);
			XSSFExcelExtractor extractoc = new XSSFExcelExtractor(filePath);
			extractoc.setFormulasNotResults(true);
			extractoc.setIncludeSheetNames(false);
			int head_count = 0;
			//for(int i=0;i<xb.getNumberOfSheets();i++){
			for(Row row: xb.getSheetAt(0))
			{
				if(row.getRowNum() == 0)
				{
					head_count=row.getPhysicalNumberOfCells();
					
				}
				else if(row.getRowNum() > 0)
				{	
					params = new ArrayList();
					int loop_count =0;
					for(int i = 0;i<onetooneTargetList.size();i++)
					{ 
						if(i<row.getLastCellNum() && row.getCell(onetooneTargetList.get(i).getCsvColumnPos()-1)!=null)
						{
							org.apache.poi.ss.usermodel.Cell cell = row.getCell(onetooneTargetList.get(i).getCsvColumnPos()-1);
							switch (cell.getCellType())
   	   		         		{
   	   		         			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING : 
   	   		         				params.add(cell.getRichStringCellValue().getString());
   	   		         			
   	   		         			break;
   	   		         			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC :
	   		         			
   	   		         				if(DateUtil.isCellInternalDateFormatted(cell)){
   	   		         					params.add("" +cell.getDateCellValue());
   	   		         				}else{
   	   		         					params.add("" +cell.getNumericCellValue());
   	   		         				}
   	   		         			
   	   		         			break;
   	   		         			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN :
   	   		         				params.add("" +cell.getBooleanCellValue());
   	   		         			
   	   		         			break;
   	   		         			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA : 
   	   		         				params.add(cell.getCellFormula());
   	   		         			
   	   		         			break;
   	   		         				   	   		        
   	   		         			default:
		   	   		        		params.add("");
		   	   		        		cell_count++;
		   	   		        	
		   	   		      
   	   		         		}
						}else
						{
							params.add("");
   	   		        		cell_count++;							
						}
						loop_count++;
					}
					
					if(params != null && loop_count != 0 && cell_count<loop_count){
						
						paramList.add(params);
						insertCount ++;
						cell_count=0;
					}
					else
					{						
						cell_count=0;
					}
					loop_count=0;
				}
				

				count ++;
				if( count % LOOPCOUNT== 0)
				{		//LOOPCOUNT씩 만큼 끊어서 넣자 
					targetListService.insertFileImport(insertSQL,paramList);	//tm_fileimport에 인서트한다. 
					paramList.clear();
					count = 0;				
				}
			}
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
	@SuppressWarnings("unchecked")
	private int  insertParsingDirectImport(String tableName){
		
		
		int insertCount = 0;		
		
		BufferedReader br = null;
		String[] strLineArray = null;
		int count = 0;
		
		String insertSQL = "";
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = "INSERT INTO "+tableName+" (fileImportID, targetID,"; 	//sql문 생성
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
			insertSQL = "INSERT INTO "+tableName+" (targetID,"; //sql문 생성
			
		this.selectSQL = "SELECT ";
		String columnSQL = "";
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			columnSQL += onetooneTargetList.get(i).getFieldName()+",";
		}
		columnSQL = columnSQL.substring(0,columnSQL.length()-1);
		if(db_type.equals(DB_TYPE_ORACLE))	
			insertSQL = insertSQL + columnSQL + ") VALUES("+tableName+"_SEQ.nextval, "+targetID+",";
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
			insertSQL = insertSQL + columnSQL + ") VALUES("+targetID+",";
			
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
	
}
