package web.admin.persistdomain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.admin.persistdomain.model.PersistDomain;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;


public class PersistDomainDAOImpl extends DBJdbcDaoSupport implements PersistDomainDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	
	@SuppressWarnings("unchecked")
	public List<PersistDomain> list(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		
		int start = (currentPage - 1) * countPerPage;
		
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
	
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.persistdomain.select");		
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " WHERE "+sSearchType+" LIKE :searchText ";
		}		
			
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.persistdomain.tail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE)){
			countPerPage = countPerPage * currentPage;
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		}
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				PersistDomain persistDomain = new PersistDomain();
				persistDomain.setDomainID(rs.getInt("domainID"));
				persistDomain.setDomain_name(rs.getString("domain_name"));
				persistDomain.setDescription(rs.getString("description"));
				persistDomain.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				persistDomain.setUserID(rs.getString("userID"));
				persistDomain.setUseYN(rs.getString("useYN"));
				
				
				return persistDomain;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+sSearchText+"%");
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
		
	
	
	public int getCount(Map<String, String> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.persistdomain.totalcount");		
		
		String searchType = searchMap.get("sSearchType");
		String searchName = searchMap.get("sSearchText");
		
		//검색조건이 있다면
		if(searchName!=null && !searchName.equals("")){
			sql += " WHERE "+searchType+" LIKE :searchName ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("searchName", "%"+searchName+"%");
		return getSimpleJdbcTemplate().queryForInt(sql,param);
		
	}
	
	
	
	public int insert(PersistDomain persistDomain)  throws DataAccessException{		
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.persistdomain.insert"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("domain_name", persistDomain.getDomain_name());
		param.put("description", persistDomain.getDescription());
		param.put("registDate", persistDomain.getRegistDate());
		param.put("userID", persistDomain.getUserID());
		param.put("useYN", persistDomain.getUseYN());
		
		
		
		
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	
	
	public int update(PersistDomain persistDomain)  throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.persistdomain.update"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 

		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("domainID", persistDomain.getDomainID());
		param.put("domain_name", persistDomain.getDomain_name());
		param.put("description", persistDomain.getDescription());
		param.put("registDate", persistDomain.getRegistDate());
		param.put("userID", persistDomain.getUserID());
		param.put("useYN", persistDomain.getUseYN());
		
		
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	
	
	public int delete(int domainID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.persistdomain.delete"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("domainID", new Integer(domainID));	
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public PersistDomain view(int domainID)  throws DataAccessException{
		PersistDomain persistDomain = new PersistDomain();
		Map resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.persistdomain.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("domainID", new Integer(domainID));		
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			if(resultMap!=null){
							
				persistDomain.setDomainID(Integer.parseInt(String.valueOf(resultMap.get("domainID"))));
				persistDomain.setDomain_name((String)resultMap.get("domain_name"));
				persistDomain.setDescription((String)resultMap.get("description"));
				persistDomain.setRegistDate(DateUtils.getStringDate(String.valueOf(resultMap.get("registDate"))));
				persistDomain.setUserID((String)resultMap.get("userID"));
				persistDomain.setUseYN((String)resultMap.get("useYN"));
				
			}else{
				return persistDomain;
			}
			
			return persistDomain;
		
	}
	
	public int selectDomain(PersistDomain persistDomain) throws DataAccessException{

		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.persistdomain.totalcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		if(persistDomain.getDomain_name()!=null && !persistDomain.getDomain_name().equals("")){
			sql += " WHERE domain_name = '" + persistDomain.getDomain_name() + "' and domainID != '" + persistDomain.getDomainID() + "'";
		}
		return  getSimpleJdbcTemplate().queryForInt(sql);
	}

	
}
