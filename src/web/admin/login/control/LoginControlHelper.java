package web.admin.login.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.admin.login.service.LoginService;



public class LoginControlHelper {
	
	private static final String MENUSERVICE_BEANID = "loginService";

	public static LoginService getUserService(ServletContext ctx) {
		WebApplicationContext wac = WebApplicationContextUtils
	               .getRequiredWebApplicationContext(ctx);

	    return (LoginService) wac.getBean(MENUSERVICE_BEANID);
	}

}
