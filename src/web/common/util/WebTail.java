package web.common.util;



import java.io.*;
import java.util.Vector;

@SuppressWarnings("unchecked")	
public class WebTail
{
	private static boolean error = false;
	private static Vector exitCase = new Vector();
	public static boolean waitForFile = false;
	public static Object writer = null;
	
	public String display(File file, boolean flag, String s, String s1, String s2)
	{
		String log = "";
		long l = 2L;
		if(s2 != null)
			l = parseLong(s2);
		if(error)
			return null;
		if(l == 0L){
			l = 2000L;
		}else{
			l *= 1000L;
		}
		RandomAccessFile randomaccessfile = null;
		try
		{
			randomaccessfile = new RandomAccessFile(file, "r");
		    seek(randomaccessfile, s, s1);
		    if(error)
		    	return null;
		    long l1 = randomaccessfile.getFilePointer();
		   do
		    {
		    	randomaccessfile.seek(l1);
		    	String s3 = randomaccessfile.readLine();
		    	l1 = randomaccessfile.getFilePointer();
		    	if(s3 == null)
		    	{
		    		if(!flag){
		    			randomaccessfile.close();
		    			return log;
		    		}
		    		Thread.sleep(l);
		    		randomaccessfile = new RandomAccessFile(file, "r");
		    		
		    	}else{
		    		if(flag)
		    		{
		    			for(int i = 0; i < exitCase.size(); i++){
		    				if(s3.indexOf((String)exitCase.elementAt(i)) != -1)
		    				{
		    					return (String)exitCase.elementAt(i);
		    				}
		    			}
		    		}
		    		log +=toKor(s3)+"<br>";
		    	}
		    } while(true); 
		  }catch(Exception exception){
			  exception.printStackTrace();
		  }
		  finally{
			  try{
				  if(randomaccessfile!=null)
					  randomaccessfile.close();
			  }catch(Exception exception){
				  exception.printStackTrace();
			  }
		  }
		  return log;
	}

	public static String startmain(String args[])
	{

		boolean flag = false;
		String s = null;
		String s1 = null;
		String s2 = null;
		String s3 = null;
  
		for(int i = 0; i < args.length; i++){
			if(args[i].equals("-n") || args[i].equals("-l")){
				s = args[++i];
			}else
				if(args[i].equals("-c") || args[i].equals("-b")){
					s1 = args[++i];
				}else{
				   if(args[i].equals("-f"))
				    flag = true;
				   else
				   if(args[i].equals("-i"))
				    s3 = args[++i];
				   else
				    s2 = args[i];
				}
		}
		File file = new File(s2);
		if(!waitForFile && !file.canRead())
		{
			return "파일을 찾을 수  없습니다.";
		}
		WebTail tail = new WebTail();
		return tail.display(file, flag, s, s1, s3);
	}

	 private long parseLong(String s)
	 {
	  long l = 1L;
	  if(s == null)
	   return 0L;
	  try
	  {
	  if(s.startsWith("+"))
	   s = s.substring(1, s.length());
	  else
	  if(s.startsWith("-"))
	  {
	   s = s.substring(1, s.length());
	   l = -1L;
	  }
	   return Long.parseLong(s) * l;
	  }
	  catch(Exception _ex)
	  {
	  }
	  return 0L;
	 }




	 private void seek(RandomAccessFile randomaccessfile, String s, String s1)
	 {
	  if(s == null && s1 == null)
	   s = "-20";
	  try
	  {
	   if(s != null)
	    seekLines(randomaccessfile, s);
	   else
	   if(s1 != null)
	    seekBytes(randomaccessfile, s1);
	   }
	  catch(Exception _ex)
	  {
	  }
	 }

	 private void seekBytes(RandomAccessFile randomaccessfile, String s)
	 throws IOException
	 {
	  long l = parseLong(s);
	  if(error)
	   return;
	  if(l >= 0L)
	   randomaccessfile.seek(l);
	  else
	   randomaccessfile.seek(randomaccessfile.length() + l);
	 }
	
	 private void seekLines(RandomAccessFile randomaccessfile, String s)
	 throws IOException
	 {
	  long l = parseLong(s);
	  if(error)
	   return;
	  String s1;
	  if(l >= 0L)
	  {
	   do
	   {
	    s1 = randomaccessfile.readLine();
	    l--;
	   } while(s1 != null && l != 0L);
	  } else
	  {
	  boolean flag = false;
	  long l1 = randomaccessfile.length() - 2L;
	   do
	   {
	    if(l1 < 0L)
	    {
	    randomaccessfile.seek(0L);
	    break;
	    }
	    randomaccessfile.seek(l1--);
	    byte byte0 = randomaccessfile.readByte();
	    if(byte0 == 13)
	    {
	     if(!flag)
	      l++;
	     else
	      flag = false;
	    } else
	    if(byte0 == 10)
	    {
	     l++;
	     flag = true;
	    } else
	    {
	     flag = false;
	    }
	   } while(l < 0L);
	  }
	 }

	 /**
	  * 8859_1을 한글코드 (KSC5601)로 변환
	  * @param s 변환을 원하는 문자열
	  * @return  캐릭터 셋이 변환된 문자열
	  */
	 public static String toKor(String s)
	 {
	  if(s != null)
	  {
	   return changeCode(s, "iso_8859-1", "KSC5601");
	  } else
	  {
	   return null;
	  }
	 }

	 public static String changeCode(String s, String s1, String s2)
	 {
	  String convString = null;
	  if(s != null)
	  {
	   try
	   {
	    convString = new String(s.getBytes(s1), s2);
	   }
	   catch(Exception exception)
	   {
	    //Log.errorlog(exception.toString());
	   }
	  }
	  return convString;
	 }
	 
	 @SuppressWarnings("unused")	
	 public static void main(String args[])
	 {
		 String [] cmd = {"D:\\ThunderMail\\ThunderMail4.5\\logs\\out20081210.log"};
		 String aaa = startmain(cmd);
	 }


}
