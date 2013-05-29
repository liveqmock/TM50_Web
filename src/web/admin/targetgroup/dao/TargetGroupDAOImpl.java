package web.admin.targetgroup.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.admin.targetgroup.model.TargetGroup;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;


public class TargetGroupDAOImpl extends DBJdbcDaoSupport implements TargetGroupDAO {
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	
	
	@SuppressWarnings("unchecked")
	public List<TargetGroup> listTargetGroup() throws DataAccessException {

		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.selectsearch");
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetGroup targetgroup = new TargetGroup(); 
				targetgroup.setTargetGroupID(rs.getInt("targetGroupID"));
				targetgroup.setTargetGroupName(rs.getString("targetGroupName"));
				targetgroup.setIsDefault(rs.getString("isDefault"));
				return targetgroup;
			}			
		};
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	

	@SuppressWarnings("unchecked")
	public List<TargetGroup> listTargetGroup(Map<String, Object> searchMap) throws DataAccessException {
		
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String searchSelect = (String)searchMap.get("searchSelect");
		String searchSelectType = (String)searchMap.get("searchSelectType");
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		
		int start = (currentPage - 1) * countPerPage;
				
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.select");
		
		if(searchText!=null && !searchText.equals("")){
			sql += " and "+searchType+" LIKE '%"+searchText+"%' ";
			if(searchSelect!=null && !searchSelect.equals("")){
				sql += " AND "+searchSelectType+" = '"+searchSelect+"' ";
			}
		}else if(searchSelect!=null && !searchSelect.equals("")){
			sql += " and "+searchSelectType+" = '"+searchSelect+"' ";
		}
		
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.tail");
		
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetGroup targetgroup = new TargetGroup(); 
				targetgroup.setTargetGroupID(rs.getInt("targetGroupID"));
				targetgroup.setTargetGroupName(rs.getString("targetGroupName"));
				targetgroup.setDescription(rs.getString("description"));
				targetgroup.setRegistDate(rs.getString("registDate"));
				targetgroup.setUseYN(rs.getString("useYN"));
				targetgroup.setIsDefault(rs.getString("isDefault"));
				return targetgroup;
			}			
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	public int getTargetGroupTotalCount(Map<String, Object> searchMap) throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.totalcount");
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String searchSelect = (String)searchMap.get("searchSelect");
		String searchSelectType = (String)searchMap.get("searchSelectType");
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " and "+searchType+" LIKE '%"+searchText+"%' ";
			if(searchSelect!=null && !searchSelect.equals("")){
				sql += " AND "+searchSelectType+" LIKE '%"+searchSelect+"%' ";
			}
		}else if(searchSelect!=null && !searchSelect.equals("")){
			sql += " and "+searchSelectType+" LIKE '%"+searchSelect+"%' ";
		}
		
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	
	public int insertTargetGroup(TargetGroup targetGroup)throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.insert");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetGroupName", targetGroup.getTargetGroupName());
		param.put("description", targetGroup.getDescription());
		param.put("useYN", targetGroup.getUseYN());
		param.put("isDefault", targetGroup.getIsDefault());
		
		
		
		return getSimpleJdbcTemplate().update(sql,param);
	
	}

	
	public int updateTargetGroup(TargetGroup targetGroup)throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.update");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetGroupName", targetGroup.getTargetGroupName());
		param.put("description", targetGroup.getDescription());
		param.put("useYN", targetGroup.getUseYN());
		param.put("targetGroupID", targetGroup.getTargetGroupID());
		param.put("isDefault", targetGroup.getIsDefault());
		
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	public int updateTargetGroup_default(TargetGroup targetGroup)throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.update_default");
		Map<String,Object> param = new HashMap<String, Object>();		
		
		param.put("targetGroupID", targetGroup.getTargetGroupID());
		
		
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	public int deleteTargetGroup(int targetGroupID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.delete");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetGroupID", new Integer(targetGroupID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	
	public TargetGroup viewTargetGroup(int targetGroupID)throws DataAccessException{
		TargetGroup targetgroup = new TargetGroup();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.targetgroup.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String, Object> param = new HashMap<String, Object>();		
		param.put("targetGroupID", new Integer(targetGroupID));
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			targetgroup.setTargetGroupID(Integer.parseInt(String.valueOf(resultMap.get("targetGroupID"))));
			targetgroup.setTargetGroupName((String)resultMap.get("targetGroupName"));
			targetgroup.setDescription((String)resultMap.get("description"));
			targetgroup.setRegistDate(String.valueOf(resultMap.get("registDate")));
			targetgroup.setUseYN((String)resultMap.get("useYN"));
			targetgroup.setIsDefault((String)resultMap.get("isDefault"));
		}
		
		return targetgroup;
	}
	
	public TargetGroup viewTargetGroupChk(String targetGroupName)throws DataAccessException{
		TargetGroup targetgroup = new TargetGroup();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.targetgroup.viewchk"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String, Object> param = new HashMap<String, Object>();		
		param.put("targetGroupName", targetGroupName);
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			targetgroup.setTargetGroupID(Integer.parseInt(String.valueOf(resultMap.get("targetGroupID"))));
			targetgroup.setTargetGroupName((String)resultMap.get("targetGroupName"));
			targetgroup.setDescription((String)resultMap.get("description"));
			targetgroup.setRegistDate(String.valueOf(resultMap.get("registDate")));
			targetgroup.setUseYN((String)resultMap.get("useYN"));
			targetgroup.setIsDefault((String)resultMap.get("isDefault"));
		}
		
		return targetgroup;
	}
	
	public int getCountTarget(int targetGroupID) throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.counttarget");
		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetGroupID", new Integer(targetGroupID));
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}

	@SuppressWarnings("unchecked")
	public List<TargetGroup> getTargetGroupDefaultY() throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.targetgroup.selectDefaultY");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetGroup targetgroup = new TargetGroup(); 
				targetgroup.setTargetGroupID(rs.getInt("targetGroupID"));
				targetgroup.setTargetGroupName(rs.getString("targetGroupName"));
				targetgroup.setIsDefault(rs.getString("isDefault"));
				return targetgroup;
			}			
		};
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
}
