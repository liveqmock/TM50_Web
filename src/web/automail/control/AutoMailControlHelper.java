package web.automail.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.automail.service.AutoMailService;;

public class AutoMailControlHelper {
	private static final String MENUSERVICE_BEANID = "autoMailService";

    public static AutoMailService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (AutoMailService) wac.getBean(MENUSERVICE_BEANID);
    }
}
