package web.content.mailtemplate.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.content.mailtemplate.model.MailTemplate;



public interface MailTemplateDAO {
	
	/**
	 * <p>메일템플릿 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<MailTemplate> listMailTemplate(String userID, String groupID,String userAuth, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>메일템플릿  카운트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public int getCountMailTemplate(String userID, String groupID, String userAuth, Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>메일템플릿 입력 
	 * @param mailTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int insertMailTemplate(MailTemplate mailTemplate) throws DataAccessException;
	
	
	
	/**
	 * <p>메일템플릿 수정 
	 * @param mailTemplate
	 * @return
	 * @throws DataAccessException
	 */
	public int updateMailTemplate(MailTemplate mailTemplate) throws DataAccessException;
	
	
	
	/**
	 * <p>메일템플릿 삭제 
	 * @param maps
	 * @return
	 * @throws DataAccessException
	 */
	public int[] deleteMailTemplate(Map<String, Object>[] maps) throws DataAccessException;
	
	
	
	/**
	 * <p>메일템플릿보기 
	 * @param templateID
	 * @return
	 * @throws DataAccessException
	 */
	public MailTemplate viewMailTemplate(int templateID) throws DataAccessException;
}
