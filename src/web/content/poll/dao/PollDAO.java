package web.content.poll.dao;

import java.util.Map;
import java.util.List;
import web.content.poll.model.*;

import org.springframework.dao.DataAccessException;

public interface PollDAO {

	
	/**
	 * <p>설문리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollInfo> selectPollInfoList(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
		
	
	/**
	 * <p>설문리스트 총카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollCount(Map<String, String> searchMap) throws DataAccessException;
		
	
	/**
	 * <p>설문기본정보 인서트 
	 * @param pollInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollInfo(PollInfo pollInfo , String type) throws DataAccessException;
	
	
	/**
	 * <p>설문 기본정보수정 
	 * @param pollInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollInfo(PollInfo pollInfo) throws DataAccessException;
	
	
	
	/**
	 * <p>설문문항입력
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollQuestion(PollQuestion pollQuestion) throws DataAccessException;
	
	
	
	
	/**
	 * <p>설문문항 수정
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollQuestion(PollQuestion pollQuestion) throws DataAccessException;
	
	/**
	 * <p>코드값을 불러온다.
	 * @param codeID
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollCode> selectPollCode(String codeID) throws DataAccessException;
	
	
	
	/**
	 * <p>설문기본정보 보기 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public PollInfo viewPollInfo(int pollID) throws DataAccessException;
	
	

	
	
	/**
	 * <p>최근 입력된 설문아이디 가져오기 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxPollID() throws DataAccessException;
	
	
	
	/**
	 * <p>코드타입별로 가져오기 
	 * @param codeType
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollCode> selectPollCodeType(String codeType) throws DataAccessException;
	
	
	
	/**
	 * <p>최근 문항입력아이디 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxQuestionID(int pollID) throws DataAccessException;
	
	
	/**
	 * <p>문항보기 입력
	 * @param pollAnswer
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollExample(PollExample pollExample) throws DataAccessException;
	
	
	
	/**
	 * <p>설문템플릿 입력 
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollTemplate(PollTemplate pollTemplate) throws DataAccessException;
	
	
	/**
	 * <p>설문템플릿 수정
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollTemplate(PollTemplate pollTemplate) throws DataAccessException;
	
	
	
	/**
	 * <p>설문템플릿 수정
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollTemplate(int pollTemplateID) throws DataAccessException;
	
	
	
	/**
	 * <p>설문템플릿리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollTemplate> selectPollTemplate(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>설문템플릿 총카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollTemplateCount(Map<String, String> searchMap) throws DataAccessException;
	
	
	
	
	/**
	 * <p>설문템플릿 보기 
	 * @param pollTemplateID
	 * @return
	 * @throws DataAccessException
	 */
	public PollTemplate viewPollTemplate(int pollTemplateID) throws DataAccessException;
	
	
	
	/**
	 * <p>설문템플릿리스트 사용자별 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollTemplate> selectPollTemplateByUsers(Map<String, String> searchMap) throws DataAccessException;
		
	
	
	
	/**
	 * <p>설문문항보기
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public PollQuestion viewPollQuestion(int pollID, int questionID) throws DataAccessException;
	
	
	
	/**
	 * <p>현재 설문문항의 질문번호를 가져온다. 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, Integer>> getPollQuestionNumber(int pollID) throws DataAccessException;
	
	
	
	/**
	 * <p>설문HTML 업데이트 
	 * @param pollID
	 * @param resultPollHTML
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDefaultResultPollHTML(int pollID, String defaultPollHTML, String resultPollHTML) throws DataAccessException;
	
	
	/**
	 * <p>설문HTML 최종 업데이트 
	 * @param pollID
	 * @param resultPollHTML
	 * @return
	 * @throws DataAccessException
	 */
	public int updateResultPollHTMLToFinish(int pollID, String resultPollHTML) throws DataAccessException;
	
	

	
	/**
	 * <p>설문 초기화 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateResultPollHTMLToDefault(int pollID) throws DataAccessException;
	
	
	/**
	 * <p>시작 문구 및 종료 문구 수정 
	 * @param gubun
	 * @param pollID
	 * @param title
	 * @return
	 * @throws DataAccessException
	 */
	public int updateStartEndTitle(String columnName, int pollID, String title) throws DataAccessException;
	
	
	
	/**
	 * <p>시작/종료 문구 보기 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public PollInfo viewStartEndTitle(int pollID) throws DataAccessException;
	 
	
	
	/**
	 * <p>템플릿 HTML을 가져온다. 
	 * @param pollTemplateID
	 * @return
	 * @throws DataAccessException
	 */
	public String showTemplateHTML(int pollTemplateID) throws DataAccessException; 
	
	
	
