package web.common.dao;

import java.util.HashMap;
import java.util.Map;

import web.common.util.QueryUtil;



public class CommonAccessDAOImpl extends DBJdbcDaoSupport  implements CommonAccessDAO {
	
		
	
	
	public int getInt(String query)
	{
		Map<String,Object> param = new HashMap<String, Object>();		
		
		
		return  getSimpleJdbcTemplate().queryForInt(query, param);
	}
	
	public int isSendMassMail()
	{
		String query = QueryUtil.getStringQuery("common_sql","common.util.selectsendmailstate");		
	
		Map<String,Object> param = new HashMap<String, Object>();		
	
		
		return  getSimpleJdbcTemplate().queryForInt(query, param);
	}


}
