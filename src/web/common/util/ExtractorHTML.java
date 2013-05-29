package web.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * <p>썬더메일5.0에서 사용하는 코드값에 대한 처리 유틸이다. 
 * @author 김유근
 *
 */
public class ExtractorHTML {
	
	
	private String location;
	
	public static void main(String[] args)
	{
		if (args.length <= 0)
		{
			System.err.println("Syntax Error : Please provide the location(URL or file) to parse");
			System.exit(-1);
		
			ExtractorHTML ExtractorHTML = new ExtractorHTML(args[0]);
			String content = ExtractorHTML.getHTML();
			System.out.println(content);
		}
	}

	public ExtractorHTML(String location)
	{
		this.location = location;
	}

	public String getHTML()
	{
		return getHTML("utf-8");
	}
	
		
	public String getHTML(String lang)
	{
		StringBuffer resulthtml = new StringBuffer("");

		URL url = null;
		URLConnection conn = null;
		BufferedReader in = null;

		try
		{
			url = new URL(location);			
			conn = url.openConnection();
			InputStreamReader isr = null;
	
			
			if(lang.equals("EUC-KR")){
				isr = new InputStreamReader(conn.getInputStream());
			}else{
				isr = new InputStreamReader(conn.getInputStream(),"UTF-8");
			}
			
			in = new BufferedReader(isr);

			String lineText = "";

			while ((lineText = in.readLine()) != null)
			{
				resulthtml.append(lineText + "\r\n");
			}
		}
		catch (Exception e)
		{
			return "url내용이 잘못되어있습니다. <p>"+location;  //수신서버의 일시적인 오류로 분류한다.			
		}
		finally
		{
			if (in != null){
				try{
					in.close();
					in = null;
				}catch (Exception ee1)	{}
			}
			if (conn != null){
				try{
					conn = null;
				}catch (Exception ee1){}
			}
			if (url != null){
				try{
					url = null;
				}catch (Exception ee1)				{
				}
			}
		}
		return resulthtml.toString();
	}	
}
