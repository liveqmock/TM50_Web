package web.admin.massmailgroup.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.admin.massmailgroup.model.MassMailGroup;

/**
 * <P>대량메일 그룹 관리 인터페이스
 * @author limyh(임영호)
 *
 */
public interface MassMailGroupDAO {
	
	
	public List<MassMailGroup> listMassMailGroup() throws DataAccessException;

	/**
	 * <p>대량메일그룹 정보를 불러온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassMailGroup> listMassMailGroup(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>대량 메일 그룹정보의 총카운트를 구해온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassMailGroupTotalCount(Map<String, Object> searchMap) throws DataAccessException;
	
	/**
	 * <p>대량메일그룹 정보를 입력한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailGroup(MassMailGroup massMailGroup) throws DataAccessException;
	
	/**
	 * <p>대량메일그룹 정보를 수정한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailGroup(MassMailGroup massMailGroup) throws DataAccessException;
	
	public int updateMassMailGroup_default(MassMailGroup massMailGroup)throws DataAccessException ;
	/**
	 * <p>대량메일그룹 정보를 삭제한다.
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailGroup(int massMailGroupID) throws DataAccessException;
	
	/**
	 * <p> 대량 메일그룹 가져와서 보여준다.
	 * @param boardID
	 * @return
	 */
	public MassMailGroup viewMassMailGroup(int massMailGroupID)throws DataAccessException;
	
	
	public MassMailGroup viewMassMailGroupChk(String massMailGroupName)throws DataAccessException;
}
