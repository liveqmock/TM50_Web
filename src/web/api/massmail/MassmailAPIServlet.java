package web.api.massmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import web.common.util.DBUtil;
import web.common.util.PropertiesUtil;
import web.common.util.QueryUtil;
import web.common.util.ServletUtil;
import web.common.util.Constant;
import web.common.util.DateUtils;
import web.target.targetlist.model.TargetList;
import web.target.targetlist.model.OnetooneTarget;
import web.target.targetlist.service.TargetListService;
import web.target.targetlist.control.TargetListControlHelper;
import web.target.targetlist.control.TargetListFileThread;
import web.massmail.write.model.MassMailInfo;
import web.massmail.write.model.MassMailSchedule;
import web.massmail.write.control.MassMailControlHelper;
import web.massmail.write.service.MassMailService;
import web.admin.systemset.control.SystemSetControllerHelper;
import web.admin.systemset.service.SystemSetService;
import web.api.massmail.control.MassMailAPIControlHelper;
import web.api.massmail.service.MassMailAPIService;



@SuppressWarnings("serial")
public class MassmailAPIServlet extends HttpServlet
{
	private static String RESULT_SUCCESS = "-100";  	//데이터 전송 성공
	private static String RESULT_FAIL_PARAM = "10"; 	//필수 파라미터 미입력
	private static String RESULT_FAIL_TARGET = "30";	//대상자 등록 실패
	private static String RESULT_FAIL_MAIL = "40";		//메일 등록 실패
	private static String RESULT_ETC = "90";			//기타
	
	private ServletContext context = null; 
	private PrintWriter out = null;
	Logger logger = Logger.getLogger("TM");
	
	private String writer = null;
	private String mailTitle = null;		
	private String senderEmail = null;
	private String senderName = null;
	private String receiverName = null;
	
	private String contentType = null;
	private String mailContent = null;
	private String template_id = null;
	
	private String targetType = null;
	private String targetQuery = null;
	private String targetDBId = null;
	private String targetString = null;
	private String fileOneToOne_st = null;
	private String sendSchedule = null;
	private String sendType = null;
	private int target_id = -1;
	
	private String massMailGroupID = null;
	
	private String attachPath = null;
	private String attachFileNames = null;
	private String attachFileRealNames = null;
	private String[] attachFileNamesArray = null;
	private String[] attachFileRealNamesArray = null;
	
	private String realUploadPath = null;
	
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		out = res.getWriter();
		context = getServletContext();
		String method = req.getParameter("method");
		
