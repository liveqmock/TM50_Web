package web.massmail.schedule.service;

import java.util.List;

import org.apache.log4j.Logger;
import web.massmail.schedule.dao.ScheduleDAO;
import web.massmail.schedule.model.ScheduleCalendar;

public class ScheduleServiceImpl implements ScheduleService{
	private ScheduleDAO scheduleDAO = null;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void setScheduleDAO(ScheduleDAO scheduleDAO) {
    	
        this.scheduleDAO = scheduleDAO;         
    }
	

	public List<ScheduleCalendar> listSchedule(String groupID, String yyyy_mm)
	 {
		 List<ScheduleCalendar> resultList = null;		
			try{
				resultList =  scheduleDAO.listSchedule(groupID, yyyy_mm);
			
			}catch(Exception e){
				logger.error(e);
			}
		return resultList;
		 
	 }
	
	public ScheduleCalendar viewSchedule(int massmailID, int scheduleID)
	{
		ScheduleCalendar sc = null;
		
		try{
			
			sc =  scheduleDAO.viewSchedule(massmailID, scheduleID);
			
		
		}catch(Exception e){
			logger.error(e);
		}
		return sc;
		
		
	}

}
