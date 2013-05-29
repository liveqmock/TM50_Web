<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>   
<%@page import="web.common.util.LoginInfo"%> 
<%@ page import="web.admin.systemset.control.SystemSetControllerHelper"%>
<%@ page import="web.admin.systemset.service.SystemSetService"%> 
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	String date = null;
	String dateBef = null;
	int startDays=-7;
	int endDays=0;
	
	SystemSetService systemSetservice = SystemSetControllerHelper.getUserService(application);
	try{
		startDays = new Integer(systemSetservice.getSystemSetInfo("1","startDays"))*-1;
		endDays = new Integer(systemSetservice.getSystemSetInfo("1","endDays"));
	}catch(Exception e){}
	if(request.getParameter("eSendScheduleDateStart")== null)
	{
		dateBef=DateUtils.getNowAddShortDate(startDays);
		date=DateUtils.getNowAddShortDate(endDays);
	}else
	{
		dateBef=request.getParameter("eSendScheduleDateStart");
		date=request.getParameter("eSendScheduleDateEnd");
		
	}
	String isAdmin = LoginInfo.getIsAdmin(request);
	String userAuth = LoginInfo.getUserAuth(request);
	String userID =LoginInfo.getUserID(request);
	String groupID = LoginInfo.getGroupID(request);
	
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:1000px">
	
	<div>						
		<input type="text" id="eSendScheduleDateStart" name="eSendScheduleDateStart" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this,$('<%=id%>_sform').eSendScheduleDateEnd,'start')" value="<%=dateBef%>"/>
		<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eSendScheduleDateStart,$('<%=id%>_sform').eSendScheduleDateEnd,'start')" align="absmiddle" />
	</div>
	<div>~</div>	
	<div>						
		<input type="text" id="eSendScheduleDateEnd" name="eSendScheduleDateEnd" size="10" style="cursor:pointer" readonly onclick="Calendar(event,this,$('<%=id%>_sform').eSendScheduleDateStart,'end')" value="<%=date%>"/>
		<img src="images/calendar.gif" style="cursor:pointer" onclick="Calendar(event,$('<%=id%>_sform').eSendScheduleDateEnd,$('<%=id%>_sform').eSendScheduleDateStart,'end')" align="absmiddle" />
	</div>
	<div>		
		<ul id="eSendType"  class="selectBox">
			<li data="">--발송타입--</li>			
			<li data="1">즉시발송</li>
			<li data="2">예약발송</li>
			<li data="3">반복발송</li>
		</ul>
	</div>
	<div>
		<c:import url="../../../include_inc/search_massmailgroup_inc.jsp">
		</c:import>
	</div>
	<div>
		<c:import url="../../../include_inc/search_usergroup_inc.jsp">
		</c:import>
	</div>
	<div>
		<ul id="sSearchType"  class="selectBox">
			<li data="m.massmailTitle" >대량메일명</li>			
			<li data="u.userName">작성자명</li>			
		</ul>
	</div>
	<div>
		<input type="text" id="sSearchText" name="sSearchText" size="33"/>
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<%if(isAdmin.equals("Y")){ %>
	<div>
		<a href="javascript:$('<%=id%>').openPriority()" class="web20button bigblue" >발송순서변경</a>
	</div>
	<%} %>
	<div style="clear:both;width:965px">
			<div class="explain">
				<img src="images/massmail/save.gif" alt="임시저장이미지"/> 임시저장
			</div>
			<div class="explain">
				<img src="images/massmail/approve.gif" alt="승인이미지"/> 승인대기중
			</div>
			<div class="explain">
				<img src="images/massmail/prepareing.gif" alt="발송준비이미지"/> 발송준비중
			</div>
			<div class="explain">
				<img src="images/massmail/ready.gif" alt="발송대기이미지"/> 발송대기중
			</div>
			<div class="explain">
				<img src="images/massmail/sending.gif" alt="발송중이미지"/> 발송 중
			</div>
			<div class="explain">
				<img src="images/massmail/pause.gif" alt="일시정지이미지"/> 일시 정지
			</div>
			<div class="explain">
				<img src="images/massmail/stop.gif" alt="발송정지이미지"/> 발송 정지
			</div>
			<div class="explain">
				<img src="images/massmail/finish.gif" alt="발송완료이미지"/> 발송 완료
			</div>
			<div class="explain">
				<img src="images/massmail/error.gif" alt="에러이미지"/> 에러
			</div>
			<div class="right">
				<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>
			</div>
			<div class="right">
				<a href="javascript:$('<%=id%>').writeWindow()" class="web20button bigblue" >대량메일작성</a>
			</div>	
			<div class="right">
				<a href="javascript:$('<%=id%>').deleteSelectedData()" class="web20button bigpink" title="삭제는 예약발송이면서  발송 준비 대기중, 임시 저장, 발송 완료, 발송 완전 중지, 발송준비중 정지, 발송 대기 중 중지, 발송 준비중 오류, 발송중 오류인 경우에만 가능합니다">선택삭제</a>
			</div>
		</div>
