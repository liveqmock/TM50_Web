package web.target.targetlist.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import web.admin.dbconnect.model.DbConnectColumn;
import web.admin.dbjdbcset.model.DbJdbcSet;
import web.common.model.FileUpload;
import web.target.targetlist.dao.TargetListDAO;
import web.target.targetlist.model.*;

public class TargetListServiceImpl implements TargetListService{
	
	private TargetListDAO targetListDAO = null; 
	private Logger logger = Logger.getLogger("TM");
	
	public void setTargetListDAO(TargetListDAO targetListDAO){
		this.targetListDAO = targetListDAO;
	}
	
	
	/**
	 * <p>타겟 리스트 
	 * @return
	 */
	public List<TargetList> listTargetList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap){
		List <TargetList> result = null;
		try{
			result = targetListDAO.listTargetList(userInfo, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>총 카운트 타겟리스트 
	 * @param userInfo
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public int totalCountTargetList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap){
		int result = 0;
		try{
			result = targetListDAO.totalCountTargetList(userInfo, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>db 리스트를 불러온다.
	 * @return
	 */
	public List<DbJdbcSet> getDBList(){
		 List<DbJdbcSet> result = null;
		 try{
			 result = targetListDAO.getDBList();
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	
	/**
	 * <p>사용자의 db 리스트를 불러온다.
	 * @return
	 */
	public List<DbJdbcSet> getDBList(String userID, String groupID){
		 List<DbJdbcSet> result = null;
		 try{
			 result = targetListDAO.getDBList(userID, groupID);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * <p>업로드한 임시 파일을 저장한다.
	 * @param fileUpload
	 * @return
	 */
	public int insertFileUpload(FileUpload fileUpload){
		int result = 0;
		try{
			result = targetListDAO.insertFileUpload(fileUpload);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>업로드키로 파일 정보를 가져온다 
	 * @return
	 */	
	public FileUpload getFileInfo(String uploadKey){
		FileUpload resultList = null;		
		try{
			resultList =  targetListDAO.getFileInfo(uploadKey);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	/**
	 * <p>대상자를 보여준다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public TargetList viewTargetList(int targetID){
		TargetList targetList = null;
		try{
			targetList = targetListDAO.viewTargetList(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return targetList;
	}
	
	/**
	 * <p>파일정보를 삭제한다. 
	 * @return
	 */
	public int deleteFileInfo(String uploadKey){
		int result = 0;	
		try{
			result =  targetListDAO.deleteFileInfo(uploadKey);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	

	/**
	 * <p>파일임포트 테이블이 있다면 테이블명 리턴 
	 * @param tableName
	 * @return
	 */
	public List<Map<String, Object>> getFileImportTableIsExist(String tableName){
		 List<Map<String, Object>> result = null;
		 try{
			 result = targetListDAO.getFileImportTableIsExist(tableName);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * <p>파일임포트 테이블 생성 
	 * @param tableName
	 * @return
	 */
	public int createFileImportTable(String tableName){
		int result = 0;	
		try{
			result =  targetListDAO.createFileImportTable(tableName);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}


	/**
	 * <p>ez_fileimport에 인서트 한다. 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")	
	public int[] insertFileImport(String sql, List paramList){
		int[] result = null;
		try{
			result = targetListDAO.insertFileImport(sql, paramList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자 등록시 시작처리
	 * @param targetID
	 * @param state
	 */
	public int updateTargetingStart(String state, String targetTable, String uploadKey, int targetID){
		int result = 0;
		try{
			result = targetListDAO.updateTargetingStart(state, targetTable, uploadKey, targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자 등록시 종료 처리 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param state
	 */
	public int updateTargetingEnd(String state, int targetCount, String queryText,  int targetID){
		int result = 0;
		try{
			result = targetListDAO.updateTargetingEnd(state, targetCount, queryText, targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자 추가 제외시 시작 변경 
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingAddStart(String state, int targetID){
		int result = 0;
		try{
			result = targetListDAO.updateTargetingAddStart(state, targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자 추가 제외시 완료 변경 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param targetCount
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingAddEnd(String targetTable, String state, int targetID){
		int result = 0;
		int targetCount = 0;
		try{
			targetCount = targetListDAO.getTargetCount(targetTable, targetID);
			result = targetListDAO.updateTargetingAddEnd(state, targetCount, targetID);
			
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>원투원정보를 불러온다.
	 * @return
	 */
	public List<OneToOne> listOneToOne(){
		List<OneToOne> result = null;
		try{
			result = targetListDAO.listOneToOne();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>Add 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 */	
	public List<OneToOne> listAddOneToOne(int targetID){
		List<OneToOne> result = null;
		try{
			result = targetListDAO.listAddOneToOne(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	/**
	 * <p>타겟ID의 컬럼 위치에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public List<OnetooneTarget> getOnetoOneTargetByColumnPos(int targetID, int columnPos){
		List<OnetooneTarget> result = null;
		try{
			result = targetListDAO.getOnetoOneTargetByColumnPos(targetID,columnPos);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 */
	public int insertTargetList(TargetList targetList) throws DataAccessException{		
		int result = 0;
		try{
			result = targetListDAO.insertTargetList(targetList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자그룹 추가/제외 등록 
	 * @param targetListAdd
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetListAdd(TargetListAdd targetList) throws DataAccessException{		
		int result = 0;
		try{
			result = targetListDAO.insertTargetListAdd(targetList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자그룹수정
	 * @param targetList
	 * @return
	 */
	public int updateTargetListFile(TargetList targetList) throws DataAccessException{		
		int result = 0;
		try{
			result = targetListDAO.updateTargetListFile(targetList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>직접입력 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListDirect(TargetList targetList){
		int result = 0;
		try{
			result = targetListDAO.updateTargetListDirect(targetList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>쿼리(DB추출) 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListQuery(TargetList targetList){
		int result = 0;
		try{
			result = targetListDAO.updateTargetListQuery(targetList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>기존발송 추출 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListSended(TargetList targetList){
		int result = 0;
		try{
			result = targetListDAO.updateTargetListSended(targetList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	/**
	 * <p>ez_target_list에서 최근 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID(){
		int result = 0;
		try{
			result = targetListDAO.getMaxTargetID();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(List<OnetooneTarget> onetooneTargetList){
		int result = 0;
		try{
			for(int i=0;i<onetooneTargetList.size();i++){
				result = targetListDAO.insertOnetooneTarget(onetooneTargetList.get(i));
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID) {
		 Map<String, Object> result = null;
		 try{
			 result = targetListDAO.getDBInfo(dbID);
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	/**
	 * <p>타겟ID에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public List<OnetooneTarget> viewOnetooneTarget(int targetID){
		List<OnetooneTarget> result = null;
		try{
			result = targetListDAO.viewOnetooneTarget(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>즐겨찾기표시 업데이트 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updateBookMark(Map<String, Object>[] maps){
		int[] result = null;
		try{
			result = targetListDAO.updateBookMark(maps);
		}catch(Exception e){
			result = null;
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자에 대한 원투원정보를 삭제한다.
	 * @param targetID
	 * @return
	 */
	public int deleteOnetooneTarget(int targetID){
		int result = 0;
		try{
			result = targetListDAO.deleteOnetooneTarget(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자에 파일인서트 DB 삭제 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteFileImport(int targetID, String tableName){
		int result = 0;
		try{
			result = targetListDAO.deleteFileImport(targetID,tableName);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>원투원 삭제후 입력 
	 * @param targetID
	 * @param onetooneTargetList
	 * @return
	 */
	public int deleteAfterInsertOnetooneTarget(int targetID, List<OnetooneTarget> onetooneTargetList){
		int result=1, result1=0, result2 = 0;
		result1 = targetListDAO.deleteOnetooneTarget(targetID);
		for(int i=0;i<onetooneTargetList.size();i++){
			result2 = targetListDAO.insertOnetooneTarget(onetooneTargetList.get(i));
		}
		if(result1<0 || result2<0){
			result = -1;
		}
		return result;
	}
	
	/**
	 * <p>대상자그룹 삭제처리 - 실제삭제하진 않고 bookMark='D'로 변경한다.
	 * @param targetID
	 * @return
	 */
	public int deleteTargetList(int targetID){
		int result = 0;
		try{
			result = targetListDAO.deleteTargetList(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자 상태 조회 
	 * @param targetID
	 * @return
	 */
	public TargetList getTargetState(String target_id){
		TargetList result = null;
		try{
			result = targetListDAO.getTargetState(target_id);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}

	/**
	 * <p>추가 / 제외 이력 리스트 
	 * @param targetID
	 * @return
	 */
	public List<TargetListAdd> addHistoryList(int targetID){
		List<TargetListAdd> result = null;
		try{
			result = targetListDAO.addHistoryList(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>추가 / 제외 이력 - 직접입력
	 * @param targetAddID
	 * @return
	 */
	public String getDirectText(int targetAddID){
		String result="";
		try{
			result = targetListDAO.getDirectText(targetAddID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자 미리보기창 카운트 가져오기 
	 * @param sql
	 * @return
	 */
	public int getTargetPreviewListTotalCount(String sql){
		int result = 0;
		try{
			result = targetListDAO.getTargetPreviewListTotalCount(sql);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>대상자 미리보기 리스트 가져오기 
	 * @param sql
	 * @return
	 */
	public List<String[]> getTargetPreviewList(String sql, String[] onetoFieldName){
		List<String[]> result = null;
		try{
			result = targetListDAO.getTargetPreviewList(sql, onetoFieldName);
		}catch(Exception e){
			logger.error(e);
		}
		
		return result;
		
	}
	
	/**
	 * <p>고객연동디비 컬럼정보
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbConnectColumn> listConnectDBColumn(String dbID){
		List<DbConnectColumn> result =null;
		try{
			result = targetListDAO.listConnectDBColumn(dbID);
		}catch(Exception e){
				logger.error(e);
		}
		return result;
	}
	
	/**
	 * 대상자DB tm_fileImport에서 해당 아이디의 이메일 컬럼명을 가져온다 
	 * - 대상자 제외 기능에서 이메일 컬럼 기준으로 delete하려고 사용
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public String getEmailFieldName(int targetID){
		String result="";
		try{
			result = targetListDAO.getEmailFieldName(targetID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자 미리보기에서 이메일 삭제
	 * @param maps
	 * @return
	 */
	public int[] deletePreviewList(Map<String, Object>[] maps, String tableName){
		int[] result = null;
		try{
			result = targetListDAO.deletePreviewList(maps,tableName);
		}catch(Exception e){
			result = null;
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>대상자 미리보기에서 이메일 수정
	 * @param maps
	 * @return
	 */
	public int[] updatePreviewList(Map<String, Object>[] maps, String tableName, List<OneToOne> onetooneTargetList){
		int[] result = null;
		try{
			result = targetListDAO.updatePreviewList(maps,tableName, onetooneTargetList);
		}catch(Exception e){
			result = null;
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 이메일,휴대폰,고객명으로 대상자 리스트 검색
	 * @param maps
	 * @return
	 */
	public String getTargetIDsBySearch(Map<String, String> map){
		String result = "";
		String searchType = (String)map.get("searchType");
		try{
			List<Map<String, Object>> fieldList = targetListDAO.getFieldList(searchType);
			for(int l=0;l<fieldList.size();l++)
			{
				map.put("searchType", (String)fieldList.get(l).get("fieldName"));
				map.put("targetID", String.valueOf(fieldList.get(l).get("targetID")));
				String table = (String)fieldList.get(l).get("targetTable");
				
				List<Map<String, Object>> tagetIDs = targetListDAO.getTargetIDsBySearch(map,table);
				for(int t =0;t<tagetIDs.size();t++)
					result += ","+tagetIDs.get(t).get("targetID");
			}
				
			if(result.startsWith(","))
				result = result.substring(1);
			else if(result.equals(""))
				result = "0";
		 }catch(Exception e){
			 logger.error(e);
		 }
		 return result;
	}
	
	
	/**
	 * <p>대상자 미리보기에서 폼 타입으로 저장
	 * @param targetID
	 * @param tableName
	 * @param addFormTypeText
	 * @author 김용연
	 * @return
	 */
	public int insertPreviewFormType(int targetID, String tableName, String[] addFormTypeText){
		int result = 0;
		try{
			result = targetListDAO.insertPreviewFormType(targetID, tableName, addFormTypeText); // 폼 타입 데이터 저장 tm_fileimport_YYYYMM
			if(result > 0){
				result = targetListDAO.updateFormTypeTargetCount(targetID); // 폼 타입 대상자 저장시 tm_target_list 테이블 targetCount(대상자그룹 총 인원수) +1 업데이트
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	public String getDbAccessKey(String targetID, String dbID) {
		String result = "";
		try {
			result = targetListDAO.getDbAccessKey(targetID, dbID);
		} catch(Exception e) {
			logger.error(e);
		}
		return result;
	}
	
}
