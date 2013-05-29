<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %> 
<%

String id = request.getParameter("id");
String method = request.getParameter("method");
String masssmsID = request.getParameter("masssmsID");
String scheduleID = request.getParameter("scheduleID");



//****************************************************************************************************/
// 대량SMS - 기본정보
//****************************************************************************************************/
if(method.equals("statisticbasic")) {
	String today = DateUtils.getStrByPattern("yyyy-MM-dd HH:mm:ss");
%>
	<jsp:useBean id="targetGroupList" class="java.lang.Object" scope="request" />
	<jsp:useBean id="masssmsSendInfo" class="web.masssms.statistic.model.MassSMSInfo" scope="request" />	
	<jsp:useBean id="masssmsInfo" class="web.masssms.statistic.model.MassSMSInfo" scope="request" />
	
	<div class="dash_box">
		<div class="dash_box_tabs"></div>
		<h2>기본정보</h2>
		<div class="dash_box_content">
			<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<table class="ctbl" width="98%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="120px">대량SMS명 </td>
				<td class="ctbl td" width="130px"><c:out value="${masssmsInfo.masssmsTitle}"/></td>
				<td class="ctbl ttd1" width="120px">작성자</td>
				<td class="ctbl td"><c:out value="${masssmsInfo.userName}"/></td>
				<td class="ctbl ttd1" width="120px">발송타입 </td>
				<td class="ctbl td" width="130px"><c:out value="${masssmsInfo.sendType}"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1">발송 대상자 그룹</td>
				<td class="ctbl td" colspan="3">
					<c:set var="totalsend" value="0"/>
					<c:forEach items="${targetGroupList}" var="targetgroup">
						<c:out value="${targetgroup.targetName}"/> [<c:out value="${targetgroup.targetCount}"/>통] &nbsp;&nbsp;&nbsp;
						<c:set var="totalsend" value="${totalsend + targetgroup.targetCount}"/>
					</c:forEach>
				</td>
				<td class="ctbl ttd1">예상 인원</td>
				<td class="ctbl td"><fmt:formatNumber value="${totalsend}" type="number"/> 통</td>
			</tr>		
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1">작성 일시</td>
				<td class="ctbl td"><c:out value="${masssmsInfo.registDate}"/></td>
				<td class="ctbl ttd1">발송 일시</td>
				<td class="ctbl td">
					<%if(!masssmsInfo.getSendStartTime().equals("-")){ %>	
						<c:out value="${masssmsInfo.sendStartTime}"/>
					<%}else{ %>
						<c:out value="${masssmsInfo.sendScheduleDate}"/>
					<%}%> 
				</td>
				<td class="ctbl ttd1">현재 상태</td>
				<td class="ctbl td"><c:out value="${masssmsInfo.stateDesc}"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1"></td>
				<td class="ctbl td">					
				</td>
				<td class="ctbl ttd1">통계 수집 완료일</td>
				<td class="ctbl td">
				<%if(masssmsInfo.getState().equals("15")){ %>	
					<c:out value="${masssmsInfo.statisticsEndDate}"/>
				<%}else{ %>
					-
				<%}%> 
				</td>
				<td class="ctbl ttd1">통계 수집 상태</td>
				<td class="ctbl td">
				<%if(masssmsInfo.getState().equals("15")){ %>			
					<%if(DateUtils.hoursBetween( today, masssmsInfo.getStatisticsEndDate(),"yyyy-MM-dd HH:mm:ss") > 0){%>
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
			<div style="margin:7px 0;"> <img src="images/tag_blue.png" alt="통계 수집 안내 "> 대량SMS는 발송후 24시간 까지만 통계수집 및 업데이트를 진행합니다.</div>
			<table class="ctbl" width="98%">
			<tbody>
				<tr>
					<td class="ctbl ttd1" width="120px">제외타게팅</td>
					<td class="ctbl td" width="130px" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','0','<c:out value="${masssmsFilter.filterType0}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${masssmsFilter.filterType0}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1" width="120px">폰번호형식오류</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','1','<c:out value="${masssmsFilter.filterType1}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${masssmsFilter.filterType1}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1" width="120px">중복필터</td>
					<td class="ctbl td" width="130px" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','2','<c:out value="${masssmsFilter.filterType2}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${masssmsFilter.filterType2}" type="number"/> 통
						</a> 
					</td>
				</tr>	
				<tr>
					<td class="ctbl ttd1">수신거부필터</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','3','<c:out value="${masssmsFilter.filterType3}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${masssmsFilter.filterType3}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1">발송제한필터</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','4','<c:out value="${masssmsFilter.filterType4}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${masssmsFilter.filterType4}" type="number"/> 통
						</a> 
					</td>
					<td class="ctbl ttd1">실패필터</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','5','<c:out value="${masssmsFilter.filterType5}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${masssmsFilter.filterType5}" type="number"/> 통
						</a> 
					</td>
				</tr>
				<tr>
					<td class="ctbl td"></td>
					<td class="ctbl td"></td>
					<td class="ctbl td"></td>
					<td class="ctbl td"></td>
					<td class="ctbl ttd1">전체합계</td>
					<td class="ctbl td" align="center">
						<a href="javascript:$('<%=id%>').personPreview('filter','filterall','<c:out value="${masssmsFilter.filterTotal}"/>')" style="cursor:pointer">
							<fmt:formatNumber value="${masssmsFilter.filterTotal}" type="number"/> 통
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
						<fmt:formatNumber value="${masssmsFilter.filterTotal + masssmsSendInfo.sendTotal}" type="number"/> 통
					</td>
					<td class="ctbl ttd1" width="120px">제외 대상자 수</td>
					<td class="ctbl td" align="center">
						<fmt:formatNumber value="${masssmsFilter.filterTotal}" type="number"/> 통
					</td>
					<td class="ctbl ttd1" width="120px">실 발송 대상자 수</td>
					<td class="ctbl td" width="130px" align="center">
						<fmt:formatNumber value="${masssmsSendInfo.sendTotal}" type="number"/> 통
					</td>
				</tr>	
			</tbody>
			</table><hr />		
			<table class="ctbl" width="98%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="180px">보내는 폰번호</td>
				<td class="ctbl td" align="center" width="220px"><c:out value="${masssmsInfo.senderPhone}"/></td>
				<td class="ctbl ttd1" width="180px">① 대상인원</td>
				<td class="ctbl td" align="center" align="center"><fmt:formatNumber value="${masssmsSendInfo.sendTotal}" type="number"/> 통</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','total','<c:out value="${masssmsSendInfo.sendTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>

			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1">발송시작시간</td>
				<td class="ctbl td" align="center"><c:out value="${masssmsInfo.sendStartTime}"/></td>
				<td class="ctbl ttd1">② 성공통수 (②/①)</td>
				<td class="ctbl td" align="center">
					<c:set var="successratio" value="0"/>
					<c:if test="${masssmsSendInfo.successTotal > 0}">
						<c:set var="successratio" value="${masssmsSendInfo.successTotal / masssmsSendInfo.sendTotal * 100}" />	
					</c:if>
					<fmt:formatNumber value="${masssmsSendInfo.successTotal}" type="number"/> 통 (<fmt:formatNumber value="${successratio}" pattern="0.00"/> %)
				</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','success','<c:out value="${masssmsSendInfo.successTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>

			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>	
				<td class="ctbl ttd1">발송종료시간</td>
				<td class="ctbl td" align="center"><c:out value="${masssmsInfo.sendEndTime}"/></td>
				<td class="ctbl ttd1">③ 실패통수 (③/①)</td>
				<td class="ctbl td" align="center">
					<c:set var="failratio" value="0"/>
					<c:if test="${masssmsSendInfo.failTotal > 0}">
						<c:set var="failratio" value="${masssmsSendInfo.failTotal / masssmsSendInfo.sendTotal * 100}"/>	
					</c:if>
					<fmt:formatNumber value="${masssmsSendInfo.failTotal}" type="number"/> 통 (<fmt:formatNumber value="${failratio}" pattern="0.00"/> %)
				</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','fail','<c:out value="${masssmsSendInfo.failTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>	
				<td class="ctbl td"></td>
				<td class="ctbl td"></td>
				<td class="ctbl ttd1">④ 대기통수 (④/②)</td>
				<td class="ctbl td" align="center">
					<c:set var="readyratio" value="0"/>
					<c:if test="${masssmsSendInfo.readyTotal > 0}">
						<c:set var="readyratio" value="${masssmsSendInfo.readyTotal / masssmsSendInfo.sendTotal * 100}"/>	
					</c:if>
					<fmt:formatNumber value="${masssmsSendInfo.readyTotal}" type="number"/> 통 (<fmt:formatNumber value="${readyratio}" pattern="0.00"/> %)
				</td>
				<td class="ctbl td" align="left" width="60"><div class="btn_green" style="float:left"><a href="javascript:$('<%=id%>').personPreview('basic','ready','<c:out value="${masssmsSendInfo.readyTotal}"/>')" style="cursor:pointer"><span>자세히</span></a></div></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			</tbody>
			</table>
		</form>
		</div>  	
	</div>
	<script type="text/javascript">
		window.addEvent('domready', function(){
			<%if(masssmsInfo.getState().equals("00") || masssmsInfo.getState().equals("01") || masssmsInfo.getState().equals("11")){%>
				$('<%=id%>_sform').personPreviewYN.value = "N";
			<%}%>
			$('<%=id%>_sform').backupYearMonth.value = "<%=masssmsInfo.getBackupYearMonth()%>";
		});
	</script>
<%
}

//****************************************************************************************************/
// 대상자 미리보기
//****************************************************************************************************/
if(method.equals("personpreview")) {

	String standard = request.getParameter("standard");
	String type = request.getParameter("type");
	
	String iTotalCnt= request.getParameter("iTotalCnt");
	int pv_width = 400;
	
%>
	<form id="<%=id%>_sform" name="<%=id%>_sform">
	<input type="hidden" id="masssmsID" name="masssmsID" value="<%=masssmsID %>">		
	<input type="hidden" id="scheduleID" name="scheduleID" value="<%=scheduleID %>">
	<input type="hidden" id="standard" name="standard" value="<%=standard %>">
	<input type="hidden" id="type" name="type" value="<%=type %>">
	
	
	<div class="search_wrapper" style="width:<%=pv_width %>px">
		<div>
			<ul id="sSearchType"  class="selectBox" >
				<li data="receiverPhone" >폰번호</li>
				<li data="smsCode" >응답코드</li>				
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
		<thead>
			<tr>
				<th style="height:30px;width:200px">폰번호</th>
				<th style="height:30px;width:80px">응답코드</th>
				<th style="height:30px">응답로그</th>
			</tr>
		</thead>
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

		location.href = "pages/masssms/statistic/masssms_personpreview_down.jsp?masssmsID=<%=masssmsID%>&scheduleID=<%=scheduleID%>&standard=<%=standard%>&type=<%=type%>&iTotalCnt=<%=iTotalCnt%>";
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
	
			url: 'masssms/statistic/masssms.do?method=personPreview&id=<%=id%>&iTotalCnt=<%=iTotalCnt%>', 
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
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	<c:forEach items="${personPreview}" var="personPreview">
		<TR class="tbl_tr" >
			<TD class="tbl_td"><c:out value="${personPreview.receiverPhone}"/></TD>
			<TD class="tbl_td"><c:out value="${personPreview.smsCode}"/></TD>
			<TD class="tbl_td" align="left"><c:out value="${personPreview.smsCodeMsg}"/></TD>
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
%>
		