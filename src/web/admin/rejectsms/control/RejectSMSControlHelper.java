package web.admin.rejectsms.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.admin.rejectsms.service.RejectSMSService;

public class RejectSMSControlHelper {
	private static final String MENUSERVICE_BEANID = "rejectSMSService";

	public static RejectSMSService getUserService(ServletContext ctx) {
		WebApplicationContext wac = WebApplicationContextUtils
	               .getRequiredWebApplicationContext(ctx);

	    return (RejectSMSService) wac.getBean(MENUSERVICE_BEANID);
	}

}