</div> 
</form>
<div style="clear:both;width:970px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
	<thead>
		<tr>
		<th style="height:30px;width:30px"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder" onclick="selectAll($('<%=id%>_list_form').eMassMailID,this.checked)"/></th>
		<th style="height:30px;width:50px">ID</th>
		<th style="height:30px;">대량메일명</th>
		<th style="height:30px;width:100px">작성자</th>
		<th style="height:30px;width:80px">발송타입</th>
		<th style="height:30px;width:130px">발송스케줄</th>
		<th style="height:30px;width:100px">총발송통수</th>
		<th style="height:30px;width:70px">성공률</th>
		<th style="height:30px;width:40px">상태</th>
		<th style="height:30px;width:40px">통계</th>		
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
	</form>
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
/* 팝업메뉴 create
/***********************************************/
$('<%=id%>').createPopup = function() {

	$('<%=id%>').popup = new PopupMenu('<%=id%>');

	$('<%=id%>').popup.add('수정', 
		function(target,e) {
			$('<%=id%>').editWindow( $('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_massmailID"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_scheduleID"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_state") );
		}
	);
	$('<%=id%>').popup.addSeparator();
	$('<%=id%>').popup.add('삭제', 
		function(target,e) { 
			$('<%=id%>').deleteData($('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_massmailID"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_scheduleID"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_sendType"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_userID"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_groupID"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_state")); 
		}
	);
	$('<%=id%>').popup.addSeparator();
	$('<%=id%>').popup.add('복사', 
		function(target,e) { 
			$('<%=id%>').copyMassMail($('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_massmailID"),$('<%=id%>').grid_content.getSelectedRow().getAttribute("tr_scheduleID")); 
		}
	);
	
	$('<%=id%>').popup.setSize(150, 0);

}

/***********************************************/
/* 설문보기
/***********************************************/
$('<%=id%>').pollWindow = function(seq) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',
	
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
	
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			//height: 500,
			title: '설문보기',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'pages/massmail/write/massmail_poll.jsp?id=<%=id%>&method=viewPoll2&pollID='+seq
		}
	);
}
/***********************************************/
/* 대량메일 수정창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq, seq2, seq3) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			//height: 600,
			title: '대량메일수정',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'massmail/write/massmail.do?id=<%=id%>_modal&method=edit&massmailID='+seq+'&scheduleID='+seq2+'&state='+seq3
		}
	);
	
}
/***********************************************/
/* 대량메일 작성창 열기
/***********************************************/
$('<%=id%>').writeWindow = function( send  ) {
	
	
	nemoWindow(
		{
			'id': 'massmailWrite',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 900,
			height: $('mainColumn').style.height.toInt(),
			title: '대량메일작성',
			container: 'desktop',
			noOtherClose: true,
			loadMethod: 'xhr',
			contentURL: 'massmail/write/massmail.do?step=write&id=massmailWrite'
		}
	);
	
}
/***********************************************/
/* 대량메일 복사 하기
/***********************************************/
$('<%=id%>').copyMassMail = function( seq, seq2) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
			width: 900,
			height: $('mainColumn').style.height.toInt(),
			title: '대량메일작성',
			type: 'modal',
			loadMethod: 'xhr',
			
			contentURL: 'massmail/write/massmail.do?id=<%=id%>_modal&method=copyMassMail&massmailID='+seq+'&scheduleID='+seq2
		}
	);
	
}
/***********************************************/
/* 대량메일 발송 스케줄 관리창 열기
/***********************************************/
$('<%=id%>').openPriority = function() {

	nemoWindow(
			{
				'id': '<%=id%>_modal',
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
				width: 800,
				height: $('mainColumn').style.height.toInt()-100,
				title: '발송 순서 변경',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/massmail/write/massmail_priority.jsp?method=priority&id=<%=id%>_modal'
			}
		);


}
/***********************************************/
/* 삭제
 * seq1 : massMailID, seq2 : scheduleID, seq3 : sendType, seq4 : userID, seq5 : groupID, seq6 : state
 */
