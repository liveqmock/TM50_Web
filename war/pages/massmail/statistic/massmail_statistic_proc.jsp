<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="web.common.util.*" %> 
<%@ page import="java.util.List" %>
<%@ page import="web.massmail.statistic.control.MassMailStatControlHelper" %>
<%@ page import="web.massmail.statistic.service.MassMailStatService" %>
<%@ page import="web.massmail.statistic.model.*" %>
<%@ page import="web.massmail.write.model.*" %>
<%@ page import="web.content.poll.model.*" %>

<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String massmailID = request.getParameter("massmailID");
String scheduleID = request.getParameter("scheduleID");
String pollID = request.getParameter("pollID");

MassMailStatService service = MassMailStatControlHelper.getUserService(application);

//****************************************************************************************************/
// 대량메일 - 기본정보
//****************************************************************************************************/
if(method.equals("statisticbasic")) {
	String today = DateUtils.getStrByPattern("yyyy-MM-dd HH:mm:ss");
%>
	<jsp:useBean id="targetGroupList" class="java.lang.Object" scope="request" />
	<jsp:useBean id="massmailSendInfo" class="web.massmail.statistic.model.MassMailInfo" scope="request" />
	<jsp:useBean id="massmailInfo" class="web.massmail.statistic.model.MassMailInfo" scope="request" />
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<h2>기본정보</h2>
		<div class="dash_box_content">
			<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<table class="ctbl" width="98%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="120px">대량메일 명 </td>
				<td class="ctbl td" width="130px"><c:out value="${massmailInfo.massmailTitle}"/></td>
				<td class="ctbl ttd1" width="120px">작성자</td>
				<td class="ctbl td"><c:out value="${massmailInfo.userName}"/></td>
				<td class="ctbl ttd1" width="120px">발송타입 </td>
				<td class="ctbl td" width="130px"><c:out value="${massmailInfo.sendType}"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1">발송 대상자 그룹</td>
				<td class="ctbl td" colspan="3">
					<c:forEach items="${targetGroupList}" var="targetgroup">
						<c:out value="${targetgroup.targetName}"/> [<c:out value="${targetgroup.targetCount}"/>통] &nbsp;&nbsp;&nbsp;
					</c:forEach>
				</td>
				<td class="ctbl ttd1">예상 인원</td>
				<td class="ctbl td"><fmt:formatNumber value="${massmailInfo.targetTotalCount}" type="number"/> 통</td>
			</tr>		
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1">작성 일시</td>
				<td class="ctbl td"><c:out value="${massmailInfo.registDate}"/></td>
				<td class="ctbl ttd1">발송 일시</td>
				<td class="ctbl td">
					<%if(!massmailInfo.getSendStartTime().equals("-")){ %>	
						<c:out value="${massmailInfo.sendStartTime}"/>
					<%}else{ %>
						<c:out value="${massmailInfo.sendScheduleDate}"/>
					<%}%> 
				</td>
				<td class="ctbl ttd1">현재 상태</td>
				<td class="ctbl td"><c:out value="${massmailInfo.stateDesc}"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1">메일본문링크</td>
				<td class="ctbl td">
					수집
					<c:if test="${massmailInfo.mailLinkYN == 'N'}">안함</c:if>
				</td>
				<td class="ctbl ttd1">통계 수집 완료일</td>
				<td class="ctbl td">
				<%if(massmailInfo.getState().equals("15")){ %>	
					<c:out value="${massmailInfo.statisticsEndDate}"/>
				<%}else{ %>
					-
				<%}%> 
				</td>
				<td class="ctbl ttd1">통계 수집 상태</td>
				<td class="ctbl td">
				<%if(massmailInfo.getState().equals("15")){ %>			
					<%if(DateUtils.hoursBetween( today, massmailInfo.getStatisticsEndDate(),"yyyy-MM-dd HH:mm:ss") > 0){%>
						수집중
					<%}else{ %>
						수집완료
					<%} 
				}else{%>
					수집대기중
				<%} %>
				</td>
			</tr>				
			</tbody>
			</table>
			<div style="margin:7px 0;"> <img src="images/tag_blue.png" alt="통계 수집 안내 "> 발송이 완료 된 메일에 대해서만 통계(오픈/클릭/수신거부)를 수집합니다. 통계 수집 완료일이 지나면 통계 수집을 완료(정지)합니다.</div>
			<table class="ctbl" width="98%">
			<tbody>
				<tr>
					<td class="ctbl ttd1" width="120px">제외타게팅</td>
					<td class="ctbl td" width="130px" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','0','<c:out value="${massmailFilter.filterType0}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${massmailFilter.filterType0}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1" width="120px">메일형식오류</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','1','<c:out value="${massmailFilter.filterType1}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${massmailFilter.filterType1}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1" width="120px">중복필터</td>
					<td class="ctbl td" width="130px" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','2','<c:out value="${massmailFilter.filterType2}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${massmailFilter.filterType2}" type="number"/> 통
						</a> 
					</td>
				</tr>	
				<tr>
					<td class="ctbl ttd1">수신거부필터</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','3','<c:out value="${massmailFilter.filterType3}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${massmailFilter.filterType3}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1">발송제한필터</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','4','<c:out value="${massmailFilter.filterType4}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${massmailFilter.filterType4}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1">미오픈필터</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','5','<c:out value="${massmailFilter.filterType5}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${massmailFilter.filterType5}" type="number"/> 통
						</a> 
					</td>
				</tr>
				<tr>
					<td class="ctbl ttd1">영구적인오류필터</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','6','<c:out value="${massmailFilter.filterType6}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${massmailFilter.filterType6}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1">영구적인실패도메인</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','7','<c:out value="${massmailFilter.filterType7}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${massmailFilter.filterType7}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1">전체합계</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','filterall','<c:out value="${massmailFilter.filterTotal}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${massmailFilter.filterTotal}" type="number"/> 통
						</a>
					</td>
				</tr>	
			</tbody>
			</table><hr />
			<table class="ctbl" width="98%">
			<tbody>
			
				<tr>					
					<td class="ctbl ttd1" width="120px">발송 대상자 수</td>
					<td class="ctbl td" width="130px" align="center">
						<fmt:formatNumber value="${massmailFilter.filterTotal + massmailSendInfo.sendTotal}" type="number"/> 통
					</td>
					<td class="ctbl ttd1" width="120px">제외 대상자 수</td>
					<td class="ctbl td" align="center">
						<fmt:formatNumber value="${massmailFilter.filterTotal}" type="number"/> 통
					</td>
					<td class="ctbl ttd1" width="120px">실 발송 대상자 수</td>
					<td class="ctbl td" width="130px" align="center">
						<fmt:formatNumber value="${massmailSendInfo.sendTotal}" type="number"/> 통
					</td>
				</tr>	
			</tbody>
			</table><hr />		
			<table class="ctbl" width="98%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="180px">메일제목</td>
				<td class="ctbl td" align="center" width="220px"><c:out value="${massmailInfo.mailTitle}"/></td>
				<td class="ctbl ttd1" width="180px">보내는 사람 이름 </td>
				<td class="ctbl td" align="center" colspan="2"><c:out value="${massmailInfo.senderName}"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1">발송시작시간</td>
				<td class="ctbl td" align="center"><c:out value="${massmailInfo.sendStartTime}"/></td>
				<td class="ctbl ttd1">보내는사람 이메일</td>
				<td class="ctbl td" align="center" colspan="2"><c:out value="${massmailInfo.senderMail}"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>	
				<td class="ctbl ttd1">발송종료시간</td>
				<td class="ctbl td" align="center"><c:out value="${massmailInfo.sendEndTime}"/></td>
				<td class="ctbl ttd1">① 대상인원</td>
				<td class="ctbl td" align="center" align="center"><fmt:formatNumber value="${massmailSendInfo.sendTotal}" type="number"/> 통</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','total','<c:out value="${massmailSendInfo.sendTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>	
				<td class="ctbl ttd1">최종재발송시작시간</td>
				<td class="ctbl td" align="center"><c:out value="${massmailInfo.retryStartTime}"/></td>
				<td class="ctbl ttd1">② 성공통수 (②/①)</td>
				<td class="ctbl td" align="center">
					<c:set var="successratio" value="0"/>
					<c:if test="${massmailSendInfo.successTotal > 0}">
						<c:set var="successratio" value="${massmailSendInfo.successTotal / massmailSendInfo.sendTotal * 100}" />	
					</c:if>
					<fmt:formatNumber value="${massmailSendInfo.successTotal}" type="number"/> 통 (<fmt:formatNumber value="${successratio}" pattern="0.00"/> %)
				</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','success','<c:out value="${massmailSendInfo.successTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>
			</tr>		
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>	
				<td class="ctbl ttd1">최종재발송종료시간</td>
				<td class="ctbl td" align="center"><c:out value="${massmailInfo.retryEndTime}"/></td>
				<td class="ctbl ttd1">③ 실패통수 (③/①)</td>
				<td class="ctbl td" align="center">
					<c:set var="failratio" value="0"/>
					<c:if test="${massmailSendInfo.failTotal > 0}">
						<c:set var="failratio" value="${massmailSendInfo.failTotal / massmailSendInfo.sendTotal * 100}"/>	
					</c:if>
					<fmt:formatNumber value="${massmailSendInfo.failTotal}" type="number"/> 통 (<fmt:formatNumber value="${failratio}" pattern="0.00"/> %)
				</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','fail','<c:out value="${massmailSendInfo.failTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>	
				<td class="ctbl ttd1">재발송</td>
				<td class="ctbl td" align="center"><c:out value="${massmailInfo.retryCount}"/> 회</td>
				<td class="ctbl ttd1">④ 오픈통수 (④/②)</td>
				<td class="ctbl td" align="center">
					<c:set var="openratio" value="0"/>
					<c:if test="${massmailSendInfo.openTotal > 0}">
						<c:set var="openratio" value="${massmailSendInfo.openTotal / massmailSendInfo.successTotal * 100}"/>	
					</c:if>
					<fmt:formatNumber value="${massmailSendInfo.openTotal}" type="number"/> 통 (<fmt:formatNumber value="${openratio}" pattern="0.00"/> %)
				</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','open','<c:out value="${massmailSendInfo.openTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>	
				<td class="ctbl ttd1">시간당평균발송량</td>
				<td class="ctbl td" align="center">
					<c:if test="${massmailInfo.avgSendCount > 0}">
						<fmt:formatNumber value="${massmailInfo.avgSendCount}" type="number"/> 통
					</c:if>
					<c:if test="${massmailInfo.avgSendCount == 0}">
						-
					</c:if>
				</td>
				<td class="ctbl ttd1">⑤ 클릭통수 (⑤/④)</td>
				<td class="ctbl td" align="center">
					<c:set var="clickratio" value="0"/>
					<c:if test="${massmailSendInfo.clickTotal > 0}">
						<c:set var="clickratio" value="${massmailSendInfo.clickTotal / massmailSendInfo.openTotal * 100}"/>	
					</c:if>
					<fmt:formatNumber value="${massmailSendInfo.clickTotal}" type="number"/> 통 (<fmt:formatNumber value="${clickratio}" pattern="0.00"/> %)
				</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','click','<c:out value="${massmailSendInfo.clickTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>	
				<td class="ctbl ttd1">메일크기</td>
				<td class="ctbl td" align="center"><fmt:formatNumber value="${massmailInfo.mailContentSize}" pattern=".00"/> KB</td>
				<td class="ctbl ttd1">⑥ 수신거부통수 (⑥/⑤)</td>
				<td class="ctbl td" align="center">
					<c:set var="rejectcallratio" value="0"/>
					<c:if test="${massmailSendInfo.rejectcallTotal > 0}">
						<c:set var="rejectcallratio" value="${massmailSendInfo.rejectcallTotal /massmailSendInfo.clickTotal * 100}"/>	
					</c:if>
					<c:out value="${massmailSendInfo.rejectcallTotal}"/> 통 (<fmt:formatNumber value="${rejectcallratio}" pattern="0.00"/> %)
				</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','rejectcall','<c:out value="${massmailSendInfo.rejectcallTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>
			</tr>
			</tbody>
			</table>
		</form>
		</div>  	
	</div>
	<script type="text/javascript">
		window.addEvent('domready', function(){
			$('<%=id%>LinkStatistic').addEvent('click', function(){
				
				<%if(massmailInfo.getMailLinkYN().equals("Y")){%>
				$('<%=id%>_sform').standard.value = 'link';
				$('<%=id%>').loadContent('statisticbar');
				$('<%=id%>List').setStyle('display','block');
				$('<%=id%>_button').setStyle('display','block');
				$('<%=id%>ChartBtn').innerHTML = '원 그래프 조회';
				$('<%=id%>').getData();
				$('<%=id%>_notLink').setStyle('display','none');
				<%}else{%>
					$('<%=id%>List').setStyle('display','none');
					$('<%=id%>_button').setStyle('display','none');
					$('<%=id%>Content').setStyle('display','none');
					$('<%=id%>_notLink').setStyle('display','block');
				<%}%>
			});
			<%if(massmailInfo.getState().equals("00") || massmailInfo.getState().equals("01") || massmailInfo.getState().equals("11")){%>
				$('<%=id%>_sform').personPreviewYN.value = "N";
			<%}%>
			$('<%=id%>_sform').retryFinishYN.value = "<%=massmailInfo.getRetryFinishYN()%>";
			$('<%=id%>_sform').backupYearMonth.value = "<%=massmailInfo.getBackupYearMonth()%>";
		});
	</script>
<%
}
//****************************************************************************************************/
//기본 통계 리스트
//****************************************************************************************************/
if(method.equals("statisticlist")) {
	String type = request.getParameter("type");
%>
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<div><h2>통계 리스트</h2></div>
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
			<thead>
					<tr>
						<th style="height:30px;">기준값</th>
						<th style="height:30px;width:120px;">전체통수</th>
						<th style="height:30px;width:120px;">성공통수</th>
						<th style="height:30px;width:120px;">실패통수</th>
						<th style="height:30px;width:120px;">오픈통수</th>
						<th style="height:30px;width:120px;">클릭통수</th>
						<th style="height:30px;width:120px;">수신거부통수</th>
					</tr>
			</thead>
			<tbody id="<%=id%>_grid_content">
			<c:set var="totalsend" value="0"/>
			<c:set var="totalssuccess" value="0"/>
			<c:set var="totalfail" value="0"/>
			<c:set var="totalopen" value="0"/>
			<c:set var="totalclick" value="0"/>
			<c:set var="totalrejectcall" value="0"/>
			<c:forEach items="${statisticList}" var="statistic">
				<TR class="tbl_tr" >
					<TD class="tbl_td" ><b><c:out value="${statistic.viewStandard}"/></b></TD>	
					<%if(type.equals("target")){ %>	
					<TD class="tbl_td"><fmt:formatNumber value="${statistic.sendTotal}" type="number"/></TD>	
					<TD class="tbl_td"><fmt:formatNumber value="${statistic.successTotal}" type="number"/></TD>		
					<TD class="tbl_td"><fmt:formatNumber value="${statistic.failTotal}" type="number"/></TD>	
					<TD class="tbl_td"><fmt:formatNumber value="${statistic.openTotal}" type="number"/></TD>	
					<TD class="tbl_td"><fmt:formatNumber value="${statistic.clickTotal}" type="number"/></TD>
					<TD class="tbl_td"><fmt:formatNumber value="${statistic.rejectcallTotal}" type="number"/></TD>
					<%}else{ %>
					<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','total','<c:out value="${statistic.sendTotal}"/>')"><fmt:formatNumber value="${statistic.sendTotal}" type="number"/></a></TD>	
					<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','success','<c:out value="${statistic.successTotal}"/>')"><fmt:formatNumber value="${statistic.successTotal}" type="number"/></a></TD>		
					<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','fail','<c:out value="${statistic.failTotal}"/>')"><fmt:formatNumber value="${statistic.failTotal}" type="number"/></a></TD>	
					<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','open','<c:out value="${statistic.openTotal}"/>')"><fmt:formatNumber value="${statistic.openTotal}" type="number"/></a></TD>	
					<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','click','<c:out value="${statistic.clickTotal}"/>')"><fmt:formatNumber value="${statistic.clickTotal}" type="number"/></a></TD>
					<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.standard}"/>','rejectcall','<c:out value="${statistic.rejectcallTotal}"/>')"><fmt:formatNumber value="${statistic.rejectcallTotal}" type="number"/></a></TD>
					<%} %>
				</TR>
				<c:set var="totalsend" value="${totalsend + statistic.sendTotal}"/>
				<c:set var="totalssuccess" value="${totalssuccess + statistic.successTotal}"/>
				<c:set var="totalfail" value="${totalfail + statistic.failTotal}"/>
				<c:set var="totalopen" value="${totalopen + statistic.openTotal}"/>
				<c:set var="totalclick" value="${totalclick + statistic.clickTotal}"/>
				<c:set var="totalrejectcall" value="${totalrejectcall + statistic.rejectcallTotal}"/>
				
			</c:forEach>
			<c:if test="${!empty statisticList}">
				<TR >
					<TD class="tbl_td" ><strong>합 계</strong></TD>
					<TD class="tbl_td" ><strong><fmt:formatNumber value="${totalsend}" type="number"/></strong></TD>
					<TD class="tbl_td" ><strong><fmt:formatNumber value="${totalssuccess}" type="number"/></strong></TD>
					<TD class="tbl_td" ><strong><fmt:formatNumber value="${totalfail}" type="number"/></strong></TD>
					<TD class="tbl_td" ><strong><fmt:formatNumber value="${totalopen}" type="number"/></strong></TD>
					<TD class="tbl_td" ><strong><fmt:formatNumber value="${totalclick}" type="number"/></strong></TD>
					<TD class="tbl_td" ><strong><fmt:formatNumber value="${totalrejectcall}" type="number"/></strong></TD>
				</TR>
			</c:if>
			</tbody>
		</TABLE>
	</div>
	<script type="text/javascript">
		window.addEvent('domready', function(){
			renderTableHeader( "<%=id %>List" );
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
			});
			$('<%=id%>').grid_content.render();
		});
	</script>	
