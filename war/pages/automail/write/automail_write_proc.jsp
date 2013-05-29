<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="message" class="java.lang.String" scope="request" />
	
	<c:forEach items="${automailEventList}" var="automailEvent">
	<TR class="tbl_tr" automailevent_id="<c:out value="${automailEvent.automailID}"/>">
		<TD class="tbl_td"><c:out value="${automailEvent.automailID}"/></TD>					
		<TD class="tbl_td" align="left"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${automailEvent.automailID}"/>')"><c:out value="${automailEvent.automailTitle}" escapeXml="true"/></b></a></TD>		
		<TD class="tbl_td"><c:out value="${automailEvent.userName}"/></TD>	
		<TD class="tbl_td"><c:out value="${automailEvent.returnMail}" /></TD>	
		<TD class="tbl_td"><c:out value="${automailEvent.templateSenderMail}"/></TD>		
		<TD class="tbl_td">
			<c:if test="${automailEvent.state == 1}" >
			<img src="images/spinner.gif" title="현재 가동중입니다. ">
			</c:if>
			<c:if test="${automailEvent.state == 2}" >
			<img src="images/spinner-placeholder.gif" title="현재  일시정지입니다. ">
			</c:if>		
			<c:if test="${automailEvent.state == 3}" >
			<img src="images/spinner-placeholder-red.gif" title="현재  완전정지입니다. ">
			</c:if>					
		</TD>
		<TD class="tbl_td" align="center">
			<a href="javascript:$('<%=id%>').showStatistic(<c:out value="${automailEvent.automailID}"/>)"><img src="images/chart_bar.png" title="통계보기 ">
		</TD>
	</TR>
	</c:forEach>
	
	<%if(!message.equals("")) { %>
	<script type="text/javascript">
		alert("<%=message%>");
	</script>
	<%}%>
	
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
if(method.equals("edit")) {
	
	String automailID = request.getParameter("automailID");
	
	
%>	
	<div style="margin-bottom:10px;width:98%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">
		<input type="hidden" id="eTemplateContent" name="eTemplateContent">		
		<input type="hidden" id="eFileName" name="eFileName">
		<input type="hidden" id="eNewFile" name="eNewFile">
		<input type="hidden" id="eAutomailID" name="eAutomailID" value="<c:out value="${automailEvent.automailID}"/>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1 mustinput" width="100px">자동메일명</td>
				<td class="ctbl td"><input type="text" id="eAutomailTitle" name="eAutomailTitle" value="<c:out value="${automailEvent.automailTitle}"/>" size="50" mustInput="Y" msg="자동메일명을  입력"/></td>
				<td class="ctbl ttd1" width="100px">설명</td>
				<td class="ctbl td"><input type="text" id="eDescription" name="eDescription" value="<c:out value="${automailEvent.description}"/>" size="50" /></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>		
			<tr>				
				<td class="ctbl ttd1 mustinput" width="100px">인코딩 타입 </td>
				<td class="ctbl td" >
					<ul id="eEncodingType"  class="selectBox">
						<li data="EUC-KR" <c:if test="${automailEvent.encodingType=='EUC-KR'}">select='YES'</c:if>>한국어(EUC-KR)</li>
						<li data="UTF-8" <c:if test="${automailEvent.encodingType=='UTF-8'}">select='YES'</c:if>>다국어(UTF-8)</li>
						
					</ul>
				</td>	
				<td class="ctbl ttd1 mustinput" width="100px">상태</td>
				<td class="ctbl td" >
					<table>
					<tr>
					<td>
					<ul id="eState"  class="selectBox">
						<li data="1" <c:if test="${automailEvent.state=='1'}">select='YES'</c:if>>가동중</li>
						<li data="2" <c:if test="${automailEvent.state=='2'}">select='YES'</c:if>>일시중지</li>
						<li data="3" <c:if test="${automailEvent.state=='3'}">select='YES'</c:if>>완전중지</li>
					</ul>
					</td>
					<td>
					<div style="margin-left:5px">
					<img src="images/help.gif" title="<div style='text-align:left'><strong>상태값 설명 :</strong><br>												
					<b>*가동중 :</b> 자동메일이  발송큐에 들어오면 바로 발송한다.<br>
					<b>*일시중지 :</b> 자동메일이  발송큐에 들어오면  발송하지 않고 발송큐에 저장한다.
					<font color='red'>(다시 가동중으로 변경하면  쌓여 있는 메일이  일괄발송된다.)</font><br>
					<b>*완전중지 :</b> 자동메일이 발송큐에 들어오면 발송되지 않고 바로 삭제가 된다.  
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
				<td class="ctbl ttd1 mustinput" width="100px">반송 메일</td>
				<td class="ctbl td"><input type="text" id="eReturnMail" name="eReturnMail" value="<c:out value="${automailEvent.returnMail}"/>" size="50" mustInput="Y" msg="반송메일을   입력"/></td>
				<td class="ctbl ttd1" width="100px">받는 사람명</td>
				<td class="ctbl td"><input type="text" id="eTemplateReceiverName" name="eTemplateReceiverName" value="<c:out value="${automailEvent.templateReceiverName}"/>" size="50" /></td>
				
			</tr>				
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">보내는 메일</td>
				<td class="ctbl td"><input type="text" id="eTemplateSenderMail" name="eTemplateSenderMail" value="<c:out value="${automailEvent.templateSenderMail}"/>" size="50" /></td>
				<td class="ctbl ttd1" width="100px">보내는 사람명 </td>
				<td class="ctbl td"><input type="text" id="eTemplateSenderName" name="eTemplateSenderName" value="<c:out value="${automailEvent.templateSenderName}"/>" size="50" /></td>
			</tr>					
			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">반복치환사용유무</td>
				<td class="ctbl td">
					<ul id="eRepeatGroupType"  name="eRepeatGroupType" class="selectBox">
						<li data="1" <c:if test="${automailEvent.repeatGroupType=='1'}">select='YES'</c:if>>사용안함</li>
						<li data="3" <c:if test="${automailEvent.repeatGroupType=='3'}">select='YES'</c:if>>메일본문에 사용</li>
											
						<!-- 첨부파일 기능 사용시에 아래 내용으로 수정 
						<li data="1" <c:if test="${automailEvent.repeatGroupType=='1'}">select='YES'</c:if>>모두사용안함</li>
						<li data="2" <c:if test="${automailEvent.repeatGroupType=='2'}">select='YES'</c:if>>첨부파일에만 사용</li>
						<li data="3" <c:if test="${automailEvent.repeatGroupType=='3'}">select='YES'</c:if>>메일본문에만 사용</li>
						<li data="4" <c:if test="${automailEvent.repeatGroupType=='4'}">select='YES'</c:if>>모두사용(메일본문,첨부파일)</li>
						 -->
					</ul>
					
					<b><font color="red">(주의)반드시 용도에 맞게 정확히 선택해야 합니다.</font></b>
				</td>
				<td class="ctbl ttd1" width="100px">메일본문 </td>
				<td class="ctbl td">
					<div class="btn_green"><a id="mailTemplateSelect" style ="cursor:pointer" href="javascript:$('<%=id%>').templateWindow(2)"><span>메일템플릿열기</span></a></div>
				</td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">메일제목</td>
				<td class="ctbl td" colspan="3"><input type="text" id="eTemplateTitle" name="eTemplateTitle" value="<c:out value="${automailEvent.templateTitle}"/>" size="100"/></td>
			</tr>
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>								
			<!-- <tr>
				<td class="ctbl ttd1" width="100px">첨부파일 </td>
				<td class="ctbl td" colspan="3">
				<div  style="float:left" id="<%=id%>uploadWrapper" ></div>
					<div style="float:left;padding:22px">
						<a id="<%=id%>_uploadBtn" href="javascript:$('<%=id%>').uploadFile()" class="web20button bigblue" >파일 업로드</a>
					</div>		
				</td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			 <tr>
				<td class="ctbl ttd1" width="100px">업로드된 첨부파일</td>
				<td class="ctbl td" style="padding:5px" colspan="3">
				<div  id="<%=id%>uploaded" >
				<div style="float:left">
					<a href="automail/automail.do?method=fileDownload&automailID=<c:out value="${automailEvent.automailID}"/>">
					<c:out value="${automailEvent.fileName}"/>
					</a>
				</div>				
				</div>
			</td>
			</tr>							
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>-->							
			<tr>				
				<td class="ctbl ttd1" width="100px">HTML</td>
				<td class="ctbl td" colspan="3">
				<IFRAME id="<%=id%>_ifrmFckEditor" name="<%=id%>_ifrmFckEditor" src="iframe/fckeditor/fck_automailwrite_iframe.jsp?automailID=<c:out value="${automailEvent.automailID}"/>" height=400  width=100% scrolling=no frameborder=0 marginwidth=0 marginheight=0></IFRAME>
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
	
	<c:if test="${automailEvent.automailID != 0}" >

	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData(<c:out value="${automailEvent.automailID}"/>)" class="web20button bigpink">삭 제</a></div>
	</c:if>
	
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData()" class="web20button bigblue">저 장</a></div>
	</div>
	<script language="javascript">
		// 셀렉트 박스 렌더링
		makeSelectBox.render($('<%=id%>_form'));		
		//$('<%=id%>').renderUpload();
		
	</script>

<%
}
%>