package web.admin.dbconnect.control;


import web.admin.dbconnect.service.*;
import web.admin.dbconnect.model.*;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.common.util.*;

import java.util.ArrayList;
import java.sql.*;


public class DbConnectController extends MultiActionController{

	private String sCurPage = null;
	private String message = "";
	private DbConnectService dbConnectService = null;
	
	
	public void setDbConnectService(DbConnectService dbConnectService){
		this.dbConnectService = dbConnectService;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{				
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/dbconnect/dbconnect.jsp");
	}
		
	
	/**
	 * <p>등록/수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
	    String dbID = ServletUtil.getParamString(req,"dbID");		
	     return new ModelAndView("/admin/dbconnect/dbconnect_proc.jsp?method=edit&dbID="+dbID,"dbconnectInfo", dbConnectService.viewConnectDBInfo(dbID));
	}
	
	/**
	 * <p>등록/수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView dbInfo(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String dbID = ServletUtil.getParamString(req,"dbID");		
	     return new ModelAndView("/admin/dbconnect/dbconnecthistory_proc.jsp?method=dbInfo","dbconnectInfo", dbConnectService.viewConnectDBInfo(dbID));
	}
	
	/**
	 * <p>등록/수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listdbHistoryInfo(HttpServletRequest req, HttpServletResponse res) throws Exception{
	    
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
		
		String dbID = ServletUtil.getParamString(req,"dbID");	
	    //날짜 검색조건 
	    String searchDateStart = ServletUtil.getParamString(req,"eSearchDateStart");
		String searchDateEnd = ServletUtil.getParamString(req,"eSearchDateEnd");
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchDateStart", searchDateStart+" 00:00:00");
		searchMap.put("searchDateEnd", searchDateEnd+" 23:59:59");
		searchMap.put("dbID", dbID);
		searchMap.put("curPage", curPage);
		searchMap.put("iLineCnt", iLineCnt);
		
		//총카운트 		
		int iTotalCnt = dbConnectService.totalCountDBConnectHistoryInfo(searchMap);
	
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));	
		req.setAttribute("message", this.message);

		this.message = ""; // 다음 호출을 위해 초기화
		
		
	    return new ModelAndView("/admin/dbconnect/dbconnecthistory_proc.jsp?method=historylist&dbID="+dbID,"dbconnectInfoList",dbConnectService.listDBConnectHistoryInfo(searchMap));
	}
	
	/**
	 * <p>디비연동리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listdbInfo(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
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
		String sUseYN 	= ServletUtil.getParamString(req,"sUseYN");
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자		
		Map<String, Object> searchMap = new HashMap<String, Object>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("useYN",  sUseYN);
		searchMap.put("curPage", curPage);
		searchMap.put("iLineCnt", iLineCnt);
		
		//총카운트 		
		int iTotalCnt = dbConnectService.totalCountDBConnectInfo(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));	
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
			
		return new ModelAndView("/admin/dbconnect/dbconnect_proc.jsp?method=list","dbconnectInfoList",dbConnectService.listDBConnectInfo(searchMap));		
		
	}
	
	/**
	 * <p>쿼리 컬럼리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listQueryColumn(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String dbID =ServletUtil.getParamString(req,"edbID");
		String queryText =ServletUtil.getParamString(req,"eQueryText");
		String checkQueryText = ServletUtil.getParamString(req,"eCheckQueryText");
		String limitQuery = "";
		String result = "success";
		res.setContentType("text/html; charset=UTF-8");
		
		Map<String,Object> dbInfo =  dbConnectService.getDBInfo(dbID);
		
		if(dbInfo == null) {
			ServletUtil.messageGoURL(res, "데이타 베이스 연결 정보가 올바르지 않습니다.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}
		
		Connection conn = getConnectionDBInfo(dbID);
		if(conn == null) {
			ServletUtil.messageGoURL(res, "데이타 베이스에 연결할 수 없습니다.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}
	
		//컴럼 정보 확인 : 변경 된 쿼리에서 기존에 등록 되었던 컬럼이  빠졌을 경우 실패 전송
		List<DbConnectColumn> dbConnectColumnList = dbConnectService.listConnectDBColumn(dbID);
		for(DbConnectColumn dbConnectColumn : dbConnectColumnList){
			if(queryText.indexOf(dbConnectColumn.getColumnName()) < 0){
				queryText = checkQueryText;
				result = "fail";
			}
		}
		
		//데이타 1개만 가져오기
		limitQuery = QueryUtil.makeTopCountQuery(queryText,String.valueOf(dbInfo.get("driverType")),1);
		if(limitQuery.equals("")) {
			ServletUtil.messageGoURL(res, "쿼리문을 실행 할 수 없습니다.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}
		
		// 쿼리를 수행하여
		List<Map<String,Object>> columnList = getQueryTextColumnName(conn, limitQuery);
		if(columnList == null || columnList.size()==0) {
			ServletUtil.messageGoURL(res, "쿼리문오류.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}
		
		req.setAttribute("queryText", queryText);
		req.setAttribute("result", result);
		return new ModelAndView("/admin/dbconnect/dbconnect_proc.jsp?method=listQueryColumn","columnList",columnList);		
	}
	
	public ModelAndView showQueryColumn(HttpServletRequest req, HttpServletResponse res) throws Exception{
		List<Map<String,Object>> columnList = new ArrayList<Map<String,Object>>();
		String dbID =ServletUtil.getParamString(req,"edbID");
		
		List<DbConnectColumn> dbConnectColumnList = dbConnectService.listConnectDBColumn(dbID);
		Map<String,Object> columnMap = null;
		for(DbConnectColumn dbConnectColumn : dbConnectColumnList){
			columnMap = new HashMap<String,Object>();
			columnMap.put("columnName",dbConnectColumn.getColumnName());
			columnMap.put("columnType",dbConnectColumn.getColumnType());
			columnMap.put("columnLength", dbConnectColumn.getColumnLength());
			columnMap.put("columnDesc", dbConnectColumn.getColumnDesc());
			columnList.add(columnMap);			
		}

		return new ModelAndView("/admin/dbconnect/dbconnect_proc.jsp?method=listQueryColumn","columnList",columnList);		
	}
	
	/**
	 * <p>쿼리에 해당하는 컬럼명을 얻어온다.
	 * @param conn
	 * @param queryText
	 * @return
	 */
	private List<Map<String,Object>> getQueryTextColumnName(Connection conn, String queryText){
		PreparedStatement ps = null;	
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		List<Map<String,Object>> columnsList = null;
		
		try{
			columnsList = new ArrayList<Map<String,Object>>();
			
			ps = conn.prepareStatement(queryText);	
			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();			
		  			
			Map<String,Object> columnMap = null;
			for(int i=1;i<=numberOfColumns;i++){
				columnMap = new HashMap<String,Object>();
				columnMap.put("columnName", rsmd.getColumnLabel(i));
				columnMap.put("columnType", rsmd.getColumnTypeName(i));
				columnMap.put("columnLength", rsmd.getColumnDisplaySize(i));
				columnsList.add(columnMap);			
			}
			
		}catch(Exception e){
			logger.error(e);	
		}finally{
			if(ps!=null) try{ps.close();}catch(Exception e){}			
			if(conn!=null) try{conn.close();}catch(Exception e){}	
			rsmd=null;
		}		
		
		return columnsList;
	}
	
	/**
	 * <p>dbID에 해당하는 접속정보를 가져오고 접속한다. 
	 * @param dbID
	 * @return
	 */
	private Connection getConnectionDBInfo(String dbID){
		Connection conn = null;
		Map<String, Object> mapdb = dbConnectService.getDBInfo(dbID);
		
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
	
	
	@SuppressWarnings("unchecked")
	public  ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String dbID =ServletUtil.getParamString(req,"edbID");
		String connectDBName =ServletUtil.getParamString(req,"eConnectDBName");
		String useYN =ServletUtil.getParamStringDefault(req,"eUseYN","Y");
		String queryText =ServletUtil.getParamString(req,"eQueryText");
		String updateType =ServletUtil.getParamString(req,"eUpdateType");
		String updateSchedule =ServletUtil.getParamString(req,"eUpdateSchedule");
		String updateScheduleHour =ServletUtil.getParamString(req,"eUpdateScheduleHour");
		String updateScheduleMinute =ServletUtil.getParamString(req,"eUpdateScheduleMinute");
		int updateValue =ServletUtil.getParamInt(req,"eUpdateValue","0");
		String userID = LoginInfo.getUserID(req);
		if(dbID.equals("")) dbID = "0";
		if(dbConnectService.checkDBInfo(dbID)>0){
			ServletUtil.messageGoURL(res,"이미 해당 DB로 등록되어 있습니다.","");
			return null;	
		}
		
		int days = 0;
		if(updateValue>0){
			days = updateValue;
		}
		
		String[] columnNames = ServletUtil.getParamStringArray(req, "eColumnName");
		String[] columnTypes = ServletUtil.getParamStringArray(req, "eColumnType");
		String[] columnLengths = ServletUtil.getParamStringArray(req, "eColumnLength");
		String[] columnDescs = ServletUtil.getParamStringArray(req, "eColumnDesc");
		
		if(Integer.parseInt(updateScheduleHour)<10) updateScheduleHour="0"+updateScheduleHour;
		if(Integer.parseInt(updateScheduleMinute)<10) updateScheduleMinute="0"+updateScheduleMinute;
		
		
		String updateScheduleDate = "";
		String sceduleDate = "";
		//1회 수집일 경우 
		if(updateType.equals(Constant.CONNECT_UPDATE_ONE)){
			sceduleDate = updateSchedule;
			updateScheduleDate = sceduleDate+" "+updateScheduleHour+":"+updateScheduleMinute+":00";			
		}
		//매주 수집 (체크된 요일의 현재날짜 이후 가장 최근의 날짜 를 얻어온다.
		else if(updateType.equals(Constant.CONNECT_UPDATE_WEEK)){
			sceduleDate = DateUtils.getDateNextWeek(days);
			updateScheduleDate = sceduleDate+" "+updateScheduleHour+":"+updateScheduleMinute+":00";

		}
		//매월 수집 
		else if(updateType.equals(Constant.CONNECT_UPDATE_MONTH)){
			sceduleDate = DateUtils.getDateNextMonth(days);
			updateScheduleDate =sceduleDate+" "+updateScheduleHour+":"+updateScheduleMinute+":00";
		}
		
		String tableName = Constant.CUSTOMER_TABLE+dbID+"_"+StringUtil.replace(sceduleDate, "-", "");
		DbConnectInfo dbConnectInfo = new DbConnectInfo();
		dbConnectInfo.setDbID(dbID);
		dbConnectInfo.setConnectDBName(connectDBName);
		dbConnectInfo.setQueryText(queryText);
		dbConnectInfo.setUpdateScheduleDate(updateScheduleDate);
		dbConnectInfo.setUseYN(useYN);
		dbConnectInfo.setUserID(userID);
		dbConnectInfo.setUpdateType(updateType);
		dbConnectInfo.setUpdateValue(updateValue);		
		dbConnectInfo.setTableName(tableName);
		
		
		Map<String, Object>[] columnMaps = new HashMap[columnNames.length];
		//컬럼정보 입력
	
		for(int i=0;i<columnNames.length;i++){
			columnMaps[i] = new HashMap<String,Object>();
			columnMaps[i].put("dbID", dbID);
			columnMaps[i].put("columnName", columnNames[i]);
			columnMaps[i].put("columnType", columnTypes[i]);
			columnMaps[i].put("columnLength", columnLengths[i]);
			columnMaps[i].put("columnDesc", columnDescs[i]);		
			
		}
	
		
		//컬럼입력 
		int resultVal = dbConnectService.insertConnectDB(dbConnectInfo,columnMaps);
		
		if(resultVal>0){
			this.sCurPage = "1";
			return listdbInfo(req,res);
				
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");			 			
			return null;	
		}	
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	public  ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String dbID =ServletUtil.getParamString(req,"edbID");
		String checkdbID =ServletUtil.getParamString(req,"eCheckdbID");
		String connectDBName =ServletUtil.getParamString(req,"eConnectDBName");
		String useYN =ServletUtil.getParamStringDefault(req,"eUseYN","Y");
		String queryText =ServletUtil.getParamString(req,"eQueryText");
		String checkQueryText = ServletUtil.getParamString(req,"eCheckQueryText");
		String updateType =ServletUtil.getParamString(req,"eUpdateType");
		String updateSchedule =ServletUtil.getParamString(req,"eUpdateSchedule");
		String updateScheduleHour =ServletUtil.getParamString(req,"eUpdateScheduleHour");
		String updateScheduleMinute =ServletUtil.getParamString(req,"eUpdateScheduleMinute");
		int updateValue =ServletUtil.getParamInt(req,"eUpdateValue","0");
		String historyYN =ServletUtil.getParamStringDefault(req,"eHistoryYN","N");
		
		String userID = LoginInfo.getUserID(req);
		
		String queryUpdateYN = ServletUtil.getParamStringDefault(req,"eQueryUpdateYN","N");		//쿼리수정여부 
		//쿼리수정을 체크했다면 쿼리가 수정된 것으로 본다.
		if(!queryUpdateYN.equals("Y")){
			dbID = checkdbID;
			queryText = checkQueryText;
		}
		
		if(dbID.equals("")) dbID = "0";
		int days = 0;
		if(updateValue>0){
			days = updateValue;
		}
		
		String[] columnNames = ServletUtil.getParamStringArray(req, "eColumnName");
		String[] columnTypes = ServletUtil.getParamStringArray(req, "eColumnType");
		String[] columnLengths = ServletUtil.getParamStringArray(req, "eColumnLength");
		String[] columnDescs = ServletUtil.getParamStringArray(req, "eColumnDesc");
		
		if(Integer.parseInt(updateScheduleHour)<10) updateScheduleHour="0"+updateScheduleHour;
		if(Integer.parseInt(updateScheduleMinute)<10) updateScheduleMinute="0"+updateScheduleMinute;
		
		
		String updateScheduleDate = "";
		String sceduleDate = "";
		//1회 수집일 경우 
		if(updateType.equals(Constant.CONNECT_UPDATE_ONE)){
			sceduleDate = updateSchedule;
			updateScheduleDate = sceduleDate+" "+updateScheduleHour+":"+updateScheduleMinute+":00";			
		}
		//매주 수집 (체크된 요일의 현재날짜 이후 가장 최근의 날짜 를 얻어온다.
		else if(updateType.equals(Constant.CONNECT_UPDATE_WEEK)){
			sceduleDate = DateUtils.getDateNextWeek(days);
			updateScheduleDate = sceduleDate+" "+updateScheduleHour+":"+updateScheduleMinute+":00";

		}
		//매월 수집 
		else if(updateType.equals(Constant.CONNECT_UPDATE_MONTH)){
			sceduleDate = DateUtils.getDateNextMonth(days);
			updateScheduleDate =sceduleDate+" "+updateScheduleHour+":"+updateScheduleMinute+":00";
		}
		
		String tableName = Constant.CUSTOMER_TABLE+dbID+"_"+StringUtil.replace(sceduleDate, "-", "");
		DbConnectInfo dbConnectInfo = new DbConnectInfo();
		dbConnectInfo.setDbID(dbID);
		dbConnectInfo.setConnectDBName(connectDBName);
		dbConnectInfo.setQueryText(queryText);
		dbConnectInfo.setUpdateScheduleDate(updateScheduleDate);
		dbConnectInfo.setUseYN(useYN);
		dbConnectInfo.setUserID(userID);
		dbConnectInfo.setUpdateType(updateType);
		dbConnectInfo.setUpdateValue(updateValue);		
		dbConnectInfo.setTableName(tableName);
		
		
		Map<String, Object>[] columnMaps = new HashMap[columnNames.length];
		//컬럼정보 입력
	
		for(int i=0;i<columnNames.length;i++){
			columnMaps[i] = new HashMap<String,Object>();
			columnMaps[i].put("columnType", columnTypes[i]);
			columnMaps[i].put("columnLength", columnLengths[i]);
			columnMaps[i].put("columnDesc", columnDescs[i]);
			columnMaps[i].put("dbID", dbID);
			columnMaps[i].put("columnName", columnNames[i]);
			
		}
	
		
		//컬럼입력 
		int resultVal = dbConnectService.updateConnectDB(dbConnectInfo,columnMaps);
		
		if(resultVal>0){
			this.sCurPage = "1";
			if(historyYN.equals("Y")){ 
				return listdbHistoryInfo(req,res);
			}else{
				return listdbInfo(req,res);
			}
				
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");			 			
			return null;	
		}	
		
		
	}
	
	
}
