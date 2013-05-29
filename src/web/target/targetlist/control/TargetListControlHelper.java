package web.target.targetlist.control;

import javax.servlet.ServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.target.targetlist.service.*;


/**
 * <p>ControllHelper는 "파일명.do?method=메소드" 로 처리되는 것이 아니라. jsp에서 페이지가 로딩될때 처리해야 하는 경우에 사용한다. 
 * <p>이 클래스를 사용하면 jsp에서 이 클래스를 이용하여 service모듈에 접근할 수가 있다. 
 * @author coolang
 *
 */
public class TargetListControlHelper {
	
	private static final String MENUSERVICE_BEANID = "targetListService";

    public static TargetListService getUserService(ServletContext ctx) {
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(ctx);
        
        return (TargetListService) wac.getBean(MENUSERVICE_BEANID);

    }

}
