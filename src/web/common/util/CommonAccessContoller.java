package web.common.util;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.common.service.CommonAccessService;




public class CommonAccessContoller extends MultiActionController{
	
				
	
	private CommonAccessService commonAccessService = null;
	
	
	
	public void setCommonAccessService(CommonAccessService commonAccessService) {
		this.commonAccessService = commonAccessService;
	}
	
	public boolean isSendMassMail() 
	{
		boolean	isSend;
		isSend = false;
		String q = "select count(*) from tm_massmail_schedule where state = 14 or state = 16";
		
		
			int a = commonAccessService.getInt(q);
			if(a!=0)
				isSend = true;			
		
		return isSend;		
	}
	
	
	

}
