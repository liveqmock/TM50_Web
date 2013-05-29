package web.target.targetlist.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.admin.dbconnect.model.DbConnectColumn;
import web.admin.dbjdbcset.model.DbJdbcSet;
import web.common.dao.DBJdbcDaoSupport;
import web.common.model.FileUpload;
import web.common.util.*;
import web.target.targetlist.model.*;

import org.springframework.dao.EmptyResultDataAccessException;

public class TargetListDAOImpl extends DBJdbcDaoSupport implements TargetListDAO {
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";
	
	/**
	 * <p>타겟 리스트 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TargetList> listTargetList(String[] userInfo, int currentPage, int countPerPage, Map<String, String> searchMap)  throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;	
		
		
		String bookMark = searchMap.get("bookMark");
		String targetType = searchMap.get("targetType");
		String shareType = searchMap.get("shareType");	
		String searchType = searchMap.get("searchType");
		String targetGroupID = searchMap.get("targetGroupID");
		String searchText = searchMap.get("searchText");
		
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.select");		
		String tailsql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.tail");		
		
		
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		
		//수퍼관리자(시스템관리자)가 아니라면 
		if(!userInfo[0].equals(Constant.USER_LEVEL_ADMIN)){
			//전체공유와 그룹공유는  출력, 그리고 지정공유라면 본인의 그룹아이디나 사용자아이디가 있다면 출력 
			sql+=" AND ((t.shareType='"+Constant.SHARE_TYPE_ALL+"')"
				+ " OR (t.shareType='"+Constant.SHARE_TYPE_GROUP+"' AND u.groupID='"+userInfo[2]+"')" 
				+ " OR (t.shareType='"+Constant.SHARE_TYPE_D+"' AND t.shareID IN ('"+userInfo[1]+"','"+userInfo[2]+"'))";
			
			//일반사용자라면 (비공유는 본인꺼만 확인 가능)
			 if(userInfo[0].equals(Constant.USER_LEVEL_USER)){		
				 sql+=" OR (t.shareType='"+Constant.SHARE_TYPE_NOT+"' AND t.userID IN ('"+userInfo[1]+"'))";
			 }		
			
			//그룹관리자라면 (그룹구성원에 대한 것은 비공유라도 볼 수 있다.)
			if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
				 sql+=" OR (t.shareType='"+Constant.SHARE_TYPE_NOT+"' AND u.groupID IN ('"+userInfo[2]+"'))";
			}
			sql+=") ";
		}else{
			//수퍼관리자는 전체를 볼수 있기 때문에 별다른 처리를 하지 않는다.
		}

		//검색에 맞게 조회한다.		
		if(bookMark!=null && !bookMark.equals("")){
			sql += " AND t.bookMark='"+bookMark+"'";
		}else{
			sql += " AND t.bookMark NOT IN('D') ";   //D는 삭제된 것이므로 제외한다.
		}
		
		if(targetType!=null && !targetType.equals("")){
			sql += " AND t.targetType='"+targetType+"'";
		}
		
		if(shareType!=null && !shareType.equals("")){
			sql += " AND t.shareType='"+shareType+"'";
		}
		
		if(targetGroupID!=null && !targetGroupID.equals("")){
			sql += " AND t.targetGroupID='"+targetGroupID+"'";
		}
		
		if(searchText!=null && !searchText.equals("")){
			if(searchType.equalsIgnoreCase("t.targetID"))
				sql += " AND  "+searchType+" in ("+searchText+") ";
			else
				sql += " AND  "+searchType+" LIKE :searchText ";
		}	
	
		
		sql+= tailsql;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {		
				TargetList targetList = new TargetList();
				targetList.setTargetID(rs.getInt("targetID"));
				targetList.setTargetName(rs.getString("targetName"));
				targetList.setUserID(rs.getString("userID"));
				targetList.setUserName(rs.getString("userName"));
				targetList.setGroupID(rs.getString("groupID")); 
				targetList.setBookMark(rs.getString("bookMark"));
				targetList.setShareType(rs.getString("shareType"));
				targetList.setShareID(rs.getString("shareID"));
				targetList.setTargetType(rs.getString("targetType"));
				targetList.setTargetCount(rs.getInt("targetCount"));
				targetList.setState(rs.getString("state"));
				targetList.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				
				return targetList;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>총 카운트 타겟리스트
	 * @param userInfo
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int totalCountTargetList(String[] userInfo, int currentPage, int countPerPage,Map<String, String> searchMap)  throws DataAccessException{
		
		String bookMark = searchMap.get("bookMark");
		String targetType = searchMap.get("targetType");
		String shareType = searchMap.get("shareType");	
		String targetGroupID = searchMap.get("targetGroupID");
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.totalcount");		
	
		//userInfo[0] = 사용자권한, userInfo[1] =사용자아이디, userInfo[2] = 사용자그룹아이디  
		
		//수퍼관리자(시스템관리자)가 아니라면 
		if(!userInfo[0].equals(Constant.USER_LEVEL_ADMIN)){
			//전체공유와 그룹공유는  출력, 그리고 지정공유라면 본인의 그룹아이디나 사용자아이디가 있다면 출력 
			sql+=" AND ((t.shareType='"+Constant.SHARE_TYPE_ALL+"')"
				+ " OR (t.shareType='"+Constant.SHARE_TYPE_GROUP+"' AND u.groupID='"+userInfo[2]+"')" 
				+ " OR (t.shareType='"+Constant.SHARE_TYPE_D+"' AND t.shareID IN ('"+userInfo[1]+"','"+userInfo[2]+"'))";
			
			//일반사용자라면 (비공유는 본인꺼만 확인 가능)
			 if(userInfo[0].equals(Constant.USER_LEVEL_USER)){		
				 sql+=" OR (t.shareType='"+Constant.SHARE_TYPE_NOT+"' AND t.userID IN ('"+userInfo[1]+"'))";
			 }		
			
			//그룹관리자라면 (그룹구성원에 대한 것은 비공유라도 볼 수 있다.)
			if(userInfo[0].equals(Constant.USER_LEVEL_MASTER)){
				 sql+=" OR (t.shareType='"+Constant.SHARE_TYPE_NOT+"' AND u.groupID IN ('"+userInfo[2]+"'))";
			}
			sql+=") ";
		}else{
			//수퍼관리자는 전체를 볼수 있기 때문에 별다른 처리를 하지 않는다.
		}
	
		//검색에 맞게 조회한다.		
		if(bookMark!=null && !bookMark.equals("")){
			sql += " AND t.bookMark='"+bookMark+"'";
		}else{
			sql += " AND t.bookMark NOT IN('D') ";   //D는 삭제된 것이므로 제외한다.
		}
		
		if(targetType!=null && !targetType.equals("")){
			sql += " AND t.targetType='"+targetType+"'";
		}
		
		if(shareType!=null && !shareType.equals("")){
			sql += " AND t.shareType='"+shareType+"'";
		}
		
		if(targetGroupID!=null && !targetGroupID.equals("")){
			sql += " AND t.targetGroupID='"+targetGroupID+"'";
		}
		
		if(searchText!=null && !searchText.equals("")){
			if(searchType.equalsIgnoreCase("t.targetID"))
				sql += " AND  "+searchType+" in ("+searchText+") ";
			else
				sql += " AND  "+searchType+" LIKE :searchText ";
		}	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	
	/**
	 * <p>사용자의 db 리스트를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbJdbcSet> getDBList() throws DataAccessException{
	
		String sql =QueryUtil.getStringQuery("targetlist_sql","target.targetlist.selectdbadmin");		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbJdbcSet  dbJdbcSet = new DbJdbcSet();						
				dbJdbcSet.setDbID(rs.getString("dbID"));			
				dbJdbcSet.setDbName(rs.getString("dbName"));				
				dbJdbcSet.setDefaultYN(rs.getString("defaultYN"));
				return dbJdbcSet;
			}			
		};
			
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	
	/**
	 * <p>사용자의 db 리스트를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbJdbcSet> getDBList(String userID, String groupID) throws DataAccessException{
	
		String sql =QueryUtil.getStringQuery("targetlist_sql","target.targetlist.selectdbbyuser");		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbJdbcSet  dbJdbcSet = new DbJdbcSet();						
				dbJdbcSet.setDbID(rs.getString("dbID"));			
				dbJdbcSet.setDbName(rs.getString("dbName"));			
				dbJdbcSet.setDefaultYN(rs.getString("defaultYN"));
				return dbJdbcSet;
			}			
		};

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID",userID);
		param.put("groupID",groupID);
			
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>업로드한 임시 파일을 저장한다.
	 * @param fileUpload
	 * @return
	 */
	public int insertFileUpload(FileUpload fileUpload) throws DataAccessException{		
	
		String sql =  QueryUtil.getStringQuery("targetlist_sql","target.fileupload.insert"); 	
		
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("upload_key", fileUpload.getUploadKey());
		param.put("real_file_name", fileUpload.getRealFileName());
		param.put("new_file_name", fileUpload.getNewFileName());
		
		return getSimpleJdbcTemplate().update(sql, param);
		
	}
	
