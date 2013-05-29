package web.api.autosms.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.api.autosms.service.AutosmsAPIService;

public class AutosmsAPIController extends MultiActionController 
{
	private AutosmsAPIService autosmsAPIService = null;
	
	public void setAutosmsAPIService(AutosmsAPIService autosmsAPIService)
	{
		this.autosmsAPIService=autosmsAPIService;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/index.jsp");
	}
	
	
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception
	{
		
		return new ModelAndView("/index.jsp");
	}

}
