package web.target.targetui.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.target.targetui.model.OnetooneTarget;
import web.target.targetui.model.TargetUIGeneralInfo;
import web.target.targetui.model.TargetUIList;
import web.target.targetui.model.TargetList;
import web.target.targetui.model.TargetUIOneToOne;
import web.target.targetui.model.TargetUIWhere;

public interface TargetUIDAO {
	

	/**
	 * 회원정보UI 리스트
	 */
	public List<TargetUIList> listTargetUI() throws DataAccessException;

	/**
	 * 기본 회원정보UI 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getDefaultTargetUIID() throws DataAccessException;
	
	
	/**
	 * 회원정보UI 기본정보 불러오기 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public TargetUIList viewTargetUI(int id) throws DataAccessException;
	
	/**
	 * 회원정보UI Where절 정보 불러오기
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<TargetUIWhere> getMakeWhere(int id) throws DataAccessException;
	
	/**
	 * tm_target_ui_select 에서 원투원 정보 가져오기
	 * @param id
	 * @return
	 */
	public List<TargetUIOneToOne> getSelectOneToOne(int id) throws DataAccessException;
	
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetList(TargetList targetList) throws DataAccessException;
	
	public int updateTargetList(TargetList targetList) throws DataAccessException;
	
	/**
	 * <p>ez_target_list에서 최근 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID() throws DataAccessException;
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(OnetooneTarget onetooneTarget) throws DataAccessException;
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID)  throws DataAccessException;
	
	/**
	 * 회원정보UI 입력값을 저장한다 
	 * @param targetUIGeneralInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetUIGeneralInfo(int targetUIManagerID, TargetUIGeneralInfo targetUIGeneralInfo) throws DataAccessException;
	
	/**
	 * targetID로 TargetManagerID를 가져온다
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetManagerID(int targetID) throws DataAccessException;
	
	/**
	 * <p>대상자를 보여준다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public TargetList viewTargetList(int targetID) throws DataAccessException;
	
	public List<TargetUIGeneralInfo> viewTargetUIGeneralInfo(int targetID, int targetUIManagerID) throws DataAccessException;
	
	public int deleteTargetUIGeneralInfo(int targetID, int targetUIManagerID) throws DataAccessException;
	
}
