<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="web.content.poll.service.PollService"%>
<%@page import="web.content.poll.control.PollControlHelper"%>
<%@ page import="java.util.*"%>

<%
	String id = request.getParameter("id");
	String preID = request.getParameter("preID");
	String pollID = request.getParameter("pollID");
	String exampleNo = request.getParameter("exampleNo");
	String multimediaURL = request.getParameter("multimediaURL");
	String viewTag = "";
	String questionID = request.getParameter("questionID");
	
	if(Integer.parseInt(questionID) < 1){
		PollService service = PollControlHelper.getUserService(application);
		questionID = service.getMaxQuestionID(Integer.parseInt(pollID))+"";
	}
	
	if(multimediaURL != null && !multimediaURL.equals("")) {
			if(multimediaURL.indexOf(".swf") < 0 && multimediaURL.indexOf(".SWF") < 0){
				viewTag = "<img src='"+multimediaURL+"'>";
			}else{
				viewTag = "<embed pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' src='"+multimediaURL+"'>";
			}
	}
	//System.out.println("id : "+id);
	//System.out.println("preID : "+preID);
	//System.out.println("pollID : "+pollID);
	//System.out.println("exampleNo : "+exampleNo);
	//System.out.println("multimediaURL : "+multimediaURL);
	//System.out.println("questionID : "+questionID);
	if(!exampleNo.equals("0")){
%>

 	<div id="<%=id%>_divQMultimedia" style="margin-top:10px">
	<div>		
		<form id="<%=id%>_multimedia_form" name="<%=id%>_multimedia_form" method="post">	
			<input type="hidden" id="fileURL" name="fileURL" value="" />
			<input type="hidden" id="tempFileURL" name="tempFileURL" value="<%=multimediaURL%>" />
			<img src='images/tag_blue.png'> 멀티미디어는 <strong>이미지</strong> 또는 <strong>플래쉬</strong> 파일을 설문지(질문, 보기)에 <strong>추가</strong>할 수 있는 기능입니다. <br/>
			<img src='images/tag_blue.png'> 용량은 <strong>50MB이하</strong>로 업로드 하셔야 합니다. <br/>
			<img src='images/tag_blue.png'> <strong>레이아웃</strong>은 업로드한 멀티미디어 파일과 보기의 <strong>배치</strong>를 설정합니다. <br/>
			<img src='images/tag_red.png'> 파일 선택 후에는 꼭 <strong>[파일업로드]</strong> 버튼을 클릭해주십시오.
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">보기</td>
				<td class="ctbl td">
					<%=exampleNo %>. <span id="<%=id%>_example_desc"></span>
				</td>
			</tr>	
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1" width="100px">레이아웃</td>
				<td class="ctbl td">
					<table>
						<tbody>
							<tr>
								<td align="center" width="60px"> <img src="/images/position1.gif"> </td>
								<td align="center" width="60px"> <img src="/images/position2.gif"> </td>
								<td align="center" width="60px"> <img src="/images/position3.gif"> </td>
								<td align="center" width="60px"> <img src="/images/position4.gif"> </td>
							</tr>
							<tr>
								<td align="center"><input type="radio" id="layoutType" name="layoutType" class="notBorder" value="1" checked></td>
								<td align="center"><input type="radio" id="layoutType" name="layoutType" class="notBorder" value="2"></td>
								<td align="center"><input type="radio" id="layoutType" name="layoutType" class="notBorder" value="3"></td>
								<td align="center"><input type="radio" id="layoutType" name="layoutType" class="notBorder" value="4"></td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>		
			<tr>
				<td class="ctbl ttd1" width="100px">파일선택</td>
				<td class="ctbl td">
					<div  style="float:left" id="<%=id%>uploadWrapper" ></div>
					<div style="float:left;padding:22px">
					<a id="<%=id%>_uploadBtn" href="javascript:$('<%=id%>').uploadFile()" class="web20button bigblue" title="선택한 파일을 업로드 하신 후  적용버튼을 클릭하세요">파일 업로드</a>
					</div>
					
				</td>
			</tr>	
			<%if(multimediaURL != null && !multimediaURL.equals("")) {%>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl td" colspan="2" align="center">
					<%=viewTag %>
				</td>
			</tr>
			<%} %>
			</tbody>
			</table>				
		</form>
	</div>
	<div>
	<div style="float:right;padding-top:10px"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>
	<%if(multimediaURL != null && !multimediaURL.equals("")) {%>
		<div style="float:right;padding-right:10px;padding-top:10px" ><a href="javascript:$('<%=id%>').deleteMultimedia()"  class="web20button bigpink">삭 제</a></div>
	<%} %>
	<div style="float:right;padding-right:10px;padding-top:10px" ><a id="<%=id%>_saveBtn2" href="javascript:$('<%=id%>').saveMultimedia()" class="web20button bigblue">적 용</a></div>
	</div>
	<script type="text/javascript">
		/***********************************************/
		/* 파일 업로드 저장 버튼 클릭
		/***********************************************/
	
		$('<%=id%>').uploadFile = function() {
			
			// 추가 일때 필요한 key
			$('<%=id%>').uploader.pollID = <%=pollID%>;
			// 파라미터 셋팅
			$('<%=id%>').uploader.setParameter({ 'id': '<%=id%>', 'pollID' : <%=pollID%>, 'questionID' : <%=questionID%>, 'exampleNo' : <%=exampleNo%> });
			// 업로드
			$('<%=id%>').uploader.upload();
			
		}	

		/***********************************************/
		/* 파일 업로드 랜더링
		/***********************************************/
		$('<%=id%>').renderUpload = function() {
			
			$('<%=id%>').uploader = new SwFileUpload('<%=id%>UploadFlash', {
				width: 350,
				container: '<%=id%>uploadWrapper',
				fileTypeName: '파일업로드(이미지,플래쉬)',
				allowFileType: '*.gif;*.jpg;*.bmp;*.jpeg;*.png;*.swf',
				uploadPage: 'content/poll/poll.do?method=fileUpload',
				onComplete: function( isError, fileNameArray ) { // 업로드 완료
					var fileName = fileNameArray[0];
					fileName = fileName.substring(fileName.indexOf("."), fileName.length);
					$('<%=id%>_multimedia_form').fileURL.value='/upload/poll/<%=pollID%>_<%=questionID%>_<%=exampleNo%>'+fileName;
				},
				onNotExistsUploadFile: function() { // 업로드 할 파일이 없을때
					alert('업로드할 파일을 선택해 주시기 바랍니다');
				}
				
			}).render();
	
		}

		/***********************************************/
		/* 멀티미디어 적용 버튼 클릭  
		/***********************************************/
		$('<%=id%>').saveMultimedia = function() {
			var frm = $('<%=id%>_multimedia_form');
			var layoutType = "";
			var checked = isChecked( frm.elements['layoutType']  );
			if( checked == 0 ) {
				alert('레이아웃을 선택하세요');
				return;
			}
			if(frm.fileURL.value==''){
				<%if(multimediaURL == null && multimediaURL.equals("")) {%>
					alert('파일을 업로드 하세요');
					return;
				<%}else{%>
					$('<%=id%>_multimedia_form').fileURL.value = $('<%=id%>_multimedia_form').tempFileURL.value;
				<%}%>
			}
			
			for(var i=0;i<frm.layoutType.length;i++){
				if(frm.layoutType[i].checked==true){
					layoutType = frm.layoutType[i].value;
				}
			}
			
			if($('<%=preID%>_question_form').eExampleDesc.length==null){
				$('<%=preID%>_question_form').eFileURL.value = $('<%=id%>_multimedia_form').fileURL.value;
				$('<%=preID%>_question_form').eLayoutType.value = layoutType;
			}else{
				$('<%=preID%>_question_form').eFileURL[<%=Integer.parseInt(exampleNo)-1%>].value = $('<%=id%>_multimedia_form').fileURL.value;
				$('<%=preID%>_question_form').eLayoutType[<%=Integer.parseInt(exampleNo)-1%>].value = layoutType;
			}
			closeWindow($('<%=id%>'));
		}

		/***********************************************/
		/* 멀티미디어 삭제 버튼 클릭  
		/***********************************************/
		$('<%=id%>').deleteMultimedia = function() {
			if($('<%=preID%>_question_form').eExampleDesc.length==null){
				$('<%=preID%>_question_form').eFileURL.value = '';
				$('<%=preID%>_question_form').eLayoutType.value = '';
			}else{
				$('<%=preID%>_question_form').eFileURL[<%=Integer.parseInt(exampleNo)-1%>].value = '';
				$('<%=preID%>_question_form').eLayoutType[<%=Integer.parseInt(exampleNo)-1%>].value = '';
			}
			closeWindow($('<%=id%>'));
		}
		
		/* 리스트 표시 */
		window.addEvent("domready",function () {
			//파일 업로드 렌더링
			$('<%=id%>').renderUpload();

			// 셀렉트 박스 렌더링		
			makeSelectBox.render($('<%=id%>_multimedia_form'));

			//보기 문구 표시
			var exampleDesc = "";
			if($('<%=preID%>_question_form').eExampleDesc.length==null){
				exampleDesc = $('<%=preID%>_question_form').eExampleDesc.value;
			}else{
				exampleDesc = $('<%=preID%>_question_form').eExampleDesc[<%=Integer.parseInt(exampleNo)-1%>].value;
			}
			
			$('<%=id%>_example_desc').innerHTML=exampleDesc;
		});
				
	</script>
	
<%}else{%>
	<div id="<%=id%>_divQMultimedia" style="margin-top:10px">
	<div>		
		<form id="<%=id%>_multimedia_form" name="<%=id%>_multimedia_form" method="post">	
			<input type="hidden" id="fileURL" name="fileURL" value="" />	
			<input type="hidden" id="tempFileURL" name="tempFileURL" value="<%=multimediaURL%>" />	
			<img src='images/tag_blue.png'> 멀티미디어는 <strong>이미지</strong> 또는 <strong>플래쉬</strong> 파일을 설문지(질문, 보기)에 <strong>추가</strong>할 수 있는 기능입니다. <br/>
			<img src='images/tag_blue.png'> 용량은 <strong>50MB이하</strong>로 업로드 하셔야 합니다. <br/>		
			<img src='images/tag_blue.png'> <strong>레이아웃</strong>은 업로드한 멀티미디어 파일과 질문의 <strong>배치</strong>를 설정합니다. <br/>
			<img src='images/tag_red.png'> 파일 선택 후에는 꼭 <strong>[파일업로드]</strong> 버튼을 클릭해주십시오.
			<table class="ctbl" width="100%">
			<tbody>
			<tr>
				<td class="ctbl ttd1" width="100px">레이아웃</td>
				<td class="ctbl td">
					<table>
						<tbody>
							<tr>
								<td align="center" width="60px"> <img src="/images/position1.gif"> </td>
								<td align="center" width="60px"> <img src="/images/position2.gif"> </td>
							</tr>
							<tr>
								<td align="center"><input type="radio" id="layoutType" name="layoutType" class="notBorder" value="1" checked></td>
								<td align="center"><input type="radio" id="layoutType" name="layoutType" class="notBorder" value="2"></td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl ttd1" width="100px">파일선택</td>
				<td class="ctbl td">
					<div  style="float:left" id="<%=id%>uploadWrapper" ></div>
					<div style="float:left;padding:22px">
					<a id="<%=id%>_uploadBtn" href="javascript:$('<%=id%>').uploadFile()" class="web20button bigblue" title="선택한 파일을 업로드 하신 후  적용버튼을 클릭하세요">파일 업로드</a>
					</div>
				</td>
			</tr>	
			<%if(multimediaURL != null && !multimediaURL.equals("")) {%>
			<tr>
				<td colspan="2" class="ctbl line"></td>
			</tr>	
			<tr>
				<td class="ctbl td" colspan="2" align="center">
					<%=viewTag %>
				</td>
			</tr>
			<%} %>
			</tbody>
			</table>				
		</form>
	</div>
	<div>
	<div style="float:right;padding-top:10px"><a href="javascript:closeWindow($('<%=id%>'))"  class="web20button bigpink">닫 기</a></div>
	<%if(multimediaURL != null && !multimediaURL.equals("")) {%>
		<div style="float:right;padding-right:10px;padding-top:10px" ><a href="javascript:$('<%=id%>').deleteMultimedia()"  class="web20button bigpink">삭 제</a></div>
	<%} %>
	<div style="float:right;padding-right:10px;padding-top:10px" ><a id="<%=id%>_saveBtn2" href="javascript:$('<%=id%>').saveMultimedia()" class="web20button bigblue">적 용</a></div>
	</div>
	<script type="text/javascript">
		/***********************************************/
		/* 파일 업로드 저장 버튼 클릭
		/***********************************************/
	
		$('<%=id%>').uploadFile = function() {
			
			// 추가 일때 필요한 key
			$('<%=id%>').uploader.pollID = <%=pollID%>;
			// 파라미터 셋팅
			$('<%=id%>').uploader.setParameter({ 'id': '<%=id%>', 'pollID' : <%=pollID%>, 'questionID' : <%=questionID%>, 'exampleNo' : <%=exampleNo%> });
			// 업로드
			$('<%=id%>').uploader.upload();
			
		}	

		/***********************************************/
		/* 파일 업로드 랜더링
		/***********************************************/
		$('<%=id%>').renderUpload = function() {
			
			$('<%=id%>').uploader = new SwFileUpload('<%=id%>UploadFlash', {
				width: 350,
				container: '<%=id%>uploadWrapper',
				fileTypeName: '파일업로드(이미지,플래쉬)',
				allowFileType: '*.gif;*.jpg;*.bmp;*.jpeg;*.png;*.swf',
				uploadPage: 'content/poll/poll.do?method=fileUpload',
				onComplete: function( isError, fileNameArray ) { // 업로드 완료
					var fileName = fileNameArray[0];
					fileName = fileName.substring(fileName.indexOf("."), fileName.length);
					$('<%=id%>_multimedia_form').fileURL.value='/upload/poll/<%=pollID%>_<%=questionID%>_<%=exampleNo%>'+fileName;
				},
				onNotExistsUploadFile: function() { // 업로드 할 파일이 없을때
					alert('업로드할 파일을 선택해 주시기 바랍니다');
				}
				
			}).render();
	
		}

		/***********************************************/
		/* 멀티미디어 적용 버튼 클릭  
		/***********************************************/
		$('<%=id%>').saveMultimedia = function() {
			var frm = $('<%=id%>_multimedia_form');
			var layoutType = "";
			var checked = isChecked( frm.elements['layoutType']  );
			if( checked == 0 ) {
				alert('레이아웃을 선택하세요');
				return;
			}
			if(frm.fileURL.value==''){
				<%if(multimediaURL == null && multimediaURL.equals("")) {%>
					alert('파일을 업로드 하세요');
					return;
				<%}else{%>
				$('<%=id%>_multimedia_form').fileURL.value = $('<%=id%>_multimedia_form').tempFileURL.value;
				<%}%>
			}
			
			for(var i=0;i<frm.layoutType.length;i++){
				if(frm.layoutType[i].checked==true){
					layoutType = frm.layoutType[i].value;
				}
			}
			
			
			$('<%=preID%>_question_form').eQuestionFileURL.value = $('<%=id%>_multimedia_form').fileURL.value;
			$('<%=preID%>_question_form').eQuestionLayoutType.value = layoutType;
			
			closeWindow($('<%=id%>'));
		}

		/***********************************************/
		/* 멀티미디어 삭제 버튼 클릭  
		/***********************************************/
		$('<%=id%>').deleteMultimedia = function() {
			$('<%=preID%>_question_form').eQuestionFileURL.value = '';
			$('<%=preID%>_question_form').eQuestionLayoutType.value = '';
			closeWindow($('<%=id%>'));
		}
		
		/* 리스트 표시 */
		window.addEvent("domready",function () {
			//파일 업로드 렌더링
			$('<%=id%>').renderUpload();

			// 셀렉트 박스 렌더링		
			makeSelectBox.render($('<%=id%>_multimedia_form'));
		});
				
	</script>
<%}%>