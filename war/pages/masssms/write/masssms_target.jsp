<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String id = request.getParameter("id");
	String preID = request.getParameter("preID");
	String method = request.getParameter("method");


//****************************************************************************************************/
//대상차 팝업  
//****************************************************************************************************/
if(method.equals("targeting")) {
%>


<form id="<%=id%>_sform" name="<%=id%>_sform">
<div class="search_wrapper">
	<div style="float:left">
	<table border="0" cellpadding="3">
		<tbody>
		<tr>
			<td>
				<div style="float:left;margin-top:2px">		
					<ul id="sBookMark"  class="selectBox" onChange="javascript:$('<%=id%>').list()">
						<li data="">전체보기</li>			
						<li data="Y">즐겨찾기(Y)</li>
						<li data="N">즐겨찾기(N)</li>
					</ul>
				</div>
			</td>
			<td>
				<ul id="sSearchType"  class="selectBox">
					<li data="t.targetName" select="yes">대상자그룹명</li>
					<li data="u.userName">등록자명</li>
				</ul>
			</td>
			<td>
				<input type="text" id="sSearchText" name="sSearchText" size="15"/>
			</td>
			<td>
				<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
			</td>
		</tr>
		</tbody>
	</table>
	</div>
	<div style="float:right">
	<table border="0" cellpadding="3">
		<tbody>
		<tr>
			<td>
				<a href="javascript:$('<%=id%>').insertTargetRow()" class="web20button bigblue">선택추가</a>
			</td>
			<td>
				<a href="javascript:closeWindow($('<%=id%>_modal'))" class="web20button bigpink">닫기</a>
			</td>	
			<td>
				<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체 목록</a>
			</td>		
		</tr>
		</tbody>
	</table>
	</div>
	
</div>

</form>

<div style="clear:both;width:620px">
	<form name="<%=id%>_list_form" id="<%=id%>_list_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="620px" >
	<thead>
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" name="sCheckAll" type="checkbox" class="notBorder"  onclick="selectAll($('<%=id%>_list_form').eTargetID,this.checked)"/></th>
		<th style="height:30px;width:50px">그룹ID</th>
		<th style="height:30px">대상자그룹명</th>
		<th style="height:30px;width:100px">등록구분</th>
		<th style="height:30px;width:80px">총대상인원</th>
		<th style="height:30px;width:100px">작성자</th>
		</tr>
	</thead>
	<tbody id="<%=id%>_grid_content">
	
	</tbody>
	</table>
	</form>
</div>
    
<div id="<%=id%>_paging" class="page_wrapper"></div>

<script type="text/javascript">

/***********************************************/
/* 전체목록
/***********************************************/
$('<%=id%>').allList = function(){
	initFormValue($('<%=id%>_sform'));
	$('<%=id%>').list ();
}

/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_sform');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );

	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );


	$('<%=id%>').list();

}

