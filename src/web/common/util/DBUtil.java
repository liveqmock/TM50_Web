package web.common.util;


/**
 * <p>데이타베이스 관련된 유틸이다. 
 * @author coolang (김유근)
 *
 */

import java.sql.*;
import web.common.dao.DataSourceManager;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;


public class DBUtil {
	
	
	/**
	 * <p>주어진 driver를 가지고 연결 컨넥션을 얻는다. 
	 * @param driver
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 */
	private static Logger logger = Logger.getLogger("common"); //log4j.xml에서  읽어들인다. 
	
	public static Connection getConnection(String driver, String url, String user, String password ){
		Connection conn  = null;		
		String[] params = new String[4];
		params[0] = driver;
		params[1] = url;
		params[2] = user;
		params[3] = password;		
		DataSourceManager dsm = new DataSourceManager();
		BasicDataSource bds = dsm.getDataSource(params);
		try{			
			conn = bds.getConnection();			
		}catch(SQLException e){
			conn = null;
		}
		return conn;		
	}
	

	
	/**
	 * <p>주어진 driver를 가지고 컨넥션이 얻어지는지 체크한다.
	 * @param driver
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 */
	public static boolean  checkConnection(String driver, String url, String user, String password ){
		boolean connected = false;
		Connection conn  = null;				
		String[] params = new String[4];
		params[0] = driver;
		params[1] = url;
		params[2] = user;
		params[3] = password;
		DataSourceManager dsm = new DataSourceManager();
		BasicDataSource bds = dsm.getDataSource(params);
		try{		
			conn = bds.getConnection();					
		}catch(SQLException e){
			logger.error(e);
			conn = null;
		}
		if(conn!=null){
			connected = true;
			try{ conn.close();}catch(SQLException e){}  //컨넥션이 연결되었다는 것을 알았기때문에 컨넥션을 닫아준다. 
		}	
		return connected;
	
	}
	
	
}


