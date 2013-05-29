<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page import="web.common.util.*" %>    


<%
	String preID = request.getParameter("preID");
	String id = request.getParameter("id");
	String method = request.getParameter("method");



//****************************************************************************************************/
//첨부파일 팝업 
//****************************************************************************************************/
if(method.equals("attachedFile")) {
%>

<div class="search_wrapper" style="width:690px">
	<div style="float:left;width:690px" >
		<form id="<%=id%>_form" name="<%=id%>_form">
		<input type="hidden" id="eFileName" name="eFileName">
		<input type="hidden" id="eFileKey" name="eFileKey">
		<input type="hidden" id="eFileSize" name="eFileSize">
		<input type="hidden" id="eFileSizeByte" name="eFileSizeByte">
			<table class="ctbl" width="690px">
			<tbody>
				<tr>
					<td class="ctbl ttd1" width="100px">첨부파일 </td>
					<td class="ctbl td" colspan="3">
					<div  id="<%=id%>uploadWrapper" ></div>	
					<div style="float:right" ><a id="<%=id%>_saveBtn" href="javascript:$('<%=id%>').uploadFile()" class="web20button bigpink">파일업로드</a></div>
					</td>
				</tr>	
			<tr>
				<td colspan="10" class="ctbl line"></td>
			</tr>	
			</tbody>
			</table>
		</form>
	</div>
	<div style="float:right">
		<table border="0">
			<tbody>
			<tr>
				<td>	
				<div style="height:30px">
				<%if(!preID.equals("no")){ %>				
					<div style="float:left;height:20px;" class="btn_b" title="체크된 파일을  메일본문 안에  커서위치에 파일링크로 삽입합니다."><a style ="cursor:pointer" href="javascript:$('<%=id%>').insertAttachedFile()"><span>체크파일 링크삽입</span></a></div>
					<div style="float:left;height:20px;" class="btn_b" title="체크된 파일을  메일에 첨부합니다."><a style ="cursor:pointer" href="javascript:$('<%=id%>').attacheAttachedFile()"><span>체크파일 첨부</span></a></div>
				<%} %>
					<div style="float:left;height:20px;" class="btn_r" title="해당첨부파일은 사용중일 수 있으므로 삭제시 신중을 기해주시기 바랍니다." ><a style ="cursor:pointer" href="javascript:$('<%=id%>').deleteAttachedFile()"><span>체크파일 삭제</span></a></div>
				</div>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
</div>
	<div style="clear:both">
		<form name="<%=id%>_list_form" id="<%=id%>_list_form">
		<TABLE class="tbl" border="0" cellspacing="0" cellpadding="0" width="690px" >
		<thead>
			<tr>
			<th style="height:30px;width:30px"><input id="sCheckAll" class="notBorder" name="sCheckAll" type="checkbox" onclick="selectAll($('<%=id%>_list_form').eFileKey,this.checked)"/></th>		
			<th style="height:30px;">첨부파일명</th>
			<th style="height:30px;width:100px">사이즈</th>
			<th style="height:30px;width:150px">등록일</th>	
			<th style="height:30px;width:150px">최근발송일</th>	
			</tr>
		</thead>
		<tbody id="<%=id%>_grid_content">
		
		</tbody>
		</table>
		</form>
		<br>
	</div>

<script type="text/javascript">

/***********************************************/
/* 검색 조건 컨트롤 초기화
/***********************************************/

$('<%=id%>').init = function() {

	var frm = $('<%=id%>_list_form');

	// 셀렉트 박스 렌더링
	makeSelectBox.render( frm );

	$('<%=id%>').renderUpload();

	// 키보드 엔터 검색 만들기
	keyUpEvent( '<%=id%>', frm );


	$('<%=id%>').list();

}

/***********************************************/
/* 파일 업로드후  저장 버튼 클릭
/***********************************************/
$('<%=id%>').saveData = function(){

	var frm = $('<%=id%>_form');

	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'massmail/write/massmail.do?method=insertAttachedFile&id=<%=id%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {

		}
	});
	nemoRequest.post(frm);
			
		
}



