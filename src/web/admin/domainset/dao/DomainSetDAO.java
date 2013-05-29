package web.admin.domainset.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import web.admin.domainset.model.DomainSet;

public interface DomainSetDAO {

	/**
	 * <p>도메인 설정 사항을 불러 온다 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<DomainSet> listDomainSet(String domainFlag);
	
	/**
	 * <p>도메인 설정 사항을 저장한다.
	 * @param board
	 * @return
	 */
	public int insertDomainSet(DomainSet domainSet) throws DataAccessException;
	
	
	/**
	 * <p>도메인 설정 사항을 삭제한다.
	 * @param board_id
	 * @return
	 * @throws DataAccessException
	 */
	public int deleteDomainSet(int DomainSetID) throws DataAccessException;
	
	
	/**
	 * <p>도메인 설정 사항을 수정한다.
	 * @param board
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDomainSet(DomainSet domainSet)  throws DataAccessException;
	

	/**
	 * <p>도메인내용표시 
	 * @param senderID
	 * @return
	 * @throws DataAccessException
	 */
	public DomainSet viewDomainSet(int domainID)  throws DataAccessException;
	
	public int selectDomain(DomainSet domainSet) throws DataAccessException;
	
	public DomainSet viewDomainNameSet(String domainName) throws DataAccessException;
}
