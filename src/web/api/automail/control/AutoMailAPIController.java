package web.api.automail.control;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.common.util.ServletUtil;
import web.api.automail.model.AutoMailAPI;
import web.api.automail.service.AutoMailAPIService;

public class AutoMailAPIController extends MultiActionController 
{
	
	private AutoMailAPIService autoMailAPIService = null; 	

	
	public void setAutoMailAPIService(AutoMailAPIService autoMailAPIService) {
		this.autoMailAPIService = autoMailAPIService;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/index.jsp");
	}
	
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception
	{
		
		String automailID =ServletUtil.getParamString(req, "automailID");
		String mailTitle =ServletUtil.getParamString(req, "mailTitle");
		String mailContent =ServletUtil.getParamString(req, "mailContent");
		String senderMail =ServletUtil.getParamString(req, "senderMail");
		String senderName =ServletUtil.getParamString(req, "senderName");
		String receiverName =ServletUtil.getParamString(req, "receiverName");
		String email =ServletUtil.getParamString(req, "email");
		String onetooneInfo =ServletUtil.getParamString(req, "onetooneInfo");
		String reserveDate =ServletUtil.getParamString(req, "reserveDate");
		
		
		AutoMailAPI autoMailAPI = new AutoMailAPI(automailID, mailTitle, mailContent,
				senderMail, senderName, receiverName,
				email, onetooneInfo, reserveDate);
		
		
		
		autoMailAPIService.insertAutoMail_queue(autoMailAPI);
		
		return new ModelAndView("/index.jsp");
	}
	

}
