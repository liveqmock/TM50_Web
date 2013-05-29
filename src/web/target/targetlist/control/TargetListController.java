package web.target.targetlist.control;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.admin.dbconnect.model.DbConnectColumn;
import web.common.model.FileUpload;
import web.common.util.*;
import web.target.targetlist.control.TargetingPreviewController;
import web.target.targetlist.model.*;
import web.target.targetlist.service.TargetListService;

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;


public class TargetListController extends MultiActionController{ 

	private TargetListService targetListService = null;
	private String sCurPage = null;
	private String message = "";
	private String realUploadPath = null;
	
	private String db_type = PropertiesUtil.getStringProperties("configure", "db_type");
	
	public void setTargetListService(TargetListService targetListService){
		this.targetListService = targetListService;
	}
	
	
	//action-servlet.xml에 저장된 업로드 경로를 읽어온다. 
	public void setRealUploadPath(String realUploadPath) {
		this.realUploadPath = realUploadPath;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
		return new ModelAndView("/pages/target/targetlist/targetlist.jsp");		
	}
	
	
	/**
	 * <p>대상자그룹 리스트   
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
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
	

		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		//검색조건 
		String sBookMark = ServletUtil.getParamString(req,"sBookMark");
		String sTargetType = ServletUtil.getParamString(req,"sTargetType");
		String sShareType = ServletUtil.getParamString(req,"sShareType");
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sTargetGroupID = ServletUtil.getParamString(req,"sTargetGroupID");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		/* 이메일,휴대폰,고객명 검색일때, fileimport 테이블에서 검색하여 targetID를 가져온다*/
		String sResult = "";
		if((sSearchText!=null&&!sSearchText.equals(""))&&(sSearchType.equals("emailType")||sSearchType.equals("phoneType")||sSearchType.equals("nameType")))
		{
			String sType = "";
			if(sSearchType.equals("emailType"))
				sType = "1";
			else if(sSearchType.equals("phoneType"))
				sType = "2";
			else if(sSearchType.equals("nameType"))
				sType = "3";
			
			sSearchType = "t.targetID";
			Map<String, String> searchMapSearch = new HashMap<String, String>(); 
			searchMapSearch.put("searchType", sType);
			searchMapSearch.put("searchText", sSearchText);
			sResult = targetListService.getTargetIDsBySearch(searchMapSearch);
			sSearchText = sResult;
			
		}
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 

		searchMap.put("bookMark", sBookMark);
		searchMap.put("targetType", sTargetType);
		searchMap.put("shareType", sShareType);
		searchMap.put("searchType", sSearchType);
		searchMap.put("targetGroupID", sTargetGroupID);
		searchMap.put("searchText", sSearchText);	
		
	
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		//총카운트 		
		int totalCount = targetListService.totalCountTargetList(userInfo, curPage, iLineCnt, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		req.setAttribute("bookMark", sBookMark);
				
		return new ModelAndView("/pages/target/targetlist/targetlist_proc.jsp?method=list","targetLists", targetListService.listTargetList(userInfo, curPage, iLineCnt, searchMap));

	}
	
