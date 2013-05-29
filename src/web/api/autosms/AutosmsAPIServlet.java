package web.api.autosms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import web.api.autosms.control.AutosmsAPIControlHelper;
import web.api.autosms.model.AutosmsAPI;
import web.api.autosms.service.AutosmsAPIService;

import web.common.util.ServletUtil;

@SuppressWarnings("serial")
public class AutosmsAPIServlet extends HttpServlet
{
	
	private ServletContext context = null; 
	private PrintWriter out = null;
	Logger logger = Logger.getLogger("TM");
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		out = res.getWriter();
		context = getServletContext();
		
		if(insertAutoSms(req, res))
			out.println("true");
		else
			out.println("false");
			
		
	}
	
	private boolean insertAutoSms(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		AutosmsAPIService service = AutosmsAPIControlHelper.getUserService(context);
		
		String autosmsID =ServletUtil.getParamString(req, "autosmsID");
		String senderPhone =ServletUtil.getParamString(req, "senderPhone");
		String receiverPhone =ServletUtil.getParamString(req, "receiverPhone");
		String smsMsg =ServletUtil.getParamString(req, "smsMsg");
		String msgType =ServletUtil.getParamString(req, "msgType");
		String onetooneInfo =ServletUtil.getParamString(req, "onetooneInfo");
		
		
		if(!(autosmsID.equals("")) && !(receiverPhone.equals("")) )
		{
			AutosmsAPI autoMailAPI = new AutosmsAPI(Integer.parseInt(autosmsID), senderPhone, receiverPhone, smsMsg, msgType,	onetooneInfo);
			if(!service.insertAutoSms_queue(autoMailAPI))
				return false;
		}
		else
			return false;
		
		return true;
	}

}
