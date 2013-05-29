package web.admin.usergroup.control;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.admin.usergroup.service.UserGroupService;
import web.admin.usergroup.model.*;
import web.common.util.Constant;
import web.common.util.PageUtil;
import web.common.util.ServletUtil;
import web.common.util.ThunderUtil;

public class UserGroupController extends MultiActionController {

	private UserGroupService userGroupService = null;

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}
	

	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String id = ServletUtil.getParamString(req,"id");
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/usergroup/usergroup.jsp?id="+id);
	}
	
	
	/**
	 * <p>사용자리스트 트리 출력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	
	public void treeView(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		out.print( userGroupService.getJsonTreeGroupUser() );
	}
	
	/**
	 * <p>자동완성 사용자검색 리스트 출력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	
	public void quickSearchUserList(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String userName = ServletUtil.getParamString(req, "input");
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		out.print( userGroupService.getJsonQuickSearchUser( userName) );
		
	}
	
	
	/**
	 * <p>사용자리스트 출력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView listUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		int currentPage = ServletUtil.getParamInt(req,"currentPage","1");
		int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		req.setAttribute("currentPage", currentPage);
		req.setAttribute("countPerPage", countPerPage);					
		String searchType = ServletUtil.getParamString(req,"searchType");
		String searchName = ServletUtil.getParamString(req,"searchName");
		String selGroupID =  ServletUtil.getParamString(req,"selGroupID");		//옵션박스에서 선택한 그룹아이디 
		String userLevel = ServletUtil.getParamString(req,"userLevel"); //사용권한 
		String useYN = ServletUtil.getParamString(req,"useYN"); //사용여부
		req.setAttribute("selGroupID",  selGroupID);	

		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map searchMap = new HashMap(); 
		searchMap.put("searchType", searchType);
		searchMap.put("searchName", searchName);		
		
		//총카운트 		
		int totalCount = userGroupService.getUserTotalCount(searchMap,selGroupID, userLevel, useYN);
		req.setAttribute("totalCount", totalCount);	
	
		//페이지분할작업 
		String pageDivideForm = PageUtil.dividePageJavascript(currentPage, countPerPage, totalCount, req, "changePage");
		req.setAttribute("pageDivideForm", pageDivideForm);
				

		return new ModelAndView("/admin/usergroup/users_list.jsp","userlist", userGroupService.listUser(currentPage,countPerPage,searchMap, selGroupID, userLevel, useYN));
		
	}
	
	/**
	 * <p>그룹리스트를 출력한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView listGroup(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		int currentPage = ServletUtil.getParamInt(req,"currentPage","1");
		int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		req.setAttribute("currentPage", currentPage);
		req.setAttribute("countPerPage", countPerPage);	
		
		String searchType = ServletUtil.getParamString(req,"searchType");
		String searchName = ServletUtil.getParamString(req,"searchName");
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map searchMap = new HashMap(); 
		searchMap.put("searchType", searchType);
		searchMap.put("searchName", searchName);		
		
		//총카운트 		
		int totalCount = userGroupService.getGroupTotalCount(searchMap);
		req.setAttribute("totalCount", totalCount);	
		
		//페이지분할작업 
		String pageDivideForm = PageUtil.dividePageJavascript(currentPage, countPerPage, totalCount, req, "changePage");
		req.setAttribute("pageDivideForm", pageDivideForm);		

		
		return new ModelAndView("/admin/usergroup/usergroup_list.jsp","grouplist", userGroupService.listGroup(currentPage,countPerPage,searchMap));
		
	}
	
	/**
	 * <p>사용자 추가  (계정을 등록하고 권한도 등록한다.)
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView insertUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		
		//사용자정보 
		String id = ServletUtil.getParamString(req, "id");

		String userID = ServletUtil.getParamString(req,"eUserID");
		String userPWD= ServletUtil.getParamString(req,"eUserPWD");
		String userName = ServletUtil.getParamString(req,"eUserName");
		String groupID = ServletUtil.getParamString(req,"eGroupID");
		String userLevel = ServletUtil.getParamString(req,"eUserLevel");		//사용자권한 1:전체관리자, 2:소속관리자, 3;일반사용자 
		String email = ServletUtil.getParamString(req,"eEmail");
		String cellPhone = ServletUtil.getParamString(req,"eCellPhone");
		String description = ServletUtil.getParamString(req,"eDescription");
		String useYN = "Y";
		String isHelper = ServletUtil.getParamString(req,"eIsHelper");
		String senderName = ServletUtil.getParamString(req,"eSenderName");
		String senderEmail = ServletUtil.getParamString(req,"eSenderEmail");
		String senderCellPhone = ServletUtil.getParamString(req,"eSenderCellPhone");
		
		userPWD = ThunderUtil.getMD5Hexa(userPWD);
		
		User user = new User();		
		user.setUserID(userID);
		user.setUserName(userName);
		user.setUserPWD(userPWD);
		user.setGroupID(groupID);
		user.setUserLevel(userLevel);
		user.setEmail(email);
		user.setCellPhone(cellPhone);
		user.setDescription(description);
		user.setUseYN(useYN);
		user.setIsHelper(isHelper);
		user.setSenderName(senderName);
		user.setSenderEmail(senderEmail);
		user.setSenderCellPhone(senderCellPhone);
		
		// 중복 id 체크
		User chkUser = userGroupService.viewUser(userID);
		
		if(chkUser.getUserID().equals(userID)) {
			ServletUtil.messageGoURL(res,"동일한 아이디가 등록된 내역이 존재합니다.\\r\\n관리자에게 문의 하시기 바랍니다.","");
			return null;
		}
		/*
		if(isHelper.equals("Y"))
		{
			chkUser = userGroupService.viewHelper();
			if(!(chkUser.getUserID().equals(""))&&!(chkUser.getUserID().equals(userID))) {
				ServletUtil.messageGoURL(res,"설정된 담당자가 이미 존재합니다.","");
				return null;
			}
		}*/
		
		
		
		int resultVal = userGroupService.insertUser(user,setUserAuth(userID,req));
		if(resultVal>0){
			return new ModelAndView("/admin/usergroup/usergroup_proc.jsp?id="+id+"&method=insert_user","user", user);

		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		}	
		
		return null;

	}
	
	/**
	 * <p>그룹 수정창으로 이동한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editGroup(HttpServletRequest req, HttpServletResponse res) throws Exception{

		String groupID = ServletUtil.getParamStringDefault(req, "groupID", "0");
		String id = ServletUtil.getParamString(req, "id");
		
		return new ModelAndView("/admin/usergroup/usergroup_proc.jsp?id="+id+"&method=edit_group","group", userGroupService.viewGroup(groupID));
		
	}
	
	

	
	/**
	 * <p>사용자의 정보 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewUser(HttpServletRequest req, HttpServletResponse res) throws Exception {		
		//페이징세팅 
		//ServletUtil.pageParamSetting(req);
		//메뉴아이디세팅
		//ServletUtil.meunParamSetting(req);			
		//List<DbSet> dbList = userGroupService.selectSetDBList();
		//req.setAttribute("dbList", dbList);			
		String id = ServletUtil.getParamString(req, "id");
		String userID = ServletUtil.getParamString(req,"userID");		
		//req.setAttribute("selUserID", selUserID);	
		
		return new ModelAndView("/admin/usergroup/usergroup_proc.jsp?id="+id+"&method=view_user","user",userGroupService.viewUser(userID));
		
	}
	
	
	/**
	 * <p>사용자 추가 화면 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//페이징세팅 
		String userID = ServletUtil.getParamString(req,"userID");		
		
		return new ModelAndView("/admin/usergroup/usergroup_proc.jsp?method=edit_user","user", userGroupService.viewUser(userID));
		
	}
	/**
	 * <p>그룹을 등록한다. 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView insertGroup(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String id = ServletUtil.getParamString(req, "id");
		String groupName = ServletUtil.getParamString(req,"eGroupName");
		String description = ServletUtil.getParamString(req,"eDescription");
		
		int groupID = userGroupService.getMaxdbID();
		if(groupID==0) groupID = Constant.DB_CHAR3ID;  //초기값은 100으로 정한다. (char(4)이기 때문에)
		
		Group group = new Group();
		group.setGroupID(Integer.toString(groupID));
		group.setGroupName(groupName);
		group.setDescription(description);

		int resultVal = userGroupService.insertGroup(group);	
		if(resultVal>0){
			return new ModelAndView("/admin/usergroup/usergroup_proc.jsp?id="+id+"&method=insert_group","group", group);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		}		
		return null;

	}
	
	
	/**
	 * <p>그룹을 수정한다. 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView updateGroup(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String id = ServletUtil.getParamString(req, "id");
		String groupID = ServletUtil.getParamString(req,"eGroupID");
		
		String groupName = ServletUtil.getParamString(req,"eGroupName");
		String description = ServletUtil.getParamString(req,"eDescription");
		
		
		Group group = new Group();
		group.setGroupID(groupID);
		group.setGroupName(groupName);
		group.setDescription(description);		
		
		int resultVal = userGroupService.updateGroup(group);	
		if(resultVal>0){
			return new ModelAndView("/admin/usergroup/usergroup_proc.jsp?id="+id+"&method=update_group","group", group);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		}
		
		return null;
		
	}
		
	/**
	 * <p>사용자를 수정한다. 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView updateUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		//사용자정보 
		@SuppressWarnings("unused")
		String id = ServletUtil.getParamString(req,"id");
		String userID = ServletUtil.getParamString(req,"eUserID");
		String userPWD= ServletUtil.getParamString(req,"eUserPWD");
		String userName = ServletUtil.getParamString(req,"eUserName");
		String groupID = ServletUtil.getParamString(req,"eGroupID");
		String userLevel = ServletUtil.getParamString(req,"eUserLevel");		//사용자권한 1:전체관리자, 2:소속관리자, 3;일반사용자 
		String email = ServletUtil.getParamString(req,"eEmail");
		String cellPhone = ServletUtil.getParamString(req,"eCellPhone");
		String description = ServletUtil.getParamString(req,"eDescription");
		String useYN = "Y"; //ServletUtil.getParamString(req,"useYN");
		String isHelper = ServletUtil.getParamString(req,"eIsHelper");
		String senderName = ServletUtil.getParamString(req,"eSenderName");
		String senderEmail = ServletUtil.getParamString(req,"eSenderEmail");
		String senderCellPhone = ServletUtil.getParamString(req,"eSenderCellPhone");
				
		userPWD = ThunderUtil.getMD5Hexa(userPWD);
			
		User user = new User();		
		user.setUserID(userID);
		user.setUserName(userName);
		user.setUserPWD(userPWD);
		user.setGroupID(groupID);
		user.setUserLevel(userLevel);
		user.setEmail(email);
		user.setCellPhone(cellPhone);
		user.setDescription(description);
		user.setUseYN(useYN);
		user.setIsHelper(isHelper);
		user.setSenderName(senderName);
		user.setSenderEmail(senderEmail);
		user.setSenderCellPhone(senderCellPhone);
		
		//if(isHelper.equals("Y"))
		//{
			//User chkUser = userGroupService.viewHelper();
			//if(!(chkUser.getUserID().equals(""))&&!(chkUser.getUserID().equals(userID))) {
				//ServletUtil.messageGoURL(res,"설정된 담당자가 이미 존재합니다.","");
				//return null;
			//}
		//}
		
		int resultVal = userGroupService.updateUsersAuth(user, setUserAuth(userID,req));	
		if(resultVal>0){
			return new ModelAndView("/admin/usergroup/usergroup_proc.jsp?method=update_user","user", user);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		}		
		return null;
	}
	
	/**
	 * <p>사용자를 수정한다. 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void updateUserSelf(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		//사용자정보 
		@SuppressWarnings("unused")
		String id = ServletUtil.getParamString(req,"id");
		String userID = ServletUtil.getParamString(req,"eUserID");
		String userPWD= ServletUtil.getParamString(req,"eUserPWD");
		String userName = ServletUtil.getParamString(req,"eUserName");
		String groupID = ServletUtil.getParamString(req,"eGroupID");
		String userLevel = ServletUtil.getParamString(req,"eUserLevel");		//사용자권한 1:전체관리자, 2:소속관리자, 3;일반사용자 
		String email = ServletUtil.getParamString(req,"eEmail");
		String cellPhone = ServletUtil.getParamString(req,"eCellPhone");
		String description = ServletUtil.getParamString(req,"eDescription");
		String senderName = ServletUtil.getParamString(req,"eSenderName");
		String senderEmail = ServletUtil.getParamString(req,"eSenderEmail");
		String senderCellPhone = ServletUtil.getParamString(req,"eSenderCellPhone");
		String isHelper = ServletUtil.getParamString(req,"eIsHelper");
		if(isHelper.equals("null")){
			isHelper=" ";
		}
		String useYN = "Y"; 
		
		userPWD = ThunderUtil.getMD5Hexa(userPWD);
		
		User user = new User();		
		user.setUserID(userID);
		user.setUserName(userName);
		user.setUserPWD(userPWD);
		user.setGroupID(groupID);
		user.setUserLevel(userLevel);
		user.setEmail(email);
		user.setCellPhone(cellPhone);
		user.setDescription(description);
		user.setUseYN(useYN);
		user.setIsHelper(isHelper);
		user.setSenderName(senderName);
		user.setSenderEmail(senderEmail);
		user.setSenderCellPhone(senderCellPhone);
		
		int resultVal = userGroupService.updateUsersAuth(user, setUserAuth(userID,req));	
		if(resultVal<1){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		}
	
		
	}
	/**
	 * <p>사용자의 그룹을 변경한다. 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void changeUserGroupID(HttpServletRequest req, HttpServletResponse res) throws Exception {
		

		String userID = ServletUtil.getParamString(req,"userID");
		String groupID = ServletUtil.getParamString(req,"groupID");

		int resultVal = userGroupService.updatechangeUserGroupID(userID, groupID);	
		if(resultVal <= 0){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		}		
		
	}	

	/**
	 * 사용자삭제 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView deleteUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String GroupIDsJson = req.getParameter("IDs");
		
		Object obj = JSONValue.parse(GroupIDsJson);

		JSONArray array = (JSONArray)obj;
		
		boolean rslt = false;
		
		rslt = userGroupService.deleteUser(array);		
		
		if(rslt){
			return new ModelAndView("/admin/usergroup/usergroup_proc.jsp");
		}else{
			ServletUtil.messageGoURL(res,"삭제처리에 실패했습니다.","");
		}	
		
		return null;
	
	}
	
	/**
	 * 그룹삭제 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView deleteGroup(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String GroupIDsJson = req.getParameter("IDs");
		
		Object obj = JSONValue.parse(GroupIDsJson);

		JSONArray array = (JSONArray)obj;
		
		boolean rslt = false;
		
		rslt = userGroupService.deleteGroup(array);		
		
		if(rslt){
			return new ModelAndView("/admin/usergroup/usergroup_proc.jsp");
		}else{
			ServletUtil.messageGoURL(res,"삭제처리에 실패했습니다.","");
		}	
		
		return null;
		
	}
	
	/**
	 * <p>특정 사용자의 메뉴 권한 json  (submenu id)
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	
	public void userMenuAuth(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		out.print( userGroupService.getJsonMenuAuth( ServletUtil.getParamString(req,"userID")) );
	}	

	/**
	 * <p>사용자권한에 따른 권한정보를 불러온다. (기존에 저장된 것중에서 최근꺼)
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView viewUserLevelAuth(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String userLevel = ServletUtil.getParamString(req,"userLevel");		
		List authList = userGroupService.selectUserLevelAuth(userLevel);
		req.setAttribute("authList", authList);	
		return new ModelAndView("/admin/usergroup/users_write.jsp");
		
	}
	
	
	/**
	 * <p>사용자권한 세팅 
	 * @param userID
	 * @param req
	 * @return
	 */
	private UserAuth[] setUserAuth(String userID, HttpServletRequest req){
		
		final String dFlag = ";";		//대구분자
		final String sFlag = ":";		//소구분자 
		
		/*
		 * 사용자권한 파라미터 처리 -----------------------------------------------------------------------------------------------------------------------------
		 */				
		//------------------------------------------------------------------------------------------------------------------------
		//	 # 대상자 메뉴 
		//------------------------------------------------------------------------------------------------------------------------
		String mainMenuIDM1 = ServletUtil.getParamString(req,"mainMenuIDM1");		//대상자대메뉴ID
		
		// 대상자등록 ------------------------------------------------------------------------------------------------------		
		String subMenuID10 = ServletUtil.getParamString(req,"subMenuID10");		//대상자대메뉴ID		
		String target_csv = ServletUtil.getParamString(req,"target_csv");				//csv권한 
		String target_query = ServletUtil.getParamString(req,"target_query");		//query권한 	
		String target_direct = ServletUtil.getParamString(req,"target_direct");
		String target_related = ServletUtil.getParamString(req,"target_related");
		String[] target_dbIDArray =  ServletUtil.getParamStringArray(req, "target_dbID"); 		//디비선택 
		
		//위에 예를 들자면 만약 권한이 cvs, query, 그리고 query를 선택하면 db를 선택해야 하므로 모두 선택되면 다음과 같이 세팅이 된다.
		// cvs:query|10:11:12     ==>10:11:12 dbID가 10,11,12를 사용한다는 것이다.
		//만약 query권한이 없다면 => cvs:   -> 이렇게만 세팅이 될것이다. 
		String target_dbIDS = "";  
		if(target_dbIDArray!=null){
			for(int i=0;i<target_dbIDArray.length;i++){
				target_dbIDS = target_dbIDS +target_dbIDArray[i] + sFlag ;
				
			}
		}
		String subAuthType10 = "1"; 
		String subMenuAuth10  = target_csv + sFlag + target_direct + sFlag + target_related + sFlag + target_query + dFlag+ target_dbIDS;
		
		
		// 대상자관리 ------------------------------------------------------------------------------------------------------
		String subMenuID11 = ServletUtil.getParamString(req,"subMenuID11");		//대상자 서브메뉴ID		
		String target_search_group_auth = ServletUtil.getParamString(req,"target_search_group_auth");		//대상자검색-소속그룹권한 
		String target_search_all_auth = ServletUtil.getParamString(req,"target_search_all_auth");	//대상자검색-전체권한
	
		String subAuthType11 = "2";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기/ 읽기 권한, 3은 쓰기/읽기권한 
		String subMenuAuth11 = target_search_group_auth + dFlag +target_search_all_auth;
	
		//------------------------------------------------------------------------------------------------------------------------
		//	 # 대량메일 메뉴 
		//------------------------------------------------------------------------------------------------------------------------
		String mainMenuIDM2 = ServletUtil.getParamString(req,"mainMenuIDM2");		//대량메일 대메뉴ID
		
		// 대량메일작성  ------------------------------------------------------------------------------------------------------		
		String subMenuID20 = ServletUtil.getParamString(req,"subMenuID20");		//대량메일 서브메뉴ID
		String massmail_send_auth = ServletUtil.getParamString(req,"massmail_send_auth");			//대량메일발송권한 
		String subAuthType20 = "3";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth20 = massmail_send_auth; 
		
		
		// 발송중/대기중 리스트  ------------------------------------------------------------------------------------------------------
		String subMenuID21 = ServletUtil.getParamString(req,"subMenuID21");			//대량메일 소메뉴ID
		String massmail_send_group_auth = ServletUtil.getParamString(req,"massmail_send_group_auth");	//대량메일 발송중/대기중리스트 소속그룹권한 
		String massmail_send_all_auth = ServletUtil.getParamString(req,"massmail_send_all_auth");		//대량메일 발송중/대기중리스트 전체권한		
		String subAuthType21 = "2";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth21 = massmail_send_group_auth + dFlag + massmail_send_all_auth; 
		
		
		
		// 발송결과 리스트  ------------------------------------------------------------------------------------------------------
		String subMenuID22 = ServletUtil.getParamString(req,"subMenuID22");				//대량메일 소메뉴ID
		String massmail_result_group_auth = ServletUtil.getParamString(req,"massmail_result_group_auth");	//대량메일 발송결과리스트 소속그룹권한		
		String massmail_result_all_auth = ServletUtil.getParamString(req,"massmail_result_all_auth");		//대량메일 발송결과리스트 전체권한
		String subAuthType22 = "2";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth22 = massmail_result_group_auth + dFlag + massmail_result_all_auth; 
		
		
		// 비교분석 리스트  ------------------------------------------------------------------------------------------------------
		String subMenuID23 = ServletUtil.getParamString(req,"subMenuID23");		//대량메일 소메뉴ID
		String massmail_compare_group_auth = ServletUtil.getParamString(req,"massmail_compare_group_auth");	//대량메일 비교분석 소속그룹권한
		String massmail_compare_all_auth = ServletUtil.getParamString(req,"massmail_compare_all_auth");				//대량메일 비교분석 전체권한
		String subAuthType23 = "2";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth23 = massmail_compare_group_auth + dFlag + massmail_compare_all_auth; 
		
		
		// 월간보고서 리스트  ------------------------------------------------------------------------------------------------------
		String subMenuID24 = ServletUtil.getParamString(req,"subMenuID24");		//대량메일 소메뉴ID
		String massmail_month_group_auth = ServletUtil.getParamString(req,"massmail_month_group_auth");	//대량메일 월간보고서 소속그룹쓰기권한
		String massmail_month_all_auth = ServletUtil.getParamString(req,"massmail_month_all_auth");				//대량메일 월간보고서 소속그룹읽기권한
		String subAuthType24 = "2";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth24 = massmail_month_group_auth + dFlag + massmail_month_all_auth; 
		
		
		// 계정별발송현황  ------------------------------------------------------------------------------------------------------
		String subMenuID25 = ServletUtil.getParamString(req,"subMenuID25");		//대량메일 소메뉴ID
		String massmail_users_group_auth = ServletUtil.getParamString(req,"massmail_users_group_auth");		//대량메일 계정별발송현황 소속그룹쓰기권한
		String massmail_users_all_auth = ServletUtil.getParamString(req,"massmail_users_all_auth");		//대량메일 계정별발송현황 소속그룹읽기권한
		String subAuthType25 = "2";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth25 = massmail_users_group_auth + dFlag + massmail_users_all_auth; 	
		
		
		// 수신거부자리스트  ------------------------------------------------------------------------------------------------------
		String subMenuID26 = ServletUtil.getParamString(req,"subMenuID26");		//대량메일 소메뉴ID
		String massmail_reject_auth = ServletUtil.getParamString(req,"massmail_reject_auth");	//대량메일 수신거부자리스트 권한
		String subAuthType26 = "3";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth26 = massmail_reject_auth; 	
		
		
		//------------------------------------------------------------------------------------------------------------------------
		//	 # 컨텐츠  메뉴 
		//------------------------------------------------------------------------------------------------------------------------
		String mainMenuIDM3 = ServletUtil.getParamString(req,"mainMenuIDM3");		//대량메일 대메뉴ID
		
		// 메일템플릿   ------------------------------------------------------------------------------------------------------
		String subMenuID30 = ServletUtil.getParamString(req,"subMenuID30");		//대량메일 소메뉴ID
		String content_mailtemplate_group_auth = ServletUtil.getParamString(req,"content_mailtemplate_group_auth");	//컨테츠 메일템플릿 소속그룹권한
		String content_mailtemplate_all_auth = ServletUtil.getParamString(req,"content_mailtemplate_all_auth");				//컨테츠 메일템플릿 전체권한
		String subAuthType30 = "2";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth30 = content_mailtemplate_group_auth + dFlag +content_mailtemplate_all_auth; 	
		
		// 설문관리   ------------------------------------------------------------------------------------------------------
		String subMenuID31 = ServletUtil.getParamString(req,"subMenuID31");		//대량메일 소메뉴ID
		String content_poll_group_auth = ServletUtil.getParamString(req,"content_poll_group_auth");	//컨테츠 설문관리 소속그룹권한
		String content_poll_all_auth = ServletUtil.getParamString(req,"content_poll_all_auth");		//컨테츠 설문관리 전체권한
		String subAuthType31= "2";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth31 = content_poll_group_auth + dFlag +content_poll_all_auth; 	
		
		
		//------------------------------------------------------------------------------------------------------------------------
		//	 # SMS   메뉴 
		//------------------------------------------------------------------------------------------------------------------------
		//TODO SMS 권한 작성할 것 
		
		
		
		//------------------------------------------------------------------------------------------------------------------------
		//	 # 자동메일   메뉴 
		//------------------------------------------------------------------------------------------------------------------------
		//TODO 자동메일 권한 작성할 것 
		
		
		
		//------------------------------------------------------------------------------------------------------------------------
		//	 # 관리자   메뉴 
		//------------------------------------------------------------------------------------------------------------------------
		String mainMenuIDM6 = ServletUtil.getParamString(req,"mainMenuIDM6");		//대량메일 대메뉴ID
		
		//  공지사항 ------------------------------------------------------------------------------------------------------
		String subMenuID60 = ServletUtil.getParamString(req,"subMenuID60");									//대량메일 소메뉴ID
		String manager_board_auth = ServletUtil.getParamString(req,"manager_board_auth");		//관리자 공지사항권한
		String subAuthType60= "3";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth60 = manager_board_auth; 		
				
		//  환경설정  ------------------------------------------------------------------------------------------------------
		String subMenuID61 = ServletUtil.getParamString(req,"subMenuID61");									//대량메일 소메뉴ID
		String manager_config_auth = ServletUtil.getParamString(req,"manager_config_auth");		//관리자 환경설정권한
		String subAuthType61= "3";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth61 = manager_config_auth;
		
		// 도메인설정 -------------------------------------------------------------------------------------------------
		String subMenuID62 = ServletUtil.getParamString(req,"subMenuID62");										//대량메일 소메뉴ID
		String manager_domain_auth = ServletUtil.getParamString(req,"manager_domain_auth");		//관리자 도메인설정권한
		String subAuthType62= "3";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth62 = manager_domain_auth;
		
		
		// 그룹계정관리  -------------------------------------------------------------------------------------------------
		String subMenuID63 = ServletUtil.getParamString(req,"subMenuID63");												//대량메일 소메뉴ID
		String manager_usergroup_auth = ServletUtil.getParamString(req,"manager_usergroup_auth");		//관리자 그룹/계정관리권한
		String subAuthType63= "3";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth63 = manager_usergroup_auth;		
		
	
		// 그룹계정관리  -------------------------------------------------------------------------------------------------
		String subMenuID64 = ServletUtil.getParamString(req,"subMenuID64");												//대량메일 소메뉴ID
		String manager_setdb_auth = ServletUtil.getParamString(req,"manager_setdb_auth");					//관리자 DB설정 쓰기권한
		String subAuthType64= "3";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth64 = manager_setdb_auth;				
				
		// 오타수정  -------------------------------------------------------------------------------------------------
		String subMenuID65 = ServletUtil.getParamString(req,"subMenuID65");												//대량메일 소메뉴ID
		String manager_setcorrect_auth = ServletUtil.getParamString(req,"manager_setcorrect_auth");		//관리자 오타수정 쓰기권한
		String subAuthType65= "3";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth65 = manager_setcorrect_auth;					
				
		// 보내는사람수정  -------------------------------------------------------------------------------------------------
		String subMenuID66 = ServletUtil.getParamString(req,"subMenuID66");												//대량메일 소메뉴ID
		String manager_sender_auth = ServletUtil.getParamString(req,"manager_sender_auth");				//관리자 보내는사람관리 쓰기권한
		String subAuthType66= "3";  //1은 사용자정의 권한 , 2는 소속과 그룹으로 나눈 쓰기 읽기 권한을 의미, 3은 쓰기/읽기권한 
		String subMenuAuth66 = manager_sender_auth;	
		
		
		/**
		 * 권한설정의 개수대로 객체배열을 생성한다. 
		 * 현재(2008-02-19 기준)는 18개의 권한메뉴가 있다. 만약 권한메뉴가 늘어나면 더 생성해주어야 한다. 
		 */
		
		UserAuth[] userAuth = new UserAuth[18];
	
		
		//----------------------------------------대상자 메뉴 권한------------------------------------------------------------//
		//대상자등록  권한세팅
		if(!mainMenuIDM1.equals("") && !subMenuID10.equals("")){			
			userAuth[0] = new UserAuth();
			userAuth[0].setUserID(userID);
			userAuth[0].setMainMenuID(mainMenuIDM1);
			userAuth[0].setSubMenuID(subMenuID10);
			userAuth[0].setAuthType(subAuthType10);			
			userAuth[0].setSubMenuAuth(subMenuAuth10);
		}
				
		//대상자관리  권한세팅
		if(!mainMenuIDM1.equals("") && !subMenuID11.equals("")){			
			userAuth[1] = new UserAuth();
			userAuth[1].setUserID(userID);
			userAuth[1].setMainMenuID(mainMenuIDM1);
			userAuth[1].setSubMenuID(subMenuID11);
			userAuth[1].setAuthType(subAuthType11);			
			userAuth[1].setSubMenuAuth(subMenuAuth11);
		}
		
		//----------------------------------------대량메일 메뉴 권한------------------------------------------------------------//
		
		//대량메일작성 권한 세팅 
		if(!mainMenuIDM2.equals("") && !subMenuID20.equals("")){			
			userAuth[2] = new UserAuth();
			userAuth[2].setUserID(userID);
			userAuth[2].setMainMenuID(mainMenuIDM2);
			userAuth[2].setSubMenuID(subMenuID20);
			userAuth[2].setAuthType(subAuthType20);			
			userAuth[2].setSubMenuAuth(subMenuAuth20);
		}
		
		
		//발송중/대기중리스트 권한 세팅 
		if(!mainMenuIDM2.equals("") && !subMenuID21.equals("")){			
			userAuth[3] = new UserAuth();
			userAuth[3].setUserID(userID);
			userAuth[3].setMainMenuID(mainMenuIDM2);
			userAuth[3].setSubMenuID(subMenuID21);
			userAuth[3].setAuthType(subAuthType21);			
			userAuth[3].setSubMenuAuth(subMenuAuth21);
		}
		
		
		//발송결과리스트 권한 세팅 
		if(!mainMenuIDM2.equals("") && !subMenuID22.equals("")){		
			userAuth[4] = new UserAuth();
			userAuth[4].setUserID(userID);
			userAuth[4].setMainMenuID(mainMenuIDM2);
			userAuth[4].setSubMenuID(subMenuID22);
			userAuth[4].setAuthType(subAuthType22);			
			userAuth[4].setSubMenuAuth(subMenuAuth22);
		}
		
		
		//비교분석  권한 세팅 
		if(!mainMenuIDM2.equals("") && !subMenuID23.equals("")){		
			userAuth[5] = new UserAuth();
			userAuth[5].setUserID(userID);
			userAuth[5].setMainMenuID(mainMenuIDM2);
			userAuth[5].setSubMenuID(subMenuID23);
			userAuth[5].setAuthType(subAuthType23);			
			userAuth[5].setSubMenuAuth(subMenuAuth23);
		}
		
		//월간보고서  권한 세팅 
		if(!mainMenuIDM2.equals("") && !subMenuID24.equals("")){		
			userAuth[6] = new UserAuth();
			userAuth[6].setUserID(userID);
			userAuth[6].setMainMenuID(mainMenuIDM2);
			userAuth[6].setSubMenuID(subMenuID24);
			userAuth[6].setAuthType(subAuthType24);			
			userAuth[6].setSubMenuAuth(subMenuAuth24);	 
		}
		
		//계정별발송현황   권한 세팅 
		if(!mainMenuIDM2.equals("") && !subMenuID25.equals("")){		
			userAuth[7] = new UserAuth();
			userAuth[7].setUserID(userID);
			userAuth[7].setMainMenuID(mainMenuIDM2);
			userAuth[7].setSubMenuID(subMenuID25);
			userAuth[7].setAuthType(subAuthType25);			
			userAuth[7].setSubMenuAuth(subMenuAuth25);	 		
		}
		
		//수신거부자   권한 세팅 
		if(!mainMenuIDM2.equals("") && !subMenuID26.equals("")){		
			userAuth[8] = new UserAuth();
			userAuth[8].setUserID(userID);
			userAuth[8].setMainMenuID(mainMenuIDM2);
			userAuth[8].setSubMenuID(subMenuID26);
			userAuth[8].setAuthType(subAuthType26);			
			userAuth[8].setSubMenuAuth(subMenuAuth26);	 
		}
		
		
		//----------------------------------------컨테츠  메뉴 권한------------------------------------------------------------//		
		//메일템플릿   권한 세팅 
		if(!mainMenuIDM3.equals("") && !subMenuID30.equals("")){		
			userAuth[9] = new UserAuth();
			userAuth[9].setUserID(userID);
			userAuth[9].setMainMenuID(mainMenuIDM3);
			userAuth[9].setSubMenuID(subMenuID30);
			userAuth[9].setAuthType(subAuthType30);			
			userAuth[9].setSubMenuAuth(subMenuAuth30);	 		
		}
		
		//설문관리   권한 세팅 
		if(!mainMenuIDM3.equals("") && !subMenuID31.equals("")){				
			userAuth[10] = new UserAuth();
			userAuth[10].setUserID(userID);
			userAuth[10].setMainMenuID(mainMenuIDM3);
			userAuth[10].setSubMenuID(subMenuID31);
			userAuth[10].setAuthType(subAuthType31);			
			userAuth[10].setSubMenuAuth(subMenuAuth31);	 		
		}
		
		
		//----------------------------------------관리자  메뉴 권한------------------------------------------------------------//
		//공지사항   권한 세팅 
		if(!mainMenuIDM6.equals("") && !subMenuID60.equals("")){		
			userAuth[11] = new UserAuth();	
			userAuth[11].setUserID(userID);
			userAuth[11].setMainMenuID(mainMenuIDM6);
			userAuth[11].setSubMenuID(subMenuID60);
			userAuth[11].setAuthType(subAuthType60);			
			userAuth[11].setSubMenuAuth(subMenuAuth60);	 			
		}
		
		//환경설정   권한 세팅 
		if(!mainMenuIDM6.equals("") && !subMenuID61.equals("")){				
			userAuth[12] = new UserAuth();
			userAuth[12].setUserID(userID);
			userAuth[12].setMainMenuID(mainMenuIDM6);
			userAuth[12].setSubMenuID(subMenuID61);
			userAuth[12].setAuthType(subAuthType61);			
			userAuth[12].setSubMenuAuth(subMenuAuth61);	 	
		}
		
		//도메인설정   권한 세팅 
		if(!mainMenuIDM6.equals("") && !subMenuID62.equals("")){				
			userAuth[13] = new UserAuth();
			userAuth[13].setUserID(userID);
			userAuth[13].setMainMenuID(mainMenuIDM6);
			userAuth[13].setSubMenuID(subMenuID62);
			userAuth[13].setAuthType(subAuthType62);			
			userAuth[13].setSubMenuAuth(subMenuAuth62);	 
		}
		
		//그룹/계정관리   권한 세팅 
		if(!mainMenuIDM6.equals("") && !subMenuID63.equals("")){		
			userAuth[14] = new UserAuth();
			userAuth[14].setUserID(userID);
			userAuth[14].setMainMenuID(mainMenuIDM6);
			userAuth[14].setSubMenuID(subMenuID63);
			userAuth[14].setAuthType(subAuthType63);			
			userAuth[14].setSubMenuAuth(subMenuAuth63);	 		
		}
		
		//DB설정 권한 세팅 
		if(!mainMenuIDM6.equals("") && !subMenuID64.equals("")){		
			userAuth[15] = new UserAuth();
			userAuth[15].setUserID(userID);
			userAuth[15].setMainMenuID(mainMenuIDM6);
			userAuth[15].setSubMenuID(subMenuID64);
			userAuth[15].setAuthType(subAuthType64);			
			userAuth[15].setSubMenuAuth(subMenuAuth64);	 		
		}
		
		//오타수정  권한 세팅 
		if(!mainMenuIDM6.equals("") && !subMenuID65.equals("")){		
			userAuth[16] = new UserAuth();
			userAuth[16].setUserID(userID);
			userAuth[16].setMainMenuID(mainMenuIDM6);
			userAuth[16].setSubMenuID(subMenuID65);
			userAuth[16].setAuthType(subAuthType65);			
			userAuth[16].setSubMenuAuth(subMenuAuth65);	
		}
		
		//보내는사람   권한 세팅 
		if(!mainMenuIDM6.equals("") && !subMenuID66.equals("")){		
			userAuth[17] = new UserAuth();
			userAuth[17].setUserID(userID);
			userAuth[17].setMainMenuID(mainMenuIDM6);
			userAuth[17].setSubMenuID(subMenuID66);
			userAuth[17].setAuthType(subAuthType66);			
			userAuth[17].setSubMenuAuth(subMenuAuth66);	 	
		}
		
		
		return userAuth;
		
	}

	/**
	 * 그룹 권한 수정 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void updateGroupAuth(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String authJson = req.getParameter("authJSON");
		String groupID = req.getParameter("groupID");

	
		boolean rslt = userGroupService.updateGroupAuth(groupID, authJson);		
		
		if(!rslt){
			ServletUtil.messageGoURL(res,"DB처리에 실패했습니다.","");
		}	
		

	}
	
	/**
	 * 개인 권한 수정 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void updateUserAuth(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String authJson = req.getParameter("authJSON");
		String userID = req.getParameter("userID");

	
		boolean rslt = userGroupService.updateUserAuth(userID, authJson);		
		
		if(!rslt){
			ServletUtil.messageGoURL(res,"DB처리에 실패했습니다.","");
		}	
		
	}

	/**
	 * 선택된 복수 개인 권한 수정 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void updateSelectedUserAuth(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String authJson = req.getParameter("authJSON");
		String userIDJson = req.getParameter("IDs");

		
		boolean rslt = userGroupService.updateSelectedUserAuth(userIDJson, authJson);		
		
		if(!rslt){
			ServletUtil.messageGoURL(res,"DB처리에 실패했습니다.","");
		}
			
		
	}
	
	/**
	 * <p>사용자 ID 사용제한 해제
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView derestrict(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String userID = ServletUtil.getParamString(req,"eUserID");
		
		int resultVal =userGroupService.updateDerestrict(userID);
		if(resultVal > 0){
			//return new ModelAndView("/admin/usergroup/usergroup_proc.jsp?method=update_user","user", user);
		}else{
			ServletUtil.messageGoURL(res,"해제에 실패 하였습니다.","");		
		}
		return null;
	}
	
	
}