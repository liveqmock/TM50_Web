<%@page import="com.oreilly.servlet.ServletUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%@ page import="web.intermail.control.InterMailControlHelper" %>
<%@ page import="web.intermail.service.InterMailService" %>
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%> 
<%@ page import="web.intermail.model.*" %>
<%@ page import="java.util.*" %>  
<%

String id = request.getParameter("id");
String method = request.getParameter("method");


//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
	
%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<c:forEach items="${intermailEventList}" var="intermailEvent">	
	<TR id="eIntermail${intermailEvent.intermailID}_${intermailEvent.scheduleID}" class="tbl_tr" tr_intermailID="${intermailEvent.intermailID}" tr_scheduleID="${intermailEvent.scheduleID}" tr_state="${intermailEvent.state}">	
		<TD class="tbl_td">${intermailEvent.intermailID}</TD>					
		<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow('${intermailEvent.intermailID}')">${intermailEvent.intermailTitle}</b></a></TD>
		<TD class="tbl_td">${intermailEvent.userName}</TD>	
		<TD class="tbl_td">${intermailEvent.templateFileName}</TD>	
		<TD class="tbl_td">${intermailEvent.registDate}</TD>
		<TD class="tbl_td">
		<c:if test="${intermailEvent.repeatGroupType == '1'}" >
			사용안함
		</c:if>
		<c:if test="${intermailEvent.repeatGroupType == '2'}" >
			첨부파일
		</c:if>
		<c:if test="${intermailEvent.repeatGroupType == '3'}" >
			메일본문
		</c:if>
		<c:if test="${intermailEvent.repeatGroupType == '4'}" >
			모두사용
		</c:if>
		
		</TD>
		<TD class="tbl_td">		
		<c:if test="${intermailEvent.sendType == '1'}" >
		<a href="javascript:$('<%=id%>').showSend('${intermailEvent.intermailID}','${intermailEvent.scheduleID}','${intermailEvent.state}')">수동발송</a>
		</c:if>
		<c:if test="${intermailEvent.sendType == '2'}" >
		<a href="javascript:$('<%=id%>').showSend('${intermailEvent.intermailID}','${intermailEvent.scheduleID}','${intermailEvent.state}')">자동발송</a>
		</c:if>		
		</TD>					
		<TD class="tbl_td">
			<a href="javascript:$('<%=id%>').remoteControl('<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>')">
			<div id="intermailState<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>">
				<c:if test="${intermailEvent.state == '00'}" >
					<img src="images/massmail/ready.gif" title="발송대기중"/>
				</c:if>					
				<c:if test="${intermailEvent.state == '10'}" >
					<img src="images/massmail/ready.gif" title="발송준비중"/>
				</c:if>				
				<c:if test="${intermailEvent.state == '11'}" >
					<img src="images/massmail/ready.gif" title="발송준비완료 <br>(발송대기)"/>
				</c:if>	
				<c:if test="${intermailEvent.state == '12'}" >
					<img src="images/massmail/sending.gif" title="발송중"/>
				</c:if>		
				<c:if test="${intermailEvent.state == '13'}" >
					<img src="images/massmail/finish.gif" title="발송완료"/>
				</c:if>		
				<c:if test="${intermailEvent.state == '14'}" >
					<img src="images/massmail/approve.gif" title="발송승인대기중"/>
				</c:if>			
				<c:if test="${intermailEvent.state == '15'}" >
					<img src="images/massmail/sending.gif" title="오류자발송중"/>
				</c:if>							
				<c:if test="${intermailEvent.state == '20'}" >
					<img src="images/massmail/pause.gif" title="발송대기중일시정지 "/>
				</c:if>								
				<c:if test="${intermailEvent.state == '21'}" >
					<img src="images/massmail/pause.gif" title="발송중일시정지 "/>
				</c:if>					
				<c:if test="${intermailEvent.state == '22'}" >
					<img src="images/massmail/stop.gif" title="발송정지 "/>
				</c:if>
				<c:if test="${intermailEvent.state == '40'}" >
					<img src="images/massmail/error.gif" title="발송준비중에러"/>
				</c:if>		
				<c:if test="${intermailEvent.state == '41'}" >
					<img src="images/massmail/error.gif" title="발송중에러"/>
				</c:if>	
				<c:if test="${intermailEvent.state == '42'}" >
					<img src="images/massmail/error.gif" title="오류자재발송중에러"/>
				</c:if>											
			</div>	
			</a>
			<input type="hidden" id="<%=id %>state<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="<%=id %>state<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" value="<c:out value="${intermailEvent.state}"/>" />			
			<div class="remoteControl_wrapper">
				<div id="<%=id %>remoteControl<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="remoteControl<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>"class="remoteControl" style="z-index:150;display:none">
					<h6>상태변경 <div class="btnClose"><a href="javascript:$('<%=id%>').remoteControlClose('<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>')"><img src="images/remote_btn_close.gif" title="리모컨 닫기" width="15" height="14"></a></div></h6>
					<div class="remoteBox">
						<!-- 재시작 :발송대기중(상태값 : 10) -->
						<div class="remoteBtn" id="<%=id %>10<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="<%=id %>20<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('10','<c:out value="${intermailEvent.intermailID}"/>','<c:out value="${intermailEvent.scheduleID}"/>')">재시작</a>
						</div>		
						<!-- 일시정지(상태값 : 20) -->
						<div class="remoteBtn" id="<%=id %>20<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="<%=id %>20<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('20','<c:out value="${intermailEvent.intermailID}"/>','<c:out value="${intermailEvent.scheduleID}"/>')">대기중일시정지</a>
						</div>						
						<!-- 일시정지(상태값 : 21) -->
						<div class="remoteBtn" id="<%=id %>21<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="<%=id %>21<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('21','<c:out value="${intermailEvent.intermailID}"/>','<c:out value="${intermailEvent.scheduleID}"/>')">발송중일시정지</a>
						</div>		
						<!-- 정지 (상태값 : 22)-->
						<div class="remoteBtn" id="<%=id %>22<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="<%=id %>22<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('22','<c:out value="${intermailEvent.intermailID}"/>','<c:out value="${intermailEvent.scheduleID}"/>')">발송정지</a>
						</div>
						<!-- 재발송 (상태값 : 12 : 발송중으로) - 일시정지 / 발송중오류시 -->
						<div class="remoteBtn" id="<%=id %>12<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="<%=id %>12<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('12','<c:out value="${intermailEvent.intermailID}"/>','<c:out value="${intermailEvent.scheduleID}"/>')">재시작</a>
						</div>
						<!-- 재발송 (상태값 : 42 : 오류자 재 발송중으로) - 일시정지 / 발송중오류시 -->
						<div class="remoteBtn" id="<%=id %>42<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="<%=id %>42<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('42','<c:out value="${intermailEvent.intermailID}"/>','<c:out value="${intermailEvent.scheduleID}"/>')">재시작</a>
						</div>
						<!-- 재발송 (상태값 : 40 : 발송준비대기중으로)- 발송준비중 오류 -->
						<div class="remoteBtn" id="<%=id %>40<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="<%=id %>40<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>">
							<a href="javascript:$('<%=id%>').changeSendState('40','<c:out value="${intermailEvent.intermailID}"/>','<c:out value="${intermailEvent.scheduleID}"/>')">재시작</a>
						</div>						
						<div id="<%=id %>not<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>" name="<%=id %>not<c:out value="${intermailEvent.intermailID}"/>_<c:out value="${intermailEvent.scheduleID}"/>">
							변경 할 수  없는<br />
							상태 입니다.
						</div>
						
					</div>
				</div>
			</div>
												
		
		</TD>
		<TD class="tbl_td" align="center">
			<a href="javascript:$('<%=id%>').showStatistic('${intermailEvent.intermailID}','${intermailEvent.scheduleID}','${intermailEvent.resultYearMonth}')"><img src="images/chart_bar.png" title="통계보기 ">
		</TD>
	</TR>
	</c:forEach>
	
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	
	
	<script type="text/javascript">

		window.addEvent('domready', function(){


			// 페이징 표시
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				,url: 'pages/common/pagingStr.jsp'
				,update: $('<%=id%>_paging') // 완료후 content가 랜더링될 element
			});
			nemoRequest.get({
				'id':'<%=id%>', 
				'curPage': '<%=curPage%>', 
				'iLineCnt': '<%=iLineCnt%>', 
				'iTotalCnt': '<%=iTotalCnt%>', 
				'iPageCnt': '10' 
			});
			
		
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			
		});
		
		/***********************************************/
		/* 발송준비된 내역보기 (수동발송일 경우)
		/***********************************************/
		$('<%=id%>').showSend = function(intermailID, scheduleID, state){
			
			if(state=='00' || state=='10'){
				alert("아직 발송준비가 완료되지 않았습니다.");
				return;
			}
			
			
			nemoWindow(
					{
						'id': '<%=id%>_approve',

						busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
						width: 850,						
						height: 600,
						title: '발송전대기리스트',
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'pages/intermail/write/intermail_approve.jsp?id=<%=id%>_approve&preID=<%=id%>&method=listInterMailSend&intermailID='+intermailID+'&scheduleID='+scheduleID
					}
				);
		}
		

	</script>
