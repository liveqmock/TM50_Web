package web.admin.dbconnect.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.admin.dbconnect.service.DbConnectService;

public class DbConnectControlHelper {
	private static final String MENUSERVICE_BEANID = "dbConnectService";

    public static DbConnectService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);
        
        return (DbConnectService) wac.getBean(MENUSERVICE_BEANID);

    }
}
