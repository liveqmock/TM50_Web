package web.admin.loginhistory.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.loginhistory.model.LoginHistory;

public interface LoginHistoryDAO {
	
	/**
	 * <p>로그인 시도 기록 조회
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public  List<LoginHistory> listLoginHistory(int currentPage, int countPerPage, Map<String, String> searchMap) throws DataAccessException;
	
	/**
	 * <p>로그인 시도 기록 조회 카운트
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	
	public int getCountLoginHistory(Map<String, String> searchMap) throws DataAccessException;

}
