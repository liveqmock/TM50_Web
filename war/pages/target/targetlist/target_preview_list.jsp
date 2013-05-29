<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.List"%>   
<%@ page import="java.util.*"%>   
<%@ page import="web.admin.dbjdbcset.model.DbJdbcSet"%>   
<%@ page import="web.admin.dbjdbcset.service.DbJdbcSetService"%>    
<%@ page import="web.admin.dbjdbcset.control.DbJdbcSetControlHelper"%>    
<%@ page import="web.target.targetlist.control.TargetListControlHelper"%>
<%@ page import="web.target.targetlist.service.TargetListService"%>
<%@ page import="web.target.targetlist.model.*"%>
<%@ page import="web.common.util.ServletUtil" %>
<%@ page import="web.common.util.StringUtil" %>
<%@ page import="web.common.util.Constant" %>


<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%
	String id = request.getParameter("id");
	String preid = "target";
	String method = request.getParameter("method");
	
	String targetID = request.getParameter("targetID");
	
	String sSearchType = request.getParameter("sSearchType");
	String sSearchText = request.getParameter("sSearchText");
	
	TargetListService service = TargetListControlHelper.getUserService(application);
	TargetList targetlist = service.viewTargetList(Integer.parseInt(targetID));
	
	String query = targetlist.getQueryText(); 
	
	query = query.replaceAll("\n"," ");
	query = query.replaceAll("\t"," "); 
	String dbId = targetlist.getDbID();
	
	List<OnetooneTarget> onetooneTargetList = null;
	onetooneTargetList = service.viewOnetooneTarget( Integer.parseInt(targetID) );
	
	
%>

<%
//****************************************************************************************************/
//대상차 팝업  
//****************************************************************************************************/
if(method.equals("preview")) {
	
%>
<c:set var="columns" value="<%=onetooneTargetList%>"/>
<form id="<%=id%>_preview_sform" name="<%=id%>_preview_sform">

<div class="search_wrapper" style="width:670px">
	<div class="start">
		<ul id="sSearchType"  class="selectBox">
			<c:forEach items="${columns}" var="column" >
				<li data="<c:out value="${column.fieldName}"/>" ><c:out value="${column.fieldDesc}" /></li>
			</c:forEach>
		</ul>
	</div>
	<div >
		<input type="text" id="sSearchText" name="sSearchText" size="15"/>
	</div>
	<div>
		<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
	</div>
	<div class="text" style="float:right">
		[ 대상인원 : <span id="<%=id%>_totalCount"></span>명 ]
	</div>
	
</div>
<br><br>
<div class="search_wrapper" style="width:670px">
	<div class="right">
		<a href="javascript:closeWindow( $('<%=id%>'))" class="web20button bigpink">닫기</a>
	</div>
	<div class="right">
		<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>
	</div>
	<div class="right">
			<a href="javascript:$('<%=id%>').personPreviewDown()" class="web20button bigblue">다운로드</a>
	</div>
	<%if(targetlist.getTargetType().equals(Constant.TARGET_TYPE_FILE)||targetlist.getTargetType().equals(Constant.TARGET_TYPE_DIRECT))
	{
	%>
	<div class="right">
			<a href="javascript:$('<%=id%>').addWindow('<%=targetID %>','<%=targetlist.getTargetTable() %>')" class="web20button bigblue">추가</a>
	</div>
	<div class="right">
			<a href="javascript:$('<%=id%>').deleteList()" class="web20button bigblue">체크 삭제</a>
	</div>
	<div class="right">
			<a href="javascript:$('<%=id%>').modifyList()" class="web20button bigblue">체크 수정</a>
	</div>
	<%} %>	
</div>
</form>

<div style="clear:both;width:670px;">
	<form name="<%=id%>_previewlist_form" id="<%=id%>_previewlist_form">
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="width:670px" >
	<thead>
		<tr>
		<th style="height:30px;width:50px"><input id="sCheckAll" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_previewlist_form').eTargetPreviewID,this.checked)"/></th>
		<c:forEach items="${columns}" var="column" >
			<th style="height:30px;width:<%=620 / onetooneTargetList.size() %>px"><c:out value="${column.fieldDesc}" /></th>
		</c:forEach>
		</tr>
	</thead>
	<tbody id="<%=id%>_preview_grid_content">
	</tbody>
	</table>
	</form>
</div>
    
<div id="<%=id%>_preview_paging" class="page_wrapper">
</div>

<script type="text/javascript">


/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_preview_sform');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );
	
	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );
}
/***********************************************/
/* 전체목록
/***********************************************/
$('<%=id%>').allList = function(){
	var frm = $('<%=id%>_preview_sform');
	initFormValue(frm);
	$('<%=id%>').list(); 
}

//************************************************************
//대상자 다운
//************************************************************/
$('<%=id%>').personPreviewDown = function() {

	location.href = "pages/target/targetlist/target_preview_list_down.jsp?targetID=<%=targetID%>";
}
/***********************************************/
/* 대상자검색리스트 
/***********************************************/

