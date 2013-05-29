package web.admin.usergroup.control;

import javax.servlet.ServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.admin.usergroup.service.UserGroupService;

public class UserGroupControlHelper {

	private static final String MENUSERVICE_BEANID = "userGroupService";

    public static UserGroupService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (UserGroupService) wac.getBean(MENUSERVICE_BEANID);
    }
}
