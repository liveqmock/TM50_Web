package tm.autosms.sendapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ThunderAutoSmsSender {
	
	private static String RESULT_SUCCESS = "-100";  	//데이터 전송 성공
	private static String RESULT_FAIL_PARAM = "10"; 	//필수 파라미터 미입력
	private static String RESULT_FAIL_CONNECT = "20"; 	//연결 실패
	private static String RESULT_ETC = "90";			//기타
	
	public String send(ThunderAutoSMS tm)
	{
		StringBuffer stingBuffer = new StringBuffer();
		
		if(tm.getThunderMailURL()!=null && tm.getAutosmsID()!=null && tm.getReceiverPhone()!=null)
		{
			try
			{
				stingBuffer.append("autosmsID="+URLEncoder.encode(tm.getAutosmsID(),"utf-8"))
					.append("&receiverPhone="+URLEncoder.encode(tm.getReceiverPhone(),"utf-8"));
				
				if(tm.getSenderPhone()!=null)
					stingBuffer.append("&senderPhone="+URLEncoder.encode(tm.getSenderPhone(),"utf-8"));
				if(tm.getSmsMsg()!=null)
					stingBuffer.append("&smsMsg="+URLEncoder.encode(tm.getSmsMsg(),"utf-8"));
				if(tm.getMsgType()!=null)
					stingBuffer.append("&msgType="+URLEncoder.encode(tm.getMsgType(),"utf-8"));
				if(tm.getOnetooneInfo()!=null)
					stingBuffer.append("&onetooneInfo="+URLEncoder.encode(tm.getOnetooneInfo(),"utf-8"));
			
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
			strReadLine = RESULT_FAIL_CONNECT;
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
