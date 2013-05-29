package web.admin.persistdomain.control;



import java.util.HashMap;
import java.util.Map;
import web.admin.persistdomain.service.PersistDomainService;
import web.common.util.ServletUtil;
import web.common.util.LoginInfo;
import web.admin.persistdomain.model.PersistDomain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;



public class PersistDomainController extends MultiActionController {
	
	private PersistDomainService persistDomainService = null; 
	private String sCurPage = null;
	private String message = "";
	
	public void setPersistDomainService(PersistDomainService persistDomainService){ 
		this.persistDomainService = persistDomainService; 
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/persistdomain/persistdomain_list.jsp");
	}
	
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{				
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		if (curPage <= 0) curPage = 1;
				
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
		int totalCount = persistDomainService.getCount(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		return new ModelAndView("/admin/persistdomain/persistdomain_list_proc.jsp?method=list","persistDomainList", persistDomainService.list(curPage, iLineCnt, searchMap));
		
	}
	
	
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int domainID = ServletUtil.getParamInt(req,"domainID","0");
		return new ModelAndView("/admin/persistdomain/persistdomain_list_proc.jsp?method=edit","persistDomain", persistDomainService.view(domainID));		
	}
	
	
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		
		String domain_name= ServletUtil.getParamString(req,"eDomainName");
		String description = ServletUtil.getParamString(req,"eDescription");
		String userID = LoginInfo.getUserID(req);		
		String useYN = ServletUtil.getParamString(req,"eUseYN");

				 
	
		PersistDomain persistDomain = new PersistDomain();
		persistDomain.setDomain_name(domain_name);
		persistDomain.setDescription(description);
		persistDomain.setUserID(userID);
		persistDomain.setUseYN(useYN);
		
		
		int resultVal = persistDomainService.insert(persistDomain);
		if(resultVal>0){
			this.sCurPage = "1"; 
			return list(req,res);
		}else if(resultVal==-1){
			ServletUtil.messageGoURL(res,"입력한 도메인은 이미 등록되어 있습니다.","");
			return null;	
		}else {
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
		
	}
	
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		
		
		int domainID = Integer.parseInt(ServletUtil.getParamString(req,"eDomainID"));
		String domain_name= ServletUtil.getParamString(req,"eDomainName");
		String description = ServletUtil.getParamString(req,"eDescription");
		String userID = LoginInfo.getUserID(req);		
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		 
		
		PersistDomain persistDomain = new PersistDomain();
		persistDomain.setDomain_name(domain_name);
		persistDomain.setDescription(description);
		persistDomain.setUserID(userID);
		persistDomain.setUseYN(useYN);
		persistDomain.setDomainID(domainID);
				
		
		int resultVal = persistDomainService.update(persistDomain);
		
		if(resultVal>0){
			//this.message = "저장 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		}
		return null;
	}
	
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{			
		
		String[] domainIDs =  req.getParameterValues("eDomainID");					
		
		if(domainIDs==null || domainIDs.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		int resultVal = persistDomainService.delete(domainIDs);
		
		if(resultVal>0){
			//this.message = "삭제 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		
		return null;		
		
	}

}
