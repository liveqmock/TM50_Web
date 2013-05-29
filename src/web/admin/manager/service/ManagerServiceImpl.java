package web.admin.manager.service;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import web.admin.manager.model.Manager;
import web.admin.manager.dao.*;


public class ManagerServiceImpl implements ManagerService{
	
	private Logger logger = Logger.getLogger("TM");
	private ManagerDAO managerDAO = null;
	
	public void setManagerDAO(ManagerDAO managerDAO){
		this.managerDAO = managerDAO;
	}
	
	/**
	 * <p>Engine 리스트 
	 * @param searchMap
	 * @return
	 */
	public List<Manager> listEngine(){
		List<Manager> result = null;
		try{
			result = managerDAO.listEngine();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>EZMAIL 발송엔진상태 업데이트
	 * @param searchMap
	 * @return
	 */
	public int enginStatusUpdate(String engineID, String engineStatus) throws DataAccessException {
		int result = -1; 
		try{
			result = managerDAO.enginStatusUpdate(engineID, engineStatus);
		}catch(Exception e){ 
			logger.error(e); 
		}
		return result;
	}
	
	public Manager getEngineState(String engine_id, String server_ip){
		Manager result = null;
		try{
			result = managerDAO.getEngineState(engine_id, server_ip);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	/**
	 * 발송중인 메일이 있는지 체크, 있으면 true, 없으면 false
	 * 발송중 : 실발송중(상태값 14), 오류자 재발송중(상태값 16)
	 */
	public boolean isSendMassMail() 
	{
		boolean	isSend = false;
		
		try
		{		
			int a = managerDAO.isSendMassMail();
			if(a>0)
				isSend = true;
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return isSend;		
	}
}
