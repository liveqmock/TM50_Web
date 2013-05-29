package web.target.targetui.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.common.dao.DBJdbcDaoSupport;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import web.target.targetui.model.OnetooneTarget;
import web.target.targetui.model.TargetUIGeneralInfo;
import web.target.targetui.model.TargetUIList;
import web.target.targetui.model.TargetUIOneToOne;
import web.target.targetui.model.TargetUIWhere;
import web.target.targetui.model.TargetList;

public class TargetUIDAOImpl extends DBJdbcDaoSupport implements TargetUIDAO {
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	
	
	/**
	 * 회원정보UI 리스트
	 */
	@SuppressWarnings("unchecked")
	public List<TargetUIList> listTargetUI() throws DataAccessException{
		
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.select");		
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetUIList targetUIList = new TargetUIList();
				targetUIList.setTargetUIManagerID(rs.getInt("targetUIManagerID"));
				targetUIList.setTargetUIManagerName(rs.getString("targetUIMAnagerName"));
				targetUIList.setDbID(rs.getString("dbID"));
				targetUIList.setDbName(rs.getString("dbName"));
				targetUIList.setDescription(rs.getString("description"));
				targetUIList.setDefaultYN(rs.getString("defaultYN"));
				
				return targetUIList;
			}
		};
		
			
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	
	/**
	 * 기본 회원정보UI 
	 * @param 
	 * @return
	 * @throws DataAccessException
	 */
	public int getDefaultTargetUIID() throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.getdefaulttargetui");	
		
		return  getSimpleJdbcTemplate().queryForInt(sql);
		
	}
	
	/**
	 * 회원정보UI 기본정보 불러오기 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public TargetUIList viewTargetUI(int id) throws DataAccessException{
		TargetUIList targetUIList = new TargetUIList();
		
		Map<String, Object> resultMap = null;
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.view");			
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("targetUIManagerID", new Integer(id));
		
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){}
		
		if(resultMap!=null){
			targetUIList.setTargetUIManagerID(Integer.parseInt(String.valueOf(resultMap.get("targetUIManagerID"))));
			targetUIList.setTargetUIManagerName((String)resultMap.get("targetUIManagerName"));
			targetUIList.setDbID((String)resultMap.get("dbID"));
			targetUIList.setDescription((String)resultMap.get("description"));
			targetUIList.setSelectText((String)resultMap.get("selectText"));
			targetUIList.setFromText((String)resultMap.get("fromText"));
			targetUIList.setWhereText((String)resultMap.get("whereText"));
		}
		
		return targetUIList;
		
	}
	
	/**
	 * 회원정보UI Where절 정보 불러오기
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TargetUIWhere> getMakeWhere(int id) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.viewmakewhere");	
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetUIWhere targetUIWhere = new TargetUIWhere();
				targetUIWhere.setTargetUIManagerID(rs.getInt("targetUIManagerID"));
				targetUIWhere.setWhereID(rs.getInt("whereID"));
				targetUIWhere.setWhereUIName(rs.getString("whereUIName"));
				targetUIWhere.setWhereFieldName(rs.getString("whereFieldName"));
				targetUIWhere.setWhereType(rs.getInt("whereType"));
				targetUIWhere.setDataType(rs.getString("dataType"));
				targetUIWhere.setExceptYN(rs.getString("exceptYN"));
				targetUIWhere.setCheckName(rs.getString("checkName"));
				targetUIWhere.setCheckValue(rs.getString("checkValue"));
				targetUIWhere.setPeriodStartType(rs.getString("periodStartType"));
				targetUIWhere.setPeriodEndType(rs.getString("periodEndType"));
				targetUIWhere.setDescription((rs.getString("description")==null)?"":rs.getString("description"));
				
				return targetUIWhere;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetUIManagerID", new Integer(id));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * tm_target_ui_select 에서 원투원 정보 가져오기
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TargetUIOneToOne> getSelectOneToOne(int id) throws DataAccessException{
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.getSelectOneToOne");	
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetUIOneToOne targetUIOneToOne = new TargetUIOneToOne();
				targetUIOneToOne.setTargetUIManagerID(rs.getInt("targetUIManagerID"));
				targetUIOneToOne.setSelectID(rs.getInt("selectID"));
				targetUIOneToOne.setSelectFieldName(rs.getString("selectFieldName"));
				targetUIOneToOne.setOnetooneID(rs.getInt("onetooneID"));
				targetUIOneToOne.setSelectDescription(rs.getString("selectDescription"));
				targetUIOneToOne.setCsvColumnPos(rs.getInt("csvColumnPos"));
				
				return targetUIOneToOne;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("targetUIManagerID", new Integer(id));
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>대상자그룹등록 
	 * @param targetList
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetList(TargetList targetList) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.insert");			
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
		param.put("targetUIID",targetList.getTargetUIID());
		return getSimpleJdbcTemplate().update(sql,param);
	}
	
	public int updateTargetList(TargetList targetList) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.update");			
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
	 * <p>ez_target_list에서 max의 targetID를 가져온다. 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxTargetID() throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.getmaxtargetid");			
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>대상자에 대한 원투원정보를 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 * @throws DataAccessException
	 */
	public int insertOnetooneTarget(OnetooneTarget onetooneTarget) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.insertonetoonetarget");			
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
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.getdbinfo");				
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("dbID",	dbID);
		return getSimpleJdbcTemplate().queryForMap(sql, param); 
	}
	
	/**
	 * 회원정보UI 입력값을 저장한다 
	 * @param targetUIGeneralInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTargetUIGeneralInfo(int targetUIManagerID, TargetUIGeneralInfo targetUIGeneralInfo) throws DataAccessException
	{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.insertgeneralinfo");
		sql = "insert into tm_target_ui_general_"+targetUIManagerID+sql;
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("targetID", targetUIGeneralInfo.getTargetID());
		param.put("whereID", targetUIGeneralInfo.getWhereID());
		param.put("whereType", targetUIGeneralInfo.getWhereType());
		param.put("checkedItems", targetUIGeneralInfo.getCheckedItems());
		param.put("periodStartValue", targetUIGeneralInfo.getPeriodStartValue());
		param.put("periodEndValue", targetUIGeneralInfo.getPeriodEndValue());
		param.put("inputValue", targetUIGeneralInfo.getInputValue());
		
		return getSimpleJdbcTemplate().update(sql,param);
		
	}
	
	/**
	 * targetID로 TargetManagerID를 가져온다
	 * @return
	 * @throws DataAccessException
	 */
	public int getTargetManagerID(int targetID) throws DataAccessException
	{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.gettargetmanagerid");
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID",targetID);

		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
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
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.viewtargetinfo");			
		
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
			targetList.setTargetGroupID(Integer.parseInt(String.valueOf(resultMap.get("targetGroupID"))));
			
		}
		return targetList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<TargetUIGeneralInfo> viewTargetUIGeneralInfo(int targetID, int targetUIManagerID) throws DataAccessException{
		
		
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.gettargetgeneralinfo");	
		sql += " tm_target_ui_general_" + targetUIManagerID + " where targetID="+targetID; 
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				TargetUIGeneralInfo targetUIGeneralInfo = new TargetUIGeneralInfo();
				
				targetUIGeneralInfo.setWhereID(rs.getInt("whereID"));
				targetUIGeneralInfo.setCheckedItems(rs.getString("checkedItems"));
				targetUIGeneralInfo.setPeriodStartValue(rs.getString("periodStartValue"));
				targetUIGeneralInfo.setPeriodEndValue(rs.getString("periodEndValue"));
				targetUIGeneralInfo.setInputValue(rs.getString("inputValue"));
				
				
				return targetUIGeneralInfo;
			}
		};
		return  getSimpleJdbcTemplate().query(sql, rowMapper);
	}
	
	public int deleteTargetUIGeneralInfo(int targetID, int targetUIManagerID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("targetlist_sql","target.targetui.deletetargetgeneralinfo");
		sql = "delete from tm_target_ui_general_"+targetUIManagerID + " " + sql;
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", new Integer(targetID));
		return getSimpleJdbcTemplate().update(sql,param);
		
	}
	
}
