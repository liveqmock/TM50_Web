package web.target.targetui.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.common.util.*;
import web.target.targetui.service.TargetUIService;
import web.target.targetui.model.TargetUIOneToOne;

import web.target.targetui.model.TargetList;
import web.target.targetui.model.OnetooneTarget;
import web.target.targetui.model.TargetUIGeneralInfo;

public class TargetUIController extends MultiActionController {
	
	private TargetUIService targetUIService = null;	
	private String sCurPage = null;
	private String message = "";
	
	public void setTargetUIService(TargetUIService targetUIService){
		this.targetUIService = targetUIService;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
		return new ModelAndView("/pages/target/targetui/targetui.jsp");		
	}
	
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int curPage = 0;
		int iLineCnt = 0;
		
		
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		
		

		
		return new ModelAndView("/pages/target/targetui/targetui_proc.jsp?method=list","targetUILists", targetUIService.listTargetUI());
	}
	
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		int targetUIManagerID = ServletUtil.getParamInt(req,"targetUIManagerID","0");
		String id = ServletUtil.getParamString(req, "id");
		
		return new ModelAndView("/pages/target/targetui/targetui_proc.jsp?id="+id+"&method=edit&targetUIManagerID="+targetUIManagerID,"targetUIList", targetUIService.viewTargetUI(targetUIManagerID));
	}
	
	public ModelAndView edittarget(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		String id = ServletUtil.getParamString(req, "id");
		
		int targetUIManagerID = targetUIService.getTargetManagerID(targetID);
		
		return new ModelAndView("/pages/target/targetui/targetui_proc.jsp?id="+id+"&method=edit&targetUIManagerID="+targetUIManagerID+"&targetID="+targetID,"targetUIList", targetUIService.viewTargetUI(targetUIManagerID));
	}
	
	/**
	 * <p>dbID에 해당하는 접속정보를 가져오고 접속한다. 
	 * @param dbID
	 * @return
	 */
	private Connection getConnectionDBInfo(String dbID){
		Connection conn = null;
		Map<String, Object> mapdb = targetUIService.getDBInfo(dbID);
		
		if(mapdb==null || mapdb.size()==0){
			return null;
		}
		
		String driver = String.valueOf(mapdb.get("driverClass"));
		String url = String.valueOf(mapdb.get("dbURL"));
		String user = String.valueOf(mapdb.get("dbUserID"));
		String password = String.valueOf(mapdb.get("dbUserPWD"));
		
		conn = DBUtil.getConnection(driver, url, user, password);
		
		return conn;
	}
	
	
	
	/**
	 * <p>대상자등록시 쿼리의 카운트(대상인원수)를 구해온다. 
	 * @param queryText
	 * @return
	 */
	private int getCountQueryText(Connection conn, String queryText){
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = -1;
		
		try{		
			ps = conn.prepareStatement(queryText);
			rs = ps.executeQuery();				
			while(rs.next()){
				count = rs.getInt("CNT");						
			}
		}catch(Exception e){
			logger.error(e);
		}finally{
			try{ps.close();}catch(Exception e){}
			try{rs.close();}catch(Exception e){}		
			try{conn.close();}catch(Exception e){}	
			
		}		
		return count;
	}
	
	/**
	 * <p>대상자 수 가져오기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public int runCountQuery(String query, String dbID) throws Exception{
		int count = 0;
		
		Connection conn = getConnectionDBInfo(dbID);
		if(conn == null) {
			return -1;
		}

		// 쿼리를 수행하여
		try {
			count = getCountQueryText(conn, query );
		} catch (Exception e) {}
		
		
		return count;
	}	
	
	public String makeWhereQuery(HttpServletRequest req)   throws Exception{
		String queryWhere = " ";
		String query = ServletUtil.getParamStringDefault(req,"eQueryText","");
		String query_upper = query.toUpperCase();
		String[] whereIDs = ServletUtil.getParamStringArray(req, "eWhereIDs");
		String[] whereTypes = ServletUtil.getParamStringArray(req, "eWhereTypes");
		String[] exceptYNs = ServletUtil.getParamStringArray(req, "eExceptYNs");
		String[] whereFieldNames = ServletUtil.getParamStringArray(req, "eWhereFieldNames");
		String[] dataTypes = ServletUtil.getParamStringArray(req, "eDataTypes");
		
		for(int i = 0;i<whereIDs.length;i++)
		{
			if(i==0)
			{
				if(query_upper.indexOf(" WHERE ") == -1)
					queryWhere = " WHERE 1=1 ";
			}
			
			
			if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_CHECK)))
			{
				String[] values = ServletUtil.getParamStringArray(req, "eCheckValue_"+whereIDs[i]);
				String s= "";
				if(values != null)
				{
					for(int t = 0;t<values.length;t++)
					{
						if(dataTypes[i].equalsIgnoreCase("string"))
						{
							if(t==0)
								s+="'"+ values[t]+"'";
							else
								s+=", '"+ values[t]+"'";
						}
						else
						{
							if(t==0)
								s+= values[t];
							else
								s+=", "+ values[t];
						}
					}
					if(exceptYNs[i].equalsIgnoreCase("Y"))
						queryWhere +=" AND " +  whereFieldNames[i]+ " NOT IN ("+s+") ";
					else
						queryWhere +=" AND " +  whereFieldNames[i]+ " IN ("+s+") ";
				}
			}
			else if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_PERIOD)))
			{
				String startVal = ServletUtil.getParamString(req, "ePeriodStartValue_"+whereIDs[i]);
				String endVal = ServletUtil.getParamString(req, "ePeriodEndValue_"+whereIDs[i]);
				String startType = ServletUtil.getParamString(req, "ePeriodStartType_"+whereIDs[i]);
				String endType = ServletUtil.getParamString(req, "ePeriodEndType_"+whereIDs[i]);
				
				if(!startVal.equals(""))
				{
					if(dataTypes[i].equalsIgnoreCase("string"))
						queryWhere += " AND " + whereFieldNames[i] + " " + startType + " '" + startVal + "' ";
					else
						queryWhere += " AND " + whereFieldNames[i] + " " + startType + " " + startVal + " ";
				}
				if(!endVal.equals(""))
				{
					if(!startVal.equals(""))
						queryWhere += " AND ";
					if(dataTypes[i].equalsIgnoreCase("string"))
						queryWhere += " AND " + whereFieldNames[i] + " " + endType + " '" + endVal + "' ";
					else
						queryWhere += " AND " +  whereFieldNames[i] + " " + endType + " " + endVal + " ";
				}
				
				
			}
			else if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_INPUT)))
			{
				if(!ServletUtil.getParamString(req, "eInputValue_"+whereIDs[i]).equals(""))
				{
					if(dataTypes[i].equalsIgnoreCase("string"))
						queryWhere += " AND " + whereFieldNames[i] + " = '" +ServletUtil.getParamString(req, "eInputValue_"+whereIDs[i]) + "' ";
					else 
						queryWhere += " AND " + whereFieldNames[i] + " = " +ServletUtil.getParamString(req, "eInputValue_"+whereIDs[i]) + " ";
				}
				
			}			
			else if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_LIKE)))
			{
				if(!ServletUtil.getParamString(req, "eInputValue_"+whereIDs[i]).equals(""))
					queryWhere += " AND " + whereFieldNames[i] + " like '%" +ServletUtil.getParamString(req, "eInputValue_"+whereIDs[i]) + "%' ";
			}
			
			if(queryWhere.endsWith(" AND "))
				queryWhere = queryWhere.substring(0, queryWhere.lastIndexOf(" AND "));
			
		}
		
		
		return query + queryWhere;
	}
	
	
	public ModelAndView addTarget(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int targetID = Integer.parseInt( ServletUtil.getParamStringDefault(req,"eTargetID","0") );
		int targetUIManagerID = Integer.parseInt( ServletUtil.getParamStringDefault(req,"eTargetUIManagerID","0") );
		int resultVal = 0;
		res.setContentType("text/html; charset=UTF-8");
		
		//////////////////////////
		TargetList targetList = new TargetList();
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_UI);		//파일등록
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));
		targetList.setState(Constant.TARGET_STATE_FINISH);
		targetList.setBookMark("N");
		targetList.setDbID(ServletUtil.getParamString(req, "eDbID")); 	
		targetList.setTargetUIID(targetUIManagerID);
		
		String[] whereIDs = ServletUtil.getParamStringArray(req, "eWhereIDs");
		String[] whereTypes = ServletUtil.getParamStringArray(req, "eWhereTypes");
		String[] exceptYNs = ServletUtil.getParamStringArray(req, "eExceptYNs");
		String queryText = makeWhereQuery(req);
		targetList.setQueryText(queryText);
		
		String countQuery = QueryUtil.makeCountQuery(queryText); 
		int targetCount = runCountQuery(countQuery, targetList.getDbID());
		targetList.setTargetCount(targetCount);
		targetList.setCountQuery(countQuery);
		if(targetCount == -1)
		{
			res.getWriter().print("0");
			ServletUtil.messageGoURL(res,"데이타 베이스에 연결할 수 없습니다.\\n\\n관리자에게 문의 하세요","");	
			return null;
		}
		
		
		//tm_target_ui_select 에서 원투원 정보 가져오기
		List<TargetUIOneToOne> targetUIOneToOneList = targetUIService.getSelectOneToOne(targetUIManagerID);
		
		// 원투원  정보를 저장
		int[] queryOneToOnes = new int[targetUIOneToOneList.size()];
		String[] descripts = new String[targetUIOneToOneList.size()];
		String[] fieldNames = new String[targetUIOneToOneList.size()];
		for(int i =0;i<targetUIOneToOneList.size();i++){
			queryOneToOnes[i] = targetUIOneToOneList.get(i).getOnetooneID();
			descripts[i] = targetUIOneToOneList.get(i).getSelectDescription();
			fieldNames[i] = targetUIOneToOneList.get(i).getSelectFieldName();
		}
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		synchronized(this){
			
			//1. 대상자를 저장한다.
			resultVal = targetUIService.insertTargetList(targetList);
			if(resultVal>0){
				targetID = targetUIService.getMaxTargetID();
			
				if(resultVal==0 || targetID==0){
					resultVal = 0;
					res.getWriter().print("0");
					
				//2. 원투원 정보를 입력한다. 	
				}else{
					OnetooneTarget onetooneTarget = null;
					if(queryOneToOnes!=null && queryOneToOnes.length>0){
						for(int i=0; i < queryOneToOnes.length; i ++ ) {
							onetooneTarget = new OnetooneTarget();
							onetooneTarget.setTargetID(targetID);
							onetooneTarget.setFieldName(fieldNames[i]);
							onetooneTarget.setFieldDesc(descripts[i]);
							onetooneTarget.setCsvColumnPos(i+1);
							onetooneTarget.setOnetooneID(queryOneToOnes[i]);
							onetooneTargetList.add(onetooneTarget);
						}
					}
					resultVal = targetUIService.insertOnetooneTarget(onetooneTargetList);

					//3.회원정보UI 정보 저장
					if(resultVal > 0 && targetID > 0)
					{
						for(int i = 0;i<whereIDs.length;i++)
						{
												
							TargetUIGeneralInfo targetUIGeneralInfo = new TargetUIGeneralInfo();
							targetUIGeneralInfo.setTargetID(targetID);
							targetUIGeneralInfo.setWhereID(Integer.parseInt(whereIDs[i]));
							targetUIGeneralInfo.setWhereType(whereTypes[i]);
							targetUIGeneralInfo.setCheckedItems("");
							targetUIGeneralInfo.setPeriodStartValue("");
							targetUIGeneralInfo.setPeriodEndValue("");
							targetUIGeneralInfo.setInputValue("");
							
							if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_CHECK)))
							{
								String[] values = ServletUtil.getParamStringArray(req, "eCheckValue_"+whereIDs[i]);
								String s= "";
								if(values!=null)
								{
									for(int t = 0;t<values.length;t++)
									{
										s=s+ values[t]+";";
									}
									targetUIGeneralInfo.setCheckedItems(s);
								}
								
							}
							else if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_PERIOD)))
							{
								targetUIGeneralInfo.setPeriodStartValue(ServletUtil.getParamString(req, "ePeriodStartValue_"+whereIDs[i]));
								targetUIGeneralInfo.setPeriodEndValue(ServletUtil.getParamString(req, "ePeriodEndValue_"+whereIDs[i]));
								
							}
							else if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_INPUT))||whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_LIKE)))
							{
								targetUIGeneralInfo.setInputValue(ServletUtil.getParamString(req, "eInputValue_"+whereIDs[i]));
								
							}
							
							resultVal = targetUIService.insertTargetUIGeneralInfo(targetUIManagerID, targetUIGeneralInfo);
							
						}
					}
				}
			}
			
		}// end synchronized
		
		if(resultVal>0){
			this.sCurPage = "1"; 
			return null;
		}
		else{
			res.getWriter().print("0");
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");	
			return null;
		}
	}
	
	public ModelAndView editTarget(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int targetID = Integer.parseInt( ServletUtil.getParamStringDefault(req,"eTargetID","0") );
		int targetUIManagerID = Integer.parseInt( ServletUtil.getParamStringDefault(req,"eTargetUIManagerID","0") );
		int resultVal = 0;
		
		
		//////////////////////////
		TargetList targetList = new TargetList();
		targetList.setTargetID(targetID);
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_UI);		//파일등록
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));
		targetList.setState(Constant.TARGET_STATE_FINISH);
		targetList.setBookMark("N");
		targetList.setDbID(ServletUtil.getParamString(req, "eDbID")); 	
		targetList.setTargetUIID(targetUIManagerID);
		String[] whereIDs = ServletUtil.getParamStringArray(req, "eWhereIDs");
		String[] whereTypes = ServletUtil.getParamStringArray(req, "eWhereTypes");
		String[] exceptYNs = ServletUtil.getParamStringArray(req, "eExceptYNs");
		String queryText = makeWhereQuery(req);
		
		targetList.setQueryText(queryText);
		String countQuery = QueryUtil.makeCountQuery(queryText); 
		int targetCount = runCountQuery(countQuery, targetList.getDbID());
		targetList.setTargetCount(targetCount);
		targetList.setCountQuery(countQuery);
		if(targetCount == -1)
		{
			res.getWriter().print("0");
			ServletUtil.messageGoURL(res,"데이타 베이스에 연결할 수 없습니다.\\n\\n관리자에게 문의 하세요","");	
			return null;
		}
		
		
		//tm_target_ui_select 에서 원투원 정보 가져오기
		List<TargetUIOneToOne> targetUIOneToOneList = targetUIService.getSelectOneToOne(targetUIManagerID);
		
		// 원투원  정보를 저장
		int[] queryOneToOnes = new int[targetUIOneToOneList.size()];
		String[] descripts = new String[targetUIOneToOneList.size()];
		String[] fieldNames = new String[targetUIOneToOneList.size()];
		for(int i =0;i<targetUIOneToOneList.size();i++){
			queryOneToOnes[i] = targetUIOneToOneList.get(i).getOnetooneID();
			descripts[i] = targetUIOneToOneList.get(i).getSelectDescription();
			fieldNames[i] = targetUIOneToOneList.get(i).getSelectFieldName();
		}
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		synchronized(this){
			
			//1. 대상자를 저장한다.
			resultVal = targetUIService.updateTargetList(targetList);
			if(resultVal>0){
				//2.회원정보UI 정보 저장
				resultVal = targetUIService.deleteTargetUIGeneralInfo(targetID, targetUIManagerID);
				if(resultVal > 0 && targetID > 0)
				{
					for(int i = 0;i<whereIDs.length;i++)
					{
						TargetUIGeneralInfo targetUIGeneralInfo = new TargetUIGeneralInfo();
						targetUIGeneralInfo.setTargetID(targetID);
						targetUIGeneralInfo.setWhereID(Integer.parseInt(whereIDs[i]));
						targetUIGeneralInfo.setWhereType(whereTypes[i]);
						targetUIGeneralInfo.setCheckedItems("");
						targetUIGeneralInfo.setPeriodStartValue("");
						targetUIGeneralInfo.setPeriodEndValue("");
						targetUIGeneralInfo.setInputValue("");
							
						if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_CHECK)))
						{
							String[] values = ServletUtil.getParamStringArray(req, "eCheckValue_"+whereIDs[i]);
							String s= "";
							if(values!=null)
							{
								for(int t = 0;t<values.length;t++)
								{
									s=s+ values[t]+";";
								}
								targetUIGeneralInfo.setCheckedItems(s);
							}
								
						}
						else if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_PERIOD)))
						{
							targetUIGeneralInfo.setPeriodStartValue(ServletUtil.getParamString(req, "ePeriodStartValue_"+whereIDs[i]));
							targetUIGeneralInfo.setPeriodEndValue(ServletUtil.getParamString(req, "ePeriodEndValue_"+whereIDs[i]));
							
						}
						else if(whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_INPUT))||whereTypes[i].equals(String.valueOf(Constant.TARGET_UI_WHERETYPE_LIKE)))
						{
							targetUIGeneralInfo.setInputValue(ServletUtil.getParamString(req, "eInputValue_"+whereIDs[i]));
								
						}
							
						resultVal = targetUIService.insertTargetUIGeneralInfo(targetUIManagerID, targetUIGeneralInfo);
							
					}
				}
			}
		}// end synchronized
		
		if(resultVal>0){
			this.sCurPage = "1"; 
			return null;
		}
		else{
			res.getWriter().print("0");
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");	
			return null;
		}
	}
	
	

}
