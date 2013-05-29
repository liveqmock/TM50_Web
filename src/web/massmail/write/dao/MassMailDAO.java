package web.massmail.write.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;

import web.admin.usergroup.model.User;
import web.massmail.write.model.*;


public interface  MassMailDAO {
	
	
	/**
	 * <p>메일본문 읽어오기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> getMailContent(int massmailID) throws DataAccessException;
	
	/**
	 * <p>가장 최근에 입력된 massmailID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMassMailIDInfo() throws DataAccessException;
	
	/**
	 * <p>메일저장내용보기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo viewMassMailInfo(int massmailID, int scheduleID) throws DataAccessException;
	
	
	
	/**
	 *  <p>대량메일작성 
	 * @param MassMailState
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailInfo(MassMailInfo massMailInfo)  throws DataAccessException;
	
	
	
	/**
	 * <p>대량메일 타게팅 그룹등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailTargetGroup(int massmailID, int targetID, String exceptYN) throws DataAccessException;
	
	
	/**
	 * <p>대량메일 스케줄등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailSchedule(MassMailSchedule massMailInfo) throws DataAccessException;
	
	
	
	/**
	 * <p>대량메일 필터링등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailFilterSet(MassMailInfo massMailInfo) throws DataAccessException;
	
	
	/**
	 * <p>대량메일 메일정보 입력 
	 * @param massMailInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailMail(MassMailInfo massMailInfo) throws DataAccessException;
	
	
	
	/**
	 * <p>메일링크삽입 
	 * @param massMailLink
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMassMailLink(MassMailLink massMailLink) throws DataAccessException;
	
	
	/**
	 * <p>대량메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailList> listMassMailList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	/**
	 * <p>대량메일 Self 리스트 
	 * @param userAuth
	 * @param countPerPage
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailList> listMassMailSelfList(String[] userInfo, int countPerPage) throws DataAccessException;
	
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountMassMailList(String[] userInfo, Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>유저별 등록된 테스트메일을 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	public  List<Map<String, String>> listTesterEmail(String userID, String testerEmail) throws DataAccessException;
	
	/**
	 * <p>대량메일에 등록할 대상자리스트를 출력한다. 
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
	 * <p>대량메일 수정 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailInfo(MassMailInfo massMailInfo) throws DataAccessException;
	
	
	/**
	 * <p>대량메일 승인 날짜 업데이트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateApproveDate(int massmailID) throws DataAccessException;
	
	/**
	 * <p>대량메일 필터링수정 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailFilterSet(MassMailInfo massMailInfo) throws DataAccessException;
	
	
		
	/**
	 * <p>기본정보를 삭제한다.
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailInfo(int massmailID) throws DataAccessException;
	
	
	/**
	 * <p>대상그룹삭제 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailTargetGroup(int massmailID) throws DataAccessException;
	
	
	
	/**
	 * <p>필터설정삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailFilterSet(int massmailID) throws DataAccessException;
	
	
	/**
	 * <p>필터링삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailFiltering(int massmailID, int scheduleID) throws DataAccessException;
	
	
	
	
	/**
	 * <p>스케줄일괄삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailSchedule(int massmailID) throws DataAccessException;
	
	/**
	 * <p>스케줄삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailSchedule(int massmailID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>메일정보삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailMail(int massmailID) throws DataAccessException;
	
	
	
	/**
	 * <p>메일링크정보삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailLink(int massmailID) throws DataAccessException;
	
	
	/**
	 * <p>메일링크클릭삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailLinkClick(int massmailID, int scheduleID) throws DataAccessException;
	
	
	/**
	 * <p>도메인통계삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailDomainStatistic(int massmailID, int scheduleID) throws DataAccessException;
	
	
	
	/**
	 * <p>실패통계삭제
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailFailStatistic(int massmailID, int scheduleID) throws DataAccessException;
	
	
	
	/**
	 * <p>시간통계삭제 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMassMailTimeStatistic(int massmailID, int scheduleID) throws DataAccessException;
	
	
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public List<TargetingGroup> listTargetingGroup(int massmailID) throws DataAccessException;
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param target_ids
	 * @return
	 * @throws DataAccessException
	 */
	public List<TargetingGroup> listTargetingGroup(String target_ids) throws DataAccessException;
	
