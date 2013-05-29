package tm.automail.sendapi;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;



public class ThunderAutoMailSender {
	
	private static String RESULT_SUCCESS = "-100";  	//데이터 전송 성공
	private static String RESULT_FAIL_PARAM = "10"; 	//필수 파라미터 미입력
	private static String RESULT_FAIL_CONNECT = "20";	//연결 실패
	private static String RESULT_ETC = "90";			//기타

	
	
	public String send(ThunderAutoMail tm)
	{
		StringBuffer stingBuffer = new StringBuffer();
		
		if(tm.getThunderMailURL()!=null && tm.getAutomailID()!=null && tm.getEmail()!=null)
		{	
			try
			{
				stingBuffer.append("method=insert")
					.append("&automailID="+URLEncoder.encode(tm.getAutomailID(),"utf-8")) 
					.append("&email="+URLEncoder.encode(tm.getEmail(),"utf-8"));
			
				if(tm.getMailTitle()!=null)
					stingBuffer.append("&mailTitle="+URLEncoder.encode(tm.getMailTitle(),"utf-8"));
			
				if(tm.getMailContent()!=null)
					stingBuffer.append("&mailContent="+URLEncoder.encode(tm.getMailContent(),"utf-8"));
			
				if(tm.getSenderMail()!=null)
					stingBuffer.append("&senderMail="+URLEncoder.encode(tm.getSenderMail(),"utf-8"));
			
				if(tm.getSenderName()!=null)
					stingBuffer.append("&senderName="+URLEncoder.encode(tm.getSenderName(),"utf-8"));
			
				if(tm.getReceiverName()!=null)
					stingBuffer.append("&receiverName="+URLEncoder.encode(tm.getReceiverName(),"utf-8"));
			
				if(tm.getOnetooneInfo()!=null)	
					stingBuffer.append("&onetooneInfo="+URLEncoder.encode(tm.getOnetooneInfo(),"utf-8"));
				
				if(tm.getCustomerID()!=null && !(tm.getCustomerID().equals("")))
					stingBuffer.append("&customerID="+URLEncoder.encode(tm.getCustomerID(),"utf-8"));
				
				if(tm.getReserveDate()!=null && !(tm.getReserveDate().equals("")))
					stingBuffer.append("&reserveDate="+URLEncoder.encode(tm.getReserveDate(),"utf-8"));
						
			
			}
			catch (UnsupportedEncodingException e) {
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
