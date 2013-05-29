package web.admin.massmailgroup.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.admin.massmailgroup.model.MassMailGroup;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;

/**
 * <P>대량메일 그룹 관리의 조호/수정/삭제를 실행하는 DAO
 * @author limyh(임영호)
 *
 */
public class MassMailGroupDAOImpl extends DBJdbcDaoSupport implements MassMailGroupDAO {
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";

	
	/**
	 * <p>대량메일그룹 정보를 불러온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailGroup> listMassMailGroup() throws DataAccessException {

		String sql = QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.select");
		if(db_type.equals(DB_TYPE_MSSQL)){
  			sql = sql + QueryUtil.getStringQuery("common_sql","common.notpages");
  		}
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailGroup massmailgroup = new MassMailGroup(); 
					massmailgroup.setMassMailGroupID(rs.getInt("massMailGroupID"));
					massmailgroup.setMassMailGroupName(rs.getString("massMailGroupName"));
					massmailgroup.setIsDefault(rs.getString("isDefault"));
				return massmailgroup;
			}			
		};
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	

	/**
	 * <p>대량메일그룹 정보를 불러온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailGroup> listMassMailGroup(Map<String, Object> searchMap) throws DataAccessException {
		
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String searchSelect = (String)searchMap.get("searchSelect");
		String searchSelectType = (String)searchMap.get("searchSelectType");
		int currentPage = (Integer)searchMap.get("curPage");
		int countPerPage = (Integer)searchMap.get("iLineCnt");
		
		int start = (currentPage - 1) * countPerPage;
		countPerPage = countPerPage * currentPage;
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.select");
		
		if(searchText!=null && !searchText.equals("")){
			sql += " and "+searchType+" LIKE :searchText ";
			if(searchSelect!=null && !searchSelect.equals("")){
				sql += " AND "+searchSelectType+" = '"+searchSelect+"' ";
			}
		}else if(searchSelect!=null && !searchSelect.equals("")){
			sql += " and "+searchSelectType+" = '"+searchSelect+"' ";
		}
		
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.tail");
		
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				MassMailGroup massmailgroup = new MassMailGroup(); 
					massmailgroup.setMassMailGroupID(rs.getInt("massMailGroupID"));
					massmailgroup.setMassMailGroupName(rs.getString("massMailGroupName"));
					massmailgroup.setDescription(rs.getString("description"));
					massmailgroup.setRegistDate(rs.getString("registDate"));
					massmailgroup.setUseYN(rs.getString("useYN"));
					massmailgroup.setIsDefault(rs.getString("isDefault"));
				return massmailgroup;
			}			
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>대량 메일 그룹정보의 총카운트를 구해온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassMailGroupTotalCount(Map<String, Object> searchMap) throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.totalcount");
		String searchText = (String)searchMap.get("searchText");
		String searchType = (String)searchMap.get("searchType");
		String searchSelect = (String)searchMap.get("searchSelect");
		String searchSelectType = (String)searchMap.get("searchSelectType");
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " and "+searchType+" LIKE :searchText ";
			if(searchSelect!=null && !searchSelect.equals("")){
				sql += " AND "+searchSelectType+" LIKE '%"+searchSelect+"%' ";
			}
		}else if(searchSelect!=null && !searchSelect.equals("")){
			sql += " and "+searchSelectType+" LIKE '%"+searchSelect+"%' ";
		}
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>대량메일그룹 정보를 입력한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailGroup(MassMailGroup massMailGroup)throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.insert");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massMailGroupName", massMailGroup.getMassMailGroupName());
		param.put("description", massMailGroup.getDescription());
		param.put("useYN", massMailGroup.getUseYN());
		param.put("isDefault", massMailGroup.getIsDefault());
		
		
		
		return getSimpleJdbcTemplate().update(sql,param);
	
	}

	/**
	 * <p>대량메일그룹 정보를 수정한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailGroup(MassMailGroup massMailGroup)throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.update");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massMailGroupName", massMailGroup.getMassMailGroupName());
		param.put("description", massMailGroup.getDescription());
		param.put("useYN", massMailGroup.getUseYN());
		param.put("massMailGroupID", massMailGroup.getMassMailGroupID());
		param.put("isDefault", massMailGroup.getIsDefault());
		
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	public int updateMassMailGroup_default(MassMailGroup massMailGroup)throws DataAccessException {
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.update_default");
		Map<String,Object> param = new HashMap<String, Object>();		
		
		param.put("massMailGroupID", massMailGroup.getMassMailGroupID());
		
		
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>대량메일그룹 정보를 삭제한다.
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailGroup(int massMailGroupID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.delete");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("massMailGroupID", new Integer(massMailGroupID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	/**
	 * <p>대량메일그룹내용을 확인한다.
	 */
	public MassMailGroup viewMassMailGroup(int massMailGroupID)throws DataAccessException{
		MassMailGroup massmailgroup = new MassMailGroup();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String, Object> param = new HashMap<String, Object>();		
		param.put("massMailGroupID", new Integer(massMailGroupID));
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massmailgroup.setMassMailGroupID(Integer.parseInt(String.valueOf(resultMap.get("massMailGroupID"))));
			massmailgroup.setMassMailGroupName((String)resultMap.get("massMailGroupName"));
			massmailgroup.setDescription((String)resultMap.get("description"));
			massmailgroup.setRegistDate(String.valueOf(resultMap.get("registDate")));
			massmailgroup.setUseYN((String)resultMap.get("useYN"));
			massmailgroup.setIsDefault((String)resultMap.get("isDefault"));
		}
		
		return massmailgroup;
	}
	
	public MassMailGroup viewMassMailGroupChk(String massMailGroupName)throws DataAccessException{
		MassMailGroup massmailgroup = new MassMailGroup();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.massmailgroup.viewchk"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String, Object> param = new HashMap<String, Object>();		
		param.put("massMailGroupName", massMailGroupName);
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			massmailgroup.setMassMailGroupID(Integer.parseInt(String.valueOf(resultMap.get("massMailGroupID"))));
			massmailgroup.setMassMailGroupName((String)resultMap.get("massMailGroupName"));
			massmailgroup.setDescription((String)resultMap.get("description"));
			massmailgroup.setRegistDate(String.valueOf(resultMap.get("registDate")));
			massmailgroup.setUseYN((String)resultMap.get("useYN"));
			massmailgroup.setIsDefault((String)resultMap.get("isDefault"));
		}
		
		return massmailgroup;
	}

}
