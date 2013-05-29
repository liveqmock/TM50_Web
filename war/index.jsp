<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@page import="web.admin.menu.model.MenuMain"%>
<%@page import="web.admin.menu.model.MenuSub"%>
<%@page import="web.admin.menu.service.MenuService"%>
<%@page import="web.admin.menu.control.MenuControlHelper"%>
<%@ include file="./include_inc/ipcheck_inc.jsp" %>
<%

	//모델을 이용하여  메인메뉴 리스트를 가져온다.
	MenuService service = MenuControlHelper.getUserService(application);
	List<MenuMain> menuMainList = service.listMenuMain();


%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ThunderMail 5.0</title>
<link rel="shortcut icon" href="images/tmfavicon.ico" />
<link rel="stylesheet" href="css/ezmail.css" type="text/css" />
<link rel="stylesheet" href="css/content.css" type="text/css" />
<link rel="stylesheet" href="css/ui.css" type="text/css" />
<link rel="stylesheet" href="css/Tips.css" type="text/css" />
<link rel="stylesheet" href="css/tree.css" type="text/css" />
<link rel="stylesheet" href="css/Web20Button.css" type="text/css" />
<link rel="stylesheet" href="css/autosuggest_inquisitor.css" type="text/css" />

<script type="text/javascript" src="scripts/mootools-1.3.2.js"></script>
<%if(accessIP){ %>
<!--[if IE]>
	<script type="text/javascript" src="scripts/excanvas_r43.js"></script>		
<![endif]-->
	
	

<script type="text/javascript" src="scripts/mocha.ie8.js"></script>
<script type="text/javascript" src="scripts/btn.js"></script>
<script language="javascript" type="text/javascript" src="scripts/showHideBusy.js"></script>
<script language="javascript" type="text/javascript" src="scripts/Tips.js"></script>
<script language="javascript" type="text/javascript" src="scripts/popupmenu.js"></script>
<script language="javascript" type="text/javascript" src="scripts/tools.js"></script>
<script language="javascript" type="text/javascript" src="scripts/selectBox.js"></script>
<script language="javascript" type="text/javascript" src="scripts/mif.tree-v1.1.js"></script>
<script language="javascript" type="text/javascript" src="scripts/Calendar.js"></script>
<script language="javascript" type="text/javascript" src="scripts/roundTable.js"></script>
<script language="javascript" type="text/javascript" src="scripts/swf_upload.js"></script>
<script language="javascript" type="text/javascript" src="scripts/bsn.AutoSuggest_c_2.0.js"></script>

<script language="javascript" type="text/javascript" src="scripts/swfobject.js"></script>
<script language="javascript" type="text/javascript" src="scripts/mocha-init.jsp"></script>
	
<%} %>
	
