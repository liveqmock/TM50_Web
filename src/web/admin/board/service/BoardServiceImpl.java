/**
 * <p>게시판에 등록/수정/삭제/조회등을 수행한다. 
 * @author coolang
 * @date 2007-10-10
 */


package web.admin.board.service;




import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import web.admin.board.model.Board;
import web.admin.board.dao.BoardDAO;
import web.common.model.FileUpload;

public class BoardServiceImpl  implements BoardService{
	

	private BoardDAO boardDAO = null;
	private Logger logger = Logger.getLogger(this.getClass());

	
	
    public void setBoardDAO(BoardDAO boardDAO) {
    	
        this.boardDAO = boardDAO;         
    }
    
    
	/**
	 * <p>공지사항을 저장한다.
	 * @param board
	 * @return
	 */
	public int insertBoard(Board board){
		
		int result = 0;
		try{
			result = boardDAO.insertBoard(board);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>공지사항을 수정한다.
	 */
	public int updateBoard(Board board){
		 
		int result = 0;
		try{
			result = boardDAO.updateBoard(board);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>공지사항의 읽은 카운트 증가 
	 */
	public int updateBoardHit(int boardID){
		 
		int result = 0;
		try{
			result = boardDAO.updateBoardHit(boardID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>공지사항을 삭제한다.
	 */
	public int deleteBoard(String[] board_ids){
		 
		int result = 0;		
		try{
			for(int i=0;i<board_ids.length;i++){								
					result = boardDAO.deleteBoard(Integer.parseInt(board_ids[i]));
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p> 공지사항 리스트를 보여준다.
	 */
	@SuppressWarnings("unchecked")
	public List listBoard(int currentPage, int countPerPage, Map searchMap){			
		List resultList = null;		
		try{
			resultList =  boardDAO.listBoard(currentPage, countPerPage, searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	/**
	 * <p> 공지사항 리스트를 보여준다.
	 */
	@SuppressWarnings("unchecked")
	public List listBoard_user(int currentPage, int countPerPage, Map searchMap, String groupID){			
		List resultList = null;		
		try{ 
			resultList =  boardDAO.listBoard_user(currentPage, countPerPage, searchMap, groupID); 
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	/**
	 * <p>공지사항을 보여준다.
	 */
	public Board viewBoard(int boardID){
		Board board = null;
		try{
			board = boardDAO.viewBoard(boardID);
			
		}catch(Exception e){
			logger.error(e);
		}
		return board;

	}
	
	/**
	 * <p>공지사항의 전체카운트를 가져온다.
	 */
	@SuppressWarnings("unchecked")
	public int getBoardTotalCount(Map searchMap){
		int result = 0;
		try{
			result = boardDAO.getBoardTotalCount(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>공지사항의 전체카운트를 가져온다.
	 */
	@SuppressWarnings("unchecked")
	public int getBoardTotalCount_user(Map searchMap,String groupID){
		int result = 0;
		try{
			result = boardDAO.getBoardTotalCount_user(searchMap, groupID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>파일을 저장한다.
	 * @param fileUpload
	 * @return
	 */
	public int insertFileUpload(FileUpload fileUpload){
		
		int result = 0;
		try{
			result = boardDAO.insertFileUpload(fileUpload);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>업로드키로 파일 정보를 가져온다 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */	
	public FileUpload getFileInfo(String uploadKey){
		FileUpload resultList = null;		
		try{
			resultList =  boardDAO.getFileInfo(uploadKey);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	
	/**
	 * <p> 공지사항 리스트를 보여준다.
	 */
	@SuppressWarnings("unchecked")
	public List listShow(int groupID){			
		List resultList = null;		
		try{
			resultList =  boardDAO.listShow(groupID);
		}catch(Exception e){
			logger.error(e);
		}
		return resultList;
	}
	
	
}
