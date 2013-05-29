package web.admin.onetoone.service;

import java.util.List;
import org.apache.log4j.Logger;
import web.admin.onetoone.model.OneToOne;
import web.admin.onetoone.dao.OneToOneDAO;

public class OneToOneServiceImpl implements OneToOneService{
	
	private Logger logger = Logger.getLogger("TM");
	private OneToOneDAO oneToOneDAO = null;
	
	public void setOneToOneDAO(OneToOneDAO oneToOneDAO){
		this.oneToOneDAO = oneToOneDAO;
	}
	
	/**
	 * <p>원투원정보를 불러온다.
	 * @return
	 * @throws DataAccessException
	 */	
	public List<OneToOne> listOneToOne(){
		List<OneToOne> result = null;
		try{
			result = oneToOneDAO.listOneToOne();
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}	
	
	
	/**
	 * <p>targetID에 해당되는 ez_onetoone_target 리스트를 가져온다.
	 * @param targetID
	 * @return	 
	 */
	public List<OneToOne> listOneToOneByTargetID(String[] targetIDs){
		List<OneToOne> result = null;
		try{
			result = oneToOneDAO.listOneToOneByTargetID(targetIDs);
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
}
