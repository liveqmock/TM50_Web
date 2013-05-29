package web.admin.systemset.control;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.admin.systemset.service.SystemSetService;
import web.admin.systemset.model.SystemSet;
import web.common.util.ServletUtil;

public class SystemSetController extends MultiActionController{
	
	
	private SystemSetService systemSetService = null ; 
	private String message = "";
	
	public void setSystemSetService(SystemSetService systemSetService) {
		this.systemSetService = systemSetService;
	}
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		return new ModelAndView("/admin/systemset/system_set.jsp");
	}
	
	
	/**
	 * <p>환경설정을 표시한다. 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		String configFlag = ServletUtil.getParamString(req,"configFlag");		
		return new ModelAndView("/admin/systemset/system_set_proc.jsp","systemSetList", systemSetService.listSystemSet(configFlag));

	}
	
	

	
	/**
	 * <p>환경설정을 업데이트 한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateSystemSet(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int itemCount = ServletUtil.getParamInt(req,"itemCount","0");
		List<SystemSet> arrSystemSet = new ArrayList<SystemSet>(); 
		
		for(int i=0; i<itemCount; i++){ 
			SystemSet systemSet = new SystemSet();		
			systemSet.setConfigID(ServletUtil.getParamInt(req,"configID"+i,"0")); 
			systemSet.setConfigValue(ServletUtil.getParamStringDefault(req,"configValue"+i,"")); 
			arrSystemSet.add(systemSet); 
		}
		
		
		int[] resultVal = systemSetService.updateSystemSet(arrSystemSet); 

		if(resultVal !=null){ 
			//this.message = "저장 되었습니다.";
			return list(req,res);
		}else{ 
			ServletUtil.messageGoURL(res,"수정에 실패했습니다","");
			return null;
		}
	}
	

	/**
	 * <p>환경설정을 업데이트 한다. (configFlag, configName 사용)
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateConfigValue(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String configFlag = ServletUtil.getParamString(req,"configFlag");
		String configName = ServletUtil.getParamString(req,"configName");
		String configValue = ServletUtil.getParamString(req,"configValue");
		
		int resultVal = systemSetService.updateConfigValue(configFlag, configName, configValue);
		if(resultVal == 0){ 
			ServletUtil.messageGoURL(res,"수정에 실패했습니다","");
		}
		
		return null;
	}
	
}
