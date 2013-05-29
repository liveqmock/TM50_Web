<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.admin.dbjdbcset.model.DbJdbcSet"%>   
<%@ page import="web.admin.dbjdbcset.service.DbJdbcSetService"%>    
<%@ page import="web.admin.dbjdbcset.control.DbJdbcSetControlHelper"%>    
<%@ page import="web.admin.dbconnect.model.DbConnectInfo"%>   
<%@ page import="web.admin.dbconnect.service.DbConnectService"%>    
<%@ page import="web.admin.dbconnect.control.DbConnectControlHelper"%> 
<%@ page import="web.admin.massmailgroup.model.MassMailGroup"%>   
<%@ page import="web.admin.massmailgroup.service.MassMailGroupService"%>    
<%@ page import="web.admin.massmailgroup.control.MassMailGroupControlHelper"%>  
<%@ page import="web.target.targetlist.control.*"%>
<%@ page import="web.target.targetlist.service.*"%>
<%@ page import="web.target.targetlist.model.*"%>
<%@ page import="java.util.*"%>  
<%@ page import="web.common.util.*" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");


//관리자라면 
String isAdmin = LoginInfo.getIsAdmin(request);
String userAuth = LoginInfo.getUserAuth(request);
String groupID = LoginInfo.getGroupID(request);
String userID = LoginInfo.getUserID(request);

//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="bookMark" class="java.lang.String" scope="request" />
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
	
	
	<c:set var="userID" value="<%=userID %>"/>
	<c:set var="groupID" value="<%=groupID %>"/>
	<c:set var="isAdmin" value="<%=isAdmin %>"/>
	<c:set var="userAuth" value="<%=userAuth %>"/>
	
	<c:forEach items="${targetLists}" var="targetList">
	<c:set var="useCheck" value="N"/>
	<TR id="eTarget<c:out value="${targetList.targetID}"/>" class="tbl_tr" target_id="<c:out value="${targetList.targetID}"/>" state="<c:out value="${targetList.state}"/>" user_id="<c:out value="${targetList.userID}"/>" group_id="<c:out value="${targetList.groupID}"/>">
		<TD class="tbl_td" align="center">
			<input type="checkbox" class="notBorder" id="eTargetID" name="eTargetID" value="<c:out value="${targetList.targetID}" />" /><c:set var="useCheck" value="Y"/>
			<c:if test="${isAdmin == 'Y' || targetList.userID == userID || (userAuth == '2' && targetList.groupID == groupID)}" >
				<input type="hidden" id="<%=id%>useYN" name="<%=id%>useYN" value="Y"/>
				<c:set var="useCheck" value="Y"/>		
			</c:if>
			<c:if test="${!(isAdmin == 'Y' || targetList.userID == userID || (userAuth == '2' && targetList.groupID == groupID))}" >
				<input type="hidden" id="<%=id%>useYN" name="<%=id%>useYN" value="N"/>
			</c:if>
		</TD>
		<TD class="tbl_td"><c:out value="${targetList.targetID}"/></TD>	
		<%if(!bookMark.equals("D")){%>
			<c:if test="${targetList.targetType != '6'}" >
			<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${targetList.targetID}"/>')"><c:out value="${targetList.targetName}" escapeXml="true"/></b></a></TD>
			</c:if>
			<c:if test="${targetList.targetType == '6'}" >
			<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindowTargetUI('<c:out value="${targetList.targetID}"/>')"><c:out value="${targetList.targetName}" escapeXml="true"/></b></a></TD>
			</c:if>
		<%}else{ %>
			<TD class="tbl_td" align="left"><b><c:out value="${targetList.targetName}" escapeXml="true"/></b></TD>
		<%} %>		
		<TD class="tbl_td"><c:out value="${targetList.userName}"/></TD>	
		<TD class="tbl_td">
			<c:if test="${targetList.targetType == '1'}" >
				<img src="images/trees/csv_file.gif" title="파일업로드"/>
			</c:if>	
			<c:if test="${targetList.targetType == '2'}" >
				<img src="images/trees/book_icon.gif" title="직접입력"/>
			</c:if>		
			<c:if test="${targetList.targetType == '3'}" >
				<img src="images/trees/query.gif" title="DB추출"/>
			</c:if>	
			<c:if test="${targetList.targetType == '4'}" >
				<img src="images/trees/dbset.gif" title="기존발송추출"/>
			</c:if>		
			<c:if test="${targetList.targetType == '5'}" >
				<img src="images/trees/dbset.gif" title="기존발송추출"/>
			</c:if>	
			<c:if test="${targetList.targetType == '6'}" >
				<img src="images/trees/dbset.gif" title="회원정보UI"/>
			</c:if>					
		</TD>	
		<TD class="tbl_td">
			<c:if test="${targetList.shareType == '1'}" >
				비공유
			</c:if>	
			<c:if test="${targetList.shareType == '2'}" >
				그룹공유
			</c:if>		
			<c:if test="${targetList.shareType == '3'}" >
				전체공유
			</c:if>				
		</TD>	
		<%if(!bookMark.equals("D")){%>
			<TD  class="tbl_td" align="right"><a href="javascript:$('<%=id%>').previewWindow(<c:out value="${targetList.targetID}"/>,<c:out value="${targetList.state}"/>)" title="미리보기"><div id="count<c:out value="${targetList.targetID}"/>" ><fmt:formatNumber value="${targetList.targetCount}" type="number"/></div></a></TD>
		<%}else{ %>
			<TD class="tbl_td" align="right"><div id="count<c:out value="${targetList.targetID}"/>" ><fmt:formatNumber value="${targetList.targetCount}" type="number"/></div></TD>
		<%} %>
		<TD class="tbl_td">
		<div id="targetState<c:out value="${targetList.targetID}"/>">
			<c:if test="${targetList.state == '1'}" >
				<img src="images/massmail/sending.gif" title="등록중" />
				<input type="hidden" id="<%=id%>writeYN" name="<%=id%>writeYN" value="N"/>
			</c:if>	
			<c:if test="${targetList.state == '2'}" >
				<img src="images/x.gif" title="등록중 에러" />
				<input type="hidden" id="<%=id%>writeYN" name="<%=id%>writeYN" value="N"/>
			</c:if>		
			<c:if test="${targetList.state == '3'}" >
				<img src="images/massmail/finish.gif" title="등록완료" />
				<input type="hidden" id="<%=id%>writeYN" name="<%=id%>writeYN" value="Y"/>
			</c:if>
		</div>			
		</TD>
		<TD class="tbl_td"><c:out value="${targetList.registDate}"/></TD>
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
				,focus: true  // 마우스 이동시 포커스 여부
				,cursor: 'pointer' // 커서
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				<%if(!bookMark.equals("D")){%>
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
				<%}%>
			});
			$('<%=id%>').grid_content.render();
			
		});
	</script>
<%} 