<%
}
//****************************************************************************************************/
//링크 분석 통계 리스트
//****************************************************************************************************/
if(method.equals("statisticlinklist")) {
%>
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<div><h2>링크 분석 리스트</h2></div>
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
			<thead>
				<tr>
					<th style="height:30px;width:70px;">링크 ID</th>
					<th style="height:30px;width:320px;">링크 NAME</th>
					<th style="height:30px;width:320px;">링크 URL</th>
					<th style="height:30px;width:70px;">링크 속성</th>
					<th style="height:30px;width:70px;">클릭 인원</th>
				</tr>
			</thead>
			<tbody id="<%=id%>_grid_content">
			<c:forEach items="${statisticList}" var="statistic">
				<TR class="tbl_tr" >
					<TD class="tbl_td"><b><c:out value="${statistic.linkID}"/></b></TD>		
					<TD class="tbl_td">
						<c:if test="${statistic.linkDesc == 'image'}">
							<a href="javascript:$('<%=id%>').viewImage('<c:out value="${statistic.linkName}"/>')"><img src="<c:out value="${statistic.linkName}"/>" height="70"></a>
						</c:if>
						<c:if test="${statistic.linkDesc == 'text'}">
							<c:out value="${statistic.linkName}"/>
						</c:if>
						
					</TD>	
					<TD class="tbl_td" align="left"><a href="<c:out value="${statistic.linkURL}"/>" target="_blank"><c:out value="${statistic.linkURL}"/></a></TD>		
					<TD class="tbl_td">
						<c:if test="${statistic.linkType == '1'}">
							일반링크
						</c:if>
						<c:if test="${statistic.linkType == '2'}">
							수신거부
						</c:if>
					</TD>
					<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.linkID}"/>','total','<c:out value="${statistic.linkCount}"/>')"><fmt:formatNumber value="${statistic.linkCount}" type="number"/></a></TD>		
				</TR>
			</c:forEach>
			</tbody>
		</TABLE>
	</div>
	<script type="text/javascript">
		window.addEvent('domready', function(){
			renderTableHeader( "<%=id %>List" );
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
			});
			$('<%=id%>').grid_content.render();
		});
	</script>	
