package web.massmail.statistic.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.massmail.statistic.service.MassMailStatService;

public class MassMailStatControlHelper {

	private static final String MENUSERVICE_BEANID = "massMailStatService";

    public static MassMailStatService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (MassMailStatService) wac.getBean(MENUSERVICE_BEANID);
    }
}
