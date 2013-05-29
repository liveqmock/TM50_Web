package web.admin.menu.dao;

import java.util.List;
import web.admin.menu.model.*;
import web.admin.usergroup.model.AuthMaster;
import web.admin.usergroup.model.DbSet;

import org.springframework.dao.DataAccessException;

public interface MenuDAO {

	/**
	 * <p>메인메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MenuMain> listMenuMain() throws DataAccessException;
	
	
	/**
	 * <p>메인메뉴에 해당하는 서브메뉴리스트를 읽어들인다. (메인메뉴가 "" 이면 전체를 불러온다)
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MenuSub> listMenuSub(String menuMainID) throws DataAccessException;
	
	/**
	 * <p>권한에 해당하는 서브메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 */	
	public List<MenuSub> listMenuSubAuth(String userID) throws DataAccessException;
	
	
	
	/**
	 * <p>권한 마스터 불러오기
	 */
	public List<AuthMaster> listAuthMaster(String menuSubID) throws DataAccessException;
	
	/**
	 * <p>db 정보 출력 
	 * @return
	 * @throws DataAccessException
	 */
	public List<DbSet> selectSetDBList() throws DataAccessException;
	
}
