package web.content.tester.dao;


import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import web.content.tester.model.*;



public interface TesterDAO {

	/**
	 * <p>테스트리스트 
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */
	public List<Tester> listTester(Map<String, String> searchMap) throws DataAccessException;
	
	
	
	/**
	 * <p>테스트 내용보기 
	 * @param email
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public Tester viewTester(int testerID) throws DataAccessException;
	
	
	
	/**
	 * <p>입력 
	 * @param tester
	 * @return
	 * @throws DataAccessException
	 */
	public int insertTester(Tester tester) throws DataAccessException;
	
	
	/**
	 * <p>수정
	 * @param tester
	 * @return
	 * @throws DataAccessException
	 */
	public int updateTester(Tester tester) throws DataAccessException;
	
	
	
	/**
	 * <p>삭제
	 * @param tester
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteTester(String[] testerIDs) throws DataAccessException;
	
	
	
	/**
	 * <p>유저당 30건만 등록가능 
	 * @param userID
	 * @return
	 * @throws DataAccessException
	 */
	public int checkTesterByUserID(String userID) throws DataAccessException;
	
	public int getTesterTotalCount(Map<String, String> searchMap) throws DataAccessException;
	
	
}
