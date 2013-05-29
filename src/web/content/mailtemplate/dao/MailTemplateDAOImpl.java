package web.content.mailtemplate.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.*;
import web.content.mailtemplate.model.MailTemplate;



public class MailTemplateDAOImpl extends DBJdbcDaoSupport implements MailTemplateDAO {
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	private static final String DB_TYPE_ORACLE = "oracle";
	//private static final String DB_TYPE_MYSQL = "mysql";


	/**
	 * <p>메일템플릿 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MailTemplate> listMailTemplate(String userID, String groupID,String userAuth, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException{
		int start = (currentPage - 1) * countPerPage;
		String searchType = searchMap.get("sSearchType");
		String searchText = searchMap.get("sSearchText");
		String useYN = searchMap.get("sUseYN");
		String selectedGroupID = searchMap.get("sSelectedGroupID");
		
		String sql = QueryUtil.getStringQuery("content_sql","content.mailtemplate.select");		
		
		//소속관리자라면 
		if(userAuth.equals(Constant.USER_LEVEL_MASTER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.mailtemplate.selectbygroup");		
		}
		//일반사용자라면 
		else if(userAuth.equals(Constant.USER_LEVEL_USER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.mailtemplate.selectbyuser");	
		}			
		
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}
		
		if(useYN!=null && !useYN.equals("")){
			sql += " AND  t.useYN ='"+useYN+"' ";
		}
		
		if(selectedGroupID!=null && !selectedGroupID.equals("")){
			sql += " AND  shareGroupID ='"+selectedGroupID+"' ";
		}
			
		String sqlTail = QueryUtil.getStringQuery("content_sql","content.mailtemplate.tail");			
		sql += sqlTail;
		
		if(db_type.equals(DB_TYPE_ORACLE))
			sql = QueryUtil.getPagingQueryByOracle(sql, start, countPerPage*currentPage );
		
		ParameterizedRowMapper rowMapper = new ParameterizedRowMapper() {				
			public Object mapRow(ResultSet rs, int rownum) throws SQLException {				
				MailTemplate mailTemplate = new MailTemplate();
				mailTemplate.setTemplateID(rs.getInt("templateID"));
				mailTemplate.setTemplateName(rs.getString("templateName"));			
				mailTemplate.setShareGroupID(rs.getString("shareGroupID"));
				mailTemplate.setUserID(rs.getString("userID"));
				mailTemplate.setUserName(rs.getString("userName"));
				mailTemplate.setUseYN(rs.getString("useYN"));
				mailTemplate.setTemplateType(rs.getString("templateType"));
				if(rs.getString("shareGroupID").equals("ALL")){
					mailTemplate.setGroupName("전체공유");
				}else if(rs.getString("shareGroupID").equals("NOT")){
					mailTemplate.setGroupName("비공유");
				}else{
					mailTemplate.setGroupName(rs.getString("groupName"));
				}
				
				mailTemplate.setRegistDate(DateUtils.getStringDate(rs.getString("registDate")));				
				return mailTemplate;
			}			
		};		
	
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("start", new Integer(start));
		param.put("countPerPage", new Integer(countPerPage));
		param.put("userID", userID);
		param.put("groupID", groupID);
		param.put("searchText", "%"+searchText+"%");
		
		return  getSimpleJdbcTemplate().query(sql, rowMapper, param);
	}
	
	
	/**
	 * <p>메일템플릿  카운트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public int getCountMailTemplate(String userID, String groupID, String userAuth, Map<String, String> searchMap) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.mailtemplate.selectcount");		
		
		String searchType = searchMap.get("sSearchType");
		String searchText = searchMap.get("sSearchText");
		String useYN = searchMap.get("sUseYN");
		String selectedGroupID = searchMap.get("sSelectedGroupID");
		
		//소속관리자라면 
		if(userAuth.equals(Constant.USER_LEVEL_MASTER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.mailtemplate.selectbygroup");		
		}
		//일반사용자라면 
		else if(userAuth.equals(Constant.USER_LEVEL_USER)){
			sql+=QueryUtil.getStringQuery("content_sql","content.mailtemplate.selectbyuser");	
		}			
		
		//검색조건이 있다면
		if(searchText!=null && !searchText.equals("")){
			sql += " AND  "+searchType+" LIKE :searchText ";
		}		
		if(useYN!=null && !useYN.equals("")){
			sql += " AND  t.useYN ='"+useYN+"' ";
		}
		
		if(selectedGroupID!=null && !selectedGroupID.equals("")){
			sql += " AND  shareGroupID ='"+selectedGroupID+"' ";
		}

		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("userID", userID);
		param.put("groupID", groupID);
		param.put("searchText", "%"+searchText+"%");
		return getSimpleJdbcTemplate().queryForInt(sql, param);
		
	}
	
	/**
	 * <p>메일템플릿 입력 
	 * @param mailTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMailTemplate(MailTemplate mailTemplate) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.mailtemplate.insert");		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("templateName", mailTemplate.getTemplateName());
		param.put("templateContent", mailTemplate.getTemplateContent());
		param.put("shareGroupID", mailTemplate.getShareGroupID());
		param.put("userID", mailTemplate.getUserID());
		param.put("useYN", mailTemplate.getUseYN());
		param.put("templateType", mailTemplate.getTemplateType());	
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	
	/**
	 * <p>메일템플릿 수정 
	 * @param mailTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMailTemplate(MailTemplate mailTemplate) throws DataAccessException{
		String sql = QueryUtil.getStringQuery("content_sql","content.mailtemplate.update");		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("templateName", mailTemplate.getTemplateName());
		param.put("templateContent", mailTemplate.getTemplateContent());
		param.put("shareGroupID", mailTemplate.getShareGroupID());
		param.put("userID", mailTemplate.getUserID());
		param.put("useYN", mailTemplate.getUseYN());		
		param.put("templateID", mailTemplate.getTemplateID());
		param.put("templateType", mailTemplate.getTemplateType());	
		
		return getSimpleJdbcTemplate().update(sql, param);
	}
	
	
	/**
	 * <p>메일템플릿 삭제 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] deleteMailTemplate(Map<String, Object>[] maps) throws DataAccessException{
		String sql =  QueryUtil.getStringQuery("content_sql","content.mailtemplate.delete"); 	//쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다.		
		return getSimpleJdbcTemplate().batchUpdate(sql,maps);
	}
	
	
	/**
	 * <p>메일템플릿보기 
	 * @param templateID
	 * @return
	 * @throws DataAccessException
	 */
	public MailTemplate viewMailTemplate(int templateID) throws DataAccessException{
		MailTemplate mailTemplate = new MailTemplate();
		Map<String, Object> resultMap = null;
		String sql =  QueryUtil.getStringQuery("content_sql","content.mailtemplate.view"); //쿼리 프로퍼티파일의 키값에 해당되는 sql문을 읽어온다. 
		Map<String,Object> param = new HashMap<String, Object>();	
		param.put("templateID", new Integer(templateID));		
			
			//SQL문이 실행된다.
			try {
				resultMap = getSimpleJdbcTemplate().queryForMap(sql,param);	
			}catch(EmptyResultDataAccessException e1){		
			}
			
			if(resultMap!=null){
				mailTemplate.setTemplateID(Integer.parseInt(String.valueOf(resultMap.get("templateID"))));
				mailTemplate.setTemplateName((String)resultMap.get("templateName"));
				mailTemplate.setTemplateContent((String)resultMap.get("templateContent"));				
				mailTemplate.setShareGroupID((String)resultMap.get("shareGroupID"));
				mailTemplate.setUserID((String)resultMap.get("userID"));
				mailTemplate.setUserName((String)resultMap.get("userName"));
				mailTemplate.setUseYN((String)resultMap.get("useYN"));
				mailTemplate.setGroupName((String)resultMap.get("groupName"));
				mailTemplate.setRegistDate(String.valueOf(resultMap.get("registDate")));
				mailTemplate.setTemplateType((String)resultMap.get("templateType"));
			}
		
			
			return mailTemplate;
	}
	
}
