package web.admin.login.service;


import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import web.admin.login.model.LoginHistory;
import web.admin.login.model.Users;
import web.admin.login.dao.LoginDAO;
import web.admin.menu.model.MenuSub;

public class LoginServiceImpl implements LoginService{
	

	private LoginDAO loginDAO = null;
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	public void setLoginDAO(LoginDAO loginDAO){
		this.loginDAO = loginDAO;
	}
	
	/**
	 * <p>아이디와 패스워드에 해당하는 유저를 가져온다. 
	 * @param userID
	 * @param userPWD
	 * @return
	 */
	public Users getUsersInfo(String userID, String userPWD){
		Users users = null;
		try{
			users = loginDAO.getUsersInfo(userID, userPWD);
		}catch(Exception e){
			//logger.error(e);  //로그인실패면 에러이기때문에 그냥 표시안해준다. 
		}
		return users;		
	}
	
	/**
	 * <p>사용자 권한에 해당하는 서브메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 *
	 */	
	public String listMenuSubAuth( String userID ){
		
		List<MenuSub> resultList = null;
		String temp = "";
		try{
			resultList =  loginDAO.listMenuSubAuth(userID);
			for(int i=0; i < resultList.size(); i++)
			{
				if(i==1)
				{
					temp = resultList.get(i).getSubMenuID();
				}
				else
				{
					temp = temp+","+resultList.get(i).getSubMenuID();
				}
				
			}
						
		}catch(Exception e){
			logger.error(e);
		}
		return temp;
		
	}
	
	/**
	 * <p>로그인 실패시 tm_users 테이블의 updateLoginFailCount를 1증가 시킨다.
	 * @param userID
	 * @return
	 */
	public int updateLoginFailCount(String userID){
		int result = 0;
		try{
			result = loginDAO.updateLoginFailCount(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>로그인 실패 횟수 확인
	 * @param userID
	 * @return
	 */
	public int checkLoginFailCount(String userID){
		int result = 0;
		try{
			result = loginDAO.checkLoginFailCount(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>로그인 설공시 tm_users 테이블의 updateLoginFailCount를 0으로 변경한다.
	 * @param userID
	 * @return
	 */
	public int updateLoginFailCountZero(String userID){
		int result = 0;
		try{
			result = loginDAO.updateLoginFailCountZero(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>로그인 시도 기록 저장
	 * @param loginHistory
	 * @return
	 */
	public int insertLoginHistory(LoginHistory loginHistory){
		int result = 0;
		try{			
			result = loginDAO.insertLoginHistory(loginHistory);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
		
	}
	
	

}
