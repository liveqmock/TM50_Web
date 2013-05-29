package web.api.autosms.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.api.autosms.service.AutosmsAPIService;

public class AutosmsAPIControlHelper {
	
	private static final String MENUSERVICE_BEANID = "autosmsAPIService";
	
	public static AutosmsAPIService getUserService(ServletContext ctx) 
	{
		WebApplicationContext wac = WebApplicationContextUtils
	               .getRequiredWebApplicationContext(ctx);

	    return (AutosmsAPIService) wac.getBean(MENUSERVICE_BEANID);
	}
	

}
