package web.filter;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import web.common.util.*;
import web.admin.systemset.control.SystemSetControllerHelper;
import web.admin.systemset.service.*;;



/**
 * <p>오픈스크립트가 아웃룩2007에서는 엑박표시가 나므로 그것을 처리하기 위해 url를 파싱하는 서블릿이다.
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class OpenServlet extends HttpServlet{
	
	private static String responseURL = null;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
		processRequest(req,res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
		processRequest(req,res);
	}
	
	
	
	/**
	 * <p>오픈URL를  파싱하여 오픈처리하는 페이지로 이동 (대량,자동,연동 모든 메일 포함)
	 * @param req
	 * @param res
	 * @throws IOException
	 * @throws ServletException
	 */
	private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
		
		if(responseURL==null || responseURL.equals("")){
			ServletContext context=getServletContext(); 
			SystemSetService service = SystemSetControllerHelper.getUserService(context);		
			responseURL = service.getSystemSetInfo(Constant.CONFIG_FLAG_MASSMAIL, "responseURL"); 
		}
		res.setContentType("text/html");
		String path=req.getRequestURL().toString();		
		String path_params = "";		
		String path_result = "";
	
		//파라미터 부분을 자른다.		
		path_params = path.substring(path.indexOf(Constant.OPEN_TAG), path.indexOf(".gif")); 		
		path_params = path_params.replace(Constant.OPEN_TAG, "?method");
		path_params = path_params.replace(Constant.OPEN_EQAUL_CHAR, "=");
		path_params = path_params.replace(Constant.OPEN_AMP_CHAR, "&");
		path_result = responseURL+path_params;
				
		res.sendRedirect(path_result);
		
	}

}
