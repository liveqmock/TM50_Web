package web.masssms.write.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.usergroup.model.User;
import web.masssms.write.model.*;


public interface MassSMSDAO {

	/**
	 * <p>Backup Date 정보를 받아온다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */	
	public List<BackupDate> getBackupDate() throws DataAccessException;
	
	
	/**
	 * <p>유저별 등록된 테스트핸드폰을 불러온다.
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Map<String, String>> listTesterHp(String userID,String testerHp) throws DataAccessException;
	
	
	/**
	 *  <p>대량SMS 기본작성
	 * @param MassSMSInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSInfo(MassSMSInfo massSMSInfo)  throws DataAccessException;
	
	
	/**
	 * <p>가장 최근에 입력된 masssmsID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassSMSIDInfo() throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 타게팅 그룹등록 
	 * @param masssmsID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSTargetGroup(int masssmsID, int targetID,  String exceptYN) throws DataAccessException;
	
	
	
	/**
	 * <p>대상자 예상카운트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int expectTargetTotalCount(int masssmsID) throws DataAccessException;
	
	/**
	 * <p>대량SMS 스케줄등록 
	 * @param massMailInfo 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSSchedule(MassSMSSchedule massSMSSchedule) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 필터링등록 
	 * @param massMailInfo 
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSFilterSet(MassSMSInfo massSMSInfo) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 메일내용 입력 
	 * @param massMailInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassSMSSMS(MassSMSInfo massSMSInfo) throws DataAccessException;
	
	
	/**
	 * <p>기본정보를 삭제한다.
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSInfo(int masssmsID) throws DataAccessException;
	
	
	/**
	 * <p>대상그룹삭제 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSTargetGroup(int masssmsID) throws DataAccessException;
	
	
	/**
	 * <p>필터설정삭제
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSFilterSet(int masssmsID) throws DataAccessException;
	
	
	/**
	 * <p>SMS정보삭제
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSSMS(int masssmsID) throws DataAccessException;
	
	
	/**
	 * <p>사용자를 정보를 받아온다 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public User getUserInfo(String userID) throws DataAccessException;
	
	
	/**
	 * <p>시스템메일에 입력한다.
	 * @param systemNotify
	 * @return
	 * @throws DataAccessException
	 */
	public int insertSystemNotify(SystemNotify systemNotify) throws DataAccessException;
	
	
	
	/**
	 * <p>테스트 전송SMS 리스트 
	 * @param notifyFlag
	 * @param userID
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */	
	public List<SystemNotify> listSystemNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 수정 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSInfo(MassSMSInfo massSMSInfo) throws DataAccessException;
	
	
	/**
	 * <p>스케줄일괄삭제
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSSchedule(int masssmsID) throws DataAccessException;
	
	
	/**
	 * <p>스케줄삭제
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSSchedule(int masssmsID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 필터링수정 
	 * @param masssmsID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSFilterSet(MassSMSInfo massSMSInfo) throws DataAccessException;
	
	
	/**
	 * <p>대량메일 메일내용 수정
	 * @param massMailInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSSMS(MassSMSInfo massSMSInfo) throws DataAccessException;
	
	
	/**
	 * <p>대량 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountMassSMSList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSList> listMassSMSList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<SMSTargetingGroup> listTargetingGroup(int masssmsID) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS저장내용보기 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo viewMassSMSInfo(int masssmsID, int scheduleID) throws DataAccessException;
	
	
	
	/**
	 * <p>필터링삭제
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSFiltering(int masssmsID, int scheduleID) throws DataAccessException;
	
	
	
	/**
	 * <p>통계삭제
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassSMSStatistic(int masssmsID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 삭제 (deleteYN = 'Y')
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassSMSScheduleDeleteYN(int masssmsID, int scheduleID) throws DataAccessException;
	
	
	
	/**
	 * <p>발송 상태 변경
	 * @param masssmsID
	 * @param scheduleID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int masssmsID, int scheduleID, String state) throws DataAccessException;
	
	
	/**
	 * <p>DB에 쿼리정보를 가져온다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getQeuryDB(int targetID) throws DataAccessException;
	
	
	/**
	 * <p>타겟ID에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public List<OnetooneTarget> listOnetooneTarget(int targetID) throws DataAccessException;
	
	
	/**
	 * <p>시스템SMS삭제(즉, 테스트SMS)
	 * @param notifyIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSystemNotify(String[] notifyIDs) throws DataAccessException;
	
	
	/**
	 * <p>대량SMS 리스트 상태값 확인 
	 * @param masssmsID
	 * @param scheduleID
	 * @return
	 */
	public MassSMSList getMasssmsState(String masssmsID, String scheduleID)  throws DataAccessException;
	
	
	/**
	 * <p>대상자리스트를 출력한다. 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<Targeting> listTargeting(int currentPage, int countPerPage,Map<String, String> searchMap, String[] userInfo) throws DataAccessException;
	
	
	/**
	 * <p>대상자리스트의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetingTotalCount(Map<String, String> searchMap, String[] userInfo)  throws DataAccessException;
	
	
	/**
	 * <p>반복SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */

	public int totalCountMassSMSRepeat(String[] userInfo, Map<String, String> searchMap) throws DataAccessException;
	
	
	
	
	/**
	 * <p>반복SMS 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassSMSList> listMassSMSRepeat(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>반복SMS 정보보기 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public MassSMSInfo viewRepeatMassSMSInfo(int masssmsID) throws DataAccessException;
	
	
	/**
	 * <p>반복SMS스케즐카운트 
	 * @param massmailID
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountRepeatSchedule(int masssmsID, Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>반복SMS 스케줄리스트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */	
	public  List<MassSMSList> listRepeatSchedule(int masssmsID, int currentPage, int countPerPage, Map<String, String> searchMap)  throws DataAccessException;
	
	
	/**
	 * <p>체크된 반복SMS 스케즐리스트 삭제 
	 * @param masssmsID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByChecked(int masssmsID, String[] scheduleIDs) throws DataAccessException;
	
	
	/**
	 * <p>기간내 반복SMS 스케즐리스트 삭제 
	 * @param masssmsID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByDate(int masssmsID, String fromDate, String toDate) throws DataAccessException;
	
	
	/**
	 * <p>반복SMS 스케줄리스트 삭제시 가장 마지막것으로 업데이트 
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRepeatSendEndDate(int masssmsID) throws DataAccessException;
		
	
	/**
	 * <p>반복SMS 삭제시 남은 스케줄이 있는지 체크하기 위해
	 * @param masssmsID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkMasssmsSchedule(int masssmsID) throws DataAccessException;
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public List<TargetingGroup> listTargetingGroup(String target_ids) throws DataAccessException;
}
