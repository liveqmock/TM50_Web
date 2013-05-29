package web.massmail.write.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.massmail.write.service.MassMailService;

public class MassMailControlHelper {

	private static final String MENUSERVICE_BEANID = "massMailService";

    public static MassMailService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (MassMailService) wac.getBean(MENUSERVICE_BEANID);
    }
}
