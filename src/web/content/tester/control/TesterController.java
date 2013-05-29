package web.content.tester.control;


import java.util.HashMap;
import java.util.Map;
import web.common.util.ServletUtil;
import web.common.util.LoginInfo;
import web.content.tester.model.*;
import web.content.tester.service.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class TesterController extends MultiActionController{
	
	private TesterService testerService = null;
	private String message = "";
	private String sCurPage = null;
	
	
	public void setTesterService(TesterService testerService){
		this.testerService = testerService;
	}

	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/pages/content/tester/tester.jsp");
	}
	
	/**
	 * <p>테스트 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
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
		String userID = LoginInfo.getUserID(req);
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		String isAdmin = LoginInfo.getIsAdmin(req);
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("userID", userID);
		searchMap.put("sSearchType", sSearchType);
		searchMap.put("sSearchText", sSearchText);	
		searchMap.put("isAdmin", isAdmin);	
		searchMap.put("curPage", Integer.toString(curPage));	
		searchMap.put("iLineCnt", Integer.toString(iLineCnt));	
		
		//총카운트 		
		int iTotalCnt = testerService.getTesterTotalCount(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		
		return new ModelAndView("/pages/content/tester/tester_proc.jsp?method=list","testerList", testerService.listTester(searchMap));
	}
	
	
	/**
	 * <p>테스트 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String testerEmail = ServletUtil.getParamString(req,"eTesterEmail");
		String testerName = ServletUtil.getParamString(req,"eTesterName");
		String testerHp = ServletUtil.getParamString(req,"eTesterHp");
		String userID = LoginInfo.getUserID(req);
		
		
		if(testerService.checkTesterByUserID(userID)>30){
			ServletUtil.messageGoURL(res,"30건이 초과되었습니다. 등록할 수 없습니다.","");
			return null;	
		}
		
		Tester tester = new Tester();
		tester.setTesterEmail(testerEmail);
		tester.setTesterName(testerName);
		tester.setTesterHp(testerHp);
		tester.setUserID(userID);
		
		int resultVal = testerService.insertTester(tester);
		if(resultVal>0){
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}				
	}
	
	
	/**
	 * <p>테스트 수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String testerEmail = ServletUtil.getParamString(req,"eTesterEmail");
		String testerName = ServletUtil.getParamString(req,"eTesterName");
		String testerHp = ServletUtil.getParamString(req,"eTesterHp");
		int testerID = ServletUtil.getParamInt(req,"eTesterID","0");
		String userID = LoginInfo.getUserID(req);
		
		Tester tester = new Tester();
		tester.setTesterEmail(testerEmail);
		tester.setTesterName(testerName);
		tester.setTesterHp(testerHp);
		tester.setUserID(userID);
		tester.setTesterID(testerID);
		
		int resultVal = testerService.updateTester(tester);
		if(resultVal>0){
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"수정에 실패했습니다","");
			return null;	
		}				
	}
	
	/**
	 * <p>테스터 삭제 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String[] testerIDs =  req.getParameterValues("eTesterID");					
		
		if(testerIDs==null || testerIDs.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
			
		int resultVal = testerService.deleteTester(testerIDs);		
		
		if(resultVal>0){
			//this.message = "삭제 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		
		return null;		
	}
	
	/**
	 * <p>테스터 수정창 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		//페이징세팅 
		ServletUtil.pageParamSetting(req);
		//메뉴아이디세팅
		ServletUtil.meunParamSetting(req);
		
		
		
		int testerID = ServletUtil.getParamInt(req,"eTesterID","0");
		return new ModelAndView("/pages/content/tester/tester_proc.jsp?method=edit","tester", testerService.viewTester(testerID));		
	}
	
}
