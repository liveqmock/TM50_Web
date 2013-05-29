package web.admin.onetoone.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


import web.admin.onetoone.service.OneToOneService;
import web.common.util.ServletUtil;

public class OneToOneController extends MultiActionController{
	
	private OneToOneService oneToOneService = null;
	
	public void setOneToOneService(OneToOneService oneToOneService){
		this.oneToOneService = oneToOneService;
	}
	
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
		return new ModelAndView("/admin/onetoone/onetoone.jsp");
	}
	
	/**
	 * <p>원투원리스트 출력
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */	
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		return new ModelAndView("/admin/onetoone/onetoone_proc.jsp?method=list","onetooneList", oneToOneService.listOneToOne());
	}
	

	/**
	 * <p>등록된 타게팅에 대한 원투원리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listOnetoOneByTargetID(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		String[] targetIDs = ServletUtil.getParamStringArray(req, "targetID");
		return new ModelAndView("/page/common/target_onetoone.jsp?method=listOnetoOneByTargetID","onetooneTargetList", oneToOneService.listOneToOneByTargetID(targetIDs));
	}
}
