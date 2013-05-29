<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*"%>  
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.target.targetlist.control.*"%>
<%@ page import="web.target.targetlist.service.*"%>
<%@ page import="web.target.targetlist.model.*"%>
<%@ page import="web.admin.dbjdbcset.model.DbJdbcSet"%>
<%@ page import="java.util.*"%>  

<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String dbID = request.getParameter("dbID");



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
	
	
	 
	<c:forEach items="${dbconnectInfoList}" var="dbconnectInfo">
	<TR class="tbl_tr" db_id="<c:out value="${dbconnectInfo.dbID}"/>" >
		<TD class="tbl_td"><b><c:out value="${dbconnectInfo.dbID}" escapeXml="true"/></b></TD>
		<TD class="tbl_td">
			<c:if test="${dbconnectInfo.defaultYN == 'Y'}" >
				<img src='images/lightning.png' title='시스템이용'>
			</c:if>
			<a href="javascript:$('<%=id%>').dbInfoViewWindow(<c:out value="${dbconnectInfo.dbID}"/>)"><c:out value="${dbconnectInfo.connectDBName}"/></a></TD>
		<TD class="tbl_td"><c:out value="${dbconnectInfo.userID}"/></TD>
		<TD class="tbl_td"><c:out value="${dbconnectInfo.useYN}"/></TD>
		<TD class="tbl_td"><c:out value="${dbconnectInfo.tableName}"/></TD>
		<TD class="tbl_td"><c:out value="${dbconnectInfo.totalCount}"/></TD>
		<TD class="tbl_td">
			<c:if test="${dbconnectInfo.state == '1'}" >
				<img src="images/massmail/ready.gif" title="수집대기중"/>
			</c:if>
			<c:if test="${dbconnectInfo.state == '2'}" >
				<img src="images/massmail/sending.gif" title="수집중"/>
			</c:if>
			<c:if test="${dbconnectInfo.state == '3'}" >
				<img src="images/massmail/finish.gif" title="수집완료"/>
			</c:if>				
			<c:if test="${dbconnectInfo.state == '4'}" >
				<img src="images/massmail/error.gif" title="수집중에러"/>
			</c:if>
		</TD>
		<TD class="tbl_td">
			<c:if test="${dbconnectInfo.state == '1'|| dbconnectInfo.state == '2'}" >
				<c:out value="${dbconnectInfo.updateScheduleDate}"/>
			</c:if>	
			<c:if test="${dbconnectInfo.state == '3'}" >
				수집 완료
			</c:if>				
			<c:if test="${dbconnectInfo.state == '4'}" >
				수집중 오류
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
				,cursor: 'pointer' // 커서
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
//편집
//****************************************************************************************************/
if(method.equals("edit")) {
	TargetListService service = TargetListControlHelper.getUserService(application);
	List<DbJdbcSet> dbJdbcSetList = service.getDBList();
%>
	<jsp:useBean id="userID" class="java.lang.String" scope="request" />
	<jsp:useBean id="userName" class="java.lang.String" scope="request" />
	<jsp:useBean id="groupID" class="java.lang.String" scope="request" />
	<jsp:useBean id="groupName" class="java.lang.String" scope="request" />
	
	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<input type="hidden" id="eFileName" name="eFileName">
			<input type="hidden" id="eNewFile" name="eNewFile">		
			<input type="hidden" id="eUserID" name="eUserID" value="<%=userID %>">
			<input type="hidden" id="eGroupID" name="eGroupID" value="<%=groupID %>">
			<input type="hidden" id="queryCMD" name="queryCMD" value="">
			<table class="ctbl" width="100%">
			<tbody>	
			<tr>				
				<td class="ctbl ttd1" width="100px">DB선택</td>
				<td class="ctbl td">
					<select id="edbID" name="edbID">
						<option value="">DB를 선택 하세요 </option>
						<%for(DbJdbcSet dbJdbcSet : dbJdbcSetList) { %>
							<option value="<%=dbJdbcSet.getDbID()%>" <% if(dbJdbcSet.getDbID().equals(dbID) || dbJdbcSet.getDefaultYN().equals("Y") ){ %> selected <%} %>><%=dbJdbcSet.getDbName()%></option>
						<%}%>
						<input type="hidden" id="eCheckdbID" name="eCheckdbID" value="<%=dbID%>" />
					</select>
				</td>
			</tr>
			<tr>				
				<td class="ctbl ttd1" width="100px">DB연동 명</td>
				<td class="ctbl td">
					<input type="text" id="eConnectDBName" name="eConnectDBName" value='<c:out value="${dbconnectInfo.connectDBName}"/>' size="125" mustInput="Y" msg="DB연동 명"/>
				</td>
			</tr>		
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>										
			<tr>				
				<td class="ctbl ttd1" width="100px">사용여부</td>
				<td class="ctbl td">
				<div style="float:left">
				<ul id="eUseYN"  class="selectBox">
					<li data="Y" <c:if test="${dbconnectInfo.useYN=='Y'}"> select='Y' </c:if>>사용</li>
					<li data="N" <c:if test="${dbconnectInfo.useYN=='N'}"> select='Y' </c:if>>미사용</li>					
				</ul>
				</div>
				<% if(dbID!=null && !dbID.equals("")){ %>
					<div style="float:left;margin-left:10px;margin-top:2px">
						<input type="checkbox" class="notBorder" value="Y" id="eQueryUpdateYN" name="eQueryUpdateYN" onclick="javascript:$('<%=id%>').chkQueryUpdate()">&nbsp;쿼리수정
					</div>
				<%} %>
				</td>
			</tr>						
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>						
			<tr>				
				<td class="ctbl ttd1" width="100px">수집쿼리
				<div style="margin-top:5px">
				<img src="images/help.gif" title="<div style='text-align:left'><strong>Query입력시 주의사항 :</strong><br>												
				1. 컬럼명을 정확히 입력하시고 (*)를 사용하지마세요.<br>
				2. 반드시 이메일이 포함된 컬럼명이 입력되어야 합니다.<br>
				3. INSERT, UPDATE, DELETE문은 사용하지마세요.<br>
				4. 복잡한 컬럼명은  단순화하여(alias) 주시기 바랍니다. <br>(예: select (member.email) as email from....)<br>
				5. 프로시저를 사용하지마세요.</div>"/>
				</div>				
				</td>
				<td class="ctbl td">
				<div style="float:left">
					<textarea id="eQueryText" name="eQueryText" style="width:510px;height:60px" ><c:out value="${dbconnectInfo.queryText}"/></textarea>
					<input type="hidden" id="eCheckQueryText" name="eCheckQueryText" value="<c:out value="${dbconnectInfo.queryText}"/>">
				</div>
				<div style="float:left;margin:20px 0 0 15px">
					<div id="<%=id%>_validateQueryBtn" style="float:left;height:20px;" class="btn_green"><a id="mailLink" style ="cursor:pointer" href="javascript:$('<%=id%>').getQueryColumn()"><span>쿼리검증</span></a></div>
					<div id="<%=id%>_validateQueryImg" style="float:left;margin-left:5px">
				</div> 				
				</td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>										
			<tr>				
				<td class="ctbl ttd1" width="100px">수집주기</td>
				<td class="ctbl td">
				<ul id="eUpdateType" class="selectBox" onchange="javascript:$('<%=id%>').chgConnectDate(this.value)" mustInput='Y'>
					<li data="">--선택--</li>
					<li data="1" <c:if test="${dbconnectInfo.updateType=='1'}"> select='Y' </c:if>>1회수집</li>
					<li data="2" <c:if test="${dbconnectInfo.updateType=='2'}"> select='Y' </c:if>>매주</li>
					<li data="3" <c:if test="${dbconnectInfo.updateType=='3'}"> select='Y' </c:if>>매월</li>
				</ul>
				</td>
			</tr>		
			<tr>				
				<td class="ctbl ttd1" width="100px">수집시간</td>
				<td class="ctbl td">
				<div style="float:left">
					<select id="eUpdateScheduleHour" name="eUpdateScheduleHour">
					<c:forEach var="hours" begin="0" end="23" step="1"> 
						<option value="<c:out value="${hours}"/>" <c:if test="${dbconnectInfo.updateScheduleHour==hours}"> selected </c:if>><c:out value="${hours}"/></option>
					</c:forEach>
					</select>	
				</div>
				<div style="float:left;margin-left:5px;margin-top:3px">시</div>						
				<div style="float:left;margin-left:10px">
					<select id="eUpdateScheduleMinute" name="eUpdateScheduleMinute">
					<c:forEach var="minutes" begin="0" end="59" step="10"> 
						<option value="<c:out value="${minutes}"/>" <c:if test="${dbconnectInfo.updateScheduleMinute==minutes}">selected</c:if>><c:out value="${minutes}"/></option>
					</c:forEach>
					</select>		
				</div>		
				<div style="float:left;margin-left:5px;margin-top:3px">분</div>			
				</td>
				</tr>
				<% if(dbID!=null && !dbID.equals("")){ %>
				<tr>				
					<td class="ctbl ttd1" width="100px">실수집시작일</td>
					<td class="ctbl td">					
						<c:out value="${dbconnectInfo.updateStartDate}"/>
					</td>
				</tr>	
				<tr>				
					<td class="ctbl ttd1" width="100px">실수집종료일</td>
					<td class="ctbl td">					
						<c:out value="${dbconnectInfo.updateEndDate}"/>
					</td>
				</tr>	
				<tr>				
					<td class="ctbl ttd1" width="100px">생성테이블명</td>
					<td class="ctbl td">					
						<c:out value="${dbconnectInfo.tableName}"/> (생성테이블명은 수집때마다 변경이 됩니다)
					</td>
				</tr>											
				<%} %>
			</tbody>
			</table>
			<div style="display:none;width:100%" id="<%=id %>_one">
			<table class="ctbl" width="100%">
			<tbody>		
			<tr>
				<td class="ctbl ttd1" width="100px">1회수집일</td>
				<td class="ctbl td">
				<div style="float:left;margin-top:2px">
				<fmt:parseDate value="${dbconnectInfo.updateScheduleDate}"  var ="fmt_sendScheduleDate" pattern="yyyy-MM-dd"/>									
				<c:if test="${dbconnectInfo.updateScheduleDate!=null}">	
				<input type="text" id="eUpdateSchedule" name="eUpdateSchedule" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<fmt:formatDate value="${fmt_sendScheduleDate}" pattern="yyyy-MM-dd"/>"/>
				</c:if>
				<c:if test="${dbconnectInfo.updateScheduleDate==null}">
				<input type="text" id="eUpdateSchedule" name="eUpdateSchedule" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this)" value="<%=DateUtils.getDateString() %>"/>
				</c:if>									
				<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_form').eUpdateSchedule)" align="absmiddle" />
				</div>
				</td>
			</tr>	
			</tbody>	
			</table>				
			</div>			
			<div style="display:none;width:100%" id="<%=id %>_week">
			<table class="ctbl" width="100%">
			<tbody>		
			<tr>
				<td class="ctbl ttd1" width="100px">매주</td>
				<td class="ctbl td">
				<input type="radio" class="notBorder" id="eUpdateValue" name="eUpdateValue" <c:if test="${dbconnectInfo.updateValue=='1'}">checked</c:if> value="1" />일&nbsp;&nbsp;
				<input type="radio" class="notBorder" id="eUpdateValue" name="eUpdateValue" <c:if test="${dbconnectInfo.updateValue=='2'}">checked</c:if> value="2" />월&nbsp;&nbsp;
				<input type="radio" class="notBorder" id="eUpdateValue" name="eUpdateValue" <c:if test="${dbconnectInfo.updateValue=='3'}">checked</c:if> value="3" />화&nbsp;&nbsp;
				<input type="radio" class="notBorder" id="eUpdateValue" name="eUpdateValue" <c:if test="${dbconnectInfo.updateValue=='4'}">checked</c:if> value="4" />수&nbsp;&nbsp;
				<input type="radio" class="notBorder" id="eUpdateValue" name="eUpdateValue" <c:if test="${dbconnectInfo.updateValue=='5'}">checked</c:if> value="5" />목&nbsp;&nbsp;
				<input type="radio" class="notBorder" id="eUpdateValue" name="eUpdateValue" <c:if test="${dbconnectInfo.updateValue=='6'}">checked</c:if> value="6" />금&nbsp;&nbsp;
				<input type="radio" class="notBorder" id="eUpdateValue" name="eUpdateValue" <c:if test="${dbconnectInfo.updateValue=='7'}">checked</c:if> value="7" />토&nbsp;&nbsp;	
				</td>
			</tr>	
			</tbody>	
			</table>				
			</div>
			<div style="display:none;width:100%" id="<%=id %>_month">
			<table class="ctbl" width="100%">
			<tbody>		
			<tr>
				<td class="ctbl ttd1" width="100px">매월</td>
				<td class="ctbl td">
				<div style="float:left">
				<ul id="eUpdateValue"  class="selectBox">
				<c:forEach var="days" begin="1" end="28" step="1"> 
				<li data="<c:out value="${days}"/>" <c:if test="${dbconnectInfo.updateValue==days}">select='Y'</c:if>><c:out value="${days}"/></li>
				</c:forEach>						
				</ul>
				</div>
				<div style="float:left;margin-left:5px;margin-top:3px">일</div>		
				</td>
			</tr>		
												
			</tbody>		
			</table>			
			</div>			
			<div>
				<table class="ctbl" style="width:770px">
				<tbody>
					<tr>
						<td>
							<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="770px" >
								<thead>
									<tr>
									<th style="height:26px;width:200px">컬럼명</th>
									<th style="height:26px;width:200px">컬럼타입</th>
									<th style="height:26px;width:100px">길이</th>
									<th style="height:26px;">설명</th>
									</tr>
								</thead>
								</TABLE>
								<div style="vertical-align:top;overflow-y:scroll;width:770px" id="<%=id%>_queryOneToOneWrapper">
							
								<TABLE style="border:0px" class="tbl" border="0" cellspacing="0" cellpadding="0" width="740px" >
								<tbody id="<%=id%>_listQueryColumn" >	
									
								
								</tbody>
								</table>
								</div>
						</td>
					</tr>
				</tbody>
				</table>	
			</div>
		</form>
	</div>
	

	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData('<%=dbID %>')" class="web20button bigblue">저 장</a></div>

	<script language="javascript">
		makeSelectBox.render($('<%=id%>_form'));	
		
		var frm = $('<%=id%>_form');
		
		if(frm.eUpdateType.value=='1'){
			$('<%=id %>_one').setStyle('display','block');
			$('<%=id %>_week').setStyle('display','none');
			$('<%=id %>_month').setStyle('display','none');
		}
		else if(frm.eUpdateType.value=='2'){
			$('<%=id %>_one').setStyle('display','none');
			$('<%=id %>_week').setStyle('display','block');
			$('<%=id %>_month').setStyle('display','none');
		}
		else if(frm.eUpdateType.value=='3'){
			$('<%=id %>_one').setStyle('display','none');
			$('<%=id %>_week').setStyle('display','none');
			$('<%=id %>_month').setStyle('display','block');
		}
		
		<%if(!dbID.equals("")){%>
			$('<%=id%>').showQueryOneToOneEdit();
		<%}%>
		
	</script>

