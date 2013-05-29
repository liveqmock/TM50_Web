<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%@ page import="web.massmail.write.service.MassMailService" %>    
<%@ page import="web.massmail.write.model.*" %>    
<%@ page import="web.massmail.write.control.MassMailControlHelper" %>   
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%> 
<%@ page import="java.util.List"%>

<%@ page import="web.admin.usergroup.model.User"%>
<%@ page import="web.admin.usergroup.service.UserGroupService"%>
<%@ page import="web.admin.usergroup.control.UserGroupControlHelper"%>

<jsp:useBean id="massmailID" class="java.lang.String" scope="request" />
<jsp:useBean id="oldMassmailID" class="java.lang.String" scope="request" />
<jsp:useBean id="state" class="java.lang.String" scope="request" />
<jsp:useBean id="massmailInfo" class="web.massmail.write.model.MassMailInfo" scope="request" />

<%	
	if(request.getParameter("eSendScheduleDateStart")!=null&&!(request.getParameter("eSendScheduleDateStart").equals("")))
	{	
		
		massmailInfo.setSendType("2");
		massmailInfo.setSendScheduleDate(request.getParameter("eSendScheduleDateStart"));
	}
	String id = request.getParameter("id");
		
	String method = request.getParameter("method");
	if(method==null) method="";
	
	//관리자라면 
	String isAdmin = LoginInfo.getIsAdmin(request);
	//발송권한이라면 
	String auth_send_mail = LoginInfo.getAuth_send_mail(request);
	String userAuth = LoginInfo.getUserAuth(request);
	
	//설문권한체크 
	String isPollAuth = LoginInfo.isAccessSubMenu("40",request);
	String isPollYN = PropertiesUtil.getStringProperties("configure", "poll_yn");
	
	

	
	String sendAuth = "N";
	if(isAdmin.equals("Y") || auth_send_mail.equals("Y")){
		sendAuth = "Y";
	}

	String groupID = LoginInfo.getGroupID(request);
	String userID = LoginInfo.getUserID(request);
	
	UserGroupService userService = UserGroupControlHelper.getUserService(application);
	User user = userService.viewUser(userID); // 로그인 한 유저 데이터 불러오기
	
	MassMailService service = MassMailControlHelper.getUserService(application);
	//List<Sender> senderList = service.selectSenderByUserID(groupID,userID,"Y");
	List<BackupDate> backupDateList = service.getBackupDate();
	String senderMail = LoginInfo.getSenderEmail(request);
	String senderName = LoginInfo.getSenderName(request);
	
	SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(application);
	if(senderMail.equals("")){
		senderMail = systemSetservice.getSystemSetInfo("1","senderMail");
	}
	if(senderName.equals("")){
		senderName = systemSetservice.getSystemSetInfo("1","senderName");
	}
	String receiverName =systemSetservice.getSystemSetInfo("1","receiverName");
	String returnMail = systemSetservice.getSystemSetInfo("1","returnMail");
	String returnMailYN = systemSetservice.getSystemSetInfo("1","returnMailYN");
	
	
	if(massmailInfo.getMailLinkYN().equals("")){
		massmailInfo.setMailLinkYN(systemSetservice.getSystemSetInfo("1","linkYN"));
	}
	if(massmailInfo.getRejectType().equals("")){
		massmailInfo.setRejectType(systemSetservice.getSystemSetInfo("1","rejectType"));
	}
	if(massmailInfo.getPersistErrorCount() < 0){
		try{
			massmailInfo.setPersistErrorCount(new Integer(systemSetservice.getSystemSetInfo("1","persistErrorCount")));
		}catch(Exception e){}
	}
	
	String filterManagerYN = systemSetservice.getSystemSetInfo("1","filterManagerYN");
	
	
	String htmls = "";
	if(massmailInfo.getAttachedFileNames()!=null&&!(massmailInfo.getAttachedFileNames().equals("")) )
	{
		//htmlrows += "<a href='"+frm.eFilePath[i].value+"'>"+frm.eFileName[i].value+"</a>("+frm.eFileSize[i].value+")<br>";
		//htmlrows += "<input type='hidden' id='eAttachedFiles' name='eAttachedFiles' value='" + frm.eFileName[i].value + "'>";
		//htmlrows += "<input type='hidden' id='eAttachedFilesPath' name='eAttachedFilesPath' value='" + frm.eFilePath[i].value.substring(frm.eFilePath[i].value.indexOf("&fileKey=")+9,frm.eFilePath[i].value.length) + "'>";
		
		String[] names = massmailInfo.getAttachedFileNames().split(";");
		String[] path = massmailInfo.getAttachedFilePath().split(";");
		for(int i = 0;i<names.length;i++)
		{
			htmls +="<a href='"+path[i]+"'>" + names[i] +"</a><br>";
			htmls +="<input type='hidden' id='eAttachedFiles' name='eAttachedFiles' value='" + names[i] + "'>";
			htmls +="<input type='hidden' id='eAttachedFilesPath' name='eAttachedFilesPath' value='" + path[i] + "'>";
		}
		htmls += "<a href=\"javascript:$('"+id+"').clearAttach()\"><img src='/images/btn_delete_sm.gif'></a>";
		
	}
	
	
%>

