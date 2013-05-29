package web.massmail.write.control;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest; 
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.common.util.*;
import web.massmail.write.service.MassMailService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import web.massmail.write.model.*;
import web.common.model.FileUpload;
import web.admin.usergroup.model.User;


public class MassMailController extends MultiActionController{
	

	private MassMailService massMailService= null;
	private String sCurPage = null;
	private String message = "";
	private Logger logger = Logger.getLogger("TM");
	private String realUploadPath = null;
	
	public void setMassMailService(MassMailService massMailService){		
		this.massMailService = massMailService;
	}
	
	//action-servlet.xml에 저장된 업로드 경로를 읽어온다. 
	public void setRealUploadPath(String realUploadPath) {
		this.realUploadPath = realUploadPath;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		String step = ServletUtil.getParamString(req, "step");
		String pageURL = "";
		if(step.equals("write")){
			pageURL = "/pages/massmail/write/massmail_write.jsp";
		}else if(step.equals("list")){
			pageURL =  "/pages/massmail/write/massmail_list.jsp";
		}else if(step.equals("listRepeat")){
			pageURL =  "/pages/massmail/write/massmail_repeat_list.jsp";
		}
		return new ModelAndView(pageURL);
	}
	
	/**
	 * <p>대량메일 작성
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req,"massmailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		String state = ServletUtil.getParamString(req, "state");
				
		req.setAttribute("targetGroupList", massMailService.listTargetingGroup(massmailID));
		req.setAttribute("state", state);
		
		return new ModelAndView("/pages/massmail/write/massmail_write.jsp?method=edit","massmailInfo", massMailService.viewMassMailInfo(massmailID,scheduleID));
	}
	
	/**
	 * <p>대량메일 복사
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView copyMassMail(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int massmailID = ServletUtil.getParamInt(req,"massmailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		
		MassMailInfo massmailInfo = massMailService.viewMassMailInfo(massmailID,scheduleID);
		massmailInfo.setMassmailID(0);
		req.setAttribute("oldMassmailID", Integer.toString(massmailID));
		return new ModelAndView("/pages/massmail/write/massmail_write.jsp","massmailInfo", massmailInfo);
	}

	/**
	 * <p>테스트 메일리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView testList(HttpServletRequest req, HttpServletResponse res) throws Exception{		

		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화		
		String userID = LoginInfo.getUserID(req);
		return new ModelAndView("/pages/massmail/write/massmail_testmail.jsp?method=testList","testerEmailList", massMailService.listTesterEmail(userID,null));

	}

	
	/**
	 * <p>보내는 사람리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView senderList(HttpServletRequest req, HttpServletResponse res) throws Exception{		

		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화	
		String groupID = LoginInfo.getGroupID(req);
		String userID = LoginInfo.getUserID(req);
		return new ModelAndView("/pages/massmail/write/massmail_sender.jsp?method=senderList","senderEmailList", massMailService.selectSenderByUserID(groupID,userID,null));

	}
	
	
	/**
	 * <p>파라미터 세팅 
	 * @param req
	 * @return
	 */
	private MassMailInfo getMassMailInfo(HttpServletRequest req){
		MassMailInfo massMailInfo = new MassMailInfo();
		
		String massmailTitle= ServletUtil.getParamString(req,"eMassmailTitle");
		String description= ServletUtil.getParamString(req,"eDescription");
		int massmailGroupID= ServletUtil.getParamInt(req,"eMassmailGroupID","0");
		String approveUserID =  ServletUtil.getParamString(req,"eApproveUserID");
		
		String[] attachedFilesArray =  ServletUtil.getParamStringArray(req,"eAttachedFiles");
		String[] attachedFilesPathArray =  ServletUtil.getParamStringArray(req,"eAttachedFilesPath"); 
		String attachedFileNames = "";
		String attachedFilePath = "";
		if(attachedFilesArray!=null&&attachedFilesArray.length!=0){
			for(int i = 0; i<attachedFilesArray.length;i++)
			{
				attachedFileNames += ";"+ attachedFilesArray[i];
				attachedFilePath += ";"+attachedFilesPathArray[i];
			}
			attachedFileNames = attachedFileNames.substring(1);
			attachedFilePath = attachedFilePath.substring(1);
		}
		
		//발송타입 
		String sendType = ServletUtil.getParamString(req,"eSendType");
		
		//반복타입 
		String repeatSendType = ServletUtil.getParamString(req,"eRepeatSendType");
		
		//반복발송시작시간 
		String repeatSendStartDate = ServletUtil.getParamString(req,"eRepeatSendStart");
		
		//반복발송종료시간
		String repeatSendEndDate = ServletUtil.getParamString(req,"eRepeatSendEnd");
		
		//반복발송될 시간/분  
		String repeatSendDateHH = ServletUtil.getParamString(req,"eRepeatSendDateHH");
		String repeatSendDateMM = ServletUtil.getParamString(req,"eRepeatSendDateMM");				
		
		//매주반복 
		String[] repeatSendWeek = ServletUtil.getParamStringArray(req,"eRepeatSendWeek");
		
		//매월반복 (매월 특정일 반복)
		int repeatSendDay = ServletUtil.getParamInt(req,"eRepeatSendDay","0");		
		
		//발송 우선 쉰위
		String priority = ServletUtil.getParamString(req,"ePriority");

		
		//필터링 
		//수신거부타입
		String rejectType  = ServletUtil.getParamString(req,"eRejectType");
		//발송된년
		String sendedYear  = "";//ServletUtil.getParamString(req,"eSendedYear");
		//발송된월
		String sendedMonth  = "";//ServletUtil.getParamString(req,"eSendedMonth");
		//발송된년월
		String sendedYearMonth  = ServletUtil.getParamString(req,"eSendedYearMonth");
		if(sendedYearMonth!=null && !sendedYearMonth.equals("") && sendedYearMonth.length() == 6){
			 sendedYear = sendedYearMonth.substring(0, 4);
			 sendedMonth = sendedYearMonth.substring(4, 6);
		}
	
		//발송된통수
		int sendedCount  = ServletUtil.getParamInt(req,"eSendedCount","0");
		//미오픈제외 
		String notOpen  = ServletUtil.getParamString(req,"eNotOpen");		
		//영구적인오류자
		int persistErrorCount  = ServletUtil.getParamInt(req,"ePersistErrorCount","0");
		
		//중복허용 여부 (Y : 허용   N : 제거)
		String duplicationYN = ServletUtil.getParamString(req,"eDuplicationYN");
		
		
		//통계기간설정 
		String statisticsType = ServletUtil.getParamString(req,"eStatisticsType");		
		//통계공유여부 
		String statisticsOpenType = ServletUtil.getParamString(req,"eStatisticsOpenType");
		//메모 
		String memo =  ServletUtil.getParamString(req,"eMemo");
		
		
		
		//컨텐츠
		String senderMail = ServletUtil.getParamString(req,"eSenderMail");
		String returnMail = ServletUtil.getParamString(req,"eReturnMail");
		String senderName = ServletUtil.getParamString(req,"eSenderName");
		String receiverName = ServletUtil.getParamString(req,"eReceiverName");
		String encodingType = ServletUtil.getParamString(req,"eEncodingType");
		String webURL = ServletUtil.getParamString(req,"eWebURL");
		String webURLType = ServletUtil.getParamString(req,"eWebURLType");
		String mailTitle = ServletUtil.getParamString(req,"eMailTitle");			
		String mailContent = ServletUtil.getParamString(req,"eMailContent");
		String mailLinkYN = ServletUtil.getParamString(req,"eMailLinkYN");
		String filterManagerYN = ServletUtil.getParamString(req,"filterManagerYN");
		
	
		int pollID = ServletUtil.getParamInt(req,"ePollID","0");
		
		//즉시발송일 경우 
		if(sendType.equals(Constant.SEND_TYPE_NOW)){
			//아무작업안함 
		}else if(sendType.equals(Constant.SEND_TYPE_RESERVE)){
			//아무작업 안함 
		//반복발송일경우 	
		}else if(sendType.equals(Constant.SEND_TYPE_REPEAT)){		
			
			
			//매일반복발송 
			if(repeatSendType.equals(Constant.REPEAT_SEND_TYPE_1)){
				
			//매주반복발송 	
			}else if(repeatSendType.equals(Constant.REPEAT_SEND_TYPE_2)){
				String weekdays = "";
				for(String dd : repeatSendWeek){
					weekdays+= dd +":";
				}				
				
				massMailInfo.setRepeatSendWeek(weekdays);				
			//매월반복발송 	
			}else if(repeatSendType.equals(Constant.REPEAT_SEND_TYPE_3)){
				massMailInfo.setRepeatSendDay(repeatSendDay);
			}
			massMailInfo.setRepeatSendType(repeatSendType);		
			
			if(Integer.parseInt(repeatSendDateHH)<10) repeatSendDateHH="0"+repeatSendDateHH;
			if(Integer.parseInt(repeatSendDateMM)<10) repeatSendDateMM="0"+repeatSendDateMM;
			//massMailInfo.setRepeatSendStartDate(repeatSendStartDate+" "+repeatSendDateHH+":"+repeatSendDateMM+":00");
			//massMailInfo.setRepeatSendEndDate(repeatSendEndDate+" "+repeatSendDateHH+":"+repeatSendDateMM+":00");
			massMailInfo.setRepeatSendStartDate(repeatSendStartDate+" "+repeatSendDateHH+":"+repeatSendDateMM+":00");
			massMailInfo.setRepeatSendEndDate(repeatSendEndDate+" "+repeatSendDateHH+":"+repeatSendDateMM+":00");		
			//System.out.println("massMailInfo.getRepeatSendStartDate()=="+massMailInfo.getRepeatSendStartDate());
			//System.out.println("massMailInfo.getRepeatSendEndDate()=="+massMailInfo.getRepeatSendEndDate());
			
		}		
		
		
		
		massMailInfo.setMassmailTitle(massmailTitle);
		massMailInfo.setDescription(description);
		massMailInfo.setMassmailGroupID(massmailGroupID);
		massMailInfo.setSendType(sendType);		
		massMailInfo.setRejectType(rejectType);
		massMailInfo.setDuplicationYN(duplicationYN);
		
		//필터링설정
		//발송된 통수제한이고 미오픈도 제한이면 둘다 제한한다. 
		if(sendedCount!=0 && (notOpen!=null && notOpen.equals("Y"))){
			massMailInfo.setSendedType(Constant.FILTER_TYPE_ALL);
		//발송통수만 제한 	
		}else if(sendedCount!=0 && (notOpen==null || notOpen.equals(""))){
			massMailInfo.setSendedType(Constant.FILTER_TYPE_SEND);
			
		//미오픈만 제한 	
		}else if(sendedCount==0 && (notOpen!=null && notOpen.equals("Y"))){
			massMailInfo.setSendedType(Constant.FILTER_TYPE_NOT);
		}		
		massMailInfo.setSendedYear(sendedYear);
		massMailInfo.setSendedMonth(sendedMonth);
		massMailInfo.setSendedCount(sendedCount);
		massMailInfo.setPersistErrorCount(persistErrorCount);
		massMailInfo.setStatisticsOpenType(statisticsOpenType);
		massMailInfo.setStatisticsType(statisticsType);
		massMailInfo.setStatisticsOpenType(statisticsOpenType);
		massMailInfo.setMemo(memo);
		
		massMailInfo.setSenderMail(senderMail);
		massMailInfo.setReturnMail(returnMail);
		massMailInfo.setSenderName(senderName);
		massMailInfo.setReceiverName(receiverName);
		massMailInfo.setEncodingType(encodingType);
		massMailInfo.setWebURL(webURL);
		massMailInfo.setWebURLType(webURLType);
		massMailInfo.setMailTitle(mailTitle);
		massMailInfo.setMailContent(mailContent);		
		massMailInfo.setMailLinkYN(mailLinkYN);
		massMailInfo.setApproveUserID(approveUserID);
		massMailInfo.setPriority(priority);
		massMailInfo.setPollID(pollID);
		massMailInfo.setFilterManagerYN(filterManagerYN);
		massMailInfo.setAttachedFileNames(attachedFileNames);
		massMailInfo.setAttachedFilePath(attachedFilePath);
		
		return massMailInfo;
	}
	
	
	/**
	 * <p>테스트메일 발송이라면 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView sendTestMail(HttpServletRequest req, HttpServletResponse res) throws Exception{
		MassMailInfo massMailInfo =getMassMailInfo(req);
		String sendTestEmail =  ServletUtil.getParamString(req,"eSendTestEmail"); //분
		String userID = LoginInfo.getUserID(req);		
		String[]sendTestEmailArray = sendTestEmail.split(";");
		String[]targetIDs = ServletUtil.getParamStringArray(req, "targetID");
		
		Map<String, Object> map = massMailService.getQeuryDB(Integer.parseInt(targetIDs[0]));

		
		String query = String.valueOf(map.get("queryText"));	
		String dbType = String.valueOf(map.get("driverType"));
		String dbID =  String.valueOf(map.get("dbID"));
		String dbDriver =  String.valueOf(map.get("driverClass"));

        String sKey = StringUtil.createSecurityKey("TM", dbID, dbDriver);

		String dbURL =  TmEncryptionUtil.decrypto(String.valueOf(map.get("dbURL")), sKey);
		String dbUserID =  TmEncryptionUtil.decrypto(String.valueOf(map.get("dbUserID")), sKey);
		String dbUserPWD =  TmEncryptionUtil.decrypto(String.valueOf(map.get("dbUserPWD")), sKey);
        
	
		String sampleQuery = QueryUtil.makeTopCountQuery(query, dbType, sendTestEmailArray.length);
		//추출된 쿼리를 수행하고 원투원 치환한다. 

		PreparedStatement ps = null;	
		ResultSet rs = null;
		
	
		Connection conn = DBUtil.getConnection(dbDriver, dbURL, dbUserID, dbUserPWD);
		
		if(conn==null){
			logger.error("테스트메일 발송중 :: Connetion null 에러 발생");
			return null;
		}
		
		List<OnetooneTarget> onetooneTargetList = massMailService.listOnetooneTarget(Integer.parseInt(targetIDs[0]));
		try{			
				
			//등록된 타겟쿼리를 실행한다. 
			ps = conn.prepareStatement(sampleQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();			
			
			SystemNotify[] systemNotifys = new SystemNotify[sendTestEmailArray.length];

			int i = 0;
			while(rs.next()){
				systemNotifys[i] = new SystemNotify();
				
				systemNotifys[i].setUserID(userID);
				systemNotifys[i].setEncodingType(massMailInfo.getEncodingType());
				systemNotifys[i].setSenderMail(massMailInfo.getSenderMail());
				systemNotifys[i].setSenderName(massMailInfo.getSenderName());
				systemNotifys[i].setReceiverMail(sendTestEmailArray[i]); //위에서 입력된 테스트메일이 입력된다.
				systemNotifys[i].setNotifyFlag(Constant.SYSTEM_NOTIFY_TESTER);
				systemNotifys[i].setNotifyType(Constant.SYSTEM_NOTIFY_TYPE_EMAIL);
				systemNotifys[i].setReturnMail(massMailInfo.getReturnMail());

				String onetooneInfo = "";
				for(OnetooneTarget onetooneTarget : onetooneTargetList){
					onetooneInfo += onetooneTarget.getOnetooneAlias() + Constant.TAG_SPLITER+rs.getString(onetooneTarget.getFieldName())+ Constant.VALUE_SPLITER;
				}

				//메일제목과 내용을 원투원 치환 
				systemNotifys[i].setReceiverName(ThunderUtil.replaceOnetoone(onetooneInfo, massMailInfo.getReceiverName()));
				systemNotifys[i].setMailTitle(Constant.SYSTEM_NOTIFY_TESTER_DESC+ThunderUtil.replaceOnetoone(onetooneInfo, massMailInfo.getMailTitle()));
				String content = "";
				if(massMailInfo.getWebURLType().equals("2"))
					content = getWebURLContent(massMailInfo.getEncodingType(), massMailInfo.getWebURL());
				else
					content = massMailInfo.getMailContent();
				systemNotifys[i].setMailContent(ThunderUtil.replaceOnetoone(onetooneInfo, content));
				systemNotifys[i].setFileNames(massMailInfo.getAttachedFileNames());
				systemNotifys[i].setFilePath(massMailInfo.getAttachedFilePath());

				i++;
			}
			//시스템공지테이블에 입력한다.
			massMailService.insertSystemNotify(systemNotifys);
		
		}catch(Exception e){						
			logger.error(e);
		}finally{		
			
			try{ps.close();}catch(Exception e){}
			try{rs.close();}catch(Exception e){}
			try{conn.close();}catch(Exception e){}
		}		
			
		
		return null;
		
	}
	
	/**
	 * <p>대량메일 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */

	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{	 
		
		int massmailID = 0;
		
		MassMailInfo massMailInfo =getMassMailInfo(req);
		
		String[] targetIDs = ServletUtil.getParamStringArray(req, "targetID");
		String[] exceptYNs = ServletUtil.getParamStringArray(req, "exceptYN");
		
		//발송스케줄 
		String sendSchedule = ServletUtil.getParamString(req,"eSendSchedule");
		String sendScheduleHH = ServletUtil.getParamString(req,"eSendScheduleHH");  //시간
		String sendScheduleMM = ServletUtil.getParamString(req,"eSendScheduleMM"); //분
					
		
		//발송상태(00: 임시저장, 10: 승인대기중, 11: 발송준비대기중)
		String saveState = ServletUtil.getParamString(req,"eSaveState");
		
		String userID = LoginInfo.getUserID(req);
		String isAdmin = LoginInfo.getIsAdmin(req);
		String auth_send_mail = LoginInfo.getAuth_send_mail(req);
		
		massMailInfo.setUserID(userID);
		if(!saveState.equals(Constant.STATE_WRITE)){
			if(!isAdmin.equals("Y") && auth_send_mail.equals("N")){			
				saveState = Constant.STATE_APPREADY;
			}else if(isAdmin.equals("Y") || auth_send_mail.equals("Y")){			
				saveState= Constant.STATE_PREPARE_READY;
			}
		}
				
		ArrayList<String> sendScheduleDatList =null; //발송일  
			
		
		//즉시발송일 경우 
		if(massMailInfo.getSendType().equals(Constant.SEND_TYPE_NOW)){
			
			sendScheduleDatList = new ArrayList<String>();
			sendScheduleDatList.add(DateUtils.getDateTimeString());
	
		//예약발송일 경우 
		}else if(massMailInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE)){
			sendScheduleDatList = new ArrayList<String>();
			if(!sendSchedule.equals("") && !sendScheduleHH.equals("") && !sendScheduleMM.equals("")){
				sendScheduleDatList.add(sendSchedule+" "+sendScheduleHH+":"+sendScheduleMM+":00");
			}
			
		//반복발송일 경우에는 	반복발송시작일과 종료일에 해당되는 발송개수가 ez_massmail_schedule에 입력된다. 
		}else if(massMailInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){
			
			//매일반복일 경우 
			if(massMailInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_1)){
				sendScheduleDatList = DateUtils.getDateDifferDay(massMailInfo.getRepeatSendStartDate(), massMailInfo.getRepeatSendEndDate());				
				
			//매주 특정요일 반복발송일 경우 	
			}else if(massMailInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_2)){
				String[] weeks =  massMailInfo.getRepeatSendWeek().split(":");
				sendScheduleDatList = DateUtils.getDateDifferWeek(massMailInfo.getRepeatSendStartDate(), massMailInfo.getRepeatSendEndDate(), weeks);
				
			//매월 특정일 반복발송일 경우 	
			}else if(massMailInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_3)){
				sendScheduleDatList = DateUtils.getDateDifferMonth(massMailInfo.getRepeatSendStartDate(), massMailInfo.getRepeatSendEndDate(), massMailInfo.getRepeatSendDay());
			}
		}
		
		
		int resultVal =0;

		if(targetIDs==null || targetIDs.length==0){
			ServletUtil.messageGoURL(res,"대상자그룹이 존재하지 않습니다.","");
			return null;	
		}
		
		synchronized(this){
			
			//1. 기본정보입력 
			if(massMailService.insertMassMailInfo(massMailInfo)>0){
				
			
				//2. 입력된 massmailID 가져오기 
				massmailID = massMailService.getMassMailIDInfo();
												
				if(massmailID>0){
					massMailInfo.setMassmailID(massmailID);
					
					MassMailSchedule[] massMailSchedules = new MassMailSchedule[sendScheduleDatList.size()];		
					
					String statisticsEndDate = ""; //통계마감일 
					
					for(int i=0;i<sendScheduleDatList.size();i++){						
						
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
						
						//일단 메일링크수집이면 임시저장으로 한다. 메일링크수집이 끝나면 발송으로 변경 ..
						if(!saveState.equals(Constant.STATE_WRITE) && massMailInfo.getMailLinkYN().equals("Y")){
							massMailSchedules[i].setState(Constant.STATE_WRITE);
						}else{
							massMailSchedules[i].setState(saveState);
						}
						
						//만약 임시저장이라면 반복발송일 경우에는 더이상 입력될 필요가 없다. 
						if(saveState.equals(Constant.STATE_WRITE)){
							break;
						}

					}
					
					try{
						
						
						//나머지 정보 일괄입력 
						massMailService.insertMassMail(massMailInfo, targetIDs, exceptYNs, massMailSchedules);
						
						//임시저장이 아니고 검증기능 사용이면
						if(!saveState.equals(Constant.STATE_WRITE)&& massMailInfo.getFilterManagerYN().equals("Y"))
						{
							//검증
							List<FilterManager> filterManagerList = massMailService.getFilter();
							ContentFilter cf = new ContentFilter(massMailInfo.getMailContent(), massMailInfo.getMailTitle());
							List<FilterInfo> filterInfolist = cf.getContentExtract( filterManagerList);	
							if(filterInfolist.size()!=0)
							{
								ServletUtil.messageGoURL(res,"메일 내용을 검증한 후 발송해야 합니다. 검증 버튼을 클릭하세요.","");
								this.sCurPage = "1"; 
								res.getWriter().print(massmailID+"||");
								return null;
							}
						}
						//임시저장이 아니고 메일링크수집허용이면 
						if(!saveState.equals(Constant.STATE_WRITE) && massMailInfo.getMailLinkYN().equals("Y")){
							try
							{
								ArrayList<MassMailLink>  massmailLinkList = massMailExtractAllLinks(massMailInfo.getMailContent(), massMailInfo.getMassmailID(), "");
								massMailService.insertMassMailLink(massmailLinkList); //ez_massmail_link에 입력한다.		
								resultVal = 1;
							}
							catch(Exception e)
							{
								logger.error(e);
								ServletUtil.messageGoURL(res,"메일 내용의 HTML이 올바르지 않습니다. HTML 원문을 확인하세요.","");
								this.sCurPage = "1"; 
								res.getWriter().print(massmailID+"||");
								return null;
							}
						} else if (!saveState.equals(Constant.STATE_WRITE) && massMailInfo.getMailLinkYN().equals("N")) {
							resultVal = 0;
						} else {
							resultVal = 1;
						}
						
						
					}catch(Exception e){
						resultVal = -1;
						logger.error(e);	
					}
					
					//실패라면 입력된 모든 데이타 삭제 
					if(resultVal<0){ 
						try{
							massMailService.deleteMassMail(massmailID);
						}catch(Exception e){
							logger.error(e);	
						}
					}
				

				}// end 	if(massmailID>0)

			}
						
		}// end synchronized
		
	

		if(resultVal>=0){
			if (resultVal == 0 && saveState.equals(Constant.STATE_APPREADY)) {
				sendApproveMail(massMailInfo, Constant.STATE_APPREADY,  LoginInfo.getUserID(req));
			}
			this.sCurPage = "1"; 
			 res.getWriter().print(massmailID+"|"+saveState);
			//return list(req,res);
			 return null;
			
		} else {
			res.getWriter().print("0");
			ServletUtil.messageGoURL(res,"저장에 실패했습니다.","");			 
			
			return null;	
		}	
	}
	
	
	/**
	 * <p>대량메일 링크추출 
	 * @param mailContent
	 * @param massmailID
	 * @return
	 * @throws Exception
	 */
	public ArrayList<MassMailLink> massMailExtractAllLinks(String mailContent, int massmailID, String rejectLinkURL) throws Exception{
		LinkExtractor le = new LinkExtractor(mailContent, massmailID, rejectLinkURL);		
		return le.massMailExtractAllLinks();
	}
	

	
	/**
	 * <p>대량메일 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */

	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{	 
	
		MassMailInfo massMailInfo =getMassMailInfo(req);
		
		String[] targetIDs = ServletUtil.getParamStringArray(req, "targetID");
		String[] exceptYNs = ServletUtil.getParamStringArray(req, "exceptYN");
		
		//발송스케줄 
		String sendSchedule = ServletUtil.getParamString(req,"eSendSchedule");
		String sendScheduleHH = ServletUtil.getParamString(req,"eSendScheduleHH");  //시간
		String sendScheduleMM = ServletUtil.getParamString(req,"eSendScheduleMM"); //분
		String userID = ServletUtil.getParamString(req,"eUserID");

		
		//발송상태(00: 임시저장, 10: 승인대기중, 11: 발송준비대기중)
		String saveState = ServletUtil.getParamString(req,"eSaveState");
		if(saveState.equals("Y") || saveState.equals("N")){
			massMailInfo.setApproveYN("Y");
			if(saveState.equals("N")){
				saveState = Constant.STATE_WRITE;
			}
		}
			
		String isAdmin = LoginInfo.getIsAdmin(req);
		String auth_send_mail = LoginInfo.getAuth_send_mail(req);
		
		massMailInfo.setUserID(userID);
		if(!saveState.equals(Constant.STATE_WRITE)){
			if(!isAdmin.equals("Y") && auth_send_mail.equals("N")){
				saveState = Constant.STATE_APPREADY;
			}else if(isAdmin.equals("Y") || auth_send_mail.equals("Y")){
				saveState= Constant.STATE_PREPARE_READY;
			}
		}
	
	
		ArrayList<String> sendScheduleDatList =null; //발송일  
		
		
		//즉시발송일 경우 
		if(massMailInfo.getSendType().equals(Constant.SEND_TYPE_NOW)){
			
			sendScheduleDatList = new ArrayList<String>();
			sendScheduleDatList.add(DateUtils.getDateTimeString());
			
		
		//예약발송일 경우 
		}else if(massMailInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE)){
			sendScheduleDatList = new ArrayList<String>();
			if(!sendSchedule.equals("") && !sendScheduleHH.equals("") && !sendScheduleMM.equals("")){
				sendScheduleDatList.add(sendSchedule+" "+sendScheduleHH+":"+sendScheduleMM+":00");
			}
			
		//반복발송일 경우에는 	반복발송시작일과 종료일에 해당되는 발송개수가 ez_massmail_schedule에 입력된다. 
		}else if(massMailInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){
			
			//매일반복일 경우 
			if(massMailInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_1)){
				sendScheduleDatList = DateUtils.getDateDifferDay(massMailInfo.getRepeatSendStartDate(), massMailInfo.getRepeatSendEndDate());				
				
			//매주 특정요일 반복발송일 경우 	
			}else if(massMailInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_2)){
				String[] weeks =  massMailInfo.getRepeatSendWeek().split(":");
				sendScheduleDatList = DateUtils.getDateDifferWeek(massMailInfo.getRepeatSendStartDate(), massMailInfo.getRepeatSendEndDate(), weeks);
				
			//매월 특정일 반복발송일 경우 	
			}else if(massMailInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_3)){
				sendScheduleDatList = DateUtils.getDateDifferMonth(massMailInfo.getRepeatSendStartDate(), massMailInfo.getRepeatSendEndDate(), massMailInfo.getRepeatSendDay());
			}
		}
		
		
		int resultVal =0;

		if(targetIDs==null || targetIDs.length==0){
			ServletUtil.messageGoURL(res,"대상자그룹이 존재하지 않습니다.","");
			return null;	
		}
		
		int pMassmailID = ServletUtil.getParamInt(req,"eMassmailID","0");
		int pScheduleID = ServletUtil.getParamInt(req,"eScheduleID","0");
		
		massMailInfo.setMassmailID(pMassmailID);
		massMailInfo.setScheduleID(pScheduleID);
		
		synchronized(this){
			
			
			//1. 기본정보수정 
			massMailService.updateMassMailInfo(massMailInfo);
			
			//2. 스케줄수정 
			MassMailSchedule[] massMailSchedules = new MassMailSchedule[sendScheduleDatList.size()];
			String statisticsEndDate = ""; //통계마감일 
			
			for(int i=0;i<sendScheduleDatList.size();i++){
				massMailSchedules[i]= new MassMailSchedule();
				massMailSchedules[i].setMassmailID(massMailInfo.getMassmailID());
				massMailSchedules[i].setScheduleID(i+1);
				massMailSchedules[i].setSendScheduleDate(sendScheduleDatList.get(i));
				
				//발송일부터 1주일까지 통계수집 
				if(massMailInfo.getStatisticsType().equals(Constant.STATISTICS_TYPE_WEEK)){
					statisticsEndDate = DateUtils.getDateAddDays(sendScheduleDatList.get(i), 7);
				}else{
					statisticsEndDate = DateUtils.getDateAddMonths(sendScheduleDatList.get(i), 1); //발송일부터 한달간 수집 
				}
				massMailSchedules[i].setStatisticsEndDate(statisticsEndDate); 
				
				//일단 메일링크수집이면 임시저장으로 한다. 메일링크수집이 끝나면 발송으로 변경 ..
				String stateValue = ServletUtil.getParamString(req,"eStateValue");
				if(!saveState.equals(Constant.STATE_WRITE) && massMailInfo.getMailLinkYN().equals("Y")){
					massMailSchedules[i].setState(Constant.STATE_WRITE);
					if (stateValue.equals(Constant.STATE_APPREADY)) {
						massMailSchedules[i].setState(Constant.STATE_APPREADY);
					}
				}else{
					massMailSchedules[i].setState(saveState);
				}
				
				//만약 임시저장이라면 반복발송일 경우에는 더이상 입력될 필요가 없다. 
				if(saveState.equals(Constant.STATE_WRITE)){
					break;
				}
			}			
		
			
			try{
				//스케줄 삭제후 나머지 정보 일괄입력 
				massMailService.updateMassMail(massMailInfo, targetIDs, exceptYNs, massMailSchedules);
				//임시저장이 아니고 검증기능 사용이면
				if(!saveState.equals(Constant.STATE_WRITE)&& massMailInfo.getFilterManagerYN().equals("Y"))
				{
					//검증
					List<FilterManager> filterManagerList = massMailService.getFilter();
					ContentFilter cf = new ContentFilter(massMailInfo.getMailContent(), massMailInfo.getMailTitle());
					List<FilterInfo> filterInfolist = cf.getContentExtract( filterManagerList);
					
					if(filterInfolist.size()!=0)
					{
						int temp = 0;
						for(int i =0; i<filterInfolist.size();i++)
						{
							if(filterInfolist.get(i).getFilterLevel()==1)temp+=1;
						}		
						if(temp>0)
						{
							ServletUtil.messageGoURL(res,"메일 내용을 검증한 후 발송해야 합니다. 검증 버튼을 클릭하세요.","");
							this.sCurPage = "1"; 
							res.getWriter().print(pMassmailID+"||");
							return null;
						}
					}
				}
				//임시저장이 아니고 메일링크수집허용이면 
				if(!saveState.equals(Constant.STATE_WRITE) && massMailInfo.getMailLinkYN().equals("Y")){
					try{
						
						List<MassMailLink>  massmailRejectLinkList = massMailService.listMassMailRejectLink(massMailInfo.getMassmailID());
						
						String rejectLinkURL = "";
						
						if ( massmailRejectLinkList != null ){
							for ( int i = 0 ; i < massmailRejectLinkList.size() ; i++ ) {
								rejectLinkURL = massmailRejectLinkList.get(i).getLinkURL();
							}
						}
												
						//일단 기존메일링크 삭제 
						massMailService.deleteMassMailLink(massMailInfo.getMassmailID());
										
						ArrayList<MassMailLink>  massmailLinkList = massMailExtractAllLinks(massMailInfo.getMailContent(), massMailInfo.getMassmailID(), rejectLinkURL);
					
						//	새로입력 
						massMailService.insertMassMailLink(massmailLinkList); //ez_massmail_link에 입력한다.
						resultVal = 1;
					}
					catch(Exception e)
					{
						logger.error(e);
						ServletUtil.messageGoURL(res,"메일 내용의 HTML이 올바르지 않습니다. HTML 원문을 확인하세요.","");
						this.sCurPage = "1"; 
						res.getWriter().print(pMassmailID+"||");
						return null;
						
					}
				} else if (!saveState.equals(Constant.STATE_WRITE) && massMailInfo.getMailLinkYN().equals("N")) {
					resultVal = 0;
				} else {
					resultVal = 1;
				}
				
			}catch(Exception e){
				logger.error(e);
			}
			
		}//end sychronzied 
		
	

		if(resultVal>=0){
			if (resultVal == 0 && saveState.equals(Constant.STATE_APPREADY)) {
				sendApproveMail(massMailInfo, Constant.STATE_APPREADY, LoginInfo.getUserID(req));
			}
			this.sCurPage = "1"; 
			 res.getWriter().print(pMassmailID+"|"+saveState);			
			 return null;
			
		
		}else{
			res.getWriter().print("0");
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");			 
			
			return null;	
		}	
	}
	
	/**
	 * <p>승인 처리 관련 담당자 메일 발송
	 * @param mailContent
	 * @param massmailID
	 * @param massmailLinkList
	 * @param encodingType
	 * @return
	 * @throws Exception
	 */
	public void sendApproveMail(MassMailInfo massMailInfo, String saveState, String senderID) throws Exception{
		User sender= massMailService.getUserInfo(senderID);
		if(sender.getEmail() == null || sender.getEmail().equals("")){
			sender.setEmail(massMailInfo.getSenderMail());
		}
		
		User receiver = null;
		String mailTitle = "";
		String mailContent = "";
		String senderMail = massMailInfo.getSenderMail();
		
		String domainName = massMailService.getConfigValue(Constant.CONFIG_FLAG_MASSMAIL, "domainName");
		String webPORT = massMailService.getConfigValue(Constant.CONFIG_FLAG_MASSMAIL, "webPORT");
		String webAddress = "http://"+domainName+":"+webPORT;
		
		//승인 요청
		if(saveState.equals("10")){
			receiver = massMailService.getUserInfo(massMailInfo.getApproveUserID());
			mailTitle = "[메일 발송 승인 요청] - "+massMailInfo.getMassmailTitle();
			mailContent = "썬더메일에  발송 승인을 요청하는 메일이 있습니다. <br>"
				         + "■ 승인 요청자 : "+sender.getUserName()+" <br><br>"
				         + webAddress;
			senderMail = sender.getEmail();
		}// 메일 발송 승인
		else if(saveState.equals("11")){
			receiver = massMailService.getUserInfo(massMailInfo.getUserID());
			mailTitle = "[메일 발송 승인] - "+massMailInfo.getMassmailTitle();
			mailContent = "썬더메일에서 작성하신 메일 '"+massMailInfo.getMassmailTitle()+"'이 발송 승인 처리되었습니다. <br>"
						+ "로그인 하셔서 발송 현황을 확인 하시기 바랍니다. <br><br>"
						+ webAddress;
		}// 메일 발송 반려
		else if(saveState.equals("00")){
			receiver = massMailService.getUserInfo(massMailInfo.getUserID());
			mailTitle = "[메일 발송 반려] - "+massMailInfo.getMassmailTitle();
			mailContent = "썬더메일에서 작성하신 메일 '"+massMailInfo.getMassmailTitle()+"'이 반려 되었습니다. <br>"
						+ "로그인 하셔서 발송 현황을 확인 하시기 바랍니다. <br><br>"
						+ webAddress;
		}
		
		if(receiver.getEmail() != null && !receiver.getEmail().equals("") ){
			SystemNotify[] systemNotifys = new SystemNotify[1];
			systemNotifys[0] = new SystemNotify();
			systemNotifys[0].setNotifyFlag(Constant.SYSTEM_NOTIFY_APPROVE);
			systemNotifys[0].setUserID(senderID);
			systemNotifys[0].setEncodingType(massMailInfo.getEncodingType());
			systemNotifys[0].setSenderMail(senderMail);
			systemNotifys[0].setSenderName(sender.getUserName());
			systemNotifys[0].setReceiverMail(receiver.getEmail());
			systemNotifys[0].setReceiverName(receiver.getUserName());
			systemNotifys[0].setReturnMail(massMailInfo.getReturnMail());
			systemNotifys[0].setMailTitle(mailTitle);
			systemNotifys[0].setMailContent(mailContent);
			try{
				massMailService.insertSystemNotify(systemNotifys);
			} catch( Exception e ) {
			}
		}
	}
	
	/**
	 * <p>타겟리스트 출력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView targetList(HttpServletRequest req, HttpServletResponse res) throws Exception{		

		
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		if (curPage <= 0) curPage = 1;
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
	

		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}
		

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		String sBookMark = ServletUtil.getParamString(req,"sBookMark");
	
		//메뉴아이디 세팅 
		//ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sSearchType", sSearchType);
		searchMap.put("sSearchText", sSearchText);	
		searchMap.put("sBookMark", sBookMark);	
		//String userAuth = LoginInfo.getUserAuth(req); //사용자권한을 가져온다.
		
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);

		//총카운트 		
		int totalCount =massMailService.getTargetingTotalCount(searchMap, userInfo);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));

		
		return new ModelAndView("/pages/massmail/write/massmail_target.jsp?method=targetList","targetingList", massMailService.listTargeting(curPage, iLineCnt, searchMap, userInfo));
	
	}
	

	
	
	/**
	 * <p>대량메일 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		if (curPage <= 0) curPage = 1;
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
	

		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		//검색조건 
		String sendScheduleDateStart = ServletUtil.getParamString(req,"eSendScheduleDateStart");
		String sendScheduleDateEnd = ServletUtil.getParamString(req,"eSendScheduleDateEnd");
		
		
		String sendType = ServletUtil.getParamString(req,"eSendType");
		String massmailGroupID = ServletUtil.getParamString(req,"sMassmailGroupID");
		String groupID = ServletUtil.getParamString(req,"sGroupID");
		
		
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sendScheduleDateStart", sendScheduleDateStart+" 00:00:00");
		searchMap.put("sendScheduleDateEnd", sendScheduleDateEnd+" 23:59:59");		
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);	
		searchMap.put("sendType", sendType);
		searchMap.put("massmailGroupID", massmailGroupID);
		searchMap.put("groupID", groupID);

	
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		//총카운트 		
		int totalCount =massMailService.totalCountMassMailList(userInfo, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
				
		return new ModelAndView("/pages/massmail/write/massmail_list_proc.jsp?method=list","massmailWriteList", massMailService.listMassMailList(userInfo, curPage, iLineCnt, searchMap));

	}

	/**
	 * <p>대량메일 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView selfList(HttpServletRequest req, HttpServletResponse res) throws Exception{	
	
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);

				
		return new ModelAndView("/pages/main/sendinfo_proc.jsp?method=list","massmailWriteList", massMailService.listMassMailSelfList(userInfo, 3));

	}
	
	
	/**
	 * <p>메일링크리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listMassMailLink(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req, "eMassmailID", "0");
		
		String state = ServletUtil.getParamString(req, "eState");
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화	
		req.setAttribute("massmailID", Integer.toString(massmailID));		
		req.setAttribute("state", state);
		return new ModelAndView("/pages/massmail/write/massmail_link.jsp?method=listMassMailLink","massmailLinkList", massMailService.listMassMailLink(massmailID));
	}

	
	
	/**
	 * <p>선택된 메일링크 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteLink(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int massmailID = ServletUtil.getParamInt(req, "eMassmailID", "0");
		String[] linkIDs = ServletUtil.getParamStringArray(req, "eLinkID");
		
		if(linkIDs==null || linkIDs.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		int resultVal = massMailService.deleteMailLinkByLinkID(massmailID, linkIDs);
		
		if(resultVal>0){
			this.message = "";
			return listMassMailLink(req,res);
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패 하였습니다.","");		
		}
		
		return null;
	}
	
	
	
	/**
	 * <p>massmailID에 해당되는 모든 schedule에 state 업데이트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateAllScheduleState(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int resultVal = 0;
		int massmailID = ServletUtil.getParamInt(req, "eMassmailID", "0");
		String state = ServletUtil.getParamString(req, "eState");
		MassMailInfo massMailInfo =massMailService.viewMassMailInfo(massmailID, 1);
		
		resultVal = massMailService.updateAllScheduleState(massmailID, state);
		
		if(resultVal>0){
			if (massMailInfo.getApproveYN().equals("Y") || state.equals(Constant.STATE_APPREADY)) {
				sendApproveMail(massMailInfo, Constant.STATE_APPREADY,  LoginInfo.getUserID(req));
			}
			this.message = "";
			return listMassMailLink(req,res);	
		
		}else{
			ServletUtil.messageGoURL(res,"저장에 실패했습니다","");
			return null;	
		}	
	}
	
	
	/**
	 * <p>테스트메일 발송결과테이블 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listTestMailResult(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		if (curPage <= 0) curPage = 1;
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
	
		String userID = LoginInfo.getUserID(req);		
		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		//검색조건 
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 

		searchMap.put("sSearchType", sSearchType);
		searchMap.put("sSearchText", sSearchText);	

		
		//총카운트 		
		int totalCount =massMailService.totalCountNotify(Constant.SYSTEM_NOTIFY_TESTER, userID, curPage, iLineCnt, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));

		return new ModelAndView("/pages/massmail/write/massmail_testmail_sendresult.jsp?method=listTestMailResult","systemNotifyList", massMailService.listSystemNotify(Constant.SYSTEM_NOTIFY_TESTER,userID, curPage, iLineCnt, searchMap));

	}
	

	/**
	 * <p>타게팅에 해당되는 onetoone리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listOnetoone(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String[] targetIDs = ServletUtil.getParamString(req, "targetIDs").split("\\^");
		
		return new ModelAndView("/pages/massmail/write/massmail_onetoone.jsp?method=listOnetoone","onetooneList", massMailService.selectOnetooneByTargetID(targetIDs));		
		
		
	}
	
	/**
	 * <p>템플릿 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listMailTemplate(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String userID = LoginInfo.getUserID(req);
		String groupID = LoginInfo.getGroupID(req);
		String userAuth = LoginInfo.getUserAuth(req);
		String templateType = ServletUtil.getParamString(req, "templateType");
		
		return new ModelAndView("/pages/massmail/write/massmail_template.jsp?method=listMailTemplate","mailTemplateList", massMailService.listMailTemplate(userID, groupID, userAuth, templateType));		
		
		
	}
	
	
	/**
	 * <p>메일템플릿 보기 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewMailTemplate(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int templateID = ServletUtil.getParamInt(req, "eTemplateID", "0");
		return new ModelAndView("/pages/massmail/write/massmail_template.jsp?method=viewMailTemplate","mailTemplate", massMailService.viewMailTemplate(templateID));
		
	}
	
	
	/**
	 * <p>메일템플릿의 사용카운트 증가 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateUsedCount(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int templateID = ServletUtil.getParamInt(req, "eTemplateID", "0");
		massMailService.updateUsedCountTemplate(templateID);		
		return null;	
	}
	
	
	
	/**
	 * <p>발송된 테스트메일 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteSystemoNotify(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] notifyIDs = ServletUtil.getParamStringArray(req, "eNotifyID");
		int resultVal = massMailService.deleteSystemNotify(notifyIDs);
		
		if(resultVal>0){
			this.sCurPage = "1"; 
			return listTestMailResult(req,res);		
		
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}	
	}
	
	/**
	 * <p>발송된 테스트메일 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteTesterEmail(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] testerEmails = ServletUtil.getParamStringArray(req, "eTesterEmail");
		String userID = LoginInfo.getUserID(req);
		int resultVal = massMailService.deleteTesterEmail(userID, testerEmails);
		
		if(resultVal>0){
			this.sCurPage = "1"; 
			return testList(req,res);		
		
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}	
	}
	
	
	/**
	 * <p>파일업로드 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void uploadFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
		
		String fileKey = mreq.getParameter("uploadKey");
		if(fileKey == null) {
			throw new Exception("추가시 uploadKey를 파라미터로 반드시 전달 해야됨");
		}

		FileUpload fileUpload = null;
		
		Iterator fileNameIterator = mreq.getFileNames();		
		while(fileNameIterator.hasNext()){
			MultipartFile multiFile = mreq.getFile((String)fileNameIterator.next());			
			if(multiFile.getSize()>0){
				fileUpload = FileUploadUtil.uploadNewFile(mreq, multiFile, this.realUploadPath, fileKey);
				fileUpload.setUploadKey(fileKey);			
			}
		}
	}
	
	/**
	 * <p>파일업로드 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void uploadImage(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
		
		String fileKey = mreq.getParameter("uploadKey");
		
		
		if(fileKey == null) {
			throw new Exception("추가시 uploadKey를 파라미터로 반드시 전달 해야됨");
		}

		FileUpload fileUpload = null;
		
		Iterator fileNameIterator = mreq.getFileNames();		
		while(fileNameIterator.hasNext()){
			MultipartFile multiFile = mreq.getFile((String)fileNameIterator.next());
			if(multiFile.getSize()>0){
				fileUpload = FileUploadUtil.uploadNewFile(mreq, multiFile, this.realUploadPath, fileKey);
				fileUpload.setUploadKey(fileKey);			
			}
		}
	}
	
	/**
	 * <p>파일삭제 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void deleteFile(String[] fileKeys) throws Exception{		
		//String[] fileKeys = ServletUtil.getParamStringArray(req, "eFileKey");
		for(String fileKey : fileKeys){
			FileUploadUtil.deleteFile(fileKey, this.realUploadPath);
		}
	}
	
	/**
	 * <p>파일을 다운로드 한다.
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public void fileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception{		
	
		String fileKey = ServletUtil.getParamString(req, "fileKey");

		AttachedFile attachedFile = massMailService.getAttachedFile(fileKey);
		
		if(attachedFile != null && attachedFile.getFileKey()!=null && !attachedFile.getFileKey().equals("")) {
			FileUploadUtil.downloadFile(req, res, this.realUploadPath, attachedFile.getFileKey(), attachedFile.getFileName());
		}
		else{
			PrintWriter out = res.getWriter();	
			out.println("<script language=javascript>");			
			out.println("location.href='/tm/pages/response/down.jsp'");
			out.println("</script>");
		}
			
	}
	
	
	/**
	 * <p>등록된 첨부파일 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteAttachedFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
	
		String[] fileKeys = ServletUtil.getParamStringArray(req, "eFileKey");
		int resultVal = massMailService.deleteAttachedFile(fileKeys);
		if(resultVal>0){
			deleteFile(fileKeys);
			return listAttachedFile(req,res);		
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}
		
	}
	
	/**
	 * <p>첨부파일 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listAttachedFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String userID = LoginInfo.getUserID(req);
		return new ModelAndView("/pages/massmail/write/massmail_file.jsp?method=listAttachedFile","attachedFileList", massMailService.listAttachedFile(userID));		

	}
	
	
	/**
	 * <p>업로드된 파일을 인서트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertAttachedFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		AttachedFile attachedFile = new AttachedFile();
		String fileName = ServletUtil.getParamString(req, "eFileName");
		String fileKey = ServletUtil.getParamString(req, "eFileKey");
	
		String fileSize = ServletUtil.getParamString(req, "eFileSize");
		String fileSizeByte = ServletUtil.getParamString(req, "eFileSizeByte");
		String userID = LoginInfo.getUserID(req);
		
		String domainName = massMailService.getConfigValue(Constant.CONFIG_FLAG_MASSMAIL, "domainName");
		String webPORT = massMailService.getConfigValue(Constant.CONFIG_FLAG_MASSMAIL, "webPORT");
		
		
		if(domainName==null || domainName.equals("") || webPORT==null || webPORT.equals("") ){
			ServletUtil.messageGoURL(res,"파일 경로가 잘못되었습니다.(환경설정, 프로퍼티파일 체크요망) 기술담당자에게 문의하시기 바랍니다.","");
			return null;	
		}
		String filePath = "http://"+domainName+":"+webPORT+"/massmail/write/massmail.do?method=fileDownload&fileKey="+fileKey;
		
		attachedFile.setFileName(fileName);
		attachedFile.setFileKey(fileKey);
		attachedFile.setFileSize(fileSize);
		attachedFile.setUserID(userID);
		attachedFile.setFilePath(filePath);
		attachedFile.setFileSizeByte(fileSizeByte);
				
		int resultVal = massMailService.insertAttachedFile(attachedFile);
		if(resultVal>0){
			this.sCurPage = "1"; 
			return listAttachedFile(req,res);		
		
		}else{
			ServletUtil.messageGoURL(res,"등록에 실패했습니다","");
			return null;	
		}	
	}
	
	/**
	 * <p>이미지 업로드 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listImageFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		String userID = LoginInfo.getUserID(req);
		return new ModelAndView("/pages/massmail/write/massmail_image.jsp?method=listImageFile","imageFileList", massMailService.listImageFile(userID));		

	}
	
	/**
	 * <p>업로드된 이미지를 인서트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertImageFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		ImageFile imageFile = new ImageFile();
		String fileName = ServletUtil.getParamString(req, "eFileName");
		String fileKey = ServletUtil.getParamString(req, "eFileKey");
	
		String fileSize = ServletUtil.getParamString(req, "eFileSize");
		String userID = LoginInfo.getUserID(req);
		
		String domainName = massMailService.getConfigValue(Constant.CONFIG_FLAG_MASSMAIL, "domainName");
		String webPORT = massMailService.getConfigValue(Constant.CONFIG_FLAG_MASSMAIL, "webPORT");
		
		
		if(domainName==null || domainName.equals("") || webPORT==null || webPORT.equals("") ){
			ServletUtil.messageGoURL(res,"파일 경로가 잘못되었습니다.(환경설정, 프로퍼티파일 체크요망) 기술담당자에게 문의하시기 바랍니다.","");
			return null;	
		}
		
		//같은 이름의 이미지 파일이 있는지 확인해서, 있다면 파일명에 (1)붙인다 
		int t = 0;
		while(massMailService.isExistImageFile(fileName, LoginInfo.getUserID(req)).size()>0)
		{
			
			fileName = fileName.replace(".", ":");
			String[] s = fileName.split(":");
			String temp = s[0];
			if( s[0].endsWith("("+t+")")== true)
			{
				temp = s[0].substring(0, s[0].lastIndexOf("("+t+")"));
			}
			t++;
			s[0] = temp + "("+t+")";
			fileName = s[0] + "." + s[1];
			
		}
						
		String filePath = "http://"+domainName+":"+webPORT+"/upload/massmail/"+fileKey;
		
		imageFile.setFileName(fileName);
		imageFile.setFileKey(fileKey);
		imageFile.setFileSize(fileSize);
		imageFile.setUserID(userID);
		imageFile.setFilePath(filePath);
		
		int resultVal = massMailService.insertImageFile(imageFile);
		
		if(resultVal>0){
			this.sCurPage = "1"; 
			return listImageFile(req,res);		
		
		}else{
			ServletUtil.messageGoURL(res,"등록에 실패했습니다","");
			return null;	
		}	
	}
	
	/**
	 * <p>등록된 이미지파일 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteImageFile(HttpServletRequest req, HttpServletResponse res) throws Exception{		
	
		String[] fileKeys = ServletUtil.getParamStringArray(req, "eFileKey");
		int resultVal = massMailService.deleteImageFile(fileKeys);
		if(resultVal>0){
			deleteFile(fileKeys);
			return listImageFile(req,res);		
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}
		
	}
	

	/**
	 * <p>대량메일 리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listRepeat(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		if (curPage <= 0) curPage = 1;
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
	

		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		//검색조건 
		String sendScheduleDateStart = ServletUtil.getParamString(req,"eSendScheduleDateStart");
		String sendScheduleDateEnd = ServletUtil.getParamString(req,"eSendScheduleDateEnd");
		
		
		String repeatSendType = ServletUtil.getParamString(req,"eRepeatSendType");
		
		String sSearchType = ServletUtil.getParamString(req,"sSearchType");
		String sSearchText = ServletUtil.getParamString(req,"sSearchText");
		
		//메뉴아이디 세팅 
		ServletUtil.meunParamSetting(req);
		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sendScheduleDateStart", sendScheduleDateStart+" 00:00:00");
		searchMap.put("sendScheduleDateEnd", sendScheduleDateEnd+" 23:59:59");		
		searchMap.put("searchType", sSearchType);
		searchMap.put("searchText", sSearchText);	
		searchMap.put("repeatSendType", repeatSendType);

	
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		//총카운트 		
		int totalCount =massMailService.totalCountMassMailRepeat(userInfo, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
				
		return new ModelAndView("/pages/massmail/write/massmail_repeat_list_proc.jsp?method=listRepeat","massmailRepeatList", massMailService.listMassMailRepeat(userInfo, curPage, iLineCnt, searchMap));

	}
	
	
	/**
	 * <p>반복메일 스케즐리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listRepeatSchedule(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
				
		int massmailID =  ServletUtil.getParamInt(req,"massmailID","0");
		int curPage = ServletUtil.getParamInt(req,"curPage","1");
		if(this.sCurPage != null) {
			curPage = Integer.parseInt(this.sCurPage);
			this.sCurPage = null; // 다음 호출을 위해 초기화
		}
		if (curPage <= 0) curPage = 1;
		
		//int countPerPage = ServletUtil.getParamInt(req,"countPerPage","10");
		int rowHeight =  ServletUtil.getCookieValue( req, "gecko" ).equals("Y") ? 31: 38;
		int iLineCnt = 20;	// 라인 갯수
	

		try {
			iLineCnt = (Integer.parseInt(ServletUtil.getParamStringDefault(req,"listHeight","0")) / rowHeight); // 그리드의 크기
			if(iLineCnt>2) iLineCnt --;
		} catch( Exception e ) {
		}

		req.setAttribute("curPage", Integer.toString(curPage));
		req.setAttribute("iLineCnt",Integer.toString(iLineCnt));
		req.setAttribute("message", this.message);
		this.message = ""; // 다음 호출을 위해 초기화
		
		//검색조건 
		String sendScheduleDateStart = ServletUtil.getParamString(req,"eSendScheduleDateStart");
		String sendScheduleDateEnd = ServletUtil.getParamString(req,"eSendScheduleDateEnd");

		
		//페이지조건과 검색조건을 모두 map에 담아 사용하자
		Map<String, String> searchMap = new HashMap<String, String>(); 
		searchMap.put("sendScheduleDateStart", sendScheduleDateStart+" 00:00:00");
		searchMap.put("sendScheduleDateEnd", sendScheduleDateEnd+" 23:59:59");		


		
		//총카운트 		
		int totalCount =massMailService.totalCountRepeatSchedule(massmailID, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));

				
		return new ModelAndView("/pages/massmail/write/massmail_repeat_edit.jsp?method=listRepeatSchedule","massmailRepeatSchedule", massMailService.listRepeatSchedule(massmailID, curPage, iLineCnt, searchMap));

	}
	
	
	/**
	 * <p>반복메일정보보기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editRepeat(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req,"massmailID","0");	
		
		return new ModelAndView("/pages/massmail/write/massmail_repeat_edit.jsp?method=editRepeat","massmailInfo", massMailService.viewRepeatMassmailInfo(massmailID));
	}

	
	/**
	 * <p>체크된 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 */
	public ModelAndView deleteRepeatScheduleByChecked(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int massmailID = ServletUtil.getParamInt(req,"eMassmailID","0");
		String[] scheduleIDs = ServletUtil.getParamStringArray(req, "eScheduleID");
		
		int resultVal = massMailService.deleteRepeatScheduleByChecked(massmailID, scheduleIDs);
		if(resultVal>0){
			return null;
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}	
	}
	
	
	/**
	 * <p>기간내 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 */
	public ModelAndView deleteRepeatScheduleByDate(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int massmailID = ServletUtil.getParamInt(req,"eMassmailID","0");
		String fromDate = ServletUtil.getParamString(req,"eSendScheduleDateStart");
		String toDate = ServletUtil.getParamString(req,"eSendScheduleDateEnd");
		
		int resultVal = massMailService.deleteRepeatScheduleByDate(massmailID, fromDate+" 00:00:00", toDate+" 23:59:59");
		if(resultVal>0){
			return null;
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}	
	}
	
	/**
	 * <p>메일삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteMassMailAll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req,"eMassmailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"eScheduleID","0");

		if(massMailService.getMassMailState(massmailID, scheduleID).equals("14")){
			ServletUtil.messageGoURL(res,"삭제할 수 없는 상태의 대량메일이 존재 합니다.","");
			return null;
		}

		boolean resultVal = true;		
		
		try{
			massMailService.deleteMassMailAll(massmailID, scheduleID);
		}catch(Exception e){
			resultVal = false;
			logger.error(e);
		}
		if(resultVal==true){
		
			return null;			
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}			
		
	}
	
	/**
	 * <p>메일선택삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteSelectMassMailAll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] massmail_ids =  req.getParameterValues("eMassMailID");
		String failIds= null;
		boolean resultVal = true;
				
		if(massmail_ids==null || massmail_ids.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		for(int i=0;i<massmail_ids.length;i++){
			int massmailID = Integer.parseInt(StringUtil.getNthString(massmail_ids[i], "-", 1));
			int scheduleID = Integer.parseInt(StringUtil.getNthString(massmail_ids[i], "-", 2));
			
			if(massMailService.getMassMailState(massmailID, scheduleID).equals("14")){
				ServletUtil.messageGoURL(res,"삭제할 수 없는 상태의 대량메일이 존재 합니다.","");
				return null;
			}
			
		}
		
		for(int i=0;i<massmail_ids.length;i++){
			int massmailID = Integer.parseInt(StringUtil.getNthString(massmail_ids[i], "-", 1));
			int scheduleID = Integer.parseInt(StringUtil.getNthString(massmail_ids[i], "-", 2));
			//String sendType = StringUtil.getNthString(massmail_ids[i], "-", 3);
		
			try{				
				massMailService.deleteMassMailAll(massmailID, scheduleID);
			}catch(Exception e){
				resultVal = false;
				failIds += massmailID+" ";
				logger.error(e);
			}
		}
		if(resultVal==true){
			this.sCurPage = "1"; 
			return list(req,res);				
		}else{
			ServletUtil.messageGoURL(res, "대량메일 ID "+failIds+"의  삭제에 실패했습니다","");
			return null;	
		}	

	}
	/**
	 * <p>메일 링크타입 수정 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateMassMailLinkType(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int massmailID = ServletUtil.getParamInt(req,"eMassmailID","0");
		int linkID = ServletUtil.getParamInt(req,"linkID","0");
		String linkType =  ServletUtil.getParamString(req, "linkType");
		int resultVal =  massMailService.updateMassMailLinkType(massmailID, linkID, linkType);
		if(resultVal>0){
			this.message = "";
			return listMassMailLink(req,res);
		}else{
			ServletUtil.messageGoURL(res,"수정에 실패 하였습니다.","");		
		}
		
		return null;
		
		
	}
	
	/**
	 * <p>반복메일정보보기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editAddTarget(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String target_ids = ServletUtil.getParamString(req, "target_ids");
		
		return new ModelAndView("/pages/massmail/write/massmail_write.jsp?method=addtarget","targetGroupList", massMailService.listTargetingGroup(target_ids));
	}
	
	

	/**
	 * <p>상태값 변경 업데이트  
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView changeSendState(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int massmailID = ServletUtil.getParamInt(req,"massmailID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		String  state = ServletUtil.getParamString(req, "state");
		String returnMail = massMailService.getConfigValue(Constant.CONFIG_FLAG_MASSMAIL, "returnMail");
		String senderMail = massMailService.getConfigValue(Constant.CONFIG_FLAG_MASSMAIL, "senderMail");
		
		boolean approveMailSend = false;
		
		String dbState = massMailService.getMassMailState(massmailID, scheduleID);
		
		if(state.equals("32") || state.equals("33")){
			if(dbState.equals("14")){
				ServletUtil.messageGoURL(res,"발송중인 상태의 대량메일 입니다.","");
				return null;
			}else if(dbState.equals("15")){
				ServletUtil.messageGoURL(res,"발송완료 된 상태의 대량메일 입니다.","");
				return null;
			}
		}
		
		MassMailInfo massMailInfo = massMailService.viewRepeatMassmailInfo(massmailID); // info 테이블 userID , massMailTitle 추출
		
		MassMailInfo massMailInfo2 = getMassMailInfo(req); // 승인 / 반려 처리 완료 메일 발송용 MassMailInfo 셋팅
		massMailInfo2.setUserID(massMailInfo.getUserID());
		massMailInfo2.setReturnMail(returnMail);
		massMailInfo2.setSenderMail(senderMail);
		massMailInfo2.setMassmailTitle(massMailInfo.getMassmailTitle());
		
		if(state.equals("Y")){
			state = "11";
			approveMailSend = true;
		}
		if(state.equals("N")){
			state = "00";
			approveMailSend = true;
		}
		int resultVal = massMailService.updateSendState(massmailID, scheduleID, state);
		
		if(approveMailSend){
			sendApproveMail(massMailInfo2, state, LoginInfo.getUserID(req)); // tm_system_notify 에 승인 완료 메일 데이터 인서트
		}
		
		if(resultVal>0){
			this.message = "";
		}else{
			ServletUtil.messageGoURL(res,"상태값 변경에 실패 하였습니다.","");		
		}
		return null;
	}
	
	
	
	/**
	 * <p>메일 검증 결과 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView checkMailResult(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		
		String mailContent = ServletUtil.getParamString(req,"eContent");
		String title = ServletUtil.getParamString(req,"eTitle");
					
		List<FilterManager> filterManagerList = massMailService.getFilter();
		
		ContentFilter cf = new ContentFilter(mailContent, title);
		List<FilterInfo> filterInfolist = cf.getContentExtract( filterManagerList);	
		
		return new ModelAndView("/pages/massmail/write/massmail_check_result.jsp?method=listCheck","filterInfoList", filterInfolist);
		 
	}
	
	/**
	 * <p>발송할 대량메일 리스트 - 우선순위 관리창에서 사용 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getMassMailList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		this.message = ""; // 다음 호출을 위해 초기화
		return new ModelAndView("/pages/massmail/write/massmail_priority.jsp?method=list","readyMassmailList", massMailService.getMassMailList());
	}
	
	/**
	 * <p>우선순위 저장 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public ModelAndView savePriority(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] massmailIDs =  req.getParameterValues("eReadyMassmailID");
		String[] scheduleIDs =  req.getParameterValues("eReadyScheduleID");
		String[] prioritys =  req.getParameterValues("ePriority");
		
		Map<String, Object>[] maps = new HashMap[massmailIDs.length];
		for(int i=0;i<massmailIDs.length;i++){
			maps[i] = new HashMap<String, Object>();
			maps[i].put("massmailID", new Integer(massmailIDs[i]));
			maps[i].put("scheduleID", new Integer(scheduleIDs[i]));
			maps[i].put("priority", new Integer(prioritys[i]));
		}
		int[] resultVal = massMailService.updatePriority(maps);
		
		if(resultVal==null){
			ServletUtil.messageGoURL(res,"저장에 실패 하였습니다.","");		
		}
		return new ModelAndView("/pages/massmail/write/massmail_priority.jsp?method=list","readyMassmailList", massMailService.getMassMailList());
	}
	
	/**
	 * <p>주어진 url주소로 접속하여 content를 리턴한다. 
	 * @param webURL
	 * @return
	 */
	private String getWebURLContent(String encodingType, String webURL){		
		String resultStr = "";
		StringBuffer resultStrBuf = new StringBuffer();
		
		URL url = null;
		BufferedReader br = null;
		String lineText = null;
		
		try{
			url = new URL(webURL);					
			br = new BufferedReader(new InputStreamReader(url.openStream(),encodingType));
			while( (lineText=br.readLine() ) != null){
				resultStrBuf.append(lineText+"\r\n");
			}
			
		}catch(Exception e){
			logger.error(e);
			resultStrBuf = null;
		}finally{		
			try{br.close();}catch(Exception e1){}
			url=null;			
		}
		
		if(resultStrBuf!=null){
			resultStr = resultStrBuf.toString();
		}else{
			resultStr = "";
		}
		
		return resultStr;
	}
	
	/**
	 * <p>파일을읽어온다.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	*/
	public ModelAndView fileImport(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String fileKey = ServletUtil.getParamString(req, "eFileKey");
		String fileContent="";
		
		FileInputStream stream = new FileInputStream(this.realUploadPath + File.separator + fileKey);		
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buffer = new BufferedReader(reader);
		
		StringBuffer sb = new StringBuffer();
		String line;
		
		while((line = buffer.readLine()) != null){
			sb.append(line);
			sb.append("\r\n");
		}
		
		stream.close();
		fileContent = sb.toString();
		req.setAttribute("fileContent", fileContent);
		FileUploadUtil.deleteFile(fileKey, this.realUploadPath);
		
		return new ModelAndView("/pages/massmail/write/massmail_fileimport.jsp?method=fileImport");
	}
	
}
