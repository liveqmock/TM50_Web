<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper" %>
<%@ page import="web.admin.systemset.service.SystemSetService" %>
<%@ page import="web.content.poll.model.PollTemplate"%>
<%@ page import="web.content.poll.service.PollService"%>
<%@ page import="web.content.poll.control.PollControlHelper"%>
<%@ page import="java.util.Map"%> 
<%
	String preID = request.getParameter("preID");
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	
	SystemSetService service = SystemSetControllerHelper.getUserService(application);
	
	//현재 서버의 도메인명을 가져온다. (url)
	String domainName = service.getSystemSetInfo(Constant.CONFIG_FLAG_MASSMAIL,"domainName");
	String webPORT = service.getSystemSetInfo(Constant.CONFIG_FLAG_MASSMAIL,"webPORT");
	

//****************************************************************************************************/
//설문리스트 
//****************************************************************************************************/
if(method.equals("poll")) {
%>

<div style="clear:both;width:580px">	
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="580px" >
	<thead>
		<tr>	
		<th style="height:30px;width:50px">설문ID</th>	
		<th style="height:30px">설문명</th>
		<th style="height:30px;width:70px">등록자</th>
		<th style="height:30px;width:120px">통계마감일</th>
		<th style="height:30px;width:50px">적용</th>
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
	</form>
</div>

<script type="text/javascript">

/***********************************************/
/* 설문URL 입력 
/***********************************************/
$('<%=id%>').insertPollURL = function(pollID){

	var pollURL = "http://<%=domainName%>:<%=webPORT%>/pages/content/poll/poll_join.jsp?testPoll=Y&pollID="+pollID;
	
	var pollHTML = "<p align=\"center\">"
					+"<a href=\""+pollURL+"\">설문참여하기</a>"
					+"</p>";
		
	$('<%=preID%>_form').ePollID.value = pollID;
	
	$('<%=preID%>_ifrmFckEditor').contentWindow.insertFCKHtml(pollHTML);
		
	closeWindow($('<%=id%>'));
}

/***********************************************/
/* 템플릿보기
/***********************************************/
$('<%=id%>').viewPollWindow = function(seq) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',
	
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
	
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			//height: 500,
			title: '메일템플릿보기',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'pages/massmail/write/massmail_poll.jsp?id=<%=id%>&method=viewPoll&pollID='+seq
		}
	);
}


/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_list_form');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );

	$('<%=id%>').list();

}


/***********************************************/
/* 설문리스트
/***********************************************/

$('<%=id%>').list = function ( ) {


	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'content/poll/poll.do?method=listPoll&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post();
}

/* 리스트 표시 */
window.addEvent("domready",function () {

	$('<%=id%>').init();
	

});

</script>


<%
}
//****************************************************************************************************/
// 설문리스트 
//****************************************************************************************************/
if(method.equals("listPoll")) {
%>

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
	
	
	<c:forEach items="${pollInfoList}" var="pollInfo">
	
	<TR class="tbl_tr" targetList_id="<c:out value="${pollInfo.pollID}"/>">
		<TD class="tbl_td"><c:out value="${pollInfo.pollID}"/></TD>	
		<TD class="tbl_td" align="left"><a href="javascript:$('<%=id%>').viewPollWindow('<c:out value="${pollInfo.pollID}"/>');"><c:out value="${pollInfo.pollTitle}" escapeXml="true"/></a>
			<input type="hidden" name="pollContent<c:out value="${pollInfo.pollID}"/>" value="<c:out value="${pollInfo.resultPollHTML}"/>"/>
		</TD>
		<TD class="tbl_td"><c:out value="${pollInfo.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${pollInfo.pollEndDate}"/></TD>
		<TD class="tbl_td"><a href="javascript:$('<%=id%>').insertPollURL('<c:out value="${pollInfo.pollID}"/>')">선택</a></TD>	
	</TR>
	</c:forEach>
	
	<script type="text/javascript">


		window.addEvent('domready', function(){
				
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
//  편집 
//****************************************************************************************************/
if(method.equals("viewPoll")) {	
	
	String pollID = request.getParameter("pollID");
	
	
%>	
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<input type="hidden" id="ePollID" name="ePollID" value="<%=pollID%>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>				
				<td class="ctbl ttd1" width="100px">HTML</td>
				<td class="ctbl td">				
				<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_pollhtml_iframe.jsp?pollID=<%=pollID%>" height=600  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
				</td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			</tbody>
			</table>
		</form>
	</div>
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;margin-right:5px;"><a href="javascript:$('<%=id%>').insertPollURL('<%=pollID %>')"  class="web20button bigblue">적 용</a></div>
	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
	</script>

<%
}
//****************************************************************************************************/
//  보기 (발송결과리스트 -> 대량메일명 이미지 클릭)
//****************************************************************************************************/
if(method.equals("viewPoll2")) {	
	
	String pollID = request.getParameter("pollID");
	Map<String,Object> resultMap = null;
	String resultPollHTML = "";
	
%>	
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<input type="hidden" id="ePollID" name="ePollID" value="<%=pollID%>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>				
				<td class="ctbl td">
<%				
					if(!pollID.equals("") && !pollID.equals("0")){
					PollService pollService = PollControlHelper.getUserService(application);
					resultMap = pollService.showResultDefaultPollHTML(Integer.parseInt(pollID));
					resultPollHTML = (String)resultMap.get("resultPollHTML");
					}
%>	
					<%=resultPollHTML%>
				</td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			</tbody>
			</table>
		</form>
	</div>
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>


<%
}
%>