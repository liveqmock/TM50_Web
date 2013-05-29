<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String preID = request.getParameter("preID");
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	String targetIDs = request.getParameter("targetIDs");
	String textName = request.getParameter("textName");

	if(method.equals("onetoone")) {
%>
		<div style="clear:both;width:300px">
			<form name="<%=id%>_list_form" id="<%=id%>_list_form">
			<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="300px" >
			<thead>
				<tr>
				<th style="height:30px;width:150px">일대일치환명</th>
				<th style="height:30px;width:150px">일대일치환변수</th>				
				</tr>
			</thead>
			<tbody id="<%=id%>_grid_content">
				
			</tbody>
			</table>
			</form>
		</div><hr />
		<div style="clear:both;">
			<img src="images/tag_blue.png"> 여러개의 대상자그룹을 선택할  경우 공통으로 설정된 <br /> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 원투원 값만 사용이 가능합니다.
		</div>	
		<script type="text/javascript">

		/***********************************************/
		/* 선택 원투원 치환
		/***********************************************/
		$('<%=id%>').selectOnetoone = function(val){
			//메일본문이라면 	
			<% if(textName.equals("eMailContent")){%>
				$('<%=preID%>_ifrmFckEditor').contentWindow.insertFCKHtml(val);			
			<%}else{%>
				$('<%=preID%>_form').<%=textName%>.value = $('<%=preID%>_form').<%=textName%>.value + val;
				$('<%=preID%>_form').<%=textName%>.focus();		
			<%}%>

			closeWindow($('<%=id%>'))
		}
		


		/***********************************************/
		/* 원투원리스트 
		/***********************************************/

		$('<%=id%>').list = function () {	

		
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>',  // busy 를 표시할 window
				updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

				url: 'massmail/write/massmail.do?method=listOnetoone&id=<%=id%>&targetIDs=<%=targetIDs%>', 
				update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
				
				onSuccess: function(html,els,resHTML,scripts) {
					
					//MochaUI.arrangeCascade();
				}
			});
			nemoRequest.post();
		}



		/* 리스트 표시 */
		window.addEvent("domready",function () {	
			$('<%=id%>').list(); 
		});
		</script>

<%
}
if(method.equals("listOnetoone")) {
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
			
			
			<c:forEach items="${onetooneList}" var="onetooneTarget">
			<TR class="tbl_tr" testList_id="<c:out value="${onetooneTarget.onetooneAlias}"/>" onclick="javascript:$('<%=id%>').selectOnetoone('<c:out value="${onetooneTarget.onetooneAlias}"/>')" >
				<TD class="tbl_td"><c:out value="${onetooneTarget.onetooneName}"/></TD>		
				<TD class="tbl_td"><c:out value="${onetooneTarget.onetooneAlias}"/></TD>		
			</TR>
			</c:forEach>
			
			<script type="text/javascript">

				window.addEvent('domready', function(){
					
				
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