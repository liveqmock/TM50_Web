<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.content.poll.control.PollControlHelper" %>
<%@ page import="web.content.poll.service.PollService" %>
<%@ page import="web.content.poll.model.PollInfo" %>
<%@ page import="web.common.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>설문참여하기</title>
<link href="/fckeditor/editor/css/fck_editorarea.css" type="text/css" rel="stylesheet" />
</head>
<body>

<%
	int pollID = 0;
	int sendID = 0;
	int massmailID = 0;
	int scheduleID = 0;
	String email = "";
	
	String testPoll = ServletUtil.getParamString(request,"testPoll");
	
	

	if(testPoll.equals("Y")){
		pollID = ServletUtil.getParamInt(request,"pollID","0");
	}else{
		pollID = ServletUtil.getParamIntBase64(request,"PLD","0");	
		sendID = ServletUtil.getParamIntBase64(request,"SND","0");
		massmailID = ServletUtil.getParamIntBase64(request,"MMD","0");
		scheduleID = ServletUtil.getParamIntBase64(request,"SDD","0");
		email = ServletUtil.getParamStringBase64(request,"EMA");
	}
	
	

	PollService pollService = PollControlHelper.getUserService(application);		
	PollInfo pollInfo = pollService.viewPollInfo(pollID);
	
	if(testPoll.equals("Y") && pollID==0){
%>
		<center><h2>존재하지 않는 설문입니다.</h2></center>		
<%
	}else if(!testPoll.equals("Y") && pollService.checkPollMassMail(pollID, massmailID)<=0){
%>
		<center><h2>존재하지 않는 설문입니다.</h2></center>		
<%			
	}else{
		
		if(pollService.checkPollAnswer(pollID, massmailID, scheduleID, sendID, email)>0){
%>
		<center><h2>이미 설문에 응답하셨습니다.</h2></center>
<%		
			return;
		}
		
		if(pollService.checkPollEndDate(pollID, DateUtils.getDateTimeString())>0){
%>
		<center><h2>해당 설문은 이미 종료되었습니다.</h2></center>
<%		
			return;
		}
		
		if(!pollInfo.getPollEndType().equals("1") && pollService.checkPollAimAnswerCnt(pollID, massmailID, scheduleID)>0){
%>
		<center><h2>해당 설문은 목표 응답 수 달성으로 종료되었습니다.</h2></center>
<%		
			return;
		}

		
%>	
<form name="form" method="post" action="/content/poll/poll.do?method=responseAnswer">	
<input type="hidden" id="pollID" name="pollID" value="<%=pollID %>">
<input type="hidden" id="massmailID" name="massmailID" value="<%=massmailID %>">
<input type="hidden" id="scheduleID" name="scheduleID" value="<%=scheduleID %>">
<input type="hidden" id="sendID" name="sendID" value="<%=sendID %>"><br>	
<input type="hidden" id="email" name="email" value="<%=email %>"><br>
<div align="right" style="width:900px">
	<a href="javascript:window.print()"><img src="/images/btn_print.gif" width="100px" border="0"></a>
</div>
<div style="border: 1px solid #CCCCCC;width:900px">
<%=pollInfo.getResultPollHTML()%>
</div>
<div align="center" style="width:900px;margin-top:10px">
<a href="javascript:sendPoll()"><img src="/images/poll_submit.gif" border="0"></a>
</div>	
</form>
<script type="text/javascript">
	var tempSeq1 = "";
	var tempSeq2 = "";
	var tempSeq3 = "";
	var tempSeq4 = "";
	function goToQuestionNum(seq2, seq3){
		var obj = "";
		var questionType ="";
		var exampleType = "";
		var exampleGubun = "";
		var returnID = "0";
		//스킵패턴 처리 - 기존 처리 사항 복원
		
		if(tempSeq1 == seq2){			
			obj = document.getElementsByName('skipAnswer'+tempSeq1+'_'+tempSeq2);
			for(var i=0; i < obj.length; i++ ) {
				questionType = document.getElementsByName("questionType_"+obj[i].value)[0].value;
				exampleType = document.getElementsByName("exampleType_"+obj[i].value)[0].value;
				exampleGubun = document.getElementsByName("exampleGubun_"+obj[i].value)[0].value;
				document.getElementsByName("requiredYN_"+obj[i].value)[0].value = document.getElementsByName("tempRequiredYN_"+obj[i].value)[0].value;
				/*일반설문*/
				if(questionType == '1'){
					/*객관식 단일, 척도*/	
					if((exampleType == '1' && exampleGubun== '1') || exampleType == '5'){
						var disabledObj =  document.getElementsByName('exampleGubun_single_'+obj[i].value);
						for(var j=0; j < disabledObj.length; j++){
							var textObj = document.getElementsByName('exampleExYN_'+obj[i].value+'_'+(j+1));
							if(textObj.length > 0){
								textObj[0].disabled=false;
								
							}
							disabledObj[j].disabled=false;
							//disabledObj[j].checked=false;
						}
					}
				}
				/*객관식 복수*/
				if(exampleType == '1' && exampleGubun== '2'){
					var  exampleCount = document.getElementsByName("exampleCount_"+obj[i].value)[0].value;
					for(var j=1; j <= exampleCount; j++){
						var disabledObj = document.getElementById("exampleGubun_multi_"+obj[i].value+"_"+j);
						disabledObj.disabled=false;
						//disabledObj.checked=false;
					}
				}
				/*주관식, 척도*/	
				if(exampleType == '2' || exampleType == '4'){
					var disabledObj = document.getElementsByName("exampleType_text_"+obj[i].value);
					disabledObj[0].disabled=false;
				}
				/*순위선택*/
				if(exampleType == '3'){
					var  exampleCount = document.getElementsByName("exampleCount_"+obj[i].value)[0].value;
					for(var j=1; j <= exampleCount; j++){
						var disabledObj = document.getElementsByName("exampleGubun_select_"+obj[i].value+"_"+j);
						disabledObj[0].disabled=false;
					}
				}
			}
		}

		//스킵패턴 처리 - 기존 처리 사항 복원( 스킵문항 답변 재선택시 스킵된 문항 스킵 해제 )
		if(tempSeq3 == seq2){			
			obj = document.getElementsByName('skipAnswer'+tempSeq3+'_'+tempSeq4);
			for(var i=0; i < obj.length; i++ ) {
				questionType = document.getElementsByName("questionType_"+obj[i].value)[0].value;
				exampleType = document.getElementsByName("exampleType_"+obj[i].value)[0].value;
				exampleGubun = document.getElementsByName("exampleGubun_"+obj[i].value)[0].value;
				document.getElementsByName("requiredYN_"+obj[i].value)[0].value = document.getElementsByName("tempRequiredYN_"+obj[i].value)[0].value;
				/*일반설문*/
				if(questionType == '1'){
					/*객관식 단일, 척도*/	
					if((exampleType == '1' && exampleGubun== '1') || exampleType == '5'){
						var disabledObj =  document.getElementsByName('exampleGubun_single_'+obj[i].value);
						for(var j=0; j < disabledObj.length; j++){
							var textObj = document.getElementsByName('exampleExYN_'+obj[i].value+'_'+(j+1));
							if(textObj.length > 0){
								textObj[0].disabled=false;
								
							}
							disabledObj[j].disabled=false;
							//disabledObj[j].checked=false;
						}
					}
				}
				/*객관식 복수*/
				if(exampleType == '1' && exampleGubun== '2'){
					var  exampleCount = document.getElementsByName("exampleCount_"+obj[i].value)[0].value;
					for(var j=1; j <= exampleCount; j++){
						var disabledObj = document.getElementById("exampleGubun_multi_"+obj[i].value+"_"+j);
						disabledObj.disabled=false;
						//disabledObj.checked=false;
					}
				}
				/*주관식, 척도*/	
				if(exampleType == '2' || exampleType == '4'){
					var disabledObj = document.getElementsByName("exampleType_text_"+obj[i].value);
					disabledObj[0].disabled=false;
				}
				/*순위선택*/
				if(exampleType == '3'){
					var  exampleCount = document.getElementsByName("exampleCount_"+obj[i].value)[0].value;
					for(var j=1; j <= exampleCount; j++){
						var disabledObj = document.getElementsByName("exampleGubun_select_"+obj[i].value+"_"+j);
						disabledObj[0].disabled=false;
					}
				}
			}
		}
		
		//스킵패턴 처리 
		obj = document.getElementsByName('skipAnswer'+seq2+'_'+seq3);
		
		for(var i=0; i < obj.length; i++ ) {
			questionType = document.getElementsByName("questionType_"+obj[i].value)[0].value;
			exampleType = document.getElementsByName("exampleType_"+obj[i].value)[0].value;
			exampleGubun = document.getElementsByName("exampleGubun_"+obj[i].value)[0].value;
			returnID = document.getElementsByName("goToQuestionNum_"+seq2+"_"+seq3)[0].value;
			document.getElementsByName("requiredYN_"+obj[i].value)[0].value ='N';

			/*일반설문*/
			if(questionType == '1'){
				/*객관식 단일, 척도*/	
				if((exampleType == '1' && exampleGubun== '1') || exampleType == '5'){
					var disabledObj =  document.getElementsByName('exampleGubun_single_'+obj[i].value);
					for(var j=0; j < disabledObj.length; j++){
						var textObj = document.getElementsByName('exampleExYN_'+obj[i].value+'_'+(j+1));
						if(textObj.length > 0){
							textObj[0].disabled=true;
							textObj[0].value="";
						}
						disabledObj[j].checked=false;
						disabledObj[j].disabled=true;
						
					}
				}
				/*객관식 복수*/
				if(exampleType == '1' && exampleGubun== '2'){
					var  exampleCount = document.getElementsByName("exampleCount_"+obj[i].value)[0].value;
					for(var j=1; j <= exampleCount; j++){
						var disabledObj = document.getElementsByName("exampleGubun_multi_"+obj[i].value+"_"+j);
						disabledObj[0].checked=false;
						disabledObj[0].disabled=true;
					}	
				}
				/*주관식, 척도*/	
				if(exampleType == '2' || exampleType == '4'){
					var disabledObj = document.getElementsByName("exampleType_text_"+obj[i].value);
					disabledObj[0].value="";
					disabledObj[0].disabled=true;
				}
				/*순위선택*/
				if(exampleType == '3'){
					var  exampleCount = document.getElementsByName("exampleCount_"+obj[i].value)[0].value;
					for(var j=1; j <= exampleCount; j++){
						var disabledObj = document.getElementsByName("exampleGubun_select_"+obj[i].value+"_"+j);
						disabledObj[0].selectedIndex=0;
						disabledObj[0].disabled=true;
					}	
				}
			}
		}
		tempSeq3 = tempSeq1;
		tempSeq4 = tempSeq2;
		tempSeq1 = seq2;
		tempSeq2 = seq3;	
		
		document.location.href="#"+returnID;
	}

	function checkRankingSelection(obj, tagName, nameInfo ,rankingCount){
		var checkValue = obj.value;
		var frm = document.form;
		for(var i=1; i <= rankingCount; i++ ) {
			checkObj = document.getElementsByName(tagName+i);
			if(checkValue != '' && i != nameInfo && checkObj[0].value == checkValue){
				alert("이미 선택한 순위 입니다.");
				obj.selectedIndex = 0;
				return;			
			}
		}
	}
	
	function onlyNumber(obj, maxvalue){
		var key = event.keyCode;
		if((key >=48 && key <=57) || (key >=96 && key <=105)){
			if(maxvalue > 0 && obj.value > maxvalue){
				alert(maxvalue + '이하의 숫자만 입력 가능합니다.');
				obj.value = maxvalue;
				obj.focus();
			}
		}else{
			obj.value = obj.value.substring(0, (obj.value.length-1));
		}	
	}
	
	function sendPoll(){
		<% if(testPoll.equals("Y")){%>
			alert('본 설문은  메일을 통해 발송되어야 설문응답이 됩니다.');
			return;
		<%}else{%>
			var frm = document.form;			
			frm.method.value = "insert" ;	
			frm.submit();
		<%}%>
	}
	
	function checkreset(questionID){
		var checkObj = document.getElementsByName("exampleGubun_single_"+questionID); // 단일응답용 객관식 보기 엘리먼트
		var exampleCnt = document.getElementsByName("exampleCount_"+questionID)[0].value;
		
		if(checkObj.length < 1){
			// 객관식 복수응답 일때 선택 취소
			for(var i=0; i < exampleCnt; i++){
				checkObj = document.getElementsByName("exampleGubun_multi_"+questionID+"_"+(i+1)); // 복수응답용 객관식 보기 엘리먼트
				checkObj[0].checked=false;
				var textObj = document.getElementsByName('exampleExYN_'+questionID+'_'+(i+1)); // 객관식 보기별 주관식 답안 텍스트 박스 엘리먼트
				if(textObj.length > 0){
					for(var j=0; j < textObj.length; j++ ){
						textObj[j].value="";
					}
				}
			}
		}else{	
			// 객관식 단일응답 일때 선택 취소
			for(var i=0; i < checkObj.length; i++ ){
				checkObj[i].checked=false;
				var textObj = document.getElementsByName('exampleExYN_'+questionID+'_'+(i+1)); // 객관식 보기별 주관식 답안 텍스트 박스 엘리먼트
				if(textObj.length > 0){
					for(var j=0; j < textObj.length; j++ ){
						textObj[j].value="";
					}
				}
			}
		}
	}
		
</script>	
<%	
	}
%>
</body>
</html>

