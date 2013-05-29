package web.admin.targetgroup.control;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.admin.massmailgroup.model.MassMailGroup;
import web.admin.massmailgroup.service.MassMailGroupService;
import web.admin.targetgroup.model.TargetGroup;
import web.admin.targetgroup.service.TargetGroupService;
import web.common.util.ServletUtil;


/**
 * 대량메일 그룹 관리 controller
 * @author limyh(임영호)
 *
 */
public class TargetGroupController extends MultiActionController{
	
	private TargetGroupService targetGroupService = null;
	private String sCurPage = null;
	private String message = "";
	public void setTargetGroupService(TargetGroupService targetGroupService){
		this.targetGroupService = targetGroupService;
	}
	
	/**
	 * <p>대량 메일 그룹 리스트를 출력한다. 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */

	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/targetgroup/target_group.jsp");
	}

	public ModelAndView list(HttpServletRequest req, HttpServletResponse res){	
		
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

		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("searchSelectType", sSearchSelectType);
		searchMap.put("searchSelect", sSearchSelect);
		searchMap.put("curPage", curPage);
		searchMap.put("iLineCnt", iLineCnt);

		//총카운트 		
		int iTotalCnt = targetGroupService.getTargetGroupTotalCount(searchMap);
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));	
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		return new ModelAndView("/admin/targetgroup/target_group_proc.jsp?id=targetgroup&method=list","targetgroupList", targetGroupService.listTargetGroup(searchMap));
	
	}
	
	/**
	 * 대량메일그룹 입력
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{	
	
		String targetGroupName = ServletUtil.getParamString(req,"eTargetGroupName");
		String description = ServletUtil.getParamString(req,"eDescription");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		String isDefault = ServletUtil.getParamString(req,"eIsDefault");
		
		TargetGroup targetgroup = new TargetGroup(); 
		targetgroup.setTargetGroupName(targetGroupName);
		targetgroup.setDescription(description);
		targetgroup.setUseYN(useYN);
		targetgroup.setIsDefault(isDefault);
		// 중복 id 체크
	
		TargetGroup chktargetgroup = targetGroupService.viewTargetGroupChk(targetGroupName);
		
		if(chktargetgroup.getTargetGroupName() != null) {
			ServletUtil.messageGoURL(res,"동일한 그룹명이 이미 존재합니다.","");
			return null;
		}
		
		int resultVal = targetGroupService.insertTargetGroup(targetgroup); 
		
		if(resultVal>0){
			//this.message = "저장 되었습니다.";
			this.sCurPage = "1"; 
			return list(req,res);
		}
		ServletUtil.messageGoURL(res,"저장에 실패하였습니다.");
		return null;
	}
	
	/**
	 * 대량메일그룹 수정
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		
		int targetGroupID = ServletUtil.getParamInt(req, "eTargetGroupID","0");
		String targetGroupName = ServletUtil.getParamString(req,"eTargetGroupName");
		String description = ServletUtil.getParamString(req,"eDescription");
		String useYN = ServletUtil.getParamString(req,"eUseYN");
		String isDefault = ServletUtil.getParamString(req,"eIsDefault");
		
		TargetGroup targetgroup = new TargetGroup(); 
		targetgroup.setTargetGroupID(targetGroupID);
		targetgroup.setTargetGroupName(targetGroupName);
		targetgroup.setDescription(description);
		targetgroup.setUseYN(useYN);
		targetgroup.setIsDefault(isDefault);
		
		int resultVal = targetGroupService.updateTargetGroup(targetgroup); 
		
		if(resultVal>0){
			//this.message = "저장 되었습니다.";
			return list(req,res);
		}
		ServletUtil.messageGoURL(res,"저장에 실패했습니다");
		return null;
	}
	
	/**
	 * 대량메일그룹 삭제
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] targetGroupIDs =  req.getParameterValues("eTargetGroupID");//ServletUtil.getParamStringArray(req,"checkbox"); 	
		
		if(targetGroupIDs == null || targetGroupIDs.length==0) {
			
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		int resultVal = targetGroupService.deleteTargetGroup(targetGroupIDs);
			
		if(resultVal>0){
			
			//this.message = "삭제 되었습니다.";
			return list(req,res);
		}
		else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다");
		}	
		return null;	
	}
	
	/**
	 * <p>대량메일그룹 등록 / 수정창으로 이동한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int targetGroupID = ServletUtil.getParamInt(req,"targetGroupID","0");
		String id = ServletUtil.getParamString(req, "id");
		
		return new ModelAndView("/admin/targetgroup/target_group_proc.jsp?id="+id+"&method=edit","targetgroup", targetGroupService.viewTargetGroup(targetGroupID));
		
	}

}
