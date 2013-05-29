package web.content.mailtemplate.control;


import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.common.util.LoginInfo;
import web.common.util.ServletUtil;
import web.content.mailtemplate.model.MailTemplate;
import web.content.mailtemplate.service.MailTemplateService;



public class MailTemplateController extends MultiActionController{
	
	private MailTemplateService mailTemplateService = null;
	private String sCurPage = null;
	private String message = "";
	
	public void setMailTemplateService(MailTemplateService mailTemplateService){
		this.mailTemplateService = mailTemplateService;
	}
	

	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/pages/content/mailtemplate/mailtemplate.jsp");
	}
	
	
	/**
	 * <p>리스트 출력 
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
		String sUseYN = ServletUtil.getParamString(req,"sUseYN");
		String sSelectedGroupID = ServletUtil.getParamString(req,"sSelectedGroupID");

		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sSearchType", sSearchType);
		searchMap.put("sSearchText", sSearchText);
		searchMap.put("sUseYN", sUseYN);	
		searchMap.put("sSelectedGroupID", sSelectedGroupID);	
		String userAuth = LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		
		String userID = LoginInfo.getUserID(req);
		String groupID =  LoginInfo.getGroupID(req);
		
	
		
		//총카운트 		
		int totalCount =mailTemplateService.getCountMailTemplate(userID, groupID, userAuth, searchMap);		
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		return new ModelAndView("/pages/content/mailtemplate/mailtemplate_proc.jsp?method=list","mailTemplateList", mailTemplateService.listMailTemplate(userID, groupID, userAuth, curPage, iLineCnt, searchMap));
		
	}
	
	
	/**
	 * <p>템플릿 추가 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
	
		String templateName= ServletUtil.getParamString(req,"eTemplateName");
		String templateContent = ServletUtil.getParamString(req,"eTemplateContent");	
		String userID = LoginInfo.getUserID(req);
		String shareGroupID =  ServletUtil.getParamString(req,"selectedGroupID");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		String templateType = ServletUtil.getParamString(req,"eTemplateType");
				 
		
		MailTemplate mailTemplate = new MailTemplate();
		mailTemplate.setTemplateName(templateName);
		mailTemplate.setTemplateContent(templateContent);
		mailTemplate.setUserID(userID);
		mailTemplate.setShareGroupID(shareGroupID);
		mailTemplate.setUseYN(useYN);
		mailTemplate.setTemplateType(templateType);
			
	
		
		int resultVal = mailTemplateService.insertMailTemplate(mailTemplate);
		if(resultVal>0){
			this.sCurPage = "1"; 
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
	}
	
	/**
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception {		
				
		String templateName= ServletUtil.getParamString(req,"eTemplateName");
		String templateContent = ServletUtil.getParamString(req,"eTemplateContent");
		String userID = LoginInfo.getUserID(req);		
		String shareGroupID =  ServletUtil.getParamString(req,"selectedGroupID");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		int templateID= ServletUtil.getParamInt(req,"eTemplateID","0");
		String templateType = ServletUtil.getParamString(req,"eTemplateType");
				 
		//사용자 권한 파라미터 끝 -------------------------------------------------------------------------------------------------------------- 
		MailTemplate mailTemplate = new MailTemplate();
		mailTemplate.setTemplateName(templateName);
		mailTemplate.setTemplateContent(templateContent);
		mailTemplate.setUserID(userID);
		mailTemplate.setShareGroupID(shareGroupID);
		mailTemplate.setUseYN(useYN);
		mailTemplate.setTemplateID(templateID);
		mailTemplate.setTemplateType(templateType);
			
		
		int resultVal = mailTemplateService.updateMailTemplate(mailTemplate);
		if(resultVal>0){
			this.sCurPage = "1"; 
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}		
	}
	
	/**
	 * <p>삭제처리 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String[] templateID =  req.getParameterValues("eTemplateID");	
		if(templateID==null || templateID.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		Map<String, Object>[] maps = new HashMap[templateID.length];
		for(int i=0;i<templateID.length;i++){
			maps[i] = new HashMap<String, Object>();
			maps[i].put("templateID", new Integer(templateID[i]));
		}		
			
		int[] resultVal = mailTemplateService.deleteMailTemplate(maps);
		
		if(resultVal!=null){
			//this.message = "삭제 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		
		return null;		
	}
	
	
	/**
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int templateID = ServletUtil.getParamInt(req,"eTemplateID","0");
		String shareGroupID =  ServletUtil.getParamString(req,"shareGroupID");
				
		return new ModelAndView("/pages/content/mailtemplate/mailtemplate_proc.jsp?method=edit&shareGroupID="+shareGroupID,"mailTemplate", mailTemplateService.viewMailTemplate(templateID));		
	}
}
