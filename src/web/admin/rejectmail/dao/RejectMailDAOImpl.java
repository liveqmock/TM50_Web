package web.admin.rejectmail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.dao.DataAccessException;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.*;
import web.admin.rejectmail.model.RejectMail;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import java.sql.PreparedStatement;

public class RejectMailDAOImpl extends DBJdbcDaoSupport implements RejectMailDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	
	
	/**
	 * <p>수신거부자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<RejectMail> listRejectMail(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String sSearchType = searchMap.get("searchType");
		String sSearchText = searchMap.get("searchText");
		String sSearchType_gubun = searchMap.get("searchType_gubun");
	
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.rejectmail.select");			
		
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " AND "+sSearchType+" LIKE :seach_text ";
		}
		if(sSearchType_gubun!=null && !sSearchType_gubun.equals("")){
			if(sSearchType_gubun.equals("a"))
				sql += " AND r.massmailID != '0' ";
			else 
				sql += " AND r.massmailID = '0' ";
				
		}
		
		if(currentPage > 0 && countPerPage > 0){
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.rejectmail.tail");			
		sql += sqlTail;
		}
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
					RejectMail rejectMail = new RejectMail();		
					rejectMail.setRejectID(rs.getInt("rejectID"));
					rejectMail.setEmail(rs.getString("email"));
					rejectMail.setMassmailID(rs.getInt("massmailID"));
					rejectMail.setMassmailGroupID(rs.getInt("massmailGroupID"));
					rejectMail.setMassmailGroupName(rs.getString("massmailGroupName"));					
					rejectMail.setCustomerID(rs.getString("customerID"));
					rejectMail.setUserID(rs.getString("userID"));
					rejectMail.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
					rejectMail.setMassmailTitle(rs.getString("massmailTitle"));	
					rejectMail.setUserName(rs.getString("userName"));	
					
					
				return rejectMail;
			}			
		};
		
			
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("seach_text", "%"+sSearchText+"%");
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>전체카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountRejectMail(Map<String, String> searchMap) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.rejectmail.totalcount");				
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		//String loginAuth = searchMap.get("loginAuth");
		//String groupID = searchMap.get("groupID");
		String sSearchType_gubun = searchMap.get("searchType_gubun");
		/*
		//소속관리자 혹은 일반사용자라면 해당 그룹만 나옴 
		if(loginAuth.equals(Constant.USER_LEVEL_MASTER) || loginAuth.equals(Constant.USER_LEVEL_USER)){
			sql += " AND  r.groupID = '"+groupID+"'";
		}
		*/
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
		}
		if(sSearchType_gubun!=null && !sSearchType_gubun.equals("")){
			if(sSearchType_gubun.equals("a"))
				sql += " AND r.massmailID != '0' ";
			else 
				sql += " AND r.massmailID = '0' ";
				
		}
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	
	/**
	 * <p>수신거부자 삭제 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] deleteRejectMail(Map<String, Object>[] maps) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.rejectmail.delete"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}
	
	
	
	/**
	 * <p>수신거부자 직접입력
	 * @param rejectMail
	 * @return
	 * @throws DataAccessException
	 */
	public int insertRejectMail(RejectMail rejectMail) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.rejectmail.insert");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("email", rejectMail.getEmail());
		param.put("massmailID", rejectMail.getMassmailID());
		param.put("targetID", rejectMail.getTargetID());
		param.put("massmailGroupID", rejectMail.getMassmailGroupID());
		param.put("userID", rejectMail.getUserID());		
		param.put("groupID",rejectMail.getGroupID());
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>ez_massmail_reject에 인서트 한다. 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public int[] insertCSVImport(String sql, final List paramList) throws DataAccessException{
		return getSimpleJdbcTemplate().getJdbcOperations().batchUpdate(sql,
			new BatchPreparedStatementSetter() { 
				public int getBatchSize() { 
					return paramList.size(); 
               } 

				public void setValues(PreparedStatement ps, int index) throws SQLException { 
					List<String> params = (List<String>) paramList.get(index);
					
					for(int i=0; i < params.size(); i ++) {
						ps.setString(i+1, params.get(i));
					}
				} 
		});  
	}
	
	public int getRejectMail(RejectMail rejectMail) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.rejectmail.totalcount");				
		String massmailGroupID = String.valueOf(rejectMail.getMassmailGroupID());
		String email = rejectMail.getEmail(); 
		
		sql += " AND r.email = '"+email+"' AND r.massmailGroupID = '" + massmailGroupID + "' ";
		
		
		return getSimpleJdbcTemplate().queryForInt(sql);
	}

}
