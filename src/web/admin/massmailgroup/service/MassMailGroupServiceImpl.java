package web.admin.massmailgroup.service;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import web.admin.massmailgroup.dao.MassMailGroupDAO;
import web.admin.massmailgroup.model.MassMailGroup;
import web.admin.massmailgroup.service.MassMailGroupService;

public class MassMailGroupServiceImpl implements MassMailGroupService {
	
	private Logger logger = Logger.getLogger("TM");
	private MassMailGroupDAO massmailgroupDAO = null;
	
	public void setMassMailGroupDAO(MassMailGroupDAO massmailgroupDAO){
		this.massmailgroupDAO = massmailgroupDAO;
	}
	
	
	/**
	 * <p>대량 메일 그룹정보를 불러온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassMailGroup> listMassMailGroup(){
		List<MassMailGroup> result = null;
		try {
			result = massmailgroupDAO.listMassMailGroup();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량 메일 그룹정보를 불러온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassMailGroup> listMassMailGroup(Map<String, Object> searchMap){
		List<MassMailGroup> result = null;
		try {
			result = massmailgroupDAO.listMassMailGroup(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량 메일 그룹정보의 총카운트를 구해온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassMailGroupTotalCount(Map<String, Object> searchMap){
		int result = -1; 
		try{
			result = massmailgroupDAO.getMassMailGroupTotalCount(searchMap);
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	/**
	 * <p>대량 메일 그룹 정보를 등록한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailGroup(MassMailGroup massMailGroup) {
		int result = -1;
		try{
			if(massMailGroup.getIsDefault().equals("Y"))
				massmailgroupDAO.updateMassMailGroup_default(massMailGroup);
			result = massmailgroupDAO.insertMassMailGroup(massMailGroup);
			
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}

	/**
	 * <p>대량 메일 그룹 정보를 수정한다.
	 * @param massmailgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailGroup(MassMailGroup massMailGroup){
		int result = -1;
		try{
			result = massmailgroupDAO.updateMassMailGroup(massMailGroup);
			if(massMailGroup.getIsDefault().equals("Y"))
				massmailgroupDAO.updateMassMailGroup_default(massMailGroup);
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}

	/**
	 * <p>대량 메일그룹 정보를 삭제한다.
	 * @param massMailGroupIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailGroup(String[] massMailGroupIDs){
		int result = -1;
		try{
			for(int i=0;i<massMailGroupIDs.length;i++){
				result = massmailgroupDAO.deleteMassMailGroup(Integer.parseInt(massMailGroupIDs[i]));
			}
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	/**
	 * <p> 대량 메일그룹 가져와서 보여준다.
	 * @param boardID
	 * @return
	 */
	public MassMailGroup viewMassMailGroup(int massMailGroupID){
		MassMailGroup massmailgroup = null;
		
		try{
			massmailgroup = massmailgroupDAO.viewMassMailGroup(massMailGroupID);
		}catch(Exception e){
			logger.error(e);
		}
		
		return massmailgroup;
		
	}
	
	public MassMailGroup viewMassMailGroupChk(String massMailGroupName){
		MassMailGroup massmailgroup = null;
		
		try{
			massmailgroup = massmailgroupDAO.viewMassMailGroupChk(massMailGroupName);
		}catch(Exception e){
			logger.error(e);
		}
		
		return massmailgroup;
		
	}
}
