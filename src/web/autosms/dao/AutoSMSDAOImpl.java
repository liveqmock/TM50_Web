package web.autosms.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.autosms.model.*;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.DateUtils;
import web.common.util.QueryUtil;

public class AutoSMSDAOImpl extends DBJdbcDaoSupport implements AutoSMSDAO {
	
	/**
	 * <p>이벤트SMS리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AutoSMSEvent> selectAutoSMSEventList(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage ;
		countPerPage = countPerPage * currentPage;
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sql = QueryUtil.getStringQuery("autosms_sql","autosms.event.select");			
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " WHERE "+sSearchType+" LIKE :seach_text ";
		}		
			
		String sqlTail = QueryUtil.getStringQuery("autosms_sql","autosms.event.tail");			
		sql += sqlTail;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				AutoSMSEvent  autoSMSEvent = new AutoSMSEvent();				
				autoSMSEvent.setAutosmsID(rs.getInt("autosmsID"));
				autoSMSEvent.setAutosmsTitle(rs.getString("autosmsTitle"));
				autoSMSEvent.setTemplateSenderPhone(rs.getString("templateSenderPhone"));
				autoSMSEvent.setUserID(rs.getString("userID"));
				autoSMSEvent.setUserName(rs.getString("userName"));
				autoSMSEvent.setState(rs.getString("state"));
				autoSMSEvent.setRegistDate(rs.getString("registDate"));				
	
				return autoSMSEvent;
			}		
		};
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("seach_text", "%"+sSearchText+"%");		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	
	/**
	 * <p>자동SMS 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoSMSEvent(Map<String, String> searchMap) throws DataAccessException{
		String searchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.event.totalcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " WHERE "+searchType+" LIKE '%" + sSearchText + "%' ";
		}
		return  getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	
	
	/**
	 * <p>자동SMS보기 
	 * @param autosmsID
	 * @throws DataAccessException
	 */
	public AutoSMSEvent viewAutoSMSEvent(int autosmsID) throws DataAccessException{
		AutoSMSEvent autoSMSEvent = new AutoSMSEvent();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.event.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
			//넘겨받은 파라미터를 세팅한다. 
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("autosmsID", new Integer(autosmsID));
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
			}catch(EmptyResultDataAccessException e1){
			}
			
