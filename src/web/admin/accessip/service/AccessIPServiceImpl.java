package web.admin.accessip.service;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import web.admin.accessip.dao.AccessIPDAO;
import web.admin.accessip.model.AccessIP;


public class AccessIPServiceImpl implements AccessIPService{

	private Logger logger = Logger.getLogger("TM");
	private AccessIPDAO accessIPDAO = null;	
	
	public void setAccessIPDAO(AccessIPDAO accessIPDAO){
		this. accessIPDAO =  accessIPDAO;
	}
	/**
	 * <p>접근IP관리 리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 */
	public List<AccessIP> listAccessIP(int currentPage, int countPerPage, Map<String, String> searchMap){
		List<AccessIP> result = null;
		try{
			result = accessIPDAO.listAccessIP(currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>접근IP관리 리스트의 총카운트를 구해온다.
	 * @return
	 */
	public int getAccessIPTotalCount(Map<String, String> searchMap){
		int result = 0;
		try{
			result = accessIPDAO.getAccessIPTotalCount(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>접근IP관리 설정 정보
	 * @param accessipID
	 * @return
	 */
	public AccessIP viewAccessIP(int accessipID){
		AccessIP result = null;
		try{
			result = accessIPDAO.viewAccessIP(accessipID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	/**
	 * <p>접근IP관리 설정 추가한다.
	 * @param accessIP
	 * @return
	 */
	public int insertAccessIP(AccessIP accessIP){
		int result = 0;
		try{
			result = accessIPDAO.insertAccessIP(accessIP);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p접근IP관리 설정 수정한다.
	 * @param accessIP
	 * @return
	 */
	public int updateAccessIP(AccessIP accessIP){
		int result = 0;
		try{
			result = accessIPDAO.updateAccessIP(accessIP);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>접근IP관리 설정을 삭제한다.
	 * @param accessipIDs
	 * @return
	 */
	public int deleteAccessIP(String[] accessipIDs){
		int result = 0;		
		try{
			for(int i=0;i<accessipIDs.length;i++){	
				
				result = accessIPDAO.deleteAccessIP(Integer.parseInt(accessipIDs[i]));
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
