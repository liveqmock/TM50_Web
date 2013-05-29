<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.common.util.*" %>    
<%@page import="web.common.util.LoginInfo"%>
<%@ page import="web.admin.usergroup.model.User"%>
<%@ page import="web.admin.usergroup.service.UserGroupService"%>
<%@ page import="web.admin.usergroup.control.UserGroupControlHelper"%>
<%
	String id = request.getParameter("id");
%>
	<div id="<%=id%>Content" style="padding:8px;">
	</div> 
<script type="text/javascript">

	$('<%=id%>').loadContent = function() {
		nemoRequest.init( 
			{
				url: 'pages/main/userinfo_proc.jsp?id=<%=id%>&method=basicinfo', 
				update: $('<%=id%>Content'),
				updateEl: $('<%=id%>Content'),
				onSuccess: function(html,els,resHTML,scripts) {
				
				}
			});
		nemoRequest.post();
	}

	$('<%=id%>').editUserWindow = function() {
		nemoWindow(
			{
				'id': '<%=id%>_modal',
	
				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
	
				width: 500,
				height: 400,
				title: '사용자 추가/수정',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/main/userinfo_proc.jsp?id=<%=id%>&method=editinfo'
			}
		);
	}
	
	/***********************************************/
	/* 사용자 저장버튼 클릭
	/***********************************************/
	$('<%=id%>').saveUserData = function() {
	
		var frm = $('<%=id%>_form');
		
		//필수입력 조건 체크
		if(!checkFormValue(frm)) {
			return;
		} 
		if(frm.eEmail.value && !CheckEmail(frm.eEmail.value)) {
			toolTip.showTipAtControl(frm.eEmail,'이메일 형식이 틀립니다');
			return;
		}
	
		if(frm.eCellPhone.value && !CheckTel(frm.eCellPhone.value)) {
			toolTip.showTipAtControl(frm.eCellPhone,'휴대전화 형식이 틀립니다');
			return;
		}
		if(frm.eSenderEmail.value && !CheckEmail(frm.eSenderEmail.value)) {
			toolTip.showTipAtControl(frm.eSenderEmail,'보내는 이메일 형식이 틀립니다');
			return;
		}
		if(frm.eSenderCellPhone.value && !CheckTel(frm.eSenderCellPhone.value)) {
			toolTip.showTipAtControl(frm.eSenderCellPhone,'보내는 휴대전화 형식이 틀립니다');
			return;
		}
		if(frm.eUserPWD2.value != frm.eUserPWD.value) {
			toolTip.showTipAtControl(frm.eUserPWD2,'패스워드가 틀립니다');
			return;
		}
		for( var i = 0; i < frm.eUserID.value.length;i++)
		{
			var c = frm.eUserID.value.charCodeAt(i);
			if( !( (  0x61 <= c && c <= 0x7A ) || ( 0x41 <= c && c <= 0x5A ) || (  0x30 <= c && c <= 0x39 ) ) )
			{
				toolTip.showTipAtControl(frm.eUserID,'사용자아이디 는 영문 또는 숫자만 입력해야 합니다 ');
				return;
			}
	
		}
		
		goUrl = 'admin/usergroup/usergroup.do?id=<%=id%>&method=updateUserSelf';
		
		nemoRequest.init( 
		{
			busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
			, url: goUrl
			, onSuccess: function(html,els,resHTML) {
				closeWindow($('<%=id%>_modal'));
				$('<%=id%>').loadContent();
			}
		});
		nemoRequest.post(frm);
		
	}

	/***********************************************/
	/* 비밀번호 변경버튼 클릭
	/***********************************************/
	$('<%=id%>').savePassword = function(var1) {
		var frm = $('<%=id%>_form');
		
		if(var1 == 'next'){
			frm.eOldUserPWD.value = frm.eCheckUserPWD.value;
			frm.eUserPWD.value = frm.eCheckUserPWD.value;
			frm.eUserPWD2.value = frm.eCheckUserPWD.value;
		}else{
			//필수입력 조건 체크
			if(frm.eOldUserPWD.value==''){
				alert('현재 비밀번호는 필수항목입니다.');
				frm.eOldUserPWD.focus();
				return;
			}

			if(frm.eUserPWD.value==''){
				alert('새로운 비밀번호는 필수항목입니다.');
				frm.eUserPWD.focus();
				return;
			}
	
			if(frm.eUserPWD2.value==''){
				alert('비밀번호확인은 필수항목입니다.');
				frm.eUserPWD2.focus();
				return;
			}
			
			if(frm.eCheckUserPWD.value != frm.eOldUserPWD.value) {
				alert('현재 비밀번호가 틀립니다');
				return;
			}
			
			if(frm.eUserPWD2.value != frm.eUserPWD.value) {
				alert('새로운 비밀번호와 비밀번호 확인의 입력값이 틀립니다.');
				return;
			}
		}

		goUrl = 'admin/usergroup/usergroup.do?id=<%=id%>&method=updateUserSelf';
		
		nemoRequest.init( 
		{
			busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
			, url: goUrl
			, onSuccess: function(html,els,resHTML) {
				closeWindow($('<%=id%>_modal'));
				$('<%=id%>').loadContent();
			}
		});
		nemoRequest.post(frm);
		
	}
	<%if(LoginInfo.getPWDChangeNoticeYN(request).equals("Y")) { %>
		nemoWindow(
		{
			'id': '<%=id%>_modal',
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
			width: 500,
			height: 250,
			title: '비밀번호 변경안내',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'pages/main/userinfo_proc.jsp?id=<%=id%>&method=pwdEdit'
		});
	<%} %>

	/* 리스트 표시 */
	window.addEvent("domready",function () {
		$('<%=id%>').loadContent();
	});
</script>