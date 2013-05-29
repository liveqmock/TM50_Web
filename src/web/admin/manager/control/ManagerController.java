package web.admin.manager.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.admin.manager.service.*;
import web.common.util.FileUploadUtil;
import web.common.util.ServletUtil;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


public class ManagerController extends MultiActionController{
	private ManagerService managerService = null;

	
	public void setManagerService(ManagerService managerService){
		this.managerService = managerService;
	}
	
	/**
	 * <p>Manager 기본 화면으로 이동한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */

	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
		String id = ServletUtil.getParamString(req,"id");
		return new ModelAndView("/admin/manager/manager.jsp?id="+id);
	}
	
	/**
	 * <p>Engine 리스트
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res){	
		
		//메뉴아이디 세팅  
		String id = ServletUtil.getParamString(req,"id");
		ServletUtil.meunParamSetting(req);
		return new ModelAndView("/admin/manager/manager_proc.jsp?mode=enginelist&id="+id,"engineList", managerService.listEngine());
	
	}
	/**
	 * <p>로그 파일을 다운로드 한다.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception D:\EZMAIL\EzMail1.0_Engine\masslogs\ez_massmail.log.2009-04-14
	 */
	public ModelAndView enginStatusUpdate(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String engineID = ServletUtil.getParamString(req,"engineID");
		String engineStatus= ServletUtil.getParamString(req,"engineStatus");
		
		if (engineStatus.equals("stop")){
			engineStatus = "2";
		}else{
			engineStatus = "1";
		}
		String id = ServletUtil.getParamString(req,"id");
		int resultVal = managerService.enginStatusUpdate(engineID, engineStatus);
		if(resultVal>0){
			return new ModelAndView("/admin/manager/manager_proc.jsp?mode=enginelist&id="+id,"engineList", managerService.listEngine());
		}else{
			ServletUtil.messageGoURL(res,"상태값 변경에  실패했습니다","");
			return null;
		}
	}
	
	/**
	 * <p>로그 파일을 다운로드 한다.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception D:\EZMAIL\EzMail1.0_Engine\masslogs\ez_massmail.log.2009-04-14
	 */
	public void fileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String logpath = ServletUtil.getParamString(req,"logpath");
		FileUploadUtil.downloadFile(req, res, logpath);
		
	}
	

	/**
	 * 발송중인 메일이 있는지 체크, 있으면 true, 없으면 false
	 * 발송중 : 실발송중(상태값 14), 오류자 재발송중(상태값 16)
	 */
	public ModelAndView checkSend(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String status = ServletUtil.getParamString(req,"status");
		String engine = ServletUtil.getParamString(req,"engine");
		String id = ServletUtil.getParamString(req,"id");
		
		if(!managerService.isSendMassMail()){
			return new ModelAndView("/admin/manager/manager_proc.jsp?id="+id+"&mode=enginStartStop&os=windows"+"&status="+status+"&engine="+engine);
		}else{
			ServletUtil.messageGoURL(res,"발송중인 메일이 있습니다.","");
			return null;
		}
	}
}