	/**
	 * <p>업로드키로 파일정보 불러오기 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public FileUpload getFileInfo(String uploadKey) throws DataAccessException{
		FileUpload fileUpload = new FileUpload();
		Map<String, Object> resultMap = null;
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.fileupload.info");			
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("upload_key", uploadKey);
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			fileUpload.setUploadKey(uploadKey);
			fileUpload.setNewFileName(String.valueOf(resultMap.get("new_file_name")));
			fileUpload.setRealFileName(String.valueOf(resultMap.get("real_file_name")));
			
		}
		
		return fileUpload;
		
	}
	
	/**
	 * <p>대상자를 보여준다. 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public TargetList viewTargetList(int targetID) throws DataAccessException{
		TargetList targetList = new TargetList();
		Map<String, Object> resultMap = null;
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.view");			
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("targetID", new Integer(targetID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			targetList.setTargetID(Integer.parseInt(String.valueOf(resultMap.get("targetID"))));
			targetList.setTargetName((String)resultMap.get("targetName"));
			targetList.setDescription((String)resultMap.get("description"));
			targetList.setUserID((String)resultMap.get("userID"));
			targetList.setGroupID((String)resultMap.get("groupID"));
			targetList.setBookMark((String)resultMap.get("bookMark"));
			targetList.setShareType((String)resultMap.get("shareType"));
			targetList.setShareID((String)resultMap.get("shareID"));
			targetList.setTargetType((String)resultMap.get("targetType"));
			targetList.setTargetTable((String)resultMap.get("targetTable"));
			targetList.setUploadKey((String)resultMap.get("upload_key"));
			targetList.setDbID((String)resultMap.get("dbID"));
			targetList.setQueryText((String)resultMap.get("queryText"));
			targetList.setCountQuery((String)resultMap.get("countQuery"));
			targetList.setTargetCount(Integer.parseInt(String.valueOf(resultMap.get("targetCount"))));
			targetList.setDirectText((String)resultMap.get("directText"));
			targetList.setState((String)resultMap.get("state"));
			targetList.setStartDate(DateUtils.getStringDate(String.valueOf(resultMap.get("startDate"))));
			targetList.setEndDate(DateUtils.getStringDate((String.valueOf(resultMap.get("endDate")))));
			targetList.setSendedDate(DateUtils.getStringDate(String.valueOf(resultMap.get("sendedDate"))));
			targetList.setSendedDate(DateUtils.getStringDate(String.valueOf(resultMap.get("sendedDate"))));
			targetList.setSuccessYN((String)resultMap.get("successYN"));
			targetList.setOpenYN((String)resultMap.get("openYN"));
			targetList.setClickYN((String)resultMap.get("clickYN"));
			targetList.setRejectcallYN((String)resultMap.get("rejectcallYN"));
			targetList.setRegistDate(String.valueOf(resultMap.get("registDate")));
			targetList.setConnectedDbID(String.valueOf(resultMap.get("connectedDbID")));
			targetList.setMassmailGroupID(Integer.parseInt(String.valueOf(resultMap.get("massmailGroupID"))));
			targetList.setTargetGroupID(Integer.parseInt(String.valueOf(resultMap.get("targetGroupID"))));
			targetList.setRealFileName((String)resultMap.get("real_file_name"));

			
		}
		return targetList;
	}
	
	/**
	 * <p>파일정보를 삭제한다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteFileInfo(String uploadKey) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.fileupload.delete");
		
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("upload_key", uploadKey);
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
		
	
	/**
	 * <p>해당월에 대상자파일 테이블이 있는지 확인 
	 * @param yyyymm
	 * @return
	 */
	public List<Map<String, Object>> getFileImportTableIsExist(String tableName){
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.isexisttable");	
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
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.createtable");	
		sql = " CREATE TABLE "+ tableName +" "+ sql ;
		
		if(db_type.equals(DB_TYPE_ORACLE))
		{
			
			String sql_seq = "CREATE SEQUENCE "+ tableName + "_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1";
			String sql_idx = "CREATE INDEX "+ tableName+"_idx on " + tableName +"(targetID, col1, col2, col3)";
			String sqls[] = {sql_seq, sql+" CONSTRAINT  " + tableName+"_PK  PRIMARY KEY (fileImportID) )", sql_idx};
			int[] result = getSimpleJdbcTemplate().getJdbcOperations().batchUpdate(sqls);
			return result[2];
		}
		else if(db_type.equals(DB_TYPE_MYSQL))
		{
			return getSimpleJdbcTemplate().update(sql);
		}else if(db_type.equals(DB_TYPE_MSSQL)){
			String sql_idx = "CREATE INDEX "+ tableName+"_idx on " + tableName +"(targetID, col1, col2, col3) ON [PRIMARY]";
			String sqls[] = {sql, sql_idx};
			int[] result = getSimpleJdbcTemplate().getJdbcOperations().batchUpdate(sqls);
			return result[1];
			
		}
		return 0;
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
	 * <p>대상자 등록시 시작 변경 
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingStart(String state, String targetTable, String uploadKey, int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.updatestatetargetstart");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetTable", targetTable);
		param.put("upload_key", uploadKey);
		param.put("targetID",  new Integer(targetID));
		param.put("state", state);
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>대상자완료 상태 변경 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingEnd(String state, int targetCount, String queryText,  int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.updatestatetargetend");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("state", state);
		param.put("targetCount",  new Integer(targetCount));
		param.put("queryText", queryText);
		param.put("targetID",  new Integer(targetID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>대상자 추가 제외시 시작 변경 
	 * @param targetID
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingAddStart(String state, int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlistadd.updatestatetargetstart");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("state", state);
		param.put("targetID",  new Integer(targetID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>대상자 추가 제외시 완료 변경 (state=1(등록중), 2(등록중 에러발생), 3(등록완료), 총카운트 업데이트 
	 * @param targetID
	 * @param targetCount
	 * @param state
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetingAddEnd(String state, int targetCount, int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlistadd.updatestatetargetend");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("state", state);
		param.put("targetCount",  new Integer(targetCount));
		param.put("targetID",  new Integer(targetID));
		return getSimpleJdbcTemplate().update(sql,param);
	}

	/**
	 * <p>대상자 추가 제외 완료 후 targetCount 
	 * @param targetID
	 * @param targetTable
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetCount(String targetTable, int targetID) throws DataAccessException{
		String sql = "SELECT count(*) FROM "+targetTable +" WHERE targetID = "+targetID;
		return  getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>원투원정보를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<OneToOne> listOneToOne() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.selectonetoone");	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				OneToOne  onetoone = new OneToOne();
				onetoone.setOnetooneID(rs.getInt("onetooneID"));
				onetoone.setOnetooneName(rs.getString("onetooneName"));
				onetoone.setOnetooneAlias(rs.getString("onetooneAlias"));
							
				return onetoone;
			}			
		};		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	/**
	 * <p>Add 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */	
	@SuppressWarnings("unchecked")
	public List<OneToOne> listAddOneToOne(int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.selectaddonetoone");	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				OneToOne  onetoone = new OneToOne();
				onetoone.setOnetooneID(rs.getInt("onetooneID"));
				onetoone.setFieldDesc(rs.getString("fieldDesc"));
				onetoone.setFieldName(rs.getString("fieldName"));
				onetoone.setOnetooneName(rs.getString("onetooneName"));
				onetoone.setOnetooneAlias(rs.getString("onetooneAlias"));
							
				return onetoone;
			}			
		};		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", new Integer(targetID));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>타겟ID의 컬럼 위치에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<OnetooneTarget> getOnetoOneTargetByColumnPos(int targetID, int columnPos) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.selectColumnPosition");	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				OnetooneTarget onetooneTarget = new OnetooneTarget();
				onetooneTarget.setTargetID(rs.getInt("targetID"));
				onetooneTarget.setFieldName(rs.getString("fieldName"));
				onetooneTarget.setOnetooneID(rs.getInt("onetooneID"));
				onetooneTarget.setCsvColumnPos(rs.getInt("csvColumnPos"));
							
				return onetooneTarget;
			}			
		};		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", new Integer(targetID));
		param.put("csvColumnPos", new Integer(columnPos));
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
		
		
	}
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetList(TargetList targetList) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.insert");			
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
	 * <p>대상자그룹 추가/제외 등록 
	 * @param targetListAdd
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetListAdd(TargetListAdd targetList) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlistadd.insert");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", targetList.getTargetID());
		param.put("addType", targetList.getAddType());
		param.put("addTypeInput", targetList.getAddTypeInput());
		param.put("upload_key", targetList.getUploadKey());
		param.put("directText",		targetList.getDirectText());
	
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>대상자그룹수정 - 파일 
	 * @param targetList
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListFile(TargetList targetList) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.updatefile");			
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetID",	targetList.getTargetID());
		param.put("targetName",	targetList.getTargetName());
		param.put("description", 	targetList.getDescription());
		param.put("shareType",		targetList.getShareType());
		param.put("shareID",			targetList.getShareID());
		param.put("targetType",	targetList.getTargetType());
		param.put("targetGroupID",targetList.getTargetGroupID());
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>직접입력 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListDirect(TargetList targetList) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.updatedirect");			
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetID",	targetList.getTargetID());
		param.put("targetName",	targetList.getTargetName());
		param.put("description", 	targetList.getDescription());
		param.put("shareType",		targetList.getShareType());
		param.put("shareID",			targetList.getShareID());
		param.put("targetType",	targetList.getTargetType());
		param.put("directText",		targetList.getDirectText());
		param.put("targetGroupID",targetList.getTargetGroupID());
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	/**
	 * <p>쿼리(DB추출) 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListQuery(TargetList targetList) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.updatequery");			
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetID",	targetList.getTargetID());
		param.put("targetName",	targetList.getTargetName());
		param.put("description", 	targetList.getDescription());
		param.put("shareType",		targetList.getShareType());
		param.put("shareID",			targetList.getShareID());
		param.put("targetType",	targetList.getTargetType());
		param.put("dbID",				targetList.getDbID());
		param.put("queryText",		targetList.getQueryText());
		param.put("countQuery",	targetList.getCountQuery());
		param.put("targetCount",	targetList.getTargetCount());
		param.put("targetGroupID",targetList.getTargetGroupID());
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>기존발송 추출 대상자를 수정한다. 
	 * @param targeting
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTargetListSended(TargetList targetList) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.updatesended");	
		
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetID",	targetList.getTargetID());
		param.put("targetName",	targetList.getTargetName());
		param.put("description", 	targetList.getDescription());
		param.put("shareType",		targetList.getShareType());
		param.put("shareID",			targetList.getShareID());
		param.put("targetType",	targetList.getTargetType());
		param.put("dbID",				targetList.getDbID());
		param.put("queryText",		targetList.getQueryText());
		param.put("countQuery",	targetList.getCountQuery());
		param.put("targetCount",	targetList.getTargetCount());
		param.put("sendedDate",targetList.getSendedDate());
		param.put("successYN",targetList.getSuccessYN());
		param.put("openYN",targetList.getOpenYN());
		param.put("clickYN",targetList.getClickYN());
		param.put("connectedDbID",targetList.getConnectedDbID());
		param.put("massmailGroupID",targetList.getMassmailGroupID());
		param.put("targetGroupID",targetList.getTargetGroupID());
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>ez_target_list에서 max의 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.getmaxtargetid");			
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(OnetooneTarget onetooneTarget) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.insertonetoonetarget");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", onetooneTarget.getTargetID());
		param.put("fieldName", onetooneTarget.getFieldName());
		param.put("onetooneID", onetooneTarget.getOnetooneID());
		param.put("fieldDesc", onetooneTarget.getFieldDesc());
		param.put("csvColumnPos", onetooneTarget.getCsvColumnPos());
		
		
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>dbID에 해당하는 db정보를 가져온다.
	 * @param queryText
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> getDBInfo(String dbID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.getdbinfo");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID",	dbID);
		return getSimpleJdbcTemplate().queryForMap(sql, param); 
	}
	
	/**
	 * <p>타겟ID에 해당되는 원투원정보를 불러온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<OnetooneTarget> viewOnetooneTarget(int targetID) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.viewonetoonetarget");	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				OnetooneTarget onetooneTarget = new OnetooneTarget();
				onetooneTarget.setTargetID(rs.getInt("targetID"));
				onetooneTarget.setFieldName(rs.getString("fieldName"));
				onetooneTarget.setOnetooneID(rs.getInt("onetooneID"));
				onetooneTarget.setCsvColumnPos(rs.getInt("csvColumnPos"));
				onetooneTarget.setFieldDesc(rs.getString("fieldDesc"));
							
				return onetooneTarget;
			}			
		};		
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", new Integer(targetID));

		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>즐겨찾기표시 업데이트 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] updateBookMark(Map<String, Object>[] maps) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("targetlist_sql","target.targetlist.updatebookmark"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}
	
	/**
	 * <p>대상자에 대한 원투원정보를 삭제한다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteOnetooneTarget(int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.deleteonetoonetarget");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", new Integer(targetID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	/**
	 * <p>대상자에 파일인서트 DB 삭제 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteFileImport(int targetID, String tableName) throws DataAccessException{
		String sql = "DELETE FROM "+tableName+" WHERE targetID="+targetID+" and addYN='N'";
		return getSimpleJdbcTemplate().update(sql);
	}

	/**
	 * <p>대상자그룹 삭제처리 - 실제삭제하진 않고 bookMark='D'로 변경한다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteTargetList(int targetID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.deletetarget");			
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", new Integer(targetID));
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	
	public TargetList getTargetState(String target_id)  throws DataAccessException{
		
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.selecttargetState");		
		Map<String, Object> resultMap = null;
		TargetList targetList = new TargetList();
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", new Integer(target_id));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			targetList.setState(String.valueOf(resultMap.get("state")));
			targetList.setTargetCount(Integer.parseInt(String.valueOf(resultMap.get("targetCount"))));
			

			
		}
		return targetList;
	}
	
	/**
	 * <p>타겟 리스트 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TargetListAdd> addHistoryList(int targetID)  throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlistadd.select");	
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {		
				TargetListAdd targetList = new TargetListAdd();
				targetList.setTargetID(rs.getInt("targetID"));
				targetList.setTargetAddID(rs.getInt("targetAddID"));
				targetList.setAddType(rs.getString("addType"));
				targetList.setAddTypeInput(rs.getString("addTypeInput"));
				targetList.setDirectText(rs.getString("directText"));
				targetList.setUploadKey(rs.getString("upload_key"));
				targetList.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				targetList.setRealFileName(rs.getString("real_file_name"));
				
				return targetList;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", new Integer(targetID));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>추가 / 제외 이력 - 직접입력
	 * @param targetAddID
	 * @return
	 */
	public String getDirectText(int targetAddID) throws DataAccessException{
		String directText ="";
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlistadd.getdirecttext");		
		Map<String, Object> resultMap = null;
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetAddID", new Integer(targetAddID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			directText = String.valueOf(resultMap.get("directText"));
		}
		return directText;
	}
	
	
	/**
	 * <p>대상자 미리보기창 카운트 가져오기 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetPreviewListTotalCount(String sql)  throws DataAccessException{
				
		return  getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	
	/**
	 * <p>대상자 미리보기창 리스트 가져오기
	 * @param sql, onetoFieldName[]
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<String[]> getTargetPreviewList(String sql, String[] onetoFieldName)  throws DataAccessException
	{	
		final String[] col = onetoFieldName;
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {		
				String oneToOneInfo[] = new String[col.length+1];	
				oneToOneInfo[0] = rs.getString("fileImportID");
				for(int i = 0; i<col.length; i++)
				{	if(db_type.equals(DB_TYPE_ORACLE))
						oneToOneInfo[i+1] = rs.getString(col[i].toUpperCase())==null?"":rs.getString(col[i].toUpperCase());
					else
						oneToOneInfo[i+1] = rs.getString(col[i]);
				}
				return oneToOneInfo;
			}
		};
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	

	/**
	 * <p>고객연동디비 컬럼정보
	 * @param dbID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DbConnectColumn> listConnectDBColumn(String dbID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("admin_sql","admin.dbconnect.selectcolumn");		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				DbConnectColumn dbConnectColumn = new DbConnectColumn();
				dbConnectColumn.setColumnName(rs.getString("columnName"));
				
				return dbConnectColumn;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID",dbID);
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * 대상자DB tm_fileImport에서 해당 아이디의 이메일 컬럼명을 가져온다 
	 * - 대상자 제외 기능에서 이메일 컬럼 기준으로 delete하려고 사용
	 * @param targetID
	 * @return String colName
	 * @throws DataAccessException
	 */
	public String getEmailFieldName(int targetID) throws DataAccessException{
		String colName ="";
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.getemailfieldname");		
		Map<String, Object> resultMap = null;
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", new Integer(targetID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			colName = String.valueOf(resultMap.get("fieldName"))+","+String.valueOf(resultMap.get("csvColumnPos"));
		}
		return colName;
	}
	
	
	