<%
}
//****************************************************************************************************/
//통계 리스트 - 실패도메인
//****************************************************************************************************/
if(method.equals("statisticfaildomainlist")) {
%>
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<div><h2>실패 도메인별 통계 리스트</h2></div>
		<div class="btn_b" id="<%=id%>retryBtn" style="display:none"><a style ="float:right;margin:5px;" href="javascript:$('<%=id%>').retryDomain()"><span>오류자 재발송</span></a></div>
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
			<thead>
				<tr>
					<th style="height:30px;width:30px;"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder" onclick="selectAll($('<%=id%>_list_form').eDomain,this.checked)"/> </th>
					<th style="height:30px;width:150px;">도메인</th>
					<th style="height:30px;width:110px;">발송 통수</th>
					<th style="height:30px;width:110px;">실패 통수</th>
					<th style="height:30px;width:110px;">일시적인오류</th>
					<th style="height:30px;width:110px;">영구적인오류</th>
					<th style="height:30px;width:110px;">기타오류</th>
					<th style="height:30px;width:110px;">결과확인불명</th>
				</tr>
			</thead>
			<tbody id="<%=id%>_grid_content">
			<c:set var="total" value="0"/>
			<c:set var="send" value="0"/>
			<c:set var="type1" value="0"/>
			<c:set var="type2" value="0"/>
			<c:set var="type3" value="0"/>
			<c:set var="type4" value="0"/>
			<c:set var="type5" value="0"/>
			<c:forEach items="${statisticList}" var="statistic">
				<TR class="tbl_tr" >
					<TD class="tbl_td"><input type="checkbox" class="notBorder" id="eDomain" name="eDomain" value="<c:out value="${statistic.domainName}"/>" /></TD>
					<TD class="tbl_td"><c:out value="${statistic.domainName}"/></TD>		
					<TD class="tbl_td"><fmt:formatNumber value="${statistic.sendCount}" type="number"/></TD>
					<TD class="tbl_td">
						<c:set var="failratio" value="${statistic.failCount / statistic.sendCount * 100}" />	
						<a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.domainName}"/>','total','<c:out value="${statistic.failCount}"/>')">
							<fmt:formatNumber value="${statistic.failCount}" type="number"/>
						</a>
						(<fmt:formatNumber value="${failratio}" pattern="0.00"/> %)
					</TD>	
					<TD class="tbl_td">
						<c:set var="type1ratio" value="${statistic.failcauseType1Count / statistic.failCount * 100}" />
						<a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.domainName}"/>','1','<c:out value="${statistic.failcauseType1Count}"/>')">
							<fmt:formatNumber value="${statistic.failcauseType1Count}" type="number"/>
						</a>
						(<fmt:formatNumber value="${type1ratio}" pattern="0.00"/> %)
					</TD>		
					<TD class="tbl_td">
						<c:set var="type2ratio" value="${statistic.failcauseType2Count / statistic.failCount * 100}" />
						<a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.domainName}"/>','2','<c:out value="${statistic.failcauseType2Count}"/>')">
							<fmt:formatNumber value="${statistic.failcauseType2Count}" type="number"/>
						</a>
						(<fmt:formatNumber value="${type2ratio}" pattern="0.00"/> %)
					</TD>
					<TD class="tbl_td">
						<c:set var="type3ratio" value="${statistic.failcauseType3Count / statistic.failCount * 100}" />
						<a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.domainName}"/>','3','<c:out value="${statistic.failcauseType3Count}"/>')">
							<fmt:formatNumber value="${statistic.failcauseType3Count}" type="number"/>
						</a>
						(<fmt:formatNumber value="${type3ratio}" pattern="0.00"/> %)
					</TD>
					<TD class="tbl_td">
						<c:set var="type4ratio" value="${statistic.failcauseType4Count / statistic.failCount * 100}" />
						<a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.domainName}"/>','4','<c:out value="${statistic.failcauseType4Count}"/>')">
							<fmt:formatNumber value="${statistic.failcauseType4Count}" type="number"/>
						</a>
						(<fmt:formatNumber value="${type4ratio}" pattern="0.00"/> %)
					</TD>
				</TR>
			<c:set var="total" value="${total + statistic.failCount}"/>
			<c:set var="send" value="${send + statistic.sendCount}"/>
			<c:set var="type1" value="${type1 + statistic.failcauseType1Count}"/>
			<c:set var="type2" value="${type2 + statistic.failcauseType2Count}"/>
			<c:set var="type3" value="${type3 + statistic.failcauseType3Count}"/>
			<c:set var="type4" value="${type4 + statistic.failcauseType4Count}"/>
			</c:forEach>
				<TR class="tbl_tr" >
						<TD class="tbl_td"></TD>
						<TD class="tbl_td" >합계</TD>		
						<TD class="tbl_td"><strong><fmt:formatNumber value="${send}" type="number"/></strong></TD>
						<TD class="tbl_td">
							<c:set var="failratio" value="${total / send * 100}" />	
							<c:if test="${send == 0}">
								<c:set var="failratio" value="0" />
							</c:if>
							<strong><fmt:formatNumber value="${total}" type="number"/>(<fmt:formatNumber value="${failratio}" pattern="0.00"/> %)</strong></TD>	
						<TD class="tbl_td">
							<c:set var="type1ratio" value="${type1 / total * 100}" />
							<c:if test="${total == 0}">
								<c:set var="type1ratio" value="0" />
							</c:if>
							<strong><fmt:formatNumber value="${type1}" type="number"/>(<fmt:formatNumber value="${type1ratio}" pattern="0.00"/> %)</strong></TD>
						<TD class="tbl_td">
							<c:set var="type2ratio" value="${type2 / total * 100}" />
							<c:if test="${total == 0}">
								<c:set var="type2ratio" value="0" />
							</c:if>
							<strong><fmt:formatNumber value="${type2}" type="number"/>(<fmt:formatNumber value="${type2ratio}" pattern="0.00"/> %)</strong></TD>
						<TD class="tbl_td">
							<c:set var="type3ratio" value="${type3 / total * 100}" />
							<c:if test="${total == 0}">
								<c:set var="type3ratio" value="0" />
							</c:if>
							<strong><fmt:formatNumber value="${type3}" type="number"/>(<fmt:formatNumber value="${type3ratio}" pattern="0.00"/> %)</strong></TD>
						<TD class="tbl_td">
							<c:set var="type4ratio" value="${type4 / total * 100}" />
							<c:if test="${total == 0}">
								<c:set var="type4ratio" value="0" />
							</c:if>
							<strong><fmt:formatNumber value="${type4}" type="number"/>(<fmt:formatNumber value="${type4ratio}" pattern="0.00"/> %)</strong></TD>
				</TR>
			</tbody>
		</TABLE>
	</div>
	<script type="text/javascript">
		window.addEvent('domready', function(){
			renderTableHeader( "<%=id %>List" );
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'default' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
			});
			$('<%=id%>').grid_content.render();
			if($('<%=id%>_sform').retryFinishYN.value == 'Y' && ($('<%=id%>_sform').backupYearMonth.value == '' || $('<%=id%>_sform').backupYearMonth.value == 'null')){
				$('<%=id%>retryBtn').setStyle('display','block');
			}
		});
	</script>	