/***********************************************/
$('<%=id%>').deleteData = function( seq1 ,seq2, seq3, seq4, seq5, seq6 ) {

	if(!((seq3=='2' && seq6 == '11') || seq6 == '00' || seq6 == '15' || seq6 == '44' || seq6 == '22' || seq6 == '24' || seq6 == '32' || seq6 == '33')){
		alert("해당 메일은 삭제 할 수 없는 상태입니다. \r\n상단의 안내를 확인하세요"); 
		return;
	}
	if(!('<%=isAdmin%>' == 'Y' || seq4 == '<%=userID%>' || ('<%=userAuth%>' == '2' && seq5 == '<%=groupID%>'))){
		alert("삭제 권한이 없습니다."); 
		return;
	}
	
	if(!confirm("정말로 삭제 하시겠습니까?  \r\n삭제하시면 관련된 모든 데이타는 영구삭제가 됩니다.")) return;
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'massmail/write/massmail.do?method=deleteMassMailAll&id=<%=id%>&eMassmailID='+seq1+'&eScheduleID='+seq2+'&eSendType='+seq3
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {			
			$('<%=id%>').list();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));

}
/***********************************************/
/* 선택삭제
/***********************************************/
$('<%=id%>').deleteSelectedData = function() {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eMassMailID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}
	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	copyForm( $('<%=id%>_rform'), frm );
	
	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

				, url: 'massmail/write/massmail.do?method=deleteSelectMassMailAll&id=<%=id%>'
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {	
					$('<%=id%>_list_form').sCheckAll.checked = false;		
					$('<%=id%>').list();
				}
			});
	nemoRequest.post(frm);
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

		url: 'massmail/write/massmail.do?method=list&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}


/***********************************************/
/* 전체목록
/***********************************************/
$('<%=id%>').allList = function(){
	
	initFormValue($('<%=id%>_sform'));
	$('<%=id%>_sform').eSendScheduleDateEnd.value ='<%=date%>';
	$('<%=id%>_sform').eSendScheduleDateStart.value='<%=dateBef%>';
	$('<%=id%>').list ();
}

/***********************************************/
/* 통계 버튼 클릭
/***********************************************/

$('<%=id%>').showStatistic = function( massmailID, scheduleID, pollID ) {

	nemoWindow(
			{
				'id': '<%=id%>Statistic',
				busyEl: '<%=id%>Statistic', // 창을 열기까지 busy 가 표시될 element
				width: 900,
				height: $('mainColumn').style.height.toInt(),
				title: '대량메일 통계 조회',
				type: 'modal',
				loadMethod: 'xhr',
				padding: { top: 0, right: 0, bottom: 0, left: 0 },
				contentURL: 'pages/massmail/statistic/massmail_statistic.jsp?id=<%=id%>Statistic&massmailID='+massmailID+'&scheduleID='+scheduleID+'&pollID='+pollID
			}
	);
	
}


/***************************************************/
/* 리스트 상태 값 갱신 1단계 (현재 리스트 값을 가져와서 서버 페이지 호출)
/***************************************************/

