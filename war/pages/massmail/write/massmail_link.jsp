<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	String massmailID = request.getParameter("massmailID");
	String state = request.getParameter("state");
	
//****************************************************************************************************/
////메일링크 창 
//****************************************************************************************************/

if(method.equals("massmailLink")) {

%>

	
<div class="search_wrapper" style="float:right;width:870px">
	<div style="float:right">
	<table border="0" cellpadding="3">
		<tbody>
		<tr>
			<td aling="right">
				<a href="javascript:$('<%=id%>').getBack()" class="web20button bigpink">돌아가기</a>
			</td>			
			<td aling="right">
				<a href="javascript:$('<%=id%>').deleteLink()" class="web20button bigpink">선택삭제</a>
			</td>	
			<td aling="right">
				<a href="javascript:$('<%=id%>').saveData()" class="web20button bigpink">발송</a>
			</td>					
		</tr>
		</tbody>
	</table>
	</div>	
</div>


<div style="clear:both;width:870px">
	<form id="<%=id%>_list_form" name="<%=id%>_list_form">
	<input type="hidden" name="eMassmailID" id="eMassmailID" value="<%=massmailID %>"/>
	<input type="hidden" name="eState" id="eState" value="<%=state %>"/>
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="870px" >
	<thead>		
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder" onclick="selectAll($('<%=id%>_list_form').eLinkID,this.checked)"/></th>
		<th style="height:30px;width:50px">링크ID</th>
		<th style="height:30px;width:300px;">링크명 </th>		
		<th style="height:30px;width:370px;">링크URL</th>		
		<th style="height:30px;width:100px;">링크타입</th>
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
	</form>
</div>


<script type="text/javascript">

/***********************************************/
/* 대량메일작성으로 돌아가기 
/***********************************************/
$('<%=id%>').getBack=function(){
	nemoWindow(
			{
				'id': '<%=id%>_modal',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 900,
				height: $('mainColumn').style.height.toInt(),
				//height: 600,
				title: '등록/수정',
				
				loadMethod: 'xhr',
				contentURL: 'massmail/write/massmail.do?id=<%=id%>_modal&method=edit&massmailID=<%=massmailID%>&scheduleID=1&state=00'
			}
		);
	closeWindow( $('<%=id%>') );
}


/***********************************************/
/* 상태변경
/***********************************************/
$('<%=id%>').saveData = function () {
	var frm = $('<%=id%>_list_form');
	nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

				, url: 'massmail/write/massmail.do?method=updateAllScheduleState&id=<%=id%>&eMassmailID=<%=massmailID%>&eState=<%=state%>'
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {
					MochaUI.massmailListWindow();
					closeWindow( $('<%=id%>') );
				}
			});
	nemoRequest.post(frm);
}



/***********************************************/
/* 링크삭제 
/***********************************************/
$('<%=id%>').deleteLink = function () {

	var frm = $('<%=id%>_list_form');
	var checked = isChecked( frm.elements['eLinkID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;



	//copyForm( $('<%=id%>_rform'), frm );

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'massmail/write/massmail.do?method=deleteLink&id=<%=id%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

		}
	});
	nemoRequest.post(frm);
}


/***********************************************/
/* 리스트 
/***********************************************/
$('<%=id%>').list = function () {
	

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'massmail/write/massmail.do?method=listMassMailLink&id=<%=id%>&eMassmailID=<%=massmailID%>&eState=<%=state%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post();
}


$('<%=id%>').init = function() {

	$('<%=id%>').list();
};

window.addEvent('domready', function(){
	$('<%=id%>').init();
});



</script>

<%
} 
//****************************************************************************************************/
//메일링크리스트 
//****************************************************************************************************/
if(method.equals("listMassMailLink")) {

%>
	
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	
	
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->

	
	<c:forEach items="${massmailLinkList}" var="massmailLink">
	<TR class="tbl_tr" massmailLinkList_id="<c:out value="${massmailLink.linkID}"/>">
		<TD class="tbl_td"><input type="checkbox" id="eLinkID" name="eLinkID" value="<c:out value="${massmailLink.linkID}" />" /></TD>
		<TD class="tbl_td"><c:out value="${massmailLink.linkID}"/></TD>		
		<TD class="tbl_td" align="center">
		<c:if test="${massmailLink.linkDesc=='image'}">
			<a href="javascript:$('<%=id%>').viewImage('<c:out value="${massmailLink.linkName}"/>')"><img src='<c:out value="${massmailLink.linkName}"/>' height="70"></a>
		</c:if>
		<c:if test="${massmailLink.linkDesc=='text'}">
			<c:out value="${massmailLink.linkName}"/>
		</c:if>
		</TD>
		<TD class="tbl_td"><a href='<c:out value="${massmailLink.linkURL}"/>' target="_blank"><c:out value="${massmailLink.linkURL}"/></a></TD>
		<TD class="tbl_td">
		<!--<ul id="eLinkType"  class="selectBox" onchange="javascript:$('<%=id%>').changeLinkType(<c:out value="${massmailLink.linkID}"/>,this.value)">
			<li data="1" <c:if test="${massmailLink.linkType=='1'}">select='Y'</c:if>>일반링크</li>
			<li data="2" <c:if test="${massmailLink.linkType=='2'}">select='Y'</c:if>>수신거부</li>
		</ul>-->
		<select  id="eLinkType" onchange="javascript:$('<%=id%>').changeLinkType(<c:out value="${massmailLink.linkID}"/>,this.value)">
			<option  value="1" <c:if test="${massmailLink.linkType=='1'}">selected</c:if>>일반링크</option>
			<option  value="2" <c:if test="${massmailLink.linkType=='2'}">selected</c:if>>수신거부</option>
		</select>	
		</TD>
	</TR>
	</c:forEach>
	
	<script type="text/javascript">


		//링크타입 변경
		$('<%=id%>').changeLinkType = function (eLinkID, eLinkType) {			
			var frm = $('<%=id%>_list_form');
			nemoRequest.init( 
			{
					//busyWindowId: '<%=id%>'  // busy 를 표시할 window
					//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

					 url: 'massmail/write/massmail.do?method=updateMassMailLinkType&id=<%=id%>&linkID='+eLinkID+'&linkType='+eLinkType
					, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
					, onSuccess: function(html,els,resHTML) {
						//refreshWindow('<%=id%>');
					}
			});
			nemoRequest.post(frm);
		};

		//링크분석 이미지 미리보기
		$('<%=id%>').viewImage = function(webURL) {
			nemoWindow(
					{
						'id': '<%=id%>_url_modal',

						busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

						width: 500,
						height: $('mainColumn').style.height.toInt(),
						title: '이미지 미리보기 ',
						type: 'modal',
						loadMethod: 'iframe',
						contentURL: webURL
					}
				);
			
		}	 	

		window.addEvent('domready', function(){

			// 셀렉트 박스 렌더링
			makeSelectBox.render($('<%=id%>_list_form"'));
			
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				//,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			
		});

	</script>

<%} %>
