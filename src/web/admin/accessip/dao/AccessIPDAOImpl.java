package web.admin.accessip.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.admin.accessip.model.AccessIP;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;

public class AccessIPDAOImpl extends DBJdbcDaoSupport implements AccessIPDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";

	/**
	 * <p>접근IP관리 리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AccessIP> listAccessIP(int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException{
		
		int start = (currentPage - 1) * countPerPage;
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sUseYN = searchMap.get("sUseYN");
		String sql = QueryUtil.getStringQuery("admin_sql","admin.AccessIP.select");
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.AccessIP.tail");
		
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " AND "+sSearchType+" LIKE :searchText ";
		}	
		if(sUseYN!=null && !sUseYN.equals("")){
			sql += " AND a.useYN LIKE '%" + sUseYN + "%' ";
		}	
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				AccessIP accessIP = new AccessIP();
				accessIP.setAccessipID(rs.getInt("accessipID"));
				accessIP.setUserName(rs.getString("userName"));
				accessIP.setDescription(rs.getString("description"));
				accessIP.setOctetA(rs.getString("octetA"));
				accessIP.setOctetB(rs.getString("octetB"));
				accessIP.setOctetC(rs.getString("octetC"));
				accessIP.setOctetD(rs.getString("octetD"));
				accessIP.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				accessIP.setUseYN(rs.getString("useYN"));
				return accessIP;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+sSearchText+"%");
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
		
	
	/**
	 * <p>접근IP관리 리스트의 총카운트를 구해온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getAccessIPTotalCount(Map<String, String> searchMap)  throws DataAccessException{
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sUseYN = searchMap.get("sUseYN");
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.AccessIP.totalcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " AND "+sSearchType+" LIKE :searchText ";
		}	
		if(sUseYN!=null && !sUseYN.equals("")){
			sql += " AND a.useYN LIKE '%" + sUseYN + "%' ";
		}		

		Map<String,Object> param = new HashMap<String, Object>();
		param.put("searchText", "%"+sSearchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	/**
	 * <p>접근IP관리 설정 정보
	 * @param accessipID
	 * @return
	 * @throws DataAccessException
	 */
	public AccessIP viewAccessIP(int accessipID)throws DataAccessException{
		AccessIP accessIP = new AccessIP();
		Map<String, Object> resultMap = null;
		String sql = QueryUtil.getStringQuery("admin_sql","admin.AccessIP.viewAccessIP"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("accessipID", new Integer(accessipID));

		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			accessIP.setAccessipID(accessipID);
			accessIP.setDescription((String)resultMap.get("description"));
			accessIP.setOctetA((String)resultMap.get("octetA"));
			accessIP.setOctetB((String)resultMap.get("octetB"));
			accessIP.setOctetC((String)resultMap.get("octetC"));
			accessIP.setOctetD((String)resultMap.get("octetD"));
			accessIP.setUseYN((String)resultMap.get("useYN"));
		}
		return accessIP;
	}
	
	/**
	 * <p>접근IP관리 설정 추가한다.
	 * @param accessIP
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAccessIP(AccessIP accessIP)throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.AccessIP.insert"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("octetA", accessIP.getOctetA());
		param.put("octetB", accessIP.getOctetB());
		param.put("octetC", accessIP.getOctetC());
		param.put("octetD", accessIP.getOctetD());
		param.put("description", accessIP.getDescription());
		param.put("octetType", accessIP.getOctetType());
		param.put("useYN", accessIP.getUseYN());
		param.put("userID", accessIP.getUserID());
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	

	/**
	 * <p>접근IP관리 설정 수정한다.
	 * @param accessIP
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAccessIP(AccessIP accessIP)throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.AccessIP.update"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("octetA", accessIP.getOctetA());
		param.put("octetB", accessIP.getOctetB());
		param.put("octetC", accessIP.getOctetC());
		param.put("octetD", accessIP.getOctetD());
		param.put("description", accessIP.getDescription());
		param.put("octetType", accessIP.getOctetType());
		param.put("useYN", accessIP.getUseYN());
		param.put("userID", accessIP.getUserID());
		param.put("accessipID", new Integer(accessIP.getAccessipID()));
		
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p> 접근IP관리 설정을 삭제한다. 
	 * @param accessipID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAccessIP(int accessipID) throws DataAccessException{		
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.AccessIP.delete"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("accessipID", new Integer(accessipID));
		return  getSimpleJdbcTemplate().update(sql,param);						
	}
}
