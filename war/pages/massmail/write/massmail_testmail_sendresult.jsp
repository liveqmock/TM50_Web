<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	
	String id = request.getParameter("id");
	String method = request.getParameter("method");



	if(method.equals("sendedTestMail")) {
%>
	<form id="<%=id%>_sform" name="<%=id%>_sform">
		<div class="search_wrapper" style="width:97%">
			<div>	
				<ul id="sSearchType"  class="selectBox">
					<li data="receiverMail" select="yes">테스트메일</li>										
				</ul>		
			</div>
			<div>
				<input type="text" id="sSearchText" name="sSearchText" size="15"/>
			</div>
			<div>
				<a href="javascript:$('<%=id%>').list()" class="web20button pink">검색</a>
			</div>
			<div class="right">
				<a href="javascript:$('<%=id%>').allList()" class="web20button bigblue">전체목록</a>
			</div>
			<div class="right">
				<a href="javascript:closeWindow($('<%=id%>'))" class="web20button bigpink">닫기</a>
			</div>
			<div class="right">
				<a href="javascript:$('<%=id%>').deleteTestResult()" class="web20button bigpink">선택삭제</a>
			</div>
		</div>
		
		</form>
		
		<div style="clear:both;width:97%">
		
			<form name="<%=id%>_list_form" id="<%=id%>_list_form">
			<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="100%" >
			<thead>
				<tr>
				<th style="height:30px;width:50px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eNotifyID,this.checked)"/></th>
				<th style="height:30px;width:200px">테스트메일</th>
				<th style="height:30px">메일제목</th>
				<th style="height:30px;width:70px">전송여부</th>
				<th style="height:30px;width:70px">발송결과</th>
				<th style="height:30px;width:150px">발송일</th>
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
		/* 전체목록
		/***********************************************/
		$('<%=id%>').allList = function(){
			initFormValue($('<%=id%>_sform'));
			$('<%=id%>').list();
		}


		/***********************************************/
		/* 삭제  
		/***********************************************/
		
		$('<%=id%>').deleteTestResult = function(){

			var frm = $('<%=id%>_list_form');
			var checked = isChecked( frm.elements['eNotifyID']  );

			if( checked == 0 ) {
				toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
				return;
			}

			
			if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

			// 마지막 페이지 에서 전부 삭제 했으면 페이지를 가감
			if(frm.elements['eNotifyID'].length == checked) {
				$('<%=id%>_rform').elements["curPage"].value = $('<%=id%>_rform').elements["curPage"].value -1;  
			}

			copyForm( $('<%=id%>_rform'), frm );

			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

				, url: 'massmail/write/massmail.do?method=deleteSystemoNotify&id=<%=id%>'
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {

					$('<%=id%>_list_form').sCheckAll.checked = false;

				}
			});
			nemoRequest.post(frm);
			
		}	
		

		/***********************************************/
		/* 테스트메일 리스트 
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
				//busyWindowId: '<%=id%>',  // busy 를 표시할 window
				updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

				url: 'massmail/write/massmail.do?method=listTestMailResult&id=<%=id%>', 
				update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
				
				onSuccess: function(html,els,resHTML,scripts) {
					
					//MochaUI.arrangeCascade();
				}
			});
			nemoRequest.post($('<%=id%>_rform'));
		}

		<%=id%>ServerTimer = $('<%=id%>').list.periodical(10000);
		MochaUI.Windows.instances.get('<%=id%>').addEvent('onClose',function() { $clear(<%=id%>ServerTimer) } );
		/* 리스트 표시 */
		window.addEvent("domready",function () {	
			$('<%=id%>').init(); 
		});
		</script>

<%
	}
	if(method.equals("listTestMailResult")) {
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
			<c:forEach items="${systemNotifyList}" var="systemNotify">
			<TR class="tbl_tr" systemNotifyList_id="<c:out value="${systemNotify.notifyID}"/>">
				<TD class="tbl_td" align="center"><input type="checkbox" class="notBorder" id="eNotifyID" name="eNotifyID" value="<c:out value="${systemNotify.notifyID}" />" /></TD>		
				<TD class="tbl_td" align="left"><c:out value="${systemNotify.receiverMail}" /></TD>	
				<TD class="tbl_td" align="left"><c:out value="${systemNotify.mailTitle}" /></TD>
				<TD class="tbl_td"><c:out value="${systemNotify.wasSended}" /></TD>
				<TD class="tbl_td">				
					<c:if test="${systemNotify.smtpResult=='0'}">
						<span title="<c:out value="${systemNotify.smtpMsg}" />">성공</span>
					</c:if>
					<c:if test="${systemNotify.smtpResult!='0' && systemNotify.wasSended == 'O'}">
						<span title="<c:out value="${systemNotify.smtpMsg}" />">실패</span>
					</c:if>
					<c:if test="${systemNotify.wasSended=='X'}">
						전송대기
					</c:if>
				</TD>
				<TD class="tbl_td" align="center"><c:out value="${systemNotify.registDate}" /></TD>
			</TR>
			</c:forEach>
			
			<script type="text/javascript">

				window.addEvent('domready', function(){
					
				
					// 테이블 렌더링
					$('<%=id%>').grid_content = new renderTable({
						element: $('<%=id%>_grid_content') // 렌더링할 대상
						,cursor: 'default' // 커서
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