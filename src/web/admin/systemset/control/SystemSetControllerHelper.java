package web.admin.systemset.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.admin.systemset.service.SystemSetService;

public class SystemSetControllerHelper {
	private static final String MENUSERVICE_BEANID = "systemSetService";

	public static SystemSetService getUserService(ServletContext ctx) {
		WebApplicationContext wac = WebApplicationContextUtils
	               .getRequiredWebApplicationContext(ctx);

	    return (SystemSetService) wac.getBean(MENUSERVICE_BEANID);
	}

}
