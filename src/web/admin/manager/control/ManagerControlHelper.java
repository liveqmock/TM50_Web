package web.admin.manager.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.admin.manager.service.ManagerService;

public class ManagerControlHelper {
	private static final String MENUSERVICE_BEANID = "managerService";

	public static ManagerService getUserService(ServletContext ctx) {
		WebApplicationContext wac = WebApplicationContextUtils
	               .getRequiredWebApplicationContext(ctx);

	    return (ManagerService) wac.getBean(MENUSERVICE_BEANID);
	}

}
