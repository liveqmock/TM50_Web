package web.massmail.statistic.service;


import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;

import web.content.poll.model.PollQuestion;
import web.massmail.statistic.model.*;
import web.massmail.write.model.MassMailList;
import web.massmail.write.model.OnetooneTarget;
import web.massmail.write.model.TargetingGroup;


public interface  MassMailStatService {
	
	
	/**
	 * <p>대상자그룹리스트 
	 * @param massmailID
	 * @return
	 */
	public List<TargetingGroup> listTargetingGroup(int massmailID);
	
	
	/**
	 * <p>대량메일 통계 기본정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo massMailStatisticBasicInfo(int massmailID, int scheduleID);
	
	/**
	 * <p>대량메일 통계 발송정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailInfo massMailStatisticSendInfo(int massmailID, int scheduleID);
	
	/**
	 * <p>대량메일 통계 필터정보
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailFilter massMailStatisticFilterInfo(int massmailID, int scheduleID);

	
	/**
	 * <p>대량메일 링크별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailLink> statisticLinkList(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 링크별 통계 Bar 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticLinkBar(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 링크별 통계 Pie 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticLinkPie(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 시간별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatistic> statisticHourlyList(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 시간별 통계 Bar 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticHourlyBar(Map<String, Object> searchMap);

	/**
	 * <p>대량메일 시간별 통계 Pie 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticHourlyPie(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 일자별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatistic> statisticDailyList(Map<String, Object> searchMap);
	
	
	/**
	 * <p>대량메일 일자별 통계 Bar 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticDailyBar(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 일자별 통계 Pie 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticDailyPie(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 도메인별 통계 리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatistic> statisticDomainList(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 도메인별 통계 Bar 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticDomainBar(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 도메인별 통계 Pie 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticDomainPie(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 실패 도메인 통계 리스트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticDomainFail> statisticFailDomainList(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 실패 도메인 통계 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticFailDomainBar(Map<String, Object> searchMap);
	

	/**
	 * <p>대량메일 실패 도메인 통계 Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticFailDomainPie(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 실패원인별 통계 리스트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticFailCause> massMailStatisticFailCauseList(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 실패원인별 통계 Bar 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticFailCauseBar(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 실패원인별 통계 pie 차트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String statisticFailCausePie(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 대상자 미리보기 총카운트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalMassMailPersonPreview(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 대상자 미리보기
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailPersonPreview> personPreview(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 월간 보고서 - 총괄현황
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailReportMonth massMailReportMonthTotalInfo(String year, String month, String dateFrom, String dateTo, String userID, String groupID, String[] userInfo);
	
	/**
	 * <p>대량메일 월간 보고서 총괄현황
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailReportMonth massMailReportMonthSendInfo(String year, String month, String dateFrom, String dateTo, String userID, String groupID, String[] userInfo);
	
	
	/**
	 * <p>대량메일 월간 보고서 - Domain별 Email 발송 현황 리스트
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailReportMonth> massmailReportMonthDomainStatisticList(String year, String month, String dateFrom, String dateTo, String userID, String groupID);
	
	/**
	 * <p>대량메일 월간 보고서 - Domain별 Email 발송 현황 (사용 안함, 확인되면 삭제 할 예정)
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailReportMonthDomainStatisticBar(String year, String month, String dateFrom, String dateTo, String userID, String groupID);
	
	/**
	 * <p>대량메일 월간 보고서 - 시간대별 Email 발송 현황 리스트
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailReportMonth> massmailReportMonthTimeStatisticList(String year, String month, String dateFrom, String dateTo, String userID, String groupID);
	
	/**
	 * <p>대량메일 월간 보고서 - 시간대별 Email 발송 현황 (사용 안함, 확인되면 삭제 할 예정)
	 * @param year
	 * @param month
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailReportMonthTimeStatisticBar(String year, String month, String dateFrom, String dateTo, String userID, String groupID);
	
	/**
	 * <p>대량메일 리스트 
	 * @param userAuth
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MassMailList> massMailReportMonthList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap);
		
	/**
	 * <p>대량메일 총카운트 
	 * @param userAuth
	 * @param searchMap
	 * @return
	 */
	public int totalCountMassMailReportMonthList(String[] userInfo, Map<String, String> searchMap);
	
