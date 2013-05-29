package web.admin.persistfail.control;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import web.admin.persistfail.service.*;
import web.common.util.ServletUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


public class PersistFailController extends MultiActionController{
	private PersistFailService persistFailService = null;
	private String sCurPage = null;
	private String message = "";
	
	public void setPersistFailService(PersistFailService persistFailService){
		this.persistFailService = persistFailService;
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
		return new ModelAndView("/admin/persistfail/persistfail.jsp");
	}
	
	
	
	/**
	 * <p>영구적인 메일리스트 
	 * @param req
	 * @param res
	 * @return
	 */
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

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));

		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		String dateStart = ServletUtil.getParamString(req,"eDateStart");
		String dateEnd = ServletUtil.getParamString(req,"eDateEnd");

		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);

		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		
		searchMap.put("dateStart", dateStart+" 00:00:00");
		searchMap.put("dateEnd", dateEnd+" 23:59:59");		
;

		//총카운트 		
		int iTotalCnt = persistFailService.totalCountPersistFailMail(searchMap);		
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));

		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		return new ModelAndView("/admin/persistfail/persistfail_proc.jsp?method=list","persistfailList", persistFailService.listPersistFailMail(curPage, iLineCnt, searchMap));
	
	}

	
	/**
	 * <p>삭제처리 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String[] sendIDS =  req.getParameterValues("ePersistfailID");	
		if(sendIDS==null || sendIDS.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		Map<String, Object>[] maps = new HashMap[sendIDS.length];
		for(int i=0;i<sendIDS.length;i++){
			maps[i] = new HashMap<String, Object>();
			maps[i].put("persistfailID", new BigDecimal(sendIDS[i]));
		}
		
			
		int[] resultVal = persistFailService.deletePersistFailMail(maps);
		
		if(resultVal!=null){
			//this.message = "삭제 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		
		return null;		
	}
	
	
}
