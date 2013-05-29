package web.target.sendhistory.service;

import java.util.List;
import java.util.Map;

import web.massmail.statistic.model.MassMailInfo;
import web.massmail.write.model.TargetingGroup;
import web.target.sendhistory.model.MassMailSendResult;

public interface SendHistoryService {

	
	public int totalCountMassMailSendResult(String[] userInfo, Map<String, String> searchMap);
	
	public List<MassMailSendResult> listMassMailSendResult(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap);
	
	/**
	 * <p>메일저장내용보기 
	 * @param massmailID
	 * @return
	 */
	public MassMailInfo viewMassMailInfo(int massmailID, int scheduleID);
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(int massmailID);
}
