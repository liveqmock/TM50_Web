package web.intermail.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.intermail.service.InterMailService;

public class InterMailControlHelper {
	private static final String MENUSERVICE_BEANID = "interMailService";

    public static InterMailService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (InterMailService) wac.getBean(MENUSERVICE_BEANID);
    }
}
