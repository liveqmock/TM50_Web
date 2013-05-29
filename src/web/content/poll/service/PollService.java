package web.content.poll.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.content.poll.model.PollAnswer;
import web.content.poll.model.PollAnswerMatrix;
import web.content.poll.model.PollExample;
import web.content.poll.model.PollCode;
import web.content.poll.model.PollInfo;
import web.content.poll.model.PollQuestion;
import web.content.poll.model.PollTemplate;

public interface PollService {

	
	/**
	 * <p>설문리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollInfo> selectPollInfoList(int currentPage, int countPerPage,Map<String, String> searchMap);
	
	
	
	/**
	 * <p>설문리스트 총카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollCount(Map<String, String> searchMap);
	
	
	/**
	 * <p>설문기본정보 인서트 
	 * @param pollInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollInfo(PollInfo pollInfo , String type);
	
	
	/**
	 * <p>설문 기본정보수정 
	 * @param pollInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollInfo(PollInfo pollInfo);
	
		
	
	/**
	 * <p>설문정보 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollInfo(int pollID);
	
	
	/**
	 * <p>설문문항입력
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollQuestion(PollQuestion pollQuestion);
	
	
	
	/**
	 * <p>설문문항 수정
	 * @param pollQuestion
	 * @return
	 */
	public int updatePollQuestion(PollQuestion pollQuestion);
	
	
	/**
	 * <p>코드값을 불러온다.
	 * @param codeID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<PollCode> selectPollCode(String codeID);
	
	/**
	 * <p>설문기본정보 보기 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public PollInfo viewPollInfo(int pollID);
	
	/**
	 * <p>최근 입력된 설문아이디 가져오기 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxPollID();
	
	
	/**
	 * <p>코드타입별로 가져오기 
	 * @param codeType
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollCode> selectPollCodeType(String codeType);
	
	
	/**
	 * <p>최근 문항입력아이디 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxQuestionID(int pollID);
	
	
	/**
	 * <p>문항보기 입력
	 * @param pollAnswer
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollExample(PollExample pollExample);
	
	
	/**
	 * <p>설문템플릿 입력 
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollTemplate(PollTemplate pollTemplate);
	
	
	/**
	 * <p>설문템플릿 수정
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollTemplate(PollTemplate pollTemplate);
	
	
	
	/**
	 * <p>설문템플릿 삭제
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollTemplate(String[] pollTemplateIDs);
	
	
	/**
	 * <p>설문템플릿리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<PollTemplate> selectPollTemplate(int currentPage, int countPerPage,Map<String, String> searchMap);
	
		
	
	/**
	 * <p>설문템플릿 총카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollTemplateCount(Map<String, String> searchMap);
	
	
	/**
	 * <p>설문템플릿 보기 
	 * @param pollTemplateID
	 * @return
	 * @throws DataAccessException
	 */
	public PollTemplate viewPollTemplate(int pollTemplateID);
	
	
	/**
	 * <p>설문템플릿리스트 사용자별 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollTemplate> selectPollTemplateByUsers(Map<String, String> searchMap);
	
	
	
	/**
	 * <p>설문문항보기
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public PollQuestion viewPollQuestion(int pollID, int questionID);
	
	
	/**
	 * <p>현재 설문문항의 질문번호를 가져온다. 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Map<String, Integer>> getPollQuestionNumber(int pollID);
	
	
	
	/**
	 * <p>설문HTML 업데이트 
	 * @param pollID
	 * @param resultPollHTML
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDefaultResultPollHTML(int pollID, String defaultPollHTML, String resultPollHTML);
	
	
	
	/**
	 * <p>설문 초기화 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateResultPollHTMLToDefault(int pollID);
	
	
	/**
	 * <p>설문HTML 최종 업데이트 
	 * @param pollID
	 * @param resultPollHTML
	 * @return
	 * @throws DataAccessException
	 */
	public int updateResultPollHTMLToFinish(int pollID, String resultPollHTML);
	
	
	/**
	 * <p>시작 문구 및 종료 문구 수정 
	 * @param gubun
	 * @param pollID
	 * @param title
	 * @return
	 * @throws DataAccessException
	 */
	public int updateStartEndTitle(String columnName, int pollID, String title);
	
	
	
	/**
	 * <p>시작/종료 문구 보기 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public PollInfo viewStartEndTitle(int pollID);
	
	/**
	 * <p>템플릿 HTML을 가져온다. 
	 * @param pollTemplateID
	 * @return
	 * @throws DataAccessException
	 */
	public String showTemplateHTML(int pollTemplateID);
	
	
	/**
	 * <p>설문HTML을 불러온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> showResultDefaultPollHTML(int pollID);

	
	/**
	 * <p>현재 설문이 사용한 적이 있는 지 확인 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPoll(int pollID);
	
	
	
	/**
	 * <p>설문삭제 
	 * @param pollID
	 * @return
	 */
	public int deletePoll(String[] pollIDs);
	
	
	/**
	 * <p>응답보기 
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollExample> selectPollExample(int pollID, int questionID, String questionType, String matrixXY);
	
	
	
	/**
	 * <p>설문 문항-보기 수정 
	 * @param pollQuestion
	 * @return
	 */
	public int updatePollQuestionExample(PollQuestion pollQuestion, PollExample[] pollExamples, String resultPollHTML);
	
	
	
