package web.admin.targetmanager.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import web.admin.dbjdbcset.model.DbJdbcSet;
import web.admin.targetmanager.model.OneToOne;
import web.admin.targetmanager.model.TargetUIManager;
import web.admin.targetmanager.model.TargetUIManagerSelect;
import web.admin.targetmanager.model.TargetUIManagerWhere;

public interface TargetManagerService {
	
	@SuppressWarnings("unchecked")
	public List list(Map<String, Object> searchMap);
	
	public int getCount(Map<String, Object> searchMap);
	
	public int getMaxID();
	
	public int insertTargetManagerInfo(TargetUIManager targetManger);
	
	public int updateTargetManagerInfo(TargetUIManager targetManger);
	
	public TargetUIManager view(int targetUIManagerID);
	
	public Map<String, Object> getDBInfo(String dbID);
	
	public List<OneToOne> listOneToOne();
	
	public List<TargetUIManagerSelect> getTargetUIManagerSelect(int targetUIManagerID);
	
	public List<TargetUIManagerWhere> getTargetUIManagerWhere(int targetUIManagerID);
	
	public List<DbJdbcSet> getDBList();
	
	public void insertTargetManagerSelect(List<TargetUIManagerSelect> targetUIManagerSelectList);
	
	public void insertTargetManagerWhere(List<TargetUIManagerWhere> targetUIManagerWhereList);
	
	public int deleteTargetManagerInfo(int targetUIManagerID); 
	
	public int deleteTargetManagerSelect(int targetUIManagerID);
	
	public int deleteTargetManagerWhere(int targetUIManagerID);
	
	public int createGeneralTable(int targetUIManagerID);
	
	public int updateTargetUseNo(int targetUIManagerID);
	
	
	

}
