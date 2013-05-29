<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.*"%>

<%
	String id = request.getParameter("id");
	String pollID = request.getParameter("pollID");
	String columnName = request.getParameter("columnName");
	String isExistTitle = request.getParameter("isExistTitle");
	String isExistEnd = request.getParameter("isExistEnd");
%>

		
		
		
		<form name="<%=id%>_title_form" id="<%=id%>_title_form">
		<input type="hidden" id="ePollID" name="ePollID" value="<%=pollID %>">		
		<%if(columnName.equals("startTitle")){ %>
			<table class="ctbl"">
			<tbody>
				<tr>
					<td class="ctbl ttd1" width="100px">시작문구</td>
					<td class="ctbl td">
					<div style="float:right" >
					<% if(isExistTitle.equals("YES")){ %>
					<textarea id="eStartTitle" name="eStartTitle" style="width:400px;height:50px" >${pollInfo.startTitle}</textarea>
					<%}else{ %>
					<textarea id="eStartTitle" name="eStartTitle" style="width:400px;height:50px" >현재 설문에서 사용하는 설문템플릿에서는 시작문구가 존재하지 않으므로 입력할 수 없습니다.</textarea>
					<%} %>				
					</div>
					</td>
				</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			</tbody>
			</table>
		<%}else if(columnName.equals("endTitle")){ %>
			<table class="ctbl"">
			<tbody>
				<tr>
					<td class="ctbl ttd1" width="100px">종료문구</td>
					<td class="ctbl td">					
					<div style="float:right" >
					<% if(isExistEnd.equals("YES")){ %>
					<textarea id="eEndTitle" name="eEndTitle" style="width:400px;height:50px" >${pollInfo.endTitle}</textarea>
					<%}else{ %>
					<textarea id="eEndTitle" name="eEndTitle" style="width:400px;height:50px" >현재 설문에서 사용하는 설문템플릿에서는 종료문구가 존재하지 않으므로 입력할 수 없습니다.</textarea>
					<%} %>		
					</div>
					</td>
				</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			</tbody>
			</table>		
		<%} %>
		</form>
		<br>
	<div>
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_titles'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>_titles').saveData('<%=columnName %>')" class="web20button bigpink">저 장</a></div>
	</div>
			


		
		
		<script type="text/javascript">
		
		
		$('<%=id%>_titles').saveData = function(seq){
			var frm = $('<%=id%>_title_form');
			
			//필수입력 조건 체크
			if(!checkFormValue(frm)) {
				return;
			}

			// 시작문구, 종료문구의 < , script , /> 등 스크립트 사용 의심 문자 필터	
			if (seq == 'startTitle') {
				if (frm.eStartTitle.value.indexOf('<') >= 0 || frm.eStartTitle.value.indexOf('/>') >= 0 || frm.eStartTitle.value.indexOf('script') >= 0) {
					alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
					return;
				}
			} else if (seq == 'endTitle') {
				if (frm.eEndTitle.value.indexOf('<') >= 0 || frm.eEndTitle.value.indexOf('/>') >= 0 || frm.eEndTitle.value.indexOf('script') >= 0) {
					alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
					return;
				}
			}
			
			//copyForm( $('<%=id%>_rform'), frm );

			if(seq == 'startTitle') {
				<% if(isExistTitle.equals("NO")){ %>
					alert("시작문구를 입력할 수 없습니다");
					return;							
				<%}%>
				goUrl = 'content/poll/poll.do?id=<%=id%>&method=updateStartTitle';
			} else if(seq == 'endTitle'){
				<% if(isExistEnd.equals("NO")){ %>
				alert("종료문구를 입력할 수 없습니다");
				return;							
			<%}%>
				goUrl = 'content/poll/poll.do?id=<%=id%>&method=updateEndTitle';
			}
			
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_title'  // busy 를 표시할 window
				, updateWindowId: '<%=id%>_info' // 완료후 버튼,힌트 가 랜더링될 windowid
				, url: goUrl
				//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element		
				, onSuccess: function(html,els,resHTML) {
					$('<%=id%>_poll_frame').src = "pages/content/poll/poll_preview.jsp?id=<%=id %>&pollID=<%=pollID %>";				
					closeWindow( $('<%=id%>_title') );
					
				}
			});

			nemoRequest.post(frm);	
		}		
		</script>