	/**
	 * <p>설문HTML을 불러온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public  Map<String, Object> showResultDefaultPollHTML(int pollID) throws DataAccessException; 
	
	
	
	
	/**
	 * <p>현재 설문이 사용한 적이 있는 지 확인 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPoll(int pollID) throws DataAccessException; 
	
	
	
	/**
	 * <p>설문보기 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollExample(int pollID) throws DataAccessException; 
	
	
	/**
	 * <p>전체 설문질문 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollQuestionAll(int pollID) throws DataAccessException; 
	
	
	
	/**
	 * <p>설문문항 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollQuestion(int pollID, int questionID) throws DataAccessException; 
	

	
	/**
	 * <p>설문정보 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollInfo(int pollID) throws DataAccessException;
	
	
	
	
	/**
	 * <p>응답보기 
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollExample> selectPollExample(int pollID, int questionID, String questionType, String matrixXY) throws DataAccessException;
	

	
	
	/**
	 * <p>설문 문항에 대한 보기 삭제 
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteExampleQuestion(int pollID, int questionID) throws DataAccessException;
	
	
	
	/**
	 * <p>설문번호삭제후 해당번호 이후 일괄업데이트 
	 * @param pollId
	 * @param questionNo
	 * @return
	 * @throws DataAccessException
	 */
	public int  updateQuestionNumAfterDelete(int pollID, int questionNo) throws DataAccessException;
	
	
	/**
	 * <p>#설문번호이동후 나머지 일괄 +1처리 
	 * @param pollId
	 * @param questionNo
	 * @return
	 * @throws DataAccessException
	 */
	public int  updateQuestionNumAfterAdd(int pollID, int questionNo) throws DataAccessException;
	
	
	/**
	 * <p>설문문항 갯수 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int getQuestionCount(int pollID) throws DataAccessException;
	
	
	
	/**
	 * <p>questionID에 해당되는 questionNo를 가져온다.
	 * @param questionNo
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> getQuestionIDByNo(int pollID, int questionNo)  throws DataAccessException;
	
	
	
	/**
	 * <p>설문번호 변경 
	 * @param chgGuestionNo
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateQuestionNo(int questionNo, int pollID, int questionID) throws DataAccessException;
	
	
	/**
	 * <p>해당 설문번호 이후에 설문아이디를 가져온다.
	 * @param pollID
	 * @param questionNo
	 * @return
	 */
	public List<Map<String,Integer>> selectQuestionNo(int pollID, int questionNoFrom, int questionNoTo);
	
	
	/**
	 * <p>설문리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<PollInfo> selectPollList(Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>해당 pollID와 massmailID가 있는지 확인 
	 * @param pollID
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollMassMail(int pollID, int massmailID) throws DataAccessException;
	
	
	
	/**
	 * <p>설문문항 리스트 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String,Integer>> selQuestion(int pollID) throws DataAccessException;

	
	
	/**
	 * <p>설문문항보기 리스트 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String,Object>> selExample(int pollID, int questionID, String matrixXY) throws DataAccessException;
	
	
	
	/**
	 * <p>설문응답넣기
	 * @param pollAnswer
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertPollAnswer(List<PollAnswer> pollAnswerList) throws DataAccessException;
	
	
	
	/**
	 * <p>설문응답체크
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param sendID
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollAnswer(int pollID, int massmailID, int scheduleID, int sendID, String email) throws DataAccessException;
	
	

	/**
	 * <p>해당 sendID와 이메일이 존재하는지 체크 
	 * @param sendID
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public int checkExistMember(int massmailID, int scheduleID, int sendID, String email) throws DataAccessException;
	
	
	/**
	 * <p>통계수집만료기간을 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkStatisticExpiredMassmail(int massmailID, int scheduleID, String nowDate) throws DataAccessException;
	
	
	
	/**
	 * <p>설문이 사용중인지 체크한다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkUsingPoll(int pollID) throws DataAccessException;
	
	
	
	/**
	 * <p>설문코드리스트 를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<PollCode> selectPollCodeList(Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>설문코드 입력 
	 * @param pollCode
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertPollCode(List<PollCode> pollCodes) throws DataAccessException;
	
	
	/**
	 * <p>설문코드 삭제 
	 * @param pollCode
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollCode(String codeID) throws DataAccessException;
	
	
	
	/**
	 * <p>설문코드 최대값 구하기 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxPollCodeID() throws DataAccessException;
	
	
	
	/**
	 * <p>설문문항을 카피합니다. 
	 * @param oldPollID
	 * @param newPollID
	 * @return
	 * @throws DataAccessException
	 */
	public int insertCopyPollQuestion(int oldPollID, int newPollID)  throws DataAccessException;
	
	
	/**
	 * <p>설문문항을 카피합니다. 
	 * @param oldPollID
	 * @param newPollID
	 * @return
	 * @throws DataAccessException
	 */
	public int insertCopyPollExample(int oldPollID, int newPollID)  throws DataAccessException;
	
	
	/**
	 * <p>설문을 카피합니다. 
	 * @param oldPollID
	 * @param newPollID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateCopyPollInfo(int oldPollID, int newPollID, String type)  throws DataAccessException;
	
	
	
	/**
	 * <p>일반 설문응답삭제 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param sendID
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollAnswer(int pollID, int massmailID, int scheduleID, int sendID, String email) throws DataAccessException;
	

	
	
	/**
	 * <p>설문 통계수집만료기간을 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollEndDate(int pollID, String nowDate) throws DataAccessException;

	
	
	/**
	 * <p>설문 통계수집만료 목표응답 수를 가져온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollAimAnswerCnt(int pollID,int aimAnswerCnt) throws DataAccessException;
	
	
	/**
	 * <p>해당 대량메일의 설문에 응답한 인원 수 출력.
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollResponseCnt(int pollID, int massmailID, int scheduleID);
	

	/**
	 * <p>설문종료조건을 변경한다.(마감일, 목표응답 수)
	 * @param pollID
	 * @param nowDate
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollEndType(int pollID, String pollEndType, String pollEndDate, String aimAnswerCnt ) throws DataAccessException;
	
	
	/**
	 * <p>문항 불러오기 - 문항 정보를 받아온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollQuestion> copyQuestionList(Map<String, Object> searchMap)throws DataAccessException;
	

	/**
	 * <p>객관식 (단일선택) 문항 리스트
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollQuestion> selSingularQuestion(int pollID)throws DataAccessException;
	

	/**
	 * <p>설문문항 스킵패턴 설정
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollSkipPattern(PollExample pollExample) throws DataAccessException;
	
	/**
	 * <p>각 계정의 groupID 검색( 템플릿 사용 권한 비교 )
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public String selectGroupID(String userID) throws DataAccessException;
}