var els = null;
var httpRequest = null;
$('<%=id%>').showState = function() {
	
	var temp = document.getElementById("<%=id%>_grid_content");
	var list = temp.childNodes;
	var json = [{massmail_id:[], schedule_id:[]}];
	for(var i=0;i<list.length;i++)
	{
		els = list.item(i);
		if(els.nodeName=='TR')
		{
			if( els.getAttribute("tr_state")=='00' || els.getAttribute("tr_state")=='11' || els.getAttribute("tr_state")=='12' || els.getAttribute("tr_state")=='13'|| els.getAttribute("tr_state")=='14' || els.getAttribute("tr_state")=='15'|| els.getAttribute("tr_state")=='16')
			{
				json[0].massmail_id.push(els.getAttribute("tr_massmailID"));
				json[0].schedule_id.push(els.getAttribute("tr_scheduleID"));
			}
		}	
	}
	$('<%=id%>').sendRequest("returnMassmailState.jsp", "massmail_id="+JSON.encode( json ), $('<%=id%>').callbackFunction, "GET");
	
}
/***************************************************/
/* 리스트 상태 값 갱신 4단계 (결과를 리스트에 반영, 리스트 태그 변경)
/***************************************************/
$('<%=id%>').callbackFunction = function() {

	var isAdmin = '<%=isAdmin%>';
	var userAuth = '<%=userAuth%>';
	var userID = '<%=userID%>';
	var groupID = '<%=groupID%>';
	
	if(httpRequest.readyState==4)
	{
		if(httpRequest.status==200)
		{			
			var st = eval("("+httpRequest.responseText+")");
			if(st[0].massmail_id!=null)
			{
			for(var i=0;i<st[0].massmail_id.length;i++)
			{
				var tagDiv = document.getElementById("massmailState"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);
				var tagTr = document.getElementById("eMassmail"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);

				if(st[0].state[i] != tagTr.getAttribute("tr_state") || tagTr.getAttribute("tr_state") == '15')
				{
					var t = tagDiv.childNodes;
					for(var y =0; y < t.length ;y++)
					{	var stemp = t.item(y);
						if(stemp.nodeName=='IMG')
						{
							var tagImg = stemp;
							var tagDiv_input = document.getElementById("input"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);								
							var input_sendtype = tagDiv_input.getAttribute("input_sendtype");
							var input_userID = tagDiv_input.getAttribute("input_userID");
							var input_groupID = tagDiv_input.getAttribute("input_groupID");

							var tagInp = document.getElementById("<%=id %>state"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);
							if(st[0].state[i]=='00')
							{
								
								tagImg.setAttribute("src","images/massmail/save.gif");					
								tagImg.setAttribute("title","임시저장중");	
								
								if(isAdmin == 'Y' || input_userID == userID || (userAuth == '2' && input_groupID == groupID))
								{
									tagDiv_input.innerHTML = "<input type='checkbox' class='notBorder' id='eMassMailID' name='eMassMailID' value='"+st[0].massmail_id[i]+"-"+st[0].schedule_id[i]+"-"+input_sendtype+"' />";
								}
								else
								{
									tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
								}		
								
							
							}
							else if(st[0].state[i]=='10')
							{
							
								tagImg.setAttribute("src","images/massmail/approve.gif");					
								tagImg.setAttribute("title","승인대기중");

								var tagDiv_input = document.getElementById("input"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);							
								tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";		
							
							}
							else if(st[0].state[i]=='11')
							{
							
								tagImg.setAttribute("src","images/massmail/ready.gif");					
								tagImg.setAttribute("title","발송준비대기중");

							
								if(input_sendtype=='2' && (isAdmin == 'Y' || input_userID == userID || (userAuth == '2' && input_groupID == groupID)))
								{
									tagDiv_input.innerHTML = "<input type='checkbox' class='notBorder' id='eMassMailID' name='eMassMailID' value='"+st[0].massmail_id[i]+"-"+st[0].schedule_id[i]+"-"+input_sendtype+"' />";
								}
								else
								{
									tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
								}				
							
							}
							else if(st[0].state[i]=='12')
							{
								
								tagImg.setAttribute("src","images/massmail/prepareing.gif");					
								tagImg.setAttribute("title","발송준비중");

								var tagDiv_input = document.getElementById("input"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);							
								tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";		
							
							}
							else if(st[0].state[i]=='13')
							{
							
								tagImg.setAttribute("src","images/massmail/ready.gif");					
								tagImg.setAttribute("title","발송준비완료");

								var tagDiv_input = document.getElementById("input"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);							
								tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";		
							
							}
							else if(st[0].state[i]=='14')
							{
							
								tagImg.setAttribute("src","images/massmail/sending.gif");					
								tagImg.setAttribute("title","발송중");	

							
								var tagDiv_input = document.getElementById("input"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);							
								tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
							
							}
							else if(st[0].state[i]=='15')
							{
							
								tagImg.setAttribute("src","images/massmail/finish.gif");					
								tagImg.setAttribute("title","발송완료");	

							
								if(isAdmin == 'Y' || input_userID == userID || (userAuth == '2' && input_groupID == groupID))
								{
									tagDiv_input.innerHTML = "<input type='checkbox' class='notBorder' id='eMassMailID' name='eMassMailID' value='"+st[0].massmail_id[i]+"-"+st[0].schedule_id[i]+"-"+input_sendtype+"' />";
								}
								else
								{
									tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
								}	
							}
							else if(st[0].state[i]=='16')
							{
							
								tagImg.setAttribute("src","images/massmail/sending.gif");					
								tagImg.setAttribute("title","오류자재발송중");

							
								var tagDiv_input = document.getElementById("input"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);							
								tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";	
							
							}
							else if(st[0].state[i]=='22')
							{
							
								tagImg.setAttribute("src","images/massmail/error.gif");					
								tagImg.setAttribute("title","발송준비중에러");

							
								if(isAdmin == 'Y' || input_userID == userID || (userAuth == '2' && input_groupID == groupID))
								{
									tagDiv_input.innerHTML = "<input type='checkbox' class='notBorder' id='eMassMailID' name='eMassMailID' value='"+st[0].massmail_id[i]+"-"+st[0].schedule_id[i]+"-"+input_sendtype+"' />";
								}
								else
								{
									tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
								}			
							
							}
							else if(st[0].state[i]=='24')
							{
							
								tagImg.setAttribute("src","images/massmail/error.gif");					
								tagImg.setAttribute("title","발송중에러");	

							
								if(isAdmin == 'Y' || input_userID == userID || (userAuth == '2' && input_groupID == groupID))
								{
									tagDiv_input.innerHTML = "<input type='checkbox' class='notBorder' id='eMassMailID' name='eMassMailID' value='"+st[0].massmail_id[i]+"-"+st[0].schedule_id[i]+"-"+input_sendtype+"' />";
								}
								else
								{
									tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
								}	
							}
							else if(st[0].state[i]=='26')
							{
								
								tagImg.setAttribute("src","images/massmail/error.gif");					
								tagImg.setAttribute("title","오류자재발송중에러");
	
							
								var tagDiv_input = document.getElementById("input"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);							
								tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";		
							
							}
							else if(st[0].state[i]=='32')
							{
							
								tagImg.setAttribute("src","images/massmail/stop.gif");					
								tagImg.setAttribute("title","발송준비중정지");	

							
								var tagDiv_input = document.getElementById("input"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);							
								tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
							}
							else if(st[0].state[i]=='34')
							{
							
								tagImg.setAttribute("src","images/massmail/pause.gif");					
								tagImg.setAttribute("title","발송일시정지");		

							
								var tagDiv_input = document.getElementById("input"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);							
								tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
							
							}
							else if(st[0].state[i]=='44')
							{
							
								tagImg.setAttribute("src","images/massmail/stop.gif");					
								tagImg.setAttribute("title","발송정지");

							
								if(isAdmin == 'Y' || input_userID == userID || (userAuth == '2' && input_groupID == groupID))
								{
									tagDiv_input.innerHTML = "<input type='checkbox' class='notBorder' id='eMassMailID' name='eMassMailID' value='"+st[0].massmail_id[i]+"-"+st[0].schedule_id[i]+"-"+input_sendtype+"' />";
								}
								else
								{
									tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
								}			
							
							}
							else if(st[0].state[i]=='45')
							{
							
								tagImg.setAttribute("src","images/massmail/stopready.gif");					
								tagImg.setAttribute("title","발송중대기");

								tagDiv_input.innerHTML = "<input type='checkbox'  class='notBorder' value=''  disabled/>";
										
							
							}
							
							
							
							var tagDiv_count = document.getElementById("count"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);							
							tagDiv_count.innerHTML=st[0].count[i]; 		

							var sc= st[0].successCount[i].replace(/,/gi,"");
							var c = st[0].count[i].replace(/,/gi,"");
							if(sc >= 0)
							{
								var avg = sc/c * 100;
								var ratio = avg.toFixed(2)+'%'; 
								var tagTitleSpan = "<span title='발송 성공 통수 : "+st[0].successCount[i]+"통 '>"+ratio+"</span>";
								var tagDiv_sucess_count = document.getElementById("sucess_count"+st[0].massmail_id[i]+"_"+st[0].schedule_id[i]);
								
								tagDiv_sucess_count.innerHTML=tagTitleSpan;
							} 
							toolTip.init(tagDiv);	
							toolTip.init(tagDiv_sucess_count);	
							tagTr.setAttribute("tr_state",st[0].state[i]); 
							tagInp.setAttribute("value",st[0].state[i]); 			
						}
					}
				}
			}
			}
		}		
		
	}	
}

