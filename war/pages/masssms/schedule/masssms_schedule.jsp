<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="web.common.util.*" %>    
<%@ page import="java.util.*"%>
<%@page import="web.common.util.LoginInfo"%>
<%@ page import="web.admin.usergroup.model.Group"%>
<%@ page import="web.admin.usergroup.service.UserGroupService"%>
<%@ page import="web.admin.usergroup.control.UserGroupControlHelper"%>
	
<% String id = request.getParameter("id");
	String now_month = "";
	int now_year = DateUtils.getYear();
	String groupID =  LoginInfo.getGroupID(request); 
	
	UserGroupService service = UserGroupControlHelper.getUserService(application);
	List<Group> groupList = service.listGroup();
%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
	<div class="search_wrapper" style="width:97%;">
		<div class="schedule">
			<ul id="sYear"  class="selectBox">
				<% if(DateUtils.getYear()!=2010 && DateUtils.getYear()!=2011 ) {	%>
				<li id="sYear_<%=DateUtils.getYear()-2 %>" data="<%=DateUtils.getYear()-2 %>" <%if(DateUtils.getYear()-2 == now_year ){%> select="yes" <%} %>><%=DateUtils.getYear()-2 %></li>
				<% } 
					if(DateUtils.getYear()!=2010 ) {	%>
				<li id="sYear_<%=DateUtils.getYear()-1 %>" data="<%=DateUtils.getYear()-1 %>" <%if(DateUtils.getYear()-1 == now_year ){%> select="yes" <%} %>><%=DateUtils.getYear()-1 %></li>
				<% } %>
				<li id="sYear_<%=DateUtils.getYear()%>" data="<%=DateUtils.getYear() %>" <%if(DateUtils.getYear() == now_year ){%> select="yes" <%} %>><%=DateUtils.getYear() %></li>			
				<li id="sYear_<%=DateUtils.getYear()+1%>" data="<%=DateUtils.getYear()+1 %>" <%if(DateUtils.getYear()+1 == now_year ){%> select="yes" <%} %>><%=DateUtils.getYear()+1 %></li>
				<li id="sYear_<%=DateUtils.getYear()+2%>" data="<%=DateUtils.getYear()+2 %>" <%if(DateUtils.getYear()+2 == now_year ){%> select="yes" <%} %>><%=DateUtils.getYear()+2 %></li>
			</ul>
		</div>
		<div class="text">년</div>
		<div>
			<ul id="sMonth"  class="selectBox" >
			<% for(int sMonth=1;sMonth<=12;sMonth++){ %>
				<li id="sMonth_<%=sMonth %>" data="<%=sMonth %>" <%if(DateUtils.getMonth()==sMonth ) { %> select="yes" <%now_month = String.valueOf(sMonth); }%>><%=sMonth %></li>
			<%} %>
			</ul>
		</div>
		<div class="text">월</div>
		<div>
			<ul id="sGroupID" class="selectBox">
				<li data="">--소속그룹--</li>
			<% for(Group group: groupList) {%>
				<li data="<%=group.getGroupID()%>"><%=group.getGroupName()%></li>
			<%}%>
			</ul>
		</div>
		<div>
			<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
		</div>
	</div>
	<div style="clear:both;width:97%">
		<div class="text" style="padding-left:8px">
			<img src="images/tag_blue.png"> 날짜를 클릭하면 대량SMS을 작성할 수 있습니다.  
		</div>
	</div>
</form>
<div style="clear:both;width:97%">
	<form name="<%=id%>_info_form" id="<%=id%>_info_form">
	<div id="<%=id%>_grid_content">
	</div>
	</form>
</div>
<script type="text/javascript">



$('<%=id%>').show = function(d){
	var y = '<%=id%>overDiv_'+d;
	if($(y).getStyle('display') == 'none'){
		$(y).setStyle('display','block');
	}else
	{
		$(y).setStyle('display','none');
	}
	
}

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_sform');
	
	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );

	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );
	
} 

