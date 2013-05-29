<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String preID = request.getParameter("preID");
	String id = request.getParameter("id");
	String method = request.getParameter("method");


	if(method.equals("sender")) {
%>
		<form id="<%=id%>_sform" name="<%=id%>_sform">
		<div class="search_wrapper" style="width:280px">
			<div style="float:right">
			<table border="0" cellpadding="3">
				<tbody>
				<tr>
					<td aling="right">
						<a href="javascript:closeWindow($('<%=id%>'))" class="web20button bigpink">닫기</a>
					</td>			
				</tr>
				</tbody>
			</table>
			</div>	
		</div>
		</form>


		<div style="clear:both;width:280px">
			<form name="<%=id%>_list_form" id="<%=id%>_list_form">
			<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="280px" >
			<thead>
				<tr>
				<th style="height:30px">보내는 사람명</th>
				<th style="height:30px">보내는  이메일</th>
				</tr>
			</thead>
			<tbody id="<%=id%>_grid_content">
				
			</tbody>
			</table>
			</form>
		</div>
		<script type="text/javascript">


		

		/***********************************************/
		/* 보내는 메일  입력 
		/***********************************************/
		$('<%=id%>').inputSenderEmail = function(senderName, senderEmail){

			
			$('<%=preID%>_form').eSenderName.value = senderName;	
			$('<%=preID%>_form').eSenderMail.value = senderEmail;		
			closeWindow($('<%=id%>'));
		}

		/***********************************************/
		/* 보내는 메일 리스트 
		/***********************************************/

		$('<%=id%>').list = function () {	

			var frm = $('<%=id%>_sform');

			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>',  // busy 를 표시할 window
				updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

				url: 'massmail/write/massmail.do?method=senderList&id=<%=id%>', 
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
if(method.equals("senderList")) {
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
			
			
			<c:forEach items="${senderEmailList}" var="sender">
			<TR class="tbl_tr" onclick="javascript:$('<%=id%>').inputSenderEmail('<c:out value="${sender.senderName}"/>','<c:out value="${sender.senderEmail}"/>')">						
				<TD class="tbl_td"><c:out value="${sender.senderName}"/></TD>	
				<TD class="tbl_td"><c:out value="${sender.senderEmail}"/></TD>	
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