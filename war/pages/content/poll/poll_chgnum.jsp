<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="java.util.*"%>

<%
	String id = request.getParameter("id");
	String pollID = request.getParameter("pollID");
	String oldQuestionNo = request.getParameter("oldQuestionNo");
	String oldQuestionID = request.getParameter("eQuestionID");
%>

		
		
		
		<form name="<%=id%>_chgnum_form" id="<%=id%>_chgnum_form">
		<input type="hidden" id="ePollID" name="ePollID" value="<%=pollID %>">	
		<input type="hidden" id="eOldQuestionNo" name="eOldQuestionNo" value="<%=oldQuestionNo %>">	
		<input type="hidden" id="eOldQuestionID" name="eOldQuestionID" value="<%=oldQuestionID %>">
	
			<table class="ctbl" style="width:400px">
			<tbody>
				<tr>
					<td class="ctbl ttd1" width="100px">번호변경 </td>
					<td class="ctbl td">					
					<div style="float:left;padding-top:1px">
					설문 <%= oldQuestionNo%>번을&nbsp;&nbsp;
					<jsp:include page="./poll_question_number_inc2.jsp" flush="true">						
							<jsp:param name="pollID" value="<%=pollID %>" />
					</jsp:include>번(으로)
					</div>
					<div style="float:left;padding-left:10px">
					<select id="eChgNumType" name="eChgNumType">
						<option value="replace">교체</option>						
						<option value="movenext">뒤로 이동</option>
						
					</select>
					</div>
					</td>
				</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			</tbody>
			</table>
		</form>
		<br>
	<div style="width:400px">
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_chgnum'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>_chgnum').saveData()" class="web20button bigpink">저 장</a></div>
	</div>
			


		
		
		<script type="text/javascript">

		//저장 클릭 
		$('<%=id%>_chgnum').saveData = function(){
			var frm = $('<%=id%>_chgnum_form');
			var oldnum = frm.eOldQuestionNo.value;
			var newnum = frm.eNewQuestionNo.value;			
			var str = frm.eChgNumType.options[frm.eChgNumType.selectedIndex].text;
			var selval = frm.eChgNumType.value;			

			if(oldnum == newnum){
				alert('서로 같은 번호입니다.');
				return;
			}

			else if(oldnum == 1 && newnum == 0){
				alert('이미 1번이 맨 앞에 있습니다.');
				return;						
			}

			else if(newnum == 0 && selval == 'replace'){
				alert('0번으로 교체할 수 없습니다.');
				return;						
			}

			if(!confirm('['+str+'] 작업 : 번호 [ '+oldnum +' ] -> [ '+newnum+' ]')){
				return;
			}

			
			nemoRequest.init( 
					{
						busyWindowId: '<%=id%>_chgnum'  // busy 를 표시할 window
						, updateWindowId: '<%=id%>_info' // 완료후 버튼,힌트 가 랜더링될 windowid
						, url: 'content/poll/poll.do?id=<%=id%>&ePollID=<%=pollID%>&method=updateQuestionNoHTML'
						//, update: MochaUI.Windows.instances.get('<%=id%>_question').contentEl // 해당 id 전체 재시작	
						, onSuccess: function(html,els,resHTML) {		
							$('<%=id%>_poll_frame').src = "pages/content/poll/poll_preview.jsp?id=<%=id %>&pollID=<%=pollID %>";
							closeWindow( $('<%=id%>_question') );								
							closeWindow( $('<%=id%>_chgnum') );
							
						}
					});

			nemoRequest.post($('<%=id%>_chgnum_form'));		

			
		}
		
	
		
		</script>