<%@ page contentType="text/html;charset=utf-8"%><%@ page import="com.oreilly.servlet.multipart.*"%><%@ page import="com.oreilly.servlet.*"%><%@ page import="java.io.*"%><%@ page import="web.common.util.DateUtils"%><%
	try{
		MultipartParser mp = new MultipartParser(request,10*1024*1024); // 10MB 
		mp.setEncoding("UTF-8");
		Part part; 
	    while ((part = mp.readNextPart()) != null) {  
	    	String savePath = "D:\\EZMAIL\\apache-tomcat-6.0.18\\webapps\\ezmail\\upload\\"+DateUtils.getDateString()+"\\";
	    	File dir = new File(savePath);
	    	if ( !dir.exists() ) {
	            try {
	                dir.mkdir();
	            } catch(Exception e1){
	            }
	        }
			String name = new String(part.getName());  
	    	//파일이 아닐때
			if (part.isParam()){     
				ParamPart paramPart = (ParamPart) part; 
				String value = paramPart.getStringValue();
	            //out.println(" >>>>>>>>>>>>> param >> name=" + name + ", value=" + value);     
			}
			//파일일때
			else if(part.isFile()){
				FilePart filePart = (FilePart) part; 
				String fileName = filePart.getFileName();
				if ( fileName != null ){
					long size = filePart.writeTo(dir);
					out.print("SUCCESS");
				}
	    	}
	    }
	}catch(Exception E){
	   out.println("error : "+E.toString());
	}
%>