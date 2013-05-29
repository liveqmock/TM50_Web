package web.content.poll.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.content.poll.service.PollService;

public class PollControlHelper {
	private static final String MENUSERVICE_BEANID = "pollService";

    public static PollService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(ctx);
        return (PollService) wac.getBean(MENUSERVICE_BEANID);
    }
}
