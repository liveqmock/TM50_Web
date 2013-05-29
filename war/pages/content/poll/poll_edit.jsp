<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %> 
<%

String id = request.getParameter("id");
String pollID = request.getParameter("pollID");

%>	
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_edit_form" name="<%=id%>_form" method="post">
		<input type="hidden" id="eResultPollHTML" name="eResultPollHTML">		
		<input type="hidden" id="eResultPollHTMLYN" name="eResultPollHTMLYN">
			<input type="hidden" id="ePollID" name="ePollID" value="<%=pollID %>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl td" height="20px">
				<b><font color="red">[주의] HTML 편집시 폼객체나 주석처리된 부분을 수정할 경우에 설문이 정상작동하지 않을 수 있습니다.<br>
				문항 수정이나 추가 삭제는 설문등록/수정-문항등록/수정에서 처리하시기 바랍니다. <br>이 페이지에서 저장은 최종 저장을 의미합니다.
				</font></b>
				<div class="text" style="margin:7px 0;"><img src="images/tag_blue.png" alt="초기화안내 ">초기화 : HTML로 수정작업하여 완료된 설문을 수정작업 전 상태로 초기화 하는 기능입니다.</div>
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			<tr>	
				<td class="ctbl td">
				<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_pollhtml_iframe.jsp?pollID=<%=pollID %>" height=600  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
				</td>
			</tr>	
			</tbody>
			</table>
		</form>
	</div>	
	
	<div style="float:right;padding-right:5px"><a href="javascript:closeWindow($('<%=id%>_edit'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;padding-right:5px"><a id="<%=id%>_edit_saveBtn" href="javascript:$('<%=id%>_edit').saveData('N')" class="web20button bigblue">초기화</a></div>
	<div style="float:right;padding-right:5px" ><a id="<%=id%>_edit_saveBtn" href="javascript:$('<%=id%>_edit').saveData('Y')" class="web20button bigblue">저 장</a></div>

	<script type="text/javascript">
		

		//저장 클릭 
		$('<%=id%>_edit').saveData = function( seq ){
			
			if(seq == 'Y'){
	
				if(!confirm('저장 하시겠습니까? \r\n여기서 저장하면 설문등록/수정 메뉴에서 더이상 문항 추가 및 수정을 할 수 없습니다.')){
					return;
				}
			}else{
				if(!confirm('정말 초기화 하시겠습니까? \r\nHTML 편집 이전으로 저장됩니다.')){
					return;
				}
			}
			
			var frm = $('<%=id%>_edit_form');
					

			frm.eResultPollHTML.value = $("<%=id%>_ifrmFckEditor").contentWindow.getFCKHtml();	
						
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_title'  // busy 를 표시할 window
				, updateWindowId: '<%=id%>' // 완료후 버튼,힌트 가 랜더링될 windowid
				, url: 'content/poll/poll.do?id=<%=id%>&method=updateResultPollHTML&resultFinishYN='+seq
				, update: MochaUI.Windows.instances.get('<%=id%>_info').contentEl // 해당 id 전체 재시작	
				, onSuccess: function(html,els,resHTML) {					
					$('<%=id%>_poll_frame').src = "pages/content/poll/poll_preview.jsp?id=<%=id %>&pollID=<%=pollID %>";
					closeWindow( $('<%=id%>_edit') );
					if(seq=='Y'){
						$('<%=id %>_pollButton').setStyle('display','none');
					}else{
						$('<%=id %>_pollButton').setStyle('display','block');
					}
					
					
				}
			});

			nemoRequest.post(frm);	
		}
		
		
	</script>

		