	/**
	 * <p>대상자 미리보기에서 이메일 삭제
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] deletePreviewList(Map<String, Object>[] maps, String tableName) throws DataAccessException{
		String sql =  "delete from " + tableName + " " + QueryUtil.getStringQuery("targetlist_sql","target.targetlist.deletepreviewlist"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}
	
	/**
	 * <p>대상자 미리보기에서 이메일 수정
	 * @param maps
	 * @return
	 */
	public int[] updatePreviewList(Map<String, Object>[] maps, String tableName, List<OneToOne> onetooneTargetList) throws DataAccessException{
		String sql =  "update " + tableName + " set " ;	
		String sql_set = "";
		for(int i = 0;i<onetooneTargetList.size();i++)
		{
			OneToOne oneToOne = onetooneTargetList.get(i);
			if(i==0)
				sql_set = sql_set + oneToOne.getFieldName() + "= :" + oneToOne.getFieldName();
			else
				sql_set = sql_set + ", "+ oneToOne.getFieldName() + "= :" + oneToOne.getFieldName();
			
		}
		sql = sql + sql_set + " where fileImportID=:fileImportID";
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}
	
	/**
	 * 이메일,휴대폰,고객명으로 대상자 리스트 검색
	 * @param maps
	 * @return
	 */
	public List<Map<String, Object>> getTargetIDsBySearch(Map<String, String> map, String tableName) throws DataAccessException{
		String searchType = (String)map.get("searchType");
		String searchText = (String)map.get("searchText");
		String targetID = (String)map.get("targetID");
		String sql = "select targetID from " + tableName + " where " + searchType + " like '%" + searchText + "%' and targetID = "+targetID+" group by targetID";
		return getSimpleJdbcTemplate().queryForList(sql);
	}
	
		
	/**
	 * <p>이메일, 고객명, 휴대폰 필드명 리스트 
	 * @return
	 */
	public List<Map<String, Object>> getFieldList(String type) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetlist.getfieldlist");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("onetooneID",type);
		return getSimpleJdbcTemplate().queryForList(sql, param);
	}
	
	/**
	 * <p>대상자 미리보기에서 폼 타입으로 저장
	 * @param targetID
	 * @param tableName
	 * @param addFormTypeText
	 * @author 김용연
	 * @return
	 */
	public int insertPreviewFormType(int targetID, String tableName, String[] addFormTypeText) throws DataAccessException{
		String sql =  "INSERT INTO " + tableName + " ( targetID, addYN";
		String sql_set1 = "";
		String sql_set2 = "";

		for(int i = 0; i < addFormTypeText.length ; i++){			
			sql_set1 = sql_set1 + ", col"+(i+1);			
		}
		
		sql = sql + sql_set1 + ") VALUES( "+targetID+",'Y'";
		
		for(int i = 0; i < addFormTypeText.length ; i++){
			sql_set2 = sql_set2 + ", '"+addFormTypeText[i]+"' ";
		}
		
		sql = sql + sql_set2 + " )";

		return  getSimpleJdbcTemplate().update(sql);
	}
	
	
	/**
	 * <p>대상자 미리보기에서 폼 별 추가시 tm_target_list 테이블 targetCount(총인원수) 증가
	 * @param targetID
	 * @author 김용연
	 * @throws DataAccessException
	 * @return
	 */
	public int updateFormTypeTargetCount(int targetID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql", "target.targetlist.updateFormTypeTargetCount");
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("targetID", targetID);
		
		return getSimpleJdbcTemplate().update(sql, param);
		
	}
	
	public String getDbAccessKey(String targetID, String dbID) throws DataAccessException {
		
		Map<String, Object> resultMap = null;
		String result = "";
		String sql = "";
		
		if(targetID.equals("0")) {
			sql = QueryUtil.getStringQuery("admin_sql", "admin.dbjdbcset.viewdbAccessKey");
		} else {
			sql = QueryUtil.getStringQuery("targetlist_sql", "target.targetlist.viewdbAccessKey");
		}
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", targetID);
		param.put("dbID", dbID);
		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
		
		if(resultMap!=null){
			result = (String)resultMap.get("dbAccessKey");
		}
		
		return result;
	}

}