	/**
	 * <p> 대상자등록창을 출력한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{		

		//페이징세팅 
		//ServletUtil.pageParamSetting(req);
		//메뉴아이디세팅
		//ServletUtil.meunParamSetting(req);
		
		
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		req.setAttribute("targetList", targetListService.viewTargetList(targetID));
		return new ModelAndView("/pages/target/targetlist/targetlist_proc.jsp","targetList", targetListService.viewTargetList(targetID));

	}
	

	/**
	 * <p>파일을 저장한다.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void fileUpload(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
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
				fileUpload = FileUploadUtil.uploadNewFile(mreq, multiFile, this.realUploadPath);
				fileUpload.setUploadKey(uploadKey);
				if(this.targetListService.insertFileUpload(fileUpload) < 0) {
					ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
					return;
				}
			}
		}
		
	}
	
	/**
	 * <p>파일을 다운로드 한다.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void fileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String uploadKey = ServletUtil.getParamString(req, "uploadKey");
		FileUpload fileUpload = null;
		
		fileUpload = targetListService.getFileInfo(uploadKey);
		
		//System.out.println("uploadKey==="+uploadKey);
	
		if(fileUpload != null) {
			
			//System.out.println("#"+ this.realUploadPath);
			//System.out.println("#"+ fileUpload.getNewFileName());
			//System.out.println("#"+ fileUpload.getRealFileName());			
			
			FileUploadUtil.downloadFile(req, res, this.realUploadPath, fileUpload.getNewFileName(), fileUpload.getRealFileName());
		}
	}
	
	
	/**
	 * <p>파일정보를 본다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getFileInfo(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		
		String uploadKey = ServletUtil.getParamStringDefault(req, "uploadKey", "");
		String targetID = ServletUtil.getParamString(req, "targetID");
		
		if(!targetID.equals("0") && uploadKey.equals("")) {
			TargetList targetList = targetListService.viewTargetList( Integer.parseInt(targetID) );
			if(targetList != null) uploadKey = targetList.getUploadKey();
		}
		
		FileUpload fileUpload= targetListService.getFileInfo(uploadKey);
		if(fileUpload != null) {
			
			if(!FileUploadUtil.existFile( fileUpload.getNewFileName(), this.realUploadPath) ) {
				return null;
			}
			
		}

		return new ModelAndView("/pages/target/targetlist/targetlist_proc.jsp","fileUpload", targetListService.getFileInfo(uploadKey));

	}
	
	
	/**
	 * <p>파일을 업로드 한후 처리밑 파일정보 보이기
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView uploadComplete(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String uploadKey = ServletUtil.getParamStringDefault(req, "uploadKey", "");
		String targetID = ServletUtil.getParamString(req, "targetID");
		
		// 수정이면
		if(!targetID.equals("0")) {
			
			TargetList targetList = targetListService.viewTargetList( Integer.parseInt(targetID) );
			String oldUploadKey = targetList.getUploadKey();
			if(targetList != null) {
				
				// 기존 파일을 삭제한다.
				FileUpload fileUpload = targetListService.getFileInfo(oldUploadKey);
				FileUploadUtil.deleteFile(fileUpload.getNewFileName(), this.realUploadPath);
			
				// 기존 파일 정보 데이타를 삭제한다.
				targetListService.deleteFileInfo(oldUploadKey);
			}
			
		}
		
		return new ModelAndView("/pages/target/targetlist/targetlist_proc.jsp","fileUpload", targetListService.getFileInfo(uploadKey));
	}
	

	/**
	 * <p> 파일 원투원 정보를 세팅하는 페이지로 이동 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getFileHeader(HttpServletRequest req, HttpServletResponse res) throws Exception{

		// 파일명 가져오기
		String uploadKey = ServletUtil.getParamString(req, "uploadKey");
		String targetID = ServletUtil.getParamStringDefault(req, "targetID","0");
		String preType = ServletUtil.getParamStringDefault(req, "preType","");
		String movePage = "/pages/target/targetlist/targetlist_proc.jsp";
		
		// 수정이면
		if(!targetID.equals("0") && uploadKey.equals("")) {
			TargetList targetList = targetListService.viewTargetList( Integer.parseInt(targetID) );
			if(targetList != null) uploadKey = targetList.getUploadKey();
		}
		FileUpload fileUpload = targetListService.getFileInfo(uploadKey);
		
		if(fileUpload != null) {
			List<FileHeaderInfo> fileHeaderInfoList = null;
			//만약 파일이 엑셀파일이라면 
			if(fileUpload.getRealFileName().toLowerCase().lastIndexOf(".xls")>0 || fileUpload.getNewFileName().toLowerCase().lastIndexOf(".xlsx")>0){
				if(fileUpload.getRealFileName().toLowerCase().lastIndexOf(".xlsx")>0){
					fileHeaderInfoList = getFileFirstColumnNameExcel2007(this.realUploadPath + fileUpload.getNewFileName());
				}else{
					fileHeaderInfoList = getFileFirstColumnNameExcel(this.realUploadPath + fileUpload.getNewFileName());
				}
			}else{
				
				fileHeaderInfoList = getFileFirstColumnName(this.realUploadPath + fileUpload.getNewFileName());	
			}
			
			if (fileHeaderInfoList == null) {
				ServletUtil.messageGoURL(res, "파일을 오픈할 수 없습니다.", "");
				return null;
			}
			if (fileHeaderInfoList.size()==0) {
				ServletUtil.messageGoURL(res, "컬럼정보를 확인할 수 없습니다.", "");
				return null;
			}
			int cnt = 0;
			for(FileHeaderInfo fileHeaderInfo: fileHeaderInfoList) {
				
				if(fileHeaderInfo.getTitle().trim().equals("")) {
					ServletUtil.messageGoURL(res,fileHeaderInfo.getColumnNumber()+ "번째 컬럼의 타이틀에 공백이 있습니다.\\n\\n수정 후 다시 시도하세요","");
					return null;	
				}
				if(preType.equals("add")){
					if(!checkAddColumnName(fileHeaderInfo.getTitle().trim(), targetID, cnt)){
						ServletUtil.messageGoURL(res,fileHeaderInfo.getColumnNumber()+ "번째 컬럼의 정보가 원본 대상자 정보와 다릅니다..\\n\\n수정 후 다시 시도하세요","");
						return null;
					}
					cnt++;
				}
			}
			if(preType.equals("add")){
				movePage = "/pages/target/targetlist/targetlist_add.jsp";
			}
			return new ModelAndView(movePage,"fileHeaderInfoList", fileHeaderInfoList);
			
		}
		
		return null;
		
	}
	
	
	
	
	/**
	 * <p> 직접입력 원투원 정보를 세팅하는 페이지로 이동 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getDirectHeader(HttpServletRequest req, HttpServletResponse res) throws Exception{

		// 파일명 가져오기
		String directText = ServletUtil.getParamString(req, "eDirectText");
		String preType = ServletUtil.getParamStringDefault(req, "preType","");
		String movePage = "/pages/target/targetlist/targetlist_proc.jsp";
		String targetID = ServletUtil.getParamString(req, "targetID");

		final int MAX_DIRECT_COUNT = 1000;
		
		int directTextCount = getCountDirectText(directText);
		
		if(directTextCount == 1){
			ServletUtil.messageGoURL(res, "대상인원이 없습니다.", "");
			return null;
		}
		//제목란 까지 포함해서 1001이 넘으면 안된다. 
		if(directTextCount > (MAX_DIRECT_COUNT+1)){
			ServletUtil.messageGoURL(res, "대상인원이 1,000명이 넘습니다. [입력한 대상인원 = "+directTextCount+"] \\n\\n파일(txt,csv)로 만들어 파일업로드를 이용하세요", "");
			return null;
		}
		
		
		List<FileHeaderInfo> fileHeaderInfoList = getDirectFirstColumnName(directText);
			if (fileHeaderInfoList == null) {
				ServletUtil.messageGoURL(res, "직접입력을 불러올수 없습니다.", "");
				return null;
			}
			int cnt = 0;
			for(FileHeaderInfo fileHeaderInfo: fileHeaderInfoList) {
				
				if(fileHeaderInfo.getTitle().trim().equals("")) {
					ServletUtil.messageGoURL(res,fileHeaderInfo.getColumnNumber()+ "번째 컬럼의 타이틀에 공백이 있습니다.\\n\\n수정 후 다시 시도하세요","");
					return null;
				}
				if(preType.equals("add")){
					
					if(!checkAddColumnName(fileHeaderInfo.getTitle().trim(), targetID, cnt)){
						ServletUtil.messageGoURL(res,fileHeaderInfo.getColumnNumber()+ "번째 컬럼( "+fileHeaderInfo.getTitle().trim()+" )의 정보가 원본 대상자 정보와 다릅니다..\\n\\n수정 후 다시 시도하세요","");
						return null;
					}
					cnt++;
				}
			}
			if(!checkDirectText(directText)){
				ServletUtil.messageGoURL(res, "입력한 정보가 잘 못 되었습니다.", "");
				return null;
			}
			if(preType.equals("add")){
				movePage = "/pages/target/targetlist/targetlist_add.jsp";
			}
			return new ModelAndView(movePage,"directHeaderInfoList", fileHeaderInfoList);
	}
	
	/**
	 * <p>대상자 파일등록
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		TargetList targetList = new TargetList();	
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_FILE);		//파일등록
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));
		targetList.setState(Constant.TARGET_STATE_READY);
		targetList.setUploadKey( ServletUtil.getParamString(req,"uploadKey"));  //파일 업로드 키 
		String uploadKey = ServletUtil.getParamString(req,"uploadKey");
		targetList.setDbID(Constant.TM5_DBID);  //파일등록이므로 기본 TM DB
		targetList.setBookMark("N");
	
		
		//파일이름을 가져온다.
		FileUpload fileUpload = targetListService.getFileInfo(targetList.getUploadKey());
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		int targetID = Integer.parseInt( ServletUtil.getParamStringDefault(req,"targetID","0") );
		
		int resultVal = 0;
		synchronized(this){
			
			//1. 대상자를 저장한다.
			resultVal = targetListService.insertTargetList(targetList);
			if(resultVal>0){
				targetID = targetListService.getMaxTargetID();
			
			
			//2. 원투원  정보를 저장
				String[] fileOneToOne = req.getParameterValues("eFileOneToOne");
				OnetooneTarget onetooneTarget = null;
				if(fileOneToOne != null) {
					for(int i=0; i < fileOneToOne.length; i ++ ) {
						
						if(fileOneToOne[i].indexOf("≠") >= 0) {
						
							String[] parseTarget = fileOneToOne[i].split("≠");
							
							onetooneTarget = new OnetooneTarget();
							onetooneTarget.setTargetID(targetID);
							onetooneTarget.setCsvColumnPos(Integer.parseInt(parseTarget[0]));
							onetooneTarget.setFieldName("col"+parseTarget[0]);
							onetooneTarget.setOnetooneID(Integer.parseInt(parseTarget[1]));
							onetooneTarget.setFieldDesc(parseTarget[2]);
							
							onetooneTargetList.add(onetooneTarget);
						
						}
					}
					resultVal = targetListService.insertOnetooneTarget(onetooneTargetList);
				}
			}
			
		}// end synchronized
			
		//3. 파일을 읽어들여 ez_fileimport_YYYYMM테이블에 인서트 할 스레드를 뛰운다.			
		if(resultVal>0 && fileUpload != null){
			String filePath = realUploadPath+fileUpload.getNewFileName();
			String fileType = "";
			if(fileUpload.getRealFileName().toLowerCase().lastIndexOf(".xls") > 0 || fileUpload.getRealFileName().toLowerCase().lastIndexOf("xlsx") >0){
				fileType="excel";
				if(fileUpload.getRealFileName().toLowerCase().lastIndexOf("xlsx") >0){
					fileType="excel2007";
				}
			}else{
				fileType="csvtxt";
			}
			TargetListFileThread fileThread = new TargetListFileThread(targetListService, "",uploadKey, targetID,filePath, onetooneTargetList,fileType,"");
			fileThread.start();
				
			if(targetID == 0) this.sCurPage = "1";		
			return list(req,res);			
		}
			
	
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
		return null;
		
	}
	
	
	/**
	 * <p>대상자 파일수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateFile(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		TargetList targetList = new TargetList();	
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		targetList.setTargetID(targetID);
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));		
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));	
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setTargetType(Constant.TARGET_TYPE_FILE);		//파일등록
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));			
		String oldUploadKey = ServletUtil.getParamString(req,"eOldUploadKey");		//기존업로드 키
		String uploadKey = ServletUtil.getParamString(req,"uploadKey");				//새업로드 키 
		targetList.setTargetTable(ServletUtil.getParamString(req,"eTargetTable"));
		boolean onoToOneChange = false;
		//-------------------------------------------------------------------
		//1. 기본정보 수정 
		//-------------------------------------------------------------------
		int resultVal = targetListService.updateTargetListFile(targetList);
		
		//-------------------------------------------------------------------
		//2. 기존 원투원 정보를 삭제한후에 다시 수집 (변경되었을지도 모르므로 아예 다시 인서트)
		//-------------------------------------------------------------------
		if(resultVal<=0){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
			return null;
		}
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		
		//새로운 원투원 정보를 저장한다. 
		String[] eCsvOneToOne = req.getParameterValues("eFileOneToOne");
		OnetooneTarget onetooneTarget = null;
		
		if(eCsvOneToOne != null) {
			int cnt = 0;
			for(int i=0; i < eCsvOneToOne.length; i ++ ) {
				
				if(eCsvOneToOne[i].indexOf("≠") >= 0) {
				
					String[] parseTarget = eCsvOneToOne[i].split("≠");
					
					onetooneTarget = new OnetooneTarget();
					onetooneTarget.setTargetID(targetID);
					onetooneTarget.setCsvColumnPos(Integer.parseInt(parseTarget[0]));
					onetooneTarget.setFieldName("col"+parseTarget[0]);
					onetooneTarget.setOnetooneID(Integer.parseInt(parseTarget[1]));
					onetooneTarget.setFieldDesc(parseTarget[2]);					
					onetooneTargetList.add(onetooneTarget);
					if(!checkOneToOneChange(Integer.parseInt(parseTarget[1]), targetID+"", cnt)){
						onoToOneChange = true;
					}
					
					cnt++;
				}
			} //end for 
		
			resultVal = targetListService.deleteAfterInsertOnetooneTarget(targetID, onetooneTargetList);
		
		
		}
		if(resultVal<0){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
			return null;
		}

		
		//--------------------------------------------------------------------------------------
		//3. 파일업로드파일이 변경되었다면
		//--------------------------------------------------------------------------------------
		if(!uploadKey.equals("")){		
			FileUpload fileUploadOld = null, fileUploadNew=null;
			fileUploadOld = targetListService.getFileInfo(oldUploadKey);
			
			// 기존 업로드된 파일정보 삭제 
			resultVal = targetListService.deleteFileInfo(oldUploadKey);

						
			//3. 파일을 읽어들여 ez_fileimport_YYYYMM테이블에 인서트 할 스레드를 뛰운다.			
			if(resultVal>0 && fileUploadOld != null){
				//기존파일 삭제 
				FileUploadUtil.deleteFile(fileUploadOld.getNewFileName(), this.realUploadPath);
				
				//새로운 파일 업로드 
				fileUploadNew = targetListService.getFileInfo(uploadKey);

				String filePath = realUploadPath+fileUploadNew.getNewFileName();
				String fileType = "";
				if(fileUploadNew.getRealFileName().toLowerCase().lastIndexOf(".xls") > 0 || fileUploadNew.getRealFileName().toLowerCase().lastIndexOf("xlsx") >0){
					fileType="excel";
					if(fileUploadNew.getRealFileName().toLowerCase().lastIndexOf("xlsx") >0){
						fileType="excel2007";
					}
				}else{
					fileType="csvtxt";
				}
				TargetListFileThread fileThread = new TargetListFileThread(targetListService, targetList.getTargetTable(), uploadKey,targetID,filePath, onetooneTargetList,fileType,"");
				fileThread.start();
	
			}
		
		}else if(onoToOneChange){
			
			FileUpload fileUploadOld = null;
			fileUploadOld = targetListService.getFileInfo(oldUploadKey);
			String filePath = realUploadPath+fileUploadOld.getNewFileName();
			String fileType = "";
			if(fileUploadOld.getRealFileName().toLowerCase().lastIndexOf(".xls") > 0 || fileUploadOld.getRealFileName().toLowerCase().lastIndexOf("xlsx") >0){
				fileType="excel";
				if(fileUploadOld.getRealFileName().toLowerCase().lastIndexOf("xlsx") >0){
					fileType="excel2007";
				}
			}else{
				fileType="csvtxt";
			}
			TargetListFileThread fileThread = new TargetListFileThread(targetListService, targetList.getTargetTable(), oldUploadKey,targetID,filePath, onetooneTargetList,fileType,"");
			fileThread.start();
			
		}
		
		if(targetID== 0) this.sCurPage = "1";		
		return list(req,res);		
		
		
	}
	
	
	/**
	 * <p>대상자 파일등록
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertDirect(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		TargetList targetList = new TargetList();	
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_DIRECT);		//파일등록
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));
		targetList.setState(Constant.TARGET_STATE_READY);
		targetList.setUploadKey( ServletUtil.getParamString(req,"uploadKey"));  //파일 업로드 키 , 수정일경우 업로드키가 없으면 targetid에서 찾아서처리
		targetList.setDbID(Constant.TM5_DBID);  //파일등록이므로 기본 TM DB
		targetList.setBookMark("N");
		String directText = ServletUtil.getParamStringDefault(req,"eDirectText","");  //직접입력창 
		targetList.setDirectText(directText);
		
				

		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		int targetID = 0;		
		int resultVal = 0;
		synchronized(this){
			
			//1. 대상자를 저장한다.
			resultVal = targetListService.insertTargetList(targetList);
			if(resultVal>0){
				targetID = targetListService.getMaxTargetID();
			
			
			//2. 원투원  정보를 저장
				String[] fileOneToOne = req.getParameterValues("eDirectOneToOne");
				OnetooneTarget onetooneTarget = null;
				if(fileOneToOne != null) {
					for(int i=0; i < fileOneToOne.length; i ++ ) {
						
						if(fileOneToOne[i].indexOf("≠") >= 0) {
						
							String[] parseTarget = fileOneToOne[i].split("≠");
							
							onetooneTarget = new OnetooneTarget();
							onetooneTarget.setTargetID(targetID);
							onetooneTarget.setCsvColumnPos(Integer.parseInt(parseTarget[0]));
							onetooneTarget.setFieldName("col"+parseTarget[0]);
							onetooneTarget.setOnetooneID(Integer.parseInt(parseTarget[1]));
							onetooneTarget.setFieldDesc(parseTarget[2]);
							
							onetooneTargetList.add(onetooneTarget);
						
						}
					}
					resultVal = targetListService.insertOnetooneTarget(onetooneTargetList);
				}
			}
			
		}// end synchronized
			
		//3. 텍스트를 읽어들여 ez_fileimport_YYYYMM테이블에 인서트 할 스레드를 뛰운다.			
		if(resultVal>0 && !directText.equals("")){
			String filePath = "";
			String fileType = "direct";
			TargetListFileThread fileThread = new TargetListFileThread(targetListService,"", targetList.getUploadKey(), targetID, filePath, onetooneTargetList,fileType, directText);
			fileThread.start();
				
			if(targetID == 0) this.sCurPage = "1";
			return list(req,res);			
		}
			
	
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
		return null;
		
	}
	
	
	/**
	 * <p>대상자 파일등록
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateDirect(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		TargetList targetList = new TargetList();	
		targetList.setTargetID(targetID);
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_DIRECT);		//파일등록
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));
		String directText = ServletUtil.getParamStringDefault(req,"eDirectText","");  //직접입력창 
		targetList.setDirectText(directText);
		targetList.setTargetTable(ServletUtil.getParamString(req,"eTargetTable"));
		
		
				
		//-------------------------------------------------------------------
		//1. 기본정보 수정 
		//-------------------------------------------------------------------
		int resultVal = targetListService.updateTargetListDirect(targetList);
		
		//-------------------------------------------------------------------
		//2. 기존 원투원 정보를 삭제한후에 다시 수집 (변경되었을지도 모르므로 아예 다시 인서트)
		//-------------------------------------------------------------------
		if(resultVal<=0){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
			return null;
		}
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		
		//새로운 원투원 정보를 저장한다. 
		String[] eCsvOneToOne = req.getParameterValues("eDirectOneToOne");
		OnetooneTarget onetooneTarget = null;
		
		if(eCsvOneToOne != null) {
			for(int i=0; i < eCsvOneToOne.length; i ++ ) {
				
				if(eCsvOneToOne[i].indexOf("≠") >= 0) {
				
					String[] parseTarget = eCsvOneToOne[i].split("≠");
					
					onetooneTarget = new OnetooneTarget();
					onetooneTarget.setTargetID(targetID);
					onetooneTarget.setCsvColumnPos(Integer.parseInt(parseTarget[0]));
					onetooneTarget.setFieldName("col"+parseTarget[0]);
					onetooneTarget.setOnetooneID(Integer.parseInt(parseTarget[1]));
					onetooneTarget.setFieldDesc(parseTarget[2]);
					//System.out.println("#"+parseTarget[2]);
					onetooneTargetList.add(onetooneTarget);
				
				}
			} //end for 
		
			resultVal = targetListService.deleteAfterInsertOnetooneTarget(targetID, onetooneTargetList);
		
		
		}
		if(resultVal<0){
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
			return null;
		}
		
			
		//3. 텍스트를 읽어들여 ez_fileimport_YYYYMM테이블에 인서트 할 스레드를 뛰운다.			
		if(resultVal>0 && !directText.equals("")){
			String filePath = "";
			String fileType = "direct";
			TargetListFileThread fileThread = new TargetListFileThread(targetListService, targetList.getTargetTable(), targetList.getUploadKey(), targetID, filePath, onetooneTargetList,fileType, directText);
			fileThread.start();
				
			if(targetID == 0) this.sCurPage = "1";
			return list(req,res);			
		}
			
	
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
		return null;
		
	}
	
	/**
	 * <p>DB접근키 확인
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView checkConfirm(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String targetID = ServletUtil.getParamString(req, "eTargetID");
		String dbID = ServletUtil.getParamString(req,"eDbID");
		String dbAccessKeyConfirmer = ServletUtil.getParamString(req,"eDbAccessKeyConfirmer");
		dbAccessKeyConfirmer = TmEncryptionUtil.encryptSHA256(dbAccessKeyConfirmer);
		String dbAccessKey = targetListService.getDbAccessKey(targetID, dbID);
		
		if(targetID.equals("0") && dbID.equals("")) {
			res.getWriter().print("dbfalse");
		} else if(dbAccessKey.equals(dbAccessKeyConfirmer)) {
			if(targetID.equals("0")) {
				res.getWriter().print("insertSuccess");
			} else {
				res.getWriter().print("updateSuccess");
			}
		} else {
			res.getWriter().print("false");
		}
		
		return null;
	}
	
	
	/**
	 * <p>대상자 DB추출용 쿼리 등록
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertQuery(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		TargetList targetList = new TargetList();	
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_DB);		//파일등록
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));
		targetList.setState(Constant.TARGET_STATE_FINISH);
		targetList.setBookMark("N");
		targetList.setQueryText(ServletUtil.getParamStringDefault(req,"eQueryText",""));		
		targetList.setDbID(ServletUtil.getParamString(req, "eDbID")); 
		targetList.setTargetCount(Integer.parseInt(ServletUtil.getParamStringDefault(req, "eTargetCount_edit", "0")));
		targetList.setCountQuery(ServletUtil.getParamStringDefault(req,"eCountQueryText",""));
		
		
	
		
		// 원투원  정보를 저장
		String[] queryOneToOnes = ServletUtil.getParamStringArray(req, "eQueryOneToOne");
		String[] descripts = ServletUtil.getParamStringArray(req, "eDescript");
		String[] fieldNames = ServletUtil.getParamStringArray(req, "eFieldName");
		
		
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		int targetID = 0;		
		int resultVal = 0;
		
		synchronized(this){
			
			//1. 대상자를 저장한다.
			resultVal = targetListService.insertTargetList(targetList);
			if(resultVal>0){
				targetID = targetListService.getMaxTargetID();
			
				if(resultVal==0 || targetID==0){
					resultVal = 0;
					
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
							if(!queryOneToOnes[i].equals("")) {
								onetooneTarget.setOnetooneID(Integer.parseInt(queryOneToOnes[i]));
							}
							onetooneTargetList.add(onetooneTarget);
						}
					}
					resultVal = targetListService.insertOnetooneTarget(onetooneTargetList);
					
					
				}

			}
			
		}// end synchronized
			
		if(resultVal>0) {
			if(targetID == 0) this.sCurPage = "1";
			return list(req,res);
		}
			
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		return null;		
	}
	
	
	/**
	 * <p>대상자 DB추출용 쿼리 수정 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateQuery(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
	
		TargetList targetList = new TargetList();	
		targetList.setTargetID(targetID);
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_DB);		//파일등록
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));
		targetList.setState(Constant.TARGET_STATE_FINISH);
		
		
		
		String queryUpdateYN = ServletUtil.getParamStringDefault(req,"eQueryUpdateYN","N");		//쿼리수정여부 
		
		//쿼리수정을 체크했다면 쿼리가 수정된 것으로 본다.
		if(queryUpdateYN.equals("Y")){
			targetList.setDbID(ServletUtil.getParamString(req, "eDbID"));
			targetList.setQueryText(ServletUtil.getParamString(req,"eQueryText"));
			targetList.setCountQuery(ServletUtil.getParamString(req,"eCountQueryText"));
			
			 
		}else{
			targetList.setDbID(ServletUtil.getParamString(req, "eDbID"));
			targetList.setQueryText(ServletUtil.getParamString(req,"eQueryTextOld"));
			targetList.setCountQuery(ServletUtil.getParamString(req,"eCountQueryTextOld"));
		}
		targetList.setTargetCount(ServletUtil.getParamInt(req,"eTargetCount_edit","0"));
	
		
		// 원투원  정보를 저장
		String[] queryOneToOnes = ServletUtil.getParamStringArray(req, "eQueryOneToOne");
		String[] descripts = ServletUtil.getParamStringArray(req, "eDescript");
		String[] fieldNames = ServletUtil.getParamStringArray(req, "eFieldName");
		
		
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		
		//-------------------------------------------------------------------
		//1. 기본정보 수정 
		//-------------------------------------------------------------------
		int resultVal = targetListService.updateTargetListQuery(targetList);
		
	
		//-------------------------------------------------------------------
		//2. 원투원 변경되었을지 모르므로 일단 삭제후 다시 입력 
		//-------------------------------------------------------------------
		if(resultVal>0){
			OnetooneTarget onetooneTarget = null;
			if(queryOneToOnes!=null && queryOneToOnes.length>0){
				for(int i=0; i < queryOneToOnes.length; i ++ ) {
					onetooneTarget = new OnetooneTarget();
					onetooneTarget.setTargetID(targetID);
					onetooneTarget.setFieldName(fieldNames[i]);
					onetooneTarget.setFieldDesc(descripts[i]);
					onetooneTarget.setCsvColumnPos(i+1);
					if(!queryOneToOnes[i].equals("")) {
						onetooneTarget.setOnetooneID(Integer.parseInt(queryOneToOnes[i]));
					}
					onetooneTargetList.add(onetooneTarget);
				}
			}
			resultVal = targetListService.deleteAfterInsertOnetooneTarget(targetID, onetooneTargetList);
		}
		

		if(resultVal>0) {
			
			if(targetID == 0) this.sCurPage = "1";
			return list(req,res);
		}
			
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		return null;		
	}
	
	/**
	 * <p>대상자 기존발송대상자 쿼리 등록
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertSended(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		TargetList targetList = new TargetList();	
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_SENDED);		//기존발송추출
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));
		targetList.setState(Constant.TARGET_STATE_FINISH);
		targetList.setBookMark("N");
		String massmailGroupID = ServletUtil.getParamStringDefault(req, "sMassmailGroupID", "0");
		if(massmailGroupID.equals("")){massmailGroupID="0";}
		targetList.setMassmailGroupID(Integer.parseInt(massmailGroupID));
		String dbID =Constant.TM5_DBID;
		targetList.setDbID(dbID); 
		targetList.setConnectedDbID(ServletUtil.getParamString(req, "eConnectedDB"));
		targetList.setTargetCount(Integer.parseInt(ServletUtil.getParamStringDefault(req, "eTargetCount", "0")));
		
		targetList.setSuccessYN(ServletUtil.getParamString(req,"eSuccessYN"));
		targetList.setOpenYN(ServletUtil.getParamString(req,"eOpenYN"));
		targetList.setClickYN(ServletUtil.getParamString(req,"eClickYN"));
		targetList.setSendedDate(ServletUtil.getParamString(req,"eSendedDate"));

		// 원투원  정보를 저장
		String[] eSendedOneToOne = ServletUtil.getParamStringArray(req, "eQueryOneToOne");
		
		String[] descripts = ServletUtil.getParamStringArray(req, "eDescript");
		
		String[] fieldNames = ServletUtil.getParamStringArray(req, "eFieldName");

		String queryText = ""; //퀴리 생성
		String emailField = ""; //Email Field
		String fields = ""; //SELECT Field 정보
		
		String connectedTable= ServletUtil.getParamString(req, "eConnectedTable");
		
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
	
		int resultVal = 0;
		
		synchronized(this){
			
			OnetooneTarget onetooneTarget = null;
			if(eSendedOneToOne!=null && eSendedOneToOne.length>0){
				
				for(int i=0; i < eSendedOneToOne.length; i ++ ) {
					onetooneTarget = new OnetooneTarget();
					//onetooneTarget.setTargetID(targetID);
					onetooneTarget.setFieldName(fieldNames[i]);
					onetooneTarget.setFieldDesc(descripts[i]);
					onetooneTarget.setCsvColumnPos(i+1);
					if(!eSendedOneToOne[i].equals("")) {
						onetooneTarget.setOnetooneID(Integer.parseInt(eSendedOneToOne[i]));
					}
					onetooneTargetList.add(onetooneTarget);
					fields +="C."+fieldNames[i]+", ";
					if(Integer.parseInt(eSendedOneToOne[i]) == 1){
						emailField = "C."+fieldNames[i];
					}
				}
			
				String tableName =" tm_massmail_sendresult_"+targetList.getSendedDate();
				//쿼리 생성
				queryText =  getSendedQueryText(fields.substring(0,fields.lastIndexOf(",")), tableName, connectedTable, emailField, targetList);
				targetList.setQueryText(queryText);
				//대상자 인원 수 확인 쿼리 생성
				String countQuery = QueryUtil.makeCountQuery(queryText);
				int targetCount = 0;
				//대상자 인원 수 확인 
				targetCount = getCountQueryText(getConnectionDBInfo(dbID), countQuery);
				targetList.setTargetCount(targetCount);
					
				//1. 대상자를 저장한다.
				resultVal = targetListService.insertTargetList(targetList);
				
				if(resultVal>0){
				//2.원투원 정보를 저장한다.
					targetID = targetListService.getMaxTargetID();
					for(int i =0; i < onetooneTargetList.size(); i++){
						onetooneTargetList.get(i).setTargetID(targetID);
					}
					resultVal = targetListService.insertOnetooneTarget(onetooneTargetList);
				}

			}
			
		}// end synchronized
			
		if(resultVal>0) {
			if(targetID == 0) this.sCurPage = "1";
			return list(req,res);
		}
			
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		return null;		
	}
	
	
	/**
	 * <p>대상자 기존발송대상자 수정
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateSended(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		
		TargetList targetList = new TargetList();
		targetList.setTargetID(targetID);
		targetList.setTargetName(ServletUtil.getParamString(req,"eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req,"eDescription"));
		targetList.setTargetGroupID(ServletUtil.getParamInt(req,"eTargetGroupID","0"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_SENDED);		//기존발송추출
		targetList.setShareType(ServletUtil.getParamString(req,"eShareType"));
		targetList.setState(Constant.TARGET_STATE_FINISH);
		targetList.setBookMark("N");
		String massmailGroupID = ServletUtil.getParamStringDefault(req, "sMassmailGroupID", "0");
		if(massmailGroupID.equals("")){massmailGroupID="0";}
		targetList.setMassmailGroupID(Integer.parseInt(massmailGroupID));
		
		String dbID =Constant.TM5_DBID;
		targetList.setDbID(dbID); 
		targetList.setConnectedDbID(ServletUtil.getParamString(req, "eConnectedDB"));
		targetList.setTargetCount(Integer.parseInt(ServletUtil.getParamStringDefault(req, "eTargetCount", "0")));
		
		targetList.setSuccessYN(ServletUtil.getParamString(req,"eSuccessYN"));
		targetList.setOpenYN(ServletUtil.getParamString(req,"eOpenYN"));
		targetList.setClickYN(ServletUtil.getParamString(req,"eClickYN"));
		targetList.setSendedDate(ServletUtil.getParamString(req,"eSendedDate"));
		
		// 원투원  정보를 저장
		String[] eSendedOneToOne = ServletUtil.getParamStringArray(req, "eQueryOneToOne");
		
		String[] descripts = ServletUtil.getParamStringArray(req, "eDescript");
		
		String[] fieldNames = ServletUtil.getParamStringArray(req, "eFieldName");

		String queryText = ""; //퀴리 생성
		String emailField = ""; //Email Field
		String fields = ""; //SELECT Field 정보
		
		String connectedTable= ServletUtil.getParamString(req, "eConnectedTable");
		
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
	
		int resultVal = 0;
		
		synchronized(this){
			
			OnetooneTarget onetooneTarget = null;
			if(eSendedOneToOne!=null && eSendedOneToOne.length>0){
				for(int i=0; i < eSendedOneToOne.length; i ++ ) {
					onetooneTarget = new OnetooneTarget();
					onetooneTarget.setTargetID(targetID);
					onetooneTarget.setFieldName(fieldNames[i]);
					onetooneTarget.setFieldDesc(descripts[i]);
					onetooneTarget.setCsvColumnPos(i+1);
					if(!eSendedOneToOne[i].equals("")) {
						onetooneTarget.setOnetooneID(Integer.parseInt(eSendedOneToOne[i]));
					}
					onetooneTargetList.add(onetooneTarget);
					fields +="C."+fieldNames[i]+", ";
					if(Integer.parseInt(eSendedOneToOne[i]) == 1){
						emailField = "C."+fieldNames[i];
					}
				}
			
				String tableName =" tm_massmail_sendresult_"+targetList.getSendedDate();
				//쿼리 생성
				queryText =  getSendedQueryText(fields.substring(0,fields.lastIndexOf(",")), tableName, connectedTable, emailField, targetList);
				targetList.setQueryText(queryText);
				//대상자 인원 수 확인 쿼리 생성
				String countQuery = QueryUtil.makeCountQuery(queryText);
				int targetCount = 0;
				//대상자 인원 수 확인 
				targetCount = getCountQueryText(getConnectionDBInfo(dbID), countQuery);
				targetList.setTargetCount(targetCount);
				
				//1. 대상자를 저장한다.
				resultVal = targetListService.updateTargetListSended(targetList);
				
				if(resultVal>0){
				//2.원투원 정보를 저장한다.
					resultVal = targetListService.deleteAfterInsertOnetooneTarget(targetID, onetooneTargetList);
				}

			}
			
		}// end synchronized
			
		if(resultVal>0) {
			if(targetID == 0) this.sCurPage = "1";
			return list(req,res);
		}
			
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		return null;		
	}
	
	
	
	/**
	 * <p>CSV,TXT에 컬럼명과 위치값을 가져온다. (CSV,TXT를 파싱하기 위한)
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<FileHeaderInfo> getFileFirstColumnName(String filePath) {
	
		BufferedReader br = null;
		String[] strLineArray = null;
		FileHeaderInfo fileHeaderInfo = null;
		
		List<FileHeaderInfo> result = new ArrayList();
		
		try{			
			br = new BufferedReader(new FileReader(filePath));
			String strLine = "";
			
			int line = 1;		//줄라인 
			while((strLine=br.readLine())!=null){		
				if(line==1){	//첫번째 줄은 필드명이다. 
					if(!strLine.equals("")){ 
						strLineArray = strLine.split(",");					
					}
					break;
				}				
				line++;
			}
			
			for(int i = 0; i < strLineArray.length; i ++) {
				fileHeaderInfo = new FileHeaderInfo();
				fileHeaderInfo.setColumnNumber( i+1 );
				fileHeaderInfo.setTitle(strLineArray[i]);
				
				result.add(fileHeaderInfo);
				
			}
			
		}catch(Exception e){
			logger.error(e);
		}finally{
			if(br!=null) try{br.close();}catch(IOException e1){}
		}					
		return result;		
	}
	
	/**
	 * <p>엑셀의 컬럼을 가져온다.
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileHeaderInfo> getFileFirstColumnNameExcel(String filePath) {
		Workbook workbook = null;
		FileHeaderInfo fileHeaderInfo = null;
		List<FileHeaderInfo> result = new ArrayList();
		try{
			workbook = Workbook.getWorkbook(new File(filePath));
			Sheet sheet = workbook.getSheet(0);
			Cell[] header = sheet.getRow(0);
			
			for(int i=0;i<header.length;i++){			
				fileHeaderInfo = new FileHeaderInfo();
				fileHeaderInfo.setColumnNumber( i+1 );
				fileHeaderInfo.setTitle(header[i].getContents());
				result.add(fileHeaderInfo);
			}
			
		}catch(Exception e){
			logger.error(e);
			return result;	
		}			
		return result;	
	}
	
	/**
	 * <p>엑셀2007의 컬럼을 가져온다.
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FileHeaderInfo> getFileFirstColumnNameExcel2007(String filePath) {
		List<FileHeaderInfo> result = new ArrayList();

		try{

			XSSFWorkbook xb= new XSSFWorkbook(filePath);
			XSSFExcelExtractor extractoc = new XSSFExcelExtractor(filePath);
			extractoc.setFormulasNotResults(true);
			extractoc.setIncludeSheetNames(false);
			int cellnum = 1;
			FileHeaderInfo fileHeaderInfo = null;
				for(Row row: xb.getSheetAt(0)){
					if(row.getRowNum() == 0){
					for(org.apache.poi.ss.usermodel.Cell cell : row){
						fileHeaderInfo = new FileHeaderInfo();
						fileHeaderInfo.setColumnNumber( cellnum  );
						switch (cell.getCellType())
   	   		         	{
   	   		         		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING : 
   	   		         		fileHeaderInfo.setTitle(cell.getRichStringCellValue().getString());
		         			break;
   	   		         		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC :
	   		         			
   	   		         			if(DateUtil.isCellInternalDateFormatted(cell)){
   	   		         				fileHeaderInfo.setTitle("" +cell.getDateCellValue());
   	   		         			}else{
   	   		         				fileHeaderInfo.setTitle("" +cell.getNumericCellValue());
   	   		         			}
	   		         		break;
   	   		         		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN :
   	   		         			fileHeaderInfo.setTitle("" +cell.getBooleanCellValue());
   	   		         		break;
   	   		         		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA : 
   	   		         			fileHeaderInfo.setTitle(cell.getCellFormula());
   	   		         		break;
		   	   		        
		   	   		        default:
		   	   		        	fileHeaderInfo.setTitle("");
   	   		         	}
						
						result.add(fileHeaderInfo);
						cellnum ++;
					}
				}
			}
			
		}catch(Exception e){
			logger.error(e);
			return result;	
		}			
		return result;	
	}
	
	/**
	 * <p>직접입력에서 컬럼을 가져온다.
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<FileHeaderInfo> getDirectFirstColumnName(String directText) {
	
		BufferedReader br = null;
		String[] strLineArray = null;
		FileHeaderInfo fileHeaderInfo = null;
		
	
		
		List<FileHeaderInfo> result = new ArrayList();
		
		try{			
			br = new BufferedReader(new StringReader(directText));
			String strLine = "";
			
			int line = 1;		//줄라인 
			while((strLine=br.readLine())!=null){		
				if(line==1){	//첫번째 줄은 필드명이다. 
					if(!strLine.equals("")){ 
						strLineArray = strLine.split(",");					
					}
					break;
				}				
				line++;
			}
			
			for(int i = 0; i < strLineArray.length; i ++) {
				fileHeaderInfo = new FileHeaderInfo();
				fileHeaderInfo.setColumnNumber( i+1 );
				fileHeaderInfo.setTitle(strLineArray[i]);
				
				//System.out.println("#"+i+"="+fileHeaderInfo.getTitle());
				
				result.add(fileHeaderInfo);
				
			}
			
		}catch(Exception e){
			logger.error(e);
		}finally{
			if(br!=null) try{br.close();}catch(IOException e1){}
		}					
		return result;		
	}
	
	/**
	 * <p>직접입력의 유효성 검사
	 * @return
	 * @throws IOException
	 */

