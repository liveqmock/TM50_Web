<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.LoginInfo"%>
<%@ page import="web.common.util.*" %> 
<%

String id = request.getParameter("id");
String method = request.getParameter("method");




//****************************************************************************************************/
// 리스트 
//****************************************************************************************************/
if(method.equals("listPollCode")) {	
%>

	
	<jsp:useBean id="curPage" class="java.lang.String" scope="request" />
	<jsp:useBean id="iLineCnt" class="java.lang.String" scope="request" />
	<jsp:useBean id="mainMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="subMenuID" class="java.lang.String" scope="request" />
	<jsp:useBean id="iTotalCnt" class="java.lang.String" scope="request" />
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
	
	
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" />
	<c:set var="temp" value=""/> 
	<c:forEach items="${pollCodeList}" var="pollCode">
	
		<c:if test="${temp == pollCode.codeID}">
			<TR style="height:30px;" >
			<TD class="tbl_td"></TD>					
			<TD class="tbl_td"></TD>									
			<TD class="tbl_td"></TD>
			<TD class="tbl_td"><c:out value="${pollCode.codeNo}"/></TD>
			<TD class="tbl_td" align="left"><c:out value="${pollCode.codeDesc}"/></TD>	
			<TD class="tbl_td"><c:out value="${pollCode.useYN}" /></TD>	
			</TR>	
		</c:if>											
		<c:if test="${temp != pollCode.codeID}">
			<c:set var="temp" value="${pollCode.codeID}"/>
				
			<TR style="height:30px;">
			<TD class="tbl_td"><c:out value="${pollCode.codeTypeDesc}"/></TD>					
			<TD class="tbl_td"><c:out value="${pollCode.codeID}"/></TD>								
			<TD class="tbl_td"><b><a href="javascript:$('<%=id%>').editWindow('<c:out value="${pollCode.codeID}"/>','<c:out value="${pollCode.codeType}"/>','<c:out value="${pollCode.useYN}"/>')"><c:out value="${pollCode.codeName}" escapeXml="true"/></a></b></TD>
			<TD class="tbl_td"><c:out value="${pollCode.codeNo}"/></TD>
			<TD class="tbl_td" align="left"><c:out value="${pollCode.codeDesc}"/></TD>				
			<TD class="tbl_td"><c:out value="${pollCode.useYN}" /></TD>	
			</TR>	
		</c:if>		
	</c:forEach>
	
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
}
//****************************************************************************************************/
//리스트 
//****************************************************************************************************/
if(method.equals("editPollCode")) {
	request.setCharacterEncoding("UTF-8");
%>	
	<jsp:useBean id="codeID" class="java.lang.String" scope="request" />
	<jsp:useBean id="codeType" class="java.lang.String" scope="request" />
	<jsp:useBean id="codeName" class="java.lang.String" scope="request" />
	<jsp:useBean id="useYN" class="java.lang.String" scope="request" />

<% 
	if(codeType.equals("")) codeType="2"; //기본은 보기유형으로 변경 
%>	


	
	<div id="<%=id%>_divInfo">
	<c:set var="who" value="<%=LoginInfo.getUserID(request)%>" /> 
	<div style="margin-bottom:10px;width:100%">		
		<form id="<%=id%>_form" name="<%=id%>_form" method="post">			
			<input type="hidden" id="eCodeID" name="eCodeID" value="<%=codeID %>" />
			<input type="hidden" id="eCodeType" name="eCodeType" value="<%=codeType %>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">코드타입</td>
				<td class="ctbl td">
				<select name="eCodeTypes" id="eCodeTypes" disabled>					
					<option value="1" <% if(codeType.equals("1")){ %> selected <%} %>>설문유형</option>
					<option value="2" <% if(codeType.equals("2")){ %> selected <%} %>>보기유형</option>
				</select>
				</td>
			</tr>			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">코드명</td>
				<td class="ctbl td">
				<input type="text" id="eCodeName" name="eCodeName" value="<%=codeName %>" size="50" mustInput="Y" msg="코드명을  입력"/>
				</td>
			</tr>			
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px">사용여부</td>
				<td class="ctbl td">
				<% if(codeID.equals("100")){ %>
				<select name="eUseYN" id="eUseYN">
					<option <% if(useYN.equals("Y")){ %> selected <%} %>>Y</option>					
				</select>
				<%}else{ %>
					<select name="eUseYN" id="eUseYN">
					<option <% if(useYN.equals("Y")){ %> selected <%} %>>Y</option>
					<option <% if(useYN.equals("N")){ %> selected <%} %>>N</option>
				</select>			
				<%} %>
				</td>
			</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>
			<tr>				
				<td class="ctbl ttd1" width="100px">코드입력</td>
				<td class="ctbl td">		
				<table>
				<tr><td>					
				<div class="left"><div class="btn_b"><a id="<%=id%>_codeDescPlus" style ="cursor:pointer"><span>(+)보기추가</span></a></div></div>
				<div class="left"><div class="btn_b"><a id="<%=id%>_codeDescMinus" style ="cursor:pointer"><span>(-)보기삭제</span></a></div></div>				
				</td>
				</tr>
	
				<tr>
				<td>			
				<table id="<%=id%>_codeDesc">
				<% if(codeID.equals("")){ %>
					<tr><td>1.<input type="text" name="eCodeDesc" size="50"/></td></tr>
				<%}else{ %>		
					<c:forEach items="${pollCodeList}" var="pollCode" varStatus="cnt">					
						<tr><td><c:out value="${cnt.count}"/>.<input type="text" name="eCodeDesc" size="50" value="${pollCode.codeDesc}"/></td></tr>
					</c:forEach>
				<%} %>	
				</table>
				</td>
				</tr>
				</table>			
				</td>				
			</tr>					
			</tbody>
			</table>
		</form>
	</div>	
	<div>	
	<div style="float:right"><a href="javascript:closeWindow($('<%=id%>_modal'))"  class="web20button bigpink">닫 기</a></div>	
	<% if(!codeID.equals("100")){ %>
	<div style="float:right;padding-right:10px" ><a href="javascript:$('<%=id%>').deleteData('<%=codeID %>')" class="web20button bigpink">삭 제</a></div>
	<%} %>	
	<div style="float:right;padding-right:10px" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').saveData('<%=codeID %>')" class="web20button bigblue">저 장</a></div>
	</div>	
		
	
	<script type="text/javascript">

		var flen = 1;
		
		/******************************************/
		/* 보기 추가
		/******************************************/ 
		function addCodeDescPlus(){
			 var tester_length = $('<%=id%>_form').eCodeDesc.length;	
			 flen = tester_length;
		/*			 
			 if (tester_length > 20)
			 {
			  	 alert("20개 이상 추가할수 없습니다.")
			 	 return false;
			 }
		*/	
			 var table = $('<%=id%>_form').getElementById("<%=id%>_codeDesc");
			 var tableRow = table.insertRow(table.rows.length);
			 var tableCell = tableRow.insertCell(0);
	
			 if(tester_length==null){
				 tester_length=1;
			 }
			 tableCell.innerHTML = (tester_length+1)+".<input type=\"text\" id=\"eCodeDesc\" name=\"eCodeDesc\" size=\"50\" />";
			 flen++;
			 eCodeDescEvent();	
		}

		//엔터버튼 이벤트
		function eCodeDescEvent() {
			var table = $('<%=id%>_form').getElementById("<%=id%>_codeDesc");
			var inputs = table.getElements('input[name=eCodeDesc]');
			var len = inputs.length;
			$each(inputs,function(el,index) {
				el.focus();
				el.removeEvents('keydown');
				el.addEvent('keydown', function(e) {
					if(e.key == 'enter') {
						new Event(e).stop();
						if(index == len-1) {
							addCodeDescPlus();
						} else {
							el.getParent('tr').getNext().getElement('input[name=eCodeDesc]').focus();								
						}
					}

				});	
			});			
		}

		
	
		/******************************************/
		/* 보기 추가 함수 호출 
		/******************************************/ 
		$('<%=id%>_codeDescPlus').addEvent('click',function(){
	
			addCodeDescPlus();
		});

		
	
		/******************************************/
		/* 보기 마이너스 
		/******************************************/ 
		$('<%=id%>_codeDescMinus').addEvent('click',function(){			
			 var file_length = 0;
			 var table = $('<%=id%>_form').getElementById("<%=id%>_codeDesc");
			 if (table.rows.length - 1 > file_length)
			 {
			  table.deleteRow(table.rows.length - 1);
			  flen--;
			 }
		});

		/* 리스트 표시 */
		window.addEvent("domready",function () {			
			eCodeDescEvent();			
		});


	</script>
	</div>
<%}%>