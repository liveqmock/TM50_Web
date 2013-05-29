package web.masssms.write.control;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import web.common.util.*;
import web.masssms.write.service.MassSMSService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import web.masssms.write.model.*;
import web.admin.usergroup.model.User;


public class MassSMSController extends MultiActionController{
	

	private MassSMSService massSMSService= null;
	private String sCurPage = null;
	private String message = "";
	private Logger logger = Logger.getLogger("TM");
	
	
	public void setMassSMSService(MassSMSService massSMSService){		
		this.massSMSService = massSMSService;
	}
	
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		
		//ModelAndView 파라미터1: jsp 경로, 파라미터2: List 객체, 파라미터3 : service 메소드	
		String step = ServletUtil.getParamString(req, "step");
		String pageURL = "";
		if(step.equals("write")){
			pageURL = "/pages/masssms/write/masssms_write.jsp";
		}else if(step.equals("list")){
			pageURL =  "/pages/masssms/write/masssms_list.jsp";
		}else if(step.equals("listRepeat")){
			pageURL =  "/pages/masssms/write/masssms_repeat_list.jsp";
		}
		return new ModelAndView(pageURL);
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
		return new ModelAndView("/pages/masssms/write/masssms_testsms.jsp?method=testList","testeSMSList", massSMSService.listTesterHp(userID,null));

	}
	
	
	/**
	 * <p>대량메일 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */

	public ModelAndView insert(HttpServletRequest req, HttpServletResponse res) throws Exception{	 
		
		int masssmsID = 0;
		
		MassSMSInfo massSMSInfo =getMassSMSInfo(req);
		
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
		String auth_send_sms = LoginInfo.getAuth_send_sms(req);
		
		massSMSInfo.setUserID(userID);
		
	
		if(!saveState.equals(Constant.SMS_STATE_WRITE)){
			if(!isAdmin.equals("Y") && auth_send_sms.equals("N")){			
				saveState = Constant.SMS_STATE_APPREADY;
			}else if(isAdmin.equals("Y") || auth_send_sms.equals("Y")){			
				saveState= Constant.SMS_STATE_PREPARE_READY;
			}
		}
				
		ArrayList<String> sendScheduleDatList =null; //발송일 
			
		
		//즉시발송일 경우 
		if(massSMSInfo.getSendType().equals(Constant.SEND_TYPE_NOW)){
			
			sendScheduleDatList = new ArrayList<String>();
			sendScheduleDatList.add(DateUtils.getDateTimeString());
	
		//예약발송일 경우 
		}else if(massSMSInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE)){
			sendScheduleDatList = new ArrayList<String>();
			if(!sendSchedule.equals("") && !sendScheduleHH.equals("") && !sendScheduleMM.equals("")){
				sendScheduleDatList.add(sendSchedule+" "+sendScheduleHH+":"+sendScheduleMM+":00");
			}
			
		//반복발송일 경우에는 	반복발송시작일과 종료일에 해당되는 발송개수가 ez_massmail_schedule에 입력된다. 
		}else if(massSMSInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){
			
			//매일반복일 경우 
			if(massSMSInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_1)){
				sendScheduleDatList = DateUtils.getDateDifferDay(massSMSInfo.getRepeatSendStartDate(), massSMSInfo.getRepeatSendEndDate());				
				
			//매주 특정요일 반복발송일 경우 	
			}else if(massSMSInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_2)){
				String[] weeks =  massSMSInfo.getRepeatSendWeek().split(":");
				sendScheduleDatList = DateUtils.getDateDifferWeek(massSMSInfo.getRepeatSendStartDate(), massSMSInfo.getRepeatSendEndDate(), weeks);
				
			//매월 특정일 반복발송일 경우 	
			}else if(massSMSInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_3)){
				sendScheduleDatList = DateUtils.getDateDifferMonth(massSMSInfo.getRepeatSendStartDate(), massSMSInfo.getRepeatSendEndDate(), massSMSInfo.getRepeatSendDay());
			}
		}
		
		
		int resultVal =0;

		if(targetIDs==null || targetIDs.length==0){
			ServletUtil.messageGoURL(res,"대상자그룹이 존재하지 않습니다.","");
			return null;	
		}
		
		synchronized(this){
			
			//1. 기본정보입력 
			if(massSMSService.insertMassSMSInfo(massSMSInfo)>0){
				
			
				//2. 입력된 masssmsID 가져오기 
				masssmsID = massSMSService.getMassSMSIDInfo();
												
				if(masssmsID>0){
					massSMSInfo.setMasssmsID(masssmsID);
					
					MassSMSSchedule[] massSMSSchedules = new MassSMSSchedule[sendScheduleDatList.size()];		
					
					String statisticsEndDate = ""; //통계마감일 
					
					for(int i=0;i<sendScheduleDatList.size();i++){						
						
						massSMSSchedules[i]= new MassSMSSchedule();
						massSMSSchedules[i].setMasssmsID(masssmsID);
						massSMSSchedules[i].setScheduleID(i+1);
						massSMSSchedules[i].setSendScheduleDate(sendScheduleDatList.get(i));
						
						//SMS는 통계수집을 1일까지만 하도록 하자.						
						statisticsEndDate = DateUtils.getDateAddDays(sendScheduleDatList.get(i), 1);
						
						massSMSSchedules[i].setStatisticsEndDate(statisticsEndDate);
						massSMSSchedules[i].setState(saveState);
					
						
						//만약 임시저장이라면 반복발송일 경우에는 더이상 입력될 필요가 없다. 
						if(saveState.equals(Constant.STATE_WRITE)){
							break;
						}

					}
					
					try{
						//나머지 정보 일괄입력 
						massSMSService.insertMassSMS(massSMSInfo, targetIDs, exceptYNs, massSMSSchedules);
						resultVal = 1;
					}catch(Exception e){
						resultVal = -1;
						logger.error(e);	
					}
					
					//실패라면 입력된 모든 데이타 삭제 
					if(resultVal<0){ 
						try{
							massSMSService.deleteMassSMS(masssmsID);
						}catch(Exception e){
							logger.error(e);	
						}
					}
				

				}// end 	if(masssmsID>0)

			}
						
		}// end synchronized
		
	

		if(resultVal>0){
			if(saveState.equals(Constant.STATE_APPREADY)){
				sendApproveMail(massSMSInfo, Constant.STATE_APPREADY,  LoginInfo.getUserID(req));
			}
			this.sCurPage = "1"; 
			 res.getWriter().print(masssmsID+"|"+saveState);
			//return list(req,res);
			 return null;
			
		
		}else{
			res.getWriter().print("0");
			ServletUtil.messageGoURL(res,"저장에 실패했습니다.","");			 
			
			return null;	
		}	
	}
	
	
	/**
	 * <p>대량메일 입력 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */

	public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception{	 
	
		MassSMSInfo massSMSInfo =getMassSMSInfo(req);
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
			massSMSInfo.setApproveYN("Y");
			if(saveState.equals("N")){
				saveState = Constant.STATE_WRITE;
			}
		}
			
		String isAdmin = LoginInfo.getIsAdmin(req);
		String auth_send_sms = LoginInfo.getAuth_send_sms(req);
		
		massSMSInfo.setUserID(userID);
		if(!saveState.equals(Constant.STATE_WRITE)){
			if(!isAdmin.equals("Y") && auth_send_sms.equals("N")){
				saveState = Constant.STATE_APPREADY;
			}else if(isAdmin.equals("Y") || auth_send_sms.equals("Y")){
				saveState= Constant.STATE_PREPARE_READY;
			}
		}
	
	
		ArrayList<String> sendScheduleDatList =null; //발송일  
		
		
		//즉시발송일 경우 
		if(massSMSInfo.getSendType().equals(Constant.SEND_TYPE_NOW)){
			
			sendScheduleDatList = new ArrayList<String>();
			sendScheduleDatList.add(DateUtils.getDateTimeString());
			
		
		//예약발송일 경우 
		}else if(massSMSInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE)){
			sendScheduleDatList = new ArrayList<String>();
			if(!sendSchedule.equals("") && !sendScheduleHH.equals("") && !sendScheduleMM.equals("")){
				sendScheduleDatList.add(sendSchedule+" "+sendScheduleHH+":"+sendScheduleMM+":00");
			}
			
		//반복발송일 경우에는 	반복발송시작일과 종료일에 해당되는 발송개수가 ez_massmail_schedule에 입력된다. 
		}else if(massSMSInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){
			
			//매일반복일 경우 
			if(massSMSInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_1)){
				sendScheduleDatList = DateUtils.getDateDifferDay(massSMSInfo.getRepeatSendStartDate(), massSMSInfo.getRepeatSendEndDate());				
				
			//매주 특정요일 반복발송일 경우 	
			}else if(massSMSInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_2)){
				String[] weeks =  massSMSInfo.getRepeatSendWeek().split(":");
				sendScheduleDatList = DateUtils.getDateDifferWeek(massSMSInfo.getRepeatSendStartDate(), massSMSInfo.getRepeatSendEndDate(), weeks);
				
			//매월 특정일 반복발송일 경우 	
			}else if(massSMSInfo.getRepeatSendType().equals(Constant.REPEAT_SEND_TYPE_3)){
				sendScheduleDatList = DateUtils.getDateDifferMonth(massSMSInfo.getRepeatSendStartDate(), massSMSInfo.getRepeatSendEndDate(), massSMSInfo.getRepeatSendDay());
			}
		}
		
		
		int resultVal =0;

		if(targetIDs==null || targetIDs.length==0){
			ServletUtil.messageGoURL(res,"대상자그룹이 존재하지 않습니다.","");
			return null;	
		}
		
		int pMasssmsID = ServletUtil.getParamInt(req,"eMasssmsID","0");
		int pScheduleID = ServletUtil.getParamInt(req,"eScheduleID","0");
		
		massSMSInfo.setMasssmsID(pMasssmsID);
		massSMSInfo.setScheduleID(pScheduleID);
		
		synchronized(this){
			
			
			//1. 기본정보수정 
			massSMSService.updateMassSMSInfo(massSMSInfo);
			
			//2. 스케줄수정 
			MassSMSSchedule[] massSMSSchedules = new MassSMSSchedule[sendScheduleDatList.size()];
			String statisticsEndDate = ""; //통계마감일 
			
			for(int i=0;i<sendScheduleDatList.size();i++){
				massSMSSchedules[i]= new MassSMSSchedule();
				massSMSSchedules[i].setMasssmsID(massSMSInfo.getMasssmsID());
				massSMSSchedules[i].setScheduleID(i+1);
				massSMSSchedules[i].setSendScheduleDate(sendScheduleDatList.get(i));
				
				//SMS는 통계수집을 1일까지만 하도록 하자.
				statisticsEndDate = DateUtils.getDateAddDays(sendScheduleDatList.get(i), 1);
				massSMSSchedules[i].setStatisticsEndDate(statisticsEndDate); 
				massSMSSchedules[i].setState(saveState);
				
				
				//만약 임시저장이라면 반복발송일 경우에는 더이상 입력될 필요가 없다. 
				if(saveState.equals(Constant.STATE_WRITE)){
					break;
				}			

			}			
		
			
			try{
				//스케줄 삭제후 나머지 정보 일괄입력 
				massSMSService.updateMassSMS(massSMSInfo, targetIDs, exceptYNs, massSMSSchedules);
				resultVal = 1;
				
			}catch(Exception e){
				logger.error(e);
			}
			
		}//end sychronzied 
		
	

		if(resultVal>0){
			if(massSMSInfo.getApproveYN().equals("Y")){
				sendApproveMail(massSMSInfo, saveState, LoginInfo.getUserID(req));
			}
			this.sCurPage = "1"; 
			 res.getWriter().print(pMasssmsID+"|"+saveState);			
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
	public void sendApproveMail(MassSMSInfo massSMSInfo, String saveState, String senderID) throws Exception{
		User sender= massSMSService.getUserInfo(senderID);
		if(sender.getEmail() == null || sender.getEmail().equals("")){
			return;
		}
		User receiver = null;
		String mailTitle = "";
		String mailContent = "";
		//승인 요청
		if(saveState.equals("10")){
			receiver = massSMSService.getUserInfo(massSMSInfo.getApproveUserID());
			mailTitle = "[대량SMS 발송 승인 요청] - "+massSMSInfo.getMasssmsTitle();
			mailContent = "대량SMS 발송 승인을 요청 합니다. <br>"
				         +"■ 승인 요청자 :" +sender.getUserName()+" <br>";
		}// 메일 발송 승인
		else if(saveState.equals("11")){
			receiver = massSMSService.getUserInfo(massSMSInfo.getUserID());
			mailTitle = "[대량SMS 발송 승인] - "+massSMSInfo.getMasssmsTitle();
			mailContent = "요청하신 "+massSMSInfo.getMasssmsTitle()+"에 대한 승인 처리 요청이 완료 되었습니다. <br>"
						+ "발송 결과를 로그인 하시에 확인 하시기 바랍니다.";
		}// 메일 발송 반려
		else if(saveState.equals("00")){
			receiver = massSMSService.getUserInfo(massSMSInfo.getUserID());
			mailTitle = "[대량SMS 발송 반려] - "+massSMSInfo.getMasssmsTitle();
			mailContent = "요청하신 "+massSMSInfo.getMasssmsTitle()+"에 대한 승인 요청이 반려 되었습니다.";
		}
		if(receiver.getEmail() != null && !receiver.getEmail().equals("") ){
			SystemNotify[] systemNotifys = new SystemNotify[1];
			systemNotifys[0] = new SystemNotify();
			systemNotifys[0].setNotifyFlag(Constant.SYSTEM_NOTIFY_APPROVE);
			systemNotifys[0].setUserID(senderID);
			systemNotifys[0].setEncodingType("EUC-KR");
			systemNotifys[0].setSenderMail(sender.getEmail());
			systemNotifys[0].setSenderName(sender.getUserName());
			systemNotifys[0].setReceiverMail(receiver.getEmail());
			systemNotifys[0].setReceiverName(receiver.getUserName());
			systemNotifys[0].setReturnMail(sender.getEmail());
			systemNotifys[0].setMailTitle(mailTitle);
			systemNotifys[0].setMailContent(mailContent);
			try{
				massSMSService.insertSystemNotify(systemNotifys);
			} catch( Exception e ) {
			}
		}
	}
	
	
	
	/**
	 * <p>파라미터 세팅 
	 * @param req
	 * @return
	 */
	private MassSMSInfo getMassSMSInfo(HttpServletRequest req){
		MassSMSInfo massSMSInfo = new MassSMSInfo();
		
		String masssmsTitle= ServletUtil.getParamString(req,"eMasssmsTitle");
		String description= ServletUtil.getParamString(req,"eDescription");		
		String approveUserID =  ServletUtil.getParamString(req,"eApproveUserID");
		
		
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
		//중복허용 여부
		String duplicationYN  = ServletUtil.getParamString(req,"eDuplicationYN");
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
		
		//발송성공제한
		String sendedTypeSuccess =  ServletUtil.getParamString(req,"eSendedTypeSuccess");
		//발송실패제한 
		String sendedTypeFail =  ServletUtil.getParamString(req,"eSendedTypeFail");
	
		//발송된통수
		int sendedCount  = ServletUtil.getParamInt(req,"eSendedCount","0");
		
		
		//통계공유여부 
		String statisticsOpenType = ServletUtil.getParamString(req,"eStatisticsOpenType");
		//메모 
		String memo =  ServletUtil.getParamString(req,"eMemo");	
		
		
		//컨텐츠
		String senderPhone = ServletUtil.getParamString(req,"eSenderPhone");
		String smsMsg = ServletUtil.getParamString(req,"smsMsg");	
	
	
		
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
				
				massSMSInfo.setRepeatSendWeek(weekdays);				
			//매월반복발송 	
			}else if(repeatSendType.equals(Constant.REPEAT_SEND_TYPE_3)){
				massSMSInfo.setRepeatSendDay(repeatSendDay);
			}
			massSMSInfo.setRepeatSendType(repeatSendType);		
			
			if(Integer.parseInt(repeatSendDateHH)<10) repeatSendDateHH="0"+repeatSendDateHH;
			if(Integer.parseInt(repeatSendDateMM)<10) repeatSendDateMM="0"+repeatSendDateMM;
			massSMSInfo.setRepeatSendStartDate(repeatSendStartDate+" "+repeatSendDateHH+":"+repeatSendDateMM+":00");
			massSMSInfo.setRepeatSendEndDate(repeatSendEndDate+" "+repeatSendDateHH+":"+repeatSendDateMM+":00");		
			
		}		
		
		
		
		massSMSInfo.setMasssmsTitle(masssmsTitle);
		massSMSInfo.setDescription(description);		
		massSMSInfo.setSendType(sendType);		
		massSMSInfo.setRejectType(rejectType);
		massSMSInfo.setDuplicationYN(duplicationYN);
		
		//필터링설정
		//발송성공통수 제한일 경우 
		if(sendedTypeSuccess.equals(Constant.SMS_FILTER_TYPE_SUCCESS) && sendedCount!=0){
			massSMSInfo.setSendedType(Constant.SMS_FILTER_TYPE_SUCCESS);	
		
		//발송실패제한 
		}else if(sendedTypeFail.equals(Constant.SMS_FILTER_TYPE_FAIL) && sendedCount!=0){
			massSMSInfo.setSendedType(Constant.SMS_FILTER_TYPE_FAIL);
			
		//둘다 제한 	
		}else if( (sendedTypeSuccess.equals(Constant.SMS_FILTER_TYPE_SUCCESS) && sendedTypeFail.equals(Constant.SMS_FILTER_TYPE_FAIL)) && sendedCount!=0 ){
			massSMSInfo.setSendedType(Constant.SMS_FILTER_TYPE_ALL);
		}
		massSMSInfo.setSendedCount(sendedCount);	
		
		massSMSInfo.setSendedYear(sendedYear);
		massSMSInfo.setSendedMonth(sendedMonth);
		massSMSInfo.setSendedCount(sendedCount);		
		massSMSInfo.setStatisticsOpenType(statisticsOpenType);
		massSMSInfo.setStatisticsOpenType(statisticsOpenType);
		massSMSInfo.setMemo(memo);		
		massSMSInfo.setSenderPhone(senderPhone);		
		massSMSInfo.setSmsMsg(smsMsg);				
		massSMSInfo.setApproveUserID(approveUserID);
		massSMSInfo.setPriority(priority);
		
		
		
		return massSMSInfo;
	}
	
	
	/**
	 * <p>대량SMS 리스트 
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
		searchMap.put("groupID", groupID);	
		String[] userInfo = new String[3];
		userInfo[0] =  LoginInfo.getUserAuth(req); //사용자권한을 가져온다. 
		userInfo[1] =  LoginInfo.getUserID(req);
		userInfo[2] =  LoginInfo.getGroupID(req);
		
		//총카운트 		
		int totalCount =massSMSService.totalCountMassSMSList(userInfo, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
				
		return new ModelAndView("/pages/masssms/write/masssms_list_proc.jsp?method=list","masssmsWriteList", massSMSService.listMassSMSList(userInfo, curPage, iLineCnt, searchMap));

	}
	
	
	
	/**
	 * <p>대량SMS 작성
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int masssmsID = ServletUtil.getParamInt(req,"masssmsID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		String state = ServletUtil.getParamString(req, "state");
		
	
		req.setAttribute("targetGroupList", massSMSService.listTargetingGroup(masssmsID));
		req.setAttribute("state", state);
		
		return new ModelAndView("/pages/masssms/write/masssms_write.jsp?method=edit","masssmsInfo", massSMSService.viewMassSMSInfo(masssmsID,scheduleID));
	}
	
	
	/**
	 * <p>대량메일 복사
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView copyMassSMS(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int masssmsID = ServletUtil.getParamInt(req,"masssmsID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		
		MassSMSInfo masssmsInfo = massSMSService.viewMassSMSInfo(masssmsID,scheduleID);
		masssmsInfo.setMasssmsID(0);
		req.setAttribute("oldMasssmsID", Integer.toString(masssmsID));
		return new ModelAndView("/pages/masssms/write/masssms_write.jsp","masssmsInfo", masssmsInfo);
	}
	
	
	/**
	 * <p>메일삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteMassSMSAll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int masssmsID = ServletUtil.getParamInt(req,"eMasssmsID","0");
		int scheduleID = ServletUtil.getParamInt(req,"eScheduleID","0");

		boolean resultVal = true;
		
		try{			
			massSMSService.deleteMassSMSAll(masssmsID, scheduleID);
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
	public ModelAndView deleteSelectMassSMSAll(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] masssms_ids =  req.getParameterValues("eMassSMSID");
		String failIds= null;
		boolean resultVal = true;
		
		
		if(masssms_ids==null || masssms_ids.length==0){
			ServletUtil.messageGoURL(res,"[파라미터 오류] 삭제할 파라미터정보가 없습니다.","");
			return null;
		}
		
		for(int i=0;i<masssms_ids.length;i++){
			int masssmsID = Integer.parseInt(StringUtil.getNthString(masssms_ids[i], "-", 1));
			int scheduleID = Integer.parseInt(StringUtil.getNthString(masssms_ids[i], "-", 2));
			//String sendType = StringUtil.getNthString(massmail_ids[i], "-", 3);
		
			try{				
				massSMSService.deleteMassSMSAll(masssmsID, scheduleID);
			}catch(Exception e){
				resultVal = false;
				failIds += masssmsID+" ";
				logger.error(e);
			}
		}
		if(resultVal==true){
			this.sCurPage = "1"; 
			return list(req,res);				
		}else{
			ServletUtil.messageGoURL(res, "대량SMS ID "+failIds+"의  삭제에 실패했습니다","");
			return null;	
		}	

	}
	
	
	/**
	 * <p>상태값 변경 업데이트  
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView changeSendState(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		int masssmsID = ServletUtil.getParamInt(req,"masssmsID","0");
		int scheduleID = ServletUtil.getParamInt(req,"scheduleID","0");
		String  state = ServletUtil.getParamString(req, "state");
		if(state.equals("Y")){
			state = "11";
		}
		if(state.equals("N")){
			state = "00";
		}
		int resultVal = massSMSService.updateSendState(masssmsID, scheduleID, state);
		
		if(resultVal>0){
			this.message = "";
		}else{
			ServletUtil.messageGoURL(res,"상태값 변경에 실패 하였습니다.","");		
		}
		return null;
	}
	
	
	/**
	 * <p>테스트SMS 발송이라면 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView sendTestSMS(HttpServletRequest req, HttpServletResponse res) throws Exception{
		MassSMSInfo massSMSInfo =getMassSMSInfo(req);
		String sendTestSMS =  ServletUtil.getParamString(req,"eSendTestSMS"); //입력된 TEST PHONE번호 
		String userID = LoginInfo.getUserID(req);		
		String[]sendTestEmailArray = sendTestSMS.split(";");
		String[]targetIDs = ServletUtil.getParamStringArray(req, "targetID");
		
		Map<String, Object> map = massSMSService.getQeuryDB(Integer.parseInt(targetIDs[0]));

		
		String query = String.valueOf(map.get("queryText"));	
		String dbType = String.valueOf(map.get("driverType"));
		String dbDriver =  String.valueOf(map.get("driverClass"));
		String dbURL =  String.valueOf(map.get("dbURL"));
		String dbUserID =  String.valueOf(map.get("dbUserID"));
		String dbUserPWD =  String.valueOf(map.get("dbUserPWD"));
	
		String sampleQuery = QueryUtil.makeTopCountQuery(query, dbType, sendTestEmailArray.length);
		//추출된 쿼리를 수행하고 원투원 치환한다. 

		PreparedStatement ps = null;	
		ResultSet rs = null;
		
	
		Connection conn = DBUtil.getConnection(dbDriver, dbURL, dbUserID, dbUserPWD);
		
		if(conn==null){
			logger.error("테스트메일 발송중 :: Connetion null 에러 발생");
			return null;
		}
		
		List<OnetooneTarget> onetooneTargetList = massSMSService.listOnetooneTarget(Integer.parseInt(targetIDs[0]));
		try{			
				
			//등록된 타겟쿼리를 실행한다. 
			ps = conn.prepareStatement(sampleQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();			
			
			SystemNotify[] systemNotifys = new SystemNotify[sendTestEmailArray.length];

			int i = 0;
			while(rs.next()){
				systemNotifys[i] = new SystemNotify();
				
				systemNotifys[i].setUserID(userID);
				systemNotifys[i].setSenderPhone(massSMSInfo.getSenderPhone());				
				systemNotifys[i].setReceiverPhone(sendTestEmailArray[i].replace("-", "")); //위에서 입력된 테스트메일이 입력된다.
				systemNotifys[i].setNotifyFlag(Constant.SYSTEM_NOTIFY_TESTER);
				systemNotifys[i].setNotifyType(Constant.SYSTEM_NOTIFY_TYPE_SMS);				

				String onetooneInfo = "";
				for(OnetooneTarget onetooneTarget : onetooneTargetList){
					onetooneInfo += onetooneTarget.getOnetooneAlias() + Constant.TAG_SPLITER+rs.getString(onetooneTarget.getFieldName())+ Constant.VALUE_SPLITER;
				}
				//SMS내용을 원투원 치환
				systemNotifys[i].setSmsContent(ThunderUtil.replaceOnetoone(onetooneInfo, massSMSInfo.getSmsMsg()));
				i++;
			}
			//시스템공지테이블에 입력한다.
			massSMSService.insertSystemNotify(systemNotifys);
		
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
	 * <p>발송된 테스트메일 삭제 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteSystemoNotify(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String[] notifyIDs = ServletUtil.getParamStringArray(req, "eNotifyID");
		int resultVal = massSMSService.deleteSystemNotify(notifyIDs);
		
		if(resultVal>0){
			this.sCurPage = "1"; 
			return listTestSMSResult(req,res);		
		
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}	
	}
	
	
	/**
	 * <p>테스트SMS 발송결과테이블 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listTestSMSResult(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
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
		int totalCount =massSMSService.totalCountNotify(Constant.SYSTEM_NOTIFY_TESTER, userID, curPage, iLineCnt, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));

		return new ModelAndView("/pages/masssms/write/masssms_testsms_sendresult.jsp?method=listTestSMSResult","systemNotifyList", massSMSService.listSystemNotify(Constant.SYSTEM_NOTIFY_TESTER,userID, curPage, iLineCnt, searchMap));

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
		int totalCount =massSMSService.getTargetingTotalCount(searchMap, userInfo);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));

		
		return new ModelAndView("/pages/masssms/write/masssms_target.jsp?method=targetList","targetingList", massSMSService.listTargeting(curPage, iLineCnt, searchMap, userInfo));
	
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
		int totalCount =massSMSService.totalCountMassSMSRepeat(userInfo, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));
				
		return new ModelAndView("/pages/masssms/write/masssms_repeat_list_proc.jsp?method=listRepeat","masssmsRepeatList", massSMSService.listMassSMSRepeat(userInfo, curPage, iLineCnt, searchMap));

	}
	
	
	/**
	 * <p>반복SMS정보보기
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView editRepeat(HttpServletRequest req, HttpServletResponse res) throws Exception{
		int masssmsID = ServletUtil.getParamInt(req,"masssmsID","0");			
		return new ModelAndView("/pages/masssms/write/masssms_repeat_edit.jsp?method=editRepeat","masssmsInfo", massSMSService.viewRepeatMassSMSInfo(masssmsID));
	}
	
	
	/**
	 * <p>반복메일 스케즐리스트 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView listRepeatSchedule(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
				
		int masssmsID =  ServletUtil.getParamInt(req,"masssmsID","0");
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
		int totalCount =massSMSService.totalCountRepeatSchedule(masssmsID, searchMap);
		req.setAttribute("iTotalCnt", Integer.toString(totalCount));

				
		return new ModelAndView("/pages/masssms/write/masssms_repeat_edit.jsp?method=listRepeatSchedule","masssmsRepeatSchedule", massSMSService.listRepeatSchedule(masssmsID, curPage, iLineCnt, searchMap));

	}
	
	
	/**
	 * <p>체크된 반복메일 스케즐리스트 삭제 
	 * @param massmailID
	 * @param scheduleIDs
	 * @return
	 */
	public ModelAndView deleteRepeatScheduleByChecked(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int masssmsID = ServletUtil.getParamInt(req,"eMasssmsID","0");
		String[] scheduleIDs = ServletUtil.getParamStringArray(req, "eScheduleID");
		
		int resultVal = massSMSService.deleteRepeatScheduleByChecked(masssmsID, scheduleIDs);
		if(resultVal>0){
			this.sCurPage = "1"; 
			return listRepeatSchedule(req,res);			
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
		
		int masssmsID = ServletUtil.getParamInt(req,"eMasssmsID","0");
		String fromDate = ServletUtil.getParamString(req,"eSendScheduleDateStart");
		String toDate = ServletUtil.getParamString(req,"eSendScheduleDateEnd");
		
		int resultVal = massSMSService.deleteRepeatScheduleByDate(masssmsID, fromDate+" 00:00:00", toDate+" 23:59:59");
		if(resultVal>0){
			this.sCurPage = "1"; 
			return listRepeatSchedule(req,res);				
		}else{
			ServletUtil.messageGoURL(res,"삭제에 실패했습니다","");
			return null;	
		}	
	}
	
	public ModelAndView editAddTarget(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String target_ids = ServletUtil.getParamString(req, "target_ids");
		
		return new ModelAndView("/pages/masssms/write/masssms_write.jsp?method=addtarget","targetGroupList", massSMSService.listTargetingGroup(target_ids));
	}
	
	
}
