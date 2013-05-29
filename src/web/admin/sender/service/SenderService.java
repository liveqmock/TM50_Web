package web.admin.sender.service;

import java.util.List;
import java.util.Map;
import web.admin.sender.model.Sender;

public interface SenderService {

	
	/**
	 * <p>보내는 사람  리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 */	
	public List<Sender> listSender(String userID, String groupID,String userAuth, int currentPage, int countPerPage,Map<String, String> searchMap);
	
	/**
	 * <p>보내는 사람 카운트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public int getCountSender(String userID, String groupID,String userAuth, Map<String, String> searchMap);
	
	
	/**
	 * <p>보내는사람 등록 
	 * @param sender
	 * @return
	 */
	public int insertSender(Sender sender);
	
	
	/**
	 * <p>보내는사람 수정 
	 * @param sender
	 * @return
	 */
	public int updateSender(Sender sender);
	
	
	
	/**
	 * <p>보내는 사람을 삭제 
	 * @param senderID
	 * @return
	 */
	public int deleteSender(String[] senderIDS);
	
	
	/**
	 * <p>보내는 사람 정보보기 
	 * @param senderID
	 * @return
	 */
	public Sender viewSender(int senderID);
	
}