/***************************************************/
/* 리스트 상태 값 갱신 3단계(결과 리턴)
/***************************************************/
$('<%=id%>').getXMLHttpRequest= function(){
	if (window.ActiveXObject) {
		try {
			return new ActiveXObject("Msxml2.XMLHTTP");
		} catch(e) {
			try {
				return new ActiveXObject("Microsoft.XMLHTTP");
			} catch(e1) { return null; }
		}
	} else if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else {
		return null;
	}
}

/***************************************************/
/* 리스트 상태 값 갱신 2단계(서버 페이지 호출하는 함수)
/***************************************************/
$('<%=id%>').sendRequest = function(url, params, callback, method) {
	
	httpRequest = $('<%=id%>').getXMLHttpRequest();
	
	var httpMethod = method ? method : 'GET';
	if (httpMethod != 'GET' && httpMethod != 'POST') {
		httpMethod = 'GET';
	}
	var httpParams = (params == null || params == '') ? null : params;
	var httpUrl = url;
	if (httpMethod == 'GET' && httpParams != null) {
		httpUrl = httpUrl + "?" + httpParams;
	}
	
	httpRequest.open(httpMethod, httpUrl, true);
	httpRequest.setRequestHeader(
		'Content-Type', 'application/x-www-form-urlencoded');
	httpRequest.onreadystatechange = callback;

	httpRequest.send(httpMethod == 'POST' ? httpParams : null);

}


