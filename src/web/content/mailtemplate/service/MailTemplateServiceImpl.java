package web.content.mailtemplate.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import web.content.mailtemplate.dao.MailTemplateDAO;
import web.content.mailtemplate.model.MailTemplate;

public class MailTemplateServiceImpl implements MailTemplateService {

	private Logger logger = Logger.getLogger("TM");
	private MailTemplateDAO mailTemplateDAO = null;
	
	public void setMailTemplateDAO(MailTemplateDAO mailTemplateDAO){
		this.mailTemplateDAO = mailTemplateDAO;
	}
	
	
	/**
	 * <p>메일템플릿 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MailTemplate> listMailTemplate(String userID, String groupID,String userAuth, int currentPage, int countPerPage,Map<String, String> searchMap){
		List<MailTemplate> result = null;
		try{
			result = mailTemplateDAO.listMailTemplate(userID, groupID, userAuth, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>메일템플릿  카운트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */	
	public int getCountMailTemplate(String userID, String groupID,  String userAuth, Map<String, String> searchMap){
		int result = 0;
		try{
			result = mailTemplateDAO.getCountMailTemplate(userID, groupID, userAuth, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	
	/**
	 * <p>메일템플릿 입력 
	 * @param mailTemplate
	 * @return
	 */
	public int insertMailTemplate(MailTemplate mailTemplate){
		int result = 0;
		try{
			
			result = mailTemplateDAO.insertMailTemplate(mailTemplate);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>메일템플릿 수정 
	 * @param mailTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMailTemplate(MailTemplate mailTemplate){
		int result = 0;
		try{
			result = mailTemplateDAO.updateMailTemplate(mailTemplate);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>메일템플릿 삭제 
	 * @param maps
	 * @return
	 */
	public int[] deleteMailTemplate(Map<String, Object>[] maps){
		int[] result = null;
		try{
			result = mailTemplateDAO.deleteMailTemplate(maps);
		}catch(Exception e){
			result = null;
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>메일템플릿보기 
	 * @param templateID
	 * @return
	 */
	public MailTemplate viewMailTemplate(int templateID){
		MailTemplate mailTemplate = null;
		try{
			mailTemplate = mailTemplateDAO.viewMailTemplate(templateID);
		}catch(Exception e){
			logger.error(e);
		}
		return mailTemplate;
	}
	

}
