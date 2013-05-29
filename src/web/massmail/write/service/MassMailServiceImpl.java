package web.massmail.write.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import web.massmail.write.dao.MassMailDAO;
import web.massmail.write.model.*;

import web.admin.usergroup.model.User;
import web.common.util.Constant;

public class MassMailServiceImpl implements MassMailService{

	private Logger logger = Logger.getLogger("TM");
	private MassMailDAO massMailDAO = null;
	
	public void setMassMailDAO(MassMailDAO massMailDAO){
		this.massMailDAO = massMailDAO;
	}
	
	
	/**
	 * <p>메일본문 읽어오기 
	 * @param massmailID
	 * @return
	 */
	public Map<String,Object> getMailContent(int massmailID){
		Map<String,Object> result = null;
		try{
			result = massMailDAO.getMailContent(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>가장 최근에 입력된 massmailID를 가져온다. 
	 * @return
	 */
	public int getMassMailIDInfo(){
		int result = 0;
		try{
			result = massMailDAO.getMassMailIDInfo();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 *  <p>대량메일작성 
	 * @param MassMailState
	 * @return
	 */
	public int insertMassMailInfo(MassMailInfo massMailInfo){
		int result = 0;
		try{
			result = massMailDAO.insertMassMailInfo(massMailInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>메일링크삽입 
	 * @param massMailLink
	 * @return
	 */
	public void insertMassMailLink(ArrayList<MassMailLink>  massmailLinkList){
		
		for(MassMailLink massMailLink : massmailLinkList){
				massMailDAO.insertMassMailLink(massMailLink);
		}
	}
	
	/**
	 * <p>링크타입을 일반타입 or 수신거부타입으로 변경
	 * @param massmailID
	 * @param linkID
	 * @param linkType
	 * @return
	 */
	public int  updateMassMailLinkType(int massmailID, int linkID, String linkType){
		
		//수신거부 링크이면 기존에 수신거부는 일반으로 변경 
		if(linkType.equals(Constant.LINK_TYPE_REJECT)){
			massMailDAO.updateMailLinkTypeAll(massmailID); //모두 일반으로 변경 
		}
		return massMailDAO.updateMailLinkType(linkID, linkType); 
	}
	
	
	/**
	 * <p>대량메일 입력시 잘못입력됬을 때 일괄삭제 
	 * @param massmailID
	 * @return
	 */
	public void deleteMassMail(int massmailID){
		massMailDAO.deleteMassMailInfo(massmailID);
		massMailDAO.deleteMassMailTargetGroup(massmailID);
		massMailDAO.deleteMassMailFilterSet(massmailID);		
		massMailDAO.deleteMassMailMail(massmailID);
		massMailDAO.deleteMassMailLink(massmailID);		
	}
	
	
	/**
	 * <p>일괄삭제 
	 * @param massmailID
	 */
	public void deleteMassMailAll(int massmailID, int scheduleID){
		massMailDAO.updateMassMailScheduleDeleteYN(massmailID, scheduleID);		
	}
	
	
	/**
	 * <p>메일저장내용보기 
	 * @param massmailID
	 * @return
	 */
	public MassMailInfo viewMassMailInfo(int massmailID, int scheduleID){
		MassMailInfo massMailInfo = null;
		try{
			massMailInfo = massMailDAO.viewMassMailInfo(massmailID, scheduleID);
		}catch(Exception e){
			logger.error(e);
		}
		return massMailInfo;
	}
	
	/**
	 * <p>대량메일 타게팅 그룹등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 */
	public int insertMassMailTargetGroup(int massmailID, int targetID, String exceptYN){
		int result = 0;
		try{
			result = massMailDAO.insertMassMailTargetGroup(massmailID, targetID, exceptYN);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량메일 스케줄등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 */
	public int insertMassMailSchedule(MassMailSchedule massMailInfo){
		int result = 0;
		try{
			result = massMailDAO.insertMassMailSchedule(massMailInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량메일 필터링등록 
	 * @param massmailID
	 * @param targetIDs
	 * @param priorNums
	 * @param exceptYNs
	 * @return
	 */
	public int insertMassMailFilterSet(MassMailInfo massMailInfo){
		int result = 0;
		try{
			result = massMailDAO.insertMassMailFilterSet(massMailInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 메일정보 입력 
	 * @param massMailInfo
	 * @return
	 */
	public int insertMassMailMail(MassMailInfo massMailInfo){
		int result = 0;
		try{
			result = massMailDAO.insertMassMailMail(massMailInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	

	/**
	 * <p>대상자정보, 스케줄, 필터링, 메일내용입력 
	 */
	public void insertMassMail(MassMailInfo massMailInfo, String[] targetIDs, String[] exceptYNs, MassMailSchedule[] massMailSchedule){
			
		//대상자정보입력 
		for(int i=0;i<targetIDs.length;i++){
			 massMailDAO.insertMassMailTargetGroup(massMailInfo.getMassmailID(), Integer.parseInt(targetIDs[i]), exceptYNs[i]);
		}
		
		int expectTargetCount = massMailDAO.expectTargetTotalCount(massMailInfo.getMassmailID());
		
		//발송스케줄 입력
		for(int i=0;i<massMailSchedule.length;i++){
			massMailSchedule[i].setTargetTotalCount(expectTargetCount);
			if(massMailSchedule[i]!=null)	massMailDAO.insertMassMailSchedule(massMailSchedule[i]);			
		}
			
		//필터링입력 
		massMailDAO.insertMassMailFilterSet(massMailInfo);		
		//메일내용입력 
		massMailDAO.insertMassMailMail(massMailInfo);

	}
	
	
	/**
	 * <p>대량메일 정보수정(나머지정보)
	 * @param massMailInfo
	 * @param targetIDs
	 * @param exceptYNs
	 * @param massMailSchedule
	 */
	public void updateMassMail(MassMailInfo massMailInfo, String[] targetIDs, String[] exceptYNs, MassMailSchedule[] massMailSchedule){
		
		
		//1. 기존대상자그룹삭제 
		massMailDAO.deleteMassMailTargetGroup(massMailInfo.getMassmailID());
		
		//2. 대상자그룹수정 
		for(int i=0;i<targetIDs.length;i++){
			if(targetIDs[i]!=null) massMailDAO.insertMassMailTargetGroup(massMailInfo.getMassmailID(), Integer.parseInt(targetIDs[i]), exceptYNs[i]);
		}
		
		//3. 발송스케줄 일괄삭제후 입력 
		massMailDAO.deleteMassMailSchedule(massMailInfo.getMassmailID());
		//발송스케줄 입력
		int expectTargetCount = massMailDAO.expectTargetTotalCount(massMailInfo.getMassmailID());
		for(int i=0;i<massMailSchedule.length;i++){
			massMailSchedule[i].setTargetTotalCount(expectTargetCount);
			if(massMailSchedule[i]!=null)	massMailDAO.insertMassMailSchedule(massMailSchedule[i]);
		}
		
		//4. 필터링 업데이트 
		massMailDAO.updateMassMailFilterSet(massMailInfo);
		
		//5. 메일내용업데이트 
		massMailDAO.updateMassMailMail(massMailInfo);
	}
	
	
	/**
	 * <p>대량메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MassMailList> listMassMailList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap){
		 List<MassMailList> result = null;
		 try{
			 result = massMailDAO.listMassMailList(userInfo, currentPage, countPerPage, searchMap);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * <p>대량메일 Self 리스트 
	 * @param userAuth
	 * @param countPerPage
	 * @return
	 */
	public List<MassMailList> listMassMailSelfList(String[] userInfo, int countPerPage){
		 List<MassMailList> result = null;
		 try{
			 result = massMailDAO.listMassMailSelfList(userInfo, countPerPage);
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
	 */
	public int totalCountMassMailList(String[]userInfo, Map<String, String> searchMap){
		int result = 0;
		try{
			result = massMailDAO.totalCountMassMailList(userInfo, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}


	
	/**
	 * <p>유저별 등록된 테스트메일을 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, String>> listTesterEmail(String userID, String testerEmail) throws DataAccessException{
		List<Map<String, String>> resultList = null;		
		try{
			resultList =  massMailDAO.listTesterEmail(userID, testerEmail);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	
	/**
	 * <p>대량메일에 등록할 대상자리스트를 출력한다. 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List listTargeting(int currentPage, int countPerPage,Map searchMap, String[] userInfo){
		List resultList = null;		
		try{
			resultList =  massMailDAO.listTargeting(currentPage, countPerPage, searchMap, userInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
		
	}
	
	/**
	 * <p>대상자등록의 총 레코드수를 가져온다.
	 * @return
	 */
	@SuppressWarnings("unchecked")	
	public int getTargetingTotalCount(Map searchMap, String[] userInfo){
		int result = 0;
		try{
			result = massMailDAO.getTargetingTotalCount(searchMap, userInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>대량메일 수정 
	 * @param massmailID
	 * @return
	 */
	public int updateMassMailInfo(MassMailInfo massMailInfo){
		int result = 0;
		try{
			result = massMailDAO.updateMassMailInfo(massMailInfo);
			if(result > 0 && massMailInfo.getApproveYN().equals("Y")){
				massMailDAO.updateApproveDate(massMailInfo.getMassmailID());
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(int massmailID){
		List<TargetingGroup> result = null;
		try{
			result = massMailDAO.listTargetingGroup(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(String target_ids){
		List<TargetingGroup> result = null;
		try{
			result = massMailDAO.listTargetingGroup(target_ids);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>메일링크리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailLink> listMassMailLink(int massmailID){
		List<MassMailLink> result = null;
		try{
			result = massMailDAO.listMassMailLink(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대량메일 메일내용 수정
	 * @param massMailInfo
	 * @return
	 */
	public int updateMassMailMail(MassMailInfo massMailInfo){
		
		int result = 0;
		try{
			result = massMailDAO.updateMassMailMail(massMailInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>ez_config에서 해당되는 configValue값을 구해온다.
	 * @param configFlag
	 * @param configName
	 * @return
	 */
	public String getConfigValue(String configFlag, String configName){
		String result = "";
		try{ 
			result = massMailDAO.getConfigValue(configFlag,configName);  
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	
	/**
	 * <p>메일컨텐츠 업데이트(링크분석후)
	 * @param massmailID
	 * @return
	 */
	public int updateMailContent(int massmailID, String mailContent){
		return massMailDAO.updateMailContent(massmailID, mailContent);
	}
	
	
	/**
	 * <p>메일링크에서 선택된 메일링크 삭제 
	 * @param massmailID
	 * @param linkID
	 * @return
	 */
	public int deleteMailLinkByLinkID(int massmailID, String[] linkIDs){
		int result = 0;
		try{ 
			result = massMailDAO.deleteMailLinkByLinkID(massmailID,linkIDs);  
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	/**
	 * <p>메일링크 처리후 최종 상태값변경 
	 * @param massmailID
	 * @return
	 */
	public int updateAllScheduleState(int massmailID, String state){
		int result = 0;
		try{ 
			result = massMailDAO.updateAllScheduleState(massmailID,state);  
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	/**
	 * <p>스케줄삭제
	 * @param massmailID
	 * @return
	 */
	public int deleteMassMailSchedule(int massmailID){
		int result = 0;
		try{ 
			result = massMailDAO.deleteMassMailSchedule(massmailID);  
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	/**
	 * <p>메일링크정보삭제
	 * @param massmailID
	 * @return
	 */
	public int deleteMassMailLink(int massmailID){
		return massMailDAO.deleteMassMailLink(massmailID);
	}
	

	
	/**
	 * <p>DB에 쿼리정보를 가져온다. 
	 * @param targetID
	 * @return
	 */
	public Map<String, Object> getQeuryDB(int targetID){
		return massMailDAO.getQeuryDB(targetID);
	}
	
	/**
	 * <p>타겟ID에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public List<OnetooneTarget> listOnetooneTarget(int targetID){
		return massMailDAO.listOnetooneTarget(targetID);
	}
	
	/**
	 * <p>시스템메일에 입력한다.
	 * @param systemNotify
	 * @return
	 */
	public void insertSystemNotify(SystemNotify[] systemNotifys){
		for(SystemNotify systemNotify : systemNotifys){
			if(systemNotify!=null) massMailDAO.insertSystemNotify(systemNotify);
		}
	}
	
	
	
	/**
	 * <p>테스트 전송메일 리스트 
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
			result =  massMailDAO.listSystemNotify(notifyFlag, userID, currentPage, countPerPage, searchMap);
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
	public int totalCountNotify(String notifyFlag, String userID, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int result = 0;
		try{
			result =  massMailDAO.totalCountNotify(notifyFlag, userID, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>타겟ID에 해당되는 원투원 정보 
	 * @param targetIDs
	 * @return
	 */
	public List<OnetooneTarget> selectOnetooneByTargetID(String[] targetIDs){
		List<OnetooneTarget> result = null;
		try{
			result =  massMailDAO.selectOnetooneByTargetID(targetIDs);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>메일템플릿 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MailTemplate> listMailTemplate(String userID, String groupID, String userAuth, String templateType){
		List<MailTemplate> result = null;
		try{
			result =  massMailDAO.listMailTemplate(userID, groupID, userAuth, templateType);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>메일 템플릿 보기 
	 * @param templateID
	 * @return
	 */
	public MailTemplate viewMailTemplate(int templateID){
		MailTemplate mailTemplate = null;
		try{
			mailTemplate = massMailDAO.viewMailTemplate(templateID);
		}catch(Exception e){
			logger.error(e);
		}
		return mailTemplate;
	}
	

	/**
	 * <p>템플릿 적용시 사용카운트 증가 
	 * @param templateID
	 * @return
	 */
	public int updateUsedCountTemplate(int templateID){
		int result = 0;
		try{
			result = massMailDAO.updateUsedCountTemplate(templateID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>시스템메일삭제(즉, 테스트메일)
	 * @param notifyIDs
	 * @return
	 */
	public int deleteSystemNotify(String[] notifyIDs){
		int result = 0;
		try{
			result = massMailDAO.deleteSystemNotify(notifyIDs);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>테스트메일 삭제 
	 * @param userID
	 * @param testerEmails
	 * @return
	 */
	public int deleteTesterEmail(String userID, String[] testerEmails){
		int result = 0;
		try{
			result = massMailDAO.deleteTesterEmail(userID, testerEmails);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>첨부파일 리스트 
	 * @param userID
	 * @return
	 */
	public List<AttachedFile> listAttachedFile(String userID){
		List<AttachedFile> result = null;
		try{
			result = massMailDAO.listAttachedFile(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>첨부파일 삽입 
	 * @param attachedFile
	 * @return
	 */
	public int insertAttachedFile(AttachedFile attachedFile){
		int result = 0;
		try{
			result = massMailDAO.insertAttachedFile(attachedFile);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	

	/**
	 * <p>해당되는 파일 정보 가져오기 
	 * @param fileID
	 * @param fileKey
	 * @return
	 */
	public AttachedFile getAttachedFile(String fileKey){
		AttachedFile attachedFile = null;
		try{
			attachedFile = massMailDAO.getAttachedFile(fileKey);
		}catch(Exception e){
			logger.error(e);
		}
		return attachedFile;
	}
	
	/**
	 * <p>첨부파일 디비에서 삭제 
	 * @param fileKeys
	 * @return
	 */
	public int deleteAttachedFile(String[] fileKeys){
		int result = 0;
		try{
			result = massMailDAO.deleteAttachedFile(fileKeys);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>반복메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MassMailList> listMassMailRepeat(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap){
		List<MassMailList> result = null;
		try{
			result = massMailDAO.listMassMailRepeat(userInfo, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
	}
	
	/**
	 * <p>반복메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */

	public int totalCountMassMailRepeat(String[] userInfo, Map<String, String> searchMap){
		int result = 0;
		try{
			result = massMailDAO.totalCountMassMailRepeat(userInfo, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
	}
	
	/**
	 * <p>반복메일 스케줄리스트 
	 * @param massmailID
	 * @return
	 */
	public  List<MassMailList> listRepeatSchedule(int massmailID, int currentPage, int countPerPage, Map<String, String> searchMap){
		List<MassMailList> result = null;
		try{
			result = massMailDAO.listRepeatSchedule(massmailID, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
	}
	
	/**
	 * <p>반복메일스케즐카운트 
	 * @param massmailID
	 * @param searchMap
	 * @return
	 */
	public int totalCountRepeatSchedule(int massmailID, Map<String, String> searchMap){
		int result= 0;
		try{
			result = massMailDAO.totalCountRepeatSchedule(massmailID, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;	
	}
	
	/**
	 * <p>반복메일 정보보기 
	 * @param massmailID
	 * @return
	 */
	public MassMailInfo viewRepeatMassmailInfo(int massmailID){
		MassMailInfo massMailInfo= null;
		try{
			massMailInfo = massMailDAO.viewRepeatMassmailInfo(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return massMailInfo;	
	}
	
	/**
	 * <p>체크된 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 */
	public int deleteRepeatScheduleByChecked(int massmailID, String[] scheduleIDs){
		int result1=0,result2=0;
		try{
			result1 = massMailDAO.deleteRepeatScheduleByChecked(massmailID, scheduleIDs);
		}catch(Exception e){
			logger.error(e);
		}
		try{
			result2 = massMailDAO.updateRepeatSendEndDate(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		
		
		//massmailID에 해당되는 스케줄이 없다면 기본정보도 삭제한다.
		if(massMailDAO.checkMassmailSchedule(massmailID)==0){
			massMailDAO.deleteMassMailInfo(massmailID);
		}
		
		return result1*result2;
			
	}
	
	
	/**
	 * <p>기간내 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 */
	public int deleteRepeatScheduleByDate(int massmailID, String fromDate, String toDate){
		int result1=0,result2=0;
		try{
			result1 = massMailDAO.deleteRepeatScheduleByDate(massmailID, fromDate,toDate);
		}catch(Exception e){
			logger.error(e);
		}
		try{
			result2 = massMailDAO.updateRepeatSendEndDate(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		
		
		//massmailID에 해당되는 스케줄이 없다면 기본정보도 삭제한다.
		if(massMailDAO.checkMassmailSchedule(massmailID)==0){
			massMailDAO.deleteMassMailInfo(massmailID);
		}
		
		return result1*result2;
	}
	
	/**
	 * <p>사용자아이디에 해당되는 보내는 사람리스트 
	 * @param userID
	 * @return
	 */
	public List<Sender> selectSenderByUserID(String groupID, String userID, String defaultYN){
		List<Sender> result = null;
		try{
			result = massMailDAO.selectSenderByUserID(groupID, userID, defaultYN);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대량메일 리스트 상태값 확인 
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 */
	public MassMailList getMassmailState(String massmail_id, String schedule_id){
		MassMailList result = null;
		try{
			result = massMailDAO.getMassmailState(massmail_id, schedule_id);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	/**
	 * <p>사용자를 정보를 받아온다 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public User getUserInfo(String userID){
		User result = null;
		try{
			result = massMailDAO.getUserInfo(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
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
			result = massMailDAO.getBackupDate();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>발송 상태 변경
	 * @param MassMailGroup
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSendState(int massmailID, int scheduleID, String state){
		int result = -1;
		try{
			result = massMailDAO.updateSendState(massmailID, scheduleID, state);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	public List<FilterManager> getFilter(){
		List<FilterManager> result = null;
		try{
			result = massMailDAO.getFilter();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>이미지 파일 리스트 
	 * @param userID
	 * @return
	 */
	public List<ImageFile> listImageFile(String userID){
		List<ImageFile> result = null;
		try{
			result = massMailDAO.listImageFile(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>이미지 파일 업로드 
	 * @param imageFile
	 * @return
	 */
	public int insertImageFile(ImageFile imageFile){
		int result = 0;
		try{
			result = massMailDAO.insertImageFile(imageFile);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>이미지파일 디비에서 삭제 
	 * @param fileKeys
	 * @return
	 */
	public int deleteImageFile(String[] fileKeys){
		int result = 0;
		try{
			result = massMailDAO.deleteImageFile(fileKeys);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 동일한 이름의 이미지파일이 있는지 확인	
	 * @param fileName
	 * @param userID
	 * @return
	 */
	public List<ImageFile> isExistImageFile(String fileName, String userID)	{
		List<ImageFile> result = null;
		try{
			result = massMailDAO.isExistImageFile(fileName, userID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>발송할 대량메일 리스트 - 우선순위 관리창에서 사용 
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailPriority> getMassMailList() {
		List<MassMailPriority> result = null;
		try{
			result = massMailDAO.getMassMailList();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>우선순위 저장 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updatePriority(Map<String, Object>[] maps) {
		int[] result = null;
		try{
			result = massMailDAO.updatePriority(maps);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	
	/**
	 * <p>발송결과리스트 - 상태 값 변경시 비교할 현재 DB 상태 값 추출
	 * @param massmailID
	 * @param schdeduleID
	 * @return
	 * @throws DataAccessException
	 */
	
	public String getMassMailState(int massmailID, int scheduleID){
		String result = "";
		
		try {
			result = massMailDAO.getMassMailState(massmailID, scheduleID);
		} catch (DataAccessException e) {
			logger.error(e);
		}
		
		return result;
		
	}
	
	
	/**
	 * <p>수신거부 링크 리스트 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailLink> listMassMailRejectLink(int massmailID){
		List<MassMailLink> result = null;
		try{
			result = massMailDAO.listMassMailRejectLink(massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
}
