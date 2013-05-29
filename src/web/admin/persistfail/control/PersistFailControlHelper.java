package web.admin.persistfail.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.admin.persistfail.service.*;

public class PersistFailControlHelper {
	
	
	private static final String MENUSERVICE_BEANID = "persistFailService";

    public static PersistFailService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);
        
        return (PersistFailService) wac.getBean(MENUSERVICE_BEANID);

    }


}
