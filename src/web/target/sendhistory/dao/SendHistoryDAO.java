package web.target.sendhistory.dao;


import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.massmail.statistic.model.MassMailInfo;
import web.massmail.write.model.TargetingGroup;
import web.target.sendhistory.model.*;


public interface SendHistoryDAO {
	
	
	public int totalCountMassMailSendResult(String[] userInfo, Map<String, String> searchMap) throws DataAccessException;
	
	public List<MassMailSendResult> listMassMailSendResult(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;

	/**
	 * <p>메일저장내용보기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo viewMassMailInfo(int massmailID, int scheduleID) throws DataAccessException;
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TargetingGroup> listTargetingGroup(int massmailID) throws DataAccessException;
}
