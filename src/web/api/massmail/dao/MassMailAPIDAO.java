package web.api.massmail.dao;

import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface MassMailAPIDAO {
	
	public int getOnetooneID(String onetooneAlias) throws DataAccessException;
	
	public int getTargetState(int targetID) throws DataAccessException;
	
	public Map<String, Object> getTemplateContent(int template_id) throws DataAccessException;
}
