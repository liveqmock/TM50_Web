package web.admin.systemset.service;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import web.admin.systemset.dao.SystemSetDAO;
import web.admin.systemset.model.SystemSet;



public class SystemSetServiceImpl  implements SystemSetService {

	private SystemSetDAO systemSetDAO = null;
	private Logger logger = Logger.getLogger("TM");
    
	public void setSystemSetDAO(SystemSetDAO systemSetDAO) {    	
        this.systemSetDAO = systemSetDAO;        
    }
	
	
	/**
	 * <p>환경설정을 보여준다. 
	 */
	public List<SystemSet> listSystemSet(String configFlag) throws DataAccessException {
		
		List<SystemSet> list = null;
		
		try{ 
			list = systemSetDAO.listSystemSet(configFlag); 
		}catch(Exception e){ 
			logger.error(e); 
		}
		return list;
	}

	

	/**
	 * <p>환경설정을 업데이트 
	 */
	public int[] updateSystemSet(List<SystemSet> arrSystemSet)
			throws DataAccessException {
		
		int[] result =null; 
		
		try{
			result = systemSetDAO.updateSystemSet(arrSystemSet);  
		}catch(Exception e){ 
			logger.error(e); 
		}
		
		return result;
	}
	
	/**
	 * <p>환경 설정에서 특정 configName의 value 값을 받아온다
	 * @param configFlag,configName
	 * @return configValue
	 * @throws DataAccessException
	 */
	public String getSystemSetInfo(String configFlag, String configName){
		String result = "";
		
		try{ 
			result = systemSetDAO.getSystemSetInfo(configFlag, configName); 
		}catch(Exception e){ 
			logger.error(e); 
		}
		
		return result;
	}

	/**
	 * <p>접근 IP 제한 (허용 IP인지 확인)
	 * @param userID
	 * @return
	 */
	public int checkAccessIP(String remoteIP){
		int result = 0;
		try{
			result = systemSetDAO.checkAccessIP(remoteIP);
		}catch(Exception e){
			logger.error(e); 
		}
		return result;
	}
	
	/**
	 * <p>환경설정 업데이트 - configFlag, configName 사용
	 * @param configFlag
	 * * @param configName
	 * @return
	 * @throws Exception
	 */
	public int updateConfigValue(String configFlag, String configName, String configValue){
		int result = 0;
		try{
			result = systemSetDAO.updateConfigValue(configFlag, configName, configValue);
		}catch(Exception e){
			logger.error(e); 
		}
		return result;
	}
	
}
