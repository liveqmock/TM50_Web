package web.admin.board.control;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import web.admin.board.service.BoardService;

public class BoardControlHelper {
	private static final String MENUSERVICE_BEANID = "boardService";

	public static BoardService getUserService(ServletContext ctx) {
		WebApplicationContext wac = WebApplicationContextUtils
	               .getRequiredWebApplicationContext(ctx);

	    return (BoardService) wac.getBean(MENUSERVICE_BEANID);
	}

}
