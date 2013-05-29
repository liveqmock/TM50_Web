package web.masssms.statistic.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.masssms.statistic.service.MassSMSStatService;

public class MassSMSStatControlHelper {

	private static final String MENUSERVICE_BEANID = "massSMSStatService";

    public static MassSMSStatService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (MassSMSStatService) wac.getBean(MENUSERVICE_BEANID);
    }
}
