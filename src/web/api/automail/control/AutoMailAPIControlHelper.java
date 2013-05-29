package web.api.automail.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.api.automail.service.AutoMailAPIService;

public class AutoMailAPIControlHelper {
	
	private static final String MENUSERVICE_BEANID = "autoMailAPIService";

	public static AutoMailAPIService getUserService(ServletContext ctx) {
		WebApplicationContext wac = WebApplicationContextUtils
	               .getRequiredWebApplicationContext(ctx);

	    return (AutoMailAPIService) wac.getBean(MENUSERVICE_BEANID);
	}

}
