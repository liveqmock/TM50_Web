package web.common.util;
 

import java.util.HashMap;
import java.util.Properties;
import java.io.File; 
import java.io.FileInputStream;
import java.io.IOException;

 
  
/**
 * <p>쿼리 프로퍼티를 읽어들여 초기화하고 프로퍼티의 값을 불러오는 유틸이다. 
 * @author coolang (김유근) 
 */ 

@SuppressWarnings("unchecked")
public class QueryUtil {
	
	 private static HashMap propMap = null;
		 
	 
	 /**
	  * <p>프로퍼티를 읽어와 초기화한다. 
	  * @param fileName
	  */	  
	 private  static  void  init(String fileName){
		  propMap = new HashMap();
		      
		
		  Properties prop = new Properties();		  
		  String filePath = PropertiesUtil.getStringProperties("configure", "sql_path") + PropertiesUtil.getStringProperties("configure", "db_type") + "/"  + fileName + Constant.PROPERTIES_EXT;
		
		  FileInputStream fis = null;
		  try{						 
			  fis = new FileInputStream(new File(filePath));
			
			  prop.load(fis);
			  
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{		  		
			  try{fis.close();}catch(IOException e){}
		  }
		  propMap.put(fileName,prop);
		 
	 }
	 
	 /**
	  * <p>쿼리 프로퍼티 내용을 읽어와 String 값으로 반환해준다.
	  * <p>첫번째 파라미터는 프로퍼티명(확장자제외), 두번째 파라미터는 프로퍼티파일에 지정된 키값을 의미한다. 
	  * @param propName (쿼리 프로퍼티명)
	  * @param propKey (쿼리 프로퍼티 키값)
	  * @return String 값 
	  */
	 public static String getStringQuery(String propName, String propKey){
		
		 if(propMap == null || propMap.isEmpty() || !propMap.containsKey(propName))
	     init(propName);		
		 Properties prop = (Properties)propMap.get(propName);
		 return prop.getProperty(propKey, "");   //해당키값이 없다면 디폴드로 공백처리...
	 }
	 /*
	  * 넘어온 쿼리를 주어진 갯수만 가져오는 쿼리로 만든다.
	  */
	public static String makeTopCountQuery( String query, String dbType, int rowCount ) throws Exception {

		
		query = query.replaceAll("\n"," ");		
		query = query.replaceAll("\t"," ");
		
		
		if(dbType.equalsIgnoreCase("mysql")) {
			return "SELECT * FROM ("+query+") TEMP LIMIT "+rowCount;
			
		} else if (dbType.equalsIgnoreCase("oracle")) {
			rowCount = rowCount+1;
			return "SELECT * FROM ("+query+") TEMP WHERE ROWNUM < "+ rowCount;
			
		} else if (dbType.equalsIgnoreCase("mssql")) {
			return "SELECT TOP "+rowCount+" * FROM ("+query+") TEMP";
			
		} else if (dbType.equalsIgnoreCase("db2")) {
			return "SELECT * FROM ("+query+") TEMP fetch first "+rowCount+" row only";
			
		} else if (dbType.equalsIgnoreCase("tibero")) {
			return "SELECT * FROM ("+query+") TEMP WHERE ROWNUM < "+ rowCount;
			
		}
		
		throw new Exception(dbType+":정의된 DB TYPE 이 없음");
		
		
	}
	 
	 
	/*
	 * 넘어온 쿼리를 카운트를 할 수 있는 쿼리로 만든다.
	 */
	public static String makeCountQuery( String query ) throws Exception {
		
		return "SELECT COUNT(*) CNT FROM ("+query+") TEMP";
	}
	
	
	
	
		
	 /**
	 * <p>DB종류에 따른 페이징 쿼리를 리턴한다.
	 * @param dbType 
	 * @param query 
	 * @param column
	 * @param startNumVal (시작값의 변수명)
	 * @param limitCountVal (끝값의 변수명)
	 * @return
	 */
 public static String getPagingQueryByDB(String dbType, String query, String column, String startNumVal, String limitCountVal){
		String resultQuery = "";
		if(dbType.toLowerCase().equals("mysql")){
			resultQuery = "(" + query +") LIMIT "+startNumVal+", "+limitCountVal;			
		}
		else if(dbType.toLowerCase().equals("oracle")){
			resultQuery = "SELECT * FROM(select A.*, rownum RN from ("+query+") A) WHERE RN>="+startNumVal +" AND RN<="+limitCountVal;		
		}	
		
		//MS-SQL2005 부터 지원 
		else if(dbType.toLowerCase().equals("mssql")){
			query = query.substring(query.toUpperCase().indexOf("SELECT ")+7);
			resultQuery = "SELECT * FROM "
				+"(SELECT ROW_NUMBER() OVER(ORDER BY "+column+") AS RN,"+query+") AS A"
				+" WHERE RN BETWEEN "+startNumVal+" AND "+limitCountVal;
		}
		else if(dbType.toLowerCase().equals("db2")){
			query = query.substring(query.toUpperCase().indexOf("SELECT ")+7);
			resultQuery = "SELECT A.* FROM "
				+"(SELECT ROWNUMBER() OVER(ORDER BY "+column+") AS RN, "+query+") AS A"
				+" WHERE RN BETWEEN "+startNumVal+" AND "+limitCountVal;	
			
			System.out.println("resultQuery : " + resultQuery);
		} else if (dbType.toLowerCase().equals("tibero")) {
			resultQuery = "SELECT * FROM(select A.*, rownum RN from ("+query+") A) WHERE RN>="+startNumVal +" AND RN<="+limitCountVal;
		}
		
		//DB를 추가할 때마다 위와 같은 페이징쿼리를 넣어주어라. 
		return resultQuery;
  }
	

 
 /**
  * <p>db타입에 따른 시작 값과 종료 값을 배열로 리턴한다.
  * @param dbType
  * @param startNum
  * @param limitCount
  * @return
  */
 public static int[] getCountByDB(String dbType, int startNum, int limitCount){

		int[] startLimitCount = new int[2];
		if(dbType.toLowerCase().equals("mysql")){
			startLimitCount[0] = startNum;
			startLimitCount[1] = limitCount;	
		}
		else if(dbType.toLowerCase().equals("mssql")||dbType.toLowerCase().equals("db2")){
			startLimitCount[0] = startNum+1;
			startLimitCount[1] = limitCount + startNum;	
		}
		else if(dbType.toLowerCase().equals("oracle")){
			startLimitCount[0] = startNum+1;
			startLimitCount[1] = limitCount + startNum;		
		} else if (dbType.toLowerCase().equals("tibero")) {
			startLimitCount[0] = startNum+1;
			startLimitCount[1] = limitCount + startNum;
		}
		
		//DB를 추가할 때마다 위와 같은 페이징카운트를  넣어주어라. 
		return startLimitCount ;
}
 
 /**
	 * <p>Oracle 페이징 쿼리를 리턴한다.
	 * @param column
	 * @param start (시작값의 변수명)
	 * @param countPerPage (끝값의 변수명)
	 * @return
	 */
 public static String getPagingQueryByOracle(String query, int start, int countPerPage){
		String resultQuery = "SELECT * FROM(select A.*, rownum RN from ("+query+") A) WHERE RN>"+start +" AND RN<="+countPerPage;
		 
		return resultQuery;
 }
	 
	 
	 
}
