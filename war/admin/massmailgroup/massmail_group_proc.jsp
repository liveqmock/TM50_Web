<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="web.common.util.JSUtil"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

String id = request.getParameter("id");
String method = request.getParameter("method");

//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("list")) {
%>
	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<!-- 메시지 가있으면 메시지를 뿌려준다 -->
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
 	<c:forEach items="${massmailgroupList}" var="massMailGroup">
 		<TR class="tbl_tr" massmailgroup_id="<c:out value="${massMailGroup.massMailGroupID}"/>" >
 			<c:if test="${massMailGroup.massMailGroupID=='1'}"> 
 			<TD class="tbl_td" align="center"><input type="checkbox" class="notBorder" id="eMassMailGroupID_default" name="eMassMailGroupID_default" value="<c:out value="${massMailGroup.massMailGroupID}" />" disabled='YES'></TD>
 			</c:if>
 			<c:if test="${massMailGroup.massMailGroupID!='1'}"> 
 			<TD class="tbl_td" align="center"><input type="checkbox" class="notBorder" id="eMassMailGroupID" name="eMassMailGroupID" value="<c:out value="${massMailGroup.massMailGroupID}" />" ></TD>
 			</c:if>	
			<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow(<c:out value="${massMailGroup.massMailGroupID}"/>)"><c:out value="${massMailGroup.massMailGroupName}"/></a></b></TD>	
			<TD class="tbl_td" align="left"><c:out value="${massMailGroup.description}" escapeXml="true"/></TD>	
			<TD class="tbl_td"><c:out value="${massMailGroup.useYN}"/></TD>	
			<TD class="tbl_td"><c:out value="${massMailGroup.isDefault}"/></TD>	
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
				//,cursor: 'pointer' // 커서
				,focus: true  // 마우스 이동시 포커스 여부
				,select: true // 마우스 클릭시 셀렉트 표시 여부
				,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
			});
			$('<%=id%>').grid_content.render();
			closeWindow( $('<%=id%>_modal') );
		});

	</script>
	
<%
}
//****************************************************************************************************/
//  편집 
//****************************************************************************************************/
if(method.equals("edit")) {
%>

	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">

			<input type="hidden" id="eMassMailGroupID" name="eMassMailGroupID" value="<c:out value="${massmailgroup.massMailGroupID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="80px">그룹명 </td>
				<td class="ctbl td"><input type="text" id="eMassMailGroupName" name="eMassMailGroupName" value="<c:out value="${massmailgroup.massMailGroupName}"/>" size="50" mustInput="Y" msg="그룹명 입력"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl ttd1" width="80px">그룹 설명</td>
				<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${massmailgroup.description}"/>" size="50"/></td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>
				<td class="ctbl ttd1" width="80px">사용 여부</td>
				<td class="ctbl td">
					<c:if test="${massmailgroup.massMailGroupID == 1  }" > 
						<c:out value="${massmailgroup.useYN}"/>
						<input type="hidden" id="eUseYN" name="eUseYN" value="Y" />
					</c:if>
					<c:if test="${massmailgroup.massMailGroupID != 1  }" > 
						<ul id="eUseYN"  class="selectBox"  />
							<li data="Y" <c:if test="${massmailgroup.useYN=='Y'}">select='YES'</c:if>>Y</li>
							<li data="N" <c:if test="${massmailgroup.useYN=='N'}">select='YES'</c:if>>N</li>
						</ul>
					</c:if>
				</td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>
				<td class="ctbl ttd1" width="80px">디폴트 여부</td>
				<td class="ctbl td">
					<ul id="eIsDefault"  class="selectBox">
						<li data="Y" <c:if test="${massmailgroup.isDefault=='Y'}">select='YES'</c:if>>Y</li>
						<li data="N" <c:if test="${massmailgroup.isDefault=='N'}">select='YES'</c:if>>N</li>
					</ul>
				</td>
			</tr>				
			</tbody>
			</table>
		</form>
	</div>
	

	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<c:if test="${massmailgroup.massMailGroupID > 1  }" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${massmailgroup.massMailGroupID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').saveData(<c:out value="${massmailgroup.massMailGroupID}"/>)" class="web20button bigblue">저 장</a></div>

	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
	</script>

<%
}
%>
		