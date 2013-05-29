<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@page import="web.admin.menu.model.MenuMain"%>
<%@page import="web.admin.menu.model.MenuSub"%>
<%@page import="web.admin.menu.service.MenuService"%>
<%@page import="web.admin.menu.control.MenuControlHelper"%>
<%@page import="web.common.util.LoginInfo"%>

<%
	//모델을 이용하여  모든 서브메뉴의 윈도우 리스트를 가져온다.
	//MenuService service = MenuControlHelper.getUserService(application);
	//List<MenuMain> menuMainList = service.listMenuMain();
	MenuService service = MenuControlHelper.getUserService(application);
	List<MenuSub> menuSubList = service.listMenuSub();

%>


initializeWindows = function(){

<%
	String windowId = "";
	String contentURL = "";

	for(MenuSub menuSub : menuSubList) {
		windowId = menuSub.getWindowId();
		contentURL = menuSub.getPath();
		if(contentURL.indexOf('?') > -1) contentURL = contentURL +"&id="+menuSub.getWindowId();
		else contentURL = contentURL +"?id="+menuSub.getWindowId();
		%>
		MochaUI.<%=windowId%>Window = function(){
			nemoWindow({
				id: '<%=windowId%>',
				title: '<%=menuSub.getSubMenuName()%>',
				loadMethod: 'xhr',
				contentURL: '<%=contentURL%>',
				//container: 'pageWrapper',
				<%if(menuSub.getFullSize().equals("Y")) { %>
				container: 'desktop',
				<%}%>
				noOtherClose: true,
				
				<% // 탭 사용 여부
				if(!menuSub.getTabPath().equals("")) { %>
				toolbar : true,
				toolbarURL : '<%=menuSub.getTabPath()%>',
				<%}%>

				<% // padding 여부
				if(menuSub.getPadding().equals("N")) { %>
					padding: { top: 0, right: 0, bottom: 0, left: 0 },
				<%}%>
				
				<%=(menuSub.getX()!=0? "x : "+menuSub.getX()+",": "" ) %>
				<%=(menuSub.getY()!=0? "y : "+menuSub.getY()+",": "" ) %>
				
				<% // width 가 0 이면 풀사이즈
				if(menuSub.getWidth() == 0) {%>
					width: $('desktop').getSize().x-60,
				<%
				} else {
				%>
					width: <%=menuSub.getWidth()%>,
				<%}%>
				
				<%if(menuSub.getFullSize().equals("Y")) { %>
					height :<%=(menuSub.getHeight()!=0? menuSub.getHeight(): "$('desktop').style.height.toInt()-100")%>,
				<%}else{ %>
					height :<%=(menuSub.getHeight()!=0? menuSub.getHeight(): "$('mainColumn').style.height.toInt()-100")%>,
				<%}%>
				
				onContentLoaded: function(){
					<% // accordian 여부
					if(menuSub.getAccordian().equals("Y")) { %>
					this.windowEl = MochaUI.Windows.instances.get('<%=windowId%>');
					var accordianDelay = function(){
						new Accordion('#' + '<%=windowId%>' + ' h3.accordianToggler', "#" + '<%=windowId%>' + ' div.accordianElement',{
						//	start: 'all-closed',
							opacity: false,
							alwaysHide: true,
							onActive: function(toggler, element){
									toggler.addClass('open');
							},
							onBackground: function(toggler, element){
									toggler.removeClass('open');
							},							
							onStart: function(toggler, element){
								this.windowEl.accordianResize = function(){
									//MochaUI.dynamicResize($('<%=windowId%>'));
								}
								this.windowEl.accordianTimer = this.windowEl.accordianResize.periodical(10);
							}.bind(this),
							onComplete: function(){
								this.windowEl.accordianTimer = $clear(this.windowEl.accordianTimer);
								//MochaUI.dynamicResize($('<%=windowId%>')) // once more for good measure
							}.bind(this)
						}, $('<%=windowId%>'));
					}.bind(this)
					accordianDelay.delay(10, this); // Delay is a fix for IE
					<% } // accordian end %>
				}
			});
		
		}	
		if ($('<%=windowId%>Link')){ 
			$('<%=windowId%>Link').addEvent('click', function(e){
				new Event(e).stop();
				MochaUI.<%=windowId%>Window();
			});
		}
				
		<%
	}
	
%>	
		MochaUI.clockWindow = function(){
		new MochaUI.Window({
			id: 'clock',
			title: 'Canvas Clock',
			addClass: 'transparent',
			loadMethod: 'xhr',
			contentURL: 'plugins/coolclock/index.html?t=' + new Date().getTime(),
			onContentLoaded: function(){
				if ( !MochaUI.clockScript == true ){
					new Request({
						url: 'plugins/coolclock/scripts/coolclock.js?t=' + new Date().getTime(),
						method: 'get',
						onSuccess: function() {
							if (Browser.Engine.trident) {
								myClockInit = function(){
									CoolClock.findAndCreateClocks();
								};
								window.addEvent('domready', function(){
									myClockInit.delay(10); // Delay is for IE
								});
								MochaUI.clockScript = true;
							}
							else {
								CoolClock.findAndCreateClocks();
							}
						}.bind(this)
					}).send();
				}
				else {
					if (Browser.Engine.trident) {
						myClockInit = function(){
							CoolClock.findAndCreateClocks();
						};
						window.addEvent('domready', function(){
							myClockInit.delay(10); // Delay is for IE
						});
						MochaUI.clockScript = true;
					}
					else {
						CoolClock.findAndCreateClocks();
					}
				}
			},
			shape: 'gauge',
			headerHeight: 30,
			width: 160,
			height: 160,
			x: 570,
			y: 140,
			padding: { top: 0, right: 0, bottom: 0, left: 0 },
			bodyBgColor: [250,250,250]
			});	
		}
		if ($('clockLinkCheck')){
			$('clockLinkCheck').addEvent('click', function(e){	
				new Event(e).stop();
				MochaUI.clockWindow();
			});
		}
		
		MochaUI.parametricsWindow = function(){	
			new MochaUI.Window({
				id: 'parametrics',
				title: 'Window Parametrics',
				loadMethod: 'xhr',
				contentURL: 'plugins/parametrics/index.html',
				onContentLoaded: function(){
					if ( !MochaUI.parametricsScript == true ){
						new Request({
							url: 'plugins/parametrics/scripts/parametrics.js',
							method: 'get',
							onSuccess: function() {
								MochaUI.addRadiusSlider.delay(10); // Delay is for IE6
								MochaUI.addShadowSlider.delay(10); // Delay is for IE6
								MochaUI.parametricsScript = true;
							}.bind(this)
						}).send();
					}
					else {
						MochaUI.addRadiusSlider.delay(10); // Delay is for IE6
						MochaUI.addShadowSlider.delay(10); // Delay is for IE6
					}
				},
				width: 305,
				height: 110,
				x: 570,
				y: 160,
				padding: { top: 12, right: 12, bottom: 10, left: 12 },
				resizable: false,
				maximizable: false,
				contentBgColor: '#fff'
			});
		}
		if ($('parametricsLinkCheck')){
			$('parametricsLinkCheck').addEvent('click', function(e){	
				new Event(e).stop();
				MochaUI.parametricsWindow();
			});
		}	
		
		if ($('saveWorkspaceLink')){
		$('saveWorkspaceLink').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.saveWorkspace();
			});
		}
	
		if ($('loadWorkspaceLink')){
			$('loadWorkspaceLink').addEvent('click', function(e){
				new Event(e).stop();
				MochaUI.loadWorkspace();
			});
		}
		
		MochaUI.helpDeskWindow = function(){	
			new MochaUI.Window({
				id: 'helpDeskWindow',
				title: 'Help Desk',
				loadMethod: 'xhr',
				contentURL: 'pages/helpdesk/helpdesk.jsp?id=helpDesk',
				width: 150,
				height: 90,
				x: $('desktop').getSize().x - 153,
				y:80
			});
		}
		
		if ($('helpLink')){
		$('helpLink').addEvent('click', function(e){
			new Event(e).stop();
				MochaUI.helpDeskWindow();
			});
		}
		
		MochaUI.LoginWindow = function(){
			nemoWindow({
				id: 'loginWin',
				title: 'ThunderMail V5.0 로그인',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'admin/login/login.do?id=loginWin',
				noOtherClose: true,
				width: 515,
				height:220
			});
		
		}	

	// View
	if ($('sidebarLinkCheck')){
		$('sidebarLinkCheck').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.Desktop.sidebarToggle();
		});
	}

	if ($('cascadeLink')){
		$('cascadeLink').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.arrangeCascade();
		});
	}

	if ($('tileLink')){
		$('tileLink').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.arrangeTile();
		});
	}

	if ($('closeLink')){
		$('closeLink').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.closeAll();
		});
	}

	if ($('minimizeLink')){
		$('minimizeLink').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.minimizeAll();
		});
	}	

	// Tools
	MochaUI.builderWindow = function(){	
		new MochaUI.Window({
			id: 'builder',
			title: 'Window Builder',
			icon: 'images/icons/page.gif',
			loadMethod: 'xhr',
			contentURL: 'plugins/windowform/',
			onContentLoaded: function(){
				if ( !MochaUI.windowformScript == true ){
					new Request({
						url: 'plugins/windowform/scripts/Window-from-form.js',
						method: 'get',
						onSuccess: function() {
							$('newWindowSubmit').addEvent('click', function(e){
								new Event(e).stop();
								new MochaUI.WindowForm();
							});
							MochaUI.windowformScript = true;
						}.bind(this)
					}).send();
				}
			},
			width: 370,
			height: 410,
			maximizable: false,
			resizable: false,
			scrollbars: false
		});
	}
	if ($('builderLinkCheck')){
		$('builderLinkCheck').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.builderWindow();
		});
	}
	
	// Todo: Add menu check mark functionality for workspaces.
	
	// Help	
	MochaUI.featuresWindow = function(){
		new MochaUI.Window({
			id: 'features',
			title: 'Features',
			loadMethod: 'xhr',
			contentURL: 'pages/features-layout.html',
			width: 305,
			height: 175,
			resizeLimit: {'x': [275, 2500], 'y': [125, 2000]},
			toolbar: true,
			toolbarURL: 'pages/features-tabs.html'
		});
	}
	if ($('featuresLinkCheck')){
		$('featuresLinkCheck').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.featuresWindow();
		});
	}

	MochaUI.faqWindow = function(){
			new MochaUI.Window({
				id: 'faq',
				title: 'FAQ',
				loadMethod: 'xhr',
				contentURL: 'pages/faq.html',
				width: 750,
				height: 350
			});
	}
	if ($('faqLinkCheck')){
		$('faqLinkCheck').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.faqWindow();
		});
	}

	MochaUI.docsWindow = function(){
			new MochaUI.Window({
				id: 'docs',
				title: 'Documentation',
				loadMethod: 'xhr',
				contentURL: 'pages/docs.html',
				width: 750,
				height: 350,
				padding: [10,10,10,10,10]
			});
	}
	if ($('docsLinkCheck')){
		$('docsLinkCheck').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.docsWindow();
		});
	}

	MochaUI.resourcesWindow = function(){
			new MochaUI.Window({
				id: 'resources',
				title: 'Resources',
				loadMethod: 'xhr',
				contentURL: 'pages/resources.html',
				width: 300,
				height: 275,
				x: 20,
				y: 90 
			});
	}
	if ($('resourcesLinkCheck')){
		$('resourcesLinkCheck').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.resourcesWindow();
		});
	}

	MochaUI.helpWindow = function(){
			new MochaUI.Window({
				id: 'help',
				title: 'Support',
				loadMethod: 'xhr',
				contentURL: 'pages/support.html',
				width: 320,
				height: 320,
				x: 20,
				y: 90 
			});
	}
	if ($('helpLinkCheck')){
		$('helpLinkCheck').addEvent('click', function(e){
			new Event(e).stop();
			MochaUI.helpWindow();
		});
	}	

	MochaUI.contributeWindow = function(){
		new MochaUI.Window({
			id: 'contribute',
			title: 'Contribute',
			loadMethod: 'xhr',
			contentURL: 'pages/contribute.html',
			width: 320,
			height: 320,
			x: 20,
			y: 90 
		});
	}
	if ($('contributeLinkCheck')){
		$('contributeLinkCheck').addEvent('click', function(e){	
			new Event(e).stop();
			MochaUI.contributeWindow();
		});
	}


	// Deactivate menu header links
	$$('a.returnFalse').each(function(el){
		el.addEvent('click', function(e){
			new Event(e).stop();
		});
	});
	
	// Build windows onDomReady
	//MochaUI.parametricsWindow();
	
	
	MochaUI.alertWindow = function( msg ){
		if(Browser.Engine.trident4) {
			alert( msg );
		} else {

			var msg = '<div style="width:100%;height:100px"><b>'+msg+'</b></div>'+
					   '<div style="padding-left:120px"><a href="#" class="web20button bigpink" onclick="MochaUI.closeWindow($(\'about\'))">닫 기</a></div>';

			new MochaUI.Window({
				id: 'about',
				title: '아주법학전문대학원',
				content: msg,
				type: 'modal2',
				width: 300,
				height: 155,
				contentBgColor: '#EFF2F8 left 3px no-repeat',
				padding: { top: 18, right: 12, bottom: 10, left: 12 },
				scrollbars:  false,
				onContentLoaded: function() {
					initWeb20Buttons($('about'));
				}
			});
		}
	}

	MochaUI.confirmWindow = {
		confirm: function( msg ){

			this.onTrue = function() {};
			this.onFalse = function() {};

			var msg = '<div style="width:100%;height:100px"><b>'+msg+'</b></div>'+
					   '<div style="float:left;margin-left:80px"><a href="#" style="margin-right:10px" class="web20button bigblue" onclick="MochaUI.confirmWindow.onTrueClick()"> 예 </a></div>'+
					   '<div style="float:left;margin-left:10px"><a href="#" class="web20button bigpink" onclick="MochaUI.confirmWindow.onFalseClick()">아니오</a></div>';

			new MochaUI.Window({
				id: 'confirm',
				title: '아주법학전문대학원',
				content: msg,
				type: 'modal2',
				width: 300,
				height: 155,
				contentBgColor: '#EFF2F8 left 3px no-repeat',
				padding: { top: 18, right: 12, bottom: 10, left: 12 },
				scrollbars:  false,
				onContentLoaded: function() {
					initWeb20Buttons($('confirm'));
				}
			});
	
		},
		onTrue: function() {},
		onFalse: function() {},
		onTrueClick: function() {
			this.result=true;
			MochaUI.closeWindow($('confirm'));
			this.onTrue();
		},
		onFalseClick: function() {
			this.result=false;
			MochaUI.closeWindow($('confirm'));
			this.onFalse();
		}
	}
	
	
}

