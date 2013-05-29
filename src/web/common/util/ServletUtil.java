package web.common.util;

/**
 * <p>Servlet(Controller)에서 처리되는 유틸모음
 * @author coolang (김유근)
 *
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.Cookie;

public class ServletUtil {


	/**
	 * <p>파라미터 String 널-공백처리 
	 * @param req
	 * @param param
	 * @return
	 * @throws UnsupportedEncodingException 
	 */	
	public static  String getParamString(HttpServletRequest req, String param) {		
		String resultVal = req.getParameter(param) == null ? "" : req.getParameter(param).trim();
		return resultVal;
	}
	
	
	/**
	 * <p>Base64로 디코딩한후에 파라미터 String 널-공백처리 
	 * @param req
	 * @param param
	 * @return
	 * @throws UnsupportedEncodingException 
	 */	
	public static  String getParamStringBase64(HttpServletRequest req, String param) {		
		String resultVal = req.getParameter(param) == null ? "" : req.getParameter(param).trim();
		return Base64.decodeToString(resultVal);
	}
	
	/**
	 * <p>파라미터 String 값이 널이면 주어진 값을  리턴 
	 * @param req
	 * @param param
	 * @param defaultVal
	 * @return
	 */
	public static  String getParamStringDefault(HttpServletRequest req, String param, String defaultVal) {		
		String resultVal = req.getParameter(param) == null ? defaultVal : req.getParameter(param).trim();
		return resultVal;
	}
	
	
	/**
	 * <p>복수의 파라미터 처리 
	 * @param req
	 * @param param
	 * @return
	 */
	public static  String[] getParamStringArray(HttpServletRequest req, String param) {		
		String[] resultVal = req.getParameterValues(param) == null ? null : (String[]) req.getParameterValues(param);
		
		return resultVal;
	}
	
	
	/**
	 * <p>파일업로드방식에서 파라미터 처리 
	 * @param mreq
	 * @param param
	 * @return
	 */
	public static String getFileParamString(MultipartHttpServletRequest mreq, String param){
		String resultVal = mreq.getParameter(param) == null ? "" : mreq.getParameter(param).trim();
		return resultVal;
	}
	
	
	/**
	 * <p>파라미터 int  널이면 주어진 값을  리턴 
	 * @param req
	 * @param param
	 * @return
	 */	
	public static  int  getParamInt(HttpServletRequest req, String param, String defaultVal){
		String resultVal = "";
		if( req.getParameter(param) == null ||  req.getParameter(param).equals("")){
			resultVal = "0";
		}else{
			resultVal =  req.getParameter(param).trim(); 
		}				
		return Integer.parseInt(resultVal);
	}
	
	/**
	 * <p>Base64로 디코팅 한 후 에 파라미터 int  널이면 주어진 값을  리턴 
	 * @param req
	 * @param param
	 * @return
	 */	
	public static  int  getParamIntBase64(HttpServletRequest req, String param, String defaultVal){	
		String resultVal = "";
		if( req.getParameter(param) == null ||  req.getParameter(param).equals("")){
			resultVal = "0";
		}else{
			resultVal =  req.getParameter(param).trim(); 
		}	
		return Integer.parseInt(Base64.decodeToString(resultVal));
	}
	

	/**
	 * <p>String 파라미터중 필수파라미터를 체크하여 한개라도 공백이면 false 리턴  
	 * @param params
	 * @return
	 */
	public static  boolean checkRequireParamString(String[] params){
		int emptyNum=0;
		if(params==null) return false;
		for(int i=0;i<params.length;i++){
			if(params[i]==null || params[i].trim().equals("")){
				emptyNum++;
			}
		}
		
		if(emptyNum>0){
			return false;
		}else{
			return true;
		}
	}	
	
	
	/**
	 * <p>데이타를 처리한후에 메시지를 출력후 해당 페이지가 있다면  이동한다.  
	 * <p>넘겨주는 쪽은 반드시 iframe으로 넘겨주어야 한다. 
	 * @param res
	 * @param msg
	 * @param redirectURL
	 * @throws IOException
	 */
	public static void messageGoURL(HttpServletResponse res, String msg, String redirectURL) throws IOException{
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();		
		out.println("<script language=javascript>");
		if(msg!=null && !msg.equals("")){
			out.println(" alert('"+msg+"');");
		}
		if(redirectURL!=null && !redirectURL.equals("")){
			out.println("location.href='"+redirectURL+"';");
		}			
		out.println("</script>");	
	}
	
	
	/**
	 * <p>메시지 출력후 현재 창 닫기
	 * @param res
	 * @param msg
	 * @param redirectURL
	 * @throws IOException
	 */
	public static void messageClose(HttpServletResponse res, String msg) throws IOException{
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();		
		out.println("<script language=javascript>");
		out.println(" alert('"+msg+"');");
		out.println(" window.close();");
		out.println("</script>");	
	}
	
	/**
	 * <p>데이타를 처리한후에 메시지를 출력후 해당 페이지가 있다면  이동한다.  
	 * <p>넘겨주는 쪽은 반드시 iframe으로 넘겨주어야 한다. 
	 * @param res
	 * @param msg
	 * @param redirectURL
	 * @throws IOException
	 */
	public static void messageGoURL(HttpServletResponse res, String msg) throws IOException{
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();		
		out.println("<script language=javascript>");
		if(msg!=null && !msg.equals("")){
			out.println(" alert('"+msg+"');");
		}	
		out.println("</script>");	
	}
	
	
	/**
	 *  <p>팝업을 불러온 창을 리로드하고 팝업은 닫는다.
	 * @param res
	 * @param msg
	 * @param redirectURL
	 * @throws IOException
	 */
	public static void messagePopupReloadClose(HttpServletResponse res, String msg, String redirectURL) throws IOException{
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();		
		out.println("<script language=javascript>");
		if(msg!=null && !msg.equals("")){
			out.println(" alert('"+msg+"');");			
		}		
		if(redirectURL!=null && !redirectURL.equals("")){
			out.println(" parent.opener.location.href='"+redirectURL+"';");
			out.println(" top.self.close()");
		}			
		
		out.println("</script>");	
	}
	
	/**
	 * <p>페이지 뒤로 이동 
	 * @param res
	 * @param msg
	 * @throws IOException
	 */
	public static void messageGoBack(HttpServletResponse res, String msg) throws IOException{
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();		
		out.println("<script language=javascript>");
		if(msg!=null && !msg.equals("")){
			out.println(" alert('"+msg+"');");
		}		
		out.println(" history.back();");					
		out.println("</script>");	
	}
	
	/**
	 * <p>스크립트 실행 
	 * @param res
	 * @param msg
	 * @throws IOException
	 */
	public static void messageAndScript(HttpServletResponse res, String msg, String script) throws IOException{
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();		
		out.println("<script language=javascript>");
		if(msg!=null && !msg.equals("")){
			out.println(" alert('"+msg+"');");
		}		
		out.println(script+";");					
		out.println("</script>");	
	}
	
	
	/**
	 *  <p>페이지 분할일 경우 페이징파라미터를 세팅한다.
	 *  <p>호출하는 메소드는 ModelAndView 리턴값이어야 한다.
	 * @param req
	 */
	public static void pageParamSetting(HttpServletRequest req){
		int currentPage = getParamInt(req,"currentPage","1");
		int countPerPage = getParamInt(req,"countPerPage","10");		
		req.setAttribute("currentPage", currentPage);
		req.setAttribute("countPerPage", countPerPage);	
	}
	
	/**
	 * <p>메뉴ID값을 세팅한다. 
	 * <p>호출하는 메소드는 ModelAndView 리턴값이어야 한다.
	 * @param req
	 */
	public static void meunParamSetting(HttpServletRequest req){
		
		String mainMenuID = getParamString(req,"mainMenuID");
		String subMenuID = getParamString(req,"subMenuID");			
		req.setAttribute("mainMenuID", mainMenuID);
		req.setAttribute("subMenuID", subMenuID);
	}
	
	
	/**
	 * <p>쿠키값 가져오기 
	 * @param req, name
	 */
	public static String getCookieValue(HttpServletRequest req, String name) {
		Cookie[] cookies = req.getCookies();
		String retv = "";

		if(cookies != null) {
			for( int i=0; i < cookies.length; i ++ ) {
				if(cookies[i].getName().equals(name)) {
					retv = cookies[i].getValue();
					break;
				}
			}
		}
		return retv;
	}
	
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		Cookie returnCookie = null;

		if(cookies != null) {
			for( int i=0; i < cookies.length; i ++ ) {
				if(cookies[i].getName().equals(name)) {
					returnCookie = cookies[i];
					break;
				}
			}
		}
		return returnCookie;
	}

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) throws Exception {
		
        Cookie cookie = getCookie(request, name);
		if(cookie == null) {
			createCookie( response, name, value );
		} else {
			cookie.setValue(value);
		}
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, String domain, String path, int maxAge) throws Exception {
		
        Cookie cookie = getCookie(request, name);
		if(cookie == null) {
			createCookie( response, name, value, domain, path, maxAge );
		} else {
			cookie.setValue(value);
		}
    }
    
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge) throws Exception {
		
        Cookie cookie = getCookie(request, name);
		if(cookie == null) {
			createCookie( response, name, value, maxAge );
		} else {
			cookie.setValue(value);
		}
    }
    
	
    public static void createCookie(HttpServletResponse response, String name, String value) throws Exception {
        Cookie cookie = new Cookie(name,value);
		response.addCookie(cookie);	
    }
    
    public static void createCookie(HttpServletResponse response, String name, String value, String path, int maxAge) throws Exception {
        Cookie cookie = new Cookie(name,value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
		response.addCookie(cookie);	
    }
    
    public static void createCookie(HttpServletResponse response, String name, String value, int maxAge) throws Exception {
        Cookie cookie = new Cookie(name,value);
        cookie.setMaxAge(maxAge);
		response.addCookie(cookie);	
    }
    

    public static void createCookie(HttpServletResponse response, String name, String value, String domain, String path,int maxAge) throws Exception {
        Cookie cookie = new Cookie(name,value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
		cookie.setDomain(domain);
		response.addCookie(cookie);	
    }
    
	public static String getTagString( String text ) {
		String retv = text;
		if (retv == null)
		{
			retv = "";
			
		} else {
			retv = retv.replace( "\\", "\\\\");
			retv = retv.replace("&", "&amp;");	// &
			retv =	retv.replace("\"","&quot;") ;	// 큰 따옴표
			retv =	retv.replace("'","\\'") ;	// 큰 따옴표
			retv =	retv.replace("<","&lt;") ;		// <
			retv =	retv.replace(">","&gt;") ;		// >
		}
		return retv;
	
	}
	
}
