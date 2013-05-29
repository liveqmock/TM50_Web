package web.target.targetui.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import web.target.targetui.service.TargetUIService;



public class TargetUIControlHelper {
	
	private static final String MENUSERVICE_BEANID = "targetUIService";
	
	 public static TargetUIService getUserService(ServletContext ctx) {
	        WebApplicationContext wac = WebApplicationContextUtils
	                .getRequiredWebApplicationContext(ctx);
	        
	        return (TargetUIService) wac.getBean(MENUSERVICE_BEANID);

	    }


}
