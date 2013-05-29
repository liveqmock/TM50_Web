package web.massmail.schedule.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import web.massmail.schedule.model.ScheduleCalendar;

public class ScheduleDAOImpl extends DBJdbcDaoSupport  implements ScheduleDAO
{
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";
	
	
	@SuppressWarnings("unchecked")
	public List<ScheduleCalendar> listSchedule(String groupID, String yyyy_mm) throws DataAccessException
	{
		String sql = QueryUtil.getStringQuery("massmail_sql","massmail.schedule.schedulelist");
		String sqlTail = QueryUtil.getStringQuery("massmail_sql","massmail.schedule.schedule.tail");	

		if(!groupID.equals(""))
			sql += " and g.groupID =  '" + groupID + "' ";

		sql += sqlTail;
	
		Map<String,Object> param = new HashMap<String, Object>();
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				ScheduleCalendar  schedule = new ScheduleCalendar();
				if(db_type.equals(DB_TYPE_ORACLE))
				{
					schedule.setGroupID(rs.getInt("groupID"));
					schedule.setMassMailID(rs.getInt("massmailID"));
					schedule.setMassMailTitle(rs.getString("massmailTitle"));
					schedule.setScheduleID(rs.getInt("scheduleID"));
					schedule.setSendScheduleDate(rs.getString("sendScheduleDate"));
					schedule.setUserID(rs.getString("userID"));
					schedule.setUserLevel(rs.getString("userLevel"));
					schedule.setUserName(rs.getString("userName"));
					schedule.setDescription(rs.getString("description"));
					schedule.setSendType(rs.getInt("sendType"));
					schedule.setState(rs.getString("state"));
					schedule.setTargetTotalCount(rs.getString("targetTotalCount"));
				}
				else if(db_type.equals(DB_TYPE_MYSQL))
				{
					schedule.setGroupID(rs.getInt("g.groupID"));
					schedule.setMassMailID(rs.getInt("s.massmailID"));
					schedule.setMassMailTitle(rs.getString("i.massmailTitle"));
					schedule.setScheduleID(rs.getInt("s.scheduleID"));
					schedule.setSendScheduleDate(rs.getString("s.sendScheduleDate"));
					schedule.setUserID(rs.getString("i.userID"));
					schedule.setUserLevel(rs.getString("u.userLevel"));
					schedule.setUserName(rs.getString("u.userName"));
					schedule.setDescription(rs.getString("i.description"));
					schedule.setSendType(rs.getInt("i.sendType"));
					schedule.setState(rs.getString("s.state"));
					schedule.setTargetTotalCount(rs.getString("s.targetTotalCount"));
				}
				else if(db_type.equals(DB_TYPE_MSSQL))
				{
					schedule.setGroupID(rs.getInt("groupID"));
					schedule.setMassMailID(rs.getInt("massmailID"));
					schedule.setMassMailTitle(rs.getString("massmailTitle"));
					schedule.setScheduleID(rs.getInt("scheduleID"));
					schedule.setSendScheduleDate(rs.getString("sendScheduleDate"));
					schedule.setUserID(rs.getString("userID"));
					schedule.setUserLevel(rs.getString("userLevel"));
					schedule.setUserName(rs.getString("userName"));
					schedule.setDescription(rs.getString("description"));
					schedule.setSendType(rs.getInt("sendType"));
					schedule.setState(rs.getString("state"));
					schedule.setTargetTotalCount(rs.getString("targetTotalCount"));
				}
				
				
				return schedule;
			}			
		};
		
		if(db_type.equals(DB_TYPE_ORACLE))
			param.put("yyyy_mm", yyyy_mm);
		else if(db_type.equals(DB_TYPE_MYSQL) || db_type.equals(DB_TYPE_MSSQL))
			param.put("yyyy_mm", yyyy_mm+"%");
		
		return (List<ScheduleCalendar>)getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
	}
	
	@SuppressWarnings("unchecked")
	public ScheduleCalendar viewSchedule(int massmailID, int scheduleID) throws DataAccessException
	{
		
		String sql = null;
		ScheduleCalendar schedule = new ScheduleCalendar();
		
		sql = QueryUtil.getStringQuery("massmail_sql","massmail.schedule.viewSchedule");
		
		Map<String,Object> param = new HashMap<String, Object>();					
		param.put("massmailID", massmailID);
		param.put("scheduleID", scheduleID);		 	
		
		Map resultMap = null;
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			
			schedule.setMassMailID(Integer.parseInt(String.valueOf(resultMap.get("MassMailID"))));
			schedule.setMassMailTitle((String)resultMap.get("MassMailTitle"));
			schedule.setScheduleID(Integer.parseInt(String.valueOf(resultMap.get("ScheduleID"))));
			schedule.setSendScheduleDate(DateUtils.getStringDate(resultMap.get("SendScheduleDate").toString()));
			schedule.setUserID(String.valueOf(resultMap.get("UserID")));
			schedule.setUserLevel((String)resultMap.get("UserLevel"));
			schedule.setGroupID(Integer.parseInt(String.valueOf(resultMap.get("groupID"))));
			schedule.setUserName((String)resultMap.get("UserName"));	
			schedule.setDescription((String)(resultMap.get("description")==null?"":resultMap.get("description")));			
			schedule.setSendType(Integer.parseInt(String.valueOf(resultMap.get("sendType"))));
			schedule.setState((String)resultMap.get("state"));	
			schedule.setTargetTotalCount(String.valueOf(resultMap.get("targetTotalCount")));
			schedule.setStatisticsOpenType(Integer.parseInt(String.valueOf(resultMap.get("statisticsOpenType"))));
			
		}
		return schedule;
		
	}

}
