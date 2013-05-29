package web.api.sso;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import web.admin.login.control.LoginControlHelper;
import web.admin.login.model.Users;
import web.admin.login.service.LoginService;
import web.common.util.LoginInfo;
import web.common.util.ServletUtil;
import web.common.util.StringUtil;
import web.common.util.ThunderUtil;

@SuppressWarnings("serial")
public class LoginThunderServlet extends HttpServlet
{
	private ServletContext context = null; 
	private PrintWriter out = null;
	Logger logger = Logger.getLogger("TM");
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		res.setContentType("Text/html;charset=utf-8");  //인코딩 설정
		res.setHeader("P3P","CP='NOI CURa ADMa DEVa TAIa OUR DELa BUS IND PHY ONL UNI COM NAV INT DEM PRE'"); 
		//다른 도메인주소에서 SSO호출할 때 한번에 세션 생성 안되는 현상 있을 때 사용
		//같은서버에 두개의 웹서비스 끼리 연동하려 할때 주소가 같으면(같은 아이피, 도메인) 세션이 공유되어 장애 발생함 - 각각 도메인 네임 설정 해줘야함
		
		out = res.getWriter();
		context = getServletContext();
		LoginService loginService= LoginControlHelper.getUserService(context); 
		
		String userID = req.getParameter("userid");
		String password = ThunderUtil.getMD5Hexa(req.getParameter("password"));
		String returnPath = req.getParameter("returnpath");   //원하는 URL 바로 열기 기능 추가 때 쓸 파라미터
		
		//if(returnPath==null ||returnPath.equals(""))
		//{
		//	returnPath = "/index.jsp";  
		//}
		
		returnPath = "/index.jsp";  //현재는 기본 화면 리턴함
		
		if(userID!=null && !(userID.equals("")))
		{
						
			if(password!=null && !(password.equals("")))
			{
				
				/*
				 * 사용자 정보로 로그인
				 */
				Users users = loginService.getUsersInfo(userID, password);
				if(users!=null)
				{
					LoginInfo.setIsLogined(req, true);
					LoginInfo.setUserID(req, users.getUserID());
					LoginInfo.setGroupID(req, users.getGroupID());
					LoginInfo.setUserName(req, users.getUserName());
					LoginInfo.setSenderName(req, users.getSenderName());
					LoginInfo.setSenderEmail(req, users.getSenderEmail());
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
					
					try
					{
					// 쿠키에 id 기억 
						if(StringUtil.toStr( req.getParameter("rememberID") ).equals("Y")) {
							ServletUtil.setCookie(req,res,"rememberID","Y",1);
							ServletUtil.setCookie(req,res,"savedUserID", userID,1);
						}
					}
					catch(Exception e)
					{
						logger.error(e);
					}
					if(StringUtil.toStr( req.getParameter("rememberID") ).equals("Y")) 
					{
						out.println("<script>Cookie.write('rememberID', 'Y', {duration: 100});Cookie.write('savedUserID', '<%=userID%>', {duration: 100});</script>");
						
					}else 
					{
						out.println("<script>Cookie.write('rememberID', '', {duration: 0});Cookie.write('savedUserID', '', {duration: 0});</script>");
					}
					out.println("<script>location.replace('"+returnPath+"')</script>");
				}
				else
				{
					//계정정보 없음
					out.println("<script>alert('로그인 할 수 없습니다. 아이디 및 패스워드를 확인해주세요.');location.replace('"+returnPath+"')</script>");
				}
			}
			else
			{
				//패스워드 틀림
				out.println("<script>alert('패스워드를 입력하세요.');location.replace('"+returnPath+"')</script>");
			}
			
		}
		else
		{
			//아이디 없음
			out.println("<script>alert('아이디를 입력하세요 .');location.replace('"+returnPath+"')</script>");
		}
		
				
	}
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		res.setContentType("Text/html;charset=utf-8");  //인코딩 설정
		res.setHeader("P3P","CP='NOI CURa ADMa DEVa TAIa OUR DELa BUS IND PHY ONL UNI COM NAV INT DEM PRE'"); 
		//다른 도메인주소에서 SSO호출할 때 한번에 세션 생성 안되는 현상 있을 때 사용
		//같은서버에 두개의 웹서비스 끼리 연동하려 할때 주소가 같으면(같은 아이피, 도메인) 세션이 공유되어 장애 발생함 - 각각 도메인 네임 설정 해줘야함
		
		out = res.getWriter();
		context = getServletContext();
		LoginService loginService= LoginControlHelper.getUserService(context); 
		
		String userID = req.getParameter("userid");
		String password = ThunderUtil.getMD5Hexa(req.getParameter("password"));
		String returnPath = req.getParameter("returnpath");   //원하는 URL 바로 열기 기능 추가 때 쓸 파라미터
		
		//if(returnPath==null ||returnPath.equals(""))
		//{
		//	returnPath = "/index.jsp";  
		//}
		
		returnPath = "/index.jsp";  //현재는 기본 화면 리턴함
		
		if(userID!=null && !(userID.equals("")))
		{
						
			if(password!=null && !(password.equals("")))
			{
				
				/*
				 * 사용자 정보로 로그인
				 */
				Users users = loginService.getUsersInfo(userID, password);
				if(users!=null)
				{
					LoginInfo.setIsLogined(req, true);
					LoginInfo.setUserID(req, users.getUserID());
					LoginInfo.setGroupID(req, users.getGroupID());
					LoginInfo.setUserName(req, users.getUserName());
					LoginInfo.setSenderName(req, users.getSenderName());
					LoginInfo.setSenderEmail(req, users.getSenderEmail());
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
					
					try
					{
					// 쿠키에 id 기억 
						if(StringUtil.toStr( req.getParameter("rememberID") ).equals("Y")) {
							ServletUtil.setCookie(req,res,"rememberID","Y",1);
							ServletUtil.setCookie(req,res,"savedUserID", userID,1);
						}
					}
					catch(Exception e)
					{
						logger.error(e);
					}
					if(StringUtil.toStr( req.getParameter("rememberID") ).equals("Y")) 
					{
						out.println("<script>Cookie.write('rememberID', 'Y', {duration: 100});Cookie.write('savedUserID', '<%=userID%>', {duration: 100});</script>");
						
					}else 
					{
						out.println("<script>Cookie.write('rememberID', '', {duration: 0});Cookie.write('savedUserID', '', {duration: 0});</script>");
					}
					out.println("<script>location.replace('"+returnPath+"')</script>");
				}
				else
				{
					//계정정보 없음
					out.println("<script>alert('로그인 할 수 없습니다. 아이디 및 패스워드를 확인해주세요.');location.replace('"+returnPath+"')</script>");
				}
			}
			else
			{
				//패스워드 틀림
				out.println("<script>alert('패스워드를 입력하세요.');location.replace('"+returnPath+"')</script>");
			}
			
		}
		else
		{
			//아이디 없음
			out.println("<script>alert('아이디를 입력하세요 .');location.replace('"+returnPath+"')</script>");
		}
		
				
	}


}
