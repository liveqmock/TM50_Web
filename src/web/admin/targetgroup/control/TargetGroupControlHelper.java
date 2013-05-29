package web.admin.targetgroup.control;

import javax.servlet.ServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.admin.targetgroup.service.TargetGroupService;


public class TargetGroupControlHelper {
	private static final String MENUSERVICE_BEANID = "targetGroupService";

    public static TargetGroupService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (TargetGroupService) wac.getBean(MENUSERVICE_BEANID);
    }

}
