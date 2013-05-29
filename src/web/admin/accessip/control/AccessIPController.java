package web.admin.accessip.control;



import java.util.HashMap;
import java.util.Map;
import web.admin.accessip.service. AccessIPService;
import web.admin.accessip.model.*;
import web.common.util.ServletUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;



public class AccessIPController extends MultiActionController {
	
	private  AccessIPService accessIPService = null; 
	private String sCurPage = null;
	private String message = "";
	
	public void setAccessIPService( AccessIPService accessIPService){ 
		this.accessIPService = accessIPService; 
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/accessip/accessip.jsp");
	}
	
	
	/**
	 * <p>접근IP관리 리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 * @throws DataAccessException
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
		} catch( Exception e ) {}
		

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		String sUseYN = ServletUtil.getParamString(req,"sUseYN");
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sSearchType", sSearchType);
		searchMap.put("sSearchText", sSearchText);	
		searchMap.put("sUseYN", sUseYN);
		
		
		//총카운트 		
		int totalCount = accessIPService.getAccessIPTotalCount(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		return new ModelAndView("/admin/accessip/accessip_proc.jsp?method=list","accessipList", accessIPService.listAccessIP(curPage, iLineCnt, searchMap));
		
	}
	
	/**
	 * <p>접근IP관리 수정창으로 이동한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int accessipID = ServletUtil.getParamInt(req,"accessipID","0");
		return new ModelAndView("/admin/accessip/accessip_proc.jsp?mode=edit","accessIP",accessIPService.viewAccessIP(accessipID));
		
	}
	
	/**
	 * <p>접근IP관리 추가
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String octetA = ServletUtil.getParamString(req,"eOctetA");
		String octetB = ServletUtil.getParamString(req,"eOctetB");
		String octetC = ServletUtil.getParamString(req,"eOctetC");
		String octetD = ServletUtil.getParamString(req,"eOctetD");
		String description = ServletUtil.getParamString(req,"eDescription");
		String useYN =  ServletUtil.getParamString(req,"eUseYN");
		String userID = ServletUtil.getParamString(req, "eUserID");
		String octetType = "4";
		if(octetD.equals("")){
			if(octetC.equals("")){
				octetType="2";
			}else{
				octetType="3";	
			}
		}
		AccessIP accessIP = new AccessIP();
		accessIP.setOctetA(octetA);
		accessIP.setOctetB(octetB);
		accessIP.setOctetC(octetC);
		accessIP.setOctetD(octetD);
		accessIP.setDescription(description);
		accessIP.setOctetType(octetType);
		accessIP.setUseYN(useYN);
		accessIP.setUserID(userID);
		int resultVal =  accessIPService.insertAccessIP(accessIP);
		if(resultVal == 0){
			ServletUtil.messageGoURL(res,"저장에 실패 하였습니다.","");
		}else{
			this.sCurPage = "1"; 
			return list(req,res);
		}
		return null;
	}
	
	/**
	 * <p>접근IP관리 수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int accessipID = ServletUtil.getParamInt(req,"accessipID","0");
		String octetA = ServletUtil.getParamString(req,"eOctetA");
		String octetB = ServletUtil.getParamString(req,"eOctetB");
		String octetC = ServletUtil.getParamString(req,"eOctetC");
		String octetD = ServletUtil.getParamString(req,"eOctetD");
		String description = ServletUtil.getParamString(req,"eDescription");
		String useYN =  ServletUtil.getParamString(req,"eUseYN");
		String userID = ServletUtil.getParamString(req, "eUserID");
		String octetType = "4";
		if(octetD.equals("")){
			if(octetC.equals("")){
				octetType="2";
			}else{
				octetType="3";	
			}
		}
		AccessIP accessIP = new AccessIP();
		accessIP.setAccessipID(accessipID);
		accessIP.setOctetA(octetA);
		accessIP.setOctetB(octetB);
		accessIP.setOctetC(octetC);
		accessIP.setOctetD(octetD);
		accessIP.setDescription(description);
		accessIP.setOctetType(octetType);
		accessIP.setUseYN(useYN);
		accessIP.setUserID(userID);
		int resultVal =  accessIPService.updateAccessIP(accessIP);
		
		if(resultVal == 0){
			ServletUtil.messageGoURL(res,"저장에 실패 하였습니다.","");
		}else{
			return list(req,res);
		}
		return null;
	}
	
	/**
	 * <p>접근IP관리 설정을 삭제한다.
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		
		String[] accessipIDs =  req.getParameterValues("eAccessipID");		
	
		if(accessipIDs==null || accessipIDs.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		int resultVal = accessIPService.deleteAccessIP(accessipIDs);

	
		if(resultVal>0){
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		
		return null;
		
		
	}
}