	public boolean checkDirectText(String directText) {
		boolean result = true;
		BufferedReader br = null;
		int checkLength=0;
		try{
			br = new BufferedReader(new StringReader(directText));
			String strLine = "";
			
			int line = 1;		//줄라인 
			while((strLine=br.readLine())!=null){	
				if(line==1){
					checkLength = strLine.split(",").length;
					
				}else{
					while(strLine.indexOf(",,")!=-1)
					{
						strLine = strLine.replaceAll(",,", ", ,"); 
					}
					if(strLine.endsWith(","))
					{
						strLine = strLine + " ";
					}
					if(strLine.startsWith(","))
					{
						strLine = " " + strLine;
					}
					if(strLine.split(",").length != checkLength){
						result = false;
						break;
					}
				}
				line++;
			}
		}catch(Exception e){
			logger.error(e);
		}finally{
			if(br!=null) try{br.close();}catch(IOException e1){}
		}					
		return result;		
	}
	/**
	 * <p>직접입력에서 컬럼을 가져온다.
	 * @return
	 * @throws IOException
	 */

	public boolean checkAddColumnName(String ColumnName, String targetID, int cnt) {
		boolean result = false;
		List<OneToOne> ontooneList = targetListService.listAddOneToOne(new Integer(targetID));
		int chekcnt = 0;
		for(OneToOne onetoone: ontooneList) {
			//System.out.println(onetoone.getOnetooneID());
			if(onetoone.getFieldDesc().trim().equals(ColumnName) && chekcnt == cnt){
				result = true;
			}
			chekcnt++;
		}
		return result;		
	}
	
