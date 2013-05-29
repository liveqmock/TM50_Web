package web.masssms.schedule.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.common.util.*;
import web.masssms.schedule.service.ScheduleService;


public class ScheduleController extends MultiActionController{
	private ScheduleService smsScheduleService= null;
	private String message = "";
	
	public void setSmsScheduleService(ScheduleService scheduleService){		
		this.smsScheduleService = scheduleService;
	}


	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/pages/masssms/schedule/masssms_schedule.jsp");
	}
	
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception
	{
		String groupID = ServletUtil.getParamStringDefault(req,"groupID","0");
		String sYear =ServletUtil.getParamString(req, "sYear");
		String sMonth =ServletUtil.getParamString(req, "sMonth");
		if(sMonth.length()==1)
			sMonth = "0"+sMonth;
		
		req.setAttribute("message", this.message);
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
	
		return new ModelAndView("/pages/masssms/schedule/masssms_schedule_proc.jsp?method=list","scheduleList", smsScheduleService.listSchedule(groupID, sYear+"-"+sMonth));
	}
	

	public ModelAndView view(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int masssmsID = ServletUtil.getParamInt(req,"masssmsID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		
		return new ModelAndView("/pages/masssms/schedule/masssms_schedule_proc.jsp?method=view","schedule", smsScheduleService.viewSchedule(masssmsID, scheduleID));		
	}

}