//****************************************************************************************************/
//편집
//****************************************************************************************************/
if(method.equals("edit")) {
	
	TargetListService service = TargetListControlHelper.getUserService(application);
	String targetID = request.getParameter("targetID");
	//현재 계정에 사용할 수 있는 db리스트
	List<DbJdbcSet> dbJdbcSetList = null;
	if(isAdmin.equals("Y")){ //관리자라면 
		dbJdbcSetList = service.getDBList();
	}else{
		dbJdbcSetList = service.getDBList(userID,"_group_"+groupID);
	}
	String csvAuth = LoginInfo.getAuth_csv(request);
	String directAuth = LoginInfo.getAuth_direct(request);
	String queryAuth = LoginInfo.getAuth_query(request);
	String relatedAuth = LoginInfo.getAuth_related(request);
	
	//대량메일 그룹아이디
	MassMailGroupService massmailgroupService = MassMailGroupControlHelper.getUserService(application);
	List<MassMailGroup> massMailGroupList = massmailgroupService.listMassMailGroup();
	
	DbConnectService dbConnectService = DbConnectControlHelper.getUserService(application);
	List<DbConnectInfo> dbConnectList = dbConnectService.listDBConnectInfo();
%>
<jsp:useBean id="targetList" class="web.target.targetlist.model.TargetList" scope="request" />

<div  style="width:760px">
	<form id="<%=id%>_form" name="<%=id%>_form" method="post">
	<input type="hidden" id="eTargetID" name="eTargetID" value="<%=targetID%>" />
	<input type="hidden" id="eTargetTable" name="eTargetTable" value="<c:out value="${targetList.targetTable}"/>" />
	<input type="hidden" id="eOldUploadKey" name="eOldUploadKey" value="<c:out value="${targetList.uploadKey}"/>" />
	
			<div>
				<table border="0" cellpadding="3" class="ctbl" width="100%">
				<tbody>
				<tr>
					<td class="ctbl ttd1 mustinput" width="100px" align="left">대상자그룹명</td>
					<td class="ctbl td">
						<div>
						<c:import url="../../../include_inc/target_group_inc.jsp">
						<c:param name="mustInput" value="Y"/>			
						<c:param name="targetGroupID" value="${targetList.targetGroupID}"/>					
						</c:import>
						</div>
					<input type="text" id="eTargetName" name="eTargetName" value="<c:out value="${targetList.targetName}"/>" size="50" mustInput="Y" msg="대량메일명을 입력하세요"/>
				</td>
				</tr>
				<tr>
					<td colspan="2" class="ctbl line"></td>
				</tr>	
				<tr>
					<td class="ctbl ttd1" width="100px">설명</td>
					<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${targetList.description}"/>" size="100"/></td>
				</tr>
				<tr>
					<td colspan="2" class="ctbl line"></td>
				</tr>				
				<tr>
					<td class="ctbl ttd1 mustinput" width="100px">공유여부</td>
					<td class="ctbl td">
					<ul id="eShareType"  class="selectBox" onChange="javascript:$('<%=id%>').showGroupUser(this.value)">
						<li data="1" <c:if test="${targetList.shareType=='1'}">select='YES'</c:if>>비공유</li>
						<li data="2" <c:if test="${targetList.shareType=='2'}">select='YES'</c:if>>그룹공유</li>
						<li data="3" <c:if test="${targetList.shareType=='3'}">select='YES'</c:if>>전체공유</li>												
					</ul>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="ctbl line"></td>
				</tr>			
				<tr>
					<td class="ctbl ttd1 mustinput" width="100px">등록구분</td>
					<td class="ctbl td">
						<input type="radio" class="notBorder" id="eTargetType" name="eTargetType" value="1" <c:if test="${targetList.targetType=='1'}">checked</c:if>>&nbsp;파일 업로드&nbsp;&nbsp;&nbsp;
						<input type="radio" class="notBorder" id="eTargetType" name="eTargetType" value="2" <c:if test="${targetList.targetType=='2'}">checked</c:if>>&nbsp;직접입력&nbsp;&nbsp;&nbsp;
						<input type="radio" class="notBorder" id="eTargetType" name="eTargetType" value="3" <c:if test="${targetList.targetType=='3'}">checked</c:if>>&nbsp;DB추출&nbsp;&nbsp;&nbsp;
						<div style="display:none">
							<input type="radio" class="notBorder" id="eTargetType" name="eTargetType" value="4" <c:if test="${targetList.targetType=='4'}">checked</c:if>>&nbsp;기존발송추출
						</div>
						<c:if test="${targetList.targetType == '5'}" >
							<input type="radio" class="notBorder" id="eTargetType" name="eTargetType" value="5" <c:if test="${targetList.targetType=='5'}">checked</c:if>>&nbsp;기존발송추출
						</c:if>	
					</td>
				</tr>				
				</tbody>
				</table>				
					<br> 
			
					<!-- ################## 파일업로드 ##########################  -->
					<div id="<%=id%>_fileUpload" >				
					<table class="ctbl" style="width:100%">
					<tbody>
					<tr>
						<td>
								<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>파일업로드&nbsp;(csv,txt,xls 파일)</b></div>
								<div style="float:right;height:25px;margin-top:10px;margin-right:10px">
									<a title="<div style='text-align:left'>												
										<img src='images/tag_red.png'><strong>주의사항</strong><br>
										&nbsp; 1.csv,txt파일의 각 필드는  반드시  콤머(,)로 구분해야 합니다.<br>
										&nbsp; 2.컬럼명(항목명)은  총 20개이하만 가능합니다.<br>
										&nbsp; 3.컬럼명은 파일의 첫번째 칸, 첫번째 줄에서 시작해야 합니다 .(예시 파일 참고)										
										</div>">
										<img src='images/tag_blue.png'> <strong>주의사항</strong>
										</a> 
								&nbsp;&nbsp;&nbsp;<a href="/pages/target/targetlist/targetlist_sample.jsp" target="_blank"><u>예시 파일 다운로드</u> </a></div>
						</td>
					</tr>
					<tr>
						<td style="padding:0">
							
							<table class="ctbl" width="100%">
							<tbody>
							<tr>
								<td class="ctbl ttd1 mustinput" width="100px">업로드할 파일</td>
								<td class="ctbl td">
									<div  style="float:left" id="<%=id%>uploadWrapper" ></div>
									<div style="float:left;padding:22px">
										<a id="<%=id%>_uploadBtn" href="javascript:$('<%=id%>').uploadFile('<%=targetID%>')" class="web20button bigblue" title="선택한 파일을 업로드 하신 후  일대일치환을 선택하세요">파일 업로드</a>
									</div>
								</td>
							</tr>					
							<tr>
								<td colspan="10" class="ctbl line"></td>
							</tr>
							<tr>
								<td class="ctbl ttd1" width="100px">업로드된 파일</td>
								<td class="ctbl td" style="padding:5px">
									<div  id="<%=id%>uploaded" >
									<div style="float:left">
										<a href="target/targetlist/target.do?method=fileDownload&uploadKey=<c:out value="${targetList.uploadKey}"/>" target="_blank">
										<c:out value="${targetList.realFileName}"/>
										</a>
									</div>	
									</div>
								</td>
							</tr>	
							<%if(!targetID.equals("0")){ %>
							<tr>
								<td colspan="10" class="ctbl line"></td>
							</tr>
							<tr>
								<td class="ctbl ttd1" width="100px">총대상자인원</td>
								<td class="ctbl td" style="padding:5px">
								<div style="float:left;margin-top:5px">
								<input type="text" id="eTargetCount" name="eTargetCount" size="10" value="<c:out value="${targetList.targetCount}"/>" style="border:0px;text-align:right" readonly /> 명
								</div>								
								</td>
							</tr>		
							<%} %>										
							</tbody>
							</table>
							
						</td>
					</tr>		
					<tr>
					<td>
					<!-- ############ 파일 일대일 치환 매칭 ################# -->
							<table class="ctbl" style="width:100%">
							<tbody>
							<tr>
								<td>
									<div>
										<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>일대일 치환</b></div>
									</div>
								</td>
							</tr>
							<tr>
								<td style="padding:0">
									
									<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0"  width="760px" >
									<thead>
										<tr>
										<th style="height:26px;width:10%">컬럼번호</th>
										<th style="height:26px;width:45%">컬럼명</th>
										<th style="height:26px;width:45%">일대일 치환명</th>
										</tr>
									</thead>
									</TABLE>
								
									<div style="vertical-align:top;overflow-y:auto;width:100%" id="<%=id%>_fileOneToOneWrapper">
								
									<TABLE style="border:0px" class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
									<tbody id="<%=id%>_fileOneToOne" >
										
									
									</tbody>
									</TABLE>
									</div>
								</td>
							</tr>
							</tbody>
							</table>
							
					</td>
					</tr>			
					</tbody>
					</table>
				</div>
				
				<!-- ################## 직접입력 ##########################  -->
				<div id="<%=id%>_direct" >				
					<table class="ctbl" style="width:100%">
					<tbody>
					<tr>
						<td>
							<div>
								<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><strong>직접입력(최대 1,000라인까지만 가능합니다. 각 라인은 엔터키로 구분됩니다.)</strong></div>
								<div style="float:left;height:25px;margin-top:10px;margin-left:10px"><a title="<div style='text-align:left'>												
								<img src='images/tag_blue.png'><strong>사용자가 소량의 메일데이터를 직접 입력하는 것을 말합니다.</strong><br />
								 &nbsp; <strong>====== 입력예  =============</strong><br>
								 &nbsp; 이메일,이름,성별 <br>
								 &nbsp; yyy@abc.com,홍길동,남  <br>
								 &nbsp; bbb@abc.com,강감찬,남<br>
								 &nbsp; <strong>==========================</strong><br>
								<img src='images/tag_red.png'><strong>주의사항</strong><br>
								&nbsp; 1.반드시  콤머(,)로 구분해야 합니다.<br>
								&nbsp; 2.컬럼명(항목명)은  총 20개이하만 가능합니다.<br>
								&nbsp; 3.항목명과 데이터의 순서가 같아야 합니다.
								</div>"><img src='images/tag_blue.png'><strong>직접입력이란?</strong></a></div>
								
							</div>
						</td>
					</tr>
					<tr>
						<td style="padding:0">
							
							<table class="ctbl" width="100%">
							<tbody>
							<tr>
								<td class="ctbl ttd1 mustinput" width="100px">직접입력창</td>
								<td class="ctbl td" >
								<div style="float:left">
								<textarea id="eDirectText" name="eDirectText" style="width:500px;height:100px"><c:out value="${targetList.directText}"/></textarea>
								</div>
								<div style="float:left;margin:20px 0 0 15px"><br>
										<div id="<%=id%>_validateDirectBtn" style="float:left" class="btn_green"><a href="javascript:$('<%=id%>').showDirectOneToOne('0')" style="cursor:pointer"><span>입력확인</span></a></div>
										<div id="<%=id%>_validateDirectImg" style="float:left;margin-left:5px">
								</div>
								</td>
								
							</tr>					
							</tbody>
							</table>
							
						</td>
					</tr>	
					<tr>
					<td>
					
						<!-- ############ 직접입력 일대일 치환 매칭 ################# -->
							<table class="ctbl" style="width:100%">
							<tbody>
							<tr>
								<td>
									<div>
										<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>일대일 치환</b></div>
										
									</div>
								</td>
							</tr>
							<tr>
								<td style="padding:0">
									
									<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0"  width="760px" >
									<thead>
										<tr>
										<th style="height:26px;width:10%">컬럼번호</th>
										<th style="height:26px;width:45%">컬럼명</th>
										<th style="height:26px;width:45%">일대일 치환명</th>
										</tr>
									</thead>
									</TABLE>
								
									<div style="vertical-align:top;overflow-y:auto;width:100%" id="<%=id%>_directOneToOneWrapper">
								
									<TABLE style="border:0px" class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
									<tbody id="<%=id%>_directOneToOne" >
										
									
									</tbody>
									</TABLE>
									</div>
								</td>
							</tr>
							</tbody>
							</table>				
					
					
					</td>
					</tr>				
					</tbody>
					</table>
				</div>
				
				<!-- ################## DB 추출  ##########################  -->
					<div id="<%=id%>_db" >				
					<table class="ctbl" style="width:100%">
					<tbody>
					<tr>
						<td>
							<div>
								<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>DB추출</b></div>								
							</div>
						</td>
					</tr>
					<tr>
						<td style="padding:0">
							<input type="hidden" id="eDbID" name="eDbID" value="<c:out value="${targetList.dbID}"/>">
							<table class="ctbl" width="100%">
							<tbody>
							<tr>
								<td class="ctbl ttd1 mustinput" width="100px">DB선택</td>
								<td class="ctbl td">
									<div style="float:left">
									<%if(targetList.getDbID() !=null && targetList.getDbID().equals("10")){ %>
										TM5_MYSQL
										<input type="hidden" id="eSelectDbID" name="eSelectDbID" value="10"/>
									<%}else{ %>
									<select id="eSelectDbID" name="eSelectDbID">
										<option value="">DB를 선택 하세요 </option>
										<%for(DbJdbcSet dbJdbcSet : dbJdbcSetList) { %>
										<option value="<%=dbJdbcSet.getDbID()%>" <% if(dbJdbcSet.getDbID().equals(targetList.getDbID()) || dbJdbcSet.getDefaultYN().equals("Y") ){ %> selected <%} %>><%=dbJdbcSet.getDbName()%></option>
										<%}%>
									</select>
									<%} %>
									</div>
									<% if(targetID!=null && !targetID.equals("0")){ %>
									<div style="float:left;margin-left:10px;margin-top:2px">
									<input type="checkbox" class="notBorder" value="Y" id="eQueryUpdateYN" name="eQueryUpdateYN" onclick="javascript:$('<%=id%>').chkQueryUpdate()" disabled>&nbsp;쿼리수정
									</div>
									<%} %>
								</td>
							</tr>					
							<tr>
								<td colspan="10" class="ctbl line"></td>
							</tr>
							<tr id="<%=id %>_confirmTable">
								<td class="ctbl ttd1 mustinput" width="100px">DB접근키</td>
								<td class="ctbl td">
									<input type="password" id="eDbAccessKeyConfirmer" name="eDbAccessKeyConfirmer" style="margin-right:1px" size="25" <c:if test="${targetList.targetType=='3'}">mustInput="Y" msg="DB접근키 입력"</c:if>/>
									<c:if test="${targetList.targetID == 0 }">
									<a style="float:right;padding-right:403px" title="<div style='text-align:left'>												
									<img src='images/help.gif'>&nbsp;<strong>DB접근키 입력</strong><br>
									* DB 추출을 위해 DB접근키는 반드시 입력하여야 합니다.<br>
									* 입력 후 확인을 누르시면 쿼리입력이 가능합니다.<br>
									<div style='color:red'>* DB접근키 분실 시 담당자에게 문의하여 주시기 바랍니다.</div>
									</div>">							
									<img src="images/help.gif">
									</a>
									<div id="<%=id %>_confirmBtn" style="float:right;padding-right:10px;" class="btn_r"><a href="javascript:$('<%=id%>').dbAccessKeyConfirm()" style="cursor:pointer"><span>확 인</span></a></div>
									</c:if>
									<c:if test="${targetList.targetID != 0 }">
									<a style="float:right;padding-right:393px" title="<div style='text-align:left'>												
									<img src='images/help.gif'>&nbsp;<strong>DB접근키 입력</strong><br>
									* DB 추출을 위해 DB접근키는 반드시 입력하여야 합니다.<br>
									* 입력 후 확인을 누르시면 쿼리입력이 가능합니다.<br>
									<div style='color:red'>* DB접근키 분실 시 담당자에게 문의하여 주시기 바랍니다.</div>
									</div>">							
									<img src="images/help.gif">
									</a>
									<div id="<%=id %>_confirmBtn" style="float:right;padding-right:10px;" class="btn_r"><a href="javascript:$('<%=id%>').dbAccessKeyConfirm()" style="cursor:pointer"><span>확 인</span></a></div>
									</c:if>
								</td>
							</tr>
							</tbody>
							</table>
							
							<table class="ctbl" width="100%" id="<%=id %>_queryTextTable" <c:if test="${targetList.targetID != 0 }">style="display:none"</c:if>>
							<tr id="<%=id %>_queryInputText">
								<td class="ctbl ttd1 mustinput" width="100px"><div>Query입력<div/>
									<div style="margin-top:5px">
										<img src="images/help.gif" title="<div style='text-align:left'><strong>Query입력시 주의사항 :</strong><br>												
									1. 컬럼명을 정확히 입력하시고 (*)를 사용하지 마세요.<br>
									2. 반드시 이메일이 포함된 컬럼이 입력되어야 합니다.<br>
									3. INSERT, UPDATE, DELETE, ORDER BY 문은 사용할 수 없습니다.<br>
									4. 복잡한 컬럼명은  단순화하여(alias) 주시기 바랍니다. <br>(예: select (member.email) as email from....)<br>
									5. 프로시저를 사용하지마세요.</div>"/>
									</div>
								
								</td>
								<td class="ctbl td">
									<div style="float:left">
										<input type="hidden" name="eQueryTextOld" id="eQueryTextOld" value="<c:out value="${targetList.queryText}"/>">
										<textarea id="eQueryText" name="eQueryText" style="width:510px;height:60px" disabled /><c:out value="${targetList.queryText}"/></textarea>
									</div>
									<div style="float:left;margin:20px 0 0 15px">
										<div style="float:left" id="<%=id%>_validateQueryBtn" class="btn_green"><a href="javascript:$('<%=id%>').testQuery()" style="cursor:pointer"><span>쿼리 검증</span></a></div>
										
										<div id="<%=id%>_validateQueryImg" style="float:left;margin-left:5px">
												
										</div>
									</div>
								
								</td>
							</tr>
							<tr>
								<td colspan="10" class="ctbl line"></td>
							</tr>
							<tr>
								<td class="ctbl ttd1 mustinput" width="100px">인원 수 계산<br/>Query입력</td> 
								<td class="ctbl td">
									<!-- <div><input type="checkbox" class="notBorder" value="Y" id="eCustomInputYN" name="eCustomInputYN" > 인원수 계산 쿼리를 직접 입력 하시려면 체크하세요</div> -->
									<div style="float:left" id="<%=id%>eCountQueryText">
										<input type="hidden" name="eCountQueryTextOld" id="eCountQueryTextOld" value="<c:out value="${targetList.countQuery}"/>">
										<textarea id="eCountQueryText" name="eCountQueryText" style="width:510px;height:60px" disabled /><c:out value="${targetList.countQuery}"/></textarea>
									</div>
									<div style="float:left;margin:5px 0 0 10px">
										<div style="float:left;margin-left:5px"><div class="btn_b" id="<%=id%>_validateCountQueryBtn"><a href="javascript:$('<%=id%>').runCountQuery()" style ="cursor:pointer"><span>인원수 계산</span></a></div></div>
										
										<div id="<%=id%>_validateCountQueryImg" style="float:left;margin-left:5px">
												
										</div>
										
										<div style="clear:both;margin-top:27px;text-align:right">
											<input type="text" id="eTargetCount_edit" name="eTargetCount_edit" size="10" value="<c:out value="${targetList.targetCount}"/>" style="border:0px;text-align:right" readonly /> 명
										</div>
									</div>
								
								</td>
							</tr>				
							</tbody>
							</table>							
						</td>
					</tr>	
					<tr>
					<td>
					
						<!-- ############ DB 추출  일대일 치환 매칭 ################# -->
						<c:if test="${targetList.targetID != 0 }">
							<table id="<%=id %>_onetooneInfo" class="ctbl" style="display:none;width:100%">
						</c:if>
						<c:if test="${targetList.targetID == '0' }">
							<table id="<%=id %>_onetooneInfo" class="ctbl" style="width:100%">
						</c:if>
						<tbody>
						<tr>
							<td>
								<div>
									<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>일대일 치환</b></div>
								</div>
							</td>
						</tr>
						<tr>
							<td style="padding:0">
								
							<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="760px" >
							<thead>
								<tr>
								<th style="height:26px;width:40px">번호</th>
								<th style="height:26px;">필드명</th>
								<th style="height:26px;width:150px">일대일 치환명</th>
								<th style="height:26px;width:350px">설명</th>
								</tr>
							</thead>
							</TABLE>
							
							<div style="vertical-align:top;overflow-y:auto;width:760px" id="<%=id%>_queryOneToOneWrapper">
						
							<TABLE style="border:0px" class="tbl" border="0" cellspacing="0" cellpadding="0" width="725px" >
							<tbody id="<%=id%>_queryOneToOne" >
								
							
							</tbody>
							</table>
							</div>
							</td>
						</tr>
						</tbody>
						</table>
				
				
					</td>
					</tr>	
					</tbody>
					</table>
				</div>
				
				<!-- ################## 기존발송추출 ##########################  -->
				<div id="<%=id%>_sended" >				
					<table class="ctbl" style="width:100%">
					<tbody>
					<tr>
						<td>
							<div>
								<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>기존발송추출</b></div>
								<div style="float:right;height:25px;margin-top:10px"><a title="<div style='text-align:left'>												
								썬더메일 내부DB에서 기존에  발송된 내역에서 대상자를 추출하는 것을 말합니다.<br>
								이때 수집된 대상자들은 발송이 완료되고 통계수집기간이 모두 완료된 후에 월별로 백업완료된 데이터만 추출할수있습니다.<br>
								가급적 서로 다른 월로 검색하기 보다는 같은 월로 검색하는 것이 좀 더 빠른 추출이 이루어집니다. 
								</div>"><b>기존발송추출이란?</b></a></div>
							</div>
						</td>
					</tr>
					<tr>
						<td style="padding:0">
							<table class="ctbl" width="100%">
							<tbody>
								<tr>
									<td class="ctbl ttd1 mustinput" width="100px">대상기간</td>
									<td class="ctbl td">
										<c:import url="../../../include_inc/backupdate_inc.jsp">
										<c:param name="mustInput" value="N"/>			
										<c:param name="sendedDate" value="${targetList.sendedDate}"/>					
										</c:import>	
									</td>
								</tr>
								<tr>
									<td colspan="10" class="ctbl line"></td>
								</tr>
								<tr>
									<td class="ctbl ttd1" width="100px">대량메일그룹</td>
									<td class="ctbl td">
									<c:import url="../../../include_inc/search_massmailgroup_inc.jsp">
									<c:param name="mustInput" value="N"/>			
									<c:param name="massmailGroupID" value="${targetList.massmailGroupID}"/>					
									</c:import>
										
									</td>
								</tr>
								<tr>
									<td colspan="10" class="ctbl line"></td>
								</tr>
								<tr>
									<td class="ctbl ttd1" width="100px">발송여부</td>
									<td class="ctbl td">
										<input type="radio" class="notBorder" id="eSuccessYN" name="eSuccessYN" value=""  <%if(targetList.getSuccessYN() == null || targetList.getSuccessYN().equals("")){%>checked<%}%>>&nbsp;전체&nbsp;&nbsp;&nbsp;
										<input type="radio" class="notBorder" id="eSuccessYN" name="eSuccessYN" value="Y" <%if(targetList.getSuccessYN() != null && targetList.getSuccessYN().equals("Y")){%>checked<%}%>>&nbsp;성공&nbsp;&nbsp;&nbsp;
										<input type="radio" class="notBorder" id="eSuccessYN" name="eSuccessYN" value="N" <%if(targetList.getSuccessYN() != null && targetList.getSuccessYN().equals("N")){%>checked<%}%>>&nbsp;실패&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
								<tr>
									<td colspan="10" class="ctbl line">
									</td>
								</tr>
								<tr>
									<td class="ctbl ttd1" width="100px">오픈여부</td>
									<td class="ctbl td">
										<input type="radio" class="notBorder" id="eOpenYN" name="eOpenYN" value="" <%if(targetList.getOpenYN() == null || targetList.getOpenYN().equals("")){%>checked<%}%>>&nbsp;전체&nbsp;&nbsp;&nbsp;
										<input type="radio" class="notBorder" id="eOpenYN" name="eOpenYN" value="Y" <%if(targetList.getOpenYN() != null && targetList.getOpenYN().equals("Y")){%>checked<%}%>>&nbsp;오픈&nbsp;&nbsp;&nbsp;
										<input type="radio" class="notBorder" id="eOpenYN" name="eOpenYN" value="N" <%if(targetList.getOpenYN() != null && targetList.getOpenYN().equals("N")){%>checked<%}%>>&nbsp;미오픈&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
								<tr>
									<td colspan="10" class="ctbl line"></td>
								</tr>
								<tr>
									<td class="ctbl ttd1" width="100px">클릭여부</td>
									<td class="ctbl td">
										<input type="radio" class="notBorder" id="eClickYN" name="eClickYN" value=""  <%if(targetList.getClickYN() == null || targetList.getClickYN().equals("")){%>checked<%}%>>&nbsp;전체&nbsp;&nbsp;&nbsp;
										<input type="radio" class="notBorder" id="eClickYN" name="eClickYN" value="Y" <%if(targetList.getClickYN() != null && targetList.getClickYN().equals("Y")){%>checked<%}%>>&nbsp;클릭&nbsp;&nbsp;&nbsp;
										<input type="radio" class="notBorder" id="eClickYN" name="eClickYN" value="N" <%if(targetList.getClickYN() != null && targetList.getClickYN().equals("N")){%>checked<%}%>>&nbsp;미클릭&nbsp;&nbsp;&nbsp;
									</td>
								</tr>		
								<tr>
									<td colspan="10" class="ctbl line"></td>
								</tr>
								<tr>
									<td class="ctbl ttd1 mustinput" width="100px">일대일치환연동</td>
									<td class="ctbl td">
									<div style="float:left;margin-right:5px;padding-top:2px">
										<ul id="eConnectedDB"  class="selectBox" onChange="javascript:$('<%=id%>').getConnectedTable(this.value)" >
											<li data="">연동을 선택 하세요</li>
											<%for(DbConnectInfo dbConnectInfo : dbConnectList) { %>
											<li data="<%=dbConnectInfo.getDbID()%>" <% if(dbConnectInfo.getDbID().equals(targetList.getConnectedDbID())){%>select="yes"<%} %>><%=dbConnectInfo.getConnectDBName()%></li>			
											<input type="hidden" id="eConnectedTable_<%=dbConnectInfo.getDbID()%>" name="eConnectedTable_<%=dbConnectInfo.getDbID()%>" value="<%=dbConnectInfo.getTableName()%>">								
											<%}%>										
										</ul>	
									</div>
									<div style="float:left;height:20px;padding-top:2px" class="btn_green"><a href="javascript:$('<%=id%>').showSendedOneToOne()" style="cursor:pointer"><span>일대일치환 불러오기</span></a></div>																										
									</td>
								</tr>											
								<tr>
									<td colspan="10" class="ctbl line"></td>
								</tr>
								<tr>
									<td class="ctbl ttd1" width="100px">총대상자수</td>
									<td class="ctbl td">
									</td>
								</tr>				
							</tbody>
							</table>
							<input type="hidden" id="eConnectedTable" name="eConnectedTable">
						</td>
						</tr>			

						<tr>
						<td>
					
							<!-- ############ 기본발송  일대일 치환 매칭 ################# -->
							<table class="ctbl" style="width:100%">
							<tbody>
							<tr>
								<td>
									<div>
										<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>일대일 치환</b></div>
									</div>
								</td>
							</tr>
							<tr>
								<td style="padding:0">
									
								<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="760px" >
								<thead>
									<tr>
									<th style="height:26px;width:40px">번호</th>
									<th style="height:26px;">필드명</th>
									<th style="height:26px;width:150px">일대일 치환명</th>
									<th style="height:26px;width:350px">설명</th>
									</tr>
								</thead>
								</TABLE>
								
								<div style="vertical-align:top;overflow-y:auto;width:760px" id="<%=id%>_sendedOneToOneWrapper">
							
								<TABLE style="border:0px" class="tbl" border="0" cellspacing="0" cellpadding="0" width="725px" >
								<tbody id="<%=id%>_sendedOneToOne" >
								</tbody>
								</table>
								</div>
								</td>
							</tr>
							</tbody>
							</table>
						</td>
						</tr>		
					</tbody>
					</table>
				</div>
				<!-- ################## 이메일별 발송 히스토리 추출##########################  -->
				<div id="<%=id%>_sendedhistory" style="display:none">				
					<table class="ctbl" style="width:100%">
					<tbody>
					<tr>
						<td>
							<div>
								<div class="gray_border" style="float:left;color:#4F7EB3;height:25px;margin-top:10px;margin-left:10px"><b>기존발송추출</b></div>
								<div style="float:right;height:25px;margin-top:10px"><a title="<div style='text-align:left'>												
								기존에  발송된 내역에서 대상자를 추출하는 것을 말합니다.<br/>
								이때 수집된 대상자들은 발송이 완료되고 통계수집기간이 모두 완료된 <br/>후에 월별로 백업완료된 데이터만 추출할수있습니다.
								</div>"><b>기존발송추출이란?</b></a></div>
							</div>
						</td>
					</tr>
					<tr>
						<td style="padding:0">
							<table class="ctbl" width="100%">
							<tbody>
								<tr>
									<td class="ctbl ttd1" width="100px">적용 대상</td>
									<td class="ctbl td" width="250px">
									<%if(targetList.getSendedDate() != null && targetList.getSendedDate().length() == 6) {%>
										<%=targetList.getSendedDate().subSequence(0,4) %>년  <%=targetList.getSendedDate().subSequence(4,6) %>월에 통계 수집이 완료된 대상자
									<%} %>  
									</td>
									<td class="ctbl ttd1" width="100px">총대상인원</td>
									<td colspan="3" class="ctbl td">
										<c:out value="${targetList.targetCount}"/> 명
									</td>
								</tr>
								<tr>
									<td colspan="4" class="ctbl line"></td>
								</tr>
								<tr>
									<td class="ctbl ttd1" width="100px">성공여부</td>
									<td class="ctbl td">
										<c:if test="${targetList.successYN == 'Y'}" >
											발송 성공
										</c:if>
										<c:if test="${targetList.successYN == 'N'}" >
											발송 실패
										</c:if>
										<c:if test="${targetList.successYN == null || targetList.successYN == ''}" >
											미설정
										</c:if>
									</td>
									<td class="ctbl ttd1" width="100px">오픈여부</td>
									<td class="ctbl td">
										<c:if test="${targetList.openYN == 'Y'}" >
											오픈
										</c:if>
										<c:if test="${targetList.openYN == 'N'}" >
											미오픈
										</c:if>
										<c:if test="${targetList.openYN == null || targetList.openYN == ''}" >
											미설정
										</c:if>
									</td>
								</tr>
								<tr>
									<td colspan="4" class="ctbl line"></td>
								</tr>
								<tr>
									<td class="ctbl ttd1" width="100px">클릭여부</td>
									<td class="ctbl td">
										<c:if test="${targetList.clickYN == 'Y'}" >
											클릭
										</c:if>
										<c:if test="${targetList.clickYN == 'N'}" >
											미클릭
										</c:if>
										<c:if test="${targetList.clickYN == null || targetList.clickYN == ''}" >
											미설정
										</c:if>
									</td>
									<td class="ctbl ttd1" width="100px">수신거부</td>
									<td class="ctbl td">
										<c:if test="${targetList.rejectcallYN == 'Y'}" >
											거부
										</c:if>
										<c:if test="${targetList.rejectcallYN == 'N'}" >
											허용
										</c:if>
										<c:if test="${targetList.rejectcallYN == null || targetList.rejectcallYN == ''}" >
											미설정
										</c:if>
									</td>
								</tr>						
							</tbody>
							</table>
							<input type="hidden" id="eConnectedTable" name="eConnectedTable">
						</td>
						</tr>
						</tbody>
					</table>
				</div>			
			</div>
	</form>
	</div>
	<p>						
	
	<div id="<%=id %>_allBtn" <c:if test="${targetList.targetID != 0 && targetList.targetType == 3}">style="visibility:hidden"</c:if>>
	<div style="float:right;margin-right:10px"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	<%if(!targetID.equals("0") && (isAdmin.equals("Y") || targetList.getUserID().equals(userID) || (targetList.getGroupID().equals(groupID)))) { %>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<%=targetID%>, '<%=userID %>', <%=groupID %>)" class="web20button bigpink">삭 제</a></div>	
	<%}%>
	<%
		if(targetID.equals("0")){%>
		<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData(<%=targetID %>)" class="web20button bigblue">저 장</a></div>
	<%}else{
		if( !targetList.getTargetType().equals(Constant.TARGET_TYPE_SENDHISTORY) && (isAdmin.equals("Y") || targetList.getUserID().equals(userID)|| (targetList.getGroupID().equals(groupID))) && (targetList.getState().equals("3") || targetList.getState().equals("2"))) { %>	
			<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData(<%=targetID %>)" class="web20button bigblue">저 장</a></div>
	<%	 } 
	 }%>
	 </div>
	 
	<script language="javascript">
	
	window.addEvent("domready",function() {

		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
		$('<%=id%>').renderUpload();

		renderTableHeader( "<%=id%>_direct" );
		renderTableHeader( "<%=id%>_db" );
		renderTableHeader( "<%=id%>_sended" );


		// 수정이고 csv 이면
		<%if(!targetID.equals("0") && targetList.getTargetType().equals("1")) { %>
			// 파일 정보와 원투원을 불러온다
			$('<%=id%>').showFileInfo( $('<%=id%>').showFileOneToOne );
		<%}%>

		$('<%=id%>').isValidateQuery = false; // 쿼리 검증 여부
		$('<%=id%>').isValidateCountQuery = false; // 카운트 검증 여부

		// 수정이고 직접입력  이면
		<%if(!targetID.equals("0") && targetList.getTargetType().equals("2")) { %>
			// 파일 정보와 원투원을 불러온다		
			$('<%=id%>').showDirectOneToOnEdit('<%=targetID%>');
		<%}%>
		$('<%=id%>').isValidateDirect = false; //원투원 검증 여부

		// 수정이고 DB추출  이면
		<%if(!targetID.equals("0") && targetList.getTargetType().equals("3")) { %>
			// 파일 정보  와 원투원을 불러온다
			$('<%=id%>').showQueryOneToOneEdit();
		<%}%>	
		
		// 수정이고 기존 발송 추출  이면
		<%if(!targetID.equals("0") && targetList.getTargetType().equals("4")) { %>
			// 파일 정보  와 원투원을 불러온다
			$('<%=id%>').showSendedOneToOneEdit();
			$('<%=id%>').getConnectedTable('<%=targetList.getConnectedDbID()%>');
		<%}%>


		var frm = $('<%=id%>_form');
		<% if(targetID==null || targetID.equals("0")){
			if(isAdmin.equals("Y") || csvAuth.equals("Y")){%>
				frm.eTargetType[0].checked=true;
			<%}else if(directAuth.equals("Y")){%>
				frm.eTargetType[1].checked=true;
			<%}else if(queryAuth.equals("Y")){%>
				frm.eTargetType[2].checked=true;
			<%}else if(relatedAuth.equals("Y")){%>
				frm.eTargetType[3].checked=true;
			<%}
			if(!isAdmin.equals("Y")){
				if(!csvAuth.equals("Y")){%>
				frm.eTargetType[0].disabled=true;
				<%}
				if(!directAuth.equals("Y")){%>
					frm.eTargetType[1].disabled=true;
				<%}
				if(!queryAuth.equals("Y")){%>
					frm.eTargetType[2].disabled=true;
				<%}
				if(!relatedAuth.equals("Y")){%>
					frm.eTargetType[3].disabled=true;
		<%		}
			}
		}else if(targetList.getTargetType().equals("1")){%>
			frm.eTargetType[0].disabled=false;
			frm.eTargetType[1].disabled=true;
			frm.eTargetType[2].disabled=true;
			frm.eTargetType[3].disabled=true;
		<%}else if(targetList.getTargetType().equals("2")){%>
			frm.eTargetType[0].disabled=true;
			frm.eTargetType[1].disabled=false;
			frm.eTargetType[2].disabled=true;
			frm.eTargetType[3].disabled=true;
		<%}else if(targetList.getTargetType().equals("3")){%>
			frm.eTargetType[0].disabled=true;
			frm.eTargetType[1].disabled=true;
			frm.eTargetType[2].disabled=false;
			frm.eTargetType[3].disabled=true;
		<%}else if(targetList.getTargetType().equals("4")){%>
			frm.eTargetType[0].disabled=true;
			frm.eTargetType[1].disabled=true;
			frm.eTargetType[2].disabled=true;
			frm.eTargetType[3].disabled=false;			
		<%}else if(targetList.getTargetType().equals("5")){%>
			frm.eTargetType[0].disabled=true;
			frm.eTargetType[1].disabled=true;
			frm.eTargetType[2].disabled=true;
			frm.eTargetType[3].disabled=true;
		<%}%>
			
		if(frm.eTargetType[0].checked==true){
			$('<%=id%>_fileUpload').setStyle('visibility','');
			$('<%=id%>_fileUpload').setStyle('height','100%');
			$('<%=id%>_direct').setStyle('display','none');		
			$('<%=id%>_db').setStyle('display','none');	
			$('<%=id%>_sended').setStyle('display','none');	
		}else if(frm.eTargetType[1].checked==true){
			$('<%=id%>_fileUpload').setStyle('visibility','hidden');
			$('<%=id%>_fileUpload').setStyle('height','0');
			$('<%=id%>_direct').setStyle('display','block');
			$('<%=id%>_db').setStyle('display','none');	
			$('<%=id%>_sended').setStyle('display','none');
		}else if(frm.eTargetType[2].checked==true){
			$('<%=id%>_fileUpload').setStyle('visibility','hidden');
			$('<%=id%>_fileUpload').setStyle('height','0');
			$('<%=id%>_direct').setStyle('display','none');
			$('<%=id%>_db').setStyle('display','block');	
			$('<%=id%>_sended').setStyle('display','none');
		}else if(frm.eTargetType[3].checked==true){
			$('<%=id%>_fileUpload').setStyle('visibility','hidden');
			$('<%=id%>_fileUpload').setStyle('height','0');
			$('<%=id%>_direct').setStyle('display','none');
			$('<%=id%>_db').setStyle('display','none');	
			$('<%=id%>_sended').setStyle('display','block');
		}else if(frm.eTargetType[4].checked==true){
			$('<%=id%>_fileUpload').setStyle('visibility','hidden');
			$('<%=id%>_fileUpload').setStyle('height','0');
			$('<%=id%>_direct').setStyle('display','none');
			$('<%=id%>_db').setStyle('display','none');	
			$('<%=id%>_sended').setStyle('display','none');
			$('<%=id%>_sendedhistory').setStyle('display','block');
		}


		//파일업로드
		$(frm.eTargetType[0]).addEvent('click', function(){
			$('<%=id%>_fileUpload').setStyle('visibility','');
			$('<%=id%>_fileUpload').setStyle('height','100%');
			$('<%=id%>_direct').setStyle('display','none');
			$('<%=id%>_db').setStyle('display','none');	
			$('<%=id%>_sended').setStyle('display','none');
		});	
		//직접입력
		$(frm.eTargetType[1]).addEvent('click', function(){
			$('<%=id%>_fileUpload').setStyle('visibility','hidden');
			$('<%=id%>_fileUpload').setStyle('height','0');
			$('<%=id%>_direct').setStyle('display','block');
			$('<%=id%>_db').setStyle('display','none');	
			$('<%=id%>_sended').setStyle('display','none');
		});	
		//DB추출
		$(frm.eTargetType[2]).addEvent('click', function(){
			$('<%=id%>_fileUpload').setStyle('visibility','hidden');
			$('<%=id%>_fileUpload').setStyle('height','0');
			$('<%=id%>_direct').setStyle('display','none');
			$('<%=id%>_db').setStyle('display','block');	
			$('<%=id%>_sended').setStyle('display','none');
		});	
		//기존발송추출
		$(frm.eTargetType[3]).addEvent('click', function(){
			$('<%=id%>_fileUpload').setStyle('visibility','hidden');
			$('<%=id%>_fileUpload').setStyle('height','0');
			$('<%=id%>_direct').setStyle('display','none');
			$('<%=id%>_db').setStyle('display','none');	
			$('<%=id%>_sended').setStyle('display','block');
		});			
	});

		
	</script>
