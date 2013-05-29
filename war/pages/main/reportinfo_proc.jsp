<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%@ page import="web.common.util.CheckServerIP" %>
<%@ page import="web.common.util.PropertiesUtil"%>
<%@ page import="web.massmail.statistic.service.MassMailStatService"%>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper"%>
<%@ page import="web.massmail.statistic.model.MassMailStatisticDomain" %>
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%>
<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal" %>
<%
	String sMethod = request.getParameter("method");
	SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(application);
	String cblCheckURL = systemSetservice.getSystemSetInfo("1","cbl");
	String kisaCheckURL = systemSetservice.getSystemSetInfo("1","kisarbl");
	String localServerIP = systemSetservice.getSystemSetInfo("1","sendIP");
	int cbl = CheckServerIP.checkSpamHouse(localServerIP);
	int rbl = cbl;//CheckServerIP.checkKisaRBL(localServerIP);
	String id = request.getParameter("id");
	//****************************************************************************************************/
	// 리스트 
	//****************************************************************************************************/
	if(sMethod.equals("cblrbl")) {
%>
		<div> 
			&nbsp; <img src="images/arrow-right2.gif"> 확인 IP : <%=localServerIP%>
		</div>
		<div>
			&nbsp; <img src="images/arrow-right2.gif"> SPAMHAUS : 
			<a href="<%=cblCheckURL%><%=localServerIP%>" target="_checkresult" title="<%=cblCheckURL%><%=localServerIP%>">
			<%if(cbl == 1){%>	
				이상무
			<%}else if(cbl == 2){ %>
				<font color = "red">SPAMHAUS 등록 </font>
			<%}else{ %>
				<font color = "orange">결과 확인 실패</font>
			<%} %>
			</a>
		</div>
		<div>
			&nbsp; <img src="images/arrow-right2.gif"> KISA : 
			<a href="<%=kisaCheckURL%>=<%=localServerIP%>" target="_checkresult" title="<%=kisaCheckURL%>=<%=localServerIP%>">
			<%if(rbl == 1){%>	
				이상무
			<%}else if(rbl == 2){ %>
				<font color = "red">KISA RBL 등록 </font>
			<%}else{ %>
				<font color = "orange">결과 확인 실패</font>
			<%} %>
			</a>
		</div>
		<hr/>
<%}
if(sMethod.equals("domain")) {
	MassMailStatService service = MassMailStatControlHelper.getUserService(application);
	List<MassMailStatisticDomain> massMailStatisticDomainList = service.massmailStatisticConcernedDomain();
	for(MassMailStatisticDomain massMailStatisticDomain : massMailStatisticDomainList){ %>
		<div>	
				&nbsp; <img src="images/arrow-right2.gif"> <span title="<div style='text-align:left'><strong>Total : </strong><%=massMailStatisticDomain.getSendTotal()%>통, <strong> Success : </strong><%=massMailStatisticDomain.getSuccessTotal() %>통, <strong>Fail : </strong><%=massMailStatisticDomain.getFailTotal() %>통 </div>">  <%=massMailStatisticDomain.getDomainName() %> : <%=massMailStatisticDomain.getSuccessRatio().setScale(1, BigDecimal.ROUND_HALF_UP)%>% </span>	
		</div>
		<%} 
		
		if(massMailStatisticDomainList.size() == 0){%>
		<div >
				&nbsp; <img src="images/arrow-right2.gif"> 발송 이력이 없습니다.
		</div>
		<%} %>
		<div style="margin-top:10px;"><img src="images/tag_blue.png" alt="Tips "> <strong>관심 도메인</strong></div>
		<div style="padding-left:10px;">도메인설정에 등록 되어 있는<br/>도메인을의미합니다.</div>
		<div><img src="images/tag_blue.png" alt="Tips "> <strong>관심 도메인 성공율</strong></div>
		<div style="padding-left:10px;">가장 최근에 발송된 대량메일<br/> 3개의  평균 성공율을 의미합니다.</div>
		
<%}%>
