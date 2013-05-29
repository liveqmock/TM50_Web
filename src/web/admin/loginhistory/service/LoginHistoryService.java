package web.admin.loginhistory.service;

import java.util.List;
import java.util.Map;

import web.admin.loginhistory.model.LoginHistory;

public interface LoginHistoryService {
	
	/**
	 * <p>로그인 시도 기록 조회
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */
	public  List<LoginHistory> listLoginHistory(int currentPage, int countPerPage, Map<String, String> searchMap);
	
	/**
	 * <p>로그인 시도 기록 조회 카운트
	 * @param searchMap
	 * @return
	 */
	public int getCountLoginHistory(Map<String, String> searchMap);

}
