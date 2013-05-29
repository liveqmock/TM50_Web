package web.masssms.schedule.service;

import java.util.List;

import web.masssms.schedule.model.ScheduleCalendar;

public interface ScheduleService {
	
	/**
	 * <p>발송 스케줄 리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 */	
	 
	public List<ScheduleCalendar> listSchedule(String groupID, String yyyy_mm);
	
	
	
	/**
	 * <p>스케줄 상세보기 
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 */
	public ScheduleCalendar viewSchedule(int masssmsID, int scheduleID);

}