<%
}
//****************************************************************************************************/
//통계 리스트 - 실패원인
//****************************************************************************************************/
if(method.equals("statisticfailcauselist")) {
%>
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<div><h2>실패 원인별 통계 리스트</h2></div>
		<div class="btn_b" id="<%=id%>retryBtn" style="display:none"><a style ="float:right;margin:5px;" href="javascript:$('<%=id%>').retryMail()"><span>오류자 재발송</span></a></div>
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
			<thead>
				<tr>
					<th style="height:30px;width:280px">실패구분 (구분코드)</th>
					<th style="height:30px;width:280px" align="left"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder" onclick="selectAll($('<%=id%>_list_form').eFailCauseCode,this.checked)"/> 상세 원인 (원인코드)</th>
					<th style="height:30px;width:280px">실패 통수</th>
				</tr>
			</thead>
			<tbody id="<%=id%>_grid_content">
				<c:set var="count" value="0"/>
				<c:forEach items="${statisticList}" var="statistic">
				<c:set var="count" value="${count + 1}"/>
				<TR class="tbl_tr">
					<c:if test="${statistic.failCauseType == '1' || statistic.failCauseType == '2'}">
						<c:if test="${count == 1 || count == 4}">
							<TD class="tbl_td" rowspan="3"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.failCauseCode}"/>','total','<c:out value="${statistic.failCount}"/>')"><c:out value="${statistic.failCauseTypeDes}"/> (<c:out value="${statistic.failCauseType}"/>)</a></TD>
						</c:if>
						<TD class="tbl_td" align="left">
							<c:if test="${statistic.failCauseType == '1'}">
								<input type="checkbox" class="notBorder" id="eFailCauseCode" name="eFailCauseCode" value="<c:out value="${statistic.failCauseCode}"/>" />
							</c:if>
							<c:if test="${statistic.failCauseType == '2'}">
								<input type="checkbox"  class="notBorder" value="" disabled />
							</c:if>
							<a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.failCauseCode}"/>','total','<c:out value="${statistic.failCount}"/>')"><c:out value="${statistic.failCauseDes}"/> (<c:out value="${statistic.failCauseCode}"/>) </a>
						</TD>		
						<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.failCauseCode}"/>','total','<c:out value="${statistic.failCount}"/>')"><fmt:formatNumber value="${statistic.failCount}" type="number"/></a></TD>
					</c:if>
					<c:if test="${statistic.failCauseType == '3'}">
						<c:if test="${count == 7}">
							<TD rowspan="2" background="red"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.failCauseCode}"/>','total','<c:out value="${statistic.failCount}"/>')"><c:out value="${statistic.failCauseTypeDes}"/> (<c:out value="${statistic.failCauseType}"/>)</a></TD>
						</c:if>
						<TD class="tbl_td" align="left"><input type="checkbox" class="notBorder" id="eFailCauseCode" name="eFailCauseCode" value="<c:out value="${statistic.failCauseCode}"/>" /><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.failCauseCode}"/>','total','<c:out value="${statistic.failCount}"/>')"><c:out value="${statistic.failCauseDes}"/> (<c:out value="${statistic.failCauseCode}"/>) </a></TD>		
						<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.failCauseCode}"/>','total','<c:out value="${statistic.failCount}"/>')"><fmt:formatNumber value="${statistic.failCount}" type="number"/></a></TD>		
					</c:if>
				<c:if test="${statistic.failCauseType == '4'}">
					<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.failCauseCode}"/>','total','<c:out value="${statistic.failCount}"/>')"><c:out value="${statistic.failCauseTypeDes}"/> (<c:out value="${statistic.failCauseType}"/>)</a></TD>
					<TD class="tbl_td" align="left"><input type="checkbox"  class="notBorder" value=""  disabled/><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.failCauseCode}"/>','total','<c:out value="${statistic.failCount}"/>')"><c:out value="${statistic.failCauseDes}"/> (<c:out value="${statistic.failCauseCode}"/>) </a></TD>		
					<TD class="tbl_td"><a href="javascript:$('<%=id%>').personPreview('<c:out value="${statistic.failCauseCode}"/>','total','<c:out value="${statistic.failCount}"/>')"><fmt:formatNumber value="${statistic.failCount}" type="number"/></a></TD>			
				</c:if>
				</TR>
				</c:forEach>
			</tbody>
		</TABLE>
	</div>
	<script type="text/javascript">
		window.addEvent('domready', function(){
			renderTableHeader( "<%=id %>List" );
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
			});
			$('<%=id%>').grid_content.render();
			if($('<%=id%>_sform').retryFinishYN.value == 'Y' && ($('<%=id%>_sform').backupYearMonth.value == '' || $('<%=id%>_sform').backupYearMonth.value == 'null')){
				$('<%=id%>retryBtn').setStyle('display','block');
			}
		});
	</script>	
