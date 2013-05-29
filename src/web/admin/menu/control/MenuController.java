package web.admin.menu.control;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.admin.menu.service.*;

public class MenuController extends MultiActionController {

	private MenuService menuService = null;

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
	

	
	/**
	 * <p>메뉴 트리 출력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	
	public void treeView(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		out.print( menuService.getJsonTreeMenu() );
	}
	


}