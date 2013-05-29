package web.masssms.write.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import web.admin.usergroup.model.User;
import web.masssms.write.model.TargetingGroup;
import web.masssms.write.model.*;
import web.masssms.write.dao.MassSMSDAO;


public class MassSMSServiceImpl implements  MassSMSService{

	private Logger logger = Logger.getLogger("TM");
	private MassSMSDAO massSMSDAO = null;
	
	public void setMassSMSDAO(MassSMSDAO massSMSDAO){
		this.massSMSDAO = massSMSDAO;
	}
	
	
	/**
	 * <p>Backup Date 정보를 받아온다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public List<BackupDate> getBackupDate(){
		List<BackupDate> result = null;
		try{
			result = massSMSDAO.getBackupDate();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>유저별 등록된 테스트핸드폰을 불러온다.
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Map<String, String>> listTesterHp(String userID,String testerHp){
		List<Map<String, String>> result = null;
		try{
			result = massSMSDAO.listTesterHp(userID, testerHp);	
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 *  <p>대량SMS 기본작성
	 * @param MassSMSInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSInfo(MassSMSInfo massSMSInfo){
		int result = 0;
		try{
			result = massSMSDAO.insertMassSMSInfo(massSMSInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>가장 최근에 입력된 masssmsID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassSMSIDInfo(){
		int result = 0;
		try{
			result = massSMSDAO.getMassSMSIDInfo();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대상자정보, 스케줄, 필터링, 메일내용입력 
	 */
	public void insertMassSMS(MassSMSInfo massSMSInfo, String[] targetIDs, String[] exceptYNs, MassSMSSchedule[] massSMSSchedule){
		
		//대상자정보입력 
		for(int i=0;i<targetIDs.length;i++){
			 massSMSDAO.insertMassSMSTargetGroup(massSMSInfo.getMasssmsID(), Integer.parseInt(targetIDs[i]), exceptYNs[i]);
		}
		
		int expectTargetCount = massSMSDAO.expectTargetTotalCount(massSMSInfo.getMasssmsID());
			
		//발송스케줄 입력
		for(int i=0;i<massSMSSchedule.length;i++){			
			massSMSSchedule[i].setTargetTotalCount(expectTargetCount);
			if(massSMSSchedule[i]!=null)	massSMSDAO.insertMassSMSSchedule(massSMSSchedule[i]);			
		}
			
		//필터링입력 
		massSMSDAO.insertMassSMSFilterSet(massSMSInfo);		
		//메일내용입력 
		massSMSDAO.insertMassSMSSMS(massSMSInfo);

	}
	
	/**
	 * <p>대량SMS 입력시 잘못입력됬을 때 일괄삭제 
	 * @param masssmsID
	 * @return
	 */
	public void deleteMassSMS(int masssmsID){
		massSMSDAO.deleteMassSMSInfo(masssmsID);
		massSMSDAO.deleteMassSMSTargetGroup(masssmsID);
		massSMSDAO.deleteMassSMSFilterSet(masssmsID);		
		massSMSDAO.deleteMassSMSSMS(masssmsID);				
	}
	
	
	/**
	 * <p>사용자를 정보를 받아온다 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public User getUserInfo(String userID){
		User user = null;
		try{
			user = massSMSDAO.getUserInfo(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return user;
	}
	
	/**
	 * <p>시스템메일에 입력한다.
	 * @param systemNotify
	 * @return
	 */
	public void insertSystemNotify(SystemNotify[] systemNotifys){
		for(SystemNotify systemNotify : systemNotifys){
			if(systemNotify!=null) massSMSDAO.insertSystemNotify(systemNotify);
		}
	}
	
