package web.admin.rejectmail.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.admin.rejectmail.service.RejectMailService;

public class RejectMailControlHelper {
	private static final String MENUSERVICE_BEANID = "rejectMailService";

	public static RejectMailService getUserService(ServletContext ctx) {
		WebApplicationContext wac = WebApplicationContextUtils
	               .getRequiredWebApplicationContext(ctx);

	    return (RejectMailService) wac.getBean(MENUSERVICE_BEANID);
	}

}