// Initialize MochaUI when the DOM is ready
window.addEvent('load', function(){
	MochaUI.Desktop = new MochaUI.Desktop();
	MochaUI.Dock = new MochaUI.Dock({dockPosition:'bottom'});

	/* Create Columns
	 
	If you are not using panels then these columns are not required.
	If you do use panels, the main column is required. The side columns are optional.
	Create your columns from left to right. Then create your panels from top to bottom,
	left to right. New Panels are inserted at the bottom of their column.

	*/	 
	new MochaUI.Column({
		id: 'sideColumn1',
		placement: 'left',
		width: 250,
		resizeLimit: [100, 300]
	});

	new MochaUI.Column({
		id: 'mainColumn',
		placement: 'main',	
		width: null,
		resizeLimit: [100, 300]
	});

	new MochaUI.Column({
		id: 'sideColumn2',
		placement: 'right',	
		width: 250,		
		resizeLimit: [195, 300]
	});

	// Add panels to first side column
	new MochaUI.Panel({
		id: 'userpanel',
		title: '개인정보',
		//loadMethod: 'xhr',
		//contentURL: 'pages/main/userinfo.jsp?id=userpanel',
		column: 'sideColumn1',
		panelBackground: '#ffffff',
		onResize:   [195, 300]
	});

	new MochaUI.Panel({
		id: 'checkPanel',
		title: '모니터링',
		//loadMethod: 'xhr',
		//contentURL: 'pages/main/reportinfo.jsp?id=checkPanel',
		column: 'sideColumn1',
		panelBackground: '#ffffff',
		height: $('desktop').style.height.toInt()-340

	});
	// Add panels to main column
	
	new MochaUI.Panel({
		id: 'mainPanel',
		title: '발송스케줄',
		//loadMethod: 'xhr',
		//contentURL: 'massmail/schedule/schedule.do?id=mainPanel',
		noOtherClose: true,
		column: 'mainColumn',
		panelBackground: '#ffffff'
	});

	// Add panels to second side column
	
	new MochaUI.Panel({
		id: 'board-panel',
		title: '공지사항',
		//loadMethod: 'xhr',
		//contentURL: 'pages/main/show_notice.jsp?id=board-panel',
		column: 'sideColumn2',
		panelBackground: '#ffffff'
	});
	new MochaUI.Panel({
		id: 'serverInfo-panel',
		title: '서버모니터링',
		//loadMethod: 'xhr',
		//contentURL: 'pages/main/serverinfo.jsp?id=serverInfo-panel',
		column: 'sideColumn2',
		panelBackground: '#ffffff',
		height: $('desktop').style.height.toInt()-340
	});
	/*
	new MochaUI.Panel({
		id: 'tips-panel',
		title: '문의하기',
		loadMethod: 'xhr',
		contentURL: 'pages/main/thunderinfo.jsp?id=tips-panel',
		column: 'sideColumn2',
		panelBackground: '#ffffff',
		height: $('desktop').style.height.toInt()-500
		//tabsURL: 'pages/panel-tabs.html'
		//scrollbars: false
		//footer: true
		//footerURL: 'pages/toolbox-demo.html'
	});
	*/

	MochaUI.Modal = new MochaUI.Modal();
	
	MochaUI.Desktop.desktop.setStyles({
		'background': '#fff',
		'visibility': 'visible'
	});
	initializeWindows();
	
	// 버튼만들기를 위한 쿠키
	Cookie.write("trident4", (Browser.Engine.trident4?"Y":"N") );
	Cookie.write("trident", (Browser.Engine.trident?"Y":"N") );
	Cookie.write("gecko", (Browser.Engine.gecko ?"Y":"N") );

	toolTip.init();
	
	<% if(!LoginInfo.getIsLogined(request)) { %>
	     // 로그아웃 된 상태 ****************************
		$('LoginLink').setAttribute('status','logout');
		MochaUI.LoginWindow();
	 <% } else { %>
	     // 로그인 된 상태 *******************************
		$('LoginLink').setAttribute('status','login');
		$('LoginUserName').set('text','<%=LoginInfo.getUserID(request)%>');
		//초기화면 정보 표출
		initPanel();
		
		<%	// 시스템관리자이면 모든메뉴 보이게
		if(LoginInfo.getUserAuth(request).equals("1")) { %>
			showAllMenu();
	 	<% } else { %>
			//로긴된 사용자 메뉴 보이게
			nemoRequest.init({
				url: 'admin/login/login.do?method=showMenu'
			});
			nemoRequest.get({});
	 	<% }%>
		
	 <% }%>
	 
	setLogInOutStatus();

});