/***********************************************/
/* 대상자그룹 추가 
/***********************************************/
$('<%=id%>').insertTargetRow = function () {

	var checkedObj = $('<%=id%>_grid_content').getElements('input[type=checkbox]');
	var gridBody = $('<%=id%>_grid_content_list');	
	  
	if(checkedObj == null || checkedObj.length == 0) {
		alert('추가할 대상자를 먼저 체크하세요');
		return;
	}

	
	var findId =  function(id) {
		var ids = $('<%=id%>_form').getElements('input[id=targetID]'); 

		for(var i=0; i<ids.length; i++ ) {
			if(ids[i].value == id) {
				return true;
			}
		}
		return false;
		
	}

	var checked = false;
	if(checkedObj.length > 2){
		$('<%=id%>_targetList').style.height = '150px';
	}

	for(var i =0;i < checkedObj.length; i ++ ) {

		if(checkedObj[i].checked) {
			checked = true;

			var tr = checkedObj[i].getParent('tr');

			if(findId(tr.get('targetList_id'))){
				alert('이미 등록되어 있습니다.');
				continue; // 동일한 아이디가 있으면 continue;
			}
					
			sourceTds = tr.getElements('td');
			
			var tr = new Element('tr',{
				'class': 'target_tr'
			});
			
			// 대상자 id
			var td = new Element('td',{
				//'text': sourceTds[1].get('text')+' '
			}).inject(tr);

			new Element('input',{
				'type':'checkbox',
				'id':'targetIDCheck',
				'class':'notBorder'
			}).inject(td);
			
			
			// 대상자 id hidden 
			new Element('input',{
				'type':'hidden',
				'id':'targetID',
				'name':'targetID',
				'value': sourceTds[1].get('text') 
			}).inject(td);

			

			// 대상자 그룹명
			new Element('td',{
				'text': sourceTds[2].get('text'),
				'align':'left'
			}).inject(tr);

			// 등록 타입
			new Element('td',{
				'text': sourceTds[3].get('text'),
				'align':'center'
			}).inject(tr);

			// 대상자 수
			new Element('td',{
				'text': sourceTds[4].get('text')
			}).inject(tr);

			// 작성자
			//new Element('td',{
			//	'text': sourceTds[5].get('text')
			//}).inject(tr);

			// 추가제외
			td = new Element('td').inject(tr);
			
			// 추가제외 체크박스
			var select = new Element('select',{
				'id' : 'exceptYN',
				'name' : 'exceptYN'
			}).inject(td);

			new Element('option',{'value':'N','text':'추가'}).inject(select);
			new Element('option',{'value':'Y','text':'제외'}).inject(select);
			

			tr.inject(gridBody);

		} 
		
		
	} 
	
	if(!checked) {
		alert('추가할 대상자를 먼저 체크하세요');
		return;
	}

	closeWindow($('<%=id%>_modal'))
	
}	
/***********************************************/
/* 대상자검색리스트 
/***********************************************/

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
		busyWindowId: '<%=id%>_modal',  // busy 를 표시할 window
		updateWindowId: '<%=id%>_modal',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'masssms/write/masssms.do?method=targetList&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_rform'));
}

/* 리스트 표시 */
window.addEvent("domready",function () {	
	$('<%=id%>').init();
	//$('<%=id%>').createPopup();
	$('<%=id%>').list(); 
});

</script>


<%
}
//****************************************************************************************************/
// 타겟리스트 
//****************************************************************************************************/
if(method.equals("targetList")) {
%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
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
	
	
	<c:forEach items="${targetingList}" var="targeting">
	<TR class="tbl_tr" targetList_id="<c:out value="${targeting.targetID}"/>">
		<TD class="tbl_td" align="center"><input type="checkbox" class="notBorder" id="eTargetID" name="eTargetID" value="<c:out value="${targeting.targetID}" />" /></TD>		
		<TD class="tbl_td" align="center"><c:out value="${targeting.targetID}" escapeXml="true"/></TD>		
		<TD class="tbl_td"><c:out value="${targeting.targetName}"/></TD>	
		<TD class="tbl_td" align="center">
			<c:if test="${targeting.targetType=='1'}">
				파일업로드	
			</c:if>
			<c:if test="${targeting.targetType=='2'}">		
				직접입력	
			</c:if>
			<c:if test="${targeting.targetType=='3'}">		
				DB추출	
			</c:if>
			<c:if test="${targeting.targetType=='4'}">		
				기존발송추출	
			</c:if>
		</TD>
		<TD class="tbl_td"><fmt:formatNumber value="${targeting.targetCount}" type="number"/></TD>	
		<TD class="tbl_td"><c:out value="${targeting.userName}"/></TD>
	</TR>
	</c:forEach>
	
	<script type="text/javascript">

		window.addEvent('domready', function(){

		
			// 페이징 표시
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				,url: 'pages/common/pagingStr.jsp'
				,update: $('<%=id%>_paging') // 완료후 content가 랜더링될 element
			});
			nemoRequest.get({
				'id':'<%=id%>', 
				'curPage': '<%=curPage%>', 
				'iLineCnt': '<%=iLineCnt%>', 
				'iTotalCnt': '<%=iTotalCnt%>', 
				'iPageCnt': '10' 
			});
			
		
			// 테이블 렌더링
			$('<%=id%>').grid_content = new renderTable({
				element: $('<%=id%>_grid_content') // 렌더링할 대상
				,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			
		});

	</script>
<%
}
%>