package web.admin.targetmanager.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.admin.targetmanager.model.TargetUIManager;
import web.admin.targetmanager.model.TargetUIManagerSelect;
import web.admin.targetmanager.model.TargetUIManagerWhere;
import web.admin.targetmanager.service.TargetManagerService;
import web.common.util.DBUtil;
import web.common.util.LoginInfo;
import web.common.util.QueryUtil;
import web.common.util.ServletUtil;

public class TargetManagerController extends MultiActionController {
	
	private TargetManagerService targetManagerService = null;
	private String sCurPage = null;
	private String message = "";
	
	public void setTargetManagerService(TargetManagerService targetManagerService)
	{
		this.targetManagerService = targetManagerService;
	}
	
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/targetUImanager/target_manager.jsp");
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
		
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("curPage", curPage);
		searchMap.put("iLineCnt", iLineCnt);

		//총카운트 		
		int iTotalCnt = targetManagerService.getCount(searchMap);

		
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));	
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		return new ModelAndView("/admin/targetUImanager/target_manager_proc.jsp?method=list","targetmanagerlist", targetManagerService.list(searchMap));
	}
	
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int targetUIManagerID = ServletUtil.getParamInt(req,"targetUIManagerID","0");	
		return new ModelAndView("/admin/targetUImanager/target_manager_proc.jsp?method=edit&targetUIManagerID="+targetUIManagerID,"targetmanager", targetManagerService.view(targetUIManagerID));
	}
	
	
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception
	{
		TargetUIManager targetUIManager = new TargetUIManager();
		
		int resultval = 0;
		int targetUIManagerID = 0;
		String selectQuery = "select ";
		List<TargetUIManagerSelect> targetUIManagerSelectList = new ArrayList<TargetUIManagerSelect>();
		int selectCount = ServletUtil.getParamInt(req, "eSelectCount", "0");
		if(selectCount!=0)
		{	
			for(int i=1; i<=selectCount;i++)
			{
				if(!ServletUtil.getParamString(req, "eSelectFieldName"+i).equals(""))
				{
					TargetUIManagerSelect targetUIManagerSelect = new TargetUIManagerSelect();
					targetUIManagerSelect.setSelectFieldName(ServletUtil.getParamString(req, "eSelectFieldName"+i));
					if(i==selectCount)
						selectQuery += ServletUtil.getParamString(req, "eSelectFieldName"+i)+ " ";
					else
						selectQuery += ServletUtil.getParamString(req, "eSelectFieldName"+i)+ ", ";
					targetUIManagerSelect.setOnetooneID(ServletUtil.getParamInt(req,"eOneToOne"+i, "0"));
					targetUIManagerSelect.setSelectDescription(ServletUtil.getParamString(req, "eSelectDescript"+i));
					targetUIManagerSelect.setCsvColumnPos(i);
					targetUIManagerSelect.setSelectID(i);
					targetUIManagerID = targetManagerService.getMaxID();
					targetUIManagerSelect.setTargetUIManagerID(targetUIManagerID+1);
					targetUIManagerSelectList.add(targetUIManagerSelect);	
				}
				
			}
		}
		else
		{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
			return null;
		}
		
		
		targetUIManager.setTargetUIManagerName(ServletUtil.getParamString(req,"eTargetUIManagerName"));
		targetUIManager.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetUIManager.setDbID(ServletUtil.getParamInt(req,"eDbID", "0"));
		targetUIManager.setSelectText(selectQuery);
		targetUIManager.setFromText(ServletUtil.getParamString(req,"eFromText"));
		targetUIManager.setWhereText(ServletUtil.getParamString(req,"eWhereText"));
		targetUIManager.setUseYN(ServletUtil.getParamString(req,"eUseYN"));
		targetUIManager.setDefaultYN(ServletUtil.getParamString(req,"eDefaultYN"));
		targetUIManager.setUserID(LoginInfo.getUserID(req));
		
		
		synchronized(this){
			resultval = targetManagerService.insertTargetManagerInfo(targetUIManager);
			if(resultval>0)
			{
				targetUIManagerID = targetManagerService.getMaxID();
				
				
				if(selectCount!=0)
				{	
					targetManagerService.insertTargetManagerSelect(targetUIManagerSelectList);
				}
				
				int whereCount = ServletUtil.getParamInt(req, "eWhereCount", "0");
				
				if(resultval >0 && whereCount!=0)
				{
					List<TargetUIManagerWhere> targetUIManagerWhereList = new ArrayList<TargetUIManagerWhere>();
					for(int i=1; i<=whereCount;i++)
					{
						if(!ServletUtil.getParamString(req, "eWhereFieldName"+i).equals(""))
						{
							TargetUIManagerWhere targetUIManagerWhere = new TargetUIManagerWhere();
							targetUIManagerWhere.setWhereFieldName(ServletUtil.getParamString(req, "eWhereFieldName"+i));
							targetUIManagerWhere.setWhereUIName(ServletUtil.getParamString(req, "eWhereName"+i));
							targetUIManagerWhere.setWhereType(ServletUtil.getParamInt(req, "eWhereType"+i, "0"));
							targetUIManagerWhere.setWhereID(i);
							targetUIManagerWhere.setTargetUIManagerID(targetUIManagerID);
							targetUIManagerWhere.setCheckName("");
							targetUIManagerWhere.setCheckValue("");
							targetUIManagerWhere.setPeriodStartType("");
							targetUIManagerWhere.setPeriodEndType("");
							targetUIManagerWhere.setDescription("");
							targetUIManagerWhere.setExceptYN("N");
							targetUIManagerWhere.setDataType("string");
							
							if(targetUIManagerWhere.getWhereType()==1)
							{
								targetUIManagerWhere.setCheckName(ServletUtil.getParamString(req, "eWhere1Value"+i));
								targetUIManagerWhere.setCheckValue(ServletUtil.getParamString(req, "eWhere2Value"+i));
							}
							else if(targetUIManagerWhere.getWhereType()==2)
							{
								targetUIManagerWhere.setPeriodStartType(ServletUtil.getParamString(req, "eWhere1Value"+i));
								targetUIManagerWhere.setPeriodEndType(ServletUtil.getParamString(req, "eWhere2Value"+i));
							}
							
							
							targetUIManagerWhereList.add(targetUIManagerWhere);	
						}
						
					}
					
					
					targetManagerService.insertTargetManagerWhere(targetUIManagerWhereList);
					targetManagerService.createGeneralTable(targetUIManagerID);
					
				}
				else
				{
					resultval = -1;
				}
			}
		}
		
		
		if(resultval >0 && targetUIManagerID > 0)
		{
			this.sCurPage = "1";
			return list(req,res);	
		}
		else
		{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
			return null;
		}
		
	}
	
	
	
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception
	{
		TargetUIManager targetUIManager = new TargetUIManager();
		
		int resultval = 0;
		int targetUIManagerID = ServletUtil.getParamInt(req,"targetManagerID", "0");
		String selectQuery = "select ";
		List<TargetUIManagerSelect> targetUIManagerSelectList = new ArrayList<TargetUIManagerSelect>();
		int selectCount = ServletUtil.getParamInt(req, "eSelectCount", "0");
		
		if(selectCount!=0)
		{	
			for(int i=1; i<=selectCount;i++)
			{
				if(!ServletUtil.getParamString(req, "eSelectFieldName"+i).equals(""))
				{
					TargetUIManagerSelect targetUIManagerSelect = new TargetUIManagerSelect();
					targetUIManagerSelect.setSelectFieldName(ServletUtil.getParamString(req, "eSelectFieldName"+i));
					if(i==selectCount)
						selectQuery += ServletUtil.getParamString(req, "eSelectFieldName"+i)+ " ";
					else
						selectQuery += ServletUtil.getParamString(req, "eSelectFieldName"+i)+ ", ";
					targetUIManagerSelect.setOnetooneID(ServletUtil.getParamInt(req,"eOneToOne"+i, "0"));
					targetUIManagerSelect.setSelectDescription(ServletUtil.getParamString(req, "eSelectDescript"+i));
					targetUIManagerSelect.setCsvColumnPos(i);
					targetUIManagerSelect.setSelectID(i);
					targetUIManagerSelect.setTargetUIManagerID(targetUIManagerID);
					targetUIManagerSelectList.add(targetUIManagerSelect);	
				}
				
			}
		}
		
		
		targetUIManager.setTargetUIManagerName(ServletUtil.getParamString(req,"eTargetUIManagerName"));
		targetUIManager.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetUIManager.setDbID(ServletUtil.getParamInt(req,"eDbID", "0"));
		targetUIManager.setSelectText(selectQuery);
		targetUIManager.setFromText(ServletUtil.getParamString(req,"eFromText"));
		targetUIManager.setWhereText(ServletUtil.getParamString(req,"eWhereText"));
		targetUIManager.setUseYN(ServletUtil.getParamString(req,"eUseYN"));
		targetUIManager.setDefaultYN(ServletUtil.getParamString(req,"eDefaultYN"));
		targetUIManager.setUserID(LoginInfo.getUserID(req));
		targetUIManager.setTargetUIManagerID(targetUIManagerID);
		
		
		synchronized(this){
			resultval = targetManagerService.updateTargetManagerInfo(targetUIManager);
			if(resultval>0)
			{
				System.out.println("2");
				
				if(selectCount!=0)
				{	targetManagerService.deleteTargetManagerSelect(targetUIManagerID);
					targetManagerService.insertTargetManagerSelect(targetUIManagerSelectList);
				}
				System.out.println("3");
				int whereCount = ServletUtil.getParamInt(req, "eWhereCount", "0");
				
				if(resultval >0 && whereCount!=0)
				{	
					List<TargetUIManagerWhere> targetUIManagerWhereList = new ArrayList<TargetUIManagerWhere>();
					for(int i=1; i<=whereCount;i++)
					{
						if(!ServletUtil.getParamString(req, "eWhereFieldName"+i).equals(""))
						{
							TargetUIManagerWhere targetUIManagerWhere = new TargetUIManagerWhere();
							targetUIManagerWhere.setWhereFieldName(ServletUtil.getParamString(req, "eWhereFieldName"+i));
							targetUIManagerWhere.setWhereUIName(ServletUtil.getParamString(req, "eWhereName"+i));
							targetUIManagerWhere.setWhereType(ServletUtil.getParamInt(req, "eWhereType"+i, "0"));
							targetUIManagerWhere.setWhereID(i);
							targetUIManagerWhere.setTargetUIManagerID(targetUIManagerID);
							targetUIManagerWhere.setCheckName("");
							targetUIManagerWhere.setCheckValue("");
							targetUIManagerWhere.setPeriodStartType("");
							targetUIManagerWhere.setPeriodEndType("");
							targetUIManagerWhere.setDescription("");
							targetUIManagerWhere.setExceptYN("N");
							targetUIManagerWhere.setDataType("string");
							
							if(targetUIManagerWhere.getWhereType()==1)
							{
								targetUIManagerWhere.setCheckName(ServletUtil.getParamString(req, "eWhere1Value"+i));
								targetUIManagerWhere.setCheckValue(ServletUtil.getParamString(req, "eWhere2Value"+i));
							}
							else if(targetUIManagerWhere.getWhereType()==2)
							{
								targetUIManagerWhere.setPeriodStartType(ServletUtil.getParamString(req, "eWhere1Value"+i));
								targetUIManagerWhere.setPeriodEndType(ServletUtil.getParamString(req, "eWhere2Value"+i));
							}
							
							targetUIManagerWhereList.add(targetUIManagerWhere);	
						}
					}
					
					targetManagerService.updateTargetUseNo(targetUIManagerID);
					targetManagerService.deleteTargetManagerWhere(targetUIManagerID);
					targetManagerService.insertTargetManagerWhere(targetUIManagerWhereList);
				}
			}
		}
		
		
		if(resultval >0 )
		{
			this.sCurPage = "1";
			return list(req,res);	
		}
		else
		{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
			return null;
		}
	}
	
	
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		
		String[] targetUIManagerIDs = req.getParameterValues("eTargetUIManagerID");
		int result = 0;
		
		if(targetUIManagerIDs==null || targetUIManagerIDs.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		for(int i=0;i<targetUIManagerIDs.length;i++){
			
			result = targetManagerService.deleteTargetManagerInfo(Integer.parseInt(targetUIManagerIDs[i]));
			if(result >0)
			{
				targetManagerService.deleteTargetManagerSelect(Integer.parseInt(targetUIManagerIDs[i]));
				targetManagerService.deleteTargetManagerWhere(Integer.parseInt(targetUIManagerIDs[i]));
				targetManagerService.updateTargetUseNo(Integer.parseInt(targetUIManagerIDs[i]));
			}
				
		}
		this.sCurPage = "1";
		return list(req,res);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
		

}
