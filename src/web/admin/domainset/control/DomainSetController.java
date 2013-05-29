package web.admin.domainset.control;


import web.admin.domainset.service.DomainSetService;
import web.common.util.CommonAccessContoller;
import web.common.util.ServletUtil;
import web.admin.domainset.model.DomainSet; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;



public class DomainSetController extends MultiActionController {
	
	
	private DomainSetService domainSetService = null; 
	private String message = "";
	
	
	public void setDomainSetService(DomainSetService domainSetService){ 
		this.domainSetService = domainSetService; 
	}

	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{					
			return new ModelAndView("/admin/domainset/domainset.jsp");
	}
		
	
	/**
	 * <p>도메인리스트 출력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		String domainFlag = ServletUtil.getParamString(req,"domainFlag");	
		return new ModelAndView("/admin/domainset/domainset_proc.jsp?method=list","domainList", domainSetService.listDomainSet(domainFlag));
	}
	

	/**
	 * <p>도메인설정 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String domainName= ServletUtil.getParamString(req,"eDomainName");
		String domainFlag= ServletUtil.getParamString(req,"eDomainFlag");
		int threadCount = ServletUtil.getParamInt(req,"eThreadCount","0");		
		int socketTimeOut = ServletUtil.getParamInt(req,"eSocketTimeOut","0");
		int socketPerSendCount = ServletUtil.getParamInt(req,"eSocketPerSendCount","0");
		
		if(threadCount==0 || socketTimeOut==0 || socketPerSendCount==0){
			this.message="0인 값을 넣을 수 없습니다.";
			return list(req,res);
		}

				 
		//사용자 권한 파라미터 끝 -------------------------------------------------------------------------------------------------------------- 
		DomainSet domain = new DomainSet();
		domain.setDomainFlag(domainFlag);
		domain.setDomainName(domainName);
		domain.setThreadCount(threadCount);
		domain.setSocketTimeOut(socketTimeOut);
		domain.setSocketPerSendCount(socketPerSendCount);	
		
		
		
		
		DomainSet chkDomain = domainSetService.viewDomainName(domainName);
		if(chkDomain.getDomainName().equals(domainName)) {
			ServletUtil.messageGoURL(res,"동일한 도메인이 이미 등록되어 있습니다.","");
			return null;
		}
		
		int resultVal = domainSetService.insertDomainSet(domain);
		if(resultVal>0){
			res.getWriter().print("O");
			return list(req, res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}
		 
	}
	
	/**
	 * <p>도메인설정 수정 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		int domainID = Integer.parseInt(ServletUtil.getParamString(req,"eDomainID"));
		String domainName= ServletUtil.getParamString(req,"eDomainName");
		int threadCount = ServletUtil.getParamInt(req,"eThreadCount","0");		
		int socketTimeOut = ServletUtil.getParamInt(req,"eSocketTimeOut","0");
		int socketPerSendCount = ServletUtil.getParamInt(req,"eSocketPerSendCount","0");
		String domainFlag = ServletUtil.getParamString(req,"eDomainFlag");
		
		if(threadCount==0 || socketTimeOut==0 || socketPerSendCount==0){
			this.message="0인 값을 넣을 수 없습니다.";
			return list(req,res);
		}
		 
		DomainSet domain = new DomainSet();
		domain.setDomainID(domainID);
		domain.setDomainName(domainName);
		domain.setThreadCount(threadCount);
		domain.setSocketTimeOut(socketTimeOut);
		domain.setSocketPerSendCount(socketPerSendCount);	
		domain.setDomainFlag(domainFlag);
		
		
			
		
		int resultVal = domainSetService.updateDomainSet(domain);
		if(resultVal>0){		
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다");
			return null;	
		}
	  
	}
	
	/**
	 * <p>도메인설정 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public  ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String[] domainIDs = ServletUtil.getParamStringArray(req,"eDomainID");
		
		if(domainIDs==null || domainIDs.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.");
			return null;
		}
		
		int resultVal = domainSetService.deleteDomainSet(domainIDs);
		
		if(resultVal>0){
			//this.message = "삭제 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.");		
		}
		
		return null;	
		
	}
	
	
	/**
	 * <p>도메인 수정창 표시 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{

		int domainID = ServletUtil.getParamInt(req,"domainID","0");
		return new ModelAndView("/admin/domainset/domainset_proc.jsp?method=edit","domain", domainSetService.viewDomain(domainID));
		
	}
	
	public ModelAndView domainName(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String domainName = ServletUtil.getParamString(req,"domainName");
		return new ModelAndView("/admin/domainset/domainset_proc.jsp?method=edit","domain", domainSetService.viewDomainName(domainName));
	}
}
