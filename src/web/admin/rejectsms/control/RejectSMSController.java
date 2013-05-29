package web.admin.rejectsms.control;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import web.admin.rejectsms.model.*;
import web.admin.rejectsms.service.*;
import web.common.util.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Iterator;
import org.springframework.web.multipart.MultipartFile;
import web.common.model.FileUpload;



public class RejectSMSController extends MultiActionController{

	private RejectSMSService rejectSMSService = null;
	private String sCurPage = null;
	private String message = "";
	private String realUploadPath = null;
	
	//action-servlet.xml에 저장된 업로드 경로를 읽어온다. 
	public void setRealUploadPath(String realUploadPath) {
		this.realUploadPath = realUploadPath;
	}
	
	public void setRejectSMSService(RejectSMSService rejectSMSService){
		this.rejectSMSService = rejectSMSService;
	}

	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/rejectsms/rejectsms.jsp");
	}
	
	
	/**
	 * <p>등록화면
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView write(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		String userID = LoginInfo.getUserID(req);
		String userName = LoginInfo.getUserName(req);
		String groupID = LoginInfo.getGroupID(req);
		String groupName = LoginInfo.getGroupName(req);
		
		req.setAttribute("userID", userID);
		req.setAttribute("userName", userName);
		req.setAttribute("groupID", groupID);
		req.setAttribute("groupName", groupName);
	
		
		return new ModelAndView("/admin/rejectsms/rejectsms_proc.jsp?method=write");		
	}
	
	
	@SuppressWarnings("unchecked")
	public void uploadFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
		
		String uploadKey = mreq.getParameter("uploadKey");
		if(uploadKey == null) {
			throw new Exception("추가시 uploadKey를 파라미터로 반드시 전달 해야됨");
		}

		FileUpload fileUpload = null;
		
		Iterator fileNameIterator = mreq.getFileNames();		
		while(fileNameIterator.hasNext()){
			MultipartFile multiFile = mreq.getFile((String)fileNameIterator.next());			
			if(multiFile.getSize()>0){
				fileUpload = FileUploadUtil.uploadNewFile(mreq, multiFile, this.realUploadPath, uploadKey);
				fileUpload.setUploadKey(uploadKey);			
			}
		}
		
	
	}	
	
	
	/**
	 * <p>수신거부자 리스트 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
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
		
		
		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));

		String loginAuth = LoginInfo.getUserAuth(req);
		String groupID = LoginInfo.getGroupID(req);
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		String sSearchType_gubun = ServletUtil.getParamString(req,"sSearchType_gubun");
		
	

		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);

		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자		
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);
		searchMap.put("loginAuth", loginAuth);
		searchMap.put("groupID", groupID);
		searchMap.put("searchType_gubun", sSearchType_gubun);

		//총카운트 		
		int iTotalCnt = rejectSMSService.totalCountRejectSMS(searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));

		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		return new ModelAndView("/admin/rejectsms/rejectsms_proc.jsp?method=list","rejectsmsList", rejectSMSService.listRejectSMS(curPage, iLineCnt, searchMap));
	}
	
	
	
	/**
	 * <p>수신거부자 삭제 
	 * @param maps
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String[] rejectIDs =  req.getParameterValues("eRejectID");	
		if(rejectIDs==null || rejectIDs.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		Map<String, Object>[] maps = new HashMap[rejectIDs.length];
		for(int i=0;i<rejectIDs.length;i++){
			maps[i] = new HashMap<String, Object>();
			maps[i].put("rejectID", new BigDecimal(rejectIDs[i]));
		}
		
			
		int[] resultVal = rejectSMSService.deleteRejectSMS(maps);
		
		if(resultVal!=null){
			//this.message = "삭제 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		return null;	
	}
	
	
	/**
	 * <p>수신거부직접 입력
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertTypeDirect(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String receiverPhone= ServletUtil.getParamString(req,"eReceiverPhone");		
		String userID= ServletUtil.getParamString(req,"eUserID");
		String groupID= ServletUtil.getParamString(req,"eGroupID");
		String[] receiverPhones = receiverPhone.split("\n", 1000);
		
		
		
		int resultVal = 0;
		for(int i =0;i<receiverPhones.length;i++)
		{
			RejectSMS rejectSMS = new RejectSMS();
			if(receiverPhones[i].indexOf("\n") != -1){
				
				rejectSMS.setReceiverPhone(receiverPhones[i].split("\n")[0]);
			}
			else if(receiverPhones[i].indexOf(",") != -1){
				
				rejectSMS.setReceiverPhone(receiverPhones[i].split(",")[0]);
			}
			else
				rejectSMS.setReceiverPhone(receiverPhones[i].replace("-", ""));
				rejectSMS.setUserID(userID);
				rejectSMS.setGroupID(groupID);
				
				if(StringUtil.isPhoneNumber(rejectSMS.getReceiverPhone())){
					resultVal = resultVal+rejectSMSService.insertRejectSMS(rejectSMS);	
				}
				
		}
		
		if(resultVal!=receiverPhones.length){
			this.message = resultVal+" 건 저장 하였습니다";
		}		
		this.sCurPage = "1"; 
		return list(req,res);
	}
	
	/**
	 * <p>파일업로드 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertTypeFile(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String userID= ServletUtil.getParamString(req,"eUserID");
		String groupID= ServletUtil.getParamString(req,"eGroupID");
		String newFile= ServletUtil.getParamString(req,"eNewFile");
		String eFileName= ServletUtil.getParamString(req,"eFileName");
		
		String filePath = this.realUploadPath+newFile;
		String fileType = "";
		if(eFileName.toLowerCase().lastIndexOf(".xls") > 0 || eFileName.toLowerCase().lastIndexOf("xlsx") >0){
			fileType="excel";
		}else{
			fileType="csvtxt";
		}
		
		//업로드된 파일일 처리할 스레드 가동 
		RejectSMSCSVThread rejectSMSCSVThread = new RejectSMSCSVThread(rejectSMSService,userID,groupID,filePath,fileType);
		rejectSMSCSVThread.start();
		
		return list(req,res);
		
	}
}