	/**
	 * <p>설문 문항-보기 수정 
	 * @param pollQuestion
	 * @return
	 */
	public int updatePollQuestionExampleMatrix(PollQuestion pollQuestion, PollExample[] pollExamplesMatrixX, PollExample[] pollExamplesMatrixY, String resultPollHTML);
	
	
	
	/**
	 * <p>설문문항 삭제 
	 * @param pollQuestion
	 * @param pollExamples
	 * @param resultPollHTML
	 * @return
	 */
	public int deletePollQuestionExample(PollQuestion pollQuestion, String resultPollHTML);
	
	
	
	/**
	 * <p>설문문항 갯수 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int getQuestionCount(int pollID);
	
	
	/**
	 * <p>questionID에 해당되는 questionNo를 가져온다.
	 * @param questionNo
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object>  getQuestionIDByNo(int pollID, int questionNo);

	
	
	/**
	 * <p>설문번호 및 html 변경 
	 * @param pollID
	 * @param oldQuestionID
	 * @param oldQuestionNo
	 * @param newQuestionID
	 * @param newQuestionNo
	 * @param resultHTML
	 * @return
	 */
	public int updateChangeQuestionNoHTML(int pollID, int oldQuestionID, int oldQuestionNo, int newQuestionID, int newQuestionNo, String resultPollHTML);
	
	
	

	/**
	 * <p>설문번호 뒤로 이동 
	 * @param pollID
	 * @param oldQuestionID
	 * @param oldQuestionNo
	 * @param newQuestionID
	 * @param newQuestionNo
	 * @return
	 */
	public int updateMoveNextQuestionNoHTML(int addminusNum, int allowQuestionNo, List<Map<String,Integer>> questionIDList,  int pollID, int oldQuestionID, int oldQuestionNo, int newQuestionID, int newQuestionNo, String resultPollHTML);
	

	/**
	 * <p>주어진 설문번호 이후에 설문아이디를 가져온다.
	 * @param pollID
	 * @param questionNo
	 * @return
	 */
	public List<Map<String,Integer>> selectQuestionNo(int pollID, int questionNoFrom, int questionNoTo);
	
	
	/**
	 * <p>설문번호 변경 
	 * @param chgGuestionNo
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateQuestionNo(int questionNo, int pollID,  int questionID);
	
	
	
	/**
	 * <p>설문리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<PollInfo> selectPollList(Map<String, String> searchMap);
	
	
	/**
	 * <p>해당 pollID와 massmailID가 있는지 확인 
	 * @param pollID
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollMassMail(int pollID, int massmailID);
	
	
	
	/**
	 * <p>설문문항 리스트 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String,Integer>> selQuestion(int pollID);

	
	
	/**
	 * <p>설문문항보기 리스트 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String,Object>> selExample(int pollID, int questionID, String matrixXY);
	
	
	
	/**
	 * <p>설문응답넣기
	 * @param pollAnswer
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertPollAnswer(List<PollAnswer> pollAnswerList);
	
	
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
	public int checkPollAnswer(int pollID, int massmailID, int scheduleID, int sendID, String email);
	
	
	
	/**
	 * <p>해당 sendID와 이메일이 존재하는지 체크 
	 * @param sendID
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public int checkExistMember(int massmailID, int scheduleID, int sendID, String email);
	
	
	/**
	 * <p>통계수집만료기간을 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkStatisticExpiredMassmail(int massmailID, int scheduleID, String nowDate);
	
	/**
	 * <p>설문이 사용중인지 체크한다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkUsingPoll(int pollID);
	
	
	/**
	 * <p>설문코드리스트 를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<PollCode> selectPollCodeList(Map<String, String> searchMap);
	
	
	/**
	 * <p>설문코드 최대값 구하기 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxPollCodeID();
	
	
	/**
	 * <p>설문코드 입력 
	 * @param pollCode
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertPollCode(List<PollCode> pollCodes);
	
	
	
	
	/**
	 * <p>설문코드변경
	 * @param codeID
	 * @param pollCodes
	 * @return
	 */
	public int[] updatePollCode(String codeID, List<PollCode> pollCodes);
	
	
	
	/**
	 * <p>설문코드 삭제 
	 * @param pollCode
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollCode(String codeID);
		
	/**
	 * <p>설문을 카피합니다. 
	 * @param oldPollID
	 * @param newPollID
	 * @return
	 * @throws DataAccessException
	 */
	public int insertCopyPoll(int oldPollID, int newPollID, String type);
	
	
		
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
	public int deletePollAnswer(int pollID, int massmailID, int scheduleID, int sendID, String email);
	
	
		
	/**
	 * <p>설문 통계수집만료기간을 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollEndDate(int pollID, String nowDate);
	
	
	/**
	 * <p>설문 통계수집만료 목표응답 수를 가져온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollAimAnswerCnt(int pollID, int massmailID, int scheduleID);
	
	
	/**
	 * <p>설문마감일을 변경한다.
	 * @param pollID
	 * @param nowDate
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollEndType(int pollID, String pollEndType, String pollEndDate, String aimAnswerCnt );
	
	/**
	 * <p>문항 불러오기 - 문항 정보를 받아온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollQuestion> copyQuestionList(Map<String, Object> searchMap);
	

	/**
	 * <p>객관식 (단일선택) 문항 리스트
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollQuestion> selSingularQuestion(int pollID);
	

	/**
	 * <p>설문문항 스킵패턴 설정
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollSkipPattern(PollExample[] pollExamples);
	
	/**
	 * <p>각 계정의 groupID 검색( 템플릿 사용 권한 비교 )
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public String selectGroupID(String userID);
}
