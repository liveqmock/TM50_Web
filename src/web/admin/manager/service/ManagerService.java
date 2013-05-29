package web.admin.manager.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import web.admin.manager.model.*;


public interface ManagerService {

	/**
	 * <p>EZMAIL 발송엔진 목록
	 * @param searchMap
	 * @return
	 */
	public List<Manager> listEngine();
	
	/**
	 * <p>EZMAIL 발송엔진상태 업데이트
	 * @param searchMap
	 * @return
	 */
	public int enginStatusUpdate(String engineID, String engineStatus) throws DataAccessException;
	
	
	public Manager getEngineState(String engine_id, String server_ip);
	

	/**
	 * 발송중인 메일이 있는지 체크, 있으면 true, 없으면 false
	 * 발송중 : 실발송중(상태값 14), 오류자 재발송중(상태값 16)
	 */
	public boolean isSendMassMail();
}
