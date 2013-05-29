package web.admin.loginhistory.control;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.admin.loginhistory.service.LoginHistoryService;
import web.common.util.ServletUtil;

public class LoginHistoryController extends MultiActionController{
	
	private LoginHistoryService loginHistoryService = null;
	
	private String sCurPage = null;
	private String message = "";
	
	public void setLoginHistoryService(LoginHistoryService loginHistoryService){
		this.loginHistoryService = loginHistoryService;
	}
	

	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/loginhistory/loginhistory.jsp");
	}
	
	
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{				
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		if (curPage <= 0) curPage = 1;
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
	

		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}
		

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
	

		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sSearchType", sSearchType);
		searchMap.put("sSearchText", sSearchText);
		
		//총카운트 		
		int totalCount = loginHistoryService.getCountLoginHistory(searchMap);		
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		return new ModelAndView("/admin/loginhistory/loginhistory_proc.jsp?method=list","loginHistoryList", loginHistoryService.listLoginHistory(curPage, iLineCnt, searchMap));
		
	}

}