var oldRemoteControlID ="";
/***********************************************/
/*상태 변경 리모콘 호출 
/***********************************************/
$('<%=id%>').remoteControl = function(id) {
	if(oldRemoteControlID != ''){
		$('<%=id %>remoteControl'+oldRemoteControlID).setStyle('display','none');
	}
	oldRemoteControlID = id;
	$('<%=id %>remoteControl'+id).setStyle('display','block');
	if($('<%=id %>state'+id).value == '12'){
		$('<%=id %>32'+id).setStyle('display','block');
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}

	if($('<%=id %>state'+id).value == '14'){
		$('<%=id %>34'+id).setStyle('display','block');
		$('<%=id %>44'+id).setStyle('display','block');
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}

	if( $('<%=id %>state'+id).value == '16'){
		$('<%=id %>36'+id).setStyle('display','block');
		$('<%=id %>44'+id).setStyle('display','block');
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}
	
	if($('<%=id %>state'+id).value == '13'){
		$('<%=id %>33'+id).setStyle('display','block');
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}
	if($('<%=id %>state'+id).value == '22' || $('<%=id %>state'+id).value == '32'){
		$('<%=id %>11'+id).setStyle('display','block');
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}
	
	if($('<%=id %>state'+id).value == '24' ||$('<%=id %>state'+id).value == '34'){
		$('<%=id %>14'+id).setStyle('display','block');
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}

	if($('<%=id %>state'+id).value == '26' || $('<%=id %>state'+id).value == '36'){
		$('<%=id %>16'+id).setStyle('display','block');
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}

	if($('<%=id %>state'+id).value == '33'){
		$('<%=id %>13'+id).setStyle('display','block');
		$('<%=id %>not'+id).setStyle('display','none');
		return;
	}	

	if($('<%=id %>state'+id).value == '10' && $('<%=id %>approveUserID'+id).value =='<%=userID%>'){
		$('<%=id %>appY'+id).setStyle('display','block');
		$('<%=id %>appN'+id).setStyle('display','block');
		$('<%=id %>not'+id).setStyle('display','none');
	}
}

