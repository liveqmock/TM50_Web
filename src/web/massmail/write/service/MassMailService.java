package web.massmail.write.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;

import web.admin.usergroup.model.User;
import web.massmail.write.model.*;


public interface  MassMailService {
	
	
	/**
	 * <p>메일본문 읽어오기 
	 * @param massmailID
	 * @return
	 */
	public Map<String,Object> getMailContent(int massmailID);
	
	
	/**
	 * <p>가장 최근에 입력된 massmailID를 가져온다. 
	 * @return
	 */
	public int getMassMailIDInfo();
	
	
	
	/**
	 * <p>메일저장내용보기 
	 * @param massmailID
	 * @return
	 */
	public MassMailInfo viewMassMailInfo(int massmailID, int scheduleID);
	
	
	/**
	 *  <p>대량메일작성 
	 * @param MassMailState
	 * @return
	 */
	public int insertMassMailInfo(MassMailInfo massMailInfo);
	
	

	/**
	 * <p>대량메일 기본삭제 
	 * @param massmailID
	 * @return
	 */
	public void deleteMassMail(int massmailID);

	/**
	 * <p>대량메일정보 일괄삭제 
	 * @param massmailID
	 * @return
	 */
	public void deleteMassMailAll(int massmailID, int scheduleID);
	
	/**
	 * <p>대량메일 정보 입력 
	 * @param massMailInfo
	 * @return
	 */
	public void  insertMassMail(MassMailInfo massMailInfo, String[] targetIDs, String[] exceptYNs, MassMailSchedule[] massMailSchedule);
	
	
	
	/**
	 * <p>대량메일 스케줄등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 */
	public int insertMassMailSchedule(MassMailSchedule massMailInfo);
	
	
	/**
	 * <p>대량메일 필터링등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 */
	public int insertMassMailFilterSet(MassMailInfo massMailInfo);
	
	
	/**
	 * <p>대량메일 메일정보 입력 
	 * @param massMailInfo
	 * @return
	 */
	public int insertMassMailMail(MassMailInfo massMailInfo);
	
	
	/**
	 * <p>메일링크삽입 
	 * @param massMailLink
	 * @return
	 */
	public void insertMassMailLink(ArrayList<MassMailLink>  massmailIinkList);
	
	/**
	 * <p>대량메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MassMailList> listMassMailList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap);
	
	/**
	 * <p>대량메일 Self 리스트 
	 * @param userAuth
	 * @param countPerPage
	 * @return
	 */
	public List<MassMailList> listMassMailSelfList(String[] userInfo, int countPerPage);
			
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 */
	public int totalCountMassMailList(String[] userInfo,Map<String, String> searchMap);

	/**
	 * <p>유저별 등록된 테스트메일을 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, String>> listTesterEmail(String userID,String testerEmail);
	
	
	/**
	 * <p>대량메일에 등록할 대상자리스트를 출력한다. 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */	
	public List<Targeting> listTargeting(int currentPage, int countPerPage,Map<String, String> searchMap, String[] userInfo);
	
	
	/**
	 * <p>대상자리스트의 총 레코드수를 가져온다.
	 * @return
	 */
	public int getTargetingTotalCount(Map<String, String> searchMap, String[] userInfo);
	
	
	/**
	 * <p>대량메일 타게팅 그룹등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 */
	public int insertMassMailTargetGroup(int massmailID, int targetID, String exceptYN);
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(int massmailID);
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(String target_ids);
	
	/**
	 * <p>메일링크리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailLink> listMassMailLink(int massmailID);
	
	
	/**
	 * <p>대량메일 수정 
	 * @param massmailID
	 * @return
	 */
	public int updateMassMailInfo(MassMailInfo massMailInfo);
	
	/**
	 * <p>대량메일 메일내용 수정
	 * @param massMailInfo
	 * @return
	 */
	public int updateMassMailMail(MassMailInfo massMailInfo);
	
	
	/**
	 * <p>대량메일 정보수정(나머지정보)
	 * @param massMailInfo
	 * @param targetIDs
	 * @param exceptYNs
	 * @param massMailSchedule
	 */
	public void updateMassMail(MassMailInfo massMailInfo, String[] targetIDs, String[] exceptYNs, MassMailSchedule[] massMailSchedule);
	
	
	/**
	 * <p>ez_config에서 해당되는 configValue값을 구해온다.
	 * @param configFlag
	 * @param configName
	 * @return
	 * @throws DataAccessException
	 */
	public String getConfigValue(String configFlag, String configName);
	
	
	/**
	 * <p>메일컨텐츠 업데이트(링크분석후)
	 * @param massmailID
	 * @return
	 */
	public int updateMailContent(int massmailID, String mailContent);
	
	
	/**
	 * <p>메일링크에서 선택된 메일링크 삭제 
	 * @param massmailID
	 * @param linkID
	 * @return
	 */
	public int deleteMailLinkByLinkID(int massmailID, String[] linkIDs);
	
	
	
	/**
	 * <p>메일링크 처리후 최종 상태값변경 
	 * @param massmailID
	 * @return
	 */
	public int updateAllScheduleState(int massmailID, String state);
	
	
	/**
	 * <p>스케줄삭제
	 * @param massmailID
	 * @return
	 */
	public int deleteMassMailSchedule(int massmailID);
	
	
	/**
	 * <p>메일링크정보삭제
	 * @param massmailID
	 * @return
	 */
	public int deleteMassMailLink(int massmailID);
	
	
	
