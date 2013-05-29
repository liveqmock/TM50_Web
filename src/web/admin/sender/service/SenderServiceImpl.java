package web.admin.sender.service;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import web.admin.sender.dao.SenderDAO;
import web.admin.sender.model.Sender;


public class SenderServiceImpl implements SenderService{

	private Logger logger = Logger.getLogger("TM");
	private SenderDAO senderDAO = null;	
	
	public void setSenderDAO(SenderDAO senderDAO){
		this.senderDAO = senderDAO;
	}
	
	/**
	 * <p>보내는 사람  리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 */
	public List<Sender> listSender(String userID, String groupID,String userAuth, int currentPage, int countPerPage,Map<String, String> searchMap){
		List<Sender> result = null;
		try{
			result = senderDAO.listSender(userID, groupID, userAuth, currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	

	/**
	 * <p>보내는 사람 카운트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */	
	public int getCountSender(String userID, String groupID,String userAuth, Map<String, String> searchMap){
		int result = 0;
		try{
			result = senderDAO.getCountSender(userID, groupID, userAuth, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	
	/**
	 * <p>보내는사람 등록 
	 * @param sender
	 * @return
	 */
	public int insertSender(Sender sender){
		int result = 0;
		try{
			result = senderDAO.insertSender(sender);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>보내는사람 수정 
	 * @param sender
	 * @return
	 */
	public int updateSender(Sender sender){
		int result = 0;
		try{
			result = senderDAO.updateSender(sender);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>보내는 사람을 삭제 
	 * @param senderID
	 * @return
	 */
	public int deleteSender(String[] senderIDS){
		int result = 0;
		try{
			for(int i=0;i<senderIDS.length;i++){
				result = senderDAO.deleteSender(Integer.parseInt(senderIDS[i]));
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * <p>보내는 사람 정보보기 
	 * @param senderID
	 * @return
	 */
	public Sender viewSender(int senderID){
		Sender sender = null;
		try{			
			sender = senderDAO.viewSender(senderID);			
		}catch(Exception e){
			logger.error(e);
		}
		return sender;
	}
	
	
}
