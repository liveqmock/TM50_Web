package web.target.sendhistory.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import  web.target.sendhistory.service.SendHistoryService;

public class SendHistoryControllerHelper {

	private static final String MENUSERVICE_BEANID = "sendHistoryService";

    public static SendHistoryService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (SendHistoryService) wac.getBean(MENUSERVICE_BEANID);
    }
}
