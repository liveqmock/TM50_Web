package web.api.automail;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import web.admin.systemset.control.SystemSetControllerHelper;
import web.admin.systemset.service.SystemSetService;
import web.api.automail.control.AutoMailAPIControlHelper;
import web.api.automail.model.AutoMailAPI;
import web.api.automail.service.AutoMailAPIService;
import web.common.util.ServletUtil;

@SuppressWarnings("serial")
public class AutomailAPIServlet extends HttpServlet
{
	private ServletContext context = null; 
	private PrintWriter out = null;
	Logger logger = Logger.getLogger("TM");
	
	private static String RESULT_SUCCESS = "-100";  	//데이터 전송 성공
	private static String RESULT_FAIL_PARAM = "10"; 	//필수 파라미터 미입력
	private static String RESULT_FAIL_INSERT = "30"; 	//연동 데이터 입력 실패
	private static String RESULT_ETC = "90";			//기타
	private static String RESULT_IP_BLOCK = "50";		//자동메일API연동 IP차단
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		out = res.getWriter();
		context = getServletContext();
		
		SystemSetService sysService = SystemSetControllerHelper.getUserService(context);
		String remoteIP = req.getRemoteAddr();
		String automailAPIAccessIP = sysService.getSystemSetInfo("1","automailAPIAccessIP");
		
		boolean accessIP = false;
		if (!automailAPIAccessIP.equals("N")) {
			
			if (remoteIP.equals("0:0:0:0:0:0:0:1")) {
				remoteIP ="127.0.0.1";
			}
			
			String[] splitAutomailAPIAccessIPs =  web.common.util.StringUtil.stringToStringArray(automailAPIAccessIP, "|");
			
			String[] splitRemoteIP = web.common.util.StringUtil.stringToStringArray(remoteIP, ".");
			String splitOneStarRemoteIP = splitRemoteIP[0] + "." + splitRemoteIP[1] + "." + splitRemoteIP[2] + ".*";
			String splitTwoStarRemoteIP = splitRemoteIP[0] + "." + splitRemoteIP[1] + ".*.*";
			
			for (int i = 0; i < splitAutomailAPIAccessIPs.length; i++) {
				
				if (splitAutomailAPIAccessIPs[i].trim().equals(remoteIP) || splitAutomailAPIAccessIPs[i].trim().equals(splitOneStarRemoteIP)
						|| splitAutomailAPIAccessIPs[i].trim().equals(splitTwoStarRemoteIP) || remoteIP.equals("127.0.0.1")) {
					accessIP = true;
					break;
				} else {
					accessIP = false;
				}
			}
			
		} else {
			accessIP = true;
		}
		
		if (accessIP) {
			String method = req.getParameter("method");
			if(method.equals("insert"))
			{
				out.println(insertAutoMail(req, res));
			}
		} else {
			out.println(RESULT_IP_BLOCK);
			out.println("This is the IP address has been blocked.");
			out.println("This IP is not enabled for AutoMail API");
		}
	}
	
	private String insertAutoMail(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		AutoMailAPIService service = AutoMailAPIControlHelper.getUserService(context);
		
		String automailID =ServletUtil.getParamString(req, "automailID");
		String mailTitle =ServletUtil.getParamString(req, "mailTitle");
		String mailContent =ServletUtil.getParamString(req, "mailContent");
		String senderMail =ServletUtil.getParamString(req, "senderMail");
		String senderName =ServletUtil.getParamString(req, "senderName");
		String receiverName =ServletUtil.getParamString(req, "receiverName");
		String email =ServletUtil.getParamString(req, "email");
		String onetooneInfo =ServletUtil.getParamString(req, "onetooneInfo");
		String customerID = ServletUtil.getParamString(req, "customerID");
		String reserveDate =ServletUtil.getParamString(req, "reserveDate");
		
		if(!(automailID.equals("")) && !(email.equals("")) )
		{
			AutoMailAPI autoMailAPI = new AutoMailAPI(automailID, mailTitle, mailContent,
					senderMail, senderName, receiverName,
					email, onetooneInfo, reserveDate);
			autoMailAPI.setCustomerID(customerID);
			if(!service.insertAutoMail_queue(autoMailAPI))
				return RESULT_FAIL_INSERT;
		}
		else
			return RESULT_FAIL_PARAM;
		
		return RESULT_SUCCESS;
	}
}
