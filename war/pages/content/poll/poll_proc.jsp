<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.LoginInfo"%>
<%@ page import="web.common.util.*" %> 
<%@ page import="web.content.poll.control.PollControlHelper" %>
<%@ page import="web.content.poll.service.PollService" %>
<%@ page import="web.content.poll.model.*" %>
<%

PollService pollservice = PollControlHelper.getUserService(application);

String id = request.getParameter("id");
String method = request.getParameter("method");
String shareGroupID = request.getParameter("shareGroupID");
String codeNo =  request.getParameter("codeNo");
String codeID =  request.getParameter("codeID");
String pollID =  request.getParameter("pollID")== null ? "0" : request.getParameter("pollID");
String pollTemplateID = request.getParameter("pollTemplateID")== null ? "0" : request.getParameter("pollTemplateID");
String usingYN = ServletUtil.getParamStringDefault(request,"usingYN","N");
String groupID = request.getParameter("groupID");
String pollUserGroupID = pollservice.selectGroupID(pollID);

//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
%>

	
	
<%@page import="web.content.poll.service.PollService"%>
<%@page import="web.content.poll.control.PollControlHelper"%><jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<c:forEach items="${pollInfoList}" var="pollInfo">
	<TR class="tbl_tr" tr_pollID="<c:out value="${pollInfo.pollID}"/>"  tr_sharegroupID="<c:out value="${pollInfo.shareGroupID}"/>" tr_codeNo="<c:out value="${pollInfo.codeNo}"/>" tr_pollTemplateID="<c:out value="${pollInfo.pollTemplateID}"/>">
		<TD class="tbl_td" align='center'>
		<input type="checkbox" class="notBorder" id="ePollID" name="ePollID" value="<c:out value="${pollInfo.pollID}" />" />
		</TD>
		<TD class="tbl_td"><c:out value="${pollInfo.pollID}"/></TD>					
		<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${pollInfo.pollID}"/>','<c:out value="${pollInfo.shareGroupID}"/>','<c:out value="${pollInfo.codeNo}"/>','<c:out value="${pollInfo.pollTemplateID}"/>')"><c:out value="${pollInfo.pollTitle}" escapeXml="true"/></a></b></TD>			
		<TD class="tbl_td"><c:out value="${pollInfo.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${pollInfo.groupName}" /></TD>	
		<TD class="tbl_td"><c:out value="${pollInfo.useYN}"/></TD>
		<TD class="tbl_td"><c:out value="${pollInfo.resultFinishYN}"/></TD>		
		<TD class="tbl_td"><c:out value="${pollInfo.registDate}"/></TD>
		<TD class="tbl_td">
			<c:if test="${pollInfo.pollEndType=='1' || pollInfo.pollEndType=='3'}">
				마감일
			</c:if>
			<c:if test="${pollInfo.pollEndType=='3'}">
				<br/>
			</c:if>
			<c:if test="${pollInfo.pollEndType=='2' || pollInfo.pollEndType=='3'}">
				목표응답수
			</c:if>
		</TD>
		<TD class="tbl_td">
		<c:if test="${pollInfo.pollEndType=='1' || pollInfo.pollEndType=='3'}">
			<c:out value="${pollInfo.pollEndDate}"/>
		</c:if>
		<c:if test="${pollInfo.pollEndType=='3'}">
			<br/>
		</c:if>
		<c:if test="${pollInfo.pollEndType=='2' || pollInfo.pollEndType=='3'}">
			<c:out value="${pollInfo.aimAnswerCnt}"/> 건
		</c:if>
		</TD>
	</TR>
	</c:forEach>
	
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
				//,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			
		});

	</script>
