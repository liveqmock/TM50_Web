package web.target.targetui.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.target.targetui.model.OnetooneTarget;
import web.target.targetui.model.TargetList;
import web.target.targetui.model.TargetUIGeneralInfo;
import web.target.targetui.model.TargetUIList;
import web.target.targetui.model.TargetUIOneToOne;
import web.target.targetui.model.TargetUIWhere;

public interface TargetUIService {
	
	/**
	 * 회원정보 UI 리스트
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<TargetUIList> listTargetUI();
	
	/**
	 * 기본 회원정보UI 
	 * @param 
	 * @return
	 */
	public int getDefaultTargetUIID() ;
	
	/**
	 * 회원정보UI 기본정보 불러오기 
	 * @param id
	 * @return
	 */
	public TargetUIList viewTargetUI(int id);
	
	/**
	 * 회원정보UI Where절 정보 불러오기
	 * @param id
	 * @return
	 */
	public List<TargetUIWhere> getMakeWhere(int id);
	
	/**
	 * tm_target_ui_select 에서 원투원 정보 가져오기
	 * @param id
	 * @return
	 */
	public List<TargetUIOneToOne> getSelectOneToOne(int id);
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 */
	public int insertTargetList(TargetList targetList);
	
	public int updateTargetList(TargetList targetList) ;

	/**
	 * <p>ez_target_list에서 최근 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID();
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(List<OnetooneTarget> onetooneTargetList);
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID);
	
	/**
	 * 회원정보UI 입력값을 저장한다 
	 * @param targetUIGeneralInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetUIGeneralInfo(int targetUIManagerID, TargetUIGeneralInfo targetUIGeneralInfo);
	
	/**
	 * targetID로 TargetManagerID를 가져온다
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetManagerID(int targetID);
	
	/**
	 * <p>대상자를 보여준다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public TargetList viewTargetList(int targetID);
	
	public List<TargetUIGeneralInfo> viewTargetUIGeneralInfo(int targetID, int targetUIManagerID);
	
	public int deleteTargetUIGeneralInfo(int targetID, int targetUIManagerID);
	
	

}
