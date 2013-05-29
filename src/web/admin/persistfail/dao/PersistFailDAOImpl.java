package web.admin.persistfail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.admin.persistfail.model.*; 
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import web.common.dao.DBJdbcDaoSupport;


public class PersistFailDAOImpl extends DBJdbcDaoSupport implements PersistFailDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";

	/**
	 * <p>영구적인 메일리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PersistFail> listPersistFailMail(int currentPage, int countPerPage,Map<String, Object> searchMap)  throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String dateStart = (String)searchMap.get("dateStart");
		String dateEnd = (String)searchMap.get("dateEnd");

		String sql = QueryUtil.getStringQuery("admin_sql","admin.persistfailmail.select");
		
		//검색조건이 있다면
		if(searchText!=null && !searchType.equals("")){
			sql += " WHERE "+searchType+" LIKE :searchText ";
		}	
		
		if(db_type.equals(DB_TYPE_ORACLE))
		{
			if(dateStart!=null && !dateStart.equals(" 00:00:00"))
				sql += " AND p.registDate >= TO_DATE('"+ dateStart +"','YYYY-MM-DD HH24:MI:ss') ";
			if(dateEnd!=null && !dateEnd.equals(" 23:59:59"))
				sql += " AND p.registDate <= TO_DATE('"+ dateEnd +"','YYYY-MM-DD HH24:MI:ss') ";
		}
		else if(db_type.equals(DB_TYPE_MYSQL))
		{
			if(dateStart!=null && !dateStart.equals(" 00:00:00"))
				sql += " AND p.registDate >= '"+ dateStart +"' ";
			if(dateEnd!=null && !dateEnd.equals(" 23:59:59"))
				sql += " AND p.registDate <= '"+ dateEnd +"' ";
		}
		
		
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.persistfailmail.tail");
		
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				PersistFail persistFail = new PersistFail();
				
				persistFail.setPersistfailID(rs.getString("persistfailID"));
				persistFail.setEmail(rs.getString("email"));
				persistFail.setMassmailID(rs.getInt("massmailID"));
				persistFail.setMassmailGroupID(rs.getInt("massmailGroupID"));
				persistFail.setTargetID(rs.getInt("targetID"));
				persistFail.setSmtpCode(rs.getString("smtpCode"));
				persistFail.setSmtpMsg(rs.getString("smtpMsg"));
				persistFail.setCustomerID(rs.getString("customerID"));
				persistFail.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				persistFail.setMassmailTitle(rs.getString("massmailTitle"));
				persistFail.setTargetName(rs.getString("targetName"));
				persistFail.setMassmailGroupName(rs.getString("massmailGroupName"));
				
				return persistFail;
			}
		};
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);

	}
	
	
	/**
	 * <p>전체카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountPersistFailMail(Map<String, Object> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.persistfailmail.totalcount");		
		
		String searchType = (String)searchMap.get("searchType");
		String searchText = (String)searchMap.get("searchText");
		String dateStart = (String)searchMap.get("dateStart");
		String dateEnd = (String)searchMap.get("dateEnd");
		sql += " where 1=1 ";
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " and "+searchType+" LIKE :searchText ";
		}
		if(db_type.equals(DB_TYPE_ORACLE))
		{
			if(dateStart!=null && !dateStart.equals(" 00:00:00"))
				sql += " AND p.registDate >= TO_DATE('"+ dateStart +"','YYYY-MM-DD HH24:MI:ss') ";
			if(dateEnd!=null && !dateEnd.equals(" 23:59:59"))
				sql += " AND p.registDate <= TO_DATE('"+ dateEnd +"','YYYY-MM-DD HH24:MI:ss') ";
		}
		else if(db_type.equals(DB_TYPE_MYSQL))
		{
			if(dateStart!=null && !dateStart.equals(" 00:00:00"))
				sql += " AND p.registDate >= '"+ dateStart +"' ";
			if(dateEnd!=null && !dateEnd.equals(" 23:59:59"))
				sql += " AND p.registDate <= '"+ dateEnd +"' ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	
	/**
	 * <p>선택한 리스트 삭제 
	 * @param persistFailMails
	 * @return
	 * @throws DataAccessException
	 */
	public int[] deletePersistFailMail(Map<String, Object>[] maps) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.persistfailmail.delete"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}	
	
}
