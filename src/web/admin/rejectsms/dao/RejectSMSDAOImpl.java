package web.admin.rejectsms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.dao.DataAccessException;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.*;
import web.admin.rejectsms.model.RejectSMS;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import java.sql.PreparedStatement;

public class RejectSMSDAOImpl extends DBJdbcDaoSupport implements RejectSMSDAO{
	
	
	/**
	 * <p>수신거부자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<RejectSMS> listRejectSMS(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String sSearchType = searchMap.get("searchType");
		String sSearchText = searchMap.get("searchText");
	
	
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.rejectsms.select");			
		
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " AND "+sSearchType+" LIKE :seach_text ";
		}
		
		if(currentPage > 0 && countPerPage > 0){
			String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.rejectsms.tail");			
			sql += sqlTail;
		}
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
					RejectSMS rejectSMS = new RejectSMS();		
					rejectSMS.setRejectID(rs.getInt("rejectID"));
					rejectSMS.setReceiverPhone(rs.getString("receiverPhone"));					
					rejectSMS.setUserID(rs.getString("userID"));
					rejectSMS.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));						
					rejectSMS.setUserName(rs.getString("userName"));
					
				return rejectSMS;
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
	public int totalCountRejectSMS(Map<String, String> searchMap) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.rejectsms.totalcount");				
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
	
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
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
	public int[] deleteRejectSMS(Map<String, Object>[] maps) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.rejectsms.delete"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}
	
	
	
	/**
	 * <p>수신거부자 직접입력
	 * @param rejectMail
	 * @return
	 * @throws DataAccessException
	 */
	public int insertRejectSMS(RejectSMS rejectSMS) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.rejectsms.insert");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("receiverPhone", rejectSMS.getReceiverPhone());
		param.put("userID", rejectSMS.getUserID());		
		param.put("groupID",rejectSMS.getGroupID());
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>ez_masssms_reject에 인서트 한다. 
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
	
	
	/**
	 * 수신거부 SMS 정보 
	 */
	public int getRejectSMS(RejectSMS rejectSMS) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.rejectsms.totalcount");
		String receiverPhone = rejectSMS.getReceiverPhone(); 		
		sql += " AND r.receiverPhone = '"+receiverPhone+"' ";
		
		return getSimpleJdbcTemplate().queryForInt(sql);
	}

}
