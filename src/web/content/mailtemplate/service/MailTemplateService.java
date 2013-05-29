package web.content.mailtemplate.service;

import java.util.List;
import java.util.Map;
import web.content.mailtemplate.model.MailTemplate;

public interface MailTemplateService {


	/**
	 * <p>메일템플릿 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public List<MailTemplate> listMailTemplate(String userID, String groupID, String userAuth, int currentPage, int countPerPage,Map<String, String> searchMap);
	
	
	/**
	 * <p>메일템플릿  카운트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */	
	public int getCountMailTemplate(String userID, String groupID, String userAuth, Map<String, String> searchMap);
	
	
	/**
	 * <p>메일템플릿 입력 
	 * @param mailTemplate
	 * @return
	 */
	public int insertMailTemplate(MailTemplate mailTemplate);
	
	
	
	/**
	 * <p>메일템플릿 수정 
	 * @param mailTemplate
	 * @return
	 */
	public int updateMailTemplate(MailTemplate mailTemplate);
	
	
	/**
	 * <p>메일템플릿 삭제 
	 * @param maps
	 * @return
	 */
	public int[] deleteMailTemplate(Map<String, Object>[] maps);
	
	
	/**
	 * <p>메일템플릿보기 
	 * @param templateID
	 * @return
	 */
	public MailTemplate viewMailTemplate(int templateID);
}
