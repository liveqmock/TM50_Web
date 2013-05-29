package web.target.targetlist.dao;


import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;

import web.admin.dbconnect.model.DbConnectColumn;
import web.admin.dbjdbcset.model.DbJdbcSet;
import web.common.model.FileUpload;
import web.target.targetlist.model.*;


public interface TargetListDAO {
	

	/**
	 * <p>타겟 리스트 
	 * @param userInfo
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<TargetList> listTargetList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap)  throws DataAccessException;
	
	
	
	/**
	 * <p>총 카운트 타겟리스트 
	 * @param userInfo
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountTargetList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap)  throws DataAccessException;
	
	
	/**
	 * <p>db 리스트를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbJdbcSet> getDBList() throws DataAccessException;	
	
	/**
	 * <p>사용자의 db 리스트를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbJdbcSet> getDBList(String userID, String groupID) throws DataAccessException;
	
	
	
	/**
	 * <p>업로드한 임시 파일을 저장한다.
	 * @param fileUpload
	 * @return
	 */
	public int insertFileUpload(FileUpload fileUpload) throws DataAccessException;	
	
	
	/**
	 * <p>업로드키로 파일정보 불러오기 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public FileUpload getFileInfo(String uploadKey) throws DataAccessException;
	
	
	/**
	 * <p>대상자를 보여준다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public TargetList viewTargetList(int targetID) throws DataAccessException;
	
	
	/**
	 * <p>파일정보를 삭제한다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteFileInfo(String uploadKey) throws DataAccessException;
	
	
	
	/**
	 * <p>해당월에 대상자파일 테이블이 있는지 확인 
	 * @param yyyymm
	 * @return
	 */
	public List<Map<String, Object>> getFileImportTableIsExist(String tableName) throws DataAccessException;
	
	
	
	/**
	 * <p>파일임포트 테이블 생성 
	 * @param tableName
	 * @return
	 * @throws DataAccessException
	 */
	public int createFileImportTable(String tableName) throws DataAccessException;
	
	/**
	 * <p>ez_fileimport 인서트 한다. 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")	
	public int[] insertFileImport(String sql, List paramList) throws DataAccessException;
	
	
	
	
	/**
	 * <p>대상자 등록시 시작처리
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingStart(String state,String targetTable, String uploadKey, int targetID) throws DataAccessException;
	
	
	/**
	 * <p>대상자 등록시 종료처리 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingEnd(String state, int targetCount, String queryText,  int targetID) throws DataAccessException;
	
	/**
	 * <p>대상자 추가 제외시 시작 변경 
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingAddStart(String state, int targetID) throws DataAccessException;
	/**
	 * <p>원투원정보를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<OneToOne> listOneToOne() throws DataAccessException;
	
	/**
	 * <p>Add 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */	
	public List<OneToOne> listAddOneToOne(int targetID) throws DataAccessException;
	
	/**
	 * <p>타겟ID의 컬럼 위치에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public List<OnetooneTarget> getOnetoOneTargetByColumnPos(int targetID, int columnPos) throws DataAccessException;
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetList(TargetList targetList) throws DataAccessException;
	
	/**
	 * <p>대상자그룹 추가/제외 등록 
	 * @param targetListAdd
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetListAdd(TargetListAdd targetList) throws DataAccessException;
	
	/**
	 * <p>대상자 추가 제외시 완료 변경 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param targetCount
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingAddEnd(String state, int targetCount, int targetID) throws DataAccessException;
	

	/**
	 * <p>대상자 추가 제외 완료 후 targetCount 
	 * @param targetID
	 * @param targetTable
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetCount(String targetTable, int targetID) throws DataAccessException;
	
	/**
	 * <p>업로드 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListFile(TargetList targetList) throws DataAccessException;
	
	
	/**
	 * <p>직접입력 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListDirect(TargetList targetList) throws DataAccessException;
	
	
	
	/**
	 * <p>쿼리(DB추출) 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListQuery(TargetList targetList) throws DataAccessException;
	
	/**
	 * <p>기존발송 추출 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListSended(TargetList targetList) throws DataAccessException;
	
	/**
	 * <p>대상자에 대한 원투원정보를 삭제한다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteOnetooneTarget(int targetID) throws DataAccessException;
	
	
	/**
	 * <p>ez_target_list에서 최근 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID() throws DataAccessException;
	
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(OnetooneTarget onetooneTarget) throws DataAccessException;
	
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID)  throws DataAccessException;
	
	
	/**
	 * <p>타겟ID에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public List<OnetooneTarget> viewOnetooneTarget(int targetID) throws DataAccessException;
	
	
	
	/**
	 * <p>즐겨찾기표시 업데이트 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updateBookMark(Map<String, Object>[] maps) throws DataAccessException;
	
	
	/**
	 * <p>대상자에 파일인서트 DB 삭제 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteFileImport(int targetID, String tableName) throws DataAccessException;

	
	/**
	 * <p>대상자그룹 삭제처리 - 실제삭제하진 않고 bookMark='D'로 변경한다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteTargetList(int targetID)  throws DataAccessException;
	
	/**
	 * <p>대상자 상태 조회 
	 * @param targetID
	 * @return
	 */
	public TargetList getTargetState(String target_id)  throws DataAccessException;


