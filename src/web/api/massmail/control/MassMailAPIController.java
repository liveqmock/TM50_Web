package web.api.massmail.control;



import web.api.massmail.service.MassMailAPIService;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class MassMailAPIController extends MultiActionController{
	
	private MassMailAPIService massMailAPIService;
	
	public void setMassMailAPIService(MassMailAPIService massMailAPIService) {
		this.massMailAPIService = massMailAPIService;
	}
	
	

}
