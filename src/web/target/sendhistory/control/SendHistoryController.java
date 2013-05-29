package web.target.sendhistory.control;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.common.util.LoginInfo;
import web.common.util.QueryUtil;
import web.common.util.ServletUtil;

import web.target.sendhistory.model.MassMailSendResult;
import web.target.sendhistory.service.*;
import web.target.targetlist.model.TargetList;

public class SendHistoryController extends MultiActionController{

	
	private SendHistoryService sendHistoryService = null;
	private String sCurPage = null;
	private String message = "";
	
	public void setSendHistoryService(SendHistoryService sendHistoryService){
		this.sendHistoryService = sendHistoryService;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
		return new ModelAndView("/pages/target/sendhistory/sendhistory.jsp");		
	}
	
	/**
	 * <p>기존발송대상자리스트  
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		String sendedYear = ServletUtil.getParamString(req, "sSendedYear");
		String sendedMonth = ServletUtil.getParamString(req, "sSendedMonth");
		
		String sendYN = ServletUtil.getParamString(req, "sSendYN");
		String openYN = ServletUtil.getParamString(req, "sOpenYN");
		String clickYN = ServletUtil.getParamString(req, "sClickYN");
		String rejectcallYN = ServletUtil.getParamString(req, "sRejectcallYN");
		
		
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
		
		//검색조건 
		String searchType = ServletUtil.getParamString(req,"sSearchType");
		String searchText = ServletUtil.getParamString(req,"sSearchText");
		
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		
		if(Integer.parseInt(sendedMonth)<10){
			sendedMonth="0"+sendedMonth;
		}

		searchMap.put("sendedYear",sendedYear);
		searchMap.put("sendedMonth",sendedMonth);
		searchMap.put("searchType", searchType);
		searchMap.put("searchText", searchText);	
		searchMap.put("sendYN", sendYN);
		searchMap.put("openYN", openYN);
		searchMap.put("clickYN", clickYN);
		searchMap.put("rejectcallYN", rejectcallYN);
	
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		//총카운트 		
		int totalCount = sendHistoryService.totalCountMassMailSendResult(userInfo, searchMap);
		
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
				
		return new ModelAndView("/pages/target/sendhistory/sendhistory_proc.jsp?method=list","sendHistoryList", sendHistoryService.listMassMailSendResult(userInfo, curPage, iLineCnt, searchMap));

	}
	
	/**
	 * <p>대량메일 작성 정보 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView view(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int massmailID = ServletUtil.getParamInt(req,"massmailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		req.setAttribute("targetGroupList", sendHistoryService.listTargetingGroup(massmailID));
		return new ModelAndView("/pages/target/sendhistory/sendhistory_proc.jsp?method=massmailinfo","massmailInfo",sendHistoryService.viewMassMailInfo(massmailID,scheduleID));
	}
	
	/**
	 * <p> 대상자 그룹 추가 정보 등록창을 출력한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addTargetWindow(HttpServletRequest req, HttpServletResponse res) throws Exception{		

		return new ModelAndView("/pages/target/sendhistory/sendhistory_proc.jsp?method=addTarget");

	}
	
}