/***********************************************/
/* 보기창 열기
/***********************************************/ 
$('<%=id%>').viewWindow = function( seq, sid ) {

	nemoWindow(
		{
			'id': '<%=id%>_modal',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 400,
			//height: $('mainColumn').style.height.toInt(),
			height: 270,
			title: '대량SMS 정보 보기',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'masssms/schedule/schedule.do?id=<%=id%>&method=view&masssmsID='+seq +'&scheduleID='+sid
		}
	);
	
}

/***********************************************/
/* 리스트창 열기
/***********************************************/
$('<%=id%>').viewList = function( send ) {
	
	closeWindow($('<%=id%>_modal'));
	
	nemoWindow(
		{
			'id': 'masssmsList',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 1000,
			height: $('mainColumn').style.height.toInt(),
			//height: 270,
			title: '발송결과리스트',
			//type: 'modal',
			container: 'desktop',
			noOtherClose: true,
			loadMethod: 'xhr',
			contentURL: 'masssms/write/masssms.do?step=list&id=masssmsList&eSendScheduleDateStart='+send+'&eSendScheduleDateEnd='+send
		}
	);
	
}


/***********************************************/
/* 반복메일 리스트창 열기
/***********************************************/
$('<%=id%>').viewListRepeat = function( send ) {
	
	closeWindow($('<%=id%>_modal'));
	nemoWindow(
		{
			'id': 'masssmsRepeat',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 1000,
			height: $('mainColumn').style.height.toInt(),
			//height: 270,
			title: '반복메일관리',
			//type: 'modal',
			container: 'desktop',
			noOtherClose: true,
			loadMethod: 'xhr',
			contentURL: 'masssms/write/masssms.do?step=listRepeat&id=masssmsRepeat&eSendScheduleDateStart='+send+'&eSendScheduleDateEnd='+send
		}
	);
	
}

/***********************************************/
/* 작성창 열기
/***********************************************/
$('<%=id%>').viewWrite = function( send  ) {
	
	
	nemoWindow(
		{
			'id': 'masssmsWrite',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 900,
			height: $('mainColumn').style.height.toInt(),
			//height: 270,
			title: '대량메일작성',
			//type: 'modal',
			container: 'desktop',
			noOtherClose: true,
			loadMethod: 'xhr',
			contentURL: 'masssms/write/masssms.do?step=write&id=masssmsWrite&eSendScheduleDateStart='+send
		}
	);
	
}


$('<%=id%>').goPrev = function(y, m) {
	var frm = $('<%=id%>_sform');
	frm.sYear.value = y;
	frm.sMonth.value = m; 
	if(document.getElementById("sYear_"+y) != null)
	{
		var sy = document.getElementById("sYear_"+y);
		var sm = document.getElementById("sMonth_"+m);
		makeSelectBox.select(sy)
		makeSelectBox.select(sm)
	}
	
	$('<%=id%>').list(); 	
	
}


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
		//busyWindowId: '<%=id%>',  // busy 를 표시할 window
		//updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'masssms/schedule/schedule.do?method=list&id=<%=id%>&groupID='+frm.sGroupID.value, 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		updateEl: $('<%=id%>'),
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
	
}


$('<%=id%>').showState = function() {
	
	$('<%=id%>').list();

	
}


window.addEvent("domready",function () {
	$('<%=id%>').init();
	$('<%=id%>').list();
	<%=id%>ServerTimer = $('<%=id%>').showState.periodical(600000);	
	if(MochaUI.Windows.instances.get('<%=id%>') == null)  
		MochaUI.Panels.instances.get('<%=id%>').addEvent('onClose',function() { $clear(<%=id%>ServerTimer) } );
	else
		MochaUI.Windows.instances.get('<%=id%>').addEvent('onClose',function() { $clear(<%=id%>ServerTimer) } );
	
	
	
});

</script>
