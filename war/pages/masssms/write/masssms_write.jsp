<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%@ page import="web.masssms.write.service.MassSMSService" %>    
<%@ page import="web.masssms.write.model.*" %>    
<%@ page import="web.masssms.write.control.MassSMSControlHelper" %>   
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%> 
<%@ page import="java.util.List"%>

<jsp:useBean id="masssmsID" class="java.lang.String" scope="request" />
<jsp:useBean id="oldMasssmsID" class="java.lang.String" scope="request" />
<jsp:useBean id="state" class="java.lang.String" scope="request" />
<jsp:useBean id="masssmsInfo" class="web.masssms.write.model.MassSMSInfo" scope="request" />

<%	
	if(request.getParameter("eSendScheduleDateStart")!=null&&!(request.getParameter("eSendScheduleDateStart").equals("")))
	{	
		
		masssmsInfo.setSendType("2");
		masssmsInfo.setSendScheduleDate(request.getParameter("eSendScheduleDateStart"));
	}
	String id = request.getParameter("id");
		
	String method = request.getParameter("method");
	if(method==null) method="";
	
	//관리자라면 
	String isAdmin = LoginInfo.getIsAdmin(request);
	//발송권한이라면 
	String auth_send_mail = LoginInfo.getAuth_send_mail(request);
	String userAuth = LoginInfo.getUserAuth(request);
	

	
	String sendAuth = "N";
	if(isAdmin.equals("Y") || auth_send_mail.equals("Y")){
		sendAuth = "Y";
	}

	String groupID = LoginInfo.getGroupID(request);
	String userID = LoginInfo.getUserID(request);
	
	MassSMSService service = MassSMSControlHelper.getUserService(application);	
	List<BackupDate> backupDateList = service.getBackupDate();
	
	String senderPhone=LoginInfo.getSenderCellPhone(request);
	SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(application);
	if(senderPhone.equals("")){
		//senderPhone = systemSetservice.getSystemSetInfo("1","senderPhone");
	}

%>

<div class="masssms_wrapper">
	<form id="<%=id%>_form" name="<%=id%>_form" method="post">
	<input type="hidden" id="eSaveState" name="eSaveState">	
	
	<input type="hidden" id="eMasssmsID" name="eMasssmsID" value="<c:out value="${masssmsInfo.masssmsID}"/>">
	<input type="hidden" id="eScheduleID" name="eScheduleID" value="<c:out value="${masssmsInfo.scheduleID}"/>">
	<input type="hidden" id="eUserID" name="eUserID" value="<c:out value="${masssmsInfo.userID}"/>">
	<input type="hidden" id="hSendType" name="hSendType" value="<c:out value="${masssmsInfo.sendType}"/>">
	
	
			<div>
				<table border="0" cellpadding="3" class="ctbl" width="100%">
				<tbody>
				<tr>
					<td class="ctbl ttd1 mustinput" width="100px" align="left">대량SMS명</td>
					<td class="ctbl td"><input type="text" id="eMasssmsTitle" name="eMasssmsTitle" mustInput="Y" msg="대량SMS명 입력" value="<c:out value="${masssmsInfo.masssmsTitle}"/>" size="53" maxlength="50"/></td>
					<td class="ctbl ttd1" width="100px">대량SMS설명</td>
					<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${masssmsInfo.description}"/>" size="53" maxlength="50"/></td>
				</tr>
				<tr>
					<td colspan="4" class="ctbl line"></td>
				</tr>				
				<tr>
					<% if(sendAuth.equals("N")){%>
						<td class="ctbl ttd1 mustinput" width="100px">발송승인담당자</td>
						<td class="ctbl td">					
						<c:import url="../../../include_inc/approve_list_inc.jsp">
						<c:param name="mustInput" value="Y"/>			
						<c:param name="groupID" value="<%=groupID %>"/>	
						<c:param name="approveUserID" value="${masssmsInfo.approveUserID}"/>
						</c:import>				
						</td>
					<% } %>
					
				</tr>		
				<% if(sendAuth.equals("Y")){%>
				<tr style="display:none" >
					<td class="ctbl ttd1 mustinput" width="100px">발송승인담당자</td>
					<td class="ctbl td" colspan="3">					
						<c:import url="../../../include_inc/approve_list_inc.jsp">
						<c:param name="mustInput" value="Y"/>			
						<c:param name="groupID" value="<%=groupID %>"/>	
						<c:param name="approveUserID" value="${masssmsInfo.approveUserID}"/>
						</c:import>				
					</td>
				</tr>
				<% } %>
				</tbody>
				</table>				
			</div>
			<div class="right">
				<div class="btn_r"><a id="testSMSMinus22" style ="cursor:pointer" href="javascript:$('<%=id%>').targetWindow()"><span>대상자추가</span></a></div>
			</div>
			<div class="right">
				<div class="btn_b"><a id="testSMSMinus11" style ="cursor:pointer" href="javascript:$('<%=id%>').removeTargetRow()"><span>선택삭제</span></a></div>
			</div>
			<div style="clear:both">
				<div id="<%=id%>_targetList" class="target">
				<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
				<thead>
					<tr>				
					<th style="height:25px;width:70px">그룹ID</th>
					<th style="height:25px;">대상자그룹명</th>
					<th style="height:25px;width:120px">등록구분</th>
					<th style="height:25px;width:120px">총대상인원</th>
					<th style="height:25px;width:120px">추가제외</th>
					</tr>
				</thead>
				</table>
				<table style="width:100%">
				<thead>
					<tr>
					<th style="height:0px;width:70px"></th>
					<th style="height:0px;"></th>
					<th style="height:0px;width:120px"></th>
					<th style="height:0px;width:120px"></th>					
					<th style="height:0px;width:120px"></th>
					</tr>
				</thead>
				<tbody id="<%=id%>_grid_content_list">
				
