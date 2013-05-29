package web.admin.loginhistory.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.admin.loginhistory.model.LoginHistory;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;

public class LoginHistoryDAOImpl extends DBJdbcDaoSupport implements LoginHistoryDAO {
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	
	/**
	 * <p>로그인 시도 기록 조회
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public  List<LoginHistory> listLoginHistory(int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String searchType = searchMap.get("sSearchType");
		String searchText = searchMap.get("sSearchText");
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.loginhistory.select");		
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.loginhistory.tail");			
		sql =  sql + " " + sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				LoginHistory loginHistory = new LoginHistory();
				loginHistory.setAccessIP(rs.getString("accessIP"));
				loginHistory.setDescription(rs.getString("description"));
				loginHistory.setLoginHistoryID(rs.getInt("loginHistoryID"));
				loginHistory.setLoginYN(rs.getString("loginYN"));
				loginHistory.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				loginHistory.setUserID(rs.getString("userID"));
									
								
				return loginHistory;
			}			
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>로그인 시도 기록 조회 카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	
	public int getCountLoginHistory(Map<String, String> searchMap) throws DataAccessException{
		
		String searchType = searchMap.get("sSearchType");
		String searchText = searchMap.get("sSearchText");
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.loginhistory.count");		
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("searchText", "%"+searchText+"%");
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}

}
