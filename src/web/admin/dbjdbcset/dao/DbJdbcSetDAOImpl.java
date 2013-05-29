package web.admin.dbjdbcset.dao;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.admin.dbjdbcset.model.*;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import web.common.util.StringUtil;
import web.common.util.TmEncryptionUtil;

public class DbJdbcSetDAOImpl  extends DBJdbcDaoSupport   implements DbJdbcSetDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";

	/**
	 * <p>ez_jdbcset에서 리스트를 가져온다.
	 */
	@SuppressWarnings("unchecked")
	public List<DbJdbcSet> listJdbcSet() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.selectjdbc");					
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbJdbcSet  dbJdbcSet = new DbJdbcSet();		
				dbJdbcSet.setJdbcDriverID(rs.getString("driverID"));
				dbJdbcSet.setJdbcDriverName(rs.getString("driverName"));
				dbJdbcSet.setJdbcDriverClass(rs.getString("driverClass"));
				dbJdbcSet.setJdbcSampleURL(rs.getString("sampleURL"));
				return dbJdbcSet;
			}			
		};
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	/**
	 * <p>ez_jdbcset에서 보여준다. 
	 */
	public DbJdbcSet viewJdbcSet(String driverID) throws DataAccessException{
		
		DbJdbcSet dbJdbcSet = new DbJdbcSet();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.viewjdbc"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("driverID", driverID);
		//SQL문이 실행된다. 
		resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);		
		if(resultMap!=null){
			dbJdbcSet.setJdbcDriverID((String)resultMap.get("driverID"));
			dbJdbcSet.setJdbcDriverName((String)resultMap.get("driverName"));
			dbJdbcSet.setJdbcDriverClass((String)resultMap.get("driverClass"));
			dbJdbcSet.setJdbcSampleURL((String)resultMap.get("sampleURL"));
		}else{
			return null;
		}
		
		return dbJdbcSet;
	}
	
	
	/*
	 * <p>ez_dbset에 인서트한다. 
	 * (non-Javadoc)	
	 */
	public int insertDbSet(DbJdbcSet dbJdbcSet) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.insertdb"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbJdbcSet.getDbID());
		param.put("dbName", dbJdbcSet.getDbName());
		param.put("dbURL", dbJdbcSet.getDbURL());
		param.put("dbUserID", dbJdbcSet.getDbUserID());
		param.put("dbUserPWD", dbJdbcSet.getDbUserPWD());
		param.put("driverID", dbJdbcSet.getDriverID());
		param.put("encodingYN", dbJdbcSet.getEncodingYN());
		param.put("description", dbJdbcSet.getDescription());
		param.put("defaultYN", dbJdbcSet.getDefaultYN());
		param.put("useYN", dbJdbcSet.getUseYN());
		param.put("dbAccessKey", dbJdbcSet.getDbAccessKey());
		
		
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	/*
	 * <p>수퍼관리자 권한에 DB정보를 인서트한다. 
	 * (non-Javadoc)	
	 */
	public int insertDbSetAdmin(DbJdbcSet dbJdbcSet) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.insertdbadmin"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbJdbcSet.getDbID());
		
		
		
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	/*
	 * <p>수퍼관리자그룹 권한에 DB정보를 인서트한다. 
	 * (non-Javadoc)	
	 */
	public int insertDbSetAdminGroup(DbJdbcSet dbJdbcSet) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.insertdbadmingroup"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbJdbcSet.getDbID());		
		
		
		return getSimpleJdbcTemplate().update(sql,param);		
	}

	
	/*
	 * <p>tm_dbset에서 삭제 처리 
	 * (non-Javadoc)
	 */
	public int deleteDbSet(String dbID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.deletedb"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbID);
		return  getSimpleJdbcTemplate().update(sql,param);			
	}
	
	/*
	 * <p>tm_user_dbset_auth에서 삭제 처리 
	 * (non-Javadoc)
	 */
	public int deleteDbSetUserAuth(String dbID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.deletedbuserauth"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbID);
		return  getSimpleJdbcTemplate().update(sql,param);			
	}

	
	
	/*
	 * <p>ez_dbset에 업데이트 한다. 
	 */
	public int updateDbSet(DbJdbcSet dbJdbcSet) throws  DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.updatedb"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbJdbcSet.getDbID());
		param.put("dbName", dbJdbcSet.getDbName());
		param.put("dbURL", dbJdbcSet.getDbURL());
		param.put("dbUserID", dbJdbcSet.getDbUserID());
		param.put("dbUserPWD", dbJdbcSet.getDbUserPWD());
		param.put("driverID", dbJdbcSet.getDriverID());
		param.put("encodingYN", dbJdbcSet.getEncodingYN());
		param.put("description", dbJdbcSet.getDescription());
		param.put("defaultYN", dbJdbcSet.getDefaultYN());
		param.put("useYN", dbJdbcSet.getUseYN());
		param.put("dbAccessKey", dbJdbcSet.getDbAccessKey());
		
		
		return  getSimpleJdbcTemplate().update(sql,param);	
	}
	
	/**
	 * <p>ez_dbset, ez_jdbcset의 조인리스트를 가져온다. 
	 */
	@SuppressWarnings("unchecked")
	public List<DbJdbcSet> listDbJdbcSet(Map<String, Object> searchMap) throws DataAccessException{
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String searchSelect = (String)searchMap.get("searchSelect");
		String searchSelectType = (String)searchMap.get("searchSelectType");
		
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		int start = (currentPage - 1) * countPerPage;
		countPerPage = countPerPage * currentPage;
		
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.selectdb");					
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
		}
	    if(searchSelect!=null && !searchSelect.equals("")){
	    	sql += " AND "+searchSelectType+" = '"+searchSelect+"' ";
		}

		String sqlTail =QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.selectdbtail");		
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbJdbcSet  dbJdbcSet = new DbJdbcSet();		
				
				dbJdbcSet.setDbID(rs.getString("dbID"));
				dbJdbcSet.setDriverID(rs.getString("driverID"));
				dbJdbcSet.setDbName(rs.getString("dbName"));
				dbJdbcSet.setJdbcDriverName(rs.getString("driverName"));
				dbJdbcSet.setDbURL(rs.getString("dbURL"));
				dbJdbcSet.setEncodingYN(rs.getString("encodingYN"));
				dbJdbcSet.setDefaultYN(rs.getString("defaultYN"));
				dbJdbcSet.setUseYN(rs.getString("useYN"));
				
				return dbJdbcSet;
			}			
		};

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}

	/**
	 * <p>tm_dbset, tm_jdbcset의 조인리스트의 총 Count를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getListDbJdbcSetTotalCount(Map<String, Object> searchMap) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.totalcount");
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String searchSelect = (String)searchMap.get("searchSelect");
		String searchSelectType = (String)searchMap.get("searchSelectType");
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";	
		}
	    if(searchSelect!=null && !searchSelect.equals("")){
			sql += " AND "+searchSelectType+" = '"+searchSelect+"' ";
		}
	    Map<String,Object> param = new HashMap<String, Object>();
	    param.put("searchText", "%"+searchText+"%");
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
	
	/**
	 *<p>tm_dbset의 dbID의 max값을 가져온다.
	 */
	public int getMaxdbID() throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.getmaxdbid");
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	
	public String getDbAccessKey(String dbID) throws DataAccessException {
		
		Map<String, Object> resultMap = null;
		String result = "";
		
		String sql = QueryUtil.getStringQuery("admin_sql", "admin.dbjdbcset.viewdbAccessKey");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbID);
		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			result = (String)resultMap.get("dbAccessKey");
		}
		
		return result;
	}
	
	/**
	 * <p>ez_dbset을 불러온다. 
	 */

	public DbJdbcSet viewDbSet(String dbID)  throws DataAccessException{
		
		DbJdbcSet dbJdbcSet = new DbJdbcSet();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.viewdb"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbID);

		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);		
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			String sKey = StringUtil.createSecurityKey("TM", dbID, (String)resultMap.get("driverClass"));
			dbJdbcSet.setDbID((String)resultMap.get("dbID"));	
			dbJdbcSet.setDbName((String)resultMap.get("dbName"));
			try {
				dbJdbcSet.setDbURL(TmEncryptionUtil.decrypto((String)resultMap.get("dbURL"), sKey));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				dbJdbcSet.setDbUserID(TmEncryptionUtil.decrypto((String)resultMap.get("dbUserID"), sKey));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				dbJdbcSet.setDbUserPWD(TmEncryptionUtil.decrypto((String)resultMap.get("dbUserPWD"), sKey));
			} catch (Exception e) {
				e.printStackTrace();
			}
			dbJdbcSet.setDriverID((String)resultMap.get("driverID"));
			dbJdbcSet.setEncodingYN((String)resultMap.get("encodingYN"));
			dbJdbcSet.setDefaultYN((String)resultMap.get("defaultYN"));
			dbJdbcSet.setUseYN((String)resultMap.get("useYN"));
			dbJdbcSet.setDescription((String)resultMap.get("description"));
			dbJdbcSet.setJdbcDriverClass((String)resultMap.get("driverClass"));
			dbJdbcSet.setDbAccessKey((String)resultMap.get("dbAccessKey"));
	
		}
		
		return dbJdbcSet;
		
	}
	
	/**
	 * <p>다수의 dbID별 DB리스트 출력 
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbJdbcSet> listUserDbList(String[] dbID)  throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.userdb");		
		
		String sqlWhere = "";
		
		for(int i=0;i<dbID.length;i++){
			if(i==0){
				sqlWhere = " AND dbID = '"+dbID[i]+"'";
			}else{
				sqlWhere += " OR dbID = '"+dbID[i]+"'";
			}
		}
		sql += sqlWhere;		
			
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbJdbcSet  dbJdbcSet = new DbJdbcSet();						
				dbJdbcSet.setDbID(rs.getString("dbID"));
				dbJdbcSet.setDbName(rs.getString("dbName"));
				dbJdbcSet.setDescription(rs.getString("description"));			
				return dbJdbcSet;
			}			
		};
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
		
	}
	
	
	/**
	 * <p>사용중인 타게팅인 dbID가 있는지 확인 
	 * @param dbID
	 * @return
	 */
	public int checkUseDBbydbID(String dbID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.checkusedb");		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbID);
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>어드민일 경우 등록된 모든 db리스트가 나온다.
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbJdbcSet> listDBList(String dbID) throws DataAccessException{
			
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.selectdb");				
		
		if(dbID!=null && !dbID.equals("")){
			sql+=" AND d.dbID='"+dbID+"'";
		}
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbJdbcSet  dbJdbcSet = new DbJdbcSet();		
				
				dbJdbcSet.setDbID(rs.getString("dbID"));
				dbJdbcSet.setDriverID(rs.getString("driverID"));
				dbJdbcSet.setDbName(rs.getString("dbName"));
				dbJdbcSet.setJdbcDriverName(rs.getString("driverName"));
				dbJdbcSet.setDbURL(rs.getString("dbURL"));
				dbJdbcSet.setEncodingYN(rs.getString("encodingYN"));
				dbJdbcSet.setDefaultYN(rs.getString("defaultYN"));
				dbJdbcSet.setUseYN(rs.getString("useYN"));
				dbJdbcSet.setDbUserID(rs.getString("dbUserID"));
				dbJdbcSet.setDbUserPWD(rs.getString("dbUserPWD"));
				dbJdbcSet.setJdbcDriverClass(rs.getString("driverClass"));
				
				return dbJdbcSet;
			}			
		};
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	
	
	public int updateDefault(DbJdbcSet dbJdbcSet) throws  DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.dbjdbcset.updatedefault"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbJdbcSet.getDbID());
		
		
		
		return  getSimpleJdbcTemplate().update(sql,param);	
	}
	
	
}
