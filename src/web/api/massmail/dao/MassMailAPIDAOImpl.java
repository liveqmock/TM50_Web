package web.api.massmail.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;

public class MassMailAPIDAOImpl extends DBJdbcDaoSupport  implements MassMailAPIDAO{
	
	
	public int getOnetooneID(String onetooneAlias) throws DataAccessException
	{
	
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.api.getonetooneid");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("onetooneAlias", onetooneAlias);		
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	public int getTargetState(int targetID) throws DataAccessException
	{
	
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.api.gettargetstate");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("targetID", targetID);		
		
		return getSimpleJdbcTemplate().queryForInt(sql, param);
	}
	
	public Map<String, Object> getTemplateContent(int template_id) throws DataAccessException
	{
	
		String sql =  QueryUtil.getStringQuery("massmail_sql","massmail.api.gettemplatecontent");
		
		Map<String,Object> param = new HashMap<String, Object>();		
		param.put("templateID", template_id);		
		
		
		
		return getSimpleJdbcTemplate().queryForMap(sql, param);
	}

}
