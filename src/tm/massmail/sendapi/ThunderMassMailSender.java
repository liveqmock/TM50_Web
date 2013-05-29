package tm.massmail.sendapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import tm.massmail.sendapi.ThunderMassMail;

public class ThunderMassMailSender {
	
	private static String RESULT_SUCCESS = "-100";  	//데이터 전송 성공
	private static String RESULT_FAIL_PARAM = "10"; 	//필수 파라미터 미입력
	private static String RESULT_FAIL_CONNECT = "20";	//연결 실패
	private static String RESULT_ETC = "90";			//기타
	
	
	public String send(ThunderMassMail tm)
	{
		StringBuffer stingBuffer = new StringBuffer();  
		
		if(tm.getThunderMailURL()!=null && tm.getWriter()!=null && tm.getMailTitle()!=null && tm.getSenderEmail()!=null && tm.getSenderName()!=null
				&& tm.getReceiverName()!=null && tm.getContentType()!=null && tm.getTargetType()!=null && tm.getFileOneToOne() != null && tm.getSendType() != null )
		{
			
			try 
			{
				stingBuffer.append("method=massmail")
				.append("&writer="+URLEncoder.encode(tm.getWriter(),"utf-8"))
				.append("&mailTitle="+URLEncoder.encode(tm.getMailTitle(),"utf-8"))
				.append("&senderEmail="+URLEncoder.encode(tm.getSenderEmail(),"utf-8")) 
				.append("&senderName="+URLEncoder.encode(tm.getSenderName(),"utf-8"))
				.append("&receiverName="+URLEncoder.encode(tm.getReceiverName(),"utf-8"))
				.append("&contentType="+URLEncoder.encode(tm.getContentType(),"utf-8"))
				.append("&targetType="+URLEncoder.encode(tm.getTargetType(),"utf-8"))
				.append("&fileOneToOne="+URLEncoder.encode(tm.getFileOneToOne(),"utf-8"))
				.append("&sendType="+URLEncoder.encode(tm.getSendType(),"utf-8"));
				
				if(tm.getContentType().equals("content"))
					stingBuffer.append("&mailContent="+URLEncoder.encode(tm.getMailContent(),"utf-8"));
				else if(tm.getContentType().equals("template"))
					stingBuffer.append("&template_id="+URLEncoder.encode(tm.getTemplate_id(),"utf-8"));
				
				if(tm.getTargetType().equals("string"))
				{
					stingBuffer.append("&targetString="+URLEncoder.encode(tm.getTargetString(),"utf-8"));
					
				}
				else if(tm.getTargetType().equals("query"))
				{
					stingBuffer.append("&targetQuery="+URLEncoder.encode(tm.getTargetQuery(),"utf-8"));
					stingBuffer.append("&targetDBId="+URLEncoder.encode(tm.getTargetDBId(),"utf-8"));
				}
				
				if(tm.getMassMailGroupID()!=null && !(tm.getMassMailGroupID().equals("")))
					stingBuffer.append("&massMailGroupID="+URLEncoder.encode(tm.getMassMailGroupID(),"utf-8"));
				
				if(tm.getSendType().equals("2")) { //예약발송
					if(tm.getSendSchedule()!=null && !(tm.getSendSchedule().equals("")))
						stingBuffer.append("&sendSchedule="+URLEncoder.encode(tm.getSendSchedule(),"utf-8"));
					else
						return RESULT_FAIL_PARAM;
						
				}
				
				if(tm.getAttachPath()!=null && !(tm.getAttachPath().equals(""))){
					stingBuffer.append("&attachPath="+URLEncoder.encode(tm.getAttachPath(),"utf-8"));
					stingBuffer.append("&attachFileNames="+URLEncoder.encode(tm.getAttachFileNames(),"utf-8"));
					stingBuffer.append("&attachFileRealNames="+URLEncoder.encode(tm.getAttachFileRealNames(),"utf-8"));
				}
				
				
			} catch (UnsupportedEncodingException e) {
				System.out.println("UnsupportedEncodingException e : "+ e);
			}
			
		}
		else
			return RESULT_FAIL_PARAM;
		
		return connectToServlet(tm.getThunderMailURL(), stingBuffer);
		
		
	}
	
	
	private static String connectToServlet(String thunderMailURL, StringBuffer sb){		
		
		OutputStreamWriter out=null;
		
		BufferedReader in = null;
		String strReadLine="";
		
		try{
			
			URL url = new URL(thunderMailURL);
			URLConnection connection = url.openConnection();
    		HttpURLConnection hurlc = (HttpURLConnection)connection; 
    		
    		hurlc.setRequestProperty("content-Type", "application/x-www-form-urlencoded");
    		hurlc.setRequestMethod("POST");		//POST 방식 전송 
    		hurlc.setDoOutput(true);
    		hurlc.setDoInput(true);
    		hurlc.setUseCaches(false);
    		hurlc.setDefaultUseCaches(false);
    		hurlc.connect();    		
    		out = new OutputStreamWriter(hurlc.getOutputStream());	    		
    		out.write(sb.toString());    		
    		out.flush();    		
    		out.close();    		
    		in = new BufferedReader(new InputStreamReader(hurlc.getInputStream()));
    		
    		String strInLine = "";
			while((strInLine=in.readLine())!=null){    
				strReadLine += strInLine;    			
			}
			
			in.close();
    		
		}catch(ConnectException ex){
			System.out.println("ConnectException ex : "+ ex);
			strReadLine = RESULT_FAIL_CONNECT;
		}catch(Exception ex){
			System.out.println("Exception ex : "+ ex);
			strReadLine =RESULT_FAIL_CONNECT;
		}finally{		
			try{
				if(out!=null) out.close();
				if(in!=null) in.close();
			}catch(Exception e){}
		}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
		
		if(strReadLine.equals(RESULT_SUCCESS))
			return RESULT_SUCCESS;
		else
			return strReadLine;
	}

}