<%} %>


<%
//****************************************************************************************************/
// 업로드가 완료되면 파일 정보 불러오기 
//****************************************************************************************************/
if(method.equals("getFileInfo")) {
%>
	<div style="float:left">
		<a href="target/targetlist/target.do?method=fileDownload&uploadKey=<c:out value="${fileUpload.uploadKey}"/>"><img src="images/trees/csv_file.gif" />
		<c:out value="${fileUpload.realFileName}"/></a>
	</div>
	
<%
}
%>		

<%
//****************************************************************************************************/
// 파일 헤더 및 원투원 불러오기 
//****************************************************************************************************/
if(method.equals("getFileHeader")) {
	
	//모델을 이용하여  모든 서브메뉴의 윈도우 리스트를 가져온다.
	TargetListService service = TargetListControlHelper.getUserService(application);
	List<OneToOne> ontooneList = service.listOneToOne();
	String targetID = ServletUtil.getParamStringDefault(request, "targetID","0");
	String uploadKey = ServletUtil.getParamStringDefault(request, "uploadKey","");

	int cnt = 0;
	
	// 필수 입력 이메일 아이디를 변수에 저장
	for(OneToOne onetoone: ontooneList) {
		if(onetoone.getOnetooneName().equals("[이메일]")) {
	%>
		<script type="text/javascript">
			$('<%=id%>').ontooneEmailID = '<%=onetoone.getOnetooneID()%>';
		</script>
		
	<%
		}else if(onetoone.getOnetooneName().equals("[휴대폰]")){
	%>
			<script type="text/javascript">
				$('<%=id%>').ontooneHpID = '<%=onetoone.getOnetooneID()%>';
			</script>
			
	<%			
		}
	}
	
%>

	<c:forEach items="${fileHeaderInfoList}" var="fileHeaderInfo">

	<TR class="tbl_tr" >
		<TD class="tbl_td dotted" style="width:60px;height:26px"><b><c:out value="${fileHeaderInfo.columnNumber}" /></b></TD>		
		<TD class="tbl_td dotted"><c:out value="${fileHeaderInfo.title}" escapeXml="true"/></TD>
		<TD class="tbl_td dotted" style="width:250px">
			<select id="eFileOneToOne" name="eFileOneToOne" onChange="$('<%=id%>').chkDuplicateSelectFile(this,'eFileOneToOne','<c:out value="${list.index}" />')">
				<option value="">이 필드는 제외</option>
			
			<%
				int ontooneID = -1;
			
				if(!targetID.equals("0") && uploadKey.equals("")) {  // 수정이면서 새로운 업로드가 아닐때 셀렉트를 지정한다.
					cnt ++;
					List<OnetooneTarget> onetooneTargetList = service.getOnetoOneTargetByColumnPos(Integer.parseInt(targetID),cnt);
					
					if(onetooneTargetList != null && onetooneTargetList.size() > 0) {
						ontooneID = onetooneTargetList.get(0).getOnetooneID();
					}
				}
				
				for(OneToOne onetoone: ontooneList) {
			%>
			
				<option value="<c:out value="${fileHeaderInfo.columnNumber}" />≠<%=onetoone.getOnetooneID()%>≠<c:out value="${fileHeaderInfo.title}" escapeXml="true"/>" <%=((ontooneID==onetoone.getOnetooneID())?"selected":"")%>><%=onetoone.getOnetooneName()%></option>
				
			<%
				}
			%>
			</select>
			
		</TD>
	</TR>	
	
	</c:forEach>
	
<%
}
//****************************************************************************************************/
// 직접입력 헤더 및 원투원 불러오기 
//****************************************************************************************************/
if(method.equals("getDirectHeader")) {
	
	//모델을 이용하여  모든 서브메뉴의 윈도우 리스트를 가져온다.
	TargetListService service = TargetListControlHelper.getUserService(application);
	List<OneToOne> ontooneList = service.listOneToOne();	
	String targetID = ServletUtil.getParamStringDefault(request, "targetID","0");
	String directText = ServletUtil.getParamStringDefault(request, "eDirectText","");
	
	int cnt = 0;
	
	// 필수 입력 이메일 아이디를 변수에 저장
	for(OneToOne onetoone: ontooneList) {
		if(onetoone.getOnetooneName().equals("[이메일]")) {
	%>
		<script type="text/javascript">
			$('<%=id%>').ontooneEmailID = '<%=onetoone.getOnetooneID()%>';
		</script>
		
	<%
		}else if(onetoone.getOnetooneName().equals("[휴대폰]")){
	%>
			<script type="text/javascript">
				$('<%=id%>').ontooneHpID = '<%=onetoone.getOnetooneID()%>';
			</script>
			
	<%				
		}
	}
	
%>

	<c:forEach items="${directHeaderInfoList}" var="fileHeaderInfo">
	<TR class="tbl_tr" >
		<TD class="tbl_td dotted" style="width:60px;height:26px"><b><c:out value="${fileHeaderInfo.columnNumber}" /></b></TD>
		<TD class="tbl_td dotted"><c:out value="${fileHeaderInfo.title}" escapeXml="true"/></TD>
		<TD class="tbl_td dotted" style="width:250px">
			<select id="eDirectOneToOne" name="eDirectOneToOne" onChange="$('<%=id%>').chkDuplicateSelectFile(this,'eDirectOneToOne','<c:out value="${list.index}" />')">
				<option value="">이 필드는 제외</option>
			<%
				int ontooneID = -1;
			
				if(targetID!=null && !targetID.equals("0")) {  // 수정이면서 새로운 업로드가 아닐때 셀렉트를 지정한다.
					cnt ++;
					List<OnetooneTarget> onetooneTargetList = service.getOnetoOneTargetByColumnPos(Integer.parseInt(targetID),cnt);
					if(onetooneTargetList != null && onetooneTargetList.size() > 0) {
						ontooneID = onetooneTargetList.get(0).getOnetooneID();					
					}
				}
				
				for(OneToOne onetoone: ontooneList) {
			%>
				<option value="<c:out value="${fileHeaderInfo.columnNumber}" />≠<%=onetoone.getOnetooneID()%>≠<c:out value="${fileHeaderInfo.title}" escapeXml="true"/>" <%=((ontooneID==onetoone.getOnetooneID())?"selected":"")%>><%=onetoone.getOnetooneName()%></option>
			<%
				}
			%>
			</select>
		</TD>
	</TR>	
	
	</c:forEach>
		

<%
}
%>
<%
//****************************************************************************************************/
//DB추출 쿼리 필드 가져 오기 (쿼리검증)
//****************************************************************************************************/
if(method.equals("makeOneToOneForQuery")) {

	TargetListService service = TargetListControlHelper.getUserService(application);
	List<OneToOne> ontooneList = service.listOneToOne();
	List<OnetooneTarget> onetooneTargetList = null;
	String targetID = ServletUtil.getParamStringDefault(request, "targetID","0");
	List<String> columns = null; 

	if(!targetID.equals("0")) {
	
		onetooneTargetList = service.viewOnetooneTarget( Integer.parseInt(targetID) );
		columns = new ArrayList();
		
		for(OnetooneTarget onetooneTarget: onetooneTargetList) {
			columns.add( onetooneTarget.getFieldName() );				
		}
		%>
		<c:set var="columns" value="<%=columns%>"/>
		<%
		
	}
	
	int cnt = 0;
	
	// 필수 입력 이메일 아이디를 변수에 저장
	for(OneToOne onetoone: ontooneList) {
		if(onetoone.getOnetooneName().equals("[이메일]")) {
	%>
		<script type="text/javascript">
			$('<%=id%>').ontooneEmailID = '<%=onetoone.getOnetooneID()%>';
		</script>
		
	<%
		}else if(onetoone.getOnetooneName().equals("[휴대폰]")){
	%>
			<script type="text/javascript">
				$('<%=id%>').ontooneHpID = '<%=onetoone.getOnetooneID()%>';
			</script>
			
	<%							
		}
	}
%>
	
	<c:forEach items="${columns}" var="column" varStatus="list">

	<TR class="tbl_tr" >
		<TD class="tbl_td dotted" style="width:40px;height:26px"><b><%=++cnt%></b></TD>		
		<TD class="tbl_td dotted"><c:out value="${column}" escapeXml="true"/></TD>
		<TD class="tbl_td dotted" style="width:150px">
			<select id="eQueryOneToOne" name="eQueryOneToOne" onChange="$('<%=id%>').chkDuplicateSelectQuery(this,'eQueryOneToOne', '<c:out value="${list.index}" />')">
				<option value="">선택</option>
			
			<%
				int ontooneID = -1;
			
				if(!targetID.equals("0")) {  // 수정이면서 새로운 업로드가 아닐때 셀렉트를 지정한다.
					ontooneID = onetooneTargetList.get(cnt-1).getOnetooneID();
				}
				
				for(OneToOne onetoone: ontooneList) {
			%>
			
				<option value="<%=onetoone.getOnetooneID()%>" <%=((ontooneID==onetoone.getOnetooneID())?"selected":"")%>><%=onetoone.getOnetooneName()%></option>
				
			<%
				}
			%>
			</select>
			
			<input type="hidden" id="eFieldName" name="eFieldName" value="<c:out value="${column}" escapeXml="true"/>"/>
		</TD>
		<TD class="tbl_td dotted" style="width:300px">
			<input type="text" id="eDescript" name="eDescript" value="<%=(!targetID.equals("0")?onetooneTargetList.get(cnt-1).getFieldDesc():"")%>" size="48"/>
		</TD>
	</TR>	
	
	</c:forEach>
		
	<script type="text/javascript">
	window.addEvent('domready', function(){

		makeSelectBox.render( $('<%=id%>_queryOneToOne') );
	});

	
	function settingDescript(){
		
		var index = $('<%=id%>_form').eQueryOneToOne.selectedIndex;
		index = index -1;
		if(index<0){
			$('<%=id%>_form').eDescript.value="";
			
			return;
		}

		$('<%=id%>_form').eDescript.value = $('<%=id%>_form').eFieldDesc[index].value;
		
			
	}
	
	</script>
	
<%
}
%>

