package web.admin.dbconnect.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import org.springframework.dao.DataAccessException;
import web.admin.dbconnect.model.DbConnectColumn;
import web.admin.dbconnect.model.DbConnectInfo;
import web.common.util.*;
import web.common.dao.DBJdbcDaoSupport;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.common.util.PropertiesUtil;

public class DbConnectDAOImpl extends DBJdbcDaoSupport implements DbConnectDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";
	
	/**
	 * <p>연동디비리스트 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbConnectInfo> listDBConnectInfo(Map<String, Object> searchMap) throws DataAccessException{
		
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String useYN = (String)searchMap.get("useYN");
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		int start = (currentPage - 1) * countPerPage;
		countPerPage = countPerPage * currentPage;
		
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.selectinfo");		
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";	
		}
		if(useYN!=null && !useYN.equals("")){
			sql += " AND c.useYN='"+useYN+"'";	
		}
		
		String sqlTail =QueryUtil.getStringQuery("admin_sql","admin.dbconnect.selectinfotail");		
		sql += sqlTail;

		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbConnectInfo dbConnectInfo = new DbConnectInfo();
				dbConnectInfo.setDbID(rs.getString("dbID"));
				dbConnectInfo.setDbName(rs.getString("dbName"));
				dbConnectInfo.setConnectDBName(rs.getString("connectDBName"));
				dbConnectInfo.setTableName(rs.getString("tableName"));
				dbConnectInfo.setQueryText(rs.getString("queryText"));
				dbConnectInfo.setUserID(rs.getString("userID"));
				dbConnectInfo.setUseYN(rs.getString("useYN"));
				dbConnectInfo.setRegistDate(rs.getString("registDate"));
				dbConnectInfo.setUpdateScheduleDate(rs.getString("updateScheduleDate"));
				dbConnectInfo.setUpdateStartDate(rs.getString("updateStartDate"));
				dbConnectInfo.setUpdateEndDate(rs.getString("updateEndDate"));
				dbConnectInfo.setState(rs.getString("state"));
				dbConnectInfo.setTotalCount(rs.getInt("totalCount"));
				dbConnectInfo.setDefaultYN(rs.getString("defaultYN"));
				return dbConnectInfo;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}

	/**
	 * <p>연동디비리스트 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbConnectInfo> listDBConnectHistoryInfo(Map<String, Object> searchMap) throws DataAccessException{
		
		String dbID = (String)searchMap.get("dbID");
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		int start = (currentPage - 1) * countPerPage;
		countPerPage = countPerPage * currentPage;
		
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.selecthistoryinfo");		
		
		String sqlTail =QueryUtil.getStringQuery("admin_sql","admin.dbconnect.selecthistoryinfotail");		
		sql += sqlTail;
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbConnectInfo dbConnectInfo = new DbConnectInfo();
				dbConnectInfo.setDbID(rs.getString("dbID"));
				dbConnectInfo.setTableName(rs.getString("tableName"));
				//System.out.println(dbConnectInfo.getTableName());
				dbConnectInfo.setUpdateStartDate(rs.getString("updateStartDate")); 
				dbConnectInfo.setUpdateEndDate(rs.getString("updateEndDate"));
				dbConnectInfo.setState(rs.getString("state"));
				dbConnectInfo.setTotalCount(rs.getInt("rs_count"));
				return dbConnectInfo;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", new Integer(dbID));
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>연동디비리스트 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountDBConnectInfo(Map<String, Object> searchMap) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.totalcountinfo");
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";	
		}
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>연동디비리스트 카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountDBConnectHistoryInfo(Map<String, Object> searchMap) throws DataAccessException{
		
		String dbID = (String)searchMap.get("dbID");
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.totalcounthistoryinfo");
		sql ="SELECT count(*) FROM ("+sql+" WHERE dbID = '"+dbID+"' GROUP BY tableName ) A ";
	    //System.out.println(sql);
		//Map<String,Object> param = new HashMap<String, Object>();		
		//param.put("dbID ", dbID );
		
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>고객연동디비 기본정보 입력 
	 * @param dbConnectInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertConnectDBInfo(DbConnectInfo dbConnectInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.insertinfo");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("dbID",dbConnectInfo.getDbID());
		param.put("connectDBName",dbConnectInfo.getConnectDBName());
		param.put("updateType",dbConnectInfo.getUpdateType());
		param.put("updateValue",dbConnectInfo.getUpdateValue());		
		param.put("updateScheduleDate",dbConnectInfo.getUpdateScheduleDate());
		param.put("queryText",dbConnectInfo.getQueryText());
		param.put("tableName",dbConnectInfo.getTableName());
		param.put("userID",dbConnectInfo.getUserID());		
		param.put("useYN",dbConnectInfo.getUseYN());
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>고객연동디비 기본정보 수정 
	 * @param dbConnectInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updateConnectDBInfo(DbConnectInfo dbConnectInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.updateinfo");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("connectDBName",dbConnectInfo.getConnectDBName());
		param.put("updateType",dbConnectInfo.getUpdateType());
		param.put("updateValue",dbConnectInfo.getUpdateValue());		
		param.put("updateScheduleDate",dbConnectInfo.getUpdateScheduleDate());
		param.put("queryText",dbConnectInfo.getQueryText());
		//param.put("tableName",dbConnectInfo.getTableName());
		param.put("userID",dbConnectInfo.getUserID());		
		param.put("useYN",dbConnectInfo.getUseYN());
		param.put("dbID",dbConnectInfo.getDbID());
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>고객연동디비 컬럼정보 입력 
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertConnectDBColumn(Map<String, Object>[] maps) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbconnect.insertcolumn"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}
	
	/**
	 * <p>고객연동디비 컬럼정보 수정
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updateConnectDBColumn(Map<String, Object>[] maps) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbconnect.updatecolumn"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}
	
	/**
	 * <p>고객연동디비 컬럼정보 삭제
	 * @param dbConnectColumn
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteConnectDBColumn(String dbID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbconnect.deletecolumn"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("dbID", new Integer(dbID));
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	
	/**
	 * <p>고객연동디비 기본정보 보기 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public DbConnectInfo viewConnectDBInfo(String dbID) throws DataAccessException{
		DbConnectInfo dbConnectInfo = new DbConnectInfo();
		Map<String,Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbconnect.viewinfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("dbID", dbID);	
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			dbConnectInfo.setDbID((String)(resultMap.get("dbID")));
			dbConnectInfo.setConnectDBName((String)(resultMap.get("connectDBName")));
			dbConnectInfo.setDbName((String)(resultMap.get("dbName")));
			dbConnectInfo.setTableName((String)(resultMap.get("tableName")));
			dbConnectInfo.setUserID((String)(resultMap.get("userID")));
			dbConnectInfo.setQueryText((String)(resultMap.get("queryText")));
			dbConnectInfo.setUpdateType((String)(resultMap.get("updateType")));
			dbConnectInfo.setUpdateValue(Integer.parseInt(String.valueOf(resultMap.get("updateValue"))));
			dbConnectInfo.setUpdateScheduleDate(String.valueOf(resultMap.get("updateScheduleDate")));
			dbConnectInfo.setUpdateScheduleHour(String.valueOf(resultMap.get("updateScheduleHour")));
			dbConnectInfo.setUpdateScheduleMinute(String.valueOf(resultMap.get("updateScheduleMinute")));			
			dbConnectInfo.setUpdateStartDate(StringUtil.replace(String.valueOf(resultMap.get("updateStartDate")),"null","-"));
			dbConnectInfo.setUpdateEndDate(StringUtil.replace(String.valueOf(resultMap.get("updateEndDate")),"null","-"));
			dbConnectInfo.setTotalCount(Integer.parseInt(String.valueOf(resultMap.get("totalCount"))));	
			dbConnectInfo.setState((String)(resultMap.get("stat")));
			dbConnectInfo.setRegistDate(String.valueOf(resultMap.get("registDate")));
	
		}else{
			return dbConnectInfo;
		}
		return dbConnectInfo;
	}
	
	
	
	/**
	 * <p>고객연동디비 컬럼리스트 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbConnectColumn> listConnectDBColumn(String dbID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.selectcolumn");		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbConnectColumn dbConnectColumn = new DbConnectColumn();
				dbConnectColumn.setDbID(rs.getString("dbID"));
				dbConnectColumn.setColumnName(rs.getString("columnName"));
				dbConnectColumn.setColumnType(rs.getString("columnType"));
				dbConnectColumn.setColumnLength(rs.getString("columnLength"));
				dbConnectColumn.setColumnDesc(rs.getString("columnDesc"));
				dbConnectColumn.setRegistDate(rs.getString("registDate"));
				return dbConnectColumn;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID",dbID);
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.getdbinfo");		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID",dbID);
		return getSimpleJdbcTemplate().queryForMap(sql, param);
	}
	
	/**
	 * <p>이미 등록된 dbID가 있는지 확인한다.
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkDBInfo(String dbID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.checkdbid");		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID",dbID);
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>연동 DB 리스트
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbConnectInfo> listDBConnectInfo() throws DataAccessException {
		 
        String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.selectinfo");             
  		if(db_type.equals(DB_TYPE_MSSQL)){
  			sql = sql + QueryUtil.getStringQuery("common_sql","common.notpages");
  		}
        ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
               public Object mapRow(ResultSet rs, int rownum) throws SQLException {
                      DbConnectInfo dbConnectInfo = new DbConnectInfo();
                      dbConnectInfo.setDbID(rs.getString("dbID"));
                      dbConnectInfo.setDbName(rs.getString("dbName"));
                      dbConnectInfo.setConnectDBName(rs.getString("connectDBName"));
                      dbConnectInfo.setTableName(rs.getString("tableName"));
                      dbConnectInfo.setQueryText(rs.getString("queryText"));
                      dbConnectInfo.setUserID(rs.getString("userID"));
                      dbConnectInfo.setUseYN(rs.getString("useYN"));
                      dbConnectInfo.setRegistDate(rs.getString("registDate"));
                      dbConnectInfo.setUpdateScheduleDate(rs.getString("updateScheduleDate"));
                      dbConnectInfo.setUpdateStartDate(rs.getString("updateStartDate"));
                      dbConnectInfo.setUpdateEndDate(rs.getString("updateEndDate"));
                      dbConnectInfo.setState(rs.getString("state"));
                      dbConnectInfo.setTotalCount(rs.getInt("totalCount"));
                      dbConnectInfo.setDefaultYN(rs.getString("defaultYN"));
                      return dbConnectInfo;
               }                   
        };
        
        return  getSimpleJdbcTemplate().query(sql, rowMapper);
  }
	
	/**
	 * <p> tm_connectdb_history의 기존 다음 수집 정보 Delete
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteHistoryNextDBConnect(String dbID) throws DataAccessException{
		//String sql =  QueryUtil.getStringQuery("dbconnect_sql","dbconnect.updatestartdate");
		String sql = "DELETE FROM  tm_connectdb_history WHERE dbID=:dbID AND state = '0'";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("dbID", dbID);
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	/**
	 * <p> tm_connectdb_history의 다음 수집 정보 Insert
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateHistoryNextDBConnect(String dbID, String queryText, String tableName, String registDate) throws DataAccessException{
		//String sql =  QueryUtil.getStringQuery("dbconnect_sql","dbconnect.updatestartdate");
		String sql = "INSERT INTO tm_connectdb_history(dbID, increaselID, queryText, tableName, state, historyDes, registDate)"+
        " VALUES(:dbID, (select IFNULL(max(increaselID)+1,1) from tm_connectdb_history B where B.dbID=:dbID), :queryText, :tableName, '0', '다음 수집 정보' , :registDate) ";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("dbID",dbID);
		param.put("queryText", queryText);
		param.put("tableName", tableName);
		param.put("registDate", registDate);
		return getSimpleJdbcTemplate().update(sql, param);	
	}
}
