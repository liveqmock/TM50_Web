package web.common.util;

import javax.servlet.http.*;
import web.common.util.StringUtil;

public class LoginInfo  {

	/**
	 * <p>로그인된 유저의 권한을을 얻어온다. 
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getUserAuth( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("userAuth") );
	}

	public static void setUserAuth( HttpServletRequest request, String userAuth ) {
		HttpSession session =  request.getSession();
		session.setAttribute("userAuth", userAuth );
	}
	
	
	/**
	 * <p>로그인 여부를 리턴  
	 * @param req
	 * @param param
	 * @return
	 */
	public static boolean getIsLogined( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("isLogined") ).equals("Y");
	}
	
	public static void setIsLogined( HttpServletRequest request, boolean isLogined) {
		HttpSession session =  request.getSession();
		session.setAttribute("isLogined", (isLogined?"Y":"N") );
	}
	

	/**
	 * <p>로그인된 유저의 ID 얻어온다. 
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getUserID( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("userID") );
	}
	public static void setUserID( HttpServletRequest request, String userID) {
		HttpSession session =  request.getSession();
		session.setAttribute("userID", userID );
	}
	
	/**
	 * <p>로그인된 유저의 이름을 얻어온다. 
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getUserName( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("userName") );
	}
	public static void setUserName( HttpServletRequest request, String userName) {
		HttpSession session =  request.getSession();
		session.setAttribute("userName", userName );
	}
	

	/**
	 * <p>로그인된 유저의 보내는 사람 이름을 얻어온다. 
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getSenderName( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("senderName") );
	}
	public static void setSenderName( HttpServletRequest request, String senderName) {
		HttpSession session =  request.getSession();
		session.setAttribute("senderName", senderName );
	}
	
	/**
	 * <p>로그인된 유저의 보내는 사람 Email을 얻어온다. 
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getSenderCellPhone( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("senderCellPhone") );
	}
	public static void setSenderCellPhone( HttpServletRequest request, String senderCellPhone) {
		HttpSession session =  request.getSession();
		session.setAttribute("senderCellPhone", senderCellPhone );
	}
	
	/**
	 * <p>로그인된 유저의 보내는 사람 핸드폰 번호를 얻어온다. 
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getSenderEmail( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("senderEmail") );
	}
	public static void setSenderEmail( HttpServletRequest request, String senderEmail) {
		HttpSession session =  request.getSession();
		session.setAttribute("senderEmail", senderEmail );
	}
	
	/**
	 * <p>로그인된 유저의 GROUPID를 얻어온다. 
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getGroupID( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("groupID") );
	}
	public static void setGroupID( HttpServletRequest request, String groupID) {
		HttpSession session =  request.getSession();
		session.setAttribute("groupID", groupID );
	}
	
	/**
	 * <p>로그인된 유저의 GROUP명을 얻어온다. 
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getGroupName( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("groupName") );
	}
	public static void setGroupName( HttpServletRequest request, String groupName) {
		HttpSession session =  request.getSession();
		session.setAttribute("groupName", groupName );
	}

	/**
	 * <p>로그인된 유저의 isAdmin을 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getIsAdmin( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("isAdmin") );
	}
	public static void setIsAdmin( HttpServletRequest request, String isAdmin) {
		HttpSession session =  request.getSession();
		session.setAttribute("isAdmin", isAdmin );
	}
	
	/**
	 * <p>로그인된 유저의 auth_csv 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getAuth_csv( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("auth_csv") );
	}
	public static void setAuth_csv( HttpServletRequest request, String auth_csv) {
		HttpSession session =  request.getSession();
		session.setAttribute("auth_csv", auth_csv );
	}
	
	/**
	 * <p>로그인된 유저의 auth_query 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getAuth_query( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("auth_query") );
	}
	public static void setAuth_query( HttpServletRequest request, String auth_query) {
		HttpSession session =  request.getSession();
		session.setAttribute("auth_query", auth_query );
	}
	
	/**
	 * <p>로그인된 유저의 auth_direct 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getAuth_direct( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("auth_direct") );
	}
	public static void setAuth_direct( HttpServletRequest request, String auth_direct) {
		HttpSession session =  request.getSession();
		session.setAttribute("auth_direct", auth_direct );
	}
	
	/**
	 * <p>로그인된 유저의 auth_related 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getAuth_related( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("auth_related") );
	}
	public static void setAuth_related( HttpServletRequest request, String auth_related) {
		HttpSession session =  request.getSession();
		session.setAttribute("auth_related", auth_related );
	}
	
	/**
	 * <p>로그인된 유저의 auth_write_mail 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getAuth_write_mail( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("auth_write_mail") );
	}
	public static void setAuth_write_mail( HttpServletRequest request, String auth_write_mail) {
		HttpSession session =  request.getSession();
		session.setAttribute("auth_write_mail", auth_write_mail );
	}
	
	/**
	 * <p>로그인된 유저의 auth_send_mail 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getAuth_send_mail( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("auth_send_mail") );
	}
	public static void setAuth_send_mail( HttpServletRequest request, String auth_send_mail) {
		HttpSession session =  request.getSession();
		session.setAttribute("auth_send_mail", auth_send_mail );
	}
	
	
	/**
	 * <p>로그인된 유저의 auth_write_sms 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getAuth_write_sms( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("auth_write_sms") );
	}
	public static void setAuth_write_sms( HttpServletRequest request, String auth_write_sms) {
		HttpSession session =  request.getSession();
		session.setAttribute("auth_write_sms", auth_write_sms );
	}
	
	/**
	 * <p>로그인된 유저의 auth_send_sms 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getAuth_send_sms( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("auth_send_sms") );
	}
	public static void setAuth_send_sms( HttpServletRequest request, String auth_send_sms) {
		HttpSession session =  request.getSession();
		session.setAttribute("auth_send_sms", auth_send_sms );
	}
	
	/**
	 * <p>로그인된 유저의 submenu를 얻어온다.
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getAuth_sub_menu( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("auth_sub_menu") );
	}
	public static void setAuth_sub_menu( HttpServletRequest request, String sub_menu) {
		HttpSession session =  request.getSession();
		session.setAttribute("auth_sub_menu", sub_menu );
	}
	
	
	/**
	 * <p>로그인된 유저의 정보를 세션에 담는다
	 * @param request
	 * @param userID
	 * @param userAuth
	 */
	public static void setLoginSession( HttpServletRequest request, String userID, String userAuth ) {
		
		HttpSession session =  request.getSession();
		
		session.setAttribute("userID", userID );
		session.setAttribute("userAuth", userAuth );
		session.setAttribute("isLogined", "Y" );
	}
	
	
	/**
	 * <p>주어진 서브메뉴아이디로 접속이 가능한지 체크 물론 관리자이면  true이다. 
	 * @param submenuID
	 * @param request
	 * @return
	 */
	public static String isAccessSubMenu(String submenuID, HttpServletRequest request){
		String result = "N";
		String submenus = getAuth_sub_menu(request);		
		if(submenus.indexOf(submenuID)!=-1){
				result = "Y";
		}		
		return result;
		
	}
	
	/**
	 * <p>로그인된 유저의 비밀번호 변경 안내 여부
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getPWDChangeNoticeYN( HttpServletRequest request) {
		HttpSession session =  request.getSession();
		return  StringUtil.toStr( (String)session.getAttribute("pwdChangeNoticeYN") );
	}
	public static void setPWDChangeNoticeYN( HttpServletRequest request, String pwdChangeNoticeYN) {
		HttpSession session =  request.getSession();
		session.setAttribute("pwdChangeNoticeYN", pwdChangeNoticeYN );
	}
	
}