</head>
<body>
<%if(!accessIP){ %>
	<div style="width:100%;text-align:center;margin-top:80px;font-size:20px;font-weight:bold;">
		<font color="red">◆◆ 접근 불가 ◆◆</font><br/><br/>
		승인 받은 사용자가 아닙니다. <br/>
		승인 후에도 이 메시지가 보이거나,<br/>
		승인을 요청하시려면 <br/>
		시스템 관리자에게 문의하세요.<br/><br/>
		접근하신 IP는 <%=remoteIP %>입니다.<br/>
		접근하신 IP정보는 기록됩니다.
	</div>
<%}else{ %>
<div id="desktop">

<div id="desktopHeader">
<div id="desktopTitlebarWrapper">
	<div id="desktopTitlebar">
		<h1 class="applicationTitle">ThunderMail</h1>
		<h2 class="tagline">A Web Applications <span class="taglineEm">ThunderMail</span></h2>
		<div id="topNav">
			<ul class="menu-right">
				<li>Welcome <a id="LoginUserName" onclick="" style="cursor:default">Guest User</a>.</li>
				<li id="loginStatus">
					<a id="LoginLink" status="logout" href="">Login</a>
				</li>
			</ul>
		</div>
	</div>
</div>
	
<div id="desktopNavbar">
	<ul id="menuItems">
	
		<% 	
			for(MenuMain menuMain : menuMainList) {
				%>
				<li><a id="mainMenu<%=menuMain.getMainMenuID()%>" style="display:none" class="returnFalse" href=""><%=menuMain.getMainMenuName()%></a>
				<%
				//메인메뉴에 해당하는 서브메뉴 호출
				List<MenuSub> menuSubList = service.listMenuSub( menuMain.getMainMenuID() );
				
				if(menuSubList.size() > 0 ) {
					%><ul><%
				}
				for(MenuSub menuSub : menuSubList) {
					if(menuSub.getPopupYN().equals("Y")){
					%>
						<li><a href="javascript:OpenPopupWithSize('<%=menuSub.getPath()%>','yes',<%=menuSub.getWidth()%>,<%=menuSub.getHeight()%>)"><%=menuSub.getSubMenuName()%></a></li>
					<%
					}else{
					%>
						<li style="display:none"><a id="<%=menuSub.getWindowId()%>Link" <%=(menuSub.getDivider().equals("Y")?"class=\"divider\"":"")%> href="<%=menuSub.getPath()%>"><%=menuSub.getSubMenuName()%></a></li>
					<%
					}
				}
				
				if(menuSubList.size() > 0 ) {
					%></ul><%
				}
			}
				
		%>
		<li><a class="returnFalse" href="">도움말</a>
			<ul>
				<li><a href="javascript:OpenPopupWithSize('/helper_docs/index.htm','yes',970,720)">도움말보기</a></li>
				<li><a target="_blank" href="/helper_docs/helper_docs.pdf">도움말 다운로드</a></li>
				<li><a href="javascript:aboutWindow()">ThunderMail 정보</a></li> 
			</ul>
		</li>
	</ul>	
<!-- 
<div class="toolbox divider2">
	
	<a id="helpLink" href="" style="cursor:help" ><img src="images/help.gif" width="17" alt="Help Desk"/></a>	
</div>

<div class="toolbox divider2">
	<a id="cascadeLink" href=""><img src="images/icons/application_double.png"  width="16" height="16" alt="Cascade Windows" 

title="Cascade"/></a>
	<a id="tileLink" href=""><img src="images/icons/application_tile_vertical.png"  width="16" height="16" alt="Tile Windows" 

title="Tile"/></a>
	<a id="minimizeLink" href=""><img src="images/icons/application_put.png"  width="16" height="16" alt="Minimize All Windows" 

title="Minimize"/></a>
	<a id="closeLink" href=""><img src="images/icons/application.png" width="16" height="16" alt="Close All Windows" title="Close"/></a>
</div>

<div class="toolbox divider2">
	<a id="saveWorkspaceLink" href=""><img src="images/disk.png" width="16" height="16" alt="SaveWorkspace" title="SaveWorkspace"/></a>
	<a id="loadWorkspaceLink" href=""><img src="images/icons/tree/_open.gif" width="16" height="16" alt="LoadWorkspace" 

title="LoadWorkspace"/></a>
	<a id="parametricsLinkCheck" href=""><img src="images/icons/cog.gif" width="16" height="16" alt="Parametrics" title="Parametrics"/></a>
	<a id="clockLinkCheck" href=""><img src="images/clock.png" width="16" height="16" alt="Clock" title="Clock"/></a>
</div>
 -->	
</div><!-- desktopNavbar end -->
</div><!-- desktopHeader end -->

<div id="dockWrapper">
	<div id="dock">
		<div id="dockPlacement"></div>
		<div id="dockAutoHide"></div>
		<div id="dockSort"><div id="dockClear" class="clear"></div></div>
	</div>
</div>

<div id="pageWrapper"></div>

<!--  div id="desktopFooterWrapper">
	<div id="desktopFooter">
		&copy; 2009 <a target="_blank" href="http://www.andwise.com/">andWise Co Ltd.</a> 
	</div>
</div>

</div --><!-- desktop end -->
<%} %>
</body>
</html>
<%if(accessIP){ %>

<script type="text/javascript">
	
	// 로그인 테스트 set
	/*
	nemoRequest.init({
		 url: 'admin/login/login.do'
		, onSuccess: function(html,els,resHTML) {
		}
	});
	nemoRequest.get({'userAuthType':''});
	*/
	function initPanel() {
		nemoRequest.init( 
				{
					url: 'massmail/schedule/schedule.do?id=mainPanel', 
					update: 'mainPanel',
					updateEl: $('mainPanel'),
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
		nemoRequest.post();

		nemoRequest.init( 
				{
					url: 'pages/main/userinfo.jsp?id=userpanel', 
					update: 'userpanel',
					updateEl: $('userpanel'),
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
		nemoRequest.post();

		nemoRequest.init( 
				{
					url: 'pages/main/reportinfo.jsp?id=checkPanel', 
					update: 'checkPanel',
					updateEl: $('checkPanel'),
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
		nemoRequest.post();

		nemoRequest.init( 
				{
					url: 'pages/main/show_notice.jsp?id=board-panel', 
					update: 'board-panel',
					updateEl: $('board-panel'),
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
		nemoRequest.post();

		nemoRequest.init( 
				{
					url: 'pages/main/serverinfo.jsp?id=serverInfo-panel', 
					update: 'serverInfo-panel',
					updateEl: $('serverInfo-panel'),
					onSuccess: function(html,els,resHTML,scripts) {
					}
				});
		nemoRequest.post();
	}

	function logoutTest() {
		nemoRequest.init({
			 url: 'admin/login/login.do?method=clearSession'
		});
		nemoRequest.get({});
	}

	/* 리스트 표시 */
	window.addEvent("load",function () {
		//initPanel();
	});
	
	function aboutWindow() {
		new MochaUI.Window({
			id: 'about',
			title: 'MochaUI',
			loadMethod: 'xhr',
			contentURL: 'pages/main/about.html',
			type: 'modal2',
			width: 350,
			height: 175,
			contentBgColor: '#a7dbff url(images/thunsermail_logo.gif) left 3px no-repeat',
			padding: { top: 43, right: 12, bottom: 10, left: 5 },
			scrollbars:  false
		});
	}

function OpenPopupWithSize(URL,sParam,wParam,hParam,tParam,lParam)
{
  if (tParam==null | lParam==null) {
	window.open(URL,"popup","width="+wParam+",height="+hParam+",resizable=yes,scrollbars="+sParam+",menubar=no");
  }else{
	window.open(URL,"popup","width="+wParam+",height="+hParam+",top="+tParam+",left="+lParam+",resizable=yes,scrollbars="+sParam+",menubar=no");
  }
}
</script>
<%}%>