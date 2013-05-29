package web.admin.login.service;


import org.springframework.dao.DataAccessException;

import web.admin.login.model.LoginHistory;
import web.admin.login.model.Users;


public interface  LoginService {
	

	/**
	 * <p>아이디와 패스워드에 해당하는 유저를 가져온다. 
	 * @param userID
	 * @param userPWD
	 * @return
	 */
	public Users getUsersInfo(String userID, String userPWD);
	
	/**
	 * <p>사용자 권한에 해당하는 서브메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 *
	 */	
	public String listMenuSubAuth( String userID );

	/**
	 * <p>로그인 실패시 tm_users 테이블의 updateLoginFailCount를 1증가 시킨다.
	 * @param userID
	 * @return
	 */
	public int updateLoginFailCount(String userID);
	
	/**
	 * <p>로그인 실패 횟수 확인
	 * @param userID
	 * @return
	 */
	public int checkLoginFailCount(String userID);
	
	/**
	 * <p>로그인 설공시 tm_users 테이블의 updateLoginFailCount를 0으로 변경한다.
	 * @param userID
	 * @return
	 */
	public int updateLoginFailCountZero(String userID);
	
	/**
	 * <p>로그인 시도 기록 저장
	 * @param loginHistory
	 * @return
	 */
	public int insertLoginHistory(LoginHistory loginHistory);
	
	
}
