package web.common.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import web.common.dao.TransferTargetDAO;
import web.common.model.TargetList;
import web.common.model.OnetooneTarget;



public class TransferTargetServiceImpl implements TransferTargetService {
	
	private TransferTargetDAO transferTargetDAO = null; 
	private Logger logger = Logger.getLogger("TM");
	
	public void setTransferTargetDAO(TransferTargetDAO transferTargetDAO){
		this.transferTargetDAO = transferTargetDAO;
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
			 result = transferTargetDAO.getDBInfo(dbID);
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
			result = transferTargetDAO.insertTargetList(targetList);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>target_list에서 최근 targetID를 가져온다. 
	 * @return
	 */
	public int getMaxTargetID(){
		int result = 0;
		try{
			result = transferTargetDAO.getMaxTargetID();
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
				result = transferTargetDAO.insertOnetooneTarget(onetooneTargetList.get(i));
			}
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
			 result = transferTargetDAO.getFileImportTableIsExist(tableName);
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
			result =  transferTargetDAO.createFileImportTable(tableName);
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
			result = transferTargetDAO.insertFileImport(sql, paramList);
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
	public int updateTargetingEnd(String state, int targetCount, String query, String tableName, int targetID, String count_query){
		int result = 0;
		try{
			result = transferTargetDAO.updateTargetingEnd(state, targetCount, query, tableName, targetID, count_query);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}

}
