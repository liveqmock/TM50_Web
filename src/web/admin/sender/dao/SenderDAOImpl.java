package web.admin.sender.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.admin.sender.model.Sender;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.Constant;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import web.common.util.ThunderUtil;

public class SenderDAOImpl extends DBJdbcDaoSupport implements SenderDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";

	/**
	 * <p>보내는 사람  리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Sender> listSender(String userID, String groupID,String userAuth, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		
		int start = (currentPage - 1) * countPerPage;
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sSearchType_gubun = searchMap.get("sSearchType_gubun");
		String sSearchType_use = searchMap.get("sSearchType_use");
	
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.sender.select");
		
		if(!(userAuth.equals(Constant.USER_LEVEL_MASTER))){
			sql+=QueryUtil.getStringQuery("admin_sql","admin.sender.select_user");		
		}
		
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " and "+sSearchType+" LIKE :searchText ";
		}	
		
		if(sSearchType_gubun!=null && !sSearchType_gubun.equals("")){
			if(sSearchType_gubun.equals("all"))
				sql += " and S.shareType = '3' ";
			else if(sSearchType_gubun.equals("group"))
				sql += " and S.shareType = '2' ";
			else if(sSearchType_gubun.equals("use_not"))
				sql += " and S.shareType = '1' ";
				
		}	
		
		if(sSearchType_use!=null && !sSearchType_use.equals("")){
			if(sSearchType_use.equals("Y"))
				sql += " and S.useYN = 'Y' ";
			else
				sql += " and S.useYN = 'N' ";
			
				
		}	
			
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.sender.tail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				Sender sender = new Sender();
				sender.setSenderID(rs.getInt("senderID"));
				sender.setSenderName(rs.getString("senderName"));
				sender.setSenderEmail(rs.getString("senderEmail"));
				sender.setSenderCellPhone(rs.getString("senderCellPhone"));
				sender.setDescription(rs.getString("description"));
				sender.setUserID(rs.getString("userID"));
				sender.setShareType(rs.getString("shareType"));
				sender.setShareTypeDesc(ThunderUtil.descShareType(rs.getString("shareType")));
				sender.setUseYN(rs.getString("useYN"));
				sender.setDefaultYN(rs.getString("defaultYN"));
				sender.setRegistDate(rs.getString("registDate"));
				sender.setUserName(rs.getString("userName"));
				return sender;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		if(!(userAuth.equals(Constant.USER_LEVEL_MASTER))){
			param.put("userID", userID);
		}
		param.put("searchText", "%"+sSearchText+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
		
	
	/**
	 * <p>보내는 사람 카운트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public int getCountSender (String userID, String groupID,String userAuth, Map<String, String> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.sender.totalcount");		
		
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		
		String sSearchType_gubun = searchMap.get("sSearchType_gubun");
		String sSearchType_use = searchMap.get("sSearchType_use");
		
		if(!(userAuth.equals(Constant.USER_LEVEL_MASTER))){
			sql+=" and (S.shareType = '3' or S.shareType = '2' or (S.userID = '"+ userID +"' and S.shareType = '1') ) ";	
		}
		
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " and "+sSearchType+" LIKE :searchText ";
		}
		
		if(sSearchType_gubun!=null && !sSearchType_gubun.equals("")){
			if(sSearchType_gubun.equals("all"))
				sql += " and S.shareType = '3' ";
			else if(sSearchType_gubun.equals("group"))
				sql += " and S.shareType = '2' ";
			else if(sSearchType_gubun.equals("use_not"))
				sql += " and S.shareType = '1' ";
				
		}	
		
		if(sSearchType_use!=null && !sSearchType_use.equals("")){
			if(sSearchType_use.equals("Y"))
				sql += " and S.useYN = 'Y' ";
			else
				sql += " and S.useYN = 'N' ";
			
		}	
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+sSearchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
	
	
	/**
	 * <p>보내는사람 등록 
	 * @param sender
	 * @return
	 * @throws DataAccessException
	 */
	public int insertSender(Sender sender)  throws DataAccessException{		
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.sender.insert"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("senderName", sender.getSenderName());
		param.put("senderEmail", sender.getSenderEmail());
		param.put("senderCellPhone", sender.getSenderCellPhone());
		param.put("description", sender.getDescription());
		param.put("userID", sender.getUserID());
		param.put("shareType", sender.getShareType());
		param.put("useYN", sender.getUseYN());		
		param.put("defaultYN", sender.getDefaultYN());	
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	
	/**
	 * <p>보내는사람 수정 
	 * @param sender
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSender(Sender sender)  throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.sender.update"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 

		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("senderID", sender.getSenderID());
		param.put("senderName", sender.getSenderName());
		param.put("senderEmail", sender.getSenderEmail());
		param.put("senderCellPhone", sender.getSenderCellPhone());
		param.put("description", sender.getDescription());
		param.put("userID", sender.getUserID());
		param.put("shareType", sender.getShareType());		
		param.put("useYN", sender.getUseYN());		
		param.put("defaultYN", sender.getDefaultYN());	
		
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	
	
	/**
	 * <p>보내는 사람을 삭제 
	 * @param senderID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSender(int senderID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.sender.delete"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("senderID", new Integer(senderID));	
		return getSimpleJdbcTemplate().update(sql,param);		
	}
	
	
	
	/**
	 * <p>보내는 사람 정보보기 
	 * @param senderID
	 * @return
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public Sender viewSender(int senderID)  throws DataAccessException{
		Sender sender = new Sender();
		Map resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.sender.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("senderID", new Integer(senderID));		
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			if(resultMap!=null){
				
				sender.setSenderID(Integer.parseInt(String.valueOf(resultMap.get("senderID"))));
				sender.setSenderName((String)resultMap.get("senderName"));
				sender.setSenderEmail((String)resultMap.get("senderEmail"));
				sender.setSenderCellPhone((String)resultMap.get("senderCellPhone"));
				sender.setDescription((String)resultMap.get("description"));
				sender.setUserID((String)resultMap.get("userID"));
				sender.setShareType((String)resultMap.get("shareType"));				
				sender.setUseYN((String)resultMap.get("useYN"));
				sender.setDefaultYN((String)resultMap.get("defaultYN"));
				sender.setRegistDate(String.valueOf(resultMap.get("registDate")));
				
			}else{
				return sender;
			}
		
			
			return sender;
		
	}
	
}
