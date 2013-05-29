package web.admin.targetmanager.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.admin.dbjdbcset.model.DbJdbcSet;
import web.admin.targetmanager.model.TargetUIManager;
import web.admin.targetmanager.model.TargetUIManagerSelect;
import web.admin.targetmanager.model.OneToOne;
import web.admin.targetmanager.model.TargetUIManagerWhere;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;


public class TargetManagerDAOImpl extends DBJdbcDaoSupport implements TargetManagerDAO {
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	
	@SuppressWarnings("unchecked")
	public List<TargetUIManager> list(Map<String, Object> searchMap) throws DataAccessException
	{
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
	
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.select");					
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";	
		}
	   
		String sqlTail =QueryUtil.getStringQuery("admin_sql","admin.targetmanager.selecttail");		
		sql += sqlTail;
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetUIManager  targetManager = new TargetUIManager();		
				
				targetManager.setTargetUIManagerID(rs.getInt("targetUIManagerID"));
				targetManager.setTargetUIManagerName(rs.getString("targetUIManagerName"));
				targetManager.setDbID(rs.getInt("dbID"));
				targetManager.setUseYN(rs.getString("useYN"));
				targetManager.setUserID(rs.getString("userID"));
				targetManager.setDescription(rs.getString("description")); 
				targetManager.setDefaultYN(rs.getString("defaultYN"));
				targetManager.setRegistdate(rs.getString("registdate"));
								
				
				return targetManager;
			}			
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	public int getCount(Map<String, Object> searchMap) throws DataAccessException
	{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.count");
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
	
		
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";	
		}
	    
		
		
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	public int getMaxID() throws DataAccessException
	{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.getmaxid");
				
		
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	public int insertTargetManagerInfo(TargetUIManager targetManager)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.targetmanager.insert"); 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetUIManagerName", targetManager.getTargetUIManagerName());
		param.put("selectText", targetManager.getSelectText());
		param.put("fromText", targetManager.getFromText());
		param.put("dbID", targetManager.getDbID());
		param.put("useYN", targetManager.getUseYN());
		param.put("defaultYN", targetManager.getUseYN());
		param.put("userID", targetManager.getUserID());
		param.put("description", targetManager.getDescription());
		param.put("whereText", targetManager.getWhereText());
		
		
		return getSimpleJdbcTemplate().update(sql,param);	
	}
	
	public int updateTargetManagerInfo(TargetUIManager targetManager)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.targetmanager.update"); 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetUIManagerName", targetManager.getTargetUIManagerName());
		param.put("selectText", targetManager.getSelectText());
		param.put("fromText", targetManager.getFromText());
		param.put("dbID", targetManager.getDbID());
		param.put("useYN", targetManager.getUseYN());
		param.put("defaultYN", targetManager.getUseYN());
		param.put("userID", targetManager.getUserID());
		param.put("description", targetManager.getDescription());
		param.put("whereText", targetManager.getWhereText());
		param.put("targetUIManagerID", targetManager.getTargetUIManagerID());
		
		
		return getSimpleJdbcTemplate().update(sql,param);	
	}
	
	
	public int insertTargetManagerSelect(TargetUIManagerSelect targetUIManagerSelect)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.targetmanager.insertselect"); 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetUIManagerID", targetUIManagerSelect.getTargetUIManagerID());
		param.put("selectID", targetUIManagerSelect.getSelectID());
		param.put("selectFieldName", targetUIManagerSelect.getSelectFieldName());
		param.put("onetooneID", targetUIManagerSelect.getOnetooneID());
		param.put("selectDescription", targetUIManagerSelect.getSelectDescription());		
		param.put("csvColumnPos", targetUIManagerSelect.getCsvColumnPos());
		
		
		return getSimpleJdbcTemplate().update(sql,param);	
	}
	
	
	
	public int insertTargetManagerWhere(TargetUIManagerWhere targetUIManagerWhere)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.targetmanager.insertwhere"); 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetUIManagerID", targetUIManagerWhere.getTargetUIManagerID());
		param.put("whereID", targetUIManagerWhere.getWhereID());
		param.put("whereUIName", targetUIManagerWhere.getWhereUIName());
		param.put("whereFieldName", targetUIManagerWhere.getWhereFieldName());
		param.put("whereType", targetUIManagerWhere.getWhereType());
		param.put("dataType", targetUIManagerWhere.getDataType());
		param.put("exceptYN", targetUIManagerWhere.getExceptYN());
		param.put("checkName", targetUIManagerWhere.getCheckName());
		param.put("checkValue", targetUIManagerWhere.getCheckValue());
		param.put("periodStartType", targetUIManagerWhere.getPeriodStartType());
		param.put("periodEndType", targetUIManagerWhere.getPeriodEndType());
		param.put("description", targetUIManagerWhere.getDescription());
		
		
		
		return getSimpleJdbcTemplate().update(sql,param);	
	}
	
	
	public TargetUIManager view(int targetUIManagerID)  throws DataAccessException{
		
		TargetUIManager targetManager = new TargetUIManager();
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.targetmanager.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetUIManagerID", new Integer(targetUIManagerID));
		
		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);		
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			targetManager.setTargetUIManagerID(Integer.parseInt(String.valueOf(resultMap.get("targetUIManagerID"))));	
			targetManager.setDbID(Integer.parseInt(String.valueOf(resultMap.get("dbID"))));	
			targetManager.setTargetUIManagerName((String)resultMap.get("targetUIManagerName"));
			targetManager.setFromText((String)resultMap.get("fromText"));
			targetManager.setDescription((String)resultMap.get("description"));
			targetManager.setUseYN((String)resultMap.get("useYN"));
			targetManager.setDefaultYN((String)resultMap.get("defaultYN"));
			targetManager.setWhereText((String)resultMap.get("whereText"));
		}
		
		return targetManager;
		
		
		
		
		
	}
	
	
	public Map<String, Object> getDBInfo(String dbID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.getdbinfo");				
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID",	dbID);
		return getSimpleJdbcTemplate().queryForMap(sql, param); 
	}
	
	/**
	 * <p>db 리스트를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbJdbcSet> getDBList() throws DataAccessException{
	
		String sql =QueryUtil.getStringQuery("admin_sql","admin.targetmanager.getdblist");		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbJdbcSet  dbJdbcSet = new DbJdbcSet();						
				dbJdbcSet.setDbID(rs.getString("dbID"));			
				dbJdbcSet.setDbName(rs.getString("dbName"));				
				dbJdbcSet.setDefaultYN(rs.getString("defaultYN"));
				return dbJdbcSet;
			}			
		};
			
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<OneToOne> listOneToOne() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.selectonetoone");	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				OneToOne  onetoone = new OneToOne();
				onetoone.setOnetooneID(rs.getInt("onetooneID"));
				onetoone.setOnetooneName(rs.getString("onetooneName"));
				onetoone.setOnetooneAlias(rs.getString("onetooneAlias"));
							
				return onetoone;
			}			
		};		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	@SuppressWarnings("unchecked")
	public List<TargetUIManagerSelect> getTargetUIManagerSelect(int targetUIManagerID) throws DataAccessException{
		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetUIManagerID", new Integer(targetUIManagerID));
		
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.gettargetuimanagerselect");	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetUIManagerSelect  targetUIManagerSelect = new TargetUIManagerSelect();
				targetUIManagerSelect.setTargetUIManagerID(rs.getInt("targetUIManagerID"));
				targetUIManagerSelect.setOnetooneID(rs.getInt("onetooneID"));
				targetUIManagerSelect.setSelectID(rs.getInt("selectID"));
				targetUIManagerSelect.setSelectFieldName(rs.getString("selectFieldName"));
				targetUIManagerSelect.setSelectDescription(rs.getString("selectDescription"));
				targetUIManagerSelect.setCsvColumnPos(rs.getInt("csvColumnPos"));
							
				return targetUIManagerSelect;
			}			
		};		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
		
	
	}
	
	@SuppressWarnings("unchecked")
	public List<TargetUIManagerWhere> getTargetUIManagerWhere(int targetUIManagerID) throws DataAccessException
	{
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetUIManagerID", new Integer(targetUIManagerID));
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.gettargetuimanagerwhere");	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetUIManagerWhere  targetUIManagerWhere = new TargetUIManagerWhere();
				targetUIManagerWhere.setTargetUIManagerID(rs.getInt("targetUIManagerID"));
				targetUIManagerWhere.setWhereID(rs.getInt("whereID"));
				targetUIManagerWhere.setWhereUIName(rs.getString("whereUIName"));
				targetUIManagerWhere.setWhereFieldName(rs.getString("whereFieldName"));
				targetUIManagerWhere.setWhereType(rs.getInt("whereType"));
				targetUIManagerWhere.setDataType(rs.getString("dataType"));
				targetUIManagerWhere.setExceptYN(rs.getString("exceptYN"));
				targetUIManagerWhere.setCheckName(rs.getString("checkName"));
				targetUIManagerWhere.setCheckValue(rs.getString("checkValue"));
				targetUIManagerWhere.setPeriodStartType(rs.getString("periodStartType"));
				targetUIManagerWhere.setPeriodEndType(rs.getString("periodEndType"));
				targetUIManagerWhere.setDescription(rs.getString("description"));
				
							
				return targetUIManagerWhere;
			}			
		};		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
		
	}
	
	public int deleteTargetManagerInfo(int targetUIManagerID) throws DataAccessException 
	{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.delete");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetUIManagerID", targetUIManagerID);
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	public int deleteTargetManagerSelect(int targetUIManagerID) throws DataAccessException 
	{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.deletetargetuimanagerselect");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetUIManagerID", targetUIManagerID);
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	public int deleteTargetManagerWhere(int targetUIManagerID) throws DataAccessException 
	{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.deletetargetuimanagerwhere");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetUIManagerID", targetUIManagerID);
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	public int createGeneralTable(int targetUIManagerID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.createtable");	
		sql = " CREATE TABLE tm_target_ui_general_"+ targetUIManagerID +" "+ sql ;
		
		if(db_type.equals(DB_TYPE_ORACLE))
		{
			sql = sql + " CONSTRAINT tm_target_ui_general_" + targetUIManagerID+"_PK PRIMARY KEY (targetID, whereID) )";
		}
		
		return getSimpleJdbcTemplate().update(sql);
	}
	
	public int updateTargetUseNo(int targetUIManagerID)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.targetmanager.updatetargetuseno"); 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetUIID", targetUIManagerID);
		
		
		
		return getSimpleJdbcTemplate().update(sql,param);	
	}
	
	public int updateTargetManagerDefault(int targetUIManagerID) throws DataAccessException 
	{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetmanager.updatetargetuimanagerdefault");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetUIManagerID", targetUIManagerID);
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	

}