<%
//****************************************************************************************************/
//기존발송 쿼리 필드 가져 오기 (쿼리검증)
//****************************************************************************************************/
if(method.equals("makeOneToOneForSended")) {
	TargetListService service = TargetListControlHelper.getUserService(application);
	List<OneToOne> ontooneList = service.listOneToOne();
	List<OnetooneTarget> onetooneTargetList = null;
	String targetID = ServletUtil.getParamStringDefault(request, "targetID","0");
	List<String> columns = null; 

	if(!targetID.equals("0")) {
	
		onetooneTargetList = service.viewOnetooneTarget( Integer.parseInt(targetID) );
		columns = new ArrayList();
		
		for(OnetooneTarget onetooneTarget: onetooneTargetList) {
			columns.add( onetooneTarget.getFieldName() );				
		}
		%>
		<c:set var="columns" value="<%=columns%>"/>
		<%
		
	}
	
	int cnt = 0;
	
	// 필수 입력 이메일 아이디를 변수에 저장
	for(OneToOne onetoone: ontooneList) {
		if(onetoone.getOnetooneName().equals("[이메일]")) {
	%>
		<script type="text/javascript">
			$('<%=id%>').ontooneEmailID = '<%=onetoone.getOnetooneID()%>';
		</script>
		
	<%
		}else if(onetoone.getOnetooneName().equals("[휴대폰]")){
	%>
		<script type="text/javascript">
			$('<%=id%>').ontooneHpID = '<%=onetoone.getOnetooneID()%>';
		</script>
	<%					
		}
	}
%>
	
	<c:forEach items="${columns}" var="column" varStatus="list">

	<TR class="tbl_tr" >
		<TD class="tbl_td dotted" style="width:40px;height:26px"><b><%=++cnt%></b></TD>		
		<TD class="tbl_td dotted"><c:out value="${column}" escapeXml="true"/></TD>
		<TD class="tbl_td dotted" style="width:150px">
			<select id="eQueryOneToOne" name="eQueryOneToOne" onChange="$('<%=id%>').chkDuplicateSelectQuery(this,'eQueryOneToOne', '<c:out value="${list.index}" />')">
				<option value="">선택</option>
			
			<%
				int ontooneID = -1;
			
				if(!targetID.equals("0")) {  // 수정이면서 새로운 업로드가 아닐때 셀렉트를 지정한다.
					ontooneID = onetooneTargetList.get(cnt-1).getOnetooneID();
				}
				
				for(OneToOne onetoone: ontooneList) {
			%>
			
				<option value="<%=onetoone.getOnetooneID()%>" <%=((ontooneID==onetoone.getOnetooneID())?"selected":"")%>><%=onetoone.getOnetooneName()%></option>
				
			<%
				}
			%>
			</select>
			
			<input type="hidden" id="eFieldName" name="eFieldName" value="<c:out value="${column}" escapeXml="true"/>"/>
		</TD>
		<TD class="tbl_td dotted" style="width:300px">
			<input type="text" id="eDescript" name="eDescript" value="<%=(!targetID.equals("0")?onetooneTargetList.get(cnt-1).getFieldDesc():"")%>" size="48"/>
		</TD>
	</TR>	
	
	</c:forEach>
		
	<script type="text/javascript">
	window.addEvent('domready', function(){

		makeSelectBox.render( $('<%=id%>_queryOneToOne') );
	});

	
	function settingDescript(){
		
		var index = $('<%=id%>_form').eQueryOneToOne.selectedIndex;
		index = index -1;
		if(index<0){
			$('<%=id%>_form').eDescript.value="";
			
			return;
		}

		$('<%=id%>_form').eDescript.value = $('<%=id%>_form').eFieldDesc[index].value;
		
			
	}
	
	</script>
	
<%
}
%>






