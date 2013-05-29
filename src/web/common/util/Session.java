package web.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Session {

	/**
	 * <p>세션키값에 해당하는 값을 얻어온다. 
	 * @param req
	 * @param param
	 * @return
	 */
	public static String getSession(HttpServletRequest req, String param){
		HttpSession session =  req.getSession();
		return (String)session.getAttribute(param);
	}
}
