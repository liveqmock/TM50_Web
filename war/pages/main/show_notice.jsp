<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<%@ page import="web.common.util.LoginInfo"%>
<%@ page import="web.admin.board.control.BoardControlHelper"%>
<%@ page import="web.admin.board.service.BoardService"%>
<%@ page import="web.admin.board.model.Board"%>
<%@ page import="java.util.List"%> 
<%
	String id = request.getParameter("id");
	String groupID =  LoginInfo.getGroupID(request);
	
%>
	<div id="<%=id%>_grid_content" style="clear:both;" >
	</div>

<script type="text/javascript">


/***********************************************/
/* 리스트 
/***********************************************/

$('<%=id%>').list = function ( forPage ) {
	nemoRequest.init( 
	{
		//busyWindowId: '<%=id%>',  // busy 를 표시할 window
		//updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'admin/board/board.do?method=listShow&id=<%=id%>&groupID=<%=Integer.parseInt(groupID)%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		updateEl: $('<%=id%>_grid_content'),
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post();
}


/***********************************************/
/* 보기창 열기
/***********************************************/
$('<%=id%>').editWindow = function( seq ) {
	closeWindow( $('<%=id%>_modal') );
	nemoWindow(
		{
			'id': '<%=id%>_modal',
			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
			width: 700,
			height: $('mainColumn').style.height.toInt(),
			//height: 500,
			title: '공지사항 보기',
			//type: 'modal',
			container: 'desktop',
			noOtherClose: true,
			loadMethod: 'xhr',
			contentURL: 'admin/board/board.do?id=<%=id%>&method=view&boardID='+seq
		}
	);
	
}

/***********************************************/
/* 공지사항 정보 보이기 / 숨기기
/***********************************************/
$('<%=id%>').showInfo = function(border_id) {
	if($('<%=id %>overDivBorderInfo'+border_id).getStyle('display') == 'none'){
		$('<%=id %>overDivBorderInfo'+border_id).setStyle('display','block');
	}else{
		$('<%=id %>overDivBorderInfo'+border_id).setStyle('display','none');
	}
}


/***********************************************/
/* 리스트창 열기
/***********************************************/
$('<%=id%>').viewList = function() {
	
	
	nemoWindow(
		{
			'id': 'noticeBoard',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 790,
			height: $('mainColumn').style.height.toInt(),
			title: '공지사항',
			//type: 'modal',
			container: 'desktop',
			noOtherClose: true,
			loadMethod: 'xhr',
			contentURL: 'admin/board/board.do?id=noticeBoard'
		}
	);
}

/***********************************************/
/* 파일 정보 보이기
/***********************************************/
$('<%=id%>').showFileInfo = function() {
	
	// 업로드가 완료되면 처리될 action
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window
		updateWindowId: $('<%=id%>uploaded'),  // 완료후 버튼,힌트 가 랜더링될 window
		url: 'admin/board/board.do?method=getFileInfo',
		update: $('<%=id%>uploaded'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {

			//if(nextFunc) nextFunc();
						
		}
	});
	nemoRequest.get({
		'id': '<%=id%>', 
		'boardID': $('<%=id%>_form').eBoardID.value,
		'uploadKey': $('<%=id%>_form').eBoardUploadKey.value
	});
	
	
}


/***********************************************/
/*파일 다운로드
/***********************************************/
$('<%=id%>').downloadFile = function( uploadKey ) {


		
	nemoRequest.init({
		busyWindowId: $('<%=id%>_modal'),  // busy 를 표시할 window

		url: 'admin/board/board.do?method=fileDownload',
		 
		onSuccess: function(html,els,resHTML,scripts) {
			document.location.href = 'admin/board/board.do?method=fileDownload&uploadKey='+uploadKey+'&reDirectDownload=Y';
		}
	});
	nemoRequest.get({'id': '<%=id%>', 'uploadKey': uploadKey});
	

}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').list(); 

});

</script>