<%
}
//****************************************************************************************************/
//  편집 
//****************************************************************************************************/
if(method.equals("edit")) {
	if(shareGroupID.equals("undefined")){
		shareGroupID = "NOT";
	}
%>	
	<div id="<%=id%>_divInfo">
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_info_form" name="<%=id%>_info_form" method="post">
		<input type="hidden" id="eShareGroupID" name="eShareGroupID">		
			<input type="hidden" id="ePollID" name="ePollID" value="<c:out value="${pollInfo.pollID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">설문제목</td>
				<td class="ctbl td">
				<input type="text" id="ePollTitle" name="ePollTitle" value="<c:out value="${pollInfo.pollTitle}"/>" size="50" mustInput="Y" msg="설문제목을  입력"/>
				</td>
			</tr>			
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">설명</td>
				<td class="ctbl td">
				<input type="text" id="eDescription" name="eDescription" value="<c:out value="${pollInfo.description}"/>" size="50" msg="설문제목을  입력"/>
				</td>
			</tr>			
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>			
			<tr>
				<td class="ctbl ttd1" width="100px">설문유형</td>
				<td class="ctbl td">
					<jsp:include page="./poll_code_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="Y" />
						<jsp:param name="codeID" value="100" />
						<jsp:param name="codeNo" value="<%=codeNo %>" />
					</jsp:include>
				</td>
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>									
			<tr>				
				<td class="ctbl ttd1" width="100px">설문템플릿선택</td>
				<td class="ctbl td">				
				<table>
				<tr>
				<td class="left">
				<jsp:include page="./poll_template_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="Y" />
						<jsp:param name="id" value="<%=id %>" />
						<jsp:param name="pollTemplateID" value="<%=pollTemplateID %>" />
				</jsp:include>
				</td>
				<td class="left">									
				<div class="btn_green"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').previewPollTemplate()"><span>미리보기</span></a></div>
				</td>
				</tr>
				</table>
				</td>		
			</tr>
			<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">공유그룹</td>
				<td class="ctbl td">
					<jsp:include page="../../../include_inc/selectgroup_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="Y" />
						<jsp:param name="groupID" value="<%=shareGroupID %>" />
					</jsp:include>
				</td>
			</tr>	
			<%}else { %>
			<c:if test="${pollInfo.userID==who || pollInfo.userID==null}">
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">공유그룹</td>
				<td class="ctbl td">
					<jsp:include page="../../../include_inc/selectgroup_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="Y" />
						<jsp:param name="groupID" value="<%=shareGroupID %>" />
					</jsp:include>					
					
				</td>
			</tr>
			</c:if>	
										
			<%} %>
				
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1">사용여부</td>
				<td class="ctbl td" >
					<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${pollInfo.useYN=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${pollInfo.useYN=='N'}">select='YES'</c:if>>N</li>
						
					</ul>
					<%}else { %>
					<c:if test="${pollInfo.userID==who || pollInfo.userID==null}">
					<ul id="eUseYN"  class="selectBox">
						<li data="Y" <c:if test="${pollInfo.useYN=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${pollInfo.useYN=='N'}">select='YES'</c:if>>N</li>
						
					</ul>			
					</c:if>
					<c:if test="${pollInfo.userID!=who}">
					<c:out value="${pollInfo.useYN}"/>
					</c:if>					
					<%} %>					
				</td>
			</tr>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1">설문종료조건</td>
				<td class="ctbl td" >
					<div style="float:left">
					<input type="checkbox" class="notBorder" id="ePollEndType" name="ePollEndType"  value="1" onclick="javascript:$('<%=id%>').changePollEndType()" <c:if test="${pollInfo.pollEndType == '1' || pollInfo.pollEndType == '3'}" > checked </c:if>>설문 마감 일&nbsp;
					<input type="checkbox" class="notBorder" id="ePollEndType" name="ePollEndType"  value="2" onclick="javascript:$('<%=id%>').changePollEndType()" <c:if test="${pollInfo.pollEndType == '2' || pollInfo.pollEndType == '3'}" > checked </c:if>>목표 응답 수
					</div>
					<c:if test="${pollInfo.pollID!='0'}">			
						<div style="float:left;margin-left:10px" class="btn_r"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').changePollEnd()"><span>설문종료조건변경</span></a></div>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr id="pollEndDateTR" style="display:none">
				<td class="ctbl ttd1">설문마감일</td>
				<td class="ctbl td" >
				<div style="float:left">
				<fmt:parseDate value="${pollInfo.pollEndDate}"  var ="fmt_pollEndDate" pattern="yyyy-MM-dd"/>									
				<c:if test="${pollInfo.pollEndDate!=null}">	
					<input type="text" id="ePollEndDate" name="ePollEndDate" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_pollEndDate}" pattern="yyyy-MM-dd"/>"/>
				</c:if>
				<c:if test="${pollInfo.pollEndDate==null}">
					<input type="text" id="ePollEndDate" name="ePollEndDate" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getNowAddShortDate(7) %>"/>
				</c:if>									
					<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_info_form').ePollEndDate)" align="absmiddle" />
				</div>
				<div style="float:left;padding-left:5px">
				<select id="ePollEndDateHH" name="ePollEndDateHH">
				<c:forEach var="hours" begin="0" end="23" step="1"> 
				<option value="<c:out value="${hours}"/>" <c:if test="${pollInfo.pollEndDateHH==hours}"> selected </c:if>><c:out value="${hours}"/></option>
				</c:forEach>
				</select>								
				</div>
				<div style="float:left;padding-top:3px;padding-left:3px">시</div>						
				<div style="float:left;padding-left:5px">
				<select id="ePollEndDateMM" name="ePollEndDateMM">
				<c:forEach var="minutes" begin="0" end="59" step="10"> 
				<option value="<c:out value="${minutes}"/>" <c:if test="${pollInfo.pollEndDateMM==minutes}">selected</c:if>><c:out value="${minutes}"/></option>
				</c:forEach>
				</select>							
				</div>		
				<div style="float:left;padding-top:3px;padding-left:3px">분</div>					
				</td>
			</tr>			
			<tr id="pollEndDateLine" style="display:none">
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr id="aimAnswerCntTR" style="display:none">
				<td class="ctbl ttd1">목표 응답 수</td>
				<td class="ctbl td" >
					<input type="text" id="eAimAnswerCnt" name="eAimAnswerCnt" size="10" maxlength="10" value="<c:if test="${pollInfo.aimAnswerCnt!='null'}"><c:out value="${pollInfo.aimAnswerCnt}" /></c:if>" /> 건
				</td>
			</tr>
			<tr id="aimAnswerCntLine" style="display:none">
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1">저장구분</td>
				<td class="ctbl td" >
					<div style="float:left">	
					<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2")){%>
					<ul id="eResultFinishYN"  class="selectBox">
						<li data="N" <c:if test="${pollInfo.resultFinishYN=='N'}">select='YES'</c:if>>임시저장</li>
						<li data="Y" <c:if test="${pollInfo.resultFinishYN=='Y'}">select='YES'</c:if>>설문완료</li>
					</ul>
					<%}else { %>
					<c:if test="${pollInfo.userID==who || pollInfo.userID==null}">
					<ul id="eResultFinishYN"  class="selectBox">
						<li data="N" <c:if test="${pollInfo.resultFinishYN=='N'}">select='YES'</c:if>>임시저장</li>
						<li data="Y" <c:if test="${pollInfo.resultFinishYN=='Y'}">select='YES'</c:if>>설문완료</li>
					</ul>			
					</c:if>
					<%} %>	
					</div>
					<div style="float:left;margin-left:20px;margin-top:5px">
								<a title="<div style='text-align:left'>												
									<img src='images/tag_red.png'><strong>설명</strong><br>
									&nbsp; 임시저장 : 설문을 수정하거나 문항을 추가/수정/삭제할 수 있습니다.<br>										
									&nbsp; 설문완료 : 설문완료로 저장하면 대량메일작성에서 해당 설문을 사용할 수 있습니다.<br>
									&nbsp; 설문완료일때 수정은 불가하며 다시 임시저장으로 해야 수정이 가능합니다.<br>
									&nbsp; 단, 대량메일에서 사용중이라면 설문완료에서 임시저장으로 변경할 수 없습니다.<br>
									</div>">
									<img src='images/tag_blue.png'> <strong>저장구분?</strong>
								</a> 
					</div>											
				</td>
			</tr>
				
			<%if(usingYN.equals("Y")){ %>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>				
			<tr>
				<td class="ctbl ttd1" colspan="2">현재 해당 설문은 대량메일에서 사용중이기 때문에 수정 및 삭제가 불가능합니다.</td>
			</tr>
			<%} %>				
			</tbody>

			</table>
		</form>
	</div>	
	<div>	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_info'))"  class="web20button bigpink">닫 기</a></div>
	<% if(usingYN.equals("N")){ %>
	<%if(LoginInfo.getIsAdmin(request).equals("Y") || LoginInfo.getUserAuth(request).equals("2") ){%>
	
	<c:if test="${pollInfo.pollID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData('<c:out value="${pollInfo.pollID}"/>')" class="web20button bigpink">삭 제</a></div>
	</c:if>
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData()" class="web20button bigblue">저 장</a></div>
	
	
	<%}else { %>
	<c:if test="${pollInfo.userID==who || pollInfo.userID==null}">
	<c:if test="${pollInfo.pollID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData()" class="web20button bigpink">삭 제</a></div>
	</c:if>
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData()" class="web20button bigblue">저 장</a></div>
	</c:if>	

	<%} %>
	<%} %>
	</div>	
	<div id="<%=id %>_pollPreview" style="float:left;padding-top:10px;display:none;width:100%">
	<table width="100%">
	<tbody>
	
	<tr>
		<td>							
	
		<div id="<%=id %>_pollButton" style="display:block">		
		<div style="float:left;padding-right:5px" class="btn_green"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').openTitle('startTitle')"><span>시작문구수정</span></a></div>		
		<div style="float:left;padding-right:5px" class="btn_b"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').openTitle('endTitle')"><span>종료문구수정</span></a></div>
		<div style="float:left;padding-right:5px" class="btn_r"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').openQuestionMake()"><span>문항등록/수정</span></a></div>
		<div style="float:left;padding-right:5px" class="btn_r"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').openSkipPattern()"><span>스킵패턴설정</span></a></div>		
		</div>		
		<% if(usingYN.equals("N")){ %>
		<div style="float:right" class="btn_r"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').openPollHTMLEdit()"><span>HTML편집</span></a></div>
		<%} %>	
		</td>
	</tr>
	
	<tr>
	<td>
	<iframe id="<%=id %>_poll_frame" src="/pages/content/poll/poll_preview.jsp?id=<%=id %>&pollID=<c:out value="${pollInfo.pollID}"/>" frameborder="1" width="100%" height="600" marginwidth="0" marginheight="0" scrolling="auto" style="border:1 solid navy">	
	</td>
	</tr>
	</tbody>
	</table>
	</div>	
		
	
	<script language="javascript">


		<c:if test="${pollInfo.resultFinishYN=='Y'}">
			$('<%=id%>_pollButton').setStyle('display','none');	
		</c:if>
				
		//시작/종료문구 창 열기 
		$('<%=id%>').openTitle = function(seq){
			var frm = $('<%=id%>_info_form');
			var pollID = frm.ePollID.value;
			var pollTemplateID = frm.ePollTemplateID.value
			
			var str = '';
			if(seq=='startTitle'){
				str	= '시작문구 등록/수정'		
			}else{
				str	= '종료문구 등록/수정'
			}
			nemoWindow(
					{
						'id': '<%=id%>_titles',		
						noOtherClose: true,
						busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
						width: 550,
						//height: $('mainColumn').style.height.toInt(),
						height: 200,
						title: str,
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'content/poll/poll.do?id=<%=id%>&ePollID='+pollID+'&method=editTitle&eColumnName='+seq+'&ePollTemplateID='+pollTemplateID
					}
				);

		}




		/***********************************************/
		/* 저장버튼 클릭 -  저장
		/***********************************************/
		$('<%=id%>').saveData = function(  ) {
			var frm = $('<%=id%>_info_form');

			var seq = frm.ePollID.value;


			//필수입력 조건 체크
			if(!checkFormValue(frm)) {
				return;
			}

			// 설문제목(ePollTitle), 설명(eDescription)의 < , script , /> 등 스크립트 사용 의심 문자 필터	
			if ( frm.ePollTitle.value.indexOf('<') >= 0 || frm.ePollTitle.value.indexOf('/>') >= 0 || frm.ePollTitle.value.indexOf('script') >= 0
				 || frm.eDescription.value.indexOf('<') >= 0 || frm.eDescription.value.indexOf('/>') >= 0 || frm.eDescription.value.indexOf('script') >= 0 ){
				alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
				return;
			}
			
			var checked = isChecked( frm.elements['ePollEndType']  );
			if( checked == 0 ) {
				alert('설문 종료 조건을 선택하세요');
				return;
			}
			if(frm.ePollEndType[1].checked==true){
				if(frm.eAimAnswerCnt.value==''){
					alert('목표 응답 수를 입력하세요');
					return;
				}
			}
			if(frm.ePollEndType[1].checked==true){
				if(frm.eAimAnswerCnt.value<'1'||frm.eAimAnswerCnt.value==''){
					alert('목표 응답 수를 1 이상의 수를 입력 하세요');
					frm.eAimAnswerCnt.value='';
					return;
				}
			}
			
			if(frm.eResultFinishYN.value=='Y'){
				if(!confirm('설문완료 하시겠습니까?\r\n완료된 설문은 대량메일에서 바로 사용할 수 있습니다.')){
					return;
				}
			}else{
				$('<%=id%>_pollButton').setStyle('display','block');	
			}
			
			copyForm( $('<%=id%>_rform'), frm );
			
			
			if(seq=='' || seq == '0') {
				goUrl = 'content/poll/poll.do?id=<%=id%>&method=insertPollInfo';
				nemoRequest.init( 
						{
							busyWindowId: '<%=id%>_info'  // busy 를 표시할 window
							, updateWindowId: '<%=id%>_info' // 완료후 버튼,힌트 가 랜더링될 windowid
							, url: goUrl
							, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element		
							, onSuccess: function(html,els,resHTML) {
								$('<%=id%>').allList();		
								$('<%=id %>_pollPreview').setStyle('display','block');	
								frm.ePollID.value = resHTML;
								$('<%=id%>_poll_frame').src = "/pages/content/poll/poll_preview.jsp?id=<%=id %>&pollID="+resHTML;		
								frm.ePollTemplateID.disabled = true;				
							}
						});
			
			} else {
				goUrl = 'content/poll/poll.do?id=<%=id%>&method=updatePollInfo';		
				nemoRequest.init( 
						{
							busyWindowId: '<%=id%>_info'  // busy 를 표시할 window
							, updateWindowId: '<%=id%>_info' // 완료후 버튼,힌트 가 랜더링될 windowid
							, url: goUrl
							, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element		
							, onSuccess: function(html,els,resHTML) {
								$('<%=id%>').allList();								
								$('<%=id%>_poll_frame').src = "/pages/content/poll/poll_preview.jsp?id=<%=id %>&pollID="+seq	
								if(frm.eResultFinishYN.value=='Y'){
									closeWindow( $('<%=id%>_info') );
								}
							}
						});
			}


			nemoRequest.post(frm);
			
			
		}

		
		//설문마감일 변경 
		$('<%=id%>').changePollEnd = function(){
			var frm = $('<%=id%>_info_form');
			var checked = isChecked( frm.elements['ePollEndType']  );
			if( checked == 0 ) {
				alert('설문 종료 조건을 선택하세요');
				return;
			}
			if(frm.ePollEndType[1].checked==true){
				if(frm.eAimAnswerCnt.value==''){
					alert('목표 응답 수를 입력하세요');
					return;
				}
			}
			
			if(!confirm("설문 종료 조건을 변경하시겠습니까?")){
				return;
			}
			
		
			goUrl = 'content/poll/poll.do?id=<%=id%>&method=updatePollEndType';		
			nemoRequest.init( 
					{
						busyWindowId: '<%=id%>_info'  // busy 를 표시할 window
						, updateWindowId: '<%=id%>_info' // 완료후 버튼,힌트 가 랜더링될 windowid
						, url: goUrl
						, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element		
						, onSuccess: function(html,els,resHTML) {
							$('<%=id%>').allList();
						}
					});
			nemoRequest.post(frm);
		}


		//선택한 설문템플릿 미리보기 
		$('<%=id%>').previewPollTemplate = function(){
			var frm = $('<%=id%>_info_form');
			if(frm.ePollTemplateID.value==''){
				alert('설문템플릿을 선택하세요.');
				return;
			}
			nemoWindow(
					{
						'id': '<%=id%>_template',
						noOtherClose: true,
						busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
						width: 800,
						//height: $('mainColumn').style.height.toInt(),
						height: 600,
						title: '설문템플릿미리보기',
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'pages/content/poll/poll_template_preview.jsp?pollTemplateID='+frm.ePollTemplateID.value
					}
				);
		}


		//설문문항 입력창을 연다 
		$('<%=id%>').openQuestionMake = function(){
			
			var frm = $('<%=id%>_info_form');
			var pollID = frm.ePollID.value;
			var pollTemplateID = frm.ePollTemplateID.value;

			

			nemoWindow(
					{
						'id': '<%=id%>_question',		
						noOtherClose: true,
						busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
						width: 800,
						//height: $('mainColumn').style.height.toInt(),
						height: 500,
						title: '설문문항등록/수정',
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'content/poll/poll.do?id=<%=id%>&ePollID='+pollID+'&ePollTemplateID='+pollTemplateID+'&method=editPollQuestion&eQuestionID=0'
					}
				);
		}	

		//스킵패턴 설정창을 연다 
		$('<%=id%>').openSkipPattern = function(){

			var frm = $('<%=id%>_info_form');
			var pollID = frm.ePollID.value;
			var pollTemplateID = frm.ePollTemplateID.value;

			nemoWindow(
					{
						'id': '<%=id%>_skippattern',		
						noOtherClose: true,
						busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
						width: 800,
						//height: $('mainColumn').style.height.toInt(),
						height: 500,
						title: '스킵패턴 설정',
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'content/poll/poll.do?id=<%=id%>_skippattern&preID=<%=id%>&ePollID='+pollID+'&ePollTemplateID='+pollTemplateID+'&method=editSkipPattern'
					}
				);
		}	

		//설문을 FCK에디터를 이용하여 연다 
		$('<%=id%>').openPollHTMLEdit = function(){
			closeWindow( $('<%=id%>_question') );
			var pollID = $('<%=id%>_info_form').ePollID.value;			
			nemoWindow(
					{
						'id': '<%=id%>_edit',
						busyEl: '<%=id%>_question', // 창을 열기까지 busy 가 표시될 element
						width: 800,
						//height: $('mainColumn').style.height.toInt(),
						height: 700,
						title: '설문편집',
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'pages/content/poll/poll_edit.jsp?id=<%=id%>&pollID='+pollID
					}
				);
			
		}



		/* 리스트 표시 */
		window.addEvent("domready",function () {
			// 셀렉트 박스 렌더링		
			makeSelectBox.render($('<%=id%>_info_form'));
			<c:if test="${pollInfo.pollID != 0}" >
				$('<%=id%>_pollPreview').setStyle('display','block');	
			</c:if>
			<c:if test="${pollInfo.pollEndType == '1' || pollInfo.pollEndType == '3'}" >
				$('pollEndDateTR').setStyle('display','');
				$('pollEndDateLine').setStyle('display','');
			</c:if>
			<c:if test="${pollInfo.pollEndType == '2' || pollInfo.pollEndType == '3'}" >
				$('aimAnswerCntTR').setStyle('display','');
				$('aimAnswerCntLine').setStyle('display','');
			</c:if>
		});


	</script>
	</div>
<%}%>		