<div class="massmail_wrapper">
	<form id="<%=id%>_form" name="<%=id%>_form" method="post">
	<input type="hidden" id="eSaveState" name="eSaveState">
	<input type="hidden" id="eStateValue" name="eStateValue">
	<input type="hidden" id="eMailContent" name="eMailContent">
	<input type="hidden" id="filterManagerYN" name="filterManagerYN" value="<%=filterManagerYN%>">		
	
	<input type="hidden" id="eMassmailID" name="eMassmailID" value="<c:out value="${massmailInfo.massmailID}"/>">
	<input type="hidden" id="eScheduleID" name="eScheduleID" value="<c:out value="${massmailInfo.scheduleID}"/>">
	<input type="hidden" id="eUserID" name="eUserID" value="<c:out value="${massmailInfo.userID}"/>">
	<input type="hidden" id="hSendType" name="hSendType" value="<c:out value="${massmailInfo.sendType}"/>">
	<input type="hidden" id="ePollID" name="ePollID" value="<c:out value="${massmailInfo.pollID}"/>">
	
			<div>
				<table border="0" cellpadding="3" class="ctbl" width="100%">
				<tbody>
				<tr>
					<td class="ctbl ttd1 mustinput" width="100px" align="left">대량메일명</td>
					<td class="ctbl td"><input type="text" id="eMassmailTitle" name="eMassmailTitle" value="<c:out value="${massmailInfo.massmailTitle}"/>" size="53" maxlength="50"/></td>
					<td class="ctbl ttd1" width="100px">대량메일설명</td>
					<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${massmailInfo.description}"/>" size="53" maxlength="50"/></td>
				</tr>
				<tr>
					<td colspan="4" class="ctbl line"></td>
				</tr>				
				<tr>
					<td class="ctbl ttd1 mustinput" width="100px">대량메일그룹선택</td>
					<td class="ctbl td" <% if(sendAuth.equals("Y")){%>colspan="3" <%} %>>
						<c:import url="../../../include_inc/massmail_group_inc.jsp">
						<c:param name="mustInput" value="Y"/>			
						<c:param name="massmailGroupID" value="${massmailInfo.massmailGroupID}"/>					
						</c:import>
					</td>
					<% if(sendAuth.equals("N")){%>
						<td class="ctbl ttd1 mustinput" width="100px">발송승인담당자</td>
						<td class="ctbl td">					
						<c:import url="../../../include_inc/approve_list_inc.jsp">
						<c:param name="mustInput" value="Y"/>			
						<c:param name="groupID" value="<%=groupID %>"/>	
						<c:param name="approveUserID" value="${massmailInfo.approveUserID}"/>
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
						<c:param name="approveUserID" value="${massmailInfo.approveUserID}"/>
						</c:import>				
					</td>
				</tr>
				<% } %>
				</tbody>
				</table>				
			</div>
			<div class="right">
				<div class="btn_r"><a id="testEmailMinus22" style ="cursor:pointer" href="javascript:$('<%=id%>').targetWindow()"><span>대상자추가</span></a></div>
			</div>
			<div class="right">
				<div class="btn_b"><a id="testEmailMinus11" style ="cursor:pointer" href="javascript:$('<%=id%>').removeTargetRow()"><span>선택삭제</span></a></div>
			</div>
			<div style="clear:both">
				<div id="<%=id%>_targetList" class="target">
				<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
				<thead>
					<tr>
					<th style="height:30px;width:70px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_form').targetIDCheck,this.checked)"/></th>
					<th style="height:30px;">대상자그룹명</th>
					<th style="height:30px;width:120px">등록구분</th>
					<th style="height:30px;width:120px">총대상인원</th>
					<th style="height:30px;width:120px">추가제외</th>
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
					<c:if test="${targetingGroup.targetType=='6'}">		
						<TD class="tbl_td" align="center">회원정보UI</TD>	
					</c:if>
					<TD><a href="javascript:$('<%=id%>').previewWindow(<c:out value="${targetingGroup.targetID}"/>,3)" title="미리보기"><fmt:formatNumber value="${targetingGroup.targetCount}" type="number"/></a></TD>
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
						<c:if test="${targetingGroup.targetType=='6'}">		
						<TD class="tbl_td" align="center">회원정보UI</TD>	
						</c:if>
					<TD><a href="javascript:$('<%=id%>').previewWindow(<c:out value="${targetingGroup.targetID}"/>,3)" title="미리보기"><fmt:formatNumber value="${targetingGroup.targetCount}" type="number"/></a></TD>
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
			
			
			<%if(filterManagerYN.equals("Y")){ %>
			<div class="right"><div id="<%=id%>_checkContentImg" style="float:left;margin-left:5px"></div></div>
			<div class="right"><a href="javascript:$('<%=id%>').checkContent()"  class="web20button bigpink">검 증</a></div>
			<%} %>	
			<div class="right"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>	
			<%
				//삭제는 예약발송이면서  발송준비대기중,  임시저장, 발송완료, 발송완전중지, 발송준비중오류, 발송중오류 인 경우만 가능 
				if( (isAdmin.equals("Y") || userID.equals(massmailInfo.getUserID()) || (userAuth.equals("2") && massmailInfo.getGroupID().equals(groupID)) ) && ((massmailInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE) && state.equals("11")) || state.equals("00") || state.equals("15") || state.equals("44") || state.equals("32") || state.equals("22") || state.equals("24") || state.equals("33"))){ 
			%>			
				<div class="right"><a href="javascript:$('<%=id%>').deleteData(<c:out value="${massmailInfo.massmailID}"/>,<c:out value="${massmailInfo.scheduleID}"/>)" class="web20button bigpink">삭 제</a></div>
			<%}%>
			<% if( (isAdmin.equals("Y") || userID.equals(massmailInfo.getUserID()) || (userAuth.equals("2") && massmailInfo.getGroupID().equals(groupID))) && ((massmailInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE) && state.equals("11")))){ %>
				<div class="right"><a href="javascript:$('<%=id%>').saveData('<c:out value="${massmailInfo.massmailID}"/>','10')" class="web20button bigpink">수정</a></div>	
			<%} %>
			<% if(state.equals("") || (state.equals("00") && (isAdmin.equals("Y") || userID.equals(massmailInfo.getUserID()) || (userAuth.equals("2") && massmailInfo.getGroupID().equals(groupID))))){ %>
				<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData('<c:out value="${massmailInfo.massmailID}"/>','10')" class="web20button bigpink">발송</a></div>
				<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData('<c:out value="${massmailInfo.massmailID}"/>','00')" class="web20button bigpink">임시저장</a></div>
			<%}%>
			<% if(state.equals("10") &&  userID.equals(massmailInfo.getApproveUserID()) && !massmailInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){ %>
				<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').apprData('<c:out value="${massmailInfo.massmailID}"/>','Y')" class="web20button bigblue">승인</a></div>
				<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').apprData('<c:out value="${massmailInfo.massmailID}"/>','N')" class="web20button bigpink">반려</a></div>
			<%} %>		
			<!-- ////////////////////////////////////////////// -->
	
			<div class="set_wrapper">
				<div class="panel-header">
					<div class="panel-headerContent">
						<div class="toolbarTabs" style="height:28px">				
							<ul id="massmailTabs" class="tab-menu">					
								<li id="tabSendSet" class="selected" ><a>발송설정</a></li>
								<li id="tabFilterSet"><a>필터링</a></li>
								<li id="tabStatisticSet"><a>통계설정</a></li>
								<li id="tabMemoSet"><a>메모</a></li>
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
							<input type="radio" id="eSendType" name="eSendType" class="notBorder" value="0" <c:if test="${massmailInfo.sendType=='0'}">checked</c:if> />테스트발송&nbsp;&nbsp;
							<input type="radio" id="eSendType" name="eSendType" class="notBorder" value="1" <c:if test="${massmailInfo.sendType=='1'}">checked</c:if> />즉시발송&nbsp;&nbsp;
							<input type="radio" id="eSendType" name="eSendType" class="notBorder" value="2" <c:if test="${massmailInfo.sendType=='2'}">checked</c:if> />예약발송&nbsp;&nbsp;
							<input type="radio" id="eSendType" name="eSendType" class="notBorder" value="3" <c:if test="${massmailInfo.sendType=='3'}">checked</c:if> onclick="javascript:$('<%=id%>').clickRepeat()"/>반복발송
							</td>
							<td class="ctbl ttd1" width="100px" style="display:none">발송 우선 순위</td>
							<td class="ctbl td" width="35px" style="display:none">
								<select id="ePriority" name="ePriority">
									<%if(isAdmin.equals("Y")){ %>
									<option value="1" <c:if test="${massmailInfo.priority=='1'}"> selected </c:if>>1</option>
									<%} 
									if(isAdmin.equals("Y") || userAuth.equals("2")){%>
									<option value="2" <c:if test="${massmailInfo.priority=='2'}"> selected </c:if>>2</option>
									<%} %>
									<option value="3" <c:if test="${massmailInfo.priority=='3'}"> selected </c:if><c:if test="${state==''}"> selected </c:if>>3</option>
									<option value="4" <c:if test="${massmailInfo.priority=='4'}"> selected </c:if>>4</option>
									<option value="5" <c:if test="${massmailInfo.priority=='5'}"> selected </c:if>>5</option>
								</select>
							</td>
						</tr>
						</tbody>
						</table>	

						<!-- ###################### 테스트발송 ####################### -->						
						<div class="hidden" id="<%=id %>_sendTestEmail">
							<table class="ctbl" width="100%">
							<tbody>					
							<tr>
								<td class="ctbl ttd1" width="100px" rowspan="2">테스트메일 입력</td>
								<td class="ctbl td">				
								<div class="left"><textarea id="eSendTestEmail" name="eSendTestEmail" style="width:400px;height:50px" ></textarea><br></div>
								<div class="left"><div class="btn_b"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').testerUserWindow()"><span>내 메일 불러오기</span></a></div></div>
								<div class="left"><div class="btn_green"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').testerWindow()"><span>테스트메일불러오기</span></a></div></div>
								<div class="left"><div class="btn_r"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').testMailResultWindow()" ><span>테스트발송내역</span></a></div></div>
								</td>
							</tr>
							<tr>
								<td>
									<img src="images/tag_blue.png" alt="테스트 메일 작성 요령 "> 여러개의 주소를 입력하실 때는 각 주소를 세미콜론(;)으로 구분해 주세요. ex) abc@abc.com;hong@gildong.net
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
										<fmt:parseDate value="${massmailInfo.sendScheduleDate}"  var ="fmt_sendScheduleDate" pattern="yyyy-MM-dd"/>									
										<c:if test="${massmailInfo.sendScheduleDate!=null}">	
											<input type="text" id="eSendSchedule" name="eSendSchedule" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_sendScheduleDate}" pattern="yyyy-MM-dd"/>"/>
										</c:if>
										<c:if test="${massmailInfo.sendScheduleDate==null}">
										<input type="text" id="eSendSchedule" name="eSendSchedule" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getDateString() %>"/>
										</c:if>									
										<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eSendSchedule)" align="absmiddle" />
									</div>
									<div class="left">
										<!-- <ul id="eSendScheduleHH"  class="selectBox">-->
										<select id="eSendScheduleHH" name="eSendScheduleHH">
										<c:forEach var="hours" begin="0" end="23" step="1"> 
												<!-- <li data="<c:out value="${hours}"/>" <c:if test="${massmailInfo.sendScheduleDateHH==hours}">select='Y'</c:if>><c:out value="${hours}"/></li>-->
												<option value="<c:out value="${hours}"/>" <c:if test="${massmailInfo.sendScheduleDateHH==hours}"> selected </c:if>><c:out value="${hours}"/></option>
										</c:forEach>
										</select>								
									    <!-- </ul>-->
									</div>
									<div class="text">시</div>						
									<div class="left">
										<!--<ul id="eSendScheduleMM"  class="selectBox">-->
										<select id="eSendScheduleMM" name="eSendScheduleMM">
										<c:forEach var="minutes" begin="0" end="59" step="10"> 
												<!--<li data="<c:out value="${minutes}"/>" <c:if test="${massmailInfo.sendScheduleDateMM==minutes}">select='Y'</c:if>><c:out value="${minutes}"/></li>-->
												<option value="<c:out value="${minutes}"/>" <c:if test="${massmailInfo.sendScheduleDateMM==minutes}">selected</c:if>><c:out value="${minutes}"/></option>
										</c:forEach>
										</select>							
										<!--</ul>-->
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
								<input type="radio" class="notBorder" id="eRepeatSendType" name="eRepeatSendType" <c:if test="${massmailInfo.repeatSendType=='1'}">checked</c:if> value="1" />매일반복발송 
								<input type="radio" class="notBorder" id="eRepeatSendType" name="eRepeatSendType" <c:if test="${massmailInfo.repeatSendType=='2'}">checked</c:if> value="2" />매주반복발송 
								<input type="radio" class="notBorder" id="eRepeatSendType" name="eRepeatSendType" <c:if test="${massmailInfo.repeatSendType=='3'}">checked</c:if> value="3" />매월반복발송 
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
									<fmt:parseDate value="${massmailInfo.repeatSendStartDate}"  var ="fmt_repeatSendStartDate" pattern="yyyy-MM-dd"/>
									<fmt:parseDate value="${massmailInfo.repeatSendEndDate}"  var ="fmt_repeatSendEndDate" pattern="yyyy-MM-dd"/>	
									<c:if test="${massmailInfo.repeatSendStartDate!=null}">
										<input type="text" id="eRepeatSendStart" name="eRepeatSendStart" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_repeatSendStartDate}" pattern="yyyy-MM-dd"/>"/>
									</c:if>
									<c:if test="${massmailInfo.repeatSendStartDate==null}">	
										<input type="text" id="eRepeatSendStart" name="eRepeatSendStart" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getDateString() %>"/>
									</c:if>											
									<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eRepeatSendStart)" />
									</div>
								</div>	
								<div>
									<div class="text">~</div>	
									<div class="left">			
									<c:if test="${massmailInfo.repeatSendEndDate!=null}">			
										<input type="text" id="eRepeatSendEnd" name="eRepeatSendEnd" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_repeatSendEndDate}" pattern="yyyy-MM-dd"/>"/>										
									</c:if>
									<c:if test="${massmailInfo.repeatSendEndDate==null}">	
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
								<!-- <ul id="eRepeatSendDateHH"  class="selectBox">-->
								<select id="eRepeatSendDateHH" name="eRepeatSendDateHH">
									<c:forEach var="hours" begin="0" end="23" step="1"> 
											<!--<li data="<c:out value="${hours}"/>" <c:if test="${massmailInfo.repeatSendTimeHH==hours}">select='Y'</c:if>><c:out value="${hours}"/></li>-->
											<option value="<c:out value="${hours}"/>" <c:if test="${massmailInfo.repeatSendTimeHH==hours}">selected</c:if>><c:out value="${hours}"/></option>
									</c:forEach>	
								</select>					
								<!-- </ul> -->
								</div>
								<div class="text">시</div>						
								<div class="left">
								<!--<ul id="eRepeatSendDateMM"  class="selectBox">-->
								<select id="eRepeatSendDateMM" name="eRepeatSendDateMM">
									<c:forEach var="minutes" begin="0" end="59" step="10"> 
											<!--<li data="<c:out value="${minutes}"/>" <c:if test="${massmailInfo.repeatSendTimeMM==minutes}">select='Y'</c:if>><c:out value="${minutes}"/></li>-->
											<option value="<c:out value="${minutes}"/>" <c:if test="${massmailInfo.repeatSendTimeMM==minutes}">selected</c:if>><c:out value="${minutes}"/></option>
									</c:forEach>
								</select>						
								<!--</ul>-->	
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
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${massmailInfo.repeatSendWeekSun=='1'}">checked</c:if> value="1" />일 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${massmailInfo.repeatSendWeekMon=='2'}">checked</c:if> value="2" />월 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${massmailInfo.repeatSendWeekTue=='3'}">checked</c:if> value="3" />화 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${massmailInfo.repeatSendWeekWed=='4'}">checked</c:if> value="4" />수 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${massmailInfo.repeatSendWeekThu=='5'}">checked</c:if> value="5" />목 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${massmailInfo.repeatSendWeekFri=='6'}">checked</c:if> value="6" />금 
								<input type="checkbox" class="notBorder" id="eRepeatSendWeek" name="eRepeatSendWeek" <c:if test="${massmailInfo.repeatSendWeekSat=='7'}">checked</c:if> value="7" />토 
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
											<li data="<c:out value="${days}"/>" <c:if test="${massmailInfo.repeatSendDay==days}">select='Y'</c:if>><c:out value="${days}"/></li>
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
								<!--<ul id="eRejectType"  class="selectBox">
									<li data="1" <c:if test="${massmailInfo.rejectType=='1'}">select='Y'</c:if>>전체 수신거부 제외</li>									
									<li data="2" <c:if test="${massmailInfo.rejectType=='2'}">select='Y'</c:if>>해당 대량메일그룹 수신거부 제외</li>
									<li data="3" <c:if test="${massmailInfo.rejectType=='3'}">select='Y'</c:if>>소속그룹 수신거부 제외</li>	
									<li data="4" <c:if test="${massmailInfo.rejectType=='4'}">select='Y'</c:if>>수신 거부제외 안함</li>
								</ul>-->
								<select id="eRejectType" name="eRejectType">
									<option value="1" <c:if test="${massmailInfo.rejectType=='1'}">selected</c:if>>전체 수신거부</option>
									<option value="2" <c:if test="${massmailInfo.rejectType=='2'}">selected</c:if>>해당 대량메일그룹 수신거부</option>
									<option value="3" <c:if test="${massmailInfo.rejectType=='3'}">selected</c:if>>소속그룹 수신거부</option>
									<option value="4" <c:if test="${massmailInfo.rejectType=='4'}">selected</c:if>>수신 거부 안함</option>
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
									<option value="<%=backupDate.getBackupYearMonth() %>" <% if(backupDate.getBackupYearMonth().equals(massmailInfo.getSendedYear()+massmailInfo.getSendedMonth())){%>selected<%} %>><%=DateUtils.reFormat(backupDate.getBackupYearMonth(),"yyyyMM","yyyy년MM월") %></option>
								<%} %>
								</select>
							</div>
							<div class="text">발송통수</div>		
							<div class="left">
									<!--<ul id="eSendedCount"  class="selectBox">-->
									<select id="eSendedCount" name="eSendedCount" <%if(backupDateList.size() == 0){ %>disabled<%} %>>
										<!--<li data="0">--</li>-->
										<option value="0">--</option>
										<c:forEach var="counts" begin="1" end="10" step="1"> 
												<!--<li data="<c:out value="${counts}"/>" <c:if test="${massmailInfo.sendedCount==counts}">select='Y'</c:if>><c:out value="${counts}"/></li>-->
												<option value="<c:out value="${counts}"/>" <c:if test="${massmailInfo.sendedCount==counts}">selected</c:if>><c:out value="${counts}"/></option>
										</c:forEach>	
									</select>
									<!--</ul>--> 							
							</div>	
							<div class="text">회 이상 제외 </div>
							<div class="text"> / </div>			
							<div class="left">
								<input type="checkbox" class="notBorder" id="eNotOpen" name="eNotOpen" value="Y" <c:if test="${massmailInfo.sendedType=='3'}"> checked </c:if>  <c:if test="${massmailInfo.sendedType=='1'}"> checked </c:if> <%if(backupDateList.size() == 0){ %>disabled<%} %>/>
							</div>
							<div class="text">미오픈제외 </div>		
							</td>
						</tr>
						<tr>
							<td class="ctbl ttd1" width="100px">영구 오류자 제한</td>
							<td class="ctbl td">
								<div class="text">영구 오류자 </div>
							<div class="left">
								<!--<ul id="ePersistErrorCount"  class="selectBox">-->
								<select id="ePersistErrorCount" name="ePersistErrorCount">
									<!--<li data="0">--</li>-->
									<option value="0">--</option>
									<c:forEach var="counts" begin="1" end="5" step="1"> 
										<!--<li data="<c:out value="${counts}"/>" <c:if test="${massmailInfo.persistErrorCount==counts}">select='Y'</c:if>><c:out value="${counts}"/></li>-->
										<option value="<c:out value="${counts}"/>" <c:if test="${massmailInfo.persistErrorCount==counts}">selected</c:if>><c:out value="${counts}"/></option>	
									</c:forEach>
								</select>
								<!-- </ul>-->						
							</div>	
							<div class="text">회 이상 제외 </div>
							</td>
						</tr>
						<tr>
							<td class="ctbl ttd1" width="100px">중복허용여부</td>
							<td class="ctbl td">
								<div style="float:left">
								<input type="radio" class="notBorder" id="eDuplicationYN" name="eDuplicationYN" value="Y" <c:if test="${massmailInfo.duplicationYN=='Y'}">checked</c:if> /> 중복 허용
								<input type="radio" class="notBorder" id="eDuplicationYN" name="eDuplicationYN" value="N" <c:if test="${massmailInfo.duplicationYN=='N'}">checked</c:if> /> 중복 제거 (동일한 이메일 주소가 여러개 있을 경우 1통만 발송됩니다.)
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
							<td class="ctbl ttd1" width="100px">통계기간설정</td>
							<td class="ctbl td">
							<div style="float:left">
							<!-- 
								<ul id="eStatisticsType"  class="selectBox" onchange="$('<%=id%>').chgStatisticType(this.value)" >
									<li data="1" <c:if test="${massmailInfo.statisticsType=='1'}">select='Y'</c:if>>발송일부터 1주일까지</li>
									<li data="2" <c:if test="${massmailInfo.statisticsType=='2'}">select='Y'</c:if>>발송일부터 한달까지</li>																		
								</ul>
							--> 
								<input type="radio" class="notBorder" id="eStatisticsType" name="eStatisticsType" value="1" <c:if test="${massmailInfo.statisticsType!='2'}">checked</c:if> /> 발송일부터 1주일까지 
								<input type="radio" class="notBorder" id="eStatisticsType" name="eStatisticsType" value="2" <c:if test="${massmailInfo.statisticsType=='2'}">checked</c:if> /> 발송일부터 한달까지 
							</div>
							</td>
						</tr>
						<tr>
							<td class="ctbl ttd1" width="100px">통계공유여부</td>
							<td class="ctbl td">
							<div class="left">
								<select id="eStatisticsOpenType" name="eStatisticsOpenType">
								<!--<ul id="eStatisticsOpenType"  class="selectBox">
									<li data="1" <c:if test="${massmailInfo.statisticsOpenType=='1'}">select='Y'</c:if>>비공유</li>
									<li data="2" <c:if test="${massmailInfo.statisticsOpenType=='2'}">select='Y'</c:if>>소속그룹공유</li>
									<li data="3" <c:if test="${massmailInfo.statisticsOpenType=='3'}">select='Y'</c:if>>전체공유</li>									
								</ul>-->
								<option value="1" <c:if test="${massmailInfo.statisticsOpenType=='1'}">selected</c:if>>비공유</option>
								<option value="2" <c:if test="${massmailInfo.statisticsOpenType=='2'}">selected</c:if>>소속그룹공유</option>
								<option value="3" <c:if test="${massmailInfo.statisticsOpenType=='3'}">selected</c:if>>전체공유</option>
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
							<textarea id="eMemo" name="eMemo" style="width:100%;height:100px"><c:out value="${massmailInfo.memo}"/></textarea>
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
						<td class="ctbl ttd1 mustinput" width="100px">보내는 이메일</td>
						<td class="ctbl td" colspan="5">
							<c:if test="${massmailInfo.senderMail==''}">	
								<div class="left"><input type="text" id="eSenderMail" name="eSenderMail" value="<%=senderMail %>" size="40" maxlength="30"/></div>
							</c:if>
							<c:if test="${massmailInfo.senderMail!=''}">	
								<div class="left"><input type="text" id="eSenderMail" name="eSenderMail" value="<c:out value="${massmailInfo.senderMail}"/>" size="40" maxlength="30"/></div>
							</c:if>
							<div class="left"><div class="btn_b"><a id="mailLink" style ="cursor:pointer" href="javascript:$('<%=id%>').senderWindow()"><span>보내는사람찾기</span></a></div></div>						
						</td>
					</tr>
					<tr>
						<td colspan="6" class="ctbl line"></td>
					</tr>	
					<tr <%if(returnMailYN.equals("N")){ %>style="display:none" <%} %>>
						<td class="ctbl ttd1 mustinput" width="100px">반송 이메일</td>
						<td class="ctbl td" colspan="5">
							<c:if test="${massmailInfo.returnMail==''}">	
								<div class="left"><input type="text" id="eReturnMail" name="eReturnMail" value="<%=returnMail %>" size="40" maxlength="30"/></div>
							</c:if>
							<c:if test="${massmailInfo.returnMail!=''}">	
								<div class="left"><input type="text" id="eReturnMail" name="eReturnMail" value="<c:out value="${massmailInfo.returnMail}"/>" size="40" maxlength="30"/></div>
							</c:if>						
						</td>
					</tr>
					<tr>
						<td colspan="6" class="ctbl line"></td>
					</tr>						
					<tr>
						<td class="ctbl ttd1 mustinput" width="100px">보내는 사람명</td>
						<td class="ctbl td" colspan="5">				
						<c:if test="${massmailInfo.senderName==''}">
							<input type="text" id="eSenderName" name="eSenderName" value="<%=senderName %>" size="40" maxlength="30"/>
						</c:if>		
						<c:if test="${massmailInfo.senderName!=''}">
							<input type="text" id="eSenderName" name="eSenderName" value="<c:out value="${massmailInfo.senderName}"/>" size="40" maxlength="30"/>						
						</c:if>							
						</td>	
					</tr>
					<tr>
						<td colspan="6" class="ctbl line"></td>
					</tr>
					<tr>
						<td class="ctbl ttd1 mustinput" width="100px">받는 사람명</td>
						<td class="ctbl td" colspan="5">
						<c:if test="${massmailInfo.receiverName==''}">
							<div class="left"><input type="text" id="eReceiverName" name="eReceiverName" value="<%=receiverName %>" size="40" maxlength="30"/></div>
						</c:if>		
						<c:if test="${massmailInfo.receiverName!=''}">
							<div class="left"><input type="text" id="eReceiverName" name="eReceiverName" value="<c:out value="${massmailInfo.receiverName}"/>" size="40" maxlength="30"/></div>
						</c:if>				
						<div class="left"><div class="btn_b"><a id="mailLink" style ="cursor:pointer" href="javascript:$('<%=id%>').onetooneWindow('eReceiverName')"><span>일대일치환삽입</span></a></div></div>							
						</td>											
					</tr>
					<tr>
						<td colspan="6" class="ctbl line"></td>
					</tr>					
					<tr>
						<td class="ctbl ttd1 mustinput" width="100px">메일제목</td>
						<td class="ctbl td" colspan="5">
						<div class="left"><input type="text" id="eMailTitle" name="eMailTitle" value="<c:out value="${massmailInfo.mailTitle}"/>" size="40" maxlength="70"/></div>
						<div class="left"><div class="btn_b"><a id="mailLink" style ="cursor:pointer" href="javascript:$('<%=id%>').onetooneWindow('eMailTitle')"><span>일대일치환삽입</span></a></div></div>
						<div class="text"><img src="images/tag_blue.png"> 메일 제목은 70자 이하로 제한됩니다.</div>						
						</td>						
					</tr>					
					<tr>
						<td colspan="6" class="ctbl line"></td>
					</tr>
					<tr>
						<td class="ctbl ttd1" width="100px">메일 인코딩</td>
						<td class="ctbl td">
						<ul id="eEncodingType"  class="selectBox">
								<li data="EUC-KR" <c:if test="${massmailInfo.encodingType=='EUC-KR'}">select='Y'</c:if>>한국어(EUC-KR)</li>
								<li data="UTF-8" <c:if test="${massmailInfo.encodingType=='UTF-8'}">select='Y'</c:if>>다국어(UTF-8)</li>
						</ul>													
						</td>		
						<td class="ctbl ttd1" width="80px">본문내용</td>
						<td class="ctbl td" width="170px">
						<ul id="eWebURLType"  class="selectBox" onchange="$('<%=id%>').chgWebURLType(this.value)">
							<li data="1" <c:if test="${massmailInfo.webURLType=='1'}">select='Y'</c:if>>HTML작성</li>
							<li data="2" <c:if test="${massmailInfo.webURLType=='2'}">select='Y'</c:if>>URL전송</li>								
						</ul>
						</td>
						<td class="ctbl ttd1" width="100px" ><div id="<%=id %>_attacheTitle" name="<%=id %>_attacheTitle" >첨부파일</div></td>
						<td class="ctbl td" width="170px">
						<div id="<%=id %>_attacheFiles" name="<%=id %>_attacheFiles">
							<%=htmls %>
						</div>
						</td>				
					</tr>					
					</tbody>
					</table>	
					<table class="ctbl" width="100%" id="<%=id %>_mailContentOption" <c:if test="${massmailInfo.webURLType=='2'}">style="display:none" </c:if>>
					<tbody>					
					<tr>
						<td class="ctbl ttd1" width="100px">메일본문링크</td>
						<td class="ctbl td">
						<input type="radio" class="notBorder" id="eMailLinkYN" name="eMailLinkYN" value="N" <c:if test="${massmailInfo.mailLinkYN!='Y'}">checked</c:if> />수집안함&nbsp;&nbsp;&nbsp;
						<input type="radio" class="notBorder" id="eMailLinkYN" name="eMailLinkYN" value="Y" <c:if test="${massmailInfo.mailLinkYN=='Y'}">checked</c:if>/>수집함
						</td>	
						<td class="ctbl ttd1" width="80px">메일본문</td>
						<td class="ctbl td" width="390px" colspan="3">
						<div class="btn_green"><a id="mailTemplateSelect" style ="cursor:pointer" href="javascript:$('<%=id%>').fileimportWindow()"><span>HTML열기</span></a></div>
						<div class="btn_green"><a id="mailTemplateSelect" style ="cursor:pointer" href="javascript:$('<%=id%>').templateWindow(1)"><span>메일템플릿열기</span></a></div>
						<div class="btn_r"><a id="mailPoll" style ="cursor:pointer" href="javascript:$('<%=id%>').attachedFileWindow()"><span>첨부파일삽입</span></a></div>
						<div class="btn_b"><a id="mailLink" style ="cursor:pointer" href="javascript:$('<%=id%>').onetooneWindow('eMailContent')"><span>일대일치환삽입</span></a></div>
						<%if((isPollAuth.equals("Y")||isAdmin.equals("Y")) && isPollYN.equalsIgnoreCase("Y")){ %>						
						<div class="btn_green"><a id="mailPoll" style ="cursor:pointer" href="javascript:$('<%=id%>').pollWindow()"><span>설문링크삽입</span></a></div>
						<%} %>
						<div class="btn_r"><a id="imageUp" style ="cursor:pointer" href="javascript:$('<%=id%>').imageWindow()"><span>이미지삽입</span></a></div>
						</td>					
					</tr>		
					<tr>
						<td colspan="6" class="ctbl line"></td>
					</tr>	
					<tr>
						<td class="ctbl ttd1" width="100px">URL입력</td>
						<td class="ctbl td" colspan="5">						
						<div class="left"><input type="text" id="eWebURLInsert" name="eWebURLInsert" value="" size="100"/></div>
						<div class="left"><div class="btn_b"><a id="urlInsert" style ="cursor:pointer" href="javascript:$('<%=id%>').mailURLInsert()"><span>URL 소스삽입</span></a></div></div>
						</td>						
					</tr>
					<tr>
						<td colspan="6" class="ctbl line"></td>
					</tr>	
					<tr>
						<td class="ctbl td" colspan="6">
						<div class="left"  style="color:red;">
						<img src="images/tag_red.png">주의사항 : 메일작성시 이미지경로와 링크경로가 동일할 경우 이미지가 깨질수 있습니다.</td>
						</div>
					</tr>
					</tbody>
					</table>	
					<table class="ctbl" width="100%" id="<%=id %>_mailURLOption" <c:if test="${massmailInfo.webURLType!='2'}">style="display:none" </c:if>>
					<tbody>					
					<tr>
						<td class="ctbl ttd1" width="100px">URL입력</td>
						<td class="ctbl td">						
						<div class="left"><input type="text" id="eWebURL" name="eWebURL" value="<c:out value="${massmailInfo.webURL}"/>" size="100"/></div>
						<div class="left"><div class="btn_b"><a id="mailLink" style ="cursor:pointer" href="javascript:$('<%=id%>').mailURLWindow()"><span>미리보기</span></a></div></div>
						</td>						
					</tr>		
					</tbody>
					</table>
					<div id="<%=id %>_fck" <c:if test="${massmailInfo.webURLType!='2'}">style="clear:both;width:100%;display:block"</c:if> <c:if test="${massmailInfo.webURLType=='2'}"> style="clear:both;width:100%;display:none" </c:if>>	
					<%if(oldMassmailID !=null && !oldMassmailID.equals("")){ %>
						<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_massmailwrite_iframe.jsp?massmailID=<%=oldMassmailID %>" width="100%" height="500" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
					<%}else{ %>
						<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_massmailwrite_iframe.jsp?massmailID=<c:out value="${massmailInfo.massmailID}"/>" width="100%" height="500" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
					<%}%>
					</div>

					<!-- //////////////////////발송버튼///////////////  -->
					<%if(filterManagerYN.equals("Y")){ %>
					<div class="right"><div id="<%=id%>_checkContentImg" style="float:left;margin-left:5px"></div></div>
					<div class="right"><a href="javascript:$('<%=id%>').checkContent()"  class="web20button bigpink">검 증</a></div>
					<%} %>	
					<div class="right"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>	
					<%
						//삭제는 예약발송이면서  발송준비대기중,  임시저장, 발송완료, 발송완전중지, 발송준비중오류, 발송중오류 인 경우만 가능 
						if( (isAdmin.equals("Y") || userID.equals(massmailInfo.getUserID()) || (userAuth.equals("2") && massmailInfo.getGroupID().equals(groupID)) ) && ((massmailInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE) && state.equals("11")) || state.equals("00") || state.equals("15") || state.equals("44") || state.equals("32") || state.equals("22") || state.equals("24")|| state.equals("33") )){ 
					%>			
						<div class="right"><a href="javascript:$('<%=id%>').deleteData(<c:out value="${massmailInfo.massmailID}"/>,<c:out value="${massmailInfo.scheduleID}"/>)" class="web20button bigpink">삭 제</a></div>
					<%}%>
					<% if( (isAdmin.equals("Y") || userID.equals(massmailInfo.getUserID()) || (userAuth.equals("2") && massmailInfo.getGroupID().equals(groupID))) && ((massmailInfo.getSendType().equals(Constant.SEND_TYPE_RESERVE) && state.equals("11")))){ %>
						<div class="right"><a href="javascript:$('<%=id%>').saveData('<c:out value="${massmailInfo.massmailID}"/>','10')" class="web20button bigpink">수정</a></div>	
					<%} %>
					<% if(state.equals("") || (state.equals("00") && (isAdmin.equals("Y") || userID.equals(massmailInfo.getUserID()) || (userAuth.equals("2") && massmailInfo.getGroupID().equals(groupID))))){ %>
						<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData('<c:out value="${massmailInfo.massmailID}"/>','10')" class="web20button bigpink">발송</a></div>
						<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData('<c:out value="${massmailInfo.massmailID}"/>','00')" class="web20button bigpink">임시저장</a></div>
					<%}%>	
					<% if(state.equals("10") && userID.equals(massmailInfo.getApproveUserID()) && !massmailInfo.getSendType().equals(Constant.SEND_TYPE_REPEAT)){ %>
						<div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').apprData('<c:out value="${massmailInfo.massmailID}"/>','Y')" class="web20button bigblue">승인</a></div>
					    <div class="right"><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').apprData('<c:out value="${massmailInfo.massmailID}"/>','N')" class="web20button bigpink">반려</a></div>
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
	/* 검증
	/***********************************************/
	$('<%=id%>').checkContent = function() {
		var frm = $('<%=id%>_form');
		frm.eMailContent.value = $('<%=id%>_ifrmFckEditor').contentWindow.getFCKHtml();

		if(frm.eMailTitle.value == '') {
			alert('메일 제목은 필수항목입니다.');
			frm.eMailTitle.focus();
			return;
		}

		if(frm.eMailContent.value == '') {
			alert('메일 내용을 입력하세요.');
			return;
		}
		
		nemoWindow(
				{
					'id': '<%=id%>_checkmailresult_modal',
					busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
					width: 800,
					height: $('mainColumn').style.height.toInt(),
					title: '메일 내용 검증 결과 ',
					type: 'modal',
					loadMethod: 'xhr',
					contentURL: 'pages/massmail/write/massmail_check_result.jsp?preID=<%=id%>&id=<%=id%>_checkmailresult_modal&method=checkMail'
				}
			);

		
	}

	/***********************************************/
	/* 저장버튼 클릭 -  저장
	/***********************************************/
	$('<%=id%>').saveData = function( seq, state ) {
		var frm = $('<%=id%>_form');
		//copyForm( $('<%=id%>_rform'), frm );
		
		frm.eMailContent.value = $('<%=id%>_ifrmFckEditor').contentWindow.getFCKHtml();

		if(frm.targetID==undefined){
			alert('대상자그룹을 추가하세요');
			return;
		}

		if(frm.eSendType[0].checked==true){
			if(state == '00'){
				alert("테스트 발송은 임시저장을 지원하지 않습니다. \r발송타입 변경후 임시저장하십시오.");
				return;
			}
			if(frm.eSendTestEmail.value==''){
				alert('테스트메일을 입력하세요');
				frm.eSendTestEmail.focus();
				return;
			}
		}
		if(frm.eSendType[3].checked==true && state=='00'){
			alert("반복메일 설정시에는 임시저장을 하실 수 없습니다.");
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
		
		
		//본문내용이 URL전송이라면
		if(frm.eWebURLType.value == '2' ){
			//메일 본문 링크 수집 여부를 수집안함으로 변경
			$('<%=id%>_form').eMailLinkYN[0].checked=true;
		}

		//필수입력 조건 체크
		//if(!checkFormValue(frm)) {
		//	return;
		//}
		if(frm.eMassmailTitle.value== '') {
				alert('대량메일명은 필수항목입니다.');
				frm.eMassmailTitle.focus();
				return;
		}
		if(frm.eMassmailGroupID.value== '') {
				alert('대량메일그룹선택은 필수항목입니다.');
				return;
		}
		if(state!='00'){
			if(frm.eSenderMail.value== ''){
				alert('보내는 이메일은 필수항목입니다.');
				frm.eSenderMail.focus();
				return;
			}
			if(!CheckEmail(frm.eSenderMail.value)) {
				alert('보내는 이메일 형식이 틀립니다');
				frm.eSenderMail.focus();
				return;
			}
			if(frm.eSenderName.value == '') {
				alert('보내는 사람 명은 필수항목입니다.');
				frm.eSenderMail.focus();
				return;
			}
			if(frm.eReceiverName.value == '') {
				alert('받는 사람 명은 필수항목입니다.');
				frm.eReceiverName.focus();
				return;
			}
			if(frm.eMailTitle.value == '') {
				alert('메일 제목은 필수항목입니다.');
				frm.eMailTitle.focus();
				return;
			}
			
		}

		// 대량메일명의 < , script , /> 등 스크립트 사용 의심 문자 필터	
		if ( frm.eMassmailTitle.value.indexOf('<') >= 0 || frm.eMassmailTitle.value.indexOf('/>') >= 0 || frm.eMassmailTitle.value.indexOf('script') >= 0 ){
			alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
			return;
		}
		
		frm.eSaveState.value = state;

		var methodInsertType = "";
		var methodUpdateType = "";

	
		if(seq =='' || seq == '0' ) {
			if(frm.eSendType[0].checked==true){
				goUrl = 'massmail/write/massmail.do?id=<%=id%>&method=sendTestMail';
			}else{
				goUrl = 'massmail/write/massmail.do?id=<%=id%>&method=insert';
			}
		} else {
			if(frm.eSendType[0].checked==true){
				goUrl = 'massmail/write/massmail.do?id=<%=id%>&method=sendTestMail';
			}else{
				goUrl = 'massmail/write/massmail.do?id=<%=id%>&method=update';
			}
		}

			nemoRequest.init( 
			{			
					busyWindowId: '<%=id%>'  // busy 를 표시할 window
					//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window
				
					, url: goUrl
					//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
					, onSuccess: function(html,els,resHTML) {
						if(resHTML.indexOf("||")>0)
						{
							var massmailID_chk = resHTML.substring(0,resHTML.indexOf("||"));
							if(seq == '0')
							{
								closeWindow( $('<%=id%>') );
								nemoWindow(
									{
										'id': '<%=id%>',
										busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
										width: 900,
										height: $('mainColumn').style.height.toInt(),
										//height: 600,
										title: '대량메일작성',
										type: 'modal',
										loadMethod: 'xhr',
										contentURL: 'massmail/write/massmail.do?id=<%=id%>&method=edit&massmailID='+massmailID_chk+'&scheduleID=1&state=00'
									}
								);
							}
							
							MochaUI.massmailListWindow();
							
						}
						else
						{
							var gubun = resHTML.indexOf("|");
							var massmailID = resHTML.substring(0,gubun);
							var state = resHTML.substring(gubun+1,resHTML.length);

							if(frm.eSendType[0].checked==false && frm.eMailLinkYN[1].checked==true && massmailID!='0'&& state!='00'){											
								$('<%=id%>').mailLinkWindow(massmailID,state);
							}else{
								if(frm.eSendType[0].checked==false ){
								
									if(state!='00'){
										closeWindow( $('<%=id%>') );
								
									//만약 리스트에서 뛰우게 한다면 아래부분을 구분파라미터를 을 주어서 처리해야 한다. 				
										MochaUI.massmailListWindow();
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
													title: '대량메일작성',
													type: 'modal',
													loadMethod: 'xhr',
													contentURL: 'massmail/write/massmail.do?id=<%=id%>&method=edit&massmailID='+massmailID+'&scheduleID=1&state=00'
												}
											);
										}
										alert('임시저장이 완료 되었습니다.');
										MochaUI.massmailListWindow();
									}	
								}else{
									$('<%=id%>').testMailResultWindow();
								}

							}
						}
					}
			});
		
		nemoRequest.post(frm);
		
		
	}


	/***********************************************/
	/* 승인 / 반려 버튼 클릭 
	/***********************************************/
	$('<%=id%>').apprData = function( seq, state ) {
		var message = "메일 발송을 승인 하시겠습니까?";
		if(state == 'N'){
			message = "메일 발송을 반려 하시겠습니까?";
		}
			
		if(!confirm(message)) return;
		var frm = $('<%=id%>_form');
		frm.eMailContent.value = $('<%=id%>_ifrmFckEditor').contentWindow.getFCKHtml();

		if(frm.targetID==undefined){
			alert('대상자그룹을 추가하세요');
			return;
		}

		if(frm.eSendType[0].checked==true){
			if(frm.eSendTestEmail.value==''){
				alert('테스트메일을 입력하세요');
				frm.eSendTestEmail.focus();
				return;
			}
		}
		if(frm.eSendType[3].checked==true && state=='00'){
			alert("반복메일 설정시에는 임시저장을 하실 수 없습니다.");
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
		
		if(frm.eMassmailTitle.value== '') {
				alert('대량메일명은 필수항목입니다.');
				frm.eMassmailTitle.focus();
				return;
		}
		if(state!='00'){
			if(frm.eSenderMail.value== ''){
				alert('보내는 이메일은 필수항목입니다.');
				frm.eSenderMail.focus();
				return;
			}
			if(!CheckEmail(frm.eSenderMail.value)) {
				alert('보내는 이메일 형식이 틀립니다');
				frm.eSenderMail.focus();
				return;
			}
			if(frm.eSenderName.value == '') {
				alert('보내는 사람 명은 필수항목입니다.');
				frm.eSenderMail.focus();
				return;
			}
			if(frm.eReceiverName.value == '') {
				alert('받는 사람 명은 필수항목입니다.');
				frm.eReceiverName.focus();
				return;
			}
			if(frm.eMailTitle.value == '') {
				alert('메일 제목은 필수항목입니다.');
				frm.eMailTitle.focus();
				return;
			}
			
		}



		frm.eSaveState.value = state;
		frm.eStateValue.value = '10';

		var methodInsertType = "";
		var methodUpdateType = "";

	
		if(seq =='' || seq == '0' ) {
			if(frm.eSendType[0].checked==true){
				goUrl = 'massmail/write/massmail.do?id=<%=id%>&method=sendTestMail';
			}else{
				goUrl = 'massmail/write/massmail.do?id=<%=id%>&method=insert';
			}
		} else {
			if(frm.eSendType[0].checked==true){
				goUrl = 'massmail/write/massmail.do?id=<%=id%>&method=sendTestMail';
			}else{
				goUrl = 'massmail/write/massmail.do?id=<%=id%>&method=update';
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
						var massmailID = resHTML.substring(0,gubun);
						var state = resHTML.substring(gubun+1,resHTML.length);

						if(frm.eMailLinkYN[1].checked==true && massmailID!='0'&& state!='00'){											
							$('<%=id%>').mailLinkWindow(massmailID,state);
						}else{
							if(frm.eSendType[0].checked==false){
								if(state!='00'){
									alert('승인이 완료 되었습니다.');
								}else{
									alert('반려가  완료 되었습니다.');
								}	
								closeWindow( $('<%=id%>') );
								MochaUI.massmailListWindow();
							}else{
								$('<%=id%>').testMailResultWindow();
							}

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

		var massmailID = frm.eMassmailID.value;
		var scheduleID = frm.eScheduleID.value;
		var sendType = frm.hSendType.value;
		
		if(!confirm("정말로 삭제 하시겠습니까?  \r\n삭제하시면 관련된 모든 데이타는 영구삭제가 됩니다.")) return;
		
		nemoRequest.init( 
		{
			busyWindowId: '<%=id%>'  // busy 를 표시할 window
			//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

			, url: 'massmail/write/massmail.do?method=deleteMassMailAll&id=<%=id%>&eMassmailID='+ massmailID+'&eScheduleID='+scheduleID+'&eSendType='+sendType
			, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
			, onSuccess: function(html,els,resHTML) {
				closeWindow( $('<%=id%>') );
				MochaUI.massmailListWindow();	
				
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
			$('<%=id %>_sendTestEmail').setStyle('display','none');
	};
	//예약발송
	$('<%=id%>').sendCheckedReserved = function(){	
			var frm = $('<%=id%>_form');			
			$(frm.eSendType[2]).checked=true;			
			$('<%=id %>_sendScheduleDate').setStyle('display','block');
			$('<%=id %>_repeatSendSet').setStyle('display','none');
			$('<%=id %>_statisticSet').setStyle('display','none');
			$('<%=id %>_memoSet').setStyle('display','none');
			$('<%=id %>_sendTestEmail').setStyle('display','none');
	};

	//웹URL타입 
	$('<%=id%>').chgWebURLType = function(val) {
		if(val=='2'){
			$('<%=id%>_fck').setStyle('display','none');
			$('<%=id %>_mailURLOption').setStyle('display','block');
			$('<%=id %>_mailContentOption').setStyle('display','none');
		}else{
			$('<%=id%>_fck').setStyle('display','block');
			$('<%=id %>_mailURLOption').setStyle('display','none');
			$('<%=id %>_mailContentOption').setStyle('display','block');
		}
	}



	

	/***********************************************/
	/* 초기화
	/***********************************************/

	window.addEvent("domready",function() {
		
		var frm = $('<%=id%>_form');

		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
		MochaUI.initializeTabs('massmailTabs');


		//발송설정 탭클릭
		$('tabSendSet').addEvent('click', function(){
			$('<%=id %>_sendTypeSet').setStyle('display','block');
			$('<%=id %>_filterSet').setStyle('display','none');
			$('<%=id %>_statisticSet').setStyle('display','none');
			$('<%=id %>_memoSet').setStyle('display','none');
		});


		//필터링설정 탭클릭
		$('tabFilterSet').addEvent('click', function(){
			$('<%=id %>_sendTypeSet').setStyle('display','none');
			$('<%=id %>_filterSet').setStyle('display','block');
			$('<%=id %>_statisticSet').setStyle('display','none');
			$('<%=id %>_memoSet').setStyle('display','none');
		});

		//통계설정 탭클릭
		$('tabStatisticSet').addEvent('click', function(){
			$('<%=id %>_sendTypeSet').setStyle('display','none');
			$('<%=id %>_filterSet').setStyle('display','none');
			$('<%=id %>_statisticSet').setStyle('display','block');
			$('<%=id %>_memoSet').setStyle('display','none');
		});


		//메모  탭클릭
		$('tabMemoSet').addEvent('click', function(){
			$('<%=id %>_sendTypeSet').setStyle('display','none');
			$('<%=id %>_filterSet').setStyle('display','none');
			$('<%=id %>_statisticSet').setStyle('display','none');
			$('<%=id %>_memoSet').setStyle('display','block');
		});

		
		//테스트발송
		$(frm.eSendType[0]).addEvent('click', function(){
			$('<%=id %>_sendScheduleDate').setStyle('display','none');
			$('<%=id %>_repeatSendSet').setStyle('display','none');
			$('<%=id %>_sendTestEmail').setStyle('display','block');
		});	

		//즉시발송 
		$(frm.eSendType[1]).addEvent('click',$('<%=id%>').sendChecked);


		//예약발송 
		$(frm.eSendType[2]).addEvent('click', function(){	
			$('<%=id %>_repeatSendSet').setStyle('display','none');
			$('<%=id %>_sendScheduleDate').setStyle('display','block');
			$('<%=id %>_sendTestEmail').setStyle('display','none');
		});	

		//반복발송 
		$(frm.eSendType[3]).addEvent('click', function(){
			$('<%=id %>_sendScheduleDate').setStyle('display','none');
			$('<%=id %>_repeatSendSet').setStyle('display','block');
			$('<%=id %>_sendTestEmail').setStyle('display','none');
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
		$('<%=id%>').isCheckContent = false;
		<%%>
		<% if(method.equals("edit")||!massmailInfo.getSendType().equals("1") ){%>		
			//즉시발송
			if(frm.eSendType[1].checked==true){
				$('<%=id%>').sendChecked();
			}	
			//예약발송
			else if(frm.eSendType[2].checked==true){
				$('<%=id %>_repeatSendSet').setStyle('display','none');
				$('<%=id %>_sendScheduleDate').setStyle('display','block');
				$('<%=id %>_sendTestEmail').setStyle('display','none');
			}
			else if(frm.eSendType[3].checked==true){
				$('<%=id %>_repeatSendSet').setStyle('display','block');
				$('<%=id %>_sendScheduleDate').setStyle('display','none');
				$('<%=id %>_sendTestEmail').setStyle('display','none');

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

		<%// if(method.equals("edit")){%>
		<%//if(massmailInfo.getSendType().equals("1")){%>
			//$('<%=id%>').sendChecked();
		<%//}else if(massmailInfo.getSendType().equals("2")){
		//else if((request.getParameter("eSendScheduleDateStart")!=null&&!(request.getParameter("eSendScheduleDateStart").equals("")))){%>
			//$('<%=id%>').sendCheckedReserved();
		<%//}else{%>
			//$('<%=id%>').sendCheckedReserved();
		<%//}%>

		
		
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
				contentURL: 'pages/massmail/write/massmail_target.jsp?id=<%=id%>&method=targeting'
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
				title: '테스트메일  선택',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/write/massmail_testmail.jsp?preID=<%=id%>&id=<%=id%>_tester_modal&method=tester'
			}
		);
		
	}


	/***********************************************/
	/* 나에게 버튼 클릭 - 
	/***********************************************/
	$('<%=id%>').testerUserWindow = function() {

		var frm = $('<%=id%>_form');
		
		frm.eSendTestEmail.value = frm.eSendTestEmail.value + '<%=user.getEmail()%>'+';';

	}


	/***********************************************/
	/* 메일링크 열기창 
	/***********************************************/
	$('<%=id%>').mailLinkWindow = function(massmailID,saveState) {

		
		nemoWindow(
			{
				'id': '<%=id%>_link_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 900,
				height: $('mainColumn').style.height.toInt()-100,
				//height: 600,
				title: '메일링크 ',
				//type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/write/massmail_link.jsp?id=<%=id%>_link_modal&method=massmailLink&massmailID='+massmailID+'&state='+saveState
			}
		);
		
	}


	/***********************************************/
	/* 메일URL 열기 창 
	/***********************************************/
	$('<%=id%>').mailURLWindow = function() {

		var frm = $('<%=id%>_form');

		var webURL = frm.eWebURL.value;
		var encodingType = frm.eEncodingType.value;
		
		if(webURL==null || webURL==''){
			alert('메일URL를 입력하세요');
			frm.eWebURL.focus();
			return;			
		}
		
		nemoWindow(
			{
				'id': '<%=id%>_url_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 800,
				//height: $('mainColumn').style.height.toInt(),
				height: 600,
				title: 'URL 미리보기 ',
				type: 'modal',
				loadMethod: 'iframe',
				contentURL: 'pages/massmail/write/massmail_urlview.jsp?encodingType='+encodingType+'&webURL='+webURL
			}
		);
		
	}


	/***********************************************/
	/* URL 소스 삽입 
	/***********************************************/
	$('<%=id%>').mailURLInsert = function() {

		var frm = $('<%=id%>_form');
		var eWebURLInsert = frm.eWebURLInsert.value;
		var encodingType = frm.eEncodingType.value;

		if(eWebURLInsert==null || eWebURLInsert==''){
			alert('메일URL를 입력하세요');
			frm.eWebURLInsert.focus();
			return;			
		}		

		nemoWindow(
			{
				'id': '<%=id%>_urlinsert_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 300,
				height: 300,
				title: 'URL 미리보기 ',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/write/massmail_urlinsert.jsp?preID=<%=id%>&id=<%=id%>_urlinsert_modal&encodingType='+encodingType+'&eWebURLInsert='+eWebURLInsert
			}
		);
		
	}

	
	/***********************************************/
	/* 테스트메일 결과 창 
	/***********************************************/
	$('<%=id%>').testMailResultWindow = function() {

		var frm = $('<%=id%>_form');
		
		nemoWindow(
			{
				'id': '<%=id%>_testmailresult_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 800,
				//height: $('mainColumn').style.height.toInt(),
				height: 600,
				title: '테스트메일발송내역 ',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/write/massmail_testmail_sendresult.jsp?id=<%=id%>_testmailresult_modal&method=sendedTestMail'
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
				contentURL: 'pages/massmail/write/massmail_onetoone.jsp?id=<%=id%>_onetoone_modal&method=onetoone&preID=<%=id%>&targetIDs='+targetIDs+'&textName='+textName
			}
		);
		
	}

	/***********************************************/
	/* HTML열기
	/***********************************************/
	
	$('<%=id%>').fileimportWindow = function(){
		var frm = $('<%=id%>_form');

		nemoWindow(
				{
					'id': '<%=id%>_file_import',
					busyEL:'<%=id%>', // 창을 열기까지 busy가 표시 될 element

					width: 640,
					height: 200,
					title:'HTML열기',
					type:'modal',
					loadMethod:'xhr',
					contentURL:'pages/massmail/write/massmail_fileimport.jsp?id=<%=id%>_file_import&method=fileImportForm&preID=<%=id%>'
				}
		);	
	}

	
	/***********************************************/
	/* 메일템플릿열기
	/***********************************************/
	$('<%=id%>').templateWindow = function(seq) {

		var frm = $('<%=id%>_form');
		
		nemoWindow(
			{
				'id': '<%=id%>_template_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 510,
				//height: $('mainColumn').style.height.toInt(),
				height: 300,
				title: '메일템플릿 ',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/write/massmail_template.jsp?id=<%=id%>_template_modal&method=mailTemplate&preID=<%=id%>&templateType=' + seq
			}
		);
		
	}


	/***********************************************/
	/* 설문열기 
	/***********************************************/
	$('<%=id%>').pollWindow = function(){
		var frm = $('<%=id%>_form');
		
		nemoWindow(
			{
				'id': '<%=id%>_poll_modal',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: 600,
				//height: $('mainColumn').style.height.toInt(),
				height: 300,
				title: '설문열기 ',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/write/massmail_poll.jsp?id=<%=id%>_poll_modal&method=poll&preID=<%=id%>'
			}
		);
	}


	/***********************************************/
	/* 첨부파일열기
	/***********************************************/
	$('<%=id%>').attachedFileWindow = function() {

		var frm = $('<%=id%>_form');
		
		nemoWindow(
			{
				'id': '<%=id%>_file_modal',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: 720,
				//height: $('mainColumn').style.height.toInt(),
				height: 300,
				title: '첨부파일 ',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/write/massmail_file.jsp?id=<%=id%>_file_modal&method=attachedFile&preID=<%=id%>'
			}
		);		
	}

	/***********************************************/
	/* 이미지 업로드 창 열기
	/***********************************************/
	$('<%=id%>').imageWindow = function() {

		var frm = $('<%=id%>_form');
		
		nemoWindow(
			{
				'id': '<%=id%>_image_modal',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: 570,
				//height: $('mainColumn').style.height.toInt(),
				height: 300,
				title: '이미지 업로드 ',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/write/massmail_image.jsp?id=<%=id%>_image_modal&method=imageFile&preID=<%=id%>'
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
				contentURL: 'pages/massmail/write/massmail_sender.jsp?id=<%=id%>_sender_modal&method=sender&preID=<%=id%>'
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

	$('<%=id%>').clearAttach = function(){
		$('<%=id%>_attacheFiles').innerHTML='';

	}
	
	/***********************************************/
	/*대상자  미리보기  창 열기
	/***********************************************/
	$('<%=id%>').previewWindow = function( seq, ing ) {

		

		var frm = $('<%=id%>_list_form');
		
		nemoWindow(
				{
					'id': '<%=id%>_preview_modal',

					busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

					width: 700,
					height: $('mainColumn').style.height.toInt(),
					title: '대상자 미리보기',
					type: 'modal',
					loadMethod: 'xhr',
					contentURL: 'pages/target/targetlist/target_preview_list.jsp?method=preview&id=<%=id%>_preview_modal&targetID='+seq
					
				}
			);
		
	}
	
</script>