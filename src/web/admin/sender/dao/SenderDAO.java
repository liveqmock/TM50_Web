package web.admin.sender.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.admin.sender.model.*;

public interface SenderDAO {
	
	/**
	 * <p>보내는 사람  리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 * @throws DataAccessException
	 */
	public List<Sender> listSender(String userID, String groupID,String userAuth, int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
		
	
	/**
	 * <p>보내는 사람 카운트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public int getCountSender(String userID, String groupID,String userAuth, Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>보내는사람 등록 
	 * @param sender
	 * @return
	 * @throws DataAccessException
	 */
	public int insertSender(Sender sender)  throws DataAccessException;
	
	
	/**
	 * <p>보내는사람 수정 
	 * @param sender
	 * @return
	 * @throws DataAccessException
	 */
	public int updateSender(Sender sender)  throws DataAccessException;
	
	
	
	/**
	 * <p>보내는 사람을 삭제 
	 * @param senderID
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteSender(int senderID) throws DataAccessException;
	
	
	
	/**
	 * <p>보내는 사람 정보보기 
	 * @param senderID
	 * @return
	 * @throws DataAccessException
	 */
	public Sender viewSender(int senderID)  throws DataAccessException;
	

}
