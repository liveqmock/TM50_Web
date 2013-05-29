package web.api.autosms.dao;

import java.util.HashMap;
import java.util.Map;

import web.api.autosms.model.AutosmsAPI;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;

public class AutosmsAPIDAOImpl extends DBJdbcDaoSupport implements AutosmsAPIDAO {
	
	public int insertAutoSms_queue(AutosmsAPI autoSmsAPI)
	{
		String sql =  QueryUtil.getStringQuery("autosms_sql","autosms.api.insert"); 
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("autosmsID", autoSmsAPI.getAutosmsID());
		param.put("senderPhone", autoSmsAPI.getSenderPhone());
		param.put("receiverPhone", autoSmsAPI.getReceiverPhone());
		param.put("smsMsg", autoSmsAPI.getSmsMsg());
		param.put("msgType", autoSmsAPI.getMsgType());
		param.put("onetooneInfo", autoSmsAPI.getOnetooneInfo());
		
		
		return getSimpleJdbcTemplate().update(sql, param);
		
	}

}
