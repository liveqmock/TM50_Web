package web.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 * LinkExtractor extracts all the links from the given webpage
 * and prints them on standard output.
 */
@SuppressWarnings("unused")
public class SystemExec
{
	 public SystemExec()
	    {
	    }

	    public static String getCMD(String as[])
	    {
	        String s = "";
	        String s1 = "";
	        try
	        {
	            Process process = Runtime.getRuntime().exec(as);
	            InputStreamReader inputstreamreader = new InputStreamReader(process.getInputStream());
	            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
	            InputStreamReader inputstreamreader1 = new InputStreamReader(process.getErrorStream());
	            BufferedReader bufferedreader1 = new BufferedReader(inputstreamreader1);
	            String s2;
	            while((s2 = bufferedreader.readLine()) != null) 
	                s = (new StringBuilder()).append(s).append(s2).append("<br>").toString();
	            if(s == "" || s.equals(""))
	                while((s2 = bufferedreader1.readLine()) != null) 
	                    s = (new StringBuilder()).append(s).append(s2).append("<br>").toString();
	            inputstreamreader1.close();
	            bufferedreader1.close();
	        }
	        catch(Exception exception)
	        {
	          
	        }
	        return s;
	    }

}