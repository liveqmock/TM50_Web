package web.target.targetlist.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.dbconnect.model.DbConnectColumn;
import web.admin.dbjdbcset.model.DbJdbcSet;
import web.common.model.FileUpload;
import web.target.targetlist.model.*;


public interface TargetListService {
	
	/**
	 * <p>타겟 리스트 
	 * @return
	 */
	public List<TargetList> listTargetList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap);
	
	
	/**
	 * <p>총 카운트 타겟리스트 
	 * @param userInfo
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public int totalCountTargetList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap);

	/**
	 * <p> db 리스트를 불러온다.
	 * @return
	 */
	public List<DbJdbcSet> getDBList();
	
	/**
	 * <p>사용자의 db 리스트를 불러온다.
	 * @return
	 */
	public List<DbJdbcSet> getDBList(String userID, String groupID);
	
	
	/**
	 * <p>업로드한 임시 파일을 저장한다.
	 * @param fileUpload
	 * @return
	 */
	public int insertFileUpload(FileUpload fileUpload);
	
	
	/**
	 * <p>업로드키로 파일 정보를 가져온다 
	 * @return
	 */	
	public FileUpload getFileInfo(String uploadKey);
	
	
	/**
	 * <p>대상자를 보여준다. 
	 * @param targetID
	 * @return
	 */
	public TargetList viewTargetList(int targetID);

	/**
	 * <p>파일정보를 삭제한다. 
	 * @return
	 */
	public int deleteFileInfo(String uploadKey);
	
	/**
	 * <p>파일임포트 테이블이 있다면 테이블명 리턴 
	 * @param tableName
	 * @return
	 */
	public List<Map<String, Object>> getFileImportTableIsExist(String tableName);
	
	
	/**
	 * <p>파일임포트 테이블 생성 
	 * @param tableName
	 * @return
	 */
	public int createFileImportTable(String tableName);
	
	
	/**
	 * <p>ez_fileimport에 인서트 한다. 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")	
	public int[] insertFileImport(String sql, List paramList);
	
	
	/**
	 * <p>대상자 등록시 시작처리
	 * @param targetID
	 * @param state
	 */
	public int updateTargetingStart(String state, String targetTable, String uploadKey, int targetID);
	
	
	/**
	 * <p>대상자 등록시 종료 처리 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param state
	 */
	public int updateTargetingEnd(String state, int targetCount, String queryText,  int targetID);
	

	/**
	 * <p>대상자 추가 제외시 시작 변경 
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingAddStart(String state, int targetID);
	
	/**
	 * <p>대상자 추가 제외시 완료 변경 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param targetCount
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingAddEnd(String targetTable, String state, int targetID);
	
	/**
	 * <p>원투원정보를 불러온다.
	 * @return
	 */	
	public List<OneToOne> listOneToOne();
	

	/**
	 * <p>Add 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 */	
	public List<OneToOne> listAddOneToOne(int targetID);
	
	
	/**
	 * <p>타겟ID의 컬럼 위치에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 */
	public List<OnetooneTarget> getOnetoOneTargetByColumnPos(int targetID, int columnPos);

	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 */
	public int insertTargetList(TargetList targetList);
	
	/**
	 * <p>대상자그룹 추가/제외 등록 
	 * @param targetListAdd
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetListAdd(TargetListAdd targetList);
	
	/**
	 * <p>대상자그룹수정-파일업로드
	 * @param targetList
	 * @return
	 */
	public int updateTargetListFile(TargetList targetList);
	
	
	/**
	 * <p>직접입력 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListDirect(TargetList targetList) ;
	
	
	
	/**
	 * <p>쿼리(DB추출) 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListQuery(TargetList targetList);
	/**
	 * <p>기존발송 추출 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListSended(TargetList targetList);
	
	
	/**
	 * <p>ez_target_list에서 최근 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID();
	
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(List<OnetooneTarget> onetooneTargetList);
	
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID);
	
	/**
	 * <p>타겟ID에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 */
	public List<OnetooneTarget> viewOnetooneTarget(int targetID);
	
	/**
	 * <p>즐겨찾기표시 업데이트 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updateBookMark(Map<String, Object>[] maps);
	
	
	/**
	 * <p>대상자에 대한 원투원정보를 삭제한다.
	 * @param targetID
	 * @return
	 */
	public int deleteOnetooneTarget(int targetID);
	
	/**
	 * <p>대상자에 파일인서트 DB 삭제 
	 * @param targetID
	 * @return
	 */
	public int deleteFileImport(int targetID, String tableName);
	
	
	
	/**
	 * <p>원투원 삭제후 입력 
	 * @param targetID
	 * @param onetooneTargetList
	 * @return
	 */
	public int deleteAfterInsertOnetooneTarget(int targetID, List<OnetooneTarget> onetooneTargetList);
	
	/**
	 * <p>대상자그룹 삭제처리 - 실제삭제하진 않고 bookMark='D'로 변경한다.
	 * @param targetID
	 * @return
	 */
	public int deleteTargetList(int targetID);
	
	/**
	 * <p>대상자 상태 조회 
	 * @param targetID
	 * @return
	 */
	public TargetList getTargetState(String target_id);
	

	/**
	 * <p>추가 / 제외 이력 리스트 
	 * @param targetID
	 * @return
	 */
	public List<TargetListAdd> addHistoryList(int targetID);
	
	/**
	 * <p>추가 / 제외 이력 - 직접입력
	 * @param targetAddID
	 * @return
	 */
	public String getDirectText(int targetAddID);
	
	
	/**
	 * <p>대상자 미리보기창 카운트 가져오기 
	 * @param sql
	 * @return
	 */
	public int getTargetPreviewListTotalCount(String sql);
	
	
	/**
	 * <p>대상자 미리보기 리스트 가져오기 
	 * @param sql
	 * @return
	 */
	public List<String[]> getTargetPreviewList(String sql, String[] onetoFieldName);

	/**
	 * <p>고객연동디비 컬럼정보
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectColumn> listConnectDBColumn(String dbID);
	
	
	public String getEmailFieldName(int targetID);
	
	/**
	 * <p>대상자 미리보기에서 이메일 삭제
	 * @param maps
	 * @return
	 */
	public int[] deletePreviewList(Map<String, Object>[] maps, String tableName);
	
	/**
	 * <p>대상자 미리보기에서 이메일 수정
	 * @param maps
	 * @return
	 */
	public int[] updatePreviewList(Map<String, Object>[] maps, String tableName, List<OneToOne> onetooneTargetList);
	
	/**
	 * 이메일,휴대폰,고객명으로 대상자 리스트 검색
	 * @param maps
	 * @return
	 */
	public String getTargetIDsBySearch(Map<String, String> map);
	
	
	
	/**
	 * <p>대상자 미리보기에서 폼 타입으로 저장
	 * @param targetID
	 * @param tableName
	 * @param addFormTypeText
	 * @author 김용연
	 * @return
	 */
	public int insertPreviewFormType(int targetID, String tableName, String[] addFormTypeText);
	
	/**
	 * <p>tm_dbset에서 dbAccessKey 값을 가져온다. 
	 * @param targetID
	 * @return
	 */
	public String getDbAccessKey(String targetID, String dbID);
	
}
