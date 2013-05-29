package web.common.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import web.common.model.*;


/**
 * <p>파일 업로드 유틸 
 * <p>원본 :  Spring 프레임워크 워크북 
 * @author coolang (김유근) : 수정/보완
 *
 */
public class FileUploadUtil {
	/**
	 * <p>파일을 업로드 한다. 
	 * @param multiFile
	 * @param realPath
	 * @return
	 */
	  public static String uploadFile(MultipartFile multiFile, String realPath) {
		  
		  InputStream stream = null;

		  // 아래부분은 파일명을 암호화하기 위한 처리 로직이다.
		  /*
	      UUID uuid = UUID.randomUUID();
	      String tempFileName = uuid.toString();
	      */
		  String tempFileName = multiFile.getOriginalFilename();		  
		  String resultFileName = renameFile(tempFileName, realPath);
		  OutputStream bos = null;
	        try {
	            stream = multiFile.getInputStream();	         
	            bos = new FileOutputStream(realPath + resultFileName);
	            int bytesRead = 0;
	            byte[] buffer = new byte[8192];
	            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
	                 bos.write(buffer, 0, bytesRead);
	            }	    
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally{
	        	try{bos.close();}catch(IOException e1){};
	        	try{stream.close();}catch(IOException e2){};
	        }
	        	        
	        return resultFileName;
	  }

		/**
		 * <p>파일을 업로드 한다. 
		 * @param multiFile
		 * @param realPath
		 */
		  public static FileUpload uploadNewFile(HttpServletRequest req, MultipartFile multiFile, String realPath) {
			  
			  String newFileName = req.getSession().getId()+new Date().getTime();
			  return FileUploadUtil.uploadNewFile(req, multiFile, realPath, newFileName);

		  }
	  
		/**
		 * <p>파일을 업로드 한다. 
		 * @param multiFile
		 * @param realPath
		 * @return
		 */
		  public static FileUpload uploadNewFile(HttpServletRequest req, MultipartFile multiFile, String realPath, String newFileName) {
			  
			  InputStream stream = null;
			  
			  FileUpload fileUpload = new FileUpload();
			  
			  String realFileName = multiFile.getOriginalFilename();
			  OutputStream bos = null;
		        try {
		        	
		        	
		        	File file = new File(realPath);
		        	if(!file.exists()) {
		        		file.mkdirs();
		        	}
		        	
		            stream = multiFile.getInputStream();	         
		            bos = new FileOutputStream(realPath + newFileName);
		            int bytesRead = 0;
		            byte[] buffer = new byte[8192];
		            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
		                 bos.write(buffer, 0, bytesRead);
		            }
		            
		            fileUpload.setRealFileName(realFileName);
		            fileUpload.setNewFileName(newFileName);
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }finally{
		        	try{bos.close();}catch(IOException e1){};
		        	try{stream.close();}catch(IOException e2){};
		        }
		        
