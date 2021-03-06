package web.admin.rejectsms.control;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;
import web.admin.rejectsms.service.RejectSMSService;
import web.common.util.FileUploadUtil;
import web.common.util.StringUtil;



/**
 * <p>사용자가 업로드한 수신거부파일을 파싱하여 수신거부테이블에 넣어준다.
 * @author 김유근
 *
 */
public class RejectSMSCSVThread extends Thread{

	private Logger logger = Logger.getLogger("TM");
	private RejectSMSService rejectSMSService = null;	
	private String userID;
	private String groupID;
	private String filePath;
	private String fileType;
	
	
	
	public RejectSMSCSVThread(RejectSMSService rejectSMSService, String userID, String groupID, String filePath,String fileType){
		this.rejectSMSService = rejectSMSService;		
		this.userID = userID;
		this.groupID = groupID;
		this.filePath = filePath;
		this.fileType = fileType;
	}
	
	
	public void run(){
		int c= 0;
		
		if(fileType.equals("excel")){
			c=insertParsingExcelImport();
		}
		else{
			c=insertParsingCSVImport();
		}
		if(c>0){
		//	logger.info("수신거부파일 등록완료");
		}else{
			logger.error("수신거부파일 등록실패!!!");
		}
		
		//파일삭제
		FileUploadUtil.deleteFile(filePath);
	}
	
	
	
	/**
	 * <p>CSV,TXT 입력
	 * @return
	 */
	private int  insertParsingCSVImport(){
		
		
		
		int insertCount = 0;				
		BufferedReader br = null;

		int count = 0;
		
		// 한 인서트 구문에서 동시에 인서트 시킬 레코드의 갯수
		final int LOOPCOUNT = 1000; //batch divide 갯수  .
		
		//sql문 생성
		String insertSQL = "INSERT INTO tm_masssms_reject(receiverPhone, userID,groupID,registDate) "
			 + " VALUES(?,'"+userID+"','"+groupID+"',NOW())";
		
		int line = 1;		//줄라인 	
		try{
			br = new BufferedReader(new FileReader(filePath));
			String strLine = "";
			
			List<Object> paramList = new ArrayList<Object>();
			List<String> params = null;
			
			int csvlineCount = getCountCSVFile(filePath);  //파일의 총라인수를 구한다. 
			while((strLine=br.readLine())!=null){
				
				if(!strLine.equals("")){
					params = new ArrayList<String>();
					int temp = strLine.indexOf(",");
					String str = null;
					if(temp != -1)
						str = strLine.substring(0,temp);
					else
						str = strLine;
					str = str.replace("-", "");
			
					
					//맞는 폰번호 형식이 맞는 거만 입력 
					if(StringUtil.isPhoneNumber(str)){
						params.add(str);
						paramList.add(params);
					}
				
				}
				insertCount ++;
				count ++;				
			
				if( count % LOOPCOUNT== 0 || line==csvlineCount){		//LOOPCOUNT씩 만큼 끊거나, 마지막라인이라면 
					rejectSMSService.insertCSVImport(insertSQL,paramList);	//tm_csvimport에 인서트한다. 
					//paramList = new ArrayList();
					paramList.clear();
					count = 0;				
				}
			
			line++;
				
			}
			
			

		}catch(Exception e){
			insertCount = 0;
			logger.error(e);
		}finally{
			try{br.close();}catch(IOException e1){}
		}
		return insertCount;
		
	}
	
	
	/**
	 * <P>엑셀입력
	 * @return
	 */
	private int  insertParsingExcelImport(){
		
		
		int insertCount = 0;		
		int count = 0;
		
		// 한 인서트 구문에서 동시에 인서트 시킬 레코드의 갯수
		final int LOOPCOUNT = 1000; //batch divide 갯수  .
		
		//sql문 생성
		String insertSQL = "INSERT INTO tm_masssms_reject(receiverPhone, userID,groupID,registDate) "
			 + " VALUES(?,'"+userID+"','"+groupID+"',NOW())";
		
		int line = 0;		//줄라인 	
		
		List<Object> paramList = new ArrayList<Object>();
		List<String> params = null;
		int lineCount = 0;	//총라인수 
		try{			
			Workbook workbook = Workbook.getWorkbook(new File(filePath));
			Sheet sheet = workbook.getSheet(0);	
			lineCount = sheet.getRows();
			
			while(line<=lineCount){
				
					params = new ArrayList<String>();
					
					try {						
						Cell[] cell = sheet.getRow(line);
						for(int l = 0;l<=cell.length;l++){
							
							if(!(cell[l].getContents().equals("")) && (StringUtil.isPhoneNumber(cell[l].getContents().replace("-", "")))){
								params.add(cell[l].getContents().replace("-", ""));
								paramList.add(params);
								insertCount ++;
							}
						}
						
					} catch (Exception e) 
					{	
						insertCount = 0;
						logger.error(e);
					}
					
					count ++;
					
					if( count % LOOPCOUNT== 0 || line==lineCount){		//LOOPCOUNT씩 만큼 끊어서 넣자 
						
						rejectSMSService.insertCSVImport(insertSQL,paramList);	//tm_csvimport에 인서트한다. 
						//paramList = new ArrayList();
						paramList.clear();
						count = 0;						
					}
				
				
				line++;
				

			}// end while
	
		}catch(Exception e){		
			insertCount = 0;
			logger.error(e);
			//e.printStackTrace( System.out);
		}finally{
			paramList = null;
			
		}		
		
		
		return insertCount;
	}
	
	
	/**
	 * <p>CSV파일의 라인수를 읽어들여 리턴한다. 
	 * @param filePath
	 * @return
	 */
	private int getCountCSVFile(String filePath){		
		int count = 0;		
		BufferedReader br = null;		
		try{			
			br = new BufferedReader(new FileReader(filePath));			
			while((br.readLine())!=null){				
				count++;
			}
		}catch(IOException e){
			count = 0;
			logger.error(e);
		}finally{
			try{br.close();}catch(IOException e1){}
		}		
		return count;  
	}
	
}
