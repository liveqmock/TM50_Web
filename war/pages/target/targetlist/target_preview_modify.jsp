<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.target.targetlist.control.*"%>
<%@ page import="web.target.targetlist.service.*"%>
<%@ page import="web.target.targetlist.model.*"%>
<%@ page import="web.common.util.*" %>    
<%@ page import="java.util.*" %>   

<%
	String id = request.getParameter("id");
	String preID = request.getParameter("preID");
	String method = request.getParameter("method");
	String targetID = request.getParameter("targetID");
	String tableName = request.getParameter("tableName");
	TargetListService service = TargetListControlHelper.getUserService(application);
	List<OneToOne> onetooneTargetList = service.listAddOneToOne(new Integer(targetID));
	
if(method.equals("view")) {
%>
<div class="search_wrapper" style="width:100%">
	<div class="right">
		<a href="javascript:closeWindow( $('<%=id%>'))" class="web20button bigpink">닫기</a>
	</div>
	<div class="right">
			<a href="javascript:$('<%=id%>').edit()" class="web20button bigblue">저장</a>
	</div>

</div>
<br><br>
	<c:set var="columns" value="<%=onetooneTargetList%>"/>
	<div style="clear:both;width:635px;">
	<form name="<%=id%>_editlist_form" id="<%=id%>_editlist_form" method="post">
	
	<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="width:670px" >
	<thead>
		<tr>
		<c:forEach items="${columns}" var="column" >
			<th style="height:30px;width:<%=655 / onetooneTargetList.size() %>px"><c:out value="${column.fieldDesc}" /></th>
		</c:forEach>
		</tr>
	</thead>
	<tbody id="<%=id%>_edit_grid_content">
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

		url: 'target/targetlist/target.do?method=previewEditList&id=<%=id%>&targetID=<%=targetID%>&tableName=<%=tableName%>', 
		update: $('<%=id%>_edit_grid_content'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post($('<%=preID%>_previewlist_form'));
}

/***********************************************/
/* 저장
/***********************************************/
$('<%=id%>').edit = function() {


	var frm = $('<%=id%>_editlist_form');
	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'target/targetlist/target.do?method=editPreview&id=<%=id%>&tableName=<%=tableName%>&targetID=<%=targetID%>'
		, update: $('<%=preID%>_preview_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			
			$('<%=preID%>_previewlist_form').sCheckAll.checked = false;
			$('<%=preID%>').allList();
			closeWindow( $('<%=id%>'));
			

		}
	});
	nemoRequest.post(frm);
	
	
	
}

/* 리스트 표시 */
window.addEvent("domready",function () {	
	$('<%=id%>').list(); 
});

</script>
 
<%}
	if(method.equals("list")) {
%>
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	
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
			
	%>
	
	<TR class="tbl_tr" >
		<input type="hidden" id="eTargetPreviewID" name="eTargetPreviewID" value="<%=entity[0]%>" />	
    <%
    
    		for(int i= 1; i < entity.length;i++)
			{
    			if( entity[i] != null && !entity[i].equals(""))
    			{
    				%>
    				<TD class="tbl_td" style="width:60px" ><input type="text" id="e_<%=onetooneTargetList.get(i-1).getFieldName()%>_<%=entity[0]%>" name="e_<%=onetooneTargetList.get(i-1).getFieldName()%>_<%=entity[0]%>" value="<%=entity[i] %>"></TD>
    				<%
    			}
				else 
				{  %>
				 	<td class="tbl_td"><input type="text" id="e_<%=onetooneTargetList.get(i-1).getFieldName()%>_<%=entity[0]%>" name="e_<%=onetooneTargetList.get(i-1).getFieldName()%>_<%=entity[0]%>" value=""></td>
				 	<%
				}
    			
			}
	%>
	</TR>
	
	<%
        }
		
	%>
			
	<script type="text/javascript">

		window.addEvent('domready', function(){

			
					
			// 테이블 렌더링
			$('<%=id%>').edit_grid_content = new renderTable({
				element: $('<%=id%>_edit_grid_content') // 렌더링할 대상
				,cursor: 'default' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				
			});
			$('<%=id%>').edit_grid_content.render();
			
		});

	</script>


<%} %>