<%
}

//****************************************************************************************************/
//대량메일 - 설문통계
//****************************************************************************************************/
if(method.equals("statisticPoll")) {
	String pollEndDate = request.getParameter("pollEndDate");
	String pollendType = request.getParameter("pollendType");
	String aimAnswerCnt = request.getParameter("aimAnswerCnt");
%>
<jsp:useBean id="sendTotalCount" class="java.lang.String" scope="request" />
<jsp:useBean id="responseCount" class="java.lang.String" scope="request" />
<jsp:useBean id="notresponseCount" class="java.lang.String" scope="request" />
<%	
	if(pollID.equals("0")){
		
%>


	<div style="padding:8px">
		<img src="images/tag_blue.png" alt="설문 미 사용 안내 "> 설문을 사용하지 않는 메일입니다.<br> 
	</div>	
	<script type="text/javascript">
		$('<%=id%>Content').setStyle('display','none');
	</script>
<%}else{ %>	

	<jsp:useBean id="pollQuestionList" class="java.util.ArrayList" scope="request" />
	<div class="dash_box">
		<div class="dash_box_tabs"></div>		
		<div><h2>설문통계</h2></div>
		<div class="dash_box_content">
		<TABLE border="0" cellspacing="0" cellpadding="0" width="840px" >
		<TR>
		<TD>
    	<div class="btn_b" id="<%=id%>retryBtn" style="display:none"><a style ="float:left;margin-bottop:10px;margin-left:10px;" href="javascript:$('<%=id%>').retryPoll()"><span>미응답자 재발송</span></a></div>
		<div style="float:right;margin-right:5px">		
		<a href="javascript:$('<%=id%>').excelDown(<%=massmailID %>,<%=scheduleID %>,<%=pollID %>)" class="web20button bigblue">Excel</a>
		</div>	
		<div style="float:right;margin-right:5px">  
    	<a href="javascript:$('<%=id%>').individualExcelDown(<%=massmailID %>,<%=scheduleID %>,<%=pollID %>)" class="web20button bigblue">개별응답현황</a>
   		</div>
		</TD>
		</TR>
		</TABLE>
		<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
		<TR style="height:30px;" >					
		<TD class="ctbl ttd1" width="210px">종료기준</TD>		
		<TD class="ctbl td" width="210px" align="center">
			<c:if test="${pollendType=='1' || pollendType=='3'}">
				마감일
			</c:if>
			<c:if test="${pollendType=='3'}">
				<br/>
			</c:if>
			<c:if test="${pollendType=='2' || pollendType=='3'}">
				목표응답수
			</c:if>
		</TD>						
		<TD class="ctbl ttd1" width="210px">목표응답수</TD>
		<TD class="ctbl td" width="210px" align="center">
			<c:if test="${pollendType=='2' || pollendType=='3'}">
				<c:out value="${aimAnswerCnt}"/>건
			</c:if>
			<c:if test="${pollendType=='1'}">-</c:if>
		</TD>
		<TD class="ctbl ttd1" width="210px">설문 마감일</TD>
		<TD class="ctbl td" width="210px" align="center">
			<c:if test="${pollendType=='1' || pollendType=='3'}">
				<c:out value="${pollEndDate}"/>
			</c:if>
			<c:if test="${pollendType=='2'}">-</c:if>
		</TD>
		</TR>
		</TABLE>
		<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
		<TR style="height:30px;" >					
		<TD class="ctbl ttd1" width="210px">전체통계</TD>		
		<TD class="ctbl td" width="210px" align="center"><%=StringUtil.formatPrice(sendTotalCount)%></TD>										
		<TD class="ctbl ttd1" width="210px">응답수</TD>
		<TD class="ctbl td" width="210px" align="center"><a href="javascript:pollPersonAllResponse('<%=pollID %>','<%=massmailID %>','<%=scheduleID %>')"><%=StringUtil.formatPrice(responseCount)%></a></TD>		
		<TD class="ctbl ttd1" width="210px">미응답수</TD>
		<TD class="ctbl td" width="210px" align="center"><a href="javascript:pollPersonAllnotResponse('<%=pollID %>','<%=massmailID %>','<%=scheduleID %>')"><%=StringUtil.formatPrice(notresponseCount)%></a></TD>		
		</TR>
		</TABLE>		
		<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
		<thead>
		<TR style="height:20px;">
			<TH class="ctbl ttd1" width="50px">번호</TD>
			<TH class="ctbl ttd1">질문</TD>
			<TH class="ctbl ttd1" width="300px">유형</TD>
		</TR>
		</thead>
		</TABLE>	
		<!-- 설문 문항 리스트 시작 -->
		<% for(int i=0;i<pollQuestionList.size();i++){ 		
			PollQuestion pollQuestion = (PollQuestion)pollQuestionList.get(i);		
			
			
			String questionTypeDesc = "";
			if(pollQuestion.getQuestionType().equals("1")){
				questionTypeDesc+="일반질문";
			}else{
				questionTypeDesc+="매트릭스";
			}
			if(pollQuestion.getExampleType().equals("1")){
				questionTypeDesc+="/객관식";
			}else if(pollQuestion.getExampleType().equals("2")){
				questionTypeDesc+="/주관식";
			}else if(pollQuestion.getExampleType().equals("3")){
				questionTypeDesc+="/순위선택";
			}else if(pollQuestion.getExampleType().equals("4")){
				questionTypeDesc+="/숫자입력";
			}else if(pollQuestion.getExampleType().equals("5")){
				questionTypeDesc+="/척도형";
			}
			if(pollQuestion.getExampleGubun().equals("1")){
				questionTypeDesc+="/단일응답";
			}else{
				questionTypeDesc+="/복수응답";
			}
			if(pollQuestion.getRequiredYN().equals("Y")){
				questionTypeDesc+="/필수응답";
			}else{
				questionTypeDesc+="/선택응답";
			}
			
		%>
		
		<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
		<TR style="height:20px;">
			<TD class="ctbl ttd1" width="50px" align="center"><b><%=pollQuestion.getQuestionNo()%></b></TD>
			<TD class="ctbl td" align="left"><a href="javascript:chartWindow(<%=massmailID %>,<%=scheduleID %>, <%=pollID %>, <%=pollQuestion.getQuestionID() %>, <%=pollQuestion.getQuestionType() %>)"><%=pollQuestion.getQuestionText() %></a></TD>
			<TD class="ctbl td" width="300px" align="center">(<%=questionTypeDesc %>)</TD>
		</TR>		
		</TABLE>
		
		<!-- 일반질문일 경우 -->		
		<%
		if(pollQuestion.getQuestionType().equals("1") && !pollQuestion.getExampleType().equals("3")){
			List<MassMailStatisticPoll> massMailStatisticPollList = service.selectPollStatisticByQuestionID(Integer.parseInt(massmailID),Integer.parseInt(scheduleID),Integer.parseInt(pollID),pollQuestion.getQuestionID());
		%>
			<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
			<TR>
			<TD class="ctbl ttd1" width="50px"></TD>
			<TD class="ctbl ttd1"></TD>
			<TD class="ctbl ttd1" width="150px">응답수</TD>
			<TD class="ctbl ttd1" width="150px">응답률</TD>
			</TR>
			<!--일반질문 문항에 해당되는 보기리스트 시작-->
			<% 
			String exampleDesc ="";
			for(int j=0;j<massMailStatisticPollList.size();j++){
				MassMailStatisticPoll massMailStatisticPoll= (MassMailStatisticPoll)massMailStatisticPollList.get(j);
				exampleDesc = massMailStatisticPoll.getExampleDesc();
				exampleDesc = exampleDesc.replace("≠"," [ &nbsp; ] ");
				
			%>			
			<TR>
				<TD class="ctbl td" align="center">(<%=massMailStatisticPoll.getExampleID()%>)</TD>
				<TD class="ctbl td" align="left"><%=exampleDesc %></TD>
				<TD class="ctbl td" align="center"><a href="javascript:pollPersonPreview(<%=pollID%>,<%=massmailID%>, <%=scheduleID%>, <%=pollQuestion.getQuestionID()%>, <%=massMailStatisticPoll.getExampleID()%>, '0','0')"><%=StringUtil.formatPrice(massMailStatisticPoll.getResponseCount()) %></a></TD>
				<TD class="ctbl td" align="center"><%=StringUtil.getRatioToString(massMailStatisticPoll.getResponseCount(),Integer.parseInt(responseCount))%>%</TD>		
			</TR>			
			<%}// end for  %>
			<!-- 일반질문 문항에 해당되는 보기리스트 끝 -->	
			</TABLE>
		<%
			}else if(pollQuestion.getExampleType().equals("3")){	
				List<MassMailStatisticPoll> massMailStatisticPollList = service.selectPollStatisticByQuestionID(Integer.parseInt(massmailID),Integer.parseInt(scheduleID),Integer.parseInt(pollID),pollQuestion.getQuestionID());
				List<MassMailStatisticPoll> massMailStatisticPollExampleType3List = service.selectPollStatisticByExampleType3(Integer.parseInt(massmailID),Integer.parseInt(scheduleID),Integer.parseInt(pollID),pollQuestion.getQuestionID(), massMailStatisticPollList.size());
				
		%>
				<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
					<TR>
					<TD class="ctbl ttd1" width="50px"></TD>
					<TD class="ctbl ttd1"></TD>
					<TD class="ctbl ttd1">1순위</TD>
					<TD class="ctbl ttd1">2순위</TD>
		<%		
				for(int j=2;j<massMailStatisticPollList.size();j++){
		%>
					<TD class="ctbl ttd1"><%=j+1 %>순위</TD>
		<% 
				} // end for
		%>
					</TR>
		<%
				for(int j=0;j<massMailStatisticPollList.size();j++){
					MassMailStatisticPoll massMailStatisticPoll= (MassMailStatisticPoll)massMailStatisticPollList.get(j);
					MassMailStatisticPoll massMailStatisticPollExampleType3= (MassMailStatisticPoll)massMailStatisticPollExampleType3List.get(j);
					String[] responseCountArry = massMailStatisticPollExampleType3.getResponseCountArry();
					
		%>
					<TR>
						<TD class="ctbl td" align="center">(<%=massMailStatisticPoll.getExampleID()%>)</TD>
						<TD class="ctbl td" align="left"><%=massMailStatisticPoll.getExampleDesc() %></TD>
						
						<%for(int c=0;c<responseCountArry.length;c++){ %>
							<TD class="ctbl td" align="center"><a href="javascript:pollPersonPreview(<%=pollID%>,<%=massmailID%>, <%=scheduleID%>, <%=pollQuestion.getQuestionID()%>, <%=massMailStatisticPoll.getExampleID()%>, '0','0','<%=c+1%>')"><%= responseCountArry[c]%></a></TD>
						<%}// end for %>
						
					</TR>	
				
		<%	
			}// end for %>
				</TABLE>
		<!-- 매트릭스일 경우 -->
		<%
			}else{				
				
				//세로(Y)축의 데이터를 가져온다. 			
				List<MassMailStatisticPoll> massMailStatisticPollListMatrixY = service.selectPollExampleMatrixY(pollQuestion.getPollID(), pollQuestion.getQuestionID());
				List<MassMailStatisticPoll> massMailStatisticPollAnswerList = service.selectPollAnswerMatrixXY(Integer.parseInt(massmailID),Integer.parseInt(scheduleID), pollQuestion.getPollID(), pollQuestion.getQuestionID());
				
		%>
			<TABLE class="ctbl" border="0" cellspacing="0" cellpadding="0" width="840px" align="center">
			<TR>
				<TD class="ctbl ttd1" width="50px">&nbsp;</TD>
				<TD class="ctbl ttd1">&nbsp;</TD>
				<% 
					for(int j=0;j<massMailStatisticPollListMatrixY.size();j++){
						MassMailStatisticPoll massMailStatisticPoll = (MassMailStatisticPoll)massMailStatisticPollListMatrixY.get(j);				
				%>
					<TD class="ctbl ttd1"><%=massMailStatisticPoll.getExampleDesc() %></TD>	
				<%} %>
			</TR>
			<%
				for(int j=0;j<massMailStatisticPollAnswerList.size();j++){
					MassMailStatisticPoll massMailStatisticPoll = (MassMailStatisticPoll)massMailStatisticPollAnswerList.get(j);
					
					int yCount = massMailStatisticPoll.arrayListValue.size();
				
			%>
			<TR>
				<TD class="ctbl td" align="center">(<%=massMailStatisticPoll.getExampleID() %>)</TD>
				<TD class="ctbl td" align="left"><%=massMailStatisticPoll.getExampleDesc() %></TD>
				<%
					for(int y=0;y<yCount;y++){
				%>
				<!-- 매트릭스 응답한카운트  -->
				<TD class="ctbl td" align="center"><a href="javascript:pollPersonPreview(<%=pollID%>,<%=massmailID%>, <%=scheduleID%>, <%=pollQuestion.getQuestionID()%>, <%=massMailStatisticPoll.getExampleID()%>, <%=j+1 %>, <%=y+1 %>)"><%=massMailStatisticPoll.arrayListValue.get(y) %></a></TD>
				<%} %>
			
			</TR>
			<%	} %>
		
		<%	} %>
	
				
		
			</TABLE>
		
		
	<%}%>
		<!-- 설문 문항 리스트 끝-->			
	</div>
	</div>
	<script type="text/javascript">

		window.addEvent('domready', function(){
			if($('<%=id%>_sform').retryFinishYN.value == 'Y' && ($('<%=id%>_sform').backupYearMonth.value == '' || $('<%=id%>_sform').backupYearMonth.value == 'null')){
					$('<%=id%>retryBtn').setStyle('display','block');
				}
		});

		/************************************************************/
		/*설문모든응답자보기
		/************************************************************/
		 function pollPersonAllResponse(pollID, massmailID, scheduleID){			 
				nemoWindow(
						{
							'id': '<%=id%>_pollAllResponse',
							busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
							width: 575,
							height: $('mainColumn').style.height.toInt(),
							title: '설문전체응답자',
							type: 'modal',
							loadMethod: 'xhr',
							contentURL: 'pages/massmail/statistic/massmail_poll_allresponse.jsp?id=<%=id%>_pollAllResponse&method=pollAllResponse&pollID='+pollID+'&massmailID='+massmailID+'&scheduleID='+scheduleID
						}
					);
		}

		/************************************************************/
		/*설문모든미응답자보기
		/************************************************************/
		 function pollPersonAllnotResponse(pollID, massmailID, scheduleID){			 
				nemoWindow(
						{
							'id': '<%=id%>_pollAllNotResponse',
							busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
							width: 575,
							height: $('mainColumn').style.height.toInt(),
							title: '설문전체미응답자',
							type: 'modal',
							loadMethod: 'xhr',
							contentURL: 'pages/massmail/statistic/massmail_poll_allnotresponse.jsp?id=<%=id%>_pollAllNotResponse&method=pollAllNotResponse&pollID='+pollID+'&massmailID='+massmailID+'&scheduleID='+scheduleID
						}
					);
		}
	

		/************************************************************/
		/*설문응답자 보기
		/************************************************************/
		 function pollPersonPreview(pollID, massmailID, scheduleID, questionID, exampleID, matrixX, matrixY, ranking){
				if(ranking == null){
					ranking = 0;
				}
				nemoWindow(
						{
							'id': '<%=id%>_pollPreview',
							busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
							width: 850,
							height: $('mainColumn').style.height.toInt(),
							title: '설문응답자',
							type: 'modal',
							loadMethod: 'xhr',
							contentURL: 'pages/massmail/statistic/massmail_poll_preview.jsp?id=<%=id%>_pollPreview&method=pollpreview&pollID='+pollID+'&massmailID='+massmailID+'&scheduleID='+scheduleID+'&questionID='+questionID+'&exampleID='+exampleID+'&matrixX='+matrixX+'&matrixY='+matrixY+'&ranking='+ranking
						}
					);
		}
		
		
		/***********************************************/
		/* 문항의 차트창 팝업 
		/***********************************************/
		function chartWindow(massmailID, scheduleID, pollID, questionID, questionType){						
				nemoWindow(
					{
						'id': '<%=id%>_chart_modal',
						busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
						width: 700,
						//height: $('mainColumn').style.height.toInt(),
						height: 500,
						title: '설문 결과 ',
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'pages/massmail/statistic/massmail_poll_chart.jsp?id=<%=id%>_chart_modal&massmailID='+massmailID+'&scheduleID='+scheduleID+'&pollID='+pollID+'&questionID='+questionID+'&questionType='+questionType
					}
				);
				
		}
		
		
		
		/************************************************************/
		/*설문 엑셀 다운 
		/************************************************************/
		$('<%=id%>').excelDown = function(massmailID, scheduleID, pollID){
			var nowTime = <%=DateUtils.getShortTimeString()%>;
			var nowDate = <%=DateUtils.getShortDateString()%>;
			
			location.href = "pages/massmail/statistic/massmail_poll_excel.jsp?massmailID="+massmailID+"&scheduleID="+scheduleID+"&pollID="+pollID+"&nowTime="+nowTime+"&nowDate="+nowDate;
		}	
		/************************************************************/
		/*설문 개인별 답변 현황 엑셀 다운 
		/************************************************************/
		$('<%=id%>').individualExcelDown = function(massmailID, scheduleID, pollID){
			var nowTime = <%=DateUtils.getShortTimeString()%>;
			var nowDate = <%=DateUtils.getShortDateString()%>;
			   
			location.href = "pages/massmail/statistic/massmail_poll_individual_excel.jsp?massmailID="+massmailID+"&scheduleID="+scheduleID+"&pollID="+pollID+"&nowTime="+nowTime+"&nowDate="+nowDate;
		} 
					
	
	</script>	
<%
	}
}


