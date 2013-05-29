package web.api.massmail.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.api.massmail.service.MassMailAPIService;

public class MassMailAPIControlHelper {
	
	
	private static final String MENUSERVICE_BEANID = "massMailAPIService";

    public static MassMailAPIService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (MassMailAPIService) wac.getBean(MENUSERVICE_BEANID);
    }

}
