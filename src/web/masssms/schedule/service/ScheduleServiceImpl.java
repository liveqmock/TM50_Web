package web.masssms.schedule.service;

import java.util.List;

import org.apache.log4j.Logger;
import web.masssms.schedule.dao.ScheduleDAO;
import web.masssms.schedule.model.ScheduleCalendar;

public class ScheduleServiceImpl implements ScheduleService{
	private ScheduleDAO smsScheduleDAO = null;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void setSmsScheduleDAO(ScheduleDAO scheduleDAO) {
    	
        this.smsScheduleDAO = scheduleDAO;         
    }
	

	
	/**
	 * <p>스케줄리스트 
	 */
	public List<ScheduleCalendar> listSchedule(String groupID, String yyyy_mm)
	 {
		 List<ScheduleCalendar> resultList = null;		
			try{
				resultList =  smsScheduleDAO.listSchedule(groupID, yyyy_mm);
			
			}catch(Exception e){
				logger.error(e);
			}
		return resultList;
		 
	 }
	
	
	/**
	 * <p>스케줄상세보기 
	 */
	public ScheduleCalendar viewSchedule(int masssmsID, int scheduleID)
	{
		ScheduleCalendar sc = null;		
		try{			
			sc =  smsScheduleDAO.viewSchedule(masssmsID, scheduleID);		
		}catch(Exception e){
			logger.error(e);
		}
		return sc;
		
		
	}

}
