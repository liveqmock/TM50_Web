package web.admin.menu.service;


import java.util.List;

import web.admin.menu.model.*;


public interface  MenuService {

	/**
	 * <p>메인메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 */
	
	public List<MenuMain>  listMenuMain();
	
	
	/**
	 * <p>메인메뉴에 해당하는 서브메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 *
	 */	
	public List<MenuSub>  listMenuSub(String mainMenuID);
	

	/**
	 * <p>서브메뉴리스트 전체를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 *
	 */	
	public List<MenuSub>  listMenuSub();

	/**
	 * <p>사용자 권한에 해당하는 서브메뉴리스트를 읽어들인다.
	 * @return
	 * @throws DataAccessException
	 *
	 */	
	public List<MenuSub>  listMenuSubAuth( String userID );
	
	
	/**
	 * <p>메뉴 트리의 json 데이타를 불러온다
	 * @return
	 */	
	public String getJsonTreeMenu();
	
	
}
