<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="web.common.util.*" %>     
<%@ page import="web.massmail.write.model.*" %> 
<%@ page import="web.massmail.write.service.*" %> 
<%@ page import="web.massmail.write.control.*" %>
<%@ page import="java.util.List" %>
<%
	String preID = request.getParameter("preID");
	String id = request.getParameter("id");
	String method = request.getParameter("method");
	
	
	List<FilterInfo>  filterInfos = null;
	
	int count = 0;	
	if(method.equals("checkMail")) {
%>
		
		<form id="<%=id%>_sform" name="<%=id%>_sform">
		<input type="hidden" id="eContent" name="eContent">	
		<input type="hidden" id="eTitle" name="eTitle">
		<input type="hidden" id="eContent_ori" name="eContent_ori">	
		<input type="hidden" id="eTitle_ori" name="eTitle_ori">
		
		
		<div class="search_wrapper" style="width:97%">
			<div class="right">
				<a href="javascript:closeWindow($('<%=id%>'))" class="web20button bigpink">닫기</a>
			</div>
			
			<div class="right">
				<a href="javascript:$('<%=id%>').send();" class="web20button bigpink">수정</a>
			</div>
			<div class="right">
				<a href="javascript:$('<%=id%>').reload();" class="web20button bigpink">되돌리기</a>
			</div>
			<div id='clickText' class="right">
				<a href="javascript:$('<%=id%>').recheck();" class="web20button bigpink">재검증</a>
			</div>
			
		</div>
		</form>
		<br>
		<div name="<%=id%>_table_form" id="<%=id%>_table_form"  style="clear:both;">
		</div>
		<br>
		<br>
		<div id="<%=id %>_fck" style="clear:both;width:100%;display:block">
		<div class="text"><img src="images/tag_blue.png"> 아래에서 내용을 수정하고, '수정' 버튼을 누르면 입력창에 반영됩니다.</div>
		<IFRAME id="content" name="content" src="iframe/fckeditor/fck_massmail_chk_iframe.jsp?parent_id=<%=id %>" width="100%" height="500" scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
		<!-- 
		<textarea id="content" name="content" style="width:100%;height:300px">
		</textarea>
		 -->
		 </div>
		<script type="text/javascript">
		
		$('<%=id%>_sform').eContent_ori.value = $('<%=preID%>_form').eMailContent.value;
		$('<%=id%>_sform').eTitle_ori.value = $('<%=preID%>_form').eMailTitle.value;
		
		$('<%=id%>').showTable = function ( ) {	

			var frm = $('<%=id%>_sform');
			
			frm.eContent.value = $('<%=preID%>_form').eMailContent.value;
			frm.eTitle.value = $('<%=preID%>_form').eMailTitle.value;
			var html = eval("frm.eContent.value");
			
			
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>',  // busy 를 표시할 window
				updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

				url: 'massmail/write/massmail.do?method=checkMailResult&id=<%=id%>&preID=<%=preID%>', 
				update: $('<%=id%>_table_form'), // 완료후 content가 랜더링될 element
				
				onSuccess: function(html,els,resHTML,scripts) {
					
					//MochaUI.arrangeCascade();
				}
			});
			nemoRequest.post($('<%=id%>_sform'));

			//$('content').contentWindow.setFCKHtml(html);
			//document.getElementById('content').value = html;
			
			
		}

		/* 리스트 표시 */
		window.addEvent("domready",function () {	
			$('<%=id%>').showTable(); 	
			
		});

		
		/***********************************************/
		/* 수정
		/***********************************************/

		$('<%=id%>').send = function ( ) {
			$('<%=preID%>_ifrmFckEditor').contentWindow.setFCKHtml($('content').contentWindow.getFCKHtml());

			if(document.getElementById("count").getAttribute("value")==0)
			{
					$('<%=preID%>').isCheckContent = true;
					new Element('img',{ 'src' : 'images/icon-check.gif' } ).inject( $('<%=preID%>_checkContentImg').empty());
			}
			
			closeWindow( $('<%=id%>') );
		}
		
		
		
		
		/***********************************************/
		/* 재검증
		/***********************************************/

		$('<%=id%>').recheck = function ( ) {
			
			$('<%=preID%>_ifrmFckEditor').contentWindow.setFCKHtml($('content').contentWindow.getFCKHtml());
			$('<%=preID%>_form').eMailContent.value = $('content').contentWindow.getFCKHtml();
			$('<%=preID%>_form').eMailTitle.value = $('<%=id%>_sform').eTitle.value;
			$('<%=id%>').showTable(); 	
			
		}

		/***********************************************/
		/* 되돌리기
		/***********************************************/

		$('<%=id%>').reload = function ( ) {
			
			$('<%=id%>_sform').eContent.value = $('<%=id%>_sform').eContent_ori.value;
			$('<%=id%>_sform').eTitle.value = $('<%=id%>_sform').eTitle_ori.value;
			
			$('content').contentWindow.setFCKHtml($('<%=id%>_sform').eContent.value);
			//document.getElementById("content").value = $('<%=id%>_sform').eContent.value;
			$('<%=preID%>_ifrmFckEditor').contentWindow.setFCKHtml($('<%=id%>_sform').eContent.value);
			$('<%=preID%>_form').eMailContent.value = $('<%=id%>_sform').eContent.value;
			$('<%=preID%>_form').eMailTitle.value = $('<%=id%>_sform').eTitle.value;
			$('<%=id%>').showTable(); 	
			
		}

		/***********************************************/
		/* 결과수정창 열기
		/***********************************************/
		$('<%=id%>').editWindow = function() {
			var s = $('<%=id%>').grid_content.getSelectedRow().getAttribute("filterInfoValue");
			nemoWindow(
				{
					'id': 'edit_modal',
					busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
					width: 600,
					height: 200,
					//height: 600,
					title: '수정',
					type: 'modal',
					loadMethod: 'xhr',
					contentURL: 'pages/massmail/write/massmail_check_result.jsp?parent_id=<%=id%>&id=edit_modal&method=edit'
				}
			);
			
		}
		
		</script>
 <%
}
if(method.equals("listCheck")) {
	filterInfos = (List<FilterInfo>)request.getAttribute("filterInfoList");
	
	int not_allowed_send_count = 0; 
	int not_suggested_send_count = 0;
	
	int contentType_spam_count = 0;
	int contentType_script_count = 0;
	int contentType_tag_count = 0;
	
	int filterType_title_count = 0;
	int filterType_content_count = 0;
	int filterType_all_count = 0;
	
	String msg = "메일을 발송 할 수 있습니다";
	
	for(int i=0;i<filterInfos.size();i++)
	{
		FilterInfo f = filterInfos.get(i);
		if(f.getFilterLevel()==1)
			not_allowed_send_count++;
		else if(f.getFilterLevel()==2)
			not_suggested_send_count++;
		
		if(f.getContentType()==1)
			contentType_spam_count++;		
		else if(f.getContentType()==2)
			contentType_script_count++;
		else if(f.getContentType()==3)
			contentType_tag_count++;
		
		if(f.getFilterType()==1)
			filterType_title_count++;		
		else if(f.getFilterType()==2)
			filterType_content_count++;
		else if(f.getFilterType()==3)
			filterType_all_count++;	
	}
	
	if(not_allowed_send_count > 0)
		msg = "메일에 포함할수 없는 내용이 있어, 발송할 수 없습니다. 검증 결과를 확인하고 메일을 수정하여야 합니다.";
	else if(not_suggested_send_count > 0)
		msg = "메일에 포함하면 발송에 영향을 줄 수 있는 내용이 있습니다. 검증 결과를 확인하고 메일을 수정하여야 합니다.";
		
 %>
 		<div class="dash_box_content">
			<form id="<%=id%>_form" name="<%=id%>_form" method="post">
			<input type="hidden" value="<%=not_allowed_send_count%>" id="count">
			<table class="ctbl" >
			<tbody>
				<tr><td class="ctbl ttd1" colspan="4">필터링 된 구문 <font color=red>총 <%=not_allowed_send_count+ not_suggested_send_count%>건 </font>  </td>
				</tr>
				<tr>
					<td class="ctbl ttd1" width="120px">발송 불가</td>
					<td class="ctbl td" align="center" width="240px">
					<%if(not_allowed_send_count>0){ %><font color=red> <%} %>
						<%=not_allowed_send_count %> 
					<%if(not_allowed_send_count>0){ %></font> <%} %>
					건
						
					</td>
					<td class="ctbl ttd1" width="120px">제외 권장</td>
					<td class="ctbl td" align="center" width="240px">
					<%if(not_suggested_send_count>0){ %><font color=red> <%} %>
						<%=not_suggested_send_count%>
					<%if(not_suggested_send_count>0){ %></font> <%} %>
					 건
					</td>
					
				</tr>
				
				<tr>
					<td class="ctbl ttd1" width="120px">제목</td>
					<td class="ctbl td" align="center" >
						<%=filterType_title_count %> 건
					</td>
					<td class="ctbl ttd1" width="120px">본문</td>
					<td class="ctbl td" align="center" >
						<%=filterType_content_count+filterType_all_count %> 건
					</td>					
				</tr>
				
				
				<tr>
					<td class="ctbl ttd1" width="120px">권장 조치 사항</td>
					<td class="ctbl td" align="center" colspan="3">
						<%=msg %>
					</td>
					
				</tr>	
			</tbody>
			</table>
			
			</form>
		</div>
		<br>
			<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="758px" >
			<thead>
				<tr>
				<th style="height:30px;width:70px">구분</th>
				<th style="height:30px">필터 내용</th>
				<th style="height:30px;width:100px">타입</th>
				<th style="height:30px;width:100px">발송</th>
				
				</tr>
			</thead>
			<tbody id="<%=id%>_grid_content">
			
			<c:forEach items="<%=filterInfos %>" var="filterInfo">
			
			<TR class="tbl_tr" filterInfoValue="<c:out value="${filterInfo.content}"/>" filterInfoType="<c:out value="${filterInfo.contentType}"/>" filterInfoLevel="<c:out value="${filterInfo.filterLevel}"/>">
				<TD class="tbl_td">
					<c:if test="${filterInfo.filterType=='1'}">제목</c:if>
					<c:if test="${filterInfo.filterType=='2'}">본문</c:if>
					<c:if test="${filterInfo.filterType=='3'}">제목/본문</c:if>					
				</TD>		
				<TD class="tbl_td"><a href="javascript:$('<%=id%>').editWindow()">
					<c:out value="${fn:substring(filterInfo.content,0,50)}" escapeXml="true"/>
					<c:if test="${fn:length(filterInfo.content) > 50}" >
					..
   					</c:if>
   					</a>
				</TD>		
				<TD class="tbl_td">
					<c:if test="${filterInfo.contentType=='1'}">스팸성 문구</c:if>
					<c:if test="${filterInfo.contentType=='2'}">Script</c:if>
					<c:if test="${filterInfo.contentType=='3'}">Tag</c:if>			
				</TD>	
				<TD class="tbl_td">
					<c:if test="${filterInfo.filterLevel=='1'}"><font color="red">발송 불가<%count++;%></font></c:if>
					<c:if test="${filterInfo.filterLevel=='2'}">제외 권장</c:if>
					<c:if test="${filterInfo.filterLevel=='3'}">사용안함</c:if>	
				</TD>
			</TR>
			</c:forEach>
			
			</tbody>
			</table>
						
			<script type="text/javascript">

				window.addEvent('domready', function(){
					
				
					// 테이블 렌더링
					$('<%=id%>').grid_content = new renderTable({
						element: $('<%=id%>_grid_content') // 렌더링할 대상
						//,cursor: 'pointer' // 커서
						,focus: true  // 마우스 이동시 포커스 여부
						,select: true // 마우스 클릭시 셀렉트 표시 여부
						,popup: $('<%=id%>').popup // 마우스 클릭시 사용할 팝업메뉴
					});
					$('<%=id%>').grid_content.render();
				});
				</script>
<%
}if(method.equals("edit")) {
	String parent_id = request.getParameter("parent_id");
	String id_modal = request.getParameter("id");
		
%>

	<div style="margin-bottom:10px;width:100%">
		
		<form id="<%=id_modal%>_form" name="<%=id_modal%>_form" method="post">
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1 mustinput" >권장 조치사항</td>
				<td class="ctbl td" id="eSuggest" name="eSuggest"></td>
						
			</tr>				
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" >검증 내용</td>
				<td class="ctbl td"><input type="text" id="eStr" name="eStr" value="" size="85" mustInput="Y"/></td>
						
			</tr>				
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" >치환할 내용</td>
				<td class="ctbl td"><input type="text" id="eStrEdit" name="eStrEdit" value="" size="85" /></td>
						
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl td" colspan="2">
					<img src="images/tag_blue.png" alt="Tip" />치환할 내용을 입력하고 '치환'버튼을 누르면 검증 내용이 변경됩니다. 
					<br> 
					<img src="images/tag_blue.png" alt="Tip" />치환할 내용을 입력하지 않으면 검증 내용이 삭제됩니다 
				</td>
						
			</tr>				
			
			</tbody>
			</table>
		</form>
	</div>
	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id_modal%>'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id_modal%>_form').saveData(<c:out value="${board.boardID}"/>)" class="web20button bigblue">치 환</a></div>

	<script language="javascript">
	var str = $('<%=parent_id%>').grid_content.getSelectedRow().getAttribute("filterInfoValue");
	var type = $('<%=parent_id%>').grid_content.getSelectedRow().getAttribute("filterInfoType");
	var level = $('<%=parent_id%>').grid_content.getSelectedRow().getAttribute("filterInfoLevel");

	var suggestMsg = document.getElementById("eSuggest");
	var msg = null;
	
	if(type=='1')
		msg='스팸메일로 필터될 수 있는 문구 입니다. 다른 내용으로 치환해야 합니다.';
	else if(type=='2')
		msg='메일 본문에는 스크립트가 포함될 수 없습니다. 해당 내용을 삭제해야 합니다.';
	else if(type=='3')
		msg='정확하지 않은 Tag가 있습니다. 템플릿을 수정해야 합니다.';
	
	suggestMsg.appendChild(document.createTextNode(msg));
	document.getElementById("eStr").setAttribute("value",str);

		/***********************************************/
		/* 치환
		/***********************************************/

		$('<%=id_modal%>_form').saveData = function ( ) {
			if(confirm("제목/본문 내용이 수정됩니다. 계속하시겠습니까?"))
			{
				var content_ori = $('content').contentWindow.getFCKHtml();
				var content_new = content_ori.replace(str, $('<%=id_modal%>_form').eStrEdit.value);
				$('content').contentWindow.setFCKHtml(content_new);

				var title_ori = $('<%=parent_id%>_sform').eTitle.value;
				var title_new = title_ori.replace(str, $('<%=id_modal%>_form').eStrEdit.value);
				$('<%=parent_id%>_sform').eTitle.value = title_new;
				//$('<%=parent_id%>').recheck();
				closeWindow($('<%=id_modal%>'));
			}
		}
	
	</script>

<%
}
%>