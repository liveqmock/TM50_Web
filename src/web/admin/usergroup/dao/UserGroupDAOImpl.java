package web.admin.usergroup.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import web.admin.usergroup.model.*;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class UserGroupDAOImpl extends DBJdbcDaoSupport   implements UserGroupDAO{

	
	/**
	 * <p>트리에 이용할 그룹의 모든 데이타를 불러온다
	 * @return
	 * @throws DataAccessException
	 */	
    @SuppressWarnings("unchecked")
	public List<Group> treeGroup() {

		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectgroup");		
		sql += " ORDER BY groupID ";
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {	
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Group  group = new Group();			
				group.setGroupID(rs.getString("groupID"));
				group.setGroupName(rs.getString("groupName"));				
				group.setDescription(rs.getString("description"));				
				group.setUserCount(rs.getInt("userCount"));
				group.setRegistDate(rs.getString("registDate"));
				group.setIsAdmin(rs.getString("isAdmin"));
				return group;
			}			
		};		
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);

	}

	/**
	 * <p>트리에 이용할  그룹별 사용자의 데이타를 불러온다
	 * @return
	 * @throws DataAccessException
	 */	
    @SuppressWarnings("unchecked")
	public List<User> treeUser( String groupID ) {

		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectuser");		
		sql += " WHERE u.groupID = '"+groupID+"' AND u.useYN='Y' ORDER BY u.userLevel, u.userName";
		    	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {	
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				User user = new User();
				user.setUserID(rs.getString("userID"));
				user.setUserPWD(rs.getString("userPWD"));
				user.setGroupID(rs.getString("groupID"));
				user.setGroupName(rs.getString("groupName"));
				user.setUserName(rs.getString("userName"));
				user.setUserLevel(rs.getString("userLevel"));
				user.setEmail(rs.getString("email"));
				user.setCellPhone(rs.getString("cellPhone"));
				user.setDescription(rs.getString("description"));				
				user.setUseYN(rs.getString("useYN"));
				user.setRegistDate(rs.getString("registDate"));
				user.setIsAdmin(rs.getString("isAdmin"));
				return user;
			}			
		};		
			
		return  getSimpleJdbcTemplate().query(sql, rowMapper);

	}
    
    
	/**
	 * <p>자동완성에서 검색한 사용자의 데이타를 불러온다
	 * @return
	 * @throws DataAccessException
	 */	
    @SuppressWarnings("unchecked")
	public List<User> quickSearchUser( String userName ) {

		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.quickSearchUser");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {	
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				User user = new User();
				user.setGroupID(rs.getString("groupID"));
				user.setUserID(rs.getString("userID"));
				user.setGroupID(rs.getString("groupID"));
				user.setUserName(rs.getString("userName"));
				return user;
			}			
		};		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userName", "%"+userName+"%" );
		param.put("userID", "%"+userName+"%" );
		
	
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		

	}
    
            
	
	/**
	 * <p>사용자그룹(tm_usergroup) 리스트를 보여준다.  //사용안하고 있음. 확실해지면 삭제 예정
	 * @return
	 * @throws DataAccessException
	 */
	 @SuppressWarnings("unchecked")
	public List<Group> listGroup(int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String searchType = searchMap.get("searchType");
		String searchName = searchMap.get("searchName");
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectgroup");		
		
		//검색조건이 있다면
		if(searchName!=null && !searchName.equals("")){
			sql += " WHERE "+searchType+" LIKE :searchName ";
		}
			
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.usergroup.grouptail");			
		sql += sqlTail;
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {	
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Group  group = new Group();			
				group.setGroupID(rs.getString("groupID"));
				group.setGroupName(rs.getString("groupName"));				
				group.setDescription(rs.getString("description"));				
				group.setUserCount(rs.getInt("userCount"));
				group.setRegistDate(rs.getString("registDate"));
				group.setIsAdmin(rs.getString("isAdmin"));
				return group;
			}			
		};		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start) );
		param.put("countPerPage", new Integer(countPerPage) );
		param.put("searchName", "%"+searchName+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	 
	 @SuppressWarnings("unchecked")
	 public List<Group> listGroup_inc(String userID) throws DataAccessException{
			
			
			String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectgroupuser");		
			
					
			
			ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {	
					
				public Object mapRow(ResultSet rs, int rownum) throws SQLException {
					Group  group = new Group();			
					group.setGroupID(rs.getString("groupID"));
					group.setGroupName(rs.getString("groupName"));				
					
					return group;
				}			
			};		
			
			Map<String,Object> param = new HashMap<String, Object>();		
			param.put("userID", userID );
			
			
			return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		}
	 
	 @SuppressWarnings("unchecked")
	 public List<Group> listGroup_inc_admin(String userID) throws DataAccessException{
			
			
			String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectgroupadmin");		
			
			ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {	
					
				public Object mapRow(ResultSet rs, int rownum) throws SQLException {
					Group  group = new Group();			
					group.setGroupID(rs.getString("groupID"));
					group.setGroupName(rs.getString("groupName"));				
					
					return group;
				}			
			};		
			
			Map<String,Object> param = new HashMap<String, Object>();		
			param.put("userID", userID );
			
			
			return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		}
	 
	 
		/**
		 * <p>사용자그룹(tm_usergroup) 리스트를 보여준다.  //사용안하고 있음. 확실해지면 삭제 예정
		 * @return
		 * @throws DataAccessException
		 */
		 @SuppressWarnings("unchecked")
		public List<User> listUser(int currentPage, int countPerPage, Map<String, String> searchMap, String groupID, String userLevel, String useYN) throws DataAccessException{
			int start = (currentPage - 1) * countPerPage;
			String searchType = searchMap.get("searchType");
			String searchName = searchMap.get("searchName");
				
			String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectuser");		
			
			String sqlWhere = " WHERE 1=1 ";
			
			sql += sqlWhere;
			//검색조건이 있다면
			if(searchName!=null && !searchName.equals("")){
				sql += " AND  "+searchType+" LIKE :searchName ";
			}					
			
			if(groupID!=null && !groupID.equals("")){
				sql += " AND u.groupID = '"+groupID+"'";
			}
			
			if(userLevel!=null && !userLevel.equals("")){
				sql += " AND u.userLevel = '"+userLevel+"'";
			}

			if(useYN!=null && !useYN.equals("")){
				sql += " AND u.useYN = '"+useYN+"'";
			}
			
			String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.usergroup.usertail");			
			sql += sqlTail;
				
			ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {	
					
				public Object mapRow(ResultSet rs, int rownum) throws SQLException {
					User user = new User();
					user.setUserID(rs.getString("userID"));
					user.setUserPWD(rs.getString("userPWD"));
					user.setGroupID(rs.getString("groupID"));
					user.setGroupName(rs.getString("groupName"));
					user.setUserName(rs.getString("userName"));
					user.setUserLevel(rs.getString("userLevel"));
					user.setEmail(rs.getString("email"));
					user.setCellPhone(rs.getString("cellPhone"));
					user.setDescription(rs.getString("description"));				
					user.setUseYN(rs.getString("useYN"));
					user.setRegistDate(rs.getString("registDate"));					
					user.setIsAdmin(rs.getString("isAdmin"));
					return user;
				}			
			};		
			
			Map<String,Object> param = new HashMap<String, Object>();		
			param.put("start", new Integer(start) );
			param.put("countPerPage", new Integer(countPerPage) );
			param.put("searchName", "%"+searchName+"%");
			return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
		}
		 
		 /**
			 * <p>사용자그룹(tm_usergroup) 리스트를 보여준다.
			 * @return
			 * @throws DataAccessException
			 */
			 @SuppressWarnings("unchecked")
			public List<User> listSearchUser(String userLevel,String groupID) throws DataAccessException{
					
				String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectuser");		
				
				String sqlWhere = " WHERE 1=1 ";
				
				sql += sqlWhere;			
				
				if(groupID!=null && !groupID.equals("") && userLevel.equals("2")){
					sql += " AND u.groupID = '"+groupID+"'";
				}
				sql += " AND u.useYN = 'Y'";

				ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {	
						
					public Object mapRow(ResultSet rs, int rownum) throws SQLException {
						User user = new User();
						user.setUserID(rs.getString("userID"));
						user.setUserPWD(rs.getString("userPWD"));
						user.setGroupID(rs.getString("groupID"));
						user.setGroupName(rs.getString("groupName"));
						user.setUserName(rs.getString("userName"));
						user.setUserLevel(rs.getString("userLevel"));
						user.setEmail(rs.getString("email"));
						user.setCellPhone(rs.getString("cellPhone"));
						user.setDescription(rs.getString("description"));				
						user.setUseYN(rs.getString("useYN"));
						user.setRegistDate(rs.getString("registDate"));					
						user.setIsAdmin(rs.getString("isAdmin"));
						return user;
					}			
				};		
				
				return  getSimpleJdbcTemplate().query(sql, rowMapper);
			}
	
	
	/**
	 * <p>tm_usergroup에 그룹을 인서트한다.
	 * @param group
	 * @return
	 * @throws DataAccessException
	 */
	public int insertGroup(Group group) throws DataAccessException{		
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertgroup"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
			//넘겨받은 파라미터를 세팅한다. 
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("groupID", group.getGroupID());
		param.put("groupName", group.getGroupName());
		param.put("description", group.getDescription());
		
		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>tm_users에 사용자를  인서트한다.
	 * @param group
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUser(User user) throws DataAccessException{		
	
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertuser"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
			//넘겨받은 파라미터를 세팅한다. 

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", user.getUserID());
		param.put("userPWD", user.getUserPWD());
		param.put("userName", user.getUserName());
		param.put("groupID", user.getGroupID());
		param.put("userLevel", user.getUserLevel());
		param.put("email", user.getEmail());
		param.put("cellPhone", user.getCellPhone());
		param.put("description", user.getDescription());
		param.put("useYN", user.getUseYN());
		param.put("isHelper", user.getIsHelper());
		param.put("senderName", user.getSenderName());
		param.put("senderEmail", user.getSenderEmail());
		param.put("senderCellPhone", user.getSenderCellPhone());
		
		
		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>tm_users에 관리자를  인서트한다.
	 * @param group
	 * @return
	 * @throws DataAccessException
	 */
	public int insertgroupadmin(User user) throws DataAccessException{		
	
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertgroupadmin"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
			//넘겨받은 파라미터를 세팅한다. 

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", user.getUserID());
		param.put("userPWD", user.getUserPWD());
		param.put("userName", user.getUserName());
		param.put("groupID", user.getGroupID());
		param.put("userLevel", user.getUserLevel());
		param.put("email", user.getEmail());
		param.put("cellPhone", user.getCellPhone());
		param.put("description", user.getDescription());
		param.put("useYN", user.getUseYN());
		param.put("isHelper", user.getIsHelper());
		param.put("senderName", user.getSenderName());
		param.put("senderEmail", user.getSenderEmail());
		param.put("senderCellPhone", user.getSenderCellPhone());
		
		
		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 *< p>사용자그룹(ez_usergroup)에서 삭제한다. 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteGroup(String groupID)  throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.deletegroup"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("groupID", groupID);
		return  getSimpleJdbcTemplate().update(sql, param);			
		
	}
	
	/**
	 *< p>사용자를 (ez_users)에서 삭제한다. 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteUser(String userID)  throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.deleteuser"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);	
		return  getSimpleJdbcTemplate().update(sql, param);			
		
	}
	
	
	/**
	 *  p>사용자그룹(ez_usergroup)에서 수정한다. 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateGroup(Group group) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.updategroup"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("groupName", group.getGroupName());		
		param.put("description", group.getDescription());
		param.put("groupID", group.getGroupID());
		
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);				
	}
	
	/**
	 *  p>그룹의 메뉴 권한을 수정한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertGroupMenuAuth(String groupID, String subMenuID) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertGroupMenuAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("_groupID", "_group_"+groupID);	
		param.put("subMenuID", subMenuID);
		param.put("groupID", groupID);
		
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);		
	
	}
	
	/**
	 *  p>그룹의 데이타 권한을 추가한다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertGroupDataAuth(String groupID, String dbID) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertGroupDataAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("_groupID", "_group_"+groupID);	
		param.put("dbID", dbID);
		param.put("groupID", groupID);
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);			
	
	}
	
	
	/**
	 *  p>그룹의 옵션 권한을 수정한다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public int updateGroupAuth(String groupID, User user) throws DataAccessException{

		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.updateGroupAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("auth_csv", user.getAuthCSV());	
		param.put("auth_direct", user.getAuthDirect());	
		param.put("auth_related", user.getAuthRelated());	
		param.put("auth_query", user.getAuthQuery());		
		param.put("auth_send_mail", user.getAuthSendMail());
		param.put("auth_write_mail", user.getAuthWriteMail());
		param.put("auth_send_sms", user.getAuthSendSMS());
		param.put("auth_write_sms", user.getAuthWriteSMS());		
		param.put("groupID", groupID);		
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);			
		
	}
	
	
	/**
	 *  p>그룹의 메뉴 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteGroupAllMenuAuth(String groupID) throws DataAccessException {
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.deleteAllMenuAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("_group", "_group_"+groupID );	
		
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);				
		
	}

	/**
	 *  p>그룹의 데이타 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteGroupAllDataAuth(String groupID) throws DataAccessException {
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.deleteAllDataAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("_group", "_group_"+groupID );		
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);	
		
	}
	
	
	/**
	 *  p>사용자그룹(tm_usergroup)에서 수정한다. 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateUser(User user) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.updateuser"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 

		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userPWD", user.getUserPWD());		
		param.put("userName", user.getUserName());
		param.put("groupID", user.getGroupID());
		param.put("userLevel", user.getUserLevel() );
		param.put("description", user.getDescription() );
		param.put("useYN", user.getUseYN());
		param.put("userID",user.getUserID() );
		param.put("isHelper", user.getIsHelper());
		param.put("senderName", user.getSenderName());
		param.put("email", user.getEmail());
		param.put("cellPhone", user.getCellPhone());
		param.put("senderEmail", user.getSenderEmail());
		param.put("senderCellPhone", user.getSenderCellPhone());
		
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 *  p>사용자의 그룹을 변경한다 
	 * @param User
	 * @return
	 * @throws DataAccessException
	 */
	public int updatechangeUserGroupID(String userID, String groupID) throws DataAccessException {
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.updatechangeUserGroupID"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("groupID", groupID);	
		param.put("userID", userID);
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);			
	}
	
	/**
	 *  p>사용자의 기본 메뉴 권한을 추가한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserMenuDefaultAuth(String userID) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertMenuDefaultAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);		
	
	}

	/**
	 *  p>사용자의 데이타 권한을 그룹의 데이타 권한으로 추가한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserDataDefaultAuth(String userID) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertDataDefaultAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);	
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);			
	
	}
	
	/**
	 *  p>관리자의 모든 메뉴 권한을 추가한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMenuGroupAdminAuth(String userID) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertMenuGroupAdminAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);		
	
	}

	/**
	 *  p>관리자의 모든 데이터 권한을 추가한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertDataGroupAdminAuth(String userID) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertDataGroupAdminAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);	
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);			
	
	}
	
	
	
	
	/**
	 *  p>사용자의 메뉴 권한을 추가한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserMenuAuth(String userID, String subMenuID) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertUserMenuAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("subMenuID", subMenuID);	
		param.put("userID", userID);	
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);				
	
	}
	
	/**
	 *  p>사용자의 데이타 권한을 추가한다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserDataAuth(String userID, String dbID) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertUserDataAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID", dbID);	
		param.put("userID", userID);	
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);				
	
	}
	
	
	/**
	 *  p>사용자의 옵션 권한을 수정한다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public int updateUserAuth(String userID, User user) throws DataAccessException{

		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.updateUserAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("auth_csv", user.getAuthCSV());
		param.put("auth_direct", user.getAuthDirect());
		param.put("auth_related", user.getAuthRelated());
		param.put("auth_query", user.getAuthQuery());
		param.put("auth_send_mail", user.getAuthSendMail());
		param.put("auth_write_mail", user.getAuthWriteMail());
		param.put("auth_send_sms", user.getAuthSendSMS());
		param.put("auth_write_sms", user.getAuthWriteSMS());		
		param.put("userID", userID);
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);				
		
	}
	
	
	/**
	 *  p>사용자의 모든 메뉴 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAllUserMenuAuth(String userID) throws DataAccessException {
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.deleteAllMenuAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("_group", userID);	
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);			
		
	}
	
	/**
	 *  <p>사용자의 메뉴 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteUserMenuAuth(String userID, String subMenuID) throws DataAccessException {
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.deleteMenuAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);	
		param.put("subMenuID", subMenuID);	
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);				
		
	}
	

	/**
	 *  <p>사용자의 모든 데이타 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAllUserDataAuth(String userID) throws DataAccessException {
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.deleteAllDataAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("_group", userID);	
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);		
		
	}
		
	/**
	 *  <p>사용자의 데이타 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteUserDataAuth(String userID, String dbID) throws DataAccessException {
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.deleteDataAuth"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);		
		param.put("dbID", dbID);
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql, param);			
		
	}
	
	
	/**
	 * <p>사용자그룹(tm_usergroup)에서 보여준다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public Group viewGroup(String groupID) throws DataAccessException{
		
		
		Group group = new Group();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.viewgroup"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

			//넘겨받은 파라미터를 세팅한다. 
			Map<String,Object> param = new HashMap<String, Object>();		
			param.put("groupID", groupID);	
			
			//SQL문이 실행된다.
			try {				
				resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
			}catch(EmptyResultDataAccessException e1){		
			}
			if(resultMap!=null){
				group.setGroupID((String)(resultMap.get("groupID")));
				group.setGroupName((String)(resultMap.get("groupName")));
				group.setDescription((String)(resultMap.get("description")));
						
			} else {
				group.setGroupID("0");
			}
		return group;		
	}
	
	
	/**
	 * <p>사용자를 (ez_users)에서 보여준다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public User viewUser(String userID) throws DataAccessException{
		
		User user = new User();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.viewuser"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);	
		
			
			//SQL문이 실행된다. 
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);		
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				
				user.setUserID((String)(resultMap.get("userID")));
				user.setUserPWD((String)(resultMap.get("userPWD")));
				user.setUserName((String)(resultMap.get("userName")));
				user.setGroupID((String)(resultMap.get("groupID")));
				user.setGroupName((String)(resultMap.get("groupName")));
				user.setUserLevel((String)(resultMap.get("userLevel")));
				user.setEmail((String)(resultMap.get("email")==null?"":resultMap.get("email")));
				user.setCellPhone((String)(resultMap.get("cellPhone")==null?"":resultMap.get("cellPhone")));
				user.setDescription((String)(resultMap.get("description")==null?"":resultMap.get("description")));
				user.setUseYN((String)(resultMap.get("useYN")));
				user.setRegistDate(String.valueOf(resultMap.get("registDate")));
				user.setIsAdmin((String)(resultMap.get("isAdmin")));
				user.setAuthCSV((String)(resultMap.get("auth_csv")));
				user.setAuthDirect((String)(resultMap.get("auth_direct")));
				user.setAuthRelated((String)(resultMap.get("auth_related")));
				user.setAuthQuery((String)(resultMap.get("auth_query")));
				user.setAuthSendMail((String)(resultMap.get("auth_send_mail")));
				user.setAuthWriteMail((String)(resultMap.get("auth_write_mail")));
				user.setIsHelper((String)(resultMap.get("isHelper")));
				user.setSenderName((String)(resultMap.get("senderName")==null?"":resultMap.get("senderName")));
				user.setSenderEmail((String)(resultMap.get("senderEmail")==null?"":resultMap.get("senderEmail")));
				user.setSenderCellPhone((String)(resultMap.get("senderCellPhone")==null?"":resultMap.get("senderCellPhone")));
				user.setAuthSendSMS((String)(resultMap.get("auth_send_sms")));
				user.setAuthWriteSMS((String)(resultMap.get("auth_write_sms")));
				user.setFailCount(Integer.parseInt(String.valueOf(resultMap.get("failCount"))));
			}else{
				user.setUserID("");
			}
		return user;		
	}
	
	
public User viewHelper() throws DataAccessException{
		
		User user = new User();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.viewhelper"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		
		
			
			//SQL문이 실행된다. 
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);		
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				
				user.setUserID((String)(resultMap.get("userID")));
				
				
			}else{
				user.setUserID("");
			}
		return user;		
	}
	
	
	/**
	 * <p>그룹의 기본 권한을 가져온다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public User viewGroupAuth(String groupID) throws DataAccessException{
		
		User user = new User();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.groupAuth"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
			//넘겨받은 파라미터를 세팅한다. 
	
			Map<String,Object> param = new HashMap<String, Object>();		
			param.put("groupID", groupID);		
			
			//SQL문이 실행된다. 
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				
				user.setAuthCSV((String)(resultMap.get("auth_csv")));
				user.setAuthDirect((String)(resultMap.get("auth_direct")));
				user.setAuthRelated((String)(resultMap.get("auth_related")));
				user.setAuthQuery((String)(resultMap.get("auth_query")));
				user.setAuthSendMail((String)(resultMap.get("auth_send_mail")));
				user.setAuthWriteMail((String)(resultMap.get("auth_write_mail")));
				user.setAuthSendSMS((String)(resultMap.get("auth_send_sms")));
				user.setAuthWriteSMS((String)(resultMap.get("auth_write_sms")));

				
			}
		return user;		
	}	
	/**
	 * <p>그룹의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */
	public int getGroupTotalCount(Map<String, String> searchMap)  throws DataAccessException{
		
		String searchType = searchMap.get("searchType");
		String searchName = searchMap.get("searchName");
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.totalgroup"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		if(searchName!=null && !searchName.equals("")){
			sql += " WHERE "+searchType +" LIKE :searchName ";
		}
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("searchName", "%"+searchName+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	

	/**
	 * <p>계정의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */
	public int getUserTotalCount(Map<String, String> searchMap,String groupID, String userLevel, String useYN)  throws DataAccessException{
		
		String searchType = searchMap.get("searchType");
		String searchName = searchMap.get("searchName");
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.totaluser"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 

		String sqlWhere = " WHERE 1=1 ";
		
		sql += sqlWhere;
		//검색조건이 있다면
		if(searchName!=null && !searchName.equals("")){
			sql += " AND  "+searchType+" LIKE :searchName ";
		}					
		
		if(groupID!=null && !groupID.equals("")){
			sql += " AND u.groupID = '"+groupID+"'";
		}
		
		if(userLevel!=null && !userLevel.equals("")){
			sql += " AND u.userLevel = '"+userLevel+"'";
		}

		if(useYN!=null && !useYN.equals("")){
			sql += " AND u.useYN = '"+useYN+"'";
		}		
		
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("searchName", "%"+searchName+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql,param);
		
	}
	
	
	/**
	 *<p>tm_usergroup의 groupID의 max값을 가져온다.
	 */
	public int getMaxdbID() throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.getmaxgroupid");
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	
	/**
	 * <p>그룹코드/그룹리스트를 보여준다. 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Group> listUserGroupInfo() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectgrouplist");					
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {	
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Group  group = new Group();		
				group.setGroupID(rs.getString("groupID"));
				group.setGroupName(rs.getString("groupName"));				
				return group;
			}			
		};
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	


	/**
	 * <p>사용자ID의 메뉴별권한 
	 * @param userID
	 * @param mainMenuID
	 * @param subMenuID
	 * @return
	 * @throws DataAccessException
	 */

	public Map<String, Object> getUserAuth(String userID, String mainMenuID, String subMenuID) throws DataAccessException{		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.getauth"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		param.put("mainMenuID", mainMenuID);
		param.put("subMenuID", subMenuID);
		
		return getSimpleJdbcTemplate().queryForMap(sql,param);		
	}
	
	
	/**
	 * <p>사용자권한 추가 
	 * @param usersAuth
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserAuth(UserAuth userAuth) throws DataAccessException{	
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.insertauth"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Object[] params  = {userAuth.getUserID(), userAuth.getMainMenuID(), userAuth.getSubMenuID(),
										userAuth.getAuthType(), userAuth.getSubMenuAuth()};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userAuth.getUserID());
		param.put("mainMenuID", userAuth.getMainMenuID());
		param.put("subMenuID", userAuth.getSubMenuID());
		param.put("authType", userAuth.getAuthType());
		param.put("subMenuAuth", userAuth.getSubMenuAuth());

		
		return getSimpleJdbcTemplate().update(sql,params);
	}
	
	

	/**
	 * <p>db 정보 출력 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbSet> selectSetDBList() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectdb");	

		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbSet dbSet = new DbSet();
				dbSet.setDbID(rs.getString("dbID"));
				dbSet.setDbName(rs.getString("dbName"));
				dbSet.setDescription(rs.getString("description"));
				
				return dbSet;
			}			
		};
			
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	
	/**
	 * <p>권한정보출력 
	 * @param userLevel
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<UserAuth> selectUserLevelAuth(String userLevel) throws DataAccessException{		
	
		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectuserlevelauth");	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userLevel", userLevel);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				UserAuth userAuth = new UserAuth();
				userAuth.setMainMenuID(rs.getString("mainMenuID"));
				userAuth.setSubMenuID(rs.getString("subMenuID"));
				userAuth.setAuthType(rs.getString("authType"));
				userAuth.setSubMenuAuth(rs.getString("subMenuAuth"));
				
				return userAuth;
			}
		};
		return  getSimpleJdbcTemplate().query(sql,rowMapper,param);
	}
	
	
	/**
	 * <p>해당사용자 권한정보출력 
	 * @param userLevel
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<UserAuth> viewUserIDLevelAuth(String selUserID) throws DataAccessException{

		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.viewuserauth");	

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", selUserID);
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				UserAuth userAuth = new UserAuth();
				userAuth.setMainMenuID(rs.getString("mainMenuID"));
				userAuth.setSubMenuID(rs.getString("subMenuID"));
				userAuth.setAuthType(rs.getString("authType"));
				userAuth.setSubMenuAuth(rs.getString("subMenuAuth"));
				
				return userAuth;
			}
		};
		return  getSimpleJdbcTemplate().query(sql,rowMapper,param);
	}
	
	/**
	 * <p>해당사용자 메뉴 권한정보출력 
	 * @param userLevel
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<UserAuth> viewUserMenuAuth(String selUserID) throws DataAccessException{

		// submenu 에 부속 권한이 있으면 그 서브메뉴권한은 제외하고 가져온다
		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.viewuserMenuTreeAuth");	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", selUserID);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				UserAuth userAuth = new UserAuth();
				userAuth.setMainMenuID(rs.getString("mainMenuID"));
				userAuth.setSubMenuID(rs.getString("subMenuID").trim());
				userAuth.setAuthType(rs.getString("authType"));
				userAuth.setSubMenuAuth(rs.getString("subMenuAuth"));
				
				return userAuth;
			}
		};
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}

	/**
	 * <p>해당사용자 데이타 접근 권한정보출력 
	 * @param userLevel
	 * @return
	 * @throws DataAccessException
	 * @auth 윤훈범
	 */
	@SuppressWarnings("unchecked")
	public List<DbSet> viewUserDbSet(String selUserID) throws DataAccessException{

		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.viewUserDbSetAuth");	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", selUserID);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				DbSet dbSet = new DbSet();
				dbSet.setDbID(rs.getString("dbid"));
				return dbSet;
			}
		};
		return  getSimpleJdbcTemplate().query(sql,rowMapper,param);
	}
	

	
	/**
	 *< p>해당사용자의 권한삭제를 한다.  
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAuth(String userID)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.deleteauth"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		return  getSimpleJdbcTemplate().update(sql,param);		
	}
	
	/**
	 * <p>그릅관리자 및 전체관리자 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<User> selectUserMaster(String groupID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.usergroup.selectmaster");	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("groupID", groupID);
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				
				User user = new User();
				user.setUserID(rs.getString("userID"));
				user.setUserName(rs.getString("userName"));
				return user;
			}
		};
		return  getSimpleJdbcTemplate().query(sql,rowMapper,param);
	}
		
	/**
	 * <p>사용자를 (ez_users)에서 보여준다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public User getHelperUser() throws DataAccessException{
		
		User user = new User();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.helperuser"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.

			//SQL문이 실행된다. 
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql);		
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				
				user.setUserID((String)(resultMap.get("userID")));
				user.setUserPWD((String)(resultMap.get("userPWD")));
				user.setUserName((String)(resultMap.get("userName")));
				user.setGroupID((String)(resultMap.get("groupID")));
				user.setGroupName((String)(resultMap.get("groupName")));
				user.setUserLevel((String)(resultMap.get("userLevel")));
				user.setEmail((String)(resultMap.get("email")));
				user.setCellPhone((String)(resultMap.get("cellPhone")));
				user.setDescription((String)(resultMap.get("description")));
				user.setUseYN((String)(resultMap.get("useYN")));
				user.setRegistDate(String.valueOf(resultMap.get("registDate")));
				user.setIsAdmin((String)(resultMap.get("isAdmin")));
				user.setAuthCSV((String)(resultMap.get("auth_csv")));
				user.setAuthQuery((String)(resultMap.get("auth_query")));
				user.setAuthSendMail((String)(resultMap.get("auth_send_mail")));
				user.setAuthWriteMail((String)(resultMap.get("auth_write_mail")));
				user.setAuthSendSMS((String)(resultMap.get("auth_send_sms")));
				user.setAuthWriteSMS((String)(resultMap.get("auth_write_sms")));
				
				
			}else{
				user.setUserID("");
			}
		return user;		
	}
	
	/**
	 * <p> 모든 사용자  IsHelper 값 N으로 변경
	 * @return
	 * @throws DataAccessException
	 */
	public int setIsHelperN() throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.setishelpern"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql);				
	}
	
	/**
	 * <p> 사용자 ID 사용 제한 해제
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDerestrict(String userID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.usergroup.updateDerestrict"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		return  getSimpleJdbcTemplate().update(sql,param);	
	}
	
}