<%
} 
//****************************************************************************************************/
//쿼리 필드 가져 오기 (쿼리실행)
//****************************************************************************************************/
if(method.equals("listQueryColumn")) {	
%>
	<jsp:useBean id="queryText" class="java.lang.String" scope="request" />
	<jsp:useBean id="result" class="java.lang.String" scope="request" />
	<c:forEach items="${columnList}" var="column">
	<TR class="tbl_tr" >
		<TD class="tbl_td dotted" style="width:200px;height:26px" escapeXml="true">
		<input type="hidden" id="eColumnName" name="eColumnName"  value="<c:out value="${column.columnName}"/>">
		<b><c:out value="${column.columnName}"/></b>
		</TD>		
		<TD class="tbl_td dotted" style="width:200px;height:26px" align="center">
		<ul id="eColumnType"  class="selectBox">
		<li data="">--선택--</li>
		<li data="VARCHAR" <c:if test="${column.columnType=='VARCHAR'}">select='Y'</c:if>>VARCHAR</li>
		<li data="CHAR" <c:if test="${column.columnType=='CHAR'}">select='Y'</c:if>>CHAR</li>
		<li data="INTEGER" <c:if test="${column.columnType=='INTEGER'}">select='Y'</c:if>>INTEGER</li>
		<li data="DOUBLE" <c:if test="${column.columnType=='DOUBLE'}">select='Y'</c:if>>DOUBLE</li>
		<li data="DATETIME" <c:if test="${column.columnType=='DATETIME'}">select='Y'</c:if>>DATETIME</li>
		</ul>
		</TD>	
		<TD class="tbl_td dotted" style="width:100px;height:26px" align="center"><input type="text" id="eColumnLength" name="eColumnLength" size="3" maxlength="3" value="<c:out value="${column.columnLength}"/>"></TD>	
		<TD class="tbl_td dotted" style="height:26px" align="center"><input type="text" id="eColumnDesc" name="eColumnDesc" size="25" title="컬럼설명" /></TD>	
	</TR>	
	</c:forEach>	
	<script language="javascript">
		makeSelectBox.render($('<%=id%>_form'));
		var frm = $('<%=id%>_form');
		
		<%if(result.equals("fail")){%>
			alert("기존에 등록 되어 있는 컬럼은 삭제 하실 수 없습니다.");
			$('<%=id%>').getQueryColumn();
			frm.eQueryText.value = "<%=queryText%>";
		<%}%>
	</script>
<%} %>
		