<%
}
//****************************************************************************************************/
//  편집 
//****************************************************************************************************/
if(method.equals("edit")) {
	

	String flag = ServletUtil.getParamString(request,"flag");
	
	String intermailID = request.getParameter("intermailID")==null? "0":request.getParameter("intermailID");
	
	InterMailService service = InterMailControlHelper.getUserService(application);	
	List<InterMailSchedule> intermailScheduleList = service.selectInterMailSchedule(Integer.parseInt(intermailID));
	
	String senderMail = "";
	String senderName = "";
	String receiverName = "";
	String returnMail = "";
	String sendType = "1";
	String secretYN = "N";
	String repeatGroupType = "1";
	
	//입력창이라면 
	if(intermailID.equals("0")){
		SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(application);
		senderMail = systemSetservice.getSystemSetInfo("3","senderMail");
		senderName = systemSetservice.getSystemSetInfo("3","senderName");
		returnMail = systemSetservice.getSystemSetInfo("3","returnMail");
		receiverName = systemSetservice.getSystemSetInfo("3","receiverName");
		sendType = systemSetservice.getSystemSetInfo("3","sendType");
		secretYN = systemSetservice.getSystemSetInfo("3","secretYN");
		repeatGroupType = systemSetservice.getSystemSetInfo("3","repeatGroupType");
	}
	
	
	
	
	
	
	
%>	
	<div style="margin-bottom:10px;width:98%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
		<input type="hidden" id="eTemplateContent" name="eTemplateContent">		
		<input type="hidden" id="eFileName" name="eFileName" value="<c:out value="${intermailEvent.templateFileName}"/>">
		<input type="hidden" id="eFileKey" name="eFileKey" value="<c:out value="${intermailEvent.fileKey}"/>">
		<input type="hidden" id="eIntermailID" name="eIntermailID" value="<c:out value="${intermailEvent.intermailID}"/>" />
		<input type="hidden" id="eFlag" name="eFlag" value="<%=flag %>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">연동메일명</td>
				<td class="ctbl td"><input type="text" id="eIntermailTitle" name="eIntermailTitle" value="${intermailEvent.intermailTitle}" size="50" mustInput="Y" msg="연동메일명을  입력"/></td>
				<td class="ctbl ttd1" width="100px">설명</td>
				<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="${intermailEvent.description}" size="50" /></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>		
			<tr>				
				<td class="ctbl ttd1 mustinput" width="100px">인코딩 타입 </td>
				<td class="ctbl td" >
					<select id="eEncodingType" name="eEncodingType">
						<option value="EUC-KR" <c:if test="${intermailEvent.encodingType=='EUC-KR'}">select='YES'</c:if>>한국어(EUC-KR)</option>
						<option value="UTF-8" <c:if test="${intermailEvent.encodingType=='UTF-8'}">select='YES'</c:if>>다국어(UTF-8)</option>						
					</select>
				</td>	
				<td class="ctbl ttd1 mustinput" width="100px">옵션</td>
				<td class="ctbl td" >				
				<div style="float:left">				
					<% if(intermailID.equals("0")){ %>
					<select id="eSecretYN" name="eSecretYN">
					<option value="N" <% if(secretYN.equals("N")){ %> selected <%} %>>보안메일 미사용</option>
					<option value="Y" <% if(secretYN.equals("Y")){ %> selected <%} %>>보안메일 사용</option>
					</select>
					<%}else{%>
					<select id="eSecretYN" name="eSecretYN">
					<option value="N" <c:if test="${intermailEvent.secretYN=='N'}">select='YES'</c:if>>보안메일 미사용</option>
					<option value="Y"  <c:if test="${intermailEvent.secretYN=='Y'}">select='YES'</c:if>>보안메일 사용</option>
					</select>				
					<%} %>
				</div>				
				</td>								
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<%
				//첫입력, 혹은 복사본이라면 
				if(intermailID.equals("0") || flag.equals("copy")){			
			%>
			<input type="hidden" id="eScheduleID" name="eScheduleID" value="1" />
			<tr>				
				<td class="ctbl ttd1 mustinput" width="100px">발송설정</td>
				<td class="ctbl td" >
				<% if(intermailID.equals("0")){ %>
					<select id="eSendType_1" name="eSendType_1" onchange="javascript:$('<%=id%>').changeSendType('1',this.value)">						
						<option value="1"  <% if(sendType.equals("1")){ %> selected <%} %>>수동발송</option>
						<option value="2" <% if(sendType.equals("2")){ %> selected <%} %>>자동발송</option>						
					</select>
				<%}else{ %>
					<select id="eSendType_1" name="eSendType_1" onchange="javascript:$('<%=id%>').changeSendType('1',this.value)">						
						<option value="1" <c:if test="${intermailEvent.sendType=='1'}">select='YES'</c:if>>수동발송</option>
						<option value="2" <c:if test="${intermailEvent.sendType=='2'}">select='YES'</c:if>>자동발송</option>						
					</select>
				<%} %>	
				</td>	
				<td class="ctbl ttd1 mustinput" width="100px">발송일</td>
				<td class="ctbl td" >
				<div style="float:left">
				<fmt:parseDate value="${intermailSchedule.sendScheduleDate}"  var ="fmt_sendScheduleDate" pattern="yyyy-MM-dd"/>									
				<c:if test="${intermailSchedule.sendScheduleDate!=null}">	
					<input type="text" id="eSendSchedule_1" name="eSendSchedule_1" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_sendScheduleDate}" pattern="yyyy-MM-dd"/>"/>
				</c:if>
				<c:if test="${intermailSchedule.sendScheduleDate==null}">
					<input type="text" id="eSendSchedule_1" name="eSendSchedule_1" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getDateString() %>"/>
				</c:if>									
					<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eSendSchedule_1)" align="absmiddle" />
				</div>
				<div style="float:left;padding-left:5px">
				<select id="eSendScheduleHH_1" name="eSendScheduleHH_1">
				<c:forEach var="hours" begin="0" end="23" step="1"> 
				<option value="<c:out value="${hours}"/>"><c:out value="${hours}"/></option>
				</c:forEach>
				</select>								
				</div>
				<div style="float:left;padding-top:3px;padding-left:3px">시</div>						
				<div style="float:left;padding-left:5px">
				<select id="eSendScheduleMM_1" name="eSendScheduleMM_1">
				<c:forEach var="minutes" begin="0" end="59" step="10"> 
				<option value="<c:out value="${minutes}"/>"><c:out value="${minutes}"/></option>
				</c:forEach>
				</select>							
				</div>		
				<div style="float:left;padding-top:3px;padding-left:3px">분</div>						
				</td>								
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>				
				<td class="ctbl ttd1 mustinput" width="100px">통계설정</td>
				<td class="ctbl td" >
				<input type="checkbox" id="eStatisticCHK_1" name="eStatisticCHK_1" checked>통계마감일 설정			
				</td>	
				<td class="ctbl ttd1 mustinput" width="100px">통계마감일</td>
				<td class="ctbl td" >
				<div style="float:left">
				<fmt:parseDate value="${intermailSchedule.sendScheduleDate}"  var ="fmt_sendScheduleDate" pattern="yyyy-MM-dd"/>									
				<c:if test="${intermailSchedule.sendScheduleDate!=null}">	
					<input type="text" id="eStatisticSchedule_1" name="eStatisticSchedule_1" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_sendScheduleDate}" pattern="yyyy-MM-dd"/>"/>
				</c:if>
				<c:if test="${intermailSchedule.sendScheduleDate==null}">
					<input type="text" id="eStatisticSchedule_1" name="eStatisticSchedule_1" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getNowAddShortDate(7) %>"/>
				</c:if>									
					<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eStatisticSchedule_1)" align="absmiddle" />
				</div>
				<div style="float:left;padding-left:5px">
				<select id="eStatisticScheduleHH_1" name="eStatisticScheduleHH_1">
				<c:forEach var="hours" begin="0" end="23" step="1"> 
				<option value="<c:out value="${hours}"/>"><c:out value="${hours}"/></option>
				</c:forEach>
				</select>								
				</div>
				<div style="float:left;padding-top:3px;padding-left:3px">시</div>						
				<div style="float:left;padding-left:5px">
				<select id="eStatisticScheduleMM_1" name="eStatisticScheduleMM_1">
				<c:forEach var="minutes" begin="0" end="59" step="10"> 
				<option value="<c:out value="${minutes}"/>"><c:out value="${minutes}"/></option>
				</c:forEach>
				</select>							
				</div>		
				<div style="float:left;padding-top:3px;padding-left:3px">분</div>						
				</td>								
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
											
			<%}else{ %>
			
			<c:forEach items="${interMailScheduleList}" var="intermailSchedule">
			<input type="hidden" id="eScheduleID" name="eScheduleID" value="${intermailSchedule.scheduleID}" />
			<tr>				
				<td class="ctbl ttd1 mustinput" width="100px">발송설정</td>
				<td class="ctbl td" >
					<select id="eSendType_${intermailSchedule.scheduleID}" name="eSendType_${intermailSchedule.scheduleID}" onchange="javascript:$('<%=id%>').changeSendType('${intermailSchedule.scheduleID}',this.value)">
						<option value="1" <c:if test="${intermailSchedule.sendType=='1'}"> selected </c:if>>수동발송</option>
						<option value="2" <c:if test="${intermailSchedule.sendType=='2'}"> selected </c:if>>자동발송</option>						
					</select>
				</td>	
				<td class="ctbl ttd1 mustinput" width="100px">발송일</td>
				<td class="ctbl td" >
				<div style="float:left">
				<fmt:parseDate value="${intermailSchedule.sendScheduleDate}"  var ="fmt_sendScheduleDate" pattern="yyyy-MM-dd"/>									
				<c:if test="${intermailSchedule.sendScheduleDate!=null}">	
					<input type="text" id="eSendSchedule_${intermailSchedule.scheduleID}" name="eSendSchedule_${intermailSchedule.scheduleID}" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_sendScheduleDate}" pattern="yyyy-MM-dd"/>"/>
				</c:if>
				<c:if test="${intermailSchedule.sendScheduleDate==null}">
					<input type="text" id="eSendSchedule_${intermailSchedule.scheduleID}" name="eSendSchedule_${intermailSchedule.scheduleID}" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getDateString() %>"/>
				</c:if>									
					<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eSendSchedule_${intermailSchedule.scheduleID})" align="absmiddle" />
				</div>
				<div style="float:left;padding-left:5px">
				<select id="eSendScheduleHH_${intermailSchedule.scheduleID}" name="eSendScheduleHH_${intermailSchedule.scheduleID}">
				<c:forEach var="hours" begin="0" end="23" step="1"> 
				<option value="<c:out value="${hours}"/>" <c:if test="${intermailSchedule.sendScheduleDateHH==hours}"> selected </c:if>><c:out value="${hours}"/></option>
				</c:forEach>
				</select>								
				</div>
				<div style="float:left;padding-top:3px;padding-left:3px">시</div>						
				<div style="float:left;padding-left:5px">
				<select id="eSendScheduleMM_${intermailSchedule.scheduleID}" name="eSendScheduleMM_${intermailSchedule.scheduleID}">
				<c:forEach var="minutes" begin="0" end="59" step="10"> 
				<option value="<c:out value="${minutes}"/>" <c:if test="${intermailSchedule.sendScheduleDateMM==minutes}"> selected </c:if>><c:out value="${minutes}"/></option>
				</c:forEach>
				</select>							
				</div>		
				<div style="float:left;padding-top:3px;padding-left:3px">분</div>						
				</td>								
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>		
			<tr>				
				<td class="ctbl ttd1 mustinput" width="100px">통계설정</td>
				<td class="ctbl td" >
				<input type="checkbox" id="eStatisticCHK_${intermailSchedule.scheduleID}" name="eStatisticCHK_${intermailSchedule.scheduleID}">통계마감일 설정			
				</td>	
				<td class="ctbl ttd1 mustinput" width="100px">통계마감일</td>
				<td class="ctbl td" >
				<div style="float:left">
				<fmt:parseDate value="${intermailSchedule.statisticEndDate}"  var ="fmt_statisticEndDate" pattern="yyyy-MM-dd"/>									
				<c:if test="${intermailSchedule.statisticEndDate!=null}">	
					<input type="text" id="eStatisticSchedule_${intermailSchedule.scheduleID}" name="eStatisticSchedule_${intermailSchedule.scheduleID}" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_statisticEndDate}" pattern="yyyy-MM-dd"/>"/>
				</c:if>
				<c:if test="${intermailSchedule.statisticEndDate==null}">
					<input type="text" id="eStatisticSchedule_${intermailSchedule.scheduleID}" name="eStatisticSchedule_${intermailSchedule.scheduleID}" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getDateString() %>"/>
				</c:if>									
					<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eSendSchedule)" align="absmiddle" />
				</div>
				<div style="float:left;padding-left:5px">
				<select id="eStatisticScheduleHH_${intermailSchedule.scheduleID}" name="eStatisticScheduleHH_${intermailSchedule.scheduleID}">
				<c:forEach var="hours" begin="0" end="23" step="1"> 
				<option value="<c:out value="${hours}"/>" <c:if test="${intermailSchedule.statisticEndDateHH==hours}"> selected </c:if>><c:out value="${hours}"/></option>
				</c:forEach>
				</select>								
				</div>
				<div style="float:left;padding-top:3px;padding-left:3px">시</div>						
				<div style="float:left;padding-left:5px">
				<select id="eStatisticScheduleMM_${intermailSchedule.scheduleID}" name="eStatisticScheduleMM_${intermailSchedule.scheduleID}">
				<c:forEach var="minutes" begin="0" end="59" step="10"> 
				<option value="<c:out value="${minutes}"/>" <c:if test="${intermailSchedule.statisticEndDateMM==minutes}"> selected </c:if>><c:out value="${minutes}"/></option>
				</c:forEach>
				</select>							
				</div>		
				<div style="float:left;padding-top:3px;padding-left:3px">분</div>						
				</td>								
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>				
			<script type="text/javascript">
				$('<%=id%>').changeSendType(${intermailSchedule.scheduleID},${intermailSchedule.sendType});
				
			</script>
			
								
			</c:forEach>
			<%} %>
			
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">반송 메일</td>
				<td class="ctbl td">
				<% if(intermailID.equals("0")){ %>
					<input type="text" id="eReturnMail" name="eReturnMail" value="<%=returnMail %>" size="50" mustInput="Y" msg="반송메일을   입력"/>
				<%}else{ %>
					<input type="text" id="eReturnMail" name="eReturnMail" value="<c:out value="${intermailEvent.returnMail}"/>" size="50" mustInput="Y" msg="반송메일을   입력"/>
				<%} %>
				</td>
				<td class="ctbl ttd1" width="100px">받는 사람명</td>
				<td class="ctbl td">
				<% if(intermailID.equals("0")){ %>
					<input type="text" id="eTemplateReceiverName" name="eTemplateReceiverName" value="<%=receiverName %>" size="50" />
				<%}else{ %>
					<input type="text" id="eTemplateReceiverName" name="eTemplateReceiverName" value="<c:out value="${intermailEvent.templateReceiverName}"/>" size="50" />
				<%} %>
				</td>
				
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">보내는 메일</td>
				<td class="ctbl td">
				<% if(intermailID.equals("0")){ %>
					<input type="text" id="eTemplateSenderMail" name="eTemplateSenderMail" value="<%=senderMail %>" size="50" />
				<%}else{ %>
					<input type="text" id="eTemplateSenderMail" name="eTemplateSenderMail" value="<c:out value="${intermailEvent.templateSenderMail}"/>" size="50" />
				<%} %>
				</td>
				<td class="ctbl ttd1" width="100px">보내는 사람명 </td>
				<td class="ctbl td">
				<% if(intermailID.equals("0")){ %>
				 	<input type="text" id="eTemplateSenderName" name="eTemplateSenderName" value="<%=senderName %>" size="50" />
				<%}else{ %>
					<input type="text" id="eTemplateSenderName" name="eTemplateSenderName" value="<c:out value="${intermailEvent.templateSenderName}"/>" size="50" />
				<%} %>				
				</td>
			</tr>					
			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">메일제목</td>
				<td class="ctbl td" colspan="3"><input type="text" id="eTemplateTitle" name="eTemplateTitle" value="<c:out value="${intermailEvent.templateTitle}"/>" size="100"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>								
			<tr>
				<td class="ctbl ttd1" width="100px">첨부파일 </td>
				<td class="ctbl td" colspan="3">
				<div style="float:left" id="<%=id%>uploadWrapper" ></div>
					<div style="float:left;padding:22px">
						<a id="<%=id%>_uploadBtn" href="javascript:$('<%=id%>').uploadFile()" class="web20button bigblue" >파일 업로드</a>
					</div>		
				</td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			 <tr>
				<td class="ctbl ttd1" width="100px">업로드된 첨부파일</td>
				<td class="ctbl td" style="padding:5px">
				<div  id="<%=id%>uploaded" >
				<div style="float:left">
					<a href="intermail/intermail.do?method=fileDownload&uploadKey=<c:out value="${intermailEvent.fileKey}"/>" target="_blank">
					<c:out value="${intermailEvent.templateFileName}"/>
					</a>
				</div>				
				</div>
				</td>
				<td class="ctbl ttd1" width="100px">메일본문 </td>
				<td class="ctbl td">
					<div class="btn_green"><a id="mailTemplateSelect" style ="cursor:pointer" href="javascript:$('<%=id%>').templateWindow(3)"><span>메일템플릿열기</span></a></div>
				</td>
			</tr>		
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			 <tr>
				<td class="ctbl ttd1" width="100px">반복치환사용유무</td>
				<td class="ctbl td" style="padding:3px" colspan="3">
				<% if(intermailID.equals("0")){ %>
					<select id="eRepeatGroupType" name="eRepeatGroupType">
						<option value="1" <% if(repeatGroupType.equals("1")){ %> selected <%} %>>모두사용안함</option>
						<option value="2" <% if(repeatGroupType.equals("2")){ %> selected <%} %>>첨부파일에만 사용</option>
						<option value="3" <% if(repeatGroupType.equals("3")){ %> selected <%} %>>메일본문에만 사용</option>
						<option value="4" <% if(repeatGroupType.equals("4")){ %> selected <%} %>>모두사용(메일본문,첨부파일)</option>
					</select>
				<%}else{ %>
					<select id="eRepeatGroupType" name="eRepeatGroupType">
						<option value="1" <c:if test="${intermailEvent.repeatGroupType=='1'}"> selected </c:if>>모두사용안함</option>
						<option value="2" <c:if test="${intermailEvent.repeatGroupType=='2'}"> selected </c:if>>첨부파일에만 사용</option>
						<option value="3" <c:if test="${intermailEvent.repeatGroupType=='3'}"> selected </c:if>>메일본문에만 사용</option>
						<option value="4" <c:if test="${intermailEvent.repeatGroupType=='4'}"> selected </c:if>>모두사용(메일본문,첨부파일)</option>
					</select>
				<%} %>	
					<b><font color="red">(주의)반드시 용도에 맞게 정확히 선택해야 합니다.</font></b>
				</td>
			</tr>									
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>							
			<tr>				
				<td class="ctbl ttd1" width="100px">HTML</td>
				<td class="ctbl td" colspan="3">
				<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_intermailwrite_iframe.jsp?intermailID=<c:out value="${intermailEvent.intermailID}"/>" height=400  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
				</td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			</tbody>
			</table>
		</form>
	</div>
	<div style="margin-bottom:10px;width:98%">
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<c:if test="${intermailEvent.intermailID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${intermailEvent.intermailID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData()" class="web20button bigblue">저 장</a></div>
	</div>
	
	<script type="text/javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));		
		$('<%=id%>').renderUpload();		
		
	</script>

<%
}
%>


<%
//****************************************************************************************************/
// 업로드가 완료되면 파일 정보 불러오기 
//****************************************************************************************************/
if(method.equals("getFileInfo")) {
%>
	<div style="float:left">
		<a href="intermail/intermail.do?method=fileDownload&uploadKey=<c:out value="${fileUpload.uploadKey}"/>"><img src="images/trees/csv_file.gif" />
		<c:out value="${fileUpload.realFileName}"/></a>
	</div>
	
<%
}
%>		