// 로그인 상태 변경
function setLogInOutStatus() {

	var status = $('LoginLink').getAttribute('status');
	
	if(status == 'login') {
		$('LoginLink').set('text','Logout');
		$('LoginLink').onclick = function() {
			// 세션 삭제
			nemoRequest.init({
				url: 'admin/login/login.do?method=logOut'
			});
			nemoRequest.get({});
		};
	} else {
		$('LoginLink').set('text','Login');
		$('LoginLink').onclick = MochaUI.LoginWindow;
	}

}
// 모든메뉴를 감춘다
function hideAllMenu() {

	var mainMenu = $(document.body).getElements('.returnFalse');
	for( var i=0; i < mainMenu.length; i++ ) {
		if(mainMenu[i].getParent().getAttribute('display') != 'fixed') {
			mainMenu[i].setStyle('display','none');
			var subMenu = mainMenu[i].getParent().getElements('li');
			for( var j =0; j < subMenu.length; j ++ ) {
				subMenu[j].setStyle('display','none');
			} 
		}
	}
	location.reload();
}

// 모든메뉴를 보이게한다
function showAllMenu() {

	var mainMenu = $(document.body).getElements('.returnFalse');
	for( var i=0; i < mainMenu.length; i++ ) {
		if(mainMenu[i].getParent().getAttribute('display') != 'fixed') {
			var subMenu = $(mainMenu[i]).getParent().getElements('li');
			for( var j =0; j < subMenu.length; j ++ ) {
				subMenu[j].setStyle('display','block');
			} 
			mainMenu[i].setStyle('display','block');
		}
	}
	
}


// This runs when a person leaves your page.
closeMochaUIFunc = function(){
	if (MochaUI) {
		MochaUI.closeAll();
		var garbageCleanUp = function() {
			MochaUI.garbageCleanUp()}
		garbageCleanUp.delay(50);
	}
}

//window.addEvent('unload', closeMochaUIFunc());

window.addEvent('unload', function() {
		$$('div.mocha').each(function(el){
			if(el) el.dispose();
		});

});