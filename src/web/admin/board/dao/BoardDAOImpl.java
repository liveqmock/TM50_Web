package web.admin.board.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.admin.board.model.*;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.DateUtils;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import web.common.model.FileUpload;

/**
 * <p>게시판에 등록/수정/삭제/조회등을 수행하는 DAO
 *      
 * @author coolang
 * @date 2007-10-10
 */

public class BoardDAOImpl extends DBJdbcDaoSupport  implements BoardDAO {
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	private static final String DB_TYPE_MYSQL = "mysql";

	/**
	 * <p>공지사항 리스트를 출력한다. 
	 */
	@SuppressWarnings("unchecked")
	public List<Board> listBoard(int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException{
		
		int start = (currentPage - 1) * countPerPage;
		countPerPage = countPerPage * currentPage;
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.board.select");

		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " WHERE "+sSearchType+" LIKE :seach_text ";
		}		
			
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.board.tail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Board  board = new Board();
				board.setBoardID(rs.getInt("boardID"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setUserID(rs.getString("userID"));
				board.setPriorNum(rs.getInt("priorNum"));
				board.setFileName(rs.getString("fileName"));
				board.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				board.setHit(rs.getInt("hit"));
				board.setUserName(rs.getString("userName"));
				
					
				return board;
			}			
		};
			
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("seach_text", "%"+sSearchText+"%");
		
		
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);

	}
	
	/**
	 * <p>공지사항 리스트를 출력한다. 
	 */
	@SuppressWarnings("unchecked")
	public List<Board> listBoard_user(int currentPage, int countPerPage, Map<String, String> searchMap, String groupID) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		countPerPage = countPerPage * currentPage;
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
	
		String sql = QueryUtil.getStringQuery("admin_sql","admin.board.select_user");		
		
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " AND "+sSearchType+" LIKE :seach_text ";
		}		
			
		String sqlTail = QueryUtil.getStringQuery("admin_sql","admin.board.tail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))	
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage );
				
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Board  board = new Board();
				board.setBoardID(rs.getInt("boardID"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setUserID(rs.getString("userID"));
				board.setPriorNum(rs.getInt("priorNum"));
				board.setFileName(rs.getString("fileName"));
				board.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				board.setHit(rs.getInt("hit"));
				board.setUserName(rs.getString("userName"));
				
					
				return board;
			}			
		};
			
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("start", new Integer(start));
		param.put("count", new Integer(countPerPage));
		param.put("seach_text", "%"+sSearchText+"%");
		param.put("groupID", groupID);
		
		
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);

	}
	
	@SuppressWarnings("unchecked")
	public List<Board> listShow(int groupID) throws DataAccessException{
		
	
		String sql = QueryUtil.getStringQuery("admin_sql","admin.board.select_show");			

					
				
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Board  board = new Board();
				board.setBoardID(rs.getInt("boardID"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setUserID(rs.getString("userID"));
				board.setPriorNum(rs.getInt("priorNum"));
				board.setFileName(rs.getString("fileName"));
				board.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));
				board.setHit(rs.getInt("hit"));
				board.setUserName(rs.getString("userName"));
				
					
				return board;
			}			
		};
			
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("groupID", groupID);
		
		
		
		
		return getSimpleJdbcTemplate().query(sql, rowMapper, param);

	}
	
	/**
	 * <p>공지사항을 저장한다.
	 * @param board
	 * @return
	 */
	public int insertBoard(Board board) throws DataAccessException{		
	
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.board.insert"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("title", board.getTitle());
		param.put("userID", board.getUserID());
		param.put("fileName", board.getFileName());
		param.put("content", board.getContent());
		param.put("priorNum", board.getPriorNum());
		param.put("upload_key", board.getUpload_key());
		param.put("readAuth", board.getReadAuth());
		
		
		
		return getSimpleJdbcTemplate().update(sql, param);
		
	}
	
	/**
	 *  <p> 공지사항을 수정한다.
	 * @param board
	 * @return  
	 */
	public int updateBoard(Board board) throws DataAccessException{
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.board.update"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다.

		Map<String,Object> param = new HashMap<String, Object>();
		param.put("title", board.getTitle());
		//param.put("userID", board.getUserID());
		param.put("fileName", board.getFileName());
		param.put("content", board.getContent());
		param.put("priorNum", board.getPriorNum());
		param.put("boardID", new Integer(board.getBoardID()));
		param.put("upload_key", board.getUpload_key());
		param.put("readAuth", board.getReadAuth());
		
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql,param);				
				
	}
	
	/*
	 * <p>공지사항의 읽은 카운트 증가 
	 * (non-Javadoc)
	 * @see bixon.web.admin.board.dao.BoardDAO#updateBoardHit(int)
	 */
	public int updateBoardHit(int boardID)  throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.board.updatehit"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("boardID", new Integer(boardID));
		
		//SQL문이 실행된다. 
		return  getSimpleJdbcTemplate().update(sql,param);				
	}
	
	
	/**
	 * <p> 공지사항을 삭제한다. 
	 */
	public int deleteBoard(int boardID) throws DataAccessException{		
		
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.board.delete"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("boardID", new Integer(boardID));
		return  getSimpleJdbcTemplate().update(sql,param);						
	}
	
	/**
	 * <p> 공지사항을 보여준다.
	 */
	
	public Board viewBoard(int boardID) throws  DataAccessException{
		Board board = new Board();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.board.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
			//넘겨받은 파라미터를 세팅한다. 
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("boardID", new Integer(boardID));
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql, param);
			}catch(EmptyResultDataAccessException e1){		
			}
			if(resultMap!=null){
				board.setBoardID(Integer.parseInt(String.valueOf(resultMap.get("boardID"))));
				board.setTitle((String)resultMap.get("title"));
				board.setUserID((String)resultMap.get("userID"));
				board.setUserName((String)resultMap.get("userName"));
				board.setContent((String)(resultMap.get("content")==null?"":resultMap.get("content")));
				board.setPriorNum(Integer.parseInt(String.valueOf(resultMap.get("priorNum"))));
				board.setFileName((String)resultMap.get("fileName"));
				board.setRegistDate(DateUtils.getStringDate(String.valueOf(resultMap.get("registDate"))));
				board.setUpload_key(String.valueOf(resultMap.get("upload_key")));
				board.setReadAuth((String)resultMap.get("readAuth"));
				board.setUserName((String)resultMap.get("userName"));
				
				
			}else{
				return board;
			}
		return board;
	}
	
	
	/**
	 * <p>공지사항을 총 갯수를 가져온다. 
	 */	
	public int getBoardTotalCount(Map<String, String> searchMap) throws DataAccessException{

		String searchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.board.totalcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " WHERE "+searchType+" LIKE :searchText ";
		}
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+sSearchText+"%");
		return  getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>공지사항을 총 갯수를 가져온다. 
	 */	
	public int getBoardTotalCount_user(Map<String, String> searchMap, String groupID) throws DataAccessException{

		String searchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.board.totalcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		sql += "   where readAuth = 3 or ( readAuth = 2 and u.groupID ='"+ groupID+"' ) ";
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " and "+searchType+" LIKE :searchText ";
		}
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("searchText", "%"+sSearchText+"%");
		return  getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	/**
	 * <p>업로드한 임시 파일을 저장한다.
	 * @param fileUpload
	 * @return
	 */
	public int insertFileUpload(FileUpload fileUpload) throws DataAccessException{		
	
		String sql =  QueryUtil.getStringQuery("admin_sql","admin.fileupload.insert"); 	
		
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
		
		String sql = QueryUtil.getStringQuery("admin_sql","admin.fileupload.info");			
		
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
	
	
}
