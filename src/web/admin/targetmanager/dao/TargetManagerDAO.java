package web.admin.targetmanager.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.dbjdbcset.model.DbJdbcSet;
import web.admin.targetmanager.model.TargetUIManager;
import web.admin.targetmanager.model.TargetUIManagerSelect;
import web.admin.targetmanager.model.OneToOne;
import web.admin.targetmanager.model.TargetUIManagerWhere;

public interface TargetManagerDAO {
	
	public List<TargetUIManager> list(Map<String, Object> searchMap) throws DataAccessException;
	
	public int getCount(Map<String, Object> searchMap) throws DataAccessException;
	
	public int getMaxID() throws DataAccessException;
	
	public int insertTargetManagerInfo(TargetUIManager targetManager)  throws DataAccessException;
	
	public int updateTargetManagerInfo(TargetUIManager targetManager)  throws DataAccessException;
	
	public TargetUIManager view(int targetUIManagerID)  throws DataAccessException;
	
	public int insertTargetManagerSelect(TargetUIManagerSelect targetManagerOnetoone)  throws DataAccessException;
	
	public int insertTargetManagerWhere(TargetUIManagerWhere targetUIManagerWhere)  throws DataAccessException;
		
	public Map<String, Object> getDBInfo(String dbID)  throws DataAccessException;
	
	public List<OneToOne> listOneToOne() throws DataAccessException;
	
	public List<TargetUIManagerSelect> getTargetUIManagerSelect(int targetUIManagerID) throws DataAccessException;
	
	public List<TargetUIManagerWhere> getTargetUIManagerWhere(int targetUIManagerID) throws DataAccessException;
	
	public List<DbJdbcSet> getDBList() throws DataAccessException;
	
	public int deleteTargetManagerInfo(int targetUIManagerID) throws DataAccessException; 
	
	public int deleteTargetManagerSelect(int targetUIManagerID) throws DataAccessException;
	
	public int deleteTargetManagerWhere(int targetUIManagerID) throws DataAccessException ;
	
	public int createGeneralTable(int targetUIManagerID) throws DataAccessException;
	
	public int updateTargetUseNo(int targetUIManagerID)  throws DataAccessException;
	
	public int updateTargetManagerDefault(int targetUIManagerID) throws DataAccessException ;
	
	

}
