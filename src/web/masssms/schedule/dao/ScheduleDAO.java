package web.masssms.schedule.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import web.masssms.schedule.model.ScheduleCalendar;

public interface ScheduleDAO {
	
	/**
	 * <p>발송 스케줄 리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 * @throws DataAccessException
	 */	
	public List<ScheduleCalendar> listSchedule(String groupID, String yyyy_mm) throws DataAccessException;
	
	
	
	/**
	 * <p>스케줄 보기 
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public ScheduleCalendar viewSchedule(int massmailID, int scheduleID) throws DataAccessException;
	


}
