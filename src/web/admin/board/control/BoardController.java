package web.admin.board.control;



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


import web.admin.board.service.BoardService;
import web.admin.board.model.*;
//import web.common.util.FileUploadUtil;
import web.common.util.FileUploadUtil;
import web.common.util.ServletUtil;
import web.common.model.FileUpload;


/**
 * <p>공지사항 controller
 * @author coolang
 *
 */
public class BoardController  extends MultiActionController {

	
	private BoardService boardService = null;
 	private String realUploadPath = null;
	private String sCurPage = null;
	private String message = "";
	
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}
	

	
	//action-servlet.xml에 저장된 업로드 경로를 읽어온다. 
	public void setRealUploadPath(String realUploadPath) {
		this.realUploadPath = realUploadPath;
	}
	


	/**
	 * <p> 공지사항 리스트를 출력한다. 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */

	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/admin/board/notice_board.jsp");
	}
	public ModelAndView listShow(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int groupID = ServletUtil.getParamInt(req,"groupID","1");
				
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		return new ModelAndView("/pages/main/show_notice_proc.jsp?id=board-panel&mode=list","boardList",boardService.listShow(groupID));
	}
		
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String groupID = ServletUtil.getParamStringDefault(req, "groupID","");
		
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
		
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sSearchType", sSearchType);
		searchMap.put("sSearchText", sSearchText);		
		
		//총카운트 		
		int iTotalCnt = 0;
		
		if(groupID.equals(""))
		{
			iTotalCnt = boardService.getBoardTotalCount(searchMap);
		}
		else
		{
			iTotalCnt = boardService.getBoardTotalCount_user(searchMap,groupID);
		}
		req.setAttribute("iTotalCnt", Integer.toString(iTotalCnt));
		
		if(groupID.equals(""))
		{
			//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드
			return new ModelAndView("/admin/board/notice_board_proc.jsp?mode=list","boardList", boardService.listBoard(curPage, iLineCnt, searchMap));
		}else{
			return new ModelAndView("/admin/board/notice_board_proc.jsp?mode=list","boardList", boardService.listBoard_user(curPage, iLineCnt, searchMap, groupID ));
		}
		
	}
	

	
	/**
	 * <p> 공지사항을 입력창을 출력한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	/*
	public ModelAndView write(HttpServletRequest req, HttpServletResponse res) throws Exception{		

		//페이징세팅 
		ServletUtil.pageParamSetting(req);
		//메뉴아이디세팅
		ServletUtil.meunParamSetting(req);
		
		return new ModelAndView("/admin/board/board_write.jsp");
		
	}
	*/

	/**
	 * <p>공지사항를 입력한다.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	//@SuppressWarnings("unchecked")	
	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//첨부파일이 있을수 있므로  아래와 같이 request를 처리한다.
		//MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;

		String userID = ServletUtil.getParamString(req, "eUserID");
		String title = req.getParameter("eTitle");
		String content = req.getParameter("eContent");
		int priorNum = Integer.parseInt(req.getParameter("ePriorNum"));
		String uploadKey = req.getParameter("eBoardUploadKey");
		String readAuth = req.getParameter("eReadAuth");

		/*
		String[] params = {title,content,userID};	
		if(!ServletUtil.checkRequireParamString(params)){	
			//필수파라마티 에러일 경우에 더이상 진행하지 않고 에러로그를 보여주고 빠져나온다. 
			ServletUtil.messageGoURL(res,"[파라미터 오류] 필수입력값이 부족합니다.","");
			return ;
		}
		*/
		
		
		Board board = new Board();
		
		board.setUserID(userID);
		board.setTitle(title);
		board.setContent(content);
		board.setPriorNum(priorNum);
		board.setFileName("");
		board.setUpload_key(uploadKey);
		board.setReadAuth(readAuth);
		
		

		/*
		//첨부파일 처리 로직 
		//첨부파일 경로는 action-servlet.xml에 기술되어 있다. 
		Iterator fileNameIterator = mreq.getFileNames();		
		while(fileNameIterator.hasNext()){
			MultipartFile multiFile = mreq.getFile((String)fileNameIterator.next());			
			if(multiFile.getSize()>0){
				board.setFileName(FileUploadUtil.uploadFile(multiFile, realUploadPath));
			}
		}
		*/
	
		int resultVal = boardService.insertBoard(board);	
	
		if(resultVal > 0){
			//ServletUtil.messageGoURL(res,"","/ezmail/admin/board/board.do?method=list&mainMenuID="+mainMenuID+"&subMenuID="+subMenuID);
			//req.setAttribute("curPage", 1);
			this.sCurPage = "1"; 
			return list(req,res);
		}
		
		ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
		
		return null;		
		
	}
	
	
	/**
	 * <p>공지사항을 수정한다. 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{		
				
		String userID = ServletUtil.getParamStringDefault(req, "eUserID", "admin");
		String title = ServletUtil.getParamString(req,"eTitle");
		String content = ServletUtil.getParamString(req,"eContent");	
		int priorNum = ServletUtil.getParamInt( req, "ePriorNum", "0");
		int boardID = ServletUtil.getParamInt(req,"eBoardID","0");
		String uploadKey = ServletUtil.getParamString(req,"eBoardUploadKey");	
		String readAuth = req.getParameter("eReadAuth");
	    
		
		Board board = new Board();		
		board.setBoardID(boardID);
		board.setUserID(userID);
		board.setTitle(title);
		board.setContent(content);
		board.setPriorNum(priorNum);
		board.setUpload_key(uploadKey);
		board.setReadAuth(readAuth);
		
		//bind(req,board);  
	
		int resultVal = boardService.updateBoard(board);
		if(resultVal>0){
		
			return list(req,res);
			//ServletUtil.messageGoURL(res,"","/ezmail/admin/board/board.do?method=view&boardID="+boardID+"&currentPage="+currentPage+"&countPerPage="+countPerPage+"&mainMenuID="+mainMenuID+"&subMenuID="+subMenuID);
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패 하였습니다.","");
		}
		
		return null;
	
	}
	
	
	/**
	 * <p>공지사항을 삭제한다.
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) throws Exception{	
		
		
		//int currentPage = ServletUtil.getParamInt(req,"currentPage","1");		 							
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");				
		
		//String mainMenuID = ServletUtil.getParamString(req,"mainMenuID");
		//String subMenuID = ServletUtil.getParamString(req,"subMenuID");
		
		String[] board_ids =  req.getParameterValues("eBoardID");		
	
		if(board_ids==null || board_ids.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		/*
		 *레코드를 삭제하기전에 첨부파일이 있다면 첨부파일을 먼저 삭제한다.
		 *단, 여러개의 공지사항을 삭제할 경우 관련된 첨부파일을 모두 삭제하고 나서 아래처럼 레코드를 삭제할 때 하나라도 에러가 생기면 레코드는 롤백이 된다.
		 *하지만, 이미 첨부파일은 이미  삭제가 된 경우가 된다. 쩝..
		 *그 이유는 체크된 boardID로 첨부파일명을 조회해서 삭제가 되야 하므로 파일이 먼저삭제되고
		 *그다음 레코드가 삭제하는 방식으로 처리했기 때문임.. (어차피 사용자가 지울려구 하는 것이므로 파일은 삭제되도 무방하다고 판단함- 김유근 작성)
		 */
		//deleteBoadFile(board_ids);
		int resultVal = boardService.deleteBoard(board_ids);

	
		if(resultVal>0){
		
			return list(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		
		return null;
		
		
	}
	
	/**
	 * <p>공지사항을 보여준다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView view(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		//페이징세팅 
		ServletUtil.pageParamSetting(req);
		//메뉴아이디세팅
		ServletUtil.meunParamSetting(req);
		int boardID = ServletUtil.getParamInt(req,"boardID","0");		
		//공지사항의 읽은 카운트 증가 
		boardService.updateBoardHit(boardID);
		return new ModelAndView("/pages/main/show_notice_proc.jsp?mode=view","board", boardService.viewBoard(boardID));
	}
	
	/**
	 * <p>공지사항 수정창으로 이동한다.
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
		
		int boardID = ServletUtil.getParamInt(req,"boardID","0");
		
		return new ModelAndView("/admin/board/notice_board_proc.jsp?mode=edit","board", boardService.viewBoard(boardID));
		
	}


	/**
	 * <p>관련된 첨부파일을 삭제처리함.(boardID로 조회한후에 fileName으로 삭제함)
	 * @param board_ids
	 */
	/*
	private void deleteBoadFile(String[] board_ids){
		Board board = null;
		for(int i=0;i<board_ids.length;i++){
			board = boardService.viewBoard(Integer.parseInt(board_ids[i]));
			if(board.getFileName()!=null && !board.getFileName().equals("")){
				FileUploadUtil.deleteFile(board.getFileName(), realUploadPath);
			}
		}
	
	}
	*/

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
				if(this.boardService.insertFileUpload(fileUpload) < 0) {
					ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
					return;
				}
			}
		}
		
		
	}
	
	/* 파일 정보를 보이게한다. */
	public ModelAndView getFileInfo(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		String uploadKey = ServletUtil.getParamStringDefault(req, "uploadKey", "");
		String boardID = ServletUtil.getParamString(req, "boardID");
		
		if(!boardID.equals("0") && uploadKey.equals("")) {
			Board board = boardService.viewBoard( Integer.parseInt(boardID) );
			if(board != null) uploadKey = board.getUpload_key();
		}
		
		FileUpload fileUpload= boardService.getFileInfo(uploadKey);
		if(fileUpload != null) {
			
			if(!FileUploadUtil.existFile( fileUpload.getNewFileName(), this.realUploadPath) ) {
				return null;
			}
			
		}

		return new ModelAndView("/admin/board/notice_board_proc.jsp?mode=getFileInfo","fileUpload", boardService.getFileInfo(uploadKey));
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
		
		
		fileUpload = boardService.getFileInfo(uploadKey);
	
		if(fileUpload != null) {
			FileUploadUtil.downloadFile(req, res, this.realUploadPath, fileUpload.getNewFileName(), fileUpload.getRealFileName());
		}
	}

	
}
