package web.api.automail.dao;


import web.api.automail.model.AutoMailAPI;

public interface AutoMailAPIDAO {
	
	public int insertAutoMail_queue(AutoMailAPI autoMailAPI);

}
