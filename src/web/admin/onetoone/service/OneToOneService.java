package web.admin.onetoone.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import web.admin.onetoone.model.OneToOne;

public interface OneToOneService {
	
	
	/**
	 * <p>원투원정보를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */	
	public List<OneToOne> listOneToOne();
	
	
	
	/**
	 * <p>targetID에 해당되는 ez_onetoone_target 리스트를 가져온다.
	 * @param targetID
	 * @return	 
	 */
	public List<OneToOne> listOneToOneByTargetID(String[] targetIDs);


}
