package web.admin.massmailgroup.control;

import javax.servlet.ServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.admin.massmailgroup.service.MassMailGroupService;


public class MassMailGroupControlHelper {
	private static final String MENUSERVICE_BEANID = "massMailGroupService";

    public static MassMailGroupService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (MassMailGroupService) wac.getBean(MENUSERVICE_BEANID);
    }

}
