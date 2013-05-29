package web.common.util;



public class CheckServerIP {
	
	public static int checkCBL(String localServerIP) 
	{
		String url="http://cbl.abuseat.org/lookup.cgi?ip="+localServerIP;
		int state = 0;
	

		ExtractorHTML ehtml = new ExtractorHTML(url);
		String content = ehtml.getHTML("EUC-KR");
		if(content.indexOf("is not listed")!= -1){
			state = 1;
		}else{
			state = 2;
		}
		
		return state;
	}
	
	public static int checkRBL(String localServerIP) 
	{
		String url="http://dnsbl.net.au/lookup/?address="+localServerIP;
		int state = 0;

		ExtractorHTML ehtml = new ExtractorHTML(url);
		String content = ehtml.getHTML("EUC-KR");
		if(content.indexOf("Not Listed on any dnsbl") != -1){
			state = 1;
		}else{
			state = 2;
		}
		return state;
	}
	
	public static int checkKisaRBL(String localServerIP) 
	{
		String url="https://www.kisarbl.or.kr/ip/ip_search.jsp?IP="+localServerIP;
		int state = 0;
		ExtractorHTML ehtml = new ExtractorHTML(url);
		String content = ehtml.getHTML("EUC-KR");
		if(content.indexOf("않습니다")!= -1){
			state = 1;
		}else{
			state = 2;
		}
		return state;
	}
	
	public static int checkSpamHouse(String localServerIP) 
	{
		String url="http://www.spamhaus.org/query/ip/"+localServerIP;
		int state = 0;
	

		ExtractorHTML ehtml = new ExtractorHTML(url);
		String content = ehtml.getHTML("EUC-KR");
		if(content.indexOf("is listed in the")!= -1){ 
			state = 2;
		}else{
			state = 1;
		}
		
		return state;
	}
}
