<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.ServletUtil" %>
<%@ page import="web.common.util.StringUtil" %>
<%
	String id = request.getParameter("id");
%>


<%@page import="java.net.URLDecoder"%><form name="<%=id%>_form" id="<%=id%>_form">
<div class="login">
	<div style="padding-top:60px;padding-left:210px;color:#777777;float:left">
		<div><strong>아이디</strong> <input type="text" id="userID" name="userID" value=""  style="margin-left:13px;ime-mode:disabled;background-color:#EEEEEE;border-color:#c1c1c1;"  onKeyDown="if(event.keyCode==13){javascript:$('<%=id%>').checkLogin()}"/></div>
		<div style="margin:5px 0"><strong>비밀번호</strong> <input type="password"  id="userPWD" name="userPWD" style="background-color:#EEEEEE;border-color:#c1c1c1;" onKeyDown="if(event.keyCode==13){javascript:$('<%=id%>').checkLogin()}"/></div>
		<input type="checkbox" class="notBorder" id="rememberID" name="rememberID" value="Y">&#160;&#160;아이디 저장
	</div>
	<a href="javascript:$('<%=id%>').checkLogin()" style="position:relative;left:5px;top:57px"><img src="images/login_btn.gif"/></a>
	<div style="clear:both;height:10px"></div>
	<div style="color:#777777">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/tag_red.png" alt="대소문자구분안내 ">	로그인시에 대/소문자를 구분합니다.</div>
	<div style="color:#777777">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/tag_red.png" alt="브라우저안내">	Mozilia Firefox, Google Chrome, Internet Explorer 8에 최적화 되어 있습니다.</div>
</div>
</form>

<script type="text/javascript">

/**************************************************************************/
// 로그인 체크  
/**************************************************************************/
$('<%=id%>').checkLogin = function() {

	var frm = $('<%=id%>_form');

	//필수입력 조건 체크
	if(frm.userID.value==''){
        toolTip.showTipAtControl(frm.userPWD,'아이디를 입력하세요');
        return;
    }
    if(frm.userPWD.value==''){
        toolTip.showTipAtControl(frm.userPWD,'비밀번호를 입력하세요');
        return;
    }
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>'  // busy 를 표시할 window
		, url: 'admin/login/login.do?id=<%=id%>&method=find'
	});
	nemoRequest.post(frm);
	
	
}
/**************************************************************************/
//초기화  
/**************************************************************************/
$('<%=id%>').init = function() {

	//엔터키 이벤트 생성
	//keyUpEvent('<%=id%>_form' , $('<%=id%>_form'), $('<%=id%>').checkLogin );
	
	// 아이디 저장
	<%if( StringUtil.toStr( ServletUtil.getCookieValue(request,"rememberID") ).equals("Y")) { %>
		$('<%=id%>_form').rememberID.checked = true;
		<%if( !StringUtil.toStr( ServletUtil.getCookieValue(request,"savedUserID") ).equals("")) { %>
			$('<%=id%>_form').userID.value = escape('<%=ServletUtil.getCookieValue(request,"savedUserID")%>');
		<%}%>
	<%}%>

	if($('<%=id%>_form').userID.value)
		$('<%=id%>_form').userPWD.focus();
	else
		$('<%=id%>_form').userID.focus();
	
}	

$('<%=id%>').init();
</script>