package web.admin.sender.control;



import java.util.HashMap;
import java.util.Map;
import web.admin.sender.service.SenderService;
import web.common.util.ServletUtil;
import web.common.util.LoginInfo;
import web.admin.sender.model.Sender;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;



public class SenderController extends MultiActionController {
	
	private SenderService senderService = null; 
	private String sCurPage = null;
	private String message = "";
	
	public void setSenderService(SenderService senderService){ 
		this.senderService = senderService; 
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/sender/sender_list.jsp");
	}
	
	
	/**
	 * <p>보내는 사람 리스트 
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
		
		String sSearchType_gubun = ServletUtil.getParamString(req,"sSearchType_gubun");
		String sSearchType_use = ServletUtil.getParamString(req,"sSearchType_use");
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sSearchType", sSearchType);
		searchMap.put("sSearchText", sSearchText);	
		searchMap.put("sSearchType_gubun", sSearchType_gubun);	
		searchMap.put("sSearchType_use", sSearchType_use);	
		
		String userAuth = LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		
		String userID = LoginInfo.getUserID(req);
		String groupID =  LoginInfo.getGroupID(req);
		
		//총카운트 		
		int totalCount = senderService.getCountSender(userID, groupID, userAuth, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		return new ModelAndView("/admin/sender/sender_list_proc.jsp?method=list","senderList", senderService.listSender(userID, groupID, userAuth, curPage, iLineCnt, searchMap));
		
	}
	
	
	/**
	 * <p>사용자 추가  (계정을 등록하고 권한도 등록한다.)
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
				
		String senderName= ServletUtil.getParamString(req,"eSenderName");
		String senderEmail = ServletUtil.getParamString(req,"eSenderEmail");
		String senderCellPhone = ServletUtil.getParamString(req,"eSenderCellPhone");
		String userID = LoginInfo.getUserID(req);
		String shareType =  ServletUtil.getParamString(req,"eShareType");	
		String description = ServletUtil.getParamString(req,"eDescription");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		String defaultYN = ServletUtil.getParamString(req, "eDefaultYN");

				 
		//사용자 권한 파라미터 끝 -------------------------------------------------------------------------------------------------------------- 
		Sender sender = new Sender();
		sender.setSenderName(senderName);
		sender.setSenderEmail(senderEmail);
		sender.setSenderCellPhone(senderCellPhone);
		sender.setDescription(description);
		sender.setUserID(userID);
		sender.setDefaultYN(defaultYN);
		sender.setUseYN(useYN);
		sender.setShareType(shareType);

		
	
		
		int resultVal = senderService.insertSender(sender);
		if(resultVal>0){
			this.sCurPage = "1"; 
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		

	}
	
	
	/**
	 * <p>보내는 사람 수정 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		
		
		int senderID = Integer.parseInt(ServletUtil.getParamString(req,"eSenderID"));
		String senderName= ServletUtil.getParamString(req,"eSenderName");
		String senderEmail = ServletUtil.getParamString(req,"eSenderEmail");
		String senderCellPhone = ServletUtil.getParamString(req,"eSenderCellPhone");		
		String userID = LoginInfo.getUserID(req);
		String shareType =  ServletUtil.getParamString(req,"eShareType");	
		String description = ServletUtil.getParamString(req,"eDescription");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		String defaultYN = ServletUtil.getParamString(req, "eDefaultYN");
		
		//사용자 권한 파라미터 끝 -------------------------------------------------------------------------------------------------------------- 
		Sender sender = new Sender();
		sender.setSenderID(senderID);
		sender.setSenderName(senderName);
		sender.setSenderEmail(senderEmail);
		sender.setSenderCellPhone(senderCellPhone);
		sender.setDescription(description);
		sender.setUserID(userID);
		sender.setUseYN(useYN);
		sender.setShareType(shareType);
		sender.setDefaultYN(defaultYN);
				
		
		int resultVal = senderService.updateSender(sender);
		
		if(resultVal>0){
			//this.message = "저장 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		}
		return null;
	}
	
	/**
	 * <p>보내는 사람 삭제 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{			
		
		String[] senderIDS =  req.getParameterValues("eSenderID");					
		
		if(senderIDS==null || senderIDS.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
			
		int resultVal = senderService.deleteSender(senderIDS);
		
		if(resultVal>0){
			//this.message = "삭제 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		
		return null;		
		
	}
	
	/**
	 * <p>보내는 사람  수정창으로 이동한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int senderID = ServletUtil.getParamInt(req,"senderID","0");
		return new ModelAndView("/admin/sender/sender_list_proc.jsp?method=edit","sender", senderService.viewSender(senderID));		
	}
	

}