		if(method.equals("massmail"))
		{
			out.println(massmail(req, res));
			
		}
		setInit();
	}
	
	/**
	 * 대량메일 API 파라미터 체크, 대상자 추가, 메일 추가 완료되면 결과 리턴
	 * @param req
	 * @param res
	 * @return String 
	 * @throws IOException
	 * @throws ServletException
	 */
	private String massmail(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		writer= ServletUtil.getParamString(req, "writer");
		mailTitle = ServletUtil.getParamString(req, "mailTitle");		
		senderEmail = ServletUtil.getParamString(req, "senderEmail");
		senderName= ServletUtil.getParamString(req, "senderName");
		receiverName= ServletUtil.getParamString(req, "receiverName");
		
		contentType = ServletUtil.getParamString(req, "contentType");
		
		targetType= ServletUtil.getParamString(req, "targetType");
		fileOneToOne_st= ServletUtil.getParamString(req, "fileOneToOne");
		sendType = ServletUtil.getParamString(req, "sendType");
		
		
		massMailGroupID = ServletUtil.getParamStringDefault(req, "massMailGroupID", "1");
					
		attachPath = ServletUtil.getParamString(req, "attachPath");
		
		realUploadPath = PropertiesUtil.getStringProperties("configure", "massmail_api_attach_path");
		
		
		if(sendType.equals("")) sendType=Constant.SEND_TYPE_NOW;
		
		//check param
		String[] mail_params = {mailTitle, senderEmail, senderName, receiverName, contentType, targetType, writer, fileOneToOne_st, sendType  };
		if(ServletUtil.checkRequireParamString(mail_params))
		{
			if(contentType.equalsIgnoreCase("content"))
				mailContent= ServletUtil.getParamString(req, "mailContent");
			else if(contentType.equals("template"))
				template_id = ServletUtil.getParamString(req, "template_id");
			
			if(targetType.equalsIgnoreCase("string"))
			{
				targetString = ServletUtil.getParamString(req, "targetString");
				if(targetString != null && !(targetString.equals("")))
				{
					String newline = System.getProperty("line.separator");
					if(targetString.indexOf(newline)>-1)
						targetString = targetString.replaceAll(newline, "");					
									
					if(targetString.indexOf("Æ")>-1)
					{
						StringBuffer s = new StringBuffer();
						String[] t = targetString.split("Æ");
						for(int i = 0;i<t.length;i++)
						{					
							s.append(t[i]).append(newline);
						}
						targetString = s.toString();
					
					}
					else
					{
						return RESULT_ETC;
					}
				}
				else
				{
					return RESULT_FAIL_PARAM;
				}
				
			}
			else if(targetType.equalsIgnoreCase("query"))
			{
				targetQuery = ServletUtil.getParamString(req, "targetQuery");
				targetDBId = ServletUtil.getParamString(req, "targetDBId");
				if(targetQuery==null||targetQuery.equals("")||targetDBId==null||targetDBId.equals(""))
					return RESULT_FAIL_PARAM;
			}
			if(sendType.equalsIgnoreCase(Constant.SEND_TYPE_NOW))
			{
				sendSchedule ="";
			}
			else if(sendType.equalsIgnoreCase(Constant.SEND_TYPE_RESERVE))
			{
				sendSchedule = ServletUtil.getParamString(req, "sendSchedule");
			}
			else
				return RESULT_FAIL_PARAM;
			
			if(!attachPath.equals("")){
				attachFileNames = ServletUtil.getParamString(req, "attachFileNames");
				attachFileRealNames = ServletUtil.getParamString(req, "attachFileRealNames");
				if(attachFileNames.equals("")||attachFileRealNames.equals(""))
					return RESULT_FAIL_PARAM;
				else{
					attachFileNamesArray = attachFileNames.split(";");
					attachFileRealNamesArray = attachFileRealNames.split(";");
					if(attachFileNamesArray.length==attachFileRealNamesArray.length){
						if(!checkFiles())
							return RESULT_ETC;
					}
					else
						return RESULT_FAIL_PARAM;
				}
			}
		}
		else
		{
			return RESULT_FAIL_PARAM;
		}
		
		
		try
		{
			//add target			
			if(targetType.equalsIgnoreCase("string"))
				target_id = addTargetDirect();
			
			else if(targetType.equalsIgnoreCase("query"))
				target_id = addTargetQuery();				
		
			
			if(target_id <= 0)
			{
				return RESULT_FAIL_TARGET;
			}
		}
		catch(Exception e)
		{
			logger.error("add target error : " + e);
			return RESULT_FAIL_TARGET;
		}
		
		
		try
		{
			//add mail
			int massmail_id = -1;
			if(contentType.equalsIgnoreCase("template"))
				mailContent = getTemplateContent(Integer.parseInt(template_id));
		
			massmail_id	= addMassmail();
		
			if(massmail_id < 0)
			{
				return RESULT_FAIL_MAIL;
			}
		}
		catch(Exception e)
		{
			logger.error("add mail error : " + e);
			return RESULT_FAIL_MAIL;
		}
		
		
		return RESULT_SUCCESS;
		
		
	}
	
	/**
	 * 대상자 추가 - 쿼리 방식
	 * @param title
	 * @param queryString
	 * @param fileOneToOne_st
	 * @param writer
	 * @param targetDBId
	 * @return
	 */
	private int addTargetQuery()
	{
		TargetListService targetListService = TargetListControlHelper.getUserService(context);
		MassMailAPIService massMailAPIService = MassMailAPIControlHelper.getUserService(context);
		
		TargetList targetList = new TargetList();
		
		targetList.setTargetName(mailTitle);		
		targetList.setUserID(writer);
		targetList.setDescription(writer);
		targetList.setTargetType(Constant.TARGET_TYPE_DB);		//파일등록
		targetList.setShareType("1");
		targetList.setState(Constant.TARGET_STATE_FINISH);		
		targetList.setDbID(targetDBId);  //파일등록이므로 기본 TM DB
		targetList.setBookMark("N");
		targetList.setQueryText(targetQuery);
		String countQuery = ""; 
		Connection conn = null;
		int count =0;
		try {
			countQuery = QueryUtil.makeCountQuery(targetQuery);
			conn = getConnectionDBInfo(targetDBId);
			count = getCountQueryText(conn, countQuery );
			
		} catch (Exception e) {
			logger.error(e);
			
		}
		
		targetList.setTargetCount(count);
		targetList.setCountQuery(countQuery);
		
		String[] queryOneToOnes = fileOneToOne_st.split("ø");
		
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		int targetID = 0;		
		
		
		synchronized(this){
			
			//1. 대상자를 저장한다.
			targetID = targetListService.insertTargetList(targetList);
			
			if(targetID>0){
				targetID = targetListService.getMaxTargetID();
					OnetooneTarget onetooneTarget = null;
					if(queryOneToOnes!=null && queryOneToOnes.length>0){
						for(int i=0; i < queryOneToOnes.length; i ++ ) {
							String[] parseTarget =  queryOneToOnes[i].split("≠");
							parseTarget[0] = String.valueOf(massMailAPIService.getOnetooneID(parseTarget[0]));
							
							 
							onetooneTarget = new OnetooneTarget();  
							onetooneTarget.setTargetID(targetID);
							String st = parseTarget[1]; 
							if(st.indexOf(".")>-1)
								onetooneTarget.setFieldName(parseTarget[1].substring(st.indexOf(".")+1));
							else
								onetooneTarget.setFieldName(parseTarget[1]);
							onetooneTarget.setFieldDesc(parseTarget[1]);
							onetooneTarget.setCsvColumnPos(i+1);
							onetooneTarget.setOnetooneID(Integer.parseInt(parseTarget[0]));
							
							onetooneTargetList.add(onetooneTarget);
						} 
					}
					targetListService.insertOnetooneTarget(onetooneTargetList);
			}
			
		}// end synchronized	
		
		
		return targetID;
	}
	
	/**
	 * 쿼리 방식에서 대상자 인원수 카운트 가져오기
	 * @param conn
	 * @param queryText
	 * @return
	 */
	private int getCountQueryText(Connection conn, String queryText){
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = -1;
		
		try{		
			ps = conn.prepareStatement(queryText);
			rs = ps.executeQuery();				
			while(rs.next()){
				count = rs.getInt("CNT");						
			}
		}catch(Exception e){
			logger.error(e);
		}finally{
			try{ps.close();}catch(Exception e){}
			try{rs.close();}catch(Exception e){}		
			try{conn.close();}catch(Exception e){}	
			
		}		
		return count;
	}
	
	
	
	
	/**
	 * <p>dbID에 해당하는 접속정보를 가져오고 접속한다. 
	 * @param dbID
	 * @return
	 */
	private Connection getConnectionDBInfo(String dbID){
		TargetListService targetListService = TargetListControlHelper.getUserService(context);
		Connection conn = null;
		Map<String, Object> mapdb = targetListService.getDBInfo(dbID);
		
		if(mapdb==null || mapdb.size()==0){
			return null;
		}
		
		String driver = String.valueOf(mapdb.get("driverClass"));
		String url = String.valueOf(mapdb.get("dbURL"));
		String user = String.valueOf(mapdb.get("dbUserID"));
		String password = String.valueOf(mapdb.get("dbUserPWD"));
		
		conn = DBUtil.getConnection(driver, url, user, password);
		
		return conn;
	}
	
	
	/**
	 *  대상자 추가 - 직접 입력 
	 * @param title
	 * @param targetString
	 * @param fileOneToOne_st
	 * @param writer
	 * @return
	 */
	private int addTargetDirect()
	{
		
		TargetListService targetListService = TargetListControlHelper.getUserService(context);
		MassMailAPIService massMailAPIService = MassMailAPIControlHelper.getUserService(context);
		
		String[] fileOneToOne = fileOneToOne_st.split("ø");
		
		TargetList targetList = new TargetList();
		
		targetList.setTargetName(mailTitle);		
		targetList.setUserID(writer);
		targetList.setDescription(writer);
		targetList.setTargetType(Constant.TARGET_TYPE_DIRECT);		//파일등록
		targetList.setShareType("1");
		targetList.setState(Constant.TARGET_STATE_READY);		
		targetList.setDbID(Constant.TM5_DBID);  //파일등록이므로 기본 TM DB
		targetList.setBookMark("N");
		targetList.setDirectText(targetString);
		
		List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
		int targetID = -1;	
		
		if(targetListService.insertTargetList(targetList)>0)
		{
			OnetooneTarget onetooneTarget = null;
			if(fileOneToOne != null) {
				targetID = targetListService.getMaxTargetID();
				
				for(int i=0; i < fileOneToOne.length; i ++ ) {
				
					if(fileOneToOne[i].indexOf("≠") >= 0) {
						
						String[] parseTarget = fileOneToOne[i].split("≠");
						
						parseTarget[0] = String.valueOf(massMailAPIService.getOnetooneID(parseTarget[0]));
												
						onetooneTarget = new OnetooneTarget();
						onetooneTarget.setTargetID(targetID);
						onetooneTarget.setCsvColumnPos(i+1);
						onetooneTarget.setFieldName("col"+parseTarget[0]);
						onetooneTarget.setOnetooneID(Integer.parseInt(parseTarget[0]));
						onetooneTarget.setFieldDesc(parseTarget[1]);
						
						onetooneTargetList.add(onetooneTarget);
					
					}
				}
			}
			
			if(targetListService.insertOnetooneTarget(onetooneTargetList)>0)
			{
				String filePath = "";
				String fileType = "direct";
				
				
				
				try {
					TargetListFileThread fileThread = new TargetListFileThread(targetListService,"", targetList.getUploadKey(), targetID, filePath, onetooneTargetList,fileType, targetString);
					fileThread.start();
					fileThread.join();
				} catch (InterruptedException e) {
					logger.error(e);
					
				}
				
				//쓰레드 완료 후 타겟 생성 완료되면
				if(massMailAPIService.getTargetState(targetID)!=3)
					targetID = -1;
					
			}
		}
		
		return targetID;
		
		
	}
	
	
	/**
	 * 대량메일 작성
	 * @param mailTitle
	 * @param massmailGroupID
	 * @param senderEmail
	 * @param senderName
	 * @param receiverName
	 * @param mailContent
	 * @param writer
	 * @param targetID
	 * @return
	 */
	
	private int addMassmail()
	{
		MassMailService massMailService = MassMailControlHelper.getUserService(context);
		SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(context);
		
		MassMailInfo massMailInfo = new MassMailInfo();
		int massmailID = -1;
		
		//발송타입 
		//String sendType = Constant.SEND_TYPE_NOW;
		
		//발송 우선 쉰위
		String priority = "3";
		//수신거부타입
		String rejectType  = "1";
		
		//통계기간설정 
		String statisticsType = "1";		
		//통계공유여부 
		String statisticsOpenType = "1";	
		//메모 
		String memo = "";
		
		//컨텐츠
		
		String returnMail = systemSetservice.getSystemSetInfo("1","returnMail");
		
		massMailInfo.setMassmailTitle(mailTitle);
		massMailInfo.setDescription("");
		massMailInfo.setMassmailGroupID(Integer.parseInt(massMailGroupID));
		massMailInfo.setSendType(sendType);	
		
		ArrayList<String> sendScheduleDatList =null; //발송일  
		sendScheduleDatList = new ArrayList<String>();
		//즉시발송일 경우 
		if(massMailInfo.getSendType().equals(Constant.SEND_TYPE_NOW))
		{
			sendScheduleDatList.add(DateUtils.getDateTimeString());
		//예약발송일 경우 
		}else if(massMailInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE))
		{
			if(!sendSchedule.equals(""))
				sendScheduleDatList.add(sendSchedule);
			else
				sendScheduleDatList.add(DateUtils.getDateTimeString());
		}
		massMailInfo.setRejectType(rejectType);
		
		
		massMailInfo.setStatisticsType(statisticsType);
		massMailInfo.setStatisticsOpenType(statisticsOpenType);
		massMailInfo.setMemo(memo);
		
		massMailInfo.setSenderMail(senderEmail);
		massMailInfo.setReturnMail(returnMail);
		massMailInfo.setSenderName(senderName);
		massMailInfo.setReceiverName(receiverName);
		massMailInfo.setEncodingType("EUC-KR");
		massMailInfo.setWebURL("");
		massMailInfo.setWebURLType("1");
		massMailInfo.setMailTitle(mailTitle);
		massMailInfo.setMailContent(mailContent);		
		massMailInfo.setMailLinkYN("N")	;
		massMailInfo.setPriority(priority);
		massMailInfo.setApproveUserID(writer);
		
		massMailInfo.setAttachedFileNames(attachFileNames);
		massMailInfo.setAttachedFilePath(attachFileRealNames);
		
		String[] targetIDs = new String[1];
		targetIDs[0] = String.valueOf(target_id);
		String[] exceptYNs = new String[1];
		exceptYNs[0] = "N";
		
		//발송상태(00: 임시저장, 10: 승인대기중, 11: 발송준비대기중)
		String saveState = "11";
		
		String userID = writer;
		
		massMailInfo.setUserID(userID);
		
		
		
		synchronized(this)
		{	 
			if(massMailService.insertMassMailInfo(massMailInfo)>0)
			{
				massmailID = massMailService.getMassMailIDInfo();
				
				if(massmailID>0)
				{
					try
					{
						massMailInfo.setMassmailID(massmailID);
					
						MassMailSchedule[] massMailSchedules = new MassMailSchedule[sendScheduleDatList.size()];		
					
						String statisticsEndDate = ""; //통계마감일 
						
						for(int i=0;i<sendScheduleDatList.size();i++)
						{						
						
							massMailSchedules[i]= new MassMailSchedule();
							massMailSchedules[i].setMassmailID(massmailID);
							massMailSchedules[i].setScheduleID(i+1);
							massMailSchedules[i].setSendScheduleDate(sendScheduleDatList.get(i));
						
							//발송일부터 1주일까지 통계수집 
							if(massMailInfo.getStatisticsType().equals(Constant.STATISTICS_TYPE_WEEK)){
								statisticsEndDate = DateUtils.getDateAddDays(sendScheduleDatList.get(i), 7);
							}else{
								statisticsEndDate = DateUtils.getDateAddMonths(sendScheduleDatList.get(i), 1); //발송일부터 한달간 수집 
							}
							massMailSchedules[i].setStatisticsEndDate(statisticsEndDate); 						
						
							massMailSchedules[i].setState(saveState);				
						}
						massMailService.insertMassMail(massMailInfo, targetIDs, exceptYNs, massMailSchedules);
				
						
					}catch(Exception e){
						
						logger.error(e);
						massMailService.deleteMassMail(massmailID);
						return -1;
					}
				}
			}			
		}
		
		
		return massmailID;
	}
	
	/**
	 * contentType이 template일 때 템플릿 아이디로 메일 내용 가져오기
	 * @param template_id
	 * @return
	 */
	private String getTemplateContent(int template_id)
	{
		MassMailAPIService massMailAPIService = MassMailAPIControlHelper.getUserService(context);
		
		return massMailAPIService.getTemplateContent(template_id);
		
	}
	
	/**
	 * 첨부파일의 갯수와 총 용량을 확인하여 첨부가능한지 판단한다. 
	 * 해당 파일을 썬더메일의 대량메일 첨부파일 경로에 넣는다.  
	 * @param attachPath
	 * @param attachFileNames
	 * @param attachFileRealNames
	 * @return boolean
	 */
	private boolean checkFiles(){
		if(attachFileRealNamesArray.length>3){
			logger.error("The number of attachFiles are over 3!! - " + attachFileRealNamesArray.length );
			return false;
		}
		
		int fileTotalSize = 0;
		int fileTotalCount = 0;
		for(int i=0;i<attachFileRealNamesArray.length;i++){
			if(attachFileRealNamesArray[i]!=null && !attachFileRealNamesArray[i].equals("")){
				logger.info("attachFileRealNamesArray["+i+"] : " + attachFileRealNamesArray[i]);
				File f = new File(attachPath + File.separator + attachFileRealNamesArray[i]);
				logger.info(attachFileRealNamesArray[i] + " size : " + f.length());
				FileInputStream fis = null;
				FileOutputStream fos = null;
				byte b[] = new byte[1024];
				try{
					fis = new FileInputStream(f);
					fos = new FileOutputStream(realUploadPath+ File.separator + attachFileRealNamesArray[i]);
					int len;
					while((len=fis.read(b))>0){
						fos.write(b, 0, len);
					}
					
				}catch(IOException e){
					logger.error(e);
					return false;
				}finally{
					if(fos!=null)try{fos.close();}catch(Exception e){}
					if(fis!=null)try{fis.close();}catch(Exception e){}
				}
				
				fileTotalCount++;
				fileTotalSize += f.length();
			}
		}
		
		if(fileTotalSize > 5242880 || fileTotalCount >3){
			logger.error("fileTotalSize is over 5Mb !! - " + fileTotalSize );
			logger.error("fileTotalCount is over 3 !! - " + fileTotalCount );
			return false;
		}
		else
			return true;
	}
	
	private void setInit(){
		writer = null;
		mailTitle = null;		
		senderEmail = null;
		senderName = null;
		receiverName = null;

		contentType = null;
		mailContent = null;
		template_id = null;

		targetType = null;
		targetQuery = null;
		targetDBId = null;
		targetString = null;
		fileOneToOne_st = null;
		sendSchedule = null;
		sendType = null;
		target_id = -1;
			
		massMailGroupID = null;

		attachPath = null;
		attachFileNames = null;
		attachFileRealNames = null;
		attachFileNamesArray = null;
		attachFileRealNamesArray = null;

		realUploadPath = null;
	}
	

}
