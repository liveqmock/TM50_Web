package web.content.poll.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import web.content.poll.dao.*;
import web.content.poll.model.PollAnswer;
import web.content.poll.model.PollAnswerMatrix;
import web.content.poll.model.PollExample;
import web.content.poll.model.PollCode;
import web.content.poll.model.PollInfo;
import web.content.poll.model.PollQuestion;
import web.content.poll.model.PollTemplate;

public class PollServiceImpl implements PollService{

	private Logger logger = Logger.getLogger("TM");
	private PollDAO pollDAO = null;
	
	public void setPollDAO(PollDAO pollDAO){
		this.pollDAO = pollDAO;
	}
	
	/**
	 * <p>설문리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollInfo> selectPollInfoList(int currentPage, int countPerPage,Map<String, String> searchMap){
		 List<PollInfo> result = null;
		 try{
			 result = pollDAO.selectPollInfoList(currentPage, countPerPage, searchMap);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * <p>설문리스트 총카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollCount(Map<String, String> searchMap){
		int result = 0;
		try{
			result = pollDAO.selectPollCount(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문기본정보 인서트 
	 * @param pollInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollInfo(PollInfo pollInfo , String type){
		int result = 0;
		try{
			result = pollDAO.insertPollInfo(pollInfo , type);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>설문 기본정보수정 
	 * @param pollInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollInfo(PollInfo pollInfo){
		int result = 0;
		try{
			result = pollDAO.updatePollInfo(pollInfo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문정보 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollInfo(int pollID){
		int result = 0;
		try{
			result = pollDAO.deletePollInfo(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문문항입력
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollQuestion(PollQuestion pollQuestion){
		int result = 0;
		try{
			result = pollDAO.insertPollQuestion(pollQuestion);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문문항 수정
	 * @param pollQuestion
	 * @return
	 */
	public int updatePollQuestion(PollQuestion pollQuestion){
		int result = 0;
		try{
			result = pollDAO.updatePollQuestion(pollQuestion);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>코드값을 불러온다.
	 * @param codeID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<PollCode> selectPollCode(String codeID){
		List<PollCode> result = null;
		try{
			result = pollDAO.selectPollCode(codeID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문기본정보 보기 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public PollInfo viewPollInfo(int pollID){
		PollInfo pollInfo = null;
		try{
			pollInfo = pollDAO.viewPollInfo(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return pollInfo;
	}
	
	/**
	 * <p>최근 입력된 설문아이디 가져오기 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxPollID(){
		int result = 0;
		try{
			result = pollDAO.getMaxPollID();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>코드타입별로 가져오기 
	 * @param codeType
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollCode> selectPollCodeType(String codeType){
		List<PollCode> result = null;
		try{
			result = pollDAO.selectPollCodeType(codeType);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>최근 문항입력아이디 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxQuestionID(int pollID){
		int result = 0;
		try{
			result = pollDAO.getMaxQuestionID(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>문항보기 입력
	 * @param pollAnswer
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollExample(PollExample pollExample){
		int result = 0;
		try{
			result = pollDAO.insertPollExample(pollExample);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문템플릿 입력 
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollTemplate(PollTemplate pollTemplate){
		int result = 0;
		try{
			result = pollDAO.insertPollTemplate(pollTemplate);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>설문템플릿 수정
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollTemplate(PollTemplate pollTemplate){
		int result = 0;
		try{
			result = pollDAO.updatePollTemplate(pollTemplate);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문템플릿 삭제
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollTemplate(String[] pollTemplateIDs){
		//result = pollDAO.deletePollTemplate(pollTemplateID);
		int result = 0;
		for(int i=0;i<pollTemplateIDs.length;i++){
			result = pollDAO.deletePollTemplate(Integer.parseInt(pollTemplateIDs[i]));
		}
		
		return result;
	}
	
	/**
	 * <p>설문템플릿리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<PollTemplate> selectPollTemplate(int currentPage, int countPerPage,Map<String, String> searchMap){
		List<PollTemplate> result = null;
		try{
			result = pollDAO.selectPollTemplate(currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문템플릿 총카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollTemplateCount(Map<String, String> searchMap){
		int result = 0;
		try{
			result = pollDAO.selectPollTemplateCount(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문템플릿 보기 
	 * @param pollTemplateID
	 * @return
	 * @throws DataAccessException
	 */
	public PollTemplate viewPollTemplate(int pollTemplateID) throws DataAccessException{
		PollTemplate result = null;
		try{
			result = pollDAO.viewPollTemplate(pollTemplateID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문템플릿리스트 사용자별 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollTemplate> selectPollTemplateByUsers(Map<String, String> searchMap) throws DataAccessException{
		List<PollTemplate> result = null;
		try{
			result = pollDAO.selectPollTemplateByUsers(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문문항보기
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public PollQuestion viewPollQuestion(int pollID, int questionID){
		PollQuestion pollQuestion = null;
		try{
			pollQuestion = pollDAO.viewPollQuestion(pollID, questionID);
		}catch(Exception e){
			logger.error(e);
		}
		return pollQuestion;
	}
	
	/**
	 * <p>현재 설문문항의 질문번호를 가져온다. 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Map<String, Integer>> getPollQuestionNumber(int pollID){
		List<Map<String, Integer>> result = null;
		try{
			result = pollDAO.getPollQuestionNumber(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문HTML 업데이트 
	 * @param pollID
	 * @param resultPollHTML
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDefaultResultPollHTML(int pollID, String defaultPollHTML, String resultPollHTML){
		int result = 0;
		try{
			result = pollDAO.updateDefaultResultPollHTML(pollID, defaultPollHTML, resultPollHTML);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	

	
	/**
	 * <p>설문 초기화 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateResultPollHTMLToDefault(int pollID){
		int result = 0;
		try{
			result = pollDAO.updateResultPollHTMLToDefault(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>설문HTML 최종 업데이트 
	 * @param pollID
	 * @param resultPollHTML
	 * @return
	 * @throws DataAccessException
	 */
	public int updateResultPollHTMLToFinish(int pollID, String resultPollHTML){
		int result = 0;
		try{
			result = pollDAO.updateResultPollHTMLToFinish(pollID, resultPollHTML);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>시작 문구 및 종료 문구 수정 
	 * @param gubun
	 * @param pollID
	 * @param title
	 * @return
	 * @throws DataAccessException
	 */
	public int updateStartEndTitle(String columnName, int pollID, String title){
		int result = 0;
		try{
			result = pollDAO.updateStartEndTitle(columnName, pollID, title);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>시작/종료 문구 보기 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public PollInfo viewStartEndTitle(int pollID){
		PollInfo pollInfo = null;
		try{
			pollInfo = pollDAO.viewStartEndTitle(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return pollInfo;
		
	}
	
	/**
	 * <p>템플릿 HTML을 가져온다. 
	 * @param pollTemplateID
	 * @return
	 * @throws DataAccessException
	 */
	public String showTemplateHTML(int pollTemplateID){
		String result = "";
		try{
			result = pollDAO.showTemplateHTML(pollTemplateID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문HTML을 불러온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> showResultDefaultPollHTML(int pollID){
		Map<String,Object> result = null;
		try{
			result = pollDAO.showResultDefaultPollHTML(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>현재 설문이 사용한 적이 있는 지 확인 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPoll(int pollID){
		int result = 0;
		try{
			result = pollDAO.checkPoll(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문삭제 
	 * @param pollID
	 * @return
	 */
	public int deletePoll(String[] pollIDs){
		//트랜젝션을 위해서 try~catch는 생략 함
		
		int result1=0,result2=0,result3=0;
		
		for(int i=0;i<pollIDs.length;i++){
			result1 = pollDAO.deletePollExample(Integer.parseInt(pollIDs[i]));
			result2 = pollDAO.deletePollQuestionAll(Integer.parseInt(pollIDs[i]));
			result3 = pollDAO.deletePollInfo(Integer.parseInt(pollIDs[i]));			
		}
	
		return result1*result2*result3;		
	}
	
	/**
	 * <p>응답보기 
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollExample> selectPollExample(int pollID, int questionID, String questionType, String matrixXY){
		List<PollExample> result = null;
		try{
			result = pollDAO.selectPollExample(pollID, questionID, questionType, matrixXY);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문 문항-보기 수정 
	 * @param pollQuestion
	 * @return
	 */
	public int updatePollQuestionExample(PollQuestion pollQuestion, PollExample[] pollExamples, String resultPollHTML){
		
		int result1=0,result2=0,result3=0, result4=0, result = 0;
		
		//1. 일단 기존의 보기 삭제 
		result1 = pollDAO.deleteExampleQuestion(pollQuestion.getPollID(), pollQuestion.getQuestionID());		
		
		//2. 새로운 보기 입력
		for(int i=0;i<pollExamples.length;i++){		
			result2 = pollDAO.insertPollExample(pollExamples[i]);
		}
		
		//3. 문항 수정 
		result3 = pollDAO.updatePollQuestion(pollQuestion);
		
		//4.HTML 업데이트		
		result4 = pollDAO.updateDefaultResultPollHTML(pollQuestion.getPollID(), resultPollHTML, resultPollHTML);
		
		if(result1<=0 || result2<=0 || result3<=0 || result4<=0){
			result = -1;
		}else{
			result = 1;
		}
		
		return result;
		
	}
	
	/**
	 * <p>설문 문항-보기 수정 
	 * @param pollQuestion
	 * @return
	 */
	public int updatePollQuestionExampleMatrix(PollQuestion pollQuestion, PollExample[] pollExamplesMatrixX, PollExample[] pollExamplesMatrixY, String resultPollHTML){
		int result1=0,result2=0,result3=0, result4=0, result5=0, result = 0;
		
		//1. 일단 기존의 보기 삭제 
		result1 = pollDAO.deleteExampleQuestion(pollQuestion.getPollID(), pollQuestion.getQuestionID());		
		
		//2. 새로운 가로보기 입력
		for(int i=0;i<pollExamplesMatrixX.length;i++){		
			result2 = pollDAO.insertPollExample(pollExamplesMatrixX[i]);
		}
		
		//2. 새로운 세로보기 입력
		for(int i=0;i<pollExamplesMatrixY.length;i++){		
			result3 = pollDAO.insertPollExample(pollExamplesMatrixY[i]);
		}

		
		//3. 문항 수정 
		result4 = pollDAO.updatePollQuestion(pollQuestion);
		
		//4.HTML 업데이트		
		result5 = pollDAO.updateDefaultResultPollHTML(pollQuestion.getPollID(), resultPollHTML, resultPollHTML);
		
		if(result1<=0 || result2<=0 || result3<=0 || result4<=0 || result5<=0){
			result = -1;
		}else{
			result = 1;
		}
		
		return result;
	}
	
	/**
	 * <p>설문문항 삭제 
	 * @param pollQuestion
	 * @param pollExamples
	 * @param resultPollHTML
	 * @return
	 */
	public int deletePollQuestionExample(PollQuestion pollQuestion, String resultPollHTML){
		int result1 = 0, result2 = 0, result3 = 0, result4 = 0;
		
		//보기삭제
		result1 = pollDAO.deleteExampleQuestion(pollQuestion.getPollID(), pollQuestion.getQuestionID());
			
		//문항삭제
		result2 = pollDAO.deletePollQuestion(pollQuestion.getPollID(), pollQuestion.getQuestionID());
			
		//삭제된 문항이후에 것들 번호 -1씩 처리 
		result3 = pollDAO.updateQuestionNumAfterDelete(pollQuestion.getPollID(), pollQuestion.getQuestionNo());
			
		//html업데이트
		result4 = pollDAO.updateDefaultResultPollHTML(pollQuestion.getPollID(), resultPollHTML, resultPollHTML);
			
		int result = 1;
		
		if(result1<0 || result2<0 || result3<0 || result4<0){
			result = -1;
		}
		return result;
	}
	
	/**
	 * <p>설문문항 갯수 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int getQuestionCount(int pollID){
		int result = 0;
		try{
			result = pollDAO.getQuestionCount(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>questionID에 해당되는 questionNo를 가져온다.
	 * @param questionNo
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> getQuestionIDByNo(int pollID, int questionNo){
		Map<String,Object> result = null;
		try{
			result = pollDAO.getQuestionIDByNo(pollID, questionNo);				
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	

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
	public int updateChangeQuestionNoHTML(int pollID, int oldQuestionID, int oldQuestionNo, int newQuestionID, int newQuestionNo, String resultPollHTML){
		
		int result1=0,result2=0,result3=0,result=0;
		
		try{
			//1. 일단 서로 번호 변경 
			result1 = pollDAO.updateQuestionNo(newQuestionNo, pollID, oldQuestionID);
			result2 = pollDAO.updateQuestionNo(oldQuestionNo, pollID, newQuestionID);
			result3 = pollDAO.updateDefaultResultPollHTML(pollID, resultPollHTML, resultPollHTML);
			result = 0;
			
			if(result1<0 || result2<0 || result3<0){
				result = -1;
			}
			
		}catch(Exception e){
			logger.error(e);
		}

		
		return result;
		
	}
	
	
	
	/**
	 * <p>설문번호 뒤로  이동 
	 * @param pollID
	 * @param oldQuestionID
	 * @param oldQuestionNo
	 * @param newQuestionID
	 * @param newQuestionNo
	 * @return
	 */
	public int updateMoveNextQuestionNoHTML(int addminusNum, int allowQuestionNo, List<Map<String,Integer>> questionIDList, int pollID, int oldQuestionID, int oldQuestionNo, int newQuestionID, int newQuestionNo, String resultPollHTML){
		int result1=0, result2=0, result3=0, result=0;
		
		try{			
			
			for(int i=0;i<questionIDList.size();i++){
				int questionID = questionIDList.get(i).get("questionID");
				int questionNo = questionIDList.get(i).get("questionNo");
				result1 = pollDAO.updateQuestionNo((questionNo+addminusNum), pollID, questionID);
			}			
			
			result2 = updateQuestionNo(allowQuestionNo, pollID, oldQuestionID);			
			result3 = pollDAO.updateDefaultResultPollHTML(pollID, resultPollHTML, resultPollHTML);
			result = 1;
			
			if(result1<0 || result2<0 ||result3<0 ){
				result = -1;
			}
			
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
	}
	

	/**
	 * <p>주어진 설문번호 이후에 설문아이디를 가져온다.
	 * @param pollID
	 * @param questionNo
	 * @return
	 */
	public List<Map<String,Integer>> selectQuestionNo(int pollID, int questionNoFrom, int questionNoTo){
		List<Map<String,Integer>> result = null;
		try{
			result = pollDAO.selectQuestionNo(pollID, questionNoFrom, questionNoTo);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문번호 변경 
	 * @param chgGuestionNo
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateQuestionNo(int questionNo, int pollID, int questionID) throws DataAccessException{
		int result = 0;
		try{
			result = pollDAO.updateQuestionNo(questionNo, pollID, questionID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<PollInfo> selectPollList(Map<String, String> searchMap){
		 List<PollInfo> result = null;
		 try{
			 result = pollDAO.selectPollList(searchMap);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * <p>해당 pollID와 massmailID가 있는지 확인 
	 * @param pollID
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollMassMail(int pollID, int massmailID){
		int result = 0;
		try{
			result = pollDAO.checkPollMassMail(pollID, massmailID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>설문문항 리스트 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String,Integer>> selQuestion(int pollID){
		List<Map<String,Integer>> result = null;
		try{
			result = pollDAO.selQuestion(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	
	/**
	 * <p>설문문항보기 리스트 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String,Object>> selExample(int pollID, int questionID, String matrixXY){
		List<Map<String,Object>> result = null;
		try{
			result = pollDAO.selExample(pollID,questionID, matrixXY);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>설문응답넣기
	 * @param pollAnswer
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertPollAnswer(List<PollAnswer> pollAnswerList){
		int[] result = null;
		try{
			result = pollDAO.insertPollAnswer(pollAnswerList);
		}catch(DataIntegrityViolationException e){
			logger.error(e);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
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
	public int checkPollAnswer(int pollID, int massmailID, int scheduleID, int sendID, String email){
		int result = 0;
		try{
			result = pollDAO.checkPollAnswer(pollID, massmailID, scheduleID, sendID, email);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	
	/**
	 * <p>해당 sendID와 이메일이 존재하는지 체크 
	 * @param sendID
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public int checkExistMember(int massmailID, int scheduleID, int sendID, String email){
		int result = 0;
		try{
			result = pollDAO.checkExistMember(massmailID, scheduleID, sendID, email);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>통계수집만료기간을 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkStatisticExpiredMassmail(int massmailID, int scheduleID, String nowDate){
		int result = 0;
		try{
			result = pollDAO.checkStatisticExpiredMassmail(massmailID, scheduleID, nowDate);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문이 사용중인지 체크한다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkUsingPoll(int pollID){
		int result = 0;
		try{
			result = pollDAO.checkUsingPoll(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문코드리스트 를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<PollCode> selectPollCodeList(Map<String, String> searchMap){
		List<PollCode> result = null;
		try{
			result = pollDAO.selectPollCodeList(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문코드 최대값 구하기 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxPollCodeID(){
		int result = 0;
		try{
			result = pollDAO.getMaxPollCodeID();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문코드 입력 
	 * @param pollCode
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertPollCode(List<PollCode> pollCodes){
		return pollDAO.insertPollCode(pollCodes);
	}
	
	/**
	 * <p>설문코드변경
	 * @param codeID
	 * @param pollCodes
	 * @return
	 */
	public int[] updatePollCode(String codeID, List<PollCode> pollCodes){
		int[] result = null;
		
		//삭제후 새로 입력 
		if(pollDAO.deletePollCode(codeID)>0){
			result = pollDAO.insertPollCode(pollCodes);
		}
		return result;		
	}
	
	
	/**
	 * <p>설문코드 삭제 
	 * @param pollCode
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollCode(String codeID){
		int result = 0;
		try{
			result = pollDAO.deletePollCode(codeID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문을 카피합니다. 
	 * @param oldPollID
	 * @param newPollID
	 * @return
	 * @throws DataAccessException
	 */
	public int insertCopyPoll(int oldPollID, int newPollID, String type){
		
		int result = 1;
		int result1=1, result2=1, result3=1;
		System.out.println("oldPollID : "+oldPollID+" newPollID : "+newPollID);
		result1 = pollDAO.updateCopyPollInfo(oldPollID, newPollID, type);
		result2 = pollDAO.insertCopyPollQuestion(oldPollID, newPollID);
		result3 = pollDAO.insertCopyPollExample(oldPollID, newPollID);	
		if(result1<1 || result2<1 || result3<1){
			result = -1;
		}

		return result;
		
	}

	
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
	public int deletePollAnswer(int pollID, int massmailID, int scheduleID, int sendID, String email){
		int result = 0;
		try{
			result = pollDAO.deletePollAnswer(pollID, massmailID, scheduleID, sendID, email);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>설문 통계수집만료기간을 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollEndDate(int pollID, String nowDate){
		int result = 0;
		try{
			result = pollDAO.checkPollEndDate(pollID, nowDate);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문 통계수집만료 목표응답 수를 가져온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollAimAnswerCnt(int pollID, int massmailID, int scheduleID){
		int result = 0;
		int aimAnswerCnt = 0;
		try{
			
			aimAnswerCnt = pollDAO.selectPollResponseCnt(pollID, massmailID, scheduleID);
			System.out.println("aimAnswerCnt : "+aimAnswerCnt);
			result = pollDAO.checkPollAimAnswerCnt(pollID, aimAnswerCnt);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>설문마감일을 변경한다.
	 * @param pollID
	 * @param nowDate
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollEndType(int pollID, String pollEndType, String pollEndDate, String aimAnswerCnt ){
		int result = 0;
		try{
			result = pollDAO.updatePollEndType(pollID, pollEndType, pollEndDate, aimAnswerCnt);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>문항 불러오기 - 문항 정보를 받아온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollQuestion> copyQuestionList(Map<String, Object> searchMap){
		List<PollQuestion> result = null;
		try{
			result = pollDAO.copyQuestionList(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

	/**
	 * <p>객관식 (단일선택) 문항 리스트
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollQuestion> selSingularQuestion(int pollID){
		List<PollQuestion> result = null;
		try{
			result = pollDAO.selSingularQuestion(pollID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>설문문항 스킵패턴 설정
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollSkipPattern(PollExample[] pollExamples){
		int result = 1;
		try{
			for(int i=0;i<pollExamples.length;i++){
				result = result * (pollDAO.updatePollSkipPattern(pollExamples[i]));
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>각 계정의 groupID 검색( 템플릿 사용 권한 비교 )
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public String selectGroupID(String userID) {
		String result="";
		
		try{
			result = pollDAO.selectGroupID(userID);			
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
