package web.api.massmail.service;

public interface MassMailAPIService {
	
	public int getOnetooneID(String onetooneAlias);
	
	public int getTargetState(int targetID);
	
	public String getTemplateContent(int template_id);

}
