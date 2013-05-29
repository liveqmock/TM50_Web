package web.common.service;

import org.apache.log4j.Logger;
import web.common.dao.CommonAccessDAO;




public class CommonAccessServiceImpl implements CommonAccessService {
	
	private static Logger logger = Logger.getLogger("TM");
	private CommonAccessDAO commonAccessDAO = null; 
	
	
	
	 public void setCommonAccessDAO(CommonAccessDAO commonAccessDAO) {
	    	
	        this.commonAccessDAO = commonAccessDAO;         
	    }
	
	public int getInt(String query)
	{
		int result = 0;
		try
		{
			result = commonAccessDAO.getInt(query);
		}
		catch(Exception e)
		{
			logger.error(e);
			
		}
		return result;
	}
	
	/**
	 * 발송중인 메일이 있는지 체크, 있으면 true, 없으면 false
	 * 발송중 : 실발송중(상태값 14), 오류자 재발송중(상태값 16)
	 */
	public boolean isSendMassMail() 
	{
		boolean	isSend = false;
		
		try
		{		
			int a = commonAccessDAO.isSendMassMail();
			if(a>0)
				isSend = true;
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return isSend;		
	}
	

}
