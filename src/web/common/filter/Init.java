package web.common.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import web.common.util.LoginInfo;
import web.common.util.StringUtil;

import javax.servlet.http.HttpServletRequest;


public class Init implements Filter {
	private String[] passUrl = {};
	
	public void init(FilterConfig filterConfig) throws ServletException {
		//filterConfig.getInitParameter("passURL");
		String passUrlString = filterConfig.getInitParameter("passUrl");
		if(passUrlString != null) {
			passUrl = passUrlString.split(";");
			for( int i = 0; i < passUrl.length; i ++ ) {
				passUrl[i] = passUrl[i].trim().toUpperCase();
			}
			
		}
	}
	
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		String URI = request.getRequestURI().toUpperCase()+'?'+StringUtil.toStr(request.getQueryString()).toUpperCase();
		String id = StringUtil.toStr(req.getParameter("id")); 
		
		for( String curURL: this.passUrl) {
			if(URI.indexOf(curURL) > -1) {
		        chain.doFilter(req, res);
				return;
			}
		}
		if(!LoginInfo.getIsLogined( (HttpServletRequest) req)) {
			
			//HttpServletResponse response = (HttpServletResponse) res;
			
			PrintWriter out = res.getWriter();
			
			out.println("<script type='text/javascript'>");
			out.println("var win = $(document.body).getElement('.isFocused');");
			out.println("if(win) {closeWindow(win); }");
			if(!id.equals("")) {
				out.println("if(!win) {closeWindow('"+id+"'); }");
			}
			out.println("window.addEvent('domready',function() {MochaUI.LoginWindow();});");
			out.println("</script>");
			
			
			
			//System.out.println(request.getRequestURI()); 
			return;
		}
        chain.doFilter(req, res);
		
	}
	
	public void destroy() {
		this.passUrl = null;
		
	}
	
}