package web.admin.filtermanager.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


import web.admin.dbjdbcset.model.DbJdbcSet;
import web.admin.filtermanager.model.FilterManager;
import web.admin.filtermanager.service.FilterManagerService;
import web.common.util.Constant;
import web.common.util.DBUtil;
import web.common.util.LoginInfo;
import web.common.util.ServletUtil;

public class FilterManagerController extends MultiActionController {
	private FilterManagerService filterManagerService = null;
	private String sCurPage = null;
	private String message = "";
	
	public void setFilterManagerService(FilterManagerService filterManagerService) {
		this.filterManagerService = filterManagerService;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/filter/filter_manager.jsp");
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

		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		String sSearchSelectType = ServletUtil.getParamString(req,"sSearchSelectType");
		String sSearchSelect = ServletUtil.getParamString(req,"sSearchSelect");
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("searchSelectType", sSearchSelectType);
		searchMap.put("searchSelect", sSearchSelect);
		searchMap.put("curPage", curPage);
		searchMap.put("iLineCnt", iLineCnt);

		//총카운트 		
		int iTotalCnt = filterManagerService.getListTotalCount(searchMap);

		
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));	
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		return new ModelAndView("/admin/filter/filter_manager_proc.jsp?method=list","filterManagerlist", filterManagerService.listFilterManager(searchMap));
		
	}
	
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		//메뉴아이디 세팅 
	   // ServletUtil.meunParamSetting(req);	
	    String filterID = ServletUtil.getParamString(req,"filterID");	
	    
    
	     return new ModelAndView("/admin/filter/filter_manager_proc.jsp?method=edit","filterManager", filterManagerService.viewFilterManager(filterID));
	 }
	
	/**
	 * <p>tm_dbset에 인서트
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{
		 
		  String content = ServletUtil.getParamString(req,"eContent");
		  String description = ServletUtil.getParamString(req,"eDescription");
		  String filterType = ServletUtil.getParamString(req,"eFilterType");
		  String contentType = ServletUtil.getParamString(req,"eContentType");
		  String filterLevel = ServletUtil.getParamString(req,"eFilterLevel");
		  String userID = LoginInfo.getUserID(req);
			
			
			FilterManager filterManager = new FilterManager();
			filterManager.setContent(content);
			filterManager.setDescription(description);
			filterManager.setFilterType(Integer.parseInt(filterType));
			filterManager.setContentType(Integer.parseInt(contentType));
			filterManager.setFilterLevel(Integer.parseInt(filterLevel));
			filterManager.setUserID(userID);
			
						
			int resultVal = filterManagerService.insert(filterManager);			
			if(resultVal>0){
				//this.message = "저장 되었습니다.";
				this.sCurPage = "1"; 
				return list(req,res);
			}
			ServletUtil.messageGoURL(res,"추가에 실패했습니다","");
			return null;
	}
	
	@SuppressWarnings("unused")
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{
			
		String content = ServletUtil.getParamString(req,"eContent");
		
		  String description = ServletUtil.getParamString(req,"eDescription");
		  String filterType = ServletUtil.getParamString(req,"eFilterType");
		  String contentType = ServletUtil.getParamString(req,"eContentType");
		 
		  String filterLevel = ServletUtil.getParamString(req,"eFilterLevel");
		  String userID = LoginInfo.getUserID(req);
		  String filterID = ServletUtil.getParamString(req,"eFilterID");
		  
		  String mainMenuID = ServletUtil.getParamString(req,"mainMenuID");
		  String subMenuID = ServletUtil.getParamString(req,"subMenuID");

	  
		  
		 
			
		  FilterManager filterManager = new FilterManager();
			filterManager.setContent(content);
			filterManager.setDescription(description);
			filterManager.setFilterType(Integer.parseInt(filterType));
			filterManager.setContentType(Integer.parseInt(contentType));
			filterManager.setFilterLevel(Integer.parseInt(filterLevel));
			filterManager.setUserID(userID);
			filterManager.setFilterID(Integer.parseInt(filterID));
			
			int resultVal = filterManagerService.update(filterManager);			
			if(resultVal>0){
				
				this.sCurPage = "1"; 
				return list(req,res);
			}
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;		
	}
	
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{	

		String[] filterIDs =  req.getParameterValues("eFilterID");
		if(filterIDs == null || filterIDs.length==0) {
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		int resultVal = 0;
		
		
		for(String filterID : filterIDs){
			resultVal = filterManagerService.delete(filterID);
		}
		
		
		if(resultVal>0){			
			//this.message = "삭제 되었습니다.";
			return list(req,res);
		}
		else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
		}	
		return null;	
	}

}
