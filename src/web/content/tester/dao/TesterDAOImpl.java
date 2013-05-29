package web.content.tester.dao;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.*;

import org.springframework.dao.EmptyResultDataAccessException;
import web.content.tester.model.Tester;

public class TesterDAOImpl extends DBJdbcDaoSupport implements TesterDAO{

	
	/**
	 * <p>테스트리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Tester> listTester(Map<String, String> searchMap) throws DataAccessException{
		
		String sSearchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String userID = searchMap.get("userID");
		String isAdmin	= searchMap.get("isAdmin");
		int curPage = Integer.parseInt(searchMap.get("curPage"));
		int iLineCnt = Integer.parseInt(searchMap.get("iLineCnt"));
		int start = (curPage - 1) * iLineCnt;
		
		String sql = QueryUtil.getStringQuery("content_sql","content.tester.select");	
		
		if(!(isAdmin.equals("Y")))
		{
			sql += " AND t.userID='" + userID + "' ";
		}
		
		//검색조건이 있다면
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " AND  "+sSearchType+" LIKE :searchText ";
		}	
		
		
		
		String sqlTail = QueryUtil.getStringQuery("content_sql","content.tester.tail");			
		sql += sqlTail;
		
		
		
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {
			
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {
				Tester tester = new Tester();
				tester.setTesterID(rs.getInt("testerID"));
				tester.setTesterName(rs.getString("testerName"));
				tester.setTesterEmail(rs.getString("testerEmail"));
				tester.setTesterHp(rs.getString("testerHp"));
				tester.setUserID(rs.getString("userID"));
				tester.setRegistDate(rs.getString("registDate"));
				tester.setUserName(rs.getString("userName"));
				return tester;	
			}
		};
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(iLineCnt));
		param.put("searchText", "%"+sSearchText+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper,param);
		
		
	}
	
	
	/**
	 * <p>테스트 내용보기 
	 * @param email
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public Tester viewTester(int testerID) throws DataAccessException{
		
		Tester tester = new Tester();
		Map<String,Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("content_sql","content.tester.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("testerID", new Integer(testerID));		

		
		//SQL문이 실행된다.
		try {
			resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
		}catch(EmptyResultDataAccessException e1){		
		}
		if(resultMap!=null){
			tester.setTesterID(Integer.parseInt(String.valueOf(resultMap.get("testerID"))));
			tester.setTesterEmail((String)resultMap.get("testerEmail"));
			tester.setTesterName((String)resultMap.get("testerName"));
			tester.setTesterHp((String)resultMap.get("testerHp"));
			tester.setUserID((String)resultMap.get("userID"));
			tester.setRegistDate(DateUtils.getStringDate(String.valueOf(resultMap.get("registDate"))));			
		}else{
			return tester;
		}
		return tester;
		
	}
	
	
	
	/**
	 * <p>입력 
	 * @param tester
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTester(Tester tester) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.tester.insert"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("testerEmail", tester.getTesterEmail());
		param.put("testerName", tester.getTesterName());
		param.put("testerHp", tester.getTesterHp());
		param.put("userID", tester.getUserID());
		
		return getSimpleJdbcTemplate().update(sql,param);	
		
	}
	
	
	/**
	 * <p>수정
	 * @param tester
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTester(Tester tester) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.tester.update"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		//넘겨받은 파라미터를 세팅한다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("testerEmail", tester.getTesterEmail());
		param.put("testerName", tester.getTesterName());
		param.put("testerHp", tester.getTesterHp());
		param.put("testerID", tester.getTesterID());
		
		return getSimpleJdbcTemplate().update(sql,param);	
	}
	
	
	
	/**
	 * <p>삭제
	 * @param tester
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteTester(String[] testerIDs) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.tester.delete"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
	
		String sqlcols = "";
		for(int i=0;i<testerIDs.length;i++){
			if(i==testerIDs.length-1){
				sqlcols +=testerIDs[i]+")";
			}else{
				sqlcols+=testerIDs[i]+",";
			}
		}
		
		sql += sqlcols;		
	
		return getSimpleJdbcTemplate().update(sql);	
	}
	
	
	/**
	 * <p>유저당 30건만 등록가능 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkTesterByUserID(String userID) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.tester.check");
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("userID", userID);
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	public int getTesterTotalCount(Map<String, String> searchMap) throws DataAccessException{

		String searchType = searchMap.get("sSearchType");
		String sSearchText = searchMap.get("sSearchText");
		String userID = searchMap.get("userID");
		String isAdmin	= searchMap.get("isAdmin");
		
		String sql =  QueryUtil.getStringQuery("content_sql","content.tester.totalcount"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.
		
		if(!(isAdmin.equals("Y")))
		{
			sql += " AND userID='" + userID + "' ";
		}
		
		if(sSearchText!=null && !sSearchText.equals("")){
			sql += " AND "+searchType+" LIKE :searchText ";
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("searchText", "%"+sSearchText+"%");
		
		return  getSimpleJdbcTemplate().queryForInt(sql, param);
	}
}