	/**
	 * <p>원투원정보 변경 확인
	 * @return
	 * @throws IOException
	 */

	public boolean checkOneToOneChange(int onetooneID, String targetID, int cnt) {
		boolean result = false;
		List<OneToOne> ontooneList = targetListService.listAddOneToOne(new Integer(targetID));
		int chekcnt = 0;
		for(OneToOne onetoone: ontooneList) {
			if( onetoone.getOnetooneID() == onetooneID && chekcnt == cnt){
				result = true;
			}
			chekcnt++;
		}
		return result;		
	}
	
	/**
	 * <p>CSV파일의 라인수를 읽어들여 리턴한다. 
	 * @param filePath
	 * @return
	 */
	private int getCountDirectText(String directText){		
		int count = 0;		
		BufferedReader br = null;		
		try{			
			br = new BufferedReader(new StringReader(directText));			
			while((br.readLine())!=null){				
				count++;
			}
		}catch(IOException e){
			count = 0;
			e.printStackTrace();
		}finally{
			try{br.close();}catch(IOException e1){}
		}		
		return count;  
	}
	
	
	/**
	 * <p>쿼리 테스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView makeOneToOneForQuery(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String query = ServletUtil.getParamString(req, "eQueryText");
		String dbID = ServletUtil.getParamString(req, "eDbID");
		String limitQuery = "";
		
		res.setContentType("text/html; charset=UTF-8");
		
		Map<String,Object> dbInfo = targetListService.getDBInfo( dbID );
		
		if(dbInfo == null) {
			ServletUtil.messageGoURL(res, "데이타 베이스 연결 정보가 올바르지 않습니다.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}

		Connection conn = getConnectionDBInfo(dbID);
		if(conn == null) {
			ServletUtil.messageGoURL(res, "데이타 베이스에 연결할 수 없습니다.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}
		
		//데이타 1개만 가져오기
		limitQuery = QueryUtil.makeTopCountQuery(query,String.valueOf(dbInfo.get("driverType")),1);
		
		if(limitQuery.equals("")) {
			ServletUtil.messageGoURL(res, "쿼리문을 확인할 수  없습니다.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}
		else
			System.out.println("limitQuery : " + limitQuery);
		
			
		// 쿼리를 수행하여
		List<String> columns = getQueryTextColumnName(conn, limitQuery);
		if(columns == null || columns.size()==0) {
			ServletUtil.messageGoURL(res, "컬럼정보를 확인할 수 없습니다.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}
		
		
		return new ModelAndView("/pages/target/targetlist/targetlist_proc.jsp","columns",columns);
		
	}	
	
	
	/**
	 * <p>쿼리 테스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView makeOneToOneForSended(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String dbid = ServletUtil.getParamString(req, "eConnectedDB");
	
		// 쿼리를 수행하여
		List<DbConnectColumn> dbConnectColumnList = targetListService.listConnectDBColumn(dbid);//getQueryTextColumnName(conn, limitQuery);
		if(dbConnectColumnList == null) {
			ServletUtil.messageGoURL(res, "일대일 치환 불러오기 오류.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}
		List<String> columns =  new ArrayList<String>();
		for(DbConnectColumn dbConnectColumn :  dbConnectColumnList ){
			columns.add(dbConnectColumn.getColumnName());
		}
		return new ModelAndView("/pages/target/targetlist/targetlist_proc.jsp","columns",columns);
		
	}	
	
	/**
	 * <p>dbID에 해당하는 접속정보를 가져오고 접속한다. 
	 * @param dbID
	 * @return
	 * @throws Exception 
	 */
	private Connection getConnectionDBInfo(String dbID) throws Exception{
		Connection conn = null;
		Map<String, Object> mapdb = targetListService.getDBInfo(dbID);
		
		if(mapdb==null || mapdb.size()==0){
			return null;
		}
		
		String driver = String.valueOf(mapdb.get("driverClass"));
		
		String sKey = StringUtil.createSecurityKey("TM", dbID, driver);
		
		String url = TmEncryptionUtil.decrypto(String.valueOf(mapdb.get("dbURL")), sKey);
		String user = TmEncryptionUtil.decrypto(String.valueOf(mapdb.get("dbUserID")), sKey);
		String password = TmEncryptionUtil.decrypto(String.valueOf(mapdb.get("dbUserPWD")), sKey);
		
		conn = DBUtil.getConnection(driver, url, user, password);
		
		return conn;
	}
	
