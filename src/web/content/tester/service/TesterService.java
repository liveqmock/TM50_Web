package web.content.tester.service;

import java.util.List;
import java.util.Map;



import web.content.tester.model.Tester;

public interface TesterService {

	/**
	 * <p>테스트리스트 
	 * @param searchMap
	 * @return
	 */
	public List<Tester> listTester(Map<String, String> searchMap);
	
	
	/**
	 * <p>테스트 내용보기 
	 * @param email
	 * @param userID
	 * @return
	 */
	public Tester viewTester(int testerID);
	
	
	
	/**
	 * <p>입력 
	 * @param tester
	 * @return
	 */
	public int insertTester(Tester tester);
	
	
	/**
	 * <p>수정
	 * @param tester
	 * @return
	 */
	public int updateTester(Tester tester);
	
	
	
	/**
	 * <p>삭제
	 * @param tester
	 * @return
	 */
	public int deleteTester(String[] testerIDs);
	
	
	/**
	 * <p>유저당 30건만 등록가능 
	 * @param userID
	 * @return
	 */
	public int checkTesterByUserID(String userID);
	
	@SuppressWarnings("unchecked")
	public int getTesterTotalCount(Map searchMap);
	
}