	/**
	 * <p>계정별 통계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 */
	public List<MassMailStatisticUsers> massmailStatisticUsersList(String[] userInfo, Map<String, String> searchMap);
	
	/**
	 * <p>계정별 통계합
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 */
	public List<MassMailStatisticUsers> massmailStatisticUsersHap(String year, String month);
	
	/**
	 * <p>대량메일 계정별 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailStatisticUsersBar(String[] userInfo, Map<String, String> searchMap);
	
	/**
	 * <p>대량메일 계정별 Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailStatisticUsersPie(String[] userInfo, Map<String, String> searchMap);
	
	
	/**
	 * <p>그룹별 통계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 */
	public List<MassMailStatisticUsers> massmailStatisticUsersGroupList(String year, String month);
	
	/**
	 * <p>그룹별 통계합계
	 * @param year
	 * @param month
	 * @param groupID
	 * @return
	 */
	public List<MassMailStatisticUsers> massmailStatisticUsersGroupHap(String year, String month);
	
	/**
	 * <p>대량메일 그룹별 Bar 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailStatisticUsersGroupBar(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 그룹별 Pie 차트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String massmailStatisticUsersGroupPie(Map<String, Object> searchMap);
	
	/**
	 * <p> 초기화면 - 관심 도메인 정보
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticDomain> massmailStatisticConcernedDomain();
	
	/**
	 * <p>선택한 실패원인 그룹의 재발송을 요청한다. 
	 * @param massmailID, scheduleID, failCauseCode
	 * @return
	 * @throws DataAccessException
	 */
	public int retryMail(int massmailID, int scheduleID, String[] failCauseCodes);
	
	/**
	 * <p>선택한 실페 도메인 그룹의 재발송을 요청한다. 
	 * @param massmailID, scheduleID, failCauseCode
	 * @return
	 * @throws DataAccessException
	 */
	public int retryDomain(int massmailID, int scheduleID, String[] domains);
	
