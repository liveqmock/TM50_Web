<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="java.util.*"%>

<%
	String id = request.getParameter("id");
	String pollID = request.getParameter("pollID");
	String pollTemplateID = request.getParameter("pollTemplateID");
	String isExistQuestion = request.getParameter("isExistQuestion");
	
%>
<jsp:useBean id="selQuestionNos" class="java.lang.String" scope="request" />
<jsp:useBean id="pollExampleList" class="java.util.ArrayList" scope="request" />
<jsp:useBean id="pollExampleListMatrixX" class="java.util.ArrayList" scope="request" />
<jsp:useBean id="pollExampleListMatrixY" class="java.util.ArrayList" scope="request" />

 	<div id="<%=id%>_divQuestion" style="margin-top:10px">
	<div>		
		<form id="<%=id%>_question_form" name="<%=id%>_question_form" method="post">				
			<input type="hidden" id="ePollID" name="ePollID" value="<%=pollID %>" />
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">질문유형</td>
				<td class="ctbl td">
				<c:if test="${pollQuestion.questionType==null || pollQuestion.questionType=='0'}">
					<input type="radio" id="eQuestionType" name="eQuestionType" class="notBorder" checked value="1"/>단일질문&nbsp;				
					<input type="radio" id="eQuestionType" name="eQuestionType" class="notBorder" value="2"/>매트릭스질문&nbsp;	
				</c:if>			
				<c:if test="${pollQuestion.questionType!=null && pollQuestion.questionType=='1'}">
					<input type="radio" id="eQuestionType" name="eQuestionType" class="notBorder" checked value="1"/>단일질문&nbsp;
					<input type="radio" id="eQuestionType" name="eQuestionType" class="notBorder" disabled value="2"/>매트릭스질문&nbsp;	
				</c:if>		
				<c:if test="${pollQuestion.questionType!=null && pollQuestion.questionType=='2'}">									
					<input type="radio" id="eQuestionType" name="eQuestionType" class="notBorder" disabled value="1"/>단일질문&nbsp;
					<input type="radio" id="eQuestionType" name="eQuestionType" class="notBorder" checked value="2"/>매트릭스질문&nbsp;		
				</c:if>								
				</td>
			</tr>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>			
			<tr>				
				<td class="ctbl ttd1" width="100px">머릿글</td>
				<td class="ctbl td">
				<textarea id="eQuestionHead" name="eQuestionHead" style="width:650px;height:50px">${pollQuestion.questionHead}</textarea>
				</td>				
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1" width="100px">번호</td>
				<td class="ctbl td">
				<div style="float:left;padding-top:1px"> 
				<jsp:include page="./poll_question_number_inc.jsp" flush="true">						
						<jsp:param name="pollID" value="<%=pollID %>" />	
						<jsp:param name="selQuestionNos" value="<%=selQuestionNos %>" />
				</jsp:include>
				</div>
				<% if(selQuestionNos!=null && !selQuestionNos.equals("")){ %>				
				<div class="btn_green" style="float:left;padding-left:5px"><a id="searchSender" style ="cursor:pointer" href="javascript:$('<%=id%>').questionChg('${pollQuestion.questionID}')"><span>번호변경</span></a></div>
				<%} %>
				</td>
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>
			<tr>
				<td class="ctbl ttd1" width="100px" rowspan="2">질문</td>
				<td class="ctbl td">		
					<div style="float:right"><div class="btn_r"><a href="javascript:$('<%=id%>').openQuestionCopyForm()" style ="cursor:pointer"><span>기존문항불러오기</span></a></div></div>
					<div style="float:right"><div class="btn_r"><a href="javascript:$('<%=id%>').openMultimediaForm(0)" style ="cursor:pointer"><span>멀티미디어</span></a></div></div>
				</td>
			</tr>	
			<tr>				
				
				<td class="ctbl td">			
					<textarea id="eQuestionText" name="eQuestionText" mustInput="Y" style="width:650px;height:50px">${pollQuestion.questionText}</textarea>
					<input type="hidden" id="eQuestionFileURL" name="eQuestionFileURL" value="${pollQuestion.fileURL}">
					<input type="hidden" id="eQuestionLayoutType" name="eQuestionLayoutType" value="${pollQuestion.layoutType}">				
				</td>				
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>			
			</tbody>
			</table>
			
			<!-- ####################################### 일반유형  ########################################################## -->			
			<div id="<%=id %>_questionTypeNormal">			
			<table class="ctbl" width="100%">
			<tbody>						
			<tr>				
				<td class="ctbl ttd1" width="100px">응답유형</td>
				<td class="ctbl td" >
				<div style="float:left">
				<input type="radio" id="eExampleType" name="eExampleType" class="notBorder" value="1" <c:if test="${pollQuestion.exampleType==null || pollQuestion.exampleType=='1'}">checked</c:if> />객관식&nbsp;
				<input type="radio" id="eExampleType" name="eExampleType" class="notBorder" value="2" <c:if test="${pollQuestion.exampleType=='2'}">checked</c:if> />주관식&nbsp;
				<input type="radio" id="eExampleType" name="eExampleType" class="notBorder" value="3" <c:if test="${pollQuestion.exampleType=='3'}">checked</c:if> />순위선택&nbsp;
				<input type="radio" id="eExampleType" name="eExampleType" class="notBorder" value="4" <c:if test="${pollQuestion.exampleType=='4'}">checked</c:if> />숫자/문자입력&nbsp;
				<input type="radio" id="eExampleType" name="eExampleType" class="notBorder" value="5" <c:if test="${pollQuestion.exampleType=='5'}">checked</c:if> />척도형&nbsp;												
				(&nbsp;<input type="checkbox" id="eRequiredYN" name="eRequiredYN" class="notBorder" value="Y" <c:if test="${pollQuestion.requiredYN=='Y'|| pollQuestion.requiredYN=='' }">checked</c:if>/>필수응답&nbsp;)
				</div>&nbsp;
				<div id="<%=id%>_divAnswer_Position" style="float:left;padding-left:10px">보기정렬:
				<select name="eExamplePosition" id="eExamplePosition">
					<option value="1" <c:if test="${pollQuestion.examplePosition=='1'}">selected</c:if> >1</option>
					<option value="2" <c:if test="${pollQuestion.examplePosition=='2'}">selected</c:if> >2</option>
					<option value="3" <c:if test="${pollQuestion.examplePosition=='3'}">selected</c:if> >3</option>
					<option value="4" <c:if test="${pollQuestion.examplePosition=='4'}">selected</c:if> >4</option>
					<option value="5" <c:if test="${pollQuestion.examplePosition=='5'}">selected</c:if> >5</option>
				</select>
				</div>
				</td>				
			</tr>
			</tbody>
			</table>
				
					
			<!-- 객관식 샘플  -->
			<div id="<%=id%>_divAnswer_sample">
			<table class="ctbl" width="100%">
			<tbody>							
			<tr>
				<td class="ctbl ttd1" width="100px">응답형식</td>
				<td class="ctbl td">
				<jsp:include page="./poll_code_type_inc.jsp" flush="true">
						<jsp:param name="mustInput" value="N" />
						<jsp:param name="codeType" value="2" />					
				</jsp:include>
				</td>
			</tr>
			</tbody>
			</table>
			</div>
			<div style="display:none" id="<%=id%>_divAnswer_example">	
			<table class="ctbl" width="100%">
			<tbody>			
			<tr>
				<td class="ctbl ttd1" width="100px">보기</td>
				<td class="ctbl td" id="<%=id%>_example"></td>
			</tr>	
			</tbody>
			</table>				
			</div>
			<div id="<%=id%>_divAnswer_custom">
			<table class="ctbl" width="100%">
			<tbody>				
		
			<tr>
				<td class="ctbl ttd1" width="100px">응답방법</td>
				<td class="ctbl td">
					<table>
					<tr>
					<td>
					<div class="left">
					<input type="radio" id="eExampleGubun" name="eExampleGubun" class="notBorder" value="1" <c:if test="${pollQuestion.exampleGubun==null || pollQuestion.exampleGubun=='1'}">checked</c:if> />단일응답&nbsp;
					<input type="radio" id="eExampleGubun" name="eExampleGubun" class="notBorder" value="2" <c:if test="${pollQuestion.exampleGubun=='2'}">checked</c:if> />복수응답&nbsp;&nbsp;
					</div>
					</td>
					<td>
					<div class="hidden" id="<%=id%>_divExample_gubun">
					<input type="text"  id="eExampleMultiCount" name="eExampleMultiCount" size="2" maxlength="2" value="${pollQuestion.exampleMultiCount}">(최대선택개수)
					<input type="text"  id="eExampleMultiMinimumCount" name="eExampleMultiMinimumCount" size="2" maxlength="2" value="${pollQuestion.exampleMultiMinimumCount}">(최소선택개수)
					&nbsp; <img src="images/tag_blue.png"> 최소선택은 0일경우 제한없음
					</div>
					</td>
					</tr>	
					</table>						
				</td>
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>							
			<tr>				
				<td class="ctbl ttd1" width="100px">보기입력</td>
				<td class="ctbl td">		
				<table>
				<tr><td>					
				<div style="float:left"><div class="btn_b"><a id="<%=id%>_examplePlus" style ="cursor:pointer"><span>(+)보기추가</span></a></div></div>
				<div style="float:left"><div class="btn_b"><a id="<%=id%>_exampleMinus" style ="cursor:pointer"><span>(-)보기삭제</span></a></div></div>			
				</td>
				</tr>
				<tr>
				<td>			
				<table id="<%=id%>_pollExample" width="100%">
				<c:if test="${pollQuestion.questionID=='0'}">
				<tr><td>
						1.<input type='text' id='eExampleDesc' name='eExampleDesc' size='50'/>&nbsp;<input type='checkbox' id='eExampleEx' name='eExampleEx' value="1"/>주관식&nbsp;|&nbsp;
						<div class="btn_r" style="float:right"><a href="javascript:$('<%=id%>').openMultimediaForm(1)" style ="cursor:pointer"><span>멀티미디어</span></a></div>
						<input type='hidden' id='eFileURL' name='eFileURL'/>
						<input type='hidden' id='eLayoutType' name='eLayoutType'/>
					</td>
				</tr>
				</c:if>
				<c:if test="${pollQuestion.questionID!='0'}">					
					<c:forEach items="${pollExampleList}" var="pollExample" varStatus="cnt">					
						<tr>
							<td>
								<c:out value="${cnt.count}"/>.<input type="text" id="eExampleDesc" name="eExampleDesc" size="50" value="${pollExample.exampleDesc}"/>&nbsp;<input type="checkbox" id="eExampleEx" name="eExampleEx" <c:if test="${pollExample.exampleExYN=='Y'}"> checked </c:if> value="<c:out value="${cnt.count}"/>"/>주관식&nbsp;&nbsp;|&nbsp;&nbsp;
								<div class="btn_r" style="float:right"><a href="javascript:$('<%=id%>').openMultimediaForm(${cnt.count})" style ="cursor:pointer"><span>멀티미디어</span></a></div>
								<input type='hidden' id='eFileURL' name='eFileURL' value="${pollExample.fileURL}"/>
								<input type='hidden' id='eLayoutType' name='eLayoutType' value="${pollExample.layoutType}"/>
							</td>
						</tr>
					</c:forEach>
				</c:if>	
				</table>
				<br/><img src='images/tag_blue.png'> 보기 입력 상자에서 엔터를 클릭하셔도 보기가 추가됩니다.
				</td>
				</tr>
				</table>			
				</td>				
			</tr>				
			</tbody>
			</table>
			</div>
			<!-- 주관식 -->
			<div id="<%=id%>_divAnswer_text">
			<table class="ctbl" width="100%">
			<tbody>							
			<tr>
				<td class="ctbl ttd1" width="100px">응답형식</td>
				<td class="ctbl td">
					<select name="eAnswerTextType1" id="eAnswerTextType1">
						<option value="" select>- 형식선택 -</option>
						<option value="1">한줄 입력</option>
						<option value="2">여러줄 입력</option>
					</select>
				</td>
			</tr>
			</tbody>
			</table>
			</div>
			<!-- 순위 선택 -->
			<div id="<%=id%>_divAnswer_ranking">
			<table class="ctbl" width="100%">
			<tbody>										
			<tr>				
				<td class="ctbl ttd1" width="100px">보기입력</td>
				<td class="ctbl td">		
				<table>
				<tr><td>					
				<div style="float:left"><div class="btn_b"><a id="<%=id%>_rankingExamplePlus" style ="cursor:pointer"><span>(+)보기추가</span></a></div></div>
				<div style="float:left"><div class="btn_b"><a id="<%=id%>_rankingExampleMinus" style ="cursor:pointer"><span>(-)보기삭제</span></a></div></div>				
				</td>
				</tr>
	
				<tr>
				<td>			
				<table id="<%=id%>_pollRankingExample">
				<c:if test="${pollQuestion.questionID=='0'}">
				<tr><td>1.<input type='text' id='eRankingExampleDesc' name='eRankingExampleDesc' size='50'/></td></tr>
				</c:if>
				<c:if test="${pollQuestion.questionID!='0'}">					
					<c:forEach items="${pollExampleList}" var="pollExample" varStatus="cnt">					
						<tr><td><c:out value="${cnt.count}"/>.<input type="text" id="eRankingExampleDesc" name="eRankingExampleDesc" size="50" value="${pollExample.exampleDesc}"/></td></tr>
					</c:forEach>
				</c:if>	
				</table>
				<br/><img src='images/tag_blue.png'> 보기 입력 상자에서 엔터를 클릭하셔도 보기가 추가됩니다.
				</td>
				</tr>
				</table>			
				</td>				
			</tr>				
			</tbody>
			</table>
			</div>	
			<!-- 숫자 입력 -->
			<div id="<%=id%>_divAnswer_number">
			<table class="ctbl" width="100%">
			<tbody>
			<tr>				
				<td class="ctbl ttd1" width="100px">입력 형식</td>
				<td class="ctbl td">
					<input type="radio" id="eNumberOrText" name="eNumberOrText" class="notBorder" value="1" <c:if test="${pollQuestion.examplePosition==null || pollQuestion.examplePosition !='1'}">checked</c:if> />숫자&nbsp;
					<input type="radio" id="eNumberOrText" name="eNumberOrText" class="notBorder" value="2" <c:if test="${pollQuestion.examplePosition=='-1'}">checked</c:if> />문자&nbsp;&nbsp;					
					<input type='hidden' id="eAnswerTextType2" name="eAnswerTextType2" value="1" />
					<br/><img src='images/tag_blue.png'> <strong> 숫자 입력</strong>일 경우 설문 응답시에 <strong>입력 상자에 숫자만</strong> 입력됩니다.<br/>
					<img src='images/tag_blue.png'> <strong>문자 입력</strong>일 경우 <strong>최대 입력 값</strong>을 <strong>초과</strong>하는 글자 수는 입력 할 수 없습니다.<br/> 
					<img src='images/tag_blue.png'> 최대 입력 값보다 <strong>큰 수를 입력</strong>하면 <strong>안내 메시지</strong>와 함께 <strong>입력 값이 최대 입력 값</strong>으로 변경 됩니다. 
				</td>				
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>									
			<tr id="<%=id %>_LimitCountTR">				
				<td class="ctbl ttd1" width="100px">입력 최대값</td>
				<td class="ctbl td">
					<input type="text" id="eLimitCount" name="eLimitCount" size="50" <c:if test="${pollQuestion.examplePosition !='0'}">value="${pollQuestion.examplePosition}"</c:if>>
				</td>				
			</tr>	
			<tr id="<%=id %>_LimitCountLine">
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>				
				<td class="ctbl ttd1" width="100px">보기 내용</td>
				<td class="ctbl td">
					이전 문자
					<c:if test="${pollQuestion.questionID=='0'}">
						<input type="text" id="eBeforeText" name="eBeforeText" size="20px">
					</c:if>
					<c:if test="${pollQuestion.questionID!='0'}">
						<c:forEach items="${pollExampleList}" var="pollExample" varStatus="cnt">	
							<c:set var="exampleDesc" value="${pollExample.exampleDesc}" />
							<c:set var="end" value="${fn:indexOf(exampleDesc, '≠')}" />
							<c:set var="exampleDesc" value="${fn:substring(exampleDesc,0,end)}" />
							<input type="text" id="eBeforeText" name="eBeforeText" size="5" value="${exampleDesc}">
						</c:forEach>				
					</c:if>
					<input type="text" size="3" style="background-color:#E9F4FE" disabled>
					이후 문자
					<c:if test="${pollQuestion.questionID=='0'}">
						<input type="text" id="eAfterText" name="eAfterText" size="7" >
					</c:if>
					<c:if test="${pollQuestion.questionID!='0'}">
						<c:forEach items="${pollExampleList}" var="pollExample" varStatus="cnt">	
							<c:set var="exampleDesc" value="${pollExample.exampleDesc}" />
							<c:set var="start" value="${fn:indexOf(exampleDesc, '≠')}" />
							<c:set var="end" value="${fn:length(exampleDesc)}" />
							<c:set var="exampleDesc" value="${fn:substring(exampleDesc,start+1,end)}" />
							<input type="text" id="eAfterText" name="eAfterText" size="5" value="${exampleDesc}">
					</c:forEach>	
					</c:if>		
					&nbsp; 예) 만 20세 ? 이전문자:만 / 이후문자:세
				</td>				
			</tr>		
			</tbody>
			</table>
			</div>
			<div id="<%=id%>_divAnswer_ranking">
			</div>		
			</div>
			<!-- ####################################### 매트릭스 질문  ########################################################## -->
			<div id="<%=id %>_questionTypeMatrix">			
			<table class="ctbl" width="100%">
			<tbody>						
			<tr>				
				<td class="ctbl ttd1" width="100px">매트릭스유형</td>
				<td class="ctbl td">	
				<div style="float:left">			
				<input type="radio" id="eExampleTypeMatrix" name="eExampleTypeMatrix" class="notBorder" value="1" <c:if test="${pollQuestion.exampleType==null || pollQuestion.exampleType=='1'}">checked</c:if> />객관식&nbsp;
				<input type="radio" id="eExampleTypeMatrix" name="eExampleTypeMatrix" class="notBorder" value="2" <c:if test="${pollQuestion.exampleType=='2'}">checked</c:if> />주관식&nbsp;				
				(&nbsp;<input type="checkbox" id="eRequiredYNMatrix" name="eRequiredYNMatrix"  readonly class="notBorder" value="Y" <c:if test="${pollQuestion.requiredYN=='Y'|| pollQuestion.requiredYN=='' }">checked</c:if>/>필수응답&nbsp;)
				</div>
				<div style="float:left;margin-left:10px" id="<%=id%>_matrixTextSize">(주관식 입력박스 사이즈
					<select name="eMatrixTextSize" id="eMatrixTextSize">
						<option value="5" <c:if test="${pollQuestion.matrixTextSize=='5'}">selected</c:if>>5</option>
						<option value="10" <c:if test="${pollQuestion.matrixTextSize=='0' || pollQuestion.matrixTextSize=='10'}">selected</c:if>>10</option>
						<option value="15" <c:if test="${pollQuestion.matrixTextSize=='15'}">selected</c:if>>15</option>
						<option value="20" <c:if test="${pollQuestion.matrixTextSize=='20'}">selected</c:if>>20</option>
						<option value="25" <c:if test="${pollQuestion.matrixTextSize=='25'}">selected</c:if>>25</option>
						<option value="30" <c:if test="${pollQuestion.matrixTextSize=='30'}">selected</c:if>>30</option>
						<option value="35" <c:if test="${pollQuestion.matrixTextSize=='35'}">selected</c:if>>35</option>
						<option value="40" <c:if test="${pollQuestion.matrixTextSize=='40'}">selected</c:if>>40</option>
						<option value="45" <c:if test="${pollQuestion.matrixTextSize=='45'}">selected</c:if>>45</option>
						<option value="50" <c:if test="${pollQuestion.matrixTextSize=='50'}">selected</c:if>>50</option>
					</select>)
				</div>				
				</td>				
			</tr>
			<tr>				
				<td class="ctbl ttd1" width="100px">행보기입력</td>
				<td class="ctbl td">				
				<table>
				<tr><td>					
				<div style="float:left"><div class="btn_b"><a id="<%=id%>_examplePlusMatrixX" style ="cursor:pointer"><span>(+)보기추가</span></a></div></div>
				<div style="float:left"><div class="btn_b"><a id="<%=id%>_exampleMinusMatrixX" style ="cursor:pointer"><span>(-)보기삭제</span></a></div></div>				
				</td>
				</tr>
	
				<tr>
				<td>			
				<table id="<%=id%>_pollExampleMatrixX">
				<c:if test="${pollQuestion.questionID=='0'}">
				<tr><td>1.<input type='text' id='eExampleDescMatrixX' name='eExampleDescMatrixX' size='50'/></td></tr>
				</c:if>
				<c:if test="${pollQuestion.questionID!='0'}">					
					<c:forEach items="${pollExampleListMatrixX}" var="pollExample" varStatus="cnt">					
						<tr><td><c:out value="${cnt.count}"/>.<input type="text" id="eExampleDescMatrixX" name="eExampleDescMatrixX" size="50" value="${pollExample.exampleDesc}"/></td></tr>
					</c:forEach>
				</c:if>	
				</table>
				</td>
				</tr>
				</table>						
				</td>				
			</tr>
			<tr>				
				<td class="ctbl ttd1" width="100px">열보기입력</td>
				<td class="ctbl td">				
				<table>
				<tr><td>					
				<div style="float:left"><div class="btn_b"><a id="<%=id%>_examplePlusMatrixY" style ="cursor:pointer"><span>(+)보기추가</span></a></div></div>
				<div style="float:left"><div class="btn_b"><a id="<%=id%>_exampleMinusMatrixY" style ="cursor:pointer"><span>(-)보기삭제</span></a></div></div>				
				</td>
				</tr>
	
				<tr>
				<td>			
				<table id="<%=id%>_pollExampleMatrixY">
				<c:if test="${pollQuestion.questionID=='0'}">
				<tr><td>1.<input type='text' id='eExampleDescMatrixY' name='eExampleDescMatrixY' size='50'/></td></tr>
				</c:if>
				<c:if test="${pollQuestion.questionID!='0'}">					
					<c:forEach items="${pollExampleListMatrixY}" var="pollExample" varStatus="cnt">					
						<tr><td><c:out value="${cnt.count}"/>.<input type="text" id="eExampleDescMatrixY" name="eExampleDescMatrixY" size="50" value="${pollExample.exampleDesc}"/></td></tr>
					</c:forEach>
				</c:if>	
				</table>
				</td>
				</tr>
				</table>						
				</td>				
			</tr>			
			</tbody>
			</table>				
			</div>
			
		</form>
	</div>
	<div>
	<div style="float:right;padding-top:10px"><a href="javascript:closeWindow($('<%=id%>_question'))"  class="web20button bigpink">닫 기</a></div>
	<div style="float:right;padding-right:10px;padding-top:10px" ><a id="<%=id%>_saveBtn2" href="javascript:$('<%=id%>').deleteQuestion('${pollQuestion.questionID}')" class="web20button bigpink">삭제</a></div>
	<div style="float:right;padding-right:10px;padding-top:10px" ><a id="<%=id%>_saveBtn2" href="javascript:$('<%=id%>').saveQuestion('${pollQuestion.questionID}','${pollQuestion.questionNo}')" class="web20button bigblue">저장</a></div>
	</div>
	<script type="text/javascript">
	
		<c:if test="${pollQuestion.questionType==null || pollQuestion.questionType=='1'}">
			$('<%=id %>_questionTypeNormal').setStyle('display','block');
			$('<%=id %>_questionTypeMatrix').setStyle('display','none');
		</c:if>
	
		//일반질문일 경우
		$($('<%=id%>_question_form').eQuestionType[0]).addEvent('click', function(){	
			$('<%=id %>_questionTypeNormal').setStyle('display','block');
			$('<%=id %>_questionTypeMatrix').setStyle('display','none');
		});
		
		//매트릭스질문일 경우
		$($('<%=id%>_question_form').eQuestionType[1]).addEvent('click', function(){	
			$('<%=id %>_questionTypeNormal').setStyle('display','none');
			$('<%=id %>_questionTypeMatrix').setStyle('display','block');
			eExampleDescEventMatrixX();
			eExampleDescEventMatrixY();
		});
		
		//매트릭스 설문이라면 
		<c:if test="${pollQuestion.questionType=='2'}">		
			$('<%=id %>_questionTypeNormal').setStyle('display','none');
			eExampleDescEventMatrixX();
			eExampleDescEventMatrixY();
		</c:if>
		
		//일반설문이라면 
		<c:if test="${pollQuestion.questionType=='1'}">
			$('<%=id %>_questionTypeMatrix').setStyle('display','none');		
		</c:if>

		//객관식이라면 
		<c:if test="${pollQuestion.exampleType==null || pollQuestion.exampleType=='1'}">	
			$('<%=id%>_divAnswer_sample').setStyle('display','block');
			$('<%=id%>_divAnswer_custom').setStyle('display','block');
			$('<%=id%>_divExample_gubun').setStyle('display','none');
			$('<%=id%>_divAnswer_text').setStyle('display','none');
			$('<%=id%>_divAnswer_ranking').setStyle('display','none');
			$('<%=id%>_divAnswer_number').setStyle('display','none');
			//복수응답일 경우 
			<c:if test="${pollQuestion.exampleGubun=='2'}">
				$('<%=id%>_divExample_gubun').setStyle('display','block');
			</c:if>
			$('<%=id%>_matrixTextSize').setStyle('display','none');
		</c:if>	

		//주관식이라면 
		<c:if test="${pollQuestion.exampleType=='2'}">
			$('<%=id%>_divAnswer_text').setStyle('display','block');
			$('<%=id%>_divAnswer_sample').setStyle('display','none');
			$('<%=id%>_divAnswer_custom').setStyle('display','none');
			$('<%=id%>_divAnswer_Position').setStyle('display','none');
			$('<%=id%>_divAnswer_ranking').setStyle('display','none');
			$('<%=id%>_divAnswer_number').setStyle('display','none');
			<c:if test="${pollQuestion.questionType=='1'}">
			$('<%=id%>_matrixTextSize').setStyle('display','none');
			</c:if>	
			<c:if test="${pollQuestion.questionType=='2'}">
			$('<%=id%>_matrixTextSize').setStyle('display','block');
			</c:if>	
		</c:if>
		
		//순위선택, 척도형 이라면 
		<c:if test="${pollQuestion.exampleType=='3' || pollQuestion.exampleType=='5'}">	
			$('<%=id%>_divAnswer_ranking').setStyle('display','block');
			$('<%=id%>_divAnswer_Position').setStyle('display','block');			
			$('<%=id%>_divAnswer_custom').setStyle('display','none');				
			$('<%=id%>_divAnswer_sample').setStyle('display','none');
			$('<%=id%>_divAnswer_example').setStyle('display','none');
			$('<%=id%>_divAnswer_number').setStyle('display','none');
			$('<%=id%>_divAnswer_text').setStyle('display','none');			
		</c:if>	
		
		//숫자입력이라면 
		<c:if test="${pollQuestion.exampleType=='4'}">	
			$('<%=id%>_divAnswer_number').setStyle('display','block');
			$('<%=id%>_divAnswer_ranking').setStyle('display','none');
			$('<%=id%>_divAnswer_Position').setStyle('display','none');			
			$('<%=id%>_divAnswer_custom').setStyle('display','none');				
			$('<%=id%>_divAnswer_sample').setStyle('display','none');
			$('<%=id%>_divAnswer_example').setStyle('display','none');
			$('<%=id%>_divAnswer_text').setStyle('display','none');			
		</c:if>	
		
		//척도형이라면
		<c:if test="${pollQuestion.exampleType=='5'}">	
			$('<%=id%>_divAnswer_Position').setStyle('display','none');
		</c:if>
		
		//일반질문 객관식일 경우 
		$($('<%=id%>_question_form').eExampleType[0]).addEvent('click', function(){	
			$('<%=id%>_divAnswer_text').setStyle('display','none');
			$('<%=id%>_divAnswer_custom').setStyle('display','block');				
			$('<%=id%>_divAnswer_sample').setStyle('display','block');
			$('<%=id%>_divAnswer_Position').setStyle('display','block');
			$('<%=id%>_divAnswer_example').setStyle('display','none');
			$('<%=id%>_divAnswer_ranking').setStyle('display','none');
			$('<%=id%>_divAnswer_number').setStyle('display','none');
		});	

		//일반질문 주관식일 경우 
		$($('<%=id%>_question_form').eExampleType[1]).addEvent('click', function(){	
			$('<%=id%>_divAnswer_text').setStyle('display','block');
			$('<%=id%>_divAnswer_custom').setStyle('display','none');
			$('<%=id%>_divAnswer_sample').setStyle('display','none');
			$('<%=id%>_divAnswer_example').setStyle('display','none');		
			$('<%=id%>_divAnswer_Position').setStyle('display','none');
			$('<%=id%>_divAnswer_ranking').setStyle('display','none');
			$('<%=id%>_divAnswer_number').setStyle('display','none');
			$('<%=id%>_question_form').eAnswerTextType2.value = "";
		});

		//일반질문 순위선택일 경우 
		$($('<%=id%>_question_form').eExampleType[2]).addEvent('click', function(){	
			$('<%=id%>_divAnswer_text').setStyle('display','none');
			$('<%=id%>_divAnswer_ranking').setStyle('display','block');
			$('<%=id%>_divAnswer_Position').setStyle('display','block');			
			$('<%=id%>_divAnswer_custom').setStyle('display','none');				
			$('<%=id%>_divAnswer_sample').setStyle('display','none');
			$('<%=id%>_divAnswer_example').setStyle('display','none');
			$('<%=id%>_divAnswer_number').setStyle('display','none');
			eRankingExampleDescEvent();
		});
		
		//일반질문 숫자/문자 입력일 경우 
		$($('<%=id%>_question_form').eExampleType[3]).addEvent('click', function(){
			$('<%=id%>_divAnswer_text').setStyle('display','none');
			$('<%=id%>_divAnswer_number').setStyle('display','block');
			$('<%=id%>_divAnswer_custom').setStyle('display','none');
			$('<%=id%>_divAnswer_sample').setStyle('display','none');
			$('<%=id%>_divAnswer_example').setStyle('display','none');		
			$('<%=id%>_divAnswer_Position').setStyle('display','none');
			$('<%=id%>_divAnswer_ranking').setStyle('display','none');
			$('<%=id%>_question_form').eAnswerTextType1.value = "";
		});

		//일반질문 척도형일 경우 
		$($('<%=id%>_question_form').eExampleType[4]).addEvent('click', function(){
			$('<%=id%>_divAnswer_text').setStyle('display','none');
			$('<%=id%>_divAnswer_ranking').setStyle('display','block');
			$('<%=id%>_divAnswer_Position').setStyle('display','none');			
			$('<%=id%>_divAnswer_custom').setStyle('display','none');				
			$('<%=id%>_divAnswer_sample').setStyle('display','none');
			$('<%=id%>_divAnswer_example').setStyle('display','none');
			$('<%=id%>_divAnswer_number').setStyle('display','none');
			eRankingExampleDescEvent();
		});
		//일반질문 숫자/문자 입력 > 숫자 일경우 
		$($('<%=id%>_question_form').eNumberOrText[0]).addEvent('click', function(){
			$('<%=id%>_question_form').eAnswerTextType1.value = "";
			$('<%=id%>_question_form').eAnswerTextType2.value = $('<%=id%>_question_form').eNumberOrText[0].value; 
		});
		//일반질문 숫자/문자 입력 > 문자 일경우 
		$($('<%=id%>_question_form').eNumberOrText[1]).addEvent('click', function(){
			$('<%=id%>_question_form').eAnswerTextType1.value = "";
			$('<%=id%>_question_form').eAnswerTextType2.value = $('<%=id%>_question_form').eNumberOrText[1].value;
		});
		//매트릭스 객관식일 경우 
		$($('<%=id%>_question_form').eExampleTypeMatrix[0]).addEvent('click', function(){
			$('<%=id%>_matrixTextSize').setStyle('display','none');
		});
		
		//매트릭스 주관식일 경우 
		$($('<%=id%>_question_form').eExampleTypeMatrix[1]).addEvent('click', function(){
			$('<%=id%>_matrixTextSize').setStyle('display','block');
		});
		

		//단일응답일 경우  
		$($('<%=id%>_question_form').eExampleGubun[0]).addEvent('click', function(){
			$('<%=id%>_divExample_gubun').setStyle('display','none');
		});

		//복수응답일 경우  
		$($('<%=id%>_question_form').eExampleGubun[1]).addEvent('click', function(){
			$('<%=id%>_divExample_gubun').setStyle('display','block');
		});		

		//번호 변경을 클릭시 
		$('<%=id%>').questionChg = function(seq){
			nemoWindow(
					{
						'id': '<%=id%>_chgnum',		
						noOtherClose: true,
						busyEl: '<%=id%>_question', // 창을 열기까지 busy 가 표시될 element
						width: 420,
						//height: $('mainColumn').style.height.toInt(),
						height: 150,
						title: '설문번호변경',
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'pages/content/poll/poll_chgnum.jsp?id=<%=id%>&pollID=<%=pollID%>&oldQuestionNo=<%=selQuestionNos%>&eQuestionID='+seq
					}
				);
		}
		
		
		//설문번호를 변경했을 때.
		$('<%=id%>_question').selectQuestion = function(seq){
		
			nemoRequest.init( 
					{
						busyWindowId: '<%=id%>_question'  // busy 를 표시할 window
						, updateWindowId: '<%=id%>_question' // 완료후 버튼,힌트 가 랜더링될 windowid
						, url: 'content/poll/poll.do?id=<%=id%>&ePollID=<%=pollID%>&ePollTemplateID=<%=pollTemplateID%>&method=editPollQuestion&eQuestionNo='+seq
						, update: MochaUI.Windows.instances.get('<%=id%>_question').contentEl // 해당 id 전체 재시작	
						, onSuccess: function(html,els,resHTML) {										
							//refreshWindow('<%=id%>_question');
							closeWindow( $('<%=id%>_chgnum') );
							
						}
					});

					nemoRequest.post($('<%=id%>_question_form'));			
		}

		//응답유형을 변경했을 경우 
		$('<%=id%>_question').selectCodeID = function(seq){		
			if(seq=='CUSTOM'){
				$('<%=id%>_divAnswer_custom').setStyle('display','block');	
				$('<%=id%>_divAnswer_example').setStyle('display','none');		
					
			}else{				
				$('<%=id%>_divAnswer_custom').setStyle('display','none');
				$('<%=id%>_divAnswer_example').setStyle('display','block');			
			
				nemoRequest.init( 
				{
					busyWindowId: '<%=id%>_question'  // busy 를 표시할 window
					//, updateWindowId: '<%=id%>_modal' // 완료후 버튼,힌트 가 랜더링될 windowid
					, url: 'content/poll/poll.do?id=<%=id%>&method=selectCode&codeType=2&codeID='+seq
					, update: $('<%=id%>_example') // 완료후 content가 랜더링될 element		
					, onSuccess: function(html,els,resHTML) {
						
					}
				});
				nemoRequest.post($('<%=id%>_question_form'));
			}
		}
		var flen = 1;
		var flenX = 1;
		var flenY = 1;

		//보기추가 
		function addExamplePlus(){
			 var tester_length = $('<%=id%>_question_form').eExampleDesc.length;		
			 flen = tester_length;		 
			 //if (tester_length > 50)
			 //{
			 // alert("더이상 추가할수 없습니다.")
			 // return false;
			 //}
			
			 var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollExample");
			 var tableRow = table.insertRow(table.rows.length);
			 var tableCell = tableRow.insertCell(0);

			 if(tester_length==null){
				 tester_length=1;
			 }
			 if($('<%=id%>_question_form').eExampleGubun[1].checked==true){
				 tableCell.innerHTML = (tester_length+1)+".<input type=\"text\" name=\"eExampleDesc\" size=\"50\" />&nbsp;<input type=\"checkbox\" name=\"eExampleEx\" value=\""+(tester_length+1)+"\"/>주관식&nbsp;|&nbsp;<div class=\"btn_r\" style=\"float:right\"><a href=\"javascript:$('<%=id%>').openMultimediaForm("+(tester_length+1)+")\" style =\"cursor:pointer\"><span>멀티미디어</span></a></div> <input type='hidden' id='eFileURL' name='eFileURL'><input type='hidden' id='eLayoutType' name='eLayoutType' >";
			 }else{
				 tableCell.innerHTML = (tester_length+1)+".<input type=\"text\" name=\"eExampleDesc\" size=\"50\" />&nbsp;<input type=\"checkbox\" name=\"eExampleEx\" value=\""+(tester_length+1)+"\"/>주관식&nbsp;|&nbsp;<div class=\"btn_r\" style=\"float:right\"><a href=\"javascript:$('<%=id%>').openMultimediaForm("+(tester_length+1)+")\" style =\"cursor:pointer\"><span>멀티미디어</span></a></div> <input type='hidden' id='eFileURL' name='eFileURL'><input type='hidden' id='eLayoutType' name='eLayoutType' >";	 
			 }
			 						
			 flen++;			 
			 eExampleDescEvent();			 
		}

		//순위 선택 보기추가 
		function addRankingExamplePlus(){
			 var tester_length = $('<%=id%>_question_form').eRankingExampleDesc.length;		
			 flen = tester_length;		 
			 //if (tester_length > 50)
			 //{
			 // alert("더이상 추가할수 없습니다.")
			 // return false;
			 //}
			
			 var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollRankingExample");
			 var tableRow = table.insertRow(table.rows.length);
			 var tableCell = tableRow.insertCell(0);

			 if(tester_length==null){
				 tester_length=1;
			 }
			 
			 tableCell.innerHTML = (tester_length+1)+".<input type=\"text\" name=\"eRankingExampleDesc\" size=\"50\" />";						
			 flen++;			 
			 eRankingExampleDescEvent();			 
		}
		
		//가로 보기추가 (행)
		function addExamplePlusMatrixX(){
			 var tester_length = $('<%=id%>_question_form').eExampleDescMatrixX.length;		
			 flenX = tester_length;		 
			 //if (tester_length > 50)
			 //{
			 // alert("더이상 추가할수 없습니다.")
			 // return false;
			 //}
			
			 var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollExampleMatrixX");
			 var tableRow = table.insertRow(table.rows.length);
			 var tableCell = tableRow.insertCell(0);

			 if(tester_length==null){
				 tester_length=1;
			 }
			 
			 tableCell.innerHTML = (tester_length+1)+".<input type=\"text\" name=\"eExampleDescMatrixX\" size=\"50\" />";						
			 flenX++;			 
			 eExampleDescEventMatrixX();			 
		}
		
		//세로 보기추가 (열)
		function addExamplePlusMatrixY(){
			 var tester_length = $('<%=id%>_question_form').eExampleDescMatrixY.length;		
			 flenY = tester_length;		 
			 if (tester_length >= 20)
			 {
			  alert("더이상 추가할수 없습니다.")
			  return false;
			 }
			
			 var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollExampleMatrixY");
			 var tableRow = table.insertRow(table.rows.length);
			 var tableCell = tableRow.insertCell(0);

			 if(tester_length==null){
				 tester_length=1;
			 }
			 
			 tableCell.innerHTML = (tester_length+1)+".<input type=\"text\" name=\"eExampleDescMatrixY\" size=\"50\" />";						
			 flenY++;			 
			 eExampleDescEventMatrixY();			 
		}
		

		//엔터버튼 이벤트
		function eExampleDescEvent() {
			var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollExample");
			var inputs = table.getElements('input[name=eExampleDesc]');
			var len = inputs.length;
			$each(inputs,function(el,index) {				
				el.focus();
				el.removeEvents('keydown');
				el.addEvent('keydown', function(e) {
						if(e.key == 'enter') {
							new Event(e).stop();
							if(index == len-1) {
								addExamplePlus();
							} else {
								if(el.getParent('tr').getNext()==null){
									addExamplePlus();
								}else{
									el.getParent('tr').getNext().getElement('input[name=eExampleDesc]').focus();
								}
							}
						}
				});	
			});			
		}
		
		//엔터버튼 이벤트 (순위선택
		function eRankingExampleDescEvent() {
			var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollRankingExample");
			var inputs = table.getElements('input[name=eRankingExampleDesc]');
			var len = inputs.length;
			$each(inputs,function(el,index) {				
				el.focus();
				el.removeEvents('keydown');
				el.addEvent('keydown', function(e) {
						if(e.key == 'enter') {
							new Event(e).stop();
							if(index == len-1) {
								addRankingExamplePlus();
							} else {
								if(el.getParent('tr').getNext()==null){
									addRankingExamplePlus();
								}else{
									el.getParent('tr').getNext().getElement('input[name=eRankingExampleDesc]').focus();
								}
							}
						}
				});	
			});			
		}
		
		//가로보기(행) 엔터버튼 이벤트
		function eExampleDescEventMatrixX() {			
			var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollExampleMatrixX");			
			var inputs = table.getElements('input[name=eExampleDescMatrixX]');
			var len = inputs.length;
			$each(inputs,function(el,index) {				
				el.focus();
				el.removeEvents('keydown');
				el.addEvent('keydown', function(e) {
						if(e.key == 'enter') {
							new Event(e).stop();
							if(index == len-1) {
								addExamplePlusMatrixX();
							} else {
								if(el.getParent('tr').getNext()==null){
									addExamplePlusMatrixX();
								}else{
									el.getParent('tr').getNext().getElement('input[name=eExampleDescMatrixX]').focus();
								}
							}
						}
				});	
			});			
		}
		
		
		//세로보기(열) 엔터버튼 이벤트
		function eExampleDescEventMatrixY() {
			var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollExampleMatrixY");
			var inputs = table.getElements('input[name=eExampleDescMatrixY]');
			var len = inputs.length;
			$each(inputs,function(el,index) {				
				el.focus();
				el.removeEvents('keydown');
				el.addEvent('keydown', function(e) {
						if(e.key == 'enter') {
							new Event(e).stop();
							if(index == len-1) {
								addExamplePlusMatrixY();
							} else {
								if(el.getParent('tr').getNext()==null){
									addExamplePlusMatrixY();
								}else{
									el.getParent('tr').getNext().getElement('input[name=eExampleDescMatrixY]').focus();
								}
							}
						}
				});	
			});			
		}

		//보기 플러스 클릭 
		$('<%=id%>_examplePlus').addEvent('click',function(){
			addExamplePlus();
		});	
		
		//순위선택 보기 플러스 클릭 
		$('<%=id%>_rankingExamplePlus').addEvent('click',function(){
			addRankingExamplePlus();
		})
		
		//가로보기(행) 플러스 클릭 
		$('<%=id%>_examplePlusMatrixX').addEvent('click',function(){
			addExamplePlusMatrixX();
		});
		
		//세로보기(열) 플러스 클릭 
		$('<%=id%>_examplePlusMatrixY').addEvent('click',function(){
			addExamplePlusMatrixY();
		});


		//보기 마이너스 클릭 
		$('<%=id%>_exampleMinus').addEvent('click',function(){			
			 var file_length = 0;
			 var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollExample");
			 if (table.rows.length - 1 > file_length)
			 {
			  table.deleteRow(table.rows.length - 1);
			  flen--;
			 }
		});

		//순위 선택 보기 마이너스 클릭 
		$('<%=id%>_rankingExampleMinus').addEvent('click',function(){			
			 var file_length = 0;
			 var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollRankingExample");
			 if (table.rows.length - 1 > file_length)
			 {
			  table.deleteRow(table.rows.length - 1);
			  flen--;
			 }
		});

		//가로보기 마이너스 클릭 
		$('<%=id%>_exampleMinusMatrixX').addEvent('click',function(){			
			 var file_length = 0;
			 var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollExampleMatrixX");
			 if (table.rows.length - 1 > file_length)
			 {
			  table.deleteRow(table.rows.length - 1);
			  flen--;
			 }
		});
		
		

		//세로보기 마이너스 클릭 
		$('<%=id%>_exampleMinusMatrixY').addEvent('click',function(){			
			 var file_length = 0;
			 var table = $('<%=id%>_question_form').getElementById("<%=id%>_pollExampleMatrixY");
			 if (table.rows.length - 1 > file_length)
			 {
			  table.deleteRow(table.rows.length - 1);
			  flen--;
			 }
		});


		/***********************************************/
		/* 문항삭제 
		/***********************************************/		
		$('<%=id%>').deleteQuestion = function(seq){
			
			var frm =  $('<%=id%>_question_form');			
			var questionNo = frm.eQuestionNo.value;


			if(!confirm(questionNo+'번을 정말 삭제하시겠습니까?')){
				return;
			}
			
			nemoRequest.init( 
					{
						busyWindowId: '<%=id%>_question'  // busy 를 표시할 window
						, updateWindowId: '<%=id%>_info' // 완료후 버튼,힌트 가 랜더링될 windowid
						, url: 'content/poll/poll.do?id=<%=id%>&method=deleteQuestion&eQuestionID='+seq+'&eQuestionNo='+questionNo
						//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element		
						, onSuccess: function(html,els,resHTML) {
							$('<%=id%>_poll_frame').src = "pages/content/poll/poll_preview.jsp?id=<%=id %>&pollID=<%=pollID %>";				
							refreshWindow('<%=id%>_question');
							
						}
					});

			nemoRequest.post($('<%=id%>_question_form'));		
		}

		/***********************************************/
		/* 문항저장 클릭 - 
		/***********************************************/
		$('<%=id%>').saveQuestion = function(questionID,questionNo) {

			var frm = $('<%=id%>_question_form');
			//필수입력 조건 체크
			if(!checkFormValue($('<%=id%>_question_form'))) {
				return;
			}			

			// 시작문구(eQuestionHead), 종료문구(eQuestionText)의 < , script , /> 등 스크립트 사용 의심 문자 필터	
			if ( frm.eQuestionHead.value.indexOf('<') >= 0 || frm.eQuestionHead.value.indexOf('/>') >= 0 || frm.eQuestionHead.value.indexOf('script') >= 0
				 || frm.eQuestionText.value.indexOf('<') >= 0 || frm.eQuestionText.value.indexOf('/>') >= 0 || frm.eQuestionText.value.indexOf('script') >= 0 ){
				alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
				return;
			}
			
			//일반 질문 
			if(frm.eQuestionType[0].checked==true){

				if(frm.eExampleGubun[1].checked==true){
					if($('<%=id%>_question_form').eExampleMultiCount.value=='' || $('<%=id%>_question_form').eExampleMultiCount.value=='0'){
						alert('복수응답 허용 갯수를 1이상의 숫자만 입력하세요');
						return;
					}
				}	
				
				//객관식이라면 보기 개수는 2개이상 되어야 한다.
				if(frm.eExampleType[0].checked==true){
					if(frm.eCodeType.value=='CUSTOM'){
						if(frm.eExampleDesc.length==null){
							alert('보기는 2개이상 등록되어야 합니다.');
							return;
						}else{
							for(i=0;i<frm.eExampleDesc.length;i++){
								if(frm.eExampleDesc[i].value==''){
									alert('보기 지문을 입력하세요');
									return;
								}
							}
							for(i=0;i<frm.eExampleDesc.length;i++){
								if ( frm.eExampleDesc[i].value.indexOf('<') >= 0 || frm.eExampleDesc[i].value.indexOf('/>') >= 0
									 || frm.eExampleDesc[i].value.indexOf('script') >= 0 ){
										alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
										return;
								}
							}
						}
					}
				}				
				
				//순위선택이라면 보기 개수는 2개이상 되어야 한다.
				if(frm.eExampleType[2].checked==true){
					if(frm.eCodeType.value=='CUSTOM'){
						if(frm.eRankingExampleDesc.length==null){
							alert('보기는 2개이상 등록되어야 합니다.');
							return;
						}else{
							for(i=0;i<frm.eRankingExampleDesc.length;i++){
								if(frm.eRankingExampleDesc[i].value==''){
									alert('보기 지문을 입력하세요');
									return;
								}
							}

							for(i=0;i<frm.eRankingExampleDesc.length;i++){
								if ( frm.eRankingExampleDesc[i].value.indexOf('<') >= 0 || frm.eRankingExampleDesc[i].value.indexOf('/>') >= 0
									 || frm.eRankingExampleDesc[i].value.indexOf('script') >= 0 ){
											alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
											return;
								}
							}
						}
					}
				}				
			
			}
			//매트릭스 질문일 경우
			else{
				if(frm.eExampleDescMatrixX.length==null && frm.eExampleDescMatrixY.length==null){
					alert('매트릭스 보기는 열이  1개이상 행이 1개이상이어야 합니다.');
					return;
				}
				if(frm.eRequiredYNMatrix.checked==false){
					alert('매트릭스는 필수응답이어야 합니다.');
					frm.eRequiredYNMatrix.focus();
					return;
				
				}
				for(i=0;i<frm.eExampleDescMatrixX.length;i++){
					if(frm.eExampleDescMatrixX[i].value==''){
						alert('행보기 지문을 입력하세요');
						return;
					}

					if ( frm.eExampleDescMatrixX[i].value.indexOf('<') >= 0 || frm.eExampleDescMatrixX[i].value.indexOf('/>') >= 0
						 || frm.eExampleDescMatrixX[i].value.indexOf('script') >= 0 ){
							alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
							return;
					}
				}
				for(i=0;i<frm.eExampleDescMatrixY.length;i++){
					if(frm.eExampleDescMatrixY[i].value==''){
						alert('열보기 지문을 입력하세요');
						return;
					}

					if ( frm.eExampleDescMatrixY[i].value.indexOf('<') >= 0 || frm.eExampleDescMatrixY[i].value.indexOf('/>') >= 0
						 || frm.eExampleDescMatrixY[i].value.indexOf('script') >= 0 ){
							alert('< , script , /> 등 스크립트 사용이 의심되는 문자는\n사용 할 수 없습니다.');
							return;
					}
				}
			}
			
			
			//copyForm( $('<%=id%>_rform'), $('<%=id%>_question_form') );

			if(questionID == '0' || questionID == '-1') {
				goUrl = 'content/poll/poll.do?id=<%=id%>&method=insertQuestion';
			} else {
				goUrl = 'content/poll/poll.do?id=<%=id%>&method=updateQuestion&eQuestionID='+questionID;
			}
	
			
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_question'  // busy 를 표시할 window
				, updateWindowId: '<%=id%>_info' // 완료후 버튼,힌트 가 랜더링될 windowid
				, url: goUrl
				//, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element		
				, onSuccess: function(html,els,resHTML) {
					$('<%=id%>_poll_frame').src = "pages/content/poll/poll_preview.jsp?id=<%=id %>&pollID=<%=pollID %>";	
					if(questionID=='0'){
						refreshWindow('<%=id%>_question');	
					}else{
						$('<%=id%>_question').selectQuestion(questionNo);
					}
				}
			});

			nemoRequest.post($('<%=id%>_question_form'));		
		}
		
		/***********************************************/
		/* 기존 문항 불러오기 - 창 
		/***********************************************/
		$('<%=id%>').openQuestionCopyForm = function() {
			nemoWindow(
					{
						'id': '<%=id%>_question_copy',		
						noOtherClose: true,
						busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
						width: 810,
						height: $('mainColumn').style.height.toInt(),
						title: '기존문항 불러오기',
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'pages/content/poll/poll_question_copy.jsp?id=<%=id%>_question_copy&preID=<%=id%>_question'
					}
				);
		}

		/***********************************************/
		/* 기존 문항 불러오기 - 선택
		/***********************************************/
		$('<%=id%>_question').copyQuestion = function(){
			var frm = $('<%=id%>_question_copy_list_form');
			var checked = isChecked( frm.elements['ePollInfo']  );

			if( checked == 0 ) {
				alert("대상 질문을 선택하세요");
				return;
			}
			
			nemoRequest.init( 
			{
				busyWindowId: '<%=id%>_question'  // busy 를 표시할 window
				, updateWindowId: '<%=id%>_question' // 완료후 버튼,힌트 가 랜더링될 windowid
				, url: 'content/poll/poll.do?id=<%=id%>&ePollID=<%=pollID%>&ePollTemplateID=<%=pollTemplateID%>&eQuestionNo=<%=selQuestionNos %>&eQuestionID=${pollQuestion.questionID}&method=copyPollQuestion'
				, update: MochaUI.Windows.instances.get('<%=id%>_question').contentEl // 해당 id 전체 재시작	
				, onSuccess: function(html,els,resHTML) {										
					closeWindow( $('<%=id%>_question_copy') );
				}
			});

			nemoRequest.post(frm);			
		}
		
		/***********************************************/
		/* 멀티미디어 설정
		/***********************************************/
		$('<%=id%>').openMultimediaForm = function(exampleNo) {
			var multimediaUrl = "";
			if(exampleNo == '0'){
				multimediaUrl = $('<%=id%>_question_form').eQuestionFileURL.value;
			}else if($('<%=id%>_question_form').eExampleDesc.length==null){
				multimediaUrl = $('<%=id%>_question_form').eFileURL.value;
			}else{
				multimediaUrl = $('<%=id%>_question_form').eFileURL[exampleNo-1].value;
			}

			nemoWindow(
					{
						'id': '<%=id%>_multimedia',		
						noOtherClose: true,
						busyEl: '<%=id%>', // 창을 열기까지 busy 가 표시될 element
						width: 710,
						height: $('mainColumn').style.height.toInt(),
						title: '멀티미디어 설정',
						type: 'modal',
						loadMethod: 'xhr',
						contentURL: 'content/poll/poll.do?id=<%=id%>_multimedia&preID=<%=id%>&method=editMultimedia&exampleNo='+exampleNo+'&multimediaURL='+multimediaUrl+'&pollID=<%=pollID%>&questionID=${pollQuestion.questionID}'
					}
				);
		}
		
		/* 리스트 표시 */
		window.addEvent("domready",function () {
			<% if(isExistQuestion.equals("NO")){%>
				alert('해당 설문 템플릿에 문항치환변수가 존재하지 않습니다.');
				closeWindow('<%=id%>_question');
			<%}%>
			
			// 셀렉트 박스 렌더링		
			makeSelectBox.render($('<%=id%>_question_form'));
			<c:if test="${pollQuestion.exampleType!='2' && pollQuestion.exampleType!='3' && pollQuestion.exampleType!='4' && pollQuestion.exampleType!='5'}">
				eExampleDescEvent();
			</c:if>
		});
				
	</script>