<% 
	if(method.equals("edit")){	
%>
			
			<jsp:useBean id="targetGroupList" class="java.lang.Object" scope="request" />
				
			
				<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
					 주석이 없으면 업데이트 되지 않으므로 주의
					 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
				-->
				<c:forEach items="${targetGroupList}" var="targetingGroup">
				<TR class="target_tr" targetList_id='<c:out value="${targetingGroup.targetID}" />'>
					<TD>
					<input type="checkbox" class="notBorder" id="targetIDCheck" name="targetIDCheck" value="<c:out value="${targetingGroup.targetID}" />" />
					<input type="hidden" id="targetID" name="targetID" value="<c:out value="${targetingGroup.targetID}" />" />
					</TD>		
					<TD align="left"><c:out value="${targetingGroup.targetName}" /></TD>			
					<c:if test="${targetingGroup.targetType=='1'}">
					<TD class="tbl_td" align="center">파일업로드</TD>	
					</c:if>
					<c:if test="${targetingGroup.targetType=='2'}">		
					<TD class="tbl_td" align="center">직접입력</TD>	
					</c:if>
					<c:if test="${targetingGroup.targetType=='3'}">		
					<TD class="tbl_td" align="center">DB추출</TD>	
					</c:if>
					<c:if test="${targetingGroup.targetType=='4'}">		
					<TD class="tbl_td" align="center">기존발송추출</TD>	
					</c:if>
					<TD><fmt:formatNumber value="${targetingGroup.targetCount}" type="number"/></TD>	
					<TD>
						<select id="exceptYN" name="exceptYN">
							<option value="N"<c:if test="${targetingGroup.exceptYN=='N'}">selected </c:if>>추가</option>
							<option value="Y"<c:if test="${targetingGroup.exceptYN=='Y'}">selected </c:if>>제외</option>
						</select>
					</TD>
				</TR>
				</c:forEach>
<%	} 
	if(method.equals("addtarget")){
%>
	<c:forEach items="${targetGroupList}" var="targetingGroup">
		<TR class="target_tr" targetList_id='<c:out value="${targetingGroup.targetID}" />'>
			<TD>
				<input type="checkbox" class="notBorder" id="targetIDCheck" name="targetIDCheck" value="<c:out value="${targetingGroup.targetID}" />" />
					<input type="hidden" id="targetID" name="targetID" value="<c:out value="${targetingGroup.targetID}" />" />
					</TD>		
					<TD align="left"><c:out value="${targetingGroup.targetName}" /></TD>			
						<c:if test="${targetingGroup.targetType=='1'}">
						<TD class="tbl_td" align="center">파일업로드</TD>	
						</c:if>
						<c:if test="${targetingGroup.targetType=='2'}">		
						<TD class="tbl_td" align="center">직접입력</TD>	
						</c:if>
						<c:if test="${targetingGroup.targetType=='3'}">		
						<TD class="tbl_td" align="center">DB추출</TD>	
						</c:if>
						<c:if test="${targetingGroup.targetType=='4'}">		
						<TD class="tbl_td" align="center">기존발송추출</TD>	
						</c:if>
					<TD><fmt:formatNumber value="${targetingGroup.targetCount}" type="number"/></TD>	
					<TD>
						<select id="exceptYN" name="exceptYN">
							<option value="N" selected >추가</option>
							<option value="Y" >제외</option>
						</select>
					</TD>
				</TR>
	</c:forEach>
<%} %>
				</tbody>
				</table>
				</div>							
			</div>
	
			<!-- //////////////////////발송버튼///////////////  -->
			<div class="right"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>	
			<%
				//삭제는 예약발송이면서  발송준비대기중,  임시저장, 발송완료, 발송완전중지, 발송준비중오류, 발송중오류 인 경우만 가능 
				if( (isAdmin.equals("Y") || userID.equals(masssmsInfo.getUserID()) || (userAuth.equals("2") && masssmsInfo.getGroupID().equals(groupID)) ) && ((masssmsInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE) && state.equals("11")) || state.equals("00") || state.equals("15") || state.equals("44") || state.equals("32") || state.equals("22") || state.equals("24") || state.equals("33"))){ 
			%>			
				<div class="right"><a href="javascript:$('<%=id%>').deleteData(<c:out value="${masssmsInfo.masssmsID}"/>,<c:out value="${masssmsInfo.scheduleID}"/>)" class="web20button bigpink">삭 제</a></div>
			<%}%>
			<% if( (isAdmin.equals("Y") || userID.equals(masssmsInfo.getUserID()) || (userAuth.equals("2") && masssmsInfo.getGroupID().equals(groupID))) && ((masssmsInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE) && state.equals("11")))){ %>
				<div class="right"><a href="javascript:$('<%=id%>').saveData('<c:out value="${masssmsInfo.masssmsID}"/>','10')" class="web20button bigpink">수정</a></div>	
			<%} %>
			<% if(state.equals("") || (state.equals("00") && (isAdmin.equals("Y") || userID.equals(masssmsInfo.getUserID()) || (userAuth.equals("2") && masssmsInfo.getGroupID().equals(groupID))))){ %>
				<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData('<c:out value="${masssmsInfo.masssmsID}"/>','10')" class="web20button bigblue">발송</a></div>
				<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData('<c:out value="${masssmsInfo.masssmsID}"/>','00')" class="web20button bigpink">임시저장</a></div>
			<%}%>
			<% if(state.equals("10") &&  userID.equals(masssmsInfo.getApproveUserID()) && !masssmsInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){ %>
				<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').apprData('<c:out value="${masssmsInfo.masssmsID}"/>','Y')" class="web20button bigblue">승인</a></div>
				<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').apprData('<c:out value="${masssmsInfo.masssmsID}"/>','N')" class="web20button bigpink">반려</a></div>
			<%} %>		
			<!-- ////////////////////////////////////////////// -->
	
			<div class="set_wrapper">
				<div class="panel-header">
					<div class="panel-headerContent">
						<div class="toolbarTabs" style="height:28px">				
							<ul id="masssmsTabs" class="tab-menu">					
								<li id="<%=id %>_tabSendSet" class="selected" ><a>발송설정</a></li>
								<li id="<%=id %>_tabFilterSet"><a>필터링</a></li>
								<li id="<%=id %>_tabStatisticSet"><a>통계설정</a></li>
								<li id="<%=id %>_tabMemoSet"><a>메모</a></li>
							</ul>
						</div>
					</div>
				</div>		
		
				<!-- ---------------------- 발송설정일 경우-----------------------------------  -->
				<div id="<%=id %>_sendTypeSet">
						<table class="ctbl" width="100%">
						<tbody>
						<tr>
							<td class="ctbl ttd1" width="100px">발송타입</td>
							<td class="ctbl td" colspan="3">
							<input type="radio" id="eSendType" name="eSendType" class="notBorder" value="0" <c:if test="${masssmsInfo.sendType=='0'}">checked</c:if> />테스트발송&nbsp;&nbsp;
							<input type="radio" id="eSendType" name="eSendType" class="notBorder" value="1" <c:if test="${masssmsInfo.sendType=='1'}">checked</c:if> />즉시발송&nbsp;&nbsp;
							<input type="radio" id="eSendType" name="eSendType" class="notBorder" value="2" <c:if test="${masssmsInfo.sendType=='2'}">checked</c:if> />예약발송&nbsp;&nbsp;
							<input type="radio" id="eSendType" name="eSendType" class="notBorder" value="3" <c:if test="${masssmsInfo.sendType=='3'}">checked</c:if> onclick="javascript:$('<%=id%>').clickRepeat()"/>반복발송
							</td>
							<td class="ctbl ttd1" width="100px" style="display:none">발송 우선 순위</td>
							<td class="ctbl td" width="35px" style="display:none">
								<select id="ePriority" name="ePriority">
									<%if(isAdmin.equals("Y")){ %>
									<option value="1" <c:if test="${masssmsInfo.priority=='1'}"> selected </c:if>>1</option>
									<%} 
									if(isAdmin.equals("Y") || userAuth.equals("2")){%>
									<option value="2" <c:if test="${masssmsInfo.priority=='2'}"> selected </c:if>>2</option>
									<%} %>
									<option value="3" <c:if test="${masssmsInfo.priority=='3'}"> selected </c:if><c:if test="${state==''}"> selected </c:if>>3</option>
									<option value="4" <c:if test="${masssmsInfo.priority=='4'}"> selected </c:if>>4</option>
									<option value="5" <c:if test="${masssmsInfo.priority=='5'}"> selected </c:if>>5</option>
								</select>
							</td>
						</tr>
						</tbody>
						</table>	

						<!-- ###################### 테스트발송 ####################### -->						
						<div class="hidden" id="<%=id %>_sendTestSMS">
							<table class="ctbl" width="100%">
							<tbody>					
							<tr>
								<td class="ctbl ttd1" width="100px" rowspan="2">테스트SMS 입력</td>
								<td class="ctbl td">				
								<div class="left"><textarea id="eSendTestSMS" name="eSendTestSMS" style="width:400px;height:50px" ></textarea><br></div>
								<div class="left"><div class="btn_green"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').testerWindow()"><span>테스트폰불러오기</span></a></div></div>
								<div class="left"><div class="btn_r"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').testSMSResultWindow()" ><span>테스트발송내역</span></a></div></div>
								</td>
							</tr>
							<tr>
								<td>
									<img src="images/tag_blue.png" alt="테스트 폰 작성 요령 "> 여러개의 폰번호를 입력하실 때는 각 폰번호를 <b>세미콜론(;)</b>으로 구분해 주세요. ex) 010XXXYYYY<b>;</b>019XXXYYYY
								</td>
							</tr>			
							</tbody>
							</table>	
						</div>
												
						<!-- ###################### 예약발송 ####################### -->						 					
						<div class="hidden" id="<%=id %>_sendScheduleDate">
							<table class="ctbl" width="100%">
							<tbody>					
							<tr>
								<td class="ctbl ttd1" width="100px">예약발송일자</td>
								<td class="ctbl td">			
									<div class="left">
										<fmt:parseDate value="${masssmsInfo.sendScheduleDate}"  var ="fmt_sendScheduleDate" pattern="yyyy-MM-dd"/>									
										<c:if test="${masssmsInfo.sendScheduleDate!=null}">	
											<input type="text" id="eSendSchedule" name="eSendSchedule" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_sendScheduleDate}" pattern="yyyy-MM-dd"/>"/>
										</c:if>
										<c:if test="${masssmsInfo.sendScheduleDate==null}">
										<input type="text" id="eSendSchedule" name="eSendSchedule" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getDateString() %>"/>
										</c:if>									
										<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eSendSchedule)" align="absmiddle" />
									</div>
									<div class="left">										
										<select id="eSendScheduleHH" name="eSendScheduleHH">
										<c:forEach var="hours" begin="0" end="23" step="1">
												<option value="<c:out value="${hours}"/>" <c:if test="${masssmsInfo.sendScheduleDateHH==hours}"> selected </c:if>><c:out value="${hours}"/></option>
										</c:forEach>
										</select>
									</div>
									<div class="text">시</div>						
									<div class="left">										
										<select id="eSendScheduleMM" name="eSendScheduleMM">
										<c:forEach var="minutes" begin="0" end="59" step="10">										
												<option value="<c:out value="${minutes}"/>" <c:if test="${masssmsInfo.sendScheduleDateMM==minutes}">selected</c:if>><c:out value="${minutes}"/></option>
										</c:forEach>
										</select>							
										
									</div>		
									<div class="text">분</div>																					
								</td>
							</tr>			
							</tbody>
							</table>	
						</div>
						<!-- ###################### 반복발송 ####################### -->	
						<div class="hidden" id="<%=id %>_repeatSendSet">	
							<table class="ctbl" width="100%">
							<tbody>								
							<tr>
								<td class="ctbl ttd1" width="100px">반복발송타입</td>
								<td class="ctbl td">
								<input type="radio" class="notBorder" id="eRepeatSendType" name="eRepeatSendType" <c:if test="${masssmsInfo.repeatSendType=='1'}">checked</c:if> value="1" />매일반복발송 
								<input type="radio" class="notBorder" id="eRepeatSendType" name="eRepeatSendType" <c:if test="${masssmsInfo.repeatSendType=='2'}">checked</c:if> value="2" />매주반복발송 
								<input type="radio" class="notBorder" id="eRepeatSendType" name="eRepeatSendType" <c:if test="${masssmsInfo.repeatSendType=='3'}">checked</c:if> value="3" />매월반복발송 
								</td>
							</tr>
							<tr>
								<td colspan="2" class="ctbl line"></td>
							</tr>
							<tr>
								<td class="ctbl ttd1">반복발송기간</td>
								<td class="ctbl td">
								<div>
									<div class="left">			
									<fmt:parseDate value="${masssmsInfo.repeatSendStartDate}"  var ="fmt_repeatSendStartDate" pattern="yyyy-MM-dd"/>
									<fmt:parseDate value="${masssmsInfo.repeatSendEndDate}"  var ="fmt_repeatSendEndDate" pattern="yyyy-MM-dd"/>	
									<c:if test="${masssmsInfo.repeatSendStartDate!=null}">
										<input type="text" id="eRepeatSendStart" name="eRepeatSendStart" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_repeatSendStartDate}" pattern="yyyy-MM-dd"/>"/>
									</c:if>
									<c:if test="${masssmsInfo.repeatSendStartDate==null}">	
										<input type="text" id="eRepeatSendStart" name="eRepeatSendStart" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getDateString() %>"/>
									</c:if>											
									<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eRepeatSendStart)" />
									</div>
								</div>	
								<div>
									<div class="text">~</div>	
									<div class="left">			
									<c:if test="${masssmsInfo.repeatSendEndDate!=null}">			
										<input type="text" id="eRepeatSendEnd" name="eRepeatSendEnd" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_repeatSendEndDate}" pattern="yyyy-MM-dd"/>"/>										
									</c:if>
									<c:if test="${masssmsInfo.repeatSendEndDate==null}">	
										<input type="text" id="eRepeatSendEnd" name="eRepeatSendEnd" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value=""/>
									</c:if>															
									<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eRepeatSendEnd)" />
									</div>
								</div>																				
								</td>
							</tr>							
							<tr>
								<td colspan="2" class="ctbl line"></td>
							</tr>	
								<tr>
								<td class="ctbl ttd1">반복발송시간 </td>
								<td class="ctbl td">
								<div class="left">								
								<select id="eRepeatSendDateHH" name="eRepeatSendDateHH">
									<c:forEach var="hours" begin="0" end="23" step="1">								
											<option value="<c:out value="${hours}"/>" <c:if test="${masssmsInfo.repeatSendTimeHH==hours}">selected</c:if>><c:out value="${hours}"/></option>
									</c:forEach>	
								</select>			
								</div>
								<div class="text">시</div>						
								<div class="left">								
								<select id="eRepeatSendDateMM" name="eRepeatSendDateMM">
									<c:forEach var="minutes" begin="0" end="59" step="10">											
											<option value="<c:out value="${minutes}"/>" <c:if test="${masssmsInfo.repeatSendTimeMM==minutes}">selected</c:if>><c:out value="${minutes}"/></option>
									</c:forEach>
								</select>
								</div>	
								<div class="text">분</div>
								</td>
							</tr>		
							</tbody>		
							</table>	
							<div class="hidden" id="<%=id %>_repeatSendWeek">											
							<table class="ctbl" width="100%">
							<tbody>														
							<tr>
								<td class="ctbl ttd1" width="100px">매주반복</td>
								<td class="ctbl td">
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${masssmsInfo.repeatSendWeekSun=='1'}">checked</c:if> value="1" />일 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${masssmsInfo.repeatSendWeekMon=='2'}">checked</c:if> value="2" />월 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${masssmsInfo.repeatSendWeekTue=='3'}">checked</c:if> value="3" />화 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${masssmsInfo.repeatSendWeekWed=='4'}">checked</c:if> value="4" />수 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${masssmsInfo.repeatSendWeekThu=='5'}">checked</c:if> value="5" />목 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${masssmsInfo.repeatSendWeekFri=='6'}">checked</c:if> value="6" />금 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${masssmsInfo.repeatSendWeekSun=='7'}">checked</c:if> value="7" />토 
								</td>
							</tr>		
							</tbody>		
							</table>
							</div>
							<div class="hidden" id="<%=id %>_repeatSendDay">
							<table class="ctbl" width="100%">
							<tbody>		
							<tr>
								<td class="ctbl ttd1" width="100px">매월반복</td>
								<td class="ctbl td">
								<div style="float:left">
								<ul id="eRepeatSendDay"  class="selectBox">
									<c:forEach var="days" begin="1" end="28" step="1"> 
											<li data="<c:out value="${days}"/>" <c:if test="${masssmsInfo.repeatSendDay==days}">select='Y'</c:if>><c:out value="${days}"/></li>
									</c:forEach>						
								</ul>
								</div>
								<div style="float:left;margin-left:5px;margin-top:3px">일</div>		
								</td>
							</tr>								
							</tbody>		
							</table>	
							</div>				
						</div>
					</div>	
					
					<!-- --------------------------------필터링 설정---------------------------------------------------- -->
					<div style="display:none;width:100%" id="<%=id %>_filterSet">
						<table class="ctbl" width="100%">
						<tbody>
						<tr>
							<td class="ctbl ttd1" width="100px">수신거부제외방법</td>
							<td class="ctbl td">
							<div style="float:left">
								<select id="eRejectType" name="eRejectType">
									<option value="1" <c:if test="${masssmsInfo.rejectType=='1'}">selected</c:if>>전체 수신거부 제외</option>									
									<option value="2" <c:if test="${masssmsInfo.rejectType=='2'}">selected</c:if>>소속그룹 수신거부 제외</option>
									<option value="3" <c:if test="${masssmsInfo.rejectType=='3'}">selected</c:if>>수신 거부제외 안함</option>
								</select>
							</div>
							</td>
						</tr>
						<tr>
							<td class="ctbl ttd1" width="100px">발송제한</td>
							<td class="ctbl td">
							<div class="left">
								<select id="eSendedYearMonth" name="eSendedYearMonth" <%if(backupDateList.size() == 0){ %>disabled<%} %>>
								<option value="">--제한년도선택--</option>
								<%for(BackupDate backupDate : backupDateList){ %>
									<option value="<%=backupDate.getBackupYearMonth() %>" <% if(backupDate.getBackupYearMonth().equals(masssmsInfo.getSendedYear()+masssmsInfo.getSendedMonth())){%>selected<%} %>><%=DateUtils.reFormat(backupDate.getBackupYearMonth(),"yyyyMM","yyyy년MM월") %></option>
								<%} %>
								</select>
							</div>
							<div class="text">발송통수</div>		
							<div class="left">									
									<select id="eSendedCount" name="eSendedCount" <%if(backupDateList.size() == 0){ %>disabled<%} %>>										
										<option value="0">--</option>
										<c:forEach var="counts" begin="1" end="10" step="1">
												<option value="<c:out value="${counts}"/>" <c:if test="${masssmsInfo.sendedCount==counts}">selected</c:if>><c:out value="${counts}"/></option>
										</c:forEach>	
									</select>									
							</div>	
							<div class="text">회 이상 제외 </div>
							<div class="text"> / </div>			
							<div class="left">
								<input type="checkbox" class="notBorder" id="eNotOpen" name="eNotOpen" value="Y" <c:if test="${masssmsInfo.sendedType=='3'}"> checked </c:if>  <c:if test="${masssmsInfo.sendedType=='1'}"> checked </c:if> <%if(backupDateList.size() == 0){ %>disabled<%} %>/>
							</div>
							<div class="text">실패제외 </div>		
							</td>
						</tr>
						
						<tr>
							<td class="ctbl ttd1" width="100px">중복허용여부</td>
							<td class="ctbl td">
								<div style="float:left">
								<input type="radio" class="notBorder" id="eDuplicationYN" name="eDuplicationYN" value="Y" <c:if test="${masssmsInfo.duplicationYN=='Y'}">checked</c:if> /> 중복 허용
								<input type="radio" class="notBorder" id="eDuplicationYN" name="eDuplicationYN" value="N" <c:if test="${masssmsInfo.duplicationYN=='N'}">checked</c:if> /> 중복 제거 (동일한 번호가 여러개 있을 경우 1통만 발송됩니다.)
							</div>
							</td>
						</tr>
						</tbody>
						</table>	
					</div>	
					
					<!-- --------------------------------통계설정 ---------------------------------------------------- -->
					<div class="hidden" id="<%=id %>_statisticSet">
						<table class="ctbl" width="100%">
						<tbody>
						<tr>
							<td class="ctbl ttd1" width="100px">통계공유여부</td>
							<td class="ctbl td">
							<div class="left">
								<select id="eStatisticsOpenType" name="eStatisticsOpenType">
								<option value="1" <c:if test="${masssmsInfo.statisticsOpenType=='1'}">selected</c:if>>비공유</option>
								<option value="2" <c:if test="${masssmsInfo.statisticsOpenType=='2'}">selected</c:if>>소속그룹공유</option>
								<option value="3" <c:if test="${masssmsInfo.statisticsOpenType=='3'}">selected</c:if>>전체공유</option>
								</select>
							</div>
							</td>
						</tr>						
						</tbody>
						</table>
					</div>		
					<!-- --------------------------------메모입력 ---------------------------------------------------- -->
					<div class="hidden" id="<%=id %>_memoSet">
						<table class="ctbl" width="100%">
						<tbody>
						<tr>
							<td class="ctbl ttd1" width="100px">메모입력</td>
							<td class="ctbl td">
							<textarea id="eMemo" name="eMemo" style="width:100%;height:100px"><c:out value="${masssmsInfo.memo}"/></textarea>
							</td>
						</tr>
						</tbody>
						</table>
					</div>
					<!-- -------------------------------- 탭구분 끝 ---------------------------------------------------- -->
					
			</div>

			<div class="panel-header">
				<div class="panel-header-ball" >
					<div class="panel-expand icon16" style="width:16px"/></div>
				</div>
				<div class="panel-headerContent">
					<h2 id="panel2_title">컨텐츠작성</h2>
				</div>
			</div>				
			<div>
					<table class="ctbl" width="100%">
					<tbody>
					<tr>
						<td class="ctbl ttd1 mustinput" width="100px">보내는 전화번호</td>
						<td class="ctbl td" colspan="3">
							<c:if test="${masssmsInfo.senderPhone==''}">	
								<div class="left"><input type="text" id="eSenderPhone" name="eSenderPhone" value="<%=senderPhone%>" size="30" maxlength="30"/></div>
							</c:if>
							<c:if test="${masssmsInfo.senderPhone!=''}">	
								<div class="left"><input type="text" id="eSenderPhone" name="eSenderPhone" value="<c:out value="${masssmsInfo.senderPhone}"/>" size="30" maxlength="30"/></div>
							</c:if>
							<!-- div class="left"><div class="btn_b"><a id="mailLink" style ="cursor:pointer" href="javascript:$('<%=id%>').senderWindow()"><span>보내는사람찾기</span></a></div></div -->
													
						</td>
					</tr>
					<tr>
					<td class="ctbl ttd1 mustinput" width="100px">SMS작성</td>
					<td class="ctbl td" colspan="3">
				
					<!-- SMS 입력창  시작  -->
						
					<table>
					<tr>
					<td>				
						<table width="200" height="340" border="0" cellpadding="0"	cellspacing="0" background="/images/phone/phone.jpg">
							<tr>
								<td width="41"></td>
								<td width="118" height="87"></td>
								<td width="41"></td>
							</tr>
							<tr>
								<td width="41" height="96"></td>
								<td width="118" height="96" valign="top">
								<table width="118" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="118" height="96" align="center">
										<div align="center">
										<textarea name="smsMsg" mustInput="Y" msg="SMS내용 입력"  style="width: 100%; height: 100px" wrap="PHYSICAL"	class="smsbox" onKeyUp="check_msglen()" onKeyDown="check_msglen()"><c:out value="${masssmsInfo.smsMsg}"/></textarea></div>
										</td>
									</tr>
								</table>
								</td>
								<td width="41" height="96"></td>
							</tr>
							<tr>
								<td width="41" height="157" rowspan="3"></td>
								<td width="118" height="25"></td>
								<td width="41" height="157" rowspan="3"></td>
							</tr>
							<tr>
								<td width="118" height="20"><font color="605E4A">글자수
								 <INPUT TYPE="text" NAME="nbytes" value='0' class='sms_box' style="width: 20px; height: 18px;">/
								2000byte</font></td>
							</tr>
							<tr>
								<td width="118" height="112"></td>
							</tr>
						</table>
					</td>
					
					
					<td valign="top">
					<table>
					<tr>
					<td>
					<div class="btn_b"><a id="mailLink" style ="cursor:pointer" href="javascript:$('<%=id%>').onetooneWindow('smsMsg')"><span>일대일치환삽입</span></a></div>
					</td>
					</tr>
					<tr>
					<td><b>※특수문자 삽입</b>&nbsp;(문자를 클릭하세요)</td>
					</tr>
					<tr>
					<td>
							<img src="/images/phone/specialchar.gif" width="140" height="121" border="0" usemap="#Map">
							<map name="Map">
							<area shape="rect" coords="4,4,21,23" href="javascript:onMergeSmsContent('▷');">
							<area shape="rect" coords="22,4,40,22" href="javascript:onMergeSmsContent('▶');">
							<area shape="rect" coords="42,4,60,22" href="javascript:onMergeSmsContent('♡');">
							<area shape="rect" coords="62,4,80,22" href="javascript:onMergeSmsContent('♥');">
							<area shape="rect" coords="81,4,99,22" href="javascript:onMergeSmsContent('☆');">
							<area shape="rect" coords="100,4,118,22" href="javascript:onMergeSmsContent('★');">
							<area shape="rect" coords="119,4,137,22" href="javascript:onMergeSmsContent('⊙');">
							<area shape="rect" coords="4,22,21,42" href="javascript:onMergeSmsContent('□');">
							<area shape="rect" coords="22,22,40,42" href="javascript:onMergeSmsContent('■');">
							<area shape="rect" coords="42,22,60,42" href="javascript:onMergeSmsContent('◁');">
							<area shape="rect" coords="62,22,80,42" href="javascript:onMergeSmsContent('◀');">
							<area shape="rect" coords="81,22,99,42" href="javascript:onMergeSmsContent('●');">
							<area shape="rect" coords="100,22,118,42" href="javascript:onMergeSmsContent('◎');">
							<area shape="rect" coords="119,22,137,42" href="javascript:onMergeSmsContent('○');">
							<area shape="rect" coords="15,54,17,55" href="javascript:onMergeSmsContent('');">
							<area shape="rect" coords="4,42,21,60" href="javascript:onMergeSmsContent('◇');">
							<area shape="rect" coords="22,42,40,60" href="javascript:onMergeSmsContent('◆');">
							<area shape="rect" coords="42,42,60,60" href="javascript:onMergeSmsContent('△');">
							<area shape="rect" coords="62,42,80,60" href="javascript:onMergeSmsContent('▲');">
							<area shape="rect" coords="81,42,99,60" href="javascript:onMergeSmsContent('▽');">
							<area shape="rect" coords="100,42,118,60" href="javascript:onMergeSmsContent('▼');">
							<area shape="rect" coords="119,42,137,60" href="javascript:onMergeSmsContent('。');">
							<area shape="rect" coords="4,60,21,79" href="javascript:onMergeSmsContent('♨');">
							<area shape="rect" coords="22,60,40,79" href="javascript:onMergeSmsContent('☏');">
							<area shape="rect" coords="42,60,60,79" href="javascript:onMergeSmsContent('☎');">
							<area shape="rect" coords="62,60,80,79" href="javascript:onMergeSmsContent('☜');">
							<area shape="rect" coords="81,60,99,79" href="javascript:onMergeSmsContent('☞');">
							<area shape="rect" coords="100,60,118,79" href="javascript:onMergeSmsContent('◈');">
							<area shape="rect" coords="119,60,137,79" href="javascript:onMergeSmsContent('▣');">
							<area shape="rect" coords="4,79,21,98" href="javascript:onMergeSmsContent('♩');">
							<area shape="rect" coords="22,79,40,98" href="javascript:onMergeSmsContent('♪');">
							<area shape="rect" coords="42,79,60,98" href="javascript:onMergeSmsContent('♬');">
							<area shape="rect" coords="62,79,80,98" href="javascript:onMergeSmsContent('§');">
							<area shape="rect" coords="81,79,99,98" href="javascript:onMergeSmsContent('※');">
							<area shape="rect" coords="100,79,118,98" href="javascript:onMergeSmsContent('◐');">
							<area shape="rect" coords="119,79,137,98" href="javascript:onMergeSmsContent('◑');">
							<area shape="rect" coords="4,98,21,117" href="javascript:onMergeSmsContent('＊');">
							<area shape="rect" coords="22,98,40,117" href="javascript:onMergeSmsContent('＠');">
							<area shape="rect" coords="42,98,60,117" href="javascript:onMergeSmsContent('→');">
							<area shape="rect" coords="62,98,80,117" href="javascript:onMergeSmsContent('←');">
							<area shape="rect" coords="81,98,99,117" href="javascript:onMergeSmsContent('↑');">
							<area shape="rect" coords="100,98,118,117" href="javascript:onMergeSmsContent('↓');">
							<area shape="rect" coords="119,98,137,117" href="javascript:onMergeSmsContent('↔');">
							</map>
							
					</td>		
					</tr>					
					</table>
					</td>
					
					</tr>
					</table>
					
					<!-- SMS 입력창  끝 -->
					
					</td>
					</tr>
					</tbody>
					</table>	
	

					<!-- //////////////////////발송버튼///////////////  -->
					<div class="right"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>	
					<%
						//삭제는 예약발송이면서  발송준비대기중,  임시저장, 발송완료, 발송완전중지, 발송준비중오류, 발송중오류 인 경우만 가능 
						if( (isAdmin.equals("Y") || userID.equals(masssmsInfo.getUserID()) || (userAuth.equals("2") && masssmsInfo.getGroupID().equals(groupID)) ) && ((masssmsInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE) && state.equals("11")) || state.equals("00") || state.equals("15") || state.equals("44") || state.equals("32") || state.equals("22") || state.equals("24")|| state.equals("33") )){ 
					%>			
						<div class="right"><a href="javascript:$('<%=id%>').deleteData(<c:out value="${masssmsInfo.masssmsID}"/>,<c:out value="${masssmsInfo.scheduleID}"/>)" class="web20button bigpink">삭 제</a></div>
					<%}%>
					<% if( (isAdmin.equals("Y") || userID.equals(masssmsInfo.getUserID()) || (userAuth.equals("2") && masssmsInfo.getGroupID().equals(groupID))) && ((masssmsInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE) && state.equals("11")))){ %>
						<div class="right"><a href="javascript:$('<%=id%>').saveData('<c:out value="${masssmsInfo.masssmsID}"/>','10')" class="web20button bigpink">수정</a></div>	
					<%} %>
					<% if(state.equals("") || (state.equals("00") && (isAdmin.equals("Y") || userID.equals(masssmsInfo.getUserID()) || (userAuth.equals("2") && masssmsInfo.getGroupID().equals(groupID))))){ %>
						<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData('<c:out value="${masssmsInfo.masssmsID}"/>','10')" class="web20button bigblue">발송</a></div>
						<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData('<c:out value="${masssmsInfo.masssmsID}"/>','00')" class="web20button bigpink">임시저장</a></div>
					<%}%>	
					<% if(state.equals("10") && userID.equals(masssmsInfo.getApproveUserID()) && !masssmsInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){ %>
						<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').apprData('<c:out value="${masssmsInfo.masssmsID}"/>','Y')" class="web20button bigblue">승인</a></div>
					    <div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').apprData('<c:out value="${masssmsInfo.masssmsID}"/>','N')" class="web20button bigpink">반려</a></div>
					<%} %>
					<!-- ////////////////////////////////////////////// -->
					</div>


			</div>
	</form>			

