package web.api.automail.dao;


import java.util.HashMap;
import java.util.Map;
import web.common.dao.DBJdbcDaoSupport;
import web.common.util.QueryUtil;
import web.api.automail.model.AutoMailAPI;


public class AutoMailAPIDAOImpl extends DBJdbcDaoSupport implements AutoMailAPIDAO {
	
	public int insertAutoMail_queue(AutoMailAPI autoMailAPI)
	{
		String sql;
		if(autoMailAPI.getReserveDate()!=null && !(autoMailAPI.getReserveDate().equals(""))){
			sql =  QueryUtil.getStringQuery("automail_sql","automail.api.insert2");
		}else{
			sql = QueryUtil.getStringQuery("automail_sql", "automail.api.insert");
		}
		Map<String,Object> param = new HashMap<String, Object>();
		
		param.put("automailID", autoMailAPI.getAutomailID());
		param.put("mailTitle", autoMailAPI.getMailTitle());
		param.put("mailContent", autoMailAPI.getMailContent());
		param.put("senderMail", autoMailAPI.getSenderMail());
		param.put("senderName", autoMailAPI.getSenderName());
		param.put("receiverName", autoMailAPI.getReceiverName());
		param.put("email", autoMailAPI.getEmail());
		param.put("onetooneInfo", autoMailAPI.getOnetooneInfo());
		param.put("customerID", autoMailAPI.getCustomerID());
		param.put("reserveDate", autoMailAPI.getReserveDate());
		
		return getSimpleJdbcTemplate().update(sql, param);
		
	}
	
	

}
