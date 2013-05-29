package web.response.control;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import web.common.util.*;
import web.response.service.*;
import java.util.Map;


/**
 * <p>고객에게 보낸 메일에 대한 반응정보를 수집처리한다. 
 * @author RoyKim(김유근)
 * @date 2009-02-05
 */
public class ResponseController extends MultiActionController {

	private Logger logger = Logger.getLogger(this.getClass());
	private ResponseService responseService = null;
	private String message = "";
	
	public void setResponseService(ResponseService responseService){
		this.responseService = responseService;
	}
	
	

	
	/**
	 * <p>대량메일 오픈메소드 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void MO29304(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		int sendID = ServletUtil.getParamIntBase64(req,"SND","0");
		int massmailID = ServletUtil.getParamIntBase64(req,"MMD","0");
		int scheduleID = ServletUtil.getParamIntBase64(req,"SDD","0");
				
		res.sendRedirect("/images/response.gif"); 
		
		if(responseService.isExistTable("tm_massmail_openresult_"+massmailID+"_"+scheduleID).size()==0){
			return;
		}
		
		if(sendID>0 && massmailID>0){
			responseService.updateOpenMassMailSendResult(sendID, massmailID, scheduleID);
		}else{
			logger.error("[대량메일오픈메소드]필수값 처리 에러");
		}
		
	}
	
	
	/**
	 * <p>대량메일 클릭메소드 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void MC31549(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		
		
		int sendID = ServletUtil.getParamIntBase64(req,"SND","0");
		int massmailID = ServletUtil.getParamIntBase64(req,"MMD","0");
		int scheduleID = ServletUtil.getParamIntBase64(req,"SDD","0");		
		int linkID = ServletUtil.getParamIntBase64(req,"LKD","0");
		String linkType = ServletUtil.getParamStringBase64(req,"LKT");		//1: 일반링크, 2: 수신거부링크 
		int targetID = ServletUtil.getParamIntBase64(req,"TGD","0");
		String email = ServletUtil.getParamStringBase64(req,"EMA");		
		
		String url = ServletUtil.getParamString(req,"URL");		
		// 링크 URL에 한글 사용시 깨짐 현상 방지
		res.setContentType("text/html;charset=EUC-KR");	
		PrintWriter out = res.getWriter();	
		if(!(responseService.isExistTable("tm_massmail_openresult_"+massmailID+"_"+scheduleID).size()==0)){
			if(sendID>0 && massmailID>0 && linkID>0 && targetID>0 && !email.equals("")){
				if(responseService.insertLinkClick(massmailID, scheduleID, linkID, sendID, email, targetID)>0){  //여러개 중복되서 쌓인다.
					if(linkType.equals(Constant.LINK_TYPE_NORMAL)){  //일반링크 				
						responseService.updateClickMassMailSendResult(sendID, massmailID,scheduleID);
					}else if(linkType.equals(Constant.LINK_TYPE_REJECT)){ //수신거부링크 
						insertMassMailReject(req,res);
					}
				}

			}else{
				logger.error("[대량메일클릭메소드]필수값 처리 에러");
			}
		}
		

		if(linkType.equals(Constant.LINK_TYPE_REJECT) && url.equals("#")){
			out.println("<script language=javascript>");			
			out.println("location.href='/tm/pages/response/reject.jsp?message="+message+"&email="+email+"';");
			out.println("</script>");
			
		}else{
			//원래 링크된 페이지로 이동한다. 
			out.println("<script language=javascript>");
			out.println("location.href='"+url+"';");
			out.println("</script>");
		}
	}
	
	
	/**
	 * <p>수신거부처리 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	private void  insertMassMailReject(HttpServletRequest req, HttpServletResponse res) throws Exception{
	
		int massmailID = ServletUtil.getParamIntBase64(req,"MMD","0");
		int scheduleID = ServletUtil.getParamIntBase64(req,"SDD","0");
		int massmailGroupID = ServletUtil.getParamIntBase64(req,"MGD","0");
		int targetID = ServletUtil.getParamIntBase64(req,"TGD","0");
		String email = ServletUtil.getParamStringBase64(req,"EMA");	
		int sendID = ServletUtil.getParamIntBase64(req,"SND","0");	
		
		if(responseService.isExistTable("tm_massmail_openresult_"+massmailID+"_"+scheduleID).size()==0){
			return;
		}

		
		if(responseService.checkReject(email, massmailID)==0){
			
			Map<String,Object> resultMap = responseService.getSendIDInfo(massmailID, scheduleID, sendID);			
			if(resultMap==null || resultMap.size()==0){
				this.message = "rejectFail";
				return;
			}
			
			String userID = String.valueOf(resultMap.get("userID"));
			String groupID = String.valueOf(resultMap.get("groupID"));
			String customerID = String.valueOf(resultMap.get("customerID"));
		
			//tm_massmail_openresult 테이블에 수신거부 여부 저장
			if(responseService.updateRejectMassMailSendResult(sendID, massmailID, scheduleID)>0)
			{
				//tm_massmail_reject 테이블에 같은 massmailGroupID로 등록된 email이 있는지 확인하고, 없으면 인서트. 있으면 안함 
				if(responseService.insertRejectClick(massmailID, email, targetID, massmailGroupID, userID, groupID, customerID)>0)
					this.message = "rejectSuccess";
				else
					this.message = "rejectAlready";
			}else{
				this.message = "rejectAlready";
			}
		}else{
			this.message = "rejectAlready";
		}
			
		
	}
	
	/**
	 * <p>자동메일 오픈메소드 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void AO73892(HttpServletRequest req, HttpServletResponse res) throws Exception{
				
		res.sendRedirect("/images/response.gif"); 
		
		String sendID = ServletUtil.getParamStringBase64(req,"SND");
		int automailID = ServletUtil.getParamIntBase64(req,"AMD","0");	
		String yearMonth = ServletUtil.getParamStringBase64(req,"YM");
		
				
		if(!sendID.equals("") && automailID>0 && !yearMonth.equals("")){
			responseService.updateOpenAutoMailSendResult(sendID, automailID, yearMonth); //최초한번만 업데이트한다. 
		}else{
			logger.error("[자동메일오픈메소드]필수값 처리 에러");
		}	
		
	}
	
	/**
	 * <p>연동메일 메일오픈메소드 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void IN37585(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String sendID = ServletUtil.getParamStringBase64(req,"SND");
		int intermailID = ServletUtil.getParamIntBase64(req,"IMD","0");
		int scheduleID = ServletUtil.getParamIntBase64(req,"SDD","0");
		String yearMonth = ServletUtil.getParamStringBase64(req, "YMD");
		
		res.sendRedirect("/images/response.gif"); 
		
		if(responseService.isExistTable("tm_intermail_sendresult_"+yearMonth).size()==0){
			return;
		}
		
		if(!sendID.equals("") && intermailID>0 && scheduleID>0 && !yearMonth.equals("") ){
			responseService.updateOpenInterMailSendResult(sendID, intermailID, scheduleID, yearMonth);
		}else{
			logger.error("[연동메일 본문오픈메소드]필수값 처리 에러");
		}
		
	}
	
	/**
	 * <p>연동메일 파일오픈메소드 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void INF2069(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		String sendID = ServletUtil.getParamStringBase64(req,"SND");
		int intermailID = ServletUtil.getParamIntBase64(req,"IMD","0");
		int scheduleID = ServletUtil.getParamIntBase64(req,"SDD","0");
		String yearMonth = ServletUtil.getParamStringBase64(req, "YMD");
		
		res.sendRedirect("/images/response.gif"); 
		
		if(responseService.isExistTable("tm_intermail_sendresult_"+yearMonth).size()==0){
			return;
		}
		
		if(!sendID.equals("") && intermailID>0 && scheduleID>0 && !yearMonth.equals("") ){
			responseService.updateOpenFileInterMailSendResult(sendID, intermailID, scheduleID, yearMonth);
		}else{
			logger.error("[연동메일 첨부파일오픈메소드]필수값 처리 에러");
		}
		
	}

		
}