</div>

<script type="text/javascript">

	//반복발송 클릭이라면 
	$('<%=id%>').clickRepeat=function(){
		var frm = $('<%=id%>_form');
		frm.eRepeatSendType[0].checked=true;
	};


	/***********************************************/
	/* 저장버튼 클릭 -  저장
	/***********************************************/
	$('<%=id%>').saveData = function( seq, state ) {
		var frm = $('<%=id%>_form');
		//copyForm( $('<%=id%>_rform'), frm );
		
		if(frm.targetID==undefined){
			alert('대상자그룹을 추가하세요');
			return;
		}

		if(frm.eSendType[0].checked==true){
			if(state == '00'){
				alert("테스트 발송은 임시저장을 지원하지 않습니다. \r발송타입 변경후 임시저장하십시오.");
				return;
			}
			if(frm.eSendTestSMS.value==''){
				alert('테스트폰 번호를 입력하세요');
				frm.eSendTestSMS.focus();
				return;
			}
		}
		if(frm.eSendType[3].checked==true && state=='00'){
			alert("반복발송 설정시에는 임시저장을 하실 수 없습니다.");
			return;
		}
			
		//예약발송일 체크 
		if(frm.eSendType[2].checked==true){
			
			var hh = frm.eSendScheduleHH.value;
			if(hh<10) hh = "0"+hh;
			var mm = frm.eSendScheduleMM.value;
			if(mm<10) mm = "0"+mm;
			var sendSchedule = frm.eSendSchedule.value+" "+hh+":"+mm+":00";

			if(sendSchedule<'<%=DateUtils.getDateTimeString()%>'){
				alert('예약발송일이 현재시간보다 이전입니다.');
				return;
			}
		}

		
		//반복발송 기간 체크 
		if(frm.eSendType[3].checked==true){
			if(frm.eRepeatSendEnd.value==''){
				alert('반복발송 기간을 선택해주세요');
				frm.eRepeatSendEnd.focus();
				return; 
			}
			if(frm.eRepeatSendEnd.value<=frm.eRepeatSendStart.value){
				alert('반복발송 기간이 잘못되었습니다');
				frm.eRepeatSendEnd.focus();
				return;
			}
			//eRepeatSendDateMM
			
			var rHH = frm.eRepeatSendDateHH.value;
			if(rHH<10) rHH = "0"+rHH;
			var rMM = frm.eRepeatSendDateMM.value;
			if(rMM<10) rMM = "0"+rMM;


			var repeatStartDate = frm.eRepeatSendStart.value+" "+rHH+":"+rMM+":00";
			var repeatEndDate = frm.eRepeatSendEnd.value+" "+rHH+":"+rMM+":00";

			if(repeatStartDate<'<%=DateUtils.getDateTimeString()%>'){
				alert('반복발송시작일이 현재시각보다 이전입니다.');
				frm.eRepeatSendStart.focus();
				return;
			}
						
			
			//매일반복이라면 
			if(frm.eRepeatSendType[0].checked==true){

				//매일반복발송인데 반복발송기간이 현재날짜부터 2일미만이라면 불가 
				if($('<%=id%>').returnDays(repeatStartDate,repeatEndDate)<2){
					alert('매일반복발송은 발송기간이 적어도 2일 이상 되어야 합니다.');
					frm.eRepeatSendEnd.focus();
					return;
				}		
			}
			//매주반복발송이라면 	
			else if(frm.eRepeatSendType[1].checked==true){
				//매주반복발송인데 반복발송기간이 현재날짜부터 7일미만이라면 불가 
				if($('<%=id%>').returnDays(repeatStartDate,repeatEndDate)<7){
					alert('매주반복발송은 발송기간이 적어도 7일 이상 되어야 합니다.');
					frm.eRepeatSendEnd.focus();
					return;
				}			 
			}
			//매월반복발송이라면
			else if(frm.eRepeatSendType[2].checked==true){
				//매월반복발송인데 반복발송기간이 현재날짜부터 2개월 미만이라면 불가 
				if($('<%=id%>').returnDays(repeatStartDate,repeatEndDate)<50){
					alert('매월반복발송은 발송기간이 적어도 50일 이상 되어야 합니다.');
					frm.eRepeatSendEnd.focus();
					return;
				}				
			}			
		}
		
		//필수입력 조건 체크
		
		if(!checkFormValue(frm)) {
			return;
		}

		if(state!='00'){
			if(frm.eSenderPhone.value== ''){
				alert('보내는 전화번호는 필수항목입니다.');
				frm.eSenderPhone.focus();
				return;
			}
		}



		frm.eSaveState.value = state;

		var methodInsertType = "";
		var methodUpdateType = "";

	
		if(seq =='' || seq == '0' ) {
			if(frm.eSendType[0].checked==true){
				goUrl = 'masssms/write/masssms.do?id=<%=id%>&method=sendTestSMS';
			}else{
				goUrl = 'masssms/write/masssms.do?id=<%=id%>&method=insert';
			}
		} else {
			if(frm.eSendType[0].checked==true){
				goUrl = 'masssms/write/masssms.do?id=<%=id%>&method=sendTestSMS';
			}else{
				goUrl = 'masssms/write/masssms.do?id=<%=id%>&method=update';
			}
		}

			nemoRequest.init( 
			{			
					busyWindowId: '<%=id%>'  // busy 를 표시할 window
					//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
				
					, url: goUrl
					//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
					, onSuccess: function(html,els,resHTML) {

						
						//console.log(html);
						//console.log(els);
						//console.log(resHTML);
						//console.log(masssmsID);
						
						var gubun = resHTML.indexOf("|");
						var masssmsID = resHTML.substring(0,gubun);
						var state = resHTML.substring(gubun+1,resHTML.length);


						if(frm.eSendType[0].checked==false ){
								
								if(state!='00'){
									closeWindow( $('<%=id%>') );
								
								//만약 리스트에서 뛰우게 한다면 아래부분을 구분파라미터를 을 주어서 처리해야 한다. 				
									MochaUI.masssmsListWindow();
								}else{
								
									if(seq == '0'){
										closeWindow( $('<%=id%>') );
										nemoWindow(
											{
												'id': '<%=id%>',

												busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

												width: 900,
												height: $('mainColumn').style.height.toInt(),
												//height: 600,
												title: '대량SMS작성',
												type: 'modal',
												loadMethod: 'xhr',
												contentURL: 'masssms/write/masssms.do?id=<%=id%>&method=edit&masssmsID='+masssmsID+'&scheduleID=1&state=00'
											}
										);
									}
									alert('임시저장이 완료 되었습니다.');
									MochaUI.masssmsListWindow();
								}	
						}else{
							$('<%=id%>').testSMSResultWindow();
						}

						
					}
			});
		
		nemoRequest.post(frm);
		
		
	}


	/***********************************************/
	/* 승인 / 반려 버튼 클릭 
	/***********************************************/
	$('<%=id%>').apprData = function( seq, state ) {
		var message = "SMS 발송을 승인 하시겠습니까?";
		if(state == 'N'){
			message = "SMS 발송을 반려 하시겠습니까?";
		}
			
		if(!confirm(message)) return;
		var frm = $('<%=id%>_form');
		//frm.eMailContent.value = $('<%=id%>_ifrmFckEditor').contentWindow.getFCKHtml();

		if(frm.targetID==undefined){
			alert('대상자그룹을 추가하세요');
			return;
		}

		if(frm.eSendType[0].checked==true){
			if(frm.eSendTestSMS.value==''){
				alert('테스트SMS을 입력하세요');
				frm.eSendTestSMS.focus();
				return;
			}
		}
		if(frm.eSendType[3].checked==true && state=='00'){
			alert("반복SMS 설정시에는 임시저장을 하실 수 없습니다.");
			return;
		}
			
		//예약발송일 체크 
		if(frm.eSendType[2].checked==true){
			
			var hh = frm.eSendScheduleHH.value;
			if(hh<10) hh = "0"+hh;
			var mm = frm.eSendScheduleMM.value;
			if(mm<10) mm = "0"+mm;
			var sendSchedule = frm.eSendSchedule.value+" "+hh+":"+mm+":00";

			if(sendSchedule<'<%=DateUtils.getDateTimeString()%>'){
				alert('예약발송일이 현재시간보다 이전입니다.');
				return;
			}
		}

		
		//반복발송 기간 체크 
		if(frm.eSendType[3].checked==true){
			if(frm.eRepeatSendEnd.value==''){
				alert('반복발송 기간을 선택해주세요');
				frm.eRepeatSendEnd.focus();
				return; 
			}
			if(frm.eRepeatSendEnd.value<=frm.eRepeatSendStart.value){
				alert('반복발송 기간이 잘못되었습니다');
				frm.eRepeatSendEnd.focus();
				return;
			}
			//eRepeatSendDateMM
			
			var rHH = frm.eRepeatSendDateHH.value;
			if(rHH<10) rHH = "0"+rHH;
			var rMM = frm.eRepeatSendDateMM.value;
			if(rMM<10) rMM = "0"+rMM;


			var repeatStartDate = frm.eRepeatSendStart.value+" "+rHH+":"+rMM+":00";
			var repeatEndDate = frm.eRepeatSendEnd.value+" "+rHH+":"+rMM+":00";

			if(repeatStartDate<'<%=DateUtils.getDateTimeString()%>'){
				alert('반복발송시작일이 현재시각보다 이전입니다.');
				frm.eRepeatSendStart.focus();
				return;
			}
						
			
			//매일반복이라면 
			if(frm.eRepeatSendType[0].checked==true){

				//매일반복발송인데 반복발송기간이 현재날짜부터 2일미만이라면 불가 
				if($('<%=id%>').returnDays(repeatStartDate,repeatEndDate)<2){
					alert('매일반복발송은 발송기간이 적어도 2일 이상 되어야 합니다.');
					frm.eRepeatSendEnd.focus();
					return;
				}		
			}
			//매주반복발송이라면 	
			else if(frm.eRepeatSendType[1].checked==true){
				//매주반복발송인데 반복발송기간이 현재날짜부터 7일미만이라면 불가 
				if($('<%=id%>').returnDays(repeatStartDate,repeatEndDate)<7){
					alert('매주반복발송은 발송기간이 적어도 7일 이상 되어야 합니다.');
					frm.eRepeatSendEnd.focus();
					return;
				}			 
			}
			//매월반복발송이라면
			else if(frm.eRepeatSendType[2].checked==true){
				//매월반복발송인데 반복발송기간이 현재날짜부터 2개월 미만이라면 불가 
				if($('<%=id%>').returnDays(repeatStartDate,repeatEndDate)<50){
					alert('매월반복발송은 발송기간이 적어도 50일 이상 되어야 합니다.');
					frm.eRepeatSendEnd.focus();
					return;
				}				
			}			
		}
		
		if(frm.eMasssmsTitle.value== '') {
				alert('대량SMS명은 필수항목입니다.');
				frm.eMasssmsTitle.focus();
				return;
		}
		if(state!='00'){
			if(frm.eSenderPhone.value== ''){
				alert('보내는 전화번호는 필수항목입니다.');
				frm.eSenderPhone.focus();
				return;
			}
			
		}


		frm.eSaveState.value = state;

		var methodInsertType = "";
		var methodUpdateType = "";

	
		if(seq =='' || seq == '0' ) {
			if(frm.eSendType[0].checked==true){
				goUrl = 'masssms/write/masssms.do?id=<%=id%>&method=sendTestSMS';
			}else{
				goUrl = 'masssms/write/masssms.do?id=<%=id%>&method=insert';
			}
		} else {
			if(frm.eSendType[0].checked==true){
				goUrl = 'masssms/write/masssms.do?id=<%=id%>&method=sendTestSMS';
			}else{
				goUrl = 'masssms/write/masssms.do?id=<%=id%>&method=update';
			}
		}

			nemoRequest.init( 
			{			
					busyWindowId: '<%=id%>'  // busy 를 표시할 window
					//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
				
					, url: goUrl
					//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
					, onSuccess: function(html,els,resHTML) {
						
						var gubun = resHTML.indexOf("|");
						var masssmsID = resHTML.substring(0,gubun);
						var state = resHTML.substring(gubun+1,resHTML.length);

						if(frm.eSendType[0].checked==false){
								if(state!='00'){
									alert('승인이 완료 되었습니다.');
								}else{
									alert('반려가  완료 되었습니다.');
								}	
								closeWindow( $('<%=id%>') );
								MochaUI.masssmsListWindow();
						}else{
							$('<%=id%>').testSMSResultWindow();
						}

						
					}
			});
		
		nemoRequest.post(frm);
		
		
	}
	/***********************************************/
	/* 삭제버튼 클릭 - 
	/***********************************************/
	$('<%=id%>').deleteData = function() {

		var frm = $('<%=id%>_form');

		var masssmsID = frm.eMasssmsID.value;
		var scheduleID = frm.eScheduleID.value;
		var sendType = frm.hSendType.value;
		
		if(!confirm("정말로 삭제 하시겠습니까?  \r\n삭제하시면 관련된 모든 데이타는 영구삭제가 됩니다.")) return;
		
		nemoRequest.init( 
		{
			busyWindowId: '<%=id%>'  // busy 를 표시할 window
			//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

			, url: 'masssms/write/masssms.do?method=deleteMassSMSAll&id=<%=id%>&eMasssmsID='+ masssmsID+'&eScheduleID='+scheduleID+'&eSendType='+sendType
			, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
			, onSuccess: function(html,els,resHTML) {
				closeWindow( $('<%=id%>') );
				MochaUI.masssmsListWindow();	
				
			}
		});
		nemoRequest.post(frm);
		
	}
	
	
	//주어진 날짜사이의 일수를 리턴한다.
	$('<%=id%>').returnDays = function(fromDate,toDate){
		var fromYears = fromDate.substring(0,4);
		var fromMonth =  fromDate.substring(5,7);
		var fromDays =  fromDate.substring(8,10);
		var fromHour =  fromDate.substring(11,13);
		var fromMin =  fromDate.substring(14,16);
		var fromSec =  fromDate.substring(17,19);

		var toYears = toDate.substring(0,4);
		var toMonth =  toDate.substring(5,7);
		var toDays =  toDate.substring(8,10);
		var toHour =  toDate.substring(11,13);
		var toMin =  toDate.substring(14,16);
		var toSec =  toDate.substring(17,19);


		date1 = new Date(fromYears,fromMonth,fromDays,fromHour,fromMin,fromSec);
		date2 = new Date(toYears,toMonth,toDays,toHour,toMin,toSec)

		var result = date2.getTime() - date1.getTime();
		
		result = result / (1000 * 60 * 60 * 24);
		
		return result;		
	};

	//즉시발송
	$('<%=id%>').sendChecked = function(){	
			var frm = $('<%=id%>_form');			
			$(frm.eSendType[1]).checked=true;			
			$('<%=id %>_sendScheduleDate').setStyle('display','none');
			$('<%=id %>_repeatSendSet').setStyle('display','none');
			$('<%=id %>_statisticSet').setStyle('display','none');
			$('<%=id %>_memoSet').setStyle('display','none');
			$('<%=id %>_sendTestSMS').setStyle('display','none');
	};
	//예약발송
	$('<%=id%>').sendCheckedReserved = function(){	
			var frm = $('<%=id%>_form');			
			$(frm.eSendType[2]).checked=true;			
			$('<%=id %>_sendScheduleDate').setStyle('display','block');
			$('<%=id %>_repeatSendSet').setStyle('display','none');
			$('<%=id %>_statisticSet').setStyle('display','none');
			$('<%=id %>_memoSet').setStyle('display','none');
			$('<%=id %>_sendTestSMS').setStyle('display','none');
	};



	/***********************************************/
	/* 초기화
	/***********************************************/

	window.addEvent("domready",function() {
		
		var frm = $('<%=id%>_form');

		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
		MochaUI.initializeTabs('masssmsTabs');


		//발송설정 탭클릭
		$('<%=id %>_tabSendSet').addEvent('click', function(){
			$('<%=id %>_sendTypeSet').setStyle('display','block');
			$('<%=id %>_filterSet').setStyle('display','none');
			$('<%=id %>_statisticSet').setStyle('display','none');
			$('<%=id %>_memoSet').setStyle('display','none');
		});


		//필터링설정 탭클릭
		$('<%=id %>_tabFilterSet').addEvent('click', function(){
			$('<%=id %>_sendTypeSet').setStyle('display','none');
			$('<%=id %>_filterSet').setStyle('display','block');
			$('<%=id %>_statisticSet').setStyle('display','none');
			$('<%=id %>_memoSet').setStyle('display','none');
		});

		//통계설정 탭클릭
		$('<%=id %>_tabStatisticSet').addEvent('click', function(){
			$('<%=id %>_sendTypeSet').setStyle('display','none');
			$('<%=id %>_filterSet').setStyle('display','none');
			$('<%=id %>_statisticSet').setStyle('display','block');
			$('<%=id %>_memoSet').setStyle('display','none');
		});


		//메모  탭클릭
		$('<%=id %>_tabMemoSet').addEvent('click', function(){
			$('<%=id %>_sendTypeSet').setStyle('display','none');
			$('<%=id %>_filterSet').setStyle('display','none');
			$('<%=id %>_statisticSet').setStyle('display','none');
			$('<%=id %>_memoSet').setStyle('display','block');
		});

		
		//테스트발송
		$(frm.eSendType[0]).addEvent('click', function(){
			$('<%=id %>_sendScheduleDate').setStyle('display','none');
			$('<%=id %>_repeatSendSet').setStyle('display','none');
			$('<%=id %>_sendTestSMS').setStyle('display','block');
		});	

		//즉시발송 
		$(frm.eSendType[1]).addEvent('click',$('<%=id%>').sendChecked);


		//예약발송 
		$(frm.eSendType[2]).addEvent('click', function(){	
			$('<%=id %>_repeatSendSet').setStyle('display','none');
			$('<%=id %>_sendScheduleDate').setStyle('display','block');
			$('<%=id %>_sendTestSMS').setStyle('display','none');
		});	

		//반복발송 
		$(frm.eSendType[3]).addEvent('click', function(){
			$('<%=id %>_sendScheduleDate').setStyle('display','none');
			$('<%=id %>_repeatSendSet').setStyle('display','block');
			$('<%=id %>_sendTestSMS').setStyle('display','none');
		});

		//매일반복발송
		$(frm.eRepeatSendType[0]).addEvent('click', function(){		
			$('<%=id %>_repeatSendDay').setStyle('display','none');
			$('<%=id %>_repeatSendWeek').setStyle('display','none');
		});
		

		//매주반복발송
		$(frm.eRepeatSendType[1]).addEvent('click', function(){		
			$('<%=id %>_repeatSendDay').setStyle('display','none');
			$('<%=id %>_repeatSendWeek').setStyle('display','block');
		});
		
		
		//매월반복발송 
		$(frm.eRepeatSendType[2]).addEvent('click', function(){		
			$('<%=id %>_repeatSendDay').setStyle('display','block');
			$('<%=id %>_repeatSendWeek').setStyle('display','none');
		});

		<%%>
		<% if(method.equals("edit")||(request.getParameter("eSendScheduleDateStart")!=null&&!(request.getParameter("eSendScheduleDateStart").equals("")))){%>		
			//즉시발송
			if(frm.eSendType[1].checked==true){
				$('<%=id%>').sendChecked();
			}	
			//예약발송
			else if(frm.eSendType[2].checked==true){
				$('<%=id %>_repeatSendSet').setStyle('display','none');
				$('<%=id %>_sendScheduleDate').setStyle('display','block');
				$('<%=id %>_sendTestSMS').setStyle('display','none');
			}
			else if(frm.eSendType[3].checked==true){
				$('<%=id %>_repeatSendSet').setStyle('display','block');
				$('<%=id %>_sendScheduleDate').setStyle('display','none');
				$('<%=id %>_sendTestSMS').setStyle('display','none');

				//매일 반복발송
				if(frm.eRepeatSendType[0].checked==true){
					$('<%=id %>_repeatSendDay').setStyle('display','none');
					$('<%=id %>_repeatSendWeek').setStyle('display','none');
				}
				//매주반복발송 
				else if(frm.eRepeatSendType[1].checked==true){
					$('<%=id %>_repeatSendDay').setStyle('display','none');
					$('<%=id %>_repeatSendWeek').setStyle('display','block');
				}
				//매월반복발송 
				else if(frm.eRepeatSendType[2].checked==true){
					$('<%=id %>_repeatSendDay').setStyle('display','block');
					$('<%=id %>_repeatSendWeek').setStyle('display','none');
				}
				
			}	
					
			
		<%}%>

		
		
		//sendTypeClickDef();
		$('<%=id %>_sendTypeSet').setStyle('display','block');
		
	});


	/***********************************************/
	/* 대상자  열기창 
	/***********************************************/
	$('<%=id%>').targetWindow = function() {
		
		nemoWindow(
			{
				'id': '<%=id%>_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 650,
				//height: $('mainColumn').style.height.toInt(),
				height: 400,
				title: '대상자리스트 선택',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/masssms/write/masssms_target.jsp?id=<%=id%>&method=targeting'
			}
		);
		
	}

	

	/***********************************************/
	/* 테스터   열기창 
	/***********************************************/
	$('<%=id%>').testerWindow = function() {
		
		nemoWindow(
			{
				'id': '<%=id%>_tester_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 400,
				//height: $('mainColumn').style.height.toInt(),
				height: 300,
				title: '테스트폰  선택',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/masssms/write/masssms_testsms.jsp?preID=<%=id%>&id=<%=id%>_tester_modal&method=tester'
			}
		);
		
	}


	
	/***********************************************/
	/* 테스트폰 결과 창 
	/***********************************************/
	$('<%=id%>').testSMSResultWindow = function() {

		var frm = $('<%=id%>_form');
		
		nemoWindow(
			{
				'id': '<%=id%>_testsmsresult_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 800,
				//height: $('mainColumn').style.height.toInt(),
				height: 600,
				title: '테스트폰 발송내역 ',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/masssms/write/masssms_testsms_sendresult.jsp?id=<%=id%>_testsmsresult_modal&method=sendedTestSMS'
			}
		);
		
	}

	/***********************************************/
	/* 원투원리스트 
	/***********************************************/
	$('<%=id%>').onetooneWindow = function(textName) {

		var frm = $('<%=id%>_form');

		if(frm.targetID==null){
			alert('대상자를 먼저 추가해주세요');
			return;
		}

		var targetLen = frm.targetID.length;
		var targetIDs = "";
	
		if(targetLen==null){
			targetLen = 1;
			targetIDs = frm.targetID.value+"^";	
									
		}else{
			for(var i=0;i<targetLen;i++){
				targetIDs=targetIDs+frm.targetID[i].value+"^";
			}
		}
	
		
		nemoWindow(
			{
				'id': '<%=id%>_onetoone_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 330,
				//height: $('mainColumn').style.height.toInt(),
				height: 300,
				title: '일대일치환',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/masssms/write/masssms_onetoone.jsp?id=<%=id%>_onetoone_modal&method=onetoone&preID=<%=id%>&targetIDs='+targetIDs+'&textName='+textName
			}
		);
		
	}	
		
	/***********************************************/
	/* 보내는 사람열기
	/***********************************************/
	$('<%=id%>').senderWindow = function() {

		var frm = $('<%=id%>_form');
		
		nemoWindow(
			{
				'id': '<%=id%>_sender_modal',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: 300,
				//height: $('mainColumn').style.height.toInt(),
				height: 200,
				title: '보내는 사람 ',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/masssms/write/masssms_sender.jsp?id=<%=id%>_sender_modal&method=sender&preID=<%=id%>'
			}
		);		
	}

	
	/***********************************************/
	/* 선택 삭제 
	/***********************************************/
	$('<%=id%>').removeTargetRow = function() {

		var checkedObj = $('<%=id%>_grid_content_list').getElements('input[type=checkbox]');

		if(checkedObj == null || checkedObj.length == 0) {
			alert('삭제할  대상자를 먼저 체크하세요');
			return;
		}

		var checked = false;
		for(var i = checkedObj.length-1; i >= 0; i--) {
			if(checkedObj[i].checked) {
				checked = true;
			
				checkedObj[i].getParent('tr').dispose();
			}
		}

		if(!checked) {
			alert('삭제할  대상자를 먼저 체크하세요');
			return;
		}
		
	}


	$('<%=id%>_form').nbytes.value = calculate_msglen($('<%=id%>_form').smsMsg.value);;

	function onMergeSmsContent( value )
	{
		var frm =$('<%=id%>_form');	
		var temp = frm.smsMsg.value;
		frm.smsMsg.value = temp + value;
		check_msglen();
	}
	

	function check_msglen()
	{
		var frm =$('<%=id%>_form');
		var length = calculate_msglen(frm.smsMsg.value);
		frm.nbytes.value = length;
		if (length > 2000) {
			alert("무선메시지는 최대 2000 바이트까지 전송하실 수 있습니다.\r\n초과된 " + (length - 2000) + "바이트는 자동으로 삭제됩니다.");
			frm.smsMsg.value = assert_msglen(frm.smsMsg.value, 2000);
		}
	}


	function calculate_msglen(message)
	{
		var nbytes = 0;
		for (i=0; i<message.length; i++) {
			var ch = message.charAt(i);
			if (escape(ch).length > 4) {
				nbytes += 2;
			} else if (ch != '\r') {
				nbytes++;
			}
		}
		return nbytes;
	}


	function assert_msglen(message, maximum)
	{
		var frm =$('<%=id%>_form');
		var inc = 0;
		var nbytes = 0;
		var msg = "";
		var msglen = message.length;
		for (i=0; i<msglen; i++) {
			var ch = message.charAt(i);
			if (escape(ch).length > 4) {
				inc = 2;
			} else if (ch != '\r') {
				inc = 1;
			}
			if ((nbytes + inc) > maximum) {
				break;
			}
			nbytes += inc;
			msg += ch;
		}
		frm.nbytes.value = nbytes;
		return msg;
	}
	
</script>