package web.target.targetlist.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import web.common.util.QueryUtil;
import web.common.util.DBUtil;
import web.common.util.StringUtil;
import web.common.util.TmEncryptionUtil;

import web.target.targetlist.service.TargetListService;
import web.target.targetlist.model.*;


public class TargetingPreviewController {
	
	private static TargetingPreviewController instance = null;
	private Logger logger = Logger.getLogger(this.getClass()); 
	private static TargetListService targetListService = null;
		
	
	private TargetingPreviewController(TargetListService targeListService)
	{
		targetListService = targeListService;		
	}
	public static synchronized TargetingPreviewController getInstance(TargetListService targeListService)
	{
		if (instance == null) 
		   	instance = new TargetingPreviewController(targeListService);           	

		return instance;
	}

	
	@SuppressWarnings("null")
	public List<String[]> getTargetPreviewList(int targetID, String queryText, String dbID, String sSearchType, String sSearchText, int currentPage, int iLineCnt, int total) throws Exception
	{
		List<String[]> list = new ArrayList<String[]>();		
		String query = "select tmp.* from ( "+ queryText + " ) tmp where 1=1 ";  
		int start_count = iLineCnt * (currentPage - 1);	//불러올 리스트 중 시작라인 번호	
					
		PreparedStatement ps = null;
		ResultSet rs = null;		
		Connection con = getConnectionDBInfo(dbID);
		if(con ==  null )
			throw new Exception("Connection is null");
				
		//원투원 필드 정보를 가져온다
		List<OnetooneTarget> onetoone = targetListService.viewOnetooneTarget(targetID);
		String[] onetoFieldName = new String[onetoone.size()];
		String[] onetoFieldDesc = new String[onetoone.size()];		
		
		for(int i = 0; i < onetoone.size(); i++)
		{			
			OnetooneTarget ot = onetoone.get(i);
			onetoFieldName[i] = ot.getFieldName();	
			onetoFieldDesc[i] = ot.getFieldDesc();	
		}
		
		//검색 조건이 있을 때
		if(sSearchText!=null && !sSearchText.equals(""))
		{			
			query = query + "AND " + sSearchType + " LIKE '%" + sSearchText + "%' "; 
		}
		Map<String, Object> mapdb = getDb(dbID);
		String dbType= null;
		String pagingYN= null;
		if(mapdb!=null || mapdb.size()!=0)
		{
			dbType = String.valueOf(mapdb.get("driverType"));
			pagingYN = String.valueOf(mapdb.get("pagingYN"));
		}
		
		
		if(pagingYN.equals("Y"))
		{
			int[] startLimitCount = new int[2];
			startLimitCount=QueryUtil.getCountByDB(dbType, start_count, iLineCnt);
			query = QueryUtil.getPagingQueryByDB(dbType, query, onetoFieldName[0], String.valueOf(startLimitCount[0]), String.valueOf(startLimitCount[1]));
		}
		
		try
		{
			//해당DB에서 리스트 가져와서 원투원 필드에 넣는다
			ps = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ps.setFetchSize(iLineCnt*1000);  
			rs = ps.executeQuery();	
			
			if(pagingYN.equals("N")) //페이징이 안 된 DB일 떄
			{        
				int curPoint = iLineCnt * (currentPage - 1);

	            for (int i = 0; i < curPoint; i++)
	            {
	            	rs.next();
	            }
			}	  
			
			for (int j = 0; rs.next() && j < iLineCnt; j++)
			{
				String oneToOneInfo[] = new String[onetoFieldName.length];
				for(int i = 0; i<onetoFieldName.length; i++)
					oneToOneInfo[i] = nullCheck(rs.getString(onetoFieldName[i]));
				
				list.add(oneToOneInfo);
			}

		}catch(Exception e){
			logger.error( e );			
		}finally{
			try{rs.close();}catch(Exception e){}		
			try{ps.close();}catch(Exception e){}
			try{con.close();}catch(Exception e){}	
			
		}		
		
		return list;
		
	}
	
		
	public int getTargetPreviewListTotalCount(String queryText, String dbID, String sSearchType, String sSearchText)throws Exception 
	{
		int count = 0;
		String sql = "select * from ( " + queryText + " ) tmp where 1=1 ";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = getConnectionDBInfo(dbID);
		
		if(con ==  null )
			throw new Exception("Connection is null");		
						
		if(sSearchText!=null && !sSearchText.equals(""))
		{			
			sql = sql + "AND " + sSearchType + " LIKE '%" + sSearchText + "%' ";					
				 
		}
		sql = QueryUtil.makeCountQuery(sql);
		try
		{	
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();	
			
			if(rs.next())
				count= rs.getInt(1);		
			
		}catch(Exception e){
			logger.error( e );			
			
		}finally{
			try{rs.close();}catch(Exception e){}		
			try{ps.close();}catch(Exception e){}	
			try{con.close();}catch(Exception e){}	
			
		}
		
		return count;
		
	}
	
	
	/**
	 * <p>dbID에 해당하는 접속정보를 가져오고 접속한다. 
	 * @param dbID
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("null")
	private synchronized Connection getConnectionDBInfo(String dbID) throws Exception{
		Connection conn = null;
		
		Map<String, Object> mapdb = getDb(dbID);
		
		if(mapdb!=null || mapdb.size()!=0){
			String driverClass = String.valueOf(mapdb.get("driverClass"));
			
			String sKey = StringUtil.createSecurityKey("TM", dbID, driverClass);
			
			String url = TmEncryptionUtil.decrypto(String.valueOf(mapdb.get("dbURL")), sKey);
			String user = TmEncryptionUtil.decrypto(String.valueOf(mapdb.get("dbUserID")), sKey);
			String password = TmEncryptionUtil.decrypto(String.valueOf(mapdb.get("dbUserPWD")), sKey);
					
			conn = DBUtil.getConnection(driverClass, url, user, password);
		}
		
		return conn;
	}
	
	/**
	 * <p>dbID에 해당하는 디비 정보를 가져온다. 
	 * @param dbID
	 * @return
	 */
	private synchronized Map<String, Object> getDb(String dbID){
			
		Map<String, Object> mapdb = targetListService.getDBInfo(dbID);
		
		return mapdb;
	}
	
	
	
	public synchronized String nullCheck(String str)
	{
		if (str == null || str.equals("null"))
		{
			str = "";
		}
		return str;
	}
	
	

}