	/**
	 * <p>테스트 전송SMS 리스트 
	 * @param notifyFlag
	 * @param userID
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */	
	public List<SystemNotify> listSystemNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap){
		List<SystemNotify> result = null;
		try{
			result = massSMSDAO.listSystemNotify(notifyFlag, userID, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량SMS 수정 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSInfo(MassSMSInfo massSMSInfo){
		int result = 0;
		try{
			result = massSMSDAO.updateMassSMSInfo(massSMSInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량SMS 정보수정(나머지정보)
	 * @param massSMSInfo
	 * @param targetIDs
	 * @param exceptYNs
	 * @param massMailSchedule
	 */
	public void updateMassSMS(MassSMSInfo massSMSInfo, String[] targetIDs, String[] exceptYNs, MassSMSSchedule[] massSMSSchedule){		
		
		//1. 기존대상자그룹삭제 
		massSMSDAO.deleteMassSMSTargetGroup(massSMSInfo.getMasssmsID());
		
		//2. 대상자그룹수정 
		for(int i=0;i<targetIDs.length;i++){
			if(targetIDs[i]!=null) massSMSDAO.insertMassSMSTargetGroup(massSMSInfo.getMasssmsID(), Integer.parseInt(targetIDs[i]), exceptYNs[i]);
		}
		
		//3. 발송스케줄 일괄삭제후 입력 
		massSMSDAO.deleteMassSMSSchedule(massSMSInfo.getMasssmsID());
		//발송스케줄 입력
		for(int i=0;i<massSMSSchedule.length;i++){
			if(massSMSSchedule[i]!=null)	massSMSDAO.insertMassSMSSchedule(massSMSSchedule[i]);
		}
		
		//4. 필터링 업데이트 
		massSMSDAO.updateMassSMSFilterSet(massSMSInfo);
		
		//5. 메일내용업데이트 
		massSMSDAO.updateMassSMSSMS(massSMSInfo);
	}
	
	
	/**
	 * <p>대량 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountMassSMSList(String[] userInfo, Map<String, String> searchMap){
		int result = 0;
		try{
			result = massSMSDAO.totalCountMassSMSList(userInfo, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSList> listMassSMSList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap){
		List<MassSMSList> result = null;
		try{
			result = massSMSDAO.listMassSMSList(userInfo, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<SMSTargetingGroup> listTargetingGroup(int masssmsID){
		List<SMSTargetingGroup> result = null;
		try{
			result = massSMSDAO.listTargetingGroup(masssmsID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량SMS저장내용보기 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo viewMassSMSInfo(int masssmsID, int scheduleID){
		MassSMSInfo massSMSInfo = null;
		try{
			massSMSInfo = massSMSDAO.viewMassSMSInfo(masssmsID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return massSMSInfo;
	}
	
	
	/**
	 * <p>일괄삭제 
	 * @param masssmsID
	 */
	public void deleteMassSMSAll(int masssmsID, int scheduleID){
		massSMSDAO.updateMassSMSScheduleDeleteYN(masssmsID, scheduleID);		
	}
	
	/**
	 * <p>발송 상태 변경
	 * @param masssmsID
	 * @param scheduleID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int masssmsID, int scheduleID, String state){
		int result = 0;
		try{
			result = massSMSDAO.updateSendState(masssmsID, scheduleID, state);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>DB에 쿼리정보를 가져온다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getQeuryDB(int targetID){
		Map<String, Object> result = null;
		try{
			result = massSMSDAO.getQeuryDB(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>타겟ID에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	*/	
	public List<OnetooneTarget> listOnetooneTarget(int targetID){
		List<OnetooneTarget> result = null;
		try{
			result = massSMSDAO.listOnetooneTarget(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>시스템SMS삭제(즉, 테스트SMS)
	 * @param notifyIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSystemNotify(String[] notifyIDs){
		int result = 0;
		try{
			result = massSMSDAO.deleteSystemNotify(notifyIDs);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap){
		int result = 0;
		try{
			result = massSMSDAO.totalCountNotify(notifyFlag, userID, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량SMS 리스트 상태값 확인 
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 */
	public MassSMSList getMasssmsState(String masssmsID, String scheduleID){
		MassSMSList result = null;
		try{
			result = massSMSDAO.getMasssmsState(masssmsID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자리스트를 출력한다. 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<Targeting> listTargeting(int currentPage, int countPerPage,Map<String, String> searchMap, String[] userInfo){
		List<Targeting> result = null;
		try{
			result = massSMSDAO.listTargeting(currentPage, countPerPage, searchMap, userInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자리스트의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetingTotalCount(Map<String, String> searchMap, String[] userInfo){
		int result = 0;
		try{
			result = massSMSDAO.getTargetingTotalCount(searchMap, userInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>반복SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */

	public int totalCountMassSMSRepeat(String[] userInfo, Map<String, String> searchMap){
		int result = 0;
		try{
			result = massSMSDAO.totalCountMassSMSRepeat(userInfo, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>반복SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSList> listMassSMSRepeat(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap){
		List<MassSMSList> result = null;
		try{
			result = massSMSDAO.listMassSMSRepeat(userInfo, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>반복SMS 정보보기 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo viewRepeatMassSMSInfo(int masssmsID){
		MassSMSInfo massSMSInfo = null;
		try{
			massSMSInfo = massSMSDAO.viewRepeatMassSMSInfo(masssmsID);
		}catch(Exception e){
			logger.error(e);
		}
		return massSMSInfo;
	}
	
	/**
	 * <p>반복SMS스케즐카운트 
	 * @param massmailID
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountRepeatSchedule(int masssmsID, Map<String, String> searchMap){
		int result = 0;
		try{
			result = massSMSDAO.totalCountRepeatSchedule(masssmsID, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>반복SMS 스케줄리스트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */	
	public  List<MassSMSList> listRepeatSchedule(int masssmsID, int currentPage, int countPerPage, Map<String, String> searchMap){
		List<MassSMSList>  result = null;
		try{
			result = massSMSDAO.listRepeatSchedule(masssmsID, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>기간내 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 */
	public int deleteRepeatScheduleByDate(int masssmsID, String fromDate, String toDate){
		int result1=0,result2=0;
		result1 = massSMSDAO.deleteRepeatScheduleByDate(masssmsID, fromDate,toDate);
		result2 = massSMSDAO.updateRepeatSendEndDate(masssmsID);
		
		//massmailID에 해당되는 스케줄이 없다면 기본정보도 삭제한다.
		if(massSMSDAO.checkMasssmsSchedule(masssmsID)==0){
			massSMSDAO.deleteMassSMSInfo(masssmsID);
		}
		
		return result1*result2;
	}
	
	
	/**
	 * <p>체크된 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 */
	public int deleteRepeatScheduleByChecked(int masssmsID, String[] scheduleIDs){
		int result1=0,result2=0;
		result1 = massSMSDAO.deleteRepeatScheduleByChecked(masssmsID, scheduleIDs);
		result2 = massSMSDAO.updateRepeatSendEndDate(masssmsID);
		
		
		//massmailID에 해당되는 스케줄이 없다면 기본정보도 삭제한다.
		if(massSMSDAO.checkMasssmsSchedule(masssmsID)==0){
			massSMSDAO.deleteMassSMSInfo(masssmsID);
		}
		
		return result1*result2;
			
	}
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(String target_ids){
		List<TargetingGroup> result = null;
		try{
			result = massSMSDAO.listTargetingGroup(target_ids);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	

}
