package web.admin.onetoone.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.admin.onetoone.model.OneToOne;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;

public class OneToOneDAOImpl extends DBJdbcDaoSupport implements OneToOneDAO{
	
	/**
	 * <p>원투원정보를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<OneToOne> listOneToOne() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.onetoone.select");	
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
	
	/**
	 * <p>targetID에 해당되는 ez_onetoone_target 리스트를 가져온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<OneToOne> listOneToOneByTargetID(String[] targetIDs) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.onetoone.selecttarget");		
		String sqlGroup  = " GROUP BY o.onetooneID, o.onetooneName, o.onetooneAlias ";
		
		String sqlWhere = " WHERE targetID IN(";
		String sqlcols = "";
		
		for(int i=0;i<targetIDs.length;i++){
			if(i==targetIDs.length-1){
				sqlcols +=Integer.parseInt(targetIDs[i])+")";	
			}else{
				sqlcols +=Integer.parseInt(targetIDs[i])+",";		
			}
				
		}		
		sql = sql + sqlWhere + sqlcols + sqlGroup;

		
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

}