			if(resultMap!=null){
				autoSMSEvent.setAutosmsID(autosmsID);
				autoSMSEvent.setAutosmsTitle((String)resultMap.get("autosmsTitle"));
				autoSMSEvent.setDescription((String)resultMap.get("Description"));
				autoSMSEvent.setUserID((String)resultMap.get("userID"));
				autoSMSEvent.setUserName((String)resultMap.get("userName"));
				autoSMSEvent.setTemplateSenderPhone((String)resultMap.get("templateSenderPhone"));
				autoSMSEvent.setTemplateMsg((String)resultMap.get("templateMsg"));			
				autoSMSEvent.setState((String)resultMap.get("state"));					
			}else{
				return autoSMSEvent;
			}
		return autoSMSEvent;
	}
	
	
	/**
	 * <p>이벤트SMS등록
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAutoSMSEvent(AutoSMSEvent autoSMSEvent) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.event.insert"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("autosmsTitle", autoSMSEvent.getAutosmsTitle());
		param.put("userID", autoSMSEvent.getUserID());
		param.put("description", autoSMSEvent.getDescription());
		param.put("templateSenderPhone", autoSMSEvent.getTemplateSenderPhone());
		param.put("templateMsg", autoSMSEvent.getTemplateMsg());
		param.put("state", autoSMSEvent.getState());		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	
	/**
	 * <p>이벤트SMS수정
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAutoSMSEvent(AutoSMSEvent autoSMSEvent) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.event.update"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("autosmsID", autoSMSEvent.getAutosmsID());
		param.put("autosmsTitle", autoSMSEvent.getAutosmsTitle());		
		param.put("userID", autoSMSEvent.getUserID());
		param.put("description", autoSMSEvent.getDescription());
		param.put("templateSenderPhone", autoSMSEvent.getTemplateSenderPhone());
		param.put("templateMsg", autoSMSEvent.getTemplateMsg());
		param.put("state", autoSMSEvent.getState());		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>이벤트SMS삭제
	 * @param autoSMSEvent
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoSMSEvent(int autosmsID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.event.delete"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("autosmsID", new Integer(autosmsID));		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	
	/**
	 * <p>자동SMS sendqueue 삭제 
	 * @param autosmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoSMSSendQueue(int autosmsID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.event.deleteQueue"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("autosmsID", new Integer(autosmsID));		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	
	/**
	 * <p>자동SMS statistic 삭제 
	 * @param autosmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAutoSMSStatistic(int autosmsID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.event.deleteStatistic"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("autosmsID", new Integer(autosmsID));		
		return getSimpleJdbcTemplate().update(sql, param);
	}

	/**
	 * <p>자동SMS - 시간별 통계 
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AutoSMSStatistic> statisticHourly(Map<String, Object> searchMap) throws DataAccessException{
		
		int autosmsID = (Integer)searchMap.get("autosmsID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String checkDate = year+month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.statistic.hourly"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("autosmsID", new Integer(autosmsID));
		param.put("checkDate", checkDate);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				AutoSMSStatistic autoSMSStatistic = new AutoSMSStatistic();				
				autoSMSStatistic.setStandard(rs.getString("standard"));
				autoSMSStatistic.setSendTotal(rs.getInt("sendTotal"));
				autoSMSStatistic.setSuccessTotal(rs.getInt("successTotal"));
				autoSMSStatistic.setFailTotal(rs.getInt("failTotal"));
				autoSMSStatistic.setReadyTotal(rs.getInt("readyTotal"));
				autoSMSStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return autoSMSStatistic;
			}		
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>자동SMS 기간중 일자별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AutoSMSStatistic> statisticDaily(Map<String, Object> searchMap) throws DataAccessException{
		
		int autosmsID = (Integer)searchMap.get("autosmsID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String checkDate = year+month;
		if(day.equals("all")){
			checkDate+="%";
		}else{
			checkDate+=day+"%";
		}
		
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.statistic.daily"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("autosmsID", new Integer(autosmsID));
		param.put("checkDate", checkDate);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				AutoSMSStatistic autoSMSStatistic = new AutoSMSStatistic();				
				autoSMSStatistic.setStandard(rs.getString("standard"));
				autoSMSStatistic.setSendTotal(rs.getInt("sendTotal"));
				autoSMSStatistic.setSuccessTotal(rs.getInt("successTotal"));
				autoSMSStatistic.setFailTotal(rs.getInt("failTotal"));
				autoSMSStatistic.setReadyTotal(rs.getInt("readyTotal"));
				autoSMSStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return autoSMSStatistic;
			}		
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	

	/**
	 * <p>자동SMS 기간중 월별 통계
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AutoSMSStatistic> statisticMonthly(Map<String, Object> searchMap) throws DataAccessException{
		
		int autosmsID = (Integer)searchMap.get("autosmsID");
		String year = (String)searchMap.get("year");
		//String month = (String)searchMap.get("month");
		String checkDate = year;

		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.statistic.monthly"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("autosmsID", new Integer(autosmsID));
		param.put("checkDate", checkDate);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				AutoSMSStatistic autoSMSStatistic = new AutoSMSStatistic();				
				autoSMSStatistic.setStandard(rs.getString("standard"));
				autoSMSStatistic.setSendTotal(rs.getInt("sendTotal"));
				autoSMSStatistic.setSuccessTotal(rs.getInt("successTotal"));
				autoSMSStatistic.setFailTotal(rs.getInt("failTotal"));
				autoSMSStatistic.setReadyTotal(rs.getInt("readyTotal"));
				autoSMSStatistic.setSuccessRatio(rs.getBigDecimal("successRatio"));
				
				return autoSMSStatistic;
			}		
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	
	/**
	 * <p>자동SMS 기간중 총 시도 건수
	 * @param massmailID
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws DataAccessException
	 */
	public int getSendTotal(Map<String, Object> searchMap) throws DataAccessException{
		
		int autosmsID = (Integer)searchMap.get("autosmsID");
		String startDate = (String)searchMap.get("startDate");
		String endDate = (String)searchMap.get("endDate");
		
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.statistic.sendTotal"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("autosmsID", new Integer(autosmsID));
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
	
	/**
	 * <p>자동SMS 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AutoSMSPerson> personPreview(Map<String, Object> searchMap) throws DataAccessException{
		int autosmsID = (Integer)searchMap.get("autosmsID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String standard = (String)searchMap.get("standard");
		String type= (String)searchMap.get("type");
		String key= (String)searchMap.get("key");
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		
		int start = (currentPage - 1) * countPerPage;
		//countPerPage = countPerPage * currentPage;
		
		String startDate =year+"-"+month+"-"+day;
		String endDate =startDate+" 23:59:59";
		if(day.equals("all")){
			startDate =  year+"-"+month+"-01";
			int lastDay = 0;
			try{
				lastDay = DateUtils.lastDay(Integer.parseInt(year), Integer.parseInt(month));
			}catch(Exception e){}
			endDate = year+"-"+month+"-"+lastDay+" 23:59:59";
		}
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewselect");
		if(standard.equals("monthly")){
			sql+= " tm_autosms_sendresult_"+key+" ";			
		}else{
			sql+= " tm_autosms_sendresult_"+year+month+" ";
		}
		if(standard.equals("hourly")){
			if(key.length() == 1){key="0"+key;}
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewhourlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewhourlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewhourlyfail");
			}else if(type.equals("ready")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewhourlyopen");
			}
		}else if(standard.equals("daily")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewdailytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewdailysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewdailyfail");
			}else if(type.equals("ready")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewdailyready");
			}
		}else if(standard.equals("monthly")){
			startDate = year+"-01-01";
			endDate = year+"-12-31 23:59:59";
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewmonthlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewmonthlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewmonthlyfail");
			}else if(type.equals("ready")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewmonthlyready");
			}
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";
		}
		if(currentPage > 0 && countPerPage > 0){
			String sqlTail = QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreview.tail");
			sql += sqlTail;
		}
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("autosmsID", new Integer(autosmsID));
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("key", key);
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				AutoSMSPerson autoSMSPerson = new AutoSMSPerson();
				autoSMSPerson.setReceiverPhone(rs.getString("receiverPhone"));				
				autoSMSPerson.setRegistDate(rs.getString("registDate"));
				autoSMSPerson.setSmsCode(rs.getString("smsCode"));
				autoSMSPerson.setSmsCodeMsg(rs.getString("smsCodeMsg"));
				return autoSMSPerson;
			}		
		};
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>자동SMS 대상자 미리보기 totalCount
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalPorsonPreview(Map<String, Object> searchMap) throws DataAccessException{
		int autosmsID = (Integer)searchMap.get("autosmsID");
		String year = (String)searchMap.get("year");
		String month = (String)searchMap.get("month");
		String day = (String)searchMap.get("day");
		String standard = (String)searchMap.get("standard");
		String type= (String)searchMap.get("type");
		String key= (String)searchMap.get("key");
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		
		String startDate =year+"-"+month+"-"+day;
		String endDate =startDate+" 23:59:59";
		if(day.equals("all")){
			startDate =  year+"-"+month+"-01";
			int lastDay = 0;
			try{
				lastDay = DateUtils.lastDay(Integer.parseInt(year), Integer.parseInt(month));
			}catch(Exception e){}
			endDate = year+"-"+month+"-"+lastDay+" 23:59:59";
		}
		
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewtotal");
		if(standard.equals("monthly")){
			sql+= " tm_autosms_sendresult_"+key+" ";			
		}else{
			sql+= " tm_autosms_sendresult_"+year+month+" ";
		}
		if(standard.equals("hourly")){
			if(key.length() == 1){key="0"+key;}
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewhourlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewhourlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewhourlyfail");
			}else if(type.equals("ready")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewhourlyready");
			}
		}else if(standard.equals("daily")){
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewdailytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewdailysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewdailyfail");
			}else if(type.equals("ready")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewdailyready");
			}
		}else if(standard.equals("monthly")){
			startDate = year+"-01-01";
			endDate = year+"-12-31 23:59:59";
			if(type.equals("total")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewmonthlytotal");
			}else if(type.equals("success")){
				sql +=QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewmonthlysuccess"); 
			}else if(type.equals("fail")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewmonthlyfail");
			}else if(type.equals("ready")){
				sql += QueryUtil.getStringQuery("autosms_sql","autosms.statistic.porsonpreviewmonthlyready");
			}
		}
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";
		}
		
		//System.out.println("sql : "+sql);
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("autosmsID", new Integer(autosmsID));
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("key", key);
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>자동SMS 월간 통계 ID 별 통계
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public List<AutoSMSStatistic> autoSMSReportMonth(Map<String, Object> searchMap) throws DataAccessException{
		
		
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		String sql = "";
		String sendTime = (String)searchMap.get("sendTime");
		int start = (currentPage - 1) * countPerPage ;
		
		if(Integer.parseInt(sendTime.substring(4))==13){
			sql = QueryUtil.getStringQuery("autosms_sql", "autosms.statistic.reportyear");
			sendTime = sendTime.substring(0,4);
		}else{
			sql = QueryUtil.getStringQuery("autosms_sql", "autosms.statistic.reportmonth");
		}
			
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper(){
			public Object mapRow(ResultSet rs , int rownum) throws SQLException{
				
				AutoSMSStatistic AutoSMSStatistic = new AutoSMSStatistic();
				
				AutoSMSStatistic.setAutosmsID(rs.getInt("autosmsID"));
				AutoSMSStatistic.setAutosmsTitle(rs.getString("autosmsTitle"));
				AutoSMSStatistic.setSendTimeHour(rs.getString("sendTimeHour"));
				AutoSMSStatistic.setSendTotal(rs.getInt("sendTotal"));
				AutoSMSStatistic.setSuccessTotal(rs.getInt("successTotal"));
				AutoSMSStatistic.setFailTotal(rs.getInt("failTotal"));
				
				return AutoSMSStatistic;
			}
		};
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sendtime", sendTime);
		param.put("start", start);
		param.put("count",countPerPage);
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>자동SMS 월간 통계 전체 발송 현황
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */	
	public AutoSMSStatistic autoSMSReportMonthAll(String sendTime) throws DataAccessException{
		Map<String, Object> resultMap = null;
		AutoSMSStatistic AutoSMSStatistic = new AutoSMSStatistic();		
		String sql = "";
		
		if(Integer.parseInt(sendTime.substring(4))==13){
			sql = QueryUtil.getStringQuery("autosms_sql", "autosms.statistic.reportyearall");
			sendTime = sendTime.substring(0,4);
		}else{
			sql = QueryUtil.getStringQuery("autosms_sql", "autosms.statistic.reportmonthall");
		}
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sendtime", sendTime);
		try{
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(Exception e){}
		
		if(resultMap!=null){
			AutoSMSStatistic.setSendTotal(Integer.parseInt(String.valueOf(resultMap.get("sendTotal"))));
			AutoSMSStatistic.setSuccessTotal(Integer.parseInt(String.valueOf(resultMap.get("successTotal"))));
			AutoSMSStatistic.setFailTotal(Integer.parseInt(String.valueOf(resultMap.get("failTotal"))));
			
		}else{
			return AutoSMSStatistic;
		}
		
		return AutoSMSStatistic;
	}
	
	/**
	 * <p>자동SMS 월간통계 카운트 
	 * @param sendTime
	 * @return
	 * @throws DataAccessException
	 */
	public int getTotalCountAutoSMSReportMonth(String sendTime) throws DataAccessException{
				
		String sql = "";
		
		if(Integer.parseInt(sendTime.substring(4))==13){
			sql = QueryUtil.getStringQuery("autosms_sql","autosms.statistic.reportyearcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
			sendTime = sendTime.substring(0,4);
		}else{
			sql = QueryUtil.getStringQuery("autosms_sql","autosms.statistic.reportmonthcount");
		}

		Map<String,Object> param = new HashMap<String, Object>();
		param.put("sendtime", sendTime);
		
		return  getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	
}