	/**
	 * <p>타겟 리스트 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public List<TargetListAdd> addHistoryList(int targetID)  throws DataAccessException;
	
	/**
	 * <p>추가 / 제외 이력 - 직접입력
	 * @param targetAddID
	 * @return
	 */
	public String getDirectText(int targetAddID) throws DataAccessException;
	
	/**
	 * <p>대상자 미리보기창 카운트 가져오기 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetPreviewListTotalCount(String sql)  throws DataAccessException;
	
	/**
	 * <p>대상자 미리보기창 리스트 가져오기
	 * @param sql, onetoFieldName[]
	 * @return
	 * @throws DataAccessException
	 */
	public List<String[]> getTargetPreviewList(String sql, String[] onetoFieldName)  throws DataAccessException;
	
	/**
	 * <p>고객연동디비 컬럼정보
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectColumn> listConnectDBColumn(String dbID) throws DataAccessException;
	
	/**
	 * 대상자DB tm_fileImport에서 해당 아이디의 이메일 컬럼명을 가져온다 
	 * - 대상자 제외 기능에서 이메일 컬럼 기준으로 delete하려고 사용
	 * @param targetID
	 * @return String colName
	 * @throws DataAccessException
	 */
	public String getEmailFieldName(int targetID) throws DataAccessException;
	
	/**
	 * <p>대상자 미리보기에서 이메일 삭제
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] deletePreviewList(Map<String, Object>[] maps, String tableName) throws DataAccessException;
	
	/**
	 * <p>대상자 미리보기에서 이메일 수정
	 * @param maps
	 * @return
	 */
	public int[] updatePreviewList(Map<String, Object>[] maps, String tableName, List<OneToOne> onetooneTargetList) throws DataAccessException;
	
	/**
	 * 이메일,휴대폰,고객명으로 대상자 리스트 검색
	 * @param maps
	 * @return
	 */
	public List<Map<String, Object>> getTargetIDsBySearch(Map<String, String> map, String tableName) throws DataAccessException;
	
	
	/**
	 * <p>이메일, 고객명, 휴대폰 필드명 리스트 
	 * @return
	 */
	public List<Map<String, Object>> getFieldList(String type) throws DataAccessException;
	
	
	/**
	 * <p>대상자 미리보기에서 폼 타입으로 저장
	 * @param targetID
	 * @param tableName
	 * @param addFormTypeText
	 * @author 김용연
	 * @return
	 */
	public int insertPreviewFormType(int targetID, String tableName, String[] addFormTypeText) throws DataAccessException;
	
	
	/**
	 * <p>대상자 미리보기에서 폼 별 추가시 tm_target_list 테이블 targetCount(총인원수) 증가
	 * @param targetID
	 * @author 김용연
	 * @throws DataAccessException
	 * @return
	 */
	public int updateFormTypeTargetCount(int targetID) throws DataAccessException;
	
	/**
	 * <p>tm_dbset 에서 dbAccessKey를 불러온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public String getDbAccessKey(String targetID, String dbID)  throws DataAccessException;
	
}