	/**
	 * <p>링크타입을 일반타입 or 수신거부타입으로 변경
	 * @param massmailID
	 * @param linkID
	 * @param linkType
	 * @return
	 */
	public int updateMassMailLinkType(int massmailID, int linkID, String linkType);
	
	
	/**
	 * <p>DB에 쿼리정보를 가져온다. 
	 * @param targetID
	 * @return
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
	 * <p>시스템메일에 입력한다.
	 * @param systemNotify
	 * @return
	 */
	public void insertSystemNotify(SystemNotify[] systemNotifys);
	
	
	
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
	 */
	public List<OnetooneTarget> selectOnetooneByTargetID(String[] targetIDs);
	
	
	/**
	 * <p>메일템플릿 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MailTemplate> listMailTemplate(String userID, String groupID, String userAuth, String templateType);
	
	
	/**
	 * <p>메일 템플릿 보기 
	 * @param templateID
	 * @return
	 */
	public MailTemplate viewMailTemplate(int templateID);
	

	/**
	 * <p>템플릿 적용시 사용카운트 증가 
	 * @param templateID
	 * @return
	 */
	public int updateUsedCountTemplate(int templateID);
	
	
	/**
	 * <p>시스템메일삭제(즉, 테스트메일)
	 * @param notifyIDs
	 * @return
	 */
	public int deleteSystemNotify(String[] notifyIDs);
	
	
	/**
	 * <p>테스트메일 삭제 
	 * @param userID
	 * @param testerEmails
	 * @return
	 */
	public int deleteTesterEmail(String userID, String[] testerEmails);
	
	
	/**
	 * <p>첨부파일 리스트 
	 * @param userID
	 * @return
	 */
	public List<AttachedFile> listAttachedFile(String userID);
	
	
	/**
	 * <p>첨부파일 삽입 
	 * @param attachedFile
	 * @return
	 */
	public int insertAttachedFile(AttachedFile attachedFile);
	
	
	/**
	 * <p>해당되는 파일 정보 가져오기 
	 * @param fileID
	 * @param fileKey
	 * @return
	 */
	public AttachedFile getAttachedFile(String fileKey);
	
	
	
	/**
	 * <p>첨부파일 디비에서 삭제 
	 * @param fileKeys
	 * @return
	 */
	public int deleteAttachedFile(String[] fileKeys);
	
	

	/**
	 * <p>반복메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MassMailList> listMassMailRepeat(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap);
	
	
	
	
	/**
	 * <p>반복메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */

	public int totalCountMassMailRepeat(String[] userInfo, Map<String, String> searchMap);
	
	
	
	/**
	 * <p>반복메일 스케줄리스트 
	 * @param massmailID
	 * @return
	 */
	public  List<MassMailList> listRepeatSchedule(int massmailID, int currentPage, int countPerPage, Map<String, String> searchMap);
	
	
	/**
	 * <p>반복메일스케즐카운트 
	 * @param massmailID
	 * @param searchMap
	 * @return
	 */
	public int totalCountRepeatSchedule(int massmailID, Map<String, String> searchMap);
	
	
	/**
	 * <p>반복메일 정보보기 
	 * @param massmailID
	 * @return
	 */
	public MassMailInfo viewRepeatMassmailInfo(int massmailID);
	
	
	/**
	 * <p>체크된 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 */
	public int deleteRepeatScheduleByChecked(int massmailID, String[] scheduleIDs);
	
	
	/**
	 * <p>기간내 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 */
	public int deleteRepeatScheduleByDate(int massmailID, String fromDate, String toDate);
	
	
	/**
	 * <p>사용자아이디에 해당되는 보내는 사람리스트 
	 * @param userID
	 * @return
	 */
	public List<Sender> selectSenderByUserID(String groupID, String userID, String defaultYN);
	
	/**
	 * <p>대량메일 리스트 상태값 확인 
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public MassMailList getMassmailState(String massmail_id, String schedule_id);
	
	/**
	 * <p>사용자를 정보를 받아온다 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public User getUserInfo(String userID);
	
	/**
	 * <p>Backup Date 정보를 받아온다. 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public List<BackupDate> getBackupDate();
	
	
	
	/**
	 * <p>발송 상태 변경
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int massmailID, int scheduleID, String state);
	
	
	public List<FilterManager> getFilter();
	

	/**
	 * <p>이미지 파일 업로드 
	 * @param imageFile
	 * @return
	 */
	public int insertImageFile(ImageFile imageFile);
	
	/**
	 * <p>이미지 파일 리스트 
	 * @param userID
	 * @return
	 */
	public List<ImageFile> listImageFile(String userID);
	
	
	/**
	 * <p>이미지파일 디비에서 삭제 
	 * @param fileKeys
	 * @return
	 */
	public int deleteImageFile(String[] fileKeys);
	
	/**
	 * 동일한 이름의 이미지파일이 있는지 확인	
	 * @param fileName
	 * @param userID
	 * @return
	 */
	public List<ImageFile> isExistImageFile(String fileName, String userID);
	
	/**
	 * <p>발송할 대량메일 리스트 - 우선순위 관리창에서 사용 
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailPriority> getMassMailList() ;
	
	/**
	 * <p>우선순위 저장 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updatePriority(Map<String, Object>[] maps);
	
	
	/**
	 * <p>발송결과리스트 - 상태 값 변경시 비교할 현재 DB 상태 값 추출
	 * @param massmailID
	 * @param schdeduleID
	 * @return
	 * @throws DataAccessException
	 */
	
	public String getMassMailState(int massmailID, int scheduleID);
	
	

	
	/**
	 * <p>수신거부 링크 리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailLink> listMassMailRejectLink(int massmailID);
}
