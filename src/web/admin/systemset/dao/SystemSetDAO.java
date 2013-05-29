package web.admin.systemset.dao;

import java.util.List;
import web.admin.systemset.model.SystemSet;
import org.springframework.dao.DataAccessException;



public interface SystemSetDAO {

	/**
	 * 환경 설정 사항을 불러 온다. 
	 * @return
	 * @throws DataAccessException
	 */	
	public List<SystemSet> listSystemSet(String configFlag) throws DataAccessException;
	
	
	/**
	 * <p>환경 설정 사항을 수정한다.
	 * @param board
	 * @return
	 * @throws DataAccessException
	 * @throws Exception 
	 */
	public int[] updateSystemSet(List<SystemSet> arrSystemSet)  throws  Exception;
	
	/**
	 * <p>환경 설정에서 특정 configName의 value 값을 받아온다
	 * @param configFlag,configName
	 * @return configValue
	 * @throws DataAccessException
	 */
	public String getSystemSetInfo(String configFlag, String configName) throws DataAccessException;
	
	/**
	 * <p>접근 IP 제한 (허용 IP인지 확인)
	 * @param userID
	 * @return
	 */
	public int checkAccessIP(String remoteIP) throws DataAccessException;
	

	/**
	 * <p>환경설정 업데이트 - configFlag, configName 사용
	 * @param configFlag
	 * * @param configName
	 * @return
	 * @throws Exception
	 */
	public int updateConfigValue(String configFlag, String configName, String configValue) throws DataAccessException;
}
