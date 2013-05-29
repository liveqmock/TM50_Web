<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>  
<%@ page import="web.target.targetui.control.TargetUIController"%>
<%@ page import="web.target.targetui.control.TargetUIControlHelper"%>
<%@ page import="web.target.targetui.service.TargetUIService"%>
<%@ page import="web.target.targetui.model.TargetList"%>
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	String targetID = request.getParameter("targetID")==null?"0":request.getParameter("targetID");
	TargetUIService service = TargetUIControlHelper.getUserService(application);
	int defaultTargetUI = service.getDefaultTargetUIID();
	int targetUIManagerID = 0;
	if(!targetID.equals("0"))
		targetUIManagerID = service.getTargetManagerID(Integer.parseInt(targetID));

	

%>

<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper" style="width:790px">
	<div>
		<c:import url="../../../include_inc/search_targetUI_inc.jsp">
		<c:param name="targetUIManagerID" value="<%=String.valueOf(targetUIManagerID) %>"/>			
		</c:import>
	</div>
</div>
</form>
<div id="<%=id%>_grid_content" style="clear:both;width:790px">

</div>




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
/* 리스트 
/***********************************************/

$('<%=id%>').list = function ( seq ) {	

	var frm = $('<%=id%>_sform');

	
	nemoRequest.init( 
	{
		url: 'target/targetui/targetui.do?id=<%=id%>&method=edit&targetUIManagerID='+seq, 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post(frm);
}

/***********************************************/
/* 창 열기
/***********************************************/
$('<%=id%>').editWindowTarget = function( seq ) {

	var frm = $('<%=id%>_sform');

	
	nemoRequest.init( 
	{
		url: 'target/targetui/targetui.do?id=<%=id%>&method=edittarget&targetID='+seq, 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post(frm);

	
}

/***********************************************/
/* 대상자 추가 버튼 클릭
/***********************************************/
$('<%=id%>').addTarget = function(targetUI, targetID ){

	
	var frm = $('<%=id%>_form');
	var goUrl = '';

	if(frm.eTargetUIManagerID.value=='0')
	{
		alert("회원검색UI를 선택하세요.");
		return;
	}

	//필수입력 조건 체크
	if(!checkFormValue(frm)) {
		return;
	}
	
	
	
	if(targetID=='0'){
		goUrl = 'target/targetui/targetui.do?id=<%=id%>&method=addTarget';
	}
	else{
		goUrl = 'target/targetui/targetui.do?id=<%=id%>&method=editTarget';
	}
	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				//updateWindowId: $('<%=id%>') // 완료후 버튼,힌트 가 랜더링될 window
				, url: goUrl
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
					if(resHTML.indexOf("0")>-1)
					{
						closeWindow( $('<%=id%>') );
					}
					else
					{
						$('<%=id%>').targetListWindow();
						closeWindow( $('<%=id%>') );
						
					}
				}
			});
	nemoRequest.post(frm);

}

/***********************************************/
/* 대상자 리스트 창 열기
/***********************************************/
$('<%=id%>').targetListWindow = function() {
	
	nemoWindow(
		{
		    'id': 'target',
			busyEl: 'target', // 창을 열기까지 busy 가 표시될 element
			width: 1000,
			height: $('mainColumn').style.height.toInt()-100,
			title: '대상자관리',
			type: 'window',
			loadMethod: 'xhr',
			contentURL: 'target/targetlist/target.do?id=target'
		}
	);
	
}

$('<%=id%>').chgView = function(targetUI){
	
	$('<%=id%>').list(targetUI); 
	
}

/* 리스트 표시 */
window.addEvent("domready",function () {
	$('<%=id%>').init();	
	 
	<%if(!targetID.equals("0")) { %>
		$('<%=id%>').editWindowTarget(<%=targetID%>);
	<%}else{%>
		$('<%=id%>').list(<%=defaultTargetUI%>); 
	<%}%>
	
});

</script>

