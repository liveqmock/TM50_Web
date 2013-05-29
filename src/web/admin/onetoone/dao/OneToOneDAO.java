package web.admin.onetoone.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import web.admin.onetoone.model.OneToOne;

public interface OneToOneDAO {

	/**
	 * <p>원투원정보를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */	
	public List<OneToOne> listOneToOne() throws DataAccessException;	
	
	
	
	/**
	 * <p>targetID에 해당되는 ez_onetoone_target 리스트를 가져온다.
	 * @param targetID
	 * @return
	 * @throws DataAccessException
	 */
	public List<OneToOne> listOneToOneByTargetID(String[] targetIDs) throws DataAccessException;	
}
