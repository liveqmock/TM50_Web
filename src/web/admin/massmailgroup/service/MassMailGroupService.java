package web.admin.massmailgroup.service;


import java.util.List;
import java.util.Map;
import web.admin.massmailgroup.model.MassMailGroup;


public interface MassMailGroupService {
	
	/**
	 * <p>대량 메일 그룹정보를 불러온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailGroup> listMassMailGroup();
	
	

	/**
	 * <p>대량 메일 그룹정보를 불러온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailGroup> listMassMailGroup(Map<String, Object> searchMap);
	
	
	/**
	 * <p>대량 메일 그룹정보의 총카운트를 구해온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassMailGroupTotalCount(Map<String, Object> searchMap);
	
	/**
	 * <p>대량 메일 그룹 정보를 등록한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailGroup(MassMailGroup massMailGroup);
	
	/**
	 * <p>대량 메일 그룹 정보를 수정한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailGroup(MassMailGroup massMailGroup);
	
	/**
	 * <p>대량 메일그룹 정보를 삭제한다.
	 * @param massMailGroupIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailGroup(String[] massmailgroupIDs);
	
	/**
	 * <p> 대량 메일그룹 가져와서 보여준다.
	 * @param boardID
	 * @return
	 */
	public MassMailGroup viewMassMailGroup(int massMailGroupID);
	
	
	public MassMailGroup viewMassMailGroupChk(String massMailGroupName);
	
}
