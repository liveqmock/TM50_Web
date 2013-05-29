package web.autosms.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.autosms.service.AutoSMSService;;

public class AutoSMSControlHelper {
	private static final String MENUSERVICE_BEANID = "autoSMSService";

    public static AutoSMSService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (AutoSMSService) wac.getBean(MENUSERVICE_BEANID);
    }
}
