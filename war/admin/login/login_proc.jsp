<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@page import="web.admin.menu.model.MenuMain"%>
<%@page import="web.admin.menu.model.MenuSub"%>
<%@page import="web.admin.menu.service.MenuService"%>
<%@page import="web.admin.menu.control.MenuControlHelper"%>
<%@page import="web.common.util.LoginInfo"%>
<%@ page import="web.common.util.ServletUtil" %>
<%@ page import="web.common.util.StringUtil" %>
<%@ page import="web.common.util.LoginInfo" %>
<%@ page import="java.util.List"%>
<%

String id = request.getParameter("id");
String sMethod = request.getParameter("method");

//****************************************************************************************************/
// 로그인 
//****************************************************************************************************/
if(sMethod.equals("find")) {
	
	String userID = request.getParameter("userID");
	String userAuth = LoginInfo.getUserAuth(request);
	
	
	// 쿠키에 id 기억 
	if(StringUtil.toStr( request.getParameter("rememberID") ).equals("Y")) {
		ServletUtil.setCookie(request,response,"rememberID","Y",1);
		ServletUtil.setCookie(request,response,"savedUserID", userID,1);
	}
	
%>
	<script type="text/javascript">
		<%if(StringUtil.toStr( request.getParameter("rememberID") ).equals("Y")) {%>
			Cookie.write('rememberID', 'Y', {duration: 100});
			Cookie.write('savedUserID', '<%=userID%>', {duration: 100});
		<%} else {%>
			Cookie.write('rememberID', '', {duration: 0});
			Cookie.write('savedUserID', '', {duration: 0});
		<%}%>

				
		location.replace('/index.jsp');
		
		
	
</script>
<%
}
//****************************************************************************************************/
//로그아웃 
//****************************************************************************************************/
if(sMethod.equals("logOut")) {
%>
	<script type="text/javascript">
	
		$('LoginLink').setAttribute('status','logout');
		$('LoginUserName').set('text','Guest User');
		
		setLogInOutStatus();
		MochaUI.closeAll();

		// 메뉴를 감춘다.
		hideAllMenu();
	</script>

<%
}
//****************************************************************************************************/
//로그인된사용자의 메뉴 보이기 
//****************************************************************************************************/
if(sMethod.equals("showMenu")) {

	String userID = LoginInfo.getUserID(request);

	//모델을 이용하여  모든 서브메뉴의 윈도우 리스트를 가져온다.
	MenuService service = MenuControlHelper.getUserService(application);
	List<MenuSub> menuSubList = service.listMenuSubAuth(userID);
	String windowId = null;
	%>
	
	<script type="text/javascript">

	<%
	for(MenuSub menuSub : menuSubList) {
		windowId = menuSub.getWindowId();
	%>
		$('<%=menuSub.getWindowId()%>Link').getParent().setStyle('display','block');
		$('mainMenu<%=menuSub.getMainMenuID()%>').setStyle('display','block');
	<%}%>

	</script>

<%
}
%>

		