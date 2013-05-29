package web.admin.login.control;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.admin.login.service.LoginService;
import web.admin.login.model.LoginHistory;
import web.admin.login.model.Users;
import web.admin.systemset.control.SystemSetControllerHelper;
import web.admin.systemset.service.SystemSetService;
import web.common.util.Constant;
import web.common.util.LoginInfo;
import web.common.util.ServletUtil;
import web.common.util.DateUtils;
import web.common.util.ThunderUtil;
public class LoginController extends MultiActionController{
	
	private LoginService loginService = null;
	
	
	public void setLoginService(LoginService loginService){
		this.loginService = loginService;
	}
	
	
	/**
	 * 테스트용 로그인 처리
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{
		return new ModelAndView("/admin/login/login.jsp");
	}
	
	
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		return new ModelAndView("/admin/login/login.jsp");
	}
	
	
	/**
	 * <p>아이디와 패스워드를 찾아서 있다면 세션에 담아주고 로그인한다. 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView find(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String userID = ServletUtil.getParamString(req,"userID");
		String userPWD= ServletUtil.getParamString(req,"userPWD");
	
		userPWD = ThunderUtil.getMD5Hexa(userPWD);

		ServletContext context=getServletContext(); 
		SystemSetService service = SystemSetControllerHelper.getUserService(context);
		int loginFialCheckCount=0; //로그인 실패 확인 횟수
		int pwdCheckDate=0; // 비밀번호 변경 주기
		
		LoginHistory loginHistory = null;
		
		//로그인 실패 확인 횟수, 비밀번호 변경 주기 값을 받아온다. - tm_config
		try{loginFialCheckCount = Integer.parseInt(service.getSystemSetInfo(Constant.CONFIG_FLAG_MASSMAIL, "loginFialCheckCount"));}catch(Exception e1){loginFialCheckCount=0; }
		try{pwdCheckDate = Integer.parseInt(service.getSystemSetInfo(Constant.CONFIG_FLAG_MASSMAIL, "pwdCheckDate"));}catch(Exception e2){pwdCheckDate=0;}
		
		Users users = loginService.getUsersInfo(userID, userPWD);
		
		if(users==null){
			if(loginFialCheckCount ==0){
				loginHistory = new LoginHistory(userID, "N", "[로그인실패] 아이디 및 패스워드를 확인해주세요", req.getRemoteAddr());
				ServletUtil.messageGoURL(res,"[로그인실패] 아이디 및 패스워드를 확인해주세요","");
			}else{
				if(loginService.updateLoginFailCount(userID) == 0){
					loginHistory = new LoginHistory(userID, "N", "[로그인실패] 등록되지 않은 아이디 입니다", req.getRemoteAddr());
					ServletUtil.messageGoURL(res,"[로그인실패] 등록되지 않은 아이디 입니다.","");
				}else{
					int failCount = loginService.checkLoginFailCount(userID);
					if(failCount > loginFialCheckCount){
						loginHistory = new LoginHistory(userID, "N", "[로그인실패] 사용 할수 없는 아이디 입니다(비밀번호 입력 오류 횟수 초과)", req.getRemoteAddr());
						ServletUtil.messageGoURL(res,"[로그인실패] 사용 할수 없는 아이디 입니다.\\r\\n (비밀번호 입력 오류 횟수 초과)\\r\\n관리자에게 문의하세요.","");
					}else{
						loginHistory = new LoginHistory(userID, "N", "[로그인실패] 패스워드를 확인해주세요. ("+failCount+"회 오류)", req.getRemoteAddr());
						ServletUtil.messageGoURL(res,"[로그인실패] 패스워드를 확인해주세요. \\r\\n"+loginFialCheckCount+"회 초과 실패시 아이디 사용 불가("+failCount+"회 오류 )","");
					}
				}
			}
			//로그인 시도 정보 인서트
			loginService.insertLoginHistory(loginHistory);
			return null;
		}
		
		if(loginFialCheckCount > 0 & users.getFailCount() > loginFialCheckCount){
			loginHistory = new LoginHistory(userID, "N", "[로그인실패] 사용 할수 없는 아이디 입니다(비밀번호 입력 오류 횟수 초과)", req.getRemoteAddr());
			ServletUtil.messageGoURL(res,"[로그인실패] 사용 할수 없는 아이디 입니다.\\r\\n (비밀번호 입력 오류 횟수 초과)\\r\\n관리자에게 문의하세요.","");
			//로그인 시도 정보 인서트
			loginService.insertLoginHistory(loginHistory);
			return null;
		}
		//로그인이 성공적으로 되었다면 loginFailCount = 0 으로..
		loginService.updateLoginFailCountZero(userID);
		loginHistory = new LoginHistory(userID, "Y", "[로그인성공] 정상 접속", req.getRemoteAddr());
		//로그인 시도 정보 인서트
		loginService.insertLoginHistory(loginHistory);
		
		//로그인이 성공적으로 되었다면 세션에 담아보자..
		LoginInfo.setIsLogined(req, true);
		LoginInfo.setUserID(req, users.getUserID());
		LoginInfo.setGroupID(req, users.getGroupID());
		LoginInfo.setUserName(req, users.getUserName());
		LoginInfo.setSenderName(req, users.getSenderName());
		LoginInfo.setSenderEmail(req, users.getSenderEmail());
		LoginInfo.setSenderCellPhone(req, users.getSenderCellPhone());
		LoginInfo.setUserAuth(req, users.getUserLevel());
		LoginInfo.setGroupName(req, users.getGroupName());
		LoginInfo.setIsAdmin(req, users.getIsAdmin());
		LoginInfo.setAuth_csv(req, users.getAuth_csv());
		LoginInfo.setAuth_direct(req, users.getAuth_direct());
		LoginInfo.setAuth_related(req, users.getAuth_related());
		LoginInfo.setAuth_query(req, users.getAuth_query());
		LoginInfo.setAuth_write_mail(req, users.getAuth_write_mail());
		LoginInfo.setAuth_send_mail(req, users.getAuth_send_mail());
		LoginInfo.setAuth_write_sms(req, users.getAuth_write_sms());
		LoginInfo.setAuth_send_sms(req, users.getAuth_send_sms());
		LoginInfo.setAuth_sub_menu(req, loginService.listMenuSubAuth(userID));
		if(pwdCheckDate > 0){
			// 비밀번호 변경 안내 대상 여부 확인
			if(DateUtils.daysBetween(DateUtils.getStrByPattern("yyyy-MM-dd"), DateUtils.addMonths(users.getModifyDate(), pwdCheckDate, "yyyy-MM-dd"), "yyyy-MM-dd") < 0){
				LoginInfo.setPWDChangeNoticeYN(req,"Y");
			}else{
				LoginInfo.setPWDChangeNoticeYN(req,"N");
			}
		}else{
			LoginInfo.setPWDChangeNoticeYN(req,"N");
		}
		
		return new ModelAndView("/admin/login/login_proc.jsp");
		
	}
	
	/**
	 * <p> 로그아웃시 세션을 비운다.
	 */
	public ModelAndView logOut(HttpServletRequest req, HttpServletResponse res) throws Exception{	

		clearSession( req, res );
		
		return new ModelAndView("/admin/login/login_proc.jsp");
		
	}
	
	/**
	 * <p> 로그인된 사용자의 메뉴를 표시한다
	 */
	public ModelAndView showMenu(HttpServletRequest req, HttpServletResponse res) throws Exception{	

		return new ModelAndView("/admin/login/login_proc.jsp");
		
	}


	/**
	 * <p> 세션을 삭제
	 */
	public void clearSession(HttpServletRequest req, HttpServletResponse res) throws Exception{	

		LoginInfo.setIsLogined(req, false);
		LoginInfo.setUserID(req, "");
		LoginInfo.setGroupID(req, "");
		LoginInfo.setUserName(req, "");
		LoginInfo.setUserAuth(req, "");
		LoginInfo.setGroupName(req, "");
		
	}


}
