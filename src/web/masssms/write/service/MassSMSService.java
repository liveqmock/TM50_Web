package web.masssms.write.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.usergroup.model.User;
import web.masssms.write.model.*;


public interface MassSMSService {
	
	/**
	 * <p>Backup Date 정보를 받아온다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public List<BackupDate> getBackupDate();

	
	/**
	 * <p>유저별 등록된 테스트핸드폰을 불러온다.
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Map<String, String>> listTesterHp(String userID,String testerHp);
	
	
	/**
	 *  <p>대량SMS 기본작성
	 * @param MassSMSInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSInfo(MassSMSInfo massSMSInfo);
	
	
	/**
	 * <p>가장 최근에 입력된 masssmsID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassSMSIDInfo();
	
	
	/**
	 * <p>대상자정보, 스케줄, 필터링, 메일내용입력 
	 */
	public void insertMassSMS(MassSMSInfo massSMSInfo, String[] targetIDs, String[] exceptYNs, MassSMSSchedule[] massSMSSchedule);
	
	
	
	/**
	 * <p>대량SMS 입력시 잘못입력됬을 때 일괄삭제 
	 * @param masssmsID
	 * @return
	 */
	public void deleteMassSMS(int masssmsID);
	
	
	/**
	 * <p>사용자를 정보를 받아온다 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public User getUserInfo(String userID);
	
	
	/**
	 * <p>시스템메일에 입력한다.
	 * @param systemNotify
	 * @return
	 */
	public void insertSystemNotify(SystemNotify[] systemNotifys);
	
	
	
	/**
	 * <p>테스트 전송SMS 리스트 
	 * @param notifyFlag
	 * @param userID
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */	
	public List<SystemNotify> listSystemNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap);
	
	
	/**
	 * <p>대량SMS 수정 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSInfo(MassSMSInfo massSMSInfo);
	
	/**
	 * <p>대량SMS 정보수정(나머지정보)
	 * @param massSMSInfo
	 * @param targetIDs
	 * @param exceptYNs
	 * @param massMailSchedule
	 */
	public void updateMassSMS(MassSMSInfo massSMSInfo, String[] targetIDs, String[] exceptYNs, MassSMSSchedule[] massSMSSchedule);
	
	
	/**
	 * <p>대량 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountMassSMSList(String[] userInfo, Map<String, String> searchMap);
	
	
	/**
	 * <p>대량SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSList> listMassSMSList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap);
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<SMSTargetingGroup> listTargetingGroup(int masssmsID);
	
	
	/**
	 * <p>대량SMS저장내용보기 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo viewMassSMSInfo(int masssmsID, int scheduleID);
	
	
	/**
	 * <p>일괄삭제 
	 * @param masssmsID
	 */
	public void deleteMassSMSAll(int masssmsID, int scheduleID);
	
	
	/**
	 * <p>발송 상태 변경
	 * @param masssmsID
	 * @param scheduleID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int masssmsID, int scheduleID, String state);
	
	
	
	/**
	 * <p>DB에 쿼리정보를 가져온다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getQeuryDB(int targetID);
	
	
	/**
	 * <p>타겟ID에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	*/	
	public List<OnetooneTarget> listOnetooneTarget(int targetID);
	
	
	/**
	 * <p>시스템SMS삭제(즉, 테스트SMS)
	 * @param notifyIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSystemNotify(String[] notifyIDs);
	
	
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap);
	
	
	/**
	 * <p>대량SMS 리스트 상태값 확인 
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 */
	public MassSMSList getMasssmsState(String masssmsID, String scheduleID);
	
	
	/**
	 * <p>대상자리스트를 출력한다. 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<Targeting> listTargeting(int currentPage, int countPerPage,Map<String, String> searchMap, String[] userInfo);
	
	
	/**
	 * <p>대상자리스트의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetingTotalCount(Map<String, String> searchMap, String[] userInfo);
	
	
	/**
	 * <p>반복SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */

	public int totalCountMassSMSRepeat(String[] userInfo, Map<String, String> searchMap);
	
	
	/**
	 * <p>반복SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSList> listMassSMSRepeat(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap);
	
	
	/**
	 * <p>반복SMS 정보보기 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo viewRepeatMassSMSInfo(int masssmsID);
	
	
	/**
	 * <p>반복SMS스케즐카운트 
	 * @param massmailID
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountRepeatSchedule(int masssmsID, Map<String, String> searchMap);
	
	
	/**
	 * <p>반복SMS 스케줄리스트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */	
	public  List<MassSMSList> listRepeatSchedule(int masssmsID, int currentPage, int countPerPage, Map<String, String> searchMap);
	
	
	/**
	 * <p>체크된 반복SMS 스케즐리스트 삭제 
	 * @param masssmsID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByChecked(int masssmsID, String[] scheduleIDs);
	
	
	/**
	 * <p>기간내 반복SMS 스케즐리스트 삭제 
	 * @param masssmsID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByDate(int masssmsID, String fromDate, String toDate);
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(String target_ids);
	


}
