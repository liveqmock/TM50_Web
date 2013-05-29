
/**
 * <p>게시판에 등록/수정/삭제/조회등을 수행하는 인터페이스 
 * @author coolang
 * @date 2007-10-10
 */

package web.admin.board.service;



import java.util.List;
import java.util.Map;
import web.admin.board.model.Board;
import web.common.model.FileUpload;


public interface BoardService {
	
	
	/**
	 * <p>공지사항을 저장한다.
	 * @param board
	 * @return
	 */
	public int insertBoard(Board board);
	
	
	/**
	 * <p>공지사항을 삭제한다.
	 * @param board_id
	 * @return
	 */
	public int deleteBoard(String[] board_ids);
	
	/**
	 * <p>공지사항 리스트를 보여준다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 */	
	 @SuppressWarnings("unchecked")	
	public List listBoard(int currentPage, int countPerPage,Map searchMap);
	
	 /**
		 * <p>공지사항 리스트를 보여준다.
		 * @param currentPage
		 * @param countPerPage
		 * @return
		 */	
		 @SuppressWarnings("unchecked")	
	 public List listBoard_user(int currentPage, int countPerPage, Map searchMap, String groupID);
	/**
	 * <p> 공지사항을 보여준다.
	 * @param boardID
	 * @return
	 */
	public Board viewBoard(int boardID);
	
	
	/**
	 * <p>공지사항을 수정한다. 
	 * @param board
	 * @return
	 */
	public int updateBoard(Board board);
	
	
	/**
	 * <p>공지사항의 읽은 카운트 증가 
	 * @param boardID
	 * @return
	 */
	public int updateBoardHit(int boardID) ;
	
	
	/**
	 * <p>공지사항의 총 레코드수를 가져온다.
	 * @return
	 */
	@SuppressWarnings("unchecked")	
	public int getBoardTotalCount(Map searchMap);
	
	
	/**
	 * <p>공지사항의 전체카운트를 가져온다.
	 */
	@SuppressWarnings("unchecked")
	public int getBoardTotalCount_user(Map searchMap,String groupID);
	/**
	 * <p>파일을 저장한다.
	 * @param fileUpload
	 * @return
	 */
	public int insertFileUpload(FileUpload fileUpload);
	
	
	/**
	 * <p>업로드키로 파일 정보를 가져온다 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 */	
	public FileUpload getFileInfo(String uploadKey);
	

	@SuppressWarnings("unchecked")
	public List listShow(int groupID);
	
	
}
