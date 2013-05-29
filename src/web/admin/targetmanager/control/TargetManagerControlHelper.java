package web.admin.targetmanager.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.admin.targetmanager.service.TargetManagerService;

public class TargetManagerControlHelper {
	
	private static final String MENUSERVICE_BEANID = "targetManagerService";

    public static TargetManagerService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);
        
        return (TargetManagerService) wac.getBean(MENUSERVICE_BEANID);

    }

}
