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
	<div style="clear:both;width:230px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="230px" >
		<thead>
			<tr style="display:none">
			<th style="height:30px;width:230px"></th>
			</tr>
		</thead>
		<tbody id="<%=id%>_grid_content">
		
		</tbody>
		</table>
	</form>
	</div>
    

<script type="text/javascript">
	/***********************************************/
	/* 리스트 
	/***********************************************/
	
	$('<%=id%>').list = function ( forPage ) {
		nemoRequest.init( 
		{
			busyWindowId: '<%=id%>',  // busy 를 표시할 window
			updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window
			
			url: 'massmail/write/massmail.do?method=selfList&id=<%=id%>', 
			update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
			updateEl: $('<%=id%>'),
			onSuccess: function(html,els,resHTML,scripts) {
				
				//MochaUI.arrangeCascade();
			}
		});
		nemoRequest.post($('<%=id%>_rform'));
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
				title: '대량메일 정보 보기',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'massmail/schedule/schedule.do?id=<%=id%>&method=view&massmailID='+seq +'&scheduleID='+sid
			}
		);
		
	}
	/***********************************************/
	/*  대량메일 발송 결과 리스트창 열기
	/***********************************************/
	$('<%=id%>').viewList = function( send ) {
		
		closeWindow($('<%=id%>_modal'));
		
		nemoWindow(
			{
				'id': 'massmailList',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 1000,
				height: $('mainColumn').style.height.toInt()-100,
				//height: 270,
				title: '발송결과리스트',
				//type: 'modal',
				noOtherClose: true,
				loadMethod: 'xhr',
				contentURL: 'massmail/write/massmail.do?step=list&id=massmailList&eSendScheduleDateStart='+send+'&eSendScheduleDateEnd='+send
			}
		);
		
	}
	/***********************************************/
	/* 반복메일 관리창 열기
	/***********************************************/
	$('<%=id%>').viewListRepeat = function( send ) {
		
		closeWindow($('<%=id%>_modal'));
		nemoWindow(
			{
				'id': 'massmailRepeat',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 1000,
				height: $('mainColumn').style.height.toInt()-100,
				//height: 270,
				title: '반복메일관리',
				//type: 'modal',
				noOtherClose: true,
				loadMethod: 'xhr',
				contentURL: 'massmail/write/massmail.do?step=listRepeat&id=massmailRepeat'
			}
		);
		
	}
	
	/* 리스트 표시 */
	window.addEvent("domready",function () {
		//$('<%=id%>').list(); 

	});

	
</script>