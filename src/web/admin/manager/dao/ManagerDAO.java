package web.admin.manager.dao;


import java.util.*;

import org.springframework.dao.DataAccessException;
import web.admin.manager.model.*; 

public interface ManagerDAO {
	
	
	/**
	 * <p>영구적인 메일리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<Manager> listEngine()  throws DataAccessException;
	
	/**
	 * <p>EZMAIL 발송엔진상태 업데이트
	 * @param searchMap
	 * @return
	 */
	public int enginStatusUpdate(String engineID, String engineStatus) throws DataAccessException;
	
	public Manager getEngineState(String engine_id, String server_ip)  throws DataAccessException;

	/**
	 * <p>대량메일 발송 확인
	 * @throws DataAccessException
	 */
	public int isSendMassMail() throws DataAccessException;
}
