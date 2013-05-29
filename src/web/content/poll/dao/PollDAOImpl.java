package web.content.poll.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import web.common.dao.DBJdbcDaoSupport;
import web.common.util.Constant;
import web.common.util.DateUtils;
import web.common.util.LoginInfo;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import web.content.poll.model.*;

public class PollDAOImpl extends DBJdbcDaoSupport implements PollDAO{
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";
	private static final String DB_TYPE_MSSQL = "mssql";

	/**
	 * <p>설문리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollInfo> selectPollInfoList(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage ;
		countPerPage = countPerPage * currentPage;
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String userAuth = searchMap.get("userAuth");
		String useYN = searchMap.get("useYN");
		String selectedGroupID = searchMap.get("selectedGroupID");
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");
		String resultFinishYN = searchMap.get("resultFinishYN");
	
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollInfoList");			
		
		
		//소속관리자라면 
		if(userAuth.equals(Constant.USER_LEVEL_MASTER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectbygroup");		
		}
		//일반사용자라면 
		else if(userAuth.equals(Constant.USER_LEVEL_USER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectbyuser");	
		}		
			
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		if(useYN!=null && !useYN.equals("")){
			sql += " AND  p.useYN ='"+useYN+"' ";
		}
		
		if(resultFinishYN!=null && !resultFinishYN.equals("")){
			sql += " AND  p.resultFinishYN='"+resultFinishYN+"' ";
		}		
		
		if(selectedGroupID!=null && !selectedGroupID.equals("")){
			sql += " AND  p.shareGroupID ='"+selectedGroupID+"' ";
		}
			
		String sqlTail = QueryUtil.getStringQuery("content_sql","content.poll.tail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		
		
		//p.pollID, p.pollTitle, p.shareType, p.userID, u.userName, p.state, p.useYN, p.registDate 
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				PollInfo  pollInfo = new PollInfo();			
				pollInfo.setPollID(rs.getInt("pollID"));
				pollInfo.setPollTitle(rs.getString("pollTitle"));
				pollInfo.setShareGroupID(rs.getString("shareGroupID"));
				
				if(rs.getString("shareGroupID").equals("ALL")){
					pollInfo.setGroupName("전체그룹");
				}else if(rs.getString("shareGroupID").equals("NOT")){
					pollInfo.setGroupName("비공유");
				}else{
					pollInfo.setGroupName(rs.getString("groupName"));	
				}
				
				pollInfo.setUserID(rs.getString("userID"));
				pollInfo.setUserName(rs.getString("userName"));				
				pollInfo.setUseYN(rs.getString("useYN"));
				pollInfo.setResultFinishYN(rs.getString("resultFinishYN"));
				pollInfo.setCodeID(rs.getString("codeID"));
				pollInfo.setCodeNo(rs.getInt("codeNo"));
				pollInfo.setPollTemplateID(rs.getInt("pollTemplateID"));
				pollInfo.setPollEndDate(DateUtils.getStringDate(rs.getString("pollEndDate")));
				pollInfo.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				pollInfo.setPollEndType(rs.getString("pollEndType"));
				pollInfo.setAimAnswerCnt(rs.getString("aimAnswerCnt"));
				return pollInfo;
			}		
		};
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));		
		param.put("userID", userID);
		param.put("groupID", groupID);
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	
	/**
	 * <p>설문리스트 총카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollCount(Map<String, String> searchMap) throws DataAccessException{
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String userAuth = searchMap.get("userAuth");
		String useYN = searchMap.get("useYN");
		String selectedGroupID = searchMap.get("selectedGroupID");
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");
		String resultFinishYN = searchMap.get("resultFinishYN");
		
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollCount");		
		
		
		//소속관리자라면 
		if(userAuth.equals(Constant.USER_LEVEL_MASTER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectbygroup");		
		}
		//일반사용자라면 
		else if(userAuth.equals(Constant.USER_LEVEL_USER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectbyuser");	
		}		
			
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		if(useYN!=null && !useYN.equals("")){
			sql += " AND  p.useYN ='"+useYN+"' ";
		}
		
		if(resultFinishYN!=null && !resultFinishYN.equals("")){
			sql += " AND  p.resultFinishYN='"+resultFinishYN+"' ";
		}		
		
		if(selectedGroupID!=null && !selectedGroupID.equals("")){
			sql += " AND  p.shareGroupID ='"+selectedGroupID+"' ";
		}
		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		param.put("groupID", groupID);
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
	
	/**
	 * <p>설문기본정보 인서트 
	 * @param pollInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollInfo(PollInfo pollInfo, String type) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.insertPollInfo");		
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("pollTitle", pollInfo.getPollTitle());
		param.put("description", pollInfo.getDescription());
		param.put("shareGroupID", pollInfo.getShareGroupID());
		
		if(type.equals("insert")){
			param.put("userID", pollInfo.getUserID());
		}else{
			param.put("userID", type);
		}
				
		param.put("useYN", pollInfo.getUseYN());
		param.put("codeID", pollInfo.getCodeID());
		param.put("codeNo", pollInfo.getCodeNo());
		param.put("pollTemplateID", pollInfo.getPollTemplateID());	
		if(db_type.equals(DB_TYPE_ORACLE))	
			param.put("pollEndDate", pollInfo.getPollEndDate().length()>=19?pollInfo.getPollEndDate().substring(0, 19):pollInfo.getPollEndDate());
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))
			param.put("pollEndDate", pollInfo.getPollEndDate());
		param.put("pollEndType", pollInfo.getPollEndType());	
		param.put("aimanswercnt", pollInfo.getAimAnswerCnt());	
				
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	/**
	 * <p>설문 기본정보수정 
	 * @param pollInfo
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollInfo(PollInfo pollInfo) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.updatePollInfo");		
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("pollID", pollInfo.getPollID());
		param.put("pollTitle", pollInfo.getPollTitle());
		param.put("description", pollInfo.getDescription());
		param.put("shareGroupID", pollInfo.getShareGroupID());
		param.put("userID", pollInfo.getUserID());
		param.put("useYN", pollInfo.getUseYN());
		param.put("resultFinishYN", pollInfo.getResultFinishYN());
		param.put("codeID", pollInfo.getCodeID());
		param.put("codeNo", pollInfo.getCodeNo());		
		param.put("pollEndDate", pollInfo.getPollEndDate());
		param.put("pollEndType", pollInfo.getPollEndType());	
		param.put("aimanswercnt", pollInfo.getAimAnswerCnt());	
		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>설문문항입력
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollQuestion(PollQuestion pollQuestion) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.insertPollQuestion");		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("pollID", pollQuestion.getPollID());
		param.put("questionID", pollQuestion.getQuestionID());
		param.put("questionType", pollQuestion.getQuestionType());
		param.put("questionHead", pollQuestion.getQuestionHead());
		param.put("questionNo", pollQuestion.getQuestionNo());
		param.put("questionText", pollQuestion.getQuestionText());
		param.put("exampleType", pollQuestion.getExampleType());
		param.put("exampleGubun", pollQuestion.getExampleGubun());		
		param.put("exampleMultiCount", pollQuestion.getExampleMultiCount());	
		param.put("exampleMultiMinimumCount", pollQuestion.getExampleMultiMinimumCount());	
		param.put("requiredYN", pollQuestion.getRequiredYN());		
		param.put("examplePosition", pollQuestion.getExamplePosition());
		param.put("matrixTextSize", pollQuestion.getMatrixTextSize());
		param.put("fileURL", pollQuestion.getFileURL());
		param.put("layoutType", pollQuestion.getLayoutType());
		
		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>설문문항 수정
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollQuestion(PollQuestion pollQuestion) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.updatePollQuestion");		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("pollID", pollQuestion.getPollID());
		param.put("questionID", pollQuestion.getQuestionID());
		param.put("questionType", pollQuestion.getQuestionType());
		param.put("questionHead", pollQuestion.getQuestionHead());		
		param.put("questionText", pollQuestion.getQuestionText());
		param.put("exampleType", pollQuestion.getExampleType());
		param.put("exampleGubun", pollQuestion.getExampleGubun());
		param.put("exampleMultiCount", pollQuestion.getExampleMultiCount());
		param.put("exampleMultiMinimumCount", pollQuestion.getExampleMultiMinimumCount());	
		param.put("requiredYN", pollQuestion.getRequiredYN());		
		param.put("examplePosition", pollQuestion.getExamplePosition());
		param.put("matrixTextSize", pollQuestion.getMatrixTextSize());	
		param.put("fileURL", pollQuestion.getFileURL());
		param.put("layoutType", pollQuestion.getLayoutType());
		
		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	/**
	 * <p>코드값을 불러온다.
	 * @param codeID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollCode> selectPollCode(String codeID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollCode");		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				PollCode pollCode = new PollCode();
				pollCode.setCodeNo(rs.getInt("codeNo"));
				pollCode.setCodeName(rs.getString("codeName"));
				pollCode.setCodeDesc(rs.getString("codeDesc"));
				return pollCode;
			}
		};
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("codeID", codeID);		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>설문기본정보 보기 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public PollInfo viewPollInfo(int pollID) throws DataAccessException{
		PollInfo pollInfo = new PollInfo();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.viewPollInfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("pollID", new Integer(pollID));		
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				pollInfo.setPollID(Integer.parseInt(String.valueOf(resultMap.get("pollID"))));
				pollInfo.setPollTitle((String)resultMap.get("pollTitle"));
				pollInfo.setDescription((String)resultMap.get("description"));
				pollInfo.setUserID((String)resultMap.get("userID"));
				pollInfo.setUserName((String)resultMap.get("userName"));
				pollInfo.setGroupName((String)resultMap.get("groupName"));
				pollInfo.setShareGroupID((String)resultMap.get("shareGroupID"));
				pollInfo.setStartTitle((String)resultMap.get("startTitle"));
				pollInfo.setEndTitle((String)resultMap.get("endTitle"));
				pollInfo.setUseYN((String)resultMap.get("useYN"));
				pollInfo.setCodeID((String)resultMap.get("codeID"));
				pollInfo.setCodeNo(Integer.parseInt(String.valueOf(resultMap.get("codeNo"))));
				pollInfo.setResultPollHTML((String)resultMap.get("resultPollHTML"));
				pollInfo.setPollTemplateID(Integer.parseInt(String.valueOf(resultMap.get("pollTemplateID"))));
				pollInfo.setResultFinishYN((String)resultMap.get("resultFinishYN"));
				pollInfo.setPollEndDate(String.valueOf(resultMap.get("pollEndDate")));
				pollInfo.setPollEndDateHH(String.valueOf(resultMap.get("pollEndDateHH")));
				pollInfo.setPollEndDateMM(String.valueOf(resultMap.get("pollEndDateMM")));
				pollInfo.setRegistDate(String.valueOf(resultMap.get("registDate")));
				pollInfo.setPollEndType(String.valueOf(resultMap.get("pollEndType")));
				pollInfo.setAimAnswerCnt(String.valueOf(resultMap.get("aimanswercnt")));
			}	
			
			return pollInfo;
	}
	

	
	
	/**
	 * <p>최근 입력된 설문아이디 가져오기 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxPollID() throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.getMaxPollID"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>코드타입별로 가져오기 
	 * @param codeType
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollCode> selectPollCodeType(String codeType) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollCodeType");		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				PollCode pollCode = new PollCode();
				pollCode.setCodeID(rs.getString("codeID"));
				pollCode.setCodeName(rs.getString("codeName"));
				return pollCode;
			}
		};
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("codeID", codeType);		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);	
	}
	
	/**
	 * <p>최근 문항입력아이디 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxQuestionID(int pollID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.getMaxQuestionID"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		return getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	/**
	 * <p>문항보기 입력
	 * @param pollAnswer
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollExample(PollExample pollExample) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.insertPollExample"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//questionID, codeID, answerGubun, multiCount, answerDesc, goToQuestionNo
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", pollExample.getPollID());
		param.put("questionID", pollExample.getQuestionID());		
		param.put("exampleID", pollExample.getExampleID());
		if(db_type.equals(DB_TYPE_ORACLE))	
			param.put("matrixXY", pollExample.getMatrixXY().equals("")?" ":pollExample.getMatrixXY());
		else if(db_type.equals(DB_TYPE_MYSQL)||db_type.equals(DB_TYPE_MSSQL))	
			param.put("matrixXY", pollExample.getMatrixXY());
		param.put("exampleDesc", pollExample.getExampleDesc());		
		param.put("exampleExYN", pollExample.getExampleExYN());
		param.put("goToQuestionNo", pollExample.getGoToQuestionNo());	
		param.put("fileURL", pollExample.getFileURL());	
		param.put("layoutType", pollExample.getLayoutType());
		param.put("noResponseStart", pollExample.getNoResponseStart());
		param.put("noResponseEnd", pollExample.getNoResponseEnd());
		
		return getSimpleJdbcTemplate().update(sql, param);	
	}

	
	/**
	 * <p>설문템플릿 입력 
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int insertPollTemplate(PollTemplate pollTemplate) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.insertPollTemplate"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollTemplateTitle", pollTemplate.getPollTemplateTitle());
		param.put("pollTemplateHTML", pollTemplate.getPollTemplateHTML());
		param.put("userID", pollTemplate.getUserID());
		param.put("shareGroupID", pollTemplate.getShareGroupID());
		param.put("useYN", pollTemplate.getUseYN());
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>설문템플릿 수정 
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollTemplate(PollTemplate pollTemplate) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.updatePollTemplate"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollTemplateTitle", pollTemplate.getPollTemplateTitle());
		param.put("pollTemplateHTML", pollTemplate.getPollTemplateHTML());
		param.put("userID", pollTemplate.getUserID());
		param.put("shareGroupID", pollTemplate.getShareGroupID());
		param.put("useYN", pollTemplate.getUseYN());
		param.put("pollTemplateID", pollTemplate.getPollTemplateID());
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>설문템플릿 수정
	 * @param pollTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollTemplate(int pollTemplateID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.deletePollTemplate"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollTemplateID", new Integer(pollTemplateID));
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>설문템플릿리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollTemplate> selectPollTemplate(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage ;
		countPerPage = countPerPage * currentPage;
		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String userAuth = searchMap.get("userAuth");
		String useYN = searchMap.get("useYN");
		String selectedGroupID = searchMap.get("selectedGroupID");
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");		
	
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollTemplate");		
		
		//소속관리자라면 
		if(userAuth.equals(Constant.USER_LEVEL_MASTER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectTemplatebygroup");		
		}
		//일반사용자라면 
		else if(userAuth.equals(Constant.USER_LEVEL_USER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectTemplatebyuser");	
		}		
			
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		if(useYN!=null && !useYN.equals("")){
			sql += " AND  p.useYN ='"+useYN+"' ";
		}
		
		if(selectedGroupID!=null && !selectedGroupID.equals("")){
			sql += " AND  p.shareGroupID ='"+selectedGroupID+"' ";
		}
			
		String sqlTail = QueryUtil.getStringQuery("content_sql","content.poll.pollTemplateTail");			
		sql += sqlTail;		
	
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				PollTemplate pollTemplate = new PollTemplate();
				pollTemplate.setPollTemplateID(rs.getInt("pollTemplateID"));
				pollTemplate.setPollTemplateTitle(rs.getString("pollTemplateTitle"));			
				pollTemplate.setShareGroupID(rs.getString("shareGroupID"));
				pollTemplate.setUserID(rs.getString("userID"));
				pollTemplate.setUserName(rs.getString("userName"));
				pollTemplate.setUseYN(rs.getString("useYN"));
				if(rs.getString("shareGroupID").equals("ALL")){
					pollTemplate.setGroupName("전체공유");
				}else if(rs.getString("shareGroupID").equals("NOT")){
					pollTemplate.setGroupName("비공유");
				}else{
					pollTemplate.setGroupName(rs.getString("groupName"));
				}
				
				pollTemplate.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));				
				return pollTemplate;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("userID", userID);
		param.put("groupID", groupID);
		param.put("searchText", "%"+searchText+"%");
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>설문템플릿 총카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollTemplateCount(Map<String, String> searchMap) throws DataAccessException{

		String searchType = searchMap.get("searchType");
		String searchText = searchMap.get("searchText");
		String userAuth = searchMap.get("userAuth");
		String useYN = searchMap.get("useYN");
		String selectedGroupID = searchMap.get("selectedGroupID");
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");		
		
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollTemplateCount");		
		
		//소속관리자라면 
		if(userAuth.equals(Constant.USER_LEVEL_MASTER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectbygroup");		
		}
		//일반사용자라면 
		else if(userAuth.equals(Constant.USER_LEVEL_USER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectbyuser");	
		}		
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		if(useYN!=null && !useYN.equals("")){
			sql += " AND  p.useYN ='"+useYN+"' ";
		}
		
		if(selectedGroupID!=null && !selectedGroupID.equals("")){
			sql += " AND  p.shareGroupID ='"+selectedGroupID+"' ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		param.put("groupID", groupID);
		param.put("searchText", "%"+searchText+"%");
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
	
	/**
	 * <p>설문템플릿 보기 
	 * @param pollTemplateID
	 * @return
	 * @throws DataAccessException
	 */
	public PollTemplate viewPollTemplate(int pollTemplateID) throws DataAccessException{
		PollTemplate pollTemplate = new PollTemplate();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.viewPollTemplate"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("pollTemplateID", new Integer(pollTemplateID));		
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				
				pollTemplate.setPollTemplateID(Integer.parseInt(String.valueOf(resultMap.get("pollTemplateID"))));
				pollTemplate.setPollTemplateTitle((String)resultMap.get("pollTemplateTitle"));
				pollTemplate.setPollTemplateHTML((String)resultMap.get("pollTemplateHTML"));
				pollTemplate.setUserID((String)resultMap.get("userID"));				
				pollTemplate.setShareGroupID((String)resultMap.get("shareGroupID"));
				pollTemplate.setUseYN((String)resultMap.get("useYN"));
				pollTemplate.setGroupName((String)resultMap.get("groupName"));
				pollTemplate.setRegistDate(String.valueOf(resultMap.get("registDate")));
			}	
			
