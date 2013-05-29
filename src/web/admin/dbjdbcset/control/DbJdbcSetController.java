package web.admin.dbjdbcset.control;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.admin.dbjdbcset.service.DbJdbcSetService;
import web.admin.dbjdbcset.model.*;
import web.common.util.*;

/**
 * <p>DB설정 메뉴 처리 
 * @author coolang
 *
 */
public class DbJdbcSetController extends MultiActionController {
	
	private DbJdbcSetService dbJdbcSetService = null;
	private String sCurPage = null;
	private String message = "";
	
	public void setDbJdbcSetService(DbJdbcSetService dbJdbcSetService) {
		this.dbJdbcSetService = dbJdbcSetService;
	}
	
	/**
	 * <p> 설정된 DB 리스트를 출력한다. 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/dbjdbcset/db_jdbc_set.jsp");
	}
	
	/**
	 * <p> DB/JDBC 리스트를 출력한다. 
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
		int iTotalCnt = dbJdbcSetService.getListDbJdbcSetTotalCount(searchMap);

		
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));	
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		return new ModelAndView("/admin/dbjdbcset/db_jdbc_set_proc.jsp?method=list","dblist", dbJdbcSetService.listDbJdbcSet(searchMap));
		
	}

	/**
	 * <p> JDBC 리스트를 보여준다. 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listjdbc(HttpServletRequest req, HttpServletResponse res) throws Exception{			
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		return new ModelAndView("/popup/dbjdbcset/dbjdbcset_jdbc_popup.jsp","jdbclist", dbJdbcSetService.listJdbcSet());
		
	}
	
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{	

		String[] dbIDs =  req.getParameterValues("eDbID");
		if(dbIDs == null || dbIDs.length==0) {
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		int resultVal = 0;
		
		/*-------------------------------------------------------------------------------------------------------
		 *현재 사용중인 타게팅이 있다면 삭제불가 처리 
		-----------------------------------------------------------------------------------------------------------*/
		for(String dbID : dbIDs){
			if(dbJdbcSetService.checkUseDBbydbID(dbID)==0){
				resultVal = dbJdbcSetService.deleteDbSet(dbID);
			}
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
	
	/**
	 * <p>DB접근키 확인
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView checkConfirm(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String dbID = ServletUtil.getParamString(req,"eDbID");
		String dbAccessKeyConfirmer = ServletUtil.getParamString(req,"eDbAccessKeyConfirmer");
		dbAccessKeyConfirmer = TmEncryptionUtil.encryptSHA256(dbAccessKeyConfirmer);
		String dbAccessKey = dbJdbcSetService.getDbAccessKey(dbID);
		
		if(dbAccessKey.equals(dbAccessKeyConfirmer)) {
			res.getWriter().print("success");
		}
		
		return null;
	}
	/**
	 * <p>DB설정  업데이트 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{
			
		  String dbID = ServletUtil.getParamString(req,"eDbID");
		  String driverID = ServletUtil.getParamString(req,"selectedDriverID");
		  String driverClass = ServletUtil.getParamString(req,"eDriverClass");
		  String dbName = ServletUtil.getParamString(req,"eDbName");
		  String dbURL = ServletUtil.getParamString(req,"eDbURL");
		  String dbUserID = ServletUtil.getParamString(req,"eDbUserID");
		  String dbUserPWD = ServletUtil.getParamString(req,"eDbUserPWD");
		  String encodingYN = ServletUtil.getParamStringDefault(req,"eEncodingYN","N");
		  String defaultYN = ServletUtil.getParamStringDefault(req,"eDefaultYN","N");
		  String useYN = ServletUtil.getParamStringDefault(req,"eUseYN","Y");
		  String description = ServletUtil.getParamString(req,"eDescription");
		  String dbAccessKey = ServletUtil.getParamString(req, "eDbAccessKey");
		  String dbAccessKeyConfirm = ServletUtil.getParamString(req, "eDbAccessKeyConfirm");
		  
		  String mainMenuID = ServletUtil.getParamString(req,"mainMenuID");
		  String subMenuID = ServletUtil.getParamString(req,"subMenuID");
		  
		  
		  String[] params = {dbID,driverID,driverClass,dbName,dbURL,dbUserID,dbUserPWD};	
		 
			if(!ServletUtil.checkRequireParamString(params)){	
				//필수파라마티 에러일 경우에 더이상 진행하지 않고 에러로그를 보여주고 빠져나온다. 
				ServletUtil.messageGoURL(res,"[파라미터 오류] 필수입력값이 부족합니다.","");
				res.getWriter().print("false");
				return null;
			}
			
			//DB연결체크 
			if(!DBUtil.checkConnection(driverClass, dbURL, dbUserID, dbUserPWD)){ //연결이 되지 않는다면 
				ServletUtil.messageGoURL(res,"[DB체크 오류] 올바르지 않은 DB정보입니다. ","");
				res.getWriter().print("false");
				return null;
			}
			
			// 암호화키 생성
			String sKey = StringUtil.createSecurityKey("TM", dbID, driverClass);
			
			DbJdbcSet dbJdbcSet = new DbJdbcSet();
			//bind(req,dbJdbcSet);
			dbJdbcSet.setDriverID(driverID);
			dbJdbcSet.setJdbcDriverClass(driverClass);
			dbJdbcSet.setDbID(dbID);
			dbJdbcSet.setDbName(dbName);
			dbJdbcSet.setDbURL(TmEncryptionUtil.encrypto(dbURL, sKey));
			dbJdbcSet.setDbUserID(TmEncryptionUtil.encrypto(dbUserID, sKey));
			dbJdbcSet.setDbUserPWD(TmEncryptionUtil.encrypto(dbUserPWD, sKey));
			dbJdbcSet.setEncodingYN(encodingYN);
			dbJdbcSet.setDefaultYN(defaultYN);
			dbJdbcSet.setUseYN(useYN);
			dbJdbcSet.setDescription(description);
			
			if(dbAccessKey == "") {
				dbJdbcSet.setDbAccessKey(dbJdbcSetService.getDbAccessKey(dbID));
			} else {
				dbJdbcSet.setDbAccessKey(TmEncryptionUtil.encryptSHA256(dbAccessKey));
			}
			
			int resultVal = dbJdbcSetService.updateDbSet(dbJdbcSet);			
			if(resultVal>0){
				
				this.sCurPage = "1"; 
				return list(req,res);
			}
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			res.getWriter().print("false");
			
			return null;		
	}
	

	 public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		//메뉴아이디 세팅 
	   // ServletUtil.meunParamSetting(req);	
	    String dbID = ServletUtil.getParamString(req,"dbID");	
	    String driverID =  ServletUtil.getParamString(req,"driverID");
    
	     return new ModelAndView("/admin/dbjdbcset/db_jdbc_set_proc.jsp?method=edit&driverID="+driverID,"dbset", dbJdbcSetService.viewDbSet(dbID));
	 }
	

	/**
	 * <p>tm_dbset에 인서트
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{
		 
		  String driverID = ServletUtil.getParamString(req,"selectedDriverID");
		  String driverClass = ServletUtil.getParamString(req,"eDriverClass");
		  String dbName = ServletUtil.getParamString(req,"eDbName");
		  String dbURL = ServletUtil.getParamString(req,"eDbURL");
		  String dbUserID = ServletUtil.getParamString(req,"eDbUserID");
		  String dbUserPWD = ServletUtil.getParamString(req,"eDbUserPWD");
		  String encodingYN = ServletUtil.getParamStringDefault(req,"eEncodingYN","N");
		  String defaultYN = ServletUtil.getParamStringDefault(req,"eDefaultYN","N");
		  String useYN = ServletUtil.getParamStringDefault(req,"eUseYN","Y");
		  String description = ServletUtil.getParamString(req,"eDescription");
		  String dbAccessKey = ServletUtil.getParamString(req, "eDbAccessKey");
		  String dbAccessKeyConfirm = ServletUtil.getParamString(req, "eDbAccessKeyConfirm");
		  
		  
		  String[] params = {driverID,driverClass, dbName,dbURL,dbUserID,dbUserPWD, dbAccessKey, dbAccessKeyConfirm};	
			if(!ServletUtil.checkRequireParamString(params)){	
				//필수파라마티 에러일 경우에 더이상 진행하지 않고 에러로그를 보여주고 빠져나온다. 
				ServletUtil.messageGoURL(res,"[파라미터 오류] 필수입력값이 부족합니다.","");
				res.getWriter().print("false");
				return null;
			}
			
			//DB연결체크 
			if(!DBUtil.checkConnection(driverClass, dbURL, dbUserID, dbUserPWD)){ //연결이 되지 않는다면 
				ServletUtil.messageGoURL(res,"[DB체크 오류] 올바르지 않은 DB정보입니다. ","");
				res.getWriter().print("false");
				return null;
			}
			
			int maxdbID = dbJdbcSetService.getMaxdbID() + 1;
			if(maxdbID==0) maxdbID = Constant.DB_CHAR2ID;  //초기값은 10으로 정한다. (char(2)이기 때문에)
			
			// 암호화키 생성
			String sKey = StringUtil.createSecurityKey("TM", Integer.toString(maxdbID), driverClass);
			
			DbJdbcSet dbJdbcSet = new DbJdbcSet();
			dbJdbcSet.setDriverID(driverID);
			dbJdbcSet.setDbID(Integer.toString(maxdbID));
			dbJdbcSet.setDbName(dbName);
			dbJdbcSet.setDbURL(TmEncryptionUtil.encrypto(dbURL, sKey));
			dbJdbcSet.setDbUserID(TmEncryptionUtil.encrypto(dbUserID, sKey));
			dbJdbcSet.setDbUserPWD(TmEncryptionUtil.encrypto(dbUserPWD, sKey));
			dbJdbcSet.setEncodingYN(encodingYN);
			dbJdbcSet.setDefaultYN(defaultYN);
			dbJdbcSet.setUseYN(useYN);
			dbJdbcSet.setDescription(description);
			dbJdbcSet.setDbAccessKey(TmEncryptionUtil.encryptSHA256(dbAccessKey));
			
			int resultVal = dbJdbcSetService.insertDbSet(dbJdbcSet);			
			if(resultVal>0){
				//this.message = "저장 되었습니다.";
				this.sCurPage = "1"; 
				return list(req,res);
			}
			ServletUtil.messageGoURL(res,"추가에 실패했습니다","");
			res.getWriter().print("false");
			
			return null;
	}
	
	/**
	 * <p>어드민일 경우 등록된 모든 db리스트가 나온다.
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbJdbcSet> listDBList(String dbID){
		return dbJdbcSetService.listDBList(dbID);
	}
		
}
