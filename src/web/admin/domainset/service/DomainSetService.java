package web.admin.domainset.service;

import java.util.List;
import org.springframework.dao.DataAccessException;
import web.admin.domainset.model.DomainSet;

public interface DomainSetService {
	
	/**
	 * <p>도메인 설정 사항을 불러 온다 
	 * @param currentPage
	 * @param countPerPage
	 * @param searchMap
	 * @return
	 * @throws DataAccessException
	 */	
	public List<DomainSet> listDomainSet(String domainFlag) throws DataAccessException;
	
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
	public int deleteDomainSet(String[] domainSetIDs) throws DataAccessException;
	
	
	/**
	 * <p>도메인 설정 사항을 수정한다.
	 * @param board
	 * @return
	 * @throws DataAccessException
	 */
	public int updateDomainSet(DomainSet domainSet)  throws DataAccessException;
	

	/**
	 * <p>도메인을 표시 
	 * @param domainID
	 * @return
	 * @throws DataAccessException
	 */
	public DomainSet viewDomain(int domainID) throws DataAccessException;
	
	
	public DomainSet viewDomainName(String domainName) throws DataAccessException;
}
