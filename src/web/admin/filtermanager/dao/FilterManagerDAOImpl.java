package web.admin.filtermanager.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;


import web.admin.dbjdbcset.model.DbJdbcSet;
import web.admin.filtermanager.model.FilterManager;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;

public class FilterManagerDAOImpl extends DBJdbcDaoSupport implements FilterManagerDAO {
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";

	
	public int getListTotalCount(Map<String, Object> searchMap) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.filtermanager.count");
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String searchSelect = (String)searchMap.get("searchSelect");
		String searchSelectType = (String)searchMap.get("searchSelectType");
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";	
		}
	    if(searchSelect!=null && !searchSelect.equals("")){
			sql += " AND "+searchSelectType+" = '"+searchSelect+"' ";
		}
	    
		return getSimpleJdbcTemplate().queryForInt(sql);		
	}
	
	@SuppressWarnings("unchecked")
	public List<FilterManager> listFilterManager(Map<String, Object> searchMap) throws DataAccessException{
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String searchSelect = (String)searchMap.get("searchSelect");
		String searchSelectType = (String)searchMap.get("searchSelectType");
		
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		int start = (currentPage - 1) * countPerPage;
		countPerPage = countPerPage * currentPage;
		
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.filtermanager.select");					
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE '%"+searchText+"%' ";	
		}
	    if(searchSelect!=null && !searchSelect.equals("")){
	    	sql += " AND "+searchSelectType+" = '"+searchSelect+"' ";
		}

		String sqlTail =QueryUtil.getStringQuery("admin_sql","admin.filtermanager.selecttail");		
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				FilterManager  filterManager = new FilterManager();		
				
				filterManager.setFilterID(rs.getInt("filterID"));
				filterManager.setUserName(rs.getString("userName"));
				filterManager.setFilterType(rs.getInt("filterType"));
				filterManager.setContent(rs.getString("content"));
				filterManager.setContentType(rs.getInt("contentType"));
				filterManager.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				filterManager.setDescription(rs.getString("description"));
				filterManager.setFilterLevel(rs.getInt("filterLevel"));
				
				
				return filterManager;
			}			
		};

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	public FilterManager viewFilterManager(String filterID)  throws DataAccessException{
		
		FilterManager  filterManager = new FilterManager();	
		Map<String, Object> resultMap = null;
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.filtermanager.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("filterID", filterID);

		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);		
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			
			filterManager.setFilterID(Integer.parseInt(String.valueOf(resultMap.get("filterID"))));
			filterManager.setUserID((String)resultMap.get("userID"));
			filterManager.setFilterType(Integer.parseInt(String.valueOf(resultMap.get("filterType"))));
			filterManager.setContent((String)resultMap.get("content"));
			filterManager.setContentType(Integer.parseInt(String.valueOf(resultMap.get("contentType"))));
			filterManager.setDescription((String)resultMap.get("description"));
			filterManager.setFilterLevel(Integer.parseInt(String.valueOf(resultMap.get("filterLevel"))));
			
	
		}
		
		return filterManager;
		
	}
	
	public int insert(FilterManager  filterManager) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.filtermanager.insert"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", filterManager.getUserID());
		param.put("filterType", filterManager.getFilterType());
		param.put("content", filterManager.getContent());
		param.put("contentType", filterManager.getContentType());
		param.put("description", filterManager.getDescription());
		param.put("filterLevel", filterManager.getFilterLevel());
				
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	public int update(FilterManager  filterManager) throws  DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.filtermanager.update"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("filterID", filterManager.getFilterID());
		param.put("userID", filterManager.getUserID());
		param.put("filterType", filterManager.getFilterType());
		param.put("content", filterManager.getContent());
		param.put("contentType", filterManager.getContentType());
		param.put("description", filterManager.getDescription());
		param.put("filterLevel", filterManager.getFilterLevel());
		
		
		return  getSimpleJdbcTemplate().update(sql,param);	
	}
	
	public int delete(String filterID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.filtermanager.delete"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("filterID", filterID);
		return  getSimpleJdbcTemplate().update(sql,param);			
	}
	

}
