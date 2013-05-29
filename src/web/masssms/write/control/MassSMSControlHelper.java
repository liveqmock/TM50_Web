package web.masssms.write.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.masssms.write.service.MassSMSService;

public class MassSMSControlHelper {

	private static final String MENUSERVICE_BEANID = "massSMSService";

    public static MassSMSService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (MassSMSService) wac.getBean(MENUSERVICE_BEANID);
    }
}