	/**
	 * <p>메일링크리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailLink> listMassMailLink(int massmailID) throws DataAccessException;
	
	
	
	
	/**
	 * <p>대상자그룹조회 
	 * @param massmailID
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public int getCountByTargetID(int massmailID, int targetID)  throws DataAccessException;
	
	/**
	 * <p>대량메일 메일내용 수정
	 * @param massMailInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailMail(MassMailInfo massMailInfo) throws DataAccessException;
	
	
	/**
	 * <p>ez_config에서 해당되는 configValue값을 구해온다.
	 * @param configFlag
	 * @param configName
	 * @return
	 * @throws DataAccessException
	 */
	public String getConfigValue(String configFlag, String configName)  throws DataAccessException;
	
	
	/**
	 * <p>메일컨텐츠 업데이트(링크분석후)
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMailContent(int massmailID, String mailContent) throws DataAccessException;
	
	
	
	/**
	 * <p>메일링크에서 선택된 메일링크 삭제 
	 * @param massmailID
	 * @param linkID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteMailLinkByLinkID(int massmailID, String[] linkIDs) throws DataAccessException;
	
	
	
	/**
	 * <p>메일링크 처리후 최종 상태값변경 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateAllScheduleState(int massmailID, String state) throws DataAccessException;
	
	
	
	/**
	 * <p>메일링크 타입을 수신거부로 변경되면 일단 기존꺼를 모두 일반으로 변경한다.
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMailLinkTypeAll(int massmailID) throws DataAccessException;
	
	
	/**
	 * <p>메일타입을 변경한다.
	 * @param linkID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMailLinkType(int linkID, String linkType) throws DataAccessException;
	
	
	
	/**
	 * <p>시스템메일에 입력한다.
	 * @param systemNotify
	 * @return
	 * @throws DataAccessException
	 */
	public int insertSystemNotify(SystemNotify systemNotify) throws DataAccessException;
	
	
	
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
	 * <p>테스트 전송메일 리스트 
	 * @param notifyFlag
	 * @param userID
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<SystemNotify> listSystemNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap);
	
	
	


	
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;

	
	
	/**
	 * <p>타겟ID에 해당되는 원투원 정보 
	 * @param targetIDs
	 * @return
	 * @throws DataAccessException
	 */
	public List<OnetooneTarget> selectOnetooneByTargetID(String[] targetIDs) throws DataAccessException;
	
	
	/**
	 * <p>메일템플릿 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailTemplate> listMailTemplate(String userID, String groupID, String userAuth, String templateType) throws DataAccessException;
	
	
	/**
	 * <p>메일 템플릿 보기 
	 * @param templateID
	 * @return
	 * @throws DataAccessException
	 */
	public MailTemplate viewMailTemplate(int templateID)  throws DataAccessException;
	
	
	
	/**
	 * <p>템플릿 적용시 사용카운트 증가 
	 * @param templateID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateUsedCountTemplate(int templateID) throws DataAccessException;
	
	
	/**
	 * <p>시스템메일삭제(즉, 테스트메일)
	 * @param notifyIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSystemNotify(String[] notifyIDs) throws DataAccessException;

	
	
	/**
	 * <p>테스트메일 삭제 
	 * @param userID
	 * @param testerEmails
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteTesterEmail(String userID, String[] testerEmails) throws DataAccessException;
	
	
	
	/**
	 * <p>첨부파일 리스트 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public List<AttachedFile> listAttachedFile(String userID) throws DataAccessException;
	
	
	
	/**
	 * <p>첨부파일 삽입 
	 * @param attachedFile
	 * @return
	 * @throws DataAccessException
	 */
	public int insertAttachedFile(AttachedFile attachedFile) throws DataAccessException;
	
	
	
	/**
	 * <p>해당되는 파일 정보 가져오기 
	 * @param fileID
	 * @param fileKey
	 * @return
	 * @throws DataAccessException
	 */
	public AttachedFile getAttachedFile(String fileKey) throws DataAccessException;
	
	
	/**
	 * <p>첨부파일 디비에서 삭제 
	 * @param fileKeys
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteAttachedFile(String[] fileKeys) throws DataAccessException;
	
		
	
	/**
	 * <p>반복메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailList> listMassMailRepeat(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>반복메일 리스트 카운트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */

	public int totalCountMassMailRepeat(String[] userInfo, Map<String, String> searchMap) throws DataAccessException;
	
	
	
	
	/**
	 * <p>반복메일 스케줄리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public  List<MassMailList> listRepeatSchedule(int massmailID, int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>반복메일스케즐카운트 
	 * @param massmailID
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountRepeatSchedule(int massmailID, Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>반복메일 정보보기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo viewRepeatMassmailInfo(int massmailID) throws DataAccessException;
	
	
	
	/**
	 * <p>체크된 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByChecked(int massmailID, String[] scheduleIDs) throws DataAccessException;
	
	
	/**
	 * <p>기간내 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteRepeatScheduleByDate(int massmailID, String fromDate, String toDate) throws DataAccessException;
	
	
	/**
	 * <p>반복메일 스케줄리스트 삭제시 가장 마지막것으로 업데이트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRepeatSendEndDate(int massmailID) throws DataAccessException;
	
	
	
	/**
	 * <p>사용자아이디에 해당되는 보내는 사람리스트 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Sender> selectSenderByUserID(String groupID, String userID, String defaultYN) throws DataAccessException;
	
	
	
	/**
	 * <p>반복메일 삭제시 남은 스케줄이 있는지 체크하기 위해
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkMassmailSchedule(int massmailID)  throws DataAccessException;

	
	
	/**
	 * <p>대상자 예상카운트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int expectTargetTotalCount(int massmailID) throws DataAccessException;
	

	/**
	 * <p>대량메일 삭제 (deleteYN = 'Y')
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMassMailScheduleDeleteYN(int massmailID, int scheduleID) throws DataAccessException;
	
	/**
	 * <p>대량메일 리스트 상태값 확인 
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public MassMailList getMassmailState(String massmail_id, String schedule_id)  throws DataAccessException;
	
	/**
	 * <p>사용자를 정보를 받아온다 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public User getUserInfo(String userID) throws DataAccessException;
	
	/**
	 * <p>Backup Date 정보를 받아온다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public List<BackupDate> getBackupDate() throws DataAccessException;
	
	/**
	 * <p>발송 상태 변경
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int massmailID, int scheduleID, String state) throws DataAccessException;
	
	
	public List<FilterManager> getFilter() throws DataAccessException;
	
	/**
	 * <p>이미지 파일 업로드 
	 * @param imageFile
	 * @return
	 * @throws DataAccessException
	 */
	public int insertImageFile(ImageFile imageFile) throws DataAccessException;
	
	/**
	 * <p>이미지 파일 리스트 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ImageFile> listImageFile(String userID) throws DataAccessException;
	
	
	/**
	 * <p>이미지파일 디비에서 삭제 
	 * @param fileKeys
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteImageFile(String[] fileKeys) throws DataAccessException;
	
	/**
	 * 동일한 이름의 이미지파일이 있는지 확인	
	 * @param fileName
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public List<ImageFile> isExistImageFile(String fileName, String userID) throws DataAccessException;
	
	/**
	 * <p>발송할 대량메일 리스트 - 우선순위 관리창에서 사용 
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailPriority> getMassMailList() throws DataAccessException;
	
	/**
	 * <p>우선순위 저장 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updatePriority(Map<String, Object>[] maps) throws DataAccessException;
	
	
	/**
	 * <p>발송결과리스트 - 상태 값 변경시 비교할 현재 DB 상태 값 추출
	 * @param massmailID
	 * @param schdeduleID
	 * @return
	 * @throws DataAccessException
	 */
	
	public String getMassMailState(int massmailID, int scheduleID) throws DataAccessException;
	
	
	
	
	/**
	 * <p>수신거부 링크 리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailLink> listMassMailRejectLink(int massmailID) throws DataAccessException;
}



	

