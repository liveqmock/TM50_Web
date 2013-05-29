package web.admin.targetgroup.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;

import web.admin.targetgroup.model.TargetGroup;


/**
 * <P>대량메일 그룹 관리 인터페이스
 * @author limyh(임영호)
 *
 */
public interface TargetGroupDAO {
	
	
	public List<TargetGroup> listTargetGroup() throws DataAccessException;

	/**
	 * <p>대량메일그룹 정보를 불러온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<TargetGroup> listTargetGroup(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>대량 메일 그룹정보의 총카운트를 구해온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetGroupTotalCount(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>대량메일그룹 정보를 입력한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetGroup(TargetGroup targetGroup) throws DataAccessException;
	
	/**
	 * <p>대량메일그룹 정보를 수정한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetGroup(TargetGroup targetGroup) throws DataAccessException;
	
	public int updateTargetGroup_default(TargetGroup targetGroup)throws DataAccessException ;
	/**
	 * <p>대량메일그룹 정보를 삭제한다.
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteTargetGroup(int targetGroupID) throws DataAccessException;
	
	/**
	 * <p> 대량 메일그룹 가져와서 보여준다.
	 * @param boardID
	 * @return
	 */
	public TargetGroup viewTargetGroup(int targetGroupID)throws DataAccessException;
	
	
	public TargetGroup viewTargetGroupChk(String targetGroupName)throws DataAccessException;
	
	public int getCountTarget(int targetGroupID) throws DataAccessException;
	
	/**
	 * <p> 디폴트값이 Y인 대상자 그룹 분류의 정보를 가져온다
	 * @param 
	 * @return
	 */
	public List<TargetGroup> getTargetGroupDefaultY() throws DataAccessException;
}