//****************************************************************************************************/
// 대상자 미리보기
//****************************************************************************************************/
if(method.equals("personpreview")) {

	String standard = request.getParameter("standard");
	String type = request.getParameter("type");
	String key= request.getParameter("key");
	String iTotalCnt= request.getParameter("iTotalCnt");
	int pv_width = 400;
	if(standard.equals("failcause") || standard.equals("faildomain")){
		pv_width = 700;
	}
	if(standard.equals("link")){
		pv_width = 500;
	}
	
	String[] targetIDs = service.getTargetIDs(Integer.parseInt(massmailID));
	List<OnetooneTarget> onetooneTargets = service.selectOnetooneByTargetID(targetIDs, Integer.parseInt(massmailID));
	pv_width = pv_width + onetooneTargets.size()*100;
	String tags = "";
%>
	<form id="<%=id%>_sform" name="<%=id%>_sform">
	<input type="hidden" id="massmailID" name="massmailID" value="<%=massmailID %>">		
	<input type="hidden" id="scheduleID" name="scheduleID" value="<%=scheduleID %>">
	<input type="hidden" id="standard" name="standard" value="<%=standard %>">
	<input type="hidden" id="type" name="type" value="<%=type %>">
	<input type="hidden" id="key" name="key" value="<%=key %>">
	
	<div class="search_wrapper" style="width:<%=pv_width %>px">
		<div>
			<ul id="sSearchType"  class="selectBox" >
				<li data="email" >E-Mail</li>
				<%for(OnetooneTarget onetooneTarget : onetooneTargets)
					{	
				%>
				<li data="onetooneInfo" ><%=onetooneTarget.getOnetooneName().replace("[","").replace("]","") %></li>
				<%
					}
					if(standard.equals("failcause") || standard.equals("faildomain")){
				%>
				<li data="smtpCode" >응답코드</li>
				<li data="smtpMsg" >응답로그</li>
				<%} %>
			</ul>
		</div>
		<div>
			<input type="text" id="sSearchText" name="sSearchText" size="15"/>
		</div>
		<div>
			<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
		</div>
		<div class="right">
			<a href="javascript:$('<%=id%>').allList()" class="web20button blue">전체목록</a>
		</div>
		<div class="right">
			<a href="javascript:$('<%=id%>').personPreviewDown()" class="web20button blue">Excel 다운</a>
		</div>
		
		
	</div>
	<div class="search_wrapper" style="clear:both;width:<%=pv_width %>px">
		<div class="right">
			<div id="<%=id%>_total"></div>
		</div>
	</div>
	</form>
	
	<div style="clear:both;width:<%=pv_width %>px">
		<form name="<%=id%>_list_form" id="<%=id%>_list_form">
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="<%=pv_width %>px" >
	<% 
		
		for(OnetooneTarget onetooneTarget : onetooneTargets)
		{
			tags = tags + "<th style='height:30px;width:100px'>"+onetooneTarget.getOnetooneName().replace("[","").replace("]","")+"</th>\n";
		}
		if (standard.equals("failcause") || standard.equals("faildomain")) {
	%>
		<thead>
			<tr>
				<th style="height:30px;width:200px">E-Mail</th>
				<%=tags %>
				<th style="height:30px;width:80px">응답코드</th>
				<th style="height:30px">응답로그</th>
			</tr>
		</thead>
	<%
		} else if (standard.equals("link")) {
	%>
		<thead>
			<tr>
				<th style="height:30px;width:200px">E-Mail</th>
				<%=tags %>
				<th style="height:30px;width:200px">클릭시간</th>
				<th style="height:30px;width:100px">클릭횟수</th>
			</tr>
		</thead>
	<%
		} else {
	%>
		<thead>
			<tr>
				<th style="height:30px;width:200px">E-Mail</th>
				<%=tags %>
		<%if ((standard.equals("basic") || standard.equals("hourly") || standard.equals("daily") || standard.equals("domain")) && type.equals("open")) { %>
				<th style="height:30px;width:200px">오픈시간</th>
		<%} else { %>
				<th style="height:30px;width:200px">발송시간</th>
		<%} %>
			</tr>
		</thead>
	<%} %>
		<tbody id="<%=id%>_grid_content">
		
		</tbody>
		</table>
		</form>
	</div>
	<div id="<%=id%>_Down" style="padding:8px;">
	</div>
	<div id="<%=id%>_paging" class="page_wrapper"></div>
	
	<script type="text/javascript">	
	
	/***********************************************/
	/* 검색 조건 컨트롤 초기화
	/***********************************************/
	
	$('<%=id%>').init = function() {
	
		var frm = $('<%=id%>_sform');
	
		// 셀렉트 박스 렌더링
		makeSelectBox.render( frm );
	
		// 키보드 엔터 검색 만들기
		keyUpEvent( '<%=id%>', frm );
	
	}
	/***********************************************/
	/* 전체목록
	/***********************************************/
	$('<%=id%>').allList = function(){
		
		initFormValue($('<%=id%>_sform'));
		$('<%=id%>').list ();
	}
	//************************************************************
	//대상자 다운
	//************************************************************/
	$('<%=id%>').personPreviewDown = function() {

		location.href = "pages/massmail/statistic/massmail_personpreview_down.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&standard=<%=standard%>&type=<%=type%>&key=<%=key%>&iTotalCnt=<%=iTotalCnt%>";
	}
	
	/***********************************************/
	/* 리스트 
	/***********************************************/
	
	$('<%=id%>').list = function ( forPage ) {
	
		var frm = $('<%=id%>_sform');
	
		//페이징 클릭에서 리스트 표시는 기존 검색을 따른다
		if(!forPage) {
			//검색 조건 체크
			if(!checkFormValue(frm)) {
				return;
			}
			// 검색 값을 새로운 폼값(검색후 픽스될) 에 담는다.
			cloneForm($('<%=id%>_sform'), '<%=id%>_rform', '<%=id%>',   $('<%=id%>_grid_content'));
		}
	
		nemoRequest.init( 
		{
			busyWindowId: '<%=id%>',  // busy 를 표시할 window
			updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window
	
			url: 'massmail/statistic/massmail.do?method=personPreview&id=<%=id%>&iTotalCnt=<%=iTotalCnt%>', 
			update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
			onSuccess: function(html,els,resHTML,scripts) {
				
				//MochaUI.arrangeCascade();
			}
		});
		nemoRequest.post($('<%=id%>_rform'));
	}
	
	
	/* 리스트 표시 */
	window.addEvent("domready",function () {
		$('<%=id%>').init();
		$('<%=id%>').list(); 
	});
	
	</script>
<%
}

