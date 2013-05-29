package web.admin.targetgroup.service;


import java.util.List;
import java.util.Map;

import web.admin.targetgroup.model.TargetGroup;



public interface TargetGroupService {
	
	
	public List<TargetGroup> listTargetGroup();
	
	

	
	public List<TargetGroup> listTargetGroup(Map<String, Object> searchMap);
	
	
	
	public int getTargetGroupTotalCount(Map<String, Object> searchMap);
	
	
	public int insertTargetGroup(TargetGroup targetGroup);
	
	
	public int updateTargetGroup(TargetGroup targetGroup);
	
	
	public int deleteTargetGroup(String[] targetgroupIDs);
	
	
	public TargetGroup viewTargetGroup(int targetGroupID);
	
	
	public TargetGroup viewTargetGroupChk(String targetGroupName);
	
	public int getCountTarget(int targetGroupID );
	
	/**
	 * <p> 디폴트값이 Y인 대상자 그룹 분류의 정보를 가져온다
	 * @param 
	 * @return
	 */
	public List<TargetGroup> getTargetGroupDefaultY();
}