/***********************************************/
/* 파일 업로드 랜더링
/***********************************************/
$('<%=id%>').renderUpload = function() {

 
  var frm = $('<%=id%>_form');


 $('<%=id%>').uploader = new SwFileUpload('<%=id%>Upload', {
  width: 550,
  container: '<%=id%>uploadWrapper',
  fileTypeName: '모든 파일',
  allowFileType: '*.*',
  limitSize:50,
  uploadPage: 'massmail/write/massmail.do?method=uploadFile',
  
  onComplete: function( isError, fileNameArray, fileSize ) { // 업로드 완료
	
   if(!isError) {
	   frm.eFileName.value = fileNameArray[0]; // 파일명	   
	   frm.eFileKey.value =$('<%=id%>').uploader.uploadKey;
	   frm.eFileSize.value = getfileSize(fileSize);
	   frm.eFileSizeByte.value = fileSize;
	   
	   //alert(frm.eFileName.value);
	   //alert(frm.eNewFile.value);
	 
	   $('<%=id%>').saveData();
	 	   
   }    
  },
  
  onNotExistsUploadFile: function() { // 업로드 할 파일이 없을때
   
   alert('업로드할 파일을 선택해 주시기 바랍니다');
  }
  
 }).render();
}



/***********************************************/
/* 파일 업로드 
/***********************************************/

$('<%=id%>').uploadFile = function() {
	
	 // 추가 일때 필요한 key
 	$('<%=id%>').uploader.uploadKey = '<%=request.getSession().getId()%>'+(new Date().getTime());
 
 	// 업로드
 	$('<%=id%>').uploader.upload();
 
 
} 


/***********************************************/
/* 탬플릿 리스트 
/***********************************************/

$('<%=id%>').list = function ( ) {	

	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>',  // busy 를 표시할 window
		updateWindowId: '<%=id%>',  // 완료후 버튼,힌트 가 랜더링될 window

		url: 'massmail/write/massmail.do?method=listAttachedFile&id=<%=id%>', 
		update: $('<%=id%>_grid_content'), // 완료후 content가 랜더링될 element
		
		onSuccess: function(html,els,resHTML,scripts) {
			
			//MochaUI.arrangeCascade();
		}
	});
	nemoRequest.post();
}

/***********************************************/
/* 체크된 첨부파일 본문에 삽입 
/***********************************************/
$('<%=id%>').insertAttachedFile = function(){
	var frm = $('<%=id%>_list_form');
	var htmlhead ="<p><table border='1' cellspacing='0' cellpadding='1' width='400px'>"
					+"<tr><th>첨부파일명</th><th align=right>사이즈</th></tr>";
	var htmlrows = "";
	var htmlfoot = "</table><p>"

	
	if(frm.eFilePath==null){
		alert('업로드된 첨부파일이 없습니다. 먼저 첨부파일을 업로드하세요');
		return;
	}
		
	if(frm.eFilePath.length==null){
		htmlrows = "<tr><td align=left><a href='"+frm.eFilePath.value+"'>"+frm.eFileName.value+"</a></td><td align=right>"+frm.eFileSize.value+"</td></tr>";
	}else{
		for(var i=0;i<frm.eFilePath.length;i++){
			if(frm.eFileKey[i].checked==true){
				htmlrows += "<tr><td align=left><a href='"+frm.eFilePath[i].value+"'>"+frm.eFileName[i].value+"</td><td align=right>"+frm.eFileSize[i].value+"</td></tr>";
			}
		}
	}
	if(htmlrows==''){
		alert('첨부파일을 선택하세요');
		return;
	}
	var html = htmlhead + htmlrows + htmlfoot;
	$('<%=preID%>_ifrmFckEditor').contentWindow.insertFCKHtml(html);	
	closeWindow($('<%=id%>'));
				
}

/* 리스트 표시 */
window.addEvent("domready",function () {	
	$('<%=id%>').init();
	//$('<%=id%>').createPopup();

});


