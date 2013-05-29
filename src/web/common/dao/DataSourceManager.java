package web.common.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;



/**
 * <p>데이타베이스 세팅 클래스 (engine.properties)
 * @author coolang
 *
 */
public class DataSourceManager {
	private BasicDataSource dataSource = null;
	private DataSourceManager instance = null;
	private Logger logger = Logger.getLogger("common"); //log4j.xml에서  읽어들인다. 
	
	public DataSourceManager getInstance(String[] params) {
		if (instance == null) {
        	instance = new DataSourceManager();        	
        	dataSource = new BasicDataSource();
        	setDataSource(dataSource,params);
		}
        	
		 return instance;
	}
	
	private void setDataSource(BasicDataSource dataSource,String[] params){
		try{
			dataSource.setDriverClassName(params[0]);
			dataSource.setUrl(params[1]);
			dataSource.setUsername(params[2]);
			dataSource.setPassword(params[3]);
			
			
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	public BasicDataSource getDataSource(String[] params) {
		
		if (instance == null) {
			instance = getInstance(params);
		}
		
		return dataSource;
	}
	
	
	
}
