package web.admin.usergroup.dao;


import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.admin.usergroup.model.*;


public interface UserGroupDAO {

	
	/**
	 * <p>트리에 이용할 그룹의 모든 데이타를 불러온다
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Group> treeGroup();

	/**
	 * <p>트리에 이용할  그룹별 사용자의 데이타를 불러온다
	 * @return
	 * @throws DataAccessException
	 */	
	public List<User> treeUser( String groupID );

	/**
	 * <p>자동완성에서 검색한 사용자의 데이타를 불러온다
	 * @return
	 * @throws DataAccessException
	 */	
	public List<User> quickSearchUser( String userName );
	
		
	/**
	 * <p>사용자그룹(tm_usergroup) 리스트를 보여준다.
	 * @return
	 * @throws DataAccessException
	 */
		
	public List<Group> listGroup(int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException;
	 
	/**
	 * <p>사용자그룹(tm_usergroup) 리스트를 보여준다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<User> listSearchUser(String userLevel, String groupID) throws DataAccessException;
	 
	/**
	 * <p>사용자(tm_users) 리스트를 보여준다.
	 * @return
	 * @throws DataAccessException
	 */		
	public List<User> listUser(int currentPage, int countPerPage, Map<String, String> searchMap, String groupID, String userLevel, String useYN) throws DataAccessException;
		
	/**
	 * <p>tm_usergroup에 그룹을 인서트한다.
	 * @param group
	 * @return
	 * @throws DataAccessException
	 */
	public int insertGroup(Group group)  throws DataAccessException;
	
	/**
	 * <p>tm_users에 사용자를  인서트한다.
	 * @param user
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUser(User user)  throws DataAccessException;
	
	/**
	 * <p>tm_users에 그룹관리자를  인서트한다.
	 * @param group
	 * @return
	 * @throws DataAccessException
	 */
	public int insertgroupadmin(User user) throws DataAccessException;
	
	/**
	 *< p>사용자그룹(tm_usergroup)에서 삭제한다. 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteGroup(String groupID)  throws DataAccessException;
	
	
	/**
	 *< p>사용자를 tm_users 에서 삭제한다. 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteUser(String userID)  throws DataAccessException;
	
	/**
	 *  p>사용자의 기본 메뉴 권한을 추가한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserMenuDefaultAuth(String userID) throws DataAccessException;
	
	
	/**
	 *  p>사용자의 데이타 권한을 그룹의 데이타 권한으로 추가한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserDataDefaultAuth(String userID) throws DataAccessException;
	
	/**
	 *  p>그룹관리자의 모든 메뉴 권한을 추가한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMenuGroupAdminAuth(String userID) throws DataAccessException;
	
	/**
	 *  p>그룹관리자의 모든 데이터 권한을 추가한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertDataGroupAdminAuth(String userID) throws DataAccessException;
	
	/**
	 *  p>사용자의 메뉴 권한을 수정한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserMenuAuth(String userID, String subMenuID) throws DataAccessException;
	/**
	 *  p>사용자의 데이타 권한을 추가한다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserDataAuth(String userID, String dbID) throws DataAccessException;
	
	/**
	 *  p>사용자의 옵션 권한을 수정한다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public int updateUserAuth(String userID, User user) throws DataAccessException;	
	
	/**
	 *  p>사용자의 모든 메뉴 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAllUserMenuAuth(String userID) throws DataAccessException;
	
	/**
	 *  p>사용자의 메뉴 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteUserMenuAuth(String userID, String subMenuID) throws DataAccessException;
	

	/**
	 *  p>사용자의 모든 데이타 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAllUserDataAuth(String userID) throws DataAccessException;
	
	
	/**
	 *  p>사용자의 데이타 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteUserDataAuth(String userID, String dbID) throws DataAccessException;
	
	
	/**
	 *  p>사용자그룹(tm_usergroup)에서 수정한다. 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateGroup(Group group) throws DataAccessException;
	
	/**
	 *  p>그룹의 메뉴 권한을 수정한다. 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertGroupMenuAuth(String groupID, String subMenuID) throws DataAccessException;
	
	/**
	 *  p>그룹의 데이타 권한을 추가한다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertGroupDataAuth(String groupID, String dbID) throws DataAccessException;
	
	
	/**
	 *  p>그룹의 옵션 권한을 수정한다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public int updateGroupAuth(String groupID, User user) throws DataAccessException;

	/**
	 *  p>그룹의 메뉴 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteGroupAllMenuAuth(String groupID) throws DataAccessException;
	
	/**
	 *  p>그룹의 데이타 권한을 삭제한다 
	 * @param subMenuIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteGroupAllDataAuth(String groupID) throws DataAccessException;
	
	/**
	 *  p>사용자를 (tm_users)에서 수정한다. 
	 * @param User
	 * @return
	 * @throws DataAccessException
	 */
	public int updateUser(User user) throws DataAccessException;
	
	/**
	 *  p>사용자의 그룹을 변경한다 
	 * @param User
	 * @return
	 * @throws DataAccessException
	 */
	public int updatechangeUserGroupID(String userID, String groupID) throws DataAccessException;
	
	
	/**
	 * <p>사용자그룹(tm_usergroup)에서 보여준다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public Group viewGroup(String groupID) throws DataAccessException;
	
	
	/**
	 * <p>사용자를 (tm_users)에서 보여준다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public User viewUser(String userID) throws DataAccessException;
	
	/**
	 * <p>그룹의 기본 권한을 가져온다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public User viewGroupAuth(String userID) throws DataAccessException;
	
	
	
	/**
	 * <p>그룹의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */	
	public int getGroupTotalCount(Map<String, String> searchMap)  throws DataAccessException;
	
	
	/**
	 * <p>계정의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */	
	public int getUserTotalCount(Map<String, String> searchMap,String groupID, String userLevel, String useYN)  throws DataAccessException;
	
	
	/**
	 *<p>tm_usergroup의 groupID의 max값을 가져온다.
	 */
	public int getMaxdbID() throws DataAccessException;
	
	
	/**
	 * <p>그룹코드/그룹리스트를 보여준다. 
	 * @return
	 * @throws DataAccessException
	 */
	public List<Group> listUserGroupInfo() throws DataAccessException;
	

	
	/**
	 * <p>사용자ID의 메뉴별권한 
	 * @param userID
	 * @param mainMenuID
	 * @param subMenuID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getUserAuth(String userID, String mainMenuID, String subMenuID) throws DataAccessException;
	
	
	/**
	 * <p>사용자권한 추가 
	 * @param usersAuth
	 * @return
	 * @throws DataAccessException
	 */
	public int insertUserAuth(UserAuth userAuth) throws DataAccessException;
	
	
	
	/**
	 * <p>db 정보 출력 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbSet> selectSetDBList() throws DataAccessException;
	
	
	/**
	 * <p>권한정보출력 
	 * @param userLevel
	 * @return
	 * @throws DataAccessException
	 */
	public List<UserAuth> selectUserLevelAuth(String userLevel) throws DataAccessException;
	

	/**
	 * <p>해당사용자 메뉴 권한정보출력 
	 * @param userLevel
	 * @return
	 * @throws DataAccessException
	 */
	
	public List<UserAuth> viewUserMenuAuth(String selUserID) throws DataAccessException;
	
	
	/**
	 * <p>해당사용자 데이타 접근 권한정보출력 
	 * @param userLevel
	 * @return
	 * @throws DataAccessException
	 * @auth 윤훈범
	 */
	public List<DbSet> viewUserDbSet(String selUserID) throws DataAccessException;
	
	
	
	/**
	 * <p>해당사용자 권한정보출력 
	 * @param userLevel
	 * @return
	 * @throws DataAccessException
	 */
	public List<UserAuth> viewUserIDLevelAuth(String selUserID) throws DataAccessException;
	
	
	/**
	 *< p>권한삭제를 한다.  
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAuth(String userID)  throws DataAccessException;
	
	
	
	/**
	 * <p>그릅관리자 및 전체관리자 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public List<User> selectUserMaster(String groupID) throws DataAccessException;
	
	/**
	 * <p>문의하기 관리자 정보를 받아온다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public User getHelperUser() throws DataAccessException;
	
	/**
	 * <p>문의하기 관리자 정보를 보여준다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public User viewHelper() throws DataAccessException;
	

	/**
	 * <p> 모든 사용자 IsHelper 값 N으로 변경
	 * @return
	 * @throws DataAccessException
	 */
	public int setIsHelperN() throws DataAccessException;
	
	 
	public List<Group> listGroup_inc_admin(String userID) throws DataAccessException;
	
	public List<Group> listGroup_inc(String userID) throws DataAccessException;
	
	/**
	 * <p> 사용자 ID 사용 제한 해제
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDerestrict(String userID) throws DataAccessException;
}
