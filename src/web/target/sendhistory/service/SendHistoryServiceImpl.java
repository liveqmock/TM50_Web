package web.target.sendhistory.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import web.massmail.statistic.model.MassMailInfo;
import web.massmail.write.model.TargetingGroup;
import web.target.sendhistory.dao.*;
import web.target.sendhistory.model.*;


public class SendHistoryServiceImpl implements SendHistoryService{
	
	private Logger logger = Logger.getLogger("TM");
	private SendHistoryDAO sendHistoryDAO = null; 

	
	public void setSendHistoryDAO(SendHistoryDAO sendHistoryDAO){
		this.sendHistoryDAO = sendHistoryDAO;
	}
	
	public int totalCountMassMailSendResult(String[] userInfo, Map<String, String> searchMap){
		int result = 0;
		try{
			result = sendHistoryDAO.totalCountMassMailSendResult(userInfo, searchMap);
		}catch(Exception e){
			logger.error(e);
			System.out.println(e.toString());
		}
		return result;
	}
	
	
	public List<MassMailSendResult> listMassMailSendResult(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap){
		List<MassMailSendResult> result = null;
		try{
			result = sendHistoryDAO.listMassMailSendResult(userInfo, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			System.out.println(e.toString());
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>메일저장내용보기 
	 * @param massmailID
	 * @return
	 */
	public MassMailInfo viewMassMailInfo(int massmailID, int scheduleID){
		MassMailInfo massMailInfo = null;
		try{
			massMailInfo = sendHistoryDAO.viewMassMailInfo(massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return massMailInfo;
	}
	

	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(int massmailID){
		List<TargetingGroup> result = null;
		try{
			result = sendHistoryDAO.listTargetingGroup(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
