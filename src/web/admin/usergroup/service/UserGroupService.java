package web.admin.usergroup.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.springframework.dao.DataAccessException;

import web.admin.usergroup.model.*;


public interface UserGroupService {

	
	/**
	 * <p>그룹과 사용자 트리의 json 데이타를 불러온다
	 * @return
	 */	
	public String getJsonTreeGroupUser();
		

	/**
	 * <p>일부문자로 사용자를 검색한 json 데이타를 불러온다
	 * @return
	 */	
	public String getJsonQuickSearchUser( String userName);
	
	
	/**
	 * <p>사용자그룹(tm_usergroup)를 출력한다.
	 * @return
	 */	
	public List<Group> listGroup(int currentPage, int countPerPage, Map<String, String> searchMap);
	public List<Group> listGroup();
	
	
	/**
	 * <p>사용자(tm_users)를 출력한다.
	 * @return
	 */		
	public List<User>  listUser(int currentPage, int countPerPage, Map<String, String> searchMap, String groupID, String userLevel, String useYn);
	
	/**
	 * <p>사용자(tm_users)를 출력한다.
	 * @return
	 */    
	public List<User> listSearchUser(String userLevel, String groupID);
	
	/**
	 * 사용자그룹(tm_usergroup)에 추가한다.
	 * @param group
	 * @return
	 */
	public int insertGroup(Group group);
	
	
	/**
	 * 사용자를 (tm_users)에 추가한다.
	 * @param group
	 * @return
	 */
	public int insertUser(User user, UserAuth[] userAuth);
	
	/**
	 * 사용자그룹(tm_usergroup)에 삭제한다.
	 * @param groupID
	 * @return
	 */	
	public boolean deleteGroup(JSONArray group_ids);
	
	/**
	 * 사용자를 (tm_users)에 삭제한다.
	 * @param groupID
	 * @return
	 */	
	public boolean deleteUser(JSONArray userIDS);
	
	/**
	 * 사용자그룹(tm_usergroup)에서 수정한다.
	 * @param group
	 * @return
	 */
	public int updateGroup(Group group);
	
	
	/**
	 * 사용자그룹(tm_usergroup)에서 보여준다.
	 * @param groupID
	 * @return
	 */
	public Group viewGroup(String groupID);
	
	
	/**
	 * 사용자를 (tm_users)에서 보여준다.
	 * @param groupID
	 * @return
	 */
	public User viewUser(String userID);
	
	
	/**
	 * <p>그룹의 총카운트를 구해온다.
	 * @return
	 */	
	public int getGroupTotalCount(Map<String, String>searchMap);	
	
	
	/**
	 * <p>계정의 총카운트를 구해온다.
	 * @return
	 */	
	public int getUserTotalCount(Map<String, String> searchMap,String groupID, String userLevel, String useYN);
	
	
	/**
	 *<p>tm_usergroup의 groupID의 max값을 가져온다.
	 */
	public int getMaxdbID();
	
	
	/**
	 * <p>그룹코드/그룹명을 보여준다.
	 * @return
	 */	
	public List<Group> listUserGroupInfo();
	

	
	
	/**
	 * <p>사용자ID의 메뉴별권한 
	 * @param userID
	 * @param mainMenuID
	 * @param subMenuID
	 * @return	 
	 */
	public String[] getUserAuth(String userID, String mainMenuID, String subMenuID);
	
	

	/**
	 * <p>db 정보 출력 
	 * @return
	 */	
	public List<DbSet>  selectSetDBList();
	
	
	/**
	 * <p>권한정보출력 
	 * @param userLevel
	 * @return
	 */
	public List<UserAuth>  selectUserLevelAuth(String userLevel);
	
	/**
	 * <p>해당사용자 메뉴 권한 정보 json 출력 
	 * @return
	 */	
	public String getJsonMenuAuth( String userID );
	
	/**
	 * <p>해당사용자 권한정보출력 
	 * @param userLevel
	 * @return
	 */
	public List<UserAuth>  viewUserIDLevelAuth(String selUserID);
	
	
	/**
	 * <p>사용자 정보/권한 수정 
	 * @param user
	 * @param userAuth
	 * @return
	 */
	public int updateUsersAuth(User user, UserAuth[] userAuth);
	
	/**
	 * <p>사용자 의 그룹 변경 
	 * @param user
	 * @param userAuth
	 * @return
	 */
	public int updatechangeUserGroupID(String userID, String groupID);
	
	/**
	 * <p>그룹의 권한 저장
	 * @param JSONStr
	 * @return
	 */
	
	public boolean updateGroupAuth( String GroupID,String JSONStr );
	
	
	/**
	 * <p>사용자의 권한 저장
	 * @param JSONStr
	 * @return
	 */
	
	public boolean updateUserAuth( String userID, String JSONStr );
	

	/**
	 * <p>선택된사용자의 권한 저장
	 * @return
	 */
	
	public boolean updateSelectedUserAuth( String userIDJson, String authJson );
	
	
	/**
	 * <p>그릅관리자 및 전체관리자 
	 * @param groupID
	 * @return
	 */
	public List<User> selectUserMaster(String groupID);

	/**
	 * <p>사용자를 (ez_users)에서 보여준다 
	 * @param groupID
	 * @return
	 * @throws DataAccessException
	 */
	public User getHelperUser();
	
	public User viewHelper();
	
	public List<Group> listGroup_inc_admin(String userID);
	
	public List<Group> listGroup_inc(String userID);
	
	/**
	 * <p> 사용자 ID 사용 제한 해제
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDerestrict(String userID);
}
