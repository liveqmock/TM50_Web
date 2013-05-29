package web.massmail.schedule.service;

import java.util.List;

import web.massmail.schedule.model.ScheduleCalendar;

public interface ScheduleService {
	
	/**
	 * <p>발송 스케줄 리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 */	
	 
	public List<ScheduleCalendar> listSchedule(String groupID, String yyyy_mm);
	
	
	public ScheduleCalendar viewSchedule(int massmailID, int scheduleID);

}
