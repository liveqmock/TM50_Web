package web.common.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.common.service.CommonAccessService;

public class CommonAccessContolHelper {
	
	private static final String MENUSERVICE_BEANID = "commonAccessService";

	public static CommonAccessService getUserService(ServletContext ctx) {
		WebApplicationContext wac = WebApplicationContextUtils
	               .getRequiredWebApplicationContext(ctx);

	    return (CommonAccessService) wac.getBean(MENUSERVICE_BEANID);
	}

}
