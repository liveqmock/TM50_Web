package web.content.tester.service;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import web.content.tester.model.Tester;
import web.content.tester.dao.TesterDAO;

public class TesterServiceImpl implements TesterService{
	
	private Logger logger = Logger.getLogger("TM");
	private TesterDAO testerDAO = null;
	
	public void setTesterDAO(TesterDAO testerDAO){
		this.testerDAO = testerDAO;
	}
	

	/**
	 * <p>테스트리스트 
	 * @param searchMap
	 * @return
	 */
	public List<Tester> listTester(Map<String, String> searchMap){
		List<Tester> result = null;
		try{
			result = testerDAO.listTester(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>테스트 내용보기 
	 * @param email
	 * @param userID
	 * @return
	 */
	public Tester viewTester(int testerID){
		Tester result = null;
		try{
			result = testerDAO.viewTester(testerID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>입력 
	 * @param tester
	 * @return
	 */
	public int insertTester(Tester tester){
		int result = 0;
		try{
			result = testerDAO.insertTester(tester);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	/**
	 * <p>수정
	 * @param tester
	 * @return
	 */
	public int updateTester(Tester tester){
		int result = 0;
		try{
			result = testerDAO.updateTester(tester);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	
	/**
	 * <p>삭제
	 * @param tester
	 * @return
	 */
	public int deleteTester(String[] testerIDs){
		int result = 0;
		try{
			result = testerDAO.deleteTester(testerIDs);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * <p>유저당 30건만 등록가능 
	 * @param userID
	 * @return
	 */
	public int checkTesterByUserID(String userID){
		int result = 0;
		try{
			result = testerDAO.checkTesterByUserID(userID);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public int getTesterTotalCount(Map searchMap){
		int result = 0;
		try{
			result = testerDAO.getTesterTotalCount(searchMap);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
}
