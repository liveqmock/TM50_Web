<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    
<%
	String preID = request.getParameter("preID");
	String id = request.getParameter("id");
	String method = request.getParameter("method");


	if(method.equals("tester")) {
%>
		<form id="<%=id%>_sform" name="<%=id%>_sform">
		<div class="search_wrapper" style="width:380px">
			<div>
				<img src="images/tag_red.png" alt="Tips "> 전화번호가 설정되어 있지 않은 대상자는 사용 할 수 없습니다.
			</div>
			<div style="clear:both;width:380px;margin-bottom:5px">
				<div class="right">
					<a href="javascript:$('<%=id%>').inputTesterHp()" class="web20button bigblue">선택추가</a>
				</div>
				<div class="right">
					<a href="javascript:closeWindow($('<%=id%>'))" class="web20button bigpink">닫기</a>
				</div>		
			</div>	
		</div>
		</form>

		<div style="clear:both;width:380px">
			<form name="<%=id%>_list_form" id="<%=id%>_list_form">
			<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="380px" >
			<thead>
				<tr>
				<th style="height:30px;width:50px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eTesterHp,this.checked)"/></th>
				<th style="height:30px">테스트 번호</th>
				<th style="height:30px">테스트 이름</th>
				</tr>
			</thead>
			<tbody id="<%=id%>_grid_content">
				
			</tbody>
			</table>
			</form>
		</div>
		<script type="text/javascript">
		/***********************************************/
		/* 삭제  
		/***********************************************/
		
		$('<%=id%>').deleteTesterEmail = function(){

			var frm = $('<%=id%>_list_form');
			var checked = isChecked( frm.elements['eTesterEmail']  );

			if( checked == 0 ) {
				toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
				return;  
			}
			if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
				//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

				, url: 'massmail/write/massmail.do?method=deleteTesterEmail&id=<%=id%>'
				, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
				, onSuccess: function(html,els,resHTML) {

					$('<%=id%>_list_form').sCheckAll.checked = false;

				}
			});
			nemoRequest.post(frm);
			
		}	

		/***********************************************/
		/* 테스트메일 텍스트창에 입력 
		/***********************************************/
		$('<%=id%>').inputTesterHp = function(){

			var frm = $('<%=id%>_list_form');
			var testerHps = "";
			var checked = isChecked( frm.elements['eTesterHp']  );

			if( checked == 0 ) {
				toolTip.showTipAtControl(frm.sCheckAll,'선택한 이메일이 없습니다');
				return;
			}
			
			
			for(var i=1;i<frm.eTesterHp.length;i++){
				if(frm.eTesterHp[i].checked==true){
					testerHps += frm.eTesterHp[i].value +";";
				}
			}
			
			$('<%=preID%>_form').eSendTestSMS.value = $('<%=preID%>_form').eSendTestSMS.value+testerHps;		
			closeWindow($('<%=id%>'));
		}

		/***********************************************/
		/* 테스트메일 리스트 
		/***********************************************/

		$('<%=id%>').list = function () {	

			var frm = $('<%=id%>_sform');

			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>',  // busy 를 표시할 window
				updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

				url: 'masssms/write/masssms.do?method=testList&id=<%=id%>', 
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
if(method.equals("testList")) {
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
			
			<TR  style="display:none" >
				<TD class="tbl_td"><input type="checkbox" class="notBorder" id="eTesterHp" name="eTesterHp" value="" /></TD>		
				<TD class="tbl_td"><c:out value=""/></TD>	
				<TD class="tbl_td"><c:out value=""/></TD>		
			</TR>
			<c:forEach items="${testeSMSList}" var="tester">
			<TR class="tbl_tr" testList_id="<c:out value="${tester.testerHp}"/>">
				<TD class="tbl_td"><input type="checkbox" class="notBorder" id="eTesterHp" name="eTesterHp" value="<c:out value="${tester.testerHp}" />" /></TD>		
				<TD class="tbl_td"><c:out value="${tester.testerHp}"/></TD>	
				<TD class="tbl_td"><c:out value="${tester.testerName}"/></TD>		
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