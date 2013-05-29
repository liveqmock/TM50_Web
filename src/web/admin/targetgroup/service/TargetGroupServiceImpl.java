package web.admin.targetgroup.service;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import web.admin.targetgroup.dao.TargetGroupDAO;
import web.admin.targetgroup.model.TargetGroup;


public class TargetGroupServiceImpl implements TargetGroupService {
	
	private Logger logger = Logger.getLogger("TM");
	private TargetGroupDAO targetGroupDAO = null;
	
	public void setTargetGroupDAO(TargetGroupDAO targetGroupDAO){
		this.targetGroupDAO = targetGroupDAO;
	}
	
	
	/**
	 * <p>대량 메일 그룹정보를 불러온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<TargetGroup> listTargetGroup(){
		List<TargetGroup> result = null;
		try {
			result = targetGroupDAO.listTargetGroup();
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
	public List<TargetGroup> listTargetGroup(Map<String, Object> searchMap){
		List<TargetGroup> result = null;
		try {
			result = targetGroupDAO.listTargetGroup(searchMap);
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
	public int getTargetGroupTotalCount(Map<String, Object> searchMap){
		int result = -1; 
		try{
			result = targetGroupDAO.getTargetGroupTotalCount(searchMap);
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	/**
	 * <p>대량 메일 그룹 정보를 등록한다.
	 * @param targetgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetGroup(TargetGroup targetGroup) {
		int result = -1;
		try{
			if(targetGroup.getIsDefault().equals("Y"))
				targetGroupDAO.updateTargetGroup_default(targetGroup);
			result = targetGroupDAO.insertTargetGroup(targetGroup);
			
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}

	/**
	 * <p>대량 메일 그룹 정보를 수정한다.
	 * @param targetgroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetGroup(TargetGroup targetGroup){
		int result = -1;
		try{
			result = targetGroupDAO.updateTargetGroup(targetGroup);
			if(targetGroup.getIsDefault().equals("Y"))
				targetGroupDAO.updateTargetGroup_default(targetGroup);
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}

	/**
	 * <p>대량 메일그룹 정보를 삭제한다.
	 * @param targetGroupIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteTargetGroup(String[] targetGroupIDs){
		int result = -1;
		try{
			for(int i=0;i<targetGroupIDs.length;i++){
				result = targetGroupDAO.deleteTargetGroup(Integer.parseInt(targetGroupIDs[i]));
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
	public TargetGroup viewTargetGroup(int targetGroupID){
		TargetGroup targetgroup = null;
		
		try{
			targetgroup = targetGroupDAO.viewTargetGroup(targetGroupID);
		}catch(Exception e){
			logger.error(e);
		}
		
		return targetgroup;
		
	}
	
	public TargetGroup viewTargetGroupChk(String targetGroupName){
		TargetGroup targetgroup = null;
		
		try{
			targetgroup = targetGroupDAO.viewTargetGroupChk(targetGroupName);
		}catch(Exception e){
			logger.error(e);
		}
		
		return targetgroup;
		
	}
	
	public int getCountTarget(int targetGroupID ){
		int result = -1; 
		try{
			result = targetGroupDAO.getCountTarget(targetGroupID);
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	
	/**
	 * <p> 디폴트값이 Y인 대상자 그룹 분류의 정보를 가져온다
	 * @param 
	 * @return
	 */
	public List<TargetGroup> getTargetGroupDefaultY(){
		List<TargetGroup> result = null;
		try {
			result = targetGroupDAO.getTargetGroupDefaultY();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
