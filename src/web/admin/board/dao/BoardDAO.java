package web.admin.board.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.admin.board.model.*;
import web.common.model.FileUpload;

/**
 * <p>공지사항 인터페이스 
 * @author coolang
 * @date 2007-09-27
 *
 */

public interface BoardDAO {
	
	
	/**
	 * <p>공지사항 리스트를 불러온다.
	 * @param currentPage
	 * @param countPerPage
	 * @return
	 * @throws DataAccessException
	 */	
	public List<Board> listBoard(int currentPage, int countPerPage,Map<String, String> searchMap) throws DataAccessException;
	
	
	/**
	 * <p>공지사항 리스트를 출력한다. 
	 */
	public List<Board> listBoard_user(int currentPage, int countPerPage, Map<String, String> searchMap, String groupID) throws DataAccessException;
	/**
	 * <p>공지사항을 저장한다.
	 * @param board
	 * @return
	 */
	public int insertBoard(Board board) throws DataAccessException;
	
	
	/**
	 * <p>공지사항을 삭제한다.
	 * @param board_id
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteBoard(int boardID) throws DataAccessException;
	
	
	/**
	 * <p>공지사항을 보여준다.
	 * @param board_id
	 * @return
	 * @throws DataAccessException
	 */
	public Board viewBoard(int boardID)  throws DataAccessException;
	
	
	/**
	 * <p>공지사항을 수정한다.
	 * @param board
	 * @return
	 * @throws DataAccessException
	 */
	public int updateBoard(Board board)  throws DataAccessException;
	
	
	/**
	 * <p>공지사항의 총카운트를 구해온다.
	 * @return
	 * @throws DataAccessException
	 */
	public int getBoardTotalCount(Map<String, String> searchMap)  throws DataAccessException;
	
	/**
	 * <p>공지사항을 총 갯수를 가져온다. 
	 */	
	public int getBoardTotalCount_user(Map<String, String> searchMap, String groupID) throws DataAccessException;
	/**
	 * <p>공지사항의 읽은 카운트 증가 
	 * @param boardID
	 * @return
	 * @throws DataAccessException
	 */
	public int updateBoardHit(int boardID)  throws DataAccessException;
	
	
	/**
	 * <p>업로드한 임시 파일을 저장한다.
	 * @param fileUpload
	 * @return
	 */
	public int insertFileUpload(FileUpload fileUpload) throws DataAccessException;	
	
	
	/**
	 * <p>업로드키로 파일정보 불러오기 
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public FileUpload getFileInfo(String uploadKey) throws DataAccessException;
	
	public List<Board> listShow(int groupID) throws DataAccessException;
	
	
	
}