if(method.equals("personpreviewlist")) { %>

	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="standard" class="java.lang.String" scope="request" />
	<jsp:useBean id="type" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<c:forEach items="${personPreview}" var="personPreview">
		<TR class="tbl_tr" >
			<TD class="tbl_td"><c:out value="${personPreview.email}"/></TD>
			<c:if test="${personPreview.onetooneInfo!=''}">
				<c:set var="onetoones" value="${fn:split(personPreview.onetooneInfo,',')}"/>
				<c:forEach var="onetoone" items="${onetoones}">
				<TD class="tbl_td"><c:out value="${onetoone}"/></TD>
				</c:forEach>
			</c:if>
			<%if (standard.equals("failcause") || standard.equals("faildomain")) { %>
				<TD class="tbl_td"><c:out value="${personPreview.smtpCode}"/></TD>
				<TD class="tbl_td" align="left"><c:out value="${personPreview.smtpMsg}"/></TD>
			<%} else if (standard.equals("link")) { %>					
				<TD class="tbl_td"><c:out value="${personPreview.registDate}"/></TD>
				<TD class="tbl_td"><c:out value="${personPreview.smtpCode}"/></TD>
			<%}else{ %>	
				<%if ((standard.equals("basic") || standard.equals("hourly") || standard.equals("daily") || standard.equals("domain")) && type.equals("open")) { %>
					<TD class="tbl_td"><c:out value="${personPreview.openDate}"/></TD>
				<%} else { %>
					<TD class="tbl_td"><c:out value="${personPreview.registDate}"/></TD>
				<%} %>
			<%} %>		
		</TR>
	</c:forEach>
	<script type="text/javascript">

		window.addEvent('domready', function(){

			$('<%=id%>_total').innerHTML = '[ 대상인원 : <%=iTotalCnt%> 통 ]';

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
				'iPageCnt': '5' 
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
// Bar 차트 조회
//****************************************************************************************************/
if(method.equals("statisticbar")) {

	String standard = request.getParameter("standard");
	if(standard.equals("failcause")){%>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/failcause_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&packages=barchart" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%}else if(standard.equals("faildomain")){ %>
	<div><img src="images/tag_blue.png" alt="통계 안내 "> 실패 통수가 많은 상위 20개 도메인에 대한  통계를 보여줍니다</div>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/faildomain_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&packages=columnchart" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%}else if(standard.equals("poll")){ %>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/poll_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&pollID=<%=pollID %>&packages=columnchart" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>	
<%}else if(standard.equals("link")){ %>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/link_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&packages=columnchart" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%}else{ %>
	<%if(standard.equals("domain")){ %>
		<img src="images/tag_blue.png" alt="통계 안내 "> 발송 통수가 많은 상위 20개 도메인에 대한  통계를 보여줍니다</div>
	<%} %>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/chart_iframe.jsp?standard=<%=standard%>&massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&packages=columnchart" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%
	}
}
//****************************************************************************************************/
// Pie 차트 조회
//****************************************************************************************************/
if(method.equals("statisticpie")) {
	String standard = request.getParameter("standard");
	String key = request.getParameter("key");
	if(standard.equals("failcause")){
%>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/failcause_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&packages=piechart" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%}else if(standard.equals("faildomain")){ %>
	<div><img src="images/tag_blue.png" alt="통계 안내 "> 실패 통수가 많은 상위 20개 도메인에 대한  통계를 보여줍니다</div>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/faildomain_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&packages=piechart" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%}else if(standard.equals("poll")){ %>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/poll_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&pollID=<%=pollID %>&packages=piechart" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%}else if(standard.equals("link")){ %>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/link_chart_iframe.jsp?massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&packages=piechart" width="840px" height="351px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%}else{ %>
	<%if(standard.equals("domain")){ %>
		<img src="images/tag_blue.png" alt="통계 안내 "> 발송 통수가 많은 상위 20개 도메인에 대한  통계를 보여줍니다</div>
	<%} %>
	<IFRAME id="<%=id%>_BarChart" name="<%=id%>_BarChart" src="iframe/googlechart/chart_iframe.jsp?standard=<%=standard%>&massmailID=<%=massmailID%>&scheduleID=<%=scheduleID%>&packages=linechart" width="840px" height="310px" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
<%	}
}
%>
		