/***********************************************/
/*상태 변경 리모콘 닫기 
/***********************************************/
$('<%=id%>').remoteControlClose = function(id) {
	$('<%=id %>remoteControl'+id).setStyle('display','none');
}

/***********************************************/
/* 발송 상태 변경
/***********************************************/

$('<%=id%>').changeSendState = function(state,massmailID,scheduleID){
	var message ="";
	if(state == '32'){
		message = "발송 준비를 정지 하시겠습니까? \r\n준비중 정지한 메일은 처음부터 다시 준비를 시작 하실 수 있습니다.";
	}
	if(state == '33'){
		message = "발송을 보류 하시겠습니까? \r\n보류한 메일은 재시작 하실 수 있습니다.";
	}
	if(state == '34' || state == '36'){
		message = "일시정지 하시겠습니까? \r\n일시정지한 메일은 재시작 하실 수 있습니다.";
	}
	if(state == '44'){
		message = "발송정지 하시겠습니까? \r\n발송 정지한 메일은 재시작 하실 수 없습니다.";
	}
	if(state == '14' || state == '13' || state == '16'){
		message = "재시작 하시겠습니까? ";
	}
	if(state == '11'){
		message = "시작 하시겠습니까? ";
	}

	if(!confirm(message)) return;
	
	nemoRequest.init( 
	{
				
		updateWindowId: '<%=id%>',  
		url: 'massmail/write/massmail.do?method=changeSendState&state='+state+'&massmailID='+massmailID+'&scheduleID='+scheduleID, 
		update: $('<%=id%>Content'), 
		onSuccess: function(html,els,resHTML,scripts) {
			$('<%=id%>').list();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
	
	
}	

/***********************************************/
/* 승인 처리
/***********************************************/
$('<%=id%>').approveYN= function(state,massmailID,scheduleID){
	var message ="";
	if(state == 'Y'){
		message = "승인 하시겠습니까? ";
	}
	if(state == 'N'){
		message = "승인 요청을 반려 하시겠습니까? ";
	}
	if(!confirm(message)) return;
	
	nemoRequest.init( 
	{
				
		updateWindowId: '<%=id%>',  
		url: 'massmail/write/massmail.do?method=changeSendState&state='+state+'&massmailID='+massmailID+'&scheduleID='+scheduleID, 
		update: $('<%=id%>Content'), 
		onSuccess: function(html,els,resHTML,scripts) {
			$('<%=id%>').list();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
	
	
}	
/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
	/* 60(초)마다 리스트 상태값 갱신됨 */ 
	<%=id%>ServerTimer = $('<%=id%>').showState.periodical(60000); 
	MochaUI.Windows.instances.get('<%=id%>').addEvent('onClose',function() { $clear(<%=id%>ServerTimer) } );
});

</script>


