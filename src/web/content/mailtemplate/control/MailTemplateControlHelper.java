package web.content.mailtemplate.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.content.mailtemplate.service.MailTemplateService;

public class MailTemplateControlHelper {
	private static final String MENUSERVICE_BEANID = "mailTemplateService";

    public static MailTemplateService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);

        return (MailTemplateService) wac.getBean(MENUSERVICE_BEANID);
    }
}