	/**
	 * <p>쿼리에 해당하는 컬럼명을 얻어온다. 이 컬럼명을 얻어와서 원투원치환때 사용한다. 
	 * @param conn
	 * @param queryText
	 * @return
	 */
	private List<String> getQueryTextColumnName(Connection conn, String queryText){
		PreparedStatement ps = null;	
		ResultSetMetaData rsmd = null;
		ResultSet rs = null;
		List<String> columns = null;
		
		try{
			columns = new ArrayList<String>();
			
			ps = conn.prepareStatement(queryText);
			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();			
		  			
			for(int i=1;i<=numberOfColumns;i++){
				columns.add(rsmd.getColumnLabel(i));
				
			}
			
		}catch(Exception e){
			logger.error(e);
			System.out.println(e);
		}finally{
			try{ps.close();}catch(Exception e){}			
			try{conn.close();}catch(Exception e){}	
			rsmd=null;
		}		
		
		return columns;
	}
	
	
	/**
	 * <p>쿼리 테스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void runCountQuery(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String query = ServletUtil.getParamString(req, "eCountQueryText");
		String dbID = ServletUtil.getParamString(req, "eDbID");
		String eCustomInputYN = ServletUtil.getParamString(req, "eCustomInputYN");
		int count = 0;
		
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		
		Map<String,Object> dbInfo = targetListService.getDBInfo( dbID );
		
		if(dbInfo == null) {
			ServletUtil.messageGoURL(res, "데이타 베이스 연결 정보가 올바르지 않습니다.\\n\\n관리자에게 문의 하세요", "");
			return;
		}

		Connection conn = getConnectionDBInfo(dbID);
		if(conn == null) {
			ServletUtil.messageGoURL(res, "데이타 베이스에 연결할 수 없습니다.\\n\\n관리자에게 문의 하세요", "");
			return;
		}

		// 쿼리를 수행하여
		try {
			count = getCountQueryText(conn, query );
		} catch (Exception e) {}
		if(count == -1) {
			if(!eCustomInputYN.equals("Y"))
				ServletUtil.messageGoURL(res, "쿼리문 오류.\\n\\n인원수 계산 쿼리를 직접 입력 하신 후 \\n\\n다시 시도해 보세요", "");
			else
				ServletUtil.messageGoURL(res, "쿼리문 오류.\\n\\n인원수 계산 쿼리를 다시 입력 하신 후 \\n\\n시도해 보세요", "");
			return;
		}
		
		out.print(count);
		
		//return new ModelAndView("/pages/target/targeting/targeting_proc.jsp","columns",columns);
		
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
	 * <p>쿼리 테스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void getCountQuery(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String query = ServletUtil.getParamString(req, "eQueryText");
		String dbID = ServletUtil.getParamString(req, "eDbID");
		String countQuery = "";
		
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		
		Map<String,Object> dbInfo = targetListService.getDBInfo( dbID );
		
		
		if(dbInfo != null) {			
			
			countQuery = QueryUtil.makeCountQuery(query); 
			out.print(countQuery);
			//System.out.println("countQuery : " + countQuery);
			
		} else {
			ServletUtil.messageGoURL(res, "지원하지 않는 DB타입 입니다.", "");
		}
		
		//return new ModelAndView("/pages/target/targeting/targeting_proc.jsp","countQuery",countQuery);
		
	}
	
	/**
	 * <p>즐겨찾기 업데이트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView updateBookMark(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] targetIDs =  req.getParameterValues("eTargetID");	
		String bookMarkYN = ServletUtil.getParamStringDefault(req, "bookMarkYN","N");

		if(targetIDs==null || targetIDs.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 업데이트할  파라미터 정보가 없습니다.","");
			return null;
		}
		Map<String, Object>[] maps = new HashMap[targetIDs.length];
		for(int i=0;i<targetIDs.length;i++){
			maps[i] = new HashMap<String, Object>();
			maps[i].put("bookMark", bookMarkYN);
			maps[i].put("targetID", new Integer(targetIDs[i]));
		}
		int[] resultVal = targetListService.updateBookMark(maps);
		if(resultVal!=null){
			//this.message = "업데이트 되었습니다.";
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"수정에 실패 하였습니다.","");		
		}
		return null;	
	}
	
	/**
	 * <p>기존발송 추출에서 조건에 맞는 쿼리를 생성한다.
	 * @return
	 * @throws IOException
	 */
	public String getSendedQueryText(String fields, String tableName, String connectedTable, String emailField, TargetList targetList) throws Exception{
	
		String queryText = "SELECT "+fields +" FROM "+tableName +" R INNER JOIN "+connectedTable+" C ON R.email = "+emailField+" WHERE 1=1 ";
		if(targetList.getMassmailGroupID() > 0){
			queryText +=" AND R.massmailGroupID="+targetList.getMassmailGroupID()+" ";
		}
		if(targetList.getSuccessYN().equals("Y")){
			queryText +=" AND R.smtpCodeType = 0 ";
		}
		if(targetList.getSuccessYN().equals("N")){
			queryText +=" AND R.smtpCodeType > 0 ";
		}
		if(!targetList.getOpenYN().equals("")){
			queryText +=" AND R.openYN ='"+targetList.getOpenYN()+"' ";
		}
		if(!targetList.getClickYN().equals("")){
			queryText +=" AND R.openYN ='"+targetList.getClickYN()+"' ";
		}
		return queryText;		
	}
	
	
	/**
	 * <p대상자 그룹 삭제 - 실제 삭체처리하진 않고 tm_target_list.bookMark='D'로 변경한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		int resultVal = targetListService.deleteTargetList(targetID);
		
		if(resultVal>0){		
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제처리에 실패했습니다.","");
		}
		return null;
		
	}

	
	
		
	/**
	 * <p>대상자 파일등록
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertDirectAdd(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		String addType = ServletUtil.getParamStringDefault(req,"eAddType","");  //(1:추가 / 2:제외)
		String addTypeInput = ServletUtil.getParamStringDefault(req,"eAddTypeInput","");  //(1:직접입력 / 2:파일업로드)
		String directText = ServletUtil.getParamStringDefault(req,"eDirectText","");  //직접입력창
		String targetTable = ServletUtil.getParamStringDefault(req,"eTargetTable","");
		TargetListAdd targetList = new TargetListAdd();	
		targetList.setTargetID(targetID);
		targetList.setAddType(addType);
		targetList.setAddTypeInput(addTypeInput);
		targetList.setDirectText(directText);

		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();		
		int resultVal = 0;
		synchronized(this){
			
			//1. 대상자를 저장한다.
			resultVal = targetListService.insertTargetListAdd(targetList);
			if(resultVal>0){
				//2. 원투원  정보
				String[] fileOneToOne = req.getParameterValues("eDirectOneToOne");
				OnetooneTarget onetooneTarget = null;
				if(fileOneToOne != null) {
					for(int i=0; i < fileOneToOne.length; i ++ ) {
							
						if(fileOneToOne[i].indexOf("≠") >= 0) {
							
							String[] parseTarget = fileOneToOne[i].split("≠");
								
							onetooneTarget = new OnetooneTarget();
							onetooneTarget.setCsvColumnPos(Integer.parseInt(parseTarget[0]));
							onetooneTarget.setFieldName("col"+parseTarget[0]);
							onetooneTarget.setOnetooneID(Integer.parseInt(parseTarget[1]));
							onetooneTarget.setFieldDesc(parseTarget[2]);
							onetooneTargetList.add(onetooneTarget);
						}
					}
				}
			}
		}// end synchronized
			
		//3. 텍스트를 읽어들여 ez_fileimport_YYYYMM테이블에 인서트 할 스레드를 뛰운다.			
		if(!directText.equals("")){
			String filePath = "";
			String fileType = "direct";

			TargetListFileAddThread fileaddThread = new TargetListFileAddThread(targetListService,targetTable, targetList.getUploadKey(), targetID, filePath, onetooneTargetList,fileType, directText, addType);
			fileaddThread.start();
						
			return null;
		}
			
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
		return null;
		
	}
	
	/**
	 * <p>대상자 파일등록
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertFileAdd(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		String addType = ServletUtil.getParamStringDefault(req,"eAddType","");  //(1:추가 / 2:제외)
		String addTypeInput = ServletUtil.getParamStringDefault(req,"eAddTypeInput","");  //(1:직접입력 / 2:파일업로드)
		String uploadKey = ServletUtil.getParamStringDefault(req,"uploadKey","");  //직접입력창
		String targetTable = ServletUtil.getParamStringDefault(req,"eTargetTable","");
		TargetListAdd targetList = new TargetListAdd();	
		targetList.setTargetID(targetID);
		targetList.setAddType(addType);
		targetList.setAddTypeInput(addTypeInput);
		targetList.setUploadKey(uploadKey);
		//파일이름을 가져온다.
		FileUpload fileUpload = targetListService.getFileInfo(targetList.getUploadKey());
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		
		int resultVal = 0;
		synchronized(this){
			
			//1. 대상자를 저장한다.
			resultVal = targetListService.insertTargetListAdd(targetList);
			if(resultVal>0){
				//2. 원투원  정보를 저장
				String[] fileOneToOne = req.getParameterValues("eFileOneToOne");
				OnetooneTarget onetooneTarget = null;
				if(fileOneToOne != null) {
					for(int i=0; i < fileOneToOne.length; i ++ ) {
						if(fileOneToOne[i].indexOf("≠") >= 0) {
							String[] parseTarget = fileOneToOne[i].split("≠");
								
							onetooneTarget = new OnetooneTarget();
							onetooneTarget.setCsvColumnPos(Integer.parseInt(parseTarget[0]));
							onetooneTarget.setFieldName("col"+parseTarget[0]);
							onetooneTarget.setOnetooneID(Integer.parseInt(parseTarget[1]));
							onetooneTarget.setFieldDesc(parseTarget[2]);
								
							onetooneTargetList.add(onetooneTarget);
						}
					}	
				}
			}
		}// end synchronized
		
		//3. 파일을 읽어들여 ez_fileimport_YYYYMM테이블에 인서트 할 스레드를 뛰운다.			
		if(resultVal>0 && fileUpload != null){
			String filePath = realUploadPath+fileUpload.getNewFileName();
			String fileType = "";
			if(fileUpload.getRealFileName().toLowerCase().lastIndexOf(".xls") > 0 || fileUpload.getRealFileName().toLowerCase().lastIndexOf("xlsx") >0){
				fileType="excel";
				if(fileUpload.getRealFileName().toLowerCase().lastIndexOf("xlsx") >0){
					fileType="excel2007";
				}
			}else{
				fileType="csvtxt";
			}
			TargetListFileAddThread fileAddThread = new TargetListFileAddThread(targetListService, targetTable , uploadKey, targetID, filePath, onetooneTargetList,fileType,"",addType);
			fileAddThread.start();
				
			return null;	
		}
			
	
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");		
		return null;
		
	}
	
	/**
	 * <p> 대상자 미리보기 창을 출력한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView previewList(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		if (curPage <= 0) curPage = 1;
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 32: 38;
		int iLineCnt = 20;	// 라인 갯수

		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}
		
		this.message = ""; // 다음 호출을 위해 초기화

		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);

		
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		TargetList temp_target=targetListService.viewTargetList(targetID);
		String query = temp_target.getQueryText();  
		String dbId = temp_target.getDbID();//ServletUtil.getParamString(req,"dbId");
		int totalCount = 0;	//총카운트 
		
		TargetingPreviewController control = null; 
		if(dbId.equals("10") && !temp_target.getTargetType().equals("4")&& !temp_target.getTargetType().equals("5"))
		{
			totalCount =getTargetPreviewListTotalCount(sSearchType, sSearchText, query);
		}
		else
		{ 
			control = TargetingPreviewController.getInstance(targetListService);
			totalCount =control.getTargetPreviewListTotalCount(query, dbId, sSearchType, sSearchText);
		}

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
		
		if(dbId.equals("10") && !temp_target.getTargetType().equals("4")&& !temp_target.getTargetType().equals("5"))
			return new ModelAndView("/pages/target/targetlist/target_preview_list.jsp?method=previewList","targetPreviewList",getTargetPreviewList(targetID, query, sSearchType, sSearchText, curPage, iLineCnt, totalCount ));
		else
			return new ModelAndView("/pages/target/targetlist/target_preview_list.jsp?method=previewList","targetPreviewList", control.getTargetPreviewList(targetID, query, dbId, sSearchType, sSearchText, curPage, iLineCnt, totalCount ));
	}
	
	/**
	 * <p>대상자그룹 리스트   
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addHistory(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int targetID = ServletUtil.getParamInt(req,"targetID","0");
		return new ModelAndView("/pages/target/targetlist/targetlist_add.jsp?method=list","addLists", targetListService.addHistoryList(targetID));

	}
	
	/**
	 * <p>미리보기 카운트 가져오기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public int getTargetPreviewListTotalCount(String sSearchType, String sSearchText, String query)throws Exception 
	{
		String sql = query.substring(query.toUpperCase().indexOf(" FROM "));
				
		if(sSearchText!=null && !sSearchText.equals(""))
		{			
			sql = sql + " AND " + sSearchType + " LIKE '%" + sSearchText + "%' ";					
				 
		}		
		sql = "select count(*) " + sql;		
		
		return targetListService.getTargetPreviewListTotalCount(sql);
		
	}
	
	/**
	 * <p>미리보기 리스트 가져오기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public List<String[]> getTargetPreviewList(int targetID, String query, String sSearchType, String sSearchText, int currentPage, int iLineCnt, int total) throws Exception
	{
		query = query.replace("select ", "select fileImportID, ");
		query = query.replace("SELECT ", "SELECT fileImportID, ");
				
		String sql = query ;

		List<OnetooneTarget> onetoone = targetListService.viewOnetooneTarget(targetID);
		String[] onetoFieldName = new String[onetoone.size()];
		String[] onetoFieldDesc = new String[onetoone.size()];
		
		for(int i = 0; i < onetoone.size(); i++){
			
			OnetooneTarget ot = onetoone.get(i);
			onetoFieldName[i] = ot.getFieldName();	
			onetoFieldDesc[i] = ot.getFieldDesc();	
		}
				
		if(sSearchText!=null && !sSearchText.equals(""))
		{			
			sql = sql + " AND " + sSearchType + " LIKE '%" + sSearchText + "%' ";	
			
		}
		int count = iLineCnt * (currentPage - 1);
		if(db_type.equalsIgnoreCase("oracle"))
			iLineCnt = iLineCnt * currentPage;
		sql = QueryUtil.getPagingQueryByDB(db_type, sql, onetoFieldName[0], String.valueOf(count), String.valueOf(iLineCnt));
		
		return targetListService.getTargetPreviewList(sql, onetoFieldName);
		
	}
	/**
	 * <p>미리보기 수정용 리스트 가져오기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public List<String[]> getTargetPreviewListForEdit(int targetID, String query, String[] targetPreviewIDs) throws Exception
	{
		query = query.replace("select ", "select fileImportID, ");
		query = query.replace("SELECT ", "SELECT fileImportID, ");
				
		String sql = query ;
		
		List<OnetooneTarget> onetoone = targetListService.viewOnetooneTarget(targetID);
		String[] onetoFieldName = new String[onetoone.size()];
		String[] onetoFieldDesc = new String[onetoone.size()];
		
		for(int i = 0; i < onetoone.size(); i++){
			
			OnetooneTarget ot = onetoone.get(i);
			onetoFieldName[i] = ot.getFieldName();	
			onetoFieldDesc[i] = ot.getFieldDesc();	
		}
		String temp = " AND fileImportID in (";
		for(int i = 0; i < targetPreviewIDs.length ; i++)
		{
			if(i==0)
				temp = temp+" " +  targetPreviewIDs[i];
			else 
				temp = temp+", " +  targetPreviewIDs[i];
			
		}
		temp = temp+")";
					
		sql = sql + temp;	
		
		return targetListService.getTargetPreviewList(sql, onetoFieldName);
		
	}
	
	public ModelAndView sendHistoryAddTarget(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String sendedYear = ServletUtil.getParamString(req, "sSendedYear");
		String sendedMonth = ServletUtil.getParamString(req, "sSendedMonth");
		String sendYN = ServletUtil.getParamString(req, "sSendYN");
		String openYN = ServletUtil.getParamString(req, "sOpenYN");
		String clickYN = ServletUtil.getParamString(req, "sClickYN");
		String rejectcallYN = ServletUtil.getParamString(req, "sRejectcallYN");
		String searchType = ServletUtil.getParamString(req,"sSearchType");
		String searchText = ServletUtil.getParamString(req,"sSearchText");
		String nameUseYN = ServletUtil.getParamString(req,"eNameUseYN");
		String dbID = Constant.TM5_DBID;
		int count = 0;
		
		// 고객명 대처 문자 처리 (email 또는 입력 문자)
		String instead = ServletUtil.getParamString(req,"eInstead");
		if(instead.trim().equals("")){
			instead = " email ";
		}else{
			instead = " '"+instead+"' ";
		}
		// 고객명 처리 쿼리 생성
		String nameField ="";
		if(nameUseYN.equals("Y")){
			nameField = QueryUtil.getStringQuery("targetlist_sql","target.sendhistory.namefield1")+instead+QueryUtil.getStringQuery("targetlist_sql","target.sendhistory.namefield2");
		}
		
		if(Integer.parseInt(sendedMonth)<10){
			sendedMonth="0"+sendedMonth;
		}
		
		//쿼리문 생성
		String query ="SELECT email "+nameField+" FROM tm_massmail_sendresult_"+sendedYear+sendedMonth+" s WHERE 1=1 ";
		
		//검색 조건 처리
		if(searchText!=null && !searchText.equals("")){
			query += " AND  "+searchType+" LIKE '%" + searchText + "%' ";
		}	
		if(sendYN.equals("Y")){
			query += " AND smtpCodeType=0 ";
		}else if(sendYN.equals("N")){
			query += " AND smtpCodeType>0 ";
		}
		if(openYN!=null && !openYN.equals("")){
			query += " AND  openYN='"+openYN+"' ";
		}
		if(clickYN!=null && !clickYN.equals("")){
			query += " AND  clickYN='"+clickYN+"' ";
		}
		if(rejectcallYN!=null && !rejectcallYN.equals("")){
			query += " AND  rejectcallYN='"+rejectcallYN+"' ";
		}
		// 카운트 쿼리 생성
		String countQuery = QueryUtil.makeCountQuery(query);
		
		//DB 연경정보 확인
		Map<String,Object> dbInfo = targetListService.getDBInfo( dbID );
		
		if(dbInfo == null) {
			ServletUtil.messageGoURL(res, "데이타 베이스 연결 정보가 올바르지 않습니다.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}
		//DB 연결 확인
		Connection conn = getConnectionDBInfo(dbID);
		if(conn == null) {
			ServletUtil.messageGoURL(res, "데이타 베이스에 연결할 수 없습니다.\\n\\n관리자에게 문의 하세요", "");
			return null;
		}

		// 쿼리를 수행하여 예상인원 수를 받아온다.
		try {
			count = getCountQueryText(conn, countQuery );
		} catch (Exception e) {}
		System.out.println("count : "+count);
		if(count < 0) {
			ServletUtil.messageGoURL(res, "선택하신 날짜에 발송 이력이 없습니다.", "");
			return null;
		}
		
		TargetList targetList = new TargetList();	
		targetList.setTargetName(ServletUtil.getParamString(req, "eTargetName"));
		targetList.setDescription(ServletUtil.getParamString(req, "eDescription"));
		targetList.setUserID(LoginInfo.getUserID(req));
		targetList.setTargetType(Constant.TARGET_TYPE_SENDHISTORY);		//발송히스토리 추출
		targetList.setShareType(ServletUtil.getParamString(req, "eShareType"));
		targetList.setState(Constant.TARGET_STATE_FINISH);
		targetList.setBookMark("N");
		targetList.setQueryText(query);		
		targetList.setDbID(dbID); 
		targetList.setTargetCount(count);
		targetList.setCountQuery(countQuery);
		targetList.setSendedDate(sendedYear+sendedMonth);
		targetList.setSuccessYN(sendYN);
		targetList.setOpenYN(openYN);
		targetList.setClickYN(clickYN);
		targetList.setRejectcallYN(rejectcallYN);
		
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		int targetID = 0;		
		int resultVal = 0;
		synchronized(this){
			
			//1. 대상자를 저장한다.
			resultVal = targetListService.insertTargetList(targetList);
			if(resultVal>0){
				targetID = targetListService.getMaxTargetID();
			
				if(resultVal==0 || targetID==0){
					resultVal = 0;
					
				//2. 원투원 정보를 입력한다. 	
				}else{
					OnetooneTarget onetooneTarget = null;
					onetooneTarget = new OnetooneTarget();
					onetooneTarget.setTargetID(targetID);
					onetooneTarget.setFieldName("email");
					onetooneTarget.setFieldDesc("[이메일]");
					onetooneTarget.setCsvColumnPos(1);
					onetooneTarget.setOnetooneID(1);
					onetooneTargetList.add(onetooneTarget);
					if(nameUseYN.equals("Y")){
						onetooneTarget = null;
						onetooneTarget = new OnetooneTarget();
						onetooneTarget.setTargetID(targetID);
						onetooneTarget.setFieldName("name");
						onetooneTarget.setFieldDesc("[고객명]");
						onetooneTarget.setCsvColumnPos(2);
						onetooneTarget.setOnetooneID(3);
						onetooneTargetList.add(onetooneTarget);
					}
					
					resultVal = targetListService.insertOnetooneTarget(onetooneTargetList);
					
				}

			}
			
		}// end synchronized
		ServletUtil.messageGoURL(res,"대상자 등록을 완료 하였습니다.","");
		return null;	
	}
	
	/**
	 * <p>대상자 미리보기에서 이메일 삭제
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView deletePreviewList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] targetPreviewIDs =  req.getParameterValues("eTargetPreviewID");
		String targetTableName = ServletUtil.getParamString(req, "tableName");
		int targetID = ServletUtil.getParamInt(req, "targetID","0");
		Map<String, Object>[] maps = new HashMap[targetPreviewIDs.length];
		for(int i=0;i<targetPreviewIDs.length;i++){
			maps[i] = new HashMap<String, Object>();
			maps[i].put("fileImportID", new Integer(targetPreviewIDs[i]));
		}
		int[] resultVal = targetListService.deletePreviewList(maps, targetTableName);
		String state ="";
		if(resultVal!=null){
			state = Constant.TARGET_STATE_FINISH;
			targetListService.updateTargetingAddEnd(targetTableName, state, targetID);
		}else{
			state = Constant.TARGET_STATE_ERROR;
			targetListService.updateTargetingAddEnd(targetTableName, state, targetID);
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		return null;	
		
	}
	
	/**
	 * <p> 대상자 미리보기에서 수정할 이메일 조회
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView previewEditList(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String[] targetPreviewIDs =  req.getParameterValues("eTargetPreviewID");
		int targetID = ServletUtil.getParamInt(req, "targetID","0");
		this.message = ""; // 다음 호출을 위해 초기화
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		TargetList temp_target=targetListService.viewTargetList(targetID);
		String query = temp_target.getQueryText();  
		
		return new ModelAndView("/pages/target/targetlist/target_preview_modify.jsp?method=list","targetPreviewList",getTargetPreviewListForEdit(targetID, query,targetPreviewIDs ));
	}
	
	/**
	 * <p>대상자 미리보기에서 이메일 삭제
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView editPreview(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String[] targetPreviewIDs =  req.getParameterValues("eTargetPreviewID");
		String targetTableName = ServletUtil.getParamString(req, "tableName");
		int targetID = ServletUtil.getParamInt(req, "targetID","0");
		List<OneToOne> onetooneTargetList = targetListService.listAddOneToOne(new Integer(targetID));
		
		Map<String, Object>[] maps = new HashMap[targetPreviewIDs.length];
		for(int i=0;i<targetPreviewIDs.length;i++){
			maps[i] = new HashMap<String, Object>();
			maps[i].put("fileImportID", new Integer(targetPreviewIDs[i]));
			for(OneToOne oneToOne : onetooneTargetList)
				maps[i].put(oneToOne.getFieldName(), ServletUtil.getParamString(req, "e_"+oneToOne.getFieldName()+"_"+targetPreviewIDs[i]));
		}
		
		int[] resultVal = targetListService.updatePreviewList(maps, targetTableName, onetooneTargetList);
		String state ="";
		if(resultVal!=null){
			state = Constant.TARGET_STATE_FINISH;
			targetListService.updateTargetingAddEnd(targetTableName, state, targetID);
		}else{
			state = Constant.TARGET_STATE_ERROR;
			targetListService.updateTargetingAddEnd(targetTableName, state, targetID);
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		
		return null;	
		
	}
	
	
	
	/**
	 * <p>대상자 미리보기에서 폼 별 저장
	 * @param req
	 * @param res
	 * @author 김용연
	 * @return
	 * @throws Exception
	 */	
	public ModelAndView formTypeSave(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] addFormTypeText = req.getParameterValues("eAddFormType");
		String tableName = ServletUtil.getParamString(req, "tableName");
		int targetID = ServletUtil.getParamInt(req, "targetID", "0");
		
		int resultVal = 0;
		
		resultVal = targetListService.insertPreviewFormType(targetID, tableName, addFormTypeText); // serviceImpl - 대상자 데이터 저장 , 대상자 그룹 총인원수 +1
		
		if(resultVal == 0){
			ServletUtil.messageGoURL(res,"저장에 실패 하였습니다.","");
		}		
		
	 return null;
	}
}
