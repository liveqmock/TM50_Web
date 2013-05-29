<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%
	String id = request.getParameter("id");
	String method = request.getParameter("method");

	//****************************************************************************************************/
	// 리스트 
	//****************************************************************************************************/
	if (method.equals("list")) {
%>

	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<c:forEach items="${autosmsEventList}" var="autosmsEvent">
	<TR class="tbl_tr" automailevent_id="<c:out value="${autosmsEvent.autosmsID}"/>">
		<TD class="tbl_td"><c:out value="${autosmsEvent.autosmsID}"/></TD>					
		<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${autosmsEvent.autosmsID}"/>')"><c:out value="${autosmsEvent.autosmsTitle}" escapeXml="true"/></b></a></TD>		
		<TD class="tbl_td"><c:out value="${autosmsEvent.userName}"/></TD>			
		<TD class="tbl_td"><c:out value="${autosmsEvent.templateSenderPhone}"/></TD>		
		<TD class="tbl_td">
			<c:if test="${autosmsEvent.state == 1}" >
			<img src="images/spinner.gif" title="현재 가동중입니다. ">
			</c:if>
			<c:if test="${autosmsEvent.state == 2}" >
			<img src="images/spinner-placeholder.gif" title="현재  일시정지입니다. ">
			</c:if>		
			<c:if test="${autosmsEvent.state == 3}" >
			<img src="images/spinner-placeholder-red.gif" title="현재  완전정지입니다. ">
			</c:if>					
		</TD>
		<TD class="tbl_td" align="center">
			<a href="javascript:$('<%=id%>').showStatistic(<c:out value="${autosmsEvent.autosmsID}"/>)"><img src="images/chart_bar.png" title="통계보기 ">
		</TD>
	</TR>
	</c:forEach>
	
	<%
			if (!message.equals("")) {
		%>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%
		}
	%>
	
	<!-- 자료가 하나도 없을경우 메인컨텐츠의 업데이트를 위하여 주석을 남긴다
		 주석이 없으면 업데이트 되지 않으므로 주의
		 (이주석은 지우지 말것 반드시 필요한 주석임)!!!!!!!!!!!!!!!!! 
	-->
	
	
	
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
	//****************************************************************************************************/
	//  편집 
	//****************************************************************************************************/
	if (method.equals("edit")) {
	
%>	
	<div style="margin-bottom:10px;width:98%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">		
		<input type="hidden" id="eAutosmsID" name="eAutosmsID" value="<c:out value="${autosmsEvent.autosmsID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">자동SMS명</td>
				<td class="ctbl td"><input type="text" id="eAutosmsTitle" name="eAutosmsTitle" value="<c:out value="${autosmsEvent.autosmsTitle}"/>" maxlength="100" size="50" mustInput="Y" msg="자동SMS명을  입력"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			<tr>				
				<td class="ctbl ttd1" width="100px">설명</td>
				<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${autosmsEvent.description}"/>" maxlength="100" size="50" /></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>		
			<tr>			
				
				<td class="ctbl ttd1 mustinput" width="100px">상태</td>
				<td class="ctbl td" >
					<table>
					<tr>
					<td>
					<ul id="eState"  class="selectBox">
						<li data="1" <c:if test="${autosmsEvent.state=='1'}">select='YES'</c:if>>가동중</li>
						<li data="2" <c:if test="${autosmsEvent.state=='2'}">select='YES'</c:if>>일시중지</li>
						<li data="3" <c:if test="${autosmsEvent.state=='3'}">select='YES'</c:if>>완전중지</li>
					</ul>
					</td>
					<td>
					<div style="margin-left:5px">
					<img src="images/help.gif" title="<div style='text-align:left'><strong>상태값 설명 :</strong><br>												
					<b>*가동중 :</b> 자동SMS가  발송큐에 들어오면 바로 발송한다.<br>
					<b>*일시중지 :</b> 자동SMS가  발송큐에 들어오면  발송하지 않고 발송큐에 저장한다.
					<font color='red'>(다시 가동중으로 변경하면  쌓여 있는 SMS가 일괄발송된다.)</font><br>
					<b>*완전중지 :</b> 자동SMS가 발송큐에 들어오면 발송되지 않고 바로 삭제가 된다.  
					</div>"/>
					</div>
					</td>
					</tr>
					</table>					
				</td>								
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">보내는 전화번호</td>
				<td class="ctbl td">
				<input type="text" id="eTemplateSenderPhone" name="eTemplateSenderPhone" value="<c:out value="${autosmsEvent.templateSenderPhone}"/>" size="20" maxlength="15"  mustInput="Y" />
				"-"는 생략해주세요. 예)029990000
				</td>
			</tr>					
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>					
			<tr>				
				<td class="ctbl ttd1" width="100px">SMS</td>
				<td class="ctbl td" colspan="3">
				
				<!-- SMS 입력창  시작  -->
					
				<table>
				<tr>
				<td>				
					<table width="200" height="340" border="0" cellpadding="0"	cellspacing="0" background="/images/phone/phone.jpg">
						<tr>
							<td width="41"></td>
							<td width="118" height="87"></td>
							<td width="41"></td>
						</tr>
						<tr>
							<td width="41" height="96"></td>
							<td width="118" height="96" valign="top">
							<table width="118" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="118" height="96" align="center">
									<div align="center">
									<textarea name="smsMsg" style="width: 100%; height: 100px" wrap="PHYSICAL"	class="smsbox" onKeyUp="check_msglen()" onKeyDown="check_msglen()"><c:out value="${autosmsEvent.templateMsg}"/></textarea></div>
									</td>
								</tr>
							</table>
							</td>
							<td width="41" height="96"></td>
						</tr>
						<tr>
							<td width="41" height="157" rowspan="3"></td>
							<td width="118" height="25"></td>
							<td width="41" height="157" rowspan="3"></td>
						</tr>
						<tr>
							<td width="118" height="20"><font color="605E4A">글자수
							 <INPUT TYPE="text" NAME="nbytes" value='0' class='sms_box' style="width: 20px; height: 18px;">/
							2000byte</font></td>
						</tr>
						<tr>
							<td width="118" height="112"></td>
						</tr>
					</table>
				</td>
				
				
				<td valign="top">
				<table>
				<tr>
				<td><b>※특수문자 삽입</b>&nbsp;(문자를 클릭하세요)</td>
				</tr>
				<tr>
				<td>
						<img src="/images/phone/specialchar.gif" width="140" height="121" border="0" usemap="#Map">
						<map name="Map">
						<area shape="rect" coords="4,4,21,23" href="javascript:onMergeSmsContent('▷');">
						<area shape="rect" coords="22,4,40,22" href="javascript:onMergeSmsContent('▶');">
						<area shape="rect" coords="42,4,60,22" href="javascript:onMergeSmsContent('♡');">
						<area shape="rect" coords="62,4,80,22" href="javascript:onMergeSmsContent('♥');">
						<area shape="rect" coords="81,4,99,22" href="javascript:onMergeSmsContent('☆');">
						<area shape="rect" coords="100,4,118,22" href="javascript:onMergeSmsContent('★');">
						<area shape="rect" coords="119,4,137,22" href="javascript:onMergeSmsContent('⊙');">
						<area shape="rect" coords="4,22,21,42" href="javascript:onMergeSmsContent('□');">
						<area shape="rect" coords="22,22,40,42" href="javascript:onMergeSmsContent('■');">
						<area shape="rect" coords="42,22,60,42" href="javascript:onMergeSmsContent('◁');">
						<area shape="rect" coords="62,22,80,42" href="javascript:onMergeSmsContent('◀');">
						<area shape="rect" coords="81,22,99,42" href="javascript:onMergeSmsContent('●');">
						<area shape="rect" coords="100,22,118,42" href="javascript:onMergeSmsContent('◎');">
						<area shape="rect" coords="119,22,137,42" href="javascript:onMergeSmsContent('○');">
						<area shape="rect" coords="15,54,17,55" href="javascript:onMergeSmsContent('');">
						<area shape="rect" coords="4,42,21,60" href="javascript:onMergeSmsContent('◇');">
						<area shape="rect" coords="22,42,40,60" href="javascript:onMergeSmsContent('◆');">
						<area shape="rect" coords="42,42,60,60" href="javascript:onMergeSmsContent('△');">
						<area shape="rect" coords="62,42,80,60" href="javascript:onMergeSmsContent('▲');">
						<area shape="rect" coords="81,42,99,60" href="javascript:onMergeSmsContent('▽');">
						<area shape="rect" coords="100,42,118,60" href="javascript:onMergeSmsContent('▼');">
						<area shape="rect" coords="119,42,137,60" href="javascript:onMergeSmsContent('。');">
						<area shape="rect" coords="4,60,21,79" href="javascript:onMergeSmsContent('♨');">
						<area shape="rect" coords="22,60,40,79" href="javascript:onMergeSmsContent('☏');">
						<area shape="rect" coords="42,60,60,79" href="javascript:onMergeSmsContent('☎');">
						<area shape="rect" coords="62,60,80,79" href="javascript:onMergeSmsContent('☜');">
						<area shape="rect" coords="81,60,99,79" href="javascript:onMergeSmsContent('☞');">
						<area shape="rect" coords="100,60,118,79" href="javascript:onMergeSmsContent('◈');">
						<area shape="rect" coords="119,60,137,79" href="javascript:onMergeSmsContent('▣');">
						<area shape="rect" coords="4,79,21,98" href="javascript:onMergeSmsContent('♩');">
						<area shape="rect" coords="22,79,40,98" href="javascript:onMergeSmsContent('♪');">
						<area shape="rect" coords="42,79,60,98" href="javascript:onMergeSmsContent('♬');">
						<area shape="rect" coords="62,79,80,98" href="javascript:onMergeSmsContent('§');">
						<area shape="rect" coords="81,79,99,98" href="javascript:onMergeSmsContent('※');">
						<area shape="rect" coords="100,79,118,98" href="javascript:onMergeSmsContent('◐');">
						<area shape="rect" coords="119,79,137,98" href="javascript:onMergeSmsContent('◑');">
						<area shape="rect" coords="4,98,21,117" href="javascript:onMergeSmsContent('＊');">
						<area shape="rect" coords="22,98,40,117" href="javascript:onMergeSmsContent('＠');">
						<area shape="rect" coords="42,98,60,117" href="javascript:onMergeSmsContent('→');">
						<area shape="rect" coords="62,98,80,117" href="javascript:onMergeSmsContent('←');">
						<area shape="rect" coords="81,98,99,117" href="javascript:onMergeSmsContent('↑');">
						<area shape="rect" coords="100,98,118,117" href="javascript:onMergeSmsContent('↓');">
						<area shape="rect" coords="119,98,137,117" href="javascript:onMergeSmsContent('↔');">
						</map>
						
				</td>		
				</tr>		
				<tr>
				<td><br><b>※일대일치환변수</b></td>
				</tr>		
				<tr>
				<td>
					일대일치환변수는 [$변수명]으로 하시고 변수명은  영문(소문자)으로 해야 합니다.<br>
					예)[$name]님 안녕하세요<br><br>
					일대일치환변수를 사용하고자 하면 입력하는 쪽에서 반드시 일대일치환변수에 맞는 값을 입력해야 합니다.<br>
					(자세한 것은 자동SMS API문서참조)
				</td>
				</tr>	
				</table>
				</td>
				
				</tr>
				</table>
				
				<!-- SMS 입력창  끝 -->
				
				

			</td>
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>			
			</tbody>
			</table>
		</form>
	</div>
	<div style="margin-bottom:10px;width:98%">
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>
	
	<c:if test="${autosmsEvent.autosmsID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${autosmsEvent.autosmsID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData()" class="web20button bigblue">저 장</a></div>
	</div>
	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));
		$('<%=id%>_form').nbytes.value = calculate_msglen($('<%=id%>_form').smsMsg.value);;

		function onMergeSmsContent( value )
		{
			var frm =$('<%=id%>_form');	
			var temp = frm.smsMsg.value;
			frm.smsMsg.value = temp + value;
			check_msglen();
		}
		

		function check_msglen()
		{
			var frm =$('<%=id%>_form');
			var length = calculate_msglen(frm.smsMsg.value);
			frm.nbytes.value = length;
			if (length > 2000) {
				alert("무선메시지는 최대 2000 바이트까지 전송하실 수 있습니다.\r\n초과된 " + (length - 2000) + "바이트는 자동으로 삭제됩니다.");
				frm.smsMsg.value = assert_msglen(frm.smsMsg.value, 2000);
			}
		}


		function calculate_msglen(message)
		{
			var nbytes = 0;
			for (i=0; i<message.length; i++) {
				var ch = message.charAt(i);
				if (escape(ch).length > 4) {
					nbytes += 2;
				} else if (ch != '\r') {
					nbytes++;
				}
			}
			return nbytes;
		}


		function assert_msglen(message, maximum)
		{
			var frm =$('<%=id%>_form');
			var inc = 0;
			var nbytes = 0;
			var msg = "";
			var msglen = message.length;
			for (i=0; i<msglen; i++) {
				var ch = message.charAt(i);
				if (escape(ch).length > 4) {
					inc = 2;
				} else if (ch != '\r') {
					inc = 1;
				}
				if ((nbytes + inc) > maximum) {
					break;
				}
				nbytes += inc;
				msg += ch;
			}
			frm.nbytes.value = nbytes;
			return msg;
		}
					
	
		
	</script>

<%
	}
%>