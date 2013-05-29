package web.masssms.schedule.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;
import web.masssms.schedule.model.ScheduleCalendar;

public class ScheduleDAOImpl extends DBJdbcDaoSupport  implements ScheduleDAO
{
	
	
	
	/**
	 * <p>스케줄리스트 
	 */
	@SuppressWarnings("unchecked")
	public List<ScheduleCalendar> listSchedule(String groupID, String yyyy_mm) throws DataAccessException
	{
		String sql = null;
		
		
		sql = QueryUtil.getStringQuery("masssms_sql","masssms.schedule.schedulelist");
			
					
		String sqlTail = QueryUtil.getStringQuery("masssms_sql","masssms.schedule.schedule.tail");	
		if(!groupID.equals(""))
		{
			sql += " and g.groupID =  '" + groupID + "' ";
		}
		sql += sqlTail;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				ScheduleCalendar  schedule = new ScheduleCalendar();
				
				schedule.setGroupID(rs.getInt("g.groupID"));
				schedule.setMasssmsID(rs.getInt("s.masssmsID"));
				schedule.setMasssmsTitle(rs.getString("i.masssmsTitle"));
				schedule.setScheduleID(rs.getInt("s.scheduleID"));
				schedule.setSendScheduleDate(rs.getString("s.sendScheduleDate"));
				schedule.setUserID(rs.getString("i.userID"));
				schedule.setUserLevel(rs.getString("u.userLevel"));
				schedule.setUserName(rs.getString("u.userName"));
				schedule.setDescription(rs.getString("i.description"));
				schedule.setSendType(rs.getInt("i.sendType"));
				schedule.setState(rs.getString("s.state"));
				schedule.setTargetTotalCount(rs.getString("s.targetTotalCount"));
				
				
					
				return schedule;
			}			
		};
			
		Map<String,Object> param = new HashMap<String, Object>();		
		
		param.put("yyyy_mm", yyyy_mm+"%");		
				
		return (List<ScheduleCalendar>)getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
	}
	
	
	
	/**
	 * <p>스케줄보기 
	 */
	@SuppressWarnings("unchecked")
	public ScheduleCalendar viewSchedule(int masssmsID, int scheduleID) throws DataAccessException
	{
		
		String sql = null;
		ScheduleCalendar schedule = new ScheduleCalendar();
		
		sql = QueryUtil.getStringQuery("masssms_sql","masssms.schedule.viewSchedule");
		
		Map<String,Object> param = new HashMap<String, Object>();					
		param.put("masssmsID", masssmsID);
		param.put("scheduleID", scheduleID);		 	
		
		Map resultMap = null;
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			
			schedule.setMasssmsID(Integer.parseInt(String.valueOf(resultMap.get("masssmsID"))));
			schedule.setMasssmsTitle((String)resultMap.get("masssmsTitle"));
			schedule.setScheduleID(Integer.parseInt(String.valueOf(resultMap.get("scheduleID"))));
			schedule.setSendScheduleDate(resultMap.get("sendScheduleDate").toString());
			schedule.setUserID(String.valueOf(resultMap.get("userID")));
			schedule.setUserLevel((String)resultMap.get("userLevel"));
			schedule.setUserName((String)resultMap.get("userName"));	
			schedule.setDescription((String)resultMap.get("description"));			
			schedule.setSendType(Integer.parseInt(String.valueOf(resultMap.get("sendType"))));
			schedule.setState((String)resultMap.get("state"));	
			schedule.setTargetTotalCount(String.valueOf(resultMap.get("targetTotalCount")));
			
		}
		return schedule;
		
	}

}