	/**
	 * <p><p>설문에 미응답한 대상자를 재발송한다.
	 * @param massmailID, scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int retryPoll(int massmailID, int scheduleID);
	
	/**
	 * <p>설문통계 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollStatistic(int pollID, int massmailID, int scheduleID);
	
	
	/**
	 * <p>대량메일에 해당하는 설문 아이디 가져오기 
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int getPollIDByMassMail(int massmailID);
	
	
	/**
	 * <p>설문응답자 보기 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param questionID
	 * @param exampleID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailPersonPreview> massmailPollPreview(int currentPage, int countPerPage, Map<String, Object> searchMap);
	
	
	/**
	 * <p>설문응답자 수 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int massmailPollPreviewTotalCount(Map<String, Object> searchMap);
	
	
	/**
	 * <p>설문통계
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> massmailPollStatisticCount(int massmailID, int scheduleID, int pollID);
	
	
	/**
	 * <p>설문통계(통계수집완료시)
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object> massmailPollStatisticCountFinish(String yearMonth, int massmailID, int scheduleID, int pollID);
	
	
	
	/**
	 * <p>대량메일 결과 백업 테이블 정보 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String getBackupYearMonth(Map<String, Object> searchMap);
	
	
	/**
	 * <p>설문모든 응답자들 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailPersonPreview> massmailPollAllResponse(int currentPage, int countPerPage, Map<String, Object> searchMap);

	
	/**
	 * <p>설문모든 미응답자들 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailPersonPreview> massmailPollAllNotResponse(int currentPage, int countPerPage, Map<String, Object> searchMap, String backupYearMonth);
	
	
	/**
	 * <p>설문전체응답자 수 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int massmailPollAllResponseTotalCount(Map<String, Object> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>설문응답상세보기 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param sendID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MassMailStatisticPoll> viewDetailAnswer(int pollID, int massmailID, int scheduleID, int sendID, String matrixX, String matrixY);
	
	
	
	/**
	 * <p>설문에 해당하는 설문 문항들을 가져온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public List<PollQuestion> selectQuestionByPollID(int pollID);
	
	
	/**
	 * <p>해당 설문ID에 해당하는 보기와 응답수를 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollStatisticByQuestionID(int massmailID, int scheduleID, int pollID, int questionID);
	
	/**
	 * <p>순위선택 응답수를 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollStatisticByExampleType3(int massmailID, int scheduleID, int pollID, int questionID, int selectCount);
	
	/**
	 * <p>세로(Y축) 보기를 가져온다.
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollExampleMatrixY(int pollID, int questionID);
	
	
	/**
	 * <p>매트릭스 응답통계를 가져온다.
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollAnswerMatrixXY(int massmailID, int scheduleID, int pollID, int questionID);
	
	
	/**
	 * <p>설문에 해당하는 문항정보보기 
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public MassMailStatisticPoll viewQuestion(int pollID, int questionID);
	
	/**
	 * <p>설문 개인별 응답 현황 
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public List<MassMailStatisticPoll> selectPollIndividualStatistic(int massmailID, int scheduleID);
	
	/**
	 * <p>대량메일 대상자별 통계 리스트 
	 * @param searchMap
	 * @return
	 */
	public List<MassMailStatistic> statisticTargetList(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 대상자별 통계 Bar 차트 
	 * @param searchMap
	 * @return
	 */
	public String statisticTargetBar(Map<String, Object> searchMap);
	
	/**
	 * <p>대량메일 대상자별 통계 Pie 차트 
	 * @param searchMap
	 * @return
	 */
	public String statisticTargetPie(Map<String, Object> searchMap);
	
	
	/**
	 * <p>타겟ID에 해당되는 원투원 정보 
	 * @param targetIDs
	 * @return
	 */
	public List<OnetooneTarget> selectOnetooneByTargetID(String[] targetIDs, int massmailID);
	
	/**
	 * 대량메일에ID에 해당되는 타겟ID 
	 * @param massmailID
	 * @return
	 */
	public String[] getTargetIDs(int massmailID);
	
	
	/**
	 * <p>설문통계( 해당 메일에 사용 된 설문의 종료기준, 목표응답수, 설문마감일 표시) 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectPollInfo(int pollID);
	
	
	/**
	 * <p>해당 설문조사 타이틀 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public String selectPollTitle(int pollID);
	
	/**
	 * <p>각 문항당 보기의 갯 수 
	 * @param massmailID, scheduleID
	 * @return 
	 * @throws DataAccessException
	 */
	public int exampleCount(int pollID, int questionID);
	
	/**
	 * <p>문항 별 보기 내용
	 * @param pollID
	 * @param questionID
	 * @param exampleID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String selectExampleDesc(int pollID,int questionID,int exampleID);
	
	/**
	 * <p>주관식 답안 목록 
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MassMailStatisticPoll> responseTextAll(int pollID, int massmailID, int scheduleID, int questionID);
	
	
	/**
	 * <p>객관식 문항  주관식 답안 갯수(문제마다 포함된 갯수) 
	 * @param pollID, questionID
	 * @return 
	 */
	public int exampleExYNcount(int pollID, int questionID);
	
	
	/**
	 * <p>객관식 문항  주관식 답안 갯수(설문내에 포함된 갯수) 
	 * @param pollID
	 * @return 
	 * @throws DataAccessException
	 */
	public int pollExampleExYNcount (int pollID);
	
	
	/**
	 * <p>설문 내 주관식 문항 갯 수
	 * @param pollID
	 * @return 
	 * @throws DataAccessException
	 */
	public int exampleTypeCount (int pollID);
	
	
	/**
	 * <p>설문 내 보기 총 갯 수(셀 총 갯 수 - 여백설정을 위한 카운트)
	 * @param pollID
	 * @return 
	 * @throws DataAccessException
	 */
	public int exampleAllCount (int pollID);
	
	/**
	 * <p>설문전체 미응답자 수 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int massmailPollAllNotResponseTotalCount(Map<String, Object> searchMap, String backupYearMonth);
}
