package web.target.targetui.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import web.target.targetui.dao.TargetUIDAO;
import web.target.targetui.model.OnetooneTarget;
import web.target.targetui.model.TargetList;
import web.target.targetui.model.TargetUIGeneralInfo;
import web.target.targetui.model.TargetUIList;
import web.target.targetui.model.TargetUIOneToOne;
import web.target.targetui.model.TargetUIWhere;

public class TargetUIServiceImpl implements TargetUIService {
	private TargetUIDAO targetUIDAO = null;
	private Logger logger = Logger.getLogger("TM");
	
	public void setTargetUIDAO(TargetUIDAO targetUIDAO){
		this.targetUIDAO = targetUIDAO;		
	}
	
	
	/**
	 * 회원정보 UI 리스트
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<TargetUIList> listTargetUI(){
		List<TargetUIList> result = null; 
		
		try{
			result = targetUIDAO.listTargetUI();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * 기본 회원정보UI 
	 * @param 
	 * @return
	 */
	public int getDefaultTargetUIID() {
		int result = 0; 
		
		try{
			result = targetUIDAO.getDefaultTargetUIID();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 회원정보UI 기본정보 불러오기 
	 * @param id
	 * @return
	 */
	public TargetUIList viewTargetUI(int id){
		TargetUIList result = null;
		try{
			result = targetUIDAO.viewTargetUI(id);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 회원정보UI Where절 정보 불러오기
	 * @param id
	 * @return
	 */
	public List<TargetUIWhere> getMakeWhere(int id){
		List<TargetUIWhere> result = null;
		try{
			result = targetUIDAO.getMakeWhere(id);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * tm_target_ui_select 에서 원투원 정보 가져오기
	 * @param id
	 * @return
	 */
	public List<TargetUIOneToOne> getSelectOneToOne(int id){
		List<TargetUIOneToOne> result = null;
		try{
			result = targetUIDAO.getSelectOneToOne(id);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 */
	public int insertTargetList(TargetList targetList) {		
		int result = 0;
		try{
			result = targetUIDAO.insertTargetList(targetList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public int updateTargetList(TargetList targetList)  {
		int result = 0;
		try{
			result = targetUIDAO.updateTargetList(targetList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	
	}
	
	/**
	 * <p>ez_target_list에서 최근 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID(){
		int result = 0;
		try{
			result = targetUIDAO.getMaxTargetID();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(List<OnetooneTarget> onetooneTargetList){
		int result = 0;
		try{
			for(int i=0;i<onetooneTargetList.size();i++){
				result = targetUIDAO.insertOnetooneTarget(onetooneTargetList.get(i));
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID) {
		 Map<String, Object> result = null;
		 try{
			 result = targetUIDAO.getDBInfo(dbID);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * 회원정보UI 입력값을 저장한다 
	 * @param targetUIGeneralInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetUIGeneralInfo(int targetUIManagerID, TargetUIGeneralInfo targetUIGeneralInfo)
	{
		int result = 0;
		try{
				result = targetUIDAO.insertTargetUIGeneralInfo(targetUIManagerID, targetUIGeneralInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * targetID로 TargetManagerID를 가져온다
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetManagerID(int targetID)
	{
		int result = 0;
		try{
				result = targetUIDAO.getTargetManagerID(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>대상자를 보여준다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public TargetList viewTargetList(int targetID){
		
		TargetList result = null;
		try{
			result = targetUIDAO.viewTargetList(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
		
	}
	
	public List<TargetUIGeneralInfo> viewTargetUIGeneralInfo(int targetID, int targetUIManagerID){
		List<TargetUIGeneralInfo> result = null;
		try{
			result = targetUIDAO.viewTargetUIGeneralInfo(targetID, targetUIManagerID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public int deleteTargetUIGeneralInfo(int targetID, int targetUIManagerID){
		int result=0;
		try{
			result = targetUIDAO.deleteTargetUIGeneralInfo(targetID, targetUIManagerID);
		}catch(Exception e){
			logger.error(e);
		}
		
		
		return result;
	}

}