			return pollTemplate;
	}
	
	
	/**
	 * <p>설문템플릿리스트 사용자별 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollTemplate> selectPollTemplateByUsers(Map<String, String> searchMap) throws DataAccessException{
		String userAuth = searchMap.get("userAuth");
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");		
	
		String sql = "";	
		if(db_type.equals(DB_TYPE_MSSQL))
			sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollTemplate1");
		else
			sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollTemplate");
		//소속관리자라면 
		if(userAuth.equals(Constant.USER_LEVEL_MASTER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectTemplatebygroup");		
		}
		//일반사용자라면 
		else if(userAuth.equals(Constant.USER_LEVEL_USER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectTemplatebyuser");	
		}		

		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				PollTemplate pollTemplate = new PollTemplate();
				pollTemplate.setPollTemplateID(rs.getInt("pollTemplateID"));
				pollTemplate.setPollTemplateTitle(rs.getString("pollTemplateTitle"));
				pollTemplate.setPollTemplateHTML(rs.getString("pollTemplateHTML"));								
				return pollTemplate;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		param.put("groupID", groupID);
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>설문문항보기
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public PollQuestion viewPollQuestion(int pollID, int questionID) throws DataAccessException{
		PollQuestion pollQuestion = new PollQuestion();
		
			
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.viewPollQuestion"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("pollID", new Integer(pollID));
		param.put("questionID", new Integer(questionID));
			
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				pollQuestion.setPollID(Integer.parseInt(String.valueOf(resultMap.get("pollID"))));
				pollQuestion.setQuestionID(Integer.parseInt(String.valueOf(resultMap.get("questionID"))));
				pollQuestion.setQuestionNo(Integer.parseInt(String.valueOf(resultMap.get("questionNo"))));	
				pollQuestion.setQuestionType((String)resultMap.get("questionType"));
				pollQuestion.setQuestionHead((String)resultMap.get("questionHead"));				
				pollQuestion.setQuestionText((String)resultMap.get("questionText"));
				pollQuestion.setExampleType((String)resultMap.get("exampleType"));
				pollQuestion.setExampleGubun((String)resultMap.get("exampleGubun"));
				pollQuestion.setExampleMultiCount(Integer.parseInt(String.valueOf(resultMap.get("exampleMultiCount"))));
				pollQuestion.setExampleMultiMinimumCount(Integer.parseInt(String.valueOf(resultMap.get("exampleMultiMinimumCount"))));
				pollQuestion.setRequiredYN((String)resultMap.get("requiredYN"));			
				pollQuestion.setExamplePosition(Integer.parseInt(String.valueOf(resultMap.get("examplePosition"))));
				pollQuestion.setMatrixTextSize(Integer.parseInt(String.valueOf(resultMap.get("matrixTextSize"))));
				pollQuestion.setFileURL((String)resultMap.get("fileURL"));
				pollQuestion.setLayoutType((String)resultMap.get("layoutType"));
				
			}	
						
			return pollQuestion;
	
	}
	
	/**
	 * <p>현재 설문문항의 질문번호를 가져온다. 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Integer>> getPollQuestionNumber(int pollID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.getPollQuestionNumber");		
				
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Map<String, Integer> resultMap = new HashMap<String, Integer>();
				resultMap.put("questionID",rs.getInt("questionID"));
				resultMap.put("questionNo",rs.getInt("questionNo"));			
				
				return resultMap;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("pollID", new Integer(pollID));		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>설문HTML 업데이트 
	 * @param pollID
	 * @param resultPollHTML
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDefaultResultPollHTML(int pollID, String defaultPollHTML, String resultPollHTML) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.updateDefaultResultPollHTML");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("defaultPollHTML", defaultPollHTML);
		param.put("resultPollHTML", resultPollHTML);
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	
	/**
	 * <p>설문HTML 최종 업데이트 
	 * @param pollID
	 * @param resultPollHTML
	 * @return
	 * @throws DataAccessException
	 */
	public int updateResultPollHTMLToFinish(int pollID, String resultPollHTML) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.updateResultPollHTMLToFinish");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("resultPollHTML", resultPollHTML);
		return getSimpleJdbcTemplate().update(sql, param);		
	}

	/**
	 * <p>설문 초기화 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateResultPollHTMLToDefault(int pollID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.updateResultPollHTMLToDefault");
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));		
		return getSimpleJdbcTemplate().update(sql, param);		
	}
	
	/**
	 * <p>시작 문구 및 종료 문구 수정 
	 * @param gubun
	 * @param pollID
	 * @param title
	 * @return
	 * @throws DataAccessException
	 */
	public int updateStartEndTitle(String columnName, int pollID, String title)  throws DataAccessException{
		String sqlHead = "UPDATE tm_poll_info SET ";
		String sqlSet = columnName +"='"+title+"'";
		String sqlWhere = " WHERE pollID="+pollID;		
		String sql = sqlHead + sqlSet + sqlWhere;
		return getSimpleJdbcTemplate().update(sql);		
	}
	
	
	
	/**
	 * <p>시작/종료 문구 보기 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public PollInfo viewStartEndTitle(int pollID) throws DataAccessException{
		PollInfo pollInfo = new PollInfo();
		String startTitle ="";
		String endTitle="";
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.viewStartEndTitle"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("pollID", new Integer(pollID));		
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				startTitle = (String)resultMap.get("startTitle");
				startTitle = startTitle.replace("<br>", "\r\n");
				endTitle = (String)resultMap.get("endTitle");
				endTitle = endTitle.replace("<br>", "\r\n");
				pollInfo.setStartTitle(startTitle);
				pollInfo.setEndTitle(endTitle);				
			}	
			
			return pollInfo;
	}
	
	
	/**
	 * <p>템플릿 HTML을 가져온다. 
	 * @param pollTemplateID
	 * @return
	 * @throws DataAccessException
	 */
	public String showTemplateHTML(int pollTemplateID) throws DataAccessException{
		Map<String, Object> resultMap = null;
		String pollTemplateHTML="";
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.showTemplateHTML"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("pollTemplateID", new Integer(pollTemplateID));		
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){				
				pollTemplateHTML = ((String)resultMap.get("pollTemplateHTML"));
			}	
			
			return pollTemplateHTML;
	}
	
	/**
	 * <p>설문HTML을 불러온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String, Object> showResultDefaultPollHTML(int pollID) throws DataAccessException{
		Map<String, Object> resultMap = null;	
	
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.showResultDefaultPollHTML"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
	
		return resultMap;
	}
	
	/**
	 * <p>현재 설문이 사용한 적이 있는 지 확인 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPoll(int pollID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.checkPoll");
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("pollID", new Integer(pollID));
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	
	/**
	 * <p>설문보기 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollExample(int pollID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.deletePollExample"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//questionID, codeID, answerGubun, multiCount, answerDesc, goToQuestionNo
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		return getSimpleJdbcTemplate().update(sql, param);
	
	}
	
	
	/**
	 * <p>설문질문 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollQuestionAll(int pollID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.deletePollQuestionAll"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//questionID, codeID, answerGubun, multiCount, answerDesc, goToQuestionNo
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>해당 문항 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollQuestion(int pollID, int questionID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.deletePollQuestion"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//questionID, codeID, answerGubun, multiCount, answerDesc, goToQuestionNo
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("questionID", new Integer(questionID));
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>설문정보 삭제 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollInfo(int pollID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.deletePollInfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//questionID, codeID, answerGubun, multiCount, answerDesc, goToQuestionNo
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	
	/**
	 * <p>응답보기 
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollExample> selectPollExample(int pollID, int questionID, String questionType, String matrixXY) throws DataAccessException{
			
			String sql =  QueryUtil.getStringQuery("content_sql","content.poll.selectPollExample"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
			
			ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
				public Object mapRow(ResultSet rs, int rownum) throws SQLException {			
					PollExample pollExample = new PollExample(); 
					pollExample.setExampleID(rs.getInt("exampleID"));
					pollExample.setQuestionID(rs.getInt("questionID"));
					pollExample.setExampleDesc(rs.getString("exampleDesc"));
					pollExample.setExampleExYN(rs.getString("exampleExYN"));
					pollExample.setGoToQuestionNo(rs.getInt("goToQuestionNo"));
					pollExample.setFileURL(rs.getString("fileURL"));
					pollExample.setLayoutType(rs.getString("layoutType"));
					pollExample.setNoResponseStart(rs.getInt("noResponseStart"));
					pollExample.setNoResponseEnd(rs.getInt("noResponseEnd"));
					return pollExample;
				}			
			};		
				
			Map<String,Object> param = new HashMap<String, Object>();		
			param.put("pollID", new Integer(pollID));		
			param.put("questionID", new Integer(questionID));	
			param.put("questionType",questionType);
			param.put("matrixXY",matrixXY);
			
			
			return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
			
	}
	
	/**
	 * <p>설문 문항에 대한 보기 삭제 
	 * @param pollID
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteExampleQuestion(int pollID, int questionID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.deleteExampleQuestion"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		//questionID, codeID, answerGubun, multiCount, answerDesc, goToQuestionNo
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("questionID", new Integer(questionID));	
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>#설문번호삭제후 해당번호 이후 일괄업데이트 
	 * @param pollId
	 * @param questionNo
	 * @return
	 * @throws DataAccessException
	 */
	public int  updateQuestionNumAfterDelete(int pollID, int questionNo) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.updateQuestionNumAfterDelete"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("questionNo", new Integer(questionNo));	
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>#설문번호이동후 나머지 일괄 +1처리 
	 * @param pollId
	 * @param questionNo
	 * @return
	 * @throws DataAccessException
	 */
	public int  updateQuestionNumAfterAdd(int pollID, int questionNo) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.updateQuestionNumAfterAdd"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("questionNo", new Integer(questionNo));	
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>설문문항 갯수 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int getQuestionCount(int pollID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.getQuestionCount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>questionID에 해당되는 questionNo를 가져온다.
	 * @param questionNo
	 * @return
	 * @throws DataAccessException
	 */
	public Map<String,Object>  getQuestionIDByNo(int pollID, int questionNo)  throws DataAccessException{
		Map<String, Object> resultMap = null;	
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.getQuestionIDByNo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
				 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("questionNo", new Integer(questionNo));
		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){		
		}
	
		return resultMap;	
	}
	
	
	/**
	 * <p>설문번호 변경 
	 * @param chgGuestionNo
	 * @param questionID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateQuestionNo(int questionNo, int pollID, int questionID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.updateQuestionNo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("questionNo", new Integer(questionNo));
		param.put("pollID", new Integer(pollID));
		param.put("questionID", new Integer(questionID));
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>해당 설문번호 이후에 설문아이디를 가져온다.
	 * @param pollID
	 * @param questionNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Integer>> selectQuestionNo(int pollID, int questionNoFrom, int questionNoTo){
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selectQuestionNo");		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Map<String, Integer> resultMap = new HashMap<String, Integer>();
				resultMap.put("questionID",rs.getInt("questionID"));
				resultMap.put("questionNo",rs.getInt("questionNo"));
				return resultMap;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("pollID", new Integer(pollID));		
		param.put("questionNoFrom", new Integer(questionNoFrom));
		param.put("questionNoTo", new Integer(questionNoTo));
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>설문리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollInfo> selectPollList(Map<String, String> searchMap) throws DataAccessException{
		String userAuth = searchMap.get("userAuth");		
		String userID = searchMap.get("userID");
		String groupID = searchMap.get("groupID");
		String nowDate = searchMap.get("nowDate");
	
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollList");			
		
		
		//소속관리자라면 
		if(userAuth.equals(Constant.USER_LEVEL_MASTER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectbygroup");		
		}
		//일반사용자라면 
		else if(userAuth.equals(Constant.USER_LEVEL_USER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.poll.selectbyuser");	
		}
		
		String sqlTail = QueryUtil.getStringQuery("content_sql","content.poll.selectPollListTail");			
		sql += sqlTail;
		

		
		//p.pollID, p.pollTitle, p.userID, u.userName, p.registDate, p.resultPollHTML
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				PollInfo  pollInfo = new PollInfo();			
				pollInfo.setPollID(rs.getInt("pollID"));
				pollInfo.setPollTitle(rs.getString("pollTitle"));
				pollInfo.setUserID(rs.getString("userID"));
				pollInfo.setUserName(rs.getString("userName"));							
				pollInfo.setResultPollHTML(rs.getString("resultPollHTML"));
				pollInfo.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));		
				pollInfo.setPollEndDate(DateUtils.getStringDate(rs.getString("pollEndDate")));
				return pollInfo;
			}		
		};
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("userID", userID);
		param.put("groupID", groupID);
		param.put("nowDate", nowDate);
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);		
	}
	
	/**
	 * <p>해당 pollID와 massmailID가 있는지 확인 
	 * @param pollID
	 * @param massmailID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollMassMail(int pollID, int massmailID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.checkPollMassMail"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>설문문항 리스트 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Integer>> selQuestion(int pollID) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selQuestion");		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Map<String, Integer> resultMap = new HashMap<String, Integer>();
				resultMap.put("questionID",rs.getInt("questionID"));		
				resultMap.put("questionNo",rs.getInt("questionNo"));		
				return resultMap;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("pollID", new Integer(pollID));		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}

	
	
	/**
	 * <p>설문문항보기 리스트 
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> selExample(int pollID, int questionID, String matrixXY) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selExample");		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("matrixXY",rs.getString("matrixXY"));
				resultMap.put("exampleID",rs.getInt("exampleID"));			
				return resultMap;
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("pollID", new Integer(pollID));		
		param.put("questionID", new Integer(questionID));
		param.put("matrixXY", matrixXY);
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>설문응답넣기
	 * @param pollAnswer
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertPollAnswer(List<PollAnswer> pollAnswerList) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.insertPollAnswer"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(pollAnswerList.toArray());
		return getSimpleJdbcTemplate().batchUpdate(sql, params);
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
	public int checkPollAnswer(int pollID, int massmailID, int scheduleID, int sendID, String email) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.checkPollAnswer"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("sendID", new Integer(sendID));
		param.put("email", email);
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	
	
	/**
	 * <p>해당 sendID와 이메일이 존재하는지 체크 
	 * @param sendID
	 * @param email
	 * @return
	 * @throws DataAccessException
	 */
	public int checkExistMember(int massmailID, int scheduleID, int sendID, String email) throws DataAccessException{
		String sql =  "SELECT COUNT(*) FROM tm_massmail_sendresult_"+massmailID+"_"+scheduleID;
		sql+=" WHERE sendID="+sendID+" AND email='"+email+"'";
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * <p>통계수집만료기간을 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkStatisticExpiredMassmail(int massmailID, int scheduleID, String nowDate) throws DataAccessException{
				
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.checkStatisticExpiredMassmail"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("nowDate", nowDate);		
		
		return getSimpleJdbcTemplate().queryForInt(sql,param);
		
	}
	
	/**
	 * <p>설문이 사용중인지 체크한다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkUsingPoll(int pollID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.checkUsingPoll"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		
		return getSimpleJdbcTemplate().queryForInt(sql,param);
	}
	
	/**
	 * <p>설문코드리스트 를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PollCode> selectPollCodeList(Map<String, String> searchMap){

		String codeType = searchMap.get("codeType");
		String codeID = searchMap.get("codeID");		
				
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.selectPollCodeList");		
			
		String sqlWhere = "";
		
		if(codeType!=null && !codeType.equals("")){
			sqlWhere = " AND codeType='"+codeType+"'";
		}
		if(codeID!=null && !codeID.equals("")){
			sqlWhere += " AND codeID='"+codeID+"'";
		}
		
		String sqlTail = QueryUtil.getStringQuery("content_sql","content.poll.selectPollCodeListTail");			
		sql = sql + sqlWhere + sqlTail;
		
		//codeID, codeNo, codeName, codeDesc, useYN, codeType
	
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				PollCode  pollCode = new PollCode();
				pollCode.setCodeID(rs.getString("codeID"));
				pollCode.setCodeNo(rs.getInt("codeNo"));
				pollCode.setCodeName(rs.getString("codeName"));
				pollCode.setCodeDesc(rs.getString("codeDesc"));
				pollCode.setUseYN(rs.getString("useYN"));
				pollCode.setCodeType(rs.getString("codeType"));				
				if(pollCode.getCodeType().equals("1")){
					pollCode.setCodeTypeDesc("설문유형");
				}else{
					pollCode.setCodeTypeDesc("보기유형");
				}
			
				return pollCode;
			}		
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper);		
	}
	
	/**
	 * <p>설문코드 입력 
	 * @param pollCode
	 * @return
	 * @throws DataAccessException
	 */
	public int[] insertPollCode(List<PollCode> pollCodes) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.insertPollCode"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(pollCodes.toArray());		
		return getSimpleJdbcTemplate().batchUpdate(sql,params);
	}
	
	
	/**
	 * <p>설문코드 삭제 
	 * @param pollCode
	 * @return
	 * @throws DataAccessException
	 */
	public int deletePollCode(String codeID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.deletePollCode"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("codeID", codeID);		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	
	/**
	 * <p>설문코드 최대값 구하기 
	 * @return
	 * @throws DataAccessException
	 */
	public int getMaxPollCodeID() throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.getMaxPollCodeID"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		return getSimpleJdbcTemplate().queryForInt(sql);
	}

	/**
	 * <p>설문문항을 카피합니다. 
	 * @param oldPollID
	 * @param newPollID
	 * @return
	 * @throws DataAccessException
	 */
	public int insertCopyPollQuestion(int oldPollID, int newPollID)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.insertCopyPollQuestion"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("oldPollID", new Integer(oldPollID));
		param.put("newPollID", new Integer(newPollID));
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>설문문항을 카피합니다. 
	 * @param oldPollID
	 * @param newPollID
	 * @return
	 * @throws DataAccessException
	 */
	public int insertCopyPollExample(int oldPollID, int newPollID)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.insertCopyPollExample"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("oldPollID", new Integer(oldPollID));
		param.put("newPollID", new Integer(newPollID));
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>설문을 카피합니다. 
	 * @param oldPollID
	 * @param newPollID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateCopyPollInfo(int oldPollID, int newPollID, String type)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.updateCopyPollInfo"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("oldPollID", new Integer(oldPollID));
		param.put("newPollID", new Integer(newPollID));
		param.put("userID", type);
		return getSimpleJdbcTemplate().update(sql, param);
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
	public int deletePollAnswer(int pollID, int massmailID, int scheduleID, int sendID, String email) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.deletePollAnswer"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		param.put("sendID", new Integer(sendID));		
		param.put("email", email);
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>설문 통계수집만료기간을 가져온다.
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollEndDate(int pollID, String nowDate) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.checkPollEndDate"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));		
		param.put("nowDate", nowDate);				
		return getSimpleJdbcTemplate().queryForInt(sql,param);		
	}
	
	/**
	 * <p>설문 통계수집만료 목표응답 수를 가져온다.
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkPollAimAnswerCnt(int pollID, int aimAnswerCnt) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.checkPollAimAnswerCnt"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("aimAnswerCnt", new Integer(aimAnswerCnt));
		return getSimpleJdbcTemplate().queryForInt(sql,param);		
	}
	
	/**
	 * <p>해당 대량메일의 설문에 응답한 인원 수 출력.
	 * @param pollID
	 * @param massmailID
	 * @param scheduleID
	 * @return
	 * @throws DataAccessException
	 */
	public int selectPollResponseCnt(int pollID, int massmailID, int scheduleID){
		String sql = QueryUtil.getStringQuery("content_sql", "content.poll.selectPollResponseCnt");
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("massmailID", new Integer(massmailID));
		param.put("scheduleID", new Integer(scheduleID));
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>설문종료조건을 변경한다.(마감일, 목표응답 수)
	 * @param pollID
	 * @param nowDate
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollEndType(int pollID, String pollEndType, String pollEndDate, String aimAnswerCnt ) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.updatePollEndDate"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		param.put("pollEndType", pollEndType);
		if(db_type.equals(DB_TYPE_ORACLE))
			param.put("pollEndDate", pollEndDate.length()>=19?pollEndDate.substring(0, 19):pollEndDate);
		else if(db_type.equals(DB_TYPE_MYSQL))
			param.put("pollEndDate", pollEndDate);
		param.put("aimAnswerCnt", aimAnswerCnt);
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	/**
	 * <p>문항 불러오기 - 문항 정보를 받아온다.
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollQuestion> copyQuestionList(Map<String, Object> searchMap)throws DataAccessException{
		String questionText = (String)searchMap.get("questionText");
		
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.copyQuestionList"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("questionText", "%"+questionText+"%");
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				PollQuestion pollQuestion = new PollQuestion();
				pollQuestion.setPollID(rs.getInt("pollID"));
				pollQuestion.setQuestionID(rs.getInt("questionID"));
				pollQuestion.setQuestionNo(rs.getInt("questionNo"));
				pollQuestion.setQuestionType(rs.getString("questionType"));
				pollQuestion.setQuestionHead(rs.getString("questionHead"));
				pollQuestion.setQuestionText(rs.getString("questionText"));
				pollQuestion.setExampleType(rs.getString("exampleType"));
				pollQuestion.setExampleGubun(rs.getString("exampleGubun"));
				pollQuestion.setExampleMultiCount(rs.getInt("exampleMultiCount"));
				pollQuestion.setExampleMultiMinimumCount(rs.getInt("exampleMultiMinimumCount"));
				pollQuestion.setRequiredYN(rs.getString("requiredYN"));
				pollQuestion.setExamplePosition(rs.getInt("examplePosition"));
				pollQuestion.setFileURL(rs.getString("fileURL"));
				pollQuestion.setLayoutType(rs.getString("layoutType"));
				return pollQuestion;
			}
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>질문 문항 리스트
	 * @param pollID
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<PollQuestion> selSingularQuestion(int pollID)throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("content_sql","content.poll.selSingularQuestion"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("pollID", new Integer(pollID));
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				PollQuestion pollQuestion = new PollQuestion();
				pollQuestion.setQuestionID(rs.getInt("questionID"));
				pollQuestion.setQuestionNo(rs.getInt("questionNo"));
				pollQuestion.setQuestionText(rs.getString("questionText"));
				pollQuestion.setQuestionType(rs.getString("questionType"));
				pollQuestion.setExampleType(rs.getString("exampleType"));
				pollQuestion.setExampleGubun(rs.getString("exampleGubun"));
	
				return pollQuestion;
			}
		};
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	/**
	 * <p>설문문항 스킵패턴 설정
	 * @param pollQuestion
	 * @return
	 * @throws DataAccessException
	 */
	public int updatePollSkipPattern(PollExample pollExample) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.poll.updatePollSkipPattern");		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("pollID", pollExample.getPollID());
		param.put("questionID", pollExample.getQuestionID());
		param.put("exampleID", pollExample.getExampleID());
		param.put("goToQuestionNo", pollExample.getGoToQuestionNo());		
		param.put("noResponseStart", pollExample.getNoResponseStart());
		param.put("noResponseEnd", pollExample.getNoResponseEnd());
		
		return getSimpleJdbcTemplate().update(sql, param);	
	}
	
	/**
	 * <p>각 계정의 groupID 검색( 템플릿 사용 권한 비교 )
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public String selectGroupID(String userID) throws DataAccessException{
		Map<String,Object> resultMap = null;
		String result="";
		
		String sql = QueryUtil.getStringQuery("content_sql", "content.poll.selectGroupID");
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("userID",userID);
		
		try{
			resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
		}catch(EmptyResultDataAccessException e1){			
		}
		
		if(resultMap != null){
			result = (String)resultMap.get("groupID");
		}
		
		return result; 
	}
}