/***********************************************/
/* 체크된 첨부파일 첨부
/***********************************************/
$('<%=id%>').attacheAttachedFile = function(){
	var frm = $('<%=id%>_list_form');
	
	var htmlrows = "";
	var totalSize = 0;
	var totalCount = 0;
	
	if(frm.eFilePath==null){
		alert('업로드된 첨부파일이 없습니다. 먼저 첨부파일을 업로드하세요');
		return;
	}
		
	if(frm.eFilePath.length==null){
		htmlrows = "<a href='"+frm.eFilePath.value+"'>"+frm.eFileName.value+"</a>("+frm.eFileSize.value+")";
		htmlrows += "<input type='hidden' id='eAttachedFiles' name='eAttachedFiles' value='" + frm.eFileName.value + "'>";
		htmlrows += "<input type='hidden' id='eAttachedFilesPath' name='eAttachedFilesPath' value='" + frm.eFilePath.value.substring(frm.eFilePath.value.indexOf("&fileKey=")+9,frm.eFilePath.value.length) + "'>";
		totalSize = Number(frm.eFileSizeByte.value);
		
		//totalSize = frm.eFileSize.value;
	}else{
		for(var i=0;i<frm.eFilePath.length;i++){
			if(frm.eFileKey[i].checked==true){
				htmlrows += "<a href='"+frm.eFilePath[i].value+"'>"+frm.eFileName[i].value+"</a><br>";
				htmlrows += "<input type='hidden' id='eAttachedFiles' name='eAttachedFiles' value='" + frm.eFileName[i].value + "'>";
				htmlrows += "<input type='hidden' id='eAttachedFilesPath' name='eAttachedFilesPath' value='" + frm.eFilePath[i].value.substring(frm.eFilePath[i].value.indexOf("&fileKey=")+9,frm.eFilePath[i].value.length) + "'>";
				totalSize += Number(frm.eFileSizeByte[i].value);
				totalCount += 1;
				
			}
		}
	}
	htmlrows += "<a href=\"javascript:$('<%=preID%>').clearAttach()\"><img src='/images/btn_delete_sm.gif'></a>";
	
	if(totalSize > 5242880 ){ //5MB
		alert('파일 첨부는 최대 5MB까지 가능합니다.');
		return;
	}

	if(totalCount > 3 ){ 
		alert('파일 첨부는 최대 3개까지 가능합니다.');
		return;
	}
		
	if(htmlrows==''){
		alert('첨부파일을 선택하세요');
		return;
	}
		
	$('<%=preID%>_attacheTitle').style.display='';
	$('<%=preID%>_attacheFiles').innerHTML=htmlrows;
	$('<%=preID%>_attacheFiles').style.display='';
	closeWindow($('<%=id%>'));
				
}


/***********************************************/
/* 등록된 첨부파일 삭제
/***********************************************/
$('<%=id%>').deleteAttachedFile = function(){

	var frm = $('<%=id%>_list_form');

	var checked = isChecked( frm.elements['eFileKey']  );

	if( checked == 0 ) {
		toolTip.showTipAtControl(frm.sCheckAll,'삭제할 자료를 먼저 선택하세요');
		return;
	}

	
	if(!confirm("선택하신 "+checked+"개의 자료를  삭제 하시겠습니까?")) return;

	
	nemoRequest.init( 
	{
		busyWindowId: '<%=id%>_modal'  // busy 를 표시할 window
		//updateWindowId: '<%=id%>', // 완료후 버튼,힌트 가 랜더링될 window

		, url: 'massmail/write/massmail.do?method=deleteAttachedFile&id=<%=id%>'
		, update: $('<%=id%>_grid_content') // 완료후 content가 랜더링될 element
		, onSuccess: function(html,els,resHTML) {
			$('<%=id%>_list_form').sCheckAll.checked = false;			
		}
	});
	nemoRequest.post(frm);
}


</script>





<%
}
//****************************************************************************************************/
// 탬플릿리스트
//****************************************************************************************************/
if(method.equals("listAttachedFile")) {
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
	
	
	<c:forEach items="${attachedFileList}" var="attachedFile">	
	<TR class="tbl_tr" attachedList_id="<c:out value="${attachedFile.fileKey}"/>">	
		<TD class="tbl_td" align="center"><input type="checkbox" class="notBorder" id="eFileKey" name="eFileKey" value="<c:out value="${attachedFile.fileKey}" />" /></TD>	
		<TD class="tbl_td" align="left"><a href="massmail/write/massmail.do?method=fileDownload&fileKey=<c:out value="${attachedFile.fileKey}"/>" target="_blank"/><c:out value="${attachedFile.fileName}"/></a></TD>
		<TD class="tbl_td" align="right"><c:out value="${attachedFile.fileSize}"/></TD>
		<TD class="tbl_td">
			<c:out value="${attachedFile.registDate}"/>
			<input type="hidden" name="eFileName" value="<c:out value="${attachedFile.fileName}"/>"/>
			<input type="hidden" name="eFilePath" value="<c:out value="${attachedFile.filePath}"/>" />
			<input type="hidden" name="eFileSize" value="<c:out value="${attachedFile.fileSize}"/>" />		
			<input type="hidden" name="eFileSizeByte" value="<c:out value="${attachedFile.fileSizeByte}"/>" />
		</TD>
		<TD class="tbl_td"><c:out value="${attachedFile.sendedDate}"/></TD>
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