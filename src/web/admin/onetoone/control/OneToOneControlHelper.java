package web.admin.onetoone.control;

import javax.servlet.ServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.admin.onetoone.service.OneToOneService;

public class OneToOneControlHelper {
	private static final String MENUSERVICE_BEANID = "oneToOneService";

    public static OneToOneService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (OneToOneService) wac.getBean(MENUSERVICE_BEANID);
    }
}