		        return fileUpload;
		  }
		  
	 public static void downloadFile(HttpServletRequest req, HttpServletResponse res, String realPath, String newFileName, String realFileName) {
		 
		 File			file		= null;
		 BufferedInputStream		inStream	= null;
		 BufferedOutputStream		outStream	= null;

		 String	str_client			= "";
		 
		 realFileName = realFileName.replaceAll(" ", "_");

		 try{

		 	file		= new File( realPath, newFileName );
		 	inStream	= new BufferedInputStream(new FileInputStream( file ));

		 	res.reset();
		 	
		 	str_client	= req.getHeader("User-Agent");
		 	res.setContentType("application/x-msdownload;");
		 	res.setHeader("Content-Description", "JSP Generated Data");

		 	if( str_client.indexOf("MSIE 5.5") != -1 )
		 	{
		 		res.setHeader("Content-Type", "doesn/matter; charset=euc-kr");
		 		res.setHeader("Content-Disposition", "filename=" + new String( realFileName.getBytes(), "ISO8859_1") );
		 	}else{
		 		res.setHeader("Content-Disposition", "attachment; filename=" + new String( realFileName.getBytes(), "ISO8859_1") );
		 	}

		 	res.setHeader("Content-Length", ""+file.length() );

		 	outStream	= new BufferedOutputStream( res.getOutputStream() );

		     int i ;
		     while( (i = inStream.read()) != -1) {
		         outStream.write(i);
		     }

		 	inStream.close();
		 	outStream.close();

		 }catch( Exception e ){


		 }finally{

		 	if( inStream != null )
		 	{
		 		try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	}
		 	if( outStream != null )
		 	{
		 		try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	}

		 }		 
		 
	 }
      	  
 public static void downloadFile(HttpServletRequest req, HttpServletResponse res, String filePath) {
		 
		 File			file		= null;
		 BufferedInputStream		inStream	= null;
		 BufferedOutputStream		outStream	= null;

		 String	str_client			= "";
		 
		 try{

		 	file		= new File(filePath);
		 	inStream	= new BufferedInputStream(new FileInputStream( file ));

		 	res.reset();
		 	
		 	str_client	= req.getHeader("User-Agent");
		 	res.setContentType("application/x-msdownload;");
		 	res.setHeader("Content-Description", "JSP Generated Data");

		 	if( str_client.indexOf("MSIE 5.5") != -1 )
		 	{
		 		res.setHeader("Content-Type", "doesn/matter; charset=euc-kr");
		 		res.setHeader("Content-Disposition", "filename=" + new String( file.getName().getBytes(), "ISO8859_1") );
		 	}else{
		 		res.setHeader("Content-Disposition", "attachment; filename=" + new String( file.getName().getBytes(), "ISO8859_1") );
		 	}

		 	res.setHeader("Content-Length", ""+file.length() );

		 	outStream	= new BufferedOutputStream( res.getOutputStream() );

		     int i ;
		     while( (i = inStream.read()) != -1) {
		         outStream.write(i);
		     }

		 	inStream.close();
		 	outStream.close();

		 }catch( Exception e ){


		 }finally{

		 	if( inStream != null )
		 	{
		 		try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	}
		 	if( outStream != null )
		 	{
		 		try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	}

		 }		 
		 
	 }
	  /**
	   * <p>파일을 삭제한다. 
	   * @param fileName
	   * @param realPath
	   * @return
	   */
		  
	  public static boolean deleteFile(String fileName, String realPath){
		  boolean resultbool = false;
		  File file = new File(realPath+fileName);
		  
		  if(file.exists()){
			  resultbool = file.delete();
		  }
		
		  return resultbool;
	  }
	  
	  /**
	   * <p>파일을 삭제한다. 
	   * @param fileName
	   * @param realPath
	   * @return
	   */
		  
	  public static boolean deleteFile(String filePath){
		  boolean resultbool = false;
		  File file = new File(filePath);
		  
		  if(file.exists()){
			  resultbool = file.delete();
		  }
		
		  return resultbool;
	  }
	  
	  /**
	   * <p>파일 존재 여부확인 
	   * @param fileName
	   * @param realPath
	   * @return
	   */
		  
	  public static boolean existFile(String fileName, String realPath){
		  File file = new File(realPath+fileName);
		  
		  if(file.exists()){
			  return true;
		  }
		
		  return false;
	  }
	  
	  /**
	   * <p>같은 이름의 파일이 이미 존재한다면  filename[1].txt, filename[2].txt, 식으로 이름을 만들어나간다.
	   * @param fileName
	   * @param dir
	   * @return
	   */
	  private static String renameFile(String fileName, String dir){
		  int idx = 0;
			String temp1 = null;
			String temp2 = null;
			String ftemp = null;
			
			try
			{
				File f1 = new File(dir, fileName);
				String temp = fileName;
				
				// Rename file name, if the same file name exists.
				// ex) file.gif, file[1].gif, file[2].gif, ...
				for (int i=1 ; f1.exists() ; i++)
				{
					try {idx = temp.indexOf(".");}
					catch(Exception e) { idx = 0; }
					temp1 = temp.substring(0, idx);
					temp2 = temp.substring(idx);
					
					ftemp = "[" + Integer.toString(i) + "]";

					fileName = temp1 + ftemp + temp2;
					f1 = new File(dir, fileName);
				}
				return fileName;
			}
			catch(Exception e)
			{
			   return e.toString();
			}
	  }
	  
	
}
