package web.common.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import web.common.util.QueryUtil;
import web.common.model.TargetList;
import web.common.model.OnetooneTarget;

public class TransferTargetDAOImpl extends DBJdbcDaoSupport implements TransferTargetDAO{
	
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("common_sql","common.util.getdbinfo");				
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID",	dbID);
		return getSimpleJdbcTemplate().queryForMap(sql, param); 
	}
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetList(TargetList targetList) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("common_sql","common.target.insert");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetName",	targetList.getTargetName());
		param.put("description", 	targetList.getDescription());
		param.put("userID", 			targetList.getUserID());
		param.put("bookMark",		targetList.getBookMark());
		param.put("shareType",		targetList.getShareType());
		param.put("shareID",			targetList.getShareID());
		param.put("targetType",	targetList.getTargetType());
		param.put("upload_key",	targetList.getUploadKey());
		param.put("dbID",				targetList.getDbID());
		param.put("queryText",		targetList.getQueryText());
		param.put("countQuery",	targetList.getCountQuery());
		param.put("targetCount",	targetList.getTargetCount());
		param.put("directText",		targetList.getDirectText());
		param.put("state",			targetList.getState());
		param.put("sendedDate",targetList.getSendedDate());
		param.put("successYN",targetList.getSuccessYN());
		param.put("openYN",targetList.getOpenYN());
		param.put("clickYN",targetList.getClickYN());
		param.put("rejectcallYN",targetList.getRejectcallYN());
		param.put("connectedDbID",targetList.getConnectedDbID());
		param.put("massmailGroupID",targetList.getMassmailGroupID());
		param.put("targetGroupID",targetList.getTargetGroupID());
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>target_list에서 max의 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("common_sql","common.target.getmaxtargetid");			
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(OnetooneTarget onetooneTarget) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("common_sql","common.target.insertonetoonetarget");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", onetooneTarget.getTargetID());
		param.put("fieldName", onetooneTarget.getFieldName());
		param.put("onetooneID", onetooneTarget.getOnetooneID());
		param.put("fieldDesc", onetooneTarget.getFieldDesc());
		param.put("csvColumnPos", onetooneTarget.getCsvColumnPos());
		
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>해당월에 대상자파일 테이블이 있는지 확인 
	 * @param yyyymm
	 * @return
	 */
	public List<Map<String, Object>> getFileImportTableIsExist(String tableName){
		String sql = QueryUtil.getStringQuery("common_sql","common.target.isexisttable");	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("tableName", tableName);
		return getSimpleJdbcTemplate().queryForList(sql, param);
	}
	
	
	/**
	 * <p>파일임포트 테이블 생성 
	 * @param tableName
	 * @return
	 * @throws DataAccessException
	 */
	public int createFileImportTable(String tableName) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("common_sql","common.target.createtable");	
		sql = " CREATE TABLE "+ tableName +" "+ sql ;
			
		return getSimpleJdbcTemplate().update(sql);
		
	}
	
	/**
	 * <p>ez_fileimport 인서트 한다. 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")	
	public int[] insertFileImport(String sql, final List paramList) throws DataAccessException{
		int count = 0;
		for(int i =0;i<sql.length();i++)
		{
			if(sql.charAt(i)=='?')
				count++;
		}
		final int COUNT_PARAM = count;
		return getSimpleJdbcTemplate().getJdbcOperations().batchUpdate(sql,
				new BatchPreparedStatementSetter() { 
					public int getBatchSize() { 
						return paramList.size(); 
	               } 

					public void setValues(PreparedStatement ps, int index) throws SQLException { 
						List<String> params = (List<String>) paramList.get(index);
						if(params.size()>=COUNT_PARAM)
						{
							for(int i=0; i < COUNT_PARAM; i ++) {
								ps.setString(i+1, params.get(i));
							}
							 
							
						}
					} 
			}); 
	}
	
	/**
	 * <p>대상자완료 상태 변경 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingEnd(String state, int targetCount, String query, String tableName, int targetID, String count_query) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("common_sql","common.target.updatestatetargetend");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("state", state);
		param.put("targetCount",  new Integer(targetCount));
		param.put("queryText",  query);
		param.put("targetTable",  tableName);
		param.put("countQuery",  count_query);
		param.put("targetID",  new Integer(targetID));
		return getSimpleJdbcTemplate().update(sql,param);
	}

}