$('<%=id%>').list = function ( forPage ) {	

	var frm = $('<%=id%>_preview_sform');

	//페이징 클릭에서 리스트 표시는 기존 검색을 따른다
	if(!forPage) {
		//검색 조건 체크
		
		// 검색 값을 새로운 폼값(검색후 픽스될) 에 담는다.
		cloneForm($('<%=id%>_preview_sform'), '<%=id%>_preview_rform', '<%=id%>',   $('<%=id%>_preview_grid_content'));
	}
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'target/targetlist/target.do?method=previewList&id=<%=id%>&targetID=<%=targetID%>', 
		update: $('<%=id%>_preview_grid_content'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=id%>_preview_rform'));
}


/***********************************************/
/* 추가/제외창 열기 
/***********************************************/
$('<%=id%>').addWindow = function( seq , seq1) {
	
	nemoWindow(
		{
			'id': '<%=id%>_add',

			busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

			width: 680,
			//height: $('mainColumn').style.height.toInt(),
			height: 310,
			title: '대상자추가/제외',
			type: 'modal',
			loadMethod: 'xhr',
			contentURL: 'pages/target/targetlist/targetlist_add.jsp?id=<%=id%>_add&preID=<%=id%>&method=editAddType&targetID='+seq+'&targetTable='+seq1
		}
	);
	
}
/***********************************************/
/* 삭제
/***********************************************/
$('<%=id%>').deleteList = function() {


	var frm = $('<%=id%>_previewlist_form');
	var checked = isChecked( frm.elements['eTargetPreviewID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'해당 자료를 먼저 선택하세요');
		return;
	}
	if(!confirm("선택하신 "+checked+"개의 이메일을 삭제하시겠습니까?")) return;
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'target/targetlist/target.do?method=deletePreviewList&id=<%=id%>&tableName=<%=targetlist.getTargetTable()%>&targetID=<%=targetID%>'
		, update: $('<%=id%>_preview_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

			$('<%=id%>_previewlist_form').sCheckAll.checked = false;
			$('<%=id%>').allList();
			$('target').allList();

		}
	});
	nemoRequest.post(frm);
	
	
	
}

/***********************************************/
/* 수정
/***********************************************/
$('<%=id%>').modifyList = function() {


	var frm = $('<%=id%>_previewlist_form');
	var checked = isChecked( frm.elements['eTargetPreviewID']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'해당 자료를 먼저 선택하세요');
		return;
	}
	
	nemoWindow(
			{
				'id': '<%=id%>_modify',

				busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element

				width: 680,
				height: $('mainColumn').style.height.toInt()-100,
				//height: 310,
				title: '대상자 수정',
				type: 'modal',
				loadMethod: 'xhr',
				contentURL: 'pages/target/targetlist/target_preview_modify.jsp?method=view&id=<%=id%>_modify&preID=<%=id%>&tableName=<%=targetlist.getTargetTable()%>&targetID=<%=targetID%>'
			}
		);
	
}


/* 리스트 표시 */
window.addEvent("domready",function () {	
	$('<%=id%>').init();
	$('<%=id%>').list(); 
});

</script>


<%
}
//****************************************************************************************************/
// 타겟리스트 
//****************************************************************************************************/
if(method.equals("previewList")) {

%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	
	<%
		ArrayList list = (ArrayList)request.getAttribute("targetPreviewList");
		Iterator it = list.iterator();
		
		String[] entity ;
		while( it.hasNext())
        {
			entity = (String[])it.next();
			if(targetlist.getTargetType().equals(Constant.TARGET_TYPE_FILE)||targetlist.getTargetType().equals(Constant.TARGET_TYPE_DIRECT))
			{
	%>
	
	<TR class="tbl_tr" >
		<TD class="tbl_td"><input type="checkbox" id="eTargetPreviewID" name="eTargetPreviewID" value="<%=entity[0]%>" /></TD>	
    <%
    		
    		for(int i= 1; i < entity.length;i++)
			{
    			if( entity[i] != null && !entity[i].equals(""))
    			{
    				%>
    				<TD class="tbl_td" <%if(i==1){%> align="left" <%}%> style="width:60px"><%=entity[i]  %></TD>
    				<%
    			}
				else 
				{  %>
				 	<td class="tbl_td">&nbsp;</td>
				 	<%
				}
    			
			}
	%>
	</TR>
	<%
			}
			else
			{
	%>
	<TR class="tbl_tr" >
		<TD class="tbl_td"><input type="checkbox" id="eTargetPreviewID" name="eTargetPreviewID" value="" /></TD>	
    <%
    		
    		for(int i= 0; i < entity.length;i++)
			{
    			if( entity[i] != null && !entity[i].equals(""))
    			{
    				%>
    				<TD class="tbl_td" <%if(i==0){%> align="left" <%}%> style="width:60px"><%=entity[i]  %></TD>
    				<%
    			}
				else 
				{  %>
				 	<td class="tbl_td">&nbsp;</td>
				 	<%
				}
    			
			}
	%>
	</TR>
	
	<%
			}
        }
		
	%>
			
	<script type="text/javascript">

		window.addEvent('domready', function(){

		
			// 페이징 표시
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>'  // busy 를 표시할 window
				,url: 'pages/common/pagingStr.jsp'
				,update: $('<%=id%>_preview_paging') // 완료후 content가 랜더링될 element
			});
			nemoRequest.get({
				'id':'<%=id%>', 
				'curPage': '<%=curPage%>', 
				'iLineCnt': '<%=iLineCnt%>', 
				'iTotalCnt': '<%=iTotalCnt%>', 
				'iPageCnt': '10' 
			});
			
		
			// 테이블 렌더링
			$('<%=id%>').preview_grid_content = new renderTable({
				element: $('<%=id%>_preview_grid_content') // 렌더링할 대상
				,cursor: 'default' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').preview_grid_content.render();
			$('<%=id%>_totalCount').innerHTML = '<%=StringUtil.formatPrice(new Integer(iTotalCnt))%>';
		});

	</script>